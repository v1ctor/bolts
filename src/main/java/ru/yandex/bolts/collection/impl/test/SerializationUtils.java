package ru.yandex.bolts.collection.impl.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Stepan Koltsov
 */
public class SerializationUtils {
    
    public static Object serializeDeserialize(Object o) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(o);
            return new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
} //~
