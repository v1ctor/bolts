package ru.yandex.bolts.function;



/**
 * @author Stepan Koltsov
 */
public abstract class Function0<R> implements java.util.concurrent.Callable<R> {
    public abstract R apply();

    @Override
    public final R call() throws Exception {
        return apply();
    }

    public static <R> Function<Function0<R>, R> applyF() {
        return new Function<Function0<R>, R>() {
            public R apply(Function0<R> a) {
                return a.apply();
            }

            public String toString() {
                return "apply";
            }
        };
    }

    public <B> Function0<B> andThen(final Function<R, B> mapper) {
        return new Function0<B>() {
            public B apply() {
                return mapper.apply(Function0.this.apply());
            }

            public String toString() {
                return mapper + "(" + Function0.this + ")";
            }
        };
    }

    public Function0V andThen(final Function1V<R> then) {
        return new Function0V() {
            public void apply() {
                then.apply(Function0.this.apply());
            }
        };
    }

    public static <T> Function0<T> newInstanceF(final Class<? extends T> clazz) {
        if (clazz == null) throw new IllegalArgumentException();
        return new Function0<T>() {
            public T apply() {
                try {
                    return clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public String toString() {
                return clazz + ".newInstance";
            }

        };
    }

    public <A> Function<A, R> asFunction() {
        return new Function<A, R>() {
            public R apply(A a) {
                return Function0.this.apply();
            }

            public String toString() {
                return Function0.this.toString();
            }
        };
    }

    public Function0V asFunction0V() {
        return new Function0V() {
            public void apply() {
                Function0.this.apply();
            }
        };
    }

    /** Function0 that always return same value */
    public static <T> Function0<T> constF(final T t) {
        return new Function0<T>() {
            public T apply() {
                return t;
            }

            public String toString() {
                return "const(" + t + ")";
            }
        };
    }

    /** Wrap */
    public static <T> Function0<T> wrap(final java.util.concurrent.Callable<T> callable) {
        if (callable instanceof Function0<?>) return (Function0<T>) callable;
        else return new Function0<T>() {
            public T apply() {
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
    public static <T> Function0<T> wrap(final java.util.concurrent.Future<T> future) {
        if (future instanceof Function0) return (ru.yandex.bolts.function.Function0<T>) future;
        else return new Function0<T>() {
            public T apply() {
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

    public Function0<R> memoize() {
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
    public <B> Function0<B> uncheckedCast() {
        return (Function0<B>) this;
    }

} //~
