package ru.yandex.bolts.collection.impl;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;

import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.Tuple2List;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.Function2B;
import ru.yandex.bolts.function.Function2V;

/**
 * Implementation of {@link MapF} algorithms.
 *
 * @author Stepan Koltsov
 */
public abstract class AbstractMapF<K, V> extends AbstractMap<K, V> implements MapF<K, V> {

    @Override
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public SetF<K> keySet() {
        return CollectionsF.x(super.keySet());
    }

    @Override
    public ListF<K> keys() {
        return keySet().toList();
    }

    public abstract SetF<Entry<K, V>> entrySet();

    public Tuple2List<K, V> entries() {
        return Tuple2List.tuple2List(entrySet().map(AbstractMapF.<K, V>tupleM()));
    }

    protected boolean eq(Object a, Object b) {
        if (b == null || a == null) return b == a;
        else return b.equals(a);
    }

    public CollectionF<V> values() {
        return CollectionsF.x(super.values());
    }

    public Option<V> getO(K key) {
        return Option.notNull(get(key));
    }

    public V getOrElse(K key, V elseValue) {
        return getO(key).getOrElse(elseValue);
    }

    public V getOrThrow(K key, String message) {
        return getO(key).getOrThrow(message);
    }

    public V getOrThrow(K key, String message, Object param) {
        return getO(key).getOrThrow(message, param);
    }

    public V getOrElseUpdate(K key, V value) {
        return getOrElseUpdate(key, Function0.constF(value));
    }

    public V getOrElseUpdate(K key, Function0<V> valueF) {
        Option<V> option = getO(key);
        if (option.isDefined()) return option.get();
        else {
            V value = valueF.apply();
            put(key, value);
            return value;
        }
    }

    public V getOrElseUpdate(final K key, final Function<K, V> value) {
        return getOrElseUpdate(key, value.bind(key));
    }

    public boolean containsEntry(K key, V value) {
        Option<V> got = getO(key);
        return got.isDefined() && got.get().equals(value);
    }

    public boolean containsEntry(Entry<K, V> entry) {
        return containsEntry(entry.getKey(), entry.getValue());
    }

    public V apply(K key) {
        return getO(key).get();
    }

    protected <A, B> MapF<A, B> newMap(CollectionF<Entry<A, B>> entries) {
        MapF<A, B> r = newMutableMap();
        r.putAllEntries(entries);
        return r;
    }

    protected <A, B> MapF<A, B> newMapFromTuples(CollectionF<Tuple2<A, B>> entries) {
        MapF<A, B> r = newMutableMap();
        r.putAll(entries);
        return r;
    }

    protected <A, B> MapF<A, B> newMutableMap() {
        return CollectionsF.hashMap();
    }

    private Function<Entry<K, V>, K> entryKeyM() {
        return new Function<Entry<K, V>, K>() {
            public K apply(Entry<K, V> entry) {
                return entry.getKey();
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public MapF<K, V> filterKeys(final Function1B<? super K> p) {
        Function1B<K> p1 = (Function1B<K>) p;
        return filterEntries(p1.compose(entryKeyM()));
    }

    @Override
    public MapF<K, V> filterEntries(Function1B<Entry<K, V>> p) {
        if (isEmpty()) return this;

        return newMap(entrySet().filter(p));
    }

    @Override
    public MapF<K, V> filterValues(Function1B<? super V> p) {
        return filterEntries(AbstractMapF.<K, V>entryValueM().andThen(p));
    }

    @Override
    public boolean forAllEntries(Function2B<? super K, ? super V> op) {
        return entries().forAll(op.asFunction1B().uncheckedCast());
    }

    @Override
    public void forEachEntry(Function2V<? super K, ? super V> op) {
        entries().forEach(op.asFunction1V().uncheckedCast());
    }

    private static <K, V> Function<Entry<K, V>, V> entryValueM() {
        return new Function<Entry<K,V>, V>() {
            public V apply(java.util.Map.Entry<K, V> a) {
                return a.getValue();
            }
        };
    }

    public MapF<K, V> unmodifiable() {
        return UnmodifiableDefaultMapF.wrap(this);
    }

    public <W> MapF<K, W> mapValues(final Function<? super V, ? extends W> f) {
        if (isEmpty()) return emptyMap();

        ListF<Tuple2<K, W>> xx = entrySet().map(new Function<Entry<K, V>, Tuple2<K, W>>() {
            public Tuple2<K, W> apply(Entry<K, V> entry) {
                return new Tuple2<K, W>(entry.getKey(), f.apply(entry.getValue()));
            }
        });
        return newMapFromTuples(xx);
    }

    public <W> ListF<W> mapEntries(final Function2<? super K, ? super V, ? extends W> f) {
        return entrySet().map(new Function<Entry<K, V>, W>() {
            public W apply(Entry<K, V> entry) {
                return f.apply(entry.getKey(), entry.getValue());
            }
        });
    }

    protected <X, Y> MapF<X, Y> emptyMap() {
        return CollectionsF.map();
    }

    public Function<K, V> asFunction() {
        return new Function<K, V>() {
            public V apply(K a) {
                return AbstractMapF.this.apply(a);
            }
        };
    }

    public Function<K, Option<V>> asFunctionO() {
        return new Function<K, Option<V>>() {
            public Option<V> apply(K key) {
                return getO(key);
            }
        };
    }

    public Function<K, V> asFunctionOrElse(V fallback) {
        return asFunctionO().andThen(Option.getOrElseF(fallback));
    }

    public Function<K, V> asFunctionOrElse(final Function<K, V> fallbackF) {
        return new Function<K, V>() {
            public V apply(K key) {
                return getO(key).getOrElse(fallbackF.apply(key));
            }
        };
    }

    /** Must check for non-null arguments */
    public V put(K key, V value) {
        throw new UnsupportedOperationException("readonly map");
    }

    public void put(Tuple2<K, V> entry) {
        put(entry.get1(), entry.get2());
    }

    public void put(Entry<K, V> entry) {
        put(entry.getKey(), entry.getValue());
    }

    public MapF<K, V> plus(MapF<K, V> map) {
        if (map.isEmpty()) return this;
        else if (this.isEmpty()) return map;
        else {
            MapF<K, V> result = newMutableMap();
            result.putAll(this);
            result.putAll(map);
            return result;
        }
    }

    public MapF<K, V> plus1(K key, V value) {
        return plus(CollectionsF.map(key, value));
    }

    public void putAll(Iterable<Tuple2<K, V>> entries) {
        for (Tuple2<K, V> value : entries) {
            put(value);
        }
    }

    public void putAllEntries(Iterable<Entry<K, V>> entries) {
        for (Entry<K, V> entry : entries) {
            put(entry);
        }
    }

    public Option<V> removeO(K key) {
        return Option.notNull(remove(key));
    }

    @SuppressWarnings("unchecked")
    public <L, W> MapF<L, W> uncheckedCast() {
        return (MapF<L, W>) this;
    }

    public static <K, V> Function<Map.Entry<K, V>, Tuple2<K, V>> tupleM() {
        return new Function<Map.Entry<K, V>, Tuple2<K, V>>() {
            public Tuple2<K, V> apply(Map.Entry<K, V> e) {
                return new Tuple2<K, V>(e.getKey(), e.getValue());
            }
        };
    }

    protected abstract static class AbstractEntry<K, V> implements Entry<K, V> {
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }
    }

    public static class SimpleEntry<K, V> extends AbstractEntry<K, V> implements Serializable {
        private static final long serialVersionUID = -3060573265091739338L;

        private final K key;
        private final V value;

        public SimpleEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    @Override
    public boolean containsKeyTs(K key) {
        return containsKey(key);
    }

    @Override
    public boolean containsKeyTu(Object key) {
        return containsKey(key);
    }

    @Override
    public boolean containsValueTs(V value) {
        return containsValue(value);
    }

    @Override
    public boolean containsValueTu(Object value) {
        return containsValue(value);
    }

    @Override
    public V removeTs(K key) {
        return remove(key);
    }

    @Override
    public V removeTu(Object key) {
        return remove(key);
    }

    @Override
    public V getTs(K key) {
        return get(key);
    }

    @Override
    public V getTu(Object key) {
        return get(key);
    }

} //~
