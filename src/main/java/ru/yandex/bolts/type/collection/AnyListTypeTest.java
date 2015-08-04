package ru.yandex.bolts.type.collection;

import org.junit.Assert;
import org.junit.Test;

import ru.yandex.bolts.collection.Cf;


public class AnyListTypeTest {

    @SuppressWarnings("unchecked")
    @Test
    public void concat() {
        Assert.assertEquals(Cf.list(), Cf.List.concat(Cf.list()));
        Assert.assertEquals(Cf.list(), Cf.List.concat(Cf.list(Cf.list(), Cf.list())));
        Assert.assertEquals(Cf.list(1, 2, 3, 4, 5, 6), Cf.List.concat(Cf.list(Cf.list(1), Cf.list(2, 3), Cf.list(4, 5, 6))));
    }

} //~
