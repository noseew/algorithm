package org.song.algorithm.base._01datatype._01base._03string_array_matrix._01string._01model;

public class StringBaseKMP01 extends StringBase {

    public StringBaseKMP01(boolean chars, char... s) {
        super(chars, s);
    }

    public StringBaseKMP01(int capacity) {
        super(capacity);
    }

    public static StringBaseKMP01 ofChars(char... s) {
        return new StringBaseKMP01(true, s);
    }

    public static StringBaseKMP01 ofCapacity(int capacity) {
        return new StringBaseKMP01(capacity);
    }

    @Override
    public boolean contains(AbsString sub) {
        return containsKMP(s, sub.s) > 0;
    }

    /**
     * TODO 未完成
     * 
     * @param main
     * @param sub
     * @return
     */
    public static int containsKMP(char[] main, char[] sub) {

        int[] next = next(sub);

        int i = 1, j = 1;
        while (i <= main.length && j <= main.length) {
            if (j == 0 || main[i] == sub[j]) {
                ++i;
                ++j;
            } else {
                j = next[j];
            }
        }
        if (j > main.length) {
            return i - main.length;
        }
        return 0;
    }

    /**
     * KMP算法中, 子串的next数组,
     * 存储的是, 如果子串当前位置j与主串当前位置i不相等, 则应该取子串的指定位置next[j]和主串当前位置i继续进行匹配(完成了子串的向右滑动一段距离)
     * next[j]的值 = 是子串j 之前 的最大公共前后缀长度的下一位, 如果没有则为0
     * 公共前后缀 = 排除自身, 数组前缀和后缀值完全相等的部分
     *
     * @param sub
     * @return
     */
    public static int[] next(char[] sub) {
        // 
        int[] next = new int[sub.length + 1];
        if (sub.length <= 1) {
            // 数组值都应该为0
            return next;
        }
        int i = 1, j = 0;
        while (i < sub.length) {
            if (j == 0 || sub[i] == sub[j]) {
                next[++i] = ++j;
            } else {
                j = next[j];
            }
        }
        return next;
    }

}
