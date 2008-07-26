package ru.yandex.bolts.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import ru.yandex.bolts.collection.impl.AbstractListF;
import ru.yandex.bolts.collection.impl.AbstractSetF;
import ru.yandex.bolts.collection.impl.ArrayListF;
import ru.yandex.bolts.collection.impl.DefaultCollectionF;
import ru.yandex.bolts.collection.impl.DefaultIteratorF;
import ru.yandex.bolts.collection.impl.DefaultListF;
import ru.yandex.bolts.collection.impl.DefaultMapF;
import ru.yandex.bolts.collection.impl.DefaultSetF;
import ru.yandex.bolts.collection.impl.EmptyIterator;
import ru.yandex.bolts.collection.impl.EmptyList;
import ru.yandex.bolts.collection.impl.EmptyMap;
import ru.yandex.bolts.collection.impl.EmptySet;
import ru.yandex.bolts.collection.impl.ListOf2;
import ru.yandex.bolts.collection.impl.ReadOnlyArrayWrapper;
import ru.yandex.bolts.collection.impl.SetFromMap;
import ru.yandex.bolts.collection.impl.SingletonList;
import ru.yandex.bolts.collection.impl.SingletonMap;
import ru.yandex.bolts.collection.impl.SingletonSet;
import ru.yandex.bolts.function.forhuman.BinaryFunction;

/**
 * Utilities to create extended collections.
 *
 * @author Stepan Koltsov
 */
public class CollectionsF {
    protected CollectionsF() { }

    /** Wrap iterator */
    public static <E> IteratorF<E> x(Iterator<E> iterator) {
        return DefaultIteratorF.wrap(iterator);
    }

    /** Wrap collection */
    public static <E> CollectionF<E> x(Collection<E> collection) {
        if (collection instanceof List)
            return DefaultListF.wrap((List<E>) collection);
        else if (collection instanceof Set)
            return DefaultSetF.wrap((Set<E>) collection);
        else
            return DefaultCollectionF.wrap(collection);
    }

    /** Wrap list */
    public static <E> ListF<E> x(List<E> list) {
        return DefaultListF.wrap(list);
    }

    /** Wrap set */
    public static <E> SetF<E> x(Set<E> set) {
        return DefaultSetF.wrap(set);
    }

    /** Wrap map */
    public static <K, V> MapF<K, V> x(Map<K, V> map) {
        return DefaultMapF.wrap(map);
    }

    @SuppressWarnings("unchecked")
    public static MapF<String, String> x(Properties properties) {
        return x((Map) properties);
    }

    /** Empty set */
    @SuppressWarnings({"unchecked"})
    public static <E> SetF<E> set() {
        return EmptySet.INSTANCE;
    }

    /** Create singleton set */
    public static <T> SetF<T> set(T o) {
        return new SingletonSet<T>(o);
    }

    /**
     * Create set of specified elements.
     * 
     * @return set of either 1 or 2 elements
     */
    public static <E> SetF<E> set(E e1, E e2) {
        return set(e1).plus1(e2);
    }

    /**
     * Create set of specified elements.
     * 
     * @return set of either 1, 2 or 3 elements
     */
    public static <E> SetF<E> set(E e1, E e2, E e3) {
        return set(e1, e2).plus1(e3);
    }

    /** Create set of specified elements */
    public static <E> SetF<E> set(E... elements) {
        if (elements.length == 0) return set();
        else if (elements.length == 1) return set(elements[0]);
        else if (elements.length == 2) return set(elements[0], elements[1]);
        else if (elements.length == 3) return set(elements[0], elements[1], elements[2]);
        else return hashSet(elements);
    }

    /**
     * Create set of elements from collection.
     */
    public static <E> SetF<E> set(Collection<E> elements) {
        if (elements.isEmpty()) return set();
        //else if (elements.size() == 1) return singleton(elements.iterator().next());
        else return hashSet(elements).unmodifiable();
    }

    /**
     * Create mutable hash set.
     */
    public static <A> SetF<A> hashSet() {
        return x(new HashSet<A>());
    }

    /**
     * Create mutable hash set of specified elements.
     */
    public static <E> SetF<E> hashSet(Collection<E> collection) {
        return x(new HashSet<E>(collection));
    }

    /**
     * Create mutable hash set of specified elements.
     */
    public static <E> SetF<E> hashSet(E... elements) {
        return hashSet(list(elements));
    }
    
    /**
     * Create mutable identity hash set.
     */
    public static <E> SetF<E> identityHashSet() {
        return new SetFromMap<E>(new IdentityHashMap<E, Boolean>());
    }
    
    /**
     * Create mutable identity hash set with specified elements.
     */
    public static <E> SetF<E> identityHashSet(Collection<E> elements) {
        SetF<E> set = identityHashSet();
        set.addAll(elements);
        return set;
    }
    
    /**
     * Create mutable identity hash set with specified elements.
     */
    public static <E> SetF<E> identityHashSet(E... elements) {
        return identityHashSet(list(elements));
    }

    /**
     * Create extended tree set.
     */
    public static <A> SetF<A> treeSet() {
        return x(new TreeSet<A>());
    }

    /**
     * Create tree set of specified elements.
     */
    public static <E> SetF<E> treeSet(Collection<E> collection) {
        return x(new TreeSet<E>(collection));
    }

    /**
     * Create tree set of specified elements.
     */
    public static <E> SetF<E> treeSet(E... elements) {
        return treeSet(list(elements));
    }

    /**
     * Empty immutable list.
     */
    @SuppressWarnings({"unchecked"})
    public static <E> ListF<E> list() {
        return EmptyList.INSTANCE;
    }

    /**
     * Immutable singleton list.
     */
    public static <E> ListF<E> list(E e) {
        return new SingletonList<E>(e);
    }

    /**
     * Immutable list with two elements.
     */
    public static <E> ListF<E> list(E e1, E e2) {
        return new ListOf2<E>(e1, e2);
    }

    /**
     * Create list of specified elements. Resulting list is immutable, however changing array could lead to changing result list.
     */
    public static <E> ListF<E> list(E... elements) {
        if (elements.length == 0) return list();
        else if (elements.length == 1) return list(elements[0]);
        else if (elements.length == 2) return list(elements[0], elements[1]);
        return new ReadOnlyArrayWrapper<E>(elements);
    }

    /**
     * Create list of elements from given collection.
     */
    @SuppressWarnings({"unchecked"})
    public static <E> ListF<E> list(Collection<E> elements) {
        return (ListF<E>) list(elements.toArray());
    }

    /**
     * Create extended mutable array list.
     */
    public static <E> ListF<E> arrayList() {
        return new ArrayListF<E>();
    }

    /**
     * Create extended mutable array list containing given elements.
     */
    public static <A> ListF<A> arrayList(Collection<A> collection) {
        return new ArrayListF<A>(collection);
    }

    /**
     * Create extended array list of elements.
     */
    public static <A> ListF<A> arrayList(A... elements) {
        return arrayList(list(elements));
    }

    /**
     * Create array list with given capacity.
     * 
     * @see ArrayList#ArrayList(int)
     */
    public static <A> ListF<A> arrayList(int initialCapacity) {
        return new ArrayListF<A>(initialCapacity);
    }

    /**
     * Singleton map.
     */
    public static <K, V> MapF<K, V> map(K key, V value) {
        return new SingletonMap<K,V>(key, value);
    }

    /**
     * Map of either 1 or 2 entries.
     */
    public static <K, V> MapF<K, V> map(K key1, V value1, K key2, V value2) {
        return map(key1, value1).plus1(key2, value2);
    }

    /**
     * Immutable empty map. 
     */
    @SuppressWarnings({"unchecked"})
    public static <K, V> MapF<K, V> map() {
        return EmptyMap.INSTANCE;
    }

    /**
     * Create extended linked list.
     */
    public static <E> ListF<E> linkedList() {
        return x(new LinkedList<E>());
    }

    /**
     * Create hash map.
     * 
     * @see HashMap
     */
    public static <K, V> MapF<K, V> hashMap() {
        return x(new HashMap<K, V>());
    }

    /**
     * Identity hash map.
     * 
     * @see IdentityHashMap
     */
    @SuppressWarnings("serial")
    public static <K, V> MapF<K, V> identityHashMap() {
        return new DefaultMapF<K, V>(new IdentityHashMap<K, V>()) {
            protected <A, B> MapF<A, B> newMutableMap() {
                return identityHashMap();
            }
        };
    }

    /**
     * Create hash map of specified entries.
     * 
     * @see HashMap
     */
    public static <K, V> MapF<K, V> hashMap(Iterable<Tuple2<K, V>> entries) {
        MapF<K, V> map = hashMap();
        map.putAll(entries);
        return map;
    }

    @SuppressWarnings("serial")
    public static <K, V> MapF<K, V> concurrentHashMap() {
        return new DefaultMapF<K, V>(new ConcurrentHashMap<K, V>()) {
            protected <A, B> MapF<A, B> newMutableMap() {
                return concurrentHashMap();
            }
        };
    }

    @SuppressWarnings({"unchecked"})
    private static final IteratorF EMPTY_ITERATOR = new EmptyIterator();

    /**
     * Empty iterator.
     */
    @SuppressWarnings({"unchecked"})
    public static <E> IteratorF<E> emptyIterator() {
        return EMPTY_ITERATOR;
    }

    /**
     * Immutable list of <code>element</code> repeated <code>times</code> times.
     */
    public static <T> ListF<T> repeat(final T element, final int times) {
        if (times == 0) return list();
        
        if (times < 0) throw new IllegalArgumentException();
        
        return new AbstractListF<T>() {
            public int size() {
                return times;
            }

            public T get(int index) {
                if (index < 0 || index >= size())
                    throw new IndexOutOfBoundsException();
                return element;
            }

            public SetF<T> unique() {
                return CollectionsF.set(element);
            }

            public boolean contains(Object o) {
                return unique().contains(o);
            }
        };
    }

    /**
     * Immutable list of integer in given range.
     */
    public static ListF<Integer> range(final int startInclusive, final int endExclusive) {
        if (startInclusive >= endExclusive) return list();
        return new Range(startInclusive, endExclusive);
    }

    private static SetF<Integer> rangeAsSet(final int startInclusive, final int endExclusive) {
        if (startInclusive >= endExclusive) return set();
        return new RangeAsSet(startInclusive, endExclusive);
    }

    private static class Range extends AbstractListF<Integer> {
        private final int startInclusive;
        private final int endExclusive;

        public Range(int startInclusive, int endExclusive) {
            this.startInclusive = startInclusive;
            this.endExclusive = endExclusive;
        }

        public int size() {
            return endExclusive - startInclusive;
        }

        public Integer get(int index) {
            if (index < 0 || index >= size())
                throw new IndexOutOfBoundsException();
            return startInclusive + index;
        }

        public SetF<Integer> unique() {
            return rangeAsSet(startInclusive, endExclusive);
        }

        public boolean contains(Object o) {
            return unique().contains(o);
        }

        public String toString() {
            return "[" + startInclusive + "," + endExclusive + ")";
        }
    }

    private static class RangeAsSet extends AbstractSetF<Integer> {
        private final int startInclusive;
        private final int endExclusive;

        public RangeAsSet(int startInclusive, int endExclusive) {
            this.startInclusive = startInclusive;
            this.endExclusive = endExclusive;
        }

        public IteratorF<Integer> iterator() {
            return toList().iterator();
        }

        public int size() {
            return endExclusive - startInclusive;
        }

        public boolean contains(Object o) {
            if (!(o instanceof Integer)) return false;
            Integer v = (Integer) o;
            return v >= startInclusive && v < endExclusive;
        }

        public ListF<Integer> toList() {
            return range(startInclusive, endExclusive);
        }

        public String toString() {
            return toList().toString();
        }
    }

    public static <A, B> BinaryFunction<MapF<A, B>, MapF<A, B>, MapF<A, B>> mapPlusF() {
        return new BinaryFunction<MapF<A, B>, MapF<A, B>, MapF<A, B>>() {
            public MapF<A, B> call(MapF<A, B> m1, MapF<A, B> m2) {
                return m1.plus(m2);
            }
        };
    }

    public static <A> BinaryFunction<ListF<A>, ListF<A>, ListF<A>> listPlusF() {
        return new BinaryFunction<ListF<A>, ListF<A>, ListF<A>>() {
            public ListF<A> call(ListF<A> l1, ListF<A> l2) {
                return l1.plus(l2);
            }
        };
    }

    public static <A> BinaryFunction<SetF<A>, SetF<A>, SetF<A>> setPlusF() {
        return new BinaryFunction<SetF<A>, SetF<A>, SetF<A>>() {
            public SetF<A> call(SetF<A> l1, SetF<A> l2) {
                return l1.plus(l2);
            }
        };
    }
} //~
