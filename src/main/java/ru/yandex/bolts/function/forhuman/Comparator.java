package ru.yandex.bolts.function.forhuman;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2I;

/**
 * Extended {@link java.util.Comparator}.
 *
 * @author Stepan Koltsov
 */
public abstract class Comparator<A> extends Function2I<A, A> implements java.util.Comparator<A> {

    /** Call {@link #compare(Object, Object)} */
    @Override
    public final int apply(A a, A b) {
        return compare(a, b);
    }

    /** Find max among a and b */
    public A max(A a, A b) {
        if (gt(a, b)) return a;
        else return b;
    }

    /** Find min among a and b */
    public A min(A a, A b) {
        if (lt(a, b)) return a;
        else return b;
    }

    /** (f compose g)(x) = f(g(x)) */
    public <B> Comparator<B> compose(Function<B, A> mapper) {
        return mapper.andThen(this);
    }

    public Comparator<A> chainTo(final Comparator<A> comparator) {
        return new Comparator<A>() {
            public int compare(A o1, A o2) {
                int r = Comparator.this.compare(o1, o2);
                if (r != 0) return r;
                return comparator.compare(o1, o2);
            }
        };
    }

    private static int nullLowCompare(Object o1, Object o2) {
        return o1 == o2 ? 0 : o1 != null ? 1 : -1;
    }

    /** Null low comparator */
    public Comparator<A> nullLowC() {
        return new Comparator<A>() {
            public int compare(A o1, A o2) {
                if (o1 == null || o2 == null) {
                    return nullLowCompare(o1, o2);
                } else {
                    return Comparator.this.compare(o1, o2);
                }
            }

            @Override
            public Comparator<A> nullLowC() {
                return this;
            }

            public String toString() {
                return "nullLow(" + Comparator.this + ")";
            }
        };
    }

    /**
     * {@link #max(Object, Object)} as function.
     */
    public Function2<A, A, A> maxF() {
        return new Function2<A, A, A>() {
            public A apply(A a, A b) {
                return max(a, b);
            }
        };
    }

    /**
     * {@link #min(Object, Object)} as function.
     */
    public Function2<A, A, A> minF() {
        return new Function2<A, A, A>() {
            public A apply(A a, A b) {
                return min(a, b);
            }
        };
    }

    /** This but with different type parameter and no type check */
    @SuppressWarnings("unchecked")
    public <B> Comparator<B> uncheckedCastC() {
        return (Comparator<B>) this;
    }

    /** Inverted comparator */
    @Override
    public Comparator<A> invert() {
        return wrap(super.invert());
    }

    /** Wrap {@link java.util.Comparator} */
    public static <A> Comparator<A> wrap(final java.util.Comparator<A> comparator) {
        if (comparator instanceof Comparator<?>) return (Comparator<A>) comparator;
        else return new Comparator<A>() {
            public int compare(A o1, A o2) {
                return comparator.compare(o1, o2);
            }

            public String toString() {
                return comparator.toString();
            }
        };
    }

    /** Wrap {@link Function2I} */
    public static <A> Comparator<A> wrap(final Function2I<A, A> comparator) {
        if (comparator instanceof Comparator<?>) return (Comparator<A>) comparator;
        else return new Comparator<A>() {
            public int compare(A o1, A o2) {
                return comparator.apply(o1, o2);
            }

            public String toString() {
                return comparator.toString();
            }

            @Override
            public Comparator<A> invert() {
                return wrap(comparator.invert());
            }

        };
    }

    /**
     * Compare {@link java.lang.Comparable}s. Null values are less then non-null.
     */
    @SuppressWarnings("unchecked")
    public static <A extends java.lang.Comparable> Comparator<A> naturalComparator()  {
        return new Comparator<A>() {
            public int compare(A o1, A o2) {
                if (o1 == o2)
                    return 0;
                else if (o1 == null || o2 == null)
                    return nullLowCompare(o1, o2);
                else
                    //noinspection unchecked
                    return o1.compareTo(o2);
            }

            @Override
            public Comparator<A> nullLowC() {
                // naturalComparator is already null-low comparator
                return this;
            }

            public String toString() {
                return "compareTo";
            }
        };
    }

    /**
     * Always returns <code>0</code>.
     */
    public static <A> Comparator<A> constEqualComparator() {
        return new Comparator<A>() {
            public int compare(A o1, A o2) {
                return 0;
            }
        };
    }
} //~
