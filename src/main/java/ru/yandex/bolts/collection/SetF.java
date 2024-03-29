package ru.yandex.bolts.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import ru.yandex.bolts.function.Function1B;


public interface SetF<E> extends CollectionF<E>, Set<E> {

    IteratorF<E> iterator();


    @Override
    SetF<E> filter(Function1B<? super E> p);

    @Override
    SetF<E> filterNot(Function1B<? super E> p);

    @Override
    SetF<E> filterNotNull();


    @Override
    <F extends E> SetF<F> filterByType(Class<F> type);

    @Override
    Tuple2<SetF<E>, SetF<E>> partition(Function1B<? super E> p);

    SetF<E> minus1(E e);


    SetF<E> minus(Set<E> set);

    SetF<E> minus(Collection<E> set);


    SetF<E> intersect(Set<E> set);

    SetF<E> plus1(E e);

    SetF<E> plus(Collection<? extends E> elements);

    SetF<E> plus(Iterator<? extends E> iterator);

    @SuppressWarnings("unchecked")
    SetF<E> plus(E... additions);

    SetF<E> unmodifiable();

    <F> SetF<F> uncheckedCast();

    @Override
    <F> SetF<F> cast();

    @Override
    <F> SetF<F> cast(Class<F> type);


    @Override
    boolean remove(Object o);
    boolean removeTs(E e);
    boolean removeTu(Object e);


    @Override
    boolean removeAll(Collection<?> c);
    boolean removeAllTs(Collection<? extends E> c);
    boolean removeAllTu(Collection<?> c);


    @Override
    boolean contains(Object o);
    boolean containsTs(E e);
    boolean containsTu(Object e);


    @Override
    boolean containsAll(Collection<?> coll);
    boolean containsAllTs(Collection<? extends E> coll);
    boolean containsAllTu(Collection<?> coll);


    @Override
    public boolean retainAll(Collection<?> c);
    public boolean retainAllTs(Collection<? extends E> c);
    public boolean retainAllTu(Collection<?> c);

} //~
