package ru.yandex.bolts.collection;

import java.util.Map;
import java.util.NoSuchElementException;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2B;
import ru.yandex.bolts.function.Function2V;


public interface MapF<K, V> extends Map<K, V> {
    boolean isNotEmpty();


    Option<V> getO(K key);


    V getOrElse(K key, V elseValue);


    V getOrThrow(K key, String message);


    V getOrThrow(K key, String message, Object param);


    V getOrElseUpdate(K key, V elseValue);


    V getOrElseUpdate(K key, Function0<V> value);


    V getOrElseUpdate(K key, Function<K, V> m);

    boolean containsEntry(K key, V value);

    boolean containsEntry(Entry<K, V> entry);


    V apply(K key) throws NoSuchElementException;


    V getOrThrow(K key) throws NoSuchElementException;

    MapF<K, V> filterKeys(Function1B<? super K> p);

    <W> MapF<K, W> mapValues(Function<? super V, ? extends W> f);

    <W> ListF<W> mapEntries(Function2<? super K, ? super V, ? extends W> f);

    MapF<K, V> filter(Function1B<? super Tuple2<K, V>> p);

    MapF<K, V> filter(Function2B<? super K, ? super V> p);

    MapF<K, V> filterEntries(Function1B<Entry<K, V>> p);

    MapF<K, V> filterValues(Function1B<? super V> p);

    void forEachEntry(Function2V<? super K, ? super V> op);

    boolean forAllEntries(Function2B<? super K, ? super V> op);


    Function<K, V> asFunction();


    Function<K, Option<V>> asFunctionO();

    Function<K, V> asFunctionOrElse(V fallback);

    Function<K, V> asFunctionOrElse(Function<K, V> fallback);


    void put(Tuple2<K, V> entry);


    void putAll(Iterable<Tuple2<K, V>> entries);

    void putAllEntries(Iterable<Entry<K, V>> entries);


    Option<V> removeO(K key);


    SetF<K> keySet();

    ListF<K> keys();

    SetF<Entry<K, V>> entrySet();

    Tuple2List<K, V> entries();


    CollectionF<V> values();

    void put(Entry<K, V> entry);

    MapF<K, V> plus(MapF<K, V> map);

    MapF<K, V> plus1(K key, V value);

    MapF<K, V> unmodifiable();

    <L, W> MapF<L, W> uncheckedCast();


    @Override
    boolean containsKey(Object key);
    boolean containsKeyTs(K key);
    boolean containsKeyTu(Object key);

    Function1B<K> containsKeyF();


    @Override
    boolean containsValue(Object value);
    boolean containsValueTs(V value);
    boolean containsValueTu(Object value);


    @Override
    V remove(Object key);
    V removeTs(K key);
    V removeTu(Object key);


    @Override
    V get(Object key);
    V getTs(K key);
    V getTu(Object key);
} //~
