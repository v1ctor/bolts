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
public class Tuple2List<A, B> extends DefaultListF<Tuple2<A, B>> {

    private Tuple2List(List<Tuple2<A, B>> list) {
        super(list);
    }

    public Tuple2List(Collection<Tuple2<A, B>> elements) {
        super(new ArrayList<Tuple2<A, B>>(elements));
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected Tuple2List<A, B> newMutableCollection() {
        return Tuple2List.arrayList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple2List<A, B> filter(Function1B<? super Tuple2<A, B>> p) {
        return (Tuple2List<A, B>) super.filter(p);
    }

    @SuppressWarnings("unchecked")
    public Tuple2<Tuple2List<A, B>, Tuple2List<A, B>> partitionT2l(Function1B<? super Tuple2<A, B>> p) {
        return (Tuple2) super.partition(p);
    }

    public Tuple2<Tuple2List<A, B>, Tuple2List<A, B>> partitionBy1(Function1B<? super A> p) {
        return partitionT2l(Tuple2.<A, B>get1F().andThen(p));
    }

    public Tuple2<Tuple2List<A, B>, Tuple2List<A, B>> partitionBy2(Function1B<? super B> p) {
        return partitionT2l(Tuple2.<A, B>get2F().andThen(p));
    }

    @Override
    public Tuple2List<A, B> subList(int fromIndex, int toIndex) {
        return Tuple2List.tuple2List(super.subList(fromIndex, toIndex));
    }

    @Override
    public Tuple2List<A, B> plus(List<? extends Tuple2<A, B>> addition) {
        return Tuple2List.tuple2List(super.plus(addition));
    }

    @Override
    public Tuple2List<A, B> plus(Collection<? extends Tuple2<A, B>> elements) {
        return Tuple2List.tuple2List(super.plus(elements));
    }

    @Override
    public Tuple2List<A, B> plus(Iterator<? extends Tuple2<A, B>> iterator) {
        return Tuple2List.tuple2List(super.plus(iterator));
    }

    @Override
    public Tuple2List<A, B> plus(Tuple2<A, B>... additions) {
        return Tuple2List.tuple2List(super.plus(additions));
    }

    @Override
    public Tuple2List<A, B> take(int count) {
        return Tuple2List.tuple2List(super.take(count));
    }

    @Override
    public Tuple2List<A, B> drop(int count) {
        return Tuple2List.tuple2List(super.drop(count));
    }

    @Override
    protected Tuple2List<A, B> emptyList() {
        return Tuple2List.tuple2List();
    }

    @Override
    public Tuple2List<A, B> reverse() {
        return Tuple2List.tuple2List(super.reverse());
    }

    @Override
    public Tuple2List<A, B> sort() {
        return Tuple2List.tuple2List(super.sort());
    }

    @Override
    public Tuple2List<A, B> sort(java.util.Comparator<? super Tuple2<A, B>> comparator) {
        return Tuple2List.tuple2List(super.sort(comparator));
    }

    @Override
    public Tuple2List<A, B> sort(Comparator<? super Tuple2<A, B>> comparator) {
        return Tuple2List.tuple2List(super.sort(comparator));
    }

    @Override
    public Tuple2List<A, B> sort(Function2I<? super Tuple2<A, B>, ? super Tuple2<A, B>> comparator) {
        return Tuple2List.tuple2List(super.sort(comparator));
    }

    @Override
    public Tuple2List<A, B> sortBy(Function<? super Tuple2<A, B>, ?> f) {
        return Tuple2List.tuple2List(super.sortBy(f));
    }

    @Override
    public Tuple2List<A, B> sortByDesc(Function<? super Tuple2<A, B>, ?> f) {
        return Tuple2List.tuple2List(super.sortByDesc(f));
    }

    public <U> Tuple2List<U, B> map1(final Function<? super A, U> f) {
        return Tuple2List.tuple2List(map(Tuple2.<A, B, U>map1F(f.<A, U>uncheckedCast())));
    }

    public <U> Tuple2List<A, U> map2(final Function<? super B, U> f) {
        return Tuple2List.tuple2List(map(Tuple2.<A, B, U>map2F(f.<B, U>uncheckedCast())));
    }

    public Tuple2List<A, B> filterBy1(Function1B<? super A> p) {
        return new Tuple2List<A, B>(filter(get1F().andThen(p)));
    }

    public Tuple2List<A, B> filterBy2(Function1B<? super B> p) {
        return new Tuple2List<A, B>(filter(get2F().andThen(p)));
    }

    /**
     * @deprecated
     */
    public Tuple2List<A, B> filter2(Function1B<? super B> p) {
        return filterBy2(p);
    }

    /**
     * @deprecated
     */
    public Tuple2List<A, B> filter1(Function1B<? super A> p) {
        return filterBy1(p);
    }

    public Option<Tuple2<A, B>> findBy1(Function1B<? super A> p) {
        return find(get1F().andThen(p));
    }

    public Option<Tuple2<A, B>> findBy2(Function1B<? super B> p) {
        return find(get2F().andThen(p));
    }

    public void add(A k, B v) {
        add(Tuple2.tuple(k, v));
    }

    public Function2V<A, B> add2F() {
        return new Function2V<A, B>() {
            public void apply(A a, B b) {
                add(a, b);
            }
        };
    }

    public void add(Tuple2<? extends A, ? extends B> tuple) {
        super.add(tuple.<A, B>uncheckedCast());
    }

    @Override
    public Tuple2List<A, B> plus1(Tuple2<A, B> e) {
        return new Tuple2List<A, B>(Cf.x(target).plus1(e));
    }

    public Tuple2List<A, B> plus1(A key, B value) {
        return plus1(Tuple2.tuple(key, value));
    }

    private Function<Tuple2<A, B>, A> get1F() {
        return Tuple2.get1F();
    }

    private Function<Tuple2<A, B>, B> get2F() {
        return Tuple2.get2F();
    }

    public ListF<A> get1() {
        return map(get1F());
    }

    public ListF<B> get2() {
        return map(get2F());
    }

    public Tuple2List<B, A> invert() {
        return tuple2List(map(Tuple2.<A, B>swapF()));
    }

    /**
     * @see CollectionF#sort()
     */
    public Tuple2List<A, B> sortBy1() {
        return sortBy1(Comparator.naturalComparator().<A, A>uncheckedCast());
    }

    /**
     * @see CollectionF#sort(Function2I)
     */
    @SuppressWarnings("unchecked")
    public Tuple2List<A, B> sortBy1(Function2I<? super A, ? super A> comparator) {
        if (size() <= 1) return this;
        return new Tuple2List<A, B>(sort(get1F().andThen((Function2I<A, A>) comparator)));
    }

    /**
     * @see CollectionF#sort()
     */
    public Tuple2List<A, B> sortBy2() {
        return sortBy2(Comparator.naturalComparator().<B, B>uncheckedCast());
    }

    /**
     * @see CollectionF#sort(Function2I)
     */
    @SuppressWarnings("unchecked")
    public Tuple2List<A, B> sortBy2(Function2I<? super B, ? super B> comparator) {
        if (size() <= 1) return this;
        return new Tuple2List<A, B>(sort(get2F().andThen((Function2I<B, B>) comparator)));
    }


    // XXX: sortByKeyBy, sortByKeyByDesc

    public MapF<A, B> toMap() {
        if (isEmpty()) return Cf.map();
        else return Cf.hashMap(this);
    }

    public MapF<A, ListF<B>> groupBy1() {
        if (isEmpty()) {
            return Cf.map();
        } else {
            return groupBy(get1F()).mapValues(new Function<ListF<Tuple2<A, B>>, ListF<B>>() {
                public ListF<B> apply(ListF<Tuple2<A, B>> list) {
                    return list.map(get2F());
                }
            });
        }
    }

    public MapF<B, ListF<A>> groupBy2() {
        return invert().groupBy1();
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Tuple2<A, B>[] toArray() {
        return toArray(new Tuple2[0]);
    }

    public Tuple2List<A, B> unmodifiable() {
        return new Tuple2List<A, B>(Cf.x(target).unmodifiable());
    }

    /**
     * @see #uncheckedCast()
     */
    @SuppressWarnings("unchecked")
    public <F, G> Tuple2List<F, G> uncheckedCastT2l() {
        return (Tuple2List<F, G>) this;
    }

    public <W> Tuple3List<A, B, W> zip3(ListF<W> list) {
        return Tuple3List.tuple3List(zip(list).map(new Function2<Tuple2<A, B>, W, Tuple3<A, B, W>>() {
            public Tuple3<A, B, W> apply(Tuple2<A, B> a, W b) {
                return Tuple3.tuple(a._1, a._2, b);
            }
        }));
    }

    public <W> Tuple3List<A, B, W> zip3With(Function2<A, B, W> f) {
        return zip3(map(f));
    }

    public <W> ListF<W> map(Function2<? super A, ? super B, ? extends W> mapper) {
        return map(mapper.<A, B, W>uncheckedCast().asFunction());
    }

    public void forEach(Function2V<A, B> f) {
        super.forEach(f.asFunction1V());
    }

    public boolean forAll(Function2B<A, B> f) {
        return super.forAll(f.asFunction1B());
    }

    public String mkString(String elemSep, final String tupleSep) {
        return map(new Function2<A, B, String>() {
            public String apply(A a, B b) {
                return a + tupleSep + b;
            }
        }).mkString(elemSep);
    }

    public Tuple2List<A, B> plus(Tuple2List<A, B> that) {
        if (that.isEmpty()) return this;
        else if (this.isEmpty()) return that;
        else return new Tuple2List<A, B>(super.plus(that.target));
    }

    /**
     * Unchecked.
     */
    public static <A, B> Tuple2List<A, B> fromPairs(Object... elements) {
        return Cf.Tuple2List.fromPairs(elements);
    }

    public static <A, B> Tuple2List<A, B> fromPairs(A a, B b) {
        return Cf.Tuple2List.fromPairs(a, b);
    }

    public static <A, B> Tuple2List<A, B> fromPairs(A a1, B b1, A a2, B b2) {
        return Cf.Tuple2List.fromPairs(a1, b1, a2, b2);
    }

    /**
     * @see ListF#zip(ListF)
     */
    public static <A, B> Tuple2List<A, B> zip(ListF<? extends A> list1, ListF<? extends B> list2) {
        Tuple2List<A, B> r = Tuple2List.arrayList();

        IteratorF<? extends A> ki = list1.iterator();
        IteratorF<? extends B> vi = list2.iterator();
        while (ki.hasNext() && vi.hasNext()) {
            r.add(ki.next(), vi.next());
        }

        return r;
    }

    /**
     * Empty immutable.
     */
    public static <A, B> Tuple2List<A, B> tuple2List() {
        return Cf.Tuple2List.cons();
    }

    public static <A, B> Tuple2List<A, B> arrayList() {
        return Cf.Tuple2List.arrayList();
    }

    public static <A, B> Tuple2List<A, B> tuple2List(Tuple2<A, B>... pairs) {
        return Cf.Tuple2List.cons(pairs);
    }

    public static <A, B> Tuple2List<A, B> wrap(ListF<Tuple2<A, B>> pairs) {
        return new Tuple2List<A, B>(pairs);
    }

    public static <A, B> Tuple2List<A, B> tuple2List(ListF<Tuple2<A, B>> pairs) {
        return Cf.Tuple2List.cons(pairs);
    }

    public static <A, B> Tuple2List<A, B> tuple2List(Collection<Tuple2<A, B>> pairs) {
        return new Tuple2List<A, B>(pairs);
    }

} //~
