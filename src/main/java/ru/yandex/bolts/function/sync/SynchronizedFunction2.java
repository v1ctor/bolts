package ru.yandex.bolts.function.sync;

import ru.yandex.bolts.function.Function2;

/**
 * @author alexbool
 */
@FunctionalInterface
public interface SynchronizedFunction2<A, B, R> extends Function2<A, B, R> {

    @Override
    default R apply(A a, B b) {
        synchronized (this) {
            return applySynchronized(a, b);
        }
    }

    R applySynchronized(A a, B b);
}
