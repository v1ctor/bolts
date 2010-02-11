package ru.yandex.bolts.function;

import org.apache.commons.collections.Transformer;

import fj.F;

import ru.yandex.bolts.function.forhuman.Comparator;





/**
 * Function
 *
 * @see F
 * @see Transformer
 *
 * @author Stepan Koltsov
 */
public abstract class Function<A, R> {

    public abstract R apply(A a);

    /**
     * (f andThen g)(x) = g(f(x))
     *
     * @see fj.Function#andThen(F, F)
     */
    public <C> Function<A, C> andThen(final Function<? super R, ? extends C> g) {
        return new Function<A, C>() {
            public C apply(A a) {
                return g.apply(Function.this.apply(a));
            }

            public String toString() {
                return g + "(" + Function.this + ")";
            }
        };
    }

    public Function1V<A> andThen(final Function1V<? super R> g) {
        return new Function1V<A>() {
            public void apply(A a) {
                g.apply(Function.this.apply(a));
            }

            public String toString() {
                return g + "(" + Function.this + ")";
            }
        };
    }

    public Function1B<A> andThen(final Function1B<? super R> g) {
        return new Function1B<A>() {
            public boolean apply(A a) {
                return g.apply(Function.this.apply(a));
            }

            public String toString() {
                return g + "(" + Function.this + ")";
            }
        };
    }

    /** Not true function composition */
    public Comparator<A> andThen(final Comparator<R> comparator) {
        return new Comparator<A>() {
            public int compare(A o1, A o2) {
                return comparator.compare(Function.this.apply(o1), Function.this.apply(o2));
            }

            @Override
            public String toString() {
                return comparator + "(" + Function.this + "(_), " + Function.this + "(_))";
            }
        };
    }

    /** (f andThen g)(x) = g(f(x)) */
    public Comparator<A> andThen(final Function2I<R, R> comparator) {
        return new Comparator<A>() {
            public int compare(A a, A b) {
                return comparator.apply(Function.this.apply(a), Function.this.apply(b));
            }

            public String toString() {
                return comparator + "(" + Function.this + ")";
            }
        };
    }

    public Function1B<A> andThenEquals(R value) {
        return andThen(Function1B.equalsF(value));
    }

    /** And then null low natural comparator */
    @SuppressWarnings({"unchecked"})
    public Comparator<A> andThenNaturalComparator() {
        return andThen((Comparator<R>) Comparator.naturalComparator());
    }

    /**
     * (f compose g)(x) = f(g(x))
     *
     * @see fj.Function#compose(F, F)
     */
    public <C> Function<C, R> compose(Function<C, A> g) {
        return g.andThen(this);
    }

    public Function0<R> bind(final A param) {
        return new Function0<R>() {
            public R apply() {
                return Function.this.apply(param);
            }

            public String toString() {
                return Function.this.toString() + "(" + param + ")";
            }

        };
    }

    @SuppressWarnings("unchecked")
    public <B, S> Function<B, S> uncheckedCast() {
        return (Function<B, S>) this;
    }

    /** Ignore result of mapping */
    public Function1V<A> ignoreResult() {
        return new Function1V<A>() {
            public void apply(A a) {
                Function.this.apply(a);
            }

            public String toString() {
                return "unit(" + Function.this + ")";
            }
        };
    }

    /** Map null to null */
    public Function<A, R> ignoreNullF() {
        return new Function<A, R>() {
            public R apply(A a) {
                if (a == null) return null;
                else return Function.this.apply(a);
            }

            public String toString() {
                return "ignoreNull(" + Function.this + ")";
            }
        };
    }

    public static <A> Function<A, A> identityF() {
        return new Function<A, A>() {
            public A apply(A a) {
                return a;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <C> Function<A, C> andThen(Function<? super A, ? extends C> g) {
                return (Function<A, C>) g;
            }

            @SuppressWarnings("unchecked")
            @Override
            public Function1B<A> andThen(Function1B<? super A> predicate) {
                return (Function1B<A>) predicate;
            }

            @Override
            public <C> Function<C, A> compose(Function<C, A> g) {
                return g;
            }

            public String toString() {
                return "identity";
            }
        };
    }

    public static <T> Function<T, String> toStringF() {
        return new Function<T, String>() {
            public String apply(T t) {
                return t != null ? t.toString() : "null";
            }

            public String toString() {
                return "toString";
            }
        };
    }

    /** Function that always returns the same value */
    public static <A, B> Function<A, B> constF(final B b) {
        return new Function<A, B>() {
            public B apply(A a) {
                return b;
            }

            public String toString() {
                return "const " + b;
            }
        };
    }

} //~
