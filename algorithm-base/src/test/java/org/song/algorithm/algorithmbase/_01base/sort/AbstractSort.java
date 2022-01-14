package org.song.algorithm.algorithmbase._01base.sort;

import java.util.Random;

public abstract class AbstractSort {

    private static Random random = new Random();

    public boolean less(Comparable c1, Comparable c2) {
        return c1.compareTo(c2) < 0;
    }

    public boolean isSorted(Comparable[] cs) {
        for (int i = 1; i < cs.length; i++) {
            if (less(cs[i], cs[i - 1])) {
                return false;
            }
        }
        return true;
    }

    public void exch(Comparable[] cs, int i1, int i2) {
        Comparable temp = cs[i1];
        cs[i1] = cs[i2];
        cs[i2] = temp;
    }
    
    public void toString(Comparable[] cs) {
        for (Comparable c : cs) {
            System.out.print(c + ", ");
        }
    }

    public abstract void sort(Comparable[] cs);
    
    public static Comparable[] build(int start, int end, int num) {
        Integer[] ints = new Integer[num];
        for (int i = 0; i < num; i++) {
            ints[i] = random.nextInt(end - start) + start;
        }
        return ints;
    }
    
    public static Comparable[] build(int num) {
        Integer[] ints = new Integer[num];
        for (int i = 0; i < num; i++) {
            ints[i] = random.nextInt(num * 10);
        }
        return ints;
    }
}
