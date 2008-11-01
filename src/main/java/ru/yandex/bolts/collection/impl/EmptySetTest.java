package ru.yandex.bolts.collection.impl;

/**
 * @author Stepan Koltsov
 */
public class EmptySetTest extends AbstractEmptyCollectionTest {

    @Override
    protected Object emptyInstance() {
        return EmptySet.INSTANCE;
    }
    
} //~
