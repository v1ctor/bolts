package ru.yandex.bolts.type.number;

import ru.yandex.bolts.collection.Cf;


public class ByteType extends IntegralType<Byte> {

    @Override
    public Byte parse(String string, int radix) throws NumberFormatException {
        return Byte.parseByte(string, radix);
    }

    @Override
    public int cmp(Byte a, Byte b) {
        return a.compareTo(b);
    }

    @Override
    public Byte divide(Byte a, Byte b) {
        return (byte) (a / b);
    }

    @Override
    public Byte minus(Byte a, Byte b) {
        return (byte) (a - b);
    }

    @Override
    public Byte multiply(Byte a, Byte b) {
        return (byte) (a * b);
    }

    @Override
    public Byte negate(Byte a) {
        return (byte) -a;
    }

    @Override
    public Byte parse(String string) throws NumberFormatException {
        return Byte.parseByte(string);
    }

    @Override
    public Byte plus(Byte a, Byte b) {
        return (byte) (a + b);
    }

    @Override
    public Byte zero() {
        return (byte) 0;
    }

} //~
