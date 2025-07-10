package com.my_shop.authservice.src.exception;

public class AuthFailedException extends RuntimeException {
    public AuthFailedException(String message) {
        super(message);
    }
}
