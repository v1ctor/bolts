package ru.yandex.bolts.collection.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2;

/**
 * Implementation of {@link IteratorF} algorithms.
 *
 * @author Stepan Koltsov
 */
public abstract class AbstractIteratorF<E> implements IteratorF<E> {
    private IteratorF<E> self() { return this; }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public ListF<E> toList() {
        if (!hasNext()) return CollectionsF.list();

        ArrayListF<E> result = new ArrayListF<E>();
        forEach(result.addF());
        return result.convertToReadOnly();
    }

    public SetF<E> toSet() {
        if (!hasNext()) return CollectionsF.set();

        SetF<E> result = CollectionsF.hashSet();
        forEach(result.addF());
        return result.unmodifiable();
    }

    @Override
    public <B> IteratorF<B> map(final Function<? super E, B> f) {
        class MappedIterator extends AbstractIteratorF<B> {
            public boolean hasNext() {
                return self().hasNext();
            }

            public B next() {
                return f.apply(self().next());
            }

            public void remove() {
                self().remove();
            }
        }
        return new MappedIterator();
    }

    @Override
    public <B> IteratorF<B> flatMap(final Function<? super E, ? extends Iterator<B>> f) {
        // copy-paste of scala.Iterator.flatMap
        class FlatMappedIterator extends AbstractIteratorF<B> {
            private IteratorF<B> cur = Cf.emptyIterator();

            @Override
            public boolean hasNext() {
                if (cur.hasNext()) return true;
                else if (self().hasNext()) {
                    cur = Cf.x(f.apply(self().next()));
                    return hasNext();
                } else return false;
            }

            @Override
            public B next() {
                if (cur.hasNext()) return cur.next();
                else if (self().hasNext()) {
                    cur = Cf.x(f.apply(self().next()));
                    return next();
                } else throw new NoSuchElementException("next on empty iterator");
            }

        }
        return new FlatMappedIterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <B> IteratorF<B> flatMapL(Function<? extends E, ? extends Iterable<B>> f0) {
        Function<E, Iterable<B>> f = (Function<E, Iterable<B>>) f0;
        Function<E, Iterator<B>> g = f.andThen(new Function<Iterable<B>, Iterator<B>>() {
            public Iterator<B> apply(Iterable<B> a) {
                return a.iterator();
            }
        });
        return flatMap(g);
    }

    private static abstract class AbstractPrefetchingIterator<E> extends AbstractIteratorF<E> {
        private Option<E> next = Option.none();
        private boolean eof = false;

        private void fill() {
            while (!eof && next.isEmpty()) {
                next = fetchNext();
                eof = next.isEmpty();
            }
        }

        @Override
        public boolean hasNext() {
            fill();
            return !eof && next.isDefined();
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException("next on empty iterator");
            E r = next.get();
            next = Option.none();
            return r;
        }

        protected abstract Option<E> fetchNext();
    }

    @Override
    public IteratorF<E> filter(final Function1B<? super E> f) {
        class FilterIterator extends AbstractPrefetchingIterator<E> {

            @Override
            protected Option<E> fetchNext() {
                while (self().hasNext()) {
                    E e = self().next();
                    if (f.apply(e))
                        return Option.some(e);
                }
                return Option.none();
            }
        };

        return new FilterIterator();
    }

    public IteratorF<Tuple2<E, Integer>> zipWithIndex() {
        class ZippedIterator extends AbstractIteratorF<Tuple2<E, Integer>> {
            private int i = 0;

            public boolean hasNext() {
                return self().hasNext();
            }

            public Tuple2<E, Integer> next() {
                return Tuple2.tuple(self().next(), i++);
            }

            public void remove() {
                self().remove();
            }
        }

        return new ZippedIterator();
    }

    public IteratorF<E> plus(final Iterator<E> b) {
        return new AbstractIteratorF<E>() {
            final IteratorF<E> a = self();

            public boolean hasNext() {
                return a.hasNext() || b.hasNext();
            }

            public E next() {
                if (a.hasNext()) return a.next();
                else return b.next();
            }
        };
    }

    public IteratorF<E> unmodifiable() {
        return UnmodifiableDefaultIteratorF.wrap(this);
    }

    public Option<E> nextO() {
        if (hasNext()) return Option.some(next());
        else return Option.none();
    }

    public Option<E> singleO() {
        Option<E> r = nextO();
        if (hasNext()) throw new NoSuchElementException("more then one element");
        return r;
    }

    public void forEach(Function1V<? super E> closure) {
        while (hasNext()) closure.apply(next());
    }

    public boolean forAll(Function1B<? super E> p) {
        while (hasNext()) {
            if (!p.apply(next())) return false;
        }
        return true;
    }

    public boolean exists(Function1B<? super E> p) {
        while (hasNext()) {
            if (p.apply(next())) return true;
        }
        return false;
    }

    public Option<E> find(Function1B<? super E> p) {
        while (hasNext()) {
            E e = next();
            if (p.apply(e)) return Option.some(e);
        }
        return Option.none();
    }

    public <B> B foldLeft(B z, Function2<B, E, B> f) {
        B acc = z;
        while (hasNext()) {
            acc = f.apply(acc, next());
        }
        return acc;
    }

    @Override
    public <B> B foldLeftW(B z, B f) {
        throw new RuntimeException("weaving must be enabled");
    }

    public <B> B foldRight(B z, Function2<E, B, B> f) {
        if (hasNext()) return f.apply(next(), foldRight(z, f));
        else return z;
    }

    @Override
    public <B> B foldRightW(B z, B f) {
        throw new RuntimeException("weaving must be enabled");
    }

    public E reduceLeft(Function2<E, E, E> f) {
        if (hasNext()) return foldLeft(next(), f);
        else throw new IllegalStateException("empty.reduceLeft");
    }

    @Override
    public E reduceLeftW(E f) {
        throw new RuntimeException("weaving must be enabled");
    }

    public E reduceRight(Function2<E, E, E> f) {
        if (!hasNext()) throw new IllegalStateException("empty.reduceRight");
        E head = next();
        if (hasNext()) return f.apply(head, reduceRight(f));
        else return head;
    }

    @Override
    public E reduceRightW(E f) {
        throw new RuntimeException("weaving must be enabled");
    }

    public IteratorF<E> drop(int count) {
        int left = count;
        while (left > 0 && hasNext()) {
            next();
            --left;
        }
        return this;
    }

    public IteratorF<E> take(final int count) {
        if (count <= 0) return Cf.emptyIterator();

        class TakeIterator extends AbstractIteratorF<E> {
            int left = count;

            public boolean hasNext() {
                return left > 0 && self().hasNext();
            }

            public E next() {
                if (left == 0) throw new NoSuchElementException();
                E next = self().next();
                --left;
                return next;
            }

        };

        return new TakeIterator();
    }

    public IteratorF<E> dropWhile(Function1B<? super E> p) {
        // XXX: should be lazy
        while (hasNext()) {
            E e = next();
            if (!p.apply(e))
                return Cf.list(e).iterator().plus(this);
        }
        return Cf.emptyIterator();
    }

    public IteratorF<E> takeWhile(final Function1B<? super E> f) {
        class TakeWhileIterator extends AbstractPrefetchingIterator<E> {
            boolean end = false;

            @Override
            protected Option<E> fetchNext() {
                if (!end && self().hasNext()) {
                    E e = self().next();
                    if (f.apply(e))
                        return Option.some(e);
                    else {
                        end = true;
                        return Option.none();
                    }
                }
                return Option.none();
            }

        }

        return new TakeWhileIterator();
    }

} //~
