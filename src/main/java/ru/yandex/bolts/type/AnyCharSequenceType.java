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
        return new Function1B<T>() {
            public boolean apply(T a) {
                return empty(a);
            }

            @Override
            public String toString() {
                return "empty";
            }
        };
    }

    public Function1B<T> notEmptyF() {
        return new Function1B<T>() {
            public boolean apply(T a) {
                return notEmpty(a);
            }

            @Override
            public String toString() {
                return "notEmpty";
            }
        };
    }

    public Function<T, Integer> lengthF() {
        return new Function<T, Integer>() {
            public Integer apply(T a) {
                return a.length();
            }

            @Override
            public String toString() {
                return "length";
            }
        };
    }

} //~
