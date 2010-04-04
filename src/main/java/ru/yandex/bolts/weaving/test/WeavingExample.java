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
    public void testMe() {
        ListF<Integer> r = Cf.list("1", "2", "3").mapW(Integer.parseInt(Cf.<String>p()));
        assertEquals(Cf.list(1, 2, 3), r);
    }

} //~
