package ru.yandex.bolts.function.forhuman;

import junit.framework.TestCase;

import ru.yandex.bolts.function.Function0;

/**
 * @author Stepan Koltsov
 */
@SuppressWarnings({"UnusedDeclaration", "unused"})
public class FactoryTest extends TestCase {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FactoryTest.class);

    public void testConst() {
        assertEquals("a", Factory.constF("a").apply());
    }

    public void testAsMapper() {
        Factory<String> f = Factory.constF("a");
        assertEquals("a", f.asMapper().map(null));
        f.asMapper().toString();
    }

    public void testAndThen() {
        Factory<String> f = Factory.constF(1).andThen(Mapper.<Integer>toStringM());
        assertEquals("1", f.apply());
        f.toString();
    }

    public void testWrap() {
        Factory<Integer> f = Factory.wrap(new Function0<Integer>() {
            public Integer apply() {
                return 16;
            }
        });
        assertEquals(16, (int) f.apply());
        f.toString();
    }
} //~
