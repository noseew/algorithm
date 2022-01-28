package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
import org.song.algorithm.algorithmbase.utils.TypeUtils;

import java.util.Comparator;

/*
红黑树
这里实现是 等价23树

等价定义
    红链接均为左链接
    没有任何一个结点同时和两条红链接相连
    该树是完美黑色平衡的, 即任意空链接到根结点的路径上的黑链接数量相同

红黑树定义
    1. 每个节点是红色或黑色的. 
    2. 根节点是黑色的. 
    3. 每个叶子节点是黑色的. 
    4. 如果一个节点为红色, 则其孩子节点必为黑色. 
    5. 从任一节点到其后代叶子的路径上, 均包含相同数目的黑节点. 
    
总结:
    1. 是红或黑, 根是黑, 叶子为空或黑, 红子必为黑, 黑高相等
    2. 新节点为红, 然后根据情况旋转和变色

 */
public class Tree05_RB23_base<V extends Comparable<V>> extends Tree03_AVL_base<V> {

    public RBTreeNode<V> root;

    public static final boolean RED = true;
    public static final boolean BLACK = false;

    public Tree05_RB23_base(Comparator<V> comparator) {
        super(comparator);
    }


    @Override
    public boolean push(V v) {
        int size = this.size;
        root = insert_recursive(root, v);
        // 根节点总为黑色
        root.color = BLACK;
        return size > this.size;
    }

    /**
     * 采用递归的方式, 插入节点
     *
     * @param parent
     * @param v
     * @return
     */
    private RBTreeNode<V> insert_recursive(RBTreeNode<V> parent, V v) {
        if (parent == null) {
            // 新建节点
            parent = new RBTreeNode<>(null, null, v, RED);
            size++;
            return parent;
        }

        if (less(v, parent.val)) {
            // 向左插入
            parent.left = insert_recursive((RBTreeNode<V>) parent.left, v);
        } else if (greater(v, parent.val)) {
            // 向右插入
            parent.right = insert_recursive((RBTreeNode<V>) parent.right, v);
        } else {
            parent.val = v; // 重复元素不处理 直接替换值
            return parent;
        }
        // 递归从叶子结点向上, 逐个判断
        parent = (RBTreeNode<V>) balance(parent);
        return parent;
    }


    /***************************************** 平衡处理-旋转-变色 *****************************************************/

    // 新增修正
    /*
    新增修正
        1. 不需要修正, 新=红色, 父=黑色
        2. 需要修正, 新=红色, 父=红, 双红需要修正
            修正总结: 叔红变 黑, 叔黑转 红
                1. 父红-叔红=>直接变色: 父叔变黑, 组变红(如果组不是root)
                2. 父红-叔黑=>旋转+变色
                    [LR:左旋] [LL:右旋] [变色:新组黑 新叔红]
                    [RL:右旋] [RR:左旋] [变色:新组黑 新叔红]
        - 前提:
            1. 新插入节点初始为红色(因为约定)
            2. 祖节点一定是黑色(因为平衡)
            
     */
    // 具体修正
    /*
    具体修正
        - 需要修正的情况有
         情况 1. 叔=红, 仅需要变色

                          祖[黑]
                        /   \
                      父[红] 叔[红]
                      /
                     新(红)
                修正 ===========>> 变色, 父=>黑 叔=>黑 祖=>红(如果组不是root)
                          祖[红]
                        /   \
                      父[黑] 叔[黑]
                      /
                     新(红)


         情况 2. 叔=黑

            1) 新为左子节点

                          祖(黑)
                        /   \
                      父(红) 叔(黑)
                      /
                     新(红)
                修正 ===========>> 旋转, 右旋, 父为轴祖右旋
                          父(红)
                        /   \
                       新(红) 祖(黑)
                              \
                              叔(黑)
                修正 ===========>> 变色, 父(新祖)=>黑 祖(新叔)=>红
                          父(黑)
                        /   \
                       新(红) 祖(红)
                              \
                              叔(黑)

            1) 新为右子节点

                          祖(黑)
                        /   \
                      父(红) 叔(黑)
                         \
                          新(红)
                修正 ===========>> 旋转, 左旋, 新为轴父左旋
                          祖(黑)
                        /   \
                      父(红) 叔(黑)
                      /
                     新(红)
                修正 ===========>> 旋转, 右旋, 父为轴祖右旋 => 参见情况1

     */
    // 删除修正
    /*
    
     */

    /**
     * 平衡判断和处理
     */
    @Override
    protected TreeNode<V> balance(TreeNode<V> node) {
        if (node == null) {
            return node;
        }
        RBTreeNode<V> rbNode = TypeUtils.down(node, RBTreeNode.class);

        // 右红左黑: 左旋 == 情况 2.2
        if (isRed((RBTreeNode<V>) rbNode.right) && !isRed((RBTreeNode<V>) rbNode.left)) {
            rbNode = (RBTreeNode<V>) leftRotation4RR(rbNode);
        }
        // 左红左左红: 右旋 == 情况 2.1
        if (isRed((RBTreeNode<V>) rbNode.left) && isRed((RBTreeNode<V>) rbNode.left.left)) {
            rbNode = (RBTreeNode<V>) rightRotate4LL(rbNode);
        }
        // 左红右红: 变色 == 情况 1
        if (isRed((RBTreeNode<V>) rbNode.left) && isRed((RBTreeNode<V>) rbNode.right)) {
            flipColors(rbNode);
        }

        return rbNode;
    }

    /**
     * 处理 / LL
     */
    @Override
    protected TreeNode<V> rightRotate4LL(TreeNode<V> p) {
        RBTreeNode<V> rbp = TypeUtils.down(p, RBTreeNode.class);
        // 复用 AVL 的右旋
        RBTreeNode<V> newParent = (RBTreeNode<V>) super.rightRotate4LL(rbp);
        // 红黑树自己的处理
        newParent.color = rbp.color;
        rbp.color = RED;

        return newParent;
    }

    /**
     * 处理 \ RR
     */
    @Override
    protected TreeNode<V> leftRotation4RR(TreeNode<V> p) {
        RBTreeNode<V> rbp = TypeUtils.down(p, RBTreeNode.class);
        // 复用 AVL 的左旋
        RBTreeNode<V> newParent = (RBTreeNode<V>) super.leftRotation4RR(rbp);
        // 红黑树自己的处理
        newParent.color = rbp.color;
        rbp.color = RED;

        return newParent;
    }

    protected boolean isRed(RBTreeNode<V> node) {
        if (node == null) {
            return false;
        }
        return node.color == RED;
    }

    protected void flipColors(RBTreeNode<V> node) {
        node.color = RED;
        ((RBTreeNode<V>) node.left).color = BLACK;
        ((RBTreeNode<V>) node.right).color = BLACK;
    }

    @Override
    public String toString() {
        return BTreePrinter.print(root, false);
    }
}
