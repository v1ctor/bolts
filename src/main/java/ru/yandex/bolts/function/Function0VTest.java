package ru.yandex.bolts.function;

import junit.framework.TestCase;


/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"UnusedDeclaration", "unused"})
public class Function0VTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Function0VTest.class);

    private int x = 0;

    public void testNop() {
        Function0V.nop().apply();
        Function0V.nop().toString();
    }

    public void testWrapRunnable() {
        Runnable r = () -> { x = 77; };
        Function0V c = Function0V.wrap(r);
        c.run();
        c.toString();
        assertEquals(77, x);
    }

    public void testWrapFunction() {
        Function0V r = () -> { x = 77; };
        Function0V c = Function0V.wrap(r);
        c.apply();
        c.toString();
        assertEquals(77, x);
    }
} //~
