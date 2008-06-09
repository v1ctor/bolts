package ru.yandex.bolts.collection.impl;

import java.util.Collections;
import java.util.Set;

import junit.framework.TestCase;

import static ru.yandex.bolts.collection.CollectionsF.set;

import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.function.forhuman.PredicateTest;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"UnusedDeclaration", "unused"})
public class AbstractSetFTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AbstractSetFTest.class);

    public void testFilter2() {
        SetF<Integer> s = set(1, 2, 3, 4);
        assertEquals(Tuple2.tuple(set(2, 4), set(1, 3)), s.filter2(PredicateTest.evenP()));
    }

    public void testUnion() {
        SetF<Integer> s = set(1, 2, 3);
        SetF<Integer> s1 = s.plus(set(3, 4, 5));
        assertEquals(set(1, 2, 3, 4, 5), s1);
    }

    public void testMinusEmpty() {
        SetF<Integer> s = set(1, 2, 3);
        SetF<Integer> es = CollectionsF.set();
        assertSame(s, s.minus(es));
        assertSame(es, es.minus(s));
    }
    
    public void testMinus() {
        assertEquals(set(1, 2), CollectionsF.x(set(1, 2, 3, 4)).minus(set(3, 4, 5, 6)));
    }

    public void testEqualsToRegular() {
        Set<String> regular = Collections.emptySet();
        SetF<String> our = CollectionsF.set();
        assertEquals(regular, our);
        assertEquals(our, regular);
        assertEquals(our, our);
    }
    
    public void testIntercect() {
        assertEquals(set(3, 4), set(1, 2, 3, 4).intersect(set(3, 4, 5, 6)));
    }

} //~
