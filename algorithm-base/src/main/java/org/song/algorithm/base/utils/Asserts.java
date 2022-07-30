package org.song.algorithm.base.utils;

public class Asserts {

    public static void isTrue(boolean val) {
        if (!val) {
            throw new IllegalArgumentException("false");
        }
    }
}
