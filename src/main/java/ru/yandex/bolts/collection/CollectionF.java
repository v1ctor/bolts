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

    CollectionF<E> filterNotNull();

    /** Pair of collection, first contains elements matching p, second contains element matching !p */
    Tuple2<? extends IterableF<E>, ? extends IterableF<E>> partition(Function1B<? super E> p);

    Tuple2<? extends IterableF<E>, ? extends IterableF<E>> partition(Function<? super E, Boolean> p);

    /** Map */
    <B> ListF<B> map(Function<? super E, B> mapper);

    /** Flat map */
    <B> ListF<B> flatMap(Function<? super E, ? extends Collection<B>> f);

    /** Flat map variant that accept mapper that returns Option instead of Collection */
    <B> ListF<B> flatMapO(Function<? super E, Option<B>> f);

    /** Map to list of map entries and construct map */
    <K, V> MapF<K, V> toMap(Function<? super E, Tuple2<K, V>> t);

    /** Map to list of map keys and construct map. Elements of this collection are used as values */
    <K> MapF<K, E> toMapMappingToKey(Function<? super E, K> m);

    /** Map to list of map values and construct map. Elements of this collection are used as keys */
    <V> MapF<E, V> toMapMappingToValue(Function<? super E, V> m);

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

    ListF<E> sortBy(Function<? super E, ?> f);

    ListF<E> sortByDesc(Function<? super E, ?> f);

    /**
     * Group elements by applying given function to each element.
     */
    <V> MapF<V, ListF<E>> groupBy(Function<? super E, ? extends V> m);

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
     * Return <code>this</code> with another type parameter and no type checks.
     */
    <F> CollectionF<F> uncheckedCast();

    /**
     * Min element using {@link Comparator#naturalComparator()}.
     */
    E min();
    /**
     * Min element using given comparator.
     */
    E min(Function2I<? super E, ? super E> comparator);

    /**
     * Max element using {@link Comparator#naturalComparator()}.
     */
    E max();
    /**
     * Max element using given comparator.
     */
    E max(Function2I<? super E, ? super E> comparator);

    CollectionF<E> tee(Function1V<? super CollectionF<E>> f);

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
