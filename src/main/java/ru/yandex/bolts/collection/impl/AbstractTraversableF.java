package ru.yandex.bolts.collection.impl;

import java.util.NoSuchElementException;

import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * Base class for {@link AbstractIteratorF} and {@link AbstractCollectionF}
 * @author Stepan Koltsov
 *
 * @param <E> element type
 */
public abstract class AbstractTraversableF<E> implements TraversableF<E> {

    protected abstract IteratorF<E> iterator();

    @Override
    public void forEach(Function1V<? super E> closure) {
        IteratorF<E> i = iterator();
        while (i.hasNext()) {
            closure.apply(i.next());
        }
    }

    @Override
    public boolean forAll(Function1B<? super E> p) {
        IteratorF<E> i = iterator();
        while (i.hasNext()) {
            if (!p.apply(i.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean exists(Function1B<? super E> p) {
        IteratorF<E> i = iterator();
        while (i.hasNext()) {
            if (p.apply(i.next())) return true;
        }
        return false;
    }

    @Override
    public boolean existsW(boolean p) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public Option<E> find(Function1B<? super E> p) {
        IteratorF<E> i = iterator();
        while (i.hasNext()) {
            E e = i.next();
            if (p.apply(e)) return Option.some(e);
        }
        return Option.none();
    }

    @Override
    public Option<E> findW(boolean p) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public int count(Function1B<? super E> p) {
        IteratorF<E> i = iterator();
        int r = 0;
        while (i.hasNext()) {
            if (p.apply(i.next()))
                ++r;
        }
        return r;
    }

    @Override
    public E single() {
        IteratorF<E> i = iterator();
        E r = i.next();
        if (i.hasNext()) {
            throw new NoSuchElementException("more then one element");
        }
        return r;
    }

    @Override
    public Option<E> singleO() {
        IteratorF<E> i = iterator();
        Option<E> r = i.nextO();
        if (i.hasNext()) {
            throw new NoSuchElementException("more then one element");
        }
        return r;
    }

    @Override
    public <B> B foldLeft(B z, Function2<B, E, B> f) {
        IteratorF<E> i = iterator();
        B acc = z;
        while (i.hasNext()) {
            acc = f.apply(acc, i.next());
        }
        return acc;
    }

    @Override
    public <B> B foldLeftW(B z, B f) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public <B> B foldRight(B z, Function2<E, B, B> f) {
        IteratorF<E> i = iterator();
        if (i.hasNext()) {
            return f.apply(i.next(), foldRight(z, f));
        } else {
            return z;
        }
    }

    @Override
    public <B> B foldRightW(B z, B f) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public E reduceLeft(Function2<E, E, E> f) {
        return reduceLeftO(f).getOrThrow("empty.reduceLeft");
    }

    @Override
    public E reduceLeftW(E f) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public E reduceRight(Function2<E, E, E> f) {
        return reduceRightO(f).getOrThrow("empty.reduceRight");
    }

    @Override
    public E reduceRightW(E f) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public Option<E> reduceLeftO(Function2<E, E, E> f) {
        IteratorF<E> i = iterator();
        if (i.hasNext()) {
            return Option.some(foldLeft(i.next(), f));
        } else {
            return Option.none();
        }
    }

    @Override
    public Option<E> reduceLeftOW(E f) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public Option<E> reduceRightO(Function2<E, E, E> f) {
        IteratorF<E> i = iterator();
        if (!i.hasNext()) {
            return Option.none();
        }

        E head = i.next();
        if (i.hasNext()) {
            return Option.some(f.apply(head, reduceRight(f)));
        } else {
            return Option.some(head);
        }
    }

    @Override
    public Option<E> reduceRightOW(E f) {
        throw new RuntimeException("weaving must be enabled");
    }


    @Override
    public E min() {
        return min(Comparator.naturalComparator().<E, E>uncheckedCast());
    }

    @SuppressWarnings("unchecked")
    public E min(Function2I<? super E, ? super E> comparator) {
        return reduceLeft(Comparator.wrap((Function2I<E, E>) comparator).minF());
    }

    @Override
    public E minW(int comparator) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public Option<E> minO() {
        return minO(Comparator.naturalComparator().<E, E>uncheckedCast());
    }

    @Override
    public Option<E> minO(Function2I<? super E, ? super E> comparator) {
        if (!iterator().hasNext()) {
            return Option.none();
        } else {
            return Option.some(min(comparator));
        }
    }

    @Override
    public Option<E> minOW(int comparator) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public E max() {
        return max(Comparator.naturalComparator().<E, E>uncheckedCast());
    }

    @SuppressWarnings("unchecked")
    @Override
    public E max(Function2I<? super E, ? super E> comparator) {
        return reduceLeft(Comparator.wrap((Function2I<E, E>) comparator).maxF());
    }

    @Override
    public E maxW(int comparator) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public Option<E> maxO() {
        return maxO(Comparator.naturalComparator().<E, E>uncheckedCast());
    }

    @Override
    public Option<E> maxO(Function2I<? super E, ? super E> comparator) {
        if (!iterator().hasNext()) {
            return Option.none();
        } else {
            return Option.some(max(comparator));
        }
    }

    @Override
    public Option<E> maxOW(int comparator) {
        throw new RuntimeException("weaving must be enabled");
    }


    @Override
    public String mkString(String sep) {
        return mkString("", sep, "");
    }

    @Override
    public String mkString(String start, String sep, String end) {
        StringBuilder sb = new StringBuilder();

        sb.append(start);

        IteratorF<E> i = iterator();

        if (i.hasNext()) {
            sb.append(i.next());
        }

        while (i.hasNext()) {
            sb.append(sep).append(i.next());
        }

        sb.append(end);

        return sb.toString();
    }



} //~
