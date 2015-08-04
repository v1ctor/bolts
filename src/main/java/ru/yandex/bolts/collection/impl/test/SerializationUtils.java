package ru.yandex.bolts.collection.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


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

    public static void assertSerializedDeserializedToSame(Object o) {
        Object r = serializeDeserialize(o);
        assertSame(o, r);
    }

    public static void assertSerializedDeserializedToEqual(Object o) {
        Object r = serializeDeserialize(o);
        assertEquals(o, r);
    }

} //~
