package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;

import java.util.Objects;

/**
 * 跳表
 * <p>
 * 1. 索引层采用数组+链表
 * 把链表节点和索引节点的对象类型重新定义了, 更加偏向于"官方", 同时redis也是类似的定义方式
 * 
 * redis中单项链表的指针是指向上一个的, 这样和索引一起就形成了类似于双向链表的功能, 这里暂时还是同向单项链表
 */
public class SkipListBase03Array<K extends Comparable<K>, V> extends AbstractSkipList<K, V> {

    protected LinkedNode<K, V> headerIndex;
    /**
     * 用于 debug 调试
     * 有索引的node数量
     */
    protected int indexCount = 0;

    public SkipListBase03Array() {
        // 临时头node节点
        LinkedNode<K, V> node = new LinkedNode<>();
        node.score = minScore; // 头结点分值最小, 其他分值必须 >= 0
        // 临时头索引节点, 初始1层
        headerIndex = buildIndex(1, node);
    }

    @Override
    public Node<K, V> put(K k, V v, double score) {
        checkMinScorePut(score);
        LinkedNode<K, V> newNode = new LinkedNode<>();
        newNode.k = k;
        newNode.v = v;
        newNode.score = score;
        LinkedNode<K, V> exitNode = (LinkedNode<K, V>) hashMap.get(k);
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
            return exitNode;
        }
    }

    @Override
    public Node<K, V> get(K k) {
        return hashMap.get(k);
    }

    @Override
    public Node<K, V> remove(K k) {
        // 从map中删除
        Node<K, V> removedNode = hashMap.remove(k);
        if (removedNode == null) {
            return null;
        }
        remove(removedNode);

        return removedNode;
    }

    @Override
    public Node<K, V> getMinNode() {
        return headerIndex.next;
    }

    @Override
    public Node<K, V> getMaxNode() {
        Node<K, V> maxNode = null;
        // 跳索引
        LinkedNode<K, V> x = headerIndex, next;
        for (int i = x.levels.length - 1; i >= 0; i--) { // y轴遍历
            next = (LinkedNode<K, V>) x.levels[i].next;
            while (next != null) { // x轴遍历
                // 向右跳
                next = (LinkedNode<K, V>) next.levels[i].next;
            }
            maxNode = x;
        }

        while (maxNode != null) {
            Node<K, V> nextNode = maxNode.next;
            if (nextNode == null) break;
            maxNode = nextNode;
        }
        return maxNode;
    }

    @Override
    public Node<K, V> getByRank(int rank) {
        return null;
    }

    @Override
    public int getKeyRank(K k) {
        return 0;
    }

    /**
     * 根据分数范围获取, 分数左开右闭, -1表示全部
     *
     * @param min
     * @param max
     * @return
     */
    @Override
    public ArrayBase01<Node<K, V>> getByScore(double min, double max) {
        checkMinScoreQuery(min);
        return getNodesByScore(min, max);
    }

    @Override
    public ArrayBase01<Node<K, V>> removeByScore(double min, double max) {
        return removeNode(min, max);
    }

    @Override
    public void clean() {
        hashMap.clean();
        indexCount = 0;
        LinkedNode<K, V> node = new LinkedNode<>();
        node.score = minScore;
        headerIndex = buildIndex(1, node);
    }

    @Override
    public int size() {
        return hashMap.size();
    }

    /************************************* 内部通用方法 *************************************/

    protected void put(LinkedNode<K, V> newNode) {
        Node<K, V> prev = getPrevNodeByScore(newNode.score);
        // 串链表
        newNode.next = prev.next;
        prev.next = newNode;

        // 新建索引
        LinkedNode<K, V> newIndex = buildIndex(buildLevel(headerIndex.levels.length), newNode);
        if (newIndex == null) {
            return;
        }
        // 从几层开始处理(倒序)
        int startIndex = 0;
        // 索引升层
        if (newIndex.levels.length >= headerIndex.levels.length) {
            upHead(newIndex);
            // 第一层已经建立关联, 所以从第2层开始(head多1层, 所以-3)
            startIndex = headerIndex.levels.length - 3;
        } else {
            // 从head下一层开始跳
            startIndex = headerIndex.levels.length - 2;
        }

        // 串索引
        addIndex(startIndex, newIndex);
    }

    protected void upHead(LinkedNode<K, V> newIndex) {
        Index<K, V>[] levels = headerIndex.levels;
        Index<K, V>[] newArray = new Index[levels.length + 1];
        System.arraycopy(levels, 0, newArray, 0, levels.length);
        headerIndex.levels = newArray;

        Index<K, V> index = new Index<>();
        index.next = newIndex;
        // 原头index上移
        headerIndex.levels[headerIndex.levels.length - 1] = headerIndex.levels[headerIndex.levels.length - 2];
        // 构建多出来的索引
        headerIndex.levels[headerIndex.levels.length - 2] = index;
    }

    /**
     * 从跳表中删除node节点, 如果节点有索引, 一并删除
     * TODO 是否考虑降低 headerIndex 索引层数?
     *
     * @param removedNode
     */
    protected void remove(Node<K, V> removedNode) {
        // 删除索引(如果该节点有索引同时删除节点)
        removeIndexAndNode(removedNode.k, removedNode.score);
        // 删除节点
        removeNode(removedNode.k, removedNode.score);
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
    protected void addIndex(int startIndex, LinkedNode<K, V> newIndex) {
        if (startIndex < 0) {
            return;
        }
        double score = newIndex.score;

        LinkedNode<K, V> x = this.headerIndex, next;
        for (int i = startIndex; i >= 0; i--) { // y轴遍历
            next = (LinkedNode<K, V>) x.levels[i].next;
            while (next != null) {  // x轴遍历
                if (next.score < score) {
                    // 向右跳
                    x = next;
                    next = (LinkedNode<K, V>) next.levels[i].next;
                    continue;
                } else if (i < newIndex.levels.length) {
                    // 添加新索引
                    newIndex.levels[i].next = next;
                    x.levels[i].next = newIndex;
                }
                break;
            }
            if (next == null && x.score < score && i < newIndex.levels.length) {
                // 添加新索引
                x.levels[i].next = newIndex;
            }
        }
    }

    /**
     * 删除索引, 同时返回该索引的前一个索引
     *
     * @param k     索引所关联的node.k
     * @param score 索引所关联的分数
     * @return
     */
    protected void removeIndexAndNode(K k, double score) {

        Node<K, V> prev = null;
        LinkedNode<K, V> x = this.headerIndex, next;
        for (int i = x.levels.length - 1; i >= 0; i--) { // y轴遍历
            next = (LinkedNode<K, V>) x.levels[i].next;
            while (next != null) {  // x轴遍历
                if (next.score == score && Objects.equals(next.k, k)) {
                    // 找到了, 删除索引
                    x.levels[i].next = next.levels[i].next;

                    // 待删除节点的prev
                    prev = x;
                } else if (next.score <= score) {
                    x = next;
                    // 向右跳
                    next = (LinkedNode<K, V>) next.levels[i].next;
                    continue;
                }
                // 跳过了 终止
                break;
            }
        }
        // 删除链表节点
        if (prev != null && prev.next != null) {
            prev.next = prev.next.next;
            indexCount--;
        }
    }

    protected void removeNode(K k, double score) {

        LinkedNode<K, V> x = this.headerIndex, next;
        for (int i = x.levels.length - 1; i >= 0; i--) { // y轴遍历
            next = (LinkedNode<K, V>) x.levels[i].next;
            while (next != null) {  // x轴遍历
                if (next.score < score) {
                    x = next;
                    // 向右跳
                    next = (LinkedNode<K, V>) next.levels[i].next;
                    continue;
                }
                // 跳过了 终止
                break;
            }
        }
        // 找到 prev
        Node<K, V> prev = x, nextNode = null;
        while (prev != null) {
            nextNode = prev.next;
            if (nextNode != null && Objects.equals(nextNode.k, k)) {
                // 从链表中删除
                indexCount--;
                prev.next = prev.next.next;
                break;
            }
            prev = nextNode;
        }
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
        LinkedNode<K, V> x = headerIndex, next;
        for (int i = x.levels.length - 1; i >= 0; i--) { // y轴遍历
            next = (LinkedNode<K, V>) x.levels[i].next;
            while (next != null) {  // x轴遍历
                if (next.score >= score) {
                    // 找到了
                    break;
                }
                // 向右跳
                next = (LinkedNode<K, V>) next.levels[i].next;
            }
            prev = x;
        }

        while (prev != null) {
            Node<K, V> nextNode = prev.next;
            if (nextNode == null || nextNode.score >= score) break;
            prev = nextNode;
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
     * 构建索引, 并返回索引头
     *
     * @param level   需要生成多少层索引
     * @param newNode 索引关联的node
     * @return
     */
    protected LinkedNode<K, V> buildIndex(int level, LinkedNode<K, V> newNode) {
        if (level == 0) {
            return null;
        }
        // 构建索引
        Index<K, V>[] levels = new Index[level];
        for (int i = 0; i < levels.length; i++) {
            levels[i] = new Index<>();
        }
        newNode.ic = level;
        newNode.levels = levels;
        indexCount++;
        return newNode;
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
        sb.append("size=").append(this.size())
                .append(", indexCount=").append(indexCount)
                .append("\r\n");

        LinkedNode<K, V> hi = headerIndex;
        Node<K, V> hn = hi;

        int count = 0;
        while (hn != null) {
            // 链表遍历
            sb.append(count++).append(". ")
                    .append(" ic=").append(hn.ic)
                    .append("\t")
                    .append(hn.score).append("{").append(wrap(hn.k)).append(":").append(wrap(hn.v)).append("} ");
            if (hi != null && hi == hn) {
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

    private void reversed(LinkedNode<K, V> index, StringBuilder sb) {
        if (index == null) {
            return;
        }
        sb.append("<-[");
        for (int i = 1; i <= index.levels.length; i++) {
            sb.append(i);
            if (i < index.levels.length) sb.append(", ");
        }
        sb.append("]");
    }

    /**
     * 获取下一个索引的头索引节点
     *
     * @param hi
     * @return
     */
    private LinkedNode<K, V> nextIndexHead(LinkedNode<K, V> hi) {
        if (hi == null) {
            return null;
        }
        return (LinkedNode<K, V>) hi.levels[0].next;
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
    protected static class Index<K extends Comparable<K>, V> {
        Node<K, V> next; // 指向下一个索引的地址
        int rank; // 当前索引的排名, 从1开始
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LinkedNode<K extends Comparable<K>, V> extends Node<K, V> {
        Index<K, V>[] levels;
    }

}
