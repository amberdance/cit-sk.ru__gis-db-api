package ru.hard2code.gisdbapi.util;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class RandomString {

    private static final int DEFAULT_STRING_LENGTH = 8;


    public RandomString() {
        throw new UnsupportedOperationException();
    }

    public static String generate() {
        return build(DEFAULT_STRING_LENGTH);
    }

    public static String generate(int length) {
        return build(length);
    }

    private static String build(int length) {
        byte[] array = new byte[length];
        new Random().nextBytes(array);

        return new String(array, StandardCharsets.UTF_8);
    }
}
