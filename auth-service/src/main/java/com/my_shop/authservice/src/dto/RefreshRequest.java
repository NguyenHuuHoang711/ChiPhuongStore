package com.my_shop.authservice.src.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshRequest {
    @NotBlank
    private String refreshToken;
}
