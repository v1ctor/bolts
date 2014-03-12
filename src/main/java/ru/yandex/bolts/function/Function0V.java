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
        return new Function1V<Function0V>() {
            public void apply(Function0V a) {
                a.apply();
            }
        };
    }

    static Function0V nop() {
        return new Function0V() {
            public void apply() {
            }

            public String toString() {
                return "nop";
            }

        };
    }

    default <R> Function0<R> asFunction0ReturnNull() {
        return new Function0<R>() {
            public R apply() {
                Function0V.this.apply();
                return null;
            }
        };
    }

    default <R> Function<R, R> asFunctionReturnParam() {
        return new Function<R, R>() {
            public R apply(R r) {
                Function0V.this.apply();
                return r;
            }
        };
    }

    @SuppressWarnings({"unchecked"})
    static <E extends Throwable> void throw0(Throwable e) throws E {
        throw (E) e;
    }

    static void throwException(Throwable e) {
        Function0V.<RuntimeException>throw0(e);
    }

    static Function0V throwC(final Function0<Throwable> th) {
        return new Function0V() {
            public void apply() {
                throwException(th.apply());
            }
        };
    }

    static Function0V throwC(Throwable th) {
        return throwC(Function0.constF(th));
    }

    static Function0V wrap(final Runnable runnable) {
        if (runnable instanceof Function0V) return (Function0V) runnable;
        else return new Function0V() {
            public void apply() {
                runnable.run();
            }

            public String toString() {
                return runnable.toString();
            }
        };
    }


} //~
