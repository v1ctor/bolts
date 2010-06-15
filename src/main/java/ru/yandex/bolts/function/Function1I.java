package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
public abstract class Function1I<A> implements java.lang.Comparable<A>  {
    public abstract int apply(A a);

    public Function<A, Integer> asFunction() {
        return new Function<A, Integer>() {
            public Integer apply(A a) {
                return Function1I.this.apply(a);
            }

            @Override
            public String toString() {
                return Function1I.this.toString();
            }
        };
    }
    
    public static <A> Function1I<A> asFunction1I(final Function<A, Integer> f) {
        return new Function1I<A>() {
            public int apply(A a) {
                return f.apply(a);
            }
            
            @Override
            public String toString() {
                return f.toString();
            }
        };
    }
    
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
        return opF(operator).apply(o);
    }

    private abstract class OperatorFunction1B extends Function1B<A> {
        public String toString() {
            return op() + "(" + Function1I.this + ")";
        }

        protected abstract String op();
    }

    /** Greater Function1B */
    public Function1B<A> gtF() {
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
    public Function1B<A> geF() {
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
    public Function1B<A> eqF() {
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
    public Function1B<A> neF() {
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
    public Function1B<A> ltF() {
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
    public Function1B<A> leF() {
        return new OperatorFunction1B() {
            public boolean apply(A a) {
                return Function1I.this.le(a);
            }

            public String op() {
                return "le";
            }
        };
    }

    public Function1B<A> opF(Function2I.Operator op) {
        switch (op) {
            case EQ: return eqF();
            case GE: return geF();
            case GT: return gtF();
            case LE: return leF();
            case LT: return ltF();
            case NE: return neF();
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

    public Function1I<A> memoize() {
        return asFunction1I(asFunction().memoize());
    }
    
} //~
