package ru.yandex.bolts.collection;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.weaving.annotation.FunctionParameter;

/**
 * Extended set.
 *
 * @author Stepan Koltsov
 */
public interface SetF<E> extends CollectionF<E>, Set<E> {
    /** Iterate */
    IteratorF<E> iterator();

    /** Filter */
    @Override
    SetF<E> filter(Function1B<? super E> p);

    @Override
    SetF<E> filterW(@FunctionParameter boolean p);

    @Override
    SetF<E> filterNotNull();

    /**
     * @deprecated
     * @see #partition(Function1B)
     */
    Tuple2<SetF<E>, SetF<E>> filter2(Function1B<? super E> p);

    @Override
    Tuple2<SetF<E>, SetF<E>> partition(Function1B<? super E> p);

    @Override
    Tuple2<SetF<E>, SetF<E>> partitionW(@FunctionParameter boolean p);

    /** this - set */
    SetF<E> minus(Set<E> set);

    SetF<E> minus(Collection<E> set);

    /** Intersect this and set */
    SetF<E> intersect(Set<E> set);

    SetF<E> plus1(E e);

    SetF<E> plus(Collection<? extends E> elements);

    SetF<E> plus(Iterator<? extends E> iterator);

    SetF<E> plus(E... additions);

    SetF<E> unmodifiable();

    <F> SetF<F> uncheckedCast();
} //~
