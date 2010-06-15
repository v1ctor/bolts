package ru.yandex.bolts.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2B;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.Function2V;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * Looks like map, but actually it is a list of {@link Tuple2} with handy interface.
 *
 * @author Stepan Koltsov
 * @author Iliya Roubin
 *
 * @deprecated
 * @see Tuple2List
 */
public class ListMap<K, V> extends Tuple2List<K, V> {
    private ListMap(List<Tuple2<K, V>> list) {
        super(list);
    }

    public ListMap() {
        super(new ArrayList<Tuple2<K, V>>());
    }

    public ListMap(Collection<Tuple2<K, V>> elements) {
        super(new ArrayList<Tuple2<K, V>>(elements));
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected ListMap<K, V> newMutableCollection() {
        return ListMap.arrayList();
    }

    @Override
    public ListMap<K, V> filter(Function1B<? super Tuple2<K, V>> p) {
        return (ListMap<K, V>) super.filter(p);
    }

    /**
     * @deprecated {@link #partitionLm(Function1B)}
     */
    @SuppressWarnings({"unchecked", "RedundantCast"})
    public Tuple2<ListMap<K, V>, ListMap<K, V>> filter2ToListMaps(Function1B<? super Tuple2<K, V>> p) {
        return (Tuple2) super.filter2(p);
    }

    @SuppressWarnings("unchecked")
    public Tuple2<ListMap<K, V>, ListMap<K, V>> partitionLm(Function1B<? super Tuple2<K, V>> p) {
        return (Tuple2) super.partition(p);
    }

    public Tuple2<ListMap<K, V>, ListMap<K, V>> partitionByKey(Function1B<? super K> p) {
        return partitionLm(Tuple2.<K, V>get1F().andThen(p));
    }

    public Tuple2<ListMap<K, V>, ListMap<K, V>> partitionByValue(Function1B<? super V> p) {
        return partitionLm(Tuple2.<K, V>get2F().andThen(p));
    }

    @Override
    public ListMap<K, V> subList(int fromIndex, int toIndex) {
        return ListMap.listMap(super.subList(fromIndex, toIndex));
    }

    @Override
    public ListMap<K, V> plus(List<? extends Tuple2<K, V>> addition) {
        return ListMap.listMap(super.plus(addition));
    }

    @Override
    public ListMap<K, V> plus(Collection<? extends Tuple2<K, V>> elements) {
        return ListMap.listMap(super.plus(elements));
    }

    @Override
    public ListMap<K, V> plus(Iterator<? extends Tuple2<K, V>> iterator) {
        return ListMap.listMap(super.plus(iterator));
    }

    @Override
    public ListMap<K, V> plus(Tuple2<K, V>... additions) {
        return ListMap.listMap(super.plus(additions));
    }

    @Override
    public ListMap<K, V> take(int count) {
        return ListMap.listMap(super.take(count));
    }

    @Override
    public ListMap<K, V> drop(int count) {
        return ListMap.listMap(super.drop(count));
    }

    @Override
    protected ListMap<K, V> emptyList() {
        return ListMap.listMap();
    }

    @Override
    public ListMap<K, V> reverse() {
        return ListMap.listMap(super.reverse());
    }

    @Override
    public ListMap<K, V> sort(Function2I<? super Tuple2<K, V>, ? super Tuple2<K, V>> comparator) {
        return ListMap.listMap(super.sort(comparator));
    }

    @Override
    public ListMap<K, V> sortBy(Function<? super Tuple2<K, V>, ?> f) {
        return ListMap.listMap(super.sortBy(f));
    }

    @Override
    public ListMap<K, V> sortByDesc(Function<? super Tuple2<K, V>, ?> f) {
        return ListMap.listMap(super.sortByDesc(f));
    }

    public <U> ListMap<U, V> mapKeys(final Function<K, U> mapper) {
        Function<Tuple2<K, V>, Tuple2<U, V>> m2 = new Function<Tuple2<K, V>, Tuple2<U, V>>() {
            public Tuple2<U, V> apply(Tuple2<K, V> a) {
                return Tuple2.tuple(mapper.apply(a.get1()), a.get2());
            }
        };

        ListMap<U, V> r = new ListMap<U, V>();
        this.iterator().map(m2).forEach(r.addOp());
        return r;
    }

    public <U> ListMap<K, U> mapValues(final Function<V, U> mapper) {
        Function<Tuple2<K, V>, Tuple2<K, U>> m2 = new Function<Tuple2<K, V>, Tuple2<K, U>>() {
            public Tuple2<K, U> apply(Tuple2<K, V> a) {
                return Tuple2.tuple(a.get1(), mapper.apply(a.get2()));
            }
        };

        ListMap<K, U> r = new ListMap<K, U>();
        this.iterator().map(m2).forEach(r.addOp());
        return r;
    }

    public ListMap<K, V> filterValues(Function1B<? super V> p) {
        return new ListMap<K, V>(filter(valueM().andThen(p)));
    }

    public ListMap<K, V> filterKeys(Function1B<? super K> p) {
        return new ListMap<K, V>(filter(keyM().andThen(p)));
    }

    public Option<Tuple2<K, V>> findByKey(Function1B<? super K> p) {
        return find(keyM().andThen(p));
    }

    public void put(K k, V v) {
        add(Tuple2.tuple(k, v));
    }

    public Function2V<K, V> putOp() {
        return new Function2V<K, V>() {
            public void apply(K a, V b) {
                put(a, b);
            }
        };
    }

    public void put(Tuple2<? extends K, ? extends V> tuple) {
        super.add(tuple.<K, V>uncheckedCast());
    }

    @Override
    public ListMap<K, V> plus1(Tuple2<K, V> e) {
        return new ListMap<K, V>(Cf.x(target).plus1(e));
    }

    public ListMap<K, V> plus1(K key, V value) {
        return plus1(Tuple2.tuple(key, value));
    }

    private Function<Tuple2<K, V>, K> keyM() {
        return Tuple2.get1M();
    }

    private Function<Tuple2<K, V>, V> valueM() {
        return Tuple2.get2M();
    }

    public ListF<K> keys() {
        return map(keyM());
    }

    public ListF<V> values() {
        return map(valueM());
    }

    public ListMap<V, K> invert() {
        return listMap(map(Tuple2.<K, V>swapM()));
    }

    /**
     * @see CollectionF#sort()
     */
    public ListMap<K, V> sortByKey() {
        return sortByKey(Comparator.naturalComparator().<K, K>uncheckedCast());
    }

    /**
     * @see CollectionF#sort(Function2I)
     */
    @SuppressWarnings("unchecked")
    public ListMap<K, V> sortByKey(Function2I<? super K, ? super K> comparator) {
        if (size() <= 1) return this;
        return new ListMap<K, V>(sort(keyM().andThen((Function2I<K, K>) comparator)));
    }

    // XXX: sortByKeyBy, sortByKeyByDesc

    public MapF<K, V> toMap() {
        if (isEmpty()) return Cf.map();
        else return Cf.hashMap(this);
    }

    @SuppressWarnings({"unchecked", "ToArrayCallWithZeroLengthArrayArgument"})
    @Override
    public Tuple2<K, V>[] toArray() {
        return toArray(new Tuple2[0]);
    }

    public ListMap<K, V> unmodifiable() {
        return new ListMap<K, V>(Cf.x(target).unmodifiable());
    }

    /**
     * @see #uncheckedCast()
     */
    @SuppressWarnings("unchecked")
    public <F, G> ListMap<F, G> uncheckedCastLm() {
        return (ListMap<F, G>) this;
    }

    public <W> ListF<W> mapEntries(Function2<? super K, ? super V, ? extends W> mapper) {
        return map(mapper.<K, V, W>uncheckedCast().asFunction());
    }

    public void forEach(Function2V<K, V> f) {
        super.forEach(f.asFunction1V());
    }

    public boolean forAll(Function2B<K, V> f) {
        return super.forAll(f.asFunction1B());
    }

    public String mkString(String elemSep, final String tupleSep) {
        return mapEntries(new Function2<K, V, String>() {
            public String apply(K a, V b) {
                return a + tupleSep + b;
            }
        }).mkString(elemSep);
    }

    public ListMap<K, V> plus(ListMap<K, V> that) {
        if (that.isEmpty()) return this;
        else if (this.isEmpty()) return that;
        else return new ListMap<K, V>(super.plus(that.target));
    }

    /**
     * Unchecked.
     */
    @SuppressWarnings("unchecked")
    public static <A, B> ListMap<A, B> listMapFromPairs(Object... elements) {
        if (elements.length % 2 != 0)
            throw new IllegalArgumentException();
        if (elements.length == 0)
            return listMap();
        Tuple2<A, B>[] es = new Tuple2[elements.length / 2];
        for (int i = 0; i < es.length; ++i) {
            es[i] = Tuple2.tuple((A) elements[i * 2], (B) elements[i * 2 + 1]);
        }
        return listMap(es);
    }

    public static <A, B> ListMap<A, B> listMapFromKeysValues(ListF<A> keys, ListF<B> values) {
        ListMap<A, B> r = ListMap.arrayList();

        IteratorF<A> ki = keys.iterator();
        IteratorF<B> vi = values.iterator();
        while (ki.hasNext() && vi.hasNext()) {
            r.put(ki.next(), vi.next());
        }

        return r;
    }

    /**
     * Empty immutable.
     */
    public static <A, B> ListMap<A, B> listMap() {
        return listMap(Cf.<Tuple2<A, B>>list());
    }

    public static <A, B> ListMap<A, B> arrayList() {
        return listMap(Cf.<Tuple2<A, B>>arrayList());
    }

    public static <A, B> ListMap<A, B> listMap(Tuple2<A, B>... pairs) {
        return listMap(Cf.list(pairs));
    }

    public static <A, B> ListMap<A, B> listMap(ListF<Tuple2<A, B>> pairs) {
        return new ListMap<A, B>(pairs);
    }

} //~
