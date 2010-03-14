package ru.yandex.bolts.function;


/**
 * @author Stepan Koltsov
 */
public abstract class Function3<A, B, C, R> {
    public abstract R apply(A a, B b, C c);

    public Function2<B, C, R> bind1(final A a) {
        return new Function2<B, C, R>() {
            public R apply(B b, C c) {
                return Function3.this.apply(a, b, c);
            }
        };
    }

    public Function2<A, C, R> bind2(final B b) {
        return new Function2<A, C, R>() {
            public R apply(A a, C c) {
                return Function3.this.apply(a, b, c);
            }
        };
    }

    public Function2<A, B, R> bind3(final C c) {
        return new Function2<A, B, R>() {
            public R apply(A a, B b) {
                return Function3.this.apply(a, b, c);
            }
        };
    }


    public static <A, B, C, R> Function2<Function3<A, B, C, R>, A, Function2<B, C, R>> bind1F2() {
        return new Function2<Function3<A, B, C, R>, A, Function2<B, C, R>>() {
            public Function2<B, C, R> apply(Function3<A, B, C, R> f, A a) {
                return f.bind1(a);
            }
        };
    }

    public Function<A, Function2<B, C, R>> bind1F() {
        return Function3.<A, B, C, R>bind1F2().bind1(this);
    }

    public static <A, B, C, R> Function2<Function3<A, B, C, R>, B, Function2<A, C, R>> bind2F2() {
        return new Function2<Function3<A,B,C,R>, B, Function2<A,C,R>>() {
            public Function2<A, C, R> apply(Function3<A, B, C, R> f, B b) {
                return f.bind2(b);
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
        };
    }

    public Function<C, Function2<A, B, R>> bind3F() {
        return Function3.<A, B, C, R>bind3F2().bind1(this);
    }

} //~
