package ru.yandex.bolts.type;

import java.util.Locale;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;

/**
 * @author Stepan Koltsov
 */
public class CharacterType extends AnyObjectType<Character> {

    public Function1B<Character> isLetterF() {
        return new Function1B<Character>() {
            public boolean apply(Character c) {
                return Character.isLetter(c);
            }
        };
    }

    public Function1B<Character> isDigitF() {
        return new Function1B<Character>() {
            public boolean apply(Character c) {
                return Character.isDigit(c);
            }
        };
    }

    public Function1B<Character> isWhitespaceF() {
        return new Function1B<Character>() {
            public boolean apply(Character c) {
                return Character.isWhitespace(c);
            }
        };
    }

    public Function1B<Character> isLetterOrDigitF() {
        return new Function1B<Character>() {
            public boolean apply(Character c) {
                return Character.isLetterOrDigit(c);
            }
        };
    }

    public Function1B<Character> isLowerCaseF() {
        return new Function1B<Character>() {
            public boolean apply(Character c) {
                return Character.isLowerCase(c);
            }
        };
    }

    public Function1B<Character> isUpperCaseF() {
        return new Function1B<Character>() {
            public boolean apply(Character c) {
                return Character.isUpperCase(c);
            }
        };
    }

    public Function<Character, Character> toUpperCaseF() {
        return new Function<Character, Character>() {
            public Character apply(Character c) {
                return Character.toUpperCase(c);
            }
        };
    }

    public Function<Character, Character> toLowerCaseF() {
        return new Function<Character, Character>() {
            public Character apply(Character c) {
                return Character.toLowerCase(c);
            }
        };
    }

    public Function<Character, Character> toUpperCaseF(final Locale locale) {
        return new Function<Character, Character>() {
            public Character apply(Character c) {
                return String.valueOf(c).toUpperCase(locale).charAt(0);
            }
        };
    }

    public Function<Character, Character> toLowerCaseF(final Locale locale) {
        return new Function<Character, Character>() {
            public Character apply(Character c) {
                return String.valueOf(c).toLowerCase(locale).charAt(0);
            }
        };
    }

    public Function<Character, Integer> typeF() {
        return new Function<Character, Integer>() {
            public Integer apply(Character c) {
                return Character.getType(c);
            }
        };
    }

    public Function1B<Character> isTypeF(int type) {
        return typeF().andThenEquals(type);
    }

} //~
