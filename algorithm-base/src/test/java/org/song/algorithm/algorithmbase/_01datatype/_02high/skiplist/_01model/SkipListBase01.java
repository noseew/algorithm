package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model;

import lombok.Data;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;

import java.util.Objects;

/**
 * 跳表
 * 结合 redis 的跳表和 JDK 的跳表特性
 * 1. 支持分数 排序/查找/删除 (redis的特性)
 * 2. 根据key有唯一性, 使用hashmap存储 (redis的特性, JDK中采用key排序作为唯一性校验)
 * 3. 存储key-val (JDK中有key和val, redis中只有key)
 * 4. 跳表索引采用二维链表形式 (参考JDK, redis中开始数组+链表形式)
 * 5. 数据节点node 1) hashmap中存储, 保证其唯一性, 2) 跳表中存储, 保证分数查找的O(logn)效率, (参考redis)
 *
 * 待实现
 * 1. 根据分值获取排名
 *
 * 跳表的特性
 * 1. 新增/删除/查找 平均O(logn), 最坏O(n), 最好O(1)
 * 2. 索引高度随机, 高度越高概率越低
 * 3. 数据链表有序
 *
 * 实现的特点
 * 1. 索引层采用二维链表 参考JDK实现
 *
 */
public class SkipListBase01<K extends Comparable<K>, V> extends AbstractSkipList<K, V> {
    /**
     * 跳表的所有的遍历都是从 headerIndex 开始
     * 最高层的索引永远在 headerIndex 中, 并且永远比其他最高索引高1层
     * 最底层索引层从开始
     * 索引层的node结点中 分值为-1, 用于标记他是空结点或者头结点
     */
    protected LinkIndex<K, V> headerIndex;

    /**
     * 用于 debug 调试
     * 有索引的node数量
     */
    protected int indexCount = 0;
    
    public SkipListBase01() {
        // 临时头node节点
        Node<K, V> node = new Node<>();
        node.score = minScore; // 头结点分值最小, 其他分值必须 >= 0
        // 临时头索引节点, 初始1层
        headerIndex = buildIndex(1, node);
    }

    @Override
    public Node<K, V> put(K k, V v, double score) {
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
        return headerIndex.node.next;
    }

    @Override
    public Node<K, V> getMaxNode() {

        Node<K, V> maxNode = null;
        // 跳索引
        LinkIndex<K, V> x = headerIndex, next;
        while (x != null) { // y轴遍历
            next = x.next;
            while (next != null) { // x轴遍历
                x = next;
                // 向右
                next = next.next;
            }
            maxNode = x.node;
            // 向下
            x = x.down;
        }

        while (maxNode != null) {
            Node<K, V> nextNode = maxNode.next;
            if (nextNode == null) break;
            maxNode = nextNode;
        }
        return maxNode;
    }

    @Override
    public double getMinScore() {
        Node<K, V> next = headerIndex.node.next;
        if (next != null) {
            return next.score;
        }
        return headerIndex.node.score;
    }

    @Override
    public double getMaxScore() {
        Node<K, V> maxNode = getMaxNode();
        if (maxNode != null) {
            return maxNode.score;
        }
        return -1;
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
        checkMinScoreQuery(min);
        return removeNode(min, max);
    }

    @Override
    public void clean() {
        hashMap.clean();
        indexCount = 0;
        Node<K, V> node = new Node<>();
        node.score = minScore;
        headerIndex = buildIndex(1, node);
    }

    @Override
    public int size() {
        return hashMap.size();
    }
    
    /************************************* 内部通用方法 *************************************/
    
    protected void put(Node<K, V> newNode) {
        Node<K, V> prev = getPrevNodeByScore(newNode.score);
        // 串链表
        newNode.next = prev.next;
        prev.next = newNode;

        // 新建索引
        LinkIndex<K, V> newIndex = buildIndex(buildLevel(headerIndex.level), newNode);
        if (newIndex == null) {
            return;
        }
        // y 前/左一个索引, n当前新增的索引
        LinkIndex<K, V> y, n;
        // 索引升层
        if (newIndex.level >= headerIndex.level) {
            upHead(newIndex);
            // 从下1层开始, 因为上一层索引已经关联, 由于headerIndex比其他都高1层, 所以这里是下2层
            y = headerIndex.down.down;
            // 新的 需要生层的索引, 最高层因为是新的, 这里已经关联, 所以需要从下面层开始
            n = newIndex.down;
        } else {
            // head索引下降到和新索引相同的高度在进行串索引
            y = headerIndex.down;
            for (int level = y.level; level > newIndex.level; level--) {
                // head 索引下跳到和新索引相同层
                y = y.down;
            }
            n = newIndex;
        }

        // 串索引
        addIndex(y, n);
    }

    protected void upHead(LinkIndex<K, V> newIndex) {
        // 生层同时串最上层索引
        headerIndex = LinkIndex.instance(headerIndex, headerIndex.node, headerIndex.level + 1);
        headerIndex.down.next = newIndex;
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
     * @param indexHead 头索引, 注意和headerIndex的区别, 这里的indexHead是headerIndex降低层数和newIndex层数相同的head
     * @param newIndex  新索引, 串好了层数, 同时必须包含node,
     */
    protected void addIndex(LinkIndex<K, V> indexHead, LinkIndex<K, V> newIndex) {
        LinkIndex<K, V> x = indexHead, next;
        while (x != null) { // y轴遍历
            next = x.next;
            while (next != null) { // x轴遍历
                if (next.node.score >= newIndex.node.score) {
                    // 跳过了, 串索引, 新索引在中间
                    newIndex.next = next;
                    x.next = newIndex;
                    break;
                }
                x = next;
                // 向右
                next = next.next;
            }
            if (next == null) {
                // 跳过了, 串索引, 新索引在右边
                x.next = newIndex;
            }
            // 向下
            x = x.down;
            // 新索引同时向下
            newIndex = newIndex.down;
        }
    }

    /**
     * 删除索引, 同时返回该索引的前一个第1层的索引
     *
     * @param k     索引所关联的node.k
     * @param score 索引所关联的分数
     * @return
     */
    protected void removeIndexAndNode(K k, double score) {

        Node<K, V> prev = null;
        LinkIndex<K, V> x = headerIndex, next;
        while (x != null) { // y轴遍历
            next = x.next;
            while (next != null) { // x轴遍历
                if (next.node.score == score && Objects.equals(k, next.node.k)) {
                    // 找到了, 删除索引
                    x.next = next.next;

                    // 待删除节点的prev
                    prev = x.node;
                } else if (next.node.score < score) {
                    x = next;
                    // 向右跳
                    next = next.next;
                    continue;
                }
                // 跳过了 终止
                break;
            }
            // 向下
            x = x.down;
        }
        // 删除链表节点
        if (prev != null && prev.next != null) {
            prev.next = prev.next.next;
            indexCount--;
        }
    }

    protected void removeNode(K k, double score) {

        LinkIndex<K, V> x = headerIndex, next;
        while (x != null) { // y轴遍历
            next = x.next;
            while (next != null) { // x轴遍历
                if (next.node.score < score) {
                    x = next;
                    // 向右跳
                    next = next.next;
                    continue;
                }
                // 跳过了 终止
                break;
            }
            // 向下
            if (x.down != null) {
                x = x.down;
            } else {
                break;
            }
        }
        // 找到 prev
        Node<K, V> prev = x.node, nextNode = null;
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
        // 跳索引
        LinkIndex<K, V> x = headerIndex, next;
        while (x != null) { // y轴遍历
            next = x.next;
            while (next != null) { // x轴遍历
                if (next.node.score >= score) {
                    // 找到了
                    break;
                }
                x = next;
                // 向右跳
                next = next.next;
            }
            prev = x.node;
            // 向下
            x = x.down;
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
    protected LinkIndex<K, V> buildIndex(int level, Node<K, V> newNode) {
        // 构建索引
        LinkIndex<K, V> head = null;
        for (int i = 1; i <= level; i++) {
            LinkIndex<K, V> down = head;
            head = new LinkIndex<>();
            head.node = newNode;
            head.level = i;

            head.down = down;
        }
        if (head != null) {
            indexCount++;
            newNode.ic = head.level;
        }
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
        sb.append("size=").append(this.size())
                .append(", indexCount=").append(indexCount)
                .append("\r\n");
        LinkIndex<K, V> hi = headerIndex;
        Node<K, V> hn = headerIndex.node;

        int count = 0;
        while (hn != null) {
            // 链表遍历
            sb.append(count++).append(". ")
                    .append(" ic=").append(hn.ic)
                    .append("\t")
                    .append(hn.score).append("{").append(wrap(hn.k)).append(":").append(wrap(hn.v)).append("} ");
            if (hi != null && hi.node == hn) {
                // 索引遍历, 逆序
                reversed(hi, sb);
                // 下一个索引头
                hi = nextIndexHead(hi.node);
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

    private void reversed(LinkIndex<K, V> index, StringBuilder sb) {
        if (index == null) {
            return;
        }
        reversed(index.down, sb);
        sb.append("<-[").append(index.level).append("]");
    }

    /**
     * 获取下一个索引的头索引节点
     *
     * @param node
     * @return
     */
    private LinkIndex<K, V> nextIndexHead(Node<K, V> node) {
        if (node == null || node.next == null) {
            return null;
        }
        // 从第1层开始
        LinkIndex<K, V> oneLevel = headerIndex;
        while (oneLevel != null && oneLevel.level > 1) oneLevel = oneLevel.down;
        // 找到下一个索引的第1层
        Node<K, V> nextIndexNode = null;
        boolean match = false; // 是否匹配到自己
        while (oneLevel != null) {
            // 用于处理分数相同的索引遍历问题, 遍历总是遍历下一个索引
            if (oneLevel.node.score == node.score) {
                if (node == oneLevel.node) {
                    match = true;
                } else if (match) {
                    nextIndexNode = oneLevel.node;
                    break;
                }
            } else if (oneLevel.node.score >= node.score) {
                nextIndexNode = oneLevel.node;
                break;
            }
            oneLevel = oneLevel.next;
        }
        // 如果接下来没有索引, 返回空
        if (nextIndexNode == null) {
            return null;
        }

        // 跳索引, 跳到下一个索引的索引头
        LinkIndex<K, V> x = headerIndex;
        while (x != null) { // y轴遍历
            LinkIndex<K, V> next = x.next;
            while (next != null) { // x轴遍历
                if (next.node == nextIndexNode) {
                    return next; // 找到了
                } else if (next.node.score > nextIndexNode.score) {
                    break; // 超过了
                }
                // 向右
                next = next.next;
            }
            // 向下
            x = x.down;
        }
        return null;
    }

    /**
     * 索引节点
     * 二维单向链表
     *
     * @param <K>
     * @param <V>
     */
    @Data
    protected static class LinkIndex<K extends Comparable<K>, V> {
        LinkIndex<K, V> next;
        Node<K, V> node;
        LinkIndex<K, V> down;
        int level;

        public static <K extends Comparable<K>, V> LinkIndex<K, V> instance(LinkIndex<K, V> down,
                                                                            Node<K, V> node,
                                                                            int level) {
            LinkIndex<K, V> index = new LinkIndex<>();
            index.down = down;
            index.node = node;
            index.level = level;
            return index;
        }

        @Override
        public String toString() {
            return "node=" + node + ", level=" + level;
        }
    }

}
