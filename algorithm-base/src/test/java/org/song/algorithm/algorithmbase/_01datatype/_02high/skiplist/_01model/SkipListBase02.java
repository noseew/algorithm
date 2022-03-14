package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;

import java.util.Objects;

/**
 * 跳表
 * <p>
 * 实现的特点
 * 1. 索引层采用数组+链表
 */
public class SkipListBase02<K extends Comparable<K>, V> extends AbstractSkipList<K, V> {

    /**
     * 用于 debug 调试
     * 有索引的node数量
     */
    protected int indexCount = 0;

    public SkipListBase02() {
    }

    @Override
    public V put(K k, V v, double score) {
        checkMinScorePut(score);
        Node<K, V> newNode = new Node<>(k, v, score, null, 0);
        Node<K, V> exitNode = hashMap.get(k);
        if (exitNode == null) {
            // 不存在
            hashMap.put(k, newNode);
            // 加入跳表
            put(newNode);
            return null;
        } else {
            // 存在则更新
            remove(exitNode);
            hashMap.put(k, newNode);
            // 加入跳表
            put(newNode);
            return exitNode.v;
        }
    }

    @Override
    public V get(K k) {
        Node<K, V> node = hashMap.get(k);
        if (node == null) {
            return null;
        }
        return node.v;
    }

    @Override
    public V remove(K k) {
        // 从map中删除
        Node<K, V> removedNode = hashMap.remove(k);
        if (removedNode == null) {
            return null;
        }
        remove(removedNode);

        return removedNode.v;
    }

    @Override
    public V getMinVal() {
        Node<K, V> minNode = getMinNode();
        if (minNode != null) {
            return minNode.v;
        }
        return null;
    }

    @Override
    public V getMaxVal() {
        Node<K, V> maxNode = getMaxNode();
        if (maxNode != null) {
            return maxNode.v;
        }
        return null;
    }

    /**
     * 根据分数范围获取, 分数左开右闭, -1表示全部
     *
     * @param min
     * @param max
     * @return
     */
    @Override
    public ArrayBase01<V> getByScore(double min, double max) {
        checkMinScoreQuery(min);
        ArrayBase01<V> vals = new ArrayBase01<>();
        ArrayBase01<Node<K, V>> nodes = getNodesByScore(min, max);
        for (int i = 0; i < nodes.length(); i++) {
            vals.add(nodes.get(i).getV());
        }
        return vals;
    }

    @Override
    public ArrayBase01<V> removeByScore(double min, double max) {
        ArrayBase01<V> vals = new ArrayBase01<>();
        ArrayBase01<Node<K, V>> nodes = removeNode(min, max);
        for (int i = 0; i < nodes.length(); i++) {
            vals.add(nodes.get(i).v);
        }
        return vals;
    }

    @Override
    public void clean() {
        hashMap.clean();
    }

    @Override
    public int size() {
        return hashMap.size();
    }

    /************************************* 内部通用方法 *************************************/

    protected Node<K, V> getMinNode() {
        return null;
    }

    protected Node<K, V> getMaxNode() {
//        ArrayBase01<Node<K, V>> nodes = getNodesByScore(maxScore, -1);
//        if (nodes.length() == 0) {
//            return null;
//        }
//        return nodes.get(nodes.length() - 1);
        return null;
    }

    protected void put(Node<K, V> newNode) {
        Node<K, V> prev = getPrevNodeByScore(newNode.score);
        // 串链表
        newNode.next = prev.next;
        prev.next = newNode;

        // 新建索引
        ArrayIndex<K, V> newIndex = buildIndex(buildLevel(), newNode);
        if (newIndex == null) {
            return;
        }
        // y 前/左一个索引, n当前新增的索引
        ArrayIndex<K, V> y = null, n = null;
        // 索引升层

        // 串索引
        addIndex(y, n);
    }

    /**
     * 从跳表中删除node节点, 如果节点有索引, 一并删除
     * TODO 是否考虑降低 headerIndex 索引层数?
     *
     * @param removedNode
     */
    protected void remove(Node<K, V> removedNode) {
        // 删除索引
        ArrayIndex<K, V> yh = removeIndex(removedNode.k, removedNode.score);
        // 找到 prev
        Node<K, V> prev = getPrevNodeByNode(yh.node, removedNode.k);
        // 从链表中删除
        if (prev != null) prev.next = prev.next.next;
    }

    /**
     * 根据分数范围删除node, 同时返回这些node
     * 这里是循环调用删除单个node的方法, 所以效率平均O(nlogn), 需要优化 TODO
     *
     * @param min
     * @param max
     * @return
     */
    protected ArrayBase01<Node<K, V>> removeNode(double min, double max) {
        checkMinScoreQuery(min);
        ArrayBase01<Node<K, V>> nodes = getNodesByScore(min, max);
        for (int i = 0; i < nodes.length(); i++) {
            remove(nodes.get(i));
        }
        return nodes;
    }

    /**
     * 向索引中添加新索引
     *
     * @param indexHead 头索引, 注意和headerIndex的区别, 这里的indexHead是headerIndex降低层数和newIndex层数相同的head
     * @param newIndex  新索引, 串好了层数, 同时必须包含node,
     */
    protected void addIndex(ArrayIndex<K, V> indexHead, ArrayIndex<K, V> newIndex) {
    }

    /**
     * 删除索引, 同时返回该索引的前一个第1层的索引
     *
     * @param k     索引所关联的node.k
     * @param score 索引所关联的分数
     * @return
     */
    protected ArrayIndex<K, V> removeIndex(K k, double score) {
        ArrayIndex<K, V> yh = null;
        return yh;
    }

    /**
     * 根据分数, 找到其前1个node,
     * 可能有多个相同分数的node, 这里只返回 < 分数的最后一个, 可能还需要从node链表中获取真正的node前一个
     *
     * @param score
     * @return
     */
    protected Node<K, V> getPrevNodeByScore(double score) {

        Node<K, V> prev = null;
        return prev;
    }

    /**
     * 根据k, 在head中找到k其前1个node
     *
     * @param head
     * @return
     */
    protected Node<K, V> getPrevNodeByNode(Node<K, V> head, K k) {
        Node<K, V> prev = head, next = null;
        while (prev != null) {
            next = prev.next;
            if (next != null && Objects.equals(next.k, k)) break;
            prev = next;
        }
        return prev;
    }

    /**
     * 根据分数范围, 返回nodes, 这里用public是为了测试, 不应该返回出去node的
     *
     * @param min
     * @param max
     * @return
     */
    public ArrayBase01<Node<K, V>> getNodesByScore(double min, double max) {
        Node<K, V> minNode = getPrevNodeByScore(min);
        ArrayBase01<Node<K, V>> nodes = new ArrayBase01<>();
        if (minNode == null) {
            return nodes;
        }
        while (minNode != null) {
            if ((minNode.score != minScore)
                    && (min == -1 || minNode.score >= min)
                    && (minNode.score < max || max == -1)) {
                nodes.add(minNode);
            }
            if (max != -1 && minNode.score >= max) {
                break;
            }
            minNode = minNode.next;
        }
        return nodes;
    }

    /**
     * 随机获取层数, 从1开始, 最低0层, 表示不构建索引, 最高层数 == headerIndex 的层数
     * 有0.5的概率不会生成索引
     * 从低位开始, 连续1的个数就是索引的层数
     *
     * @return
     */
    protected int buildLevel() {
        // 随机层高
        int nextInt = r.nextInt(Integer.MAX_VALUE);
        int level = 0;
        return level;
    }

    /**
     * 构建索引, 并返回索引头
     *
     * @param level   需要生成多少层索引
     * @param newNode 索引关联的node
     * @return
     */
    protected ArrayIndex<K, V> buildIndex(int level, Node<K, V> newNode) {
        // 构建索引
        ArrayIndex<K, V> head = null;
        return head;
    }

    private void checkMinScorePut(double min) {
        if (min < 0) {
            throw new RuntimeException("min 不能小于 0");
        }
    }

    private void checkMinScoreQuery(double min) {
        if (min < -1) {
            throw new RuntimeException("min 不能小于 -1");
        }
    }

    /************************************* 打印方法 *************************************/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    private String wrap(Object o) {
        String s = String.valueOf(o == null ? "" : o);
        StringBuilder sb = new StringBuilder();
        int max = 4;
        for (int i = 0; i < max - s.length(); i++) {
            sb.append(" ");
        }
        return sb.append(s).toString();
    }

    private void reversed(ArrayIndex<K, V> index, StringBuilder sb) {
        if (index == null) {
            return;
        }
//        reversed(index.down, sb);
//        sb.append("<-[").append(index.level).append("]");
    }

    /**
     * 获取下一个索引的头索引节点
     *
     * @param node
     * @return
     */
    private ArrayIndex<K, V> nextIndexHead(Node<K, V> node) {
        if (node == null || node.next == null) {
            return null;
        }
        return null;
    }

    /**
     * 和链表实现的方式不同
     * 索引分为两个结构
     * 1. 索引的节点, 索引节点只有一个指针, 指向的是自己关联的下一跳的索引数组的地址; 如何定位到当前节点的下一跳是哪个节点呢? 只需要找到相同的数组下标即可
     * 2. 索引数组, 包含多个索引节点
     *
     * @param <K>
     * @param <V>
     */
    protected static class ArrayIndex<K extends Comparable<K>, V> {
        /**
         * 存储下一个索引地址的数组
         */
        Index<K, V>[] array;
        /**
         * 当前索引的排名, 从1开始
         */
        int rank;
        Node<K, V> node;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    protected static class Index<K extends Comparable<K>, V> {
        /**
         * 指向下一个索引的地址
         */
        ArrayIndex<K, V> next;
    }

}
