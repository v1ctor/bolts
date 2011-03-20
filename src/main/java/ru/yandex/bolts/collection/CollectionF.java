package ru.yandex.bolts.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.forhuman.Comparator;
import ru.yandex.bolts.weaving.annotation.FunctionParameter;

/**
 * Extended collection.
 *
 * Methods like {@link #filter(Function1B)}, {@link #sort()} or {@link #plus(Collection)}
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

    CollectionF<E> filter(Function<? super E, Boolean> p);

    CollectionF<E> filterW(@FunctionParameter boolean p);

    CollectionF<E> filterNotNull();

    /**
     * @see Class#isInstance(Object)
     */
    <F extends E> CollectionF<F> filterByType(Class<F> type);

    /** Pair of collection, first contains elements matching p, second contains element matching !p */
    Tuple2<? extends IterableF<E>, ? extends IterableF<E>> partition(Function1B<? super E> p);

    Tuple2<? extends IterableF<E>, ? extends IterableF<E>> partition(Function<? super E, Boolean> p);

    Tuple2<? extends IterableF<E>, ? extends IterableF<E>> partitionW(@FunctionParameter boolean p);

    /** Map */
    <B> ListF<B> map(Function<? super E, B> mapper);

    <B> ListF<B> mapW(@FunctionParameter B b);

    /** Flat map */
    <B> ListF<B> flatMap(Function<? super E, ? extends Collection<B>> f);

    /** Flat map variant that accept mapper that returns Option instead of Collection */
    <B> ListF<B> flatMapO(Function<? super E, Option<B>> f);

    <B> ListF<B> flatMapW(@FunctionParameter Collection<? extends B> f);

    /**
     * Map to list of map entries and construct map
     * @see #toMap(Function, Function)
     * @see #toTuple2List(Function)
     */
    <K, V> MapF<K, V> toMap(Function<? super E, Tuple2<K, V>> t);

    <K, V> MapF<K, V> toMapW(@FunctionParameter Tuple2<K, V> t);

    /**
     * @see #toMap(Function)
     */
    <K, V> MapF<K, V> toMap(Function<? super E, K> fk, Function<? super E, ? extends V> fv);

    <K, V> MapF<K, V> toMapW(@FunctionParameter K fk, @FunctionParameter V fv);

    /** Map to list of map keys and construct map. Elements of this collection are used as values */
    <K> MapF<K, E> toMapMappingToKey(Function<? super E, K> m);

    <K> MapF<K, E> toMapMappingToKeyW(@FunctionParameter K m);

    /** Map to list of map values and construct map. Elements of this collection are used as keys */
    <V> MapF<E, V> toMapMappingToValue(Function<? super E, V> m);

    <V> MapF<E, V> toMapMappingToValueW(@FunctionParameter V m);

    /**
     * @see #toMap(Function)
     * @see #toTuple2List(Function, Function)
     */
    <A, B> Tuple2List<A, B> toTuple2List(Function<? super E, Tuple2<A, B>> f);

    <A, B> Tuple2List<A, B> toTuple2ListW(@FunctionParameter Tuple2<A, B> f);

    /**
     * @see #toTuple2List(Function)
     * @see #toMap(Function, Function)
     */
    <A, B> Tuple2List<A, B> toTuple2List(Function<? super E, ? extends A> fa, Function<? super E, ? extends B> fb);

    <A, B> Tuple2List<A, B> toTuple2ListW(@FunctionParameter A fa, @FunctionParameter B fb);

    <A, B, C> Tuple3List<A, B, C> toTuple3List(Function<? super E, Tuple3<A, B, C>> f);

    <A, B, C> Tuple3List<A, B, C> toTuple3ListW(@FunctionParameter Tuple3<A, B, C> f);

    <A, B, C> Tuple3List<A, B, C> toTuple3List(Function<? super E, ? extends A> fa, Function<? super E, ? extends B> fb, Function<? super E, ? extends C> fc);

    <A, B, C> Tuple3List<A, B, C> toTuple3ListW(@FunctionParameter A fa, @FunctionParameter B fb, @FunctionParameter C fc);

    /**
     * @deprecated
     */
    Function1B<E> containsP();

    /** Delegate to {@link #contains(Object)} */
    Function1B<E> containsF();

    /** Convert this to list */
    ListF<E> toList();

    /** Return set of elements of this */
    SetF<E> unique();

    /** Elements sorted by {@link Comparator#naturalComparator()} */
    ListF<E> sort();

    /** Elements sorted by given comparator */
    ListF<E> sort(Function2I<? super E, ? super E> comparator);

    ListF<E> sort(Comparator<? super E> comparator);

    ListF<E> sort(java.util.Comparator<? super E> comparator);

    ListF<E> sortW(@FunctionParameter int comparator);

    ListF<E> sortBy(Function<? super E, ?> f);

    ListF<E> sortByW(@FunctionParameter Object f);

    ListF<E> sortByDesc(Function<? super E, ?> f);

    ListF<E> sortByDescW(@FunctionParameter Object f);

    /**
     * Group elements by applying given function to each element.
     */
    <V> MapF<V, ListF<E>> groupBy(Function<? super E, ? extends V> m);

    <V> MapF<V, ListF<E>> groupByW(@FunctionParameter V m);

    /** @deprecated */
    Function1V<E> addOp();

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
    CollectionF<E> plus(E... additions);

    /**
     * Varargs version of <code>addAll</code>
     *
     * @see #addAll(Collection)
     */
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
     * Single element of collection
     *
     * @throws NoSuchElementException if this collection is empty or has more then one element.
     */
    E single() throws NoSuchElementException;

    /**
     * @throws NoSuchElementException is collection has more then one element.
     */
    Option<E> singleO() throws NoSuchElementException;

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

    /**
     * Min element using {@link Comparator#naturalComparator()}.
     */
    E min();
    /**
     * Min element using given comparator.
     */
    E min(Function2I<? super E, ? super E> comparator);

    E minW(@FunctionParameter int comparator);

    Option<E> minO();

    Option<E> minO(Function2I<? super E, ? super E> comparator);

    Option<E> minOW(@FunctionParameter int comparator);

    /**
     * Max element using {@link Comparator#naturalComparator()}.
     */
    E max();
    /**
     * Max element using given comparator.
     */
    E max(Function2I<? super E, ? super E> comparator);

    E maxW(@FunctionParameter int comparator);

    Option<E> maxO();

    Option<E> maxO(Function2I<? super E, ? super E> comparator);

    Option<E> maxOW(@FunctionParameter int comparator);

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
