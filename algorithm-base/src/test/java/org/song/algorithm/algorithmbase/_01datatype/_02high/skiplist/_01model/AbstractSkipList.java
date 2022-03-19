package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.HashMap_base_04;

import java.util.Objects;
import java.util.Random;

public abstract class AbstractSkipList<K extends Comparable<K>, V> {

    /**
     * map为了O(1)的方式定位到结点, 同时做到结点去重
     */
    protected HashMap_base_04<K, SkipListBase01.Node<K, V>> hashMap = new HashMap_base_04<>(8);

    /**
     * 索引层从1开始
     * 数据链表不属于任何层
     * 默认最大索引层为32层, 数据在2^32次方内, 始终保持索引的随机特性, 跳表的效率不会有太多变化
     * 因为headerIndex比其他索引高1层, 所以headerIndex最多能到33层
     * 当前索引中的最高level,
     * 等于headerIndex的level - 1
     */
    protected final int maxLevel = 32;

    protected double minScore = -1; // 最小分值, 只能出现在 headerIndex 中, 用户数据最小分值从0开始

    protected final Random r = new Random();

    public abstract Node<K, V> put(K k, V v, double score);

    public abstract Node<K, V> get(K k);

    public abstract Node<K, V> remove(K k);

    public abstract Node<K, V> getMinNode();

    public abstract Node<K, V> getMaxNode();

    public abstract double getMinScore();

    public abstract double getMaxScore();

    public abstract Node<K, V> getByRank(int rank);

    public abstract int getKeyRank(K k);

    public abstract ArrayBase01<Node<K, V>> getByScore(double min, double max);

    public abstract ArrayBase01<Node<K, V>> removeByScore(double min, double max);

    public abstract void clean();

    public abstract int size();

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
    public static class Node<K, V> {
        K k;
        V v;
        double score;
        Node<K, V> next;
        int ic; // 用于debug调试, 拥有索引层数
        int no; // 用于debug调试, 新增编号

        @Override
        public String toString() {
            return k + "=" + v + "(" + score + ")";
        }
    }
}
