package ru.yandex.bolts.collection.impl;

import java.util.Map;

import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Unmodifiable;


class UnmodifiableSetFromMap<E> extends SetFromMap<E> implements Unmodifiable {
    private static final long serialVersionUID = 189028259515583178L;

    public UnmodifiableSetFromMap(Map<E, Boolean> map) {
        super(map);
    }

    @Override
    public boolean add(E o) {
        throw new UnsupportedOperationException("unmodifiable");
    }

    @Override
    public IteratorF<E> iterator() {
        return super.iterator().unmodifiable();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("unmodifiable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public SetF<E> unmodifiable() {
        return this;
    }
}
