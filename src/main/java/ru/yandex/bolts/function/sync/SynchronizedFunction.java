package ru.yandex.bolts.function.sync;

import ru.yandex.bolts.function.Function;

/**
 * @author alexbool
 */
@FunctionalInterface
public interface SynchronizedFunction<A, R> extends Function<A, R> {

    @Override
    default R apply(A a) {
        synchronized (this) {
            return applySynchronized(a);
        }
    }

    R applySynchronized(A a);
}
