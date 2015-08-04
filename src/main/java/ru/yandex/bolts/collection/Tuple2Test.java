package ru.yandex.bolts.collection;

import junit.framework.TestCase;

import ru.yandex.bolts.function.Function2;


@SuppressWarnings("unused")
public class Tuple2Test extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Tuple2Test.class);

    public void testKickEmma() {
        Tuple2.tuple(1, "a").toString();
        Tuple2.tuple(1, "a").hashCode();
        Tuple2.tuple(1, "a").equals(null);
    }

    public void testCons() {
        assertEquals(Tuple2.tuple(1, "a"), Tuple2.consF().apply(1, "a"));
    }

    public void testReduce() {
        assertEquals("1a", Tuple2.tuple(1, "a").reduce((i, s) -> i + s));
    }
} //~
