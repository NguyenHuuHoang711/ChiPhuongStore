package com.my_shop.authservice.src.dto;

import lombok.Data;

@Data
public class AuthResponse {
    public String accessToken;
    public String refreshToken;
}
