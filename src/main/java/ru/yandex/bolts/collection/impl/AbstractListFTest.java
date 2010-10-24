package ru.yandex.bolts.collection.impl;

import static ru.yandex.bolts.collection.CollectionsF.list;
import static ru.yandex.bolts.collection.CollectionsF.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.impl.test.Generator;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function0V;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1BTest;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2Test;
import ru.yandex.bolts.function.FunctionTest;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings("unused")
public class AbstractListFTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AbstractListFTest.class);

    private void assertException(Function0V closure) {
        try {
            closure.apply();
            fail("expecting exception");
        } catch (Exception e) {
            // ok
        }
    }

    public void testFirstLast() {
        ListF<Integer> list = list(1, 2, 3, 4, 5);
        assertEquals((Object) 1, list.first());
        assertEquals((Object) 5, list.last());

        assertException(new Function0V() {
            public void apply() {
                list().first();
            }
        });

        assertException(new Function0V() {
            public void apply() {
                list().last();
            }
        });
    }

    public void testTake() {
        ListF<Integer> list = list(1, 2, 3, 4, 5);
        assertEquals(list(), list.take(0));
        assertEquals(list(1, 2), list.take(2));
        assertSame(list, list.take(5));
        assertSame(list, list.take(6));
    }

    public void testDrop() {
        ListF<Integer> list = list(1, 2, 3, 4, 5);
        assertSame(list, list.drop(0));
        assertEquals(list(3, 4, 5), list.drop(2));
        assertEquals(list(), list.drop(5));
        assertEquals(list(), list.drop(6));
    }

    public void testTakeDropWhile() {
        Generator.ints().lists().checkForAll(new Function1V<ListF<Integer>>() {
            public void apply(ListF<Integer> a0) {
                for (boolean wrapper : CollectionsF.list(true, false)) {
                    ListF<Integer> a;
                    if (wrapper) {
                        a = Cf.x(new ArrayList<Integer>());
                        a.addAll(0);
                    } else {
                        a = a0;
                    }

                    Function1B<Integer> f = Cf.Integer.comparator().gtF(0);
                    ListF<Integer> b = a.takeWhile(f);
                    ListF<Integer> c = a.dropWhile(f);
                    assertEquals(a, b.plus(c));
                    assertTrue(b.forAll(f));
                    assertTrue(b.length() == a.length() || !f.apply(a.get(b.length())));
                }
            }
        });
    }

    public void testPlus() {
        ListF<Integer> l1 = list(1, 2, 3);
        ListF<Integer> l2 = list(4, 5, 6);
        assertEquals(list(1, 2, 3, 4, 5, 6), l1.plus(l2));
        assertEquals(list(1, 2, 3, 4, 5, 6), l1.plus(l2.toArray(Integer.class)));
    }

    public void testPlus1() {
        assertEquals(list(1, 2, 3), list(1).plus1(2).plus1(3));
    }

    public void testToList() {
        ListF<Integer> l = list(1, 2, 3);
        assertSame(l, l.toList());
    }

    public void testSort() {
        ListF<Integer> l0 = list();
        ListF<Integer> l1 = list(1);
        ListF<Integer> l2 = list(2, 1);
        ListF<Integer> l4 = list(1, 4, 2, 3);

        assertSame(l0, l0.sort());
        assertSame(l1, l1.sort());
        assertEquals(list(1, 2), l2.sort());
        assertEquals(list(1, 2, 3, 4), l4.sort());
    }

    public void testAddAllArray() {
        ListF<Integer> l = CollectionsF.arrayList(1, 2);
        l.addAll(new Integer[] { 3, 4, 5 });
        assertEquals(list(1, 2, 3, 4, 5), l);
    }

    public void testFilter() {
        ListF<Integer> l = list(1, 2, 3, 4, 5, 6);
        assertEquals(list(2, 4, 6), l.filter(Function1BTest.evenF()));
        assertEquals(Tuple2.tuple(list(2, 4, 6), list(1, 3, 5)), l.partition(Function1BTest.evenF()));
    }

    public void testMap() {
        ListF<Integer> l = list(1, 2, 3);
        ListF<Integer> m = l.map(FunctionTest.plus1());
        assertEquals(list(2, 3, 4), m);
    }

    public void testFlatMap() {
        ListF<Integer> l = list(0, 1, 2);
        ListF<Integer> m = l.flatMap(new Function<Integer, Collection<Integer>>() {
            public Collection<Integer> apply(Integer integer) {
                return CollectionsF.repeat(integer, integer);
            }
        });
        assertEquals(list(1, 2, 2), m);
    }

    public void testFlatMapO() {
        ListF<Integer> l = list(1, 2, 3, 4);
        ListF<String> m = l.flatMapO(new Function<Integer, Option<String>>() {
            public Option<String> apply(Integer integer) {
                if (integer % 2 == 1) return Option.some(integer.toString());
                else return Option.none();
            }
        });
        assertEquals(list("1", "3"), m);
    }

    public void testUnique() {
        Generator.ints(1, 10).lists().checkForAll(new Function1V<ListF<Integer>>() {
            public void apply(ListF<Integer> l) {
                SetF<Integer> u = l.unique();
                assertTrue(u.forAll(l.containsF()));
                assertTrue(l.forAll(u.containsF()));
            }
        });

        // simple
        ListF<Integer> l = list(1, 2, 3, 4, 1, 2, 1);
        assertEquals(set(1, 2, 3, 4), l.unique());
    }

    public void testForAll() {
        assertFalse(list(2, 3).forAll(Function1BTest.evenF()));
        assertTrue(list(2, 4).forAll(Function1BTest.evenF()));
        assertTrue(CollectionsF.<Integer>list().forAll(Function1BTest.evenF()));
    }

    public void testExists() {
        assertFalse(list(1, 3).exists(Function1BTest.evenF()));
        assertTrue(list(2, 3).exists(Function1BTest.evenF()));
        assertTrue(list(3, 2).exists(Function1BTest.evenF()));
    }

    public void testFind() {
        assertEquals(2, (int) list(1, 2, 3).find(Function1BTest.evenF()).get());
        assertFalse(list(1, 5, 3).find(Function1BTest.evenF()).isDefined());
    }

    public void testCount() {
        assertEquals(2, list(3, 2, 6).count(Function1BTest.evenF()));
    }

    private static <T> Function1B<Collection<T>> notEmptyF() {
        return new Function1B<Collection<T>>() {
            public boolean apply(Collection<T> a) {
                return a.size() > 0;
            }
        };
    }

    public void testReduce() {

        Generator.strings().lists().filter(AbstractListFTest.<String>notEmptyF()).checkForAll(new Function1V<ListF<String>>() {
            public void apply(ListF<String> a) {
                String expected = "";
                for (String s : a) expected += s;

                assertEquals(expected, a.reduceLeft(Function2Test.stringPlusF()));
                assertEquals(expected, a.reduceRight(Function2Test.stringPlusF()));
            }
        });

        // simple
        ListF<String> l = list("a", "b", "c");
        assertEquals("abc", l.reduceLeft(Function2Test.stringPlusF()));
        assertEquals("abc", l.reduceRight(Function2Test.stringPlusF()));
    }

    public void testFold() {

        Generator.strings().lists().checkForAll(new Function1V<ListF<String>>() {
            public void apply(ListF<String> a) {
                String expectedLeft = "x";
                for (String s : a) expectedLeft += s;

                String expectedRight = "";
                for (String s : a) expectedRight += s;
                expectedRight += "y";

                assertEquals(expectedLeft, a.foldLeft("x", Function2Test.stringPlusF()));
                assertEquals(expectedRight, a.foldRight("y", Function2Test.stringPlusF()));
            }
        });

        // simple
        ListF<String> l = list("a", "b", "c");
        assertEquals("xabc", l.foldLeft("x", Function2Test.stringPlusF()));
        assertEquals("abcx", l.foldRight("x", Function2Test.stringPlusF()));
    }

    public void testReverse() {
        ListF<Integer> l0 = Cf.arrayList();
        ListF<Integer> l1 = Cf.list(1);
        assertSame(l0, l0.reverse());
        assertSame(l1, l1.reverse());

        // simple
        assertEquals(list(4, 3, 2, 1), list(1, 2, 3, 4).reverse());

        Generator.ints().lists().checkForAll(new Function1V<ListF<Integer>>() {
            public void apply(ListF<Integer> a) {
                ListF<Integer> r = a.reverse();
                for (int i = 0; i < a.length(); ++i)
                    assertSame(a.get(i), r.get(r.length() - i - 1));
            }
        });
    }

    public void testFirstLastO() {
        assertEquals(Option.some(1), list(1, 2, 3).firstO());
        assertEquals(Option.some("c"), list("a", "b", "c").lastO());
        assertEquals(Option.none(), list().firstO());
        assertEquals(Option.none(), list().lastO());
        assertEquals(Option.some(1), list(1).firstO());
    }

    public void testZipWithIndex() {
        // simple
        ListF<String> l = list("a", "bb");
        List<Tuple2<String, Integer>> ll = l.zipWithIndex();

        assertEquals("a", ll.get(0).get1());
        assertEquals(Integer.valueOf(0), ll.get(0).get2());

        assertEquals("bb", ll.get(1).get1());
        assertEquals(Integer.valueOf(1), ll.get(1).get2());

        // better

        Generator.strings().lists().checkForAll(new Function1V<ListF<String>>() {
            public void apply(ListF<String> a) {
                ListF<Tuple2<String, Integer>> z = a.zipWithIndex();

                assertEquals(a.length(), z.length());

                for (int i = 0; i < z.length(); ++i) {
                    assertSame(a.get(i), z.get(i).get1());
                    assertEquals(i, z.get(i).get2().intValue());
                }
            }
        });
    }

    public void testIteratorToList() {
        ListF<Integer> l = Cf.list(1, 2, 3);

        IteratorF<Integer> i1 = l.iterator();
        i1.next();
        assertEquals(Cf.list(2, 3), i1.toList());

        IteratorF<Integer> i2 = l.iterator();
        assertEquals(Cf.list(1, 2, 3), i2.toList());

        IteratorF<Integer> i3 = l.iterator();
        i2.next();
        i2.next();
        i2.next();
        assertEquals(Cf.list(), i2.toList());
    }

} //~
