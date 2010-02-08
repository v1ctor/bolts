package ru.yandex.bolts.collection.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.RandomAccess;

import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Unmodifiable;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;

/**
 * @author Stepan Koltsov
 */
public class SingletonList<E> extends AbstractListF<E> implements RandomAccess, Serializable, Unmodifiable {
    private static final long serialVersionUID = -3371815609806480352L;

    private final E e;

    public SingletonList(E e) {
        this.e = e;
    }

    public int size() {
        return 1;
    }

    @Override
    public SetF<E> unique() {
        return CollectionsF.set(e);
    }

    public E get(int index) {
        if (index == 0) return e;
        else throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean contains(Object o) {
        return eq(e, o);
    }

    @Override
    public <B> ListF<B> map(Function<? super E, B> f) {
        return new SingletonList<B>(f.apply(e));
    }

    @Override
    public ListF<E> filter(Function1B<? super E> p) {
        if (p.apply(e)) return this;
        else return emptyList();
    }

    @Override
    public ListF<E> plus1(E e) {
        return CollectionsF.list(this.e, e);
    }

    @Override
    public ListF<E> plus(Iterator<? extends E> iterator) {
        if (!iterator.hasNext()) return this;
        else return plus1(iterator.next()).plus(iterator);
    }
} //~
