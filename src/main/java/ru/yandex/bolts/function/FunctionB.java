package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
public abstract class FunctionB<A> implements Function1B<A> {
    
    public abstract boolean apply(A a);
    
    @SuppressWarnings("unchecked")
    public <B> FunctionB<B> uncheckedCast() {
        return (FunctionB<B>) this;
    }
    
} //~
