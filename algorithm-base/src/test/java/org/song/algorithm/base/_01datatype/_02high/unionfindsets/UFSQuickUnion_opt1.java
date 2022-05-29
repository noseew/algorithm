package org.song.algorithm.base._01datatype._02high.unionfindsets;

/*
采用 quick union 思路实现

一种优化
优化思路: union不是单纯的左边根指向右边根, 而是哪个少改哪个
1. 基于size, 哪边节点数少, 改哪边(这里采用基于size实现)
    缺点: 可能存在树不平衡问题, 不平衡的树, size不能体现出高度
2. 基于rank, 哪边树的高度低, 改哪边

 */
public class UFSQuickUnion_opt1 extends UFSQuickUnion {

    /*
    size数组, 集合的元素数数组
    数组的下标表示元素节点
    数组的元素表示该元素父节点所在集合的元素个数
     */
    protected int[] sizes;
    
    protected UFSQuickUnion_opt1(int capacity) {
        super(capacity);
        sizes = new int[capacity];
        for (int i = 0; i < sizes.length; i++) {
            // 初始化每个集合元素个数都是1
            sizes[i] = 1;
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
        if (sizes[p1] < sizes[p2]) {
            parents[p1] = p2;
            // size需要增加
            sizes[p2] += sizes[p1];
        } else {
            parents[p2] = p1;
            // size需要增加
            sizes[p1] += sizes[p2];
        }
    }
}
