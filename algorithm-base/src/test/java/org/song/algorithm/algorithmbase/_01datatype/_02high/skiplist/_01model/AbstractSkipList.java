package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.HashMap_base_04;

import java.util.Random;

public abstract class AbstractSkipList<K extends Comparable<K>, V> {

    /**
     * map为了O(1)的方式定位到结点, 同时做到结点去重
     */
    protected HashMap_base_04<K, SkipListBase01.Node<K, V>> hashMap = new HashMap_base_04<>(8);
    /**
     * 跳表的所有的遍历都是从 headerIndex 开始
     * 最高层的索引永远在 headerIndex 中, 并且永远比其他最高索引高1层
     * 最底层索引层从开始
     * 索引层的node结点中 分值为-1, 用于标记他是空结点或者头结点
     */
    protected Index<K, V> headerIndex, maxIndex;

    /**
     * 索引层从1开始
     * 数据链表不属于任何层
     * 默认最大索引层为32层, 数据在2^32次方内, 始终保持索引的随机特性, 跳表的效率不会有太多变化
     * 因为headerIndex比其他索引高1层, 所以headerIndex最多能到33层
     * 当前索引中的最高level,
     * 等于headerIndex的level - 1
     */
    protected final int maxLevel = 32;

    protected final Random r = new Random();

    public abstract V put(K k, V v, double score);

    public abstract V get(K k);

    public abstract V remove(K k);

    public abstract V getMinVal();

    public abstract V getMaxVal();

    public abstract ArrayBase01<V> getByScore(double min, double max);

    public abstract ArrayBase01<V> removeByScore(double min, double max);

    public abstract void clean();

    public abstract int size();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    protected static class Index<K extends Comparable<K>, V> {
        Index<K, V> next;
        Index<K, V> down;
        Node<K, V> node;
        int level;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Node<K, V> {
        K k;
        V v;
        double score;
        Node<K, V> next;
        // 用于debug调试, 新增编号
        int no;
        // 用于debug调试, 拥有索引层数
        int ic;

        @Override
        public String toString() {
            return k + "=" + v + "(" + score + ")";
        }
    }
}
