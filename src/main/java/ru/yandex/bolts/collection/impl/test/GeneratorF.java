package ru.yandex.bolts.collection.impl.test;

import java.util.Iterator;

import net.java.quickcheck.Characteristic;
import net.java.quickcheck.Generator;
import net.java.quickcheck.QuickCheck;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import net.java.quickcheck.generator.CombinedGenerators;
import net.java.quickcheck.generator.PrimitiveGenerators;

import ru.yandex.bolts.collection.Cf;
import ru.yandex.bolts.collection.ListF;
import ru.yandex.bolts.collection.SetF;
import ru.yandex.bolts.collection.Tuple2;
import ru.yandex.bolts.collection.impl.AbstractIteratorF;
import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function1B;
import ru.yandex.bolts.function.Function1V;

/**
 * @author Stepan Koltsov
 */
public abstract class GeneratorF<T> extends AbstractIteratorF<T> implements Generator<T> {
    
    @Override
    public final boolean hasNext() {
        return true;
    }

    private GeneratorF<T> self() { return this; }
    
    @Override
    public <B> GeneratorF<B> map(Function<? super T, B> f) {
        return x(super.map(f));
    }
    
    @Override
    public GeneratorF<T> filter(Function1B<? super T> f) {
        return x(super.filter(f));
    }

    public GeneratorF<ListF<T>> lists() {
        return x(CombinedGenerators.lists(this)).map(Cf.<T>wrapListM());
    }

    public GeneratorF<ListF<T>> lists(GeneratorF<Integer> lengths) {
        return x(CombinedGenerators.lists(this, lengths)).map(Cf.<T>wrapListM());
    }
    
    public GeneratorF<SetF<T>> sets() {
        return lists().map(new Function<ListF<T>, SetF<T>>() {
            public SetF<T> apply(ListF<T> a) {
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
    
    public void checkForAllVerbose(final Function1V<T> ch) {
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
    
    public static <T> GeneratorF<T> x(final Iterator<T> g) {
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
    
    public static GeneratorF<Character> characters() {
        return x(PrimitiveGenerators.characters('a', 'z'));
    }
    
    public static GeneratorF<String> strings() {
        return x(PrimitiveGenerators.strings(characters()));
    }
    
} //~
