package ru.yandex.bolts.methodFunction;

/**
 * @author Stepan Koltsov
 */
public class Invocation {

    FunctionsForClass ffc;
    Object thiz;
    int currentMethodIndex = -1;
    Object[] currentMethodArgs;

    public static final ThreadLocal<Invocation> current = new ThreadLocal<Invocation>();
} //~
