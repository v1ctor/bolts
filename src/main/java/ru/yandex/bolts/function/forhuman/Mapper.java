package ru.yandex.bolts.function.forhuman;

import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function1;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;
import ru.yandex.bolts.function.Function2I;

/**
 * A => B
 * @author Iliya Roubin
 *         Date: Sep 3, 2007
 */
public abstract class Mapper<A, B> implements Function1<A, B>, HumanFunction {

    public abstract B map(A a);

    public final B apply(A a) {
        return map(a);
    }

    /** (f andThen g)(x) = g(f(x)) */
    public <C> Mapper<A, C> andThen(final Function1<? super B, ? extends C> g) {
        return new Mapper<A, C>() {
            public C map(A a) {
                return g.apply(Mapper.this.map(a));
            }

            public String toString() {
                return g + "(" + Mapper.this + ")";
            }
        };
    }

    /** (f compose g)(x) = f(g(x)) */
    public <C> Mapper<C, B> compose(final Function1<C, A> g) {
        Mapper<C, A> mapper1 = wrap(g);
        return mapper1.andThen(this);
    }
    
    /** (f andThen g)(x) = g(f(x)) */
    public Operation<A> andThen(final Function1V<B> g) {
        return new Operation<A>() {
            public void execute(A a) {
                g.apply(Mapper.this.map(a));
            }

            @Override
            public String toString() {
                return g + "(" + Mapper.this + ")";
            }
        };
    }

    /** Map null to null */
    public Mapper<A, B> ignoreNullM() {
        return new Mapper<A, B>() {
            public B map(A a) {
                if (a == null) return null;
                else return Mapper.this.map(a);
            }

            public String toString() {
                return "ignoreNull(" + Mapper.this + ")";
            }
        };
    }

    /** (f andThen g)(x) = g(f(x)) */
    public Predicate<A> andThen(final Function1B<? super B> predicate) {
        return new Predicate<A>() {
            public boolean evaluate(A a) {
                return predicate.apply(map(a));
            }

            public String toString() {
                return predicate + "(" + Mapper.this + ")";
            }
        };
    }

    /** (f andThen g)(x) = g(f(x)) */
    public Comparator<A> andThen(final Function2I<B, B> comparator) {
        return new Comparator<A>() {
            public int compare(A a, A b) {
                return comparator.apply(map(a), map(b));
            }

            public String toString() {
                return comparator + "(" + Mapper.this + ")";
            }
        };
    }
    
    /** And then null low natural comparator */
    @SuppressWarnings({"unchecked"})
    public Comparator<A> andThenNaturalComparator() {
        return andThen((Comparator) Comparator.naturalComparator().nullLowC());
    }

    /** Bind argument */
    public Factory<B> bind(final A a) {
        return bind(Factory.constF(a));
    }

    /** Bind argument */
    public Factory<B> bind(final Function0<A> factory) {
        return new Factory<B>() {
            public B create() {
                return Mapper.this.map(factory.apply());
            }

            public String toString() {
                return Mapper.this + "(" + factory + ")";
            }
        };
    }

    /** Ignore result of mapping */
    public Operation<A> ignoreResult() {
        return new Operation<A>() {
            public void execute(A a) {
                Mapper.this.map(a);
            }

            public String toString() {
                return "unit(" + Mapper.this + ")";
            }
        };
    }

    public Mapper<A, B> describe(String string) {
        return new Mapper<A, B>() {
            public B map(A a) {
                return Mapper.this.map(a);
            }
        };
    }
    
    @SuppressWarnings("unchecked")
    public <U, V> Mapper<U, V> uncheckedCast() {
        return (Mapper<U, V>) this;
    }

    /** Wrap */
    @SuppressWarnings("unchecked")
    public static <A, B> Mapper<A, B> wrap(final Function1<? super A, ? extends B> mapper) {
        if (mapper instanceof Mapper) return (Mapper<A, B>) mapper;
        else return new Mapper<A, B>() {
            public B map(A a) {
                return mapper.apply(a);
            }

            public String toString() {
                return mapper.toString();
            }
        };
    }

    /** Identity mapper */
    public static <T> Mapper<T, T> identityM() {
        return new Mapper<T, T>() {
            public T map(T t) {
                return t;
            }

            public <C> Mapper<T, C> andThen(Function1<? super T, ? extends C> g) {
                return Mapper.wrap(g);
            }

            public <C> Mapper<C, T> compose(Function1<C, T> g) {
                return Mapper.wrap(g);
            }

            public String toString() {
                return "identity";
            }
        };
    }

    public static <T> Mapper<T, String> toStringM() {
        return new Mapper<T, String>() {
            public String map(T t) {
                return t != null ? t.toString() : "null";
            }

            public String toString() {
                return "toString";
            }
        };
    }

}
