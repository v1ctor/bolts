package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
public abstract class FunctionV<A> implements Function1V<A> {

    public abstract void apply(A a);
    
    @SuppressWarnings("unchecked")
    public <B> FunctionV<B> uncheckedCast() {
        return (FunctionV<B>) this;
    }

} //~
