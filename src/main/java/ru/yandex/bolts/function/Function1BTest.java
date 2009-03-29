package ru.yandex.bolts.function;

import java.util.ArrayList;

import junit.framework.TestCase;


/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"unchecked", "UnusedDeclaration", "unused"})
public class Function1BTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Function1BTest.class);

    private static Function1B<Object> anotherTrueP() {
        return new Function1B<Object>() {
            public boolean apply(Object o) {
                return true;
            }
        };
    }

    private static Function1B<Object> anotherFalseP() {
        return new Function1B<Object>() {
            public boolean apply(Object o) {
                return false;
            }
        };
    }

    public void testAnd() {
        assertTrue(anotherTrueP().andP(anotherTrueP()).apply(null));
        assertFalse(anotherTrueP().andP(anotherFalseP()).apply(null));
        assertFalse(anotherFalseP().andP(anotherTrueP()).apply(null));
        assertFalse(anotherFalseP().andP(anotherFalseP()).apply(null));
        anotherFalseP().andP(anotherTrueP()).toString();
    }

    public void testOr() {
        assertTrue(anotherTrueP().orP(anotherTrueP()).apply(null));
        assertTrue(anotherTrueP().orP(anotherFalseP()).apply(null));
        assertTrue(anotherFalseP().orP(anotherTrueP()).apply(null));
        assertFalse(anotherFalseP().orP(anotherFalseP()).apply(null));
        anotherFalseP().orP(anotherTrueP()).toString();
    }

    public void testNot() {
        assertFalse(anotherTrueP().notP().apply(null));
        assertTrue(anotherFalseP().notP().apply(null));
        anotherTrueP().notP().toString();
        Function1B atp = anotherTrueP();
        assertSame(atp.notP().notP(), atp);
    }

    public void testTrue() {
        Function1B atp = anotherFalseP();
        Function1B tp = Function1B.trueP();
        assertSame(atp, tp.andP(atp));
        assertSame(tp, tp.orP(atp));
        tp.toString();

        assertFalse(tp.notP().apply(null));
    }

    public void testFalse() {
        Function1B afp = anotherFalseP();
        Function1B fp = Function1B.falseP();
        assertSame(fp, fp.andP(afp));
        assertSame(afp, fp.orP(afp));
        fp.toString();

        assertTrue(fp.notP().apply(null));
    }

    public void testasFunction() {
        assertTrue(anotherTrueP().asFunction().apply(null));
        assertFalse(anotherFalseP().asFunction().apply(null));
        anotherTrueP().asFunction().toString();
    }

    public void testEquals() {
        assertTrue(Function1B.equalsP(new ArrayList()).apply(new ArrayList()));
        assertFalse(Function1B.equalsP("aa").apply("bb"));
        Function1B.equalsP("aa").toString();
    }

    public void testSame() {
        assertTrue(Function1B.sameP("a").apply("a"));
        assertFalse(Function1B.sameP(new ArrayList()).apply(new ArrayList()));
        Function1B.sameP("aa").toString();
    }

    public void testNotNull() {
        assertTrue(Function1B.notNullP().apply(""));
        assertFalse(Function1B.notNullP().apply(null));
        Function1B.notNullP().toString();
    }

    public void testAllOfP() {
        assertTrue(Function1B.allOfP().apply(null));
        assertTrue(Function1B.allOfP(anotherTrueP()).apply(null));
        assertTrue(Function1B.allOfP(anotherTrueP(), anotherTrueP()).apply(null));
        assertTrue(Function1B.allOfP(anotherTrueP(), anotherTrueP(), anotherTrueP()).apply(null));

        assertFalse(Function1B.allOfP(anotherFalseP()).apply(null));
        assertFalse(Function1B.allOfP(anotherFalseP(), anotherFalseP()).apply(null));
        assertFalse(Function1B.allOfP(anotherTrueP(), anotherFalseP()).apply(null));
        assertFalse(Function1B.allOfP(anotherFalseP(), anotherFalseP(), anotherTrueP()).apply(null));

        Function1B.allOfP(anotherTrueP(), anotherFalseP(), anotherTrueP()).toString();
    }

    public void testAnyOfP() {
        assertFalse(Function1B.anyOfP().apply(null));
        assertFalse(Function1B.anyOfP(anotherFalseP()).apply(null));
        assertFalse(Function1B.anyOfP(anotherFalseP(), anotherFalseP()).apply(null));
        assertFalse(Function1B.anyOfP(anotherFalseP(), anotherFalseP(), anotherFalseP()).apply(null));

        assertTrue(Function1B.anyOfP(anotherTrueP()).apply(null));
        assertTrue(Function1B.anyOfP(anotherTrueP(), anotherTrueP()).apply(null));
        assertTrue(Function1B.anyOfP(anotherTrueP(), anotherFalseP()).apply(null));
        assertTrue(Function1B.anyOfP(anotherFalseP(), anotherFalseP(), anotherTrueP()).apply(null));

        Function1B.anyOfP(anotherTrueP(), anotherFalseP(), anotherTrueP()).toString();
    }

    public void testWrapMapper() {
        assertTrue(Function1B.wrap(anotherTrueP().asFunction()).apply(null));
        assertFalse(Function1B.wrap(anotherFalseP().asFunction()).apply(null));
        Function1B.wrap(anotherTrueP().asFunction()).toString();
    }

    public void testCompose() {
        Function1B<Integer> p = Function1B.equalsP("1").compose(Function.<Integer>toStringF());
        assertTrue(p.apply(1));
        assertFalse(p.apply(2));
        p.toString();
    }

    /** Integer is even */
    public static Function1B<Integer> evenP() {
        return new Function1B<Integer>() {
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };
    }
} //~
