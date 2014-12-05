package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
@FunctionalInterface
public interface Function0V extends Runnable {
    void apply();

    @Override
    default void run() {
        apply();
    }

    static Function1V<Function0V> applyF() {
        return Function0V::apply;
    }

    static Function0V nop() {
        return () -> {};
    }

    default <R> Function0<R> asFunction0ReturnNull() {
        return () -> {
            apply();
            return null;
        };
    }

    default <R> Function<R, R> asFunctionReturnParam() {
        return r -> {
            apply();
            return r;
        };
    }

    @SuppressWarnings({"unchecked"})
    static <E extends Throwable> void throw0(Throwable e) throws E {
        throw (E) e;
    }

    static void throwException(Throwable e) {
        Function0V.throw0(e);
    }

    static Function0V throwC(final Function0<Throwable> th) {
        return () -> throwException(th.apply());
    }

    static Function0V throwC(Throwable th) {
        return () -> throwException(th);
    }

    static Function0V wrap(final Runnable runnable) {
        if (runnable instanceof Function0V) return (Function0V) runnable;
        else return runnable::run;
    }

} //~
