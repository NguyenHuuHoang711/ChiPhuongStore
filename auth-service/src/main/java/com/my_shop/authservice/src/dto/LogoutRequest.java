package com.my_shop.authservice.src.dto;

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
