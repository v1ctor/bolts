package ru.yandex.bolts.collection;

import java.util.Map;
import java.util.NoSuchElementException;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2B;
import ru.yandex.bolts.function.Function2V;

/**
 * Mutable map with Scala-like Map methods.
 *
 * @see IterableF
 * @author Stepan Koltsov
 */
public interface MapF<K, V> extends Map<K, V> {
    boolean isNotEmpty();

    /** Get value by key
     *
     * @param key key
     *
     * @return option element
     * */
    Option<V> getO(K key);

    /** Get value or default
     *
     * @param key key
     * @param elseValue eleValue
     *
     * @return element or else
     * */
    V getOrElse(K key, V elseValue);

    /** Get value or throw
     *
     * @param key key
     * @param message error message
     *
     * @return element
     * */
    V getOrThrow(K key, String message);

    /** Get value or throw
     *
     * @param key key
     * @param message error message
     * @param param exception param
     *
     * @return element
     * */
    V getOrThrow(K key, String message, Object param);

    /** Get value or else update
     *
     * @param key key
     * @param elseValue else
     *
     * @return element
     * */
    V getOrElseUpdate(K key, V elseValue);

    /** Get value or else update
     *
     * @param key key
     * @param value else
     *
     * @return element
     * */
    V getOrElseUpdate(K key, Function0<V> value);

    /** Get value or else update. Function maps key to default value
     *
     * @param key key
     * @param m else
     *
     * @return element
     * */
    V getOrElseUpdate(K key, Function<K, V> m);

    boolean containsEntry(K key, V value);

    boolean containsEntry(Entry<K, V> entry);

    /**
     * Use {@link #getOrThrow(Object)} instead.
     *
     * @param key key
     *
     * @return element
     * */
    V apply(K key) throws NoSuchElementException;

    /** Throws if there is no entry for key
     *
     * @param key key
     *
     * @return element
     * */
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

    /** Delegate to {@link #apply(Object)}
     *
     * @return function
     * */
    Function<K, V> asFunction();

    /** Delegate to {@link #getO(Object)}
     *
     * @return function
     * */
    Function<K, Option<V>> asFunctionO();

    Function<K, V> asFunctionOrElse(V fallback);

    Function<K, V> asFunctionOrElse(Function<K, V> fallback);

    /** Put
     *
     * @param entry entry
     * */
    void put(Tuple2<K, V> entry);

    /** Put all
     *
     * @param entries entriew
     * */
    void putAll(Iterable<Tuple2<K, V>> entries);

    void putAllEntries(Iterable<Entry<K, V>> entries);

    /** Remove element. Return old
     *
     * @param key key
     *
     * @return element
     * */
    Option<V> removeO(K key);

    /** Key set
     *
     * @return keys
     * */
    SetF<K> keySet();

    ListF<K> keys();

    SetF<Entry<K, V>> entrySet();

    Tuple2List<K, V> entries();

    /** Values
     *
     * @return values
     * */
    CollectionF<V> values();

    void put(Entry<K, V> entry);

    MapF<K, V> plus(MapF<K, V> map);

    MapF<K, V> plus1(K key, V value);

    MapF<K, V> unmodifiable();

    <L, W> MapF<L, W> uncheckedCast();

    /** @deprecated */
    @Override
    boolean containsKey(Object key);
    boolean containsKeyTs(K key);
    boolean containsKeyTu(Object key);

    Function1B<K> containsKeyF();

    /** @deprecated */
    @Override
    boolean containsValue(Object value);
    boolean containsValueTs(V value);
    boolean containsValueTu(Object value);

    /** @deprecated */
    @Override
    V remove(Object key);
    V removeTs(K key);
    V removeTu(Object key);

    /** @deprecated */
    @Override
    V get(Object key);
    V getTs(K key);
    V getTu(Object key);
} //~
