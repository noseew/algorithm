package algorithmbase.leetcode.simple._01array;

import org.junit.Test;

public class Leetcode_14_LongestCommonPrefix {

    @Test
    public void test() {
        String prefix = longestCommonPrefix(new String[]{"flower","flow","flight"});
        System.out.println("最长前缀: " + prefix);
    }

    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length <= 0) {
            return "";
        }

        char[] lastChars = null;
        for (String str : strs) {
            char[] currentChars = str.toCharArray();
            if (currentChars == null || currentChars.length <= 0) {
                return "";
            }
            if (lastChars == null) {
                lastChars = currentChars;
            } else {
                int minLen = Math.min(lastChars.length, currentChars.length);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < minLen; i++) {
                    if (lastChars[i] == currentChars[i]) {
                        sb.append(lastChars[i]);
                        if (i == minLen - 1) {
                            lastChars = sb.toString().toCharArray();
                        }
                        continue;
                    }
                    if (i == 0) {
                        return "";
                    }
                    lastChars = sb.toString().toCharArray();
                    break;
                }
            }
        }
        return new String(lastChars).toLowerCase();
    }
}
