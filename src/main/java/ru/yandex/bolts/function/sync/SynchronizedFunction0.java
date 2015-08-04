package ru.yandex.bolts.function.sync;

import ru.yandex.bolts.function.Function0;


@FunctionalInterface
public interface SynchronizedFunction0<R> extends Function0<R> {

    @Override
    default R apply() {
        synchronized (this) {
            return applySynchronized();
        }
    }

    R applySynchronized();
}
