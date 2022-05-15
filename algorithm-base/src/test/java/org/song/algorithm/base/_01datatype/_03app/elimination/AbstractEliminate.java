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

    /**
     * 获取缓存
     * 
     * @param k
     * @return
     */
    public abstract V get(K k);

    /**
     * 存入或更新
     * 
     * @param k
     * @param v
     * @return
     */
    public abstract V putOrUpdate(K k, V v);

    /**
     * 存入并返回淘汰的缓存
     * 
     * @param k
     * @param v
     * @return
     */
    public abstract V putReturnEliminated(K k, V v);

    /**
     * 删除一个缓存
     * 
     * @param k
     * @return
     */
    public abstract V remove(K k);

    public int size() {
        return size;
    }

}
