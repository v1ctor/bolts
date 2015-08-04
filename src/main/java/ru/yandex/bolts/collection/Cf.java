package ru.yandex.bolts.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import ru.yandex.bolts.collection.impl.AbstractListF;
import ru.yandex.bolts.collection.impl.AbstractSetF;
import ru.yandex.bolts.collection.impl.DefaultCollectionF;
import ru.yandex.bolts.collection.impl.DefaultEnumerationF;
import ru.yandex.bolts.collection.impl.DefaultIteratorF;
import ru.yandex.bolts.collection.impl.DefaultListF;
import ru.yandex.bolts.collection.impl.DefaultMapF;
import ru.yandex.bolts.collection.impl.DefaultSetF;
import ru.yandex.bolts.collection.impl.EmptyIterator;
import ru.yandex.bolts.collection.impl.EmptyMap;
import ru.yandex.bolts.collection.impl.EmptySet;
import ru.yandex.bolts.collection.impl.SetFromMap;
import ru.yandex.bolts.collection.impl.SingletonMap;
import ru.yandex.bolts.collection.impl.SingletonSet;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.function.forhuman.Comparator;
import ru.yandex.bolts.type.BooleanType;
import ru.yandex.bolts.type.CharSequenceType;
import ru.yandex.bolts.type.CharacterType;
import ru.yandex.bolts.type.ObjectType;
import ru.yandex.bolts.type.StringType;
import ru.yandex.bolts.type.collection.ArrayListType;
import ru.yandex.bolts.type.collection.CollectionType;
import ru.yandex.bolts.type.collection.ListType;
import ru.yandex.bolts.type.collection.SetType;
import ru.yandex.bolts.type.collection.Tuple2ListType;
import ru.yandex.bolts.type.collection.Tuple3ListType;
import ru.yandex.bolts.type.number.ByteType;
import ru.yandex.bolts.type.number.DoubleType;
import ru.yandex.bolts.type.number.FloatType;
import ru.yandex.bolts.type.number.IntegerType;
import ru.yandex.bolts.type.number.LongType;
import ru.yandex.bolts.type.number.ShortType;


public class Cf {

    protected Cf() { }


    public static <E> ListF<E> wrap(List<E> list) {
        return x(list);
    }


    public static <E> SetF<E> wrap(Set<E> set) {
        return x(set);
    }


    public static <E> CollectionF<E> wrap(Collection<E> coll) {
        return x(coll);
    }


    public static <E> IteratorF<E> wrap(Iterator<E> iter) {
        return x(iter);
    }


    public static <E> IteratorF<E> wrap(Enumeration<E> enumeration) {
        return x(enumeration);
    }


    public static <K, V> MapF<K, V> wrap(Map<K, V> map) {
        return x(map);
    }



    public static MapF<String, String> wrap(Properties ps) {
        return x(ps);
    }


    public static <E> ListF<E> wrap(E[] array) {
        return x(array);
    }


    public static <E> IteratorF<E> x(Iterator<E> iterator) {
        return DefaultIteratorF.wrap(iterator);
    }


    public static <E> IteratorF<E> x(Enumeration<E> enumeration) {
        return DefaultEnumerationF.wrap(enumeration);
    }


    public static <E> CollectionF<E> x(Collection<E> collection) {
        if (collection instanceof List<?>)
            return DefaultListF.wrap((List<E>) collection);
        else if (collection instanceof Set<?>)
            return DefaultSetF.wrap((Set<E>) collection);
        else
            return DefaultCollectionF.wrap(collection);
    }


    public static <E> Option<E> x(Optional<E> optional) {
        return Option.wrap(optional);
    }


    public static <E> ListF<E> x(List<E> list) {
        return DefaultListF.wrap(list);
    }


    public static <E> SetF<E> x(Set<E> set) {
        return DefaultSetF.wrap(set);
    }


    public static <K, V> MapF<K, V> x(Map<K, V> map) {
        return DefaultMapF.wrap(map);
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static MapF<String, String> x(Properties properties) {
        return x((Map) properties);
    }


    public static <E> ListF<E> x(E[] array) {
        return x(Arrays.asList(array));
    }



    @SuppressWarnings({"unchecked"})
    public static <E> SetF<E> set() {
        return EmptySet.INSTANCE;
    }


    public static <T> SetF<T> set(T o) {
        return new SingletonSet<>(o);
    }


    public static <E> SetF<E> set(E e1, E e2) {
        return set(e1).plus1(e2);
    }


    public static <E> SetF<E> set(E e1, E e2, E e3) {
        return set(e1, e2).plus1(e3);
    }


    @SuppressWarnings("unchecked")
    public static <E> SetF<E> set(E... elements) {
        if (elements.length == 0) return set();
        else if (elements.length == 1) return set(elements[0]);
        else if (elements.length == 2) return set(elements[0], elements[1]);
        else if (elements.length == 3) return set(elements[0], elements[1], elements[2]);
        else return hashSet(elements);
    }


    public static <E> SetF<E> set(Collection<E> elements) {
        if (elements.isEmpty()) return set();
        //else if (elements.size() == 1) return singleton(elements.iterator().next());
        else return hashSet(elements).unmodifiable();
    }


    public static <A> SetF<A> hashSet() {
        return x(new HashSet<>());
    }


    public static <E> SetF<E> hashSet(Collection<E> collection) {
        return x(new HashSet<>(collection));
    }


    @SuppressWarnings("unchecked")
    public static <E> SetF<E> hashSet(E... elements) {
        return hashSet(list(elements));
    }


    public static <E> Function0<SetF<E>> newHashSetF() {
        return Cf::hashSet;
    }


    public static <E> SetF<E> identityHashSet() {
        return new SetFromMap<>(new IdentityHashMap<>());
    }


    public static <E> SetF<E> identityHashSet(Collection<E> elements) {
        SetF<E> set = identityHashSet();
        set.addAll(elements);
        return set;
    }


    @SuppressWarnings("unchecked")
    public static <E> SetF<E> identityHashSet(E... elements) {
        return identityHashSet(list(elements));
    }


    public static <A> SetF<A> treeSet() {
        return x(new TreeSet<>());
    }


    public static <E> SetF<E> treeSet(Collection<E> collection) {
        return x(new TreeSet<>(collection));
    }


    @SuppressWarnings("unchecked")
    public static <E> SetF<E> treeSet(E... elements) {
        return treeSet(list(elements));
    }


    public static <E> ListF<E> list() {
        return List.cons();
    }


    public static <E> ListF<E> list(E e) {
        return List.cons(e);
    }


    public static <E> ListF<E> list(E e1, E e2) {
        return List.cons(e1, e2);
    }


    @SuppressWarnings("unchecked")
    public static <E> ListF<E> list(E... elements) {
        return List.cons(elements);
    }


    public static <E> ListF<E> list(Collection<E> elements) {
        return List.cons(elements);
    }


    public static <E> ListF<E> arrayList() {
        return ArrayList.cons();
    }


    public static <A> ListF<A> arrayList(Collection<A> collection) {
        return ArrayList.cons(collection);
    }


    @SuppressWarnings("unchecked")
    public static <A> ListF<A> arrayList(A... elements) {
        return ArrayList.cons(elements);
    }


    public static <A> ListF<A> arrayList(int initialCapacity) {
        return ArrayList.cons(initialCapacity);
    }

    public static <A> Function0<ListF<A>> newArrayListF() {
        return ArrayList.consF();
    }


    public static <K, V> MapF<K, V> map(K key, V value) {
        return new SingletonMap<>(key, value);
    }


    public static <K, V> MapF<K, V> map(K key1, V value1, K key2, V value2) {
        return map(key1, value1).plus1(key2, value2);
    }


    public static <K, V> MapF<K, V> map(Collection<Tuple2<K, V>> pairs) {
        return Tuple2List.cons(Cf.x(pairs).toList()).toMap();
    }


    @SuppressWarnings({"unchecked"})
    public static <K, V> MapF<K, V> map() {
        return EmptyMap.INSTANCE;
    }


    public static <E> ListF<E> linkedList() {
        return x(new LinkedList<>());
    }


    public static <K, V> MapF<K, V> hashMap() {
        return x(new HashMap<>());
    }

    public static <K, V> Function0<MapF<K, V>> newHashMapF() {
        return Cf::hashMap;
    }


    @SuppressWarnings("serial")
    public static <K, V> MapF<K, V> identityHashMap() {
        return new DefaultMapF<K, V>(new IdentityHashMap<>()) {
            protected <A, B> MapF<A, B> newMutableMap() {
                return identityHashMap();
            }
        };
    }


    public static <K, V> MapF<K, V> hashMap(Iterable<Tuple2<K, V>> entries) {
        MapF<K, V> map = hashMap();
        map.putAll(entries);
        return map;
    }


    public static <K, V> MapF<K, V> hashMap(Map<K, V> entries) {
        MapF<K, V> map = hashMap();
        map.putAll(entries);
        return map;
    }


    @SuppressWarnings("serial")
    public static <K, V> MapF<K, V> concurrentHashMap() {
        return new DefaultMapF<K, V>(new ConcurrentHashMap<>()) {
            protected <A, B> MapF<A, B> newMutableMap() {
                return concurrentHashMap();
            }
        };
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final IteratorF EMPTY_ITERATOR = new EmptyIterator();


    @SuppressWarnings({"unchecked"})
    public static <E> IteratorF<E> emptyIterator() {
        return EMPTY_ITERATOR;
    }

    /**
     * Immutable list of <code>element</code> repeated <code>times</code> times.
     *
     * @param element element to repeat
     * @param times amount of repeats
     * @param <T> element
     *
     * @return list with repeated elements
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
                return Cf.set(element);
            }

            @SuppressWarnings("deprecation")
            public boolean contains(Object o) {
                return unique().contains(o);
            }
        };
    }

    @SuppressWarnings({"unchecked"})
    public static <T> ListF<T> repeat(Function0<T> factory, int times) {
        Object[] elements = new Object[times];
        for (int i = 0; i < times; i++) {
            elements[i] = factory.apply();
        }
        return Cf.x((T[]) elements);
    }

    /**
     * Immutable list of integer in given range.
     *
     * @param startInclusive start of range
     * @param endExclusive end of range
     *
     * @return empty list if <code>end &lt; start</code>
     */
    public static ListF<Integer> range(final int startInclusive, final int endExclusive) {
        if (startInclusive >= endExclusive) return list();
        return new Range(startInclusive, endExclusive);
    }

    /**
     * Immutable set of integer in given range.
     *
     * @param startInclusive start of range
     * @param endExclusive end of range
     *
     * @return empty set if <code>end &lt; start</code>
     */
    private static SetF<Integer> rangeAsSet(final int startInclusive, final int endExclusive) {
        if (startInclusive >= endExclusive) return set();
        return new RangeAsSet(startInclusive, endExclusive);
    }

    private static class Range extends AbstractListF<Integer> implements Serializable {
        private static final long serialVersionUID = 5434453557597790996L;

        private final int startInclusive;
        private final int endExclusive;

        public Range(int startInclusive, int endExclusive) {
            this.startInclusive = startInclusive;
            this.endExclusive = endExclusive;
        }

        @Override
        public int size() {
            return endExclusive - startInclusive;
        }

        @Override
        public Integer get(int index) {
            if (index < 0 || index >= size())
                throw new IndexOutOfBoundsException();
            return startInclusive + index;
        }

        @Override
        public SetF<Integer> unique() {
            return rangeAsSet(startInclusive, endExclusive);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean contains(Object o) {
            return unique().contains(o);
        }

        @Override
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

        @Override
        public IteratorF<Integer> iterator() {
            return toList().iterator();
        }

        @Override
        public int size() {
            return endExclusive - startInclusive;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Integer)) return false;
            Integer v = (Integer) o;
            return v >= startInclusive && v < endExclusive;
        }

        @Override
        public ListF<Integer> toList() {
            return range(startInclusive, endExclusive);
        }

        @Override
        public String toString() {
            return toList().toString();
        }
    }


    public static <A, B> Function2<MapF<A, B>, MapF<A, B>, MapF<A, B>> mapPlusF() {
        return MapF::plus;
    }


    public static <A> Function2<ListF<A>, ListF<A>, ListF<A>> listPlusF() {
        return List.plusF();
    }


    public static <A> Function2<SetF<A>, SetF<A>, SetF<A>> setPlusF() {
        return Set.plusF();
    }


    public static <E> Function<List<E>, ListF<E>> wrapListF() {
        return List.wrapF();
    }


    public static <E> Function<Set<E>, SetF<E>> wrapSetF() {
        return Cf::x;
    }


    public static <K, V> Function<Map<K, V>, MapF<K, V>> wrapMapF() {
        return Cf::x;
    }


    public static <E> Function<Iterator<E>, IteratorF<E>> wrapIteratorF() {
        return Cf::x;
    }


    public static <E> Function<Enumeration<E>, IteratorF<E>> wrapEnumerationF() {
        return Cf::x;
    }


    public static <T> Function<Collection<T>, Integer> sizeF() {
        return Collection.sizeF().uncheckedCast();
    }


    public static <T> Function1B<Iterator<T>> hasNextF() {
        return Iterator::hasNext;
    }


    public static <T> Function<Iterable<T>, IteratorF<T>> iteratorF() {
        return a -> x(a.iterator());
    }


    public static <F, T> Function2<Collection<F>, Function<? super F, T>, ListF<T>> mapF() {
        return Collection.mapF();
    }


    public static <F, T> Function<Collection<F>, ListF<T>> mapF(Function<? super F, T> f) {
        return Collection.mapF(f);
    }


    public static <E> Function2<Collection<E>, Comparator<? super E>, ListF<E>> sortedF() {
        return Collection.sortedF();
    }


    public static <E> Function<Collection<E>, ListF<E>> sortedF(
            Comparator<? super E> comparator)
    {
        return Collection.sortedF(comparator);
    }


    public static <E> Function<Collection<E>, SetF<E>> uniqueF() {
        return input -> Cf.x(input).unique();
    }


    public static <E> Function<Collection<E>, ListF<E>> toListF() {
        return input -> Cf.x(input).toList();
    }


    public static Function2<Collection<?>, String, String> mkStringF() {
        return (a, sep) -> Cf.x(a).mkString(sep);
    }

    public static Function<Collection<?>, String> mkStringF(String sep) {
        return mkStringF().bind2(sep);
    }

    public static <T> ListF<T> flatten(Collection<? extends Collection<T>> l) {
        return Cf.x(l).flatMap(Function.<Collection<T>>identityF());
    }

    public static final ObjectType Object = new ObjectType();
    public static final BooleanType Boolean = new BooleanType();
    public static final IntegerType Integer = new IntegerType();
    public static final LongType Long = new LongType();
    public static final ShortType Short = new ShortType();
    public static final ByteType Byte = new ByteType();
    public static final FloatType Float = new FloatType();
    public static final DoubleType Double = new DoubleType();
    public static final CharacterType Character = new CharacterType();
    public static final CharSequenceType CharSequence = new CharSequenceType();
    public static final StringType String = new StringType();
    public static final ListType List = new ListType();
    public static final ArrayListType ArrayList = new ArrayListType();
    public static final SetType Set = new SetType();
    public static final CollectionType Collection = new CollectionType();
    public static final Tuple2ListType Tuple2List = new Tuple2ListType();
    public static final Tuple3ListType Tuple3List = new Tuple3ListType();

} //~
