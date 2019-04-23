package com.example.jsonrest;

public class Validator {
    public static boolean isValidIP4(String ip) {
        return ip.matches("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b");
    }
}
