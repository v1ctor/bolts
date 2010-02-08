package ru.yandex.bolts.collection.impl;

import static ru.yandex.bolts.collection.CollectionsF.list;
import static ru.yandex.bolts.collection.CollectionsF.set;

import java.util.Arrays;

import junit.framework.TestCase;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2Test;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * @author Stepan Koltsov
 */
public class AbstractCollectionFTest extends TestCase {

    public void testForEach() {
        ListF<Object> l = CollectionsF.arrayList();
        list(1, 2, 3).forEach(l.addF());
        assertEquals(list(1, 2, 3), l);
    }

    public void testAddF() {
        ListF<String> l = Cf.arrayList();
        Function1V<String> op = l.addF();
        op.apply("a");
        op.apply("b");
        op = l.addF();
        op.apply("c");
        assertEquals(Cf.list("a", "b", "c"), l);

        l.addF().toString(); // for coverage
    }

    public void testMkString() {
        assertEquals("", list().mkString(", "));
        assertEquals("[1]", list(1).mkString("[", ", ", "]"));
        assertEquals("[1, 2]", list(1, 2).mkString("[", ", ", "]"));
        assertEquals("[1, 2, 3]", list(1, 2, 3).mkString("[", ", ", "]"));
    }

    public void testToList() {
        assertEquals(list(1), set(1).toList());
        assertEquals(set(1, 2, 3, 4, 5), set(1, 2, 3, 4, 5).toList().unique());
    }

    public void testReduce() {
        ListF<Integer> l = list(1, 2, 3);
        assertEquals(6, (int) l.reduceLeft(Function2Test.plusF()));
        assertEquals(6, (int) l.reduceRight(Function2Test.plusF()));
    }

    public void testPlus1() {
        assertEquals(Cf.list(1, 2, 3, 4), Cf.list().plus1(1).plus1(2).plus1(3).plus1(4));
        assertEquals(Cf.set(1, 2, 3), Cf.set().plus1(1).plus1(2).plus1(3).plus1(1).plus1(2));
    }

    public void testToMap() {
        MapF<String, Integer> m = list(1, 2, 3).toMapMappingToKey(Function.toStringF());
        MapF<String, Integer> expected = list("1", "2", "3").toMapMappingToValue(new Function<String, Integer>() {
            public Integer apply(String s) {
                return Integer.parseInt(s);
            }
        });
        assertEquals(expected, m);
    }

    public void testSort() {
        assertEquals(Cf.list(1, 2, 3), Cf.list(1, 3, 2).sort());
        assertEquals(Cf.list(3, 2, 1), Cf.list(1, 3, 2).sort(Comparator.naturalComparator().invert()));
        assertEquals(Cf.list("1", "30", "200"), Cf.list("1", "200", "30").sortBy(stringLengthM()));
        assertEquals(Cf.list("200", "30", "1"), Cf.list("1", "200", "30").sortByDesc(stringLengthM()));
    }

    public void testSortByIsNullLow() {
        assertEquals(Cf.list(null, "1", "30", "200"), Cf.list("1", null, "200", "30").sortBy(stringLengthM()));
        assertEquals(Cf.list("200", "30", "1", null), Cf.list("1", null, "200", "30").sortByDesc(stringLengthM()));
    }

    public void testToPrimitiveArray() {
        assertTrue(Arrays.equals(new int[] { 1, 2, 3 }, Cf.list(1, 2, 3).toIntArray()));
        assertTrue(Arrays.equals(new long[] { 1, 2, 3 }, Cf.<Integer>list(1, 2, 3).toLongArray()));
        assertTrue(Arrays.equals(new char[] { 'a', 'b', 'c' }, Cf.list('a', 'b', 'c').toCharArray()));
    }

    private static Function<String, Object> stringLengthM() {
        return new Function<String, Object>() {
            public Object apply(String s) {
                return s.length();
            }
        };
    }

    public void testMin() {
        CollectionF<String> coll = Cf.list("b", "a", "d", "c");
        assertEquals("a", coll.min());
        assertEquals("d", coll.min(Comparator.naturalComparator().<String, String>uncheckedCast().invert()));
    }

    public void testMax() {
        CollectionF<String> coll = Cf.list("b", "a", "d", "c");
        assertEquals("d", coll.max());
        assertEquals("a", coll.max(Comparator.naturalComparator().<String, String>uncheckedCast().invert()));
    }
} //~
