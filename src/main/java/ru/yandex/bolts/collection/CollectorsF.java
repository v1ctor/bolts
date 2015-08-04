package ru.yandex.bolts.collection;

import java.util.stream.Collector;
import java.util.stream.Collectors;


public class CollectorsF {

    public static <T> Collector<T, ?, ListF<T>> toList() {
        return Collectors.collectingAndThen(
                Collectors.toCollection(Cf::arrayList),
                ListF::makeReadOnly);
    }

}
