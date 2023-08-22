package com.LifeTales.global.util;

import java.security.SecureRandom;

public class KeyGeneratorUtil {
    public StringBuilder genet(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256 bits = 32 bytes
        secureRandom.nextBytes(keyBytes);

        StringBuilder key = new StringBuilder();
        for (byte b : keyBytes) {
            key.append(String.format("%02x", b));
        }
        System.out.println("Generated Key: " + key.toString());

        return key;
    }

}
