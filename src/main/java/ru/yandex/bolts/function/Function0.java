package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
@FunctionalInterface
public interface Function0<R> extends java.util.concurrent.Callable<R> {
    R apply();

    @Override
    default R call() throws Exception {
        return apply();
    }

    static <R> Function<Function0<R>, R> applyF() {
        return Function0::apply;
    }

    default <B> Function0<B> andThen(final Function<R, B> mapper) {
        return () -> mapper.apply(apply());
    }

    default Function0V andThen(final Function1V<R> then) {
        return () -> then.apply(apply());
    }

    static <T> Function0<T> newInstanceF(final Class<? extends T> clazz) {
        if (clazz == null) throw new IllegalArgumentException();
        return () -> {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    default <A> Function<A, R> asFunction() {
        return a -> apply();
    }

    default Function0V asFunction0V() {
        return this::apply;
    }

    /** Function0 that always return same value
     *
     * @param <T> element
     * @param t element
     *
     * @return function
     * */
    static <T> Function0<T> constF(final T t) {
        return () -> t;
    }

    /** Wrap */
    static <T> Function0<T> wrap(final java.util.concurrent.Callable<T> callable) {
        if (callable instanceof Function0<?>) return (Function0<T>) callable;
        else return () -> {
            try {
                return callable.call();
            } catch (Exception e) {
                // XXX: throw unchecked
                throw new RuntimeException(e);
            }
        };
    }

    /** Wrap
     *
     * @param <T> element
     * @param future future
     *
     * @return function
     * */
    @SuppressWarnings("unchecked")
    static <T> Function0<T> wrap(final java.util.concurrent.Future<T> future) {
        if (future instanceof Function0) return (ru.yandex.bolts.function.Function0<T>) future;
        else return () -> {
            try {
                return future.get();
            } catch (Exception e) {
                // XXX: throw unchecked
                throw new RuntimeException(e);
            }
        };
    }

    default Function0<R> memoize() {
        return new Function0<R>() {
            private boolean haveValue = false;
            private R cached;
            public synchronized R apply() {
                if (haveValue) return cached;
                cached = Function0.this.apply();
                haveValue = true;
                return cached;
            }
        };
    }

    @SuppressWarnings("unchecked")
    default <B> Function0<B> uncheckedCast() {
        return (Function0<B>) this;
    }

} //~
