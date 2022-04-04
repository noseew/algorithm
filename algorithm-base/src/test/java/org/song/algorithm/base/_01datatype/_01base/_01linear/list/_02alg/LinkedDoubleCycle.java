package org.song.algorithm.base._01datatype._01base._01linear.list._02alg;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._01base._01linear.list._01model.LinkedDoubleCycleJoseph01;

/**
 * 单向链表相关算法
 */
public class LinkedDoubleCycle {

    private LinkedDoubleCycleJoseph01<Integer> initData(int count) {
        LinkedDoubleCycleJoseph01<Integer> linked = new LinkedDoubleCycleJoseph01<>();
        for (int i = 0; i < count; i++) {
            linked.rpush(i);
        }
        return linked;
    }

    /**
     * 约瑟夫问题
     * 一圈人, 从第1个开始, 每3个杀死1个, 死者剔除, 杀死后继续往下数3个, 请问谁是最后一个活着的 
     * 
     */
    @Test
    public void test_Joseph01() {
        LinkedDoubleCycleJoseph01<Integer> joseph01 = initData(10);
        int n = 3; // 每n个人杀一个

        joseph01.reset();
        while (!joseph01.isEmpty()) {
            for (int i = 1; i < n; i++) {
                joseph01.next();
            }
            System.out.println(joseph01.remove()); // 被杀的人
        }
    }

}
