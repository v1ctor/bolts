package ru.yandex.bolts.function;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.internal.ReflectionUtils;
import ru.yandex.bolts.internal.Validate;

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

    public Function1B<Tuple2<A, B>> asFunction1B() {
        return new Function1B<Tuple2<A, B>>() {
            public boolean apply(Tuple2<A, B> a) {
                return Function2B.this.apply(a.get1(), a.get2());
            }
        };
    }

    public Function<Tuple2<A, B>, Boolean> asFunction() {
        return new Function<Tuple2<A, B>, Boolean>() {
            public Boolean apply(Tuple2<A, B> a) {
                return Function2B.this.apply(a.get1(), a.get2());
            }
        };
    }

    public static <A, B> Function2B<A, B> asFunction2B(final Function<Tuple2<A, B>, Boolean> f) {
        return new Function2B<A, B>() {
            public boolean apply(A a, B b) {
                return f.apply(Tuple2.tuple(a, b));
            }

            @Override
            public String toString() {
                return f.toString();
            }
        };
    }

    public static <A, B> Function2B<A, B> asFunction2B(Function1B<Tuple2<A, B>> f) {
        return asFunction2B(f.asFunction());
    }

    public static <A, B> Function2B<A, B> combine(final Function1B<A> fA,
            final Function1B<B> fB)
    {
        return new Function2B<A, B>() {
            public boolean apply(A a, B b) {
                return fA.apply(a) && fB.apply(b);
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

    @SuppressWarnings("unchecked")
    public <C, D> Function2B<C, D> uncheckedCast() {
        return (Function2B<C, D>) this;
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

    public <C> Function2B<C, B> compose1(final Function<? super C, ? extends A> f) {
        return new Function2B<C, B>() {
            public boolean apply(C c, B b) {
                return Function2B.this.apply(f.apply(c), b);
            }
        };
    }

    public <C> Function2B<A, C> compose2(final Function<? super C, ? extends B> f) {
        return new Function2B<A, C>() {
            public boolean apply(A a, C c) {
                return Function2B.this.apply(a, f.apply(c));
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
        return Cf.Object.equals(a, b);
    }

    public Function2B<A, B> memoize() {
        return asFunction2B(asFunction().memoize());
    }

    public static <A, B> Function2B<A, B> wrap(final Method method) {
        Validate.isTrue(method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class), "method return type must be boolean or Boolean");
        if ((method.getModifiers() & Modifier.STATIC) != 0) {
            Validate.isTrue(method.getParameterTypes().length == 2, "static method must have 2 arguments, " + method);
            return new Function2B<A, B>() {
                public boolean apply(A a, B b) {
                    return (Boolean) ReflectionUtils.invoke(method, null, a, b);
                }
            };
        } else {
            Validate.isTrue(method.getParameterTypes().length == 1, "instance method must have 1 argument, " + method);
            return new Function2B<A, B>() {
                public boolean apply(A a, B b) {
                    return (Boolean) ReflectionUtils.invoke(method, a, b);

                }
            };
        }
    }

} //~
