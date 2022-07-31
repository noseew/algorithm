package org.song.algorithm.base._02alg.thinking._03greedy;

import java.util.*;

/**
 * 0-1 背包问题
 * 有n个物品, 总重量不超过W, 每个物品重量w_i
 * 在保证不超过W的情况下, 讲哪些物品装进背包, 使得总价值最大?
 * 每个物品只有1个, 要么选择1个要么选择0个
 * <p>
 * 贪心算法实现思路, 不是全局最优解
 * 1. 价值主导, 优先选择价值最高
 * 2. 重量主导, 优先选择重量最轻
 * 3. 性价比主导, 性价比=价值/重量, 优先选择性价比
 */
public class _03Knapsack {
    public void main(String[] args) {
        select("价值主导", (Article a1, Article a2) -> {
            // 价值大的优先
            return a2.value - a1.value;
        });
        select("重量主导", Comparator.comparingInt((Article a) -> a.weight));
        select("价值密度主导", (Article a1, Article a2) -> {
            // 价值密度大的优先
            return Double.compare(a2.valueDensity, a1.valueDensity);
        });
    }

    /**
     * 以一个属性为主导实现贪心策略
     *
     * @param title 显示标题
     * @param cmp   比较器决定主导属性, [价值、重量、价值密度]
     */
    void select(String title, Comparator<Article> cmp) {
        // 模拟题意的物品
        Article[] data = new Article[]{
                new Article(35, 10), new Article(30, 40),
                new Article(60, 30), new Article(50, 50),
                new Article(40, 35), new Article(10, 40),
                new Article(25, 30)
        };
        // 通过比较器, 按某个主导属性进行排序
        Arrays.sort(data, cmp);
        // 以某个属性为主导, 实现贪心策略
        int capacity = 150, // 总重量限制
                weight = 0, // 当前权重
                value = 0; // 已累加总价值
        
        
        List<Article> selectedArticles = new ArrayList<>(); // 选择的物品集合
        for (int i = 0; i < data.length && weight < capacity; i++) {
            int newWeight = weight + data[i].weight;
            if (newWeight <= capacity) {
                weight = newWeight;
                value += data[i].value;
                selectedArticles.add(data[i]);
            }
        }
        
        // 打印选择结果
        System.out.println("-----------------------------");
        System.out.println("【" + title + "】");
        System.out.println("总价值: " + value);
        for (Article article : selectedArticles) {
            System.out.println(article);
        }
    }

    static class Article {
        int weight; // 重量
        int value;  // 价值
        double valueDensity; // 价值密度

        public Article(int weight, int value) {
            this.weight = weight;
            this.value = value;
            valueDensity = value * 1.0 / weight;
        }

        @Override
        public String toString() {
            return "Article [weight=" + weight + ", value=" + value + ", ValueDensity=" + valueDensity + "]";
        }
    }

}
