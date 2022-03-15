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

    protected ArrayIndex<K, V> headerIndex;
    /**
     * 用于 debug 调试
     * 有索引的node数量
     */
    protected int indexCount = 0;

    public SkipListBase02() {
        // 临时头node节点
        Node<K, V> node = new Node<>();
        node.score = minScore; // 头结点分值最小, 其他分值必须 >= 0
        // 临时头索引节点, 初始1层
        headerIndex = buildIndex(1, node);
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
        ArrayIndex<K, V> newIndex = buildIndex(buildLevel(headerIndex.array.length), newNode);
        if (newIndex == null) {
            return;
        }
        // 从几层开始处理(倒序)
        int startIndex = 0;
        // 索引升层
        if (newIndex.array.length >= headerIndex.array.length) {
            upHead(newIndex);
            // 第一层已经建立关联, 所以从第2层开始(head多1层, 所以-3)
            startIndex = headerIndex.array.length - 3;
        } else {
            // 从head下一层开始跳
            startIndex = headerIndex.array.length - 2;
        }

        // 串索引
        addIndex(startIndex, newIndex);
    }

    protected void upHead(ArrayIndex<K, V> newIndex) {
        Index<K, V>[] array = headerIndex.array;
        Index<K, V>[] newArray = new Index[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        headerIndex.array = newArray;

        Index<K, V> index = new Index<>();
        index.next = newIndex;
        // 原头index上移
        headerIndex.array[headerIndex.array.length - 1] = headerIndex.array[headerIndex.array.length - 2];
        // 构建多出来的索引
        headerIndex.array[headerIndex.array.length - 2] = index;
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
     * @param newIndex
     */
    protected void addIndex(int startIndex, ArrayIndex<K, V> newIndex) {
        if (startIndex < 0) {
            return;
        }
        double score = newIndex.node.score;
        
        ArrayIndex<K, V> x = this.headerIndex, xh = x;
        for (int i = startIndex; i >= 0; i--) { // y轴遍历
            while (x != null && x.node.score < score) {  // x轴遍历
                // 向右跳
                xh = x;
                x = x.array[i].next;
            }
            if (i < newIndex.array.length) {
                // 添加新索引
                newIndex.array[i].next = x;
                xh.array[i].next = newIndex;
            }
            // 退一步
            x = xh;
        }
    }

    /**
     * 删除索引, 同时返回该索引的前一个索引
     *
     * @param k     索引所关联的node.k
     * @param score 索引所关联的分数
     * @return
     */
    protected ArrayIndex<K, V> removeIndex(K k, double score) {
        ArrayIndex<K, V> x = this.headerIndex, xh = x;
        for (int i = x.array.length - 1; i >= 0; i--) { // y轴遍历
            while (x != null && x.node.score <= score
                    && !Objects.equals(x.node.k, k)) {  // x轴遍历
                // 向右跳
                xh = x;
                x = x.array[i].next;
            }
            // 删除索引
            if (x != null && x.node.score == score && Objects.equals(x.node.k, k)) {
                xh.array[i].next = x.array[i].next;
            }
            // 退一步
            x = xh;
        }
        return xh;
    }

    /**
     * 根据分数, 找到其前1个node,
     * 可能有多个相同分数的node, 这里只返回 < 分数的最后一个, 可能还需要从node链表中获取真正的node前一个
     *
     * @param score
     * @return
     */
    protected Node<K, V> getPrevNodeByScore(double score) {
        ArrayIndex<K, V> x = this.headerIndex, xh = x;
        for (int i = x.array.length - 1; i >= 0; i--) { // y轴遍历
            while (x != null && x.node.score < score) {  // x轴遍历
                // 向右跳
                xh = x;
                x = x.array[i].next;
            }
        }

        Node<K, V> prev = xh.node;
        while (prev != null) {
            Node<K, V> next = prev.next;
            if (next == null || next.score >= score) break;
            prev = next;
        }
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
        // 最高层数 == headerIndex 的层数
        for (int i = 0; i < headerIndex.array.length && i <= maxLevel; i++) {
            if ((nextInt & 0B1) != 0B1) break;
            nextInt = nextInt >>> 1;
            level++;
        }
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
        if (level == 0) {
            return null;
        }
        // 构建索引
        ArrayIndex<K, V> head = new ArrayIndex<>();
        Index<K, V>[] array = new Index[level];
        for (int i = 0; i < array.length; i++) {
            array[i] = new Index<>();
        }
        newNode.ic = level;
        head.array = array;
        head.node = newNode;
        indexCount = level;
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
        sb.append("size=").append(hashMap.size())
                .append(", indexCount=").append(indexCount)
                .append("\r\n");

        ArrayIndex<K, V> hi = this.headerIndex;
        Node<K, V> hn = hi.node;

        int count = 0;
        while (hn != null) {
            // 链表遍历
            sb.append(count++).append(". ")
                    .append(" ic=").append(hn.ic)
                    .append("\t")
                    .append(hn.score).append("{").append(wrap(hn.k)).append(":").append(wrap(hn.v)).append("} ");
            if (hi != null && hi.node == hn) {
                // 索引遍历
                reversed(hi, sb);
                // 下一个索引头
                hi = nextIndexHead(hi);
            }
            sb.append("\r\n");
            hn = hn.next;
        }
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
        sb.append("<-[");
        for (int i = 1; i <= index.array.length; i++) {
            sb.append(i);
            if (i < index.array.length) sb.append(", ");
        }
        sb.append("]");
    }

    /**
     * 获取下一个索引的头索引节点
     *
     * @param hi
     * @return
     */
    private ArrayIndex<K, V> nextIndexHead(ArrayIndex<K, V> hi) {
        if (hi == null) {
            return null;
        }
        return hi.array[0].next;
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
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    protected static class ArrayIndex<K extends Comparable<K>, V> {
        /**
         * 存储下一个索引地址的数组
         */
        Index<K, V>[] array;
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
        /**
         * 当前索引的排名, 从1开始
         */
        int rank;
    }

}
