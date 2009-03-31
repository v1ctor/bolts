package ru.yandex.bolts.function;

import ru.yandex.bolts.collection.Tuple2;

/**
 * @author Stepan Koltsov
 */
public abstract class Function2B<A, B> {
    public abstract boolean apply(A a, B b);
    
    public Function1B<B> bind1(final A a) {
        return new Function1B<B>() {
            public boolean apply(B b) {
                return Function2B.this.apply(a, b);
            }
        };
    }
    
    public Function1B<A> bind2(final B b) {
        return new Function1B<A>() {
            public boolean apply(A a) {
                return Function2B.this.apply(a, b);
            }
        };
    }
    
    public Function1B<Tuple2<A, B>> asTupleFunction() {
        return new Function1B<Tuple2<A, B>>() {
            public boolean apply(Tuple2<A, B> a) {
                return Function2B.this.apply(a.get1(), a.get2());
            }
        };
    }
    
    public Function2B<A, B> notF() {
        return new Function2B<A, B>() {
            public boolean apply(A a, B b) {
                return !Function2B.this.apply(a, b);
            }

            @Override
            public Function2B<A, B> notF() {
                return Function2B.this;
            }

            @Override
            public String toString() {
                return "not(" + Function2B.this + ")";
            }
            
        };
    }
    
} //~
