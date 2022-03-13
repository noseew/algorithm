package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.HashMap_base_04;

import java.util.Objects;
import java.util.Random;

/**
 */
public class SkipListBase01<K extends Comparable<K>, V> {
    /**
     * map为了O(1)的方式定位到结点, 同时做到结点去重
     */
    private HashMap_base_04<K, Node<K, V>> hashMap = new HashMap_base_04<>(8);
    /**
     * 跳表的所有的遍历都是从 headerIndex 开始
     * 最高层的索引永远在 headerIndex 中, 并且永远比其他最高索引高1层
     * 最底层索引层从开始
     * 索引层的node结点中 分值为-1, 用于标记他是空结点或者头结点
     */
    private Index<K, V> headerIndex;

    /**
     * 索引层从1开始
     * 数据链表不属于任何层
     * 默认最大索引层为32层, 数据在2^32次方内, 始终保持索引的随机特性, 跳表的效率不会有太多变化
     * 因为headerIndex比其他索引高1层, 所以headerIndex最多能到33层
     */
    private final int maxLevel = 32;
    /**
     * 当前索引中的最高level, 
     * 等于headerIndex的level - 1
     */
    
    private final Random r = new Random();
    
    protected double maxScore = 0; // 用户最大分值, 跟随用户传入数据变化而变化, 用户获取尾节点
    protected double minScore = -1; // 最小分值, 只能出现在 headerIndex 中, 用户数据最小分值从0开始

    /**
     * 用于 debug 调试
     * 有索引的node数量
     */
    protected int indexCount = 0;
    /**
     * 用于 debug 调试
     * node的插入顺序
     */
    protected int NO = 0;
    
    public SkipListBase01() {
        // 临时头node节点
        Node<K, V> node = new Node<>();
        node.score = minScore; // 头结点分值最小, 其他分值必须 >= 0
        // 临时头索引节点, 初始1层
        headerIndex = buildIndex(1, node);
    }
    
    public V put(K k, V v, double score) {
        checkMinScorePut(score);
        Node<K, V> newNode = new Node<>(k, v, score, null, NO++, 0);
        Node<K, V> exitNode = hashMap.get(k);
        maxScore = Math.max(maxScore, score);
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

    public V get(K k) {
        Node<K, V> node = hashMap.get(k);
        if (node == null) {
            return null;
        }
        return node.v;
    }

    public V remove(K k) {
        // 从map中删除
        Node<K, V> removedNode = hashMap.remove(k);
        if (removedNode == null) {
            return null;
        }
        remove(removedNode);

        return removedNode.v;
    }

    public V getMinVal() {
        Node<K, V> minNode = getMinNode();
        if (minNode != null) {
            return minNode.v;
        }
        return null;
    }

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
    public ArrayBase01<V> getByScore(double min, double max) {
        checkMinScoreQuery(min);
        ArrayBase01<V> vals = new ArrayBase01<>();
        ArrayBase01<Node<K, V>> nodes = getNodesByScore(min, max);
        for (int i = 0; i < nodes.length(); i++) {
            vals.add(nodes.get(i).getV());
        }
        return vals;
    }

    public ArrayBase01<V> removeByScore(double min, double max) {
        ArrayBase01<V> vals = new ArrayBase01<>();
        ArrayBase01<Node<K, V>> nodes = removeNode(min, max);
        for (int i = 0; i < nodes.length(); i++) {
            vals.add(nodes.get(i).v);
        }
        return vals;
    }

    public void clean() {
        hashMap.clean();
        indexCount = 0;
        maxScore = 0;
        NO = 0;
        Node<K, V> node = new Node<>();
        node.score = minScore;
        headerIndex = buildIndex(1, node);
    }

    public int size() {
        return hashMap.size();
    }
    
    /************************************* 内部通用方法 *************************************/
    
    protected Node<K, V> getMinNode() {
        return headerIndex.node.next;
    }

    protected Node<K, V> getMaxNode() {
        ArrayBase01<Node<K, V>> nodes = getNodesByScore(maxScore, -1);
        if (nodes.length() == 0) {
            return null;
        }
        return nodes.get(nodes.length() - 1);
    }

    protected void put(Node<K, V> newNode) {
        Node<K, V> prev = getPrevNodeByScore(newNode.score);
        // 串链表
        newNode.next = prev.next;
        prev.next = newNode;

        // 新建索引
        Index<K, V> newIndex = buildIndex(buildLevel(), newNode);
        if (newIndex == null) {
            return;
        }
        // y 前/左一个索引, n当前新增的索引
        Index<K, V> y, n;
        // 索引升层
        if (newIndex.level >= headerIndex.level) {
            // 生层同时串最上层索引
            headerIndex = new Index<>(null, headerIndex, headerIndex.node, headerIndex.level + 1);
            headerIndex.down.next = newIndex;

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

    /**
     * 从跳表中删除node节点, 如果节点有索引, 一并删除
     *
     * @param removedNode
     */
    protected void remove(Node<K, V> removedNode) {
        // 删除索引
        Index<K, V> yh = removeIndex(removedNode.k, removedNode.score);
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
    protected void addIndex(Index<K, V> indexHead, Index<K, V> newIndex) {
        while (indexHead != null) { // y轴遍历
            Index<K, V> x = indexHead.next, xh = indexHead;
            while (x != null) { // x轴遍历
                if (x.node.score >= newIndex.node.score) {
                    // 跳过了, 串索引, 新索引在中间
                    newIndex.next = x;
                    xh.next = newIndex;
                    break;
                }
                xh = x;
                // 向右
                x = x.next;
            }
            if (x == null) {
                // 跳过了, 串索引, 新索引在右边
                xh.next = newIndex;
            }

            // 向下
            indexHead = xh.down;
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
    protected Index<K, V> removeIndex(K k, double score) {
        Index<K, V> y = headerIndex, yh = null;
        while (y != null) { // y轴遍历
            Index<K, V> x = y.next, xh = y;
            while (x != null) { // x轴遍历
                if (x.node.score == score && Objects.equals(k, x.node.k)) {
                    // 找到了, 删除索引
                    xh.next = x.next;
                    break;
                } else if (x.node.score < score) {
                    xh = x;
                    // 向右
                    x = x.next;
                    continue;
                }
                break;
            }
            yh = y;
            // 向下
            y = xh.down;
        }
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
        // 跳索引
        Index<K, V> y = headerIndex;
        while (y != null) { // y轴遍历
            Index<K, V> x = y.next, xh = y;
            while (x != null) { // x轴遍历
                if (x.node.score >= score) {
                    // 找到了
                    break;
                }
                xh = x;
                // 向右
                x = x.next;
            }
            prev = xh.node;
            // 向下
            y = xh.down;
        }
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
            if (Objects.equals(next.k, k)) break;
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
        for (int i = 0; i < headerIndex.level && i <= maxLevel; i++) {
            if ((nextInt & 0B1) != 0B1) break;
            nextInt = nextInt >>> 1;
            level++;
        }
        return level;
    }

    /**
     * 构建索引, 并返回索引头
     * 
     * @param level 需要生成多少层索引
     * @param newNode 索引关联的node
     * @return
     */
    protected Index<K, V> buildIndex(int level, Node<K, V> newNode) {
        // 构建索引
        Index<K, V> head = null;
        for (int i = 1; i <= level; i++) {
            Index<K, V> down = head;
            head = new Index<>();
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
        sb.append("size=").append(hashMap.size())
                .append(", indexCount=").append(indexCount)
                .append("\r\n");
        Index<K, V> hi = headerIndex;
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
    
    private void reversed(Index<K, V> index, StringBuilder sb) {
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
    private Index<K, V> nextIndexHead(Node<K, V> node) {
        if (node == null || node.next == null) {
            return null;
        }
        // 从第1层开始
        Index<K, V> oneLevel = headerIndex;
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
        Index<K, V> y = headerIndex;
        while (y != null) { // y轴遍历
            Index<K, V> x = y.next;
            while (x != null) { // x轴遍历
                if (x.node == nextIndexNode) {
                    return x; // 找到了
                } else if (x.node.score > nextIndexNode.score) {
                    break; // 超过了
                }
                // 向右
                x = x.next;
            }
            // 向下
            y = y.down;
        }
        return null;
    }

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
