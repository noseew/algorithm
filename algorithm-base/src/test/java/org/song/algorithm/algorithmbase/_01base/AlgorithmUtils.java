package org.song.algorithm.algorithmbase._01base;

import java.util.Arrays;
import java.util.Random;

public class AlgorithmUtils {
    
    private AlgorithmUtils() {
        
    }

    public static void main(String[] args) {
//        System.out.println(Arrays.toString(generateIntArrAsc(20, 1, 100)));
//        System.out.println(Arrays.toString(generateIntArrDesc(20, 1, 100)));
//        System.out.println(Arrays.toString(generateIntArr(20, 1, 100)));
        System.out.println(Arrays.toString(generateDoubleArr(20, 5)));
    }

    public static Random random = new Random();

    /**
     * 指定范围指定数量的升序序列
     * 
     * @param count
     * @param min
     * @param max
     * @return
     */
    public static int[] generateIntArrAsc(int count, int min, int max) {
        int[] values = new int[count];
        int step = (max - min) / count;
        for (int i = 0; i < count; i++) {
            int r = random.nextInt(step) + (step == 0 ? 1 : 0);
            min += r;
            values[i] = min;
        }
        return values;
    }

    public static int[] generateIntArrDesc(int count, int min, int max) {
        int[] values = new int[count];
        int step = (max - min) / count;
        for (int i = 0; i < count; i++) {
            int r = random.nextInt(step) + (step == 0 ? 1 : 0);
            max -= r;
            values[i] = max;
        }
        return values;
    }

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

    public static double[] generateDoubleArr(int count, int maxDecimal) {
        double[] values = new double[count];
        for (int i = 0; i < count; i++) {
            // 小数随机, 整数位也随机
            values[i] = random.nextDouble() * Math.pow(10, random.nextInt(maxDecimal));
        }
        return values;
    }

    public static double[] generateDoubleArr() {
        return generateDoubleArr(random.nextInt(), 10);
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
