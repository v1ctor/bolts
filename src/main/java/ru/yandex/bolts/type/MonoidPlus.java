package ru.yandex.bolts.type;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function3;


public abstract class MonoidPlus<T> extends AnyObjectType<T> {
    public abstract T zero();

    public abstract T plus(T a, T b);

    public T plus(T a, T b, T c) {
        return plus(plus(a, b), c);
    }

    public Function2<T, T, T> plusF() {
        return this::plus;
    }

    public Function<T, T> plusF(T param) {
        return plusF().bind1(param);
    }

    public Function3<T, T, T, T> plus3F() {
        return this::plus;
    }

    public boolean isZero(T a) {
        return zero().equals(a);
    }

    public Function1B<T> zeroF() {
        return this::isZero;
    }

} //~
