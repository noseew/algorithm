package org.song.algorithm.base._02alg.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

public class QueueBase {

    @Test
    public void test01() {
        System.out.println(Arrays.toString(monotonic(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));
    }

    /**
     * 单调队列, 取窗口最值
     */
    public int[] monotonic(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k < 1 || k > nums.length) return null;
        if (k == 1) return nums;

        LinkedList<Integer> queue = new LinkedList<>();
        int[] max_k = new int[nums.length - k + 1];

        for (int w_end = 0; w_end < nums.length; w_end++) {
            int w_start = w_end - k + 1;
            while (!queue.isEmpty() && queue.peekFirst() < w_start) queue.pollFirst();
            while (!queue.isEmpty() && queue.peekLast() <= nums[w_end]) queue.pollLast();
            queue.addLast(w_end);
            System.out.printf("%s queue=%s \r\n", w_end, queue.toString());
            if (w_start >= 0) {
                max_k[w_start] = nums[queue.peekFirst()];
                System.out.printf("%s array=%s \r\n", w_start, Arrays.toString(max_k));
            }
        }
        return max_k;
    }


}
