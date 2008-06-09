package ru.yandex.bolts.collection.impl;

import java.io.Serializable;

import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Unmodifiable;
import ru.yandex.bolts.function.forhuman.Predicate;

/**
 * @author Stepan Koltsov
 */
public class EmptySet<A> extends AbstractSetF<A> implements Unmodifiable, Serializable {
    private static final long serialVersionUID = -2106694758991579344L;
    
    @SuppressWarnings("unchecked")
    public static final SetF INSTANCE = new EmptySet();
    
    private EmptySet() { }
    
    public IteratorF<A> iterator() {
        return CollectionsF.emptyIterator();
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Predicate<A> containsP() {
        // optimization
        return Predicate.falseP();
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
        return CollectionsF.set(a);
    }

    /*
    @Override
    public SetF<A> plus(Iterator<? extends A> iterator) {
        if (!iterator.hasNext()) return this;
        else return plus1(iterator.next()).plus(iterator);
    }
    */

    private Object readResolve() {
        return INSTANCE;
    }
} //~
