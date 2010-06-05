package ru.yandex.bolts.weaving;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.util.CheckClassAdapter;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.Tuple2List;
import ru.yandex.bolts.weaving.MethodInfo.LambdaInfo;

public class SecondPassVisitor extends ClassAdapter {

    private final FetchLambdaInfoVisitor fetchLambdaInfoVisitor;
    private final FunctionParameterCache functionParameterCache;

    private Type type;

    private int currentLambdaId;

    Tuple2List<String, byte[]> extraClasses = Tuple2List.arrayList();

    public SecondPassVisitor(ClassVisitor cv, FetchLambdaInfoVisitor fetchLambdaInfoVisitor, FunctionParameterCache functionParameterCache) {
        super(cv);
        this.fetchLambdaInfoVisitor = fetchLambdaInfoVisitor;
        this.functionParameterCache = functionParameterCache;
    }

    private class SecondPassMethodVisitor extends MethodAdapter {
        private final MethodInfo methodInfo;
        private LambdaInfo currentLambda;
        private int currentLambdaParam;
        private final IteratorF<LambdaInfo> lambdasIterator;

        private ClassWriter lambdaWriter0;
        private ClassVisitor lambdaWriter;
        private MethodVisitor originalMethodWriter;

        public SecondPassMethodVisitor(MethodVisitor mv, MethodInfo methodInfo) {
            super(mv);
            this.methodInfo = methodInfo;
            this.originalMethodWriter = mv;
            this.lambdasIterator = methodInfo.lambdas.iterator();
        }


        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            Method method = new Method(name, desc);
            if (BoltsNames.isNewLambdaMethod(Type.getObjectType(owner), method).isDefined()) {
                if (!isInLambda()) {
                    currentLambda = lambdasIterator.next();
                    currentLambdaParam = 0;
                    nextLambdaId();

                    createLambdaClass();
                    createLambdaConstructor();
                    createApplyMethodAndRedirectMethodVisitorToIt();
                }
                mv.visitVarInsn(Opcodes.ALOAD, ++currentLambdaParam);

            } else if (isInLambda()) {
                Option<FunctionParameterInfo> function = functionParameterCache.getFunctionParameterFor(Type.getObjectType(owner), method);
                if (function.isEmpty()) {
                    super.visitMethodInsn(opcode, owner, name, desc);
                    return;
                }
                returnFromApply();
                endLambdaClass();
                restoreOriginalMethodWriterAndInstantiateTheLambda();

                Method replacementMethod = function.get().getImplMethod();
                super.visitMethodInsn(opcode, owner, replacementMethod.getName(), replacementMethod.getDescriptor());
            } else {
                super.visitMethodInsn(opcode, owner, name, desc);
            }
        }

        @Override
        public void visitIincInsn(int var, int increment) {
            if (methodInfo.isLocalAccessedFromLambda(var))
                incrementInArray(var, increment);
            else
                super.visitIincInsn(var, increment);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            if (methodInfo.isLocalAccessedFromLambda(var)) {
                Type type = methodInfo.getTypeOfLocal(var);
                if (isThis(var)) {
                    if (isInLambda()) {
                        mv.visitVarInsn(Opcodes.ALOAD, var);
                        mv.visitFieldInsn(Opcodes.GETFIELD, currentLambdaClass().getInternalName(), lambdaFieldNameForLocal(var), Type.getDescriptor(Object.class));
                        mv.visitTypeInsn(Opcodes.CHECKCAST, type.getInternalName());
                    } else {
                        super.visitIntInsn(opcode, var);
                    }

                } else {
                    loadArrayFromLocalOrLambda(var, type);
                    accessFirstArrayElement(opcode, type);
                }
            } else {
                super.visitVarInsn(opcode, var);
            }
        }

        @Override
        public void visitCode() {
            super.visitCode();
            initAccessedLocalsAndParametersAsArrays();
        }


        void incrementInArray(int var, int increment) {
            loadArrayFromLocalOrLambda(var, methodInfo.getTypeOfLocal(var));
            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitInsn(Opcodes.DUP2);
            mv.visitInsn(Opcodes.IALOAD);
            if (increment >= Byte.MIN_VALUE && increment <= Byte.MAX_VALUE)
                mv.visitIntInsn(Opcodes.BIPUSH, increment);
            else if (increment >= Short.MIN_VALUE && increment <= Short.MAX_VALUE)
                mv.visitIntInsn(Opcodes.SIPUSH, increment);
            else
                mv.visitLdcInsn(increment);
            mv.visitInsn(Opcodes.IADD);
            mv.visitInsn(Opcodes.IASTORE);
        }

        void loadArrayFromLocalOrLambda(int operand, Type type) {
            if (isInLambda()) {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, currentLambdaClass().getInternalName(), lambdaFieldNameForLocal(operand), Type.getDescriptor(Object.class));
                mv.visitTypeInsn(Opcodes.CHECKCAST, "[" + type.getDescriptor());
            } else {
                mv.visitVarInsn(Opcodes.ALOAD, operand);
            }
        }

        void accessFirstArrayElement(int opcode, Type type) {
            if (opcode >= Opcodes.ISTORE && opcode <= Opcodes.ASTORE) {
                mv.visitInsn(Opcodes.SWAP);
                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitInsn(Opcodes.SWAP);
                mv.visitInsn(type.getOpcode(Opcodes.IASTORE));
            } else {
                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitInsn(type.getOpcode(Opcodes.IALOAD));
            }
        }


        String lambdaFieldNameForLocal(int local) {
            return isThis(local) ? "this$0" : "val$" + local;
        }

        boolean isMethodParameter(int operand) {
            return operand <= methodInfo.getMethod().getArgumentTypes().length;
        }

        boolean isThis(int var) {
            return var == 0;
        }

        void initAccessedLocalsAndParametersAsArrays() {
            for (int local : methodInfo.getAccessedLocals())
                if (!isThis(local))
                    initArray(local, methodInfo.getTypeOfLocal(local));
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            super.visitLineNumber(line, start);
        }

        @Override
        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            if (methodInfo.isLocalAccessedFromLambda(index))
                desc = Type.getDescriptor(Object.class); // XXX: [desc
            super.visitLocalVariable(name, desc, signature, start, end, index);
        }

        void newArray(Type type) {
            int typ;
            switch (type.getSort()) {
            case Type.BOOLEAN:
                typ = Opcodes.T_BOOLEAN;
                break;
            case Type.CHAR:
                typ = Opcodes.T_CHAR;
                break;
            case Type.BYTE:
                typ = Opcodes.T_BYTE;
                break;
            case Type.SHORT:
                typ = Opcodes.T_SHORT;
                break;
            case Type.INT:
                typ = Opcodes.T_INT;
                break;
            case Type.FLOAT:
                typ = Opcodes.T_FLOAT;
                break;
            case Type.LONG:
                typ = Opcodes.T_LONG;
                break;
            case Type.DOUBLE:
                typ = Opcodes.T_DOUBLE;
                break;
            default:
                mv.visitTypeInsn(Opcodes.ANEWARRAY, type.getInternalName());
                return;
            }
            mv.visitIntInsn(Opcodes.NEWARRAY, typ);
        }

        void initArray(int operand, Type type) {
            mv.visitInsn(Opcodes.ICONST_1);
            newArray(type);

            if (isMethodParameter(operand)) {
                mv.visitInsn(Opcodes.DUP);
                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitVarInsn(type.getOpcode(Opcodes.ILOAD), operand);
                mv.visitInsn(type.getOpcode(Opcodes.IASTORE));
            }

            mv.visitVarInsn(Opcodes.ASTORE, operand);
        }







        private void createLambdaClass() {
            if (isInLambda())
                throw new IllegalStateException();
            lambdaWriter0 = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            if (LambdaTransformer.DEBUG)
                lambdaWriter = new CheckClassAdapter(lambdaWriter0, false);
            else
                lambdaWriter = lambdaWriter0;
            // XXX: better name
            // XXX: another function types
            lambdaWriter.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, currentLambdaClass().getInternalName(), null, currentLambda.functionTypeType().getInternalName(), null);
            lambdaWriter.visitOuterClass(type.getInternalName(), methodInfo.getMethod().getName(), methodInfo.getMethod().getDescriptor());
            lambdaWriter.visitInnerClass(currentLambdaClass().getInternalName(), null, null, 0);
        }

        private void createLambdaConstructor() {
            ListF<Type> parameterTypes = Cf.arrayList();
            for (int local : currentLambda.accessedLocals) {
                Type fieldType = Type.getType(Object.class);
                lambdaWriter.visitField(Opcodes.ACC_FINAL | Opcodes.ACC_SYNTHETIC, lambdaFieldNameForLocal(local), fieldType.getInternalName(), null, null).visitEnd();
                parameterTypes.add(fieldType);
            }

            Method cons = new Method("<init>", Type.VOID_TYPE, parameterTypes.toArray(Type.class));
            mv = lambdaWriter.visitMethod(Opcodes.ACC_PUBLIC, cons.getName(), cons.getDescriptor(), null, null);

            mv.visitCode();

            int i = 1;
            for (int local : currentLambda.accessedLocals) {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ALOAD, i++);
                mv.visitFieldInsn(Opcodes.PUTFIELD, currentLambdaClass().getInternalName(), lambdaFieldNameForLocal(local), Type.getDescriptor(Object.class));
            }

            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, currentLambda.functionTypeType().getInternalName(), "<init>", "()V");
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        private void createApplyMethodAndRedirectMethodVisitorToIt() {

            mv = lambdaWriter.visitMethod(Opcodes.ACC_PUBLIC,
                    currentLambda.applyMethod().getName(), currentLambda.applyMethod().getDescriptor(), null, null);
            mv.visitCode();
        }

        private void restoreOriginalMethodWriterAndInstantiateTheLambda() {
            if (mv == originalMethodWriter)
                throw new IllegalStateException();
            mv = originalMethodWriter;
            mv.visitTypeInsn(Opcodes.NEW, currentLambdaClass().getInternalName());
            mv.visitInsn(Opcodes.DUP);

            // XXX: add constructor parameter
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, currentLambdaClass().getInternalName(), "<init>", "()V");
        }

        void returnFromApply() {
            switch (currentLambda.functionType.getReturnType()) {
            case OBJECT:
                mv.visitInsn(Opcodes.ARETURN);
                break;
            case BOOLEAN:
            case INT:
                mv.visitInsn(Opcodes.IRETURN);
                break;
            case VOID:
                mv.visitInsn(Opcodes.RETURN);
                break;
            default:
                throw new IllegalStateException();
            }

            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        void endLambdaClass() {
            lambdaWriter.visitEnd();
            cv.visitInnerClass(currentLambdaClass().getInternalName(), null, null, 0);

            byte[] bs = lambdaWriter0.toByteArray();
            extraClasses.put(currentLambdaClass().getClassName(), bs);

            lambdaWriter = null;
        }

        void nextLambdaId() {
            currentLambdaId++;
        }

        Type currentLambdaClass() {
            if (currentLambda == null)
                throw new IllegalStateException("not in lambda");
            String lambdaClass = currentLambda.functionType.simpleClassName();
            return Type.getObjectType(type.getInternalName() + "$" + lambdaClass + "_" + currentLambdaId);
        }

        private boolean isInLambda() {
            return lambdaWriter != null;
        }

    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.type = Type.getObjectType(name);
    }



    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor superVisitor = super.visitMethod(access, name, desc, signature, exceptions);

        MethodInfo methodInfo = fetchLambdaInfoVisitor.getMethod(new Method(name, desc));
        if (methodInfo.lambdas.isEmpty())
            return superVisitor;

        //System.err.println("weaving method " + name);
        return new SecondPassMethodVisitor(superVisitor, methodInfo);
    }

}
