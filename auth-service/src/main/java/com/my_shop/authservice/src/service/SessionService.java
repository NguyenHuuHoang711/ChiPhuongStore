package com.my_shop.authservice.src.service;

import com.my_shop.authservice.src.redis.SessionInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SessionService {
    private final RedisTemplate<String, Object> redisTemplate;

    public SessionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Tạo key chính theo session id
    public String getRedisKey(String sessionId) {
        return "session:" + sessionId;
    }

    // Tạo key để sau này tìm kiếm theo refreshToken
    public String refreshTokenKey(String refreshToken) {
        return "refresh_token:" + refreshToken;
    }

    // Nạp key cho session
    public void storeSession(SessionInfo sessionInfo, long ttlMillis) {
        String sessionKey = getRedisKey(sessionInfo.getSessionId());
        String refreshTokenKey = refreshTokenKey(sessionInfo.getRefreshToken());
        redisTemplate.opsForValue().set(sessionKey, sessionInfo, ttlMillis, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(refreshTokenKey, sessionInfo, ttlMillis, TimeUnit.MILLISECONDS);
    }

    public void removeSessionByRefreshToken(String refreshToken) {
        String sessionId = (String) redisTemplate.opsForValue().get(refreshTokenKey(refreshToken));
        if(sessionId != null) {
            redisTemplate.delete(getRedisKey(sessionId));
            redisTemplate.delete(refreshTokenKey(refreshToken));
        }
    }

    public SessionInfo getSession(String sessionId) {
        String key = getRedisKey(sessionId);
        Object value = redisTemplate.opsForValue().get(key);
        if(value == null) throw new RuntimeException("Session not found");

        return (SessionInfo) value;
    }

    public SessionInfo getSessionByRefreshToken(String refreshToken) {
        String sessionId = (String) redisTemplate.opsForValue().get(refreshTokenKey(refreshToken));
        if(sessionId != null) throw new RuntimeException("Invalid refresh token");

        return getSession(refreshToken);
    }
}
