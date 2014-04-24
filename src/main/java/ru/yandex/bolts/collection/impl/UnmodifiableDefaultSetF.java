package ru.yandex.bolts.collection.impl;

import java.util.Set;
import java.util.Collection;

import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Unmodifiable;

/**
 * @author Stepan Koltsov
 */
public class UnmodifiableDefaultSetF<E> extends DefaultSetF<E> implements Unmodifiable {
    private static final long serialVersionUID = -3357028613260080942L;

    public UnmodifiableDefaultSetF(Set<E> target) {
        super(target);
    }

    @Override
    public SetF<E> unmodifiable() {
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
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public IteratorF<E> iterator() {
        return new UnmodifiableDefaultIteratorF<>(target.iterator());
    }

    public static <E> SetF<E> wrap(Set<E> set) {
        if (set instanceof SetF<?> && set instanceof Unmodifiable) return (SetF<E>) set;
        else return new UnmodifiableDefaultSetF<>(set);
    }
} //~
