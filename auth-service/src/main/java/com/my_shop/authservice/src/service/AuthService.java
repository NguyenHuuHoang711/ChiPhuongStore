package com.my_shop.authservice.src.service;

import com.my_shop.authservice.src.dto.*;
import com.my_shop.authservice.src.model.AccessToken;
import com.my_shop.authservice.src.model.User;
import com.my_shop.authservice.src.redis.SessionInfo;
import com.my_shop.authservice.src.repository.AccessTokenRepository;
import com.my_shop.authservice.src.repository.UserRepository;
import com.my_shop.authservice.src.util.GenerateHexId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RedisTemplate<String, String> redisTemplate;
    private final SessionService sessionService;

    public AuthService(
            JwtService jwtService,
            AccessTokenRepository accessTokenRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RedisTemplate<String, String> redisTemplate,
            SessionService sessionService
            ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenRepository = accessTokenRepository;
        this.redisTemplate = redisTemplate;
        this.sessionService = sessionService;
    }

    private AuthResponse generateTokens(String userId,
                                        MetaData metaData) {
        String sessionId = UUID.randomUUID().toString();
        TokenPair token = jwtService.generateToken(userId, sessionId);

        AccessToken tokenEntity = new AccessToken();
        tokenEntity.setToken(token.getAccessToken());
        tokenEntity.setUserId(userId);
        tokenEntity.setExpiredAt(jwtService.getExpirationDate(token.getAccessToken()).toInstant()
        .atZone(ZoneId.systemDefault()).toLocalDateTime());
        tokenEntity.setRevoked(false);
        accessTokenRepository.save(tokenEntity);

        AuthResponse res = new AuthResponse();
        res.accessToken = token.getAccessToken();
        res.refreshToken = token.getRefreshToken();

        long now = System.currentTimeMillis();

        SessionInfo session = new SessionInfo(
                sessionId,
                userId,
                token.getRefreshToken(),
                metaData.getIp(),
                metaData.getUserAgent(),
                now,
                jwtService.getRefreshExpiration(),
                false,
                false,
                metaData.getLocation(),
                metaData.getDeviceId()
        );

        sessionService.storeSession(session, jwtService.getRefreshExpiration());

        return res;
    }

    public AuthResponse login(LoginRequest request,
                              MetaData metaData
    ) {
        User user = userRepository.findByEmail(request.email).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.password, user.getPassword())) throw new RuntimeException("Invalid password");

        return  generateTokens(user.getId(), metaData);
    }

    public AuthResponse register(RegisterRequest request,
                                 MetaData metaData
    ) {
        User user = new User();
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        userRepository.save(user);
        return  generateTokens(user.getId(), metaData);
    }

    public AuthResponse refresh(RefreshRequest request,
                                MetaData metaData
    ) {
        SessionInfo oldSession = sessionService.getSessionByRefreshToken(request.getRefreshToken());

        if (oldSession == null) throw new RuntimeException("Session not found");
        if (oldSession.getExpiresAt() < System.currentTimeMillis()) throw new RuntimeException("Refresh expired");
        if (oldSession.isRevoked()) throw new RuntimeException("Refresh revoked");

        sessionService.removeSessionByRefreshToken(request.getRefreshToken());
        return  generateTokens(oldSession.getUserId(), metaData);
    }

    public void logout(LogoutRequest request) {
        redisTemplate.delete("refresh:" + request.getRefreshToken());
    }
}
