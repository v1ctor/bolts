package ru.yandex.bolts.weaving;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.commons.Method;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.collection.Option;

public class FetchLambdaInfoVisitor extends EmptyVisitor {

    private final FunctionParameterCache functionParameterCache;


    public FetchLambdaInfoVisitor(FunctionParameterCache functionParameterCache) {
        this.functionParameterCache = functionParameterCache;
    }


    private MethodInfo currentMethod;
    private MapF<Method, MethodInfo> methods = Cf.hashMap();

    private int functionArity;


    private boolean isInLambda() {
        return functionArity > 0;
    }

    public MethodInfo getMethod(Method method) {
        return methods.apply(method);
    }

    private void checkMethod(MethodInfo methodInfo) {
        if (methodInfo.lambdas.length() > 0) {
            if (methodInfo.lastLambda().functionType == null)
                throw new IllegalStateException("method " + currentMethod.getMethod() + " has incomplete lambdas");
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        currentMethod = new MethodInfo(new Method(name, desc));
        methods.put(currentMethod.getMethod(), currentMethod);
        this.functionArity = 0;
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        Method method = new Method(name, desc);
        Option<Integer> p = BoltsNames.isNewLambdaMethod(Type.getObjectType(owner), method);
        if (p.isDefined()) {
            if (functionArity == 0)
                currentMethod.newLambda();
            functionArity = Math.max(functionArity, p.get());
        } else if (isInLambda()) {
            Option<FunctionParameterInfo> impl = functionParameterCache.getFunctionParameterFor(Type.getObjectType(owner), method);
            if (impl.isDefined()) {
                if (functionArity != impl.get().getFunctionType().getArity())
                    throw new IllegalStateException(
                            "wrong number of parameters when calling " + name + ", " +
                            "we have " + functionArity + " params when passing to " + impl.get().getImplMethod()
                            );

                currentMethod.lastLambda().functionType = impl.get().getFunctionType();

                // reset
                functionArity = 0;
            }
        }
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        currentMethod.setInfoForLocal(index, name, Type.getType(desc));
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        if (isInLambda())
            currentMethod.accessLocalFromLambda(var);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        if (isInLambda())
            currentMethod.accessLocalFromLambda(var);
    }



    void check() {
        for (MethodInfo methodInfo : methods.values())
            checkMethod(methodInfo);
    }
} //~
