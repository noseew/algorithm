package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;

import java.util.Random;

public abstract class AbstractSkipListMap<K extends Comparable<K>, V> {

    protected final int maxLevel = 32;

    protected double minKey = -1; // 最小分值, 只能出现在 headerIndex 中, 用户数据最小分值从0开始

    protected final Random r = new Random();

    public abstract V put(K k, V v);

    public abstract V get(K k);

    public abstract V remove(K k);

    public abstract V getMin();

    public abstract V getMax();

    public abstract ArrayBase01<V> getByRange(K min, K max);

    public abstract ArrayBase01<Node<K, V>> removeByRange(K min, K max);

    public abstract void clean();

    public abstract int size();

    protected boolean less(K k1, K k2) {
        // k==null, 说明是头结点
        return k1 == null || k1.compareTo(k2) < 0;
    }

    protected boolean gather(K k1, K k2) {
        return k1 != null && k1.compareTo(k2) > 0;
    }

    /**
     * 随机获取层数, 从1开始, 最低0层, 表示不构建索引, 最高层数 == headerIndex 的层数
     * 有0.5的概率不会生成索引
     * 从低位开始, 连续1的个数就是索引的层数
     *
     * @return
     */
    protected int buildLevel(int maxLevel) {
        // 随机层高
        int nextInt = r.nextInt(Integer.MAX_VALUE);
        int level = 0;
        // 最高层数 == headerIndex 的层数
        for (int i = 1; i <= maxLevel && i <= this.maxLevel; i++) {
            if ((nextInt & 0B1) != 0B1) break;
            nextInt = nextInt >>> 1;
            level++;
        }
        return level;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Index<K, V> {
        Node<K, V> next;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Node<K, V> {
        K k;
        V v;
        Index<K, V>[] levels;
        int ic; // 用于debug调试, 拥有索引层数

        @Override
        public String toString() {
            return "(key=" + k + ",val=" + v + ")";
        }
    }
}
