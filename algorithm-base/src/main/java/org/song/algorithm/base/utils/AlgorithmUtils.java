package org.song.algorithm.base.utils;

import java.util.Random;

public class AlgorithmUtils {

    public static final Random random = new Random();
    
    private AlgorithmUtils() {
        
    }

    public static void main(String[] args) {
        int gcd = 10;
        for (int i = 10; i < 30; i+=5) {
            gcd = gcd(gcd, i);
        }
        System.out.println(gcd);
        
    }

    /**
     * 最小公倍数
     * 
     */
    public static int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    /**
     * 欧几里得算法求最大公约数
     */
    public static int gcd(int a, int b) {
        if (a == 0) {
            return b;
        }
        return gcd(b % a, a);
    }

    /**
     * 返回合理范围的默认值
     * 
     * @param val 入参值
     * @param min 最小不小于
     * @param max 最大不大于
     * @return
     */
    public static int getValidRange(int val, int min, int max) {
        return Math.min(Math.max(val, min), val);
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
