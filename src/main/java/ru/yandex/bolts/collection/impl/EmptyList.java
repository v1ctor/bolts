package ru.yandex.bolts.collection.impl;

import java.io.Serializable;
import java.util.RandomAccess;

import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Unmodifiable;

/**
 * @author Stepan Koltsov
 */
public class EmptyList<A> extends AbstractListF<A> implements Unmodifiable, RandomAccess, Serializable {
    private static final long serialVersionUID = 8505000537896934647L;

    @SuppressWarnings("unchecked")
    public static final ListF INSTANCE = new EmptyList();

    public int size() {
        return 0;
    }

    public A get(int index) {
        throw new IndexOutOfBoundsException("EmptyList.get(...)");
    }

    public IteratorF<A> iterator() {
        return CollectionsF.emptyIterator();
    }

    public ListF<A> unmodifiable() {
        return this;
    }

    private Object readResolve() {
        return INSTANCE;
    }

} //~
