package ru.yandex.bolts.collection.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.RandomAccess;

import ru.yandex.bolts.collection.ListF;

/**
 * Copied-pasted from Harmony ArrayList r679987 and then refactored.
 * 
 * @author Stepan Koltsov
 */
public class ArrayListF<E> extends ArrayListBase<E>
        implements ListF<E>, Cloneable, Serializable, RandomAccess
{
    private static final long serialVersionUID = 8683452581122892189L;

    /**
     * Constructs a new instance of ArrayList with capacity for ten elements.
     */
    public ArrayListF() {
        this(10);
    }

    /**
     * Constructs a new instance of ArrayList with the specified capacity.
     * 
     * @param capacity
     *            the initial capacity of this ArrayList
     */
    public ArrayListF(int capacity) {
        firstIndex = lastIndex = 0;
        try {
            array = newElementArray(capacity);
        } catch (NegativeArraySizeException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Constructs a new instance of ArrayList containing the elements in the
     * specified collection. The ArrayList will have an initial capacity which
     * is 110% of the size of the collection. The order of the elements in this
     * ArrayList is the order they are returned by the collection iterator.
     * 
     * @param collection
     *            the collection of elements to add
     */
    public ArrayListF(Collection<? extends E> collection) {
        int size = collection.size();
        firstIndex = 0;
        array = newElementArray(size + (size / 10));
        collection.toArray(array);
        lastIndex = size;
        modCount = 1;
    }

    @SuppressWarnings("unchecked")
    private E[] newElementArray(int size) {
        return (E[]) new Object[size];
    }
    
    /**
     * Return readonly array list with data of this. This is cleared.
     */
    public ReadOnlyArrayList<E> convertToReadOnly() {
        return new ReadOnlyArrayList<E>(this);
    }

    /**
     * Inserts the specified object into this ArrayList at the specified
     * location. The object is inserted before any previous element at the
     * specified location. If the location is equal to the size of this
     * ArrayList, the object is added at the end.
     * 
     * @param location
     *            the index at which to insert
     * @param object
     *            the object to add
     * 
     * @exception IndexOutOfBoundsException
     *                when <code>location < 0 || >= size()</code>
     */
    @Override
    public void add(int location, E object) {
        int size = lastIndex - firstIndex;
        if (0 < location && location < size) {
            if (firstIndex == 0 && lastIndex == array.length) {
                growForInsert(location, 1);
            } else if ((location < size / 2 && firstIndex > 0)
                    || lastIndex == array.length) {
                System.arraycopy(array, firstIndex, array, --firstIndex,
                        location);
            } else {
                int index = location + firstIndex;
                System.arraycopy(array, index, array, index + 1, size
                        - location);
                lastIndex++;
            }
            array[location + firstIndex] = object;
        } else if (location == 0) {
            if (firstIndex == 0) {
                growAtFront(1);
            }
            array[--firstIndex] = object;
        } else if (location == size) {
            if (lastIndex == array.length) {
                growAtEnd(1);
            }
            array[lastIndex++] = object;
        } else {
            throw new IndexOutOfBoundsException();
        }

        modCount++;
    }

    /**
     * Adds the specified object at the end of this ArrayList.
     * 
     * @param object
     *            the object to add
     * @return true
     */
    @Override
    public boolean add(E object) {
        if (lastIndex == array.length) {
            growAtEnd(1);
        }
        array[lastIndex++] = object;
        modCount++;
        return true;
    }

    /**
     * Inserts the objects in the specified Collection at the specified location
     * in this ArrayList. The objects are added in the order they are returned
     * from the Collection iterator.
     * 
     * @param location
     *            the index at which to insert
     * @param collection
     *            the Collection of objects
     * @return true if this ArrayList is modified, false otherwise
     * 
     * @exception IndexOutOfBoundsException
     *                when <code>location < 0 || > size()</code>
     */
    @Override
    public boolean addAll(int location, Collection<? extends E> collection) {
        int size = lastIndex - firstIndex;
        if (location < 0 || location > size) {
            throw new IndexOutOfBoundsException();
        }
        int growSize = collection.size();
        if (0 < location && location < size) {
            if (array.length - size < growSize) {
                growForInsert(location, growSize);
            } else if ((location < size / 2 && firstIndex > 0)
                    || lastIndex > array.length - growSize) {
                int newFirst = firstIndex - growSize;
                if (newFirst < 0) {
                    int index = location + firstIndex;
                    System.arraycopy(array, index, array, index - newFirst,
                            size - location);
                    lastIndex -= newFirst;
                    newFirst = 0;
                }
                System.arraycopy(array, firstIndex, array, newFirst, location);
                firstIndex = newFirst;
            } else {
                int index = location + firstIndex;
                System.arraycopy(array, index, array, index + growSize, size
                        - location);
                lastIndex += growSize;
            }
        } else if (location == 0) {
            growAtFront(growSize);
            firstIndex -= growSize;
        } else if (location == size) {
            if (lastIndex > array.length - growSize) {
                growAtEnd(growSize);
            }
            lastIndex += growSize;
        }

        if (growSize > 0) {
            Object[] dumparray = new Object[growSize];
            collection.toArray(dumparray);
            System.arraycopy(dumparray, 0, this.array, location + firstIndex,
                    growSize);
            modCount++;
            return true;
        }
        return false;
    }

    /**
     * Adds the objects in the specified Collection to this ArrayList.
     * 
     * @param collection
     *            the Collection of objects
     * @return true if this ArrayList is modified, false otherwise
     */
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        Object[] dumpArray = collection.toArray();
        if (dumpArray.length == 0) {
            return false;
        }
        if (dumpArray.length > array.length - lastIndex) {
            growAtEnd(dumpArray.length);
        }
        System.arraycopy(dumpArray, 0, this.array, lastIndex, dumpArray.length);
        lastIndex += dumpArray.length;
        modCount++;
        return true;
    }

    /**
     * Removes all elements from this ArrayList, leaving it empty.
     * 
     * @see #isEmpty
     * @see #size
     */
    @Override
    public void clear() {
        if (firstIndex != lastIndex) {
            Arrays.fill(array, firstIndex, lastIndex, null);
            firstIndex = lastIndex = 0;
            modCount++;
        }
    }

    /**
     * Answers a new ArrayList with the same elements, size and capacity as this
     * ArrayList.
     * 
     * @return a shallow copy of this ArrayList
     * 
     * @see java.lang.Cloneable
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            ArrayListF<E> newList = (ArrayListF<E>) super.clone();
            newList.array = array.clone();
            return newList;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }


    /**
     * Ensures that this ArrayList can hold the specified number of elements
     * without growing.
     * 
     * @param minimumCapacity
     *            the minimum number of elements that this ArrayList will hold
     *            before growing
     */
    public void ensureCapacity(int minimumCapacity) {
        if (array.length < minimumCapacity) {
            if (firstIndex > 0) {
                growAtFront(minimumCapacity - array.length);
            } else {
                growAtEnd(minimumCapacity - array.length);
            }
        }
    }

    private void growAtEnd(int required) {
        int size = lastIndex - firstIndex;
        if (firstIndex >= required - (array.length - lastIndex)) {
            int newLast = lastIndex - firstIndex;
            if (size > 0) {
                System.arraycopy(array, firstIndex, array, 0, size);
                int start = newLast < firstIndex ? firstIndex : newLast;
                Arrays.fill(array, start, array.length, null);
            }
            firstIndex = 0;
            lastIndex = newLast;
        } else {
            int increment = size / 2;
            if (required > increment) {
                increment = required;
            }
            if (increment < 12) {
                increment = 12;
            }
            E[] newArray = newElementArray(size + increment);
            if (size > 0) {
                System.arraycopy(array, firstIndex, newArray, 0, size);
                firstIndex = 0;
                lastIndex = size;
            }
            array = newArray;
        }
    }

    private void growAtFront(int required) {
        int size = lastIndex - firstIndex;
        if (array.length - lastIndex + firstIndex >= required) {
            int newFirst = array.length - size;
            if (size > 0) {
                System.arraycopy(array, firstIndex, array, newFirst, size);
                int length = firstIndex + size > newFirst ? newFirst
                        : firstIndex + size;
                Arrays.fill(array, firstIndex, length, null);
            }
            firstIndex = newFirst;
            lastIndex = array.length;
        } else {
            int increment = size / 2;
            if (required > increment) {
                increment = required;
            }
            if (increment < 12) {
                increment = 12;
            }
            E[] newArray = newElementArray(size + increment);
            if (size > 0) {
                System.arraycopy(array, firstIndex, newArray, newArray.length
                        - size, size);
            }
            firstIndex = newArray.length - size;
            lastIndex = newArray.length;
            array = newArray;
        }
    }

    private void growForInsert(int location, int required) {
        int size = lastIndex - firstIndex;
        int increment = size / 2;
        if (required > increment) {
            increment = required;
        }
        if (increment < 12) {
            increment = 12;
        }
        E[] newArray = newElementArray(size + increment);
        int newFirst = increment - required;
        // Copy elements after location to the new array skipping inserted
        // elements
        System.arraycopy(array, location + firstIndex, newArray, newFirst
                + location + required, size - location);
        // Copy elements before location to the new array from firstIndex
        System.arraycopy(array, firstIndex, newArray, newFirst, location);
        firstIndex = newFirst;
        lastIndex = size + increment;

        array = newArray;
    }


    /**
     * Removes the object at the specified location from this ArrayList.
     * 
     * @param location
     *            the index of the object to remove
     * @return the removed object
     * 
     * @exception IndexOutOfBoundsException
     *                when <code>location < 0 || >= size()</code>
     */
    @Override
    public E remove(int location) {
        E result;
        int size = lastIndex - firstIndex;
        if (0 <= location && location < size) {
            if (location == size - 1) {
                result = array[--lastIndex];
                array[lastIndex] = null;
            } else if (location == 0) {
                result = array[firstIndex];
                array[firstIndex++] = null;
            } else {
                int elementIndex = firstIndex + location;
                result = array[elementIndex];
                if (location < size / 2) {
                    System.arraycopy(array, firstIndex, array, firstIndex + 1,
                            location);
                    array[firstIndex++] = null;
                } else {
                    System.arraycopy(array, elementIndex + 1, array,
                            elementIndex, size - location - 1);
                    array[--lastIndex] = null;
                }
            }
            if (firstIndex == lastIndex) {
                firstIndex = lastIndex = 0;
            }
        } else {
            throw new IndexOutOfBoundsException();
        }

        modCount++;
        return result;
    }

    /**
     * Removes the first one of the specified object in this list, if present.
     * 
     * @param object
     *            the object to removes
     * @return true if the list contains the object
     * @see java.util.AbstractCollection#remove(java.lang.Object)
     */
    @Override
    public boolean remove(Object object) {
        int location = indexOf(object);
        if (location >= 0) {
            remove(location);
            return true;
        }
        return false;
    }

    /**
     * Removes the objects in the specified range from the start to the end, but
     * not including the end index.
     * 
     * @param start
     *            the index at which to start removing
     * @param end
     *            the index one past the end of the range to remove
     * 
     * @exception IndexOutOfBoundsException
     *                when <code>start < 0, start > end</code> or
     *                <code>end > size()</code>
     */
    @Override
    protected void removeRange(int start, int end) {
        if (start >= 0 && start <= end && end <= (lastIndex - firstIndex)) {
            if (start == end) {
                return;
            }
            int size = lastIndex - firstIndex;
            if (end == size) {
                Arrays.fill(array, firstIndex + start, lastIndex, null);
                lastIndex = firstIndex + start;
            } else if (start == 0) {
                Arrays.fill(array, firstIndex, firstIndex + end, null);
                firstIndex += end;
            } else {
                System.arraycopy(array, firstIndex + end, array, firstIndex
                        + start, size - end);
                int newLast = lastIndex + start - end;
                Arrays.fill(array, newLast, lastIndex, null);
                lastIndex = newLast;
            }
            modCount++;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Replaces the element at the specified location in this ArrayList with the
     * specified object.
     * 
     * @param location
     *            the index at which to put the specified object
     * @param object
     *            the object to add
     * @return the previous element at the index
     * 
     * @exception IndexOutOfBoundsException
     *                when <code>location < 0 || >= size()</code>
     */
    @Override
    public E set(int location, E object) {
        if (0 <= location && location < (lastIndex - firstIndex)) {
            E result = array[firstIndex + location];
            array[firstIndex + location] = object;
            return result;
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Sets the capacity of this ArrayList to be the same as the size.
     * 
     * @see #size
     */
    public void trimToSize() {
        int size = lastIndex - firstIndex;
        E[] newArray = newElementArray(size);
        System.arraycopy(array, firstIndex, newArray, 0, size);
        array = newArray;
        firstIndex = 0;
        lastIndex = array.length;
        modCount = 0;
    }

    private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField(
            "size", Integer.TYPE) }; //$NON-NLS-1$

    private void writeObject(ObjectOutputStream stream) throws IOException {
        ObjectOutputStream.PutField fields = stream.putFields();
        fields.put("size", lastIndex - firstIndex); //$NON-NLS-1$
        stream.writeFields();
        stream.writeInt(array.length);
        Iterator<?> it = iterator();
        while (it.hasNext()) {
            stream.writeObject(it.next());
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream stream) throws IOException,
            ClassNotFoundException {
        ObjectInputStream.GetField fields = stream.readFields();
        lastIndex = fields.get("size", 0); //$NON-NLS-1$
        array = newElementArray(stream.readInt());
        for (int i = 0; i < lastIndex; i++) {
            array[i] = (E) stream.readObject();
        }
    }
} //~
