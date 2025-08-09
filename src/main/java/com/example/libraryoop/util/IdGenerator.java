package com.example.libraryoop.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IdGenerator {
    private static final DateTimeFormatter ID_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private IdGenerator() {
        // Private constructor to prevent instantiation
    }

    public static String generateId(String prefix) {
         if (prefix == null) {
            prefix = ""; // Đảm bảo an toàn, không gây NullPointerException
        }
        return prefix.toUpperCase() + LocalDateTime.now().format(ID_FORMATTER);
    }
} 

