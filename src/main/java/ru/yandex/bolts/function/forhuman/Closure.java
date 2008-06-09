package ru.yandex.bolts.function.forhuman;

import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function0V;

/**
 * () => void
 *
 * @author Stepan Koltsov
 */
public abstract class Closure implements Function0V, Runnable, HumanFunction {
    public final void apply() {
        execute();
    }

    public final void run() {
        execute();
    }

    public abstract void execute();

    public <T> Factory<T> andThen(final Function0<T> f) {
        return new Factory<T>() {
            public T create() {
                Closure.this.execute();
                return f.apply();
            }
        };
    }

    public <T> Factory<T> asFactory(T t) {
        return andThen(Factory.constF(t));
    }

    public static Closure nop() {
        return new Closure() {
            public void execute() {
            }

            public String toString() {
                return "nop";
            }

            public <T> Factory<T> andThen(Function0<T> f) {
                return Factory.wrap(f);
            }
        };
    }
    
    public static Closure sleepC(final long millis) {
        return new Closure() {
            public void execute() {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    throwException(e);
                }
            }

            @Override
            public String toString() {
                return "sleep(" + millis + ")";
            }
        };
    }

    @SuppressWarnings({"unchecked"})
    private static <E extends Throwable> void throw0(Throwable e) throws E {
        throw (E) e;
    }

    private static void throwException(Throwable e) {
        Closure.<RuntimeException>throw0(e);
    }

    public static Closure throwC(final Function0<Throwable> th) {
        return new Closure() {
            public void execute() {
                throwException(th.apply());
            }
        };
    }

    public static Closure throwC(Throwable th) {
        return throwC(Factory.constF(th));
    }

    public static Closure wrap(final Function0V f) {
        if (f instanceof Closure) return (Closure) f;
        else return new Closure() {
            public void execute() {
                f.apply();
            }

            public String toString() {
                return f.toString();
            }
        };
    }

    public static Closure wrap(final Runnable runnable) {
        if (runnable instanceof Closure) return (Closure) runnable;
        else return new Closure() {
            public void execute() {
                runnable.run();
            }

            public String toString() {
                return runnable.toString();
            }
        };
    }

    public Closure describe(final String string) {
        return new Closure() {
            public void execute() {
                Closure.this.execute();
            }

            public String toString() {
                return string;
            }
        };
    }
} //~
