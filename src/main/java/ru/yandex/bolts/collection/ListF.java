package ru.yandex.bolts.collection;

import java.util.List;
import java.util.Collection;
import java.util.Iterator;

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

    ListF<E> filter(Function1B<? super E> p);

    @Override
    ListF<E> filterW(@FunctionParameter boolean p);

    @Override
    ListF<E> filterNotNull();

    /**
     * Return pair of lists, first list contains elements matching <code>p</code>
     * and second lists contains elements matching <code>!p</code>.
     *
     * @deprecated
     * @see #partition(Function1B)
     */
    Tuple2<ListF<E>, ListF<E>> filter2(Function1B<? super E> p);

    /**
     * Return pair of lists, first list contains elements matching <code>p</code>
     * and second lists contains elements matching <code>!p</code>.
     */
    Tuple2<ListF<E>, ListF<E>> partition(Function1B<? super E> p);

    @Override
    Tuple2<ListF<E>, ListF<E>> partitionW(@FunctionParameter boolean p);

    /** Sub list from index to index */
    ListF<E> subList(int fromIndex, int toIndex);

    /**
     * Zip with index.
     */
    ListMap<E, Integer> zipWithIndex();

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

    ListF<E> takeWhileW(@FunctionParameter boolean p);

    /** Elements after {@link #takeWhile(Function1B)} */
    ListF<E> dropWhile(Function1B<? super E> f);

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
    <B> ListMap<E, B> zip(ListF<B> that);

    <B> ListMap<E, B> zipWith(Function<? super E, ? extends B> f);

    <B> ListMap<E, B> zipWithW(@FunctionParameter B f);
} //~
