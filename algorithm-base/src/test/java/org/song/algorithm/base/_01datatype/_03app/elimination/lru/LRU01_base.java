package org.song.algorithm.base._01datatype._03app.elimination.lru;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._03app.elimination.AbstractEliminate;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRU01_base {

    /*
    LRU (最近很少用的)
        算法题目概述
        运用你所掌握的数据结构, 设计和实现一个 LRU (最近最少使用) 缓存机制. 
        它应该支持以下操作:  获取数据 get 和 写入数据 put . 
        获取数据 get(key) - 如果key 存在于缓存中, 则获取密钥的值(总是正数), 否则返回 -1.  
        写入数据 put(key, value) - 如果密钥不存在, 则写入其数据值. 
        当缓存容量达到上限时, 它应该在写入新数据之前删除最近最少使用的数据值, 从而为新的数据值留出空间. 
    
    进阶: 你是否可以在 O(1) 时间复杂度内完成这两种操作?
    
    LRU 利用了缓存的时间局部性原理
    
     */
    
    /*
    整体的设计思路是, 
        可以使用 HashMap 存储 key, 这样可以做到 save 和 get key的时间都是 O(1), 
        而 HashMap 的 Value 指向双向链表实现的 LRU 的 Node 节点
    
    其中 head 代表双向链表的表头, tail 代表尾部. 
    首先预先设置 LRU 的容量, 如果存储满了, 可以通过 O(1) 的时间淘汰掉双向链表的尾部, 
    每次新增和访问数据, 都可以通过 O(1)的效率把新的节点增加到对头, 或者把已经存在的节点移动到队头. 
    
    总结一下核心操作的步骤:
        1. save(key, value), 首先在 HashMap 找到 Key 对应的节点, 
            如果节点存在, 更新节点的值, 并把这个节点移动队头. 
            如果不存在, 需要构造新的节点, 并且尝试把节点塞到队头, 
            如果LRU空间不足, 则通过 tail 淘汰掉队尾的节点, 同时在 HashMap 中移除 Key. 
        2. get(key), 通过 HashMap 找到 LRU 链表节点, 
            因为根据LRU 原理, 这个节点是最新访问的, 所以要把节点插入到队头, 然后返回缓存的值. 
            
     */

    /**
     *
     */
    @Test
    public void test_01() {
        LRULinkedHashMap<String, Object> lru = new LRULinkedHashMap<>(5);
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

    @Test
    public void test_02() {
        // 并发安全
        ConcurrentLRULinkedHashMap<String, Object> lru = new ConcurrentLRULinkedHashMap<>(5);
        lru.put("1", 1);
        new Thread(() -> {
            lru.put("5", 5);
        }).start();
        new Thread(() -> {
            lru.put("7", 7);
        }).start();

        System.out.println(lru);
        lru.put("4", 4);
        lru.put("3", 3);
        lru.put("8", 8);
        lru.put("6", 6);
        lru.put("2", 2);


    }

    /*
    采用 HashMap 和双向链表实现 LRU
    
     */
    @Test
    public void test_03() {
        LRUCache<String, Object> lru = new LRUCache<>(5);
        lru.putOrUpdate("1", 1);
        lru.putOrUpdate("5", 5);
        lru.putOrUpdate("7", 7);
        lru.putOrUpdate("4", 4);
        lru.putOrUpdate("3", 3);
        lru.putOrUpdate("8", 8);
        System.out.println(lru); // 迭代顺序, 就是插入顺序, 排序越靠前就越 old, 越容易被删除
        System.out.println(lru.get("1"));

        System.out.println(lru.get("3")); // 访问会调整它的顺序, 顺序会越靠后
        System.out.println(lru);

        lru.putOrUpdate("6", 6);
        lru.putOrUpdate("2", 2);
    }

    /**
     * 基于LinkedHashMap 实现LRU数据结构
     * LinkedHashMap 本身就是来用于实现LRU算法的数据结构, LRU主要用于删除不常用的元素,
     * 所以只需要指定最大容量和
     * removeEldestEntry 重写满足淘汰条件
     *
     * @param <K>
     * @param <V>
     */
    public static class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        /**
         * 最大容量
         * LRU淘汰的最大容量
         */
        private final int maxCapacity;

        private static final float DEFAULT_LOAD_FACTOR = 0.75f;

        /**
         * 初始化指定 最大容量 和 负载因子
         *
         * @param maxCapacity
         */
        public LRULinkedHashMap(int maxCapacity) {
            /*
            容量由用户指定
            负载因子默认 0.75f
            顺序是访问顺序
             */
            super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
            this.maxCapacity = maxCapacity;
        }

        /**
         * 是否满足淘汰条件
         *
         * @param eldest
         * @return
         */
        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
            return size() > maxCapacity;
        }
    }

    /**
     * 默认的 LRU(LinkedHashMap) 是非线程安全的, 
     * 所以如果想实现线程安全版本, 可以在所有操作上加锁
     *
     * @param <K>
     * @param <V>
     */
    public static class ConcurrentLRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        /**
         * 最大容量
         * LRU淘汰的最大容量
         */
        private final int maxCapacity;

        private static final float DEFAULT_LOAD_FACTOR = 0.75f;

        /**
         * 加个锁, 目的是实现线程安全的 LRU 数据结构
         */
        private final Lock lock = new ReentrantLock();

        public ConcurrentLRULinkedHashMap(int maxCapacity) {
            super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
            this.maxCapacity = maxCapacity;
        }

        /**
         * 是否满足淘汰条件
         *
         * @param eldest
         * @return
         */
        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
            return size() > maxCapacity;
        }

        @Override
        public boolean containsKey(Object key) {
            try {
                lock.lock();
                return super.containsKey(key);
            } finally {
                lock.unlock();
            }
        }


        @Override
        public V get(Object key) {
            try {
                lock.lock();
                return super.get(key);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public V put(K key, V value) {
            try {
                lock.lock();
                return super.put(key, value);
            } finally {
                lock.unlock();
            }
        }

        public int size() {
            try {
                lock.lock();
                return super.size();
            } finally {
                lock.unlock();
            }
        }

        public void clear() {
            try {
                lock.lock();
                super.clear();
            } finally {
                lock.unlock();
            }
        }

        public Collection<Map.Entry<K, V>> getAll() {
            try {
                lock.lock();
                return new ArrayList<Map.Entry<K, V>>(super.entrySet());
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * HashMap + 链表 实现
     */
    public static class LRUCache<K, V> extends AbstractEliminate<K, V> {

        protected HashMap<K, LRUNode<K, V>> cacheMaps;
        // 头节点
        protected LRUNode<K, V> first;
        // 尾节点
        protected LRUNode<K, V> last;

        public LRUCache(int size) {
            super(size);
            cacheMaps = new HashMap<>(size);
        }

        public V putOrUpdate(K k, V v) {
            LRUNode<K, V> exitNode = cacheMaps.get(k);
            if (exitNode == null) {
                // 删除旧值
                if (cacheMaps.size() >= capacity) {
                    // 如果超出范围, 则删除尾节点
                    // 从 HashMap 中删除尾节点
                    cacheMaps.remove(last.key);
                    // 从链表中删除尾节点
                    removeLast();
                }
                // 如果节点不存在, 则新建一个
                exitNode = new LRUNode<>();
                exitNode.key = k;
            }
            // 如果节点已存在, 则更新值
            exitNode.value = v;
            // 将节点移动到队首
            moveToFirst(exitNode);
            LRUNode<K, V> put = cacheMaps.put(k, exitNode);
            return put != null ? put.value : null;
        }

        public V putReturnEliminated(K k, V v) {
            LRUNode<K, V> node = cacheMaps.get(k);
            LRUNode<K, V> eliminateNode = null;
            if (node == null) {
                // 删除旧值
                if (cacheMaps.size() >= capacity) {
                    // 如果超出范围, 则删除尾节点
                    // 从 HashMap 中删除尾节点
                    eliminateNode = cacheMaps.remove(last.key);
                    // 从链表中删除尾节点
                    removeLast();
                }
                // 如果节点不存在, 则新建一个
                node = new LRUNode<>();
                node.key = k;
            }
            // 将节点移动到队首
            moveToFirst(node);
            cacheMaps.put(k, node);
            return eliminateNode != null ? eliminateNode.value : null;
        }

        public V get(K k) {
            LRUNode<K, V> node = getNode(k);
            if (node == null) return null;
            return node.value;
        }

        public LRUNode<K, V> getNode(K k) {
            LRUNode<K, V> node = cacheMaps.get(k);
            if (node == null) return null;
            moveToFirst(node);
            return node;
        }

        public V remove(K k) {
            LRUNode<K, V> node = cacheMaps.get(k);
            if (node == null) {
                return null;
            }
            removeNode(k);
            return node.value;
        }

        public LRUNode<K, V> removeNode(K k) {
            LRUNode<K, V> node = cacheMaps.get(k);
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
            node.pre = null;
            node.next = null;
            return node;
        }

        public void clear() {
            first = null;
            last = null;
            cacheMaps.clear();
        }
        
        public int size() {
            return cacheMaps.size();
        }

        /**
         * 将双向队列中的某个元素, 移动到对首
         *
         * @param node
         */
        protected void moveToFirst(LRUNode node) {
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
        protected void removeLast() {
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
            LRUNode node = first;
            while (node != null) {
                sb.append(String.format("%s:%s ", node.key, node.value));
                node = node.next;
            }

            return sb.toString();
        }
    }

    public static class LRUCacheModel<K, V> extends LRUCache<K, V> {

        public LRUCacheModel(int size) {
            super(size);
        }

        public LRUNode<K, V> putOrUpdate(LRUNode<K, V> node) {
            LRUNode<K, V> exitNode = cacheMaps.get(node.key);
            if (exitNode == null) {
                if (cacheMaps.size() >= capacity) {
                    cacheMaps.remove(last.key);
                    removeLast();
                }
                exitNode = node;
            }
            exitNode.value = node.value;
            exitNode.times = node.times;
            moveToFirst(node);
            return cacheMaps.put(node.key, node);
        }
    }
    
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LRUNode<K, V> {
        public LRUNode<K, V> pre;
        public LRUNode<K, V> next;
        public K key;
        public V value;

        public Position position; // 给SLRU使用
        public int times; // 用于LFU缓存的计数, LRU不用他

        public LRUNode(K key, V value, int times) {
            this.key = key;
            this.value = value;
            this.times = times;
        }
    }
    
    public enum Position {
        PROTECTION, PROBATION
    }
}
