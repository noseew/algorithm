package algorithmbase.leetcode.midle;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * 3. 无重复字符的最长子串
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
 * <p>
 *  
 * <p>
 * 示例 1:
 * <p>
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 示例 2:
 * <p>
 * 输入: s = "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * 示例 3:
 * <p>
 * 输入: s = "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 *      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 * 示例 4:
 * <p>
 * 输入: s = ""
 * 输出: 0
 *  
 * <p>
 * 提示：
 * <p>
 * 0 <= s.length <= 5 * 104
 * s 由英文字母、数字、符号和空格组成
 * <p>
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-substring-without-repeating-characters
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leecode_03_MaxStr {

    @Test
    public void test() {

    }

    /**
     * 未完成
     *
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        if (s == null) {
            return 0;
        }
        if (s.length() <= 1) {
            return s.length();
        }
        char[] chars = s.toCharArray();
        Set<Character> maxSet = new HashSet<>();
        int maxHeadIndex = 0;
        int maxTailIndex = 0;

        Set<Character> nextSet = new HashSet<>();
        int nextHeadIndex = -1;
        int nextTailIndex = -1;

        boolean hasRepeat = false;
        for (int i = 0; i < chars.length; i++) {
            if (nextSet.size() > maxSet.size()) {
                maxSet.clear();
                maxSet.addAll(nextSet);
                nextSet.clear();
                maxHeadIndex = nextHeadIndex;
                maxTailIndex = nextTailIndex;
                nextHeadIndex = -1;
                nextTailIndex = -1;

            }
            if (hasRepeat) {
                i = maxHeadIndex + 1;
                continue;
            }

            if (nextSet.add(chars[i]) && !hasRepeat) {
                maxTailIndex = i;
            } else {
                hasRepeat = true;
                nextHeadIndex = maxHeadIndex + 1;
                nextTailIndex = i;

            }

        }

        return 0;

    }
}
