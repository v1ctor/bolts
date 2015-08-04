package ru.yandex.bolts.collection.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.Tuple3;
import ru.yandex.bolts.collection.Tuple4;


public class TupleExample {

    @Test
    public void simple() {
        // simple example

        assertEquals("zz", Tuple2.tuple("zz", 17).get1());
        assertEquals(17, Tuple2.tuple("zz", 17).get2().intValue());
    }

    public void getNF() {
        // get Nth column as function

        ListF<String> data = Cf.list(
                "Moscow;Russia;10.38e6",
                "New York;USA;8.14e6",
                "Rio de Janeiro;Brazil;6.02e6");

        ListF<String> cities =
                data
                .map(Cf.String.split3F(";")) // split columns
                .map(Tuple3.<String, String, String>get1F()); // get the first column

        assertEquals(Cf.list("Moscow", "New York", "Rio de Janeiro"), cities);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void mapNF() {
        // very advanced topic: explains how getNF works

        ListF<String> data = Cf.list(
                "Moscow;Russia;10.38e6",
                "New York;USA;8.14e6",
                "Rio de Janeiro;Brazil;6.02e6");

        ListF<Tuple3<String, String, Double>> dataParsed =
                data
                .map(Cf.String.split3F(";")) // split columns
                .map(Tuple3.<String, String, String, Double>map3F(Cf.Double.parseF())); // parse the third column

        ListF<Tuple3<String, String, Double>> expected = Cf.list(
                Tuple3.tuple("Moscow", "Russia", 10.38e6),
                Tuple3.tuple("New York", "USA", 8.14e6),
                Tuple3.tuple("Rio de Janeiro", "Brazil", 6.02e6));

        assertEquals(expected, dataParsed);
    }

} //~
