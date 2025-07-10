package com.my_shop.apigateway.src.dto.auth;

import com.my_shop.apigateway.src.validation.ValidPasswordLength;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterRequestDto {

    @NotBlank @Email
    public String email;

    @NotBlank @Length(min = 6, max = 20)
    @ValidPasswordLength
    public String password;

    public RegisterRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
