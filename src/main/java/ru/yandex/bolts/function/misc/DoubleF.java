package ru.yandex.bolts.function.misc;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * Function on {@link Double}
 *
 * @author Stepan Koltsov
 */
public class DoubleF {

    public static Function2<Double, Double, Double> plusF() {
        return new Function2<Double, Double, Double>() {
            public Double apply(Double a, Double b) {
                return a + b;
            }
        };
    }

    public static Function2<Double, Double, Double> minusF() {
        return new Function2<Double, Double, Double>() {
            public Double apply(Double a, Double b) {
                return a - b;
            }
        };
    }

    public static Comparator<Double> naturalComparator() {
        return Comparator.naturalComparator();
    }

    public static Function2<Double, Double, Double> maxF() {
        return naturalComparator().maxF();
    }

    public static Function2<Double, Double, Double> minF() {
        return naturalComparator().minF();
    }

    public static Function<String, Double> parseF() {
        return new Function<String, Double>() {
            public Double apply(String a) {
                return Double.parseDouble(a);
            }
        };
    }

} //~
