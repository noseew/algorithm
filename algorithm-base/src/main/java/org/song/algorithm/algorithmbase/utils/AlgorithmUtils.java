package org.song.algorithm.algorithmbase.utils;

import java.util.Arrays;
import java.util.Random;

public class AlgorithmUtils {

    public static final Random random = new Random();
    
    private AlgorithmUtils() {
        
    }

    public static void main(String[] args) {
        int gcd = 10;
        for (int i = 10; i < 30; i+=5) {
            gcd = gcdEuclidean(gcd, i);
        }
        System.out.println(gcd);
        
    }

    /**
     * 欧几里得算法求最大公约数
     */
    public static int gcdEuclidean(int a, int b) {
        if (a == 0) {
            return b;
        }
        return gcdEuclidean(b % a, a);
    }

    /**
     * 暴力算法求最大公约数
     *
     * @param p
     * @param q
     * @return
     */
    public static int gcd(int p, int q) {
        // num 用来存放最大公约数, 默认为1
        int num = 1;
        if (p <= q) {
            int k = q;
            q = p;
            p = k;
        }
        for (int i = 1; i < Math.sqrt(q); i++) {
            // i是否是q的约数, 如果是, 在判断是否是p的约数
            if (q % i == 0) {
                if (p % i == 0) {
                    num = i;
                }
                if (p % (q / i) == 0) {
                    num = q / i;
                    return num;
                }
            }
        }
        return num;
    }

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
