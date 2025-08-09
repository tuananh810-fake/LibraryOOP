package com.example.libraryoop.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidate {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static Pattern pattern;
    
    public EmailValidate() {
        pattern = Pattern.compile(EMAIL_REGEX);
    }

    public boolean validate(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}