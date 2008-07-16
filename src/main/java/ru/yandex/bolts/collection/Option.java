package ru.yandex.bolts.collection;

import ru.yandex.bolts.collection.impl.AbstractListF;
import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function1;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.forhuman.Factory;
import ru.yandex.bolts.function.forhuman.Predicate;
import ru.yandex.bolts.function.forhuman.Mapper;

import java.util.NoSuchElementException;
import java.util.Collection;
import java.io.Serializable;

/**
 * Port of scala <a href="http://www.scala-lang.org/docu/files/api/scala/Option.html">Option</a>.
 * @author Stepan Koltsov
 */
@SuppressWarnings({"unchecked"})
public abstract class Option<T> extends AbstractListF<T> implements Serializable {
    private static final long serialVersionUID = 204194056296233266L;

    private Option() { }

    public abstract boolean isEmpty();

    public final boolean isDefined() { return !isEmpty(); }

    public abstract T get() throws NoSuchElementException;

    public final T getOrElse(T u) {
        if (isDefined()) return get();
        else return u;
    }

    public final T getOrElse(Function0<T> u) {
        if (isDefined()) return get();
        else return u.apply();
    }

    public final T getOrNull() {
        return getOrElse((T) null);
    }

    public final Option<T> orElse(Option<T> elseOption) {
        return orElse(Factory.constF(elseOption));
    }

    public final Option<T> orElse(Function0<Option<T>> elseOption) {
        if (isDefined()) return this;
        else return elseOption.apply();
    }

    /** Throw specified exception if {@link #isEmpty()}. */
    public final <E extends Throwable> T getOrThrow(E e) throws E {
        return getOrThrow(Factory.constF(e));
    }

    /** Throw specified exception if {@link #isEmpty()}. */
    public final <E extends Throwable> T getOrThrow(Function0<E> e) throws E {
        if (isDefined()) return get();
        else throw e.apply();
    }

    public final T getOrThrow(final String message) throws RuntimeException {
        return getOrThrow(new Factory<NoSuchElementException>() {
            public NoSuchElementException create() {
                return new NoSuchElementException(message);
            }
        });
    }

    public final T getOrThrow(final String message, final Object param) throws RuntimeException {
        return getOrThrow(new Factory<NoSuchElementException>() {
            public NoSuchElementException create() {
                return new NoSuchElementException(message + param);
            }
        });
    }

    public int size() {
        if (isDefined()) return 1;
        else return 0;
    }

    public T get(int index) {
        if (index == 0) return get();
        else throw new NoSuchElementException();
    }

    @Override
    public final <U> Option<U> map(Function1<? super T, U> f) {
        if (isDefined()) return some(f.apply(get()));
        else return none();
    }

    @Override
    public final <U> Option<U> flatMapO(Function1<? super T, Option<U>> f) {
        if (isDefined()) return f.apply(get());
        else return none();
    }

    @Override
    public <B> ListF<B> flatMap(Function1<? super T, ? extends Collection<B>> f) {
        if (isDefined()) return CollectionsF.x(f.apply(get())).toList();
        else return CollectionsF.list();
    }

    @Override
    public final Option<T> filter(Function1B<? super T> p) {
        if (isEmpty() || p.apply(get())) return this;
        else return none();
    }

    public final SetF<T> toSet() {
        if (isDefined()) return CollectionsF.set(get());
        else return CollectionsF.set();
    }

    @Override
    public SetF<T> unique() {
        return toSet();
    }
    
    @Override
    public <F> Option<F> uncheckedCast() {
        return (Option<F>) this;
    }

    public static <T> Option<T> none() { return None.NONE; }

    // XXX: prohibit null
    public static <T> Option<T> some(T x) { return new Some(x); }

    /**
     * Some if not null, None otherwise.
     */
    public static <T> Option<T> notNull(T x) {
        if (x != null) return some(x);
        else return none();
    }

    public static Option<String> notEmpty(String s) {
        if (s != null && s.length() > 0) return some(s);
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
                Object b = ((Some) obj).get();
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

    public static Predicate<Option> isDefinedP() {
        return new Predicate<Option>() {
            public boolean evaluate(Option option) {
                return option.isDefined();
            }

            public String toString() {
                return "isDefined";
            }
        };
    }

    public static Predicate<Option> isEmptyP() {
        return isDefinedP().notP();
    }

    public static <T> Mapper<T, Option<T>> optionM() {
        return new Mapper<T, Option<T>>() {
            public Option<T> map(T t) {
                return Option.notNull(t);
            }
        };
    }
} //~
