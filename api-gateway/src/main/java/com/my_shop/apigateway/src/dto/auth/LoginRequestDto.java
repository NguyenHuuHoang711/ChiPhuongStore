package com.my_shop.apigateway.src.dto.auth;

import com.my_shop.apigateway.src.validation.ValidPasswordLength;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    public String email;

    @NotBlank(message = "Password is required")
    @ValidPasswordLength
    public String password;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}


