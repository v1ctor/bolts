package ru.yandex.bolts.collection;

import java.util.Iterator;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.weaving.annotation.FunctionParameter;

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
    <B> IteratorF<B> map(Function<? super E, B> op);

    <B> IteratorF<B> mapW(@FunctionParameter B op);

    <B> IteratorF<B> flatMap(Function<? super E, ? extends Iterator<B>> f);

    <B> IteratorF<B> flatMapL(Function<? extends E, ? extends Iterable<B>> f);

    IteratorF<E> filter(Function1B<? super E> f);

    IteratorF<E> filterW(@FunctionParameter E f);

    IteratorF<E> filterNotNull();

    /** Call function for each element */
    void forEach(Function1V<? super E> f);

    /** True iff all elements match predicate */
    boolean forAll(Function1B<? super E> p);

    /** True iff element matching predicate exists */
    boolean exists(Function1B<? super E> p);

    boolean existsW(@FunctionParameter E p);

    /** Find element that matches predicate */
    Option<E> find(Function1B<? super E> p);

    Option<E> findW(@FunctionParameter E p);

    /** Fold left */
    <B> B foldLeft(B z, Function2<B, E, B> f);

    <B> B foldLeftW(B z, @FunctionParameter B f);

    /** Fold right */
    <B> B foldRight(B z, Function2<E, B, B> f);

    <B> B foldRightW(B z, @FunctionParameter B f);

    /** Reduce left */
    E reduceLeft(Function2<E, E, E> f);

    E reduceLeftW(@FunctionParameter E f);

    /** Reduce right */
    E reduceRight(Function2<E, E, E> f);

    E reduceRightW(@FunctionParameter E f);

    Option<E> reduceLeftO(Function2<E, E, E> f);

    Option<E> reduceLeftOW(@FunctionParameter E f);

    Option<E> reduceRightO(Function2<E, E, E> f);

    Option<E> reduceRightOW(@FunctionParameter E f);

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

    /** Longest prefix of elements that satisfy p */
    IteratorF<E> takeWhile(Function1B<? super E> p);

    IteratorF<E> takeWhileW(@FunctionParameter E p);

    /** Elements after {@link #takeWhile(Function1B)} */
    IteratorF<E> dropWhile(Function1B<? super E> p);

    IteratorF<E> dropWhileW(@FunctionParameter E p);

    IteratorF<ListF<E>> paginate(int pageSize);
} //~
