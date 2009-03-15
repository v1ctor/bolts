package ru.yandex.bolts.function;

import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.function.forhuman.Operation;

/**
 * @author Stepan Koltsov
 * 
 * @see F2V
 */
public abstract class Function2V<A, B> {
    public abstract void apply(A a, B b);
    
    public Operation<B> bind1(final A a) {
        return new Operation<B>() {
            @Override
            public void execute(B b) {
                Function2V.this.apply(a, b);
            }
        };
    }
    
    public Operation<A> bind2(final B b) {
        return new Operation<A>() {
            @Override
            public void execute(A a) {
                Function2V.this.apply(a, b);
            }
        };
    }
    
    public FunctionV<Tuple2<A, B>> asTupleFunction() {
        return new FunctionV<Tuple2<A, B>>() {
            public void apply(Tuple2<A, B> a) {
                Function2V.this.apply(a.get1(), a.get2());
            }
        };
    }
    
} //~
