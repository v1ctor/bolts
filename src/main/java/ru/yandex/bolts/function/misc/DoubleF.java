package ru.yandex.bolts.function.misc;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * Function on {@link Double}
 *
 * @author Stepan Koltsov
 * @deprecated
 * @see CollectionsF#Double
 */
public class DoubleF {

    public static Function2<Double, Double, Double> plusF() {
        return Cf.Double.plusF();
    }

    public static Function2<Double, Double, Double> minusF() {
        return Cf.Double.minusF();
    }

    public static Comparator<Double> naturalComparator() {
        return Cf.Double.comparator();
    }

    public static Function2<Double, Double, Double> maxF() {
        return Cf.Double.maxF();
    }

    public static Function2<Double, Double, Double> minF() {
        return Cf.Double.minF();
    }

    public static Function<String, Double> parseF() {
        return Cf.Double.parseF();
    }

} //~
