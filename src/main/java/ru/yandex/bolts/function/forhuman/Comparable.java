package ru.yandex.bolts.function.forhuman;

import ru.yandex.bolts.function.Function1I;

/**
 * Extended comparable.
 *
 * @author Stepan Koltsov
 */
public abstract class Comparable<A> implements Function1I<A>, java.lang.Comparable<A>, HumanFunction {
    public int apply(A a) {
        return compareTo(a);
    }

    public abstract int compareTo(A o);

    /** Greater then */
    public boolean gt(A o) {
        return compareTo(o) > 0;
    }

    /** Greater of equal */
    public boolean ge(A o) {
        return compareTo(o) >= 0;
    }

    /** Equal to */
    public boolean eq(A o) {
        return compareTo(o) == 0;
    }

    /** Not equal to */
    public boolean ne(A o) {
        return compareTo(o) != 0;
    }

    /** Less then */
    public boolean lt(A o) {
        return compareTo(o) < 0;
    }

    /** Less or equal */
    public boolean le(A o) {
        return compareTo(o) <= 0;
    }

    public boolean useOperator(ru.yandex.bolts.function.forhuman.Comparator.Operator operator, A o) {
        return opP(operator).evaluate(o);
    }

    private abstract class OperatorPredicate extends Predicate<A> {
        public String toString() {
            return op() + "(" + Comparable.this + ")";
        }

        protected abstract String op();
    }

    /** Greater predicate */
    public Predicate<A> gtP() {
        return new OperatorPredicate() {
            public boolean evaluate(A a) {
                return Comparable.this.gt(a);
            }

            protected String op() {
                return "gt";
            }
        };
    }

    /** Greater or equal predicate */
    public Predicate<A> geP() {
        return new OperatorPredicate() {
            public boolean evaluate(A a) {
                return Comparable.this.ge(a);
            }

            public String op() {
                return "ge";
            }
        };
    }

    /** Equal predicate */
    public Predicate<A> eqP() {
        return new OperatorPredicate() {
            public boolean evaluate(A a) {
                return Comparable.this.eq(a);
            }

            public String op() {
                return "eq";
            }
        };
    }

    /** Not equal predicate */
    public Predicate<A> neP() {
        return new OperatorPredicate() {
            public boolean evaluate(A a) {
                return Comparable.this.ne(a);
            }

            public String op() {
                return "ne";
            }
        };
    }

    /** Less then predicate */
    public Predicate<A> ltP() {
        return new OperatorPredicate() {
            public boolean evaluate(A a) {
                return Comparable.this.lt(a);
            }

            public String op() {
                return "lt";
            }
        };
    }

    /** Less or equal predicate */
    public Predicate<A> leP() {
        return new OperatorPredicate() {
            public boolean evaluate(A a) {
                return Comparable.this.le(a);
            }

            public String op() {
                return "le";
            }
        };
    }

    public Predicate<A> opP(ru.yandex.bolts.function.forhuman.Comparator.Operator op) {
        switch (op) {
            case EQ: return eqP();
            case GE: return geP();
            case GT: return gtP();
            case LE: return leP();
            case LT: return ltP();
            case NE: return neP();
        }
        throw new IllegalArgumentException("unknown operator: " + op);
    }

    public Comparable<A> describe(final String string) {
        return new Comparable<A>() {
            public int compareTo(A o) {
                return Comparable.this.compareTo(o);
            }

            public String toString() {
                return string;
            }
        };
    }

    /** Wrap */
    public static <A> Comparable<A> wrap(final java.lang.Comparable<A> comparable) {
        if (comparable instanceof Comparable) return (Comparable<A>) comparable;
        else return new Comparable<A>() {
            public int compareTo(A o) {
                return comparable.compareTo(o);
            }

            public String toString() {
                return comparable + ".asComparable";
            }
        };
    }
} //~
