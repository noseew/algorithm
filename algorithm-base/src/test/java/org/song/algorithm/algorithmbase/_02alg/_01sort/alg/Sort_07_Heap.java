package org.song.algorithm.algorithmbase._02alg._01sort.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.heap.Heap_base_03;
import org.song.algorithm.algorithmbase._02alg._01sort.AbstractSort;

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
public class Sort_07_Heap {


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
            // 将数组原地建堆(如果需要升序, 则需要大堆)
            Heap_base_03 heapLittle = new Heap_base_03(false, cs);
            heapLittle.heapify3();
            
            // 依次取出, 并原地排序, 取出大堆头数据放入队尾, 完成后正好是升序
            for (int i = 0; i < cs.length; i++) {
                cs[cs.length - i - 1] = (Comparable) heapLittle.pop();
            }
        }

    }
}
