package ru.yandex.bolts.function;

import java.util.ArrayList;

import junit.framework.TestCase;


/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"unchecked", "UnusedDeclaration", "unused"})
public class Function1BTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Function1BTest.class);

    private static Function1B<Object> anotherTrueF() {
        return o -> true;
    }

    private static Function1B<Object> anotherFalseF() {
        return o -> false;
    }

    public void testAnd() {
        assertTrue(anotherTrueF().andF(anotherTrueF()).apply(null));
        assertFalse(anotherTrueF().andF(anotherFalseF()).apply(null));
        assertFalse(anotherFalseF().andF(anotherTrueF()).apply(null));
        assertFalse(anotherFalseF().andF(anotherFalseF()).apply(null));
        anotherFalseF().andF(anotherTrueF()).toString();
    }

    public void testOr() {
        assertTrue(anotherTrueF().orF(anotherTrueF()).apply(null));
        assertTrue(anotherTrueF().orF(anotherFalseF()).apply(null));
        assertTrue(anotherFalseF().orF(anotherTrueF()).apply(null));
        assertFalse(anotherFalseF().orF(anotherFalseF()).apply(null));
        anotherFalseF().orF(anotherTrueF()).toString();
    }

    public void testNot() {
        assertFalse(anotherTrueF().notF().apply(null));
        assertTrue(anotherFalseF().notF().apply(null));
        anotherTrueF().notF().toString();
        Function1B atp = anotherTrueF();
        assertSame(atp.notF().notF(), atp);
    }

    public void testTrue() {
        Function1B atp = anotherFalseF();
        Function1B tp = Function1B.trueF();
        assertSame(atp, tp.andF(atp));
        assertSame(tp, tp.orF(atp));
        tp.toString();

        assertFalse(tp.notF().apply(null));
    }

    public void testFalse() {
        Function1B afp = anotherFalseF();
        Function1B fp = Function1B.falseF();
        assertSame(fp, fp.andF(afp));
        assertSame(afp, fp.orF(afp));
        fp.toString();

        assertTrue(fp.notF().apply(null));
    }

    public void testasFunction() {
        assertTrue(anotherTrueF().asFunction().apply(null));
        assertFalse(anotherFalseF().asFunction().apply(null));
        anotherTrueF().asFunction().toString();
    }

    public void testEquals() {
        assertTrue(Function1B.equalsF(new ArrayList()).apply(new ArrayList()));
        assertFalse(Function1B.equalsF("aa").apply("bb"));
        Function1B.equalsF("aa").toString();
    }

    public void testSame() {
        assertTrue(Function1B.sameF("a").apply("a"));
        assertFalse(Function1B.sameF(new ArrayList()).apply(new ArrayList()));
        Function1B.sameF("aa").toString();
    }

    public void testNotNull() {
        assertTrue(Function1B.notNullF().apply(""));
        assertFalse(Function1B.notNullF().apply(null));
        Function1B.notNullF().toString();
    }

    public void testAllOfF() {
        assertTrue(Function1B.allOfF().apply(null));
        assertTrue(Function1B.allOfF(anotherTrueF()).apply(null));
        assertTrue(Function1B.allOfF(anotherTrueF(), anotherTrueF()).apply(null));
        assertTrue(Function1B.allOfF(anotherTrueF(), anotherTrueF(), anotherTrueF()).apply(null));

        assertFalse(Function1B.allOfF(anotherFalseF()).apply(null));
        assertFalse(Function1B.allOfF(anotherFalseF(), anotherFalseF()).apply(null));
        assertFalse(Function1B.allOfF(anotherTrueF(), anotherFalseF()).apply(null));
        assertFalse(Function1B.allOfF(anotherFalseF(), anotherFalseF(), anotherTrueF()).apply(null));

        Function1B.allOfF(anotherTrueF(), anotherFalseF(), anotherTrueF()).toString();
    }

    public void testAnyOfF() {
        assertFalse(Function1B.anyOfF().apply(null));
        assertFalse(Function1B.anyOfF(anotherFalseF()).apply(null));
        assertFalse(Function1B.anyOfF(anotherFalseF(), anotherFalseF()).apply(null));
        assertFalse(Function1B.anyOfF(anotherFalseF(), anotherFalseF(), anotherFalseF()).apply(null));

        assertTrue(Function1B.anyOfF(anotherTrueF()).apply(null));
        assertTrue(Function1B.anyOfF(anotherTrueF(), anotherTrueF()).apply(null));
        assertTrue(Function1B.anyOfF(anotherTrueF(), anotherFalseF()).apply(null));
        assertTrue(Function1B.anyOfF(anotherFalseF(), anotherFalseF(), anotherTrueF()).apply(null));

        Function1B.anyOfF(anotherTrueF(), anotherFalseF(), anotherTrueF()).toString();
    }

    public void testWrapMapper() {
        assertTrue(Function1B.wrap(anotherTrueF().asFunction()).apply(null));
        assertFalse(Function1B.wrap(anotherFalseF().asFunction()).apply(null));
        Function1B.wrap(anotherTrueF().asFunction()).toString();
    }

    public void testCompose() {
        Function1B<Integer> p = Function1B.equalsF("1").compose(Function.<Integer>toStringF());
        assertTrue(p.apply(1));
        assertFalse(p.apply(2));
        p.toString();
    }

    /** Integer is even */
    public static Function1B<Integer> evenF() {
        return i -> i % 2 == 0;
    }
} //~
