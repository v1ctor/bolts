package ru.yandex.bolts.weaving;

import java.lang.reflect.Method;

import junit.framework.Assert;
import org.junit.Test;
import org.objectweb.asm.Type;

import ru.yandex.bolts.collection.CollectionsF;

/**
 * @author Stepan Koltsov
 */
public class BoltsNamesTest {

    private static org.objectweb.asm.commons.Method asmMethod(Method method) {
        return org.objectweb.asm.commons.Method.getMethod(method);
    }

    @Test
    public void testNames() throws Exception {
        Type type = Type.getType(CollectionsF.class);
        Assert.assertEquals(1, BoltsNames.isNewLambdaMethod(type, asmMethod(CollectionsF.class.getMethod("p", new Class[0]))).get().intValue());
        Assert.assertEquals(1, BoltsNames.isNewLambdaMethod(type, asmMethod(CollectionsF.class.getMethod("p1", new Class[0]))).get().intValue());
        Assert.assertEquals(2, BoltsNames.isNewLambdaMethod(type, asmMethod(CollectionsF.class.getMethod("p2", new Class[0]))).get().intValue());
        Assert.assertEquals(3, BoltsNames.isNewLambdaMethod(type, asmMethod(CollectionsF.class.getMethod("p3", new Class[0]))).get().intValue());
        Assert.assertTrue(BoltsNames.isNewLambdaMethod(type, asmMethod(CollectionsF.class.getMethod("list", new Class[0]))).isEmpty());
        Assert.assertTrue(BoltsNames.isNewLambdaMethod(type, asmMethod(Object.class.getMethod("toString", new Class[0]))).isEmpty());
    }

} //~
