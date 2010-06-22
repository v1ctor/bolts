package ru.yandex.bolts.collection.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import junit.framework.TestCase;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.impl.test.Generator;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1BTest;
import ru.yandex.bolts.function.Function1V;

/**
 * @author Stepan Koltsov
 *
 * @see AbstractIteratorF
 */
@SuppressWarnings("unused")
public class AbstractIteratorFTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AbstractIteratorFTest.class);

    public void testPlus() {
        ListF<Integer> got = Cf.list(1, 2, 3).iterator().plus(Cf.list(4, 5).iterator()).toList();
        assertEquals(Cf.list(1, 2, 3, 4, 5), got);
    }

    public void testFlatMapSimple() {
        IteratorF<Integer> i = Cf.list(1, 2, 3).iterator().flatMap(new Function<Integer, IteratorF<Integer>>() {
            public IteratorF<Integer> apply(Integer a) {
                return Cf.repeat(a, a).iterator();
            }
        });

        // too simple

        assertTrue(i.hasNext());
        assertEquals(1, i.next().intValue());

        assertEquals(2, i.next().intValue());
        assertTrue(i.hasNext());
        assertEquals(2, i.next().intValue());

        assertTrue(i.hasNext());
        assertEquals(3, i.next().intValue());
        assertEquals(3, i.next().intValue());
        assertEquals(3, i.next().intValue());

        assertFalse(i.hasNext());

        try {
            i.next();
            fail();
        } catch (NoSuchElementException e) {
            // ok
        }
    }


    public void testFlatMap() {
        Generator.ints(1, 10).lists().lists().checkForAll(new Function1V<ListF<ListF<Integer>>>() {
            public void apply(ListF<ListF<Integer>> a) {
                checkFlatMapOn(a);
            }
        });
    }

    private void checkFlatMapOn(ListF<ListF<Integer>> l) {
        IteratorF<Integer> it = l.iterator().flatMap(new Function<ListF<Integer>, Iterator<Integer>>() {
            public Iterator<Integer> apply(ListF<Integer> a) {
                return a.iterator();
            }
        });
        ListF<Integer> expected = l.flatMap(Function.<ListF<Integer>>identityF());
        checkIteratorAgainst(it, expected);
    }


    public void testFlatMapL() {
        Generator.ints(1, 10).lists().lists().checkForAll(new Function1V<ListF<ListF<Integer>>>() {
            public void apply(ListF<ListF<Integer>> a) {
                checkFlatMapLOn(a);
            }
        });
    }

    private void checkFlatMapLOn(ListF<ListF<Integer>> l) {
        IteratorF<Integer> it = l.iterator().flatMapL(Function.<ListF<Integer>>identityF());
        ListF<Integer> expected = l.flatMap(Function.<ListF<Integer>>identityF());
        checkIteratorAgainst(it, expected);
    }



    public void testFilterSimple() {
        ListF<Integer> got = Cf.list(1, 2, 3, 4, 5, 6).iterator().filter(Function1BTest.evenF()).toList();

        // XXX to simple
        ListF<Integer> expected = Cf.list(2, 4, 6);
        assertEquals(expected, got);
    }

    private Generator<Integer> integers() {
        return Generator.ints();
    }

    private Generator<ListF<Integer>> listsOfIntegers() {
        return Generator.ints().lists();
    }

    private static final Random r = new Random();

    private ListF<Integer> even(ListF<Integer> integers) {
        ListF<Integer> r = Cf.arrayList();
        for (int e : integers) {
            if (e % 2 == 0) r.add(e);
        }
        return r;
    }

    public void testFilter3() {
        listsOfIntegers().checkForAll(new Function1V<ListF<Integer>>() {
            @Override
            public void apply(ListF<Integer> a) {
                testFilterOn(a);
            }
        });
    }

    private void testFilterOn(ListF<Integer> l) {
        checkIteratorAgainst(l.iterator().filter(Function1BTest.evenF()), l.filter(Function1BTest.evenF()));
    }

    private <E> void checkIteratorAgainst(IteratorF<E> it, ListF<E> elements) {
        for (int i = 0; i < elements.size(); ++i) {
            if (r.nextBoolean())
                assertTrue(it.hasNext());

            if (r.nextBoolean())
                assertTrue(it.hasNext());

            assertEquals(it.next(), elements.get(i));
        }

        assertFalse(it.hasNext());
        assertFalse(it.hasNext());
        assertFalse(it.hasNext());
    }

    public void testPaginate() {
        ListF<Integer> l = Cf.list(1, 2, 3, 4, 5);
        IteratorF<ListF<Integer>> i = l.iterator().paginate(3);
        assertTrue(i.hasNext());
        assertEquals(Cf.list(1, 2, 3), i.next());
        assertTrue(i.hasNext());
        assertEquals(Cf.list(4, 5), i.next());
        assertFalse(i.hasNext());
    }

    public void testPaginateExact() {
        ListF<Integer> l = Cf.list(1, 2, 3, 4);
        IteratorF<ListF<Integer>> i = l.iterator().paginate(2);
        assertTrue(i.hasNext());
        assertEquals(Cf.list(1, 2), i.next());
        assertTrue(i.hasNext());
        assertEquals(Cf.list(3, 4), i.next());
        assertFalse(i.hasNext());
    }

    public void testPaginateEmpty() {
        ListF<Integer> l = Cf.list();
        IteratorF<ListF<Integer>> i = l.iterator().paginate(3);
        assertFalse(i.hasNext());
    }

} //~
