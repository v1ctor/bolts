package ru.yandex.bolts.collection.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.CollectionF;
import ru.yandex.bolts.collection.IteratorF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.function.Function1B;

/**
 * @author Stepan Koltsov
 */
public abstract class AbstractSetF<E> extends AbstractCollectionF<E> implements SetF<E> {

    @Override
    protected <B> SetF<B> newMutableCollection() {
        return Cf.hashSet();
    }

    @Override
    protected <B> CollectionF<B> emptyCollection() {
        return Cf.set();
    }

    @Override
    protected <B> CollectionF<B> singletonCollection(B b) {
        return Cf.set(b);
    }

    @Override
    protected <B> SetF<B> collect(IteratorF<B> iterator) {
        return iterator.toSet();
    }

    @Override
    public SetF<E> filter(Function1B<? super E> p) {
        return (SetF<E>) super.filter(p);
    }

    @Override
    public SetF<E> filterNot(Function1B<? super E> p) {
        return (SetF<E>) super.filterNot(p);
    }

    @Override
    public SetF<E> filterNotNull() {
        return filter(Function1B.notNullF());
    }

    @Override
    public <F extends E> SetF<F> filterByType(Class<F> type) {
        return filter(Function1B.instanceOfF(type)).uncheckedCast();
    }

    @SuppressWarnings({"unchecked"})
    public Tuple2<SetF<E>, SetF<E>> partition(Function1B<? super E> p) {
        return (Tuple2<SetF<E>, SetF<E>>) super.partition(p);
    }

    @Override
    public SetF<E> minus1(E e) {
        if (isEmpty()) return this;

        return filter(Function1B.equalsF(e).notF());
    }

    public SetF<E> minus(Set<E> es) {
        if (this.isEmpty() || es.isEmpty()) return this;

        return filter(Cf.x(es).containsF().notF());
    }

    public SetF<E> minus(Collection<E> es) {
        if (this.isEmpty() || es.isEmpty()) return this;

        return minus(Cf.x(es).unique());
    }

    public SetF<E> intersect(final Set<E> b) {
        if (isEmpty()) return this;
        else if (b.isEmpty()) return Cf.x(b);
        return this.filter(Cf.x(b).containsF());
    }

    public abstract IteratorF<E> iterator();

    public SetF<E> plus1(E e) {
        return (SetF<E>) super.plus1(e);
    }

    public SetF<E> plus(Collection<? extends E> elements) {
        return (SetF<E>) super.plus(elements);
    }

    @Override
    public SetF<E> plus(Iterator<? extends E> iterator) {
        return (SetF<E>) super.plus(iterator);
    }

    @SuppressWarnings("unchecked")
    public SetF<E> plus(E... additions) {
        return (SetF<E>) super.plus(additions);
    }

    public SetF<E> unmodifiable() {
        //if (this instanceof Unmodifiable) return this;
        return Cf.x(Collections.unmodifiableSet(this));
    }

    public SetF<E> unique() {
        return this;
    }

    @Override
    public CollectionF<E> stableUnique() {
        return this;
    }

    @Override
    public <F> SetF<F> uncheckedCast() {
        return this.cast();
    }

    @Override
    public <F> SetF<F> cast() {
        return (SetF<F>) super.<F>cast();
    }

    @Override
    public <F> SetF<F> cast(Class<F> type) {
        return (SetF<F>) super.cast(type);
    }

    // Copy paste from AbstractSet from Harmony r532306. Do not add anything below this line, please

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Set<?>) {
            Set<?> s = (Set<?>) object;

            try {
                return size() == s.size() && containsAll(s);
            } catch (ClassCastException cce) {
                return false;
            }
        }
        return false;
    }

    public int hashCode() {
        int result = 0;
        Iterator<?> it = iterator();
        while (it.hasNext()) {
            Object next = it.next();
            result += next == null ? 0 : next.hashCode();
        }
        return result;
    }

    public boolean removeAll(Collection<?> collection) {
        boolean result = false;
        if (size() <= collection.size()) {
            Iterator<?> it = iterator();
            while (it.hasNext()) {
                if (collection.contains(it.next())) {
                    it.remove();
                    result = true;
                }
            }
        } else {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                result = remove(it.next()) || result;
            }
        }
        return result;
    }

} //~
