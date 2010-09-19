package ru.yandex.bolts.type;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2B;

/**
 * @author Stepan Koltsov
 */
public abstract class AnyObjectType<T> {

    public Function<T, Integer> hashCodeF() {
        return new Function<T, Integer>() {
            public Integer apply(T a) {
                return a != null ? a.hashCode() : 0;
            }

            @Override
            public String toString() {
                return "hashCode";
            }

        };
    }

    public Function<T, String> toStringF() {
        return new Function<T, String>() {
            public String apply(T a) {
                return a != null ? a.toString() : "null";
            }

            @Override
            public String toString() {
                return "toString";
            }
        };
    }

    public boolean equals(T a, T b) {
        if (a == null || b == null) {
            return a == b;
        } else {
            return a.equals(b);
        }
    }

    public Function2B<T, T> equalsF() {
        return new Function2B<T, T>() {
            public boolean apply(T a, T b) {
                return AnyObjectType.this.equals(a, b);
            }
        };
    }

    public Function1B<T> equalsF(T that) {
        return equalsF().bind2(that);
    }

} //~
