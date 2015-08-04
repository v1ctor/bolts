package ru.yandex.bolts.type.number;

import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function;


public abstract class IntegralType<T extends Number> extends NumberType<T> {
    public abstract T parse(String string, int radix) throws NumberFormatException;

    public T parseDecimal(String string) throws NumberFormatException {
        return parse(string, 10);
    }

    public Option<T> parseSafe(String string, int radix) {
        if (string == null || string.length() == 0)
            return Option.none();
        try {
            return Option.some(parse(string, radix));
        } catch (NumberFormatException e) {
            return Option.none();
        }
    }

    public Function<String, T> parseF(final int radix) {
        return a -> parse(a, radix);
    }

    public Function<String, T> parseDecimalF() {
        return parseF(10);
    }

    public Function<String, Option<T>> parseSafeF(final int radix) {
        return a -> parseSafe(a, radix);
    }
}
