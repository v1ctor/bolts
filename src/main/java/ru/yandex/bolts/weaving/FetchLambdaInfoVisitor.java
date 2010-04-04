package ru.yandex.bolts.weaving;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.commons.Method;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.function.meta.FunctionType;
import ru.yandex.bolts.function.meta.FunctionType.ReturnType;

public class FetchLambdaInfoVisitor extends EmptyVisitor {

    private MethodInfo currentMethod;
    private MapF<Method, MethodInfo> methods = Cf.hashMap();

    private int functionArity;
    private ReturnType returnType;


    private boolean isInLambda() {
        return functionArity > 0;
    }

    public MethodInfo getMethod(Method method) {
        return methods.apply(method);
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
        if (BoltsNames.isNewLambdaMethod(Type.getObjectType(owner), method)) {
            if (functionArity == 0)
                currentMethod.newLambda();
            ++functionArity;
        } else if (isInLambda() && BoltsNames.isFunctionAcceptingMethod(method).isDefined()) {
            returnType = BoltsNames.isFunctionAcceptingMethod(method).get();

            currentMethod.lastLambda().functionType = new FunctionType(functionArity, returnType);

            // reset
            functionArity = 0;
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

} //~
