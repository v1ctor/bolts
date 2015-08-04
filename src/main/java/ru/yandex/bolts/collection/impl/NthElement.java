package ru.yandex.bolts.collection.impl;

import java.lang.IndexOutOfBoundsException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Function;

import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.function.Function2V;


public class NthElement {

    private static final Random R = new Random();

    private static <T> void swap(T[] elements, int i, int j) {
        T tmp = elements[i];
        elements[i] = elements[j];
        elements[j] = tmp;
    }

    private static <T> void nth(Function<Integer, T> getter, Function2V<Integer, Integer> swapper,
            Comparator<T> comparator, int leftBorder, int rightBorder,
            int nthPosition)
    {
        while (leftBorder < rightBorder) {
            T separator = getter.apply(leftBorder + R.nextInt(rightBorder - leftBorder + 1));
            int firstGreater = leftBorder;
            int lastLess = rightBorder;
            while (firstGreater <= lastLess) {
                while (firstGreater <= rightBorder && comparator.compare(getter.apply(firstGreater), separator) < 0) {
                    ++firstGreater;
                }
                if (firstGreater > rightBorder) {
                    throw new IllegalStateException("Check that you use correct comparator");
                }
                while (lastLess >= leftBorder && comparator.compare(getter.apply(lastLess), separator) > 0) {
                    --lastLess;
                }
                if (lastLess < leftBorder) {
                    throw new IllegalStateException("Check that you use correct comparator");
                }
                if (firstGreater <= lastLess) {
                    swapper.apply(firstGreater, lastLess);
                    ++firstGreater;
                    --lastLess;
                }
            }
            if (nthPosition <= lastLess) {
                rightBorder = lastLess;
            } else if (nthPosition >= firstGreater) {
                leftBorder = firstGreater;
            } else {
                break;
            }
        }
    }

    public static <T> void inplaceNth(T[] elements, Comparator<T> comparator, int size, int nthPosition) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        if (size > elements.length) {
            throw new IllegalArgumentException("Size can't be more than number of elements");
        }
        if (nthPosition < 0 || nthPosition >= size) {
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", nthPosition, size));
        }
        nth(index -> elements[index], (p1, p2) -> swap(elements, p1, p2), comparator, 0, size - 1,
                nthPosition);
    }

    // List must be mutable
    public static <T> void inplaceNth(ListF<T> elements, Comparator<T> comparator, int nthPosition) {
        if (nthPosition < 0 || nthPosition >= elements.size()) {
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", nthPosition, elements.size()));
        }
        nth(elements::get, (p1, p2) -> Collections.swap(elements, p1, p2), comparator, 0, elements.size() - 1,
                nthPosition);
    }

}
