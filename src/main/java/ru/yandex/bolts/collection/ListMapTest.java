package ru.yandex.bolts.collection;

import junit.framework.TestCase;

import ru.yandex.bolts.function.Function1B;

/**
 * @author Stepan Koltsov
 * @author Iliya Roubin
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
        ListMap<String, Integer> lm = ListMap.listMapFromPairs("a", 1, "b", 2);
        assertSame(lm, lm.plus(ListMap.<String, Integer>arrayList()));
        assertSame(lm, ListMap.<String, Integer>arrayList().plus(lm));

        assertEquals(ListMap.listMapFromPairs("a", 1, "b", 2, "c", 3), ListMap.listMapFromPairs("a", 1, "b", 2).plus(ListMap.listMapFromPairs("c", 3)));
        assertEquals(ListMap.<String, Integer>listMapFromPairs("a", 1, "b", 2, "c", 3),
                ListMap.<String, Integer>listMapFromPairs("a", 1, "b", 2).plus(Tuple2.tuple("c", 3)));
    }

    public void testFilter() {
        ListMap<String, Integer> lm = ListMap.listMapFromPairs("a", 1, "b", 2);
        ListMap<String, Integer> lmf = lm.filter(new Function1B<Tuple2<String, Integer>>() {
            @Override
            public boolean apply(Tuple2<String, Integer> t) {
                return "a".equals(t.get1());
            }
        });
        assertEquals(ListMap.<String, Integer>listMapFromPairs("a", 1), lmf);
    }

    public void testFilter2ToListMaps() {
        ListMap<String, Integer> lm = ListMap.listMapFromPairs("a", 1, "b", 2);
        Tuple2<ListMap<String, Integer>, ListMap<String, Integer>> t = lm.filter2ToListMaps(new Function1B<Tuple2<String, Integer>>() {
            @Override
            public boolean apply(Tuple2<String, Integer> t) {
                return "a".equals(t.get1());
            }
        });
        assertEquals(ListMap.<String, Integer>listMapFromPairs("a", 1), t.get1());
        assertEquals(ListMap.<String, Integer>listMapFromPairs("b", 2), t.get2());
    }

    public void testTakeDrop() {
        ListMap<String, Integer> lm = ListMap.listMapFromPairs("a", 1, "b", 2);
        assertEquals(ListMap.<String, Integer>listMapFromPairs("a", 1), lm.take(1));
        assertEquals(ListMap.<String, Integer>listMapFromPairs("b", 2), lm.drop(1));
    }

    public void testReverse() {
        assertEquals(ListMap.listMapFromPairs("a", 1, "b", 2),
                ListMap.listMapFromPairs("b", 2, "a", 1).reverse());
    }

    public void testMapPlusToArray() {
        assertEquals(1, ListMap.listMapFromPairs("a", 1).map(Tuple2.get2F()).plus(Cf.list(2)).toArray()[0]);
        assertEquals(1, ListMap.listMap().map(Tuple2.get2F()).plus(Cf.list(1)).toArray()[0]);
    }
} //~
