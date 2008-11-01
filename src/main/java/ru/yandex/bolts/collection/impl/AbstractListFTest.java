package ru.yandex.bolts.collection.impl;

import java.util.Collection;
import java.util.List;

import static ru.yandex.bolts.collection.CollectionsF.list;
import static ru.yandex.bolts.collection.CollectionsF.set;

import junit.framework.TestCase;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.impl.test.GeneratorF;
import ru.yandex.bolts.function.forhuman.Closure;
import ru.yandex.bolts.function.forhuman.Mapper;
import ru.yandex.bolts.function.forhuman.MapperTest;
import ru.yandex.bolts.function.forhuman.Operation;
import ru.yandex.bolts.function.forhuman.Predicate;
import ru.yandex.bolts.function.forhuman.PredicateTest;
import ru.yandex.bolts.function.forhuman.BinaryFunctionTest;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings("unused")
public class AbstractListFTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AbstractListFTest.class);

    private void assertException(Closure closure) {
        try {
            closure.execute();
            fail("expecting exception");
        } catch (Exception e) {
            // ok
        }
    }

    public void testFirstLast() {
        ListF<Integer> list = list(1, 2, 3, 4, 5);
        assertEquals((Object) 1, list.first());
        assertEquals((Object) 5, list.last());

        assertException(new Closure() {
            public void execute() {
                list().first();
            }
        });

        assertException(new Closure() {
            public void execute() {
                list().last();
            }
        });
    }

    public void testTake() {
        ListF<Integer> list = list(1, 2, 3, 4, 5);
        assertEquals(list(), list.take(0));
        assertEquals(list(1, 2), list.take(2));
        assertEquals(list, list.take(5));
        assertEquals(list, list.take(6));
    }

    public void testDrop() {
        ListF<Integer> list = list(1, 2, 3, 4, 5);
        assertEquals(list, list.drop(0));
        assertEquals(list(3, 4, 5), list.drop(2));
        assertEquals(list(), list.drop(5));
        assertEquals(list(), list.drop(6));
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
        assertEquals(list(2, 4, 6), l.filter(PredicateTest.evenP()));
        assertEquals(Tuple2.tuple(list(2, 4, 6), list(1, 3, 5)), l.filter2(PredicateTest.evenP()));
    }

    public void testMap() {
        ListF<Integer> l = list(1, 2, 3);
        ListF<Integer> m = l.map(MapperTest.plus1());
        assertEquals(list(2, 3, 4), m);
    }

    public void testFlatMap() {
        ListF<Integer> l = list(0, 1, 2);
        ListF<Integer> m = l.flatMap(new Mapper<Integer, Collection<Integer>>() {
            public Collection<Integer> map(Integer integer) {
                return CollectionsF.repeat(integer, integer);
            }
        });
        assertEquals(list(1, 2, 2), m);
    }

    public void testFlatMapO() {
        ListF<Integer> l = list(1, 2, 3, 4);
        ListF<String> m = l.flatMapO(new Mapper<Integer, Option<String>>() {
            public Option<String> map(Integer integer) {
                if (integer % 2 == 1) return Option.some(integer.toString());
                else return Option.none();
            }
        });
        assertEquals(list("1", "3"), m);
    }

    public void testUnique() {
        GeneratorF.integers(1, 10).lists().checkForAllVerbose(new Operation<ListF<Integer>>() {
            public void execute(ListF<Integer> l) {
                SetF<Integer> u = l.unique();
                assertTrue(u.forAll(l.containsP()));
                assertTrue(l.forAll(u.containsP()));
            }
        });
        
        // simple
        ListF<Integer> l = list(1, 2, 3, 4, 1, 2, 1);
        assertEquals(set(1, 2, 3, 4), l.unique());
    }

    public void testForAll() {
        assertFalse(list(2, 3).forAll(PredicateTest.evenP()));
        assertTrue(list(2, 4).forAll(PredicateTest.evenP()));
        assertTrue(CollectionsF.<Integer>list().forAll(PredicateTest.evenP()));
    }

    public void testExists() {
        assertFalse(list(1, 3).exists(PredicateTest.evenP()));
        assertTrue(list(2, 3).exists(PredicateTest.evenP()));
        assertTrue(list(3, 2).exists(PredicateTest.evenP()));
    }

    public void testFind() {
        assertEquals(2, (int) list(1, 2, 3).find(PredicateTest.evenP()).get());
        assertFalse(list(1, 5, 3).find(PredicateTest.evenP()).isDefined());
    }
    
    private static <T> Predicate<Collection<T>> notEmptyP() {
        return new Predicate<Collection<T>>() {
            public boolean evaluate(Collection<T> a) {
                return a.size() > 0;
            }
        };
    }

    public void testReduce() {
        
        GeneratorF.strings().lists().filter(AbstractListFTest.<String>notEmptyP()).checkForAllVerbose(new Operation<ListF<String>>() {
            public void execute(ListF<String> a) {
                String expected = "";
                for (String s : a) expected += s;
                
                assertEquals(expected, a.reduceLeft(BinaryFunctionTest.stringPlusF()));
                assertEquals(expected, a.reduceRight(BinaryFunctionTest.stringPlusF()));
            }
        });
        
        // simple
        ListF<String> l = list("a", "b", "c");
        assertEquals("abc", l.reduceLeft(BinaryFunctionTest.stringPlusF()));
        assertEquals("abc", l.reduceRight(BinaryFunctionTest.stringPlusF()));
    }

    public void testFold() {
        
        GeneratorF.strings().lists().checkForAllVerbose(new Operation<ListF<String>>() {
            public void execute(ListF<String> a) {
                String expectedLeft = "x";
                for (String s : a) expectedLeft += s;
                
                String expectedRight = "";
                for (String s : a) expectedRight += s;
                expectedRight += "y";
                
                assertEquals(expectedLeft, a.foldLeft("x", BinaryFunctionTest.stringPlusF()));
                assertEquals(expectedRight, a.foldRight("y", BinaryFunctionTest.stringPlusF()));
            }
        });
        
        // simple
        ListF<String> l = list("a", "b", "c");
        assertEquals("xabc", l.foldLeft("x", BinaryFunctionTest.stringPlusF()));
        assertEquals("abcx", l.foldRight("x", BinaryFunctionTest.stringPlusF()));
    }

    public void testReverse() {
        ListF<Integer> l0 = Cf.arrayList();
        ListF<Integer> l1 = Cf.list(1);
        assertSame(l0, l0.reverse());
        assertSame(l1, l1.reverse());
        assertEquals(list(4, 3, 2, 1), list(1, 2, 3, 4).reverse());
    }

    public void testFirstLastO() {
        assertEquals(Option.some(1), list(1, 2, 3).firstO());
        assertEquals(Option.some("c"), list("a", "b", "c").lastO());
        assertEquals(Option.none(), list().firstO());
        assertEquals(Option.none(), list().lastO());
        assertEquals(Option.some(1), list(1).firstO());
    }
    
    public void testZipWithIndex() {
        ListF<String> l = list("a", "bb");
        List<Tuple2<String, Integer>> ll = l.zipWithIndex();

        assertEquals("a", ll.get(0).get1());
        assertEquals(Integer.valueOf(0), ll.get(0).get2());

        assertEquals("bb", ll.get(1).get1());
        assertEquals(Integer.valueOf(1), ll.get(1).get2());
    }

} //~
