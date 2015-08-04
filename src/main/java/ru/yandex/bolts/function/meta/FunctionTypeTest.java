package ru.yandex.bolts.function.meta;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.meta.FunctionType.ReturnType;


public class FunctionTypeTest {
    @Test
    public void testParse() {
        assertEquals(Option.none(), FunctionType.parseSimpleClassName("Function1"));
        assertEquals(Option.none(), FunctionType.parseSimpleClassName("Functio1B"));
        assertEquals(new FunctionType(1, ReturnType.OBJECT), FunctionType.parseSimpleClassName("Function").get());
        assertEquals(new FunctionType(3, ReturnType.BOOLEAN), FunctionType.parseSimpleClassName("Function3B").get());
        assertEquals(new FunctionType(0, ReturnType.OBJECT), FunctionType.parseSimpleClassName("Function0").get());
    }
} //~
