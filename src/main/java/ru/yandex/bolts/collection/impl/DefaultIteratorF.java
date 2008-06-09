package ru.yandex.bolts.collection.impl;

import java.util.Iterator;

import ru.yandex.bolts.collection.IteratorF;

/**
 * Implementation of IteratorF with delegation
 * 
 * @author Stepan Koltsov
 */
public class DefaultIteratorF<E> extends AbstractIteratorF<E> {
    protected Iterator<E> iterator;

    protected DefaultIteratorF(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public IteratorF<E> unmodifiable() {
        return UnmodifiableDefaultIteratorF.wrap(iterator);
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public E next() {
        return iterator.next();
    }

    public void remove() {
        iterator.remove();
    }

    public static <A> IteratorF<A> wrap(Iterator<A> iterator) {
        if (iterator instanceof IteratorF) return (IteratorF<A>) iterator;
        else return new DefaultIteratorF<A>(iterator);
    }
} //~
