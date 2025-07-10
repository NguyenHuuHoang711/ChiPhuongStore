package com.my_shop.authservice.src.util;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class CookieUtil {

    @Value("${spring.profiles.active:dev}")
    static String activeProfile;

    public static Cookie createHttpOnlyCookie(String name,
                                              String value,
                                              int maxAgeInSeconds,
                                              String path) {
        boolean secure = !"dev".equals(activeProfile);
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setPath(path);
        return cookie;
    }

    public static Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
