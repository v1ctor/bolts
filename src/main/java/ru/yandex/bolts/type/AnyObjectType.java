package ru.yandex.bolts.type;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2B;


public abstract class AnyObjectType<T> {

    public Function<T, Integer> hashCodeF() {
        return a -> a != null ? a.hashCode() : 0;
    }

    public Function<T, String> toStringF() {
        return a -> a != null ? a.toString() : "null";
    }

    public boolean equals(T a, T b) {
        if (a == null || b == null) {
            return a == b;
        } else {
            return a.equals(b);
        }
    }

    public Function2B<T, T> equalsF() {
        return this::equals;
    }

    public Function1B<T> equalsF(T that) {
        return equalsF().bind2(that);
    }

} //~
