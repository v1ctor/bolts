package ru.yandex.bolts.type.number;

import ru.yandex.bolts.collection.CollectionsF;


/**
 * @author Stepan Koltsov
 * @see CollectionsF#Integer
 */
public class IntegerType extends IntegralType<Integer> {

    @Override
    public Integer zero() {
        return 0;
    }

    @Override
    public Integer parse(String string) throws NumberFormatException {
        return Integer.parseInt(string);
    }

    @Override
    public Integer parse(String string, int radix) throws NumberFormatException {
        return Integer.parseInt(string, radix);
    }

    @Override
    public Integer plus(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer minus(Integer a, Integer b) {
        return a - b;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        return a / b;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public Integer negate(Integer a) {
        return -a;
    }

    @Override
    public int cmp(Integer a, Integer b) {
        return a.compareTo(b);
    }

}
