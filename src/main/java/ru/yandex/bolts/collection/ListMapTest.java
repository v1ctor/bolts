package ru.yandex.bolts.collection;

import junit.framework.TestCase;

/**
 * @author Stepan Koltsov
 */
public class ListMapTest extends TestCase {
    
    public void testFlatMapFromPairs() {
        ListMap<String, Integer> fm = ListMap.listMapFromPairs("a", 1, "b", 2);
        assertEquals(2, fm.length());
        assertEquals(Tuple2.tuple("a", 1), fm.get(0));
        assertEquals(Tuple2.tuple("b", 2), fm.get(1));
    }

} //~
