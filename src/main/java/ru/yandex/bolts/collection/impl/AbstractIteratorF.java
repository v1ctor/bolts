package ru.yandex.bolts.collection.impl;

import java.util.NoSuchElementException;
import java.util.Iterator;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.function.Function1;
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

        ListF<E> result = CollectionsF.arrayList();
        forEach(result.addOp());
        return result.unmodifiable();
    }

    public SetF<E> toSet() {
        if (!hasNext()) return CollectionsF.set();

        SetF<E> result = CollectionsF.hashSet();
        forEach(result.addOp());
        return result.unmodifiable();
    }

    @Override
    public <B> IteratorF<B> map(final Function1<? super E, B> f) {
        class MappedIterator extends AbstractIteratorF<B> {
            public boolean hasNext() {
                return AbstractIteratorF.this.hasNext();
            }

            public B next() {
                return f.apply(AbstractIteratorF.this.next());
            }

            public void remove() {
                AbstractIteratorF.this.remove();
            }
        }
        return new MappedIterator();
    }
    
    @Override
    public <B> IteratorF<B> flatMap(final Function1<? super E, ? extends Iterator<B>> f) {
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

    /*
    public Iterator<E> filter(Function1B<? super E> f) {
        class FilteredIterator extends AbstractIteratorF<E> {
            public boolean hasNext() {
                return false;
            }

            public E next() {
                
                return null;
            }
        }
        return new FilteredIterator();
    }
    */

    public IteratorF<Tuple2<E, Integer>> zipWithIndex() {
        class ZippedIterator extends AbstractIteratorF<Tuple2<E, Integer>> {
            private int i = 0;

            public boolean hasNext() {
                return AbstractIteratorF.this.hasNext();
            }

            public Tuple2<E, Integer> next() {
                return Tuple2.tuple(AbstractIteratorF.this.next(), i++);
            }

            public void remove() {
                AbstractIteratorF.this.remove();
            }
        }

        return new ZippedIterator();
    }

    public IteratorF<E> plus(final Iterator<E> b) {
        return new AbstractIteratorF<E>() {
            final IteratorF<E> a = AbstractIteratorF.this;

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

    public Option<E> find(Function1B<E> p) {
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

    public <B> B foldRight(B z, Function2<E, B, B> f) {
        if (hasNext()) return f.apply(next(), foldRight(z, f));
        else return z;
    }

    public E reduceLeft(Function2<E, E, E> f) {
        if (hasNext()) return foldLeft(next(), f);
        else throw new IllegalStateException("empty.reduceLeft");
    }

    public E reduceRight(Function2<E, E, E> f) {
        if (!hasNext()) throw new IllegalStateException("empty.reduceRight");
        E head = next();
        if (hasNext()) return f.apply(head, reduceRight(f));
        else return head;
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
                return left > 0 && AbstractIteratorF.this.hasNext();
            }

            public E next() {
                if (left == 0) throw new NoSuchElementException();
                E next = AbstractIteratorF.this.next();
                --left;
                return next;
            }
            
        };

        return new TakeIterator();
    }
    
} //~
