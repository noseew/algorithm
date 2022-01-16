package org.song.algorithm.algorithmbase.alg._01sort.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase.alg._01sort.AbstractSort;

/**
 * 堆排序
 * 非比较排序, 而是利用额外工具 堆(也可以将数组原地变堆), 进行排序,
 * 思路和选择排序类似
 * 选择排序: 在剩下的数组中选择最小的数据, 依次码放
 * 堆排序: 从堆中选择最小的数据, 依次码放, 区别在于,
 * -    堆选出来最小的数据的复杂度是O(1), 这是由堆得性质决定的,
 * -    同时, 还要将数组中的数据放入堆中
 * -    如果是原地变堆, 则过程是将堆顶元素和数组最后元素交换
 * <p>
 * 复杂度是 O(logn), 主要在数组堆化这一步是 O(logn) 的
 */
public class Sort_Heap {


    @Test
    public void test() {
        Comparable[] build = AbstractSort.build(10);

        new HeapSort().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }
    
    public static class HeapSort extends AbstractSort {
        @Override
        public void sort(Comparable[] cs) {
            // 将数组建堆(小堆)
            HeapLittle heapLittle = new HeapLittle(cs.length);
            for (int i = 0; i < cs.length; i++) {
                heapLittle.push(cs[i]);
            }
            // 依次取出, 并原地排序
            for (int i = 0; i < cs.length; i++) {
//                cs[cs.length - i - 1] = heapLittle.pop();
                cs[i] = heapLittle.pop();
            }
        }

        static class HeapLittle {

            Comparable[] datas;
            int size;

            public HeapLittle(int capacity) {
                this.datas = new Comparable[capacity];
            }

            public Comparable pop() {
                Comparable cs = datas[0];
                datas[0] = datas[size - 1];
                datas[size - 1] = 0;
                size--;
                shiftDown();
                return cs;
            }

            public void push(Comparable val) {
                datas[size++] = val;
                if (size == 1) {
                    return;
                }
                shiftUp(size - 1);
            }

            private void shiftUp(int i) {
                int child = i;
                int parent;
                while ((parent = (child - 1) >> 1) >= 0) {
                    if (less(datas[child], datas[parent])) {
                        exchange(datas, parent, child);
                        child = parent;
                    } else {
                        break;
                    }
                }
            }

            private void shiftDown() {
                int parent = 0;
                int left;
                while ((left = ((parent << 1) + 1)) < size) {
                    int child = lessEq(datas[left], datas[left + 1]) || left + 1 >= size ? left : left + 1;
                    if (less(datas[child], datas[parent]) && child < size) {
                        exchange(datas, parent, child);
                        parent = child;
                    } else {
                        break;
                    }
                }
            }

        }
    }
}
