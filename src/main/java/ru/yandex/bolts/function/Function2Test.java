package ru.yandex.bolts.function;


import junit.framework.TestCase;


@SuppressWarnings("unused")
public class Function2Test extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Function2Test.class);

    private static Function2<Integer, Integer, Integer> minusF() {
        return (a, b) -> a - b;
    }

    public static Function2<Integer, Integer, Integer> plusF() {
        return (a, b) -> a + b;
    }

    public static Function2<String, String, String> stringPlusF() {
        return (a, b) -> a + b;
    }

    public void testKickEmma() {
        assertEquals(4, (int) minusF().apply(6, 2));
    }

    public void testBind() {
        Function<Integer, Integer> m1 = minusF().bind1(5);
        assertEquals(3, (int) m1.apply(2));

        Function<Integer, Integer> m2 = minusF().bind2(2);
        assertEquals(3, (int) m2.apply(5));

        m1.toString();
        m2.toString();
    }
} //~
