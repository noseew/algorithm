package algorithmbase.leetcode.simple._02string;

import org.junit.Test;

import java.util.Arrays;

/**
 * 66. 加一
 * 给定一个由 整数 组成的 非空 数组所表示的非负整数，在该数的基础上加一。
 * <p>
 * 最高位数字存放在数组的首位， 数组中每个元素只存储单个数字。
 * <p>
 * 你可以假设除了整数 0 之外，这个整数不会以零开头。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：digits = [1,2,3]
 * 输出：[1,2,4]
 * 解释：输入数组表示数字 123。
 * 示例 2：
 * <p>
 * 输入：digits = [4,3,2,1]
 * 输出：[4,3,2,2]
 * 解释：输入数组表示数字 4321。
 * 示例 3：
 * <p>
 * 输入：digits = [0]
 * 输出：[1]
 *  
 * <p>
 * 提示：
 * <p>
 * 1 <= digits.length <= 100
 * 0 <= digits[i] <= 9
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/plus-one
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_66_PlusOne {

    @Test
    public void test() {
        String s = Arrays.toString(plusOne3(new int[]{1, 2, 3}));
        System.out.println(s);
    }

    public int[] plusOne(int[] digits) {
        int[] ints = new int[digits.length];
        System.arraycopy(digits, 0, ints, 0, digits.length);
        boolean plus = false;
        for (int i = ints.length - 1; i >= 0; i--) {
            if (plus) {
                ints[i]++;
                plus = false;
            }
            if (i == ints.length - 1) {
                ints[i]++;
            }
            if (ints[i] > 9) {
                ints[i] = ints[i] % 10;
                plus = true;
            } else {
                break;
            }
        }
        if (plus) {
            int[] newInts = new int[ints.length + 1];
            newInts[0] = 1;
            System.arraycopy(ints, 0, newInts, 1, ints.length);
            return newInts;
        }
        return ints;
    }

    public int[] plusOne2(int[] digits) {
        int[] ints = new int[digits.length];
        boolean plus = false;
        for (int i = ints.length - 1; i >= 0; i--) {
            ints[i] = digits[i];
            if (plus) {
                ints[i]++;
                plus = false;
            }
            if (i == ints.length - 1) {
                ints[i]++;
            }
            if (ints[i] > 9) {
                ints[i] = ints[i] % 10;
                plus = true;
            }
        }
        if (plus) {
            int[] newInts = new int[ints.length + 1];
            newInts[0] = 1;
            System.arraycopy(ints, 0, newInts, 1, ints.length);
            return newInts;
        }
        return ints;
    }

    public int[] plusOne3(int[] digits) {
        int[] ints = new int[digits.length];
        int[] ints2 = new int[digits.length + 1];
        boolean plus = false;
        for (int i = ints.length - 1; i >= 0; i--) {
            ints[i] = digits[i];
            ints2[i + 1] = digits[i];
            if (plus) {
                ints[i]++;
                plus = false;
            }
            if (i == ints.length - 1) {
                ints[i]++;
                ints2[i + 1]++;
            }
            if (ints[i] > 9) {
                ints[i] = ints[i] % 10;
                ints2[i + 1] = ints[i] % 10;
                plus = true;
            }
        }
        if (plus) {
            ints2[0] = 1;
            return ints2;
        }
        return ints;
    }

}
