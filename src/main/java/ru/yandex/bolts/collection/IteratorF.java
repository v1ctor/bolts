package ru.yandex.bolts.collection;

import java.util.Iterator;

import ru.yandex.bolts.collection.impl.TraversableF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.weaving.annotation.FunctionParameter;

/**
 * Extended iterator.
 *
 * @see IterableF
 * @author Stepan Koltsov
 */
public interface IteratorF<E> extends TraversableF<E>, Iterator<E> {
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

    int count();

    /** Zip with index */
    IteratorF<Tuple2<E, Integer>> zipWithIndex();

    IteratorF<E> plus(Iterator<E> i);

    IteratorF<E> unmodifiable();

    Option<E> nextO();

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
