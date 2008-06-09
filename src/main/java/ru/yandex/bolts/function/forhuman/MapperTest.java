package ru.yandex.bolts.function.forhuman;

import junit.framework.TestCase;

import ru.yandex.bolts.function.Function1;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"UnusedDeclaration", "unchecked", "unused"})
public class MapperTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MapperTest.class);

    public static Mapper<Integer, Integer> plus1() {
        return BinaryFunctionTest.plusF().bind2(1);
    }

    public void testAndThen() {
        Mapper<Integer, String> m = plus1().andThen(Mapper.toStringM());
        m.toString();
        assertEquals("3", m.apply(2));
    }

    public void testCompose() {
        Mapper<Integer, String> m = Mapper.<Integer>toStringM().compose(plus1());
        m.toString();
        assertEquals("3", m.apply(2));
    }

    public void testIgnoreNull() {
        Mapper<Integer, Integer> m = plus1().ignoreNullM();
        assertNull(m.apply(null));
        assertEquals(3, (int) m.apply(2));
        m.toString();
    }

    public void testBind() {
        Factory<Integer> f = plus1().bind(2);
        assertEquals(3, (int) f.apply());
        f.toString();
    }

    private int x = 0;

    public void testIngoreResult() {
        Operation o = new Mapper() {
            public Object map(Object o) {
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
        assertSame("a", Mapper.identityM().apply("a"));
        assertSame(null, Mapper.identityM().apply(null));
        Mapper.identityM().toString();

        Mapper tsm = Mapper.toStringM();
        assertSame(tsm, Mapper.identityM().andThen(tsm));
        assertSame(tsm, Mapper.identityM().compose(tsm));
    }

    public void testAndThenComparator() {
        Comparator<Integer> c = Mapper.<Integer>toStringM().andThen(Comparator.<String>naturalComparator());
        assertTrue(c.gt(2, 11));
        c.toString();
    }

    public void testWrap() {
        Mapper<Integer, Integer> m = Mapper.wrap(new Function1<Integer, Integer>() {
            public Integer apply(Integer integer) {
                return integer - 1;
            }
        });
        assertEquals(2, (int) m.apply(3));
        m.toString();
    }
} //~
