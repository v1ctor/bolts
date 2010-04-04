package ru.yandex.bolts.weaving;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.MapF;

/**
 * @author Stepan Koltsov
 */
public class LambdaTransformer {

    public static final boolean DEBUG = true;

    static Set<String> packagesToSkip = new HashSet<String>();

    static {
        packagesToSkip.add("java.");
        packagesToSkip.add("javax.");
        packagesToSkip.add("sun.");
        packagesToSkip.add("$Proxy");
    }

    private boolean isNotToBeInstrumented(String name) {
        for (String prefix : packagesToSkip)
            if (name.startsWith(prefix))
                return true;
        return false;
    }

    private MapF<String, byte[]> lambdasByJavaClassName = Cf.hashMap();

    /**
     * @return <code>null</code> if not needed to be transformed.
     */
    public ClassTransformationResult transform(String javaClassName, byte[] classfileBuffer) {
        if (isNotToBeInstrumented(javaClassName))
            return null;

        //byte[] bs = lambdasByJavaClassName.get(javaClassName);
        //if (bs != null)
        //    return bs;

        ClassReader cr = new ClassReader(classfileBuffer);
        FirstPassVisitor firstPassVisitor = new FirstPassVisitor();
        cr.accept(firstPassVisitor, 0);
        if (!firstPassVisitor.hasLambdas()) {
            //System.err.println("skipping " + javaClassName);
            return null;
        }

        System.err.println("weaving " + javaClassName);

        FetchLambdaInfoVisitor fetchLambdaInfoVisitor = new FetchLambdaInfoVisitor();
        cr.accept(fetchLambdaInfoVisitor, 0);

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = cw;
        if (DEBUG) {
            cv = new CheckClassAdapter(cw);
        }
        SecondPassVisitor secondPassVisitor = new SecondPassVisitor(cv, fetchLambdaInfoVisitor);
        cr.accept(secondPassVisitor, 0);
        return new ClassTransformationResult(cw.toByteArray(), secondPassVisitor.extraClasses);
    }

} //~
