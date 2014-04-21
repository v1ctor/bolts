package ru.yandex.bolts.function;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.function.forhuman.Comparator;
import ru.yandex.bolts.internal.ReflectionUtils;
import ru.yandex.bolts.internal.Validate;

/**
 * Function
 *
 * @see Function
 *
 * @author Stepan Koltsov
 */
@FunctionalInterface
public interface Function<A, R> extends java.util.function.Function<A, R> {

    R apply(A a);

    /**
     * (f andThen g)(x) = g(f(x))
     *
     * @see fj.Function#andThen(F, F)
     */
    default <C> Function<A, C> andThen(final Function<? super R, ? extends C> g) {
        return a -> g.apply(apply(a));
    }

    default Function1V<A> andThen(final Function1V<? super R> g) {
        return a -> g.apply(apply(a));
    }

    default Function1B<A> andThen(final Function1B<? super R> g) {
        return a -> g.apply(apply(a));
    }

    /** Not true function composition */
    default Comparator<A> andThen(final Comparator<R> comparator) {
        return (o1, o2) -> comparator.compare(apply(o1), apply(o2));
    }

    /** (f andThen g)(x) = g(f(x)) */
    default Comparator<A> andThen(final Function2I<R, R> comparator) {
        return (a, b) -> comparator.apply(apply(a), apply(b));
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
        return () -> apply(param);
    }

    static <A, R> Function2<Function<A, R>, A, Function0<R>> bindF2() {
        return (f, a) -> f.bind(a);
    }

    default Function<A, Function0<R>> bindF() {
        return Function.<A, R>bindF2().bind1(this);
    }


    static <A, R> Function2<Function<A, R>, A, R> applyF() {
        return (f, a) -> f.apply(a);
    }

    @SuppressWarnings("unchecked")
    default <B, S> Function<B, S> uncheckedCast() {
        return (Function<B, S>) this;
    }

    /** Ignore result of mapping */
    default Function1V<A> ignoreResult() {
        return a -> apply(a);
    }

    /** Map null to null */
    default Function<A, R> ignoreNullF() {
        return a -> {
            if (a == null) return null;
            else return Function.this.apply(a);
        };
    }

    static <A> Function<A, A> identityF() {
        return a -> a;
    }

    static <T> Function<T, String> toStringF() {
        return t -> t != null ? t.toString() : "null";
    }

    /** Function that always returns the same value */
    static <A, B> Function<A, B> constF(final B b) {
        return a -> b;
    }

    @SuppressWarnings("unchecked")
    static <A, B> Function<A, B> wrap(final Method method) {
        if ((method.getModifiers() & Modifier.STATIC) != 0) {
            Validate.isTrue(method.getParameterTypes().length == 1, "static method must have single argument, " + method);
            return a -> (B) ReflectionUtils.invoke(method, null, a);
        } else {
            Validate.isTrue(method.getParameterTypes().length == 0, "instance method must have no arguments, " + method);
            return a -> (B) ReflectionUtils.invoke(method, a);
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
