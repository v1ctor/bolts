package ru.yandex.bolts.function.forhuman;

import ru.yandex.bolts.function.Function2V;

/**
 * @author Stepan Koltsov
 */
public abstract class F2V<A, B> implements Function2V<A, B> {
    public Operation<B> bind1(final A a) {
        return new Operation<B>() {
            @Override
            public void execute(B b) {
                F2V.this.apply(a, b);
            }
        };
    }
    
    public Operation<A> bind2(final B b) {
        return new Operation<A>() {
            @Override
            public void execute(A a) {
                F2V.this.apply(a, b);
            }
        };
    }

} //~
