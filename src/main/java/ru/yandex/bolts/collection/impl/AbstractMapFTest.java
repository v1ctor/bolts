package ru.yandex.bolts.collection.impl;

import java.util.NoSuchElementException;

import junit.framework.TestCase;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1BTest;

import static ru.yandex.bolts.collection.Cf.list;
import static ru.yandex.bolts.collection.Cf.set;

/**
 * @author Stepan Koltsov
 */
public class AbstractMapFTest extends TestCase {

    private static MapF<Integer, String> map123() {
        return list(1, 2, 3).toMapMappingToValue(Function.toStringF());
    }

    public void testFilterKeys() {
        MapF<Integer, String> m = map123();
        MapF<Integer, String> n = m.filterKeys(Function1BTest.evenF());
        assertEquals(Cf.map(2, "2"), n);
    }

    public void testFilter() {
        MapF<Integer, String> m = map123().plus1(11, "11");
        MapF<Integer, String> n = m.filter((a, b) -> (a % 2) == 0 || b.length() > 1);
        assertEquals(Cf.map(2, "2", 11, "11"), n);
    }

    public void testFilterStinky() {
        Object stinky = new Object() {
            public int hashCode() {
                throw new UnsupportedOperationException("I stink");
            }
        };
        MapF<Integer, Object> mapToFilter = Cf.hashMap();
        mapToFilter.put(1, stinky);
        mapToFilter.put(-3, "flower");
        mapToFilter.put(2, 2);
        mapToFilter.put(-1, mapToFilter);
        assertEquals(set(1, 2), mapToFilter.filterKeys(Cf.Integer.gtF(0)).keySet());
        assertEquals(set(-1, -3), mapToFilter.filterKeys(Cf.Integer.ltF(0)).keySet());
    }

    public void testMapValues() {
        MapF<Integer, String> n = map123().mapValues((Function<String, String>) s -> "a" + s);
        assertEquals(list(1, 2, 3).toMapMappingToValue((Function<Integer, String>) i -> "a" + i), n);
    }

    public void testAsFunction() {
        Function<Integer, String> f = map123().asFunction();

        assertEquals("3", f.apply(3));
    }

    public void testAsFunctionLookupFailure() {
        Function<Integer, String> f = map123().asFunction();

        try {
            f.apply(4);
            fail("expecting NoSuchElementException");
        } catch (NoSuchElementException e) {
            // ok
        }
    }

    public void testAsFunctionO() {
        Function<Integer, Option<String>> f = map123().asFunctionO();

        assertEquals(Option.none(), f.apply(0));
        assertEquals(Option.some("1"), f.apply(1));
    }

    public void testKeysValues() {
        assertEquals(set(1, 2, 3), map123().keySet());
        assertEquals(set("1", "2", "3"), map123().values().unique());
    }

    public void testGet() {
        assertEquals("1", map123().getTs(1));
        assertNull(map123().getTs(4));
    }

    public void testGetOrElseConst() {
        assertEquals("1", map123().getOrElse(1, "2"));
        assertEquals("4", map123().getOrElse(4, "4"));
    }

    public void testGetO() {
        assertEquals(Option.some("1"), map123().getO(1));
        assertEquals(Option.none(), map123().getO(4));
    }

    public void testGetOrThrow() {
        assertEquals("1", map123().getOrThrow(1, "message"));
        assertEquals("1", map123().getOrThrow(1, "message", "param"));

        try {
            map123().getOrThrow(5, "msg");
            fail();
        } catch (NoSuchElementException e) {
            assertEquals("msg", e.getMessage());
        }

        try {
            map123().getOrThrow(5, "xx: ", 1);
            fail();
        } catch (NoSuchElementException e) {
            assertEquals("xx: 1", e.getMessage());
        }
    }

    public void testGetOrElseUpdateConst() {
        MapF<Integer, String> m = map123();
        assertEquals("1", m.getOrElseUpdate(1, "11111"));
        assertEquals("1", m.getOrElseUpdate(1, "11111"));
        assertEquals("1", m.getOrElseUpdate(1, "11111"));
        assertEquals("4", m.getOrElseUpdate(4, "4"));
        assertEquals("4", m.apply(4));
    }

    public void testContainsEntry() {
        assertTrue(map123().containsEntry(1, "1"));
        assertFalse(map123().containsEntry(2, "1"));
        assertFalse(map123().containsEntry(2, "7"));
        assertFalse(map123().containsEntry(7, "7"));
        assertFalse(map123().containsEntry(7, "2"));
    }

    public void testPlus() {
        MapF<Integer, String> m2 = Cf.hashMap();
        m2.put(3, "a");
        m2.put(4, "b");

        MapF<Integer, String> e = Cf.hashMap();
        e.put(1, "1");
        e.put(2, "2");
        e.put(3, "a");
        e.put(4, "b");
    }

    public void testPlus1() {
        MapF<Integer, String> m = map123().plus1(11, "11");
        assertEquals(m, Cf.list(1, 2, 3, 11).toMapMappingToValue(Function.toStringF()));
    }

} //~
