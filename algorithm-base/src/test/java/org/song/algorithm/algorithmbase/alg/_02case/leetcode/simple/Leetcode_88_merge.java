package org.song.algorithm.algorithmbase.alg._02case.leetcode.simple;

import org.junit.Test;

/**
 * 88. 合并两个有序数组
 * 给你两个有序整数数组 nums1 和 nums2，请你将 nums2 合并到 nums1 中，使 nums1 成为一个有序数组。
 * <p>
 * 初始化 nums1 和 nums2 的元素数量分别为 m 和 n 。你可以假设 nums1 的空间大小等于 m + n，这样它就有足够的空间保存来自 nums2 的元素。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
 * 输出：[1,2,2,3,5,6]
 * 示例 2：
 * <p>
 * 输入：nums1 = [1], m = 1, nums2 = [], n = 0
 * 输出：[1]
 *  
 * <p>
 * 提示：
 * <p>
 * nums1.length == m + n
 * nums2.length == n
 * 0 <= m, n <= 200
 * 1 <= m + n <= 200
 * -109 <= nums1[i], nums2[i] <= 109
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/merge-sorted-array
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_88_merge {

    @Test
    public void test() {
        int[] ints = {2,0};
        merge(ints, 1, new int[]{1}, 1);
        System.out.println();
    }

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        if (n == 0) {
            return;
        }
        if (m == 0 && n == 1) {
            nums1[0] = nums2[0];
            return;
        }

        int mNext = m - 1;
        int nNext = n - 1;
        for (int last = nums1.length - 1; last >= 0; last--) {
            if (nNext < 0) {
                break;
            }
            if (mNext < 0) {
                for (int j = 0; j <= nNext; j++) {
                    nums1[j] = nums2[j];
                }
                break;
            }
            if (nums1[mNext] > nums2[nNext]) {
                nums1[last] = nums1[mNext];
                mNext--;
            } else {
                nums1[last] = nums2[nNext];
                nNext--;
            }
        }
    }
}
