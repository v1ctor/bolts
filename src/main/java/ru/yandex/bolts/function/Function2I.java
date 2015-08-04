package ru.yandex.bolts.function;

import ru.yandex.bolts.collection.Tuple2;


@FunctionalInterface
public interface Function2I<A, B> {

    static enum Operator {
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

    int apply(A a, B b);

    default boolean gt(A a, B b) {
        return apply(a, b) > 0;
    }

    default boolean ge(A a, B b) {
        return apply(a, b) >= 0;
    }

    default boolean eq(A a, B b) {
        return apply(a, b) == 0;
    }

    default boolean ne(A a, B b) {
        return apply(a, b) != 0;
    }

    default boolean lt(A a, B b) {
        return apply(a, b) < 0;
    }

    default boolean le(A a, B b) {
        return apply(a, b) <= 0;
    }

    default Function1B<A> gtF(B b) {
        return bind2(b).gtF();
    }

    default Function1B<A> geF(B b) {
        return bind2(b).geF();
    }

    default Function1B<A> eqF(B b) {
        return bind2(b).eqF();
    }

    default Function1B<A> neF(B b) {
        return bind2(b).neF();
    }

    default Function1B<A> ltF(B b) {
        return bind2(b).ltF();
    }

    default Function1B<A> leF(B b) {
        return bind2(b).leF();
    }

    default boolean op(Operator op, A a, B b) {
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


    default Function1I<B> bind1(final A a) {
        return b -> apply(a, b);
    }

    default Function1I<A> bind2(final B b) {
        return a -> apply(a, b);
    }

    default <C> Function2I<C, B> compose1(final Function<? super C, ? extends A> f) {
        return (c, b) -> apply(f.apply(c), b);
    }

    default <C> Function2I<A, C> compose2(final Function<? super C, ? extends B> f) {
        return (a, c) -> apply(a, f.apply(c));
    }

    default Function<Tuple2<A, B>, Integer> asFunction() {
        return a -> apply(a.get1(), a.get2());
    }

    static <A, B> Function2I<A, B> asFunction2I(final Function<Tuple2<A, B>, Integer> f) {
        return (a, b) -> f.apply(Tuple2.tuple(a, b));
    }


    default Function2I<B, A> invert() {
        return (b, a) -> apply(a, b);
    }

    @SuppressWarnings("unchecked")
    default <C, D> Function2I<C, D> uncheckedCast() {
        return (Function2I<C, D>) this;
    }

    default Function2I<A, B> memoize() {
        return asFunction2I(asFunction().memoize());
    }

} //~
