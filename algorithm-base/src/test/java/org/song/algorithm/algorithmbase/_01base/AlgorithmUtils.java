package org.song.algorithm.algorithmbase._01base;

import java.util.Random;

public class AlgorithmUtils {

    public static Random random = new Random();

    public static int[] generateIntArr(int count, int min, int max) {
        int[] values = new int[count];
        for (int i = 0; i < count; i++) {
            values[i] = random.nextInt(max - min) + min;
        }
        return values;
    }

    public static int[] generateIntArr(int count) {
        return generateIntArr(count, 0, Integer.MAX_VALUE);
    }

    public static int[] generateIntArr() {
        return generateIntArr(random.nextInt(), 0, Integer.MAX_VALUE);
    }

    public static long[] generateLongArr(int count) {
        long[] values = new long[count];
        for (int i = 0; i < count; i++) {
            values[i] = random.nextLong();
        }
        return values;
    }

    public static long[] generateLongArr() {
        return generateLongArr(random.nextInt());
    }

    public static double[] generateDoubleArr(int count) {
        double[] values = new double[count];
        for (int i = 0; i < count; i++) {
            values[i] = random.nextDouble();
        }
        return values;
    }

    public static double[] generateDoubleArr() {
        return generateDoubleArr(random.nextInt());
    }

    public static boolean eq(Object[] a, Object[] b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

}
