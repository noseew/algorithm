package org.song.algorithm.base._01datatype._03app.elimination.lfu;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._03app.elimination.AbstractEliminate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/*
window 

window 就是通过窗口解决数据老化数据不被淘汰的问题, 缓存污染

 */
public class LFU03_WLFU {
    /*
    https://blog.csdn.net/yunhua_lee/article/details/7648549
    Windows-LFU是LFU的一个改进版，差别在于Window-LFU并不记录所有数据的访问历史，而只是记录过去一段时间内的访问历史，这就是Window的由来，基于这个原因，传统的LFU又被称为“Perfect-LFU”。
    
    
    https://blog.csdn.net/weixin_38569499/article/details/113268370
    window-LFU算法描述
    Window-LFU被用来一定程度上解决LFU上述两步不可避免的问题. 
    Window是用来描述算法保存的历史请求数量的窗口大小的. Window-LFU并不记录所有数据的访问历史, 而只是记录过去一段时间内的访问历史. 即当请求次数达到或超过window的大小后, 每次有一条新的请求到来, 都会清理掉最旧的一条请求, 以保证算法记录的请求次数不超过window的大小. 
    Window-LFU的优缺点
    3.1 优点
        由于Window-LFU不存储全部历史数据, 所以其额外内存开销要明显低于LFU算法, 同时由于数据量明显减少, Window-LFU排序的处理器成本也要低于LFU. 
        另外, 由于早于前window次的缓存访问记录会被清理掉, 所以Window-LFU也可以避免缓存污染的问题, 因为过早时间访问的缓存已经被清理掉了. 
        在缓存命中率方面, Window-LFU一般会由于LFU, 因为Window-LFU一定程度上解决了缓存污染的问题, 缓存的有效性更高了. 缓存污染问题越严重的场景, Window-LFU的命中率就比LFU高的越多. 
       另外, 和LFU-Aging相比, Window-LFU对缓存污染问题的解决更彻底一些, 所以在缓存使用场景产生改变时, 命中率会优于LFU-Aging. 
    3.2 缺点
        但是Window-LFU需要维护一个队列去记录历史的访问流, 复杂度略高于LFU. 
     */


    @Test
    public void test() {
        WindowLFU<String, Integer> cache = new WindowLFU<>(10, 20);
        Random random = new Random();
        // cal func(x,y) = 3*x+y
        for (int i = 0; i < 1000; i++) {
            int x = random.nextInt() % 5;
            int y = random.nextInt() % 5;
            String key = String.format("x=%d,y=%d", x, y);
            if (cache.get(key) == null) {
                cache.put(key, 3 * x + y);
            }
        }
        System.out.println("HitRate=" + cache.hitrate());
    }

    @Test
    public void test2() {
        WindowLFU<String, Integer> cache = new WindowLFU<>(5, 10);
        WindowLFU2<String, Integer> cache2 = new WindowLFU2<>(5, 10);
        Random random = new Random();
        // cal func(x,y) = 3*x+y
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt() % 5;
            int y = random.nextInt() % 5;
            String key = String.format("x=%d,y=%d", x, y);
            if (cache.get(key) == null) {
                cache.put(key, 3 * x + y);
            }
            if (cache2.get(key) == null) {
                cache2.put(key, 3 * x + y);
            }
        }
        System.out.println("HitRate=" + cache.hitrate());
    }

    public static class WindowLFU<K, V extends Comparable<V>> {
        private MinHeap<K, V> minHeap;
        private Map<K, Node2<K, V>> dataMap;
        private LinkedList<K> window;
        private int windowSize;
        private int total;
        private int hits;


        public WindowLFU(int cacheSize, int windowSize) {
            minHeap = new MinHeap<>(cacheSize);
            window = new LinkedList<>();
            dataMap = new HashMap<>((int) ((float) cacheSize / 0.75F + 1.0F));
            this.windowSize = windowSize;
            total = 0;
            hits = 0;
        }

        public void put(K key, V value) {
            if (key == null || value == null) {
                return;
            }

            appendWindow(key);
            Node2<K, V> previous;
            if ((previous = dataMap.get(key)) != null) { // exists
                previous.setValue(value);
                minHeap.reVisited(previous.getIndex());
            } else {
                if (minHeap.isFull()) {
                    dataMap.remove(minHeap.getMin()
                            .getKey());
                }
                int cnt = 0;
                for (K k : window) {
                    if (k.equals(key)) {
                        cnt++;
                    }
                }
                Node2<K, V> node = new Node2<>(key, value, cnt);
                dataMap.put(key, node);
                minHeap.add(node);
            }
        }

        public V get(K key) {
            total++;
            V value = null;
            if (key != null) {
                Node2<K, V> node = dataMap.get(key);
                if (node != null) {
                    hits++;
                    appendWindow(key);
                    value = node.getValue();
                    minHeap.reVisited(node.getIndex());
                }
            }
            return value;
        }

        private void appendWindow(K key) {
            window.offer(key);
            if (dataMap.containsKey(key)) {
                // 进一次窗口, 次数+1
                dataMap.get(key).addCount(1);
            }
            if (window.size() > windowSize) {
                K first = window.poll(); // 移除队首元素
                if (dataMap.containsKey(first)) {
                    // 出一次窗口, 次数-1
                    dataMap.get(first).addCount(-1);
                }
            }

        }

        public float hitrate() {
            if (total == 0) {
                return 0;
            }
            return (float) hits / total;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("\r\n").append("数据map").append("\r\n");
            dataMap.forEach((k, v) -> {
                sb.append("key=").append(k).append(", val={").append(v).append("}").append("\r\n");
            });
            sb.append("window").append("\r\n");
            sb.append("[");
            window.forEach(e -> {
                sb.append(e).append(",");
            });
            sb.append("]");
            return sb.toString();
        }


        @Data
        static class Node2<K, V extends Comparable<V>> implements Comparable<Node2<K, V>> {
            private K key;
            private V value;
            private int index;
            private int counts;
            private long lastTime;

            Node2(K key, V value, int counts) {
                this.key = key;
                this.value = value;
                this.counts = counts;
            }

            @Override
            public int compareTo(Node2<K, V> node) {
                if (counts == node.counts) {
                    return (int) (this.lastTime - node.lastTime);
                }
                return counts - node.counts;
            }

            void addCount(int val) {
                this.counts += val;
            }

        }

        static class MinHeap<K, V extends Comparable<V>> {
            private Node2<K, V>[] heap;
            private int currentSize;
            private long count;

            MinHeap(int size) {
                count = 0;
                currentSize = 1;
                heap = new Node2[size + 1];
            }

            boolean isFull() {
                return currentSize >= heap.length;
            }

            Node2<K, V> add(Node2<K, V> value) {
                Node2<K, V> previous = value;
                if (currentSize >= heap.length) {
                    previous = removeMin();
                }
                value.setLastTime(count++);
                value.setIndex(currentSize);
                heap[currentSize++] = value;
                siftUp(currentSize - 1);
                return previous;
            }

            Node2<K, V> getMin() {
                return heap[1];
            }

            Node2<K, V> removeMin() {
                return remove(1);
            }

            /**
             * 堆元素不会主动排序, 而是每次新增的时候才会排序, 所以需要将元素删除, 然后再新增
             *
             * @param index
             * @return
             */
            Node2<K, V> reVisited(int index) {
                Node2<K, V> node = heap[index];
                remove(node.getIndex());
                add(node);
                return node;
            }

            Node2<K, V> remove(int index) {
                Node2<K, V> previous = heap[index];
                heap[index] = heap[--currentSize];
                siftDown(index);
                return previous;
            }

            private void siftDown(int index) {
                int left = 2 * index;
                int right = 2 * index + 1;
                int largest;
                if (left < currentSize && heap[left].compareTo(heap[index]) < 0)
                    largest = left;
                else
                    largest = index;
                if (right < currentSize && heap[right].compareTo(heap[largest]) < 0)
                    largest = right;
                if (largest != index) {
                    Node2<K, V> temp = heap[index];
                    heap[index] = heap[largest];
                    heap[largest] = temp;
                    heap[index].setIndex(largest);
                    heap[largest].setIndex(index);
                    siftDown(largest);
                }
            }

            private void siftUp(int index) {
                while (index > 1 && heap[index].compareTo(heap[index / 2]) < 0) {
                    Node2<K, V> temp = heap[index];
                    heap[index] = heap[index / 2];
                    heap[index / 2] = temp;
                    heap[index].setIndex(index / 2);
                    heap[index / 2].setIndex(index);
                    index = index / 2;
                }
            }
        }
    }

    /**
     * LFU 基础上增加了一个 窗口
     * 出窗口 访问次数-1
     * 通过出窗口操作, 将访问次数逐渐减少, 这样就不会存留缓存污染的数据了(瞬间访问次数过多, 之后就不再访问的缓存, 迟迟得不到淘汰)
     * 
     * 特点: 窗口的大小就是访问次数最高的大小, 
     * 
     * @param <K>
     * @param <V>
     */
    public static class WindowLFU2<K, V extends Comparable<V>> extends AbstractEliminate<K, V> {

        Map<Integer, InnerLinked> timesMap = new HashMap<>(); // 访问次数map
        private Map<K, Node> dataMap;
        private LinkedList<K> window;
        private int windowSize;
        int minTimes;


        public WindowLFU2(int cacheSize, int windowSize) {
            super(cacheSize);
            window = new LinkedList<>();
            dataMap = new HashMap<>((int) ((float) cacheSize / 0.75F + 1.0F));
            this.windowSize = windowSize;
        }

        public V get(K key) {
            Node node = dataMap.get(key);
            if (node == null) {
                return null;
            }
            addNodeTimes(node, 1);
            appendWindow(node);
            return node.val;
        }

        public V put(K key, V value) {
            if (key == null || value == null || capacity == 0) {
                return null;
            }
            Node node = dataMap.get(key);
            V oldVal = null;
            if (node == null) { // put新元素
                if (dataMap.size() == capacity) {
                    // 需要删除
                    InnerLinked innerLinked = timesMap.get(minTimes);
                    Node removeNode = innerLinked.getLast();
                    removeNode(removeNode);
                }
                int times = 1;
                for (K k : window) {
                    if (k.equals(key)) {
                        times++;
                    }
                }
                node = new Node(key, value, times);
                addNode(node);
                minTimes = Math.min(minTimes, times); // 最小次数重置
            } else {
                oldVal = node.val;
                node.val = value;
                addNodeTimes(node, 1);
            }
            appendWindow(node);
            return oldVal;
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

        private void addNodeTimes(Node node, int addTimes) {
            // 从次数小的LRU链表中删除
            removeNode(node);
            // 添加到次数大的LRU链表头中
            node.times = node.times + addTimes;
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

        /**
         * 每一次访问, 都会进入一次窗口, 可以重复进入窗口
         * 每次出窗口, 访问次数就会-1, 也就是次数衰减
         * 如果入窗口和出窗口是同一个缓存, 则总体上次数不变, 维持原来次数,  
         * 
         * @param node
         */
        private void appendWindow(Node node) {
            window.offer(node.key);
            if (window.size() > windowSize) {
                K tailKey = window.poll(); // 移除队首元素
                Node tail = dataMap.get(tailKey);
                if (tail != null) {
                    // 出一次窗口, 次数-1
                    addNodeTimes(tail, -1);
                }
            }
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
            sb.append("window").append("\r\n");
            sb.append("[");
            window.forEach(e -> {
                sb.append(e).append(",");
            });
            sb.append("]");
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

            public Node(K k, V v, int times) {
                this.key = k;
                this.val = v;
                this.times = times;
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
