package ru.yandex.bolts.type;

/**
 * @author Stepan Koltsov
 */
public class CharSequenceType extends AnyCharSequenceType<CharSequence> {

    @Override
    public CharSequence plus(CharSequence a, CharSequence b) {
        return a.toString() + b.toString();
    }

    @Override
    public CharSequence zero() {
        return "";
    }

} //~
