package ru.yandex.bolts.collection.impl;

import java.util.Map;

import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.Unmodifiable;


public class UnmodifiableDefaultMapF<K, V> extends DefaultMapF<K, V> implements Unmodifiable {
    private static final long serialVersionUID = -1672128761500262911L;

    protected UnmodifiableDefaultMapF(Map<K, V> target) {
        super(target);
    }

    @Override
    public MapF<K, V> unmodifiable() {
        return this;
    }

    @Override
    public void put(Tuple2<K, V> entry) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public void put(Entry<K, V> entry) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public void putAllEntries(Iterable<Entry<K, V>> entries) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public void putAll(Iterable<Tuple2<K, V>> entries) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public Option<V> removeO(K key) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> t) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public SetF<K> keySet() {
        return new UnmodifiableDefaultSetF<>(target.keySet());
    }

    @Override
    public SetF<Entry<K, V>> entrySet() {
        return new UnmodifiableDefaultSetF<>(target.entrySet());
    }

    @Override
    public CollectionF<V> values() {
        return super.values();
    }

    public static <K, V> MapF<K, V> wrap(Map<K, V> map) {
        if (map instanceof MapF<?, ?> && map instanceof Unmodifiable) return (MapF<K, V>) map;
        else return new UnmodifiableDefaultMapF<>(map);
    }
} //~
