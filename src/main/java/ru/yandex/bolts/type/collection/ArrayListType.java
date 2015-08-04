package ru.yandex.bolts.type.collection;

import java.util.Collection;

import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.impl.ArrayListF;


public class ArrayListType extends AnyListType {

    @Override
    public <A> ListF<A> cons() {
        return new ArrayListF<>();
    }

    public <A> ListF<A> cons(int capacity) {
        return new ArrayListF<>(capacity);
    }

    @Override
    public <A> ListF<A> cons(A a1) {
        ListF<A> r = cons(1);
        r.add(a1);
        return r;
    }

    @Override
    public <A> ListF<A> cons(A a1, A a2) {
        ListF<A> r = cons(1);
        r.add(a1);
        r.add(a2);
        return r;
    }

    @Override
    public <A> ListF<A> cons(Collection<A> collection) {
        return new ArrayListF<>(collection);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A> ListF<A> cons(A... elements) {
        return ArrayListF.valueOf(elements);
    }

} //~
