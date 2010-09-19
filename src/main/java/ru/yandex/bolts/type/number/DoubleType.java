package ru.yandex.bolts.type.number;

import ru.yandex.bolts.collection.CollectionsF;

/**
 * @author Stepan Koltsov
 * @see CollectionsF#Double
 */
public class DoubleType extends NumberType<Double> {

    @Override
    public Double zero() {
        return 0.0;
    }

    @Override
    public Double parse(String string) throws NumberFormatException {
        return Double.parseDouble(string);
    }

    @Override
    public Double plus(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double minus(Double a, Double b) {
        return a - b;
    }

    @Override
    public Double divide(Double a, Double b) {
        return a / b;
    }

    @Override
    public Double multiply(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double negate(Double a) {
        return -a;
    }

    @Override
    public int cmp(Double a, Double b) {
        return a.compareTo(b);
    }

} //~
