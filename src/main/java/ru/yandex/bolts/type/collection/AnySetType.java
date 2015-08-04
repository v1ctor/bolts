package ru.yandex.bolts.type.collection;

import java.util.Set;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function2;


public class AnySetType extends AnyCollectionType {

    public <A> Function2<Set<A>, Function1B<? super A>, SetF<A>> filterF() {
        return (set, f) -> Cf.x(set).filter(f);
    }

    public <A> Function<Set<A>, SetF<A>> filterF(Function1B<? super A> f) {
        return this.<A>filterF().bind2(f);
    }

    public <A> Function2<SetF<A>, SetF<A>, SetF<A>> intersectF() {
        return SetF::intersect;
    }

    public <A> Function2<SetF<A>, SetF<A>, SetF<A>> plusF() {
        return SetF::plus;
    }

} //~
