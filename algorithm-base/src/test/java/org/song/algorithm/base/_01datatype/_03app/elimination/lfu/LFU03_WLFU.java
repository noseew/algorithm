package org.song.algorithm.base._01datatype._03app.elimination.lfu;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

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

    public static class WindowLFU<K, V extends Comparable<V>> {
        private MinHeap<K, V> minHeap;
        private Map<K, Node<K, V>> map;
        private LinkedList<K> window;
        private int windowSize;
        private int total;
        private int hits;


        public WindowLFU(int cacheSize, int windowSize) {
            minHeap = new MinHeap<>(cacheSize);
            window = new LinkedList<>();
            map = new HashMap<>((int) ((float) cacheSize / 0.75F + 1.0F));
            this.windowSize = windowSize;
            total = 0;
            hits = 0;
        }

        public void put(K key, V value) {
            if (key == null || value == null) {
                return;
            }

            appendWindow(key);
            Node<K, V> previous;
            if ((previous = map.get(key)) != null) { // exists
                previous.setValue(value);
                minHeap.reVisited(previous.getIndex());
            } else {
                if (minHeap.isFull()) {
                    map.remove(minHeap.getMin()
                            .getKey());
                }
                int cnt = 0;
                for (K k : window) {
                    if (k.equals(key)) {
                        cnt++;
                    }
                }
                Node<K, V> node = new Node<>(key, value, cnt);
                map.put(key, node);
                minHeap.add(node);
            }
        }

        public V get(K key) {
            total++;
            V value = null;
            if (key != null) {
                Node<K, V> node = map.get(key);
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
            if (map.containsKey(key)) {
                // 进一次窗口, 次数+1
                map.get(key).addCount(1);
            }
            if (window.size() > windowSize) {
                K first = window.poll(); // 移除队首元素
                if (map.containsKey(first)) {
                    // 出一次窗口, 次数-1
                    map.get(first).addCount(-1);
                }
            }

        }

        public float hitrate() {
            if (total == 0) {
                return 0;
            }
            return (float) hits / total;
        }


        @Data
        static class Node<K, V extends Comparable<V>> implements Comparable<Node<K, V>> {
            private K key;
            private V value;
            private int index;
            private int counts;
            private long lastTime;

            Node(K key, V value, int counts) {
                this.key = key;
                this.value = value;
                this.counts = counts;
            }

            @Override
            public int compareTo(Node<K, V> node) {
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
            private Node<K, V>[] heap;
            private int currentSize;
            private long count;

            MinHeap(int size) {
                count = 0;
                currentSize = 1;
                heap = new Node[size + 1];
            }

            boolean isFull() {
                return currentSize >= heap.length;
            }

            Node<K, V> add(Node<K, V> value) {
                Node<K, V> previous = value;
                if (currentSize >= heap.length) {
                    previous = removeMin();
                }
                value.setLastTime(count++);
                value.setIndex(currentSize);
                heap[currentSize++] = value;
                siftUp(currentSize - 1);
                return previous;
            }

            Node<K, V> getMin() {
                return heap[1];
            }

            Node<K, V> removeMin() {
                return remove(1);
            }

            /**
             * 堆元素不会主动排序, 而是每次新增的时候才会排序, 所以需要将元素删除, 然后再新增
             * 
             * @param index
             * @return
             */
            Node<K, V> reVisited(int index) {
                Node<K, V> node = heap[index];
                remove(node.getIndex());
                add(node);
                return node;
            }

            Node<K, V> remove(int index) {
                Node<K, V> previous = heap[index];
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
                    Node<K, V> temp = heap[index];
                    heap[index] = heap[largest];
                    heap[largest] = temp;
                    heap[index].setIndex(largest);
                    heap[largest].setIndex(index);
                    siftDown(largest);
                }
            }

            private void siftUp(int index) {
                while (index > 1 && heap[index].compareTo(heap[index / 2]) < 0) {
                    Node<K, V> temp = heap[index];
                    heap[index] = heap[index / 2];
                    heap[index / 2] = temp;
                    heap[index].setIndex(index / 2);
                    heap[index / 2].setIndex(index);
                    index = index / 2;
                }
            }
        }
    }

}
