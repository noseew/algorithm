package org.song.algorithm.algorithmbase._02case.leetcode.midle;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * 11. 盛最多水的容器
 * 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0) 。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 * <p>
 * 说明：你不能倾斜容器。
 * 输入：[1,8,6,2,5,4,8,3,7]
 * 输出：49
 * 解释：图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
 * 示例 2：
 * <p>
 * 输入：height = [1,1]
 * 输出：1
 * 示例 3：
 * <p>
 * 输入：height = [4,3,2,1,4]
 * 输出：16
 * 示例 4：
 * <p>
 * 输入：height = [1,2,1]
 * 输出：2
 *  
 * <p>
 * 提示：
 * <p>
 * n = height.length
 * 2 <= n <= 3 * 104
 * 0 <= height[i] <= 3 * 104
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/container-with-most-water
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_11_maxArea {

    @Test
    public void test() {
        System.out.println(maxArea2(new int[]{1, 1})); // 1
        System.out.println(maxArea2(new int[]{4, 3, 2, 1, 4})); // 16
        System.out.println(maxArea2(new int[]{1, 2, 1})); // 2
    }

    @Test
    public void test2() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int len = random.nextInt(100);
            int[] height = new int[len];
            for (int j = 0; j < len; j++) {
                height[j] = random.nextInt(100);
            }
            if (maxArea(height) != maxArea2(height)) {
                System.err.println("error" + Arrays.toString(height));
            }
        }
    }

    /**
     * 超时
     */
    public int maxArea(int[] height) {
        if (height == null || height.length <= 1) {
            return 0;
        }
        int max = 0;
        for (int i = 0; i < height.length; i++) {

            for (int j = i; j < height.length; j++) {
                int area = (j - i) * Math.min(height[i], height[j]);
                max = Math.max(area, max);
            }
        }
        return max;

    }

    /**
     * 官方题解
     *
     * @param height
     * @return
     */
    public int maxArea2(int[] height) {
        if (height == null || height.length <= 1) {
            return 0;
        }
        int max = 0;
        int l = 0, r = height.length - 1;
        while (l < r) {
            int area = (r - l) * Math.min(height[l], height[r]);
            max = Math.max(area, max);
            if (height[l] <= height[r]) {
                l++;
            } else {
                r--;
            }
        }
        return max;

    }
}
