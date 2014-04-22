package ru.yandex.bolts.collection.impl.test;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Random;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.impl.AbstractIteratorF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;

/**
 * @author Stepan Koltsov
 */
public abstract class Generator<A> extends AbstractIteratorF<A> {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Generator.class);

    @Override
    public final boolean hasNext() {
        return true;
    }

    private Generator<A> self() { return this; }

    @Override
    public <B> Generator<B> map(Function<? super A, B> f) {
        return x(super.map(f));
    }

    @Override
    public Generator<A> filter(Function1B<? super A> f) {
        return x(super.filter(f));
    }

    public Generator<ListF<A>> lists() {
        return new ListGenerator<>(this, ints(0, 14));
    }

    public Generator<ListF<A>> lists(Generator<Integer> lengths) {
        return new ListGenerator<>(this, lengths);
    }

    public static Generator<Integer> ints() {
        return ints(0, 20);
    }

    protected final Random random = new Random();

    public static Generator<Integer> ints(final int min, final int maxExclusive) {
        return new Generator<Integer>() {
            public Integer next() {
                return min + random.nextInt(maxExclusive - min);
            }
        };
    }

    public Generator<SetF<A>> sets() {
        return lists().map(Cf.uniqueF());
    }

    public <U> Generator<Tuple2<A, U>> tuples(final Generator<U> g) {
        return new Generator<Tuple2<A,U>>() {
            public Tuple2<A, U> next() {
                return Tuple2.tuple(self().next(), g.next());
            }
        };
    }

    public Generator<Tuple2<A, A>> tuples() {
        return tuples(this);
    }

    private static final int C = 200;

    public void assertTrueForAll(Function1B<A> p) {
        for (int i = 0; i < C; ++i) {
            A next = next();
            logger.debug("Checking '" + next + "'");
            assertTrue(p.apply(next));
        }
    }

    public void checkForAll(Function1V<A> op) {
        for (int i = 0; i < C; ++i) {
            A next = next();
            logger.debug("Checking '" + next + "'");
            op.apply(next);
        }
    }



    public static <T> Generator<T> x(final Generator<T> g) {
        return new Generator<T>() {
            public T next() {
                return g.next();
            }
        };
    }

    public static <T> Generator<T> x(final Iterator<T> g) {
        return new Generator<T>() {
            public T next() {
                return g.next();
            }
        };
    }

    private static abstract class AbstractCharSequence implements CharSequence {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length(); ++i) {
                sb.append(charAt(i));
            }
            return sb.toString();
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return toString().subSequence(start, end);
        }
    }

    private static CharSequence letters() {
        return new AbstractCharSequence() {
            public char charAt(int index) { return (char) ('a' + index); }
            public int length() { return 'z' - 'a' + 1; }
        };
    }

    public static StringGenerator strings() {
        return strings(letters());
    }

    public static StringGenerator strings(CharSequence alphabet) {
        return strings(alphabet, ints(0, 20));
    }

    public static StringGenerator strings(CharSequence alphabet, Generator<Integer> lengths) {
        return new StringGenerator(alphabet, lengths);
    }

} //~
