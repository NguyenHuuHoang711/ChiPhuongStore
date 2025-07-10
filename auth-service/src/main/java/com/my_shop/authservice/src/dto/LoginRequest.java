package com.my_shop.authservice.src.dto;

import com.my_shop.authservice.src.validation.ValidPasswordLength;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    public String email;

    @NotBlank(message = "Password is required")
    @ValidPasswordLength
    public String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}


