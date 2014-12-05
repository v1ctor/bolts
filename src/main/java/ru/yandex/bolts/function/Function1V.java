package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
@FunctionalInterface
public interface Function1V<A> extends java.util.function.Consumer<A> {

    void apply(A a);

    @Override
    default void accept(A a) {
        apply(a);
    }

    /** (f compose g)(x) = f(g(x)) */
    default <B> Function1V<B> compose(final Function<B, A> g) {
        return g.andThen(this);
    }

    default Function0V bind(final A param) {
        return () -> apply(param);
    }

    static <A> Function2<Function1V<A>, A, Function0V> bindF2() {
        return Function1V::bind;
    }

    default Function<A, Function0V> bindF() {
        return Function1V.<A>bindF2().bind1(this);
    }

    default Function<A, A> asFunctionReturnParam() {
        return a -> {
            apply(a);
            return a;
        };
    }

    default <R> Function<A, R> asFunctionReturn(final Function0<R> rv) {
        if (rv == null) throw new IllegalArgumentException("rv constructor must not be null");
        return a -> {
            apply(a);
            return rv.apply();
        };
    }

    default <R> Function<A, R> asFunctionReturnValue(R rv) {
        return asFunctionReturn(Function0.constF(rv));
    }

    default <B> Function<A, B> asFunctionReturnNull() {
        return asFunctionReturnValue(null);
    }

    @SuppressWarnings("unchecked")
    default <B> Function1V<B> uncheckedCast() {
        return (Function1V<B>) this;
    }

    static <A> Function1V<A> nop() {
        return a -> {};
    }

} //~
