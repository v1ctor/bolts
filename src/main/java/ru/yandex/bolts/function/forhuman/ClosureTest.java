package ru.yandex.bolts.function.forhuman;

import junit.framework.TestCase;

import ru.yandex.bolts.function.Function0V;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"UnusedDeclaration", "unused"})
public class ClosureTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ClosureTest.class);

    private int x = 0;

    public void testAndThen() {
        int r = new Closure() {
            public void execute() {
                x = 77;
            }
        }.andThen(new Factory<Integer>() {
            public Integer create() {
                return 15;
            }
        }).apply();
        assertEquals(15, r);
        assertEquals(77, x);
    }

    public void testAsFacrtory() {
        int r = new Closure() {
            public void execute() {
                x = 77;
            }
        }.asFactory(15).create();
        assertEquals(15, r);
        assertEquals(77, x);
    }

    public void testNop() {
        Closure.nop().execute();
        Closure.nop().toString();
    }

    public void testWrapRunnable() {
        Closure c = Closure.wrap(new Runnable() {
            public void run() {
                x = 77;
            }
        });
        c.run();
        c.toString();
        assertEquals(77, x);
    }

    public void testWrapFunction() {
        Closure c = Closure.wrap(new Function0V() {
            public void apply() {
                x = 77;
            }
        });
        c.apply();
        c.toString();
        assertEquals(77, x);
    }
} //~
