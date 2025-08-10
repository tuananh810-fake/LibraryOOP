package com.example.libraryoop.util;

import java.util.Random;

public class IdGenerator {
    private static final int ID_LENGTH = 8; // Độ dài phần số ngẫu nhiên (có thể điều chỉnh)
    private static final Random random = new Random();

    private IdGenerator() {
        // Private constructor to prevent instantiation
    }

    public static String generateId(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        StringBuilder sb = new StringBuilder(prefix.toUpperCase());
        for (int i = 0; i < ID_LENGTH; i++) {
            sb.append(random.nextInt(10)); // Thêm số ngẫu nhiên từ 0-9
        }
        return sb.toString();
    }
}