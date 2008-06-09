package ru.yandex.bolts.collection;

import ru.yandex.bolts.function.forhuman.Factory;

/**
 * @author Stepan Koltsov
 */
public abstract class Lazy<T> extends Factory<T> {
    private T value;
    private boolean defined = false;

    public T get() {
        if (!defined) {
            value = create();
            defined = true;
        }
        return value;
    }

    public abstract T create();
} //~
