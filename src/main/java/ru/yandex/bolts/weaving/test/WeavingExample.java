package ru.yandex.bolts.weaving.test;

import junit.framework.TestCase;

import org.junit.Test;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;

/**
 * @author Stepan Koltsov
 */
public class WeavingExample extends TestCase {
    @Test
    public void testMap() {
        ListF<Integer> r = Cf.list("1", "2", "3").mapW(Integer.parseInt(Cf.<String>p()));
        assertEquals(Cf.list(1, 2, 3), r);
    }

    @Test
    public void testFilter() {
        ListF<Integer> r = Cf.list(1, 2, 3).filterW(Cf.<Integer>p() % 2 == 0);
        assertEquals(Cf.list(2), r);
    }

    @Test
    public void testReduceLeft() {
        int sum = Cf.list(1, 2, 3).reduceLeftW(Cf.<Integer>p() + Cf.<Integer>p());
        assertEquals(6, sum);
    }

} //~
