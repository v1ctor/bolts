package ru.yandex.bolts.collection.impl;

import java.io.Serializable;

import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Unmodifiable;

/**
 * @author Stepan Koltsov
 */
public class EmptyMap<K, V> extends AbstractMapF<K, V> implements Unmodifiable, Serializable {
    private static final long serialVersionUID = -3018389216178387666L;
    
    @SuppressWarnings("unchecked")
    public static final MapF INSTANCE = new EmptyMap();

    public SetF<Entry<K, V>> entrySet() {
        return CollectionsF.set();
    }

    @Override
    public SetF<K> keySet() {
        return CollectionsF.set();
    }

    @Override
    public CollectionF<V> values() {
        return CollectionsF.list();
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public Option<V> getO(K key) {
        return Option.none();
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public MapF<K, V> unmodifiable() {
        return this;
    }

    private Object readResolve() {
        return INSTANCE;
    }
} //~
