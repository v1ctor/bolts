package ru.yandex.bolts.type.collection;

import java.util.Collection;

import ru.yandex.bolts.collection.Tuple3;
import ru.yandex.bolts.collection.Tuple3List;

/**
 * @author Stepan Koltsov
 */
public class Tuple3ListType {

    public <A, B, C> Tuple3List<A, B, C> cons(Collection<Tuple3<A, B, C>> pairs) {
        return Tuple3List.tuple3List(pairs);
    }

} //~
