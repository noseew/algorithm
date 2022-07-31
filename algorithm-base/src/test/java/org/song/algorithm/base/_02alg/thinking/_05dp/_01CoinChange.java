package org.song.algorithm.base._02alg.thinking._05dp;

import org.junit.jupiter.api.Test;

/**
 * 采用动态规划计算换零钱问题
 */
public class _01CoinChange {

    /**
     * 零钱 1, 5, 20, 25
     * 找零 41=20+20+1, 3个
     * 找零 19=5+5+5+1+1+1+1, 7个
     */
    @Test
    public void test() {
        assert coins1_1(41) == 3;
        assert coins1_1(19) == 7;
        assert coins1_2(41) == 3;
        assert coins1_2(19) == 7;
    }

    /**
     * 暴力递归(自顶向下的调用, 出现了重叠子问题)
     * 面值总共有4种, 也就是将4种都试一下, 将每一种的结果拿来做对比, 选出最小的那个
     */
    int coins1_1(int n) {
        /*
        递归基
        换零钱, 如果剩余的金额正好是面值, 则返回1, 表示最少用1枚硬币就能够兑换
        这也是该问题的最小子问题
         */
        if (n == 1 || n == 5 || n == 20 || n == 25) return 1;
        int min = Integer.MAX_VALUE;
        // 找出四种取法的最小值, 一个大问题的4个子问题
        if (n > 1) min = Math.min(min, coins1_1(n - 1));
        if (n > 5) min = Math.min(min, coins1_1(n - 5));
        if (n > 20) min = Math.min(min, coins1_1(n - 20));
        if (n > 25) min = Math.min(min, coins1_1(n - 25));
        // 选出最小的那个之后, 纸币数量+1
        return min + 1;
    }

    /**
     * 暴力递归(自顶向下的调用, 出现了重叠子问题)
     * coins1_1 的优化写法
     */
    int coins1_2(int n) {
        /*
        递归基
        只要n<1, 统一返回无效值, 最大值, 因为后面是按照最小值选取的, 所以最大值是无效值, 会被忽略
         */
        if (n < 1) return Integer.MAX_VALUE;
        if (n == 1 || n == 5 || n == 20 || n == 25) return 1;
        // 找出四种取法的最小值, 这里不考虑减完了之后变成负值的情况, 因为负值到递归基后统一变成无效值
        int min1 = Math.min(coins1_2(n - 25), coins1_2(n - 20));
        int min2 = Math.min(coins1_2(n - 5), coins1_2(n - 1));
        return Math.min(min1, min2) + 1;
    }

    /**
     * 零钱 1, 5, 20, 25
     * 找零 41=20+20+1, 3个
     * 找零 19=5+5+5+1+1+1+1, 7个
     */
    @Test
    public void test2() {
        assert coins2_1(41) == 3;
        assert coins2_1(19) == 7;
        assert coins2_2(41) == 3;
        assert coins2_2(19) == 7;
    }

    /**
     * 记忆化搜索(自顶向下的调用)
     * 由于暴力递归有大量的重复计算, 就像斐波那契数的递归基算一样, 这里采用数组记录重复计算的结果
     */
    int coins2_1(int n) {
        if (n < 1) return -1; // 处理非法数据
        // 记录重复计算的结果, 数组, 数组下标i就是面值dp[i]就是该面值兑换所需要的最小数量
        int[] dp = new int[n + 1];
        // 初始化面值兑换数, 等值的兑换数就是1
        if (n >= 25) dp[25] = 1;
        if (n >= 20) dp[20] = 1;
        if (n >= 5) dp[5] = 1;
        if (n >= 1) dp[1] = 1;
        return coins2_1(n, dp);
    }

    int coins2_1(int n, int[] dp) {
        // 递归基
        if (n < 1) return Integer.MAX_VALUE;
        if (dp[n] == 0) { // 记忆化搜索, dp[n] == 0 表示以前没有算过, 那便初始化一下
            // 找出四种取法的最小值, 一个大问题的4个子问题
            int min = Integer.MAX_VALUE;
            if (n > 1) min = Math.min(min, coins2_1(n - 1));
            if (n > 5) min = Math.min(min, coins2_1(n - 5));
            if (n > 20) min = Math.min(min, coins2_1(n - 20));
            if (n > 25) min = Math.min(min, coins2_1(n - 25));
            // 选出最小的那个之后, 纸币数量+1
            dp[n] = min + 1;
        }
        return dp[n];
    }

    /**
     * 记忆化搜索(自顶向下的调用)
     * 由于暴力递归有大量的重复计算, 就像斐波那契数的递归基算一样, 这里采用数组记录重复计算的结果
     * <p>
     * 优化coins2_1上面的if, 放到循环里
     */
    int coins2_2(int n) {
        if (n < 1) return -1; // 处理非法数据
        // 记录重复计算的结果, 数组
        int[] dp = new int[n + 1];
        // 将金额放到数组里, 
        int[] faces = new int[]{1, 5, 20, 25}; // 给定的面值数组, 用来决定初始化哪些面值
        for (int face : faces) {
            // 如果我要凑的钱是20元, 那么我肯定用不到25元面值
            if (face > n) break; // 用不到的面值不用初始化
            dp[face] = 1; // 初始化可能用到的面值
        }
        return coins2_2(n, dp);
    }

    int coins2_2(int n, int[] dp) {
        // 递归基
        if (n < 1) return Integer.MAX_VALUE;
        if (dp[n] == 0) { // 记忆化搜索, dp[n] == 0 表示以前没有算过, 那便初始化一下
            int min1 = Math.min(coins2_2(n - 25, dp), coins2_2(n - 20, dp));
            int min2 = Math.min(coins2_2(n - 5, dp), coins2_2(n - 1, dp));
            dp[n] = Math.min(min1, min2) + 1;
        }
        return dp[n];
    }

    @Test
    public void test3() {
        assert coins3_1(41) == 3;
        assert coins3_1(19) == 7;
    }

    /**
     * 递推(自底向上)
     * 递推, 也就是使用非递归完成上述问题, 采用遍历/枚举/迭代的方式
     */
    int coins3_1(int n) {
        if (n < 1) return -1; // 处理非法数据
        int[] dp = new int[n + 1];
        /*
        遍历: 从1到n
        也就是计算出从1到n每一个数的兑换硬币的最小枚数
        1. 问题分解
            如果要求出凑够 n元 的最小硬币数, 必须要求出下面四种子情况的最小硬币数
                n-1 元
                n-4 元
                n-20 元
                n-25 元
        2. 自底向上
            先求出 n=1 的情况
                1-1 元
                1-4 元
                1-20 元
                1-25 元
            再求出 n=2 的情况
                2-1 元
                2-4 元
                2-20 元
                2-25 元
            以此类推, 直到求出 n=n 的情况
                n-1 元
                n-4 元
                n-20 元
                n-25 元
        3. 记忆化搜索复用
            关键: 在此过程中, 由于需要求出 n-1 的情况, 而 n-1 已经在前面求过了, 所以可以直接拿来用
            这就是递推
         */
        for (int i = 1; i <= n; i++) { // 自底向上的递推
            int min = Integer.MAX_VALUE;
            if (i >= 1) min = Math.min(min, dp[i - 1]);
            if (i >= 5) min = Math.min(min, dp[i - 5]);
            if (i >= 20) min = Math.min(min, dp[i - 20]);
            if (i >= 25) min = Math.min(min, dp[i - 25]);
            /*
            关键: 由于i是从1到n的, 所以i之前的数, 一定已经求过了
            示例: 
                第1次, i=1, dp[1] = dp[1 - 1] = 0, 初始为0
                                = 新值 0+1 = 硬币数1
                第2次, i=2, dp[2] = dp[2 - 1]
                                = dp[1] = 1, 前一步已经算过了
                                = 新值 1+1 = 硬币数2
                ...
                第6次, i=6, dp[6] = dp[6 - 5]
                                = dp[1] = 1, 前一步已经算过了
                                = 新值 1+1 = 硬币数2
                第7次, i=7, dp[7] = dp[7 - 5]
                                = dp[2] = 2, 前一步已经算过了
                                = 新值 2+1 = 硬币数3
             */
            dp[i] = min + 1;
        }
        return dp[n];
    }

//    /**
//     * 递推(自底向上)
//     * 特殊写法, 因为面值有1, 所以1可以忽略
//     */
//    int coins3_2(int n) {
//        if (n < 1) return -1; // 处理非法数据
//        int[] dp = new int[n + 1];
//        for (int i = 1; i <= n; i++) { // 自底向上的递推
//            int min = dp[i - 1];
//            if (i >= 5) min = Math.min(min, dp[i - 5]);
//            if (i >= 20) min = Math.min(min, dp[i - 20]);
//            if (i >= 25) min = Math.min(min, dp[i - 25]);
//            dp[i] = min + 1;
//        }
//        return dp[n];
//    }

    /**
     * 打印动态规划过程
     * 打印出凑够硬币到底是哪几枚
     */
    @Test
    public void test4() {
        assert coins4(41) == 3;
        assert coins4(19) == 7;
    }

    /**
     * 打印出凑够硬币到底是哪几枚
     */
    int coins4(int n) {
        if (n < 1) return -1; // 处理非法数据
        int[] dp = new int[n + 1];
        // faces[i] 是凑够i分时最后选择的那枚硬币的面值
        int[] faces = new int[dp.length]; // 存放硬币面值(为了输出)
        for (int i = 1; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            if (i >= 1 && dp[i - 1] < min) {
                min = dp[i - 1];
                faces[i] = 1;
            }
            // 上面一步其实必然执行, 可以直接写成下面这样
            // int min = dp[i - 1];
            faces[i] = 1;
            if (i >= 5 && dp[i - 5] < min) {
                min = dp[i - 5];
                faces[i] = 5;
            }
            if (i >= 20 && dp[i - 20] < min) {
                min = dp[i - 20];
                faces[i] = 20;
            }
            if (i >= 25 && dp[i - 25] < min) {
                min = dp[i - 25];
                faces[i] = 25;
            }
            dp[i] = min + 1;
            print(faces, i); // 打印凑够面值 1 ~ n 的方案
        }
        print(faces, n); // 打印凑够面值 n 的方案
        return dp[n];
    }


    @Test
    public void test5() {
        assert coins5_1( 41, new int[]{1, 5, 20, 25}) == 3;
        assert coins5_1(19, new int[]{1, 5, 20, 25}) == 7;
    }

    /**
     * 将面值当成数组参数传进去
     */
    int coins5_1(int n, int[] faces) {
        if (n < 1 || faces == null || faces.length == 0) return -1;
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            for (int face : faces) {
                // 假如给我的面值是20, 要凑的是15, 则跳过此轮循环
                if (face > i) continue; // 如果给我的面值比我要凑的面值还大, 跳过此轮循环
                min = Math.min(dp[i - face], min);
            }
            dp[i] = min + 1;
        }
        return dp[n];
    }

    int coins5_2(int n, int[] faces) {
        if (n < 1 || faces == null || faces.length == 0) return -1;
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            for (int face : faces) {
                // 假如给我的面值是20, 要凑的是15, 则跳过此轮循环
                if (face > i) continue; // 如果给我的面值比我要凑的面值还大, 跳过此轮循环
                int v = dp[i - face];
                if (v < 0 || v >= min) continue;
                min = v;
            }
            // 说明上面的循环中每次都是continue, 要凑的面值比给定的所有面值小
            if (min == Integer.MAX_VALUE) {
                dp[i] = -1;
            } else {
                dp[i] = min + 1;
            }
        }
        return dp[n];
    }

    // 打印凑够面值 n
    // 的方案

    void print(int[] faces, int n) {
        System.out.print("[" + n + "] = ");
        while (n > 0) {
            System.out.print(faces[n] + " ");
            n -= faces[n];
        }
        System.out.println();
    }

}
