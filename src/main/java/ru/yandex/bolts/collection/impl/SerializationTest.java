package ru.yandex.bolts.collection.impl;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.impl.test.SerializationUtils;
import junit.framework.TestCase;

public class SerializationTest extends TestCase {

    private static <T> ListF<T> addInPlace(ListF<T> list, T... elements) {
        for (T element: elements) {
            list.add(element);
        }
        return list;
    }

    public void testLists() {
        SerializationUtils.assertSerializedDeserializedToSame(Cf.list());
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.list(4));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.list(1, 3));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.list(1, 3, 7));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.list(1, 3, 7, 2));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.list(1, 3, 7, 2, 9));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.list(1, 3, 7, 2, 9, 2));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.list(1, 3, 7, 2, 9, 2, 5));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.list(1, 3, 7, 2, 9, 2, 5, 7));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.list(1, 3, 7, 2, 9, 2, 5, 7, 8));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.list(1, 3, 7, 2, 9, 2, 5, 7, 8));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.arrayList());
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.arrayList(1));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.arrayList(1, 4));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.arrayList(1, 4, 8));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.linkedList());
        SerializationUtils.assertSerializedDeserializedToEqual(addInPlace(Cf.linkedList(), 2, 3));
    }

    public void testSets() {
        SerializationUtils.assertSerializedDeserializedToSame(Cf.set());
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.set(1));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.set(1, 4));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.set(1, 4, 8));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.set(1, 4, 8, 9));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.set(1, 4, 8, 9, 2));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.set(1, 4, 8, 9, 2, 3));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.set(1, 4, 8, 9, 2, 3, 5));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.hashSet());
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.hashSet(1));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.hashSet(1, 4));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.hashSet(1, 4, 8));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.hashSet(1, 4, 8, 9));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.hashSet(1, 4, 8, 9, 2));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.hashSet(1, 4, 8, 9, 2, 3));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.hashSet(1, 4, 8, 9, 2, 3, 5));
    }

    public void testMaps() {
        SerializationUtils.assertSerializedDeserializedToSame(Cf.map());
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.map(1, "a"));
        SerializationUtils.assertSerializedDeserializedToEqual(Cf.map(1, "a", 2, "b"));
    }

} //~
