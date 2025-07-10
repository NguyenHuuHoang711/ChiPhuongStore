package com.my_shop.apigateway.src.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshRequestDto {
    @NotBlank
    private String refreshToken;
}
