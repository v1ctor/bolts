package ru.yandex.bolts.function.sync;

import ru.yandex.bolts.function.Function0V;

/**
 * @author alexbool
 */
@FunctionalInterface
public interface SynchronizedFunction0V extends Function0V {

    @Override
    default void apply() {
        synchronized (this) {
            applySynchronized();
        }
    }

    void applySynchronized();
}
