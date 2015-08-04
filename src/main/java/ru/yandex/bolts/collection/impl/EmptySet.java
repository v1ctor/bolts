package ru.yandex.bolts.collection.impl;

import java.io.Serializable;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Unmodifiable;
import ru.yandex.bolts.function.Function1B;


public class EmptySet<A> extends AbstractSetF<A> implements Unmodifiable, Serializable {
    private static final long serialVersionUID = -2106694758991579344L;

    @SuppressWarnings("rawtypes")
    public static final SetF INSTANCE = new EmptySet();

    private EmptySet() { }

    public IteratorF<A> iterator() {
        return Cf.emptyIterator();
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Function1B<A> containsF() {
        // optimization
        return Function1B.falseF();
    }

    public int size() {
        return 0;
    }

    @Override
    public SetF<A> unmodifiable() {
        return this;
    }

    @Override
    public SetF<A> plus1(A a) {
        return Cf.set(a);
    }



    private Object readResolve() {
        return INSTANCE;
    }
} //~
