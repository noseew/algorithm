package algorithmbase.leetcode.midle;

import org.junit.Test;

/**
 * 5. 最长回文子串
 * 给你一个字符串 s，找到 s 中最长的回文子串。
 *
 * 示例 1：
 *
 * 输入：s = "babad"
 * 输出："bab"
 * 解释："aba" 同样是符合题意的答案。
 * 示例 2：
 *
 * 输入：s = "cbbd"
 * 输出："bb"
 * 示例 3：
 *
 * 输入：s = "a"
 * 输出："a"
 * 示例 4：
 *
 * 输入：s = "ac"
 * 输出："a"
 *  
 *
 * 提示：
 *
 * 1 <= s.length <= 1000
 * s 仅由数字和英文字母（大写和/或小写）组成
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-palindromic-substring
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_05_LongestPalindrome {

    @Test
    public void test() {
        System.out.println(longestPalindrome("cccccc"));
        System.out.println();
    }

    /**
     * 未完成
     *
     */
    public String longestPalindrome(String s) {

        if (s.length() <= 1) {
            return s;
        }

        char[] chars = s.toCharArray();

        int startIndex = 0;
        int maxLen = 1;

        for (int i = 1; i < chars.length; i++) {

            if (chars[i] == chars[i - 1]) {
                // 偶对称
                int maxLenTemp = 2;
                int startIndexTemp = i - 1;
                for (int j = 0; j < chars.length - i - 1 && j <= i; j++) {
                    if (i + j <= chars.length - 1 && i - j - 1 >= 0
                            && chars[i + j] == chars[i - j - 1]) {
                        maxLenTemp = j * 2;
                        startIndexTemp = i - j - 1;
                    }
                }
                if (maxLenTemp > maxLen) {
                    startIndex = startIndexTemp;
                    maxLen = maxLenTemp;
                }
            }
            if (i > 1 && chars[i] == chars[i - 2]) {
                // 奇对称
                int maxLenTemp = 3;
                int startIndexTemp = i - 2;
                for (int j = 2; j < chars.length - i - 1 && j <= i; j++) {
                    if (i + j <= chars.length - 1 && i - j >= 0
                            && chars[i + j] == chars[i - j]) {
                        maxLenTemp = j * 2 + 1;
                        startIndexTemp = i - j;
                    }
                }
                if (maxLenTemp > maxLen) {
                    startIndex = startIndexTemp;
                    maxLen = maxLenTemp;
                }
            }

        }

        return s.substring(startIndex, startIndex + maxLen);
    }
}
