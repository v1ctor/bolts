package ru.yandex.bolts.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ru.yandex.bolts.collection.impl.DefaultListF;
import ru.yandex.bolts.collection.impl.ReadOnlyArrayList;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2B;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.Function2V;
import ru.yandex.bolts.function.forhuman.Comparator;


@SuppressWarnings("serial")
public class Tuple2List<A, B> extends DefaultListF<Tuple2<A, B>> {

    private Tuple2List(List<Tuple2<A, B>> list) {
        super(list);
    }

    public Tuple2List(Collection<Tuple2<A, B>> elements) {
        super(new ArrayList<>(elements));
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

    public Tuple2List<A, B> filter(Function2B<? super A, ? super B> p) {
        return filter(p.<A, B>uncheckedCast().asFunction1B().uncheckedCast());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Tuple2<Tuple2List<A, B>, Tuple2List<A, B>> partitionT2l(Function1B<? super Tuple2<A, B>> p) {
        return (Tuple2) super.partition(p);
    }

    public Tuple2<Tuple2List<A, B>, Tuple2List<A, B>> partition(Function2B<? super A, ? super B> p) {
        return partitionT2l(p.<A, B>uncheckedCast().asFunction1B());
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

    @SuppressWarnings("unchecked")
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
    public Tuple2List<A, B> sorted() {
        return sorted(get1F().andThenNaturalComparator().chainTo(get2F().andThenNaturalComparator()));
    }

    @Override
    public Tuple2List<A, B> sorted(java.util.Comparator<? super Tuple2<A, B>> comparator) {
        return Tuple2List.tuple2List(super.sorted(comparator));
    }

    @Override
    public Tuple2List<A, B> sortedBy(Function<? super Tuple2<A, B>, ?> f) {
        return Tuple2List.tuple2List(super.sortedBy(f));
    }

    @Override
    public Tuple2List<A, B> sortedByDesc(Function<? super Tuple2<A, B>, ?> f) {
        return Tuple2List.tuple2List(super.sortedByDesc(f));
    }

    @Override
    public Tuple2List<A, B> takeSorted(int k) {
        return takeSorted(get1F().andThenNaturalComparator().chainTo(get2F().andThenNaturalComparator()), k);
    }

    @Override
    public Tuple2List<A, B> takeSortedDesc(int k) {
        return takeSorted(get1F().andThenNaturalComparator().chainTo(get2F().andThenNaturalComparator()).invert(), k);
    }

    @Override
    public Tuple2List<A, B> takeSorted(java.util.Comparator<? super Tuple2<A, B>> comparator, int k) {
        return Tuple2List.tuple2List(super.takeSorted(comparator, k));
    }

    @Override
    public Tuple2List<A, B> takeSortedBy(Function<? super Tuple2<A, B>, ?> f, int k) {
        return Tuple2List.tuple2List(super.takeSortedBy(f, k));
    }

    @Override
    public Tuple2List<A, B> takeSortedByDesc(Function<? super Tuple2<A, B>, ?> f, int k) {
        return Tuple2List.tuple2List(super.takeSortedByDesc(f, k));
    }

    public <U> Tuple2List<U, B> map1(final Function<? super A, U> f) {
        return Tuple2List.tuple2List(map(Tuple2.map1F(f.uncheckedCast())));
    }

    public <U> Tuple2List<A, U> map2(final Function<? super B, U> f) {
        return Tuple2List.tuple2List(map(Tuple2.map2F(f.uncheckedCast())));
    }

    public Tuple2List<A, B> filterBy1(Function1B<? super A> p) {
        return new Tuple2List<>(filter(get1F().andThen(p)));
    }

    public Tuple2List<A, B> filterBy2(Function1B<? super B> p) {
        return new Tuple2List<>(filter(get2F().andThen(p)));
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
        return this::add;
    }

    public void add(Tuple2<? extends A, ? extends B> tuple) {
        super.add(tuple.uncheckedCast());
    }

    @Override
    public Tuple2List<A, B> plus1(Tuple2<A, B> e) {
        return new Tuple2List<>(Cf.x(target).plus1(e));
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
        return tuple2List(map(Tuple2::swap));
    }


    public Tuple2List<A, B> sortedBy1() {
        return sortedBy1(Comparator.naturalComparator().uncheckedCast());
    }


    @SuppressWarnings("unchecked")
    public Tuple2List<A, B> sortedBy1(Function2I<? super A, ? super A> comparator) {
        if (size() <= 1) return this;
        return new Tuple2List<>(sorted(get1F().andThen((Function2I<A, A>) comparator)));
    }


    public Tuple2List<A, B> sortedBy2() {
        return sortedBy2(Comparator.naturalComparator().uncheckedCast());
    }


    @SuppressWarnings("unchecked")
    public Tuple2List<A, B> sortedBy2(Function2I<? super B, ? super B> comparator) {
        if (size() <= 1) return this;
        return new Tuple2List<>(sorted(get2F().andThen((Function2I<B, B>) comparator)));
    }

    public Tuple2List<A, B> takeSortedBy1(int k) {
        return takeSortedBy1(Comparator.naturalComparator().uncheckedCast(), k);
    }

    public Tuple2List<A, B> takeSortedBy1Desc(int k) {
        return takeSortedBy1(Comparator.naturalComparator().invert().uncheckedCast(), k);
    }

    public Tuple2List<A, B> takeSortedBy1(Function2I<? super A, ? super A> comparator, int k) {
        return takeSorted(get1F().andThen((Function2I<A, A>) comparator), k);
    }

    public Tuple2List<A, B> takeSortedBy2(int k) {
        return takeSortedBy2(Comparator.naturalComparator().uncheckedCast(), k);
    }

    public Tuple2List<A, B> takeSortedBy2Desc(int k) {
        return takeSortedBy2(Comparator.naturalComparator().invert().uncheckedCast(), k);
    }

    public Tuple2List<A, B> takeSortedBy2(Function2I<? super B, ? super B> comparator, int k) {
        return takeSorted(get2F().andThen((Function2I<B, B>) comparator), k);
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
            return groupBy(get1F()).mapValues(list -> list.map(get2F()));
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

    @Override
    public Tuple2List<A, B> makeReadOnly() {
        if (target instanceof ReadOnlyArrayList<?>) {
            return this;
        } else {
            return new Tuple2List<>(super.makeReadOnly());
        }
    }


    @SuppressWarnings("unchecked")
    public <F, G> Tuple2List<F, G> uncheckedCastT2l() {
        return (Tuple2List<F, G>) this;
    }

    public <W> Tuple3List<A, B, W> zip3(ListF<W> list) {
        return Tuple3List.tuple3List(zip(list).map((a, b) -> Tuple3.tuple(a._1, a._2, b)));
    }

    public <W> Tuple3List<A, B, W> zip3With(Function2<A, B, W> f) {
        return zip3(map(f));
    }

    public <W> Tuple3List<A, B, W> zip3With(Function<Tuple2<A, B>, W> f) {
        return zip3(map(f));
    }

    public <W> ListF<W> map(Function2<? super A, ? super B, ? extends W> mapper) {
        return map(mapper.<A, B, W>uncheckedCast().asFunction());
    }

    public <C, D> Tuple2List<C, D> toTuple2List(Function2<? super A, ? super B, Tuple2<C, D>> f) {
        return super.toTuple2List(f.<A, B, Tuple2<C, D>>uncheckedCast().asFunction());
    }

    public void forEach(Function2V<A, B> f) {
        super.forEach(f.asFunction1V());
    }

    public boolean forAll(Function2B<A, B> f) {
        return super.forAll(f.asFunction1B());
    }

    public String mkString(String elemSep, final String tupleSep) {
        return map((a, b) -> a + tupleSep + b).mkString(elemSep);
    }

    public Tuple2List<A, B> plus(Tuple2List<A, B> that) {
        if (that.isEmpty()) return this;
        else if (this.isEmpty()) return that.uncheckedCastT2l();
        else return new Tuple2List<>(super.plus(that.<A, B>uncheckedCastT2l().target));
    }


    public static <A, B> Tuple2List<A, B> fromPairs(Object... elements) {
        return Cf.Tuple2List.fromPairs(elements);
    }

    public static <A, B> Tuple2List<A, B> fromPairs(A a, B b) {
        return Cf.Tuple2List.fromPairs(a, b);
    }

    public static <A, B> Tuple2List<A, B> fromPairs(A a1, B b1, A a2, B b2) {
        return Cf.Tuple2List.fromPairs(a1, b1, a2, b2);
    }


    public static <A, B> Tuple2List<A, B> zip(ListF<? extends A> list1, ListF<? extends B> list2) {
        Tuple2List<A, B> r = Tuple2List.arrayList();

        IteratorF<? extends A> ki = list1.iterator();
        IteratorF<? extends B> vi = list2.iterator();
        while (ki.hasNext() && vi.hasNext()) {
            r.add(ki.next(), vi.next());
        }

        return r;
    }


    public static <A, B> Tuple2List<A, B> tuple2List() {
        return Cf.Tuple2List.cons();
    }

    public static <A, B> Tuple2List<A, B> arrayList() {
        return Cf.Tuple2List.arrayList();
    }

    @SuppressWarnings("unchecked")
    public static <A, B> Tuple2List<A, B> tuple2List(Tuple2<A, B>... pairs) {
        return Cf.Tuple2List.cons(pairs);
    }

    public static <A, B> Tuple2List<A, B> wrap(ListF<Tuple2<A, B>> pairs) {
        return new Tuple2List<>(pairs);
    }

    public static <A, B> Tuple2List<A, B> tuple2List(ListF<Tuple2<A, B>> pairs) {
        return Cf.Tuple2List.cons(pairs);
    }

    public static <A, B> Tuple2List<A, B> tuple2List(Collection<Tuple2<A, B>> pairs) {
        return new Tuple2List<>(pairs);
    }

} //~
