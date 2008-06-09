package ru.yandex.bolts.collection;

import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import ru.yandex.bolts.function.forhuman.Closure;
import ru.yandex.bolts.function.forhuman.Factory;
import ru.yandex.bolts.function.forhuman.Operation;
import ru.yandex.bolts.function.forhuman.Predicate;
import ru.yandex.bolts.function.forhuman.Mapper;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"UnusedDeclaration", "unchecked", "unused"})
public class OptionTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OptionTest.class);

    protected static <T> Operation<T> expect(final T t) {
        return new Operation<T>() {
            public void execute(T got) {
                assertEquals(got, t);
            }
        };
    }

    protected void assertThrows(Closure closure) {
        try {
            closure.execute();
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
        assertEquals("b", n.getOrElse(Factory.constF("b")));
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

    protected Factory throwFactory() {
        return Closure.throwC(new AssertionError()).asFactory(null);
    }

    protected Mapper throwMapper() {
        return throwFactory().asMapper();
    }

    public void testSize() {
        assertEquals(0, Option.none().size());
        assertEquals(1, Option.some(1).size());
    }

    public void testGet() {
        assertSame("a", Option.some("a").get(0));
        assertThrows(new Closure() {
            public void execute() {
                Option.none().get(0);
            }
        });
        assertThrows(new Closure() {
            public void execute() {
                Option.none().get(1);
            }
        });
        assertThrows(new Closure() {
            public void execute() {
                Option.none().get(2);
            }
        });
        assertThrows(new Closure() {
            public void execute() {
                Option.some(1).get(1);
            }
        });
        assertThrows(new Closure() {
            public void execute() {
                Option.some(1).get(2);
            }
        });
    }

    public void testMap() {
        assertEquals(Option.none(), Option.none().map(throwMapper()));
        assertEquals(Option.some("1"), Option.some(1).map(Mapper.toStringM()));
    }

    public Option<Throwable> tryCatch(Closure closure) {
        try {
            closure.execute();
            return Option.none();
        } catch (Throwable e) {
            return Option.some(e);
        }
    }

    public void testGetOrThrowMessage() {
        assertEquals("hello", tryCatch(new Closure() {
            public void execute() {
                Option.none().getOrThrow("hello");
            }
        }).get().getMessage());

        assertEquals("hello: 17", tryCatch(new Closure() {
            public void execute() {
                Option.none().getOrThrow("hello: ", 17);
            }
        }).get().getMessage());

        assertEquals("a", Option.some("a").getOrThrow("hello"));
        assertEquals("a", Option.some("a").getOrThrow("hello: ", 17));
    }

    public void testGetOrThrow() throws Exception {
        assertEquals("hello", Option.some("hello").getOrThrow(new Exception()));
        final RuntimeException e = new RuntimeException();
        assertSame(e, tryCatch(new Closure() {
            public void execute() {
                Option.none().getOrThrow(e);
            }
        }).get());
    }

    public void testFlatMap() {
        ListF<Integer> c = CollectionsF.list(1);
        assertTrue(Option.none().flatMap(Factory.constF(c).asMapper()).isEmpty());
        assertSame(c, Option.some(1).flatMap(expect(1).andThen(Factory.constF(c))));
    }

    public void testFlatMapO() {
        Option<Integer> s = Option.some(1);
        assertSame(Option.none(), Option.none().flatMapO(Factory.constF(s).asMapper()));
        assertSame(s, Option.some(1).flatMapO(expect(1).andThen(Factory.constF(s))));
    }

    public void testFilter() {
        assertSame(Option.none(), Option.none().filter(Predicate.trueP()));
        assertSame(Option.none(), Option.none().filter(Predicate.falseP()));
        assertSame(Option.none(), Option.some(1).filter(expect(1).chainTo(Predicate.<Integer>falseP())));
        Option<Integer> s = Option.some(1);
        assertSame(s, s.filter(expect(1).chainTo(Predicate.<Integer>trueP())));
    }

    public void testToString() {
        assertEquals("Some(1)", Option.some(1).toString());
        assertEquals("None", Option.none().toString());
    }

    public void testNotEmpty() {
        assertSame(Option.none(), Option.notEmpty(null));
        assertSame(Option.none(), Option.notEmpty(""));
        assertEquals(Option.some("a"), Option.notEmpty("a"));
    }

    public void testPredicates() {
        assertTrue(Option.isDefinedP().evaluate(Option.some(1)));
        assertFalse(Option.isDefinedP().evaluate(Option.none()));
        assertTrue(Option.isEmptyP().evaluate(Option.none()));
        assertFalse(Option.isEmptyP().evaluate(Option.some(1)));
        Option.isDefinedP().toString();
        Option.isEmptyP().toString();
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

    private Predicate<Option<String>> throwPredicate() {
        return new Predicate<Option<String>>() {
            public boolean evaluate(Option<String> option) {
                throw new AssertionError();
            }
        };
    }

    private static Object serializeDeserialize(Object o) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(o);
        return new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())).readObject();
    }

    public void testSerializable() throws Exception {
        assertEquals(Option.some("aa"), serializeDeserialize(Option.some("aa")));
        assertSame(Option.none(), serializeDeserialize(Option.none()));
    }
} //~
