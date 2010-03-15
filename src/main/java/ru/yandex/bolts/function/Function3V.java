package ru.yandex.bolts.function;

import ru.yandex.bolts.collection.Tuple3;

/**
 * @author Stepan Koltsov
 */
public abstract class Function3V<A, B, C> {
    public abstract void apply(A a, B b, C c);

    public Function2V<B, C> bind1(final A a) {
        return new Function2V<B, C>() {
            public void apply(B b, C c) {
                Function3V.this.apply(a, b, c);
            }
        };
    }

    public Function2V<A, C> bind2(final B b) {
        return new Function2V<A, C>() {
            public void apply(A a, C c) {
                Function3V.this.apply(a, b, c);
            }
        };
    }

    public Function2V<A, B> bind3(final C c) {
        return new Function2V<A, B>() {
            public void apply(A a, B b) {
                Function3V.this.apply(a, b, c);
            }
        };
    }

    public Function1V<Tuple3<A, B, C>> asFunction() {
        return asTupleFunction();
    }

    public Function1V<Tuple3<A, B, C>> asTupleFunction() {
        return new Function1V<Tuple3<A, B, C>>() {
            public void apply(Tuple3<A, B, C> a) {
                Function3V.this.apply(a.get1(), a.get2(), a.get3());
            }
        };
    }
} //~
