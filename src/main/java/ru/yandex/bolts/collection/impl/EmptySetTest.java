package ru.yandex.bolts.collection.impl;


public class EmptySetTest extends EmptyCollectionTestSupport {

    @Override
    protected Object emptyInstance() {
        return EmptySet.INSTANCE;
    }

} //~
