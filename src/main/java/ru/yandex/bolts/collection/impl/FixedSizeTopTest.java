package ru.yandex.bolts.collection.impl;

import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;

/**
 * @author sankear
 */
public class FixedSizeTopTest extends TestCase {

    private static final Random R = new Random();

    public void test() {
        for (int i = 0; i < 100; ++i) {
            FixedSizeTop<Integer> top = FixedSizeTop.cons(100);
            int size = R.nextInt(10_000);
            ListF<Integer> elements = Cf.arrayList();
            for (int j = 0; j < size; ++j) {
                int val = R.nextInt(100_000);
                top.add(val);
                elements.add(val);
            }
            Assert.assertEquals(elements.sorted().take(100), top.getTopElements());
        }
    }

}
