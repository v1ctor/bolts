package ru.yandex.bolts.function.sync;

import ru.yandex.bolts.function.Function2V;


@FunctionalInterface
public interface SynchronizedFunction2V<A, B> extends Function2V<A, B> {

    @Override
    default void apply(A a, B b) {
        synchronized (this) {
            applySynchronized(a, b);
        }
    }

    void applySynchronized(A a, B b);
}
