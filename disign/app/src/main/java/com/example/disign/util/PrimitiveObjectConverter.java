package com.example.disign.util;

import java.util.Arrays;

public class PrimitiveObjectConverter {

    public static Integer convert(int value) {
        return Integer.valueOf(value);
    }

    public static Double convert(double value) {
        return Double.valueOf(value);
    }

    public static Character convert(char value) {
        return Character.valueOf(value);
    }

    public static Boolean convert(boolean value) {
        return Boolean.valueOf(value);
    }

    public static <T> T[] convert(T[] array) {
        return Arrays.copyOf(array, array.length);
    }
    public static <T extends Object>T convert(T value) {
        return value;
    }
}
