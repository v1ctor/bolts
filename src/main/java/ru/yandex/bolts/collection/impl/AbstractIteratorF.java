package ru.yandex.bolts.collection.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.forhuman.Comparator;


public abstract class AbstractIteratorF<E> extends AbstractTraversableF<E> implements IteratorF<E> {

    @Override
    protected IteratorF<E> iterator() {
        return this;
    }

    private IteratorF<E> self() { return this; }

    @Override
    public ListF<E> toList() {
        return toListImpl(-1);
    }

    @Override
    public ListF<E> toList(int initialCapacity) {
        return toListImpl(initialCapacity);
    }

    private ListF<E> toListImpl(int initialCapacity) {
        if (!hasNext()) return Cf.list();

        ArrayListF<E> result;
        if (initialCapacity < 0) {
            result = new ArrayListF<>();
        } else {
            result = new ArrayListF<>(initialCapacity);
        }
        forEach(result.addF());
        return result.convertToReadOnly();
    }

    public SetF<E> toSet() {
        if (!hasNext()) return Cf.set();

        SetF<E> result = Cf.hashSet();
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
        class FlatMappedIterator extends AbstractIteratorF<B> {
            private IteratorF<B> cur = Cf.emptyIterator();

            @Override
            public boolean hasNext() {
                while (!cur.hasNext() && self().hasNext()) {
                    cur = Cf.x(f.apply(self().next()));
                }
                return cur.hasNext();
            }

            @Override
            public B next() {
                if (hasNext()) return cur.next();
                throw new NoSuchElementException("next on empty iterator");
            }

        }
        return new FlatMappedIterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <B> IteratorF<B> flatMapL(Function<? super E, ? extends Iterable<B>> f0) {
        Function<E, Iterable<B>> f = (Function<E, Iterable<B>>) f0;
        Function<E, Iterator<B>> g = f.andThen((Function<Iterable<B>, Iterator<B>>) Iterable::iterator);
        return flatMap(g);
    }

    @Override
    public <B> IteratorF<B> flatMapO(Function<? super E, Option<B>> f) {
        return flatMapL(f);
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

    @Override
    public IteratorF<E> filterNot(Function1B<? super E> f) {
        return filter(f.notF());
    }

    @Override
    public IteratorF<E> filterNotNull() {
        return filter(Function1B.<E>notNullF());
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

    @Override
    public int count() {
        return count(Function1B.trueF());
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

    @Override
    public ListF<E> takeSorted(int count) {
        return takeSorted(Comparator.naturalComparator().uncheckedCastC(), count);
    }

    @Override
    public ListF<E> takeSortedDesc(int count) {
        return takeSorted(Comparator.naturalComparator().invert().uncheckedCastC(), count);
    }


    @Override
    public ListF<E> takeSorted(java.util.Comparator<? super E> comparator, int count) {
        if (count == 0) {
            return Cf.list();
        }
        if (count < 0) {
            throw new IllegalArgumentException("K must be greater than 0");
        }
        FixedSizeTop<E> top = FixedSizeTop.cons(count, (java.util.Comparator<E>) comparator);
        while (hasNext()) {
            top.add(next());
        }
        return top.getTopElements();
    }

    @Override
    public ListF<E> takeSortedBy(Function<? super E, ?> f, int count) {
        return takeSorted(f.andThenNaturalComparator().nullLowC(), count);
    }

    @Override
    public ListF<E> takeSortedByDesc(Function<? super E, ?> f, int count) {
        return takeSorted(f.andThenNaturalComparator().nullLowC().invert(), count);
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

    @Override
    public IteratorF<ListF<E>> paginate(int pageSize) {
        if (pageSize <= 0) throw new IllegalArgumentException();
        return new AbstractIteratorF<ListF<E>>() {
            @Override
            public boolean hasNext() {
                return AbstractIteratorF.this.hasNext();
            }

            @Override
            public ListF<E> next() {
                if (!hasNext()) throw new NoSuchElementException();
                ArrayListF<E> items = new ArrayListF<>(pageSize);
                for (int i = 0; i < pageSize && AbstractIteratorF.this.hasNext(); ++i) {
                    items.add(AbstractIteratorF.this.next());
                }
                return items.convertToReadOnly();
            }
        };
    }
} //~
