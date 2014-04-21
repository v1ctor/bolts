package ru.yandex.bolts.function;

/**
 * @author Stepan Koltsov
 */
@FunctionalInterface
public interface Function1I<A> extends java.lang.Comparable<A> {
    int apply(A a);

    default Function<A, Integer> asFunction() {
        return this::apply;
    }

    static <A> Function1I<A> asFunction1I(final Function<A, Integer> f) {
        return a -> f.apply(a);
    }

    @Override
    default int compareTo(A o) {
        return apply(o);
    }

    /** Greater than */
    default boolean gt(A o) {
        return compareTo(o) > 0;
    }

    /** Greater of equal */
    default boolean ge(A o) {
        return compareTo(o) >= 0;
    }

    /** Equal to */
    default boolean eq(A o) {
        return compareTo(o) == 0;
    }

    /** Not equal to */
    default boolean ne(A o) {
        return compareTo(o) != 0;
    }

    /** Less than */
    default boolean lt(A o) {
        return compareTo(o) < 0;
    }

    /** Less or equal */
    default boolean le(A o) {
        return compareTo(o) <= 0;
    }

    default boolean useOperator(ru.yandex.bolts.function.forhuman.Comparator.Operator operator, A o) {
        return opF(operator).apply(o);
    }

    abstract class OperatorFunction1B<A> implements Function1B<A> {
        private final Function1I<A> f;

        protected OperatorFunction1B(Function1I<A> f) {
            this.f = f;
        }

        public String toString() {
            return op() + "(" + f + ")";
        }

        protected abstract String op();
    }

    /** Greater Function1B */
    default Function1B<A> gtF() {
        return this::gt;
    }

    /** Greater or equal Function1B */
    default Function1B<A> geF() {
        return this::ge;
    }

    /** Equal Function1B */
    default Function1B<A> eqF() {
        return this::eq;
    }

    /** Not equal Function1B */
    default Function1B<A> neF() {
        return this::ne;
    }

    /** Less than Function1B */
    default Function1B<A> ltF() {
        return this::lt;
    }

    /** Less or equal Function1B */
    default Function1B<A> leF() {
        return this::le;
    }

    default Function1B<A> opF(Function2I.Operator op) {
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

    static <A> Function1I<A> wrap(final java.lang.Comparable<A> comparable) {
        if (comparable instanceof Function1I) return (Function1I<A>) comparable;
        else return comparable::compareTo;
    }

    default Function1I<A> memoize() {
        return asFunction1I(asFunction().memoize());
    }

    @SuppressWarnings("unchecked")
    default <B> Function1I<B> uncheckedCast() {
        return (Function1I<B>) this;
    }

} //~
