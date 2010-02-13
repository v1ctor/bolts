package ru.yandex.bolts.collection.impl;

import java.util.Enumeration;

import ru.yandex.bolts.collection.IteratorF;

/**
 * @author Stepan Koltsov
 */
public class DefaultEnumerationF<E> extends AbstractIteratorF<E> {

    protected Enumeration<E> enumeration;

    public DefaultEnumerationF(Enumeration<E> enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    @Override
    public E next() {
        return enumeration.nextElement();
    }

    public static <A> IteratorF<A> wrap(Enumeration<A> enumeration) {
        return new DefaultEnumerationF<A>(enumeration);
    }

} //~
