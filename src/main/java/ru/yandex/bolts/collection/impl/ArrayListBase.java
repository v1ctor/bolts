package ru.yandex.bolts.collection.impl;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.RandomAccess;

import ru.yandex.bolts.collection.ListF;

/**
 * Base for {@link ArrayListF} and {@link ReadOnlyArrayList} implementations.
 *  
 * @author Stepan Koltsov
 */
public abstract class ArrayListBase<E> extends AbstractListF<E> implements ListF<E>, Serializable, RandomAccess {

    private static final long serialVersionUID = 3227478780818417896L;
    
    protected transient E[] array;
    
    protected transient int firstIndex;

    protected transient int lastIndex;

    protected ArrayListBase() {
    }

    protected ArrayListBase(E[] array, int firstIndex, int lastIndex) {
        this.array = array;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
    }

    /**
     * Searches this ArrayList for the specified object.
     * 
     * @param object
     *            the object to search for
     * @return true if <code>object</code> is an element of this ArrayList,
     *         false otherwise
     */
    @Override
    public boolean contains(Object object) {
        if (object != null) {
            for (int i = firstIndex; i < lastIndex; i++) {
                if (object.equals(array[i])) {
                    return true;
                }
            }
        } else {
            for (int i = firstIndex; i < lastIndex; i++) {
                if (array[i] == null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Answers the element at the specified location in this ArrayList.
     * 
     * @param location
     *            the index of the element to return
     * @return the element at the specified index
     * 
     * @exception IndexOutOfBoundsException
     *                when <code>location < 0 || >= size()</code>
     */
    @Override
    public E get(int location) {
        if (0 <= location && location < (lastIndex - firstIndex)) {
            return array[firstIndex + location];
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Searches this ArrayList for the specified object and returns the index of
     * the first occurrence.
     * 
     * @param object
     *            the object to search for
     * @return the index of the first occurrence of the object
     */
    @Override
    public int indexOf(Object object) {
        if (object != null) {
            for (int i = firstIndex; i < lastIndex; i++) {
                if (object.equals(array[i])) {
                    return i - firstIndex;
                }
            }
        } else {
            for (int i = firstIndex; i < lastIndex; i++) {
                if (array[i] == null) {
                    return i - firstIndex;
                }
            }
        }
        return -1;
    }

    /**
     * Answers if this ArrayList has no elements, a size of zero.
     * 
     * @return true if this ArrayList has no elements, false otherwise
     * 
     * @see #size
     */
    @Override
    public boolean isEmpty() {
        return lastIndex == firstIndex;
    }

    /**
     * Searches this ArrayList for the specified object and returns the index of
     * the last occurrence.
     * 
     * @param object
     *            the object to search for
     * @return the index of the last occurrence of the object
     */
    @Override
    public int lastIndexOf(Object object) {
        if (object != null) {
            for (int i = lastIndex - 1; i >= firstIndex; i--) {
                if (object.equals(array[i])) {
                    return i - firstIndex;
                }
            }
        } else {
            for (int i = lastIndex - 1; i >= firstIndex; i--) {
                if (array[i] == null) {
                    return i - firstIndex;
                }
            }
        }
        return -1;
    }

    /**
     * Answers the number of elements in this ArrayList.
     * 
     * @return the number of elements in this ArrayList
     */
    @Override
    public int size() {
        return lastIndex - firstIndex;
    }

    /**
     * Answers a new array containing all elements contained in this ArrayList.
     * 
     * @return an array of the elements from this ArrayList
     */
    @Override
    public Object[] toArray() {
        int size = lastIndex - firstIndex;
        Object[] result = new Object[size];
        System.arraycopy(array, firstIndex, result, 0, size);
        return result;
    }

    /**
     * Answers an array containing all elements contained in this ArrayList. If
     * the specified array is large enough to hold the elements, the specified
     * array is used, otherwise an array of the same type is created. If the
     * specified array is used and is larger than this ArrayList, the array
     * element following the collection elements is set to null.
     * 
     * @param contents
     *            the array
     * @return an array of the elements from this ArrayList
     * 
     * @exception ArrayStoreException
     *                when the type of an element in this ArrayList cannot be
     *                stored in the type of the specified array
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] contents) {
        int size = lastIndex - firstIndex;
        if (size > contents.length) {
            Class<?> ct = contents.getClass().getComponentType();
            contents = (T[]) Array.newInstance(ct, size);
        }
        System.arraycopy(array, firstIndex, contents, 0, size);
        if (size < contents.length) {
            contents[size] = null;
        }
        return contents;
    }

} //~
