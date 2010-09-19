package ru.yandex.bolts.function.misc;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.function.Function;

/**
 * Functions on {@link Object}
 *
 * @author Stepan Koltsov
 * @deprecated
 * @see CollectionsF#Object
 */
public class ObjectF {

    public static Function<Object, Integer> hashCodeF() {
        return Cf.Object.hashCodeF();
    }

    public static Function<Object, String> toStringF() {
        return Cf.Object.toStringF();
    }

} //~
