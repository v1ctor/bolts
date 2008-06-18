package ru.yandex.bolts.collection.impl;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.IterableF;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.function.Function1;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.forhuman.Comparator;
import ru.yandex.bolts.function.forhuman.Mapper;
import ru.yandex.bolts.function.forhuman.Operation;
import ru.yandex.bolts.function.forhuman.Predicate;

/**
 * Implementation of {@link CollectionF} algorithms.
 * 
 * @author Stepan Koltsov
 */
public abstract class AbstractCollectionF<E> extends AbstractCollection<E> implements CollectionF<E> {
    protected <B> CollectionF<B> newMutableCollection() {
        return CollectionsF.arrayList();
    }

    protected <B> CollectionF<B> emptyCollection() {
        return CollectionsF.list();
    }

    protected <B> CollectionF<B> singletonCollection(B b) {
        return CollectionsF.list(b);
    }

    protected <B> CollectionF<B> collect(IteratorF<B> iterator) {
        return iterator.toList();
    }

    public Predicate<E> containsP() {
        return new Predicate<E>() {
            public boolean evaluate(E e) {
                return contains(e);
            }
        };
    }

    public ListF<E> toList() {
        return CollectionsF.list(this);
    }

    public SetF<E> unique() {
        return CollectionsF.set(this);
    }
    
    @SuppressWarnings({"unchecked"})
    public E[] toArray(Class<E> cl) {
        return toArray((E[]) Array.newInstance(cl, size()));
    }
    
    private Object toPrimitiveArray(Class<?> componentClass) {
        Object array = Array.newInstance(componentClass, size());
        int i = 0;
        for (E e : this) {
            Array.set(array, i++, e);
        }
        return array;
    }
    
    public boolean[] toBooleanArray() {
        return (boolean[]) toPrimitiveArray(boolean.class);
    }
    
    public byte[] toByteArray() {
        return (byte[]) toPrimitiveArray(byte.class);
    }

    public char[] toCharArray() {
        return (char[]) toPrimitiveArray(char.class);
    }

    public double[] toDoubleArray() {
        return (double[]) toPrimitiveArray(double.class);
    }

    public float[] toFloatArray() {
        return (float[]) toPrimitiveArray(float.class);
    }

    public int[] toIntArray() {
        return (int[]) toPrimitiveArray(int.class);
    }

    public long[] toLongArray() {
        return (long[]) toPrimitiveArray(long.class);
    }

    public short[] toShortArray() {
        return (short[]) toPrimitiveArray(short.class);
    }

    public CollectionF<E> filter(Function1B<? super E> p) {
        if (isEmpty()) return this;

        CollectionF<E> result = newMutableCollection();
        for (E e : this) {
            if (p.apply(e)) result.add(e);
        }
        return result;
    }

    public abstract IteratorF<E> iterator();

    public Tuple2<? extends IterableF<E>, ? extends IterableF<E>> filter2(Function1B<? super E> p) {
        CollectionF<E> matched = this.newMutableCollection();
        CollectionF<E> unmatched = this.newMutableCollection();
        for (E e : this) {
            (p.apply(e) ? matched : unmatched).add(e);
        }
        return Tuple2.tuple(matched, unmatched);
    }

    public <B> ListF<B> map(Function1<? super E, B> f) {
        if (isEmpty()) return CollectionsF.list();
        else return iterator().map(f).toList();
    }

    public <B> ListF<B> flatMap(Function1<? super E, ? extends Collection<B>> f) {
        if (isEmpty()) return CollectionsF.list();

        ListF<B> result = CollectionsF.arrayList();
        for (E e : this) {
            result.addAll(f.apply(e));
        }
        return result;
    }

    public <B> ListF<B> flatMapO(final Function1<? super E, Option<B>> f) {
        if (isEmpty()) return CollectionsF.list();

        return flatMap(new Function1<E, Collection<B>>() {
            public Collection<B> apply(E e) {
                return f.apply(e).toList();
            }
        });
    }

    public Operation<E> addOp() {
        return new Operation<E>() {
            public void execute(E e) {
                add(e);
            }

            public String toString() {
                return AbstractCollectionF.this + ".add";
            }
        };
    }

    public CollectionF<E> plus1(E e) {
        if (isEmpty()) return singletonCollection(e);

        CollectionF<E> c = newMutableCollection();
        c.addAll(this);
        c.add(e);
        return c;
    }

    public CollectionF<E> plus(Collection<? extends E> elements) {
        return plus(elements.iterator());
    }

    @SuppressWarnings("unchecked")
    public CollectionF<E> plus(Iterator<? extends E> iterator) {
        if (!iterator.hasNext()) return this;

        if (isEmpty()) return (CollectionF<E>) collect(Cf.x(iterator));

        CollectionF<E> c = newMutableCollection();
        c.addAll(this);
        Cf.x(iterator).forEach(c.addOp());
        return c;
    }

    public CollectionF<E> plus(E... additions) {
        return plus(CollectionsF.list(additions));
    }

    @SuppressWarnings({"unchecked"})
    public ListF<E> sort() {
        if (size() <= 1) return toList();

        ListF<E> r = CollectionsF.arrayList(this);
        Collections.sort((List<Comparable>) r);
        return r;
    }

    @SuppressWarnings({"unchecked"})
    public ListF<E> sort(Function2I<? super E, ? super E> comparator) {
        if (size() <= 1) return toList();

        ListF<E> r = CollectionsF.arrayList(this);
        Collections.sort(r, Comparator.wrap((Function2I<E, E>) comparator));
        return r;
    }

    public ListF<E> sortBy(Function1<? super E, ?> f) {
        return sort(Mapper.wrap(f).andThenNaturalComparator());
    }
    
    public ListF<E> sortByDesc(Function1<? super E, ?> f) {
        return sort(Mapper.wrap(f).andThenNaturalComparator().invert());
    }

    public CollectionF<E> unmodifiable() {
        //if (this instanceof Unmodifiable) return this;
        return UnmodifiableDefaultCollectionF.wrap(this);
    }

    public void forEach(Function1V<? super E> f) {
        iterator().forEach(f);
    }

    public boolean forAll(Function1B<? super E> p) {
        return iterator().forAll(p);
    }

    public boolean exists(Function1B<? super E> p) {
        return iterator().exists(p);
    }

    public Option<E> find(Function1B<E> p) {
        return iterator().find(p);
    }

    public <B> B foldLeft(B z, Function2<B, E, B> f) {
        return iterator().foldLeft(z, f);
    }

    public <B> B foldRight(B z, Function2<E, B, B> f) {
        return iterator().foldRight(z, f);
    }

    public E reduceLeft(Function2<E, E, E> f) {
        return iterator().reduceLeft(f);
    }

    public E reduceRight(Function2<E, E, E> f) {
        return iterator().reduceRight(f);
    }

    public String mkString(String sep) {
        return mkString("", sep, "");
    }

    public String mkString(String start, String sep, String end) {
        StringBuilder sb = new StringBuilder();

        sb.append(start);

        IteratorF<E> i = iterator();

        if (i.hasNext()) sb.append(i.next());

        while (i.hasNext()) sb.append(sep).append(i.next());

        sb.append(end);

        return sb.toString();
    }

    public <K, V> MapF<K, V> toMap(Function1<? super E, Tuple2<K, V>> mapper) {
        if (isEmpty()) return CollectionsF.map();
        else return CollectionsF.hashMap(map(mapper));
    }

    public <K> MapF<K, E> toMapMappingToKey(final Function1<? super E, K> mapper) {
        return toMap(new Mapper<E, Tuple2<K, E>>() {
            public Tuple2<K, E> map(E e) {
                return Tuple2.tuple(mapper.apply(e), e);
            }
        });
    }

    public <V> MapF<E, V> toMapMappingToValue(final Function1<? super E, V> mapper) {
        return toMap(new Mapper<E, Tuple2<E, V>>() {
            public Tuple2<E, V> map(E e) {
                return Tuple2.tuple(e, mapper.apply(e));
            }
        });
    }

    public <V> MapF<V, ListF<E>> groupBy(Function1<? super E, ? extends V> m) {
        if (isEmpty()) return CollectionsF.map();

        MapF<V, ListF<E>> map = CollectionsF.hashMap();
        for (E e : this) {
            V key = m.apply(e);
            ListF<E> list = map.getOrElseUpdate(key, CollectionsF.<E>arrayList());
            list.add(e);
        }
        return map;
    }

    public Projection<E> projection() {
        throw new UnsupportedOperationException("not yet");
    }

    protected boolean eq(Object a, Object b) {
        return AbstractCollectionF.equals(a, b);
    }

    public void addAll(E... additions) {
        addAll(CollectionsF.list(additions));
    }

    public E single() throws NoSuchElementException {
        return singleO().get();
    }

    public Option<E> singleO() throws NoSuchElementException {
        return iterator().singleO();
    }
    
    @SuppressWarnings("unchecked")
    public <F> CollectionF<F> uncheckedCast() {
        return (CollectionF<F>) this;
    }

    public static boolean equals(Object a, Object b) {
        if (b == null || a == null) return b == a;
        else return b.equals(a);
    }
} //~
