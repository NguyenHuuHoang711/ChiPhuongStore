package com.my_shop.authservice.src.util;

import java.util.UUID;

public class AuthServiceUtil {
    public static String generateHexId() {
        return UUID.randomUUID().toString();
    }
}
