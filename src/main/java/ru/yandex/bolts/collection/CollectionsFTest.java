package ru.yandex.bolts.collection;

import junit.framework.TestCase;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.misc.IntegerF;

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
        assertTrue(set.contains(2));
        assertFalse(set.contains(new Integer(2)));

        SetF<Integer> uset = set.unmodifiable();
        assertEquals(4, set.size());
        try {
            uset.add(5);
            fail();
        } catch (Exception e) {
            // OK
        }
    }

    public void testRepeatEmpty() {
        ListF<String> set = Cf.repeat("a", 0);
        assertFalse(set.contains("a"));
    }

    public void testMapFandSortFandUniqueF() {
        ListMap<String, Integer> data = ListMap.<String, Integer>listMap()
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
                .mapValues(Cf.mapF(Tuple2.<String, Integer>get2F()).andThen(
                        Cf.<Integer>sortF(IntegerF.naturalComparator())));

        assertEquals(Cf.list(102, 111, 111), groupped.get("foo"));
        assertEquals(Cf.list(97, 98, 114), groupped.get("bar"));
        assertEquals(Cf.list(97, 98, 122), groupped.get("baz"));

        MapF<String, SetF<Integer>> unique = groupped.mapValues(Cf.<Integer>uniqueF());

        assertEquals(Cf.set(102, 111), unique.get("foo"));
        assertEquals(Cf.set(97, 98, 114), unique.get("bar"));
        assertEquals(Cf.set(97, 98, 122), unique.get("baz"));
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
                Cf.filterF(new Function1B<Integer>() {
                    public boolean apply(Integer integer) {
                        return integer >= 0;
                    }})
                .andThen(Cf.sortF(IntegerF.naturalComparator()))
                .andThen(Cf.<Integer>toListF()));

        assertEquals(Cf.list(
                Cf.list(1, 2, 5),
                Cf.list(4, 8, 10),
                Cf.list(0, 7),
                Cf.list(), Cf.list()), positiveIntegers);
    }
} //~
