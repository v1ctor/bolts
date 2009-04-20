package ru.yandex.bolts.function;

import ru.yandex.bolts.collection.Tuple2;

/**
 * @author Stepan Koltsov
 */
public abstract class Function2V<A, B> {
    public abstract void apply(A a, B b);
    
    public Function1V<B> bind1(final A a) {
        return new Function1V<B>() {
            @Override
            public void apply(B b) {
                Function2V.this.apply(a, b);
            }
        };
    }
    
    public Function1V<A> bind2(final B b) {
        return new Function1V<A>() {
            @Override
            public void apply(A a) {
                Function2V.this.apply(a, b);
            }
        };
    }
    
    public Function1V<Tuple2<A, B>> asFunction() {
        return new Function1V<Tuple2<A, B>>() {
            public void apply(Tuple2<A, B> a) {
                Function2V.this.apply(a.get1(), a.get2());
            }
        };
    }
    
} //~
