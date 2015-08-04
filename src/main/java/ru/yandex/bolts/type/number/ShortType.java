package ru.yandex.bolts.type.number;

import ru.yandex.bolts.collection.Cf;


public class ShortType extends IntegralType<Short> {

    @Override
    public Short parse(String string, int radix) throws NumberFormatException {
        return Short.parseShort(string, radix);
    }

    @Override
    public int cmp(Short a, Short b) {
        return a.compareTo(b);
    }

    @Override
    public Short divide(Short a, Short b) {
        return (short) (a / b);
    }

    @Override
    public Short minus(Short a, Short b) {
        return (short) (a - b);
    }

    @Override
    public Short plus(Short a, Short b) {
        return (short) (a + b);
    }

    @Override
    public Short multiply(Short a, Short b) {
        return (short) (a * b);
    }

    @Override
    public Short negate(Short a) {
        return (short) -a;
    }

    @Override
    public Short parse(String string) throws NumberFormatException {
        return Short.parseShort(string);
    }

    @Override
    public Short zero() {
        return (short) 0;
    }

}
