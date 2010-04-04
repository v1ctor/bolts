package ru.yandex.bolts.collection;

import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2;

/**
 * Extended iterable.
 *
 * @author Stepan Koltsov
 */
public interface IterableF<E> extends Iterable<E> {
    /** Elements as iterator */
    IteratorF<E> iterator();

    /** Execute function for each element */
    void forEach(Function1V<? super E> f);

    /** Return true iff predicate matches all elements */
    boolean forAll(Function1B<? super E> p);

    /** Return true iff element matching predicate exists */
    boolean exists(Function1B<? super E> p);

    /** Find element matching predicate */
    Option<E> find(Function1B<? super E> p);

    /**
     * Fold left. Compute f(f(...f(f(z, e_1), e_2) ..., e_n-1), e_n)
     */
    <B> B foldLeft(B z, Function2<B, E, B> f);

    <B> B foldLeftW(B z, B f);

    /**
     * Fold right. Compute f(e_1, f(e_2, ... f(e_n-1, f(e_n, z)) ...))
     */
    <B> B foldRight(B z, Function2<E, B, B> f);

    <B> B foldRightW(B z, B f);

    /**
     * Reduce left. Compute f(f(...f(f(f(e_1, e_2), e_3), e_4) ..., e_n-1), e_n)
     */
    E reduceLeft(Function2<E, E, E> f);

    E reduceLeftW(E f);

    /** Reduce right. Compute f(e_1, f(e_2, ... f(e_n-2, f(e_n-1, e_n)) ...)) */
    E reduceRight(Function2<E, E, E> f);

    E reduceRightW(E f);

    /** Make string by joining elements with given separator */
    String mkString(String sep);

    /** Make string */
    String mkString(String start, String sep, String end);

    <F> IterableF<F> uncheckedCast();
} //~
