package ru.yandex.bolts.function.forhuman;

import junit.framework.TestCase;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function2I;


@SuppressWarnings("unused")
public class ComparatorTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ComparatorTest.class);

    private <A> Comparator<A> throwC() {
        return new Comparator<A>() {
            public int compare(A o1, A o2) {
                throw new AssertionError();
            }

        };
    }

    public void testOp() {
        Comparator.Operator[] operators = {
                Comparator.Operator.EQ,
                Comparator.Operator.GE,
                Comparator.Operator.GT,
                Comparator.Operator.LE,
                Comparator.Operator.LT,
                Comparator.Operator.NE,
        };

        boolean compareResults[] = {
                false,
                true,
                true,
                false,
                false,
                true,
        };

        for (int i = 0; i < 6; ++i) {
            Comparator.Operator op = operators[i];
            boolean expectedResult = compareResults[i];

            assertEquals(expectedResult, Comparator.naturalComparator().op(op, "b", "a"));
        }

        assertEquals("<", Comparator.Operator.LT.getName());
        assertEquals("<=", Comparator.Operator.LE.toString());

        try {
            Comparator.naturalComparator().op(null, "a", "b");
            fail();
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    public void testCompose() {
        Comparator<Integer> c = Comparator.<String>naturalComparator().compose(Function.<Integer>toStringF());
        assertTrue(c.gt(2, 11));
        c.toString();
    }

    public void testBind() {
        assertTrue(Comparator.naturalComparator().bind1("a").lt("b"));
        assertTrue(Comparator.naturalComparator().bind2("a").gt("b"));
        Comparator.naturalComparator().bind1("a").toString();
        Comparator.naturalComparator().bind2("a").toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testNullLowC() {
        class NC implements Comparator<String> {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        }

        Comparator c = new NC().nullLowC();
        assertTrue(c.ge("b", "a"));
        assertTrue(c.lt("a", "b"));
        assertTrue(c.lt(null, "a"));
        assertTrue(c.gt("a", null));
        assertFalse(c.gt(null, null));
        assertTrue(c.eq(null, null));
        c.toString();
    }

    private void checkComparator(Comparator<String> c) {
        assertTrue(c.gt("b", "a"));
        assertTrue(c.le("a", "b"));
        c.toString();
    }

    public void testWrapComparator() {
        checkComparator(Comparator.wrap(new Function2I<String, String>() {
            public int apply(String s, String s1) {
                return Comparator.naturalComparator().compare(s, s1);
            }
        }));
    }

    public void testWrapFunction() {
        checkComparator(Comparator.wrap(new java.util.Comparator<String>() {
            public int compare(String o1, String o2) {
                return Comparator.naturalComparator().compare(o1, o2);
            }
        }));
    }

    public void testInvertInvert() {
        Comparator<String> c = Comparator.naturalComparator();
        assertSame(c.apply("1", "2"), c.invert().invert().apply("1", "2"));
    }


    public void testChainTo() {
        assertTrue(Comparator.<Integer>naturalComparator().chainTo(this.<Integer>throwC()).compare(1, 2) < 0);
        assertTrue(Comparator.<Integer>naturalComparator().chainTo(this.<Integer>throwC()).compare(2, 1) > 0);

        assertTrue(Comparator.<Integer>constEqualComparator().chainTo(Comparator.<Integer>naturalComparator()).compare(1, 2) < 0);
        assertTrue(Comparator.<Integer>constEqualComparator().chainTo(Comparator.<Integer>naturalComparator()).compare(2, 1) > 0);
        assertTrue(Comparator.<Integer>constEqualComparator().chainTo(Comparator.<Integer>naturalComparator()).compare(2, 2) == 0);
    }

    public void testNaturalComparator() {
        assertTrue(Comparator.<Integer>naturalComparator().compare(1, 2) < 0);
        assertTrue(Comparator.<Integer>naturalComparator().compare(2, 1) > 0);
        assertTrue(Comparator.<Integer>naturalComparator().compare(2, 2) == 0);
        assertTrue(Comparator.<Integer>naturalComparator().compare(null, null) == 0);
        assertTrue(Comparator.<Integer>naturalComparator().compare(null, 1) < 0);
        assertTrue(Comparator.<Integer>naturalComparator().compare(2, null) > 0);
    }

    public void testValueLowC() {
        assertTrue(Comparator.valueLowC("a").compare("a", "b")  <  0);
        assertTrue(Comparator.valueLowC("a").compare("b", "a")  >  0);
        assertTrue(Comparator.valueLowC("a").compare("a", "a")  == 0);
        assertTrue(Comparator.valueLowC("a").compare("b", "b")  == 0);
        assertTrue(Comparator.valueLowC("a").compare("b", null) == 0);
        assertTrue(Comparator.valueLowC("a").compare(null, "b") == 0);

        assertTrue(Comparator.valueLowC(null).compare(null, "b")  <  0);
        assertTrue(Comparator.valueLowC(null).compare("b", null)  >  0);
        assertTrue(Comparator.valueLowC(null).compare("b", "b")   == 0);
        assertTrue(Comparator.valueLowC(null).compare(null, null) == 0);

        Comparator<Integer> c = Comparator.valueLowC(999).chainTo(Comparator.<Integer>naturalComparator());
        assertTrue(c.compare(1, 1) == 0);
        assertTrue(c.compare(1, 2) <  0);
        assertTrue(c.compare(2, 1) >  0);
        assertTrue(c.compare(999, 1) < 0);
        assertTrue(c.compare(1, 999) > 0);
        assertTrue(c.compare(999, 999) == 0);
    }
} //~
