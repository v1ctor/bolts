package ru.yandex.bolts.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ru.yandex.bolts.collection.impl.ArrayListF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;


public interface ListF<E> extends CollectionF<E>, List<E> {

    IteratorF<E> iterator();

    IteratorF<E> reverseIterator();

    @Override
    ListF<E> filter(Function1B<? super E> p);

    @Override
    ListF<E> filterNot(Function1B<? super E> p);

    @Override
    ListF<E> filterNotNull();


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


    @Override
    ListF<E> stableUnique();


    ListF<E> subList(int fromIndex, int toIndex);


    Tuple2List<E, Integer> zipWithIndex();


    ListF<E> plus(List<? extends E> addition);

    ListF<E> plus(Collection<? extends E> elements);

    ListF<E> plus(Iterator<? extends E> iterator);

    @SuppressWarnings("unchecked")
    ListF<E> plus(E... additions);

    ListF<E> plus1(E e);


    E first() throws IndexOutOfBoundsException;


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


    ListF<E> drop(int count);

    ListF<E> rtake(int count);

    ListF<E> rdrop(int count);


    ListF<E> takeWhile(Function1B<? super E> f);


    ListF<E> dropWhile(Function1B<? super E> f);


    <B> B foldRight(B z, Function2<? super E, ? super B, B> f);


    E reduceRight(Function2<E, E, E> f);

    Option<E> reduceRightO(Function2<E, E, E> f);


    ListF<E> makeReadOnly();


    int length();


    ListF<E> reverse();

    @Override
    <F> ListF<F> uncheckedCast();

    @Override
    <F> ListF<F> cast();

    @Override
    <F> ListF<F> cast(Class<F> type);


    <B> Tuple2List<E, B> zip(ListF<? extends B> that);

    <B> Tuple2List<E, B> zipWith(Function<? super E, ? extends B> f);


    @Override
    boolean remove(Object o);
    boolean removeTs(E e);
    boolean removeTu(Object e);


    @Override
    boolean removeAll(Collection<?> c);
    boolean removeAllTs(Collection<? extends E> c);
    boolean removeAllTu(Collection<?> c);


    @Override
    boolean contains(Object o);
    boolean containsTs(E e);
    boolean containsTu(Object e);


    @Override
    boolean containsAll(Collection<?> coll);
    boolean containsAllTs(Collection<? extends E> coll);
    boolean containsAllTu(Collection<?> coll);


    @Override
    boolean retainAll(Collection<?> c);
    boolean retainAllTs(Collection<? extends E> c);
    boolean retainAllTu(Collection<?> c);

    @Override
    int indexOf(Object o);
    int indexOfTs(E o);
    int indexOfTu(Object o);

} //~
