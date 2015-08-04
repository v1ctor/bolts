package ru.yandex.bolts.collection.impl;

import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2I;
import ru.yandex.bolts.function.forhuman.Comparator;


public interface TraversableF<E> {


    boolean forAll(Function1B<? super E> p);


    boolean exists(Function1B<? super E> p);


    Option<E> find(Function1B<? super E> p);


    int count(Function1B<? super E> p);


    Option<E> singleO();

    E single();


    <B> B foldLeft(B z, Function2<? super B, ? super E, B> f);


    E reduceLeft(Function2<E, E, E> f);

    Option<E> reduceLeftO(Function2<E, E, E> f);



    E min();


    E min(Function2I<? super E, ? super E> comparator);

    E minBy(Function<? super E, ?> f);

    Option<E> minO();

    Option<E> minO(Function2I<? super E, ? super E> comparator);

    Option<E> minByO(Function<? super E, ?> f);


    E max();


    E max(Function2I<? super E, ? super E> comparator);

    E maxBy(Function<? super E, ?> f);

    Option<E> maxO();

    Option<E> maxO(Function2I<? super E, ? super E> comparator);

    Option<E> maxByO(Function<? super E, ?> f);


    String mkString(String sep);


    String mkString(String start, String sep, String end);


} //~
