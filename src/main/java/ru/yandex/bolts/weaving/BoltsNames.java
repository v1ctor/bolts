package ru.yandex.bolts.weaving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Type functionType(FunctionType functionType) {
        return Type.getObjectType(functionType.fullClassName().replace('.', '/'));
    }

    private static final Pattern P_METHOD_NAME_PATTERN = Pattern.compile("p(\\d+)");

    public static Option<Integer> isNewLambdaMethod(Type owner, Method method) {
        if (!Cf.list(COLLECTIONSF_TYPE, CF_TYPE).contains(owner))
            return Option.none();
        if (!method.getReturnType().equals(OBJECT_TYPE) || method.getArgumentTypes().length != 0)
            return Option.none();
        if (method.getName().equals("p"))
            return Option.some(1);
        Matcher m = P_METHOD_NAME_PATTERN.matcher(method.getName());
        if (!m.matches())
            return Option.none();
        return Option.some(Integer.parseInt(m.group(1)));
    }

    public static Option<FunctionType> isFunction(Type type) {
        if (type.getSort() != Type.OBJECT)
            return Option.none();
        if (!type.getInternalName().startsWith(FUNCTION_PACKAGE_INTERNAL_NAME + "/"))
            return Option.none();
        return FunctionType.parseSimpleClassName(type.getInternalName().substring(FUNCTION_PACKAGE_INTERNAL_NAME.length() + 1));
    }

    public static Method FUNCTION_APPLY_METHOD = new Method("apply", OBJECT_TYPE, new Type[] { OBJECT_TYPE });

} //~
