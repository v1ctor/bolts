package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
public abstract class Function<A, R> implements Function1<A, R> {
    
    public abstract R apply(A a);
    
    @SuppressWarnings("unchecked")
    public <B, S> Function<B, S> uncheckedCast() {
        return (Function<B, S>) this;
    }
    
} //~
