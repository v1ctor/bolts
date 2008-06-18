package ru.yandex.bolts.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ru.yandex.bolts.function.Function1;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.forhuman.Comparator;
import ru.yandex.bolts.function.forhuman.Operation;
import ru.yandex.bolts.function.forhuman.Predicate;

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
    <B> ListF<B> map(Function1<? super E, B> mapper);

    /** Flat map */
    <B> ListF<B> flatMap(Function1<? super E, ? extends Collection<B>> f);

    /** Flat map variant that accept mapper that returns Option instead of Collection */
    <B> ListF<B> flatMapO(Function1<? super E, Option<B>> f);
    
    /** Map to list of map entries and construct map */
    <K, V> MapF<K, V> toMap(Function1<? super E, Tuple2<K, V>> t);

    /** Map to list of map keys and construct map. Elements of this collection are used as values */
    <K> MapF<K, E> toMapMappingToKey(Function1<? super E, K> m);

    /** Map to list of map values and construct map. Elements of this collection are used as keys */
    <V> MapF<E, V> toMapMappingToValue(Function1<? super E, V> m);

    /** Predicate whether object contained in this */
    Predicate<E> containsP();

    /** Convert this to list */
    ListF<E> toList();

    /** Return set of elements of this */
    SetF<E> unique();

    /** Elements sorted by {@link Comparator#naturalComparator()} */
    ListF<E> sort();

    /** Elements sorted by given comparator */
    ListF<E> sort(Function2I<? super E, ? super E> comparator);

    ListF<E> sortBy(Function1<? super E, ?> f);
    
    ListF<E> sortByDesc(Function1<? super E, ?> f);
    
    <V> MapF<V, ListF<E>> groupBy(Function1<? super E, ? extends V> m);

    /** Add element to this collection */
    Operation<E> addOp();

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

} //~
