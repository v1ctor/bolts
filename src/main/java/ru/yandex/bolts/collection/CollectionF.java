package ru.yandex.bolts.collection;

import java.util.Collection;
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
    IteratorF<E> iterator();

    /**
     * Return collection with only elements that match predicate.
     */
    CollectionF<E> filter(Function1B<? super E> p);
    
    /** Pair of collection, first contains elements matching p, second contains element matching !p */
    Tuple2<? extends IterableF<E>, ? extends IterableF<E>> filter2(Function1B<? super E> p);

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
    
    /** Delegate of {@link #contains(Object)} */
    Function1B<E> containsF();

    /** Convert this to list */
    ListF<E> toList();

    /** Return set of elements of this */
    SetF<E> unique();

    /** Elements sorted by {@link Comparator#naturalComparator()} */
    ListF<E> sort();

    /** Elements sorted by given comparator */
    ListF<E> sort(Function2I<? super E, ? super E> comparator);

    ListF<E> sortBy(Function<? super E, ?> f);
    
    ListF<E> sortByDesc(Function<? super E, ?> f);
    
    <V> MapF<V, ListF<E>> groupBy(Function<? super E, ? extends V> m);

    /** Add element to this collection */
    Function1V<E> addOp();

    CollectionF<E> plus1(E e);

    CollectionF<E> plus(Collection<? extends E> elements);

    CollectionF<E> plus(Iterator<? extends E> iterator);

    CollectionF<E> plus(E... additions);

    void addAll(E... additions);
    
    /** Copy elements to the new array */
    E[] toArray(Class<E> cl);
    
    byte[] toByteArray();
    short[] toShortArray();
    int[] toIntArray();
    long[] toLongArray();
    boolean[] toBooleanArray();
    char[] toCharArray();
    float[] toFloatArray();
    double[] toDoubleArray();

    /**
     * Return unmodifiable view or unmodifiable copy of this.
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
    
    <F> CollectionF<F> uncheckedCast();

    /** Not implemented yet */
    interface Projection<E> extends CollectionF<E>, IterableF.Projection<E> {
        CollectionF<E> force();
    }

    Projection<E> projection();

    E min();
    E min(Function2I<? super E, ? super E> comparator);
    E max();
    E max(Function2I<? super E, ? super E> comparator);
} //~
