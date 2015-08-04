package ru.yandex.bolts.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ru.yandex.bolts.collection.impl.DefaultListF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.Function3;
import ru.yandex.bolts.function.Function3B;
import ru.yandex.bolts.function.Function3V;
import ru.yandex.bolts.function.forhuman.Comparator;

/**
 * @author Stepan Koltsov
 */
public class Tuple3List<A, B, C> extends DefaultListF<Tuple3<A,B,C>> {

    public Tuple3List(List<Tuple3<A, B, C>> target) {
        super(target);
    }

    public Tuple3List(Collection<Tuple3<A, B, C>> elements) {
        super(new ArrayList<>(elements));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Tuple3List<A, B, C> newMutableCollection() {
        return Tuple3List.arrayList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple3List<A, B, C> filter(Function1B<? super Tuple3<A, B, C>> p) {
        return (Tuple3List<A, B, C>) super.filter(p);
    }

    public Tuple3List<A, B, C> filter(Function3B<? super A, ? super B, ? super C> p) {
        return filter(p.<A, B, C>uncheckedCast().asFunction1B().uncheckedCast());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Tuple2<Tuple3List<A, B, C>, Tuple3List<A, B, C>> partitionT3l(Function1B<? super Tuple3<A, B, C>> p) {
        return (Tuple2) super.partition(p);
    }

    public Tuple2<Tuple3List<A, B, C>, Tuple3List<A, B, C>> partition(Function3B<? super A, ? super B, ? super C> p) {
        return partitionT3l(p.<A, B, C>uncheckedCast().asFunction1B());
    }

    public Tuple2<Tuple3List<A, B, C>, Tuple3List<A, B, C>> partitionBy1(Function1B<? super A> p) {
        return partitionT3l(Tuple3.<A, B, C>get1F().andThen(p));
    }

    public Tuple2<Tuple3List<A, B, C>, Tuple3List<A, B, C>> partitionBy2(Function1B<? super B> p) {
        return partitionT3l(Tuple3.<A, B, C>get2F().andThen(p));
    }

    public Tuple2<Tuple3List<A, B, C>, Tuple3List<A, B, C>> partitionBy3(Function1B<? super C> p) {
        return partitionT3l(Tuple3.<A, B, C>get3F().andThen(p));
    }

    @Override
    public Tuple3List<A, B, C> subList(int fromIndex, int toIndex) {
        return Tuple3List.tuple3List(super.subList(fromIndex, toIndex));
    }

    @Override
    public Tuple3List<A, B, C> plus(List<? extends Tuple3<A, B, C>> addition) {
        return Tuple3List.tuple3List(super.plus(addition));
    }

    @Override
    public Tuple3List<A, B, C> plus(Collection<? extends Tuple3<A, B, C>> elements) {
        return Tuple3List.tuple3List(super.plus(elements));
    }

    @Override
    public Tuple3List<A, B, C> plus(Iterator<? extends Tuple3<A, B, C>> iterator) {
        return Tuple3List.tuple3List(super.plus(iterator));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple3List<A, B, C> plus(Tuple3<A, B, C>... additions) {
        return Tuple3List.tuple3List(super.plus(additions));
    }

    @Override
    public Tuple3List<A, B, C> take(int count) {
        return Tuple3List.tuple3List(super.take(count));
    }

    @Override
    public Tuple3List<A, B, C> drop(int count) {
        return Tuple3List.tuple3List(super.drop(count));
    }

    @Override
    protected Tuple3List<A, B, C> emptyList() {
        return Tuple3List.tuple3List();
    }

    @Override
    public Tuple3List<A, B, C> reverse() {
        return Tuple3List.tuple3List(super.reverse());
    }

    @Override
    public Tuple3List<A, B, C> sorted() {
        Comparator<Tuple3<A, B, C>> comparator =
                get1F().andThenNaturalComparator().chainTo(
                get2F().andThenNaturalComparator()).chainTo(
                get3F().andThenNaturalComparator());
        return sorted(comparator);

    }

    @Override
    public Tuple3List<A, B, C> sorted(java.util.Comparator<? super Tuple3<A, B, C>> comparator) {
        return Tuple3List.tuple3List(super.sorted(comparator));
    }

    @Override
    public Tuple3List<A, B, C> sortedBy(Function<? super Tuple3<A, B, C>, ?> f) {
        return Tuple3List.tuple3List(super.sortedBy(f));
    }

    @Override
    public Tuple3List<A, B, C> sortedByDesc(Function<? super Tuple3<A, B, C>, ?> f) {
        return Tuple3List.tuple3List(super.sortedByDesc(f));
    }

    public <D> Tuple3List<D, B, C> map1(final Function<? super A, D> f) {
        return Tuple3List.tuple3List(map(Tuple3.map1F(f.uncheckedCast())));
    }

    public <D> Tuple3List<A, D, C> map2(final Function<? super B, D> f) {
        return Tuple3List.tuple3List(map(Tuple3.map2F(f.uncheckedCast())));
    }

    public <D> Tuple3List<A, B, D> map3(final Function<? super C, D> f) {
        return Tuple3List.tuple3List(map(Tuple3.map3F(f.uncheckedCast())));
    }

    public Tuple3List<A, B, C> filterBy1(Function1B<? super A> p) {
        return new Tuple3List<>(filter(get1F().andThen(p)));
    }

    public Tuple3List<A, B, C> filterBy2(Function1B<? super B> p) {
        return new Tuple3List<>(filter(get2F().andThen(p)));
    }

    public Tuple3List<A, B, C> filterBy3(Function1B<? super C> p) {
        return new Tuple3List<>(filter(get3F().andThen(p)));
    }

    public Option<Tuple3<A, B, C>> findBy1(Function1B<? super A> p) {
        return find(get1F().andThen(p));
    }

    public Option<Tuple3<A, B, C>> findBy2(Function1B<? super B> p) {
        return find(get2F().andThen(p));
    }

    public Option<Tuple3<A, B, C>> findBy3(Function1B<? super C> p) {
        return find(get3F().andThen(p));
    }

    public void add(A a, B b, C c) {
        add(Tuple3.tuple(a, b, c));
    }

    public Function3V<A, B, C> add2F() {
        return this::add;
    }

    public void add(Tuple3<? extends A, ? extends B, ? extends C> tuple) {
        super.add(tuple.<A, B, C>uncheckedCast());
    }

    @Override
    public Tuple3List<A, B, C> plus1(Tuple3<A, B, C> e) {
        return new Tuple3List<>(Cf.x(target).plus1(e));
    }

    public Tuple3List<A, B, C> plus1(A a, B b, C c) {
        return plus1(Tuple3.tuple(a, b, c));
    }

    private Function<Tuple3<A, B, C>, A> get1F() {
        return Tuple3.get1F();
    }

    private Function<Tuple3<A, B, C>, B> get2F() {
        return Tuple3.get2F();
    }

    private Function<Tuple3<A, B, C>, C> get3F() {
        return Tuple3.get3F();
    }

    private Function<Tuple3<A, B, C>, Tuple2<A, B>> get12F() {
        return Tuple3.get12F();
    }

    private Function<Tuple3<A, B, C>, Tuple2<B, C>> get23F() {
        return Tuple3.get23F();
    }

    private Function<Tuple3<A, B, C>, Tuple2<A, C>> get13F() {
        return Tuple3.get13F();
    }

    public ListF<A> get1() {
        return map(get1F());
    }

    public ListF<B> get2() {
        return map(get2F());
    }

    public ListF<C> get3() {
        return map(get3F());
    }

    public Tuple2List<A, B> get12() {
        return Tuple2List.tuple2List(map(get12F()));
    }

    public Tuple2List<B, C> get23() {
        return Tuple2List.tuple2List(map(get23F()));
    }

    public Tuple2List<A, C> get13() {
        return Tuple2List.tuple2List(map(get13F()));
    }



    /**
     * @see CollectionF#sorted()
     */
    public Tuple3List<A, B, C> sortedBy1() {
        return sortedBy1(Comparator.naturalComparator().uncheckedCast());
    }

    /**
     * @see CollectionF#sorted(java.util.Comparator)
     */
    @SuppressWarnings("unchecked")
    public Tuple3List<A, B, C> sortedBy1(Function2I<? super A, ? super A> comparator) {
        if (size() <= 1) return this;
        return new Tuple3List<>(sorted(get1F().andThen((Function2I<A, A>) comparator)));
    }

    /**
     * @see CollectionF#sorted()
     */
    public Tuple3List<A, B, C> sortedBy2() {
        return sortedBy2(Comparator.naturalComparator().uncheckedCast());
    }

    /**
     * @see CollectionF#sorted(java.util.Comparator)
     */
    @SuppressWarnings("unchecked")
    public Tuple3List<A, B, C> sortedBy2(Function2I<? super B, ? super B> comparator) {
        if (size() <= 1) return this;
        return new Tuple3List<>(sorted(get2F().andThen((Function2I<B, B>) comparator)));
    }

    /**
     * @see CollectionF#sorted()
     */
    public Tuple3List<A, B, C> sortedBy3() {
        return sortedBy3(Comparator.naturalComparator().uncheckedCast());
    }

    /**
     * @see CollectionF#sorted(java.util.Comparator)
     */
    @SuppressWarnings("unchecked")
    public Tuple3List<A, B, C> sortedBy3(Function2I<? super C, ? super C> comparator) {
        if (size() <= 1) return this;
        return new Tuple3List<>(sorted(get3F().andThen((Function2I<C, C>) comparator)));
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Tuple3<A, B, C>[] toArray() {
        return toArray(new Tuple3[0]);
    }

    public Tuple3List<A, B, C> unmodifiable() {
        return new Tuple3List<>(Cf.x(target).unmodifiable());
    }

    /**
     * @see #uncheckedCast()
     */
    @SuppressWarnings("unchecked")
    public <D, E, F> Tuple3List<D, E, F> uncheckedCastT3l() {
        return (Tuple3List<D, E, F>) this;
    }

    public <D> ListF<D> map(Function3<? super A, ? super B, ? super C, ? extends D> mapper) {
        return map(mapper.<A, B, C, D>uncheckedCast().asFunction());
    }

    public <D> Tuple2List<D, C> map12(final Function2<? super A, ? super B, ? extends D> f) {
        return Tuple2List.tuple2List(map((a, b, c) -> Tuple2.tuple(f.apply(a, b), c)));
    }

    public <D> Tuple2List<A, D> map23(final Function2<? super B, ? super C, ? extends D> f) {
        return Tuple2List.tuple2List(map((a, b, c) -> Tuple2.tuple(a, f.apply(b, c))));
    }

    public void forEach(Function3V<A, B, C> f) {
        super.forEach(f.asFunction1V());
    }

    public boolean forAll(Function3B<A, B, C> f) {
        return super.forAll(f.asFunction1B());
    }

    public Tuple3List<A, B, C> plus(Tuple3List<A, B, C> that) {
        if (that.isEmpty()) return this;
        else if (this.isEmpty()) return that;
        else return new Tuple3List<>(super.plus(that.target));
    }

    /**
     * Empty immutable.
     */
    public static <A, B, C> Tuple3List<A, B, C> tuple3List() {
        return tuple3List(Cf.<Tuple3<A, B, C>>list());
    }

    public static <A, B, C> Tuple3List<A, B, C> arrayList() {
        return tuple3List(Cf.<Tuple3<A, B, C>>arrayList());
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C> Tuple3List<A, B, C> tuple3List(Tuple3<A, B, C>... pairs) {
        return tuple3List(Cf.list(pairs));
    }

    public static <A, B, C> Tuple3List<A, B, C> tuple3List(ListF<Tuple3<A, B, C>> pairs) {
        return new Tuple3List<>(pairs);
    }

    public static <A, B, C> Tuple3List<A, B, C> tuple3List(Collection<Tuple3<A, B, C>> pairs) {
        return new Tuple3List<>(pairs);
    }

    public static <A, B, C> Tuple3List<A, B, C> wrap(ListF<Tuple3<A, B, C>> pairs) {
        return new Tuple3List<>(pairs);
    }

} //~
