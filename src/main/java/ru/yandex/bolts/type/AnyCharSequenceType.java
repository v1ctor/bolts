package ru.yandex.bolts.type;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;

public abstract class AnyCharSequenceType<T extends CharSequence> extends MonoidPlus<T> {

    public boolean empty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public boolean notEmpty(CharSequence s) {
        return !empty(s);
    }

    public int length(CharSequence s) {
        return s != null ? s.length() : 0;
    }

    public Function1B<T> emptyF() {
        return this::empty;
    }

    public Function1B<T> notEmptyF() {
        return this::notEmpty;
    }

    public Function<T, Integer> lengthF() {
        return T::length;
    }

} //~
