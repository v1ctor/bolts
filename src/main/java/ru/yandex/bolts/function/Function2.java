package ru.yandex.bolts.function;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import fj.F2;

import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.internal.ReflectionUtils;
import ru.yandex.bolts.internal.Validate;


/**
 * Function with two arguments.
 *
 * @see F2
 *
 * @author Stepan Koltsov
 */
@FunctionalInterface
public interface Function2<A, B, R> {
    R apply(A a, B b);



    /** Bind first param to the given value */
    default Function<B, R> bind1(final A a) {
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
    default Function<A, R> bind2(final B b) {
        return new Function<A, R>() {
            public R apply(A a) {
                return Function2.this.apply(a, b);
            }

            public String toString() {
                return Function2.this + "(_, " + b + ")";
            }
        };
    }


    static <A, B, R> Function2<Function2<A, B, R>, A, Function<B, R>> bind1F2() {
        return new Function2<Function2<A, B, R>, A, Function<B, R>>() {
            public Function<B, R> apply(Function2<A, B, R> f, A a) {
                return f.bind1(a);
            }

            public String toString() {
                return "bind1";
            }
        };
    }

    default Function<A, Function<B, R>> bind1F() {
        return Function2.<A, B, R>bind1F2().bind1(this);
    }

    static <A, B, R> Function2<Function2<A, B, R>, B, Function<A, R>> bind2F2() {
        return new Function2<Function2<A, B, R>, B, Function<A, R>>() {
            public Function<A, R> apply(Function2<A, B, R> f, B b) {
                return f.bind2(b);
            }

            public String toString() {
                return "bind2";
            }
        };
    }

    default Function<B, Function<A, R>> bind2F() {
        return Function2.<A, B, R>bind2F2().bind1(this);
    }


    default Function<Tuple2<A, B>, R> asFunction() {
        return new Function<Tuple2<A,B>, R>() {
            public R apply(Tuple2<A, B> t) {
                return Function2.this.apply(t.get1(), t.get2());
            }
        };
    }

    default Function2<B, A, R> swap() {
        return new Function2<B, A, R>() {
            public R apply(B b, A a) {
                return Function2.this.apply(a, b);
            }
        };
    }

    default <S> Function2<A, B, S> andThen(final Function<? super R, ? extends S> f) {
        return new Function2<A, B, S>() {
            public S apply(A a, B b) {
                return f.apply(Function2.this.apply(a, b));
            }
        };
    }

    default <C> Function2<C, B, R> compose1(final Function<? super C, ? extends A> f) {
        return new Function2<C, B, R>() {
            public R apply(C c, B b) {
                return Function2.this.apply(f.apply(c), b);
            }
        };
    }

    default <C> Function2<A, C, R> compose2(final Function<? super C, ? extends B> f) {
        return new Function2<A, C, R>() {
            public R apply(A a, C c) {
                return Function2.this.apply(a, f.apply(c));
            }
        };
    }

    @SuppressWarnings("unchecked")
    default <A1, B1, R1> Function2<A1, B1, R1> uncheckedCast() {
        return (Function2<A1, B1, R1>) this;
    }

    default Function2<A, B, R> memoize() {
        return new Function2<A, B, R>() {
            private final Function<Tuple2<A, B>, R> f = Function2.this.asFunction().memoize();
            public R apply(A a, B b) {
                return f.apply(Tuple2.tuple(a, b));
            }
        };
    }

    @SuppressWarnings("unchecked")
    static <A, B, R> Function2<A, B, R> wrap(final Method method) {
        if ((method.getModifiers() & Modifier.STATIC) != 0) {
            Validate.isTrue(method.getParameterTypes().length == 2, "static method must have 2 arguments, " + method);
            return new Function2<A, B, R>() {
                public R apply(A a, B b) {
                    return (R) ReflectionUtils.invoke(method, null, a, b);
                }
            };
        } else {
            Validate.isTrue(method.getParameterTypes().length == 1, "instance method must have 1 argument, " + method);
            return new Function2<A, B, R>() {
                public R apply(A a, B b) {
                    return (R) ReflectionUtils.invoke(method, a, b);
                }
            };
        }
    }

} //~
