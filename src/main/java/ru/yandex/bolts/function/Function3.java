package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
public abstract class Function3<A, B, C, R> {
    public abstract R apply(A a, B b, C c);
    
    public Function2<B, C, R> bind1(final A a) {
        return new Function2<B, C, R>() {
            public R apply(B b, C c) {
                return Function3.this.apply(a, b, c);
            }
        };
    }
    
    public Function2<A, C, R> bind2(final B b) {
        return new Function2<A, C, R>() {
            public R apply(A a, C c) {
                return Function3.this.apply(a, b, c);
            }
        };
    }
    
    public Function2<A, B, R> bind3(final C c) {
        return new Function2<A, B, R>() {
            public R apply(A a, B b) {
                return Function3.this.apply(a, b, c);
            }
        };
    }
} //~
