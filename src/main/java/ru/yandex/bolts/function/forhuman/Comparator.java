package ru.yandex.bolts.function.forhuman;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2I;


@FunctionalInterface
public interface Comparator<A> extends Function2I<A, A>, java.util.Comparator<A> {


    @Override
    default int apply(A a, A b) {
        return compare(a, b);
    }


    default A max(A a, A b) {
        if (gt(a, b)) return a;
        else return b;
    }


    default A min(A a, A b) {
        if (lt(a, b)) return a;
        else return b;
    }


    default <B> Comparator<B> compose(Function<B, A> mapper) {
        return mapper.andThen(this);
    }


    default Comparator<A> chainTo(final Comparator<A> comparator) {
        return (o1, o2) -> {
            int r = compare(o1, o2);
            if (r != 0) return r;
            return comparator.compare(o1, o2);
        };
    }


    default Comparator<A> nullLowC() {
        return (o1, o2) -> {
            if (o1 == null || o2 == null) {
                return o1 == o2 ? 0 : o1 != null ? 1 : -1;
            } else {
                return compare(o1, o2);
            }
        };
    }

    static <A> Comparator<A> valueLowC(final A low) {
        return (o1, o2) -> {
            if (low == null) {
                return o1 == o2 ? 0 : o1 != null ? 1 : -1;
            }
            if (Cf.Object.equals(o1, o2))
                return 0;
            if (o1 != null && low.equals(o1))
                return -1;
            if (o2 != null && low.equals(o2))
                return 1;
            return 0;
        };
    }

    default Comparator<A> chainToValueLowC(A low) {
        return chainTo(valueLowC(low));
    }


    default Function2<A, A, A> maxF() {
        return this::max;
    }


    default Function2<A, A, A> minF() {
        return this::min;
    }


    @SuppressWarnings("unchecked")
    default <B> Comparator<B> uncheckedCastC() {
        return (Comparator<B>) this;
    }


    @Override
    default Comparator<A> invert() {
        return wrap(Function2I.super.invert());
    }


    static <A> Comparator<A> wrap(final java.util.Comparator<A> comparator) {
        if (comparator instanceof Comparator<?>) return (Comparator<A>) comparator;
        else return comparator::compare;
    }


    static <A> Comparator<A> wrap(final Function2I<A, A> comparator) {
        if (comparator instanceof Comparator<?>) return (Comparator<A>) comparator;
        else return comparator::apply;
    }


    @SuppressWarnings("unchecked")
    static <A extends java.lang.Comparable<?>> Comparator<A> naturalComparator()  {
        return (o1, o2) -> {
            if (o1 == o2) {
                return 0;
            } else if (o1 == null || o2 == null) {
                return o1 == o2 ? 0 : o1 != null ? 1 : -1;
            } else {
                //noinspection unchecked
                return ((Comparable<Object>) o1).compareTo(o2);
            }
        };
    }

    static <A> Comparator<A> constEqualComparator() {
        return (o1, o2) -> 0;
    }
} //~
