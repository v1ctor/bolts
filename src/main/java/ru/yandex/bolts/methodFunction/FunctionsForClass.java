package ru.yandex.bolts.methodFunction;

import java.lang.reflect.Method;

import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2B;
import ru.yandex.bolts.internal.NotImplementedException;

public class FunctionsForClass {
    private final Object proxy;
    private final ListF<Method> methods;

    public FunctionsForClass(Class<?> clazz) {
        Tuple2<Object, ListF<Method>> t = ProxyGenerator.generateProxy(clazz);
        this.proxy = t._1;
        this.methods = t._2;
    }

    public Object getProxy() {
        return proxy;
    }

    public static void setCurrent(FunctionsForClass ffc, Object thiz) {
        Invocation invocation = new Invocation();
        invocation.ffc = ffc;
        invocation.thiz = thiz;
        Invocation.current.set(invocation);
    }

    public static void setCurrentMethod(int index, Object[] args) {
        Invocation invocation = Invocation.current.get();
        invocation.currentMethodIndex = index;
        invocation.currentMethodArgs = args;
    }

    public static <A, B> Function<A, B> getCurrentFunction() {
        try {
            Invocation invocation = Invocation.current.get();
            FunctionsForClass ffc = invocation.ffc;
            if (invocation.thiz != null) {
                Function2<Object, Object, Object> f2 = ffc.getCurrentFunctionI2().uncheckedCast();
                return f2.bind1(invocation.thiz).uncheckedCast();
            } else {
                if (invocation.currentMethodArgs.length == 0) {
                    return ffc.getCurrentFunctionI().uncheckedCast();
                } else if (invocation.currentMethodArgs.length == 1) {
                    return ffc.getCurrentFunctionI2().uncheckedCast().bind2(invocation.currentMethodArgs[0]).uncheckedCast();
                } else {
                    throw new NotImplementedException();
                }
            }
        } finally {
            Invocation.current.remove();
        }
    }

    public static <A> Function1B<A> getCurrentFunction1B() {
        try {
            Invocation invocation = Invocation.current.get();
            FunctionsForClass ffc = invocation.ffc;
            if (invocation.thiz != null) {
                Function2B<Object, Object> f2 = ffc.getCurrentFunctionI2B().uncheckedCast();
                return f2.bind1(invocation.thiz).uncheckedCast();
            } else {
                if (invocation.currentMethodArgs.length == 0) {
                    return ffc.getCurrentFunctionI1B().uncheckedCast();
                } else if (invocation.currentMethodArgs.length == 2) {
                    return ffc.getCurrentFunctionI2B().uncheckedCast().bind1(invocation.currentMethodArgs[0]).uncheckedCast();
                } else {
                    throw new NotImplementedException();
                }
            }
        } finally {
            Invocation.current.remove();
        }
    }

    private Integer getCurrentMethodIndex() {
        return Invocation.current.get().currentMethodIndex;
    }

    private Function<?, ?> getCurrentFunctionI() {
        int index = getCurrentMethodIndex();
        return Function.wrap(methods.get(index));
    }

    private Function2<?, ?, ?> getCurrentFunctionI2() {
        int index = getCurrentMethodIndex();
        return Function2.wrap(methods.get(index));
    }

    private Function1B<?> getCurrentFunctionI1B() {
        int index = getCurrentMethodIndex();
        return Function1B.wrap(methods.get(index));
    }

    private Function2B<?, ?> getCurrentFunctionI2B() {
        int index = getCurrentMethodIndex();
        return Function2B.wrap(methods.get(index));
    }

} //~
