package ru.yandex.bolts.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ru.yandex.bolts.collection.impl.DefaultListF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2B;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.Function2V;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings("serial")
public class Tuple2List<K, V> extends DefaultListF<Tuple2<K, V>> {

    private Tuple2List(List<Tuple2<K, V>> list) {
        super(list);
    }

    public Tuple2List(Collection<Tuple2<K, V>> elements) {
        super(new ArrayList<Tuple2<K, V>>(elements));
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected Tuple2List<K, V> newMutableCollection() {
        return Tuple2List.arrayList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple2List<K, V> filter(Function1B<? super Tuple2<K, V>> p) {
        return (Tuple2List<K, V>) super.filter(p);
    }

    @SuppressWarnings("unchecked")
    public Tuple2<Tuple2List<K, V>, Tuple2List<K, V>> partitionT2l(Function1B<? super Tuple2<K, V>> p) {
        return (Tuple2) super.partition(p);
    }

    public Tuple2<Tuple2List<K, V>, Tuple2List<K, V>> partitionBy1(Function1B<? super K> p) {
        return partitionT2l(Tuple2.<K, V>get1F().andThen(p));
    }

    public Tuple2<Tuple2List<K, V>, Tuple2List<K, V>> partitionBy2(Function1B<? super V> p) {
        return partitionT2l(Tuple2.<K, V>get2F().andThen(p));
    }

    @Override
    public Tuple2List<K, V> subList(int fromIndex, int toIndex) {
        return Tuple2List.tuple2List(super.subList(fromIndex, toIndex));
    }

    @Override
    public Tuple2List<K, V> plus(List<? extends Tuple2<K, V>> addition) {
        return Tuple2List.tuple2List(super.plus(addition));
    }

    @Override
    public Tuple2List<K, V> plus(Collection<? extends Tuple2<K, V>> elements) {
        return Tuple2List.tuple2List(super.plus(elements));
    }

    @Override
    public Tuple2List<K, V> plus(Iterator<? extends Tuple2<K, V>> iterator) {
        return Tuple2List.tuple2List(super.plus(iterator));
    }

    @Override
    public Tuple2List<K, V> plus(Tuple2<K, V>... additions) {
        return Tuple2List.tuple2List(super.plus(additions));
    }

    @Override
    public Tuple2List<K, V> take(int count) {
        return Tuple2List.tuple2List(super.take(count));
    }

    @Override
    public Tuple2List<K, V> drop(int count) {
        return Tuple2List.tuple2List(super.drop(count));
    }

    @Override
    protected Tuple2List<K, V> emptyList() {
        return Tuple2List.tuple2List();
    }

    @Override
    public Tuple2List<K, V> reverse() {
        return Tuple2List.tuple2List(super.reverse());
    }

    @Override
    public Tuple2List<K, V> sort() {
        return Tuple2List.tuple2List(super.sort());
    }

    @Override
    public Tuple2List<K, V> sort(java.util.Comparator<? super Tuple2<K, V>> comparator) {
        return Tuple2List.tuple2List(super.sort(comparator));
    }

    @Override
    public Tuple2List<K, V> sort(Comparator<? super Tuple2<K, V>> comparator) {
        return Tuple2List.tuple2List(super.sort(comparator));
    }

    @Override
    public Tuple2List<K, V> sort(Function2I<? super Tuple2<K, V>, ? super Tuple2<K, V>> comparator) {
        return Tuple2List.tuple2List(super.sort(comparator));
    }

    @Override
    public Tuple2List<K, V> sortBy(Function<? super Tuple2<K, V>, ?> f) {
        return Tuple2List.tuple2List(super.sortBy(f));
    }

    @Override
    public Tuple2List<K, V> sortByDesc(Function<? super Tuple2<K, V>, ?> f) {
        return Tuple2List.tuple2List(super.sortByDesc(f));
    }

    public <U> Tuple2List<U, V> map1(final Function<K, U> mapper) {
        Function<Tuple2<K, V>, Tuple2<U, V>> m2 = new Function<Tuple2<K, V>, Tuple2<U, V>>() {
            public Tuple2<U, V> apply(Tuple2<K, V> a) {
                return Tuple2.tuple(mapper.apply(a.get1()), a.get2());
            }
        };

        Tuple2List<U, V> r = Tuple2List.arrayList();
        this.iterator().map(m2).forEach(r.addOp());
        return r;
    }

    public <U> Tuple2List<K, U> map2(final Function<V, U> mapper) {
        Function<Tuple2<K, V>, Tuple2<K, U>> m2 = new Function<Tuple2<K, V>, Tuple2<K, U>>() {
            public Tuple2<K, U> apply(Tuple2<K, V> a) {
                return Tuple2.tuple(a.get1(), mapper.apply(a.get2()));
            }
        };

        Tuple2List<K, U> r = Tuple2List.arrayList();
        this.iterator().map(m2).forEach(r.addOp());
        return r;
    }

    public Tuple2List<K, V> filter2(Function1B<? super V> p) {
        return new Tuple2List<K, V>(filter(get2F().andThen(p)));
    }

    public Tuple2List<K, V> filter1(Function1B<? super K> p) {
        return new Tuple2List<K, V>(filter(get1F().andThen(p)));
    }

    public Option<Tuple2<K, V>> findBy1(Function1B<? super K> p) {
        return find(get1F().andThen(p));
    }

    public void add(K k, V v) {
        add(Tuple2.tuple(k, v));
    }

    public Function2V<K, V> add2F() {
        return new Function2V<K, V>() {
            public void apply(K a, V b) {
                add(a, b);
            }
        };
    }

    public void add(Tuple2<? extends K, ? extends V> tuple) {
        super.add(tuple.<K, V>uncheckedCast());
    }

    @Override
    public Tuple2List<K, V> plus1(Tuple2<K, V> e) {
        return new Tuple2List<K, V>(Cf.x(target).plus1(e));
    }

    public Tuple2List<K, V> plus1(K key, V value) {
        return plus1(Tuple2.tuple(key, value));
    }

    private Function<Tuple2<K, V>, K> get1F() {
        return Tuple2.get1F();
    }

    private Function<Tuple2<K, V>, V> get2F() {
        return Tuple2.get2F();
    }

    public ListF<K> get1() {
        return map(get1F());
    }

    public ListF<V> get2() {
        return map(get2F());
    }

    public Tuple2List<V, K> invert() {
        return tuple2List(map(Tuple2.<K, V>swapF()));
    }

    /**
     * @see CollectionF#sort()
     */
    public Tuple2List<K, V> sortBy1() {
        return sortBy1(Comparator.naturalComparator().<K, K>uncheckedCast());
    }

    /**
     * @see CollectionF#sort(Function2I)
     */
    @SuppressWarnings("unchecked")
    public Tuple2List<K, V> sortBy1(Function2I<? super K, ? super K> comparator) {
        if (size() <= 1) return this;
        return new Tuple2List<K, V>(sort(get1F().andThen((Function2I<K, K>) comparator)));
    }

    // XXX: sortByKeyBy, sortByKeyByDesc

    public MapF<K, V> toMap() {
        if (isEmpty()) return Cf.map();
        else return Cf.hashMap(this);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Tuple2<K, V>[] toArray() {
        return toArray(new Tuple2[0]);
    }

    public Tuple2List<K, V> unmodifiable() {
        return new Tuple2List<K, V>(Cf.x(target).unmodifiable());
    }

    /**
     * @see #uncheckedCast()
     */
    @SuppressWarnings("unchecked")
    public <F, G> Tuple2List<F, G> uncheckedCastT2l() {
        return (Tuple2List<F, G>) this;
    }

    public <W> ListF<W> map(Function2<? super K, ? super V, ? extends W> mapper) {
        return map(mapper.<K, V, W>uncheckedCast().asFunction());
    }

    public void forEach(Function2V<K, V> f) {
        super.forEach(f.asFunction1V());
    }

    public boolean forAll(Function2B<K, V> f) {
        return super.forAll(f.asFunction1B());
    }

    public String mkString(String elemSep, final String tupleSep) {
        return map(new Function2<K, V, String>() {
            public String apply(K a, V b) {
                return a + tupleSep + b;
            }
        }).mkString(elemSep);
    }

    public Tuple2List<K, V> plus(Tuple2List<K, V> that) {
        if (that.isEmpty()) return this;
        else if (this.isEmpty()) return that;
        else return new Tuple2List<K, V>(super.plus(that.target));
    }

    /**
     * Unchecked.
     */
    @SuppressWarnings("unchecked")
    public static <A, B> Tuple2List<A, B> fromPairs(Object... elements) {
        if (elements.length % 2 != 0)
            throw new IllegalArgumentException();
        if (elements.length == 0)
            return tuple2List();
        Tuple2<A, B>[] es = new Tuple2[elements.length / 2];
        for (int i = 0; i < es.length; ++i) {
            es[i] = Tuple2.tuple((A) elements[i * 2], (B) elements[i * 2 + 1]);
        }
        return tuple2List(es);
    }

    /**
     * @see ListF#zip(ListF)
     */
    public static <A, B> Tuple2List<A, B> zip(ListF<A> list1, ListF<B> list2) {
        Tuple2List<A, B> r = Tuple2List.arrayList();

        IteratorF<A> ki = list1.iterator();
        IteratorF<B> vi = list2.iterator();
        while (ki.hasNext() && vi.hasNext()) {
            r.add(ki.next(), vi.next());
        }

        return r;
    }

    /**
     * Empty immutable.
     */
    public static <A, B> Tuple2List<A, B> tuple2List() {
        return tuple2List(Cf.<Tuple2<A, B>>list());
    }

    public static <A, B> Tuple2List<A, B> arrayList() {
        return tuple2List(Cf.<Tuple2<A, B>>arrayList());
    }

    public static <A, B> Tuple2List<A, B> tuple2List(Tuple2<A, B>... pairs) {
        return tuple2List(Cf.list(pairs));
    }

    public static <A, B> Tuple2List<A, B> tuple2List(ListF<Tuple2<A, B>> pairs) {
        return new Tuple2List<A, B>(pairs);
    }

} //~
