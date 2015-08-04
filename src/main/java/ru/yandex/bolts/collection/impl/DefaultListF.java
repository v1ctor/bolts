package ru.yandex.bolts.collection.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Collection;
import java.util.ListIterator;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;


public class DefaultListF<E> extends AbstractListF<E> implements Serializable {
    private static final long serialVersionUID = -9125154479157748328L;

    protected List<E> target;

    public DefaultListF(List<E> target) {
        if (target == null) throw new IllegalArgumentException("target is null");

        this.target = target;
    }

    @Override
    public ListF<E> unmodifiable() {
        return UnmodifiableDefaultListF.wrap(target);
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

    public IteratorF<E> iterator() {
        return Cf.x(target.iterator());
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

    public boolean addAll(int index, Collection<? extends E> c) {
        return target.addAll(index, c);
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

    public E get(int index) {
        return target.get(index);
    }

    public E set(int index, E element) {
        return target.set(index, element);
    }

    public void add(int index, E element) {
        target.add(index, element);
    }

    public E remove(int index) {
        return target.remove(index);
    }

    public int indexOf(Object o) {
        return target.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return target.lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
        return target.listIterator();
    }

    public ListIterator<E> listIterator(int index) {
        return target.listIterator(index);
    }

    public ListF<E> subList(int fromIndex, int toIndex) {
        return wrap(target.subList(fromIndex, toIndex));
    }

    public static <A> ListF<A> wrap(List<A> list) {
        if (list instanceof ListF<?>) return (ListF<A>) list;
        else return new DefaultListF<>(list);
    }
} //~
