package ru.yandex.bolts.type.number;

import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.forhuman.Comparator;
import ru.yandex.bolts.type.MonoidPlus;

/**
 * @author Stepan Koltsov
 */
public abstract class NumberType<T extends Number> extends MonoidPlus<T> {
    public abstract T parse(String string) throws NumberFormatException;
    public abstract T minus(T a, T b);
    public abstract T negate(T a);
    public abstract T multiply(T a, T b);
    public abstract T divide(T a, T b);
    public abstract int cmp(T a, T b);

    public Option<T> parseSafe(String string) {
        if (string == null || string.length() == 0)
            return Option.none();

        try {
            return Option.some(parse(string));
        } catch (NumberFormatException e) {
            return Option.none();
        }
    }

    public Function<String, T> parseF() {
        return new Function<String, T>() {
            public T apply(String string) {
                return parse(string);
            }
        };
    }

    public Function<String, Option<T>> parseSafeF() {
        return new Function<String, Option<T>>() {
            public Option<T> apply(String string) {
                return parseSafe(string);
            }
        };
    }

    public Function2<T, T, T> minusF() {
        return new Function2<T, T, T>() {
            public T apply(T a, T b) {
                return minus(a, b);
            }
        };
    }

    public Function<T, T> negateF() {
        return new Function<T, T>() {
            public T apply(T a) {
                return negate(a);
            }
        };
    }

    public Function2<T, T, T> multiplyF() {
        return new Function2<T, T, T>() {
            public T apply(T a, T b) {
                return multiply(a, b);
            }
        };
    }

    public Function2<T, T, T> divideF() {
        return new Function2<T, T, T>() {
            public T apply(T a, T b) {
                return divide(a, b);
            }
        };
    }

    public Comparator<T> comparator() {
        return new Comparator<T>() {
            public int compare(T a, T b) {
                return cmp(a, b);
            }
        };
    }

    // shortcuts

    public Function2<T, T, T> maxF() {
        return comparator().maxF();
    }

    public Function2<T, T, T> minF() {
        return comparator().minF();
    }


    public Function1B<T> gtF(T b) {
        return comparator().gtF(b);
    }

    public Function1B<T> geF(T b) {
        return comparator().geF(b);
    }

    public Function1B<T> eqF(T b) {
        return comparator().eqF(b);
    }

    public Function1B<T> neF(T b) {
        return comparator().neF(b);
    }

    public Function1B<T> ltF(T b) {
        return comparator().ltF(b);
    }

    public Function1B<T> leF(T b) {
        return comparator().leF(b);
    }


    public long toLong(T t) {
        return t.longValue();
    }

    public int toInteger(T t) {
        return t.intValue();
    }


    public Function<T, Long> toLongF() {
        return new Function<T, Long>() {
            public Long apply(T a) {
                return toLong(a);
            }
        };
    }

    public Function<T, Integer> toIntegerF() {
        return new Function<T, Integer>() {
            public Integer apply(T a) {
                return toInteger(a);
            }
        };
    }

}
