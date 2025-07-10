package com.my_shop.authservice.src.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.exp}")
    private long exp;

    @Value("${jwt.ref-exp}")
    private long refExp;
}
