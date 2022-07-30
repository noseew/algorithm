package org.song.algorithm.base._02alg.thinking.greedy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * 假设有 25 分、10 分、5 分、1 分的硬币，
 * 现要找给客户 41 分的零钱，如何办到硬币个数最少？
 * 
 * 贪心算法并不能得到全局最优解, 知识做到每一步的最优解
 */
public class CoinChange {
    Integer[] integers = {25, 10, 5, 1};

    @Test
    public void main( ) {
        System.out.println(coinChange1(integers, 41));
        System.out.println(coinChange2(integers, 41));
        System.out.println(coinChange3(integers, 41));
    }

    int coinChange1(Integer[] faces, int money) {
        Arrays.sort(faces); // 排序, 默认从小到大
        int coins = 0;
        // 贪心策略, 选择面值最大的硬币, 由于顺序小 -> 大, 从后往前放
        for (int i = faces.length - 1; i >= 0; i--) {
            // 如果面值比我要的钱大, 进行下一轮
            if (money < faces[i]) continue;
            System.out.println(faces[i]);
            money -= faces[i];
            coins++;
            i = faces.length;
        }
        return coins;
    }

    int coinChange2(Integer[] faces, int money) {
        // 排序, 传入了比较器, 所以是从大到小排序
        Arrays.sort(faces, (Integer f1, Integer f2) -> f2 - f1);
        int coins = 0, i = 0;
        // 贪心策略, 选择面值最大的硬币, 由于顺序大 -> 小, 从前往后放
        while (i < faces.length) {
            if (money < faces[i]) {
                i++;
                continue;
            }
            System.out.println(faces[i]);
            money -= faces[i];
            coins++;
            i = 0; // 这步是不需要的
        }
        return coins;
    }

    int coinChange3(Integer[] faces, int money) {
        Arrays.sort(faces);
        int coins = 0, idx = faces.length - 1;
        while (idx >= 0) {
            while (money >= faces[idx]) {
                System.out.println(faces[idx]);
                money -= faces[idx];
                coins++;
            }
            idx--;
        }
        return coins;
    }
}
