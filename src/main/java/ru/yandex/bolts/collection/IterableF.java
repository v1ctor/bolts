package ru.yandex.bolts.collection;

import ru.yandex.bolts.collection.impl.TraversableF;

/**
 * Extended iterable.
 *
 * @author Stepan Koltsov
 */
public interface IterableF<E> extends TraversableF<E>, Iterable<E> {
    /** Elements as iterator */
    IteratorF<E> iterator();

    <F> IterableF<F> uncheckedCast();
} //~
