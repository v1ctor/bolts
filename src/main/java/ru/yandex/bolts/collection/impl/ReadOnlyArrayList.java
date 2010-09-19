package ru.yandex.bolts.collection.impl;

import java.util.Arrays;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Unmodifiable;

/**
 *
 * @author Stepan Koltsov
 *
 * @param <E>
 */
public class ReadOnlyArrayList<E> extends ArrayListBase<E> implements Unmodifiable {

    private static final long serialVersionUID = -4843929454311459758L;

    private static final Object[] EMPTY_ARRAY = new Object[0];

    public static final ReadOnlyArrayList<?> EMPTY = new ReadOnlyArrayList<Object>(EMPTY_ARRAY, 0, 0);

    /**
     * Construct this by destroying ArrayListF
     *
     * @param arrayList
     * @see ArrayListF#convertToReadOnly()
     */
    @SuppressWarnings("unchecked")
    ReadOnlyArrayList(ArrayListF<E> arrayList) {
        array = arrayList.array;
        firstIndex = arrayList.firstIndex;
        lastIndex = arrayList.lastIndex;

        arrayList.array = (E[]) EMPTY_ARRAY;
        arrayList.firstIndex = arrayList.lastIndex = 0;
    }

    @SuppressWarnings("unchecked")
    ReadOnlyArrayList(Object[] array, int firstIndex, int lastIndex) {
        super((E[]) array, firstIndex, lastIndex);

        if (firstIndex < 0 || lastIndex < 0)
            throw new IllegalArgumentException();
        if (firstIndex > lastIndex)
            throw new IllegalArgumentException();
        if (lastIndex > array.length)
            throw new IllegalArgumentException();
    }

    ReadOnlyArrayList(Object[] array) {
        this(array, 0, array.length);
    }

    @Override
    public ListF<E> take(int count) {
        if (count < 0)
            throw new IllegalArgumentException();

        if (count == 0)
            return Cf.list();

        if (count >= length())
            return this;

        return new ReadOnlyArrayList<E>(array, firstIndex, firstIndex + count);
    }

    @Override
    public ListF<E> drop(int count) {
        if (count < 0)
            throw new IllegalArgumentException();

        if (count == 0)
            return this;

        if (count >= length())
            return Cf.list();

        return new ReadOnlyArrayList<E>(array, firstIndex + count, lastIndex);
    }

    @Override
    public ListF<E> unmodifiable() {
        return this;
    }

    public static <E> ReadOnlyArrayList<E> valueOf(E[] array) {
        return new ReadOnlyArrayList<E>(Arrays.copyOf(array, array.length), 0, array.length);
    }

    public static <E> ReadOnlyArrayList<E> cons(E e) {
        return new ReadOnlyArrayList<E>(new Object[] { e });
    }

    public static <E> ReadOnlyArrayList<E> cons(E e1, E e2) {
        return new ReadOnlyArrayList<E>(new Object[] { e1, e2 });
    }

} //~
