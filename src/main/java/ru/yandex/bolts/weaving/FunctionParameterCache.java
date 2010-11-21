package ru.yandex.bolts.weaving;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.commons.Method;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.Tuple3;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.meta.FunctionType;

/**
 * @author Stepan Koltsov
 */
public class FunctionParameterCache {

    private class TypeInfo {
        private final Option<Type> superclass;
        private final ListF<Type> interfaces;
        private final MapF<Method, FunctionParameterInfo> functionsByMarker;

        public TypeInfo(Option<Type> superclass, ListF<Type> interfaces, ListF<FunctionParameterInfo> functions) {
            if (superclass == null)
                throw new IllegalArgumentException();
            if (interfaces == null)
                throw new IllegalArgumentException();

            this.superclass = superclass;
            this.interfaces = interfaces;
            this.functionsByMarker = functions.toMapMappingToKey(new Function<FunctionParameterInfo, Method>() {
                public Method apply(FunctionParameterInfo a) {
                    return a.getMarkerMethod();
                }
            });
        }

        public Option<FunctionParameterInfo> getFunctionParameterForMarker(Method method) {
            return functionsByMarker.getO(method);
        }
    }

    private MapF<Type, TypeInfo> classCache = Cf.hashMap();

    private class TypeInfoFinder extends EmptyVisitor {
        private Option<Type> superclass;
        private ListF<Type> interfaces;

        private Method currentMethod;

        private ListF<Tuple2<Method, Integer>> markers = Cf.arrayList();
        private MapF<String, Tuple3<Method, FunctionType, Integer>> impls = Cf.hashMap();

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.superclass = superName != null ? Option.some(Type.getObjectType(superName)) : Option.<Type>none();
            this.interfaces = interfaces != null ? Cf.x(interfaces).map(new Function<String, Type>() {
                public Type apply(String internalName) {
                    return Type.getObjectType(internalName);
                }
            }) : Cf.<Type>list();
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            Method method = new Method(name, desc);

            Type[] argumentTypes = method.getArgumentTypes();

            if (argumentTypes.length == 0)
                return null;

            for (int i = 0; i < argumentTypes.length; ++i) {
                Type arg = argumentTypes[i];
                Option<FunctionType> function = BoltsNames.isFunction(arg);
                if (function.isDefined()) {
                    if (impls.put(method.getName(), Tuple3.tuple(method, function.get(), i)) != null)
                        throw new IllegalStateException("cannot have two methods accepting functions with same name");
                    return null;
                }
            }

            currentMethod = method;
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

        @Override
        public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
            Type.getType(desc);
            if (currentMethod == null)
                throw new IllegalStateException("method may not have two @FunctionParameter anotations");
            if (!desc.equals(BoltsNames.FUNCTION_PARAMETER_TYPE.getDescriptor())) {
                return null;
            }

            markers.add(Tuple2.tuple(currentMethod, parameter));

            return null;
        }

        public TypeInfo getTypeFunctionInfo() {
            return new TypeInfo(superclass, interfaces, markers.map(new Function<Tuple2<Method, Integer>, FunctionParameterInfo>() {
                public FunctionParameterInfo apply(Tuple2<Method, Integer> a) {
                    Method marker = a._1;
                    if (!marker.getName().endsWith("W"))
                        throw new IllegalStateException("got left marker: " + marker);
                    String implName = marker.getName().substring(0, marker.getName().length() - 1);
                    Tuple3<Method, FunctionType, Integer> impl = impls.getOrThrow(implName, "no function for marker: ", marker.getName());

                    if (a._2 != impl._3)
                        throw new IllegalStateException("must be in same position");

                    return new FunctionParameterInfo(marker, impl._1, a._2, impl._2);
                }
            }));
        }
    }

    public Option<FunctionParameterInfo> getFunctionParameterFor(final Type owner, Method method) {
        if (!method.getName().endsWith("W"))
            return Option.none();
        if (LambdaTransformer.isNotToBeInstrumented(owner.getClassName()))
            return Option.none();

        TypeInfo typeInfo = classCache.getOrElseUpdate(owner, new Function0<TypeInfo>() {
            public TypeInfo apply() {
                try {
                    // XXX: better
                    ClassReader cr = new ClassReader(owner.getClassName());
                    TypeInfoFinder f = new TypeInfoFinder();
                    cr.accept(f, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                    return f.getTypeFunctionInfo();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Option<FunctionParameterInfo> r = typeInfo.getFunctionParameterForMarker(method);
        if (r.isEmpty() && typeInfo.superclass.isDefined()) {
            r = getFunctionParameterFor(typeInfo.superclass.get(), method);
        }
        if (r.isEmpty()) {
            for (Type intf : typeInfo.interfaces) {
                if (r.isEmpty())
                    r = getFunctionParameterFor(intf, method);
            }
        }
        return r;

    }
} //~
