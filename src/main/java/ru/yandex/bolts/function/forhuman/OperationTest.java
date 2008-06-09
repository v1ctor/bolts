package ru.yandex.bolts.function.forhuman;

import junit.framework.TestCase;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"UnusedDeclaration", "unchecked", "unused"})
public class OperationTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OperationTest.class);

    public void testNop() {
        Operation.nop().execute(null);
        Operation.nop().toString();
    }

    public void testBind() {
        Closure cl = new Operation() {
            public void execute(Object o) {
                assertEquals("a", o);
            }
        }.bind("a");
        cl.execute();
        cl.toString();
    }

    public void testEatEmma() {
        Operation.nop().apply(null);
    }
} //~
