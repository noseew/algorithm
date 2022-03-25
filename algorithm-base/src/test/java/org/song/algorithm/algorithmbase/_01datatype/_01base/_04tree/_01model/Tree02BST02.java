package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
一棵二叉查找树(BST)是一棵二叉树, 

node 节点使用parent指针
 */
public class Tree02BST02<V extends Comparable<V>> extends Tree02BST01<V> {

    public Tree02BST02(Comparator<V> comparator) {
        super(comparator);
    }

    @Override
    public V remove(V v) {
        TreeNode<V> node = search_traverse(root, v);
        remove(node);
        return null;
    }

    /***************************************** 通用方法 可重写 *****************************************************/

    /**
     * 采用循环的方式, 插入节点
     *
     * @param parent 以 parent 为root
     * @param v
     * @return 返回该节点
     */
    protected TreeNode<V> insert_traverse(TreeNode<V> parent, V v) {
        if (parent == null) {
            return newNode(v);
        }

        // 获取v的父节点, 如果v存在就是v的父节点, 如果v不存在就是v应该插入的父节点
        TreeNode<V> xp = getParentNode(parent, v);
        if (xp == null) {
            // v == parent
            return parent;
        }

        if ((xp.left != null && eq(xp.left.val, v))
                || (xp.right != null && eq(xp.right.val, v))) {
            // 等值不处理
            return parent;
        }

        TreeNode<V> x = newNode(v);
        x.parent = xp; // 记录parent指针
        if (less(v, xp.val)) {
            xp.left = x;
        } else if (greater(v, xp.val)) {
            xp.right = x;
        }
        return x;
    }

    /**
     * 删除提供的节点
     * 
     * @param x
     */
    protected void remove(TreeNode<V> x) {
        // 待删除节点
        if (x == null) return;
        
        /*
        删除的节点永远是叶子结点: 如何理解这句话呢?
        1. 如果删除的节点x度为2, 则取其直接 前驱/后继 节点来替代它, 也就等价于删除其直接 前驱/后继 节点, 
            此时x=其直接 前驱/后继 节点, 此时新的x是度为1或者度为0的节点, 
        2. 如果x节点的度为1, 则删除x需要将其子节点直接替代x即可, 等价于删除其x的子节点
        3. 如果x节点的度为0, 则x直接删除即可, x子节点的替代者为空
        
        这中间"替代者"指的是最终的那个替代者, 要么是某个叶子结点, 要么是空节点
         */

        // 删除 度为2的节点
        if (x.right != null && x.left != null) {
            // 度为2的节点, 要找到其后继节点代替它
            TreeNode<V> successor = getMinNode(x.right);
            x.val = successor.val; // 采用值替换的方式删除
            // 接下来需要删除的就是 successor
            x = successor;
        }
        /*
        replacement: 替代节点, 注意这里的替代并不是 替代原始被删除节点x, 
        上面删除度为2节点的时候, 已经使用 successor 替代了x, 
        所以这里删除的新的x节点 的替代者是x的子节点, 也是叶子节点
        
        替代者节点就是最终的替代者, 而不是最初的替代者
         */
        TreeNode<V> replacement = x.right != null ? x.right : x.left;

        // 删除 度为1和0的节点
        if (replacement != null) { // 度为1
            replacement.parent = x.parent;
        }
        
        // 度为1和0 共用
        if (x.parent == null) {
            root = replacement; // 删除根节点, 则替换root
        } else if (isLeft(x.parent, x)) {
            x.parent.left = replacement; // 删除的是左子节点
        } else {
            x.parent.right = replacement; // 删除的是右子节点
        }
        size--;
    }

    protected TreeNode<V> removeAndReturnMax(TreeNode<V> parent) {
        if (parent == null) {
            return null;
        }
        while (parent.right != null) {
            parent = parent.right;
        }
        // max = parent 待删除的节点
        if (parent.parent != null) {
            parent.parent.right = parent.left;
        }
        if (parent.left != null) {
            parent.left.parent = parent.parent;
        }
        return parent;
    }
    
    protected TreeNode<V> removeAndReturnMin(TreeNode<V> parent) {
        if (parent == null) {
            return null;
        }
        while (parent.left != null) {
            parent = parent.left;
        }
        // min = parent 待删除的节点
        if (parent.parent != null) {
            parent.parent.left = parent.right;
        }
        if (parent.right != null) {
            parent.right.parent = parent.parent;
        }
        return parent;
    }
    
    protected TreeNode<V> removeMaxReturnNewParent(TreeNode<V> parent) {
        if (parent == null) {
            return null;
        }
        while (parent.right != null) {
            parent = parent.right;
        }
        TreeNode<V> newParent = parent.left;
        // max = parent 待删除的节点
        if (parent.parent != null) {
            parent.parent.right = parent.left;
        }
        if (parent.left != null) {
            parent.left.parent = parent.parent;
        }
        return newParent;
    }
    
    protected TreeNode<V> removeMinReturnNewParent(TreeNode<V> parent) {
        if (parent == null) {
            return null;
        }
        while (parent.left != null) {
            parent = parent.left;
        }
        TreeNode<V> newParent = parent.right;;
        // min = parent 待删除的节点
        if (parent.parent != null) {
            parent.parent.left = parent.right;
        }
        if (parent.right != null) {
            parent.right.parent = parent.parent;
        }
        return newParent;
    }
}
