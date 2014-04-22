package ru.yandex.bolts.type;

import ru.yandex.bolts.function.Function;

/**
 * @author Stepan Koltsov
 */
public class BooleanType {

    public boolean parse(String string) {
        return Boolean.parseBoolean(string);
    }

    public Function<String, Boolean> parseF() {
        return this::parse;
    }

} //~
