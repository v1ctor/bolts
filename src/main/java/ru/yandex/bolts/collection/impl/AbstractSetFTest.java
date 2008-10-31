package ru.yandex.bolts.collection.impl;

import static ru.yandex.bolts.collection.CollectionsF.set;

import java.util.Collections;
import java.util.Set;

import junit.framework.TestCase;
import net.java.quickcheck.Generator;
import net.java.quickcheck.QuickCheck;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import net.java.quickcheck.collection.Pair;
import net.java.quickcheck.generator.CombinedGenerators;
import net.java.quickcheck.generator.PrimitiveGenerators;

import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.impl.test.QuickCheckAdditions;
import ru.yandex.bolts.function.forhuman.PredicateTest;

/**
 * @author Stepan Koltsov
 */
public class AbstractSetFTest extends TestCase {

    public void testFilter2() {
        SetF<Integer> s = set(1, 2, 3, 4);
        assertEquals(Tuple2.tuple(set(2, 4), set(1, 3)), s.filter2(PredicateTest.evenP()));
    }

    public void testMinusEmpty() {
        SetF<Integer> s = set(1, 2, 3);
        SetF<Integer> es = CollectionsF.set();
        assertSame(s, s.minus(es));
        assertSame(es, es.minus(s));
    }
    
    public void testEqualsToRegular() {
        Set<String> regular = Collections.emptySet();
        SetF<String> our = CollectionsF.set();
        assertEquals(regular, our);
        assertEquals(our, regular);
        assertEquals(our, our);
    }
    
    /////
    // union/intersect/minus
    
    private Generator<SetF<Integer>> smallSets() {
        return QuickCheckAdditions.sets(PrimitiveGenerators.integers(1, 10));
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
        assertEquals(set(1, 2), CollectionsF.x(set(1, 2, 3, 4)).minus(set(3, 4, 5, 6)));
    }
    

    public void testBinaryOps() {
        QuickCheck.forAllVerbose(CombinedGenerators.pairs(smallSets(), smallSets()), new AbstractCharacteristic<Pair<SetF<Integer>, SetF<Integer>>>() {
            protected void doSpecify(Pair<SetF<Integer>, SetF<Integer>> pair) throws Throwable {
                testIntersectFor(pair.getFirst(), pair.getSecond());
                testUnionFor(pair.getFirst(), pair.getSecond());
                testMinusFor(pair.getFirst(), pair.getSecond());
            }
        });
    }
    
    private void testIntersectFor(SetF<Integer> s1, SetF<Integer> s2) {
        SetF<Integer> is = s1.intersect(s2);
        
        assertEquals(is, s2.intersect(s1));
        
        for (int e : is) {
            assertTrue(s1.contains(e));
            assertTrue(s2.contains(e));
        }
        
        for (int e : s1) {
            assertTrue(is.contains(e) || !s2.contains(e));
        }
    }
    
    private void testUnionFor(SetF<Integer> s1, SetF<Integer> s2) {
        SetF<Integer> u = s1.plus(s2);
        
        assertEquals(u, s2.plus(s1));
        
        for (int e : u) {
            assertTrue(s1.contains(e) || s2.contains(e));
        }
        
        for (int e : s1) {
            assertTrue(u.contains(e));
        }
    }
    
    private void testMinusFor(SetF<Integer> s1, SetF<Integer> s2) {
        SetF<Integer> d = s1.minus(s2);
        
        for (int e : d) {
            assertTrue(s1.contains(e));
            assertFalse(s2.contains(e));
        }
        
        for (int e : s1) {
            assertTrue(d.contains(e) || s2.contains(e));
        }
        
        for (int e : s2) {
            assertFalse(d.contains(e));
        }
    }

} //~
