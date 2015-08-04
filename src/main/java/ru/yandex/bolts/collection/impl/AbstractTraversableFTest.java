package ru.yandex.bolts.collection.impl;

import org.junit.Assert;
import org.junit.Test;

import ru.yandex.bolts.collection.Cf;


public class AbstractTraversableFTest {

    @Test
    public void reduceLeft() {
        Assert.assertEquals(1, Cf.list(1).reduceLeft(Cf.Integer.plusF()).intValue());
    }

    @Test
    public void foldLeft() {
        Assert.assertEquals(2, Cf.list(1).foldLeft(1, Cf.Integer.plusF()).intValue());
    }

} //~
