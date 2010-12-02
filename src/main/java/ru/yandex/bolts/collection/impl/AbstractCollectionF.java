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
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.forhuman.Comparator;

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
        return Cf.list();
    }

    protected <B> ListF<B> emptyList() {
        return Cf.list();
    }

    protected <B> CollectionF<B> singletonCollection(B b) {
        return CollectionsF.list(b);
    }

    protected <B> CollectionF<B> collect(IteratorF<B> iterator) {
        return iterator.toList();
    }

    public final Function1B<E> containsP() {
        return containsF();
    }

    @Override
    public Function1B<E> containsF() {
        return new Function1B<E>() {
            public boolean apply(E e) {
                return contains(e);
            }
        };
    }

    @Override
    public boolean isNotEmpty() {
        return !isEmpty();
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

    @Override
    public CollectionF<E> filter(Function<? super E, Boolean> p) {
        return filter(Function1B.wrap(p));
    }

    @Override
    public CollectionF<E> filterW(boolean p) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public CollectionF<E> filterNotNull() {
        return filter(Function1B.<E>notNullF());
    }

    @Override
    public <F extends E> CollectionF<F> filterByType(Class<F> type) {
        return filter(Function1B.instanceOfF(type)).uncheckedCast();
    }

    public abstract IteratorF<E> iterator();

    @Override
    public Tuple2<? extends IterableF<E>, ? extends IterableF<E>> partition(Function1B<? super E> p) {
        CollectionF<E> matched = this.newMutableCollection();
        CollectionF<E> unmatched = this.newMutableCollection();
        for (E e : this) {
            (p.apply(e) ? matched : unmatched).add(e);
        }
        return Tuple2.tuple(matched, unmatched);
    }

    @Override
    public Tuple2<? extends IterableF<E>, ? extends IterableF<E>> partition(Function<? super E, Boolean> p) {
        return partition(Function1B.wrap(p));
    }

    @Override
    public Tuple2<? extends IterableF<E>, ? extends IterableF<E>> partitionW(boolean p) {
        throw new RuntimeException("weaving must be enabled");
    }

    public <B> ListF<B> map(Function<? super E, B> f) {
        if (isEmpty()) return Cf.list();
        else return iterator().map(f).toList();
    }

    @Override
    public <B> ListF<B> mapW(B b) {
        return map(Function.<E, B>getCurrent());
    }

    public <B> ListF<B> flatMap(Function<? super E, ? extends Collection<B>> f) {
        if (isEmpty()) return CollectionsF.list();

        ArrayListF<B> result = new ArrayListF<B>();
        for (E e : this) {
            result.addAll(f.apply(e));
        }
        return result.convertToReadOnly();
    }

    public <B> ListF<B> flatMapO(final Function<? super E, Option<B>> f) {
        if (isEmpty()) return CollectionsF.list();

        return flatMap(new Function<E, Collection<B>>() {
            public Collection<B> apply(E e) {
                return f.apply(e).toList();
            }
        });
    }

    @Override
    public <B> ListF<B> flatMapW(Collection<? extends B> f) {
        return flatMap(Function.f(f)).uncheckedCast();
    }

    public final Function1V<E> addOp() {
        return addF();
    }

    public Function1V<E> addF() {
        return new Function1V<E>() {
            public void apply(E e) {
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
        Cf.x(iterator).forEach(c.addF());
        return c;
    }

    public CollectionF<E> plus(E... additions) {
        return plus(CollectionsF.list(additions));
    }

    @SuppressWarnings({"unchecked"})
    public ListF<E> sort() {
        if (size() <= 1) return toList();

        ArrayListF<E> r = new ArrayListF<E>(this);
        Collections.sort((List<Comparable>) r);
        return r.convertToReadOnly();
    }

    @Override
    public ListF<E> sort(java.util.Comparator<? super E> comparator) {
        if (size() <= 1) return toList();

        ArrayListF<E> r = new ArrayListF<E>(this);
        Collections.sort(r, comparator);
        return r.convertToReadOnly();
    }

    @Override
    public ListF<E> sort(Comparator<? super E> comparator) {
        return sort((java.util.Comparator<? super E>) comparator);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public ListF<E> sort(Function2I<? super E, ? super E> comparator) {
        return sort(Comparator.wrap((Function2I<E, E>) comparator));
    }

    @Override
    public ListF<E> sortW(int comparator) {
        throw new RuntimeException("weaving must be enabled");
    }

    public ListF<E> sortBy(Function<? super E, ?> f) {
        return sort(f.andThenNaturalComparator().nullLowC());
    }

    @Override
    public ListF<E> sortByW(Object f) {
        throw new RuntimeException("weaving must be enabled");
    }

    public ListF<E> sortByDesc(Function<? super E, ?> f) {
        return sort(f.andThenNaturalComparator().nullLowC().invert());
    }

    @Override
    public ListF<E> sortByDescW(Object f) {
        throw new RuntimeException("weaving must be enabled");
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

    @Override
    public boolean forAllW(boolean p) {
        return forAll(Function1B.f(p));
    }

    public boolean exists(Function1B<? super E> p) {
        return iterator().exists(p);
    }

    @Override
    public boolean existsW(boolean p) {
        return exists(Function1B.f(p));
    }

    public Option<E> find(Function1B<? super E> p) {
        return iterator().find(p);
    }

    @Override
    public Option<E> findW(boolean p) {
        return find(Function1B.f(p));
    }

    @Override
    public int count(Function1B<? super E> p) {
        return iterator().count(p);
    }

    public <B> B foldLeft(B z, Function2<B, E, B> f) {
        return iterator().foldLeft(z, f);
    }

    @Override
    public <B> B foldLeftW(B z, B f) {
        throw new RuntimeException("weaving must be enabled");
    }

    public <B> B foldRight(B z, Function2<E, B, B> f) {
        return iterator().foldRight(z, f);
    }

    @Override
    public <B> B foldRightW(B z, B f) {
        throw new RuntimeException("weaving must be enabled");
    }

    public E reduceLeft(Function2<E, E, E> f) {
        return iterator().reduceLeft(f);
    }

    @Override
    public E reduceLeftW(E f) {
        throw new RuntimeException("weaving must be enabled");
    }

    public E reduceRight(Function2<E, E, E> f) {
        return iterator().reduceRight(f);
    }

    @Override
    public E reduceRightW(E f) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public Option<E> reduceLeftO(Function2<E, E, E> f) {
        return iterator().reduceLeftO(f);
    }

    @Override
    public Option<E> reduceLeftOW(E f) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public Option<E> reduceRightO(Function2<E, E, E> f) {
        return iterator().reduceRightO(f);
    }

    @Override
    public Option<E> reduceRightOW(E f) {
        throw new RuntimeException("weaving must be enabled");
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

    public <K, V> MapF<K, V> toMap(Function<? super E, Tuple2<K, V>> mapper) {
        if (isEmpty()) return CollectionsF.map();
        else return CollectionsF.hashMap(map(mapper));
    }

    @Override
    public <K, V> MapF<K, V> toMapW(Tuple2<K, V> t) {
        throw new RuntimeException("weaving must be enabled");
    }

    public <K> MapF<K, E> toMapMappingToKey(final Function<? super E, K> mapper) {
        return toMap(new Function<E, Tuple2<K, E>>() {
            public Tuple2<K, E> apply(E e) {
                return Tuple2.tuple(mapper.apply(e), e);
            }
        });
    }

    @Override
    public <K> MapF<K, E> toMapMappingToKeyW(K m) {
        throw new RuntimeException("weaving must be enabled");
    }

    public <V> MapF<E, V> toMapMappingToValue(final Function<? super E, V> mapper) {
        return toMap(new Function<E, Tuple2<E, V>>() {
            public Tuple2<E, V> apply(E e) {
                return Tuple2.tuple(e, mapper.apply(e));
            }
        });
    }

    @Override
    public <V> MapF<E, V> toMapMappingToValueW(V m) {
        throw new RuntimeException("weaving must be enabled");
    }

    public <V> MapF<V, ListF<E>> groupBy(Function<? super E, ? extends V> m) {
        if (isEmpty()) return CollectionsF.map();

        MapF<V, ListF<E>> map = CollectionsF.hashMap();
        for (E e : this) {
            V key = m.apply(e);
            ListF<E> list = map.getOrElseUpdate(key, CollectionsF.<E>arrayList());
            list.add(e);
        }
        return map;
    }

    @Override
    public <V> MapF<V, ListF<E>> groupByW(V m) {
        return groupBy(Function.f(m));
    }

    protected boolean eq(Object a, Object b) {
        return AbstractCollectionF.equals(a, b);
    }

    public void addAll(E... additions) {
        addAll(CollectionsF.list(additions));
    }

    public E single() throws NoSuchElementException {
        return iterator().single();
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

    public E max() {
        return max(Comparator.naturalComparator().<E, E>uncheckedCast());
    }

    @SuppressWarnings("unchecked")
    public E max(Function2I<? super E, ? super E> comparator) {
        return reduceLeft(Comparator.wrap((Function2I<E, E>) comparator).maxF());
    }

    @Override
    public E maxW(int comparator) {
        throw new RuntimeException("weaving must be enabled");
    }

    @Override
    public CollectionF<E> tee(Function1V<? super CollectionF<E>> f) {
        f.apply(this);
        return this;
    }

    @Override
    public ListF<ListF<E>> paginate(int pageSize) {
        return iterator().paginate(pageSize).toList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F> ListF<F> flatten() {
        return Cf.flatten((CollectionF<? extends Collection<F>>) this);
    }

    @Override
    public boolean removeTs(E e) {
        return remove(e);
    }

    @Override
    public boolean removeTu(Object e) {
        return remove(e);
    }

    @Override
    public boolean containsTs(E e) {
        return contains(e);
    }

    @Override
    public boolean removeAllRu(Collection<?> c) {
        return removeAll(c);
    }

    @Override
    public boolean removeAllTs(Collection<? extends E> c) {
        return removeAll(c);
    }

    @Override
    public boolean containsTu(Object e) {
        return contains(e);
    }

    @Override
    public boolean containsAllTs(Collection<? extends E> coll) {
        return containsAll(coll);
    }

    @Override
    public boolean containsAllTu(Collection<?> coll) {
        return containsAll(coll);
    }

    @Override
    public boolean retainAllTs(Collection<? extends E> c) {
        return retainAll(c);
    }

    @Override
    public boolean retainAllTu(Collection<?> c) {
        return retainAll(c);
    }

} //~
