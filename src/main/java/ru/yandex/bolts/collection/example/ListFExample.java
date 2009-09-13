package ru.yandex.bolts.collection.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.misc.IntegerF;
import ru.yandex.bolts.function.misc.StringF;

/**
 * Examples of {@link ListF}.
 * 
 * @author Stepan Koltsov
 */
public class ListFExample {
    
    @Test
    public void firstLast() {
        // "first" method returns first element of the list
        assertEquals(1, Cf.list(1, 2, 3).first().intValue());
        
        // "last" method returns last element of the list
        assertEquals("world", Cf.list("hello", "world").last());
        
        // "first" method throws on empty list
        try {
            Cf.list().first();
            fail();
        } catch (Exception e) {
            // OK
        }
        
        // "firstO" method returns None on empty list
        assertEquals(Option.none(), Cf.<Long>list().firstO());
    }
    
    @Test
    public void unique() {
        // "unique" method returns unique elements as Set
        assertEquals(Cf.set(1, 2, 3), Cf.list(1, 2, 3, 2, 1, 3).unique());
    }
    
    @Test
    public void takeDrop() {
        // "take" first elements
        assertEquals(Cf.list(1, 2), Cf.list(1, 2, 3, 4).take(2));
        
        // it is OK to take more elements then collection contains
        assertEquals(Cf.list(1, 2), Cf.list(1, 2).take(10));
        
        // "drop" first elements
        assertEquals(Cf.list(4, 5), Cf.list(1, 2, 3, 4, 5).drop(3));
    }
    
    @Test
    public void reverse() {
        assertEquals(Cf.list(4, 3, 2, 1), Cf.list(1, 2, 3, 4).reverse());
    }
    
    @Test
    public void plus() {
        // bolts allows easy collection concatenation:
        
        Option<Integer> some = Option.some(6);
        Option<Integer> none = Option.<Integer>none();
        ListF<Integer> extraList = Cf.list(3, 4);
        
        ListF<Integer> list = Cf.list(1, 2).plus(extraList).plus1(5).plus(some).plus(none);
        assertEquals(Cf.list(1, 2, 3, 4, 5, 6, 7), list);
    }
    
    @SuppressWarnings("unused")
    @Test
    public void uncheckedCast() {
        ListF<ListF<String>> list = Cf.list(Cf.list("a"), Cf.list("b", "c"));
        
        // unchecked cast is dangerous operation, but it is
        // is sometimes required to avoid typing lots of code like ListF<? extends ListF<? extends CharSequence>>
        ListF<? extends ListF<? extends CharSequence>> properList = list;
        ListF<ListF<CharSequence>> cs = list.uncheckedCast();
        
        // regular cast to ListF<ListF<CharSequence>> is prohibited by JLS
        //ListF<ListF<CharSequence>> prohibited = (ListF<ListF<CharSequence>>) list.uncheckedCast();
    }
    
    /// functional operations
    
    @Test
    public void reduceFold() {
        // sum of elements
        assertEquals(10, Cf.list(1, 2, 3, 4).foldLeft(0, IntegerF.plusF()).intValue());
        
        // maximum element
        assertEquals(4, Cf.list(1, 4, 3, 2).reduceLeft(IntegerF.maxF()).intValue());
        // actually, ListF has max() method that is implemented using foldLeft
        assertEquals(4, Cf.list(1, 4, 3, 2).max().intValue());
        
        // fold unlike reduce works with empty collections
        assertEquals(0, Cf.<Integer>list().foldLeft(0, IntegerF.plusF()).intValue());
    }
    
    @Test
    public void map() {
        // apply function to all elements
        assertEquals(Cf.list(1, 2, 4), Cf.list("1", "2", "4").map(IntegerF.parseF()));
    }
    
    @Test
    public void filter() {
        // take only positive elements
        assertEquals(Cf.list(1, 2), Cf.list(0, 1, -3, 2, 0).filter(IntegerF.naturalComparator().gtF(0)));
    }
    
    @Test
    public void flatMap() {
        ListF<String> words = Cf.list("a,b", "c", "d,e,f").flatMap(StringF.splitF(","));
        assertEquals(Cf.list("a", "b", "c", "d", "e", "f"), words);
    }
    
    @Test
    public void forAllExists() {
        // predicate that tests that string length is greater or equal to 2
        Function1B<String> lengthGe2 = StringF.lengthF().andThen(IntegerF.naturalComparator().geF(2));
        
        // All strings has length => 2?
        assertTrue(Cf.list("aaa", "bb", "cc").forAll(lengthGe2));
        assertFalse(Cf.list("a", "bb", "cc").forAll(lengthGe2));
        
        // Has any string with length >= 2?
        assertTrue(Cf.list("aaa", "b", "c").exists(lengthGe2));
        assertFalse(Cf.list("a", "b", "c").exists(lengthGe2));
    }
    
} //~
