package org.n3r.aoc.utils;

public class Tuple<T1, T2> {
    public T1 _1;
    public T2 _2;

    public Tuple(T1 t1, T2 t2) {
        this._1 = t1;
        this._2 = t2;
    }

    public static <T1, T2> Tuple<T1, T2> make(T1 t1, T2 t2) {
        return new Tuple<T1, T2>(t1, t2);
    }

    @Override
    public String toString() {
        return "Tuple{" + _1 + "," + _2 + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple tuple = (Tuple) o;

        if (_1 != null ? !_1.equals(tuple._1) : tuple._1 != null) return false;
        if (_2 != null ? !_2.equals(tuple._2) : tuple._2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _1 != null ? _1.hashCode() : 0;
        result = 31 * result + (_2 != null ? _2.hashCode() : 0);
        return result;
    }
}
