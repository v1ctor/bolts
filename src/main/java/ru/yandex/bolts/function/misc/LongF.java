package ru.yandex.bolts.function.misc;

import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * Functions on {@link Long}
 *
 * @author Iliya Roubin
 */
public class LongF {

    public static Function2<Long, Long, Long> plusF() {
        return new Function2<Long, Long, Long>() {
            public Long apply(Long a, Long b) {
                return a + b;
            }
        };
    }

    public static Function2<Long, Long, Long> minusF() {
        return new Function2<Long, Long, Long>() {
            public Long apply(Long a, Long b) {
                return a - b;
            }
        };
    }

    public static Comparator<Long> naturalComparator() {
        return Comparator.naturalComparator();
    }

    public static Function2<Long, Long, Long> maxF() {
        return naturalComparator().maxF();
    }

    public static Function2<Long, Long, Long> minF() {
        return naturalComparator().minF();
    }

    public static Function<String, Long> parseF() {
        return new Function<String, Long>() {
            public Long apply(String a) {
                return Long.parseLong(a);
            }
        };
    }

}
