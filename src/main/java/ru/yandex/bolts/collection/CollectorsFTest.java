package ru.yandex.bolts.collection;

import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Stepan Koltsov
 */
public class CollectorsFTest {

    @Test
    public void toList() {
        ListF<Integer> list = Stream.of(10, 20, 30).collect(CollectorsF.toList());
        Assert.assertEquals(Cf.list(10, 20, 30), list);
        try {
            list.add(40);
            Assert.fail("must be immutable");
        } catch (Exception e) {
            // expected
        }
    }

}
