package ru.yandex.bolts.weaving;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.commons.Method;

/**
 * @author Stepan Koltsov
 */
public class FirstPassVisitor extends EmptyVisitor {
    private boolean hasLambdas;

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return this;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        if (opcode == Opcodes.INVOKESTATIC) {
            if (BoltsNames.isNewLambdaMethod(Type.getObjectType(owner), new Method(name, desc))) {
                hasLambdas = true;
            }
        }
    }

    public boolean hasLambdas() {
        return hasLambdas;
    }

} //~
