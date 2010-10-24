package ru.yandex.bolts.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import ru.yandex.bolts.function.Function1B;

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
    SetF<E> filterNotNull();

    /**
     * {@inheritDoc}
     */
    @Override
    <F extends E> SetF<F> filterByType(Class<F> type);

    @Override
    Tuple2<SetF<E>, SetF<E>> partition(Function1B<? super E> p);

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

    /** @deprecated */
    @Override
    boolean remove(Object o);
    boolean removeTs(E e);
    boolean removeTu(Object e);

    /** @deprecated */
    @Override
    boolean removeAll(Collection<?> c);
    boolean removeAllTs(Collection<? extends E> c);
    boolean removeAllRu(Collection<?> c);

    /** @deprecated */
    @Override
    boolean contains(Object o);
    boolean containsTs(E e);
    boolean containsTu(Object e);

    /** @deprecated */
    @Override
    boolean containsAll(Collection<?> coll);
    boolean containsAllTs(Collection<? extends E> coll);
    boolean containsAllTu(Collection<?> coll);

    /** @deprecated */
    @Override
    public boolean retainAll(Collection<?> c);
    public boolean retainAllTs(Collection<? extends E> c);
    public boolean retainAllTu(Collection<?> c);

} //~
