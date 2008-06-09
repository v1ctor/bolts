package ru.yandex.bolts.function.forhuman;

import junit.framework.TestCase;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings("unused")
public class BinaryFunctionTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BinaryFunctionTest.class);

    private static BinaryFunction<Integer, Integer, Integer> minusF() {
        return new BinaryFunction<Integer, Integer, Integer>() {
            public Integer call(Integer a, Integer b) {
                return a - b;
            }
        };
    }

    public static BinaryFunction<Integer, Integer, Integer> plusF() {
        return new BinaryFunction<Integer, Integer, Integer>() {
            public Integer call(Integer a, Integer b) {
                return a + b;
            }
        };
    }

    public static BinaryFunction<String, String, String> stringPlusF() {
        return new BinaryFunction<String, String, String>() {
            public String call(String a, String b) {
                return a + b;
            }
        };
    }

    public void testKickEmma() {
        assertEquals(4, (int) minusF().apply(6, 2));
    }

    public void testBind() {
        Mapper<Integer, Integer> m1 = minusF().bind1(5);
        assertEquals(3, (int) m1.apply(2));

        Mapper<Integer, Integer> m2 = minusF().bind2(2);
        assertEquals(3, (int) m2.apply(5));

        m1.toString();
        m2.toString();
    }
} //~
