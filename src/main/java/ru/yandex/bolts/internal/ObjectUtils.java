package ru.yandex.bolts.internal;

/**
 * @author Stepan Koltsov
 */
public class ObjectUtils {

    public static boolean equals(Object a, Object b) {
        if (a == null || b == null) {
            return a == b;
        } else {
            return a.equals(b);
        }
    }

    public static int hashCode(Object o) {
        if (o == null)
            return 0;
        else
            return o.hashCode();
    }

} //~
