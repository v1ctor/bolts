package ru.yandex.bolts.collection;

import junit.framework.TestCase;

import ru.yandex.bolts.function.Function2;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings("unused")
public class Tuple2Test extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Tuple2Test.class);

    public void testKickEmma() {
        Tuple2.tuple(1, "a").toString();
        Tuple2.tuple(1, "a").hashCode();
        Tuple2.tuple(1, "a").equals(null);
    }

    public void testCons() {
        assertEquals(Tuple2.tuple(1, "a"), Tuple2.consM().apply(1, "a"));
    }

    public void testReduce() {
        assertEquals("1a", Tuple2.tuple(1, "a").reduce(new Function2<Integer, String, String>() {
            public String apply(Integer i, String s) {
                return i + s;
            }
        }));
    }
} //~
