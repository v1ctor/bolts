package ru.yandex.bolts.function.misc;

import java.util.Locale;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;

/**
 * @deprecated
 * @see CollectionsF#Character
 */
public class CharacterF {
    public static Function1B<Character> isLetterF() {
        return Cf.Character.isLetterF();
    }

    public static Function1B<Character> isDigitF() {
        return Cf.Character.isDigitF();
    }

    public static Function1B<Character> isWhitespaceF() {
        return Cf.Character.isWhitespaceF();
    }

    public static Function1B<Character> isLetterOrDigitF() {
        return Cf.Character.isLetterOrDigitF();
    }

    public static Function1B<Character> isLowerCaseF() {
        return Cf.Character.isLowerCaseF();
    }

    public static Function1B<Character> isUpperCaseF() {
        return Cf.Character.isUpperCaseF();
    }

    public static Function<Character, Character> toUpperCaseF() {
        return Cf.Character.toUpperCaseF();
    }

    public static Function<Character, Character> toLowerCaseF() {
        return Cf.Character.toLowerCaseF();
    }

    public static Function<Character, Character> toUpperCaseF(final Locale locale) {
        return Cf.Character.toLowerCaseF(locale);
    }

    public static Function<Character, Character> toLowerCaseF(final Locale locale) {
        return Cf.Character.toLowerCaseF(locale);
    }

    public static Function<Character, Integer> typeF() {
        return Cf.Character.typeF();
    }

    public static Function1B<Character> isTypeF(int type) {
        return Cf.Character.isTypeF(type);
    }

} //~
