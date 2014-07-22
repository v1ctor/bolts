package ru.yandex.bolts.collection.impl;

import java.util.Collections;
import java.util.Set;

import junit.framework.TestCase;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.impl.test.Generator;
import ru.yandex.bolts.function.Function1BTest;

import static ru.yandex.bolts.collection.Cf.set;

/**
 * @author Stepan Koltsov
 */
public class AbstractSetFTest extends TestCase {

    public void testFilter2() {
        SetF<Integer> s = set(1, 2, 3, 4);
        assertEquals(Tuple2.tuple(set(2, 4), set(1, 3)), s.partition(Function1BTest.evenF()));
    }

    public void testFilter() {
        SetF<Integer> s = set(1, 2, 3, 4);
        assertEquals(set(2, 4), s.filter(Function1BTest.evenF()));
    }

    public void testFilterNot() {
        SetF<Integer> s = set(1, 2, 3, 4);
        assertEquals(set(1, 3), s.filterNot(Function1BTest.evenF()));
    }

    public void testMinusEmpty() {
        SetF<Integer> s = set(1, 2, 3);
        SetF<Integer> es = Cf.set();
        assertSame(s, s.minus(es));
        assertSame(es, es.minus(s));
    }

    public void testEqualsToRegular() {
        Set<String> regular = Collections.emptySet();
        SetF<String> our = Cf.set();
        assertEquals(regular, our);
        assertEquals(our, regular);
        assertEquals(our, our);
    }

    /////
    // union/intersect/minus

    private Generator<SetF<Integer>> smallSets() {
        return Generator.ints(1, 10).sets();
    }

    public void testUnionSimple() {
        SetF<Integer> s = set(1, 2, 3);
        SetF<Integer> s1 = s.plus(set(3, 4, 5));
        assertEquals(set(1, 2, 3, 4, 5), s1);
    }

    public void testIntersectSimple() {
        assertEquals(set(3, 4), set(1, 2, 3, 4).intersect(set(3, 4, 5, 6)));
    }

    public void testSimple() {
        assertEquals(set(1, 2), Cf.x(set(1, 2, 3, 4)).minus(set(3, 4, 5, 6)));
    }


    public void testBinaryOps() {
        smallSets().tuples().checkForAll(pair -> {
            testIntersectFor(pair.get1(), pair.get2());
            testUnionFor(pair.get1(), pair.get2());
            testMinusFor(pair.get1(), pair.get2());
        });
    }

    private void testIntersectFor(SetF<Integer> s1, SetF<Integer> s2) {
        SetF<Integer> is = s1.intersect(s2);

        assertEquals(is, s2.intersect(s1));

        for (int e : is) {
            assertTrue(s1.containsTs(e));
            assertTrue(s2.containsTs(e));
        }

        for (int e : s1) {
            assertTrue(is.containsTs(e) || !s2.containsTs(e));
        }
    }

    private void testUnionFor(SetF<Integer> s1, SetF<Integer> s2) {
        SetF<Integer> u = s1.plus(s2);

        assertEquals(u, s2.plus(s1));

        for (int e : u) {
            assertTrue(s1.containsTs(e) || s2.containsTs(e));
        }

        for (int e : s1) {
            assertTrue(u.containsTs(e));
        }
    }

    private void testMinusFor(SetF<Integer> s1, SetF<Integer> s2) {
        SetF<Integer> d = s1.minus(s2);

        for (int e : d) {
            assertTrue(s1.containsTs(e));
            assertFalse(s2.containsTs(e));
            assertFalse(s1.equals(s1.minus1(e)));
            assertEquals(s2, s2.minus1(e));
        }

        for (int e : s1) {
            assertTrue(d.containsTs(e) || s2.containsTs(e));
        }

        for (int e : s2) {
            assertFalse(d.containsTs(e));
        }
    }

} //~
