package ru.yandex.bolts.type.number;

import ru.yandex.bolts.collection.CollectionsF;

/**
 * @author Stepan Koltsov
 * @see CollectionsF#Long
 */
public class LongType extends IntegralType<Long> {

    @Override
    public Long zero() {
        return 0L;
    }

    @Override
    public Long parse(String string) throws NumberFormatException {
        return Long.parseLong(string);
    }

    @Override
    public Long parse(String string, int radix) throws NumberFormatException {
        return Long.parseLong(string, radix);
    }

    @Override
    public Long plus(Long a, Long b) {
        return a + b;
    }

    @Override
    public Long minus(Long a, Long b) {
        return a - b;
    }

    @Override
    public Long divide(Long a, Long b) {
        return a / b;
    }

    @Override
    public Long multiply(Long a, Long b) {
        return a * b;
    }

    @Override
    public Long negate(Long a) {
        return -a;
    }

    @Override
    public int cmp(Long a, Long b) {
        return a.compareTo(b);
    }

}
