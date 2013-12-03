package ru.yandex.bolts.collection.impl;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
import ru.yandex.bolts.collection.Tuple2List;
import ru.yandex.bolts.collection.Tuple3;
import ru.yandex.bolts.collection.Tuple3List;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * Implementation of {@link CollectionF} algorithms.
 *
 * @author Stepan Koltsov
 */
public abstract class AbstractCollectionF<E> extends AbstractTraversableF<E> implements CollectionF<E> {

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        Iterator<E> e = iterator();
        if (o == null) {
            while (e.hasNext())
                if (e.next() == null)
                    return true;
        } else {
            while (e.hasNext())
                if (o.equals(e.next()))
                    return true;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        // Estimate size of array; be prepared to see more or fewer elements
        Object[] r = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < r.length; i++) {
            if (!it.hasNext()) // fewer elements than expected
                return Arrays.copyOf(r, i);
            r[i] = it.next();
        }
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // Estimate size of array; be prepared to see more or fewer elements
        int size = size();
        T[] r = a.length >= size ? a : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        Iterator<E> it = iterator();

        for (int i = 0; i < r.length; i++) {
            if (!it.hasNext()) { // fewer elements than expected
                if (a != r)
                    return Arrays.copyOf(r, i);
                r[i] = null; // null-terminate
                return r;
            }
            r[i] = (T) it.next();
        }
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
        int i = r.length;
        while (it.hasNext()) {
            int cap = r.length;
            if (i == cap) {
                int newCap = ((cap / 2) + 1) * 3;
                if (newCap <= cap) { // integer overflow
                    if (cap == Integer.MAX_VALUE)
                        throw new OutOfMemoryError("Required array size too large");
                    newCap = Integer.MAX_VALUE;
                }
                r = Arrays.copyOf(r, newCap);
            }
            r[i++] = (T) it.next();
        }
        // trim if overallocated
        return (i == r.length) ? r : Arrays.copyOf(r, i);
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        Iterator<E> e = iterator();
        if (o == null) {
            while (e.hasNext()) {
                if (e.next() == null) {
                    e.remove();
                    return true;
                }
            }
        } else {
            while (e.hasNext()) {
                if (o.equals(e.next())) {
                    e.remove();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<?> e = c.iterator();
        while (e.hasNext())
            if (!contains(e.next()))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        Iterator<? extends E> e = c.iterator();
        while (e.hasNext()) {
            if (add(e.next()))
                modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Iterator<?> e = iterator();
        while (e.hasNext()) {
            if (c.contains(e.next())) {
                e.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> e = iterator();
        while (e.hasNext()) {
            if (!c.contains(e.next())) {
                e.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        Iterator<E> e = iterator();
        while (e.hasNext()) {
            e.next();
            e.remove();
        }
    }

    @Override
    public String toString() {
        Iterator<E> i = iterator();
        if (! i.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = i.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! i.hasNext())
                return sb.append(']').toString();
            sb.append(", ");
        }
    }



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

    @Override
    public CollectionF<E> stableUnique() {
        return toList().stableUnique();
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
        else return iterator().map(f).toList(size());
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

    @Override
    public <B> Tuple2List<E, B> zipWithFlatMapO(Function<? super E, Option<B>> f) {
        return toList()
            .zipWith(f)
            .filterBy2(Option.<B>isDefinedF())
            .map2(Option.<B>getF())
        ;
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
    public ListF<E> sortByW(Object b) {
        return sortBy(Function.f(b));
    }

    public ListF<E> sortByDesc(Function<? super E, ?> f) {
        return sort(f.andThenNaturalComparator().nullLowC().invert());
    }

    @Override
    public ListF<E> sortByDescW(Object b) {
        return sortByDescW(Function.f(b));
    }

    @Override
    public ListF<E> shuffle() {
        ListF<E> r = Cf.arrayList(this);
        Collections.shuffle(r);
        return r.makeReadOnly();
    }

    public CollectionF<E> unmodifiable() {
        //if (this instanceof Unmodifiable) return this;
        return UnmodifiableDefaultCollectionF.wrap(this);
    }

    @Override
    public <K, V> MapF<K, V> toMap(Function<? super E, Tuple2<K, V>> mapper) {
        if (isEmpty()) return CollectionsF.map();
        else return CollectionsF.hashMap(map(mapper));
    }

    @Override
    public <K, V> MapF<K, V> toMapW(Tuple2<K, V> t) {
        return toMap(Function.f(t));
    }

    @Override
    public <K, V> MapF<K, V> toMap(Function<? super E, K> fk, Function<? super E, ? extends V> fv) {
        return toTuple2List(fk, fv).toMap();
    }

    @Override
    public <K, V> MapF<K, V> toMapW(K fk, V fv) {
        return toMap(Function.f(fk), Function.f(fv));
    }

    @Override
    public <K> MapF<K, E> toMapMappingToKey(final Function<? super E, K> mapper) {
        return toMap(new Function<E, Tuple2<K, E>>() {
            public Tuple2<K, E> apply(E e) {
                return Tuple2.tuple(mapper.apply(e), e);
            }
        });
    }

    @Override
    public <K> MapF<K, E> toMapMappingToKeyW(K m) {
        return toMapMappingToKey(Function.f(m));
    }

    @Override
    public <V> MapF<E, V> toMapMappingToValue(final Function<? super E, V> mapper) {
        return toMap(new Function<E, Tuple2<E, V>>() {
            public Tuple2<E, V> apply(E e) {
                return Tuple2.tuple(e, mapper.apply(e));
            }
        });
    }

    @Override
    public <V> MapF<E, V> toMapMappingToValueW(V m) {
        return toMapMappingToValue(Function.f(m));
    }

    @Override
    public <A, B> Tuple2List<A, B> toTuple2List(Function<? super E, ? extends A> fa, Function<? super E, ? extends B> fb) {
        return toTuple2List(Tuple2.join(fa.<E, A>uncheckedCast(), fb.<E, B>uncheckedCast()));
    }

    @Override
    public <A, B> Tuple2List<A,B> toTuple2ListW(A fa, B fb) {
        return toTuple2List(Function.f(fa), Function.f(fb));
    }

    @Override
    public <A, B> Tuple2List<A, B> toTuple2List(Function<? super E, Tuple2<A, B>> f) {
        return Cf.Tuple2List.cons(map(f));
    }

    @Override
    public <A, B> Tuple2List<A, B> toTuple2ListW(Tuple2<A, B> f) {
        return toTuple2List(Function.f(f));
    }

    @Override
    public <A, B, C> Tuple3List<A, B, C> toTuple3List(Function<? super E, ? extends A> fa, Function<? super E, ? extends B> fb, Function<? super E, ? extends C> fc) {
        return toTuple3List(Tuple3.join(fa.<E, A>uncheckedCast(), fb.<E, B>uncheckedCast(), fc.<E, C>uncheckedCast()));
    }

    @Override
    public <A, B, C> Tuple3List<A, B, C> toTuple3ListW(A fa, B fb, C fc) {
        return toTuple3List(Function.f(fa), Function.f(fb), Function.f(fc));
    }

    @Override
    public <A, B, C> Tuple3List<A, B, C> toTuple3List(Function<? super E, Tuple3<A, B, C>> f) {
        return Cf.Tuple3List.cons(map(f));
    }

    @Override
    public <A, B, C> Tuple3List<A, B, C> toTuple3ListW(Tuple3<A, B, C> f) {
        return toTuple3List(Function.f(f));
    }

    public <V> MapF<V, ListF<E>> groupBy(Function<? super E, ? extends V> m) {
        if (isEmpty()) return CollectionsF.map();

        MapF<V, ListF<E>> map = CollectionsF.hashMap();
        for (E e : this) {
            V key = m.apply(e);
            ListF<E> list = map.getOrElseUpdate(key, CollectionsF.<E>newArrayListF());
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

    public <F> CollectionF<F> uncheckedCast() {
        return cast();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F> CollectionF<F> cast() {
        return (CollectionF<F>) this;
    }

    @Override
    public <F> CollectionF<F> cast(Class<F> type) {
        for (E item : this) {
            type.cast(item);
        }
        return cast();
    }

    public static boolean equals(Object a, Object b) {
        if (b == null || a == null) return b == a;
        else return b.equals(a);
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
    public boolean removeAllTu(Collection<?> c) {
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
