package ru.yandex.bolts.collection.impl;

import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.forhuman.Comparator;
import ru.yandex.bolts.weaving.annotation.FunctionParameter;

/**
 * Base for {@link IteratorF} and {@link CollectionF}. Do not use directly.
 * @author Stepan Koltsov
 */
public interface TraversableF<E> {

    /** Call function for each element */
    void forEach(Function1V<? super E> f);

    /** True iff all elements match predicate */
    boolean forAll(Function1B<? super E> p);

    boolean forAllW(@FunctionParameter boolean p);

    /** True iff element matching predicate exists */
    boolean exists(Function1B<? super E> p);

    boolean existsW(@FunctionParameter boolean p);

    /** Find element that matches predicate */
    Option<E> find(Function1B<? super E> p);

    Option<E> findW(@FunctionParameter boolean p);

    /** Count elements matching predicate */
    int count(Function1B<? super E> p);

    /**
     * Single or none element of this iterator.
     */
    Option<E> singleO();

    E single();

    /** Fold left */
    <B> B foldLeft(B z, Function2<? super B, ? super E, B> f);

    <B> B foldLeftW(B z, @FunctionParameter B f);

    /** Reduce left */
    E reduceLeft(Function2<E, E, E> f);

    E reduceLeftW(@FunctionParameter E f);

    Option<E> reduceLeftO(Function2<E, E, E> f);

    Option<E> reduceLeftOW(@FunctionParameter E f);


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

    /** Make string by joining elements with given separator */
    String mkString(String sep);

    /** Make string */
    String mkString(String start, String sep, String end);


} //~
