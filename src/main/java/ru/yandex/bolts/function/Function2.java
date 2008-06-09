package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
public interface Function2<A, B, R> {
    R apply(A a, B b);
} //~
