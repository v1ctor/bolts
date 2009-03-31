package ru.yandex.bolts.function;


import junit.framework.TestCase;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"UnusedDeclaration", "unchecked", "unused"})
public class Function1VTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Function1VTest.class);

    public void testNop() {
        Function1V.nop().apply(null);
        Function1V.nop().toString();
    }

    public void testBind() {
        Function0V cl = new Function1V() {
            public void apply(Object o) {
                assertEquals("a", o);
            }
        }.bind("a");
        cl.apply();
        cl.toString();
    }

    public void testEatEmma() {
        Function1V.nop().apply(null);
    }
} //~