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
     *
     * @param p filter function
     *
     * @return modified collection
     */
    CollectionF<E> filter(Function1B<? super E> p);

    /**
     * Return collection with only elements that do not match predicate.
     *
     * @param p filter function
     *
     * @return modified collection
     */
    CollectionF<E> filterNot(Function1B<? super E> p);

    CollectionF<E> filterNotNull();

    /**
     * @see Class#isInstance(Object)
     *
     * @param type to filter
     * @param <F> element
     *
     * @return modified collection
     */
    <F extends E> CollectionF<F> filterByType(Class<F> type);

    /** Pair of collection, first contains elements matching p, second contains element matching !p
     *
     * @param p partition function
     *
     * @return partitioned typle
     * */
    Tuple2<? extends IterableF<E>, ? extends IterableF<E>> partition(Function1B<? super E> p);

    /** Map
     *
     * @param mapper mapper function
     * @param <B> element
     *
     * @return modified list
     * */
    <B> ListF<B> map(Function<? super E, B> mapper);

    /** Flat map
     *
     * @param f mapper function
     * @param <B> element
     *
     * @return modified list
     * */
    <B> ListF<B> flatMap(Function<? super E, ? extends Collection<B>> f);

    /** Flat map variant that accept mapper that returns Option instead of Collection
     *
     * @param f mapper function
     * @param <B> element
     *
     * @return modified list
     * */
    <B> ListF<B> flatMapO(Function<? super E, Option<B>> f);

    /**
     * @see #flatMapO(Function)
     *
     * @param f mapper function
     * @param <B> element
     *
     * @return result tuple
     */
    <B> Tuple2List<E, B> zipWithFlatMapO(Function<? super E, Option<B>> f);

    /**
     * Map to list of map entries and construct map
     * @see #toMap(Function, Function)
     * @see #toTuple2List(Function)
     * @param <K> key
     * @param <V> value
     * @param t function
     *
     * @return map
     */
    <K, V> MapF<K, V> toMap(Function<? super E, Tuple2<K, V>> t);

    /**
     * @see #toMap(Function)
     *
     * @param <K> key
     * @param <V> value
     * @param fk function
     * @param fv function
     *
     * @return map
     */
    <K, V> MapF<K, V> toMap(Function<? super E, K> fk, Function<? super E, ? extends V> fv);

    /** Map to list of map keys and construct map. Elements of this collection are used as values
     *
     * @param <K> key
     * @param m function
     * */
    <K> MapF<K, E> toMapMappingToKey(Function<? super E, K> m);

    /** Map to list of map values and construct map. Elements of this collection are used as keys
     *
     * @param <V> value
     * @param m function
     *
     * @return map
     * */
    <V> MapF<E, V> toMapMappingToValue(Function<? super E, V> m);

    /**
     * @see #toMap(Function)
     * @see #toTuple2List(Function, Function)
     *
     * @param <A> value
     * @param <B> value
     * @param f function
     *
     * @return tuple
     */
    <A, B> Tuple2List<A, B> toTuple2List(Function<? super E, Tuple2<A, B>> f);

    /**
     * @see #toTuple2List(Function)
     * @see #toMap(Function, Function)
     *
     * @param <A> value
     * @param <B> value
     * @param fa function
     * @param fb function
     *
     * @return tuple
     */
    <A, B> Tuple2List<A, B> toTuple2List(Function<? super E, ? extends A> fa, Function<? super E, ? extends B> fb);

    <A, B, C> Tuple3List<A, B, C> toTuple3List(Function<? super E, Tuple3<A, B, C>> f);

    <A, B, C> Tuple3List<A, B, C> toTuple3List(Function<? super E, ? extends A> fa, Function<? super E, ? extends B> fb, Function<? super E, ? extends C> fc);

    /** Delegate to {@link #contains(Object)}
     *
     * @return function
     * */
    Function1B<E> containsF();

    /** Convert this to list
     *
     *
     * @return list
     * */
    ListF<E> toList();

    /** Return set of elements of this
     *
     * @return set
     * */
    SetF<E> unique();

    CollectionF<E> stableUnique();

    /** Elements sorted by {@link Comparator#naturalComparator()}
     *
     * @return list
     * */
    ListF<E> sorted();

    /** Elements sorted by given comparator
     *
     * @param comparator comparator
     *
     * @return list
     * */
    ListF<E> sorted(java.util.Comparator<? super E> comparator);

    ListF<E> sortedBy(Function<? super E, ?> f);

    ListF<E> sortedByDesc(Function<? super E, ?> f);

    /** First k elements sorted by {@link Comparator#naturalComparator()}
     *
     * @param k amount of elements
     *
     * @return list
     * */
    ListF<E> takeSorted(int k);

    ListF<E> takeSortedDesc(int k);

    /** First k elements sorted by given comparator
     *
     *
     * @param k amount of elements
     * @param comparator comparator
     *
     * @return list
     * */
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
     *
     * @param m function to group
     * @param <V> value
     *
     * @return list
     */
    <V> MapF<V, ListF<E>> groupBy(Function<? super E, ? extends V> m);

    /** Delegate to {@link #add(Object)}
     *
     * @return function
     * */
    Function1V<E> addF();

    /**
     * This collection plus one element.
     *
     * @param e element to add
     *
     * @return collection
     * */
    CollectionF<E> plus1(E e);

    /**
     * Collection with all elements of this collection and that collection.
     *
     * @param elements elements to add
     *
     * @return collection
     * */
    CollectionF<E> plus(Collection<? extends E> elements);

    /**
     * Collection with all elements of this collection and that collection.
     *
     * @param iterator elements to add
     *
     * @return collection
     */
    CollectionF<E> plus(Iterator<? extends E> iterator);

    /**
     * Varargs variant of {@link #plus(Collection)}.
     *
     * @param additions element to add
     *
     * @return collection
     */
    @SuppressWarnings("unchecked")
    CollectionF<E> plus(E... additions);

    /**
     * Varargs version of <code>addAll</code>
     *
     * @see #addAll(Collection)
     *
     * @param additions
     *
     */
    @SuppressWarnings("unchecked")
    void addAll(E... additions);

    /** Copy elements to the new array
     *
     * @param cl result array type
     *
     * @return array
     * */
    E[] toArray(Class<E> cl);

    /**
     * Copy all elements of this collection to newly allocated byte array.
     * Fail if any element of this collection is null or not {@link Byte}.
     *
     * @return byte array
     */
    byte[] toByteArray();

    /**
     * @see #toByteArray()
     *
     * @return short array
     */
    short[] toShortArray();

    /**
     * @see #toByteArray()
     *
     * @return int array
     */
    int[] toIntArray();

    /**
     * @see #toByteArray()
     *
     * @return long array
     */
    long[] toLongArray();

    /**
     * @see #toByteArray()
     *
     * @return boolean array
     */
    boolean[] toBooleanArray();

    /**
     * @see #toByteArray()
     *
     * @return char array
     */
    char[] toCharArray();

    /**
     * @see #toByteArray()
     *
     * @return float array
     */
    float[] toFloatArray();

    /**
     * @see #toByteArray()
     *
     * @return double array
     */
    double[] toDoubleArray();

    /**
     * Return unmodifiable view or unmodifiable copy of this.
     *
     * @see Collections#unmodifiableCollection(Collection)
     *
     * @return unmodifiable collection
     */
    CollectionF<E> unmodifiable();

    /**
     * Old name for {@link #cast()}.
     *
     * @param <F> type to cast
     *
     * @return collection cast
     */
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
     *
     * @return <code>this</code>
     */
    <F> CollectionF<F> cast(Class<F> type);

    ListF<ListF<E>> paginate(int pageSize);

    /**
     * A fluent-style friendly variant of Cf.flatten().  It is an unsafe method in that
     * it requires you to specify a correct generic type.
     *
     * CollectionF&lt;CollectionF&lt;A&gt;&gt; coll = ...;
     * CollectionF&lt;ltA&gt; = coll.&lt;A&gt;flatten();
     *
     * @param <F> type to cast
     *
     * @return flatten list
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
