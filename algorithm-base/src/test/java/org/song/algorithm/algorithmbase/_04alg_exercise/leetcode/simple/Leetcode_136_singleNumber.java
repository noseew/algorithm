package org.song.algorithm.algorithmbase._04alg_exercise.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.bit._03app.bitmap._01model.BitMap01;

import java.util.*;

/**
 * 136. 只出现一次的数字
 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 * <p>
 * 说明：
 * <p>
 * 你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/single-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_136_singleNumber {

    @Test
    public void test() {
        System.out.println(singleNumber2(new int[]{1, 2, 2}));
    }

    /**
     *
     * @param nums
     * @return
     */
    public int singleNumber(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }

        Set<Integer> set = new HashSet<>(nums.length);

        for (int num : nums) {
            if (!set.contains(num)) {
                set.add(num);
            } else {
                set.remove(num);
            }
        }
        Iterator<Integer> iterator = set.iterator();
        return iterator.next();

    }

    /**
     * 采用异或运算
     * 两个相同的数进行异或等于0
     * a ^ a = 0;
     * b ^ 0 = b
     * 成对出现的a可以相互抵消, 没有成对出现的b就会凸显出来
     *
     * @param nums
     * @return
     */
    public int singleNumber2(int[] nums) {
        int single = 0;
        for (int num : nums) {
            single ^= num;
        }
        return single;
    }

    @Test
    public void testExt() {
        System.out.println(Arrays.toString(singleNumberExt(new int[]{1, 2, 2, 2, 3, 33})));
        System.out.println(Arrays.toString(singleNumberExt2(new int[]{1, 2, 2, 2, 3, 33})));
    }

    /**
     * 海量int数组中, 只出现一次的数字
     * 
     * 上面的题目有限制
     * 1. 数据最多只有一个单独的数字
     * 2. 重复的数字只能重复2的倍数次
     * 
     * 这个拓展题目放开了上面的两个限制
     * 这里增加其他限制
     * 1. 数组中不允许出现负数, 这里不考虑负数, 防止位图溢出, 如果考虑负数, 则要使用long类型位图
     * 
     * 解决思路, 采用 位图, 从而降低内存限制
     * 1. 采用两个相同大小的位图
     * 2. bitmap1用于记录只出现一次的数字, bitmap2用来记录出现多次的数字
     * 3. 将数字放入bitmap1中, 
     * 如果该数字已存在(bitmap1中值为1 或者 bitmap2中值为1), 则将bitmap1该位置设置为0, 同时将bitmap2中该位置设置为1, 表示该数字出现多次
     * 如果该数字不存在(bitmap1中值为0 且 bitmap2中值为0), 则将bitmap1中值为1
     * 4. 统计bitmap1中值为1的对应的int数字, 就是只出现一次的数据
     * 
     * @param nums
     * @return
     */
    public int[] singleNumberExt(int[] nums) {
        // 找到数组中最大值和最小值, 这里不考虑负数, 防止位图溢出, 如果考虑负数, 则要使用long类型位图
        // 最小值和最大值, 来确定位图的大小, 并将最小值映射成位图的第一位, 减小位图的大小
        int min = nums[0], max = nums[0];
        for (int n : nums) {
            if (n > max) {
                max = n;
            }
        }

        // 定义两个位图
        BitMap01 bitmap1 = new BitMap01(max);
        BitMap01 bitmap2 = new BitMap01(max);

        // 将数组数据放入位图
        for (int n : nums) {
            // 如果数字不存在
            if (bitmap1.getBit(n) == 0 && bitmap2.getBit(n) == 0) {
                bitmap1.setBit(n);
                continue;
            }
            // 如果数字已存在, 但只出现1次
            if (bitmap1.getBit(n) == 1) {
                // 将 bitmap1 设为0, 说明该数据有重复的
                bitmap1.removeBit(n);
                // 将 bitmap2 设为1, 说明该数据有重复, 不限次数 >1
                bitmap2.setBit(n);
                continue;
            }
            // 如果数字已存在, 有重复, 不限次数 >1
            if (bitmap2.getBit(n) == 1) {
                // 不做任何处理
            }
        }

        // bitmap1中的数据就是, 只出现1次的数据
        return bitmap1.getValues();

    }

    public int[] singleNumberExt2(int[] nums) {
        // 找到数组中最大值和最小值, 这里不考虑负数, 防止位图溢出, 如果考虑负数, 则要使用long类型位图
        // 最小值和最大值, 来确定位图的大小, 并将最小值映射成位图的第一位, 减小位图的大小
        int min = nums[0], max = nums[0];
        for (int n : nums) {
            if (n < min) {
                min = n;
            }
            if (n > max) {
                max = n;
            }
        }

        // 定义两个位图
        BitMap01 bitmap1 = new BitMap01(max - min + 1);
        BitMap01 bitmap2 = new BitMap01(max - min + 1);

        // 将数组数据放入位图
        for (int n : nums) {
            int bitmapValue = n - min;
            // 如果数字不存在
            if (bitmap1.getBit(bitmapValue) == 0 && bitmap2.getBit(bitmapValue) == 0) {
                bitmap1.setBit(bitmapValue);
                continue;
            }
            // 如果数字已存在, 但只出现1次
            if (bitmap1.getBit(bitmapValue) == 1) {
                // 将 bitmap1 设为0, 说明该数据有重复的
                bitmap1.removeBit(bitmapValue);
                // 将 bitmap2 设为1, 说明该数据有重复, 不限次数 >1
                bitmap2.setBit(bitmapValue);
                continue;
            }
            // 如果数字已存在, 有重复, 不限次数 >1
            if (bitmap2.getBit(bitmapValue) == 1) {
                // 不做任何处理
            }
        }

        // bitmap1中的数据就是, 只出现1次的数据
        int[] values = bitmap1.getValues();
        for (int i = 0; i < values.length; i++) {
            // 弥补偏差
            values[i] += min;
        }
        return values;

    }

}
