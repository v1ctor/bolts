package ru.yandex.bolts.function.forhuman;

import java.util.Collection;

import ru.yandex.bolts.collection.CollectionsF;
import ru.yandex.bolts.function.Function1;
import ru.yandex.bolts.function.Function1B;

/**
 * (A, B) => boolean
 *
 * @author Iliya Roubin
 * @date Sep 3, 2007
 */
public abstract class Predicate<A> implements Function1B<A>, HumanFunction {

    public abstract boolean evaluate(A a);

    public final boolean apply(A a) {
        return evaluate(a);
    }

    /** (f compose g)(x) = g(f(x)) */
    public <B> Predicate<B> compose(Function1<B, A> g) {
        return Mapper.<B, A>wrap(g).andThen(this);
    }

    /** Not(this) */
    public Predicate<A> notP() {
        return new Predicate<A>() {
            public boolean evaluate(A a) {
                return !Predicate.this.evaluate(a);
            }

            public Predicate<A> notP() {
                return Predicate.this;
            }

            public String toString() {
                return "not(" + Predicate.this + ")";
            }
        };
    }

    /** Or */
    public Predicate<A> orP(final Predicate<A> p) {
        return new Predicate<A>() {
            public boolean evaluate(A a) {
                return Predicate.this.evaluate(a) || p.evaluate(a);
            }

            /*
            public Predicate<A> orP(Predicate<A> last) {
                return Predicate.anyOfP(Predicate.this, p, last);
            }
            */

            public String toString() {
                return "or(" + Predicate.this + ", " + p + ")";
            }
        };
    }

    /** And */
    public Predicate<A> andP(final Predicate<A> p) {
        return new Predicate<A>() {
            public boolean evaluate(A a) {
                return Predicate.this.evaluate(a) && p.evaluate(a);
            }

            /*
            public Predicate<A> andP(Predicate<A> last) {
                return Predicate.allOfP(Predicate.this, p, last);
            }
            */

            public String toString() {
                return "and(" + Predicate.this + ", " + p + ")";
            }
        };
    }

    /** Check for null before calling this p */
    public Predicate<A> nullIsFalseP() {
        return Predicate.<A>notNullP().andP(this);
    }

    /** As mapper to Boolean */
    public Mapper<A, Boolean> asMapper() {
        return new Mapper<A, Boolean>() {
            public Boolean map(A a) {
                return Predicate.this.evaluate(a);
            }

            public String toString() {
                return Predicate.this + ".asMapper";
            }
        };
    }

    public Predicate<A> describe(final String string) {
        return new Predicate<A>() {
            public boolean evaluate(A a) {
                return Predicate.this.evaluate(a);
            }

            public String toString() {
                return string;
            }
        };
    }

    /*
    public Mapper<A, Option<A>> asIdentityFlatMapper() {
        return new Mapper<A, Option<A>>() {
            public Option<A> map(A a) {
                if (evaluate(a)) return Option.some(a);
                else return Option.none();
            }
        };
    }
    */
    
    @SuppressWarnings("unchecked")
    public <B> Predicate<B> uncheckedCast() {
        return (Predicate<B>) this;
    }

    /** Equal to the given value predicate */
    public static <B> Predicate<B> equalsP(final B b) {
        return new Predicate<B>() {
            public boolean evaluate(B o) {
                if (b == null || o == null) return b == o;
                else return b.equals(o);
            }

            public String toString() {
                return "equals(" + b + ")";
            }
        };
    }

    public static <B> Predicate<B> sameP(final B b) {
        return new Predicate<B>() {
            public boolean evaluate(B o) {
                return b == o;
            }

            public String toString() {
                return "same("+ b + ")";
            }
        };
    }

    /** Check argument is not null */
    public static <T> Predicate<T> notNullP() {
        return new Predicate<T>() {
            public boolean evaluate(T o) {
                return o != null;
            }

            public Predicate<T> nullIsFalseP() {
                return this;
            }

            public String toString() {
                return "notNull";
            }
        };
    }

    /** True predicate */
    public static <B> Predicate<B> trueP() {
        return new Predicate<B>() {
            public boolean evaluate(B b) {
                return true;
            }

            public Predicate<B> orP(Predicate<B> p) {
                return this;
            }

            public Predicate<B> andP(Predicate<B> p) {
                return p;
            }

            public Predicate<B> notP() {
                return falseP();
            }

            public String toString() {
                return "true";
            }
        };
    }

    /** False predicate */
    public static <B> Predicate<B> falseP() {
        return new Predicate<B>() {
            public boolean evaluate(B b) {
                return false;
            }

            public Predicate<B> orP(Predicate<B> p) {
                return p;
            }

            public Predicate<B> andP(Predicate<B> p) {
                return this;
            }

            public Predicate<B> notP() {
                return trueP();
            }

            public String toString() {
                return "false";
            }
        };
    }

    public static <B> Predicate<B> allOfP(final Collection<? extends Function1B<B>> predicates) {
        if (predicates.size() == 0) return trueP();
        else if (predicates.size() == 1) return wrap(predicates.iterator().next());
        else return new Predicate<B>() {
            public boolean evaluate(B b) {
                for (Function1B<? super B> predicate : predicates) {
                    if (!predicate.apply(b)) return false;
                }
                return true;
            }

            public String toString() {
                return CollectionsF.x(predicates).mkString("allOf(", ", ", ")");
            }
        };
    }

    public static <B> Predicate<B> allOfP(Predicate<B>... predicates) {
        return allOfP(CollectionsF.list(predicates));
    }

    public static <B> Predicate<B> anyOfP(final Collection<? extends Function1B<B>> predicates) {
        if (predicates.size() == 0) return falseP();
        else if (predicates.size() == 1) return wrap(predicates.iterator().next());
        else return new Predicate<B>() {
            public boolean evaluate(B b) {
                for (Function1B<? super B> predicate : predicates) {
                    if (predicate.apply(b)) return true;
                }
                return false;
            }

            public String toString() {
                return CollectionsF.x(predicates).mkString("anyOf(", ", ", ")");
            }
        };
    }

    public static <B> Predicate<B> anyOfP(Predicate<B>... predicates) {
        return anyOfP(CollectionsF.list(predicates));
    }

    /** Wrap */
    @SuppressWarnings("unchecked")
    public static <B> Predicate<B> wrap(final Function1B<? super B> predicate) {
        if (predicate instanceof Predicate) return (Predicate<B>) predicate;
        else return new Predicate<B>() {
            public boolean evaluate(B b) {
                return predicate.apply(b);
            }

            public String toString() {
                return predicate.toString();
            }
        };
    }

    /** Wrap */
    public static <B> Predicate<B> wrap(final Function1<B, Boolean> mapper) {
        return new Predicate<B>() {
            public boolean evaluate(B b) {
                return mapper.apply(b);
            }

            public String toString() {
                return mapper.toString();
            }
        };
    }

    public static <B> Predicate<B> instanceOfP(final Class<?> cl) {
        return new Predicate<B>() {
            public boolean evaluate(B b) {
                return cl.isInstance(b);
            }

            public String toString() {
                return "instanceof " + cl.getName();
            }
        };
    }
}
