package org.song.algorithm.algorithmbase.alg._02case.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * 344. 反转字符串
 * 编写一个函数，其作用是将输入的字符串反转过来。输入字符串以字符数组 char[] 的形式给出。
 * <p>
 * 不要给另外的数组分配额外的空间，你必须原地修改输入数组、使用 O(1) 的额外空间解决这一问题。
 * <p>
 * 你可以假设数组中的所有字符都是 ASCII 码表中的可打印字符。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reverse-string
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_344_reverseString {

    @Test
    public void test() {
        char[] s = new char[]{'1', '2', '3'};
        reverseString(s);
        System.out.println(Arrays.toString(s));
    }

    /**
     * 遍历并替换
     */
    public void reverseString(char[] s) {
        if (s.length <= 1) {
            return;
        }
        for (int head = 0; head < s.length; head++) {
            char temp = s[head];
            int tail = s.length - head - 1;
            if (tail <= head) {
                break;
            }
            s[head] = s[tail];
            s[tail] = temp;
        }
    }


}
