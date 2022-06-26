package org.song.algorithm.base._01datatype._02high.unionfindsets;

public interface UFS<T> {

    T findRoot(T n);
    
    void union(T n1, T n2);

    boolean isSame(T t1, T t2);
}
