package org.song.algorithm.base._01datatype._03app.elimination;

/**
 * 淘汰算法
 * 
 * @param <K>
 * @param <V>
 */
public abstract class AbstractEliminate<K, V> {

    // 容量
    protected int capacity;
    // 数量
    protected int size;
    
    protected AbstractEliminate(int capacity) {
        this.capacity = capacity;
    }

    public abstract V get(K k);

    public abstract V put(K k, V v);

    public abstract V remove(K k);

    public int size() {
        return size;
    }

}
