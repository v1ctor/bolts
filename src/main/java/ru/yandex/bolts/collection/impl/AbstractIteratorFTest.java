package ru.yandex.bolts.collection.impl;

import java.util.NoSuchElementException;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.function.forhuman.Mapper;

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
    
    public void testFlatMap() {
        IteratorF<Integer> i = Cf.list(1, 2, 3).iterator().flatMap(new Mapper<Integer, IteratorF<Integer>>() {
            public IteratorF<Integer> map(Integer a) {
                return Cf.repeat(a, a).iterator();
            }
        });
        
        // too simple
        
        assertTrue(i.hasNext());
        assertEquals(1, i.next().intValue());
        
        assertEquals(2, i.next().intValue());
        assertTrue(i.hasNext());
        assertEquals(2, i.next().intValue());
        
        assertTrue(i.hasNext());
        assertEquals(3, i.next().intValue());
        assertEquals(3, i.next().intValue());
        assertEquals(3, i.next().intValue());
        
        assertFalse(i.hasNext());
        
        try {
            i.next();
            fail();
        } catch (NoSuchElementException e) {
            // ok
        }
    }
    
} //~
