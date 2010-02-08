package ru.yandex.bolts.collection;

import static ru.yandex.bolts.collection.CollectionsF.list;
import junit.framework.TestCase;

import ru.yandex.bolts.function.Function;

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
} //~
