package org.song.algorithm.base._02alg.classical.elimination.lfu;

import org.song.algorithm.base._02alg.classical.counter.CountMinSketch_test;
import org.song.algorithm.base._02alg.classical.elimination.lru.LRU01_base;
import org.song.algorithm.base._02alg.classical.elimination.lru.LRU02_SLRU;
import org.song.algorithm.base._01datatype._02high.bloom.BloomFilter;

import java.util.HashMap;
import java.util.Map;

public class LFU05_WTinyLFU {
    
    /*
    caffeine 缓存淘汰算法
    
    https://mp.weixin.qq.com/s/tKwPaYEKY0SkTglpl_l6dw
    https://segmentfault.com/a/1190000008751999
    https://www.51cto.com/article/603434.html
    https://blog.csdn.net/yingyujianmo/article/details/122755222
     */

    static class WindowsTinyLFU<K, V> {
        LRU01_base.LRUCache<K, V> wlru;
        LRU02_SLRU.SLRUCache<K, V> slru;
        BloomFilter bf; // 看门人, 防止尾部数据占用 cmSketch 的计数
        CountMinSketch_test.CountMinSketch<K> cmSketch;
        /**
         * 所有数据的索引
         * 会记录数据所在的区域
         */
        Map<K, LRU01_base.LRUNode<K, V>> dataMap;
        int totalVisit = 0;
        int threshold = 100;    // 保鲜机制

        public WindowsTinyLFU(int capacity) {
            wlru = new LRU01_base.LRUCache<>(capacity / 100);
            slru = new LRU02_SLRU.SLRUCache<>(capacity - (capacity / 100));
            bf = new BloomFilter(capacity, capacity);
            cmSketch = new CountMinSketch_test.CountMinSketch<>(capacity);
            dataMap = new HashMap<>(capacity);

        }

        /**
         * 主缓存使用SLRU驱逐策略和TinyLFU允许策略，而窗口缓存使用LRU驱逐策略，而不使用任何允许策略。
         * SLRU策略在主缓存中的A1和A2区域被静态划分，因此80%的空间被分配给热项(A2)，被驱逐者则从20%的非热项(A1)中选取。
         * 任何到达的项总是允许进入窗口缓存，而窗口缓存的被驱逐者也有机会进入主缓存。
         * 如果它被承认，那么W-TinyLFU的被驱逐者就是主缓存的被驱逐者，否则就是窗口缓存的被驱逐者
         *
         * @param k
         * @param v
         * @return
         */
        public V put(K k, V v) {
            LRU01_base.LRUNode<K, V> newNode = new LRU01_base.LRUNode<>(k, v);
            newNode.position = LRU01_base.Position.WINDOWS_LRU;
            // 将数据放入wlru，如果wlru没满，则直接返回
            LRU01_base.LRUNode<K, V> wlruEliminated = wlru.putReturnEliminated(newNode);
            if (wlruEliminated == null) {
                dataMap.put(k, newNode);
                return null;
            }
            // 如果此时发生了淘汰，将淘汰节点删去
            else if (dataMap.get(wlruEliminated.key).position == LRU01_base.Position.WINDOWS_LRU) {
                dataMap.remove(wlruEliminated.key);
            }

            //如果slru没满，则插入到slru中。
            newNode.position = LRU01_base.Position.SLRU;
            LRU01_base.LRUNode<K, V> victim = slru.victim();
            if (victim == null) {
                dataMap.put(k, newNode);
                slru.putNode(newNode);
                return null;
            }

            //如果该值没有在布隆过滤器中出现过，其就不可能比victimNode高频，则插入布隆过滤器后返回
            if (!bf.contains(k.toString())) {
                bf.add(k.toString());
                return null;
            }
            //如果其在布隆过滤器出现过，此时就对比其和victim在cmSketch中的计数，保留大的那一个
            int victimCount = cmSketch.count(victim.key);
            int newNodeCount = cmSketch.count(newNode.key);
            //如果victim大于当前节点，则没有插入的必要
            if (newNodeCount < victimCount) {
                return null;
            }
            dataMap.put(k, newNode);
            slru.putNode(newNode);

            //如果满了，此时发生了淘汰，将淘汰节点删去
            if (dataMap.get(victim.key).position == LRU01_base.Position.SLRU) {
                dataMap.remove(victim.key);
            }
            return null;
        }

        public V get(K k) {
            //判断访问次数，如果访问次数达到限制，则触发保鲜机制
            ++totalVisit;
            if (totalVisit >= threshold) {
                cmSketch.reset();
                bf.clear();
                totalVisit = 0;
            }
            //在cmSketch对访问计数
            cmSketch.add(k);
            //首先查找map，如果map中没有找到则说明不存在
            LRU01_base.LRUNode<K, V> lruNode = dataMap.get(k);
            if (lruNode == null) {
                return null;
            }
            if (lruNode.position == LRU01_base.Position.WINDOWS_LRU) {
                wlru.get(k);
            } else {
                slru.get(k);
            }
            return lruNode.value;
        }

        public V del(K k) {
            LRU01_base.LRUNode<K, V> lruNode = dataMap.remove(k);
            if (lruNode == null) {
                return null;
            }
            return lruNode.value;
        }
    }
}
