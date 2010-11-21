package ru.yandex.bolts.weaving;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;


/**
 * @author Stepan Koltsov
 */
public class LambdaClassFileTransformer implements ClassFileTransformer {

    private final LambdaTransformer transformer = new LambdaTransformer();

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String javaClassName = className.replace('/', '.');

        ClassTransformationResult r = transformer.transform(javaClassName, classfileBuffer);
        if (r == null)
            return null;
        if (true)
            throw new RuntimeException("not yet");
        return r.getTransformedClass();
    }

} //~
