package ru.yandex.bolts.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;

/**
 * @author Stepan Koltsov
 */
public class ReflectionUtils {

    public static RuntimeException translate(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new ReflectionException(e);
        }
    }

    public static Function1B<Method> isFinalF() {
        return method -> (method.getModifiers() & Modifier.FINAL) != 0;
    }

    @SuppressWarnings("unchecked")
    public static <A> Class<A> defineClass(ClassLoader classLoader, final byte[] classData) {
        try {
            return (Class<A>) new ClassLoader(classLoader) {
                public Class<?> defineClass() {
                    return defineClass(null, classData, 0, classData.length);
                }
            }.defineClass();
        } catch (VerifyError e) {
            throw new ReflectionException("failed to define class: " + e, e);
        }
    }

    public static ClassLoader getAnyClassLoader(Class<?> clazz) {
        ClassLoader r;

        r = clazz.getClassLoader();
        if (r != null)
            return r;

        r = Thread.currentThread().getContextClassLoader();
        if (r != null)
            return r;

        r = ClassLoader.getSystemClassLoader();
        if (r != null)
            return r;

        throw new IllegalStateException("unable to find any classloader");
    }

    public static Function<Constructor<?>, Integer> getConstructorParameterCountF() {
        return constructor -> constructor.getParameterTypes().length;
    }

    public static Function1B<Constructor<?>> isPublicOrProtectedF() {
        return c -> (c.getModifiers() & (Modifier.PUBLIC | Modifier.PROTECTED)) != 0;
    }

    public static String getSimpleName(String className) {
        return className.replaceFirst(".*\\.", "");
    }

    public static Object invoke(Method method, Object thiz, Object... args) {
        try {
            return method.invoke(thiz, args);
        } catch (Exception e) {
            throw new ReflectionException("failed to invoke " + method + ": " + e, e);
        }
    }

} //~
