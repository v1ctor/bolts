package ru.yandex.bolts.type.collection;

import java.util.Collection;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2B;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.forhuman.Comparator;


/**
 * @author Stepan Koltsov
 */
public abstract class AnyCollectionType {

    public <F, T> Function2<Collection<F>, Function<? super F, T>, ListF<T>> mapF() {
        return new Function2<Collection<F>, Function<? super F, T>, ListF<T>>() {
            public ListF<T> apply(Collection<F> input, Function<? super F, T> transform) {
                return Cf.x(input).map(transform);
            }
        };
    }

    public <F, T> Function<Collection<F>, ListF<T>> mapF(
            Function<? super F, T> transform)
    {
        return this.<F, T>mapF().bind2(transform);
    }

    public <F> Function2<Collection<F>, Function1B<? super F>, Option<F>> findF() {
        return new Function2<Collection<F>, Function1B<? super F>, Option<F>>() {
            public Option<F> apply(Collection<F> c, Function1B<? super F> f) {
                return Cf.x(c).find(f);
            }
        };
    }

    public <F> Function<Collection<F>, Option<F>> findF(
            Function1B<? super F> f)
    {
        return this.<F>findF().bind2(f);
    }

    public Function<Collection<?>, Integer> sizeF() {
        return new Function<Collection<?>, Integer>() {
            public Integer apply(Collection<?> c) {
                return c.size();
            }
        };
    }

    /** {@link CollectionF#single()} as function */
    public <A> Function<Collection<A>, A> singleF() {
        return new Function<Collection<A>, A>() {
            public A apply(Collection<A> c) {
                return Cf.x(c).single();
            }
        };
    }

    /** {@link CollectionF#singleO()} as function */
    public <A> Function<Collection<A>, Option<A>> singleOF() {
        return new Function<Collection<A>, Option<A>>() {
            public Option<A> apply(Collection<A> c) {
                return Cf.x(c).singleO();
            }
        };
    }

    /** {@link CollectionF#sort()} as function */
    public <E> Function2<Collection<E>, Function2I<? super E, ? super E>, ListF<E>> sortF() {
        return new Function2<Collection<E>, Function2I<? super E, ? super E>, ListF<E>>() {
            public ListF<E> apply(Collection<E> input,
                    Function2I<? super E, ? super E> comparator)
            {
                return Cf.x(input).sort(comparator);
            }
        };
    }

    /** {@link CollectionF#sort()} as function, convenience form */
    public <E> Function<Collection<E>, ListF<E>> sortF(
            Function2I<? super E, ? super E> comparator)
    {
        return this.<E>sortF().bind2(comparator);

    }

    public <E extends Comparable<?>> Function<Collection<E>, ListF<E>> sortComparablesF() {
        return this.<E>sortF().bind2(Comparator.<E>naturalComparator());
    }

    public <E> Function<Collection<E>, ListF<E>> sortByF(Function<? super E, ?> by) {
        return this.<E>sortF(by.andThenNaturalComparator());
    }

    public <E> Function2B<Collection<E>, E> containsF() {
        return new Function2B<Collection<E>, E>() {
            public boolean apply(Collection<E> collection, E element) {
                return collection.contains(element);
            }
        };
    }

} //~
