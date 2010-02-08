package ru.yandex.bolts.function.misc;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * Functions on {@link Integer}
 *
 * @author Stepan Koltsov
 */
public class IntegerF {

    public static Function2<Integer, Integer, Integer> plusF() {
        return new Function2<Integer, Integer, Integer>() {
            public Integer apply(Integer a, Integer b) {
                return a + b;
            }
        };
    }

    public static Function2<Integer, Integer, Integer> minusF() {
        return new Function2<Integer, Integer, Integer>() {
            public Integer apply(Integer a, Integer b) {
                return a - b;
            }
        };
    }

    public static Comparator<Integer> naturalComparator() {
        return Comparator.naturalComparator();
    }

    public static Function2<Integer, Integer, Integer> maxF() {
        return naturalComparator().maxF();
    }

    public static Function2<Integer, Integer, Integer> minF() {
        return naturalComparator().minF();
    }

    public static Function<String, Integer> parseF() {
        return new Function<String, Integer>() {
            public Integer apply(String a) {
                return Integer.parseInt(a);
            }
        };
    }

}
