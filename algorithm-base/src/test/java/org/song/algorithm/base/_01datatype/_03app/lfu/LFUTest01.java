package org.song.algorithm.base._01datatype._03app.lfu;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class LFUTest01 {

    /*
    LFU 最近不经常使用算法, 相当于LRU的升级版
    和LRU类似, 维护数据的最近使用时间, 但是要多维护一个使用次数
    当cache已满的时候, 优先删除使用次数最小的cache, 如果使用次数相等, 则优先删除最近使用时间最早的那个cache
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

    public static class LFUCache<K, V> {

        Map<Integer, InnerLinked> timesMap = new HashMap<>(); // 访问次数map
        Map<K, Node> dataMap = new HashMap<>(); // 数据map
        int cap;
        int minTimes;

        public LFUCache(int capacity) {
            cap = capacity;
        }

        public V get(K key) {
            Node node = dataMap.get(key);
            if (node == null) {
                return null;
            }
            addNodeTimes(node);
            return node.val;
        }

        public void put(K key, V value) {
            if (cap == 0) {
                return;
            }
            Node node = dataMap.get(key);
            if (node == null) { // put新元素
                if (dataMap.size() == cap) {
                    // 需要删除
                    InnerLinked innerLinked = timesMap.get(minTimes);
                    Node removeNode = innerLinked.getLast();
                    removeNode(removeNode);
                }
                node = new Node(key, value);
                // 添加新节点, 其频率是1
                addNode(node);
                minTimes = 1; // 最小次数重置成1
            } else {
                addNodeTimes(node);
            }
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

        class InnerLinked {
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
            // 删除链表中的 x 节点（x 一定存在）
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
        class Node {
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
}
