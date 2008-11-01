package ru.yandex.bolts.collection.impl.test;

import java.util.List;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.function.forhuman.Mapper;
import ru.yandex.bolts.function.forhuman.Operation;

import net.java.quickcheck.Characteristic;
import net.java.quickcheck.Generator;
import net.java.quickcheck.QuickCheck;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import net.java.quickcheck.generator.CombinedGenerators;
import net.java.quickcheck.generator.PrimitiveGenerators;

/**
 * @author Stepan Koltsov
 */
public abstract class GeneratorF<T> implements Generator<T> {
    
    private GeneratorF<T> self() { return this; }
    
    public <U> GeneratorF<U> map(final Mapper<T, U> f) {
        return new GeneratorF<U>() {
            public U next() {
                return f.map(self().next());
            }
        };
    }
    
    public GeneratorF<ListF<T>> lists() {
        return x(CombinedGenerators.lists(this)).map(new Mapper<List<T>, ListF<T>>() {
            public ListF<T> map(List<T> a) {
                return Cf.x(a);
            }
        });
    }
    
    public GeneratorF<SetF<T>> sets() {
        return lists().map(new Mapper<ListF<T>, SetF<T>>() {
            public SetF<T> map(ListF<T> a) {
                return a.unique();
            }
        });
    }
    
    public <U> GeneratorF<Tuple2<T, U>> tuples(final GeneratorF<U> g) {
        return new GeneratorF<Tuple2<T,U>>() {
            public Tuple2<T, U> next() {
                return Tuple2.tuple(self().next(), g.next());
            }
        };
    }
    
    public GeneratorF<Tuple2<T, T>> tuples() {
        return tuples(this);
    }
    
    
    public void checkForAllVerbose(Characteristic<T> ch) {
        QuickCheck.forAllVerbose(this, ch);
    }
    
    public void checkForAllVerbose(final Operation<T> ch) {
        checkForAllVerbose(new AbstractCharacteristic<T>() {
            protected void doSpecify(T t) throws Throwable {
                ch.apply(t);
            }
        });
    }
    
    
    public static <T> GeneratorF<T> x(final Generator<T> g) {
        return new GeneratorF<T>() {
            public T next() {
                return g.next();
            }
        };
    }
    
    public static GeneratorF<Integer> integers() {
        return x(PrimitiveGenerators.integers());
    }
    
    public static GeneratorF<Integer> integers(int a, int b) {
        return x(PrimitiveGenerators.integers(a, b));
    }
    
} //~
