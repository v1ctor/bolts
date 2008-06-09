package ru.yandex.bolts.function.forhuman;

import java.util.ArrayList;

import junit.framework.TestCase;

import ru.yandex.bolts.function.Function1B;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"unchecked", "UnusedDeclaration", "unused"})
public class PredicateTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PredicateTest.class);

    private static Predicate<Object> anotherTrueP() {
        return new Predicate<Object>() {
            public boolean evaluate(Object o) {
                return true;
            }
        };
    }

    private static Predicate<Object> anotherFalseP() {
        return new Predicate<Object>() {
            public boolean evaluate(Object o) {
                return false;
            }
        };
    }

    public void testAnd() {
        assertTrue(anotherTrueP().andP(anotherTrueP()).evaluate(null));
        assertFalse(anotherTrueP().andP(anotherFalseP()).evaluate(null));
        assertFalse(anotherFalseP().andP(anotherTrueP()).evaluate(null));
        assertFalse(anotherFalseP().andP(anotherFalseP()).evaluate(null));
        anotherFalseP().andP(anotherTrueP()).toString();
    }

    public void testOr() {
        assertTrue(anotherTrueP().orP(anotherTrueP()).evaluate(null));
        assertTrue(anotherTrueP().orP(anotherFalseP()).evaluate(null));
        assertTrue(anotherFalseP().orP(anotherTrueP()).evaluate(null));
        assertFalse(anotherFalseP().orP(anotherFalseP()).evaluate(null));
        anotherFalseP().orP(anotherTrueP()).toString();
    }

    public void testNot() {
        assertFalse(anotherTrueP().notP().evaluate(null));
        assertTrue(anotherFalseP().notP().evaluate(null));
        anotherTrueP().notP().toString();
        Predicate atp = anotherTrueP();
        assertSame(atp.notP().notP(), atp);
    }

    public void testTrue() {
        Predicate atp = anotherFalseP();
        Predicate tp = Predicate.trueP();
        assertSame(atp, tp.andP(atp));
        assertSame(tp, tp.orP(atp));
        tp.toString();

        assertFalse(tp.notP().evaluate(null));
    }

    public void testFalse() {
        Predicate afp = anotherFalseP();
        Predicate fp = Predicate.falseP();
        assertSame(fp, fp.andP(afp));
        assertSame(afp, fp.orP(afp));
        fp.toString();

        assertTrue(fp.notP().evaluate(null));
    }

    public void testAsMapper() {
        assertTrue(anotherTrueP().asMapper().map(null));
        assertFalse(anotherFalseP().asMapper().map(null));
        anotherTrueP().asMapper().toString();
    }

    public void testEquals() {
        assertTrue(Predicate.equalsP(new ArrayList()).evaluate(new ArrayList()));
        assertFalse(Predicate.equalsP("aa").evaluate("bb"));
        Predicate.equalsP("aa").toString();
    }

    public void testSame() {
        assertTrue(Predicate.sameP("a").evaluate("a"));
        assertFalse(Predicate.sameP(new ArrayList()).evaluate(new ArrayList()));
        Predicate.sameP("aa").toString();
    }

    public void testNotNull() {
        assertTrue(Predicate.notNullP().evaluate(""));
        assertFalse(Predicate.notNullP().evaluate(null));
        Predicate.notNullP().toString();
    }

    public void testAllOfP() {
        assertTrue(Predicate.allOfP().evaluate(null));
        assertTrue(Predicate.allOfP(anotherTrueP()).evaluate(null));
        assertTrue(Predicate.allOfP(anotherTrueP(), anotherTrueP()).evaluate(null));
        assertTrue(Predicate.allOfP(anotherTrueP(), anotherTrueP(), anotherTrueP()).evaluate(null));

        assertFalse(Predicate.allOfP(anotherFalseP()).evaluate(null));
        assertFalse(Predicate.allOfP(anotherFalseP(), anotherFalseP()).evaluate(null));
        assertFalse(Predicate.allOfP(anotherTrueP(), anotherFalseP()).evaluate(null));
        assertFalse(Predicate.allOfP(anotherFalseP(), anotherFalseP(), anotherTrueP()).evaluate(null));

        Predicate.allOfP(anotherTrueP(), anotherFalseP(), anotherTrueP()).toString();
    }

    public void testAnyOfP() {
        assertFalse(Predicate.anyOfP().evaluate(null));
        assertFalse(Predicate.anyOfP(anotherFalseP()).evaluate(null));
        assertFalse(Predicate.anyOfP(anotherFalseP(), anotherFalseP()).evaluate(null));
        assertFalse(Predicate.anyOfP(anotherFalseP(), anotherFalseP(), anotherFalseP()).evaluate(null));

        assertTrue(Predicate.anyOfP(anotherTrueP()).evaluate(null));
        assertTrue(Predicate.anyOfP(anotherTrueP(), anotherTrueP()).evaluate(null));
        assertTrue(Predicate.anyOfP(anotherTrueP(), anotherFalseP()).evaluate(null));
        assertTrue(Predicate.anyOfP(anotherFalseP(), anotherFalseP(), anotherTrueP()).evaluate(null));

        Predicate.anyOfP(anotherTrueP(), anotherFalseP(), anotherTrueP()).toString();
    }

    public void testWrapPredicate() {
        Function1B pf = new Function1B() {
            public boolean apply(Object o) {
                return o != null;
            }
        };
        Predicate p = Predicate.wrap(pf);
        assertTrue(p.evaluate(1));
        assertFalse(p.evaluate(null));
        p.toString();
    }

    public void testWrapMapper() {
        assertTrue(Predicate.wrap(anotherTrueP().asMapper()).evaluate(null));
        assertFalse(Predicate.wrap(anotherFalseP().asMapper()).evaluate(null));
        Predicate.wrap(anotherTrueP().asMapper()).toString();
    }

    public void testCompose() {
        Predicate<Integer> p = Predicate.equalsP("1").compose(Mapper.<Integer>toStringM());
        assertTrue(p.evaluate(1));
        assertFalse(p.evaluate(2));
        p.toString();
    }

    /** Integer is even */
    public static Predicate<Integer> evenP() {
        return new Predicate<Integer>() {
            public boolean evaluate(Integer integer) {
                return integer % 2 == 0;
            }
        };
    }
} //~
