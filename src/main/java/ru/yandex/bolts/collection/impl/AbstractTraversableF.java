package ru.yandex.bolts.collection.impl;

import java.util.NoSuchElementException;

import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.forhuman.Comparator;


public abstract class AbstractTraversableF<E> implements TraversableF<E> {

    protected abstract IteratorF<E> iterator();

    @Override
    public boolean forAll(Function1B<? super E> p) {
        IteratorF<E> i = iterator();
        while (i.hasNext()) {
            if (!p.apply(i.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean exists(Function1B<? super E> p) {
        IteratorF<E> i = iterator();
        while (i.hasNext()) {
            if (p.apply(i.next())) return true;
        }
        return false;
    }

    @Override
    public Option<E> find(Function1B<? super E> p) {
        IteratorF<E> i = iterator();
        while (i.hasNext()) {
            E e = i.next();
            if (p.apply(e)) return Option.some(e);
        }
        return Option.none();
    }

    @Override
    public int count(Function1B<? super E> p) {
        IteratorF<E> i = iterator();
        int r = 0;
        while (i.hasNext()) {
            if (p.apply(i.next()))
                ++r;
        }
        return r;
    }

    @Override
    public E single() {
        IteratorF<E> i = iterator();
        E r = i.next();
        if (i.hasNext()) {
            throw new NoSuchElementException("more than one element");
        }
        return r;
    }

    @Override
    public Option<E> singleO() {
        IteratorF<E> i = iterator();
        Option<E> r = i.nextO();
        if (i.hasNext()) {
            throw new NoSuchElementException("more than one element");
        }
        return r;
    }

    @Override
    public <B> B foldLeft(B z, Function2<? super B, ? super E, B> f) {
        IteratorF<E> i = iterator();
        B acc = z;
        while (i.hasNext()) {
            acc = f.apply(acc, i.next());
        }
        return acc;
    }

    @Override
    public E reduceLeft(Function2<E, E, E> f) {
        return reduceLeftO(f).getOrThrow("empty.reduceLeft");
    }

    @Override
    public Option<E> reduceLeftO(Function2<E, E, E> f) {
        IteratorF<E> i = iterator();
        if (i.hasNext()) {
            return Option.some(i.foldLeft(i.next(), f));
        } else {
            return Option.none();
        }
    }

    @Override
    public E min() {
        return min(Comparator.naturalComparator().uncheckedCast());
    }

    @SuppressWarnings("unchecked")
    public E min(Function2I<? super E, ? super E> comparator) {
        return reduceLeft(Comparator.wrap((Function2I<E, E>) comparator).minF());
    }

    @Override
    public E minBy(Function<? super E, ?> f) {
        return min(f.andThenNaturalComparator().nullLowC());
    }

    @Override
    public Option<E> minO() {
        return minO(Comparator.naturalComparator().uncheckedCast());
    }

    @Override
    public Option<E> minO(Function2I<? super E, ? super E> comparator) {
        return Option.when(iterator().hasNext(), () -> min(comparator));
    }

    @Override
    public Option<E> minByO(Function<? super E, ?> f) {
        return Option.when(iterator().hasNext(), () -> minBy(f));
    }

    @Override
    public E max() {
        return max(Comparator.naturalComparator().uncheckedCast());
    }

    @SuppressWarnings("unchecked")
    @Override
    public E max(Function2I<? super E, ? super E> comparator) {
        return reduceLeft(Comparator.wrap((Function2I<E, E>) comparator).maxF());
    }

    @Override
    public E maxBy(Function<? super E, ?> f) {
        return max(f.andThenNaturalComparator().nullLowC());
    }

    @Override
    public Option<E> maxO() {
        return maxO(Comparator.naturalComparator().uncheckedCast());
    }

    @Override
    public Option<E> maxO(Function2I<? super E, ? super E> comparator) {
        return Option.when(iterator().hasNext(), () -> max(comparator));
    }

    @Override
    public Option<E> maxByO(Function<? super E, ?> f) {
        return Option.when(iterator().hasNext(), () -> maxBy(f));
    }

    @Override
    public String mkString(String sep) {
        return mkString("", sep, "");
    }

    @Override
    public String mkString(String start, String sep, String end) {
        StringBuilder sb = new StringBuilder();

        sb.append(start);

        IteratorF<E> i = iterator();

        if (i.hasNext()) {
            sb.append(i.next());
        }

        while (i.hasNext()) {
            sb.append(sep).append(i.next());
        }

        sb.append(end);

        return sb.toString();
    }



} //~
