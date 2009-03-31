package ru.yandex.bolts.function;

import junit.framework.TestCase;


/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"UnusedDeclaration", "unused"})
public class Function0Test extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Function0Test.class);

    public void testConst() {
        assertEquals("a", Function0.constF("a").apply());
    }

    public void testAsMapper() {
        Function0<String> f = Function0.constF("a");
        assertEquals("a", f.asFunction().apply(null));
        f.asFunction().toString();
    }

    public void testAndThen() {
        Function0<String> f = Function0.constF(1).andThen(Function.<Integer>toStringF());
        assertEquals("1", f.apply());
        f.toString();
    }

    public void testWrap() {
        Function0<Integer> f = Function0.wrap(new Function0<Integer>() {
            public Integer apply() {
                return 16;
            }
        });
        assertEquals(16, (int) f.apply());
        f.toString();
    }
} //~