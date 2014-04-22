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
        return this::parse;
    }

    public Function<String, Option<T>> parseSafeF() {
        return this::parseSafe;
    }

    public Function2<T, T, T> minusF() {
        return this::minus;
    }

    public Function<T, T> negateF() {
        return this::negate;
    }

    public Function2<T, T, T> multiplyF() {
        return this::multiply;
    }

    public Function2<T, T, T> divideF() {
        return this::divide;
    }

    public Comparator<T> comparator() {
        return this::cmp;
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
        return this::toLong;
    }

    public Function<T, Integer> toIntegerF() {
        return this::toInteger;
    }

}
