package org.example.util;

import org.example.model.User;

import java.util.regex.Pattern;

public class UserValidator {

    // Regex kiểm tra định dạng email đơn giản
    private static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Kiểm tra email có hợp lệ không
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Kiểm tra user có đủ 15 tuổi không
     */
    public static boolean isValidAge(User user) {
        return user.getDateOfBirth() != null && user.isOver15YearsOld();
    }

    /**
     * Kiểm tra hợp lệ tổng thể (email + tuổi)
     */
    public static boolean isValidUser(User user) {
        return isValidEmail(user.getEmail()) && isValidAge(user);
    }
}
