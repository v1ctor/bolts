package ru.yandex.bolts.collection.impl;

import java.util.Arrays;

import junit.framework.TestCase;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2Test;
import ru.yandex.bolts.function.forhuman.Comparator;

import static ru.yandex.bolts.collection.Cf.list;
import static ru.yandex.bolts.collection.Cf.set;

/**
 * @author Stepan Koltsov
 */
public class AbstractCollectionFTest extends TestCase {

    public void testForEach() {
        ListF<Object> l = Cf.arrayList();
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
        MapF<String, Integer> expected = list("1", "2", "3").toMapMappingToValue(Integer::parseInt);
        assertEquals(expected, m);
    }

    public void testSort() {
        assertEquals(Cf.list(1, 2, 3), Cf.list(1, 3, 2).sorted());
        assertEquals(Cf.list(3, 2, 1), Cf.list(1, 3, 2).sorted(Comparator.naturalComparator().invert()));
        assertEquals(Cf.list("1", "30", "200"), Cf.list("1", "200", "30").sortedBy(stringLengthM()));
        assertEquals(Cf.list("200", "30", "1"), Cf.list("1", "200", "30").sortedByDesc(stringLengthM()));
    }

    public void testSortByIsNullLow() {
        assertEquals(Cf.list(null, "1", "30", "200"), Cf.list("1", null, "200", "30").sortedBy(stringLengthM()));
        assertEquals(Cf.list("200", "30", "1", null), Cf.list("1", null, "200", "30").sortedByDesc(stringLengthM()));
    }

    public void testToPrimitiveArray() {
        assertTrue(Arrays.equals(new int[] { 1, 2, 3 }, Cf.list(1, 2, 3).toIntArray()));
        assertTrue(Arrays.equals(new long[] { 1, 2, 3 }, Cf.list(1, 2, 3).toLongArray()));
        assertTrue(Arrays.equals(new char[] { 'a', 'b', 'c' }, Cf.list('a', 'b', 'c').toCharArray()));
    }

    private static Function<String, Object> stringLengthM() {
        return String::length;
    }

    public void testMin() {
        CollectionF<String> coll = Cf.list("b", "a", "d", "c");
        assertEquals("a", coll.min());
        assertEquals("d", coll.min(Comparator.naturalComparator().uncheckedCast().invert()));
    }

    public void testMinBy() {
        Pojo expected = new Pojo(-1);
        assertEquals(expected, Cf.list(new Pojo(42), expected, new Pojo(0)).minBy(Pojo::getComparable));
    }

    public void testMinByO() {
        Pojo expected = new Pojo(-1);
        assertEquals(expected, Cf.list(new Pojo(42), expected, new Pojo(0)).minByO(Pojo::getComparable).get());
        assertTrue(Cf.<Pojo>list().minByO(Pojo::getComparable).isEmpty());
    }

    public void testMax() {
        CollectionF<String> coll = Cf.list("b", "a", "d", "c");
        assertEquals("d", coll.max());
        assertEquals("a", coll.max(Comparator.naturalComparator().uncheckedCast().invert()));
    }

    public void testMaxBy() {
        Pojo expected = new Pojo(42);
        assertEquals(expected, Cf.list(new Pojo(-1), expected, new Pojo(0)).maxBy(Pojo::getComparable));
    }

    public void testMaxByO() {
        Pojo expected = new Pojo(42);
        assertEquals(expected, Cf.list(new Pojo(-1), expected, new Pojo(0)).maxByO(Pojo::getComparable).get());
        assertTrue(Cf.<Pojo>list().maxByO(Pojo::getComparable).isEmpty());
    }

    public void testPaginate() {
        assertEquals(Cf.list(Cf.list(1, 2, 3), Cf.list(4, 5)),
                Cf.list(1, 2, 3, 4, 5).paginate(3));
        assertEquals(Cf.list(Cf.list(1, 2), Cf.list(3, 4)),
                Cf.list(1, 2, 3, 4).paginate(2));
        assertEquals(Cf.list(), Cf.list().paginate(3));
    }

    public void testFlatten() {
        CollectionF<? extends CollectionF<String>> coll = Cf.list(Cf.list("a", "b"), Cf.list("c"));
        assertEquals(Cf.list("a", "b", "c"), coll.flatten());
    }

    public void testTakeSorted() {
        ListF<Integer> lst = Cf.range(0, 100).shuffle();
        CollectionF<Integer> coll = Cf.list(lst);
        assertEquals(Cf.range(0, 10), coll.takeSorted(10));
        assertEquals(lst, coll);
        assertEquals(Cf.range(90, 100).reverse(), coll.takeSorted(java.util.Comparator.<Integer>reverseOrder(), 10));
        assertEquals(lst, coll);
        assertEquals(Cf.range(0, 10).filter(x -> x % 2 == 0), coll.takeSortedBy(x -> (x % 2) * coll.size() + x, 5));

        SetF<Integer> set = Cf.range(0, 100).unique();
        assertEquals(Cf.range(0, 50), set.takeSorted(50));
    }

    public void testGetSorted() {
        ListF<Integer> lst = Cf.range(0, 100).shuffle();
        CollectionF<Integer> coll = Cf.list(lst);
        assertEquals(10, coll.getSorted(10).intValue());
        assertEquals(lst, coll);
        assertEquals(90, coll.getSorted(java.util.Comparator.<Integer>reverseOrder(), 9).intValue());
        assertEquals(lst, coll);
        assertEquals(8, coll.getSorted(x -> (x % 2) * coll.size() + x, 4).intValue());

        SetF<Integer> set = Cf.range(0, 100).unique();
        assertEquals(50, set.getSorted(50).intValue());
    }

    public static final class Pojo {

        private final int comparable;

        public Pojo(int comparable) {
            this.comparable = comparable;
        }

        public int getComparable() {
            return comparable;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }

            Pojo pojo = (Pojo) other;
            return comparable == pojo.comparable;
        }

        @Override
        public int hashCode() {
            return comparable;
        }
    }

} //~
