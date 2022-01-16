package org.song.algorithm.algorithmbase.alg._02case.leetcode.simple;

import org.junit.jupiter.api.Test;

/**
 * 703. 数据流中的第 K 大元素
 * <p>
 * 设计一个找到数据流中第 k 大元素的类（class）。注意是排序后的第 k 大元素，不是第 k 个不同的元素。
 * <p>
 * 请实现 KthLargest 类：
 * <p>
 * KthLargest(int k, int[] nums) 使用整数 k 和整数流 nums 初始化对象。
 * int add(int val) 将 val 插入数据流 nums 后，返回当前数据流中第 k 大的元素。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/kth-largest-element-in-a-stream
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Lootcode_703_KthLargest {

    @Test
    public void test() {
        // 未完成
        KthLargest kthLargest = new KthLargest(1, new int[]{1, 2, 3, 4, 5});
        System.out.println();
    }

    class KthLargest {

        // 大堆
        int[] datas;
        int size = 0;
        int k;

        public KthLargest(int k, int[] nums) {
            datas = nums;
            size = datas.length;
            this.k = k;
        }

        public int add(int val) {

            return this.k;
        }
    }
}
