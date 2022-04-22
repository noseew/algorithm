package org.song.algorithm.base._01datatype._03app.elimination.lru;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class LRULinkedTest02 {

    /*
    采用 HashMap 和双向链表实现 LRU
    
     */
    @Test
    public void test_01() {
        LRUCache<String, Object> lru = new LRUCache<>(5);
        lru.put("1", 1);
        lru.put("5", 5);
        lru.put("7", 7);
        lru.put("4", 4);
        lru.put("3", 3);
        lru.put("8", 8);
        System.out.println(lru); // 迭代顺序, 就是插入顺序, 排序越靠前就越 old, 越容易被删除
        System.out.println(lru.get("1"));

        System.out.println(lru.get("3")); // 访问会调整它的顺序, 顺序会越靠后
        System.out.println(lru);

        lru.put("6", 6);
        lru.put("2", 2);
    }
    
    public static class LRUCache<K, V> {

        private int currentCacheSize;
        private int capacity;
        private HashMap<K, CacheNode> cacheMaps;
        // 头节点
        private CacheNode first;
        // 尾节点
        private CacheNode last;

        public LRUCache(int size) {
            currentCacheSize = 0;
            this.capacity = size;
            cacheMaps = new HashMap<K, CacheNode>(size);
        }

        public void put(K k, V v) {
            CacheNode node = cacheMaps.get(k);
            if (node == null) {
                // 删除旧值
                if (cacheMaps.size() >= capacity) {
                    // 如果超出范围, 则删除尾节点
                    // 从 HashMap 中删除尾节点
                    cacheMaps.remove(last.key);
                    // 从链表中删除尾节点
                    removeLast();
                }
                // 如果节点不存在, 则新建一个
                node = new CacheNode();
                node.key = k;
            }
            // 如果节点已存在, 则更新值
            node.value = v;
            // 将节点移动到队首
            moveToFirst(node);
            cacheMaps.put(k, node);
        }

        public Object get(K k) {
            CacheNode node = cacheMaps.get(k);
            if (node == null) {
                return null;
            }
            // 将节点移动到队首
            moveToFirst(node);
            return node.value;
        }

        public Object remove(K k) {
            CacheNode node = cacheMaps.get(k);
            if (node != null) {
                if (node.pre != null) {
                    node.pre.next = node.next;
                }
                if (node.next != null) {
                    node.next.pre = node.pre;
                }
                if (node == first) {
                    first = node.next;
                }
                if (node == last) {
                    last = node.pre;
                }
            }

            return cacheMaps.remove(k);
        }

        public void clear() {
            first = null;
            last = null;
            cacheMaps.clear();
        }

        /**
         * 将双向队列中的某个元素, 移动到对首
         * 
         * @param node
         */
        private void moveToFirst(CacheNode node) {
            // 如果已经是队首, 则不处理
            if (first == node) {
                return;
            }
            // 将当前节点从 双向链表中摘出
            if (node.next != null) {
                node.next.pre = node.pre;
            }
            if (node.pre != null) {
                node.pre.next = node.next;
            }
            // 如果是队尾, 则更新last
            if (node == last) {
                last = last.pre;
            }
            if (first == null || last == null) {
                first = last = node;
                return;
            }

            node.next = first;
            first.pre = node;
            first = node;
            first.pre = null;

        }

        /**
         * 删除双向链表尾节点
         */
        private void removeLast() {
            if (last != null) {
                // 将尾节点 指针重置指向前一个
                last = last.pre;
                if (last == null) {
                    first = null;
                } else {
                    // 同时将真正尾节点值空
                    last.next = null;
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            CacheNode node = first;
            while (node != null) {
                sb.append(String.format("%s:%s ", node.key, node.value));
                node = node.next;
            }

            return sb.toString();
        }

        public static class CacheNode {
            public CacheNode pre;
            public CacheNode next;
            public Object key;
            public Object value;

            public int times; // 用于LFU缓存的计数, LRU不用他
        }
    }
}
