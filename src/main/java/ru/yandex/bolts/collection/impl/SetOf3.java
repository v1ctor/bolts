package ru.yandex.bolts.collection.impl;

import java.io.Serializable;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Unmodifiable;


class SetOf3<E> extends AbstractSetF<E> implements Serializable, Unmodifiable {
    private static final long serialVersionUID = 8121848888345607526L;

    private final E e1;
    private final E e2;
    private final E e3;

    SetOf3(E e1, E e2, E e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListF<E> toList() {
        return Cf.list(e1, e2, e3);
    }

    @Override
    public IteratorF<E> iterator() {
        return toList().iterator();
    }

    @Override
    public boolean contains(Object o) {
        return eq(e1, o) || eq(e2, o) || eq(e3, o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SetF<E> plus1(E e) {
        return Cf.set(e1, e2, e3, e);
    }

    public int size() {
        return 3;
    }
} //~
