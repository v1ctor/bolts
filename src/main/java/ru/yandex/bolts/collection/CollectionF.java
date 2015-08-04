package ru.yandex.bolts.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * Extended collection.
 *
 * Methods like {@link #filter(Function1B)}, {@link #sorted()} or {@link #plus(Collection)}
 * are permitted to return <code>this</code> collection if result of operation is a collection equal to this,
 * even if this collection is mutable.
 *
 * @author Stepan Koltsov
 */
public interface CollectionF<E> extends Collection<E>, IterableF<E> {
    boolean isNotEmpty();

    IteratorF<E> iterator();


    CollectionF<E> filter(Function1B<? super E> p);


    CollectionF<E> filterNot(Function1B<? super E> p);

    CollectionF<E> filterNotNull();


    <F extends E> CollectionF<F> filterByType(Class<F> type);


    Tuple2<? extends IterableF<E>, ? extends IterableF<E>> partition(Function1B<? super E> p);


    <B> ListF<B> map(Function<? super E, B> mapper);


    <B> ListF<B> flatMap(Function<? super E, ? extends Collection<B>> f);


    <B> ListF<B> flatMapO(Function<? super E, Option<B>> f);


    <B> Tuple2List<E, B> zipWithFlatMapO(Function<? super E, Option<B>> f);


    <K, V> MapF<K, V> toMap(Function<? super E, Tuple2<K, V>> t);


    <K, V> MapF<K, V> toMap(Function<? super E, K> fk, Function<? super E, ? extends V> fv);


    <K> MapF<K, E> toMapMappingToKey(Function<? super E, K> m);


    <V> MapF<E, V> toMapMappingToValue(Function<? super E, V> m);


    <A, B> Tuple2List<A, B> toTuple2List(Function<? super E, Tuple2<A, B>> f);


    <A, B> Tuple2List<A, B> toTuple2List(Function<? super E, ? extends A> fa, Function<? super E, ? extends B> fb);

    <A, B, C> Tuple3List<A, B, C> toTuple3List(Function<? super E, Tuple3<A, B, C>> f);

    <A, B, C> Tuple3List<A, B, C> toTuple3List(Function<? super E, ? extends A> fa, Function<? super E, ? extends B> fb, Function<? super E, ? extends C> fc);


    Function1B<E> containsF();


    ListF<E> toList();


    SetF<E> unique();

    CollectionF<E> stableUnique();


    ListF<E> sorted();


    ListF<E> sorted(java.util.Comparator<? super E> comparator);

    ListF<E> sortedBy(Function<? super E, ?> f);

    ListF<E> sortedByDesc(Function<? super E, ?> f);


    ListF<E> takeSorted(int k);

    ListF<E> takeSortedDesc(int k);


    ListF<E> takeSorted(java.util.Comparator<? super E> comparator, int k);

    ListF<E> takeSortedBy(Function<? super E, ?> f, int k);

    ListF<E> takeSortedByDesc(Function<? super E, ?> f, int k);


    E getSorted(int n);

    E getSorted(java.util.Comparator<? super E> comparator, int n);

    E getSorted(Function<? super E, ?> f, int n);

    ListF<E> shuffle();


    <V> MapF<V, ListF<E>> groupBy(Function<? super E, ? extends V> m);


    Function1V<E> addF();


    CollectionF<E> plus1(E e);


    CollectionF<E> plus(Collection<? extends E> elements);


    CollectionF<E> plus(Iterator<? extends E> iterator);


    @SuppressWarnings("unchecked")
    CollectionF<E> plus(E... additions);

    /**
     * Varargs version of <code>addAll</code>
     *
     * @see #addAll(Collection)
     *
     * @param additions elements
     *
     */
    @SuppressWarnings("unchecked")
    void addAll(E... additions);


    E[] toArray(Class<E> cl);


    byte[] toByteArray();


    short[] toShortArray();


    int[] toIntArray();


    long[] toLongArray();


    boolean[] toBooleanArray();


    char[] toCharArray();


    float[] toFloatArray();


    double[] toDoubleArray();


    CollectionF<E> unmodifiable();


    <F> CollectionF<F> uncheckedCast();

    /**
     * Return <code>this</code> with another type parameter and no type checks.
     *
     * @param <F> type to cast
     *
     * @return array
     */
    <F> CollectionF<F> cast();

    /**
     * @throws ClassCastException if any element of this cannot be cast to specified type
     *
     * @param <F> type to cast
     * @param type type to cast
     *
     * @return <code>this</code>
     */
    <F> CollectionF<F> cast(Class<F> type);

    ListF<ListF<E>> paginate(int pageSize);


    <F> ListF<F> flatten();


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
