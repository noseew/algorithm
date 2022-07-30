package org.song.algorithm.base._02alg.thinking.recursion;

import org.junit.jupiter.api.Test;

/*
汉诺塔
- 每次只能移动一个盘子
- 大盘子只能放在小盘子下面

实现挪动的过程, 将过程打印出来
 */
public class _03Hanoi {
    
    
    @Test
    public void test01() {
        hanoi(4, "A", "B", "C");
        
    }
    /*
    思路
    柱子: A,B,C, 初始盘子在A上, 目标盘子在C上
    当n = 1时, 直接将盘子从A移动到C
    当n > 1时, 
        1. 将n-1个盘子从A移动到B,
        2. 将n的盘子从A移动到C
        3. 将n-1个盘子从B移动到C
    如此反复
     */

    /**
     * 最终将 P1 移动到 P3, P2位临时柱子
     * 复杂度 O(2^n)
     * 
     * @param n
     * @param p1
     * @param p2
     * @param p3
     */
    public void hanoi(int n, String p1, String p2, String p3) {
        if (n <= 1) {
            move(n, p1, p3);
            return;
        }
//        1. 将n-1个盘子从A移动到B,
        hanoi(n - 1, p1, p3, p2);
//        2. 将n的盘子从A移动到C
        move(n, p1, p3);
//        3. 将n-1个盘子从B移动到C
        hanoi(n - 1, p2, p1, p3);
    }

    public void move(int num, String from, String to) {
        System.out.println("将编号" + num + "盘子从" + from + "移动到" + to);
    }
}
