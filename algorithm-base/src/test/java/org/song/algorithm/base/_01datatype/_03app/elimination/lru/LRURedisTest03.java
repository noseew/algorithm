package org.song.algorithm.base._01datatype._03app.elimination.lru;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class LRURedisTest03 {

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

    public static class LRURedis2Cache<K, V> {

        public int capacity;
        public HashMap<K, CacheNode> cacheMaps;

        public LRURedis2Cache(int size) {
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
            node.lru = System.currentTimeMillis(); // 访问时间
            cacheMaps.put(k, node);
        }

        public Object get(K k) {
            CacheNode node = cacheMaps.get(k);
            if (node == null) {
                return null;
            }
            node.lru = System.currentTimeMillis(); // 更新访问时间
            return node.value;
        }

        public void removeLast() {
            CacheNode cacheNode = getMin(sample(capacity));
            if (cacheNode != null) {
                cacheMaps.remove(cacheNode.key);
            }
        }

        public CacheNode getMin(Collection<CacheNode> nodes) {
            if (nodes != null && !nodes.isEmpty()) {
                CacheNode minNode = null;
                for (CacheNode node : nodes) {
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
        public List<CacheNode> sample(int size) {
            // 随机位置
            int randomSkip = ThreadLocalRandom.current().nextInt(cacheMaps.size() / 2);
            ArrayList<CacheNode> entrys = new ArrayList<>(size);
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

        public static class CacheNode {
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
