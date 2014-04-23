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
@FunctionalInterface
public interface Function2B<A, B> {
    boolean apply(A a, B b);

    default Function1B<B> bind1(final A a) {
        return b -> apply(a, b);
    }

    default Function1B<A> bind2(final B b) {
        return a -> apply(a, b);
    }

    static <A, B> Function2<Function2B<A, B>, A, Function1B<B>> bind1F2() {
        return (f, a) -> f.bind1(a);
    }

    default Function<A, Function1B<B>> bind1F() {
        return Function2B.<A, B>bind1F2().bind1(this);
    }

    static <A, B> Function2<Function2B<A, B>, B, Function1B<A>> bind2F2() {
        return (f, b) -> f.bind2(b);
    }

    default Function<B, Function1B<A>> bind2F() {
        return Function2B.<A, B>bind2F2().bind1(this);
    }

    default Function1B<Tuple2<A, B>> asFunction1B() {
        return a -> apply(a.get1(), a.get2());
    }

    default Function<Tuple2<A, B>, Boolean> asFunction() {
        return a -> apply(a.get1(), a.get2());
    }

    static <A, B> Function2B<A, B> asFunction2B(final Function<Tuple2<A, B>, Boolean> f) {
        return (a, b) -> f.apply(Tuple2.tuple(a, b));
    }

    static <A, B> Function2B<A, B> asFunction2B(Function1B<Tuple2<A, B>> f) {
        return asFunction2B(f.asFunction());
    }

    static <A, B> Function2B<A, B> combine(final Function1B<A> fA,
            final Function1B<B> fB)
    {
        return (a, b) -> fA.apply(a) && fB.apply(b);
    }

    default Function2B<A, B> notF() {
        return (a, b) -> !Function2B.this.apply(a, b);
    }

    @SuppressWarnings("unchecked")
    default <C, D> Function2B<C, D> uncheckedCast() {
        return (Function2B<C, D>) this;
    }

    static <A> Function2B<A, A> sameF() {
        return (a, b) -> a == b;
    }

    default <C> Function2B<C, B> compose1(final Function<? super C, ? extends A> f) {
        return (c, b) -> apply(f.apply(c), b);
    }

    default <C> Function2B<A, C> compose2(final Function<? super C, ? extends B> f) {
        return (a, c) -> apply(a, f.apply(c));
    }

    /**
     * Delegate to {@link #equals(Object, Object)}.
     */
    static <A> Function2B<A, A> equalsF() {
        return Function2B::equals;
    }

    /**
     * Check whether two values are equal.
     */
    static <A> boolean equals(A a, A b) {
        return Cf.Object.equals(a, b);
    }

    default Function2B<A, B> memoize() {
        return asFunction2B(asFunction().memoize());
    }

    static <A, B> Function2B<A, B> wrap(final Method method) {
        Validate.isTrue(method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class), "method return type must be boolean or Boolean");
        if ((method.getModifiers() & Modifier.STATIC) != 0) {
            Validate.isTrue(method.getParameterTypes().length == 2, "static method must have 2 arguments, " + method);
            return (a, b) -> (Boolean) ReflectionUtils.invoke(method, null, a, b);
        } else {
            Validate.isTrue(method.getParameterTypes().length == 1, "instance method must have 1 argument, " + method);
            return (a, b) -> (Boolean) ReflectionUtils.invoke(method, a, b);
        }
    }

} //~
