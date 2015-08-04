package ru.yandex.bolts.collection;

import java.util.Iterator;

import ru.yandex.bolts.collection.impl.TraversableF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;

/**
 * Extended iterator.
 *
 * @see IterableF
 * @author Stepan Koltsov
 */
public interface IteratorF<E> extends TraversableF<E>, Iterator<E> {

    default void forEach(Function1V<? super E> closure) {
        forEachRemaining(closure);
    }

    /** Collect elements
     *
     * @return list
     * */
    ListF<E> toList();

    ListF<E> toList(int initialCapacity);

    /** Collect elements to set
     *
     * @return set
     * */
    SetF<E> toSet();

    /** Map
     *
     * @param op function
     * @param <B> element
     *
     * @return iterator
     * */
    <B> IteratorF<B> map(Function<? super E, B> op);

    <B> IteratorF<B> flatMap(Function<? super E, ? extends Iterator<B>> f);

    <B> IteratorF<B> flatMapL(Function<? super E, ? extends Iterable<B>> f);

    <B> IteratorF<B> flatMapO(Function<? super E, Option<B>> f);

    IteratorF<E> filter(Function1B<? super E> f);

    IteratorF<E> filterNot(Function1B<? super E> f);

    IteratorF<E> filterNotNull();

    int count();

    /** Zip with index
     *
     * @return iterator
     * */
    IteratorF<Tuple2<E, Integer>> zipWithIndex();

    IteratorF<E> plus(Iterator<E> i);

    IteratorF<E> unmodifiable();

    Option<E> nextO();

    IteratorF<E> drop(int count);

    IteratorF<E> take(int count);

    /**
     * First k elements sorted by {@link ru.yandex.bolts.function.forhuman.Comparator#naturalComparator()}
     * Equals toList().sorted().take(count)
     *
     * @param count count
     *
     * @return list
     */
    ListF<E> takeSorted(int count);

    ListF<E> takeSortedDesc(int count);

    /** First k elements sorted by given comparator
     *
     * @param comparator comparator
     * @param count count
     *
     * @return list
     * */
    ListF<E> takeSorted(java.util.Comparator<? super E> comparator, int count);

    ListF<E> takeSortedBy(Function<? super E, ?> f, int count);

    ListF<E> takeSortedByDesc(Function<? super E, ?> f, int count);

    /** Longest prefix of elements that satisfy p
     *
     * @param p function
     *
     * @return iterator
     * */
    IteratorF<E> takeWhile(Function1B<? super E> p);

    /** Elements after {@link #takeWhile(Function1B)}
     *
     * @param p function
     *
     * @return iterator
     * */
    IteratorF<E> dropWhile(Function1B<? super E> p);

    IteratorF<ListF<E>> paginate(int pageSize);
} //~
