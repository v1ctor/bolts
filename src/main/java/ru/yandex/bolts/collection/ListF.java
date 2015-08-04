package ru.yandex.bolts.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ru.yandex.bolts.collection.impl.ArrayListF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;

/**
 * Extended list.
 *
 * @see CollectionF
 *
 * @author Stepan Koltsov
 * @author Iliya Roubin
 */
public interface ListF<E> extends CollectionF<E>, List<E> {
    /** Iterate list
     *
     * @return iterator
     * */
    IteratorF<E> iterator();

    IteratorF<E> reverseIterator();

    @Override
    ListF<E> filter(Function1B<? super E> p);

    @Override
    ListF<E> filterNot(Function1B<? super E> p);

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
     *
     * @param p function
     *
     * @return tuple
     */
    @Override
    Tuple2<ListF<E>, ListF<E>> partition(Function1B<? super E> p);

    /**
     * @see #unique()
     *
     * @return list
     */
    @Override
    ListF<E> stableUnique();

    /** Sub list from index to index
     *
     * @param fromIndex from
     * @param toIndex to
     *
     * @return sublist
     * */
    ListF<E> subList(int fromIndex, int toIndex);

    /**
     * Zip with index.
     *
     * @return tuple
     */
    Tuple2List<E, Integer> zipWithIndex();

    /** Concatenate two lists
     *
     * @param addition list to concatenate
     *
     * @return list
     * */
    ListF<E> plus(List<? extends E> addition);

    ListF<E> plus(Collection<? extends E> elements);

    ListF<E> plus(Iterator<? extends E> iterator);

    @SuppressWarnings("unchecked")
    ListF<E> plus(E... additions);

    ListF<E> plus1(E e);

    /**
     * First element
     *
     * @return first element
     */
    E first() throws IndexOutOfBoundsException;

    /**
     * Last element
     *
     * @return last element
     */
    E last() throws IndexOutOfBoundsException;

    /**
     * Return <code>Option.some(first())</code> or <code>Option.none()</code>.
     *
     * @return option first
     */
    Option<E> firstO();

    /**
     * Return <code>Option.some(last())</code> or <code>Option.none()</code>.
     *
     * @return option last
     */
    Option<E> lastO();

    /** Task first <code>count</code> elements
     *
     * @param count count
     *
     * @return list
     * */
    ListF<E> take(int count);

    /** Drop first count elements
     *
     * @param count count
     *
     * @return list
     * */
    ListF<E> drop(int count);

    ListF<E> rtake(int count);

    ListF<E> rdrop(int count);

    /** Longest prefix of elements that satisfy p
     *
     * @param f function
     *
     * @return list
     * */
    ListF<E> takeWhile(Function1B<? super E> f);

    /** Elements after {@link #takeWhile(Function1B)}
     *
     * @param f finction
     *
     * @return list
     * */
    ListF<E> dropWhile(Function1B<? super E> f);

    /** Fold right
     *
     * @param z accumulator
     * @param f function
     * @param <B> type result
     *
     * @return result
     * */
    <B> B foldRight(B z, Function2<? super E, ? super B, B> f);

    /** Reduce right
     *
     * @param f function
     *
     * @return result
     * */
    E reduceRight(Function2<E, E, E> f);

    Option<E> reduceRightO(Function2<E, E, E> f);

    /**
     * Return unmodifiable list with content of this list.
     * This list becomes invalid after method invocation.
     *
     * @see ArrayListF#convertToReadOnly()
     *
     * @return readonly list
     */
    ListF<E> makeReadOnly();

    /** Alias for {@link #size()}
     *
     * @return size
     * */
    int length();

    /**
     * List with elements in reverse order
     *
     *
     * @return reverse list
     */
    ListF<E> reverse();

    @Override
    <F> ListF<F> uncheckedCast();

    @Override
    <F> ListF<F> cast();

    @Override
    <F> ListF<F> cast(Class<F> type);

    /** List of pairs of elements with the same index in two lists
     *
     * @param <B> element
     * @param that that
     *
     * @return tuple
     * */
    <B> Tuple2List<E, B> zip(ListF<? extends B> that);

    <B> Tuple2List<E, B> zipWith(Function<? super E, ? extends B> f);

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
    boolean retainAll(Collection<?> c);
    boolean retainAllTs(Collection<? extends E> c);
    boolean retainAllTu(Collection<?> c);

    @Override
    int indexOf(Object o);
    int indexOfTs(E o);
    int indexOfTu(Object o);

} //~
