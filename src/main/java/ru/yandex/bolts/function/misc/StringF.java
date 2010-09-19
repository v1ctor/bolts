package ru.yandex.bolts.function.misc;

import java.util.regex.Pattern;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.Tuple3;
import ru.yandex.bolts.collection.Tuple4;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2B;

/**
 * Misc {@link String} functions.
 *
 * @author Stepan Koltsov
 * @deprecated
 * @see CollectionsF#String
 */
public class StringF {

    public static Function1B<String> notEmptyF() {
        return Cf.String.notEmptyF();
    }

    public static Function1B<String> emptyF() {
        return Cf.String.emptyF();
    }

    public static Function<String, Integer> lengthF() {
        return Cf.String.lengthF();
    }

    /**
     * Concatenate two strings.
     */
    public static Function2<String, String, String> plusF() {
        return Cf.String.plusF();
    }

    /**
     * Join string using specified separator.
     */
    public static Function2<String, String, String> plusF(final String sep) {
        return Cf.String.joinF(sep);
    }

    public static Function<String, String> addSuffixF(String suffix) {
        return Cf.String.addSuffixF(suffix);
    }

    public static Function<String, String> addPrefixF(String prefix) {
        return Cf.String.addPrefixF(prefix);
    }

    public static Function2B<String, String> startsWithF() {
        return Cf.String.startsWithF();
    }

    public static Function1B<String> startsWithF(String prefix) {
        return Cf.String.startsWithF(prefix);
    }

    public static Function2B<String, String> endsWithF() {
        return Cf.String.endsWithF();
    }

    public static Function1B<String> endsWithF(String suffix) {
        return Cf.String.endsWithF(suffix);
    }

    public static Function<String, String> toLowerCaseF() {
        return Cf.String.toLowerCaseF();
    }

    public static Function<String, String> toUpperCaseF() {
        return Cf.String.toUpperCaseF();
    }

    public static Function<String, String> trimF() {
        return Cf.String.trimF();
    }

    /**
     * Split string using the specified regex.
     *
     * @see Pattern#split(CharSequence)
     */
    public static Function<String, ListF<String>> splitF(String regex) {
        return Cf.String.splitF(regex);
    }

    /**
     * Split string into two strings using the specified regex.
     *
     * @see Pattern#split(CharSequence, int)
     */
    public static Function<String, Tuple2<String, String>> split2F(String regex) {
        final Pattern pattern = Pattern.compile(regex);
        return new Function<String, Tuple2<String, String>>() {
            public Tuple2<String, String> apply(String a) {
                String[] array = pattern.split(a, 2);
                return Tuple2.tuple(array[0], array[1]);
            }
        };
    }

    /**
     * Split string into three strings using the specified regex.
     *
     * @see Pattern#split(CharSequence, int)
     */
    public static Function<String, Tuple3<String, String, String>> split3F(String regex) {
        final Pattern pattern = Pattern.compile(regex);
        return new Function<String, Tuple3<String,String,String>>() {
            public Tuple3<String, String, String> apply(String a) {
                String[] array = pattern.split(a, 3);
                return Tuple3.tuple(array[0], array[1], array[2]);
            }
        };
    }

    /**
     * Split string into four strings using the specified regex.
     *
     * @see Pattern#split(CharSequence, int)
     */
    public static Function<String, Tuple4<String, String, String, String>> split4F(String regex) {
        final Pattern pattern = Pattern.compile(regex);
        return new Function<String, Tuple4<String, String, String, String>>() {
            public Tuple4<String, String, String, String> apply(String a) {
                String[] array = pattern.split(a, 4);
                return Tuple4.tuple(array[0], array[1], array[2], array[3]);
            }
        };
    }

} //~
