package ru.yandex.bolts.collection.impl;

import java.util.NoSuchElementException;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.function.Function;


public class EmptyIterator<A> extends AbstractIteratorF<A> {
    public boolean hasNext() {
        return false;
    }

    public A next() {
        throw new NoSuchElementException("EmptyIterator.next()");
    }

    @Override
    public <B> IteratorF<B> map(Function<? super A, B> f) {
        return Cf.emptyIterator();
    }

    @Override
    public IteratorF<Tuple2<A, Integer>> zipWithIndex() {
        return Cf.emptyIterator();
    }

    @Override
    public ListF<A> toList() {
        return Cf.list();
    }

    @Override
    public SetF<A> toSet() {
        return Cf.set();
    }

} //~
