package ru.yandex.bolts.methodFunction;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;

/**
 * @author Stepan Koltsov
 */
public class FunctionBuilderTest {

    @Test
    public void classExample() {
        ListF<File> list = Cf.list(new File("aa"), new File("bb"));

        ListF<String> r = list.mapW(Cf.p(File.class).getName());

        Assert.assertEquals(Cf.list("aa", "bb"), r);
    }

    protected static class Adder {
        private final int a;

        public Adder(int a) {
            this.a = a;
        }

        public int add(int b) {
            return a + b;
        }
    }

    @Test
    public void instanceExample() {
        Adder a = new Adder(3);
        ListF<Integer> list = Cf.list(5, 6, 8);

        ListF<Integer> r = list.mapW(Cf.p(a).add(0));

        Assert.assertEquals(Cf.list(8, 9, 11), r);
    }

    @Test
    public void saveParameterExample() {
        ListF<Adder> list = Cf.list(new Adder(2), new Adder(4));

        ListF<Integer> r = list.mapW(Cf.p(Adder.class).add(3));

        Assert.assertEquals(Cf.list(5, 7), r);
    }

} //~
