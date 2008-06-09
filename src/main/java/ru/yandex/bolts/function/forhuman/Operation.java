package ru.yandex.bolts.function.forhuman;

import ru.yandex.bolts.function.Function0;
import ru.yandex.bolts.function.Function1;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;

/**
 * A => void
 *
 * @author Stepan Koltsov
 */
public abstract class Operation<A> implements Function1V<A>, HumanFunction {

    public final void apply(A a) {
        execute(a);
    }

    public abstract void execute(A a);

    /** Bind argument */
    public Closure bind(final A a) {
        return bind(Factory.constF(a));
    }

    /** Bind argument */
    public Closure bind(final Function0<A> factory) {
        return new Closure() {
            public void execute() {
                Operation.this.execute(factory.apply());
            }

            public String toString() {
                return Operation.this + "(" + factory + ")";
            }
        };
    }

    public <B> Mapper<A, B> andThen(final Function0<B> f) {
        return new Mapper<A, B>() {
            public B map(A a) {
                execute(a);
                return f.apply();
            }
        };
    }
    
    /** (f compose g)(x) = f(g(x)) */
    public <B> Operation<B> compose(final Function1<B, A> g) {
        return Mapper.<B, A>wrap(g).andThen(this);
    }

    public Predicate<A> chainTo(final Function1B<A> p) {
        return new Predicate<A>() {
            public boolean evaluate(A a) {
                execute(a);
                return p.apply(a);
            }
        };
    }

    public Operation<A> chainTo(final Function1V<A> op) {
        return new Operation<A>() {
            public void execute(A a) {
                Operation.this.execute(a);
                op.apply(a);
            }
        };
    }

    public <B> Mapper<A, B> asMapperToNull() {
        return andThen(Factory.constF((B) null));
    }

    public Operation<A> onlyIf(final Function1B<A> predicate) {
        return new Operation<A>() {
            public void execute(A a) {
                if (predicate.apply(a)) {
                    Operation.this.execute(a);
                }
            }
        };
    }

    public Operation<A> describe(final String string) {
        return new Operation<A>() {
            public void execute(A a) {
                Operation.this.execute(a);
            }

            public String toString() {
                return string;
            }
        };
    }

    public static <A> Operation<A> nop() {
        return new Operation<A>() {
            public void execute(A a) {
            }

            public String toString() {
                return "nop";
            }
        };
    }

    public static <B> Operation<B> wrap(final Function1V<B> f) {
        if (f instanceof Operation) return (Operation<B>) f;
        else return new Operation<B>() {
            public void execute(B o) {
                f.apply(o);
            }

            public String toString() {
                return f.toString();
            }
        };
    }
} //~
