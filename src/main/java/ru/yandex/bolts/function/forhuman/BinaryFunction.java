package ru.yandex.bolts.function.forhuman;

import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.function.Function2;

/**
 * (A, B) => R
 * @author Stepan Koltsov
 */
public abstract class BinaryFunction<A, B, R> implements Function2<A, B, R>, HumanFunction {
    public final R apply(A a, B b) {
        return call(a, b);
    }

    public abstract R call(A a, B b);

    /** Bind first param to the given value */
    public Mapper<B, R> bind1(final A a) {
        return new Mapper<B, R>() {
            public R map(B b) {
                return BinaryFunction.this.call(a, b);
            }

            public String toString() {
                return BinaryFunction.this + "(" + a + ", _)";
            }
        };
    }

    /** Bind second param to the given value */
    public Mapper<A, R> bind2(final B b) {
        return new Mapper<A, R>() {
            public R map(A a) {
                return BinaryFunction.this.call(a, b);
            }

            public String toString() {
                return BinaryFunction.this + "(_, " + b + ")";
            }
        };
    }
    
    public Mapper<Tuple2<A, B>, R> asMapperFromTuple() {
        return new Mapper<Tuple2<A,B>, R>() {
            public R map(Tuple2<A, B> t) {
                return call(t.get1(), t.get2());
            }
        };
    }

    public BinaryFunction<A, B, R> describe(final String string) {
        return new BinaryFunction<A, B, R>() {
            public R call(A a, B b) {
                return BinaryFunction.this.call(a, b);
            }

            public String toString() {
                return string;
            }
        };
    }
    
    public static <A, B, R> BinaryFunction<A, B, R> wrap(final Function2<A, B, R> bf) {
        if (bf instanceof BinaryFunction<?, ?, ?>) return (BinaryFunction<A, B, R>) bf;
        else return new BinaryFunction<A, B, R>() {
            public R call(A a, B b) {
                return bf.apply(a, b);
            }

            @Override
            public String toString() {
                return bf.toString();
            }
            
        };
    }
} //~
