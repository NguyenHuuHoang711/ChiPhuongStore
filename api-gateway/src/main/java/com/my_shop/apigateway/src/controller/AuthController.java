package com.my_shop.apigateway.src.controller;

import auth.AuthServiceGrpc;
import com.my_shop.apigateway.src.dto.auth.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto) {
        
    }
}
