package org.song.algorithm.base._01datatype._03app.elimination.lru;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._03app.elimination.AbstractEliminate;

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
        private LRU01_base.LRUCache4LRUK<K, V> timesMaps;
        // 数据缓存
        private LRU01_base.LRUCache4LRUK<K, V> cacheMaps;

        private int timesK;

        public LURKCache(int timesK, int cap) {
            super(cap);
            this.timesK = timesK;
            timesMaps = new LRU01_base.LRUCache4LRUK<>(timesK);
            cacheMaps = new LRU01_base.LRUCache4LRUK<>(cap);
        }

        public V putOrUpdate(K k, V v) {
            LRU01_base.CacheNode<K, V> exitNode = timesMaps.get(k);
            if (exitNode == null) {
                timesMaps.putNode(new LRU01_base.CacheNode<>(k, v, 1));
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

        @Override
        public V putReturnEliminated(K k, V v) {
            return null;
        }

        public V get(K k) {
            LRU01_base.CacheNode<K, V> exitNode = timesMaps.get(k);
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
            LRU01_base.CacheNode<K, V> exitNode = timesMaps.remove(k);
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
}
