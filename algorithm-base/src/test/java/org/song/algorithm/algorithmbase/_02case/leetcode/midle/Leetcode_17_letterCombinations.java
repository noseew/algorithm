package org.song.algorithm.algorithmbase._02case.leetcode.midle;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 17. 电话号码的字母组合
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。
 * <p>
 * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_17_letterCombinations {

    @Test
    public void test() {

        List<String> list = letterCombinations("23");
        System.out.println(Arrays.toString(list.toArray()));

        System.out.println('0' - 48);
        System.out.println('9' - 48);

    }

    /**
     * 未完成 建议递归试试
     */
    public List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();

        List<String[]> chars = new ArrayList<>();
        for (char c : digits.toCharArray()) {
            chars.add(parse(c - 48));
        }

        int[] bits = new int[chars.size()];

        int bit = chars.size() - 1;
        while (bit >= 0) {
            StringBuilder sb = new StringBuilder(chars.size());
            for (int i = 0; i < chars.size(); i++) {
                sb.append(chars.get(i)[bits[i]]);
                if (i > bit) {

                } else if (i == bit) {

                    bits[i]++;
                    if (bits[i] >= chars.get(i).length) {
                        bit--;
                        bits[i] = 0;
                    }
                }
            }
            res.add(sb.toString());
        }


        return res;
    }

    private String[] parse(int i) {
        switch (i) {
            case 2:
                return new String[]{"a", "b", "c"};
            case 3:
                return new String[]{"d", "e", "f"};
            case 4:
                return new String[]{"g", "h", "i"};
            case 5:
                return new String[]{"j", "k", "l"};
            case 6:
                return new String[]{"m", "n", "o"};
            case 7:
                return new String[]{"p", "q", "r", "s"};
            case 8:
                return new String[]{"t", "u", "v"};
            case 9:
                return new String[]{"w", "x", "y", "z"};
        }
        return new String[]{};
    }

}
