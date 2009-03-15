package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
public abstract class FunctionI<A> implements Function1I<A> {

    public abstract int apply(A a);
    
    @SuppressWarnings("unchecked")
    public <B> FunctionI<B> uncheckedCast() {
        return (FunctionI<B>) this;
    }

} //~
