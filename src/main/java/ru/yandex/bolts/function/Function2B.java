package ru.yandex.bolts.function;

import ru.yandex.bolts.collection.Tuple2;

/**
 * @author Stepan Koltsov
 */
public abstract class Function2B<A, B> {
    public abstract boolean apply(A a, B b);
    
    public FunctionB<B> bind1(final A a) {
        return new FunctionB<B>() {
            public boolean apply(B b) {
                return Function2B.this.apply(a, b);
            }
        };
    }
    
    public FunctionB<A> bind2(final B b) {
        return new FunctionB<A>() {
            public boolean apply(A a) {
                return Function2B.this.apply(a, b);
            }
        };
    }
    
    public FunctionB<Tuple2<A, B>> asTupleFunction() {
        return new FunctionB<Tuple2<A, B>>() {
            public boolean apply(Tuple2<A, B> a) {
                return Function2B.this.apply(a.get1(), a.get2());
            }
        };
    }

} //~
