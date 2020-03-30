package com.example.pursuit;

public class RandomKeyGenerator {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefgghijklmnopqrstuvwxyz0123456789";
    private static final String LOWER_CASE_ALPHA_NUMERIC_STRING ="abcdefghijklmnopqrstuvwxyz01234566789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static String randomInviteCode(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * LOWER_CASE_ALPHA_NUMERIC_STRING.length());
            builder.append(LOWER_CASE_ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

}