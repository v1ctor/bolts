package ru.yandex.bolts.collection.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.SetF;


public class SetFromMap<E> extends AbstractSetF<E> implements Serializable {
    private static final long serialVersionUID = 8122743926193204170L;

    private final Map<E, Boolean> map;

    public SetFromMap(Map<E, Boolean> map) {
        if (map == null) throw new IllegalArgumentException();
        this.map = map;
    }

    private Set<E> set() {
        return map.keySet();
    }

    @Override
    public IteratorF<E> iterator() {
        return Cf.x(set().iterator());
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean add(E o) {
        return map.put(o, Boolean.TRUE) == null;
    }

    @Override
    public boolean contains(Object o) {
        return set().contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set().containsAll(c);
    }

    @Override
    public boolean isEmpty() {
        return set().isEmpty();
    }

    @Override
    public boolean remove(Object o) {
        return set().remove(o);
    }

    @Override
    public SetF<E> unmodifiable() {
        return new UnmodifiableSetFromMap<E>(map);
    }

}
