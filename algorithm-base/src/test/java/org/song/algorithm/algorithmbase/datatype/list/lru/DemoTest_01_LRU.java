package org.song.algorithm.algorithmbase.datatype.list.lru;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DemoTest_01_LRU {

    /*
    LRU 算法题目概述
        运用你所掌握的数据结构, 设计和实现一个 LRU (最近最少使用) 缓存机制. 
        它应该支持以下操作:  获取数据 get 和 写入数据 put . 
        获取数据 get(key) - 如果key 存在于缓存中, 则获取密钥的值(总是正数), 否则返回 -1.  
        写入数据 put(key, value) - 如果密钥不存在, 则写入其数据值. 
        当缓存容量达到上限时, 它应该在写入新数据之前删除最近最少使用的数据值, 从而为新的数据值留出空间. 
    
    进阶: 你是否可以在 O(1) 时间复杂度内完成这两种操作?
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
        lru.put("6", 6);
        lru.put("2", 2);
        
        
        
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
    public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
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
    public class ConcurrentLRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
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

}
