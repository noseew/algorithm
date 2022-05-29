package org.song.algorithm.base._01datatype._02high.unionfindsets;

/*
采用 quick union 思路实现

一种优化
优化思路: union不是单纯的左边根指向右边根, 而是哪个少改哪个
1. 基于size, 哪边节点数少, 改哪边
    缺点: 可能存在树不平衡问题, 不平衡的树, size不能体现出高度
2. 基于rank, 哪边树的高度低, 改哪边(这里采用基于rank实现)
    缺点: 树的高度会越来越高

 */
public class UFSQuickUnion_opt2 extends UFSQuickUnion {

    /*
    ranks数组, 集合的高度数组
    数组的下标表示元素节点
    数组的元素表示该元素父节点所在集合的高度
     */
    protected int[] ranks;
    
    protected UFSQuickUnion_opt2(int capacity) {
        super(capacity);
        ranks = new int[capacity];
        for (int i = 0; i < ranks.length; i++) {
            // 初始化每个集合高度都是1
            ranks[i] = 1;
        }
    }


    /*
    优化
    基于size, 哪边节点数少, 改哪边
     */
    @Override
    public void union(int n1, int n2) {
        validRange(n1);
        validRange(n2);
        int p1 = findRoot(n1);
        int p2 = findRoot(n2);
        if (p1 == p2) {
            // 属于同一个集合, 不用处理
            return;
        }

        //  哪边节点数少, 改哪边
        if (ranks[p1] < ranks[p2]) {
            parents[p1] = p2;
            // 注意: rank不需要增加, 因为: 高度低的加到高度高的树上, 总高度是不变的
        } else if(ranks[p2] < ranks[p1]) {
            parents[p2] = p1;
            // 注意: rank不需要增加, 因为: 高度低的加到高度高的树上, 总高度是不变的
        }else{
            parents[p1] = p2;
            // 注意: 只有此时rank高度才需要增加, 两个高度相等的数相加, 被增加的那个高度+1
            ranks[p2]++;
        }
    }
}
