package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model;

import lombok.Data;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.queue.Queue_Link_01;

import java.util.Objects;

/**
 * 跳表
 * SkipListBase01 增加rank
 */
public class SkipListBase03<K extends Comparable<K>, V> extends AbstractSkipList<K, V> {

    protected LinkIndex<K, V> headerIndex;

    protected int indexCount = 0;
    protected int NO = 0;

    public SkipListBase03() {
        // 临时头node节点
        Node<K, V> node = new Node<>();
        node.score = minScore; // 头结点分值最小, 其他分值必须 >= 0
        // 临时头索引节点, 初始1层
        headerIndex = buildIndex(1, node);
    }

    @Override
    public Node<K, V> put(K k, V v, double score) {
        checkMinScorePut(score);
        Node<K, V> newNode = Node.<K, V>builder().k(k).v(v).score(score).no(NO++).build();
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

        int r = 0;
        boolean match = false;
        LinkIndex<K, V> x = headerIndex, next, prevX = x;
        while (x != null && !match) { // y轴遍历
            prevX = x;
            next = x.next;
            while (next != null) { // x轴遍历
                if (r + x.rank == rank) {
                    // 找到了
                    prevX = x;
                    match = true;
                    break;
                }
                if (r + x.rank > rank) {
                    // 跳过了
                    break;
                }
                r += x.rank;
                x = next;
                prevX = x;
                // 向右
                next = next.next;
            }
            // 向下
            x = x.down;
        }
        Node<K, V> node = prevX.node;
        while (node != null && r < rank) {
            r++;
            node = node.next;
        }
        return node;
    }

    @Override
    public int getKeyRank(K k) {
        Node<K, V> node = hashMap.get(k);
        if (node == null) {
            return -1;
        }
        int rank = 0;
        Node<K, V> prevNode = null;
        LinkIndex<K, V> x = headerIndex, next;
        while (x != null) { // y轴遍历
            prevNode = x.node;
            next = x.next;
            while (next != null) { // x轴遍历
                if (next.node.score < node.score) {
                    rank += x.rank;
                    x = next;
                    prevNode = x.node;
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

        while (prevNode != null && !Objects.equals(prevNode.k, node.k)) {
            rank++;
            prevNode = prevNode.next;
        }
        
        return rank;
    }

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

    /************************************* 排名相关方法 *************************************/

    /**
     * 获取该索引之前所有的关联索引(包含该索引), 待更新他们的rank
     *
     * @param prevIndex
     * @return
     */
    protected Queue_Link_01<LinkIndex<K, V>> prevIndexes(LinkIndex<K, V> prevIndex) {

        Queue_Link_01<LinkIndex<K, V>> queue = new Queue_Link_01<>();

        LinkIndex<K, V> x = headerIndex, next;
        while (x != null) { // y轴遍历
            next = x.next;
            while (next != null) { // x轴遍历
                if (next.node.score == prevIndex.node.score
                        && Objects.equals(next.node.k, prevIndex.node.k)) {
                    // 找到了
                    queue.rpop();
                    x = next;
                    break;
                }
                if (next.node.score > prevIndex.node.score) {
                    // 跳过了
                    break;
                }
                x = next;
                // 向右
                next = next.next;
            }
            queue.lpush(x);
            // 向下
            x = x.down;
        }

        return queue;
    }

    /**
     * 将新增的索引放入待更新rank队列
     * 
     * @param newIndex
     * @return
     */
    protected Queue_Link_01<LinkIndex<K, V>> newIndexes(LinkIndex<K, V> newIndex) {

        Queue_Link_01<LinkIndex<K, V>> queue = new Queue_Link_01<>();
        while (newIndex != null) { // y轴遍历
            queue.lpush(newIndex);
            newIndex = newIndex.down; // 向下
        }
        return queue;
    }

    /**
     * 计算rank
     *
     * @param prevIndexes
     */
    protected void recalculateRank(Queue_Link_01<LinkIndex<K, V>> prevIndexes) {

        int rank = 0;
        LinkIndex<K, V> leftLast = null, rightLast = null;

        boolean first = true;
        while (!prevIndexes.isEmpty()) {
            LinkIndex<K, V> left = prevIndexes.lpop();
            LinkIndex<K, V> right = left.next;
            if (first) {
                // 最底层直接遍历链表获取rank
                first = false;
                rank = getRankByLinkedNode(left, right);
            } else {
                if (leftLast != null && !sameIndex(left, leftLast)) {
                    // 如果是左边index, 则取左边的down index rank的和
                    rank = getRankByIndex(left.down, right);
                }
                if (right == null) {
                    // 如果索引没有下一个, rank=0
                    rank = 0;
                } else if (rightLast != null && !sameIndex(right, rightLast)) {
                    // 如果是右边index, 则取右边的down index rank的和
                    rank = getRankByIndex(left.down, right);
                }
            }
            
            left.rank = rank;

            leftLast = left;
            rightLast = right;
        }
    }

    /**
     * 索引节点是否是同一个
     * 
     * @param left
     * @param right
     * @return
     */
    protected boolean sameIndex(LinkIndex<K, V> left, LinkIndex<K, V> right) {
        if (left == null || right == null) {
            return false;
        }
        return Objects.equals(left.node.k, right.node.k);
    }

    /**
     * 获取两个索引之间的rank, 直接通过线性跳跃的方式累加
     * 如果next为null, 则rank=0
     *
     * @param left
     * @param right
     * @return
     */
    protected int getRankByIndex(LinkIndex<K, V> left, LinkIndex<K, V> right) {
        int rank = 0;
        while (!sameIndex(left, right)) {
            if (left == null) {
                return 0;
            }
            rank += left.rank;
            left = left.next;
        }
        return rank;
    }

    /**
     * 最底层直接遍历链表获取rank
     *
     * @param left
     * @param right
     * @return
     */
    protected int getRankByLinkedNode(LinkIndex<K, V> left, LinkIndex<K, V> right) {
        if (left == null || right == null) {
            return 0;
        }
        return getRankByLinkedNode(left.node, right.node);
    }
    
    protected int getRankByLinkedNode(Node<K, V> left, Node<K, V> right) {
        int rank = 0;
        while (left != null && right != null && !Objects.equals(left.k, right.k)) {
            rank++;
            left = left.next;
        }
        return rank;
    }

    /************************************* 内部通用方法 *************************************/

    protected void put(Node<K, V> newNode) {
        LinkIndex<K, V> prevIndex = getPrevIndexByScore(newNode.score);
        Node<K, V> prev = getPrevNodeByScore(prevIndex.node, newNode.score);
        // 串链表
        newNode.next = prev.next;
        prev.next = newNode;

        // 新建索引
        LinkIndex<K, V> newIndex = buildIndex(buildLevel(headerIndex.level), newNode);
        if (newIndex == null) {
            // 重新计算排名
            recalculateRank(prevIndexes(prevIndex));
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

        // 重新计算排名, 要修改两类索引的排名, 1. 新建的索引, 2.新索引的prev索引
        recalculateRank(newIndexes(newIndex));
        recalculateRank(prevIndexes(prevIndex));
    }

    protected void upHead(LinkIndex<K, V> newIndex) {
        // 生层同时串最上层索引
        headerIndex = LinkIndex.instance(headerIndex, headerIndex.node, headerIndex.level + 1);
        headerIndex.down.next = newIndex;
    }

    protected void remove(Node<K, V> removedNode) {
        // 删除索引(如果该节点有索引同时删除节点), 为什么分开删除, 因为删除索引分数可能相同, 有的节点有索引有的没有, 跳索引的时候可能会跳过头
        removeIndexAndNode(removedNode.k, removedNode.score);
        // 删除节点
        LinkIndex<K, V> prevIndex = removeNode(removedNode.k, removedNode.score);

        // 重新计算排名
        recalculateRank(prevIndexes(prevIndex));
    }

    protected ArrayBase01<Node<K, V>> removeNode(double min, double max) {
        checkMinScoreQuery(min);
        ArrayBase01<Node<K, V>> nodes = getNodesByScore(min, max);
        for (int i = 0; i < nodes.length(); i++) {
            remove(nodes.get(i));
        }
        return nodes;
    }

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

    protected LinkIndex<K, V> removeNode(K k, double score) {

        LinkIndex<K, V> x = headerIndex, next, prevX = x;
        while (x != null) { // y轴遍历
            prevX = x;
            next = x.next;
            while (next != null) { // x轴遍历
                if (next.node.score < score) {
                    x = next;
                    prevX = x;
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
        return prevX;
    }

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

    protected Node<K, V> getPrevNodeByScore(Node<K, V> prev, double score) {
        while (prev != null) {
            Node<K, V> nextNode = prev.next;
            if (nextNode == null || nextNode.score >= score) break;
            prev = nextNode;
        }
        return prev;
    }

    protected LinkIndex<K, V> getPrevIndexByScore(double score) {

        // 跳索引
        LinkIndex<K, V> x = headerIndex, next, prevX = x;
        while (x != null) { // y轴遍历
            prevX = x;
            next = x.next;
            while (next != null) { // x轴遍历
                if (next.node.score >= score) {
                    // 找到了
                    break;
                }
                x = next;
                prevX = x;
                // 向右跳
                next = next.next;
            }
            // 向下
            x = x.down;
        }
        return prevX;
    }

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
                    .append(" no=").append(hn.no)
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
        sb.append("<-[").append(index.level).append("_").append(index.rank).append("]");
    }

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
        int rank;

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
