package ru.yandex.bolts.type;

import org.junit.Assert;
import org.junit.Test;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;

/**
 * @author ilyak
 */
public class StringTypeTest {

    @Test
    public void blank() {
        Assert.assertTrue(Cf.String.isBlank(null));
        Assert.assertTrue(Cf.String.isBlank(""));
        Assert.assertTrue(Cf.String.isBlank(" "));
        Assert.assertTrue(Cf.String.isBlank("\t \n"));
        Assert.assertFalse(Cf.String.isBlank("\t w\n"));
        Assert.assertFalse(Cf.String.isBlank(" |\n"));
        Assert.assertFalse(Cf.String.isBlank("1234"));

        Assert.assertEquals(Cf.list("w"),
                Cf.list("", " ", "w", "\n").filter(Cf.String.isNotBlankF()));
        Assert.assertEquals(Cf.list("", " ", "\n"),
                Cf.list("", " ", "w", "\n").filter(Cf.String.isBlankF()));
    }

} //~
