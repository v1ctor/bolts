package ru.yandex.bolts.function.forhuman;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2I;

/**
 * Extended {@link java.util.Comparator}.
 *
 * @author Stepan Koltsov
 */
@FunctionalInterface
public interface Comparator<A> extends Function2I<A, A>, java.util.Comparator<A> {

    /** Call {@link #compare(Object, Object)} */
    @Override
    default int apply(A a, A b) {
        return compare(a, b);
    }

    /** Find max among a and b */
    default A max(A a, A b) {
        if (gt(a, b)) return a;
        else return b;
    }

    /** Find min among a and b */
    default A min(A a, A b) {
        if (lt(a, b)) return a;
        else return b;
    }

    /** (f compose g)(x) = f(g(x)) */
    default <B> Comparator<B> compose(Function<B, A> mapper) {
        return mapper.andThen(this);
    }

    /**
     * Call specified comparator if object are equal by this comparator.
     */
    default Comparator<A> chainTo(final Comparator<A> comparator) {
        return (o1, o2) -> {
            int r = Comparator.this.compare(o1, o2);
            if (r != 0) return r;
            return comparator.compare(o1, o2);
        };
    }

    /** Null low comparator */
    default Comparator<A> nullLowC() {
        return (o1, o2) -> {
            if (o1 == null || o2 == null) {
                return o1 == o2 ? 0 : o1 != null ? 1 : -1;
            } else {
                return Comparator.this.compare(o1, o2);
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

    /**
     * {@link #max(Object, Object)} as function.
     */
    default Function2<A, A, A> maxF() {
        return this::max;
    }

    /**
     * {@link #min(Object, Object)} as function.
     */
    default Function2<A, A, A> minF() {
        return this::min;
    }

    /** This but with different type parameter and no type check */
    @SuppressWarnings("unchecked")
    default <B> Comparator<B> uncheckedCastC() {
        return (Comparator<B>) this;
    }

    /** Inverted comparator */
    @Override
    default Comparator<A> invert() {
        return wrap(Function2I.super.invert());
    }

    /** Wrap {@link java.util.Comparator} */
    static <A> Comparator<A> wrap(final java.util.Comparator<A> comparator) {
        if (comparator instanceof Comparator<?>) return (Comparator<A>) comparator;
        else return comparator::compare;
    }

    /** Wrap {@link Function2I} */
    static <A> Comparator<A> wrap(final Function2I<A, A> comparator) {
        if (comparator instanceof Comparator<?>) return (Comparator<A>) comparator;
        else return comparator::apply;
    }

    /**
     * Compare {@link java.lang.Comparable}s. Null values are less than non-null.
     */
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

    /**
     * Always returns <code>0</code>.
     */
    static <A> Comparator<A> constEqualComparator() {
        return (o1, o2) -> 0;
    }
} //~
