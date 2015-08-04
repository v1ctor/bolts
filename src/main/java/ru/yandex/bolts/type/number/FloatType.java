package ru.yandex.bolts.type.number;

import ru.yandex.bolts.collection.Cf;



public class FloatType extends NumberType<Float> {

    @Override
    public Float zero() {
        return 0.0f;
    }

    @Override
    public Float parse(String string) throws NumberFormatException {
        return Float.parseFloat(string);
    }

    @Override
    public Float plus(Float a, Float b) {
        return a + b;
    }

    @Override
    public Float minus(Float a, Float b) {
        return a - b;
    }

    @Override
    public Float divide(Float a, Float b) {
        return a / b;
    }

    @Override
    public Float multiply(Float a, Float b) {
        return a * b;
    }

    @Override
    public Float negate(Float a) {
        return -a;
    }

    @Override
    public int cmp(Float a, Float b) {
        return a.compareTo(b);
    }

}
