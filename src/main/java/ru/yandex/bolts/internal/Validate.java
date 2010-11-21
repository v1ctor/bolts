package ru.yandex.bolts.internal;

/**
 * @author Stepan Koltsov
 */
public class Validate {

    public static void isTrue(boolean condition, String message) {
        if (!condition)
            throw new IllegalArgumentException(message);
    }

} //~
