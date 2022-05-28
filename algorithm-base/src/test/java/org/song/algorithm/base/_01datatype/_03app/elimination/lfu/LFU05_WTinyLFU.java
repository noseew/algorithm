package org.song.algorithm.base._01datatype._03app.elimination.lfu;

import org.song.algorithm.base._01datatype._02high.bloom.BloomFilter;
import org.song.algorithm.base._01datatype._03app.elimination.AbstractEliminate;
import org.song.algorithm.base._01datatype._03app.elimination.lru.LRU01_base;
import org.song.algorithm.base._01datatype._03app.elimination.lru.LRU02_SLRU;

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
        LRU01_base.LRUCache<K, V> _wlru;
        LRU02_SLRU.SLRUCache<K, LRU01_base.LRUNode<K, V>> _slru;
        BloomFilter _bloomFilter;
        LFU02_CountMinSketch.CountMinSketch<K> _cmSketch;
        Map<K, LRU01_base.LRUNode<K, V>> _dataMap;    //用于记录数据所在的区域
        int _totalVisit = 0;
        int _threshold = 100;    //保鲜机制

        static int BLOOM_FALSE_POSITIVE_RATE = 1;
        static int KEY_HASH_SEED = 0xbc9f1d34;
        static int CONFLICT_HASH_SEED = 0x9ae16a3b;

        public WindowsTinyLFU(int capacity) {
            _wlru = new LRU01_base.LRUCache<>(capacity / 100);
            _slru = new LRU02_SLRU.SLRUCache<>(capacity - (capacity / 100));
            _bloomFilter = new BloomFilter(capacity, capacity);
            _cmSketch = new LFU02_CountMinSketch.CountMinSketch<>(capacity);
            _dataMap = new HashMap<>(capacity);

        }

        public V put(K k, V v) {
            int hash1 = AbstractEliminate.hash1(k.hashCode());
            int hash2 = AbstractEliminate.hash2(k.hashCode());

            LRU01_base.LRUNode<K, V> newNode = new LRU01_base.LRUNode<>(k, v);
            newNode.position = LRU01_base.Position.WINDOWS_LRU;
            // 将数据放入wlru，如果wlru没满，则直接返回
            LRU01_base.LRUNode<K, V> eliminatedNode = _wlru.putReturnEliminated(newNode);
            if (eliminatedNode == null) {
                _dataMap.put(k, newNode);
                return null;
            }
            // 如果此时发生了淘汰，将淘汰节点删去
            if (_dataMap.get(eliminatedNode.key).position == LRU01_base.Position.WINDOWS_LRU) {
                _dataMap.remove(eliminatedNode.key);
            }
            //如果slru没满，则插入到slru中。
            LRU01_base.LRUNode<K, V> victim = _slru.victim();
            if (victim == null) {
                _dataMap.put(k, newNode);
                _slru.put(k, newNode);
                return null;
            }

            //如果该值没有在布隆过滤器中出现过，其就不可能比victimNode高频，则插入布隆过滤器后返回
            if (!_bloomFilter.contains(k.toString())) {
                _bloomFilter.add(k.toString());
                return null;
            }
            //如果其在布隆过滤器出现过，此时就对比其和victim在cmSketch中的计数，保留大的那一个
            int victimCount = _cmSketch.count(victim.key);
            int newNodeCount = _cmSketch.count(newNode.key);
            //如果victim大于当前节点，则没有插入的必要
            if (newNodeCount < victimCount) {
                return null;
            }
            _dataMap.put(k, newNode);
            _slru.put(k, newNode);

            //如果满了，此时发生了淘汰，将淘汰节点删去
            if (_dataMap.get(eliminatedNode.key).position == LRU01_base.Position.SLRU) {
                _dataMap.remove(eliminatedNode.key);
            }
            return null;
        }

        public V get(K k) {
            int hash1 = AbstractEliminate.hash1(k.hashCode());
            int hash2 = AbstractEliminate.hash2(k.hashCode());
            //判断访问次数，如果访问次数达到限制，则触发保鲜机制
            ++_totalVisit;
            if (_totalVisit >= _threshold) {
                _cmSketch.reset();
                _bloomFilter.clear();
                _totalVisit = 0;
            }
            //在cmSketch对访问计数
            _cmSketch.add(k);
            //首先查找map，如果map中没有找到则说明不存在
            LRU01_base.LRUNode<K, V> lruNode = _dataMap.get(k);
            if (lruNode == null) {
                return null;
            }
            if (lruNode.position == LRU01_base.Position.WINDOWS_LRU) {
                _wlru.get(k);
            } else {
                _slru.get(k);
            }
            return lruNode.value;
        }

        public V del(K k) {
            int hash1 = AbstractEliminate.hash1(k.hashCode());
            int hash2 = AbstractEliminate.hash2(k.hashCode());
            LRU01_base.LRUNode<K, V> lruNode = _dataMap.remove(k);
            if (lruNode == null) {
                return null;
            }
            return lruNode.value;
        }
    }
}
