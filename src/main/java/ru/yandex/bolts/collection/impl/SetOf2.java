package ru.yandex.bolts.collection.impl;

import java.io.Serializable;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Unmodifiable;


class SetOf2<E> extends AbstractSetF<E> implements Serializable, Unmodifiable {
    private static final long serialVersionUID = 6009991372576971341L;

    private final E e1;
    private final E e2;

    public SetOf2(E e1, E e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public ListF<E> toList() {
        return Cf.list(e1, e2);
    }

    @Override
    public IteratorF<E> iterator() {
        return toList().iterator();
    }

    @Override
    public boolean contains(Object o) {
        return eq(e1, o) || eq(e2, o);
    }

    @Override
    public SetF<E> plus1(E e) {
        if (contains(e)) return this;
        else return new SetOf3<>(e1, e2, e);
    }

    public int size() {
        return 2;
    }
} //~
