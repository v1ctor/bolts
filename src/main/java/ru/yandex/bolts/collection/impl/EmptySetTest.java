package ru.yandex.bolts.collection.impl;

/**
 * @author Stepan Koltsov
 */
public class EmptySetTest extends EmptyCollectionTestSupport {

    @Override
    protected Object emptyInstance() {
        return EmptySet.INSTANCE;
    }

} //~
