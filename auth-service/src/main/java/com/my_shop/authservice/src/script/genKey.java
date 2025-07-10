package com.my_shop.authservice.src.script;

import java.util.Base64;

public class genKey {
    public static void main(String[] args) {
        String key = "cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc";
        String base64Key = Base64.getEncoder().encodeToString(key.getBytes());
        System.out.println(base64Key);
    }
}
