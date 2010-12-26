package ru.yandex.bolts.methodFunction;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objenesis.ObjenesisStd;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.internal.ReflectionException;
import ru.yandex.bolts.internal.ReflectionUtils;
import ru.yandex.bolts.internal.Validate;

/**
 * @author Stepan Koltsov
 */
public class ProxyGenerator {
    private final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
    private final ClassVisitor cv;

    {
        ClassVisitor cv = cw;
        if (FunctionBuilder.DEBUG) {
            cv = new CheckClassAdapter(cw, false);
        }
        this.cv = cv;
    }

    private final Class<Object> clazz;
    private final ListF<java.lang.reflect.Method> methodsToCatch;

    private final Type type;
    private final Type proxyType;

    @SuppressWarnings("unchecked")
    private ProxyGenerator(Class<?> clazz) {
        Validate.isTrue((clazz.getModifiers() & Modifier.FINAL) == 0, "cannot inherit from final class " + clazz);

        this.clazz = (Class<Object>) clazz;
        this.methodsToCatch = findMethods();

        this.type = Type.getType(clazz);
        if (type.getInternalName().startsWith("java")) {
            this.proxyType = Type.getObjectType(Type.getType(ProxyGenerator.class).getInternalName() + "$" + ReflectionUtils.getSimpleName(type.getClassName()));
        } else {
            this.proxyType = Type.getObjectType(type.getInternalName() + "$XX");
        }
    }

    private ListF<java.lang.reflect.Method> findMethods() {
        return Cf.x(clazz.getMethods()).filter(ReflectionUtils.isFinalF().notF()); // XXX: more methods
    }

    private Tuple2<Object, ListF<java.lang.reflect.Method>> generate() {
        cv.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, proxyType.getInternalName(), null, type.getInternalName(), null);

        generateConstructor();

        for (Tuple2<java.lang.reflect.Method, Integer> t : methodsToCatch.zipWithIndex()) {
            generateMethod(t._1, t._2);
        }

        cv.visitEnd();

        Class<Object> proxyClass = ReflectionUtils.defineClass(ReflectionUtils.getAnyClassLoader(clazz), cw.toByteArray());
        ObjenesisStd objenesisStd = new ObjenesisStd(false);
        Object proxy = objenesisStd.newInstance(proxyClass);
        //Object proxy = proxyClass.newInstance();
        return Tuple2.tuple(proxy, methodsToCatch);
    }

    public static Tuple2<Object, ListF<java.lang.reflect.Method>> generateProxy(Class<?> clazz) {
        try {
            return new ProxyGenerator(clazz).generate();
        } catch (Exception e) {
            throw new ReflectionException("failed to generate proxy for " + clazz + ": " + e, e);
        }
    }

    private void pushDefaultValue(GeneratorAdapter ma, Type type) {
        switch (type.getSort()) {
        case Type.INT:
            ma.push(0);
            break;
        case Type.LONG:
            ma.push(1L);
            break;
        case Type.SHORT:
            ma.push((short) 0);
            break;
        case Type.BYTE:
            ma.push((byte) 0);
            break;
        case Type.FLOAT:
            ma.push(0.0f);
            break;
        case Type.DOUBLE:
            ma.push(0.0);
            break;
        case Type.BOOLEAN:
            ma.push(false);
            break;
        case Type.CHAR:
            ma.push('\0');
            break;
        case Type.ARRAY:
        case Type.OBJECT:
            ma.visitInsn(Opcodes.ACONST_NULL);
            break;
        case Type.VOID:
            break;
        default:
            throw new IllegalStateException("unknown type: " + type);
        }
    }

    public void generateMethod(java.lang.reflect.Method method, int methodIndex) {
        Method asmMethod = Method.getMethod(method);
        MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, asmMethod.getName(), asmMethod.getDescriptor(), null, null);
        GeneratorAdapter ma = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC, asmMethod.getName(), asmMethod.getDescriptor());
        ma.visitCode();

        ma.push(methodIndex);
        ma.loadArgArray();
        ma.invokeStatic(Type.getType(FunctionsForClass.class), new Method("setCurrentMethod", "(I[Ljava/lang/Object;)V"));

        pushDefaultValue(ma, asmMethod.getReturnType());
        ma.returnValue();

        ma.endMethod();
    }

    private Constructor<?> getBestConstructor() {
        Option<Constructor<?>> r = Cf.x(clazz.getDeclaredConstructors())
            .filter(ReflectionUtils.isPublicOrProtectedF())
            .sortBy(ReflectionUtils.getConstructorParameterCountF())
            .firstO();
        if (r.isDefined()) {
            return r.get();
        }
        throw new IllegalStateException("constructors not found in " + clazz);
    }

    private void generateConstructor() {
        Constructor<?> constructor = getBestConstructor();

        MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        GeneratorAdapter ma = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC, "<init>", "()V");

        ma.visitCode();

        ma.loadThis();

        for (Class<?> type : constructor.getParameterTypes()) {
            pushDefaultValue(ma, Type.getType(type));
        }

        ma.invokeConstructor(this.type, Method.getMethod(constructor));

        ma.returnValue();

        ma.endMethod();
    }

} //~
