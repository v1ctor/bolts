package ru.yandex.bolts.function;

import ru.yandex.bolts.collection.Tuple2;


/**
 * @author Stepan Koltsov
 */
public abstract class Function2<A, B, R> {
    public abstract R apply(A a, B b);
    
    /** Bind first param to the given value */
    public Function<B, R> bind1(final A a) {
        return new Function<B, R>() {
            public R apply(B b) {
                return Function2.this.apply(a, b);
            }

            public String toString() {
                return Function2.this + "(" + a + ", _)";
            }
        };
    }

    /** Bind second param to the given value */
    public Function<A, R> bind2(final B b) {
        return new Function<A, R>() {
            public R apply(A a) {
                return Function2.this.apply(a, b);
            }

            public String toString() {
                return Function2.this + "(_, " + b + ")";
            }
        };
    }
    
    public Function<Tuple2<A, B>, R> asFunction() {
        return new Function<Tuple2<A,B>, R>() {
            public R apply(Tuple2<A, B> t) {
                return Function2.this.apply(t.get1(), t.get2());
            }
        };
    }


} //~
