package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import java.util.Random;

/*
二叉树
 */
public class Tree01_B_base<V> extends _01TreeBase<V> {

    private int size;

    public TreeNode<V> root;

    /*
    二叉树由于没有 比较/排序, 所以插入并没有具体的规则要插到哪个节点下
    这里为了体现二叉树共有特性, 采用随机的方式插入 左子节点或者右子节点
    随机的深度 默认8
     */
    private Random r = new Random();
    private int maxDeep = 8;

    @Override
    public boolean push(V v) {
        if (root == null) {
            root = new TreeNode<>(null, null, v);
            size++;
            return true;
        }

        boolean added = false;
        
        TreeNode<V> parent = root;
        // 限制最大深度
        for (int i = 0; i < maxDeep; i++) {
            // 随机 左子节点或者右子节点
            boolean left = r.nextInt(100) % 2 == 0;
            if (left) {
                if (parent.left != null) {
                    parent = parent.left;
                } else {
                    // 插入成功则终止
                    parent.left = new TreeNode<>(null, null, v);
                    added = true;
                    break;
                }
            } else {
                if (parent.right != null) {
                    parent = parent.right;
                } else {
                    // 插入成功则终止
                    parent.right = new TreeNode<>(null, null, v);
                    added = true;
                    break;
                }
            }
        }
        if (added) {
            size++;
            return true;
        }
        return false;
    }

    @Override
    public V get(V v) {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

}
