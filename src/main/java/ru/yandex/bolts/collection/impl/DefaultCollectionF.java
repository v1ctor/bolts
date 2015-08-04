package ru.yandex.bolts.collection.impl;

import java.io.Serializable;
import java.util.Collection;

import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;


public class DefaultCollectionF<E> extends AbstractCollectionF<E> implements Serializable {
    private static final long serialVersionUID = 8678268074197952366L;

    protected Collection<E> target;


    protected DefaultCollectionF(Collection<E> target) {
        this.target = target;
    }

    @Override
    public CollectionF<E> unmodifiable() {
        return UnmodifiableDefaultCollectionF.wrap(target);
    }

    public IteratorF<E> iterator() {
        return DefaultIteratorF.wrap(target.iterator());
    }

    public int size() {
        return target.size();
    }

    public boolean isEmpty() {
        return target.isEmpty();
    }

    public boolean contains(Object o) {
        return target.contains(o);
    }

    public Object[] toArray() {
        return target.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return target.toArray(a);
    }

    public boolean add(E o) {
        return target.add(o);
    }

    public boolean remove(Object o) {
        return target.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return target.containsAll(c);
    }

    public boolean addAll(Collection<? extends E> c) {
        return target.addAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return target.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return target.retainAll(c);
    }

    public void clear() {
        target.clear();
    }

    public boolean equals(Object o) {
        return target.equals(o);
    }

    public int hashCode() {
        return target.hashCode();
    }

    public static <A> CollectionF<A> wrap(Collection<A> set) {
        if (set instanceof CollectionF<?>) return (CollectionF<A>) set;
        else return new DefaultCollectionF<>(set);
    }

} //~
