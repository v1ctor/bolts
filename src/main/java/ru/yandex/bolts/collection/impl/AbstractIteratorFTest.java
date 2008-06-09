package ru.yandex.bolts.collection.impl;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;

import junit.framework.TestCase;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings("unused")
public class AbstractIteratorFTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AbstractIteratorFTest.class);

    public void testPlus() {
        ListF<Integer> got = Cf.list(1, 2, 3).iterator().plus(Cf.list(4, 5).iterator()).toList();
        assertEquals(Cf.list(1, 2, 3, 4, 5), got);
    }
} //~
