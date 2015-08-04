package ru.yandex.bolts.collection.impl;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Unmodifiable;


public class UnmodifiableDefaultListF<E> extends DefaultListF<E> implements Unmodifiable {
    private static final long serialVersionUID = -1174132251632064137L;

    public UnmodifiableDefaultListF(List<E> target) {
        super(target);
    }

    public ListF<E> unmodifiable() {
        return this;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public boolean add(E o) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public ListIterator<E> listIterator() {
        // XXX: immutable ListIterator
        return super.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        // XXX: immutable ListIterator
        return super.listIterator(index);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public ListF<E> subList(int fromIndex, int toIndex) {
        return new UnmodifiableDefaultListF<>(target.subList(fromIndex, toIndex));
    }

    @Override
    public IteratorF<E> iterator() {
        return new UnmodifiableDefaultIteratorF<>(target.iterator());
    }

    public static <E> ListF<E> wrap(List<E> list) {
        if (list instanceof ListF<?> && list instanceof Unmodifiable) return (ListF<E>) list;
        else return new UnmodifiableDefaultListF<>(list);
    }
} //~
