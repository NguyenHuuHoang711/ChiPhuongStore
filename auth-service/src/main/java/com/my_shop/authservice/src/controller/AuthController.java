package com.my_shop.authservice.src.controller;

import com.my_shop.authservice.src.dto.*;
import com.my_shop.authservice.src.service.AuthService;
import com.my_shop.authservice.src.util.CookieUtil;
import com.my_shop.authservice.src.util.controllerUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request, HttpServletResponse response, HttpServletRequest httpRequest) {

        log.info("Register endpoint hit...");
        MetaData metaData = controllerUtil.getMetaData(httpRequest);
        AuthResponse authResponse = authService.register(request, metaData);

        // Tạo và set cookie
        Cookie cookie = CookieUtil.createHttpOnlyCookie(
                "accessToken",
                String.valueOf(authResponse.accessToken),
                (int) Duration.ofMinutes(15).getSeconds(),
                "/"
        );
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of(
                "refreshToken", authResponse.refreshToken,
                "accessToken", authResponse.accessToken
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response, HttpServletRequest httpRequest) {

        log.info("Login endpoint hit...");
        MetaData metaData = controllerUtil.getMetaData(httpRequest);
        AuthResponse authResponse = authService.login(request, metaData);

        Cookie cookie = CookieUtil.createHttpOnlyCookie(
                "accessToken",
                String.valueOf(authResponse.accessToken),
                (int) Duration.ofMinutes(15).getSeconds(),
                "/"
        );
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of(
                "refreshToken", authResponse.refreshToken,
                "accessToken", authResponse.accessToken
        ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request, HttpServletResponse response, HttpServletRequest httpRequest) {

        log.info("Refreshing token endpoint hit...");
        MetaData metaData = controllerUtil.getMetaData(httpRequest);
        AuthResponse authResponse = authService.refresh(request, metaData);

        Cookie cookie = CookieUtil.createHttpOnlyCookie(
                "accessToken",
                String.valueOf(authResponse.accessToken),
                (int) Duration.ofMinutes(15).getSeconds(),
                "/"
        );
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of(
                "refreshToken", authResponse.refreshToken,
                "accessToken", authResponse.accessToken
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest request, HttpServletResponse response) {
        log.info("Logout endpoint hit...");

        authService.logout(request);
        Cookie cookie = CookieUtil.createHttpOnlyCookie(
                "accessToken",
                String.valueOf(""),
                (int) Duration.ofMinutes(15).getSeconds(),
                "/"
        );
        response.addCookie(cookie);

        return ResponseEntity.ok("Logout success");
    }
}
