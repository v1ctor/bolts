package ru.yandex.bolts.collection.impl;

import java.io.Serializable;
import java.util.Map;

import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.collection.SetF;

/**
 * Implementation of MapF with delegation
 *
 * @author Stepan Koltsov
 */
public class DefaultMapF<K, V> extends AbstractMapF<K,V> implements Serializable {
    private static final long serialVersionUID = 2421450360288762730L;

    protected Map<K,V> target;

    protected DefaultMapF(Map<K, V> target) {
        this.target = target;
    }

    public MapF<K, V> unmodifiable() {
        return UnmodifiableDefaultMapF.wrap(target);
    }

    public int size() {
        return target.size();
    }

    public boolean isEmpty() {
        return target.isEmpty();
    }

    public boolean containsKey(Object key) {
        return target.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return target.containsValue(value);
    }

    public V get(Object key) {
        return target.get(key);
    }

    public V put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("map keys cannot be null");
        //if (value == null) throw new IllegalArgumentException("map values cannot be null");
        return target.put(key, value);
    }

    public V remove(Object key) {
        return target.remove(key);
    }

    public void putAll(Map<? extends K, ? extends V> t) {
        target.putAll(t);
    }

    public void clear() {
        target.clear();
    }

    public SetF<K> keySet() {
        return CollectionsF.x(target.keySet());
    }

    public CollectionF<V> values() {
        return CollectionsF.x(target.values());
    }

    public SetF<Entry<K, V>> entrySet() {
        return CollectionsF.x(target.entrySet());
    }

    public boolean equals(Object o) {
        return target.equals(o);
    }

    public int hashCode() {
        return target.hashCode();
    }

    public static <A, B> MapF<A, B> wrap(Map<A, B> map) {
        if (map instanceof MapF<?, ?>) return (MapF<A, B>) map;
        else return new DefaultMapF<A, B>(map);
    }
} //~
