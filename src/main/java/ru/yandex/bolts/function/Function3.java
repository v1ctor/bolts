package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
public abstract class Function3<A, B, C, R> {
    public abstract R apply(A a, B b, C c);
} //~
