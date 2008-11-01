package ru.yandex.bolts.collection.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import junit.framework.TestCase;
import net.java.quickcheck.Generator;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import net.java.quickcheck.generator.PrimitiveGenerators;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.impl.test.GeneratorF;
import ru.yandex.bolts.function.forhuman.Mapper;
import ru.yandex.bolts.function.forhuman.Operation;
import ru.yandex.bolts.function.forhuman.Predicate;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings("unused")
public class AbstractIteratorFTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AbstractIteratorFTest.class);

    public void testPlus() {
        ListF<Integer> got = Cf.list(1, 2, 3).iterator().plus(Cf.list(4, 5).iterator()).toList();
        assertEquals(Cf.list(1, 2, 3, 4, 5), got);
    }
    
    public void testFlatMapSimple() {
        IteratorF<Integer> i = Cf.list(1, 2, 3).iterator().flatMap(new Mapper<Integer, IteratorF<Integer>>() {
            public IteratorF<Integer> map(Integer a) {
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
        GeneratorF.integers(1, 10).lists().lists().checkForAllVerbose(new Operation<ListF<ListF<Integer>>>() {
            public void execute(ListF<ListF<Integer>> a) {
                checkFlatMapOn(a);
            }
        });
    }
    
    private void checkFlatMapOn(ListF<ListF<Integer>> i) {
        IteratorF<Integer> it = i.iterator().flatMap(new Mapper<ListF<Integer>, Iterator<Integer>>() {
            public Iterator<Integer> map(ListF<Integer> a) {
                return a.iterator();
            }
        });
        ListF<Integer> expected = i.flatMap(Mapper.<ListF<Integer>>identityM());
        checkIteratorAgainst(it, expected);
    }
    
    public void testFilterSimple() {
        ListF<Integer> got = Cf.list(1, 2, 3, 4, 5, 6).iterator().filter(evenP()).toList();
        
        // XXX to simple
        ListF<Integer> expected = Cf.list(2, 4, 6);
        assertEquals(expected, got);
    }

    private static Predicate<Integer> evenP() {
        return new Predicate<Integer>() {
            public boolean evaluate(Integer a) {
                return a % 2 == 0;
            }
        };
    }
    
    private static Predicate<Integer> oddP() {
        return evenP().notP();
    }
    
    private Generator<Integer> integers() {
        return PrimitiveGenerators.integers();
    }
    
    private GeneratorF<ListF<Integer>> listsOfIntegers() {
        return GeneratorF.integers().lists();
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
        listsOfIntegers().checkForAllVerbose(new AbstractCharacteristic<ListF<Integer>>() {
            protected void doSpecify(ListF<Integer> arg0) throws Throwable {
                testFilterOn(Cf.x(arg0));
            }
        });
    }
    
    private void testFilterOn(ListF<Integer> l) {
        checkIteratorAgainst(l.iterator().filter(evenP()), l.filter(evenP()));
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
    
} //~
