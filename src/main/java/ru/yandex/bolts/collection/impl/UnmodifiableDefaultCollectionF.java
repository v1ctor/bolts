package ru.yandex.bolts.collection.impl;

import java.util.Collection;

import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.Unmodifiable;


public class UnmodifiableDefaultCollectionF<E> extends DefaultCollectionF<E> implements Unmodifiable {
    private static final long serialVersionUID = -5425184381772318904L;

    protected UnmodifiableDefaultCollectionF(Collection<E> target) {
        super(target);
    }

    @Override
    public CollectionF<E> unmodifiable() {
        return this;
    }

    @Override
    public boolean add(E o) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public IteratorF<E> iterator() {
        return new UnmodifiableDefaultIteratorF<>(target.iterator());
    }

    public static <E> CollectionF<E> wrap(Collection<E> collection) {
        if (collection instanceof CollectionF<?> && collection instanceof Unmodifiable) return (CollectionF<E>) collection;
        else return new UnmodifiableDefaultCollectionF<>(collection);
    }
} //~
