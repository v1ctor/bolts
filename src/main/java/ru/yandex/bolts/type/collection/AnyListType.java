package ru.yandex.bolts.type.collection;

import java.util.Collection;
import java.util.List;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;

/**
 * @author Stepan Koltsov
 * @see ListType
 * @see CollectionsF#List
 */
public abstract class AnyListType extends AnyCollectionType {

    public abstract <A> ListF<A> cons();
    public abstract <A> ListF<A> cons(A a1);
    public abstract <A> ListF<A> cons(A a1, A a2);
    public abstract <A> ListF<A> cons(Collection<A> collection);
    public abstract <A> ListF<A> cons(A... elements);

    public <A> Function0<ListF<A>> consF() {
        return new Function0<ListF<A>>() {
            public ListF<A> apply() {
                return cons();
            }
        };
    }

    public <A> Function<A[], ListF<A>> consFromArrayF() {
        return new Function<A[], ListF<A>>() {
            public ListF<A> apply(A[] a) {
                return cons(a);
            }
        };
    }

    public <A> Function<A, ListF<A>> consFrom1F() {
        return new Function<A, ListF<A>>() {
            public ListF<A> apply(A a) {
                return cons(a);
            }
        };
    }

    public <A> ListF<A> plus(ListF<A> l1, ListF<A> l2) {
        return l1.plus(l2);
    }

    public <A> Function2<ListF<A>, ListF<A>, ListF<A>> plusF() {
        return new Function2<ListF<A>, ListF<A>, ListF<A>>() {
            public ListF<A> apply(ListF<A> l1, ListF<A> l2) {
                return plus(l1, l2);
            }
        };
    }

    public <A> Function2<List<A>, Function1B<? super A>, ListF<A>> filterF() {
        return new Function2<List<A>, Function1B<? super A>, ListF<A>>() {
            public ListF<A> apply(List<A> set, Function1B<? super A> f) {
                return Cf.x(set).filter(f);
            }
        };
    }

    public <A> Function<List<A>, ListF<A>> filterF(Function1B<? super A> f) {
        return this.<A>filterF().bind2(f);
    }

    public <A> Function2<ListF<A>, Integer, A> getF() {
        return new Function2<ListF<A>, Integer, A>() {
            public A apply(ListF<A> list, Integer index) {
                return list.get(index);
            }
        };
    }

    public <A> Function<ListF<A>, A> getF(final int index) {
        return this.<A>getF().bind2(index);
    }

    public <A> Function<ListF<A>, A> firstF() {
        return getF(0);
    }


} //~
