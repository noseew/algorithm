package org.song.algorithm.base._02alg.classical.elimination.lru;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._02alg.classical.elimination.AbstractEliminate;

public class LRU02_SLRU {


    @Test
    public void test_01() {
        SLRUCache<String, Object> lru = new SLRUCache<>(5);
        lru.put("1", 1);
        lru.put("5", 5);
        lru.put("7", 7);
        System.out.println(lru.get("1"));
        System.out.println(lru.get("5"));
        lru.put("4", 4);
        lru.put("3", 3);
        System.out.println(lru.get("7"));

        System.out.println();

    }
    
    /*
    SLRU被分为两个段, 试用段和保护段. 新数据会被加到试用段里. 
    如果试用段或者保护段的数据再次被命中, 那么数据会被加入到保护段的头部. 
    保护段的大小是有限的. 如果需要清除数据, 那么数据会从保护段的末尾开始清除. 
    
    
    似乎相当于 LRUK k=2, 未完成 TODO
    
    主缓存 SLRU 
    其主要分为两个区域：protected 和 probation. 
    probation 区即非热门数据区（命中一次）, 其占据主缓存大小的 20%, 
    而 protected 则是热门缓存区（命中两次及以上）, 其占用了主缓存大小的 80%. 
    
    protected 与 probation 的数据是怎样流动的呢?
        1. 我们在进行 put 操作时, 数据只能被放入 probation. 
        2. 只有在 probation 中 get 命中缓存时, 才会将数据转移到 protected 中. 
        此时我们会将新数据与 protected 末尾的数据进行位置交换. 
    
     */
    
    public static class SLRUCache<K, V> extends AbstractEliminate<K, V> {

        // 考察期
        private LRU01_base.LRUCache<K, V> probation;
        // 保护期
        private LRU01_base.LRUCache<K, V> protection;
        private int probationSize, protectionSize;

        public SLRUCache(int capacity) {
            super(capacity);
            // 两个占比可以自己调, 这里占比 1:4
            probation = new LRU01_base.LRUCache<>((int) (capacity * 0.2));
            protection = new LRU01_base.LRUCache<>((int) (capacity * 0.8));
        }

        /**
         * 我们在进行 put 操作时, 数据只能被放入 probation.
         * 只有在 probation 中 get 命中缓存时, 才会将数据转移到 protected 中.
         * 此时我们会将新数据与 protected 末尾的数据进行位置交换.
         * 
         * @param k
         * @return
         */
        @Override
        public V get(K k) {
            V exitNode = probation.get(k);
            if (exitNode == null) {
                return protection.get(k);
            }
            V v = probation.remove(k);
            // 如果观察组存在, 说明是2次访问, 则直接进入保护区
            protection.put(k, v);
            return v;
        }

        /**
         * 我们在进行 put 操作时, 数据只能被放入 probation.
         * 只有在 probation 中 get 命中缓存时, 才会将数据转移到 protected 中.
         * 此时我们会将新数据与 protected 末尾的数据进行位置交换.
         *
         * @param k
         * @param v
         * @return
         */
        @Override
        public V put(K k, V v) {
            // 优先存储在 观察组
            LRU01_base.LRUNode<K, V> exitNode = probation.getNode(k);
            if (exitNode == null) {
                exitNode = protection.getNode(k);
                if (exitNode == null) {
                    probationSize++;
                    exitNode = new LRU01_base.LRUNode<>(k, v);
                    return probation.putNode(exitNode);
                } else {
                    protectionSize++;
                    exitNode = new LRU01_base.LRUNode<>(k, v);
                    return protection.putNode(exitNode);
                }
            }
            probation.remove(k);
            // 如果观察组存在, 说明是2次访问, 则直接进入保护区
            LRU01_base.LRUNode<K, V> eliminated = protection.putReturnEliminated(exitNode);
            if (eliminated != null) {
                // 保护去如果有数据被淘汰，则回收到观察组，再给次机会
                probation.putNode(eliminated);
            }
            return null;
        }
        
        public V putNode(LRU01_base.LRUNode<K, V> node) {
            K k = node.key;
            V v = node.value;
            // 优先存储在 观察组
            LRU01_base.LRUNode<K, V> exitNode = probation.getNode(k);
            if (exitNode == null) {
                exitNode = protection.getNode(k);
                if (exitNode == null) {
                    probationSize++;
                    exitNode = node;
                    return probation.putNode(exitNode);
                } else {
                    protectionSize++;
                    exitNode = node;
                    return protection.putNode(exitNode);
                }
            }
            exitNode = node;
            probation.remove(k);
            // 如果观察组存在, 说明是2次访问, 则直接进入保护区
            return protection.putNode(exitNode);
        }

        @Override
        public V remove(K k) {
            V v = probation.remove(k);
            if (v != null) {
                probationSize--;
            } else {
                v = protection.remove(k);
                if (v != null) {
                    protectionSize--;
                }
            }
            return v;
        }
        
        public LRU01_base.LRUNode<K, V> victim() {
            if (protectionSize + probationSize > size) {
                return probation.removeLast();
            }
            return null;
        }

    }
}
