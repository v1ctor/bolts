package ru.yandex.bolts.type.collection;

import java.util.Collection;
import java.util.List;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;


public abstract class AnyListType extends AnyCollectionType {

    public abstract <A> ListF<A> cons();
    public abstract <A> ListF<A> cons(A a1);
    public abstract <A> ListF<A> cons(A a1, A a2);
    public abstract <A> ListF<A> cons(Collection<A> collection);
    @SuppressWarnings("unchecked")
    public abstract <A> ListF<A> cons(A... elements);

    public <A> Function0<ListF<A>> consF() {
        return this::cons;
    }

    public <A> Function<A[], ListF<A>> consFromArrayF() {
        return a -> cons(a);
    }

    public <A> Function<A, ListF<A>> consFrom1F() {
        return this::cons;
    }

    public <A> ListF<A> plus(ListF<A> l1, ListF<A> l2) {
        return l1.plus(l2);
    }

    public <A> Function2<ListF<A>, ListF<A>, ListF<A>> plusF() {
        return this::plus;
    }

    public <A> ListF<A> concat(ListF<ListF<A>> lists) {
        Function2<Integer, Collection<?>, Integer> c = Cf.Integer.plusF().compose2(Cf.List.sizeF());
        int totalLength = lists.foldLeft(0, c);
        ListF<A> r = Cf.arrayList(totalLength);
        for (ListF<A> l : lists) {
            r.addAll(l);
        }
        return r.makeReadOnly();
    }

    public <A> Function2<List<A>, Function1B<? super A>, ListF<A>> filterF() {
        return (set, f) -> Cf.x(set).filter(f);
    }

    public <A> Function<List<A>, ListF<A>> filterF(Function1B<? super A> f) {
        return this.<A>filterF().bind2(f);
    }

    public <A> Function2<List<A>, Integer, A> getF() {
        return List::get;
    }

    public <A> Function<List<A>, A> getF(final int index) {
        return this.<A>getF().bind2(index);
    }

    public <A> Function<List<A>, A> firstF() {
        return getF(0);
    }

    public <A> Function<List<A>, Option<A>> firstOF() {
        return a -> Cf.x(a).firstO();
    }

} //~
