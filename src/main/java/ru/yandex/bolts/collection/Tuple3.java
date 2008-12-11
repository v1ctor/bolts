package ru.yandex.bolts.collection;

import java.io.Serializable;

/**
 * @author Stepan Koltsov
 * 
 * @see Tuple2
 */
public class Tuple3<T1, T2, T3> implements Serializable {
    private static final long serialVersionUID = 2341548432691171988L;
    
    private final T1 t1;
    private final T2 t2;
    private final T3 t3;
    
    public Tuple3(T1 t1, T2 t2, T3 t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }

    /** First */
    public T1 get1() {
        return t1;
    }

    /** Second */
    public T2 get2() {
        return t2;
    }

    /** Third */
    public T3 get3() {
        return t3;
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
        result = prime * result + ((t1 == null) ? 0 : t1.hashCode());
        result = prime * result + ((t2 == null) ? 0 : t2.hashCode());
        result = prime * result + ((t3 == null) ? 0 : t3.hashCode());
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
        if (t1 == null) {
            if (other.t1 != null) return false;
        } else if (!t1.equals(other.t1)) return false;
        if (t2 == null) {
            if (other.t2 != null) return false;
        } else if (!t2.equals(other.t2)) return false;
        if (t3 == null) {
            if (other.t3 != null) return false;
        } else if (!t3.equals(other.t3)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "(" + t1 + ", " + t2 + ", " + t3 + ")";
    }
    
    /** Construct */
    public static <A, B, C> Tuple3<A, B, C> tuple(A a, B b, C c) {
        return new Tuple3<A, B, C>(a, b, c);
    }
    
} //~
