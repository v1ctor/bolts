package ru.yandex.bolts.function;

import ru.yandex.bolts.function.forhuman.F2V;

/**
 * @author Stepan Koltsov
 * 
 * @see F2V
 */
public interface Function2V<A, B> {
    void apply(A a, B b);
}
