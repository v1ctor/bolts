package ru.yandex.bolts.function;



/**
 * @author Stepan Koltsov
 */
public abstract class Function0V implements Runnable {
    public abstract void apply();

    @Override
    public final void run() {
        apply();
    }

    public static Function0V nop() {
        return new Function0V() {
            public void apply() {
            }

            public String toString() {
                return "nop";
            }

        };
    }

    public <R> Function0<R> asFunction0ReturnNull() {
        return new Function0<R>() {
            public R apply() {
                Function0V.this.apply();
                return null;
            }
        };
    }

    @SuppressWarnings({"unchecked"})
    private static <E extends Throwable> void throw0(Throwable e) throws E {
        throw (E) e;
    }

    private static void throwException(Throwable e) {
        Function0V.<RuntimeException>throw0(e);
    }

    public static Function0V throwC(final Function0<Throwable> th) {
        return new Function0V() {
            public void apply() {
                throwException(th.apply());
            }
        };
    }

    public static Function0V throwC(Throwable th) {
        return throwC(Function0.constF(th));
    }

    public static Function0V wrap(final Runnable runnable) {
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
