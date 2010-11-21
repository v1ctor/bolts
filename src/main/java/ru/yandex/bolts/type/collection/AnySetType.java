package ru.yandex.bolts.type.collection;

import java.util.Set;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;

/**
 * @author Stepan Koltsov
 */
public class AnySetType extends AnyCollectionType {

    public <A> Function2<Set<A>, Function1B<? super A>, SetF<A>> filterF() {
        return new Function2<Set<A>, Function1B<? super A>, SetF<A>>() {
            public SetF<A> apply(Set<A> set, Function1B<? super A> f) {
                return Cf.x(set).filter(f);
            }
        };
    }

    public <A> Function<Set<A>, SetF<A>> filterF(Function1B<? super A> f) {
        return this.<A>filterF().bind2(f);
    }

} //~