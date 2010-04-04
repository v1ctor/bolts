package ru.yandex.bolts.function.meta;


/**
 * @author Stepan Koltsov
 */
public class FunctionType {

    public enum ReturnType {
        OBJECT(""),
        BOOLEAN("B"),
        INT("I"),
        VOID("V"),
        ;

        private final String suffix;

        private ReturnType(String suffix) {
            this.suffix = suffix;
        }
    }

    private final int arity;
    private final ReturnType returnType;

    public FunctionType(int arity, ReturnType returnType) {
        if (arity < 0)
            throw new IllegalArgumentException();
        if (returnType == null)
            throw new IllegalArgumentException();

        this.arity = arity;
        this.returnType = returnType;
    }

    public String simpleClassName() {
        if (arity == 1 && returnType == ReturnType.OBJECT)
            return "Function";
        else
            return "Function" + arity + returnType.suffix;
    }

    public String fullClassName() {
        return "ru.yandex.bolts.function." + simpleClassName();
    }
} //~
