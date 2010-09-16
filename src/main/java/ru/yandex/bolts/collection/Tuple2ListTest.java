package ru.yandex.bolts.collection;

import junit.framework.TestCase;

import ru.yandex.bolts.function.Function1B;

/**
 * @author Stepan Koltsov
 * @author Iliya Roubin
 */
public class Tuple2ListTest extends TestCase {

    public void testFlatMapFromPairs() {
        assertEquals(Tuple2List.tuple2List(), Tuple2List.fromPairs());

        Tuple2List<String, Integer> fm = Tuple2List.fromPairs("a", 1, "b", 2);
        assertEquals(2, fm.length());
        assertEquals(Tuple2.tuple("a", 1), fm.get(0));
        assertEquals(Tuple2.tuple("b", 2), fm.get(1));
    }

    public void testSortByKey() {
        assertEquals(Tuple2List.fromPairs("a", 2, "a", 1, "b", 4), Tuple2List.fromPairs("b", 4, "a", 2, "a", 1).sortBy1());
    }

    @SuppressWarnings("unchecked")
    public void testPlus() {
        Tuple2List<String, Integer> lm = Tuple2List.fromPairs("a", 1, "b", 2);
        assertSame(lm, lm.plus(Tuple2List.<String, Integer>arrayList()));
        assertSame(lm, Tuple2List.<String, Integer>arrayList().plus(lm));

        assertEquals(Tuple2List.fromPairs("a", 1, "b", 2, "c", 3), Tuple2List.fromPairs("a", 1, "b", 2).plus(Tuple2List.fromPairs("c", 3)));
        assertEquals(Tuple2List.<String, Integer>fromPairs("a", 1, "b", 2, "c", 3),
                Tuple2List.<String, Integer>fromPairs("a", 1, "b", 2).plus(Tuple2.tuple("c", 3)));
    }

    public void testFilter() {
        Tuple2List<String, Integer> lm = Tuple2List.fromPairs("a", 1, "b", 2);
        Tuple2List<String, Integer> lmf = lm.filter(new Function1B<Tuple2<String, Integer>>() {
            @Override
            public boolean apply(Tuple2<String, Integer> t) {
                return "a".equals(t.get1());
            }
        });
        assertEquals(Tuple2List.<String, Integer>fromPairs("a", 1), lmf);
    }

    public void testPartitionLm() {
        Tuple2List<String, Integer> lm = Tuple2List.fromPairs("a", 1, "b", 2);
        Tuple2<Tuple2List<String, Integer>, Tuple2List<String, Integer>> t = lm.partitionT2l(new Function1B<Tuple2<String, Integer>>() {
            @Override
            public boolean apply(Tuple2<String, Integer> t) {
                return "a".equals(t.get1());
            }
        });
        assertEquals(Tuple2List.<String, Integer>fromPairs("a", 1), t.get1());
        assertEquals(Tuple2List.<String, Integer>fromPairs("b", 2), t.get2());
    }

    public void testTakeDrop() {
        Tuple2List<String, Integer> lm = Tuple2List.fromPairs("a", 1, "b", 2);
        assertEquals(Tuple2List.<String, Integer>fromPairs("a", 1), lm.take(1));
        assertEquals(Tuple2List.<String, Integer>fromPairs("b", 2), lm.drop(1));
    }

    public void testReverse() {
        assertEquals(Tuple2List.fromPairs("a", 1, "b", 2),
                Tuple2List.fromPairs("b", 2, "a", 1).reverse());
    }

    public void testMapPlusToArray() {
        assertEquals(1, Tuple2List.fromPairs("a", 1).map(Tuple2.get2F()).plus(Cf.list(2)).toArray()[0]);
        assertEquals(1, Tuple2List.tuple2List().map(Tuple2.get2F()).plus(Cf.list(1)).toArray()[0]);
    }

    public void testGroupBy1() {
        MapF<Integer, ListF<String>> g = Tuple2List.<Integer, String>fromPairs(1, "a", 1, "b", 2, "c", 3, "d", 1, "e").groupBy1();
        assertEquals(3, g.size());
        assertEquals(Cf.list("a", "b", "e"), g.apply(1));
        assertEquals(Cf.list("c"), g.apply(2));
        assertEquals(Cf.list("d"), g.apply(3));
    }
} //~
