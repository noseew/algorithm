package org.song.algorithm.base._02alg._01sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
排序效率对比
doc/img/排序对比.png
 */
public abstract class AbstractSort {

    private static Random random = new Random();

    public static boolean less(Comparable c1, Comparable c2) {
        return c1 != null && c2 != null ? c1.compareTo(c2) < 0 : false;
    }

    public static boolean less(int c1, int c2) {
        return c1 < c2;
    }

    public boolean greater(int c1, int c2) {
        return c1 > c2;
    }

    public boolean greater(Comparable c1, Comparable c2) {
        return c1.compareTo(c2) > 0;
    }

    public static boolean lessEq(Comparable c1, Comparable c2) {
        return c1.compareTo(c2) <= 0;
    }

    public static boolean lessEq(int c1, int c2) {
        return c1 <= c2;
    }

    public static boolean isSorted(Comparable[] cs) {
        for (int i = 1; i < cs.length; i++) {
            if (less(cs[i], cs[i - 1])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSorted(int[] cs) {
        for (int i = 1; i < cs.length; i++) {
            if (less(cs[i], cs[i - 1])) {
                return false;
            }
        }
        return true;
    }

    public static void exchange(Comparable[] cs, int i1, int i2) {
        Comparable temp = cs[i1];
        cs[i1] = cs[i2];
        cs[i2] = temp;
    }
    
    public static void toString(Comparable[] cs) {
        for (Comparable c : cs) {
            System.out.print(c + ", ");
        }
    }
    
    public static void toString(int[] cs) {
        System.out.println(Arrays.toString(cs));
    }

    public abstract void sort(Comparable[] cs);
    
    public static Comparable[] build(int start, int end, int num) {
        Integer[] ints = new Integer[num];
        for (int i = 0; i < num; i++) {
            ints[i] = random.nextInt(end - start) + start;
        }
        return ints;
    }
    
    public static int[] buildInt(int start, int end, int num) {
        int[] ints = new int[num];
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
    
    public static List<Integer> buildList(int num) {
        List<Integer> list = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            list.add(random.nextInt(num * 10));
        }
        return list;
    }
    
    public static int[] buildInt(int num) {
        int[] ints = new int[num];
        for (int i = 0; i < num; i++) {
            ints[i] = random.nextInt(num * 10);
        }
        return ints;
    }
}
