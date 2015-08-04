package ru.yandex.bolts.function.sync;

import ru.yandex.bolts.function.Function1V;


@FunctionalInterface
public interface SynchronizedFunction1V<A> extends Function1V<A> {

    @Override
    default void apply(A a) {
        synchronized (this) {
            applySynchronized(a);
        }
    }

    void applySynchronized(A a);
}
