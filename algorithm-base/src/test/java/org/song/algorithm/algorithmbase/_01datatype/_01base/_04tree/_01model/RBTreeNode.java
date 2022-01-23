package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RBTreeNode<V> extends TreeNode<V> {
    
    public boolean color;
    public int n;

    public RBTreeNode(TreeNode<V> left, TreeNode<V> right, V val, boolean color, int n) {
        super(left, right, val);
        this.color = color;
        this.n = n;
    }

}
