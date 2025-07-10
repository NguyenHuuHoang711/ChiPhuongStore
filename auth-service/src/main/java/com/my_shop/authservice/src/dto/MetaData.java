package com.my_shop.authservice.src.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Chỉ cho refresh/login/register

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaData {
    private String ip;
    private String userAgent;
    private String location;
    private String deviceId;
}

