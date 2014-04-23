package ru.yandex.bolts.function;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.internal.ReflectionUtils;
import ru.yandex.bolts.internal.Validate;

/**
 * Function with two arguments.
 *
 * @author Stepan Koltsov
 */
@FunctionalInterface
public interface Function2<A, B, R> {
    R apply(A a, B b);

    /** Bind first param to the given value */
    default Function<B, R> bind1(final A a) {
        return b -> apply(a, b);
    }

    /** Bind second param to the given value */
    default Function<A, R> bind2(final B b) {
        return a -> apply(a, b);
    }


    static <A, B, R> Function2<Function2<A, B, R>, A, Function<B, R>> bind1F2() {
        return (f, a) -> f.bind1(a);
    }

    default Function<A, Function<B, R>> bind1F() {
        return Function2.<A, B, R>bind1F2().bind1(this);
    }

    static <A, B, R> Function2<Function2<A, B, R>, B, Function<A, R>> bind2F2() {
        return (f, b) -> f.bind2(b);
    }

    default Function<B, Function<A, R>> bind2F() {
        return Function2.<A, B, R>bind2F2().bind1(this);
    }


    default Function<Tuple2<A, B>, R> asFunction() {
        return t -> apply(t.get1(), t.get2());
    }

    default Function2<B, A, R> swap() {
        return (b, a) -> apply(a, b);
    }

    default <S> Function2<A, B, S> andThen(final Function<? super R, ? extends S> f) {
        return (a, b) -> f.apply(apply(a, b));
    }

    default <C> Function2<C, B, R> compose1(final Function<? super C, ? extends A> f) {
        return (c, b) -> apply(f.apply(c), b);
    }

    default <C> Function2<A, C, R> compose2(final Function<? super C, ? extends B> f) {
        return (a, c) -> apply(a, f.apply(c));
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
            return (a, b) -> (R) ReflectionUtils.invoke(method, null, a, b);
        } else {
            Validate.isTrue(method.getParameterTypes().length == 1, "instance method must have 1 argument, " + method);
            return (a, b) -> (R) ReflectionUtils.invoke(method, a, b);
        }
    }

} //~
