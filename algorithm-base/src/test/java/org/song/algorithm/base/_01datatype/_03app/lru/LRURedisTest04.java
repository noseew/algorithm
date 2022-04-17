package org.song.algorithm.base._01datatype._03app.lru;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class LRURedisTest04 {


    /*
    参考 redis 3.0+ 实现 LRU
    
    和 LRURedisTest03 相比, LRURedisTest03 随机采样5个有可能取出的是最新的5个, 这样删除缓存的准确性就降低了很多
    这里优化了随机采样算法, 维护一个最早的缓存池, 每次采样的时候从原始数据中起一个和缓存池中对比, 并取出最早的一个, 然后删除, 
    这样删除的数据就大大接近更早的了, 随着访问的增加, 缓存池中的缓存会主键的接近于更早的缓存, 所以删除会更加准确
    
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


    public static class LRUCache<K, V> extends LRURedisTest03.LRURedis2Cache<K, V> {

        /*
        在 redis 3.0 以后对该算法进行了一个升级, 新的算法维护了一个候选池(pool), 
        首次筛选出来的 key 会被全部放入到候选池中, 在后续的筛选过程中只有 lru 小于候选池中最小的 lru 才能被放入到候选池, 
        直至候选池放满, 当候选池满了的时候, 如果有新的数据继续放入, 则需要将候选池中 lru 字段最大值取出
         */
        private Set<CacheNode> pool;

        public LRUCache(int size) {
            super(size);
        }

        public void removeLast() {
            if (pool == null) {
                pool = Sets.newHashSet(sample(capacity));
            }

            CacheNode last = getMin(pool);
            
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
    }
}
