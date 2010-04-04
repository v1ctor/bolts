package ru.yandex.bolts.weaving;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;
import org.objectweb.asm.Type;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.function.Function;

/**
 * @author Stepan Koltsov
 */
public class BoltsNamesTest {

    @Test
    public void testNames() throws Exception {
        Method method = CollectionsF.class.getMethod(BoltsNames.P_METHOD.getName(), Cf.x(BoltsNames.P_METHOD.getArgumentTypes()).map(new Function<Type, Class<?>>() {
            public Class<?> apply(Type a) {
                try {
                    return Class.forName(a.getClassName());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).toArray(new Class[0]));
        Assert.assertEquals(Type.getType(method.getReturnType()), BoltsNames.P_METHOD.getReturnType().getClassName());
    }

} //~
