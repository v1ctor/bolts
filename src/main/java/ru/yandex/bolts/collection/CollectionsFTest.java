package ru.yandex.bolts.collection;

import junit.framework.TestCase;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;

import static ru.yandex.bolts.collection.CollectionsF.list;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings("unused")
public class CollectionsFTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CollectionsFTest.class);

    public void testRange() {
        assertEquals(list(0, 1, 2, 3), CollectionsF.range(0, 4));
    }

    public void testRangeMapPossibleBug() {
        assertEquals(list("0", "1", "2", "3"), CollectionsF.range(0, 4).map(Function.toStringF()));
    }

    public void testRangeEmpty() {
        assertEquals(list(), CollectionsF.range(0, 0));
        assertEquals(list(), CollectionsF.range(1, 1));
        assertEquals(list(), CollectionsF.range(5, 1));
    }

    public void testIdentityHashSet() {
        SetF<Integer> set = Cf.identityHashSet(1, 2, 3, new Integer(1));
        assertEquals(4, set.size());
        assertTrue(set.containsTs(2));
        assertFalse(set.containsTs(new Integer(2)));

        SetF<Integer> uset = set.unmodifiable();
        assertEquals(4, set.size());
        try {
            uset.add(5);
            fail();
        } catch (Exception e) {
            // OK
        }
    }

    public void testSetFromMap() {
        SetF<Object> set = Cf.identityHashSet();
        assertTrue(set.add(new Object()));
        Object o = new Object();
        assertTrue(set.add(o));
        assertFalse(set.add(o));
    }

    public void testRepeatEmpty() {
        ListF<String> set = Cf.repeat("a", 0);
        assertFalse(set.containsTs("a"));
    }

    public void testMapFandSortFandUniqueF() {
        Tuple2List<String, Integer> data = Tuple2List.<String, Integer>tuple2List()
                .plus1("foo", 102)
                .plus1("bar", 98)
                .plus1("baz", 98)
                .plus1("foo", 111)
                .plus1("foo", 111)
                .plus1("bar", 97)
                .plus1("baz", 97)
                .plus1("bar", 114)
                .plus1("baz", 122);

        MapF<String, ListF<Integer>> groupped
                = data.groupBy(Tuple2.<String, Integer>get1F())
                .mapValues(Cf.<Tuple2<String, Integer>, Integer>mapF(Tuple2.<String, Integer>get2F()).andThen(
                        Cf.<Integer>sortedF(Cf.Integer.comparator())));

        assertEquals(Cf.list(102, 111, 111), groupped.getTs("foo"));
        assertEquals(Cf.list(97, 98, 114), groupped.getTs("bar"));
        assertEquals(Cf.list(97, 98, 122), groupped.getTs("baz"));

        MapF<String, SetF<Integer>> unique = groupped.mapValues(Cf.<Integer>uniqueF());

        assertEquals(Cf.set(102, 111), unique.getTs("foo"));
        assertEquals(Cf.set(97, 98, 114), unique.getTs("bar"));
        assertEquals(Cf.set(97, 98, 122), unique.getTs("baz"));
    }

    @SuppressWarnings("unchecked")
    public void testFilterFandSortFandListF() {
        ListF<SetF<Integer>> data = Cf.list(
                Cf.set(5, -3, 2, 1),
                Cf.set(8, 10, -9, 4),
                Cf.set(0, 7, -1, -1),
                Cf.set(-4),
                Cf.<Integer>set());

        ListF<ListF<Integer>> positiveIntegers = data.map(
                Cf.Set.<Integer>filterF(integer -> integer >= 0)
                .andThen(Cf.<Integer>sortedF(Cf.Integer.comparator()))
                .andThen(Cf.<Integer>toListF()));

        assertEquals(Cf.list(
                Cf.list(1, 2, 5),
                Cf.list(4, 8, 10),
                Cf.list(0, 7),
                Cf.list(), Cf.list()), positiveIntegers);
    }

    public void testFlatten() {
        assertEquals(Cf.list(1, 2, 3, 4), Cf.flatten(Cf.list(Cf.list(1, 2), Cf.list(3, 4))));
    }
} //~
