package ru.yandex.bolts.type.collection;

import java.util.Collection;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.Option;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;


/**
 * @author Stepan Koltsov
 */
public abstract class AnyCollectionType {

    public <F, T> Function2<Collection<F>, Function<? super F, T>, ListF<T>> mapF() {
        return new Function2<Collection<F>, Function<? super F, T>, ListF<T>>() {
            public ListF<T> apply(Collection<F> input, Function<? super F, T> transform) {
                return Cf.x(input).map(transform);
            }
        };
    }

    public <F, T> Function<Collection<F>, ListF<T>> mapF(
            Function<? super F, T> transform)
    {
        return this.<F, T>mapF().bind2(transform);
    }

    public <F> Function2<Collection<F>, Function1B<? super F>, Option<F>> findF() {
        return new Function2<Collection<F>, Function1B<? super F>, Option<F>>() {
            public Option<F> apply(Collection<F> c, Function1B<? super F> f) {
                return Cf.x(c).find(f);
            }
        };
    }

    public <F> Function<Collection<F>, Option<F>> findF(
            Function1B<? super F> f)
    {
        return this.<F>findF().bind2(f);
    }

    public Function<Collection<?>, Integer> sizeF() {
        return new Function<Collection<?>, Integer>() {
            public Integer apply(Collection<?> c) {
                return c.size();
            }
        };
    }

} //~
