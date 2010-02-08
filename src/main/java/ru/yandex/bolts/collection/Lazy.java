package ru.yandex.bolts.collection;

import ru.yandex.bolts.function.Function0;

/**
 * @author Stepan Koltsov
 */
public abstract class Lazy<T> extends Function0<T> {
    private T value;
    private boolean defined = false;

    public T get() {
        if (!defined) {
            value = create();
            defined = true;
        }
        return value;
    }

    @Override
    public final T apply() {
        return get();
    }

    public abstract T create();
} //~
