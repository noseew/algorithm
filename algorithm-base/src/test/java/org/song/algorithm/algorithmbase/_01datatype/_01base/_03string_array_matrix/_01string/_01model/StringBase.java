package org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._01string._01model;

public class StringBase extends AbsString {

    public StringBase(boolean chars, char... s) {
        super(chars, s);
    }

    public StringBase(int capacity) {
        super(capacity);
    }

    public static StringBase ofChars(char... s) {
        return new StringBase(true, s);
    }

    public static StringBase ofCapacity(int capacity) {
        return new StringBase(capacity);
    }

    @Override
    public AbsString subString(int start, int end) {
        checkIndex(start, end);
        // 子串大小
        StringBase sub = ofCapacity(end - start);
        // 直接复制子串
        for (int i = start, j = 0; i < end; ) {
            sub.s[j++] = s[i++];
        }
        return sub;
    }

    @Override
    public boolean eq(AbsString str) {
        // 逐个对比
        if (str == null || length() != str.length()) {
            return false;
        }
        for (int i = 0; i < s.length; i++) {
            if (s[i] != str.s[i]) return false;
        }
        return true;
    }

    @Override
    public AbsString append(AbsString str) {
        StringBase sub = ofCapacity(length() + str.length());
        // 先复制第一个
        for (int i = 0; i < length(); i++) {
            sub.s[i] = s[i];
        }
        // 接着复制第二个
        for (int i = 0; i < str.length(); i++) {
            sub.s[i + length()] = str.s[i];
        }
        return sub;
    }

    @Override
    public AbsString insert(int index, AbsString str) {
        checkIndex(index);
        StringBase sub = ofCapacity(length() + str.length());
        // 先复制 0~index
        for (int i = 0; i < index; i++) {
            sub.s[i] = s[i];
        }
        // 再复制 str
        for (int i = 0; i < str.length(); i++) {
            sub.s[index + i] = str.s[i];
        }
        // 最后复制 index~最后
        for (int i = 0; i < sub.length() - index - str.length(); i++) {
            sub.s[index + str.length() + i] = s[i + index];
        }
        return sub;
    }

    @Override
    public AbsString remove(int start, int end) {
        checkIndex(start, end);
        // 先复制 0~start
        StringBase sub = ofCapacity(length() - (end - start));
        for (int i = 0; i < start; i++) {
            sub.s[i] = s[i];
        }
        // 再复制 end~最后
        for (int i = end; i < length(); i++) {
            sub.s[start++] = s[i];
        }
        return sub;
    }

    @Override
    public int charAt(char c) {
        for (int i = 0; i < length(); i++) {
            if (s[i] == c) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int length() {
        return s.length;
    }

    @Override
    public char indexOf(int index) {
        checkIndex(index);
        return s[index];
    }

    @Override
    public boolean contains(AbsString sub) {
        if (length() < sub.length()) {
            return false;
        }
        // BF 暴力解法
        int eqCount = 0; // 匹配成功次数(位置)
        for (int start = 0; // 当前匹配到的位置, 成功的开始位置
             start < length() - (sub.length() - 1); start++) {
            for (int i = start, j = 0; i < length() && j < sub.length(); ) {
                if (s[i] == sub.s[j]) {
                    i++;
                    j++;
                    if (++eqCount == sub.length()) {
                        return true; // 完全匹配
                    }
                } else {
                    eqCount = 0; // 归零
                    break; // start 从下一个开始继续对比
                }
            }
        }
        return false;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= length()) {
            throw new RuntimeException("数组下标越界");
        }
    }

    private void checkIndex(int start, int end) {
        if (start > end) {
            throw new RuntimeException("起止下标越界");
        }
        checkIndex(start);
        checkIndex(end);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (char c : s) sb.append(c);
        return sb.toString();
    }
}
