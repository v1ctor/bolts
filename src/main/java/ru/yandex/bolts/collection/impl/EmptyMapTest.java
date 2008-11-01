package ru.yandex.bolts.collection.impl;


/**
 * @author Stepan Koltsov
 */
public class EmptyMapTest extends AbstractEmptyCollectionTest {
    
    @Override
    protected Object emptyInstance() {
        return EmptyMap.INSTANCE;
    }
    
} //~
