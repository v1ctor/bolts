package ru.yandex.bolts.collection;

import junit.framework.TestCase;

/**
 * @author Stepan Koltsov
 */
public class ListMapTest extends TestCase {
    
    public void testFlatMapFromPairs() {
        assertEquals(ListMap.listMap(), ListMap.listMapFromPairs());
        
        ListMap<String, Integer> fm = ListMap.listMapFromPairs("a", 1, "b", 2);
        assertEquals(2, fm.length());
        assertEquals(Tuple2.tuple("a", 1), fm.get(0));
        assertEquals(Tuple2.tuple("b", 2), fm.get(1));
    }
    
    public void testSortByKey() {
        assertEquals(ListMap.listMapFromPairs("a", 2, "a", 1, "b", 4), ListMap.listMapFromPairs("b", 4, "a", 2, "a", 1).sortByKey());
    }
    
    public void testPlus() {
        ListMap<String, Integer> lm = ListMap.<String, Integer>listMapFromPairs("a", 1, "b", 2);
        assertSame(lm, lm.plus(ListMap.<String, Integer>arrayList()));
        assertSame(lm, ListMap.<String, Integer>arrayList().plus(lm));
        
        assertEquals(ListMap.listMapFromPairs("a", 1, "b", 2, "c", 3), ListMap.listMapFromPairs("a", 1, "b", 2).plus(ListMap.listMapFromPairs("c", 3)));
    }

} //~
