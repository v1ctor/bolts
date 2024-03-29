package ru.yandex.bolts.collection.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.RandomAccess;

import ru.yandex.bolts.collection.ListF;


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
     *                when <code>location &lt; 0 || &gt;= size()</code>
     */
    @Override
    public E get(int location) {
        if (0 <= location && location < (lastIndex - firstIndex)) {
            return array[firstIndex + location];
        }
        throw new IndexOutOfBoundsException();
    }


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


    @Override
    public boolean isEmpty() {
        return lastIndex == firstIndex;
    }


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


    @Override
    public int size() {
        return lastIndex - firstIndex;
    }


    @Override
    public Object[] toArray() {
        int size = lastIndex - firstIndex;
        Object[] result = new Object[size];
        System.arraycopy(array, firstIndex, result, 0, size);
        return result;
    }


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

    @SuppressWarnings("unchecked")
    protected E[] newElementArray(int size) {
        return (E[]) new Object[size];
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
