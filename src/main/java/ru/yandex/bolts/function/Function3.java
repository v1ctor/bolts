package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
public interface Function3<A, B, C, R> {
    R apply(A a, B b, C c);
} //~
