package org.song.algorithm.base._01datatype._03app.elimination.lru;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._03app.elimination.AbstractEliminate;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class LRU03_Redis {

    /*
    参考 redis 3.0- 实现 LRU
    
    和普通LRU相比, 普通LRU是通过双向链表来维护时间关系, 链表尾部元素一定是最早访问的元素, 如果需要删除, 则删除它即可
    这里的实现是, 通过时间戳字段lru来维护访问时间, lru越小则一定是最早访问元素, 如何定位他呢? 这里并不是采用排除而是随机采样
    随机取出比如5个, 在这5个中找出lru最小的那个然后删除
    
    这里实现的LRU仅仅是实验性质, 并没有证据表明他比普通LRU效率更高或空间更小, 待测试, 不过他写法更简单是真的
    
    为什么redis不使用普通的链表LRU算法?
    个人猜测: redis的cache有TTL, 不一定哪个cache会随时失效, 而失效的key补发通过普通LRU来删除, 但是可以通过随机采样来删除
    因此此方法兼容性更好
     */
    @Test
    public void test_01() {
        LRURedis2Cache<String, Object> lru = new LRURedis2Cache<>(5);
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

    /*
    参考 redis 3.0+ 实现 LRU
    
    和 LRURedisTest03 相比, LRURedisTest03 随机采样5个有可能取出的是最新的5个, 这样删除缓存的准确性就降低了很多
    这里优化了随机采样算法, 维护一个最早的缓存池, 每次采样的时候从原始数据中起一个和缓存池中对比, 并取出最早的一个, 然后删除, 
    这样删除的数据就大大接近更早的了, 随着访问的增加, 缓存池中的缓存会主键的接近于更早的缓存, 所以删除会更加准确
    
    https://www.jianshu.com/p/e0226aa946c4
    https://zhuanlan.zhihu.com/p/34133067
     */
    @Test
    public void test_02() {
        LRURedis3Cache<String, Object> lru = new LRURedis3Cache<>(5);
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

    public static class LRURedis2Cache<K, V> extends AbstractEliminate<K, V> {

        public HashMap<K, CacheNode<K, V>> cacheMaps;

        public LRURedis2Cache(int size) {
            super(size);
            this.capacity = size;
            cacheMaps = new HashMap<>(size);
        }

        public V putOrUpdate(K k, V v) {
            CacheNode<K, V> node = cacheMaps.get(k);
            if (node == null) {
                if (cacheMaps.size() >= capacity) {
                    removeLast();
                }
                node = new CacheNode<>();
                node.key = k;
            }
            node.value = v;
            node.lru = System.currentTimeMillis(); // 访问时间
            CacheNode<K, V> put = cacheMaps.put(k, node);
            return put != null ? put.value : null;
        }

        @Override
        public V remove(K k) {
            CacheNode<K, V> node = cacheMaps.remove(k);
            return node != null ? node.value : null;
        }

        public V get(K k) {
            CacheNode<K, V> node = cacheMaps.get(k);
            if (node == null) {
                return null;
            }
            node.lru = System.currentTimeMillis(); // 更新访问时间
            return node.value;
        }

        public void removeLast() {
            CacheNode<K, V> cacheNode = getMin(sample(capacity));
            if (cacheNode != null) {
                cacheMaps.remove(cacheNode.key);
            }
        }

        public CacheNode<K, V> getMin(Collection<CacheNode<K, V>> nodes) {
            if (nodes != null && !nodes.isEmpty()) {
                CacheNode<K, V> minNode = null;
                for (CacheNode<K, V> node : nodes) {
                    if (minNode == null) {
                        minNode = node;
                        continue;
                    }
                    if (minNode.lru > node.lru) {
                        minNode = node;
                    }
                }
                return minNode;
            }
            return null;
        }

        /**
         * 采样指定数量的key
         * redis中随机获取entry采用的方式是, 对数组随机然后对链表随机, 获取到最终的entry, 调用一次获取一个entry
         * java中无法直接获取到数组和链表, 且java中有红黑树的存在, 所以这里采用近似的方式获取
         */
        public List<CacheNode<K, V>> sample(int size) {
            // 随机位置
            int randomSkip = ThreadLocalRandom.current().nextInt(cacheMaps.size() / 2);
            ArrayList<CacheNode<K, V>> entrys = new ArrayList<>(size);
            for (Map.Entry<K, CacheNode<K, V>> kCacheNodeEntry : cacheMaps.entrySet()) {
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
            List<CacheNode<K, V>> collect = cacheMaps.values().stream().sorted(Comparator.comparing(e -> e.lru)).collect(Collectors.toList());
            collect.forEach(v -> {
                sb.append(v).append(", ");
            });
            return sb.toString();
        }

        public class CacheNode<K, V> {
            long lru;
            K key;
            V value;

            @Override
            public String toString() {
                return key + "=" + value;
            }
        }
    }

    public static class LRURedis3Cache<K, V> extends LRU03_Redis.LRURedis2Cache<K, V> {

        /*
        在 redis 3.0 以后对该算法进行了一个升级, 新的算法维护了一个候选池(pool), 
        首次筛选出来的 key 会被全部放入到候选池中, 在后续的筛选过程中只有 lru 小于候选池中最小的 lru 才能被放入到候选池, 
        直至候选池放满, 当候选池满了的时候, 如果有新的数据继续放入, 则需要将候选池中 lru 字段最大值取出
         */
        private Set<CacheNode<K, V>> pool;

        public LRURedis3Cache(int size) {
            super(size);
        }

        public void removeLast() {
            if (pool == null) {
                pool = Sets.newHashSet(sample(capacity));
            }

            CacheNode<K, V> last = getMin(pool);

            // 随机一个 key 和 pool 合并
            int max = 100;
            for (int i = 0; i < max && pool.size() < capacity; i++) {
                List<CacheNode<K, V>> sample = sample(1);
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
