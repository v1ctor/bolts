package ru.yandex.bolts.function;

import junit.framework.TestCase;


@SuppressWarnings("unused")
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
        Function0<String> f = Function0.constF(1).andThen(Function.toStringF());
        assertEquals("1", f.apply());
        f.toString();
    }

    public void testWrap() {
        Function0<Integer> f = Function0.wrap(() -> 16);
        assertEquals(16, (int) f.apply());
        f.toString();
    }
} //~
