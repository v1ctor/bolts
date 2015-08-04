package ru.yandex.bolts.collection.impl;

import ru.yandex.bolts.collection.impl.test.SerializationUtils;

import junit.framework.TestCase;


public abstract class EmptyCollectionTestSupport extends TestCase {

    protected abstract Object emptyInstance();

    public void testSerialization() {
        assertSame(EmptyMap.INSTANCE, SerializationUtils.serializeDeserialize(EmptyMap.INSTANCE));
    }

} //~
