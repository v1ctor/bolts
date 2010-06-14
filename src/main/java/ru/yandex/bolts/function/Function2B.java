package ru.yandex.bolts.function;

import ru.yandex.bolts.collection.Tuple2;

/**
 * @author Stepan Koltsov
 */
public abstract class Function2B<A, B> {
    public abstract boolean apply(A a, B b);

    public Function1B<B> bind1(final A a) {
        return new Function1B<B>() {
            public boolean apply(B b) {
                return Function2B.this.apply(a, b);
            }
        };
    }

    public Function1B<A> bind2(final B b) {
        return new Function1B<A>() {
            public boolean apply(A a) {
                return Function2B.this.apply(a, b);
            }
        };
    }

    public static <A, B> Function2<Function2B<A, B>, A, Function1B<B>> bind1F2() {
        return new Function2<Function2B<A, B>, A, Function1B<B>>() {
            public Function1B<B> apply(Function2B<A, B> f, A a) {
                return f.bind1(a);
            }

            public String toString() {
                return "bind1";
            }
        };
    }

    public Function<A, Function1B<B>> bind1F() {
        return Function2B.<A, B>bind1F2().bind1(this);
    }

    public static <A, B> Function2<Function2B<A, B>, B, Function1B<A>> bind2F2() {
        return new Function2<Function2B<A, B>, B, Function1B<A>>() {
            public Function1B<A> apply(Function2B<A, B> f, B b) {
                return f.bind2(b);
            }

            public String toString() {
                return "bind2";
            }
        };
    }

    public Function<B, Function1B<A>> bind2F() {
        return Function2B.<A, B>bind2F2().bind1(this);
    }

    public Function<Tuple2<A, B>, Boolean> asFunction() {
        return new Function<Tuple2<A, B>, Boolean>() {
            public Boolean apply(Tuple2<A, B> a) {
                return Function2B.this.apply(a.get1(), a.get2());
            }
        };
    }

    public Function2B<A, B> notF() {
        return new Function2B<A, B>() {
            public boolean apply(A a, B b) {
                return !Function2B.this.apply(a, b);
            }

            @Override
            public Function2B<A, B> notF() {
                return Function2B.this;
            }

            @Override
            public String toString() {
                return "not(" + Function2B.this + ")";
            }

        };
    }

    public static <A> Function2B<A, A> sameF() {
        return new Function2B<A, A>() {
            public boolean apply(A a, A b) {
                return a == b;
            }

            public String toString() {
                return "eq";
            }
        };
    }

    /**
     * Delegate to {@link #equals(Object, Object)}.
     */
    public static <A> Function2B<A, A> equalsF() {
        return new Function2B<A, A>() {
            public boolean apply(A a, A b) {
                return equals(a, b);
            }

            @Override
            public String toString() {
                return "equals";
            }

        };
    }

    /**
     * Check whether two values are equal.
     */
    public static <A> boolean equals(A a, A b) {
        return a == null || b == null ? a == b : a.equals(b);
    }

} //~
