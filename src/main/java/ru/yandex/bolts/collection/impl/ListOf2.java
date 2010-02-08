package ru.yandex.bolts.collection.impl;

import java.io.Serializable;
import java.util.RandomAccess;

import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Unmodifiable;

/**
 * @author Stepan Koltsov
 */
public class ListOf2<E> extends AbstractListF<E> implements RandomAccess, Unmodifiable, Serializable {
    private static final long serialVersionUID = -3152613254192270879L;

    private final E e1;
    private final E e2;

    public ListOf2(E e1, E e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public int size() {
        return 2;
    }

    public E get(int index) throws IndexOutOfBoundsException {
        if (index == 0) return e1;
        else if (index == 1) return e2;
        else throw new IndexOutOfBoundsException();
    }

    @Override
    public ListF<E> unmodifiable() {
        return this;
    }

    @Override
    public E first() {
        return e1;
    }

    @Override
    public E last() {
        return e2;
    }

    @Override
    public SetF<E> unique() {
        return CollectionsF.set(e1, e2);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListF<E> plus1(E e) {
        return CollectionsF.list(this.e1, this.e2, e);
    }
} //~
