package org.song.algorithm.base._01datatype._01base._04tree.trie;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class TrieTest {

    private Random r = new Random();
    private final int valueSize = 10_0000;
    static char[] baseWords = new char[('z' - 'a') + 1];

    @Test
    public void test01() {
        TrieBase<String> trie = new TrieBase<>();
        trie.put("abc", "abc");
        trie.put("abd", "abd");
        trie.put("abdf", "abdf");
        trie.put("aa", "aa");


        trie.put("bb", "bb");
        trie.put("bbr", "bbr");

        System.out.println(trie);
    }

    @Test
    public void test_auto() {
        TrieBase<String> trie = new TrieBase<>();

        for (int i = 0; i < valueSize; i++) {
//            String key = randomHalfString(8);
//            String key = randomString(8);
            String key = randomAll(8);
            trie.put(key, key);
            boolean equals = trie.get(key).equals(key);
            if (!equals) {
                trie.get(key);
                trie.put(key, key);
                assert equals;
            }
        }

        System.out.println(trie);
    }

    
    static {
        int lower = 'z' - 'a';
        for (int i = 0; i < lower + 1; i++) {
            baseWords[i] = (char) ('a' + i);
        }
    }

    public String randomAll(int len) {
        int maxIndex = Math.max(1, r.nextInt(len));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxIndex; i++) {
            sb.append(baseWords[r.nextInt(baseWords.length)]);
        }
        return sb.toString();
    }

    public String randomString(int len) {
        int maxIndex = Math.max(1, r.nextInt(len));
        StringBuilder sb = new StringBuilder();
        int nextInt = r.nextInt(baseWords.length);
        for (int i = 0; i < maxIndex && nextInt + i < baseWords.length; i++) {
            sb.append(baseWords[nextInt + i]);
        }
        return sb.toString();
    }

    public String randomHalfString(int len) {
        int maxIndex = Math.max(1, r.nextInt(len));
        StringBuilder sb = new StringBuilder();
        int nextInt = r.nextInt(baseWords.length / 2);
        for (int i = 0; i < maxIndex && nextInt + i < baseWords.length; i++) {
            sb.append(baseWords[nextInt + i]);
        }
        return sb.toString();
    }
}
