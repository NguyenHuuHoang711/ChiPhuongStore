package com.my_shop.authservice.src.util;

import com.my_shop.authservice.src.dto.MetaData;
import jakarta.servlet.http.HttpServletRequest;

public class controllerUtil {
    public static MetaData getMetaData(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        String userAgent = request.getHeader("User-Agent");
        String location = request.getHeader("X-Location");
        String deviceId = request.getHeader("X-Device-Id");

        return new MetaData(userAgent, ip, location, deviceId);
    }
}
