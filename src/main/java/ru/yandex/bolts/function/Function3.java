package ru.yandex.bolts.function;

// this file is generated by ru.yandex.bolts.function.gen.GenerateFunctions

import ru.yandex.bolts.collection.Tuple3;

/**
 * @see fj.F3
 */
public abstract class Function3<A, B, C, R> {

    public abstract R apply(A a, B b, C c);

    public Function2<B, C, R> bind1(final A a) {
        return new Function2<B, C, R>() {
            public R apply(B b, C c) {
                return Function3.this.apply(a, b, c);
            }

            public String toString() {
                return Function3.this + "(" + a + ", _, _)";
            }
        };
    }

    public Function2<A, C, R> bind2(final B b) {
        return new Function2<A, C, R>() {
            public R apply(A a, C c) {
                return Function3.this.apply(a, b, c);
            }

            public String toString() {
                return Function3.this + "(_, " + b + ", _)";
            }
        };
    }

    public Function2<A, B, R> bind3(final C c) {
        return new Function2<A, B, R>() {
            public R apply(A a, B b) {
                return Function3.this.apply(a, b, c);
            }

            public String toString() {
                return Function3.this + "(_, _, " + c + ")";
            }
        };
    }


    public static <A, B, C, R> Function2<Function3<A, B, C, R>, A, Function2<B, C, R>> bind1F2() {
        return new Function2<Function3<A, B, C, R>, A, Function2<B, C, R>>() {
            public Function2<B, C, R> apply(Function3<A, B, C, R> f, A a) {
                return f.bind1(a);
            }

            public String toString() {
                return "bind1";
            }
        };
    }

    public Function<A, Function2<B, C, R>> bind1F() {
        return Function3.<A, B, C, R>bind1F2().bind1(this);
    }

    public static <A, B, C, R> Function2<Function3<A, B, C, R>, B, Function2<A, C, R>> bind2F2() {
        return new Function2<Function3<A, B, C, R>, B, Function2<A, C, R>>() {
            public Function2<A, C, R> apply(Function3<A, B, C, R> f, B b) {
                return f.bind2(b);
            }

            public String toString() {
                return "bind2";
            }
        };
    }

    public Function<B, Function2<A, C, R>> bind2F() {
        return Function3.<A, B, C, R>bind2F2().bind1(this);
    }

    public static <A, B, C, R> Function2<Function3<A, B, C, R>, C, Function2<A, B, R>> bind3F2() {
        return new Function2<Function3<A, B, C, R>, C, Function2<A, B, R>>() {
            public Function2<A, B, R> apply(Function3<A, B, C, R> f, C c) {
                return f.bind3(c);
            }

            public String toString() {
                return "bind3";
            }
        };
    }

    public Function<C, Function2<A, B, R>> bind3F() {
        return Function3.<A, B, C, R>bind3F2().bind1(this);
    }

    public Function<Tuple3<A, B, C>, R> asFunction() {
        return new Function<Tuple3<A, B, C>, R>() {
            public R apply(Tuple3<A, B, C> t) {
                return Function3.this.apply(t._1, t._2, t._3);
            }

            public String toString() {
                return Function3.this.toString();
            }
        };
    }

    @SuppressWarnings("unchecked")
    public <A1, B1, C1, R1> Function3<A1, B1, C1, R1> uncheckedCast() {
        return (Function3<A1, B1, C1, R1>) this;
    }

    public Function3<A, B, C, R> memoize() {
        return new Function3<A, B, C, R>() {
            private final Function<Tuple3<A, B, C>, R> f = asFunction().memoize();
            public synchronized R apply(A a, B b, C c) {
                return f.apply(Tuple3.tuple(a, b, c));
            }
        };
    }

} //~
