package org.song.algorithm.algorithmbase._02case.demo;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class DemoTest_01 {

    /**
     * 超级水王问题
     * 给定一个数组, 如何快速判断数组中某个数超出数组长度的一半, 如果没有这样的数, 则返回-1
     */
    @Test
    public void test_01() {
        int[] numbers = build();
        demo_01(numbers);
        demo_02(numbers);
    }
    
    private int [] build(){
        int count = 20;
        int[] numbers = new int[count];
        for (int i = 0; i < count; i++) {
            int val = ThreadLocalRandom.current().nextInt(2) + 1;
            numbers[i] = val;
        }
        return numbers;
    }

    /**
     * 方法1, 采用map计数法
     * 时间复杂度 O(n)
     * 额外空间复杂度 O(n)
     */
    public void demo_01(int[] numbers) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int number : numbers) {
            Integer val = map.getOrDefault(number, 0);
            map.put(number, val + 1);
        }
        int maxV = 0;
        int maxK = -1;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > maxV) {
                maxV =entry.getValue();
                maxK = entry.getKey();
            }
        }

        System.out.println(maxV > (numbers.length >> 1) ? maxK : -1);
    }

    /**
     * 方法2
     * 1. 一次删除数组中两个不相同的数
     * 2. 剩下的那个数就有可能水王数
     * 3. 将剩下的那个数出现的次数在统计一遍, 如果真的超出一半, 则剩下的那个数就是水王数, 如果没有剩下的数, 则没有水王数
     * 时间复杂度 O(n)
     * 额外空间复杂度 O(1)
     *
     * <p>
     * 如何一次删除连个不同的数呢?
     * 构建两个变量
     * 候选者 和 血量
     * 候选者: 指的是当前未被删除的数字
     * hp: 当前未被删除的数字剩余的血量(出现的次数-消耗的次数), 每次
     * 
     * @param numbers
     */
    public void demo_02(int[] numbers) {
        // 血量, 当血量为0的时候, 说明没有候选者
        int hp = 0;
        // 候选数字
        int candidate = -1;

        for (int number : numbers) {
            if (hp == 0) {
                // 血量=0时, 后选择无意义
                candidate = number;
                hp++;
            } else if (candidate != number) {
                /*
                当两个数字不相等时, 候选者归零, 血量-1, 也就是候选者和这个不相等的数同时删除
                    此时 候选者被其他数逐一消耗后仍然多出 hp 个, 
                    candidate != number 这里候选者仍要被消耗一个血量, 所以 hp--
                如果 hp > 0
                    此时 候选者被其他数逐一消耗后仍然多出 hp 个, 候选者仍然是之前的数, 所以不用重置候选者
                如果 hp = 0
                    此时 候选者和其他数一样多, 双方都会消耗完了, 由于前一个判断 hp == 0 会重置候选者, 所以这里不用重置候选者
                 */
                hp--;
            } else {
                // 其他情况, 候选者数量增加
                hp++;
            }
        }

        int count = 0;
        for (int number : numbers) {
            if (candidate == number) {
                count++;
            }
        }
        System.out.println(count > (numbers.length >> 1) ? candidate : -1);
    }
}
