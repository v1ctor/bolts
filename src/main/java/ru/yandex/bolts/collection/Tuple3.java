package ru.yandex.bolts.collection;

import java.io.Serializable;

import ru.yandex.bolts.function.Function;

/**
 * @author Stepan Koltsov
 * 
 * @see Tuple2
 */
public class Tuple3<T1, T2, T3> implements Serializable {
    private static final long serialVersionUID = 2341548432691171988L;
    
    private final T1 _1;
    private final T2 _2;
    private final T3 _3;
    
    public Tuple3(T1 _1, T2 _2, T3 _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }

    /** First */
    public T1 get1() {
        return _1;
    }

    /** Second */
    public T2 get2() {
        return _2;
    }

    /** Third */
    public T3 get3() {
        return _3;
    }
    
    public Tuple2<T1, T2> get12() {
        return Tuple2.tuple(_1, _2);
    }
    
    public Tuple2<T2, T3> get23() {
        return Tuple2.tuple(_2, _3);
    }
    
    public Tuple2<T1, T3> get13() {
        return Tuple2.tuple(_1, _3);
    }

    @SuppressWarnings("unchecked")
    public <U1, U2, U3> Tuple3<U1, U2, U3> uncheckedCast() {
        return (Tuple3<U1, U2, U3>) this;
    }

    @Override
    public int hashCode() {
        // generated by Eclipse
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_1 == null) ? 0 : _1.hashCode());
        result = prime * result + ((_2 == null) ? 0 : _2.hashCode());
        result = prime * result + ((_3 == null) ? 0 : _3.hashCode());
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        // generated by Eclipse
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Tuple3 other = (Tuple3) obj;
        if (_1 == null) {
            if (other._1 != null) return false;
        } else if (!_1.equals(other._1)) return false;
        if (_2 == null) {
            if (other._2 != null) return false;
        } else if (!_2.equals(other._2)) return false;
        if (_3 == null) {
            if (other._3 != null) return false;
        } else if (!_3.equals(other._3)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ")";
    }
    
    public static <A, B, C> Function<Tuple3<A, B, C>, A> get1M() {
        return new Function<Tuple3<A, B, C>, A>() {
            public A apply(Tuple3<A, B, C> tuple) {
                return tuple._1;
            }
        };
    }

    public static <A, B, C> Function<Tuple3<A, B, C>, B> get2M() {
        return new Function<Tuple3<A, B, C>, B>() {
            public B apply(Tuple3<A, B, C> tuple) {
                return tuple._2;
            }
        };
    }

    public static <A, B, C> Function<Tuple3<A, B, C>, C> get3M() {
        return new Function<Tuple3<A, B, C>, C>() {
            public C apply(Tuple3<A, B, C> tuple) {
                return tuple._3;
            }
        };
    }
    
    public static <A, B, C, X> Function<Tuple3<A, B, C>, Tuple3<X, B, C>> map1M(final Function<A, X> m) {
        return new Function<Tuple3<A, B, C>, Tuple3<X, B, C>>() {
            public Tuple3<X, B, C> apply(Tuple3<A, B, C> t) {
                return tuple(m.apply(t._1), t._2, t._3);
            }
        };
    }

    public static <A, B, C, X> Function<Tuple3<A, B, C>, Tuple3<A, X, C>> map2M(final Function<B, X> m) {
        return new Function<Tuple3<A, B, C>, Tuple3<A, X, C>>() {
            public Tuple3<A, X, C> apply(Tuple3<A, B, C> t) {
                return tuple(t._1, m.apply(t._2), t._3);
            }
        };
    }

    public static <A, B, C, X> Function<Tuple3<A, B, C>, Tuple3<A, B, X>> map3M(final Function<C, X> m) {
        return new Function<Tuple3<A, B, C>, Tuple3<A, B, X>>() {
            public Tuple3<A, B, X> apply(Tuple3<A, B, C> t) {
                return tuple(t._1, t._2, m.apply(t._3));
            }
        };
    }
    
    // ?
    
    public static <A, B, C> Function<Tuple3<A, B, C>, Tuple2<A, B>> get12M() {
        return new Function<Tuple3<A, B, C>, Tuple2<A, B>>() {
            public Tuple2<A, B> apply(Tuple3<A, B, C> a) {
                return a.get12();
            }
        };
    }

    public static <A, B, C> Function<Tuple3<A, B, C>, Tuple2<A, C>> get13M() {
        return new Function<Tuple3<A, B, C>, Tuple2<A, C>>() {
            public Tuple2<A, C> apply(Tuple3<A, B, C> a) {
                return a.get13();
            }
        };
    }

    public static <A, B, C> Function<Tuple3<A, B, C>, Tuple2<B, C>> get23M() {
        return new Function<Tuple3<A, B, C>, Tuple2<B, C>>() {
            public Tuple2<B, C> apply(Tuple3<A, B, C> a) {
                return a.get23();
            }
        };
    }
    
    /** Construct */
    public static <A, B, C> Tuple3<A, B, C> tuple(A a, B b, C c) {
        return new Tuple3<A, B, C>(a, b, c);
    }
    
} //~
