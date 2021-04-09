package algorithmbase.leetcode.simple._02string;

import org.junit.Test;

/**
 * 67. 二进制求和
 * 给你两个二进制字符串，返回它们的和（用二进制表示）。
 * <p>
 * 输入为 非空 字符串且只包含数字 1 和 0。
 * <p>
 *  
 * <p>
 * 示例 1:
 * <p>
 * 输入: a = "11", b = "1"
 * 输出: "100"
 * 示例 2:
 * <p>
 * 输入: a = "1010", b = "1011"
 * 输出: "10101"
 *  
 * <p>
 * 提示：
 * <p>
 * 每个字符串仅由字符 '0' 或 '1' 组成。
 * 1 <= a.length, b.length <= 10^4
 * 字符串如果不是 "0" ，就都不含前导零。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-binary
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_67_AddBinary {

    @Test
    public void test() {
        System.out.println(addBinary2("11", "1"));
    }

    /**
     * 数据溢出问题, 未通过
     */
    public String addBinary(String a, String b) {

        char[] achars = a.toCharArray();
        int aint = 0;
        for (int i = achars.length - 1; i >= 0; i--) {
            int val = achars[i] == 49 ? 1 : 0;
            aint |= (val << (achars.length - i - 1));
        }
        char[] bchars = b.toCharArray();
        int bint = 0;
        for (int i = bchars.length - 1; i >= 0; i--) {
            int val = bchars[i] == 49 ? 1 : 0;
            bint |= (val << (bchars.length - i - 1));
        }

        return Integer.toBinaryString(aint + bint);
    }

    public String addBinary2(String a, String b) {

        char[] achars = a.toCharArray();
        char[] bchars = b.toCharArray();
        int max = Math.max(achars.length, bchars.length);
        char[] res = new char[max + 1];
        boolean plus = false;
        for (int i = 0; i < max; i++) {
            int acharindex = achars.length - i - 1;
            int aval = 0;
            if (acharindex >= 0) {
                aval = achars[acharindex] == '1' ? 1 : 0;
            }
            int bcharindex = bchars.length - i - 1;
            int bval = 0;
            if (bcharindex >= 0) {
                bval = bchars[bcharindex] == '1' ? 1 : 0;
            }
            int newVal = aval + bval;
            if (plus) {
                newVal++;
            }
            if (newVal <= 1) {
                char newChar = newVal == 1 ? '1' : '0';
                res[max - i] = newChar;
                plus = false;
            } else {
                char newChar = (newVal & 1) == 1 ? '1' : '0';
                res[max - i] = newChar;
                plus = true;
            }
        }

        if (plus) {
            res[0] = '1';
            return String.valueOf(res);
        }
        return String.valueOf(res).substring(1);
    }
}
