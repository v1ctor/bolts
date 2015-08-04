package ru.yandex.bolts.collection.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.MapF;
import ru.yandex.bolts.function.Function1B;


public class MapFExample {

    @Test
    public void getOrElse() {
        MapF<String, Integer> map = Cf.map("a", 1, "b", 2);

        // works like "get" when map has specified key
        assertEquals(1, map.getOrElse("a", 44).intValue());

        // otherwise uses default value
        assertEquals(33, map.getOrElse("z", 33).intValue());
    }

    @Test
    public void getOrElseUpdate() {
        MapF<String, Integer> map = Cf.hashMap();

        // initialize value by key "a"
        assertEquals(1, map.getOrElseUpdate("a", 1).intValue());
        // works like "get", because map contains requested key
        assertEquals(1, map.getOrElseUpdate("a", 2).intValue());
    }

    @Test
    public void plus() {
        // plus can be used to union maps
        MapF<String, Integer> orig = Cf.hashMap();
        assertEquals(Cf.map("a", 1, "b", 2), orig.plus1("a", 1).plus(Cf.map("b", 2)));

        // original collection is still empty, because "plus" operation is functional
        assertTrue(orig.isEmpty());
    }

    @Test
    public void filterKeys() {
        Function1B<String> goodKeyPredicate = Function1B.equalsF("b").notF();
        // map keys may be filtered
        assertEquals(Cf.map("a", 1), Cf.map("a", 1, "b", 2).filterKeys(goodKeyPredicate));
    }

    @Test
    public void mapValues() {
        // mapValues applies function to each value of map
        assertEquals(Cf.map("a", 1, "b", 2), Cf.map("a", "1", "b", "2").mapValues(Cf.Integer.parseF()));
    }

} //~
