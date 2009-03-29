package ru.yandex.bolts.function;


/**
 * @author Stepan Koltsov
 */
public abstract class Function1I<A> implements java.lang.Comparable<A>  {
    public abstract int apply(A a);

    @Override
    public int compareTo(A o) {
        return apply(o);
    }
    
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
        return opP(operator).apply(o);
    }

    private abstract class OperatorFunction1B extends Function1B<A> {
        public String toString() {
            return op() + "(" + Function1I.this + ")";
        }

        protected abstract String op();
    }

    /** Greater Function1B */
    public Function1B<A> gtP() {
        return new OperatorFunction1B() {
            public boolean apply(A a) {
                return Function1I.this.gt(a);
            }

            protected String op() {
                return "gt";
            }
        };
    }

    /** Greater or equal Function1B */
    public Function1B<A> geP() {
        return new OperatorFunction1B() {
            public boolean apply(A a) {
                return Function1I.this.ge(a);
            }

            public String op() {
                return "ge";
            }
        };
    }

    /** Equal Function1B */
    public Function1B<A> eqP() {
        return new OperatorFunction1B() {
            public boolean apply(A a) {
                return Function1I.this.eq(a);
            }

            public String op() {
                return "eq";
            }
        };
    }

    /** Not equal Function1B */
    public Function1B<A> neP() {
        return new OperatorFunction1B() {
            public boolean apply(A a) {
                return Function1I.this.ne(a);
            }

            public String op() {
                return "ne";
            }
        };
    }

    /** Less then Function1B */
    public Function1B<A> ltP() {
        return new OperatorFunction1B() {
            public boolean apply(A a) {
                return Function1I.this.lt(a);
            }

            public String op() {
                return "lt";
            }
        };
    }

    /** Less or equal Function1B */
    public Function1B<A> leP() {
        return new OperatorFunction1B() {
            public boolean apply(A a) {
                return Function1I.this.le(a);
            }

            public String op() {
                return "le";
            }
        };
    }

    public Function1B<A> opP(ru.yandex.bolts.function.forhuman.Comparator.Operator op) {
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

    public static <A> Function1I<A> wrap(final java.lang.Comparable<A> comparable) {
        if (comparable instanceof Function1I) return (Function1I<A>) comparable;
        else return new Function1I<A>() {
            public int apply(A o) {
                return comparable.compareTo(o);
            }

            public String toString() {
                return comparable.toString();
            }
        };
    }

} //~
