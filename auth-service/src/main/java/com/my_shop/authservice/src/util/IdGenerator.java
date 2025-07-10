package com.my_shop.authservice.src.util;

import java.util.UUID;

public class IdGenerator {
    public static String GenerateId(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
