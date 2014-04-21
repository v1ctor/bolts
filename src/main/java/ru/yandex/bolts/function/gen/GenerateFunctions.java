package ru.yandex.bolts.function.gen;

import java.io.BufferedWriter;
import java.io.FileWriter;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.function.meta.FunctionType;
import ru.yandex.bolts.function.meta.FunctionType.ReturnType;

/**
 * @author Stepan Koltsov
 */
public class GenerateFunctions {
    private static String classNameSimple(int paramCount, ReturnType returnType) {
        return new FunctionType(paramCount, returnType).simpleClassName();
    }

    private static ListF<String> paramTypeLetters(int paramCount) {
        return Cf.range(0, paramCount).map((Integer a) -> "" + (char) ('A' + a));
    }

    private static String returnTypeName(ReturnType returnType) {
        switch (returnType) {
        case BOOLEAN: return "boolean";
        case INT: return "int";
        case VOID: return "void";
        case OBJECT: return "R";
        }
        throw new RuntimeException();
    }

    private static String returnTypeNameObject(ReturnType returnType) {
        switch (returnType) {
        case BOOLEAN: return "Boolean";
        case INT: return "Integer";
        case VOID: return "Object";
        case OBJECT: return "R";
        }
        throw new RuntimeException();
    }

    private static String letter(int i) {
        return "" + (char) + ('A' + i);
    }

    private static class ClassWriter {
        private final int paramCount;
        private final ReturnType returnType;

        private final String className;
        private ListF<String> paramTypeLetters;

        public ClassWriter(int paramCount, ReturnType returnType) {
            this.paramCount = paramCount;
            this.returnType = returnType;

            className = classNameSimple(paramCount, returnType);
            paramTypeLetters = paramTypeLetters(paramCount);
        }

        private ListF<String> allParamTypeLetters() {
            if (returnType == ReturnType.OBJECT)
                return paramTypeLetters.plus("R");
            else
                return paramTypeLetters;
        }

        private String classNameSimpleAfterBind(int i) {
            return classNameSimple(paramCount - 1, returnType);
        }

        private ListF<String> lettersAfterBind(int i) {
            return paramTypeLetters.take(i).plus(paramTypeLetters.drop(i + 1));
        }

        private ListF<String> allLettersAfterBind(int i) {
            if (returnType == ReturnType.OBJECT)
                return lettersAfterBind(i).plus("R");
            else
                return lettersAfterBind(i);
        }

        private String classNameFullAfterBind(int i) {
            return classNameSimpleAfterBind(i) + "<" + allLettersAfterBind(i).mkString(", ") + ">";
        }

        private String classNameFull() {
            return className + "<" + allParamTypeLetters().mkString(", ") + ">";
        }

        public void run() throws Exception {
            FileWriter w0 = new FileWriter(className + ".java");
            BufferedWriter w = new BufferedWriter(w0);

            w.write("package ru.yandex.bolts.function;\n");
            w.write("\n");
            w.write("// this file is generated by " + this.getClass().getName().replaceFirst("\\$.*", "") + "\n");
            w.write("\n");
            w.write("import ru.yandex.bolts.collection.Tuple" + paramCount + ";\n");
            w.write("\n");

            w.write("/**\n");
            w.write(" */\n");
            w.write("@FunctionalInterface\n");
            w.write("public interface " + classNameFull() + " {\n");
            w.write("\n");
            w.write("    " + returnTypeName(returnType) + " apply(");
            w.write(lettersToArgs(paramTypeLetters));
            w.write(");\n");

            w.write("\n");

            for (int i = 0; i < paramCount; ++i) {

                w.write("    default " + classNameFullAfterBind(i) + " bind" + (i + 1) + "(");
                w.write("final " + letter(i) + " " + letter(i).toLowerCase());
                w.write(") {\n");
                w.write("        return " + lettersToLambda(lettersAfterBind(i)) + " -> apply(" + paramTypeLetters.map(Cf.String.toLowerCaseF()).mkString(", ") + ");\n");
                w.write("    }\n");
                w.write("\n");
            }

            w.write("\n");

            for (int i = 0; i < paramCount; ++i) {

                String rt = "Function2<" + classNameFull() + ", " + letter(i) + ", " + classNameFullAfterBind(i) + ">";

                w.write("    static <" + allParamTypeLetters().mkString(", ") + "> " + rt + " bind" + (i + 1) + "F2() {\n");
                w.write("        return (f, " + letter(i).toLowerCase() + ") -> f.bind" + (i + 1) + "(" + letter(i).toLowerCase() + ");\n");
                w.write("    }\n");
                w.write("\n");

                w.write("    default Function<" + letter(i) + ", " + classNameFullAfterBind(i) + "> bind" + (i + 1) + "F() {\n");
                w.write("        return " + className + ".<" + allParamTypeLetters().mkString(", ") + ">bind" + (i + 1) + "F2().bind1(this);\n");
                w.write("    }\n");
                w.write("\n");
            }

            if (paramCount > 1) {
                String rt = "Function<Tuple" + paramCount + "<" + paramTypeLetters.mkString(", ") + ">, " + returnTypeNameObject(returnType) + ">";
                w.write("    default " + rt + " asFunction() {\n");
                if (returnType == ReturnType.VOID) {
                    w.write("        return t -> {\n");
                    w.write("            apply(" + Cf.range(1, paramCount + 1).map(Cf.Object.toStringF()).map(Cf.String.plusF().bind1("t._")).mkString(", ") + ");\n");
                    w.write("            return null;\n");
                    w.write("        };\n");
                } else {
                    w.write("        return t -> apply(" + Cf.range(1, paramCount + 1).map(Cf.Object.toStringF()).map(Cf.String.plusF().bind1("t._")).mkString(", ") + ");\n");
                }
                w.write("    }\n");
                w.write("\n");
            }

            if (paramCount > 1 && returnType != ReturnType.OBJECT) {
                String rt = classNameSimple(1, returnType) + "<Tuple" + paramCount + "<" + paramTypeLetters.mkString(", ") + ">>";
                w.write("    default " + rt + " as" + classNameSimple(1, returnType) + "() {\n");
                w.write("        return t -> apply(" + Cf.range(1, paramCount + 1).map(Cf.Object.toStringF()).map(Cf.String.plusF().bind1("t._")).mkString(", ") + ");\n");
                w.write("    }\n");
                w.write("\n");
            }

            if (returnType != ReturnType.OBJECT) {
                String rt = classNameSimple(paramCount, ReturnType.OBJECT) + "<" + paramTypeLetters.mkString(", ") + ", " + returnTypeNameObject(returnType) + ">";
                w.write("    default " + rt + " as" + classNameSimple(paramCount, ReturnType.OBJECT) + "() {\n");
                if (returnType == ReturnType.VOID) {
                    w.write("        return (" + paramTypeLetters.map(Cf.String.toLowerCaseF()).mkString(", ") + ") -> {\n");
                    w.write("            apply(" + paramTypeLetters.map(Cf.String.toLowerCaseF()).mkString(", ") + ");\n");
                    w.write("            return null;\n");
                    w.write("        };\n");
                }   else {
                    w.write("        return (" + paramTypeLetters.map(Cf.String.toLowerCaseF()).mkString(", ") + ") -> apply(" + paramTypeLetters.map(Cf.String.toLowerCaseF()).mkString(", ") + ");\n");
                }
                w.write("    }\n");
                w.write("\n");
            }

            {
                String letters1 = "<" + allParamTypeLetters().map(Cf.String.plusF().bind2("1")).mkString(", ") + ">";
                w.write("    @SuppressWarnings(\"unchecked\")\n");
                w.write("    default " + letters1 + " " + className + letters1 + " uncheckedCast() {\n");
                w.write("        return (" + className + letters1 + ") this;\n");
                w.write("    }\n");
                w.write("\n");
            }

            w.write("} //~\n");

            w.flush();
            w0.close();
        }
    }

    private void generate(int paramCount, ReturnType returnType) throws Exception {
        new ClassWriter(paramCount, returnType).run();
    }

    private static String lettersToArgs(ListF<String> paramTypeLetters) {
        return paramTypeLetters.zipWith(Cf.String.toLowerCaseF()).mkString(", ", " ");
    }

    private static String lettersToLambda(ListF<String> paramTypeLetters) {
        String lambda = paramTypeLetters.map(Cf.String.toLowerCaseF()).mkString(", ");
        if (paramTypeLetters.size() > 1) {
            return "(" + lambda + ")";
        }
        return lambda;
    }

    private void generate(int paramCount) throws Exception {
        for (ReturnType returnType : ReturnType.values()) {
            generate(paramCount, returnType);
        }
    }

    private void run() throws Exception {
        for (int i = 0; i <= 3; ++i) {
            generate(i);
        }
    }

    public static void main(String[] args) throws Exception {
        new GenerateFunctions().run();
    }

} //~
