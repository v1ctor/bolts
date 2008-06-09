package ru.yandex.bolts.collection.impl;

import java.util.Iterator;

import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.Unmodifiable;

/**
 * @author Stepan Koltsov
 */
public class UnmodifiableDefaultIteratorF<E> extends DefaultIteratorF<E> implements Unmodifiable {
    protected UnmodifiableDefaultIteratorF(Iterator<E> target) {
        super(target);
    }

    public IteratorF<E> unmodifiable() {
        return this;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("immutable");
    }

    public static <E> IteratorF<E> wrap(Iterator<E> iterator) {
        if (iterator instanceof IteratorF && iterator instanceof Unmodifiable) return (IteratorF<E>) iterator;
        else return new UnmodifiableDefaultIteratorF<E>(iterator);
    }
} //~
