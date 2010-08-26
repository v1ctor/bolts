package ru.yandex.bolts.collection;

import java.util.NoSuchElementException;

import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;

/**
 * Either something or something else
 *
 * @author Stepan Koltsov
 *
 * @see fj.data.Either
 */
public class Either<A, B> {
    private Either() { }

    public A getLeft() {
        throw new NoSuchElementException("Either.Right.left");
    }

    public B getRight() {
        throw new NoSuchElementException("Either.Left.right");
    }

    /** Left projection */
    public LeftProjection<A, B> left() { return new LeftProjection<A, B>(this); }
    /** Right projection */
    public RightProjection<A, B> right() { return new RightProjection<A, B>(this); }

    /** Is this object left? */
    public boolean isLeft() { return this instanceof Left<?, ?>; }
    /** Is this object right? */
    public boolean isRight() { return this instanceof Right<?, ?>; }

    /** Some if this is left and none otherwise */
    public Option<A> leftO() {
        return isLeft() ? Option.<A>some(getLeft()) : Option.<A>none();
    }

    /** Some if this is right and none otherwise */
    public Option<B> rightO() {
        return isRight() ? Option.<B>some(getRight()) : Option.<B>none();
    }

    /** Convert left to right and vice versa */
    public Either<B, A> swap() {
        if (isLeft()) return Either.<B, A>right(getLeft());
        else return Either.<B, A>left(getRight());
    }

    /** This with different type parameters and no type checks */
    @SuppressWarnings("unchecked")
    public <C, D> Either<C, D> uncheckedCast() {
        return (Either<C, D>) this;
    }

    private static class Left<A, B> extends Either<A, B> {
        private final A value;

        private Left(A value) {
            this.value = value;
        }

        @Override
        public A getLeft() {
            return value;
        }

        @Override
        public String toString() {
            return "Either.Left(" + value + ")";
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Left
                    && (((Left<A, B>) obj).value == value
                        || ((Left<A, B>) obj).value.equals(value));
        }
    }

    private static class Right<A, B> extends Either<A, B> {
        private final B value;

        private Right(B value) {
            this.value = value;
        }

        @Override
        public B getRight() {
            return value;
        }

        @Override
        public String toString() {
            return "Either.Right(" + value + ")";
        }


        @Override
        public boolean equals(Object obj) {
            return obj instanceof Right
                    && (((Right<A, B>) obj).value == value
                        || ((Right<A, B>) obj).value.equals(value));
        }
    }

    /** Base class for left and right projection */
    public abstract static class Projection<A, B, R> {
        protected final Either<A, B> either;

        private Projection(Either<A, B> either) {
            this.either = either;
        }

        public boolean isLeft() { return either.isLeft(); }
        public boolean isRight() { return either.isRight(); }
        public boolean isDefined() { return getO().isDefined(); }

        public abstract R get();
        public abstract Option<R> getO();
        public R getOrElse(R elseValue) { return getO().getOrElse(elseValue); }
        public R getOrElse(Function0<R> elseValue) { return getO().getOrElse(elseValue); }

        protected abstract <C> Object /** Either<?, ?> */ map(Function<R, C> f);
        // flatMap
        public Option<Either<A, B>> filter(Function1B<R> p) {
            if (isDefined() && p.apply(get())) return Option.some(either);
            else return Option.none();
        }

        public void forEach(Function1V<R> f) { getO().forEach(f); }
        public boolean forAll(Function1B<R> p) { return getO().forAll(p); }
        public boolean exists(Function1B<R> p) { return getO().exists(p); }
    }

    public static class LeftProjection<A, B> extends Projection<A, B, A> {
        private LeftProjection(Either<A, B> either) { super(either); }

        public A get() { return either.getLeft(); }
        public Option<A> getO() { return either.leftO(); }

        public <C> Either<C, B> map(Function<A, C> f) {
            if (isLeft()) return Either.left(f.apply(get()));
            else return either.uncheckedCast();
        }

        @Override
        public String toString() {
            return "Either.LeftProjection(" + getO() + ")";
        }
    }

    public static class RightProjection<A, B> extends Projection<A, B, B> {
        private RightProjection(Either<A, B> either) { super(either); }

        public B get() { return either.getRight(); }
        public Option<B> getO() { return either.rightO(); }

        public <C> Either<A, C> map(Function<B, C> f) {
            if (isRight()) return Either.right(f.apply(get()));
            else return either.uncheckedCast();
        }

        @Override
        public String toString() {
            return "Either.RightProjection(" + getO() + ")";
        }

    }

    public static <A, B> Either<A, B> left(A a) { return new Left<A, B>(a); }
    public static <A, B> Either<A, B> right(B b) { return new Right<A, B>(b); }

    public static <A, B> Function<A, Either<A, B>> leftF() {
        return new Function<A, Either<A, B>>() {
            public Either<A, B> apply(A a) {
                return left(a);
            }
        };
    }

    public static <A, B> Function<B, Either<A, B>> rightF() {
        return new Function<B, Either<A, B>>() {
            public Either<A, B> apply(B b) {
                return right(b);
            }
        };
    }

    /** Execution function and return left with value or right with {@link Throwable} */
    public static <A> Either<A, Throwable> tryCatch(Function0<A> f) {
        try {
            return Either.<A, Throwable>left(f.apply());
        } catch (Throwable t) {
            return Either.<A, Throwable>right(t);
        }
    }

} //~
