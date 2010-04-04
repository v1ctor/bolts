package ru.yandex.bolts.weaving;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.meta.FunctionType;
import ru.yandex.bolts.weaving.annotation.FunctionParameter;

/**
 * @author Stepan Koltsov
 */
public class BoltsNames {
    public static Type OBJECT_TYPE = Type.getType(Object.class);

    public static Type COLLECTIONSF_TYPE = Type.getType(CollectionsF.class);
    public static Type CF_TYPE = Type.getType(Cf.class);
    public static Type FUNCTION_TYPE = Type.getType(Function.class);
    public static Type FUNCTION_PARAMETER_TYPE = Type.getType(FunctionParameter.class);

    public static final String FUNCTION_PACKAGE_INTERNAL_NAME = FUNCTION_TYPE.getInternalName().replaceFirst("/[^/]*$", "");

    public static Method P_METHOD = new Method("p", OBJECT_TYPE, new Type[0]);

    public static Type functionType(FunctionType functionType) {
        return Type.getObjectType(functionType.fullClassName().replace('.', '/'));
    }

    public static boolean isNewLambdaMethod(Type owner, Method method) {
        return Cf.list(COLLECTIONSF_TYPE, CF_TYPE).contains(owner) && method.equals(P_METHOD);
    }

    // XXX: check annotations
    /*
    public static Option<ReturnType> isFunctionAcceptingMethod(Method method) {
        if (method.getArgumentTypes().length != 1)
            return Option.none();
        if (!method.getName().endsWith("W"))
            return Option.none();
        if (method.getArgumentTypes()[0].equals(OBJECT_TYPE))
            return Option.some(ReturnType.OBJECT);
        else if (method.getArgumentTypes()[0].equals(Type.BOOLEAN_TYPE))
            return Option.some(ReturnType.BOOLEAN);
        else
            return Option.none();
    }
    */

    public static Option<FunctionType> isFunction(Type type) {
        if (type.getSort() != Type.OBJECT)
            return Option.none();
        if (!type.getInternalName().startsWith(FUNCTION_PACKAGE_INTERNAL_NAME + "/"))
            return Option.none();
        return FunctionType.parseSimpleClassName(type.getInternalName().substring(FUNCTION_PACKAGE_INTERNAL_NAME.length() + 1));
    }

    public static Method FUNCTION_APPLY_METHOD = new Method("apply", OBJECT_TYPE, new Type[] { OBJECT_TYPE });

    /*
    public static Method replacementMethod(Method method, FunctionType functionType) {
        Method replacementMethod = new Method(
                method.getName().substring(0, method.getName().length() - 1),
                method.getReturnType(), new Type[] { functionType(functionType) });
        return replacementMethod;
    }
    */

} //~
