package org.song.algorithm.algorithmbase._01base.sort;

import org.junit.Test;

import java.util.Arrays;

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
        int[] arr = new int[]{3, 0, 4, 2, 1};
        System.out.println(Arrays.toString(sort_01(arr)));
    }

    /**
     */
    public int[] sort_01(int[] data) {

        // 将数组原地建堆(小堆)
        HeapLittle heapLittle = new HeapLittle(data);
        for (int i = 0; i < data.length; i++) {
            heapLittle.push(data[i]);
        }
        // 依次取出, 并原地排序
        for (int i = 0; i < data.length; i++) {
            data[data.length - i - 1] = heapLittle.pop();
        }
        return data;
    }

    static class HeapLittle {

        int[] datas;
        int size;

        public HeapLittle(int[] datas) {
            this.datas = datas;
        }

        public int pop() {
            int data = datas[0];
            datas[0] = datas[size - 1];
            datas[size - 1] = 0;
            size--;
            shiftDown();
            return data;
        }

        public void push(int val) {
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
                if (datas[parent] > datas[child]) {
                    exchange(parent, child);
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
                int child = datas[left] <= datas[left + 1] || left + 1 >= size ? left : left + 1;
                if (datas[parent] > datas[child] && child < size) {
                    exchange(parent, child);
                    parent = child;
                } else {
                    break;
                }
            }
        }

        private void exchange(int parentIndex, int childIndex) {
            int parent = datas[parentIndex];
            datas[parentIndex] = datas[childIndex];
            datas[childIndex] = parent;
        }

    }
}
