package ru.yandex.bolts.collection.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;

/**
 * Implementation of SetF with delegation
 * 
 * @author Stepan Koltsov
 */
public class DefaultSetF<E> extends AbstractSetF<E> implements Serializable {
    private static final long serialVersionUID = 1043989682388913605L;
    
    protected Set<E> target;

    public DefaultSetF(Set<E> target) {
        this.target = target;
    }

    @Override
    public SetF<E> unmodifiable() {
        return UnmodifiableDefaultSetF.wrap(target);
    }

    public ListF<E> sort() {
        if (target instanceof SortedSet<?>) return toList();
        else return super.sort();
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

    public boolean retainAll(Collection<?> c) {
        return target.retainAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return target.removeAll(c);
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

    public static <A> SetF<A> wrap(Set<A> set) {
        if (set instanceof SetF<?>) return (SetF<A>) set;
        else return new DefaultSetF<A>(set);
    }
} //~
