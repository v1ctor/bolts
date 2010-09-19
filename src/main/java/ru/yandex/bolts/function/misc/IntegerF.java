package ru.yandex.bolts.function.misc;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * Functions on {@link Integer}
 *
 * @author Stepan Koltsov
 * @deprecated
 * @see CollectionsF#Integer
 */
public class IntegerF {

    public static Function2<Integer, Integer, Integer> plusF() {
        return Cf.Integer.plusF();
    }

    public static Function2<Integer, Integer, Integer> minusF() {
        return Cf.Integer.minusF();
    }

    public static Comparator<Integer> naturalComparator() {
        return Cf.Integer.comparator();
    }

    public static Function2<Integer, Integer, Integer> maxF() {
        return Cf.Integer.maxF();
    }

    public static Function2<Integer, Integer, Integer> minF() {
        return Cf.Integer.minF();
    }

    public static Function<String, Integer> parseF() {
        return Cf.Integer.parseF();
    }

}
