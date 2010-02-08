package ru.yandex.bolts.function;



/**
 * @author Stepan Koltsov
 */
public abstract class Function1V<A> {

    public abstract void apply(A a);

    /** (f compose g)(x) = f(g(x)) */
    public <B> Function1V<B> compose(final Function<B, A> g) {
        return g.andThen(this);
    }

    public Function0V bind(final A param) {
        return new Function0V() {
            public void apply() {
                Function1V.this.apply(param);
            }
        };
    }

    public <R> Function<A, R> asFunctionReturn(final Function0<R> rv) {
        if (rv == null) throw new IllegalArgumentException("rv constructor must not be null");
        return new Function<A, R>() {
            public R apply(A a) {
                Function1V.this.apply(a);
                return rv.apply();
            }
        };
    }

    public <R> Function<A, R> asFunctionReturnValue(R rv) {
        return asFunctionReturn(Function0.constF(rv));
    }

    public <B> Function<A, B> asFunctionReturnNull() {
        return asFunctionReturnValue(null);
    }

    @SuppressWarnings("unchecked")
    public <B> Function1V<B> uncheckedCast() {
        return (Function1V<B>) this;
    }

    public static <A> Function1V<A> nop() {
        return new Function1V<A>() {
            public void apply(A a) {
            }

            public String toString() {
                return "nop";
            }
        };
    }

} //~
