package org.song.algorithm.algorithmbase._01datatype._02high.hashmap.jdk;

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
    }
}
