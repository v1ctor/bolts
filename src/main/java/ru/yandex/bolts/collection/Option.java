package ru.yandex.bolts.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import ru.yandex.bolts.collection.impl.AbstractListF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;
import ru.yandex.bolts.internal.ObjectUtils;

/**
 * Port of scala <a href="http://www.scala-lang.org/docu/files/api/scala/Option.html">Option</a>.
 *
 * @see java.util.Optional
 *
 * @author Stepan Koltsov
 */
@SuppressWarnings({"unchecked"})
public abstract class Option<T> extends AbstractListF<T> implements Serializable {
    private static final long serialVersionUID = 204194056296233266L;

    private Option() { }

    /**
     * <code>true</code> iff this is none.
     */
    public abstract boolean isEmpty();

    /**
     * <code>true</code> iff this is some.
     */
    public final boolean isDefined() { return !isEmpty(); }

    public final boolean isSome(T value) {
        return isDefined() && ObjectUtils.equals(get(), value);
    }

    /**
     * Get the value.
     *
     * @throws NoSuchElementException if this is none.
     */
    public abstract T get() throws NoSuchElementException;

    /**
     * If this is some return value of this, or return given value otherwise.
     */
    public final T getOrElse(T u) {
        if (isDefined()) return get();
        else return u;
    }

    /**
     * If this is some return value of this, or evaluate function and return it value otherwise.
     */
    public final T getOrElse(Function0<T> u) {
        if (isDefined()) return get();
        else return u.apply();
    }

    /**
     * <code>getOrElse(null)</code>, but works with types better.
     */
    public final T getOrNull() {
        return getOrElse((T) null);
    }

    /**
     * <code>this</code> if this is some, or given option otherwise.
     */
    public final Option<T> orElse(Option<T> elseOption) {
        return orElse(Function0.constF(elseOption));
    }

    /**
     * <code>this</code> if this is some, or evaluate function and return option otherwise.
     */
    public final Option<T> orElse(Function0<Option<T>> elseOption) {
        if (isDefined()) return this;
        else return elseOption.apply();
    }

    /** Throw specified exception if {@link #isEmpty()}. */
    public final <E extends Throwable> T getOrThrow(E e) throws E {
        return getOrThrow(Function0.constF(e));
    }

    /** Throw specified exception if {@link #isEmpty()}. */
    public final <E extends Throwable> T getOrThrow(Function0<E> e) throws E {
        if (isDefined()) return get();
        else throw e.apply();
    }

    /** Throw exception with specified message if this is empty */
    public final T getOrThrow(final String message) throws RuntimeException {
        return getOrThrow((Function0<NoSuchElementException>) () -> new NoSuchElementException(message));
    }

    /**
     * Get or throw exception if this is empty. Message is constructed by concatenating given params.
     */
    public final T getOrThrow(final String message, final Object param) throws RuntimeException {
        return getOrThrow((Function0<NoSuchElementException>) () -> new NoSuchElementException(message + param));
    }

    @Override
    public int size() {
        if (isDefined()) return 1;
        else return 0;
    }

    @Override
    public T get(int index) {
        if (index == 0) return get();
        else throw new NoSuchElementException();
    }

    @Override
    public final <U> Option<U> map(Function<? super T, U> f) {
        if (isDefined()) return some(f.apply(get()));
        else return none();
    }

    @Override
    public final <U> Option<U> flatMapO(Function<? super T, Option<U>> f) {
        if (isDefined()) return f.apply(get());
        else return none();
    }

    @Override
    public <B> ListF<B> flatMap(Function<? super T, ? extends Collection<B>> f) {
        if (isDefined()) return Cf.x(f.apply(get())).toList();
        else return Cf.list();
    }

    @Override
    public final Option<T> filter(Function1B<? super T> p) {
        if (isEmpty() || p.apply(get())) return this;
        else return none();
    }

    @Override
    public ListF<T> filterNot(Function1B<? super T> p) {
        if (isEmpty() || !p.apply(get())) return this;
        else return none();
    }

    @Override
    public final Option<T> filterNotNull() {
        return filter(Function1B.notNullF());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F extends T> ListF<F> filterByType(Class<F> type) {
        return filter(Function1B.instanceOfF(type)).uncheckedCast();
    }

    public final SetF<T> toSet() {
        if (isDefined()) return Cf.set(get());
        else return Cf.set();
    }

    @Override
    public SetF<T> unique() {
        return toSet();
    }

    /** This object with different type parameters */
    @Override
    public <F> Option<F> uncheckedCast() {
        return (Option<F>) this;
    }

    /**
     * Return singleton none object.
     */
    public static <T> Option<T> none() { return None.NONE; }

    /**
     * Construct some containing given value.
     */
    public static <T> Option<T> some(T x) { return new Some<>(x); }

    /**
     * Construct option from {@link Optional}
     */
    public static <T> Option<T> wrap(Optional<T> x) {
        if (x.isPresent()) return some(x.get());
        else return none();
    }

    /**
     * Some if not null, None otherwise.
     */
    public static <T> Option<T> notNull(T x) {
        if (x != null) return some(x);
        else return none();
    }

    /**
     * Some if pred, None otherwise.
     */
    public static <T> Option<T> when(boolean pred, T x) {
        if (pred) return some(x);
        else return none();
    }

    /**
     * Some if pred, None otherwise, lazy evaluation.
     */
    public static <T> Option<T> when(boolean pred, Function0<T> x) {
        if (pred) return some(x.apply());
        else return none();
    }

    public static <T> Option<T> x(Optional<T> optional) {
        if (optional.isPresent()) return some(optional.get());
        else return none();
    }

    public static Option<Integer> x(OptionalInt optional) {
        if (optional.isPresent()) return some(optional.getAsInt());
        else return none();
    }

    public static Option<Long> x(OptionalLong optional) {
        if (optional.isPresent()) return some(optional.getAsLong());
        else return none();
    }

    public static Option<Double> x(OptionalDouble optional) {
        if (optional.isPresent()) return some(optional.getAsDouble());
        else return none();
    }

    /**
     * Some.
     *
     * @see #some(Object) to create Some instance
     * @see fj.data.Some
     */
    public static final class Some<T> extends Option<T> {
        private static final long serialVersionUID = -8660767248065584199L;

        private final T x;

        private Some(T x) {
            this.x = x;
        }

        public T get() {
            return x;
        }

        public boolean isEmpty() {
            return false;
        }

        public String toString() {
            return "Some(" + x + ")";
        }

        public boolean equals(Object obj) {
            if (obj instanceof Some) {
                Object a = this.get();
                Object b = ((Some<?>) obj).get();
                if (a == null || b == null) return a == b;
                else return a.equals(b);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return x != null ? x.hashCode() : 0;
        }
    }

    /**
     * None. Instance could be obtained by {@link Option#none()}.
     *
     * @see #none() to optain None singleton
     * @see java.util.Optional.EMPTY
     */
    public static final class None<T> extends Option<T> {
        private static final long serialVersionUID = 3461376542565825187L;

        @SuppressWarnings("rawtypes")
        private static final None NONE = new None();

        private None() {
        }

        public boolean isEmpty() {
            return true;
        }

        public T get() {
            throw new NoSuchElementException("None.get");
        }

        public String toString() {
            return "None";
        }

        public boolean equals(Object obj) {
            return this == obj;
        }

        public int hashCode() {
            return 0x64DA3256;
        }

        private Object readResolve() {
            return NONE;
        }
    }

    /**
     * Delegate to {@link #isDefined()}.
     */
    public static <U> Function1B<Option<U>> isDefinedF() {
        return Option::isDefined;
    }

    /**
     * Delegate to {@link #isEmpty()}.
     */
    public static <U> Function1B<Option<U>> isEmptyF() {
        return Option.<U>isDefinedF().notF();
    }

    /**
     * Delegate to {@link #notNull(Object)}.
     */
    public static <T> Function<T, Option<T>> notNullF() {
        return Option::notNull;
    }

    /**
     * Delegate to {@link #get()}.
     */
    public static <U> Function<Option<U>, U> getF() {
        return Option::get;
    }

    /**
     * Delegate to {@link #getOrElse(Object)}.
     */
    public static <U> Function<Option<U>, U> getOrElseF(final U fallback) {
        return us -> us.getOrElse(fallback);
    }

    /**
     * Delegate to {@link #some(Object)}.
     */
    public static <U> Function<U, Option<U>> someF() {
        return Option::some;
    }

    /**
     * @see #map(Function)
     */
    public static <A, B> Function2<Option<A>, Function<A, B>, Option<B>> mapF() {
        return Option::map;
    }

    /**
     * @see #map(Function)
     */
    public static <A, B> Function<Option<A>, Option<B>> mapF(Function<A, B> f) {
        return Option.<A, B>mapF().bind2(f);
    }

} //~
