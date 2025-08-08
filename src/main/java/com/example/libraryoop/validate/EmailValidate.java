package com.example.libraryoop.validate;

import java.util.regex.Pattern;

public class EmailValidate {
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    // 2. Biên dịch Pattern một lần duy nhất khi lớp được nạp
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // 3. Đặt constructor là private để ngăn người khác tạo đối tượng từ lớp tiện ích này
    private EmailValidate() {
        // Lớp này không nên được khởi tạo.
    }

    // 4. Tạo một phương thức public, static, với tên tham số rõ ràng
    public static boolean isValid(String email) {
        // Nếu email rỗng hoặc null, coi như không hợp lệ
        if (email == null || email.isEmpty()) {
            return false;
        }
        // So khớp email với pattern đã được biên dịch
        return EMAIL_PATTERN.matcher(email).matches();
    }
}