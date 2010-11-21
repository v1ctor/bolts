package ru.yandex.bolts.weaving.offline;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.weaving.ClassTransformationResult;
import ru.yandex.bolts.weaving.LambdaTransformer;

/**
 * Offline weaver.
 *
 * @author Stepan Koltsov
 */
public class Main {

    private final String src;
    private final String dest;

    private final LambdaTransformer lambdaTransformer = new LambdaTransformer();

    private Main(String src, String dest) {
        this.src = src;
        this.dest = dest;
    }

    private static String join(String src, String suffix) {
        if (suffix.isEmpty())
            return src;
        else if (src.isEmpty())
            return suffix;
        else
            return src + "/" + suffix;
    }

    private void run() {
        transform("");
    }

    private static byte[] read(File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            copy(is, os);
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(is);
        }
    }

    private static void write(File file, byte[] data) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data);
            os.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(os);
        }
    }

    private static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[10000];
        for (;;) {
            int c = is.read(buffer);
            if (c > 0) {
                os.write(buffer, 0, c);
            } else {
                break;
            }
        }
    }

    private static void copy(File src, File dest) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);
            copy(in, out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable == null)
            return;
        try {
            closeable.close();
        } catch (Throwable e) {
        }
    }

    private void mkdirs(File file) {
        if (file.isDirectory())
            return;
        if (!file.mkdirs())
            throw new RuntimeException("failed to mkdirs " + file);
    }

    private void transform(String path) {
        File srcFull = new File(join(src, path));
        File destFull = new File(join(dest, path));
        if (srcFull.isDirectory()) {
            mkdirs(destFull);
            for (String name : srcFull.list()) {
                transform(join(path, name));
            }
        } else if (srcFull.getName().endsWith(".class")) {
            ClassTransformationResult tr = lambdaTransformer.transform(path.replace('/', '.').replaceFirst("\\.class$", ""), read(srcFull));
            if (tr == null) {
                copy(srcFull, destFull);
            } else {
                write(destFull, tr.getTransformedClass());
                for (Tuple2<String, byte[]> t : tr.getExtraClasses()) {
                    write(new File(join(dest, t._1.replace('.', '/') + ".class")), t._2);
                }
            }
        }
    }

    public static void main(String[] args) {
        new Main(args[0], args[1]).run();
    }

} //~
