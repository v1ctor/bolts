package ru.yandex.bolts.function;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.internal.ReflectionUtils;
import ru.yandex.bolts.internal.Validate;


@FunctionalInterface
public interface Function1B<A> {
    boolean apply(A a);

    default Function<A, Boolean> asFunction() {
        return this::apply;
    }

    static <A> Function1B<A> asFunction1B(final Function<A, Boolean> f) {
        return f::apply;
    }

    @SuppressWarnings("unchecked")
    default <B> Function1B<B> uncheckedCast() {
        return (Function1B<B>) this;
    }


    default Function1B<A> nullIsFalseF() {
        return Function1B.<A>notNullF().andF(this);
    }


    default <B> Function1B<B> compose(Function<B, A> g) {
        return g.andThen(this);
    }


    default Function1B<A> notF() {
        return a -> !apply(a);
    }


    default Function1B<A> orF(final Function1B<A> p) {
        return a -> apply(a) || p.apply(a);
    }


    default Function1B<A> andF(final Function1B<A> p) {
        return a -> apply(a) && p.apply(a);
    }


    static <B> Function1B<B> equalsF(final B b) {
        return Function2B.<B>equalsF().bind2(b);
    }


    static <B> Function1B<B> sameF(final B b) {
        return Function2B.<B>sameF().bind2(b);
    }


    static <T> Function1B<T> notNullF() {
        return o -> o != null;
    }


    static <B> Function1B<B> trueF() {
        return b -> true;
    }


    static <B> Function1B<B> falseF() {
        return b -> false;
    }

    static <B> Function1B<B> allOfF(final Collection<? extends Function1B<B>> functions) {
        if (functions.size() == 0) return trueF();
        else if (functions.size() == 1) return functions.iterator().next();
        else return b -> {
            for (Function1B<? super B> Function1B : functions) {
                if (!Function1B.apply(b)) return false;
            }
            return true;
        };
    }

    @SuppressWarnings("unchecked")
    static <B> Function1B<B> allOfF(Function1B<B>... functions) {
        return allOfF(Cf.list(functions));
    }

    static <B> Function1B<B> anyOfF(final Collection<? extends Function1B<B>> functions) {
        if (functions.size() == 0) return b -> false;
        else if (functions.size() == 1) return functions.iterator().next();
        else return b -> {
            for (Function1B<? super B> f : functions) {
                if (f.apply(b)) return true;
            }
            return false;
        };
    }

    @SuppressWarnings("unchecked")
    static <B> Function1B<B> anyOfF(Function1B<B>... functions) {
        return anyOfF(Cf.list(functions));
    }

    static <B> Function1B<B> instanceOfF(final Class<?> cl) {
        return cl::isInstance;
    }


    static <B> Function1B<B> wrap(final Function<B, Boolean> mapper) {
        return mapper::apply;
    }

    default Function1B<A> memoize() {
        return asFunction1B(asFunction().memoize());
    }

    static <B> Function1B<B> constF(boolean value) {
        return b -> value;
    }

    static <A> Function1B<A> wrap(final Method method) {
        Validate.isTrue(method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class), "method return type must be boolean or Boolean");
        if ((method.getModifiers() & Modifier.STATIC) != 0) {
            Validate.isTrue(method.getParameterTypes().length == 1, "static method must have single argument, " + method);
            return a -> (Boolean) ReflectionUtils.invoke(method, null, a);
        } else {
            Validate.isTrue(method.getParameterTypes().length == 0, "instance method must have no arguments, " + method);
            return a -> (Boolean) ReflectionUtils.invoke(method, a);
        }
    }

} //~
