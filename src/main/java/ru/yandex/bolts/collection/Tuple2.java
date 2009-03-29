package ru.yandex.bolts.collection;

import java.io.Serializable;

import ru.yandex.bolts.function.Function;
import ru.yandex.bolts.function.Function2;

/**
 * Pair
 *
 * @author Stepan Koltsov
 * 
 * @see Tuple3
 * @see Tuple4
 */
public class Tuple2<T1, T2> implements Serializable {
    private static final long serialVersionUID = 3326001902955370713L;
    
    private final T1 _1;
    private final T2 _2;

    public Tuple2(T1 _1, T2 _2) {
        this._1 = _1;
        this._2 = _2;
    }

    /** First */
    public T1 get1() {
        return _1;
    }

    /** Second */
    public T2 get2() {
        return _2;
    }

    public <R> R reduce(Function2<T1, T2, R> bf) {
        return bf.apply(_1, _2);
    }

    public Tuple2<T2, T1> swap() {
        return tuple(_2, _1);
    }
    
    @SuppressWarnings("unchecked")
    public <T3, T4> Tuple2<T3, T4> uncheckedCast() {
        return (Tuple2<T3, T4>) this;
    }

    @SuppressWarnings({"RedundantIfStatement"})
    public boolean equals(Object o) {
        // generated by Idea
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;

        if (_1 != null ? !_1.equals(tuple2._1) : tuple2._1 != null) return false;
        if (_2 != null ? !_2.equals(tuple2._2) : tuple2._2 != null) return false;

        return true;
    }

    public int hashCode() {
        // generated by Idea
        int result = (_1 != null ? _1.hashCode() : 0);
        result = 31 * result + (_2 != null ? _2.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "(" + _1 + ", " + _2 + ")";
    }

    public static <A, B> Function2<A, B, Tuple2<A, B>> consM() {
        return new Function2<A, B, Tuple2<A, B>>() {
            public Tuple2<A, B> apply(A a, B b) {
                return tuple(a, b);
            }
        };
    }

    public static <A, B> Function<Tuple2<A, B>, A> get1M() {
        return new Function<Tuple2<A, B>, A>() {
            public A apply(Tuple2<A, B> tuple) {
                return tuple._1;
            }
        };
    }

    public static <A, B> Function<Tuple2<A, B>, B> get2M() {
        return new Function<Tuple2<A, B>, B>() {
            public B apply(Tuple2<A, B> tuple) {
                return tuple._2;
            }
        };
    }

    public static <A, B> Function<Tuple2<A, B>, Tuple2<B, A>> swapM() {
        return new Function<Tuple2<A, B>, Tuple2<B, A>>() {
            public Tuple2<B, A> apply(Tuple2<A, B> tuple) {
                return tuple.swap();
            }
        };
    }

    public static <A, B, C> Function<Tuple2<A, B>, Tuple2<C, B>> map1M(final Function<A, C> m) {
        return new Function<Tuple2<A, B>, Tuple2<C, B>>() {
            public Tuple2<C, B> apply(Tuple2<A, B> tuple) {
                return tuple(m.apply(tuple._1), tuple._2);
            }
        };
    }

    public static <A, B, C> Function<Tuple2<A, B>, Tuple2<A, C>> map2M(final Function<B, C> m) {
        return new Function<Tuple2<A, B>, Tuple2<A, C>>() {
            public Tuple2<A, C> apply(Tuple2<A, B> tuple) {
                return tuple(tuple._1, m.apply(tuple._2));
            }
        };
    }
    
    /** Construct */
    public static <A, B> Tuple2<A, B> tuple(A a, B b) {
        return new Tuple2<A, B>(a, b);
    }
    
} //~
