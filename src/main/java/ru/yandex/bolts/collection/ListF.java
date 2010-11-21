package ru.yandex.bolts.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.weaving.annotation.FunctionParameter;

/**
 * Extended list.
 *
 * @see CollectionF
 *
 * @author Stepan Koltsov
 * @author Iliya Roubin
 */
public interface ListF<E> extends CollectionF<E>, List<E> {
    /** Iterate list */
    IteratorF<E> iterator();

    IteratorF<E> reverseIterator();

    @Override
    ListF<E> filter(Function1B<? super E> p);

    @Override
    ListF<E> filter(Function<? super E, Boolean> p);

    @Override
    ListF<E> filterW(@FunctionParameter boolean p);

    @Override
    ListF<E> filterNotNull();

    /**
     * {@inheritDoc}
     */
    @Override
    <F extends E> ListF<F> filterByType(Class<F> type);

    /**
     * Return pair of lists, first list contains elements matching <code>p</code>
     * and second lists contains elements matching <code>!p</code>.
     */
    @Override
    Tuple2<ListF<E>, ListF<E>> partition(Function1B<? super E> p);

    @Override
    Tuple2<ListF<E>, ListF<E>> partition(Function<? super E, Boolean> p);

    @Override
    Tuple2<ListF<E>, ListF<E>> partitionW(@FunctionParameter boolean p);

    /** Sub list from index to index */
    ListF<E> subList(int fromIndex, int toIndex);

    /**
     * Zip with index.
     */
    Tuple2List<E, Integer> zipWithIndex();

    /** Concatenate two lists */
    ListF<E> plus(List<? extends E> addition);

    ListF<E> plus(Collection<? extends E> elements);

    ListF<E> plus(Iterator<? extends E> iterator);

    ListF<E> plus(E... additions);

    ListF<E> plus1(E e);

    /**
     * First element
     */
    E first() throws IndexOutOfBoundsException;

    /**
     * Last element
     */
    E last() throws IndexOutOfBoundsException;

    /**
     * Return <code>Option.some(first())</code> or <code>Option.none()</code>.
     */
    Option<E> firstO();

    /**
     * Return <code>Option.some(last())</code> or <code>Option.none()</code>.
     */
    Option<E> lastO();

    /** Task first <code>count</code> elements */
    ListF<E> take(int count);

    /** Drop first count elements */
    ListF<E> drop(int count);

    /** Longest prefix of elements that satisfy p */
    ListF<E> takeWhile(Function1B<? super E> f);

    ListF<E> takeWhile(Function<? super E, Boolean> f);

    ListF<E> takeWhileW(@FunctionParameter boolean p);

    /** Elements after {@link #takeWhile(Function1B)} */
    ListF<E> dropWhile(Function1B<? super E> f);

    ListF<E> dropWhile(Function<? super E, Boolean> f);

    ListF<E> dropWhileW(@FunctionParameter boolean p);

    /** Pair of sublists returned by {@link #takeWhile(Function1B)} and {@link #dropWhile(Function1B)} */
    //ListF<E> span(Function1B<? super E> p);

    /** Unmodifiable view or copy of this collection */
    ListF<E> unmodifiable();

    /** Alias for {@link #size()} */
    int length();

    /**
     * List with elements in reverse order
     */
    ListF<E> reverse();

    <F> ListF<F> uncheckedCast();

    /** List of pairs of elements with the same index in two lists */
    <B> Tuple2List<E, B> zip(ListF<? extends B> that);

    <B> Tuple2List<E, B> zipWith(Function<? super E, ? extends B> f);

    <B> Tuple2List<E, B> zipWithW(@FunctionParameter B f);

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
