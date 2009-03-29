package ru.yandex.bolts.function;

import java.util.Collection;

import ru.yandex.bolts.collection.CollectionsF;

/**
 * @author Stepan Koltsov
 */
public abstract class Function1B<A> {
    public abstract boolean apply(A a);
    
    public Function<A, Boolean> asFunction() {
        return new Function<A, Boolean>() {
            public Boolean apply(A a) {
                return Function1B.this.apply(a);
            }

            @Override
            public String toString() {
                return Function1B.this.toString();
            }
        };
    }
    
    @SuppressWarnings("unchecked")
    public <B> Function1B<B> uncheckedCast() {
        return (Function1B<B>) this;
    }
    
    /** Check for null before calling this p */
    public Function1B<A> nullIsFalseP() {
        return Function1B.<A>notNullP().andP(this);
    }

    /** (f compose g)(x) = g(f(x)) */
    public <B> Function1B<B> compose(Function<B, A> g) {
        return g.andThen(this);
    }

    /** Not(this) */
    public Function1B<A> notP() {
        return new Function1B<A>() {
            public boolean apply(A a) {
                return !Function1B.this.apply(a);
            }

            public Function1B<A> notP() {
                return Function1B.this;
            }

            public String toString() {
                return "not(" + Function1B.this + ")";
            }
        };
    }

    /** Or */
    public Function1B<A> orP(final Function1B<A> p) {
        return new Function1B<A>() {
            public boolean apply(A a) {
                return Function1B.this.apply(a) || p.apply(a);
            }

            /*
            public Function1B<A> orP(Function1B<A> last) {
                return Function1B.anyOfP(Function1B.this, p, last);
            }
            */

            public String toString() {
                return "or(" + Function1B.this + ", " + p + ")";
            }
        };
    }

    /** And */
    public Function1B<A> andP(final Function1B<A> p) {
        return new Function1B<A>() {
            public boolean apply(A a) {
                return Function1B.this.apply(a) && p.apply(a);
            }

            /*
            public Function1B<A> andP(Function1B<A> last) {
                return Function1B.allOfP(Function1B.this, p, last);
            }
            */

            public String toString() {
                return "and(" + Function1B.this + ", " + p + ")";
            }
        };
    }

    /** Equal to the given value Function1B */
    public static <B> Function1B<B> equalsP(final B b) {
        return new Function1B<B>() {
            public boolean apply(B o) {
                if (b == null || o == null) return b == o;
                else return b.equals(o);
            }

            public String toString() {
                return "equals(" + b + ")";
            }
        };
    }

    public static <B> Function1B<B> sameP(final B b) {
        return new Function1B<B>() {
            public boolean apply(B o) {
                return b == o;
            }

            public String toString() {
                return "same("+ b + ")";
            }
        };
    }

    /** Check argument is not null */
    public static <T> Function1B<T> notNullP() {
        return new Function1B<T>() {
            public boolean apply(T o) {
                return o != null;
            }

            public Function1B<T> nullIsFalseP() {
                return this;
            }

            public String toString() {
                return "notNull";
            }
        };
    }

    /** True Function1B */
    public static <B> Function1B<B> trueP() {
        return new Function1B<B>() {
            public boolean apply(B b) {
                return true;
            }

            public Function1B<B> orP(Function1B<B> p) {
                return this;
            }

            public Function1B<B> andP(Function1B<B> p) {
                return p;
            }

            public Function1B<B> notP() {
                return falseP();
            }

            public String toString() {
                return "true";
            }
        };
    }

    /** False Function1B */
    public static <B> Function1B<B> falseP() {
        return new Function1B<B>() {
            public boolean apply(B b) {
                return false;
            }

            public Function1B<B> orP(Function1B<B> p) {
                return p;
            }

            public Function1B<B> andP(Function1B<B> p) {
                return this;
            }

            public Function1B<B> notP() {
                return trueP();
            }

            public String toString() {
                return "false";
            }
        };
    }

    public static <B> Function1B<B> allOfP(final Collection<? extends Function1B<B>> Function1Bs) {
        if (Function1Bs.size() == 0) return trueP();
        else if (Function1Bs.size() == 1) return Function1Bs.iterator().next();
        else return new Function1B<B>() {
            public boolean apply(B b) {
                for (Function1B<? super B> Function1B : Function1Bs) {
                    if (!Function1B.apply(b)) return false;
                }
                return true;
            }

            public String toString() {
                return CollectionsF.x(Function1Bs).mkString("allOf(", ", ", ")");
            }
        };
    }

    public static <B> Function1B<B> allOfP(Function1B<B>... Function1Bs) {
        return allOfP(CollectionsF.list(Function1Bs));
    }

    public static <B> Function1B<B> anyOfP(final Collection<? extends Function1B<B>> Function1Bs) {
        if (Function1Bs.size() == 0) return falseP();
        else if (Function1Bs.size() == 1) return Function1Bs.iterator().next();
        else return new Function1B<B>() {
            public boolean apply(B b) {
                for (Function1B<? super B> f : Function1Bs) {
                    if (f.apply(b)) return true;
                }
                return false;
            }

            public String toString() {
                return CollectionsF.x(Function1Bs).mkString("anyOf(", ", ", ")");
            }
        };
    }

    public static <B> Function1B<B> anyOfP(Function1B<B>... Function1Bs) {
        return anyOfP(CollectionsF.list(Function1Bs));
    }
    
    public static <B> Function1B<B> instanceOfP(final Class<?> cl) {
        return new Function1B<B>() {
            public boolean apply(B b) {
                return cl.isInstance(b);
            }

            public String toString() {
                return "instanceof " + cl.getName();
            }
        };
    }
    
    /** Wrap */
    public static <B> Function1B<B> wrap(final Function<B, Boolean> mapper) {
        return new Function1B<B>() {
            public boolean apply(B b) {
                return mapper.apply(b);
            }

            public String toString() {
                return mapper.toString();
            }
        };
    }

} //~
