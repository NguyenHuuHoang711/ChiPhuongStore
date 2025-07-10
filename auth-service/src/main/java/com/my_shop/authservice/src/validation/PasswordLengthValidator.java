package com.my_shop.authservice.src.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordLengthValidator implements ConstraintValidator<ValidPasswordLength, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        if (value.length() < 6) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("password is too short").addConstraintViolation();
            return false;
        };
        if (value.length() > 20) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("password is too long").addConstraintViolation();
            return false;
        }
        return true;
    }
}