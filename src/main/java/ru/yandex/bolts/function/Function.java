package ru.yandex.bolts.function;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.collections.Transformer;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.function.forhuman.Comparator;
import ru.yandex.bolts.internal.ReflectionUtils;
import ru.yandex.bolts.internal.Validate;
import fj.F;





/**
 * Function
 *
 * @see F
 * @see Transformer
 *
 * @author Stepan Koltsov
 */
@FunctionalInterface
public interface Function<A, R> {

    R apply(A a);

    /**
     * (f andThen g)(x) = g(f(x))
     *
     * @see fj.Function#andThen(F, F)
     */
    default <C> Function<A, C> andThen(final Function<? super R, ? extends C> g) {
        return new Function<A, C>() {
            public C apply(A a) {
                return g.apply(Function.this.apply(a));
            }

            public String toString() {
                return g + "(" + Function.this + ")";
            }
        };
    }

    default Function1V<A> andThen(final Function1V<? super R> g) {
        return new Function1V<A>() {
            public void apply(A a) {
                g.apply(Function.this.apply(a));
            }

            public String toString() {
                return g + "(" + Function.this + ")";
            }
        };
    }

    default Function1B<A> andThen(final Function1B<? super R> g) {
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
    default Comparator<A> andThen(final Comparator<R> comparator) {
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
    default Comparator<A> andThen(final Function2I<R, R> comparator) {
        return new Comparator<A>() {
            public int compare(A a, A b) {
                return comparator.apply(Function.this.apply(a), Function.this.apply(b));
            }

            public String toString() {
                return comparator + "(" + Function.this + ")";
            }
        };
    }

    default Function1B<A> andThenEquals(R value) {
        return andThen(Function1B.equalsF(value));
    }

    /** And then null low natural comparator */
    @SuppressWarnings({"unchecked"})
    default Comparator<A> andThenNaturalComparator() {
        return andThen((Comparator<R>) Comparator.naturalComparator());
    }

    /**
     * (f compose g)(x) = f(g(x))
     *
     * @see fj.Function#compose(F, F)
     */
    default <C> Function<C, R> compose(Function<C, A> g) {
        return g.andThen(this);
    }

    default Function0<R> bind(final A param) {
        return new Function0<R>() {
            public R apply() {
                return Function.this.apply(param);
            }

            public String toString() {
                return Function.this.toString() + "(" + param + ")";
            }

        };
    }

    static <A, R> Function2<Function<A, R>, A, Function0<R>> bindF2() {
        return new Function2<Function<A,R>, A, Function0<R>>() {
            public Function0<R> apply(Function<A, R> f, A a) {
                return f.bind(a);
            }

            public String toString() {
                return "bind";
            }
        };
    }

    default Function<A, Function0<R>> bindF() {
        return Function.<A, R>bindF2().bind1(this);
    }


    static <A, R> Function2<Function<A, R>, A, R> applyF() {
        return new Function2<Function<A, R>, A, R>() {
            public R apply(Function<A, R> f, A a) {
                return f.apply(a);
            }

            public String toString() {
                return "apply";
            }
        };
    }

    @SuppressWarnings("unchecked")
    default <B, S> Function<B, S> uncheckedCast() {
        return (Function<B, S>) this;
    }

    /** Ignore result of mapping */
    default Function1V<A> ignoreResult() {
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
    default Function<A, R> ignoreNullF() {
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

    static <A> Function<A, A> identityF() {
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

    static <T> Function<T, String> toStringF() {
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
    static <A, B> Function<A, B> constF(final B b) {
        return new Function<A, B>() {
            public B apply(A a) {
                return b;
            }

            public String toString() {
                return "const " + b;
            }
        };
    }

    @SuppressWarnings("unchecked")
    static <A, B> Function<A, B> wrap(final Method method) {
        if ((method.getModifiers() & Modifier.STATIC) != 0) {
            Validate.isTrue(method.getParameterTypes().length == 1, "static method must have single argument, " + method);
            return new Function<A, B>() {
                public B apply(A a) {
                    return (B) ReflectionUtils.invoke(method, null, a);
                }
            };
        } else {
            Validate.isTrue(method.getParameterTypes().length == 0, "instance method must have no arguments, " + method);
            return new Function<A, B>() {
                public B apply(A a) {
                    return (B) ReflectionUtils.invoke(method, a);
                }
            };
        }
    }

    default Function<A, R> memoize() {
        return new Function<A, R>() {
            private final MapF<A, R> cache = Cf.hashMap();
            public synchronized R apply(A a) {
                return cache.getOrElseUpdate(a, Function.this);
            }
        };
    }
} //~
