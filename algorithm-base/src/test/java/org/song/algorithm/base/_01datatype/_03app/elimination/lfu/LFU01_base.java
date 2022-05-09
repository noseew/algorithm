package org.song.algorithm.base._01datatype._03app.elimination.lfu;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._01base._04tree.heap.Heap_base_02;
import org.song.algorithm.base._01datatype._01base._04tree.heap.Heap_build_01;
import org.song.algorithm.base._01datatype._03app.elimination.AbstractEliminate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class LFU01_base {

    /*
    LFU 最近不经常使用算法, 相当于LRU的升级版
    和LRU类似, 维护数据的最近使用时间, 但是要多维护一个使用次数
    当cache已满的时候, 优先删除使用次数最小的cache, 如果使用次数相等, 则优先删除最近使用时间最早的那个cache
    

    https://blog.csdn.net/weixin_38569499/article/details/113768134
    特点
    3.1 优点
    一般情况下, LFU效率要优于LRU, 能够避免周期性或者偶发性的操作导致缓存命中率下降的问题
    3.2 缺点
    复杂度较高: 需要额外维护一个队列或双向链表, 复杂度较高
    对新缓存不友好: 新加入的缓存容易被清理掉, 即使可能会被经常访问
    缓存污染: 一旦缓存的访问模式发生变化, 访问记录的历史存量, 会导致缓存污染. 
    内存开销: 需要对每一项缓存数据维护一个访问次数, 内存成本较大. 
    处理器开销: 需要对访问次数排序, 会增加一定的处理器开销
    
    缺点:
    实现无法应对突发流量，无法驱逐历史热 keys
    
     */

    /*
    整体的设计思路是, 
        在LRU基础上, 增加一个Map记录cache的访问次数, key=访问次数, val=LRU链表, 
        LRU链表: head [lru=4,times=1]<=>[lru=3,times=2]<=>[lru=2,times=1]<=>[lru=1,times=2] tail
        访问次数map: key=1, val=[lru=4,times=1]<=>[lru=2,times=1]
                   key=2, val=[lru=3,times=2]<=>[lru=1,times=2]
                   每一个map的val 都是一个独立的LRU链表
        删除的时候
            优先访问次数最小: key=1
            其次LRU: [lru=2,times=1]
            
    来自网络
     */
    @Test
    public void test_01() {
        LFUCache<String, Object> lfu = new LFUCache<>(5);

        lfu.put("1", 1);
        lfu.put("5", 5);
        lfu.put("7", 7);
        lfu.put("4", 4);
        lfu.put("3", 3);
        System.out.println(lfu.get("1"));
        System.out.println(lfu.get("4"));
        lfu.put("8", 8);
        System.out.println(lfu.get("5")); // 应该为null
        lfu.put("9", 9);
        System.out.println(lfu.get("7")); // 应该为null

    }

    /**
     * LFU
     * 采用 HashMap 来统计访问次数, key=访问次数, val=LRU链表
     * 加上 LRU 来实现
     * 
     * @param <K>
     * @param <V>
     */
    public static class LFUCache<K, V> extends AbstractEliminate<K, V> {

        Map<Integer, InnerLinked> timesMap = new HashMap<>(); // 访问次数map
        Map<K, Node> dataMap = new HashMap<>(); // 数据map
        int minTimes;

        public LFUCache(int capacity) {
            super(capacity);
        }

        public V get(K key) {
            Node node = dataMap.get(key);
            if (node == null) {
                return null;
            }
            addNodeTimes(node);
            return node.val;
        }

        public V put(K key, V value) {
            if (capacity == 0) {
                return null;
            }
            Node node = dataMap.get(key);
            if (node == null) { // put新元素
                if (dataMap.size() == capacity) {
                    // 需要删除
                    InnerLinked innerLinked = timesMap.get(minTimes);
                    Node removeNode = innerLinked.getLast();
                    removeNode(removeNode);
                }
                node = new Node(key, value);
                // 添加新节点, 其频率是1
                addNode(node);
                minTimes = 1; // 最小次数重置成1
                return null;
            } else {
                V oldVal = node.val;
                node.val = value;
                addNodeTimes(node);
                return oldVal;
            }
        }

        @Override
        public V remove(K k) {
            Node node = dataMap.remove(k);
            if (node != null) {
                timesMap.get(node.times).remove(node);
                return node.val;
            }
            return null;
        }

        private void addNodeTimes(Node node) {
            // 从次数小的LRU链表中删除
            removeNode(node);
            // 添加到次数大的LRU链表头中
            node.times = node.times + 1;
            addNode(node);
        }

        private void removeNode(Node node) {
            InnerLinked innerLinked = timesMap.get(node.times);
            innerLinked.remove(node);
            dataMap.remove(node.key);
            if (innerLinked.isEmpty()) {
                timesMap.remove(node.times);
                if (minTimes == node.times) {
                    // 当前次数的key为空了, 且是最小次数, 最小key加1
                    minTimes++;
                }
            }
        }

        private void addNode(Node node) {
            // 设置新的
            InnerLinked innerLinked = timesMap.getOrDefault(node.times, new InnerLinked());
            innerLinked.addFirst(node);
            dataMap.put(node.key, node);
            timesMap.put(node.times, innerLinked);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("次数map").append("\r\n");
            timesMap.forEach((k, v) -> {
                sb.append("key=").append(k).append(", val={").append(v).append("}").append("\r\n");
            });
            sb.append("\r\n").append("数据map").append("\r\n");
            dataMap.forEach((k, v) -> {
                sb.append("key=").append(k).append(", val={").append(v).append("}").append("\r\n");
            });
            return sb.toString();
        }

        public class InnerLinked {
            private Node head, tail; // 头尾虚节点
            private int size; // 链表元素数

            public InnerLinked() {
                head = new Node();
                tail = new Node();
                head.next = tail;
                tail.prev = head;
                size = 0;
            }

            // O(1)
            // 头插
            public void addFirst(Node x) {
                x.next = head.next;
                x.prev = head;
                head.next.prev = x;
                head.next = x;
                size++;
            }

            public Node getFirst() {
                if (tail.prev == head) {
                    return null;
                }
                return head.next;
            }

            public Node getLast() {
                if (tail.prev == head) {
                    return null;
                }
                return tail.prev;
            }

            // O(1)
            // 删除链表中的 x 节点(x 一定存在)
            public void remove(Node x) {
                x.prev.next = x.next;
                x.next.prev = x.prev;
                size--;
            }

            // O(1)
            // 删除链表中最后一个节点, 并返回该节点
            public Node removeLast() {
                if (tail.prev == head) {
                    return null;
                }
                Node last = tail.prev;
                remove(last);
                return last;
            }

            // 返回链表长度
            public int size() {
                return size;
            }

            public boolean isEmpty() {
                return size == 0;
            }

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("size=").append(size);
                Node h = this.head;
                while (h != null) {
                    sb.append("[").append(h).append("]").append(">");
                    h = h.next;
                }
                return sb.toString();
            }
        }

        // 双向链表的节点
        public class Node {
            public K key;
            public V val;
            public Node next, prev;
            int times = 1; // 该节点的访问次数

            public Node(K k, V v) {
                this.key = k;
                this.val = v;
            }

            public Node() {

            }

            @Override
            public String toString() {
                return key == null ? "" : key + ":" + (val == null ? "" : val);
            }
        }
    }

    /**
     * TODO LFU 采用优先级队列实现
     * 
     * @param <K>
     * @param <V>
     */
    public static class LFUCache2<K, V> extends AbstractEliminate<K, V> {

        PriorityQueue<Node> minHeap;
        Map<K, Node> dataMap = new HashMap<>(); // 数据map

        public LFUCache2(int cap) {
            super(cap);
            minHeap = new PriorityQueue<Node>(cap, Comparator.comparing(e -> e));
        }

        @Override
        public V get(K k) {
            return null;
        }

        @Override
        public V put(K k, V v) {
            return null;
        }

        @Override
        public V remove(K k) {
            return null;
        }


        // 节点
        public class Node implements Comparable<Node> {
            public K key;
            public V val;
            int times = 1; // 该节点的访问次数
            int index = 1; // 该节点的访问时间位置, 实现LRU功能, 越小越久没访问, 就越应该删除

            public Node(K k, V v) {
                this.key = k;
                this.val = v;
            }

            public Node() {

            }

            @Override
            public String toString() {
                return key == null ? "" : key + ":" + (val == null ? "" : val);
            }

            /**
             * Node 节点需要比较大小
             * 
             * @param o
             * @return
             */
            @Override
            public int compareTo(Node o) {
                if (o == null) {
                    return times;
                }
                // 优先对比访问次数
                if (times != o.times) {
                    return times - o.times;
                }
                // 然后对比访问时间, 
                return index - o.index;
            }
        }
    }
}
