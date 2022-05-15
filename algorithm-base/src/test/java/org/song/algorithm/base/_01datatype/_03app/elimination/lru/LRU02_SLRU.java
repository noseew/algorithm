package org.song.algorithm.base._01datatype._03app.elimination.lru;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._03app.elimination.AbstractEliminate;

public class LRU02_SLRU {


    @Test
    public void test_01() {
        SLRUCache<String, Object> lru = new SLRUCache<>(5);
        lru.putOrUpdate("1", 1);
        lru.putOrUpdate("5", 5);
        lru.putOrUpdate("7", 7);
        System.out.println(lru.get("1"));
        System.out.println(lru.get("5"));
        lru.putOrUpdate("4", 4);
        lru.putOrUpdate("3", 3);
        System.out.println(lru.get("7"));

        System.out.println();

    }
    
    /*
    SLRU被分为两个段, 试用段和保护段. 新数据会被加到试用段里. 
    如果试用段或者保护段的数据再次被命中, 那么数据会被加入到保护段的头部. 
    保护段的大小是有限的. 如果需要清除数据, 那么数据会从保护段的末尾开始清除. 
    
    
    似乎相当于 LRUK k=2, 未完成 TODO
    
     */
    
    public static class SLRUCache<K, V> extends AbstractEliminate<K, V> {

        // 考察期
        private LRU01_base.LRUCache<K, V> probation;
        // 保护期
        private LRU01_base.LRUCache<K, V> protection;

        protected SLRUCache(int capacity) {
            super(capacity);
            probation = new LRU01_base.LRUCache<>(capacity);
            protection = new LRU01_base.LRUCache<>(capacity);
        }

        @Override
        public V get(K k) {
            V exitNode = probation.get(k);
            if (exitNode == null) {
                return protection.get(k);
            }
            V v = probation.remove(k);
            // 如果观察组存在, 说明是2次访问, 则直接进入保护区
            protection.putOrUpdate(k, v);
            return v;
        }

        @Override
        public V putOrUpdate(K k, V v) {
            // 优先存储在 观察组
            V exitNode = probation.get(k);
            if (exitNode == null) {
                exitNode = protection.get(k);
                if (exitNode == null) {
                    return probation.putOrUpdate(k, v);
                } else {
                    return protection.putOrUpdate(k, v);
                }
            }
            probation.remove(k);
            // 如果观察组存在, 说明是2次访问, 则直接进入保护区
            return protection.putOrUpdate(k, v);
        }

        @Override
        public V remove(K k) {
            V v = probation.remove(k);
            if (v == null) {
                v = protection.remove(k);
            }
            return v;
        }
    }
}
