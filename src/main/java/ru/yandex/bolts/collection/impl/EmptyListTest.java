package ru.yandex.bolts.collection.impl;

/**
 * @author Stepan Koltsov
 */
public class EmptyListTest extends AbstractEmptyCollectionTest {

    @Override
    protected Object emptyInstance() {
        return EmptyList.INSTANCE;
    }
    
} //~
