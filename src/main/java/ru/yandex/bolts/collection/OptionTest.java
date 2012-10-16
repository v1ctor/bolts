package ru.yandex.bolts.collection;

import junit.framework.TestCase;

import ru.yandex.bolts.collection.impl.test.SerializationUtils;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function0V;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"UnusedDeclaration", "unchecked", "unused"})
public class OptionTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OptionTest.class);

    protected static <T> Function1V<T> expect(final T t) {
        return new Function1V<T>() {
            public void apply(T got) {
                assertEquals(got, t);
            }
        };
    }

    protected void assertThrows(Function0V closure) {
        try {
            closure.apply();
            fail("expecting exception from " + closure);
        } catch (Exception e) {
            // ok
        }
    }

    public void testOrElseNotCalled() {
        Option<String> o = Option.some("ss");
        String got = o.orElse(throwFactory()).orElse(throwFactory()).get();
        assertEquals("ss", got);
    }

    public void testGetOrElse() {
        Option<String> s = Option.some("a");
        Option<String> n = Option.none();
        assertEquals("a", s.getOrElse("b"));
        assertEquals("a", s.getOrElse(throwFactory()));
        assertEquals("b", n.getOrElse("b"));
        assertEquals("b", n.getOrElse(Function0.constF("b")));
        assertEquals("a", s.getOrNull());
        assertEquals(null, n.getOrNull());
    }

    public void testOrElse() {
        Option<Integer> s1 = Option.some(1);
        Option<Integer> s2 = Option.some(2);
        Option<Integer> n = Option.none();
        assertSame(s1, s1.orElse(s2));
        assertSame(s1, s1.orElse(n));
        assertSame(s1, n.orElse(s1));
        assertSame(n, n.orElse(n));
    }

    public void testWhen() {
        Option<Integer> s1 = Option.when(true, 1);
        Option<Integer> s2 = Option.when(false, 2);
        Option<Integer> s3 = Option.when(s1.isDefined(), Function0.constF(3));;
        assertEquals(s1, Option.some(1));
        assertEquals(s2, Option.none());
        assertEquals(s3, Option.some(3));
    }

    protected Function0 throwFactory() {
        return new Function0<Object>() {
            public Object apply() {
                throw new AssertionError();
            }
        };
    }

    protected Function throwMapper() {
        return throwFactory().asFunction();
    }

    public void testSize() {
        assertEquals(0, Option.none().size());
        assertEquals(1, Option.some(1).size());
    }

    public void testGet() {
        assertSame("a", Option.some("a").get(0));
        assertThrows(new Function0V() {
            public void apply() {
                Option.none().get(0);
            }
        });
        assertThrows(new Function0V() {
            public void apply() {
                Option.none().get(1);
            }
        });
        assertThrows(new Function0V() {
            public void apply() {
                Option.none().get(2);
            }
        });
        assertThrows(new Function0V() {
            public void apply() {
                Option.some(1).get(1);
            }
        });
        assertThrows(new Function0V() {
            public void apply() {
                Option.some(1).get(2);
            }
        });
    }

    public void testMap() {
        assertEquals(Option.none(), Option.none().map(throwMapper()));
        assertEquals(Option.some("1"), Option.some(1).map(Function.toStringF()));
    }

    public Option<Throwable> tryCatch(Function0V closure) {
        try {
            closure.apply();
            return Option.none();
        } catch (Throwable e) {
            return Option.some(e);
        }
    }

    public void testGetOrThrowMessage() {
        assertEquals("hello", tryCatch(new Function0V() {
            public void apply() {
                Option.none().getOrThrow("hello");
            }
        }).get().getMessage());

        assertEquals("hello: 17", tryCatch(new Function0V() {
            public void apply() {
                Option.none().getOrThrow("hello: ", 17);
            }
        }).get().getMessage());

        assertEquals("a", Option.some("a").getOrThrow("hello"));
        assertEquals("a", Option.some("a").getOrThrow("hello: ", 17));
    }

    public void testGetOrThrow() throws Exception {
        assertEquals("hello", Option.some("hello").getOrThrow(new Exception()));
        final RuntimeException e = new RuntimeException();
        assertSame(e, tryCatch(new Function0V() {
            public void apply() {
                Option.none().getOrThrow(e);
            }
        }).get());
    }

    public void testToString() {
        assertEquals("Some(1)", Option.some(1).toString());
        assertEquals("None", Option.none().toString());
    }

    public void testPredicates() {
        assertTrue(Option.<Integer>isDefinedF().apply(Option.some(1)));
        assertFalse(Option.isDefinedF().apply(Option.none()));
        assertTrue(Option.isEmptyF().apply(Option.none()));
        assertFalse(Option.<Integer>isEmptyF().apply(Option.some(1)));
        Option.isDefinedF().toString();
        Option.isEmptyF().toString();
    }

    public void testToSet() {
        assertEquals(CollectionsF.set(1), Option.some(1).unique());
        assertEquals(CollectionsF.set(1), Option.some(1).toSet());
        assertEquals(CollectionsF.set(), Option.none().toSet());
        assertEquals(CollectionsF.set(), Option.none().unique());
    }

    public void testEquals() {
        assertTrue(Option.some(1).equals(Option.some(1)));
        assertFalse(Option.some(1).equals(Option.some(2)));

        assertFalse(Option.some(1).equals(Option.none()));
        assertFalse(Option.none().equals(Option.some(1)));

        assertTrue(Option.none().equals(Option.none()));
    }

    public void testHashCode() {
        Option.some(1).hashCode();
        Option.none().hashCode();
    }

    private Function1B<Option<String>> throwPredicate() {
        return new Function1B<Option<String>>() {
            public boolean apply(Option<String> option) {
                throw new AssertionError();
            }
        };
    }

    public void testSerializable() throws Exception {
        SerializationUtils.assertSerializedDeserializedToEqual(Option.some("aa"));
        SerializationUtils.assertSerializedDeserializedToSame(Option.none());
    }
} //~
