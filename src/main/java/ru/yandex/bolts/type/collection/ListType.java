package ru.yandex.bolts.type.collection;

import java.util.Collection;
import java.util.List;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.impl.ArrayListF;
import ru.yandex.bolts.collection.impl.ReadOnlyArrayList;
import ru.yandex.bolts.function.Function;


public class ListType extends AnyListType {

    @SuppressWarnings("unchecked")
    @Override
    public <A> ListF<A> cons() {
        return (ListF<A>) ReadOnlyArrayList.EMPTY;
    }

    @Override
    public <A> ListF<A> cons(A a1) {
        return ReadOnlyArrayList.cons(a1);
    }

    @Override
    public <A> ListF<A> cons(A a1, A a2) {
        return ReadOnlyArrayList.cons(a1, a2);
    }

    @Override
    public <A> ListF<A> cons(Collection<A> collection) {
        return new ArrayListF<>(collection).convertToReadOnly();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A> ListF<A> cons(A... elements) {
        return ReadOnlyArrayList.valueOf(elements);
    }

    public <A> ListF<A> wrap(List<A> list) {
        return Cf.x(list);
    }

    public <A> Function<List<A>, ListF<A>> wrapF() {
        return this::wrap;
    }

} //~
