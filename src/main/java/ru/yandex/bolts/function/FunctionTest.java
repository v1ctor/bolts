package ru.yandex.bolts.function;

import junit.framework.TestCase;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"UnusedDeclaration", "unchecked", "unused"})
public class FunctionTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FunctionTest.class);

    public static Function<Integer, Integer> plus1() {
        return Function2Test.plusF().bind2(1);
    }

    public void testAndThen() {
        Function<Integer, String> m = plus1().andThen(Function.toStringF());
        m.toString();
        assertEquals("3", m.apply(2));
    }

    public void testCompose() {
        Function<Integer, String> m = Function.<Integer>toStringF().compose(plus1());
        m.toString();
        assertEquals("3", m.apply(2));
    }

    public void testIgnoreNull() {
        Function<Integer, Integer> m = plus1().ignoreNullF();
        assertNull(m.apply(null));
        assertEquals(3, (int) m.apply(2));
        m.toString();
    }

    public void testBind() {
        Function0<Integer> f = plus1().bind(2);
        assertEquals(3, (int) f.apply());
        f.toString();
    }

    private int x = 0;

    public void testIngoreResult() {
        Function1V o = new Function() {
            public Object apply(Object o) {
                assertEquals(o, "a");
                x = 17;
                return o;
            }
        }.ignoreResult();
        o.apply("a");
        assertEquals(17, x);
        o.toString();
    }

    public void testIdentity() {
        assertSame("a", Function.identityF().apply("a"));
        assertSame(null, Function.identityF().apply(null));
        Function.identityF().toString();

        Function tsm = Function.toStringF();
        assertSame(tsm, Function.identityF().andThen(tsm));
        assertSame(tsm, Function.identityF().compose(tsm));
    }

    public void testAndThenComparator() {
        Comparator<Integer> c = Function.<Integer>toStringF().andThen(Comparator.<String>naturalComparator());
        assertTrue(c.gt(2, 11));
        c.toString();
    }

} //~
