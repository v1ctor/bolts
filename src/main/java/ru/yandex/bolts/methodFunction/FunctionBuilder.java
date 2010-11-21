package ru.yandex.bolts.methodFunction;

import java.util.Map;
import java.util.WeakHashMap;

import com.sun.istack.internal.Nullable;

import ru.yandex.bolts.function.Function;

/**
 * @author Stepan Koltsov
 */
public class FunctionBuilder {

    public static final boolean DEBUG = true;

    private static final Map<Class<?>, FunctionsForClass> cache
        = new WeakHashMap<Class<?>, FunctionsForClass>();

    /** Short for param */
    public static <A> A p(Class<A> clazz) {
        return getProxy(clazz, null);
    }

    @SuppressWarnings("unchecked")
    public static synchronized <A> A p(A p) {
        return getProxy((Class<A>) p.getClass(), p);
    }

    // XXX: global lock
    @SuppressWarnings("unchecked")
    private static synchronized <A> A getProxy(Class<A> clazz, @Nullable A thiz) {
        FunctionsForClass ffc = cache.get(clazz);
        if (ffc == null) {
            ffc = new FunctionsForClass(clazz);
            cache.put(clazz, ffc);
        }
        FunctionsForClass.setCurrent(ffc, thiz);
        return (A) ffc.getProxy();
    }

    public static <A, B> Function<A, B> function(B b) {
        return FunctionsForClass.getCurrentFunction().uncheckedCast();
    }

} //~
