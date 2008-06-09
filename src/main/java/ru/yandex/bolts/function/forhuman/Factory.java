package ru.yandex.bolts.function.forhuman;

import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function1;

/**
 * () => R
 *
 * @author Iliya Roubin
 *         Date: Sep 3, 2007
 */
public abstract class Factory<R> implements Function0<R>, java.util.concurrent.Callable<R>, HumanFunction {

    public abstract R create();

    public final R apply() {
        return create();
    }

    public final R call() {
        return create();
    }

    /** Create mapper that ignores passed argument */
    public Mapper<Object, R> asMapper() {
        return new Mapper<Object, R>() {
            public R map(Object o) {
                return Factory.this.apply();
            }

            public String toString() {
                return Factory.this + ".asMapper";
            }
        };
    }

    public <B> Factory<B> andThen(final Function1<R, B> mapper) {
        return new Factory<B>() {
            public B create() {
                return mapper.apply(Factory.this.create());
            }

            public String toString() {
                return mapper + "(" + Factory.this + ")";
            }
        };
    }

    public Factory<R> describe(final String string) {
        return new Factory<R>() {
            public R create() {
                return Factory.this.create();
            }

            public String toString() {
                return string;
            }
        };
    }

    /** Factory that always return same value */
    public static <T> Factory<T> constF(final T t) {
        return new Factory<T>() {
            public T create() {
                return t;
            }

            public String toString() {
                return "const(" + t + ")";
            }
        };
    }

    /** Wrap */
    public static <T> Factory<T> wrap(final Function0<T> factory) {
        if (factory instanceof Factory) return (Factory<T>) factory;
        else return new Factory<T>() {
            public T create() {
                return factory.apply();
            }

            public String toString() {
                return factory.toString();
            }
        };
    }

    /** Wrap */
    public static <T> Factory<T> wrap(final java.util.concurrent.Callable<T> callable) {
        if (callable instanceof Factory) return (Factory<T>) callable;
        else return new Factory<T>() {
            public T create() {
                try {
                    return callable.call();
                } catch (Exception e) {
                    // XXX: throw unchecked
                    throw new RuntimeException(e);
                }
            }

            public String toString() {
                return callable.toString();
            }
        };
    }

    /** Wrap */
    @SuppressWarnings("unchecked")
    public static <T> Factory<T> wrap(final java.util.concurrent.Future<T> future) {
        if (future instanceof Factory) return (Factory<T>) future;
        else return new Factory<T>() {
            public T create() {
                try {
                    return future.get();
                } catch (Exception e) {
                    // XXX: throw unchecked
                    throw new RuntimeException(e);
                }
            }

            public String toString() {
                return future.toString();
            }
        };
    }
}
