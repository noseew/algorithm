package org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model;

import lombok.AllArgsConstructor;

public abstract class AbstractMap<K, V> {

    protected AbstractMap() {
        
    }

    protected AbstractMap(int capacity) {
        
    }

    public abstract V get(K k);

    public abstract V put(K k, V v);

    public abstract V remove(K k);

    @AllArgsConstructor
    protected static class Entry<K, V> {
        K k;
        V val;
        Entry<K, V> next;
        // 新增一个指针, 不需要重复计算了, 不同的实现方式, 可能用不到这个字段
        int hash;

        public Entry(K k, V val, Entry<K, V> next) {
            this.k = k;
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            return k + "=>" + val ;
        }
    }
}
