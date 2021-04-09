package algorithmbase.leetcode.simple._02string;

import org.junit.Test;

/**
 * 58. 最后一个单词的长度
 * <p>
 * 给你一个字符串 s，由若干单词组成，单词之间用空格隔开。返回字符串中最后一个单词的长度。如果不存在最后一个单词，请返回 0 。
 * <p>
 * 单词 是指仅由字母组成、不包含任何空格字符的最大子字符串。
 * <p>
 * 示例 1：
 * <p>
 * 输入：s = "Hello World"
 * 输出：5
 * 示例 2：
 * <p>
 * 输入：s = " "
 * 输出：0
 *  
 * <p>
 * 提示：
 * <p>
 * 1 <= s.length <= 104
 * s 仅有英文字母和空格 ' ' 组成
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/length-of-last-word
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_58_LastWord {

    @Test
    public void test() {
        System.out.println(lengthOfLastWord("a a  "));
    }

    public int lengthOfLastWord(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] chars = s.toCharArray();
        int lastWordStart = -1;
        int lastWordEnd = -1;
        boolean hasWord = false;
        for (int i = chars.length - 1; i >= 0; i--) {
            hasWord = chars[i] != ' ';
            if (hasWord && lastWordEnd < 0) {
                lastWordEnd = i;
            } else if (!hasWord && lastWordEnd >= 0) {
                lastWordStart = i;
            }
            if (lastWordEnd >= 0 && lastWordStart >= 0) {
                break;
            }
        }
        return lastWordEnd - lastWordStart;
    }

    public int lengthOfLastWord2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] chars = s.toCharArray();
        int lastWordStart = -1, lastWordEnd = -1;
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] != ' ' && lastWordEnd < 0) {
                lastWordEnd = i;
            } else if (chars[i] == ' ' && lastWordEnd >= 0) {
                lastWordStart = i;
                break;
            }
        }
        return lastWordEnd - lastWordStart;
    }
}
