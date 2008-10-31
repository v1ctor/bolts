package ru.yandex.bolts.collection;

import java.util.Iterator;

import ru.yandex.bolts.function.Function1;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function1V;

/**
 * Extended iterator.
 *
 * @see IterableF
 * @author Stepan Koltsov
 */
public interface IteratorF<E> extends Iterator<E> {
    /** Collect elements */
    ListF<E> toList();

    /** Collect elements to set */
    SetF<E> toSet();

    /** Map */
    <B> IteratorF<B> map(Function1<? super E, B> op);
    
    <B> IteratorF<B> flatMap(Function1<? super E, ? extends Iterator<B>> f);
    
    IteratorF<E> filter(Function1B<? super E> f);

    /** Call function for each element */
    void forEach(Function1V<? super E> f);

    /** True iff all elements match predicate */
    boolean forAll(Function1B<? super E> p);

    /** True iff element matching predicate exists */
    boolean exists(Function1B<? super E> p);

    /** Find element that matches predicate */
    Option<E> find(Function1B<E> p);

    /** Fold left */
    <B> B foldLeft(B z, Function2<B, E, B> f);

    /** Fold right */
    <B> B foldRight(B z, Function2<E, B, B> f);

    /** Reduce left */
    E reduceLeft(Function2<E, E, E> f);

    /** Reduce right */
    E reduceRight(Function2<E, E, E> f);

    /** Zip with index */
    IteratorF<Tuple2<E, Integer>> zipWithIndex();

    IteratorF<E> plus(Iterator<E> i);

    IteratorF<E> unmodifiable();

    Option<E> nextO();

    /**
     * Single or none element of this iterator.
     */
    Option<E> singleO();
    
    IteratorF<E> drop(int count);
    
    IteratorF<E> take(int count);
} //~
