package ru.yandex.bolts.weaving;

import org.objectweb.asm.commons.Method;

import ru.yandex.bolts.function.meta.FunctionType;

/**
 * @author Stepan Koltsov
 */
public class FunctionParameterInfo {
    private final Method markerMethod;
    private final Method implMethod;
    private final FunctionType functionType;
    private final int paramNo;

    public FunctionParameterInfo(Method markerMethod, Method implMethod, int paramNo, FunctionType functionType) {
        this.markerMethod = markerMethod;
        this.implMethod = implMethod;
        this.paramNo = paramNo;
        this.functionType = functionType;
    }

    public Method getMarkerMethod() {
        return markerMethod;
    }

    public Method getImplMethod() {
        return implMethod;
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

    public int getParamNo() {
        return paramNo;
    }

} //~
