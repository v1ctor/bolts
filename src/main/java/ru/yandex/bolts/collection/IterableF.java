package ru.yandex.bolts.collection;

import ru.yandex.bolts.collection.impl.TraversableF;


public interface IterableF<E> extends TraversableF<E>, Iterable<E> {

    IteratorF<E> iterator();

    <F> IterableF<F> uncheckedCast();
} //~
