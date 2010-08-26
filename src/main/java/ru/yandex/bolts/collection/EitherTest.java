package ru.yandex.bolts.collection;

import ru.yandex.bolts.collection.Either.Projection;
import junit.framework.TestCase;

/**
 * @author ssytnik
 */
public class EitherTest extends TestCase {

    public void testToString() {
        Either<Long, String> either;
        Projection<Long, String, ?> projection;

        either = Either.left(123L);
        assertEquals("Either.Left(123)", either.toString());
        projection = either.left();
        assertEquals("Either.LeftProjection(Some(123))", projection.toString());

        either = Either.right("str");
        assertEquals("Either.Right(str)", either.toString());
        projection = either.right();
        assertEquals("Either.RightProjection(Some(str))", projection.toString());

        projection = either.left();
        assertEquals("Either.LeftProjection(None)", projection.toString());
    }

    public void testEquals() {
        assertEquals(Either.right(null), Either.right(null));
        assertFalse(Either.right(null).equals(Either.right(1)));
        assertFalse(Either.right(1).equals(Either.right(null)));
    }

}
