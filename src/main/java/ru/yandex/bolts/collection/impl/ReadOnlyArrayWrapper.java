package ru.yandex.bolts.collection.impl;

import java.io.Serializable;
import java.util.RandomAccess;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Unmodifiable;

/**
 * @author Stepan Koltsov
 */
public class ReadOnlyArrayWrapper<E> extends AbstractListF<E> implements RandomAccess, Unmodifiable, Serializable {
    private static final long serialVersionUID = -9093427257855589948L;
    
    private final E[] array;

    public ReadOnlyArrayWrapper(E... array) {
        this.array = array;
    }

    public int size() {
        return array.length;
    }

    public E get(int index) {
        if (index < 0 || index >= array.length) throw new IndexOutOfBoundsException();
        else return array[index];
    }

    public ListF<E> unmodifiable() {
        return this;
    }

    public SetF<E> unique() {
        return Cf.set(array);
    }

    @Override
    public Object[] toArray() {
        return array.clone();
    }

    @Override
    public IteratorF<E> iterator() {
        return super.readOnlyIterator();
    }
} //~
