package ru.yandex.bolts.type;

import java.util.Locale;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;

/**
 * @author Stepan Koltsov
 */
public class CharacterType extends AnyObjectType<Character> {

    public Function1B<Character> isLetterF() {
        return Character::isLetter;
    }

    public Function1B<Character> isDigitF() {
        return Character::isDigit;
    }

    public Function1B<Character> isWhitespaceF() {
        return Character::isWhitespace;
    }

    public Function1B<Character> isLetterOrDigitF() {
        return Character::isLetterOrDigit;
    }

    public Function1B<Character> isLowerCaseF() {
        return Character::isLowerCase;
    }

    public Function1B<Character> isUpperCaseF() {
        return Character::isUpperCase;
    }

    public Function<Character, Character> toUpperCaseF() {
        return Character::toUpperCase;
    }

    public Function<Character, Character> toLowerCaseF() {
        return Character::toLowerCase;
    }

    public Function<Character, Character> toUpperCaseF(final Locale locale) {
        return c -> String.valueOf(c).toUpperCase(locale).charAt(0);
    }

    public Function<Character, Character> toLowerCaseF(final Locale locale) {
        return c -> String.valueOf(c).toLowerCase(locale).charAt(0);
    }

    public Function<Character, Integer> typeF() {
        return Character::getType;
    }

    public Function1B<Character> isTypeF(int type) {
        return typeF().andThenEquals(type);
    }

} //~
