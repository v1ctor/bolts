package ru.yandex.bolts.function.misc;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;

/**
 * {@link CharSequence} functions.
 *
 * @author Stepan Koltsov
 *
 * @see StringF
 */
public class CharSequenceF {

    /**
     * <code>true</code> iff char sequence is not null and has positive length.
     *
     * @see #emptyF()
     */
    public static Function1B<CharSequence> notEmptyF() {
        return new Function1B<CharSequence>() {
            public boolean apply(CharSequence a) {
                return a != null && a.length() > 0;
            }
        };
    }

    /**
     * <code>true</code> iff char sequence is null or has zero length.
     *
     * @see #notEmptyF()
     */
    public static Function1B<CharSequence> emptyF() {
        return notEmptyF().notF();
    }

    /**
     * Length of char sequence.
     */
    public static Function<CharSequence, Integer> lengthF() {
        return new Function<CharSequence, Integer>() {
            public Integer apply(CharSequence a) {
                return a.length();
            }
        };
    }

} //~
