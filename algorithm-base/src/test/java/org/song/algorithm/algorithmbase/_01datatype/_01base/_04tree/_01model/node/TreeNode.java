package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.printer.BTreeUtils;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode<V> {

    public TreeNode<V> left;
    public TreeNode<V> right;
    /**
     * parent 指针, 
     * 实现的时候有两种方式, 一种有parent指针, 一种没有parent指针
     * 这里冗余加上
     */
    public TreeNode<V> parent;
    public V val;
    /*
     树的高度: 从最低的叶子节点开始, 由0开始, 如果左右子树长度不一样, 则以最长的为准
     区别于树的深度: 根节点的深度为0, 每个叶子节点的深度不一样
     有的定义是从1开始
     */
    public int height;
    /**
     * 红黑树颜色
     * true=红色
     * false=黑色
     */
    public boolean red;

    public TreeNode(V val) {
        this.val = val;
    }

    public TreeNode(V val, boolean red) {
        this.val = val;
        this.red = red;
    }

    public TreeNode(TreeNode<V> parent, V val, boolean red) {
        this.parent = parent;
        this.val = val;
        this.red = red;
    }

    @Override
    public String toString() {
        return BTreeUtils.simplePrint(this);
    }
    
    public String toBaseString() {
        return String.valueOf(val);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeNode)) return false;
        TreeNode<?> treeNode = (TreeNode<?>) o;
        return Objects.equals(val, treeNode.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }
}
