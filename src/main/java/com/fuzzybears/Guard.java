package com.fuzzybears;

public class Guard {
    private Guard() {
    }

    public static void checkArgumentExists(Object object, String message) {
        if (object == null) {
            throw new RuntimeException(message);
        }
    }

    public static void checkArgumentValid(boolean conditionThatMustBeTrue, String message) {
        if (!conditionThatMustBeTrue) {
            throw new RuntimeException(message);
        }
    }
}
