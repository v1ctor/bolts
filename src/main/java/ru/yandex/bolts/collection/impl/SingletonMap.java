package ru.yandex.bolts.collection.impl;

import java.io.Serializable;

import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Unmodifiable;

/**
 * @author Stepan Koltsov
 */
public class SingletonMap<K, V> extends AbstractMapF<K, V> implements Serializable, Unmodifiable {
    private static final long serialVersionUID = 1863420184567712379L;

    private final K key;
    private final V value;

    public SingletonMap(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public SetF<Entry<K, V>> entrySet() {
        return CollectionsF.set(new SimpleEntry<>(key, value));
    }

    @Override
    public V get(Object key) {
        if (this.eq(key, this.key)) return value;
        else return null;
    }

    @Override
    public boolean containsValue(Object value) {
        return this.eq(this.value, value);
    }

    @Override
    public SetF<K> keySet() {
        return CollectionsF.set(key);
    }

    @Override
    public int size() {
        return 1;
    }

} //~
