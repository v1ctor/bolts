package ru.yandex.bolts.function;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.apache.commons.collections.Predicate;

import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.internal.ReflectionUtils;
import ru.yandex.bolts.internal.Validate;
import ru.yandex.bolts.methodFunction.FunctionsForClass;

/**
 * Predicate.
 *
 * @see Predicate
 *
 * @author Stepan Koltsov
 */
public abstract class Function1B<A> {
    public abstract boolean apply(A a);

    public Function<A, Boolean> asFunction() {
        return new Function<A, Boolean>() {
            public Boolean apply(A a) {
                return Function1B.this.apply(a);
            }

            @Override
            public String toString() {
                return Function1B.this.toString();
            }
        };
    }

    public static <A> Function1B<A> asFunction1B(final Function<A, Boolean> f) {
        return new Function1B<A>() {
            public boolean apply(A a) {
                return f.apply(a);
            }

            @Override
            public String toString() {
                return f.toString();
            }
        };
    }

    @SuppressWarnings("unchecked")
    public <B> Function1B<B> uncheckedCast() {
        return (Function1B<B>) this;
    }

    /** Check for null before calling this p */
    public Function1B<A> nullIsFalseF() {
        return Function1B.<A>notNullF().andF(this);
    }

    /** (f compose g)(x) = g(f(x)) */
    public <B> Function1B<B> compose(Function<B, A> g) {
        return g.andThen(this);
    }

    /** Not(this) */
    public Function1B<A> notF() {
        return new Function1B<A>() {
            public boolean apply(A a) {
                return !Function1B.this.apply(a);
            }

            public Function1B<A> notF() {
                return Function1B.this;
            }

            public String toString() {
                return "not(" + Function1B.this + ")";
            }
        };
    }

    /**
     * @deprecated
     */
    public final Function1B<A> notP() {
        return notF();
    }

    /** Or */
    public Function1B<A> orF(final Function1B<A> p) {
        return new Function1B<A>() {
            public boolean apply(A a) {
                return Function1B.this.apply(a) || p.apply(a);
            }

            /*
            public Function1B<A> orF(Function1B<A> last) {
                return Function1B.anyOfF(Function1B.this, p, last);
            }
            */

            public String toString() {
                return "or(" + Function1B.this + ", " + p + ")";
            }
        };
    }

    /** And */
    public Function1B<A> andF(final Function1B<A> p) {
        return new Function1B<A>() {
            public boolean apply(A a) {
                return Function1B.this.apply(a) && p.apply(a);
            }

            /*
            public Function1B<A> andF(Function1B<A> last) {
                return Function1B.allOfF(Function1B.this, p, last);
            }
            */

            public String toString() {
                return "and(" + Function1B.this + ", " + p + ")";
            }
        };
    }

    /**
     * @see Function2B#equalsF()
     */
    public static <B> Function1B<B> equalsF(final B b) {
        return Function2B.<B>equalsF().bind2(b);
    }

    /**
     * @deprecated
     */
    public static <B> Function1B<B> equalsP(final B b) {
        return equalsF(b);
    }

    /**
     * @see Function2B#sameF()
     */
    public static <B> Function1B<B> sameF(final B b) {
        return Function2B.<B>sameF().bind2(b);
    }

    /** Check argument is not null */
    public static <T> Function1B<T> notNullF() {
        return new Function1B<T>() {
            public boolean apply(T o) {
                return o != null;
            }

            public Function1B<T> nullIsFalseF() {
                return this;
            }

            public String toString() {
                return "notNull";
            }
        };
    }

    /** Function that always returns <code>true</code> */
    public static <B> Function1B<B> trueF() {
        return new Function1B<B>() {
            public boolean apply(B b) {
                return true;
            }

            public Function1B<B> orF(Function1B<B> p) {
                return this;
            }

            public Function1B<B> andF(Function1B<B> p) {
                return p;
            }

            public Function1B<B> notF() {
                return falseF();
            }

            public String toString() {
                return "true";
            }
        };
    }

    /** Function that always returns <code>false</code> */
    public static <B> Function1B<B> falseF() {
        return new Function1B<B>() {
            public boolean apply(B b) {
                return false;
            }

            public Function1B<B> orF(Function1B<B> p) {
                return p;
            }

            public Function1B<B> andF(Function1B<B> p) {
                return this;
            }

            public Function1B<B> notF() {
                return trueF();
            }

            public String toString() {
                return "false";
            }
        };
    }

    public static <B> Function1B<B> allOfF(final Collection<? extends Function1B<B>> functions) {
        if (functions.size() == 0) return trueF();
        else if (functions.size() == 1) return functions.iterator().next();
        else return new Function1B<B>() {
            public boolean apply(B b) {
                for (Function1B<? super B> Function1B : functions) {
                    if (!Function1B.apply(b)) return false;
                }
                return true;
            }

            public String toString() {
                return CollectionsF.x(functions).mkString("allOf(", ", ", ")");
            }
        };
    }

    public static <B> Function1B<B> allOfF(Function1B<B>... functions) {
        return allOfF(CollectionsF.list(functions));
    }

    public static <B> Function1B<B> anyOfF(final Collection<? extends Function1B<B>> functions) {
        if (functions.size() == 0) return falseF();
        else if (functions.size() == 1) return functions.iterator().next();
        else return new Function1B<B>() {
            public boolean apply(B b) {
                for (Function1B<? super B> f : functions) {
                    if (f.apply(b)) return true;
                }
                return false;
            }

            public String toString() {
                return CollectionsF.x(functions).mkString("anyOf(", ", ", ")");
            }
        };
    }

    public static <B> Function1B<B> anyOfF(Function1B<B>... functions) {
        return anyOfF(CollectionsF.list(functions));
    }

    public static <B> Function1B<B> instanceOfF(final Class<?> cl) {
        return new Function1B<B>() {
            public boolean apply(B b) {
                return cl.isInstance(b);
            }

            public String toString() {
                return "instanceof " + cl.getName();
            }
        };
    }

    /** Wrap */
    public static <B> Function1B<B> wrap(final Function<B, Boolean> mapper) {
        return new Function1B<B>() {
            public boolean apply(B b) {
                return mapper.apply(b);
            }

            public String toString() {
                return mapper.toString();
            }
        };
    }

    public Function1B<A> memoize() {
        return asFunction1B(asFunction().memoize());
    }

    public static <B> Function1B<B> constF(boolean value) {
        return value ? Function1B.<B>trueF() : Function1B.<B>falseF();
    }

    public static <A> Function1B<A> getCurrent() {
        // XXX: add message weaving
        return FunctionsForClass.getCurrentFunction1B();
    }

    public static <A> Function1B<A> f(boolean b) {
        return getCurrent();
    }

    public static <A> Function1B<A> wrap(final Method method) {
        Validate.isTrue(method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class), "method return type must be boolean or Boolean");
        if ((method.getModifiers() & Modifier.STATIC) != 0) {
            Validate.isTrue(method.getParameterTypes().length == 1, "static method must have single argument, " + method);
            return new Function1B<A>() {
                public boolean apply(A a) {
                    return (Boolean) ReflectionUtils.invoke(method, null, a);
                }
            };
        } else {
            Validate.isTrue(method.getParameterTypes().length == 0, "instance method must have no arguments, " + method);
            return new Function1B<A>() {
                public boolean apply(A a) {
                    return (Boolean) ReflectionUtils.invoke(method, a);
                }
            };
        }
    }

} //~
