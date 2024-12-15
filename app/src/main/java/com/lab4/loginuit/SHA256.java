package com.lab4.loginuit;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static String gen(String password) throws NoSuchAlgorithmException {
        MessageDigest md=MessageDigest.getInstance("SHA-256");
        byte[] hashBytes=md.digest(password.getBytes());
        StringBuilder hashString = new StringBuilder();
        for (byte b: hashBytes){
            hashString.append(String.format("%02x",b));
        }
        return hashString.toString();
    }
}
