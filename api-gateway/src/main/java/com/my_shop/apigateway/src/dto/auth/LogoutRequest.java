package com.my_shop.apigateway.src.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogoutRequest {
    @NotBlank
    String refreshToken;

    public LogoutRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
