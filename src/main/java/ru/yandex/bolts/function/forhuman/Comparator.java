package ru.yandex.bolts.function.forhuman;

import ru.yandex.bolts.function.Function1;
import ru.yandex.bolts.function.Function2I;

/**
 * Extended comparator.
 *
 * @author Stepan Koltsov
 */
public abstract class Comparator<A> implements Function2I<A, A>, java.util.Comparator<A>, HumanFunction {
    public enum Operator {
        GT(">"),
        GE(">="),
        NE("!="),
        EQ("="),
        LT("<"),
        LE("<="),
        ;
        private final String name;

        Operator(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return getName();
        }
    }

    public int apply(A a, A b) {
        return compare(a, b);
    }

    public boolean gt(A a, A b) {
        return compare(a, b) > 0;
    }

    public boolean ge(A a, A b) {
        return compare(a, b) > 0;
    }

    public boolean eq(A a, A b) {
        return compare(a, b) == 0;
    }

    public boolean ne(A a, A b) {
        return compare(a, b) != 0;
    }

    public boolean lt(A a, A b) {
        return compare(a, b) < 0;
    }

    public boolean le(A a, A b) {
        return compare(a, b) <= 0;
    }

    public boolean op(Operator op, A a, A b) {
        if (op != null) switch (op) {
            case EQ: return eq(a, b);
            case GE: return ge(a, b);
            case GT: return gt(a, b);
            case LE: return le(a, b);
            case LT: return lt(a, b);
            case NE: return ne(a, b);
        }
        throw new IllegalArgumentException("unknown operator: " + op);
    }

    public Predicate<A> gtP(A b) {
        return bind2(b).gtP();
    }

    public Predicate<A> geP(A b) {
        return bind2(b).geP();
    }

    public Predicate<A> eqP(A b) {
        return bind2(b).eqP();
    }

    public Predicate<A> neP(A b) {
        return bind2(b).neP();
    }

    public Predicate<A> ltP(A b) {
        return bind2(b).ltP();
    }

    public Predicate<A> leP(A b) {
        return bind2(b).leP();
    }

    public A max(A a, A b) {
        if (gt(a, b)) return a;
        else return b;
    }

    public A min(A a, A b) {
        if (lt(a, b)) return a;
        else return b;
    }

    /** (f compose g)(x) = f(g(x)) */
    public <B> Comparator<B> compose(final Function1<B, A> mapper) {
        return Mapper.<B, A>wrap(mapper).andThen(this);
    }
    
    public Comparator<A> chainTo(final Comparator<A> comparator) {
        return new Comparator<A>() {
            public int compare(A o1, A o2) {
                int r = Comparator.this.compare(o1, o2);
                if (r != 0) return r;
                return comparator.compare(o1, o2);
            }
        };
    }

    /** Invert current comparator */
    public Comparator<A> invert() {
        return new Comparator<A>() {
            public int compare(A o1, A o2) {
                return Comparator.this.compare(o2, o1);
            }

            public String toString() {
                return "invert(" + Comparator.this + ")";
            }
        };

    }

    /** Bind first argument */
    public ru.yandex.bolts.function.forhuman.Comparable<A> bind1(final A a) {
        return new ru.yandex.bolts.function.forhuman.Comparable<A>() {
            public int compareTo(A b) {
                return Comparator.this.compare(a, b);
            }

            public String toString() {
                return Comparator.this + "(" + a + ", _)";
            }
        };
    }

    public ru.yandex.bolts.function.forhuman.Comparable<A> bind2(final A b) {
        return new ru.yandex.bolts.function.forhuman.Comparable<A>() {
            public int compareTo(A a) {
                return Comparator.this.compare(a, b);
            }

            public String toString() {
                return Comparator.this + "(_, " + b + ")";
            }
        };
    }

    /** Null low comparator */
    public Comparator<A> nullLowC() {
        return new Comparator<A>() {
            public int compare(A o1, A o2) {
                if (o1 == null || o2 == null) {
                    return o1 == o2 ? 0 : o1 != null ? 1 : -1;
                } else {
                    return Comparator.this.compare(o1, o2);
                }
            }

            public String toString() {
                return "nullLow(" + Comparator.this + ")";
            }
        };
    }

    public BinaryFunction<A, A, A> maxF() {
        return new BinaryFunction<A, A, A>() {
            public A call(A a, A b) {
                return max(a, b);
            }
        };
    }

    public BinaryFunction<A, A, A> minF() {
        return new BinaryFunction<A, A, A>() {
            public A call(A a, A b) {
                return min(a, b);
            }
        };
    }

    public Comparator<A> describe(final String string) {
        return new Comparator<A>() {
            public int compare(A o1, A o2) {
                return Comparator.this.compare(o1, o2);
            }

            public String toString() {
                return string;
            }
        };
    }

    public static <A> Comparator<A> wrap(final java.util.Comparator<A> comparator) {
        if (comparator instanceof Comparator) return (Comparator<A>) comparator;
        else return new Comparator<A>() {
            public int compare(A o1, A o2) {
                return comparator.compare(o1, o2);
            }

            public String toString() {
                return comparator.toString();
            }
        };
    }

    public static <A> Comparator<A> wrap(final Function2I<A, A> comparator) {
        if (comparator instanceof Comparator) return (Comparator<A>) comparator;
        else return new Comparator<A>() {
            public int compare(A o1, A o2) {
                return comparator.apply(o1, o2);
            }

            public String toString() {
                return comparator.toString();
            }
        };
    }

    /** Compare {@link java.lang.Comparable}s. */
    @SuppressWarnings("unchecked")
    public static <A extends java.lang.Comparable> Comparator<A> naturalComparator()  {
        return new Comparator<A>() {
            public int compare(A o1, A o2) {
                //noinspection unchecked
                return o1.compareTo(o2);
            }

            public String toString() {
                return "compareTo";
            }
        };
    }
    
    /**
     * Always returns <code>0</code>.
     */
    public static <A> Comparator<A> constEqualComparator() {
        return new Comparator<A>() {
            public int compare(A o1, A o2) {
                return 0;
            }
        };
    }
} //~
