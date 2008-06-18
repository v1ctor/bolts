package ru.yandex.bolts.collection.impl;

import static ru.yandex.bolts.collection.CollectionsF.list;
import static ru.yandex.bolts.collection.CollectionsF.set;

import java.util.Arrays;

import junit.framework.TestCase;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.function.forhuman.BinaryFunctionTest;
import ru.yandex.bolts.function.forhuman.Comparator;
import ru.yandex.bolts.function.forhuman.Mapper;
import ru.yandex.bolts.function.forhuman.Operation;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"UnusedDeclaration", "unused"})
public class AbstractCollectionFTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AbstractCollectionFTest.class);

    public void testForEach() {
        ListF<Object> l = CollectionsF.arrayList();
        list(1, 2, 3).forEach(l.addOp());
        assertEquals(list(1, 2, 3), l);
    }

    public void testAddOp() {
        ListF<String> l = Cf.arrayList();
        Operation<String> op = l.addOp();
        op.execute("a");
        op.execute("b");
        op = l.addOp();
        op.execute("c");
        assertEquals(Cf.list("a", "b", "c"), l);

        l.addOp().toString(); // for coverage
    }

    public void testMkString() {
        assertEquals("", list().mkString(", "));
        assertEquals("[1]", list(1).mkString("[", ", ", "]"));
        assertEquals("[1, 2]", list(1, 2).mkString("[", ", ", "]"));
    }

    public void testToList() {
        assertEquals(list(1), set(1).toList());
        assertEquals(set(1, 2, 3, 4, 5), set(1, 2, 3, 4, 5).toList().unique());
    }

    public void testReduce() {
        ListF<Integer> l = list(1, 2, 3);
        assertEquals(6, (int) l.reduceLeft(BinaryFunctionTest.plusF()));
        assertEquals(6, (int) l.reduceRight(BinaryFunctionTest.plusF()));
    }

    public void testPlus1() {
        assertEquals(Cf.list(1, 2, 3, 4), Cf.list().plus1(1).plus1(2).plus1(3).plus1(4));
        assertEquals(Cf.set(1, 2, 3), Cf.set().plus1(1).plus1(2).plus1(3).plus1(1).plus1(2));
    }

    public void testToMap() {
        MapF<String, Integer> m = list(1, 2, 3).toMapMappingToKey(Mapper.toStringM());
        MapF<String, Integer> expected = list("1", "2", "3").toMapMappingToValue(new Mapper<String, Integer>() {
            public Integer map(String s) {
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
    
    public void testToPrimitiveArray() {
        assertTrue(Arrays.equals(new int[] { 1, 2, 3 }, Cf.list(1, 2, 3).toIntArray()));
        assertTrue(Arrays.equals(new long[] { 1, 2, 3 }, Cf.<Integer>list(1, 2, 3).toLongArray()));
        assertTrue(Arrays.equals(new char[] { 'a', 'b', 'c' }, Cf.list('a', 'b', 'c').toCharArray()));
    }

    private static Mapper<String, Object> stringLengthM() {
        return new Mapper<String, Object>() {
            public Object map(String s) {
                return s.length();
            }
        };
    }
} //~
