package ru.yandex.bolts.type.collection;

import java.util.Collection;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.Tuple2List;

/**
 * @author Stepan Koltsov
 */
public class Tuple2ListType {

    private static final Tuple2List<Object, Object> EMPTY = Tuple2List.wrap(Cf.<Tuple2<Object, Object>>list());

    public <A, B> Tuple2List<A, B> cons() {
        return EMPTY.uncheckedCastT2l();
    }

    public <A, B> Tuple2List<A, B> arrayList() {
        return wrap(Cf.<Tuple2<A, B>>arrayList());
    }

    public <A, B> Tuple2List<A, B> cons(Tuple2<A, B>... pairs) {
        return cons(Cf.list(pairs));
    }

    public <A, B> Tuple2List<A, B> cons(Collection<Tuple2<A, B>> pairs) {
        return Tuple2List.tuple2List(pairs);
    }

    public static <A, B> Tuple2List<A, B> wrap(ListF<Tuple2<A, B>> pairs) {
        return Tuple2List.wrap(pairs);
    }

    /**
     * Unchecked.
     */
    @SuppressWarnings("unchecked")
    public <A, B> Tuple2List<A, B> fromPairs(Object... elements) {
        if (elements.length % 2 != 0)
            throw new IllegalArgumentException();
        if (elements.length == 0)
            return cons();
        Tuple2<A, B>[] es = new Tuple2[elements.length / 2];
        for (int i = 0; i < es.length; ++i) {
            es[i] = Tuple2.tuple((A) elements[i * 2], (B) elements[i * 2 + 1]);
        }
        return cons(es);
    }

    @SuppressWarnings("unchecked")
    public <A, B> Tuple2List<A, B> fromPairs(A a, B b) {
        return cons(Tuple2.tuple(a, b));
    }

    @SuppressWarnings("unchecked")
    public <A, B> Tuple2List<A, B> fromPairs(A a1, B b1, A a2, B b2) {
        return cons(Tuple2.tuple(a1, b1), Tuple2.tuple(a2, b2));
    }

} //~
