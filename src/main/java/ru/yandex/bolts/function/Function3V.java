package ru.yandex.bolts.function;

// this file is generated by ru.yandex.bolts.function.gen.GenerateFunctions

import ru.yandex.bolts.collection.Tuple3;


@FunctionalInterface
public interface Function3V<A, B, C> {

    void apply(A a, B b, C c);

    default Function2V<B, C> bind1(final A a) {
        return (b, c) -> apply(a, b, c);
    }

    default Function2V<A, C> bind2(final B b) {
        return (a, c) -> apply(a, b, c);
    }

    default Function2V<A, B> bind3(final C c) {
        return (a, b) -> apply(a, b, c);
    }


    static <A, B, C> Function2<Function3V<A, B, C>, A, Function2V<B, C>> bind1F2() {
        return Function3V::bind1;
    }

    default Function<A, Function2V<B, C>> bind1F() {
        return Function3V.<A, B, C>bind1F2().bind1(this);
    }

    static <A, B, C> Function2<Function3V<A, B, C>, B, Function2V<A, C>> bind2F2() {
        return Function3V::bind2;
    }

    default Function<B, Function2V<A, C>> bind2F() {
        return Function3V.<A, B, C>bind2F2().bind1(this);
    }

    static <A, B, C> Function2<Function3V<A, B, C>, C, Function2V<A, B>> bind3F2() {
        return Function3V::bind3;
    }

    default Function<C, Function2V<A, B>> bind3F() {
        return Function3V.<A, B, C>bind3F2().bind1(this);
    }

    default Function<Tuple3<A, B, C>, Object> asFunction() {
        return t -> {
            apply(t._1, t._2, t._3);
            return null;
        };
    }

    default Function1V<Tuple3<A, B, C>> asFunction1V() {
        return t -> apply(t._1, t._2, t._3);
    }

    default Function3<A, B, C, Object> asFunction3() {
        return (a, b, c) -> {
            apply(a, b, c);
            return null;
        };
    }

    @SuppressWarnings("unchecked")
    default <A1, B1, C1> Function3V<A1, B1, C1> uncheckedCast() {
        return (Function3V<A1, B1, C1>) this;
    }

} //~
