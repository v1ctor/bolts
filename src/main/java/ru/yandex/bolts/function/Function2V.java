package ru.yandex.bolts.function;

// this file is generated by ru.yandex.bolts.function.gen.GenerateFunctions

import ru.yandex.bolts.collection.Tuple2;

/**
 */
@FunctionalInterface
public interface Function2V<A, B> {

    void apply(A a, B b);

    default Function1V<B> bind1(final A a) {
        return b -> apply(a, b);
    }

    default Function1V<A> bind2(final B b) {
        return a -> apply(a, b);
    }


    static <A, B> Function2<Function2V<A, B>, A, Function1V<B>> bind1F2() {
        return Function2V::bind1;
    }

    default Function<A, Function1V<B>> bind1F() {
        return Function2V.<A, B>bind1F2().bind1(this);
    }

    static <A, B> Function2<Function2V<A, B>, B, Function1V<A>> bind2F2() {
        return Function2V::bind2;
    }

    default Function<B, Function1V<A>> bind2F() {
        return Function2V.<A, B>bind2F2().bind1(this);
    }

    default Function<Tuple2<A, B>, Object> asFunction() {
        return t -> {
            apply(t._1, t._2);
            return null;
        };
    }

    default Function1V<Tuple2<A, B>> asFunction1V() {
        return t -> apply(t._1, t._2);
    }

    default Function2<A, B, Object> asFunction2() {
        return (a, b) -> {
            apply(a, b);
            return null;
        };
    }

    @SuppressWarnings("unchecked")
    default <A1, B1> Function2V<A1, B1> uncheckedCast() {
        return (Function2V<A1, B1>) this;
    }

} //~
