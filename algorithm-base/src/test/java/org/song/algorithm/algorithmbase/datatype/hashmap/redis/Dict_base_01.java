package org.song.algorithm.algorithmbase.datatype.hashmap.redis;

/**
 * 实现简单功能的 dict, 模仿redis中的dict
 *
 * @param <K>
 * @param <V>
 */
public class Dict_base_01<K, V> {

    private Entry<K, V>[] datas;

    class Entry<K, V> {

        K k;
        V val;
        Entry<K, V> next;

        public Entry(K k, V val, Entry<K, V> next) {
            this.k = k;
            this.val = val;
            this.next = next;
        }
    }
}
