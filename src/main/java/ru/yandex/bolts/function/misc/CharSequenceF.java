package ru.yandex.bolts.function.misc;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;

/**
 * {@link CharSequence} functions.
 *
 * @author Stepan Koltsov
 *
 * @deprecated
 * @see CollectionsF#CharSequence
 */
public class CharSequenceF {

    /**
     * <code>true</code> iff char sequence is not null and has positive length.
     *
     * @see #emptyF()
     */
    public static Function1B<CharSequence> notEmptyF() {
        return Cf.CharSequence.notEmptyF();
    }

    /**
     * <code>true</code> iff char sequence is null or has zero length.
     *
     * @see #notEmptyF()
     */
    public static Function1B<CharSequence> emptyF() {
        return Cf.CharSequence.notEmptyF();
    }

    /**
     * Length of char sequence.
     */
    public static Function<CharSequence, Integer> lengthF() {
        return Cf.CharSequence.lengthF();
    }

} //~
