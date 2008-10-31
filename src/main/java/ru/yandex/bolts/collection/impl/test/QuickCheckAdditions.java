package ru.yandex.bolts.collection.impl.test;

import java.util.List;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.CombinedGenerators;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.function.forhuman.Mapper;

/**
 * Not included into bolts distribution.
 * 
 * @author Stepan Koltsov
 */
public class QuickCheckAdditions {
    
    public static <A> Generator<ListF<A>> lists(Generator<A> g) {
        return map(CombinedGenerators.lists(g), new Mapper<List<A>, ListF<A>>() {
            public ListF<A> map(List<A> a) {
                return Cf.x(a);
            }
        });
    }
    
    public static <A> Generator<SetF<A>> sets(Generator<A> g) {
        return map(lists(g), new Mapper<ListF<A>, SetF<A>>() {
            public SetF<A> map(ListF<A> a) {
                return a.unique();
            }
        });
    }
    
    public static <A, B> Generator<B> map(final Generator<A> g, final Mapper<A, B> m) {
        return new Generator<B>() {
            public B next() {
                return m.apply(g.next());
            }
        };
    }
    
} //~
