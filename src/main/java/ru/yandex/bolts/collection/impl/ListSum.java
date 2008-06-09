package ru.yandex.bolts.collection.impl;

import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;

/**
 * Propbably useless.
 * 
 * @author Stepan Koltsov
 */
class ListSum<E> extends AbstractListF<E> {
    private final ListF<E> a;
    private final ListF<E> b;

    public ListSum(ListF<E> a, ListF<E> b) {
        this.a = a;
        this.b = b;
    }

    public int size() {
        return a.size() + b.size();
    }

    public E get(int index) {
        if (index < a.size()) return a.get(index);
        else return b.get(index - a.size());
    }

    public IteratorF<E> iterator() {
        return a.iterator().plus(b.iterator());
    }
} //~
