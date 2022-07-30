package org.song.algorithm.base._02alg.classical.matching;

import org.song.algorithm.base.utils.Asserts;

public class Main {
    public static void main(String[] args) {
        Asserts.isTrue(BruteForce01.indexOf("Hello World", "H") == 0);
        Asserts.isTrue(BruteForce01.indexOf("Hello World", "d") == 10);
        Asserts.isTrue(BruteForce01.indexOf("Hello World", "or") == 7);
        Asserts.isTrue(BruteForce01.indexOf("Hello World", "abc") == -1);
        Asserts.isTrue(BruteForce01.indexOf2("Hello World", "H") == 0);
        Asserts.isTrue(BruteForce01.indexOf2("Hello World", "d") == 10);
        Asserts.isTrue(BruteForce01.indexOf2("Hello World", "or") == 7);
        Asserts.isTrue(BruteForce01.indexOf2("Hello World", "abc") == -1);
        Asserts.isTrue(BruteForce02.indexOf("Hello World", "H") == 0);
        Asserts.isTrue(BruteForce02.indexOf("Hello World", "d") == 10);
        Asserts.isTrue(BruteForce02.indexOf("Hello World", "or") == 7);
        Asserts.isTrue(BruteForce02.indexOf("Hello World", "abc") == -1);
        Asserts.isTrue(KMP.indexOf("Hello World", "H") == 0);
        Asserts.isTrue(KMP.indexOf("Hello World", "d") == 10);
        Asserts.isTrue(KMP.indexOf("Hello World", "or") == 7);
        Asserts.isTrue(KMP.indexOf("Hello World", "abc") == -1);
    }
}
