package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._03app.lru;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DemoTest_04_LRU {


    /*
    参考 redis 3.0+ 实现 LRU
    https://www.jianshu.com/p/e0226aa946c4
    https://zhuanlan.zhihu.com/p/34133067
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


    class LRUCache<K, V> {

        private int capacity;
        /*
        在 redis 3.0 以后对该算法进行了一个升级, 新的算法维护了一个候选池(pool), 
        首次筛选出来的 key 会被全部放入到候选池中, 在后续的筛选过程中只有 lru 小于候选池中最小的 lru 才能被放入到候选池, 
        直至候选池放满, 当候选池满了的时候, 如果有新的数据继续放入, 则需要将候选池中 lru 字段最大值取出
         */
        private Set<CacheNode> pool;
        private HashMap<K, CacheNode> cacheMaps;

        public LRUCache(int size) {
            this.capacity = size;
            cacheMaps = new HashMap<K, CacheNode>(size);
        }

        public void put(K k, V v) {
            CacheNode node = cacheMaps.get(k);
            if (node == null) {
                if (cacheMaps.size() >= capacity) {
                    removeLast();
                }
                node = new CacheNode();
                node.key = k;
            }
            node.value = v;
            node.lru = System.currentTimeMillis();
            cacheMaps.put(k, node);
        }

        public Object get(K k) {
            CacheNode node = cacheMaps.get(k);
            if (node == null) {
                return null;
            }
            node.lru = System.currentTimeMillis();
            return node.value;
        }

        private void removeLast() {
            if (pool == null) {
                pool = Sets.newHashSet(sample(capacity));
            }

            ArrayList<CacheNode> poolList = Lists.newArrayList(pool);
            poolList.sort(Comparator.comparing(e -> e.lru));
            CacheNode last = poolList.get(0);
            
            // 随机一个 key 和 pool 合并
            int max = 100;
            for (int i = 0; i < max && pool.size() < capacity; i++) {
                List<CacheNode> sample = sample(1);
                if (sample.isEmpty()) {
                    continue;
                }
                // 只选择 比 pool 最老的还老的 key
                if (last.lru <= sample.get(0).lru) {
                    continue;
                }
                pool.addAll(sample);
            }
            
            if (pool.size() < capacity) {
                return;
            }
            if (last != null) {
                pool.remove(last);
                cacheMaps.remove(last.key);
            }
        }

        /**
         * 采样指定数量的key
         * redis中随机获取entry采用的方式是, 对数组随机然后对链表随机, 获取到最终的entry, 调用一次获取一个entry
         * java中无法直接获取到数组和链表, 且java中有红黑树的存在, 所以这里采用近似的方式获取
         */
        private List<CacheNode> sample(int size) {
            // 随机位置
            int randomSkip = ThreadLocalRandom.current().nextInt(cacheMaps.size());
            List<CacheNode> entrys = new ArrayList<>(size);
            for (Map.Entry<K, CacheNode> kCacheNodeEntry : cacheMaps.entrySet()) {
                if (randomSkip-- > 0) {
                    continue;
                }
                entrys.add(kCacheNodeEntry.getValue());
                if (size-- <= 0) {
                    break;
                }
            }
            return entrys;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            List<CacheNode> collect = cacheMaps.values().stream().sorted(Comparator.comparing(e -> e.lru)).collect(Collectors.toList());
            collect.forEach(v -> {
                sb.append(v).append(", ");
            });
            return sb.toString();
        }

        class CacheNode {
            long lru;
            Object key;
            Object value;

            @Override
            public String toString() {
                return key + "=" + value;
            }
        }
    }
}
