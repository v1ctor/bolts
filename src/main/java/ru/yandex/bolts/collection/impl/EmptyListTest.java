package ru.yandex.bolts.collection.impl;

/**
 * @author Stepan Koltsov
 */
public class EmptyListTest extends EmptyCollectionTestSupport {

    @Override
    protected Object emptyInstance() {
        return EmptyList.INSTANCE;
    }
    
} //~
