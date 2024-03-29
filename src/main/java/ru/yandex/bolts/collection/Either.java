package ru.yandex.bolts.collection;

import java.util.NoSuchElementException;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.internal.ObjectUtils;


public abstract class Either<A, B> {
    private Either() { }

    public A getLeft() {
        throw new NoSuchElementException("Either.Right.left");
    }

    public B getRight() {
        throw new NoSuchElementException("Either.Left.right");
    }


    public LeftProjection<A, B> left() { return new LeftProjection<>(this); }


    public RightProjection<A, B> right() { return new RightProjection<>(this); }


    public boolean isLeft() { return this instanceof Left<?, ?>; }


    public boolean isRight() { return this instanceof Right<?, ?>; }


    public Option<A> leftO() {
        return isLeft() ? Option.some(getLeft()) : Option.none();
    }


    public Option<B> rightO() {
        return isRight() ? Option.some(getRight()) : Option.none();
    }


    public Either<B, A> swap() {
        if (isLeft()) return Either.right(getLeft());
        else return Either.left(getRight());
    }

    public <C> C fold(Function<? super A, C> leftF, Function<? super B, C> rightF) {
        if (isLeft())
            return leftF.apply(getLeft());
        else
            return rightF.apply(getRight());
    }


    @SuppressWarnings("unchecked")
    public <C, D> Either<C, D> uncheckedCast() {
        return (Either<C, D>) this;
    }

    protected abstract Object getValue();

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Either<?, ?>)) {
            return false;
        }
        Either<?, ?> that = (Either<?, ?>) obj;
        return this.isLeft() == that.isLeft() && ObjectUtils.equals(this.getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(getValue()) ^ (isLeft() ? 0 : 0xB12328AB);
    }

    private static class Left<A, B> extends Either<A, B> {
        private final A value;

        private Left(A value) {
            this.value = value;
        }

        @Override
        protected Object getValue() {
            return value;
        }

        @Override
        public A getLeft() {
            return value;
        }

        @Override
        public String toString() {
            return "Either.Left(" + value + ")";
        }

    }

    private static class Right<A, B> extends Either<A, B> {
        private final B value;

        private Right(B value) {
            this.value = value;
        }

        @Override
        protected Object getValue() {
            return value;
        }

        @Override
        public B getRight() {
            return value;
        }

        @Override
        public String toString() {
            return "Either.Right(" + value + ")";
        }

    }


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

        protected abstract <C> Object  map(Function<R, C> f);
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

    public static <A, B> Either<A, B> left(A a) { return new Left<>(a); }
    public static <A, B> Either<A, B> right(B b) { return new Right<>(b); }

    public static <A, B> Function<A, Either<A, B>> leftF() {
        return Either::left;
    }

    public static <A, B> Function<B, Either<A, B>> rightF() {
        return Either::right;
    }


    public static <A> Either<A, Throwable> tryCatch(Function0<A> f) {
        try {
            return Either.left(f.apply());
        } catch (Throwable t) {
            return Either.right(t);
        }
    }

    public static <A, B> Either<A, B> fromOptions(Option<A> left, Option<B> right) {
        if (left.isDefined() && right.isDefined()) {
            throw new IllegalArgumentException("Both are defined!");
        } else if (left.isDefined()) {
            return Either.left(left.get());
        } else if (right.isDefined()) {
            return Either.right(right.get());
        } else {
            throw new IllegalArgumentException("Neither is defined!");
        }
    }

    public static <A, B> Function<Either<A, B>, Option<A>> leftOF() {
        return Either::leftO;
    }

    public static <A, B> Function<Either<A, B>, Option<B>> rightOF() {
        return Either::rightO;
    }

    public static Function1B<Either<?, ?>> isLeftF() {
        return Either::isLeft;
    }

    public static Function1B<Either<?, ?>> isRightF() {
        return Either::isRight;
    }

} //~
