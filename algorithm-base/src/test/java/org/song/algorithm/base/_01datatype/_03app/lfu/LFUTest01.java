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

        Map<Integer, DoubleList> freMap = new HashMap<>(); // 访问次数map
        Map<K, Integer> keyFreMap = new HashMap<>();
        Map<K, Node> keyNodeMap = new HashMap<>(); // 数据map
        int cap;
        int minFre;

        public LFUCache(int capacity) {
            cap = capacity;
        }

        public V get(K key) {
            if (!keyFreMap.containsKey(key)) {
                return null;
            }
            int fre = keyFreMap.get(key);
            Node oldNode = keyNodeMap.get(key);
            V retVal = oldNode.val;
            // 删除旧的
            removeExistKey(key);
            // 设置新的
            addNewNode(fre + 1, oldNode);
            return retVal;
        }

        public void put(K key, V value) {
            if (cap == 0) {
                return;
            }
            if (!keyFreMap.containsKey(key)) { // put新元素
                if (keyNodeMap.size() == cap) {
                    // 需要删除
                    DoubleList doubleList = freMap.get(minFre);
                    Node removeNode = doubleList.getLast();
                    removeExistKey(removeNode.key);
                }
                Node newNode = new Node(key, value);
                // 添加新节点，其频率是1
                addNewNode(1, newNode);
                minFre = 1;
            } else {
                int fre = keyFreMap.get(key);
                // 删除旧的
                removeExistKey(key);
                // 设置新的
                Node newNode = new Node(key, value);
                addNewNode(fre + 1, newNode);
            }
        }

        // 删除原来就存在的一个key
        private void removeExistKey(K key) {
            int fre = keyFreMap.get(key);
            DoubleList doubleList = freMap.get(fre);
            // 删除老的
            Node oldNode = keyNodeMap.get(key);
            doubleList.remove(oldNode);
            keyNodeMap.remove(key);
            keyFreMap.remove(key);
            if (doubleList.isEmpty()) {
                freMap.remove(fre);
                if (minFre == fre) {
                    minFre++;
                }
            }
        }

        // 添加新节点，fre为新节点的fre， node为新Node
        private void addNewNode(int fre, Node node) {
            // 设置新的
            DoubleList doubleList = freMap.getOrDefault(fre, new DoubleList());
            doubleList.addFirst(node);
            keyNodeMap.put(node.key, doubleList.getFirst());
            freMap.put(fre, doubleList);
            keyFreMap.put(node.key, fre);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("次数map").append("\r\n");
            freMap.forEach((k, v) -> {
                sb.append("key=").append(k).append(", val={").append(v).append("}").append("\r\n");
            });
            sb.append("\r\n").append("数据map").append("\r\n");
            keyNodeMap.forEach((k, v) -> {
                sb.append("key=").append(k).append(", val={").append(v).append("}").append("\r\n");
            });
            return sb.toString();
        }

        class DoubleList {
            private Node head, tail; // 头尾虚节点
            private int size; // 链表元素数

            public DoubleList() {
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
            // 删除链表中最后一个节点，并返回该节点
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
