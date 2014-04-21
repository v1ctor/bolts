package ru.yandex.bolts.type.collection;

import java.util.Collection;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.SetF;
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
        return (input, transform) -> Cf.x(input).map(transform);
    }

    public <F, T> Function<Collection<F>, ListF<T>> mapF(
            Function<? super F, T> transform)
    {
        return this.<F, T>mapF().bind2(transform);
    }

    public <F> Function2<Collection<F>, Function1B<? super F>, Option<F>> findF() {
        return (c, f) -> Cf.x(c).find(f);
    }

    public <F> Function<Collection<F>, Option<F>> findF(
            Function1B<? super F> f)
    {
        return this.<F>findF().bind2(f);
    }

    public <E> Function<Collection<E>, E> maxF(final Function2I<? super E, ? super E> f) {
        return collection -> Cf.x(collection).max(f);
    }

    public <E> Function<Collection<E>, E> maxF() {
        return this.<E>maxF(Comparator.naturalComparator().<E, E>uncheckedCast());
    }

    public <E> Function<Collection<E>, E> minF(final Function2I<? super E, ? super E> f) {
        return collection -> Cf.x(collection).min(f);
    }

    public <E> Function<Collection<E>, E> minF() {
        return this.<E>minF(Comparator.naturalComparator().<E, E>uncheckedCast());
    }

    public Function<Collection<?>, Integer> sizeF() {
        return Collection::size;
    }

    public Function1B<Collection<?>> isEmptyF() {
        return Collection::isEmpty;
    }

    public Function1B<Collection<?>> isNotEmptyF() {
        return c -> !c.isEmpty();
    }

    /** {@link CollectionF#single()} as function */
    public <A> Function<Collection<A>, A> singleF() {
        return c -> Cf.x(c).single();
    }

    /** {@link CollectionF#singleO()} as function */
    public <A> Function<Collection<A>, Option<A>> singleOF() {
        return c -> Cf.x(c).singleO();
    }

    /** {@link CollectionF#sorted()} as function */
    public <E> Function2<Collection<E>, Comparator<? super E>, ListF<E>> sortedF() {
        return (input, comparator) -> Cf.x(input).sorted(comparator);
    }

    public <E> Function<Collection<E>, SetF<E>> uniqueF() {
        return c -> Cf.x(c).unique();
    }

    public <E> Function<Collection<E>, ListF<E>> toListF() {
        return c -> Cf.x(c).toList();
    }

    /** {@link CollectionF#sorted()} as function, convenience form */
    public <E> Function<Collection<E>, ListF<E>> sortedF(
            Comparator<? super E> comparator)
    {
        return this.<E>sortedF().bind2(comparator);

    }

    public <E extends Comparable<?>> Function<Collection<E>, ListF<E>> sortComparablesF() {
        return this.<E>sortedF().bind2(Comparator.<E>naturalComparator());
    }

    public <E> Function<Collection<E>, ListF<E>> sortedByF(Function<? super E, ?> by) {
        return this.<E>sortedF(by.andThenNaturalComparator());
    }

    public <E> Function2B<Collection<E>, E> containsF() {
        return Collection::contains;
    }

} //~
