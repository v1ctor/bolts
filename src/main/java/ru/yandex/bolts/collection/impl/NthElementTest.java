package ru.yandex.bolts.collection.impl;

import java.util.Comparator;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.function.Function;

/**
 * @author sankear
 */
public class NthElementTest extends TestCase {

    private static final Random R = new Random();

    private void test(Function<Integer, Function<Integer, Integer>> generateAndGetGetterF, int tests, int size) {
        for (int i = 0; i < tests; ++i) {
            int pos = R.nextInt(size);
            Function<Integer, Integer> getter = generateAndGetGetterF.apply(pos);
            Assert.assertEquals(pos, getter.apply(pos).intValue());
            SetF<Integer> vals = Cf.hashSet();
            for (int j = 0; j < size; ++j) {
                int val = getter.apply(j);
                Assert.assertTrue(val >= 0 && val < size);
                Assert.assertTrue(vals.add(val));
            }
            for (int j = 0; j < pos; ++j) {
                Assert.assertTrue(getter.apply(j) < pos);
            }
            for (int j = pos + 1; j < size; ++j) {
                Assert.assertTrue(getter.apply(j) > pos);
            }
        }
    }

    public void testArray() {
        test(pos -> {
            Integer[] arr = new Integer[100];
            for (int i = 0; i < 100; ++i) {
                arr[i] = i;
            }
            NthElement.inplaceNth(arr, Comparator.<Integer>naturalOrder(), 100, pos);
            return p -> arr[p];
        }, 100, 100);
    }

    public void testList() {
        test(pos -> {
            ListF<Integer> elements = Cf.arrayList(Cf.range(0, 100).shuffle());
            NthElement.inplaceNth(elements, Comparator.<Integer>naturalOrder(), pos);
            return elements::get;
        }, 100, 100);
    }

}
