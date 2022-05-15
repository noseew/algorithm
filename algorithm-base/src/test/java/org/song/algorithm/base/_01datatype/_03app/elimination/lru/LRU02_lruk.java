package org.song.algorithm.base._01datatype._03app.elimination.lru;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._03app.elimination.AbstractEliminate;

import java.util.HashMap;

public class LRU02_lruk {
    
    /*
    LRU-K中的K代表最近使用的次数, 因此LRU可以认为是LRU-1. LRU-K的主要目的是为了解决LRU算法"缓存污染"的问题, 其核心思想是将"最近使用过1次"的判断标准扩展为"最近使用过K次". 
    
    相比LRU, LRU-K需要多维护一个队列, 用于记录所有缓存数据被访问的历史. 只有当数据的访问次数达到K次的时候, 才将数据放入缓存. 当需要淘汰数据时, LRU-K会淘汰第K次访问时间距当前时间最大的数据. 
    
    1. 数据第一次被访问, 加入到访问历史列表;
    2. 如果数据在访问历史列表里后没有达到K次访问, 则按照一定规则(FIFO, LRU)淘汰;
    3. 当访问历史队列中的数据访问次数达到K次后, 将数据索引从历史队列删除, 将数据移到缓存队列中, 并缓存此数据, 缓存队列重新按照时间排序;
    4. 缓存数据队列中被再次访问后, 重新排序;
    5. 需要淘汰数据时, 淘汰缓存队列中排在末尾的数据, 即: 淘汰"倒数第K次访问离现在最久"的数据. 
    LRU-K具有LRU的优点, 同时能够避免LRU的缺点, 实际应用中LRU-2是综合各种因素后最优的选择, LRU-3或者更大的K值命中率会高, 但适应性差, 需要大量的数据访问才能将历史访问记录清除掉. 
     */

    @Test
    public void test_01() {
        LURKCache<String, Object> lru = new LURKCache<>(2, 3);
        lru.putOrUpdate("1", 1);
        lru.putOrUpdate("5", 5);
        lru.putOrUpdate("7", 7);
        System.out.println(lru.get("1")); // null, 被移除了
        System.out.println(lru.get("5")); // 此时进入缓存
        lru.putOrUpdate("4", 4);
        lru.putOrUpdate("3", 3);
        System.out.println(lru.get("7")); // null, 被移除了

        System.out.println();
        
    }

    public static class LURKCache<K, V> extends AbstractEliminate<K, V> {

        // 访问历史记录缓存
        private LRUCache<K, V> timesMaps;
        // 数据缓存
        private LRUCache<K, V> cacheMaps;

        private int timesK;

        public LURKCache(int timesK, int cap) {
            super(cap);
            this.timesK = timesK;
            timesMaps = new LRUCache<>(timesK);
            cacheMaps = new LRUCache<>(cap);
        }

        public V putOrUpdate(K k, V v) {
            CacheNode<K, V> exitNode = timesMaps.get(k);
            if (exitNode == null) {
                timesMaps.putNode(new CacheNode<>(k, v, 1));
                return null;
            }
            exitNode.times++;
            V oldVal = exitNode.value;
            exitNode.value = v;

            if (exitNode.times >= timesK) {
                return cacheMaps.putNode(exitNode);
            }
            return oldVal;
        }

        public V get(K k) {
            CacheNode<K, V> exitNode = timesMaps.get(k);
            if (exitNode != null) {
                exitNode.times++;
                if (exitNode.times >= timesK) {
                    timesMaps.remove(k);
                    cacheMaps.putNode(exitNode);
                }
                return exitNode.value;
            }
            exitNode = cacheMaps.get(k);
            if (exitNode != null) {
                return exitNode.value;
            }
            return null;
        }

        public V remove(K k) {
            CacheNode<K, V> exitNode = timesMaps.remove(k);
            if (exitNode != null) {
                return exitNode.value;
            }
            exitNode = cacheMaps.remove(k);
            if (exitNode != null) {
                return exitNode.value;
            }
            return null;
        }

        public void clear() {
            timesMaps.clear();
            cacheMaps.clear();
        }

        public int size() {
            return cacheMaps.size();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("k=").append(timesK).append("\r\n");
            sb.append("data=").append(cacheMaps).append("\r\n");
            sb.append("history=").append(timesMaps).append("\r\n");
            return sb.toString();
        }
    }

    @AllArgsConstructor
    public static class CacheNode<K, V> {
        public CacheNode<K, V> pre;
        public CacheNode<K, V> next;
        public K key;
        public V value;
        public int times; // 缓存访问次数

        public CacheNode(K key, V value, int times) {
            this.key = key;
            this.value = value;
            this.times = times;
        }
    }

    public static class LRUCache<K, V> {

        private int capacity;
        private HashMap<K, CacheNode<K, V>> cacheMaps;
        private CacheNode<K, V> first;
        private CacheNode<K, V> last;

        public LRUCache(int size) {
            this.capacity = size;
            cacheMaps = new HashMap<K, CacheNode<K, V>>(size);
        }

        public V putNode(CacheNode<K, V> node) {
            CacheNode<K, V> exitNode = cacheMaps.get(node.key);
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
            CacheNode<K, V> put = cacheMaps.put(node.key, node);
            return put != null ? put.value : null;
        }

        public CacheNode<K, V> get(K k) {
            CacheNode<K, V> node = cacheMaps.get(k);
            if (node == null) {
                return null;
            }
            moveToFirst(node);
            return node;
        }

        public CacheNode<K, V> remove(K k) {
            CacheNode<K, V> node = cacheMaps.get(k);
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
                node.pre = null;
                node.next = null;
            }

            return cacheMaps.remove(k);
        }

        public void clear() {
            first = null;
            last = null;
            cacheMaps.clear();
        }

        public int size() {
            return cacheMaps.size();
        }

        private void moveToFirst(CacheNode node) {
            if (first == node) {
                return;
            }
            if (node.next != null) {
                node.next.pre = node.pre;
            }
            if (node.pre != null) {
                node.pre.next = node.next;
            }
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

        private void removeLast() {
            if (last != null) {
                last = last.pre;
                if (last == null) {
                    first = null;
                } else {
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
    }
}
