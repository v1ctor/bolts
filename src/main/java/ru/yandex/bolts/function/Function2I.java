package ru.yandex.bolts.function;

import ru.yandex.bolts.collection.Tuple2;


/**
 * @author Stepan Koltsov
 */
public abstract class Function2I<A, B> {

    public static enum Operator {
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

    public abstract int apply(A a, B b);

    public boolean gt(A a, B b) {
        return apply(a, b) > 0;
    }

    public boolean ge(A a, B b) {
        return apply(a, b) >= 0;
    }

    public boolean eq(A a, B b) {
        return apply(a, b) == 0;
    }

    public boolean ne(A a, B b) {
        return apply(a, b) != 0;
    }

    public boolean lt(A a, B b) {
        return apply(a, b) < 0;
    }

    public boolean le(A a, B b) {
        return apply(a, b) <= 0;
    }

    public Function1B<A> gtF(B b) {
        return bind2(b).gtF();
    }

    public Function1B<A> geF(B b) {
        return bind2(b).geF();
    }

    public Function1B<A> eqF(B b) {
        return bind2(b).eqF();
    }

    public Function1B<A> neF(B b) {
        return bind2(b).neF();
    }

    public Function1B<A> ltF(B b) {
        return bind2(b).ltF();
    }

    public Function1B<A> leF(B b) {
        return bind2(b).leF();
    }

    public boolean op(Operator op, A a, B b) {
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

    /** Bind first argument */
    public Function1I<B> bind1(final A a) {
        return new Function1I<B>() {
            public int apply(B b) {
                return Function2I.this.apply(a, b);
            }

            public String toString() {
                return Function2I.this + "(" + a + ", _)";
            }
        };
    }

    public Function1I<A> bind2(final B b) {
        return new Function1I<A>() {
            public int apply(A a) {
                return Function2I.this.apply(a, b);
            }

            public String toString() {
                return Function2I.this + "(_, " + b + ")";
            }
        };
    }

    public Function<Tuple2<A, B>, Integer> asFunction() {
        return new Function<Tuple2<A, B>, Integer>() {
            public Integer apply(Tuple2<A, B> a) {
                return Function2I.this.apply(a.get1(), a.get2());
            }
        };
    }

    public static <A, B> Function2I<A, B> asFunction2I(final Function<Tuple2<A, B>, Integer> f) {
        return new Function2I<A, B>() {
            public int apply(A a, B b) {
                return f.apply(Tuple2.tuple(a, b));
            }

            @Override
            public String toString() {
                return f.toString();
            }
        };
    }

    /** Invert current comparator */
    public Function2I<B, A> invert() {
        return new Function2I<B, A>() {
            public int apply(B b, A a) {
                return Function2I.this.apply(a, b);
            }

            public String toString() {
                return "invert(" + Function2I.this + ")";
            }

            @Override
            public Function2I<A, B> invert() {
                return Function2I.this;
            }

        };

    }

    @SuppressWarnings("unchecked")
    public <C, D> Function2I<C, D> uncheckedCast() {
        return (Function2I<C, D>) this;
    }

    public Function2I<A, B> memoize() {
        return asFunction2I(asFunction().memoize());
    }

} //~
