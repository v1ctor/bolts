package ru.yandex.bolts.weaving;

import static org.objectweb.asm.Type.getArgumentTypes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.function.meta.FunctionType;

class MethodInfo {
    private final Method method;

    public MethodInfo(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    Map<Integer, LocalInfo> accessedLocalsByIndex = new HashMap<Integer, LocalInfo>();
    ListF<MethodInfo.LambdaInfo> lambdas = Cf.arrayList();

    void accessLocalFromLambda(int operand) {
        LocalInfo local = accessedLocalsByIndex.get(operand);
        if (local == null) {
            local = new LocalInfo();
            accessedLocalsByIndex.put(operand, local);
        }
        lastLambda().accessedLocals.add(operand);
    }

    void newLambda() {
        lambdas.add(new LambdaInfo());
    }

    MethodInfo.LambdaInfo lastLambda() {
        return lambdas.last();
    }

    Iterator<MethodInfo.LambdaInfo> lambdas() {
        return lambdas.iterator();
    }

    void setInfoForLocal(int index, String name, Type type) {
        LocalInfo localInfo = accessedLocalsByIndex.get(index);
        if (localInfo != null) {
            localInfo.name = name;
            localInfo.type = type;
        }
    }

    Type getTypeOfLocal(int var) {
        return accessedLocalsByIndex.get(var).type;
    }

    String getNameOfLocal(int var) {
        return accessedLocalsByIndex.get(var).name;
    }

    boolean isLocalAccessedFromLambda(int index) {
        return accessedLocalsByIndex.containsKey(index);
    }

    Set<Integer> getAccessedArguments() {
        Set<Integer> accessedArguments = new HashSet<Integer>();
        for (int i = 1; i <= getArgumentTypes(method.getDescriptor()).length; i++)
            if (accessedLocalsByIndex.keySet().contains(i))
                accessedArguments.add(i);
        return accessedArguments;
    }

    String getAccessedArgumentsAndLocalsString(Set<Integer> accessedLocals) {
        accessedLocals = new HashSet<Integer>(accessedLocals);
        Set<Integer> accessedArguments = getAccessedArguments();
        accessedArguments.retainAll(accessedLocals);
        accessedLocals.removeAll(accessedArguments);
        return "(" + getAccessedLocalsString(accessedArguments) + ")" + "[" + getAccessedLocalsString(accessedLocals) + "]";
    }

    String getAccessedLocalsString(Set<Integer> accessedLocals) {
        String s = "";
        for (Iterator<Integer> i = accessedLocals.iterator(); i.hasNext();) {
            s += getNameOfLocal(i.next());
            if (i.hasNext())
                s += ", ";
        }
        return s;
    }

    static class LocalInfo {
        String name;
        int index;
        Type type;
    }

    static class LambdaInfo {
        FunctionType functionType;
        Set<Integer> accessedLocals = new HashSet<Integer>();

        public Type functionTypeType() {
            return Type.getObjectType(functionType.fullClassName().replace('.', '/'));
        }

        public Type functionTypeReturnType() {
            switch (functionType.getReturnType()) {
            case OBJECT:
                return BoltsNames.OBJECT_TYPE;
            case BOOLEAN:
                return Type.BOOLEAN_TYPE;
            case INT:
                return Type.INT_TYPE;
            case VOID:
                return Type.VOID_TYPE;
            default:
                throw new IllegalStateException();
            }
        }

        public Method applyMethod() {
            return new Method("apply", functionTypeReturnType(), Cf.repeat(BoltsNames.OBJECT_TYPE, functionType.getArity()).toArray(Type.class));
        }
    }

    Set<Integer> getAccessedLocals() {
        return accessedLocalsByIndex.keySet();
    }
}
