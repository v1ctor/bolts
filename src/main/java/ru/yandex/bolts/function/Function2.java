package ru.yandex.bolts.function;

import fj.F2;

import ru.yandex.bolts.collection.Tuple2;


/**
 * Function with two arguments.
 *
 * @see F2
 *
 * @author Stepan Koltsov
 */
public abstract class Function2<A, B, R> {
    public abstract R apply(A a, B b);

    /** Bind first param to the given value */
    public Function<B, R> bind1(final A a) {
        return new Function<B, R>() {
            public R apply(B b) {
                return Function2.this.apply(a, b);
            }

            public String toString() {
                return Function2.this + "(" + a + ", _)";
            }
        };
    }

    /** Bind second param to the given value */
    public Function<A, R> bind2(final B b) {
        return new Function<A, R>() {
            public R apply(A a) {
                return Function2.this.apply(a, b);
            }

            public String toString() {
                return Function2.this + "(_, " + b + ")";
            }
        };
    }


    public static <A, B, R> Function2<Function2<A, B, R>, A, Function<B, R>> bind1F2() {
        return new Function2<Function2<A, B, R>, A, Function<B, R>>() {
            public Function<B, R> apply(Function2<A, B, R> f, A a) {
                return f.bind1(a);
            }

            public String toString() {
                return "bind1";
            }
        };
    }

    public Function<A, Function<B, R>> bind1F() {
        return Function2.<A, B, R>bind1F2().bind1(this);
    }

    public static <A, B, R> Function2<Function2<A, B, R>, B, Function<A, R>> bind2F2() {
        return new Function2<Function2<A, B, R>, B, Function<A, R>>() {
            public Function<A, R> apply(Function2<A, B, R> f, B b) {
                return f.bind2(b);
            }

            public String toString() {
                return "bind2";
            }
        };
    }

    public Function<B, Function<A, R>> bind2F() {
        return Function2.<A, B, R>bind2F2().bind1(this);
    }


    public Function<Tuple2<A, B>, R> asFunction() {
        return new Function<Tuple2<A,B>, R>() {
            public R apply(Tuple2<A, B> t) {
                return Function2.this.apply(t.get1(), t.get2());
            }
        };
    }

    public <S> Function2<A, B, S> andThen(final Function<? super R, ? extends S> f) {
        return new Function2<A, B, S>() {
            public S apply(A a, B b) {
                return f.apply(Function2.this.apply(a, b));
            }
        };
    }

    @SuppressWarnings("unchecked")
    public <A1, B1, R1> Function2<A1, B1, R1> uncheckedCast() {
        return (Function2<A1, B1, R1>) this;
    }


} //~
