package ru.yandex.bolts.collection.impl;

import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * Base for {@link IteratorF} and {@link CollectionF}. Do not use directly.
 * @author Stepan Koltsov
 */
public interface TraversableF<E> {

    /** True iff all elements match predicate
     *
     * @param p function
     *
     * @return boolean
     * */
    boolean forAll(Function1B<? super E> p);

    /** True iff element matching predicate exists
     *
     *
     * @param p function
     *
     * @return boolean
     * */
    boolean exists(Function1B<? super E> p);

    /** Find element that matches predicate
     *
     * @param p function
     *
     * @return option
     * */
    Option<E> find(Function1B<? super E> p);

    /** Count elements matching predicate
     *
     *
     * @param p function
     *
     * @return amount
     * */
    int count(Function1B<? super E> p);

    /**
     * Single or none element of this iterator.
     *
     * @return option
     */
    Option<E> singleO();

    E single();

    /** Fold left
     *
     * @param z accumulator
     * @param f function
     * @param <B> result type
     *
     * @return result
     * */
    <B> B foldLeft(B z, Function2<? super B, ? super E, B> f);

    /** Reduce left
     *
     * @param f function
     *
     * @return result
     * */
    E reduceLeft(Function2<E, E, E> f);

    Option<E> reduceLeftO(Function2<E, E, E> f);


    /**
     * Min element using {@link Comparator#naturalComparator()}.
     *
     * @return minimum
     */
    E min();

    /**
     * Min element using given comparator.
     *
     * @param comparator comparator
     *
     * @return min
     */
    E min(Function2I<? super E, ? super E> comparator);

    E minBy(Function<? super E, ?> f);

    Option<E> minO();

    Option<E> minO(Function2I<? super E, ? super E> comparator);

    Option<E> minByO(Function<? super E, ?> f);

    /**
     * Max element using {@link Comparator#naturalComparator()}.
     *
     * @return max
     */
    E max();

    /**
     * Max element using given comparator.
     *
     * @param comparator comparator
     *
     * @return max
     */
    E max(Function2I<? super E, ? super E> comparator);

    E maxBy(Function<? super E, ?> f);

    Option<E> maxO();

    Option<E> maxO(Function2I<? super E, ? super E> comparator);

    Option<E> maxByO(Function<? super E, ?> f);

    /** Make string by joining elements with given separator
     *
     * @param sep separator
     *
     * @return result string
     * */
    String mkString(String sep);

    /** Make string
     *
     * @param start start
     * @param sep separator
     * @param end end
     *
     * @return result string
     * */
    String mkString(String start, String sep, String end);


} //~
