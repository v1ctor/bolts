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

    /**
     * Return collection with only elements that match predicate.
     */
    CollectionF<E> filter(Function1B<? super E> p);

    /**
     * Return collection with only elements that do not match predicate.
     */
    CollectionF<E> filterNot(Function1B<? super E> p);

    CollectionF<E> filterNotNull();

    /**
     * @see Class#isInstance(Object)
     */
    <F extends E> CollectionF<F> filterByType(Class<F> type);

    /** Pair of collection, first contains elements matching p, second contains element matching !p */
    Tuple2<? extends IterableF<E>, ? extends IterableF<E>> partition(Function1B<? super E> p);

    /** Map */
    <B> ListF<B> map(Function<? super E, B> mapper);

    /** Flat map */
    <B> ListF<B> flatMap(Function<? super E, ? extends Collection<B>> f);

    /** Flat map variant that accept mapper that returns Option instead of Collection */
    <B> ListF<B> flatMapO(Function<? super E, Option<B>> f);

    /**
     * @see #flatMapO(Function)
     */
    <B> Tuple2List<E, B> zipWithFlatMapO(Function<? super E, Option<B>> f);

    /**
     * Map to list of map entries and construct map
     * @see #toMap(Function, Function)
     * @see #toTuple2List(Function)
     */
    <K, V> MapF<K, V> toMap(Function<? super E, Tuple2<K, V>> t);

    /**
     * @see #toMap(Function)
     */
    <K, V> MapF<K, V> toMap(Function<? super E, K> fk, Function<? super E, ? extends V> fv);

    /** Map to list of map keys and construct map. Elements of this collection are used as values */
    <K> MapF<K, E> toMapMappingToKey(Function<? super E, K> m);

    /** Map to list of map values and construct map. Elements of this collection are used as keys */
    <V> MapF<E, V> toMapMappingToValue(Function<? super E, V> m);

    /**
     * @see #toMap(Function)
     * @see #toTuple2List(Function, Function)
     */
    <A, B> Tuple2List<A, B> toTuple2List(Function<? super E, Tuple2<A, B>> f);

    /**
     * @see #toTuple2List(Function)
     * @see #toMap(Function, Function)
     */
    <A, B> Tuple2List<A, B> toTuple2List(Function<? super E, ? extends A> fa, Function<? super E, ? extends B> fb);

    <A, B, C> Tuple3List<A, B, C> toTuple3List(Function<? super E, Tuple3<A, B, C>> f);

    <A, B, C> Tuple3List<A, B, C> toTuple3List(Function<? super E, ? extends A> fa, Function<? super E, ? extends B> fb, Function<? super E, ? extends C> fc);

    /** Delegate to {@link #contains(Object)} */
    Function1B<E> containsF();

    /** Convert this to list */
    ListF<E> toList();

    /** Return set of elements of this */
    SetF<E> unique();

    CollectionF<E> stableUnique();

    /** Elements sorted by {@link Comparator#naturalComparator()} */
    ListF<E> sorted();

    /** Elements sorted by given comparator */
    ListF<E> sorted(java.util.Comparator<? super E> comparator);

    ListF<E> sortedBy(Function<? super E, ?> f);

    ListF<E> sortedByDesc(Function<? super E, ?> f);

    /** First k elements sorted by {@link Comparator#naturalComparator()} */
    ListF<E> takeSorted(int k);

    ListF<E> takeSortedDesc(int k);

    /** First k elements sorted by given comparator */
    ListF<E> takeSorted(java.util.Comparator<? super E> comparator, int k);

    ListF<E> takeSortedBy(Function<? super E, ?> f, int k);

    ListF<E> takeSortedByDesc(Function<? super E, ?> f, int k);

    /** Element that would be n-th element of sorted() */
    E getSorted(int n);

    E getSorted(java.util.Comparator<? super E> comparator, int n);

    E getSorted(Function<? super E, ?> f, int n);

    ListF<E> shuffle();

    /**
     * Group elements by applying given function to each element.
     */
    <V> MapF<V, ListF<E>> groupBy(Function<? super E, ? extends V> m);

    /** Delegate to {@link #add(Object)} */
    Function1V<E> addF();

    /**
     * This collection plus one element.
     */
    CollectionF<E> plus1(E e);

    /**
     * Collection with all elements of this collection and that collection.
     */
    CollectionF<E> plus(Collection<? extends E> elements);

    /**
     * Collection with all elements of this collection and that collection.
     */
    CollectionF<E> plus(Iterator<? extends E> iterator);

    /**
     * Varargs variant of {@link #plus(Collection)}.
     */
    @SuppressWarnings("unchecked")
    CollectionF<E> plus(E... additions);

    /**
     * Varargs version of <code>addAll</code>
     *
     * @see #addAll(Collection)
     */
    @SuppressWarnings("unchecked")
    void addAll(E... additions);

    /** Copy elements to the new array */
    E[] toArray(Class<E> cl);

    /**
     * Copy all elements of this collection to newly allocated byte array.
     * Fail if any element of this collection is null or not {@link Byte}.
     */
    byte[] toByteArray();
    /**
     * @see #toByteArray()
     */
    short[] toShortArray();
    /**
     * @see #toByteArray()
     */
    int[] toIntArray();
    /**
     * @see #toByteArray()
     */
    long[] toLongArray();
    /**
     * @see #toByteArray()
     */
    boolean[] toBooleanArray();
    /**
     * @see #toByteArray()
     */
    char[] toCharArray();
    /**
     * @see #toByteArray()
     */
    float[] toFloatArray();
    /**
     * @see #toByteArray()
     */
    double[] toDoubleArray();

    /**
     * Return unmodifiable view or unmodifiable copy of this.
     *
     * @see Collections#unmodifiableCollection(Collection)
     */
    CollectionF<E> unmodifiable();

    /**
     * Old name for {@link #cast()}.
     */
    <F> CollectionF<F> uncheckedCast();

    /**
     * Return <code>this</code> with another type parameter and no type checks.
     */
    <F> CollectionF<F> cast();

    /**
     * @throws ClassCastException if any element of this cannot be cast to specified type
     *
     * @return <code>this</code>
     */
    <F> CollectionF<F> cast(Class<F> type);

    ListF<ListF<E>> paginate(int pageSize);

    /**
     * A fluent-style friendly variant of Cf.flatten().  It is an unsafe method in that
     * it requires you to specify a correct generic type.
     *
     * CollectionF<CollectionF<A>> coll = ...;
     * CollectionF<A> = coll.<A>flatten();
     */
    <F> ListF<F> flatten();

    /** @deprecated */
    @Override
    boolean remove(Object o);
    boolean removeTs(E e);
    boolean removeTu(Object e);

    /** @deprecated */
    @Override
    boolean removeAll(Collection<?> c);
    boolean removeAllTs(Collection<? extends E> c);
    boolean removeAllTu(Collection<?> c);

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
