package ru.yandex.bolts.collection.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Unmodifiable;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;


public class SingletonSet<E> extends AbstractSetF<E> implements Unmodifiable, Serializable {
    private static final long serialVersionUID = -7623844541769642464L;
    private final E e;

    public SingletonSet(E e) {
        this.e = e;
    }


    public int size() {
        return 1;
    }

    public IteratorF<E> iterator() {
        return Cf.x(Collections.singleton(e)).iterator();
    }

    @Override
    public ListF<E> toList() {
        return Cf.list(e);
    }

    @Override
    public boolean contains(Object o) {
        return eq(o, e);
    }

    @Override
    public Function1B<E> containsF() {
        // optimization
        return Function1B.equalsF(e);
    }

    @Override
    public <B> ListF<B> map(Function<? super E, B> f) {
        return Cf.list(f.apply(e));
    }

    @Override
    public SetF<E> filter(Function1B<? super E> p) {
        if (p.apply(e)) return this;
        else return Cf.set();
    }

    @Override
    public SetF<E> unmodifiable() {
        return this;
    }

    @Override
    public SetF<E> plus1(E e) {
        if (contains(e)) return this;
        else return new SetOf2<>(this.e, e);
    }

    @Override
    public SetF<E> plus(Iterator<? extends E> iterator) {
        if (!iterator.hasNext()) return this;
        else return plus1(iterator.next()).plus(iterator);
    }
} //~
