package algorithmbase.leetcode.midle;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
public class Leetcode_03_MaxStr {

    @Test
    public void test() {
        System.out.println(lengthOfLongestSubstring2("bbbbb"));
    }

    /**
     */
    public int lengthOfLongestSubstring(String s) {
        if (s == null) {
            return 0;
        }
        if (s.length() <= 1) {
            return s.length();
        }
        char[] chars = s.toCharArray();
        Map<Character, Integer> maxMap = new HashMap<>();

        Map<Character, Integer> nextMap = new HashMap<>();
        int nextHeadIndex = 0;

        for (int i = 0; i < chars.length; i++) {
            Integer put = nextMap.put(chars[i], i);
            if (put == null) {
                if (nextMap.size() > maxMap.size()) {
                    maxMap.clear();
                    maxMap.putAll(nextMap);
                }
            } else {
                for (int j = nextHeadIndex; j < put; j++) {
                    nextMap.remove(chars[j]);
                }
                nextHeadIndex = put + 1;
            }

        }
        return maxMap.size();
    }

    /**
     */
    public int lengthOfLongestSubstring2(String s) {
        if (s == null) {
            return 0;
        }
        if (s.length() <= 1) {
            return s.length();
        }
        char[] chars = s.toCharArray();
        Map<Character, Integer> maxMap = new HashMap<>();

        Map<Character, Integer> nextMap = new HashMap<>();
        int nextHeadIndex = 0;

        for (int i = 0; i < chars.length; i++) {
            Integer put = nextMap.put(chars[i], i);
            if (put != null) {
                if (nextMap.size() > maxMap.size()) {
                    maxMap.clear();
                    maxMap.putAll(nextMap);
                    maxMap.put(chars[i], put);
                }
                for (int j = nextHeadIndex; j < put; j++) {
                    nextMap.remove(chars[j]);
                }
                nextHeadIndex = put + 1;
            }
        }
        if (nextMap.size() > maxMap.size()) {
            maxMap.clear();
            maxMap.putAll(nextMap);
        }
        return maxMap.size();
    }
}
