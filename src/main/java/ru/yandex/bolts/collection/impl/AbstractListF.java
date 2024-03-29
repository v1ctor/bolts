package ru.yandex.bolts.collection.impl;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.ListIteratorF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.Tuple2List;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;


public abstract class AbstractListF<E> extends AbstractCollectionF<E> implements ListF<E> {
    private AbstractListF<E> self() { return this; }

    @Override
    protected <B> ListF<B> newMutableCollection() {
        return Cf.arrayList();
    }

    @Override
    protected <B> ListF<B> emptyCollection() {
        return Cf.list();
    }

    @Override
    protected <B> ListF<B> collect(IteratorF<B> iterator) {
        return iterator.toList();
    }

    @Override
    public ListF<E> filter(Function1B<? super E> p) {
        return (ListF<E>) super.filter(p);
    }

    @Override
    public ListF<E> filterNot(Function1B<? super E> p) {
        return (ListF<E>) super.filterNot(p);
    }

    @Override
    public ListF<E> filterNotNull() {
        return filter(Function1B.notNullF());
    }

    @Override
    public <F extends E> ListF<F> filterByType(Class<F> type) {
        return filter(Function1B.instanceOfF(type)).uncheckedCast();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple2<ListF<E>, ListF<E>> partition(Function1B<? super E> p) {
        return (Tuple2<ListF<E>, ListF<E>>) super.partition(p);
    }

    @Override
    public ListF<E> stableUnique() {
        if (size() <= 1) {
            return this;
        }

        return filter(new Function1B<E>() {
            private SetF<E> added = Cf.hashSet();

            @Override
            public boolean apply(E a) {
                return added.add(a);
            }
        });
    }

    @Override
    public Tuple2List<E, Integer> zipWithIndex() {
        return Tuple2List.tuple2List(iterator().zipWithIndex().toList());
    }

    @Override
    public ListF<E> plus(List<? extends E> addition) {
        if (addition.isEmpty()) return this;
        else if (isEmpty()) return Cf.x(addition).uncheckedCast();
        else return honestPlus(addition);
    }

    @Override
    public ListF<E> plus(Collection<? extends E> elements) {
        if (elements.isEmpty()) return this;
        else return honestPlus(elements);
    }

    @Override
    public ListF<E> plus(Iterator<? extends E> iterator) {
        return (ListF<E>) super.plus(iterator);
    }

    protected ListF<E> honestPlus(Collection<? extends E> elements) {
        ListF<E> result = Cf.arrayList(size() + elements.size());
        result.addAll(this);
        result.addAll(elements);
        return result;
    }

    @SuppressWarnings("unchecked")
    public ListF<E> plus(E... additions) {
        return plus(Cf.list(additions));
    }

    @Override
    public ListF<E> plus1(E e) {
        return (ListF<E>) super.plus1(e);
    }

    public E first() {
        return get(0);
    }

    public E last() {
        return get(size() - 1);
    }

    public Option<E> firstO() {
        return iterator().nextO();
    }

    public Option<E> lastO() {
        return reverseIterator().nextO();
    }

    public ListF<E> take(int count) {
        if (count <= 0) return emptyList();
        else if (count < size()) return subList(0, count);
        else return this;
    }

    public ListF<E> drop(int count) {
        if (count <= 0) return this;
        else if (count < size()) return subList(count, size());
        else return emptyList();
    }

    @Override
    public ListF<E> rtake(int count) {
        return drop(length() - count);
    }

    @Override
    public ListF<E> rdrop(int count) {
        return take(length() - count);
    }

    @Override
    public ListF<E> dropWhile(Function1B<? super E> f) {
        return iterator().dropWhile(f).toList();
    }

    @Override
    public ListF<E> takeWhile(Function1B<? super E> f) {
        return iterator().takeWhile(f).toList();
    }

    @Override
    public E reduceRight(Function2<E, E, E> f) {
        return reverseIterator().reduceLeft(f.swap());
    }

    @Override
    public Option<E> reduceRightO(Function2<E, E, E> f) {
        return reverseIterator().reduceLeftO(f.swap());
    }

    @Override
    public <B> B foldRight(B z, Function2<? super E, ? super B, B> f) {
        return reverseIterator().foldLeft(z, f.swap());
    }


    public ListF<E> toList() {
        return this;
    }

    @SuppressWarnings({"unchecked"})
    protected ListF<E> emptyList() {
        return Cf.list();
    }

    public ListF<E> unmodifiable() {
        //if (this instanceof Unmodifiable) return this;
        return UnmodifiableDefaultListF.wrap(this);
    }

    @Override
    public ListF<E> makeReadOnly() {
        return Cf.list(this);
    }

    @Override
    public <F> ListF<F> uncheckedCast() {
        return cast();
    }

    @Override
    public <F> ListF<F> cast() {
        return (ListF<F>) super.<F>cast();
    }

    @Override
    public <F> ListF<F> cast(Class<F> type) {
        return (ListF<F>) super.cast(type);
    }

    public int length() {
        return size();
    }

    public ListF<E> reverse() {
        if (size() <= 1) return this;
        return reverseIterator().toList();
    }

    public IteratorF<E> reverseIterator() {
        final ListIterator<E> listIterator = listIterator(size());
        return new AbstractIteratorF<E>() {
            @Override
            public boolean hasNext() {
                return listIterator.hasPrevious();
            }

            @Override
            public E next() {
                return listIterator.previous();
            }

            @Override
            public void remove() {
                listIterator.remove();
            }
        };
    }

    public <B> Tuple2List<E, B> zip(ListF<? extends B> that) {
        return Tuple2List.zip(this, that);
    }

    @Override
    public <B> Tuple2List<E, B> zipWith(final Function<? super E, ? extends B> f) {
        ListF<? extends B> that = map(f);
        return zip(that);
    }

    private class ReadOnlyItr extends SimpleListIterator {
        public void remove() {
            throw new UnsupportedOperationException("immutable");
        }
    }

    private class ReadOnlyListItr extends FullListIterator {
        private ReadOnlyListItr(int index) {
            super(index);
        }

        public void set(E o) {
            throw new UnsupportedOperationException("immutable");
        }

        public void add(E o) {
            throw new UnsupportedOperationException("immutable");
        }

        public void remove() {
            throw new UnsupportedOperationException("immutable");
        }
    }

    protected IteratorF<E> readOnlyIterator() {
        return new ReadOnlyItr();
    }

    protected ListIterator<E> readOnlyListIterator(int index) {
        return new ReadOnlyListItr(index);
    }

    // copy-paste from ArrayList from Harmony r561214 below this line
    // please do not add stuff here

    protected transient int modCount;

    private class SimpleListIterator extends AbstractIteratorF<E> {
        int pos = -1;

        int expectedModCount;

        int lastPosition = -1;

        SimpleListIterator() {
            super();
            expectedModCount = modCount;
        }

        public boolean hasNext() {
            return pos + 1 < size();
        }

        public E next() {
            if (expectedModCount == modCount) {
                try {
                    E result = get(pos + 1);
                    lastPosition = ++pos;
                    return result;
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }
            throw new ConcurrentModificationException();
        }

        public void remove() {
            if (this.lastPosition == -1) {
                throw new IllegalStateException();
            }

            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            try {
                AbstractListF.this.remove(lastPosition);
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }

            expectedModCount = modCount;
            if (pos == lastPosition) {
                pos--;
            }
            lastPosition = -1;
        }

        @Override
        public ListF<E> toList() {
            // does not check concurrent modifications, methods above shouldn't too
            return self().drop(lastPosition + 1);
        }

        @Override
        public IteratorF<E> drop(int count) {
            return toList().drop(count).iterator();
        }

        @Override
        public IteratorF<E> take(int count) {
            return toList().take(count).iterator();
        }

        @Override
        public IteratorF<E> dropWhile(Function1B<? super E> p) {
            while (hasNext()) {
                E e = next();
                if (!p.apply(e))
                    return self().drop(lastPosition).iterator();
            }
            return Cf.emptyIterator();
        }
    }

    private class FullListIterator extends SimpleListIterator implements ListIterator<E> {
        FullListIterator(int start) {
            super();
            if (0 <= start && start <= size()) {
                pos = start - 1;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        public void add(E object) {
            if (expectedModCount == modCount) {
                try {
                    AbstractListF.this.add(pos + 1, object);
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
                pos++;
                lastPosition = -1;
                if (modCount != expectedModCount) {
                    expectedModCount++;
                }
            } else {
                throw new ConcurrentModificationException();
            }
        }

        public boolean hasPrevious() {
            return pos >= 0;
        }

        public int nextIndex() {
            return pos + 1;
        }

        public E previous() {
            if (expectedModCount == modCount) {
                try {
                    E result = get(pos);
                    lastPosition = pos;
                    pos--;
                    return result;
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }
            throw new ConcurrentModificationException();
        }

        public int previousIndex() {
            return pos;
        }

        public void set(E object) {
            if (expectedModCount == modCount) {
                try {
                    AbstractListF.this.set(lastPosition, object);
                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalStateException();
                }
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    private static final class SubAbstractListRandomAccess<E> extends SubAbstractList<E> implements RandomAccess {
        SubAbstractListRandomAccess(AbstractListF<E> list, int start, int end) {
            super(list, start, end);
        }
    }

    private static class SubAbstractList<E> extends AbstractListF<E> {
        private final AbstractListF<E> fullList;

        private int offset;

        private int size;

        private static final class SubAbstractListIterator<E> extends AbstractIteratorF<E> implements ListIteratorF<E> {
            private final SubAbstractList<E> subList;

            private final ListIterator<E> iterator;

            private int start;

            private int end;

            SubAbstractListIterator(ListIterator<E> it,
                    SubAbstractList<E> list, int offset, int length) {
                super();
                iterator = it;
                subList = list;
                start = offset;
                end = start + length;
            }

            public void add(E object) {
                iterator.add(object);
                subList.sizeChanged(true);
                end++;
            }

            public boolean hasNext() {
                return iterator.nextIndex() < end;
            }

            public boolean hasPrevious() {
                return iterator.previousIndex() >= start;
            }

            public E next() {
                if (iterator.nextIndex() < end) {
                    return iterator.next();
                }
                throw new NoSuchElementException();
            }

            public int nextIndex() {
                return iterator.nextIndex() - start;
            }

            public E previous() {
                if (iterator.previousIndex() >= start) {
                    return iterator.previous();
                }
                throw new NoSuchElementException();
            }

            public int previousIndex() {
                int previous = iterator.previousIndex();
                if (previous >= start) {
                    return previous - start;
                }
                return -1;
            }

            public void remove() {
                iterator.remove();
                subList.sizeChanged(false);
                end--;
            }

            public void set(E object) {
                iterator.set(object);
            }
        }

        SubAbstractList(AbstractListF<E> list, int start, int end) {
            super();
            fullList = list;
            modCount = fullList.modCount;
            offset = start;
            size = end - start;
        }

        @Override
        public void add(int location, E object) {
            if (modCount == fullList.modCount) {
                if (0 <= location && location <= size) {
                    fullList.add(location + offset, object);
                    size++;
                    modCount = fullList.modCount;
                } else {
                    throw new IndexOutOfBoundsException();
                }
            } else {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean addAll(int location, Collection<? extends E> collection) {
            if (modCount == fullList.modCount) {
                if (0 <= location && location <= size) {
                    boolean result = fullList.addAll(location + offset,
                            collection);
                    if (result) {
                        size += collection.size();
                        modCount = fullList.modCount;
                    }
                    return result;
                }
                throw new IndexOutOfBoundsException();
            }
            throw new ConcurrentModificationException();
        }

        @Override
        public boolean addAll(Collection<? extends E> collection) {
            if (modCount == fullList.modCount) {
                boolean result = fullList.addAll(offset + size, collection);
                if (result) {
                    size += collection.size();
                    modCount = fullList.modCount;
                }
                return result;
            }
            throw new ConcurrentModificationException();
        }

        @Override
        public E get(int location) {
            if (modCount == fullList.modCount) {
                if (0 <= location && location < size) {
                    return fullList.get(location + offset);
                }
                throw new IndexOutOfBoundsException();
            }
            throw new ConcurrentModificationException();
        }

        @Override
        public IteratorF<E> iterator() {
            return listIterator(0);
        }

        @Override
        public ListIteratorF<E> listIterator(int location) {
            if (modCount == fullList.modCount) {
                if (0 <= location && location <= size) {
                    return new SubAbstractListIterator<>(fullList
                            .listIterator(location + offset), this, offset,
                            size);
                }
                throw new IndexOutOfBoundsException();
            }
            throw new ConcurrentModificationException();
        }

        @Override
        public E remove(int location) {
            if (modCount == fullList.modCount) {
                if (0 <= location && location < size) {
                    E result = fullList.remove(location + offset);
                    size--;
                    modCount = fullList.modCount;
                    return result;
                }
                throw new IndexOutOfBoundsException();
            }
            throw new ConcurrentModificationException();
        }

        @Override
        protected void removeRange(int start, int end) {
            if (start != end) {
                if (modCount == fullList.modCount) {
                    fullList.removeRange(start + offset, end + offset);
                    size -= end - start;
                    modCount = fullList.modCount;
                } else {
                    throw new ConcurrentModificationException();
                }
            }
        }

        @Override
        public E set(int location, E object) {
            if (modCount == fullList.modCount) {
                if (0 <= location && location < size) {
                    return fullList.set(location + offset, object);
                }
                throw new IndexOutOfBoundsException();
            }
            throw new ConcurrentModificationException();
        }

        @Override
        public int size() {
            return size;
        }

        void sizeChanged(boolean increment) {
            if (increment) {
                size++;
            } else {
                size--;
            }
            modCount = fullList.modCount;
        }
    }

    /**
     * Inserts the specified object into this List at the specified location.
     * The object is inserted before any previous element at the specified
     * location. If the location is equal to the size of this List, the object
     * is added at the end.
     *
     *
     * @param location
     *            the index at which to insert
     * @param object
     *            the object to add
     *
     * @exception UnsupportedOperationException
     *                when adding to this List is not supported
     * @exception ClassCastException
     *                when the class of the object is inappropriate for this
     *                List
     * @exception IllegalArgumentException
     *                when the object cannot be added to this List
     * @exception IndexOutOfBoundsException
     *                when <code>location &lt; 0 || &gt;= size()</code>
     */
    public void add(int location, E object) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean add(E object) {
        add(size(), object);
        return true;
    }

    /**
     * Inserts the objects in the specified Collection at the specified location
     * in this List. The objects are added in the order they are returned from
     * the Collection iterator.
     *
     *
     * @param location
     *            the index at which to insert
     * @param collection
     *            the Collection of objects
     * @return true if this List is modified, false otherwise
     *
     * @exception UnsupportedOperationException
     *                when adding to this List is not supported
     * @exception ClassCastException
     *                when the class of an object is inappropriate for this List
     * @exception IllegalArgumentException
     *                when an object cannot be added to this List
     * @exception IndexOutOfBoundsException
     *                when <code>location &lt; 0 || &gt;= size()</code>
     */
    public boolean addAll(int location, Collection<? extends E> collection) {
        Iterator<? extends E> it = collection.iterator();
        while (it.hasNext()) {
            add(location++, it.next());
        }
        return !collection.isEmpty();
    }


    @Override
    public void clear() {
        removeRange(0, size());
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof List<?>) {
            List<?> list = (List<?>) object;
            if (list.size() != size()) {
                return false;
            }

            Iterator<?> it1 = iterator(), it2 = list.iterator();
            while (it1.hasNext()) {
                Object e1 = it1.next(), e2 = it2.next();
                if (!(e1 == null ? e2 == null : e1.equals(e2))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Answers the element at the specified location in this List.
     *
     *
     * @param location
     *            the index of the element to return
     * @return the element at the specified index
     *
     * @exception IndexOutOfBoundsException
     *                when <code>location &lt; 0 || &gt;= size()</code>
     */
    public abstract E get(int location);


    @Override
    public int hashCode() {
        int result = 1;
        Iterator<?> it = iterator();
        while (it.hasNext()) {
            Object object = it.next();
            result = (31 * result) + (object == null ? 0 : object.hashCode());
        }
        return result;
    }


    public int indexOf(Object object) {
        ListIterator<?> it = listIterator();
        if (object != null) {
            while (it.hasNext()) {
                if (object.equals(it.next())) {
                    return it.previousIndex();
                }
            }
        } else {
            while (it.hasNext()) {
                if (it.next() == null) {
                    return it.previousIndex();
                }
            }
        }
        return -1;
    }

    @Override
    public int indexOfTs(E o) {
        return indexOf(o);
    }

    @Override
    public int indexOfTu(Object o) {
        return indexOf(o);
    }


    @Override
    public IteratorF<E> iterator() {
        return new SimpleListIterator();
    }


    public int lastIndexOf(Object object) {
        ListIterator<?> it = listIterator(size());
        if (object != null) {
            while (it.hasPrevious()) {
                if (object.equals(it.previous())) {
                    return it.nextIndex();
                }
            }
        } else {
            while (it.hasPrevious()) {
                if (it.previous() == null) {
                    return it.nextIndex();
                }
            }
        }
        return -1;
    }


    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    /**
     * Answers a ListIterator on the elements of this List. The elements are
     * iterated in the same order that they occur in the List. The iteration
     * starts at the specified location.
     *
     *
     * @param location
     *            the index at which to start the iteration
     * @return a ListIterator on the elements of this List
     *
     * @exception IndexOutOfBoundsException
     *                when <code>location &lt; 0 || &gt;= size()</code>
     *
     * @see ListIterator
     */
    public ListIterator<E> listIterator(int location) {
        return new FullListIterator(location);
    }

    /**
     * Removes the object at the specified location from this List.
     *
     *
     * @param location
     *            the index of the object to remove
     * @return the removed object
     *
     * @exception UnsupportedOperationException
     *                when removing from this List is not supported
     * @exception IndexOutOfBoundsException
     *                when <code>location &lt; 0 || &gt;= size()</code>
     */
    public E remove(int location) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the objects in the specified range from the start to the, but not
     * including, end index.
     *
     *
     * @param start
     *            the index at which to start removing
     * @param end
     *            the index one past the end of the range to remove
     *
     * @exception UnsupportedOperationException
     *                when removing from this List is not supported
     * @exception IndexOutOfBoundsException
     *                when <code>start &lt; 0</code>
     */
    protected void removeRange(int start, int end) {
        Iterator<?> it = listIterator(start);
        for (int i = start; i < end; i++) {
            it.next();
            it.remove();
        }
    }

    /**
     * Replaces the element at the specified location in this List with the
     * specified object.
     *
     *
     * @param location
     *            the index at which to put the specified object
     * @param object
     *            the object to add
     * @return the previous element at the index
     *
     * @exception UnsupportedOperationException
     *                when replacing elements in this List is not supported
     * @exception ClassCastException
     *                when the class of an object is inappropriate for this List
     * @exception IllegalArgumentException
     *                when an object cannot be added to this List
     * @exception IndexOutOfBoundsException
     *                when <code>location &lt; 0 || &gt;= size()</code>
     */
    public E set(int location, E object) {
        throw new UnsupportedOperationException();
    }


    public ListF<E> subList(int start, int end) {
        if (0 <= start && end <= size()) {
            if (start <= end) {
                if (this instanceof RandomAccess) {
                    return new SubAbstractListRandomAccess<>(this, start, end);
                }
                return new SubAbstractList<>(this, start, end);
            }
            throw new IllegalArgumentException();
        }
        throw new IndexOutOfBoundsException();
    }

} //~
