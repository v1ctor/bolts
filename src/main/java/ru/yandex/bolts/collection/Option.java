package ru.yandex.bolts.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

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
     *
     * @return boolean
     */
    public abstract boolean isEmpty();

    /**
     * <code>true</code> iff this is some.
     *
     * @return boolean
     */
    public final boolean isDefined() { return !isEmpty(); }

    public final boolean isSome(T value) {
        return isDefined() && ObjectUtils.equals(get(), value);
    }


    public abstract T get() throws NoSuchElementException;


    public final T getOrElse(T u) {
        if (isDefined()) return get();
        else return u;
    }


    public final T getOrElse(Function0<T> u) {
        if (isDefined()) return get();
        else return u.apply();
    }

    /**
     * <code>getOrElse(null)</code>, but works with types better.
     *
     * @return value
     */
    public final T getOrNull() {
        return getOrElse((T) null);
    }

    /**
     * <code>this</code> if this is some, or given option otherwise.
     * @param elseOption else
     *
     * @return value
     */
    public final Option<T> orElse(Option<? extends T> elseOption) {
        return orElse(() -> elseOption);
    }

    /**
     * <code>this</code> if this is some, or evaluate function and return option otherwise.
     * @param elseOption else
     *
     * @return value
     */
    public final Option<T> orElse(Function0<Option<? extends T>> elseOption) {
        if (isDefined()) return this;
        else return elseOption.apply().uncheckedCast();
    }


    public final <E extends Throwable> T getOrThrow(E e) throws E {
        return getOrThrow((Function0<E>) () -> e);
    }


    public final <E extends Throwable> T getOrThrow(Function0<E> e) throws E {
        if (isDefined()) return get();
        else throw e.apply();
    }


    public final T getOrThrow(final String message) throws RuntimeException {
        return getOrThrow((Function0<NoSuchElementException>) () -> new NoSuchElementException(message));
    }


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
    public Option<T> filterNot(Function1B<? super T> p) {
        if (isEmpty() || !p.apply(get())) return this;
        else return none();
    }

    @Override
    public final Option<T> filterNotNull() {
        return filter(o -> o != null);
    }

    public <F> Option<F> flattenO() {
        return isEmpty() ? cast() : this.<Option<F>>cast().get();
    }


    @Override
    public <F extends T> ListF<F> filterByType(Class<F> type) {
        return filter(type::isInstance).uncheckedCast();
    }

    public final SetF<T> toSet() {
        if (isDefined()) return Cf.set(get());
        else return Cf.set();
    }

    @Override
    public SetF<T> unique() {
        return toSet();
    }


    @Override
    public <F> Option<F> uncheckedCast() {
        return cast();
    }

    @Override
    public <F> Option<F> cast() {
        return (Option<F>) super.<F>cast();
    }

    @Override
    public <F> Option<F> cast(Class<F> type) {
        if (isDefined()) {
            type.cast(get());
        }
        return cast();
    }

    public final Optional<T> toOptional() {
        return map(Optional::of).getOrElse(Optional::empty);
    }

    public final <U> Optional<U> mapToOptional(Function<? super T, U> f) {
        return map(f).toOptional();
    }

    public final OptionalInt mapToOptionalInt(ToIntFunction<? super T> f) {
        return map(x -> OptionalInt.of(f.applyAsInt(x))).getOrElse(OptionalInt.empty());
    }

    public final OptionalLong mapToOptionalLong(ToLongFunction<? super T> f) {
        return map(x -> OptionalLong.of(f.applyAsLong(x))).getOrElse(OptionalLong.empty());
    }

    public final OptionalDouble mapToOptionalDouble(ToDoubleFunction<? super T> f) {
        return map(x -> OptionalDouble.of(f.applyAsDouble(x))).getOrElse(OptionalDouble.empty());
    }


    public static <T> Option<T> none() { return None.NONE; }


    public static <T> Option<T> some(T x) { return new Some<>(x); }


    public static <T> Option<T> wrap(Optional<T> x) {
        if (x.isPresent()) return some(x.get());
        else return none();
    }


    public static <T> Option<T> notNull(T x) {
        if (x != null) return some(x);
        else return none();
    }


    public static <T> Option<T> when(boolean pred, T x) {
        if (pred) return some(x);
        else return none();
    }


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


    public static <U> Function1B<Option<U>> isDefinedF() {
        return Option::isDefined;
    }


    public static <U> Function1B<Option<U>> isEmptyF() {
        return Option.<U>isDefinedF().notF();
    }


    public static <T> Function<T, Option<T>> notNullF() {
        return Option::notNull;
    }


    public static <U> Function<Option<U>, U> getF() {
        return Option::get;
    }


    public static <U> Function<Option<U>, U> getOrElseF(final U fallback) {
        return us -> us.getOrElse(fallback);
    }


    public static <U> Function<U, Option<U>> someF() {
        return Option::some;
    }


    public static <A, B> Function2<Option<A>, Function<A, B>, Option<B>> mapF() {
        return Option::map;
    }


    public static <A, B> Function<Option<A>, Option<B>> mapF(Function<A, B> f) {
        return Option.<A, B>mapF().bind2(f);
    }

} //~
