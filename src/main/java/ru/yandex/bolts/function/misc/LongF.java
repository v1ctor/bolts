package ru.yandex.bolts.function.misc;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * Functions on {@link Long}
 *
 * @author Iliya Roubin
 * @deprecated
 * @see CollectionsF#Long
 */
public class LongF {

    public static Function2<Long, Long, Long> plusF() {
        return Cf.Long.plusF();
    }

    public static Function2<Long, Long, Long> minusF() {
        return Cf.Long.minusF();
    }

    public static Comparator<Long> naturalComparator() {
        return Cf.Long.comparator();
    }

    public static Function2<Long, Long, Long> maxF() {
        return Cf.Long.maxF();
    }

    public static Function2<Long, Long, Long> minF() {
        return Cf.Long.minF();
    }

    public static Function<String, Long> parseF() {
        return Cf.Long.parseF();
    }

}
