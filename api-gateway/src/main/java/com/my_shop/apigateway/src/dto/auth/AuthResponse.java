package com.my_shop.apigateway.src.dto.auth;

import lombok.Data;

@Data
public class AuthResponse {
    public String accessToken;
    public String refreshToken;
}
