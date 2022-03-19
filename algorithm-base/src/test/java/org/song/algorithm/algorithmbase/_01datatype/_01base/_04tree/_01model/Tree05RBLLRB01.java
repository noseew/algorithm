package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree.printer.BTreeUtils;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树
这里实现是 等价23树, 左倾红黑树, 这里的实现参考《算法4》

红黑树 1972年由Rudolf Bayer发明的
左倾红黑树 2008年 Robert Sedgewick 对其进行了改进, 并命名为 LLRBT(Left-leaning Red Black Tree 左倾红黑树). 
左倾红黑树相比1978年的红黑树要简单很多, 实现的代码量也少很多. Robert Sedgewick也是Algorithms(中文版叫《算法》)这本书的作者

等价定义
    红链接均为左链接
    没有任何一个结点同时和两条红链接相连
    该树是完美黑色平衡的, 即任意空链接到根结点的路径上的黑链接数量相同

红黑树定义
    1. 每个节点是红色或黑色的. 
    2. 根节点是黑色的. 
    3. 每个叶子节点是黑色的, 空子节点默认为黑色. 
    4. 如果一个节点为红色, 则其孩子节点必为黑色. 
    5. 从任一节点到其后代叶子的路径上, 均包含相同数目的黑节点. 
    
总结:
    1. 是红或黑, 根是黑, 叶子为空或黑, 红子必为黑, 黑高相等
    2. 新节点为红, 然后根据情况旋转和变色


可以通过理解23树来理解左倾红黑树的旋转和变色
 */
public class Tree05RBLLRB01<V extends Comparable<V>> extends Tree05RBAbs<V> {

    public Tree05RBLLRB01(Comparator<V> comparator) {
        super(comparator);
    }

    @Override
    public boolean add(V v) {
        int size = this.size;
        root = insert_recursive(root, v);
        // 根节点总为黑色
        root.red = BLACK;
        return size > this.size;
    }

    @Override
    public V remove(V v) {
        root = remove_recursive(root, v);
        return v;
    }

    @Override
    protected TreeNode<V> insert_recursive(TreeNode<V> parent, V v) {
        if (parent == null) {
            // 新建节点, 高度默认1
            parent = new TreeNode<>(v, RED);
            parent.height = 1;
            size++;
            return parent;
        }

        if (less(v, parent.val)) {
            // 向左插入
            parent.left = insert_recursive(parent.left, v);
        } else if (greater(v, parent.val)) {
            // 向右插入
            parent.right = insert_recursive(parent.right, v);
        } else {
            parent.val = v; // 重复元素不处理 直接替换值
            return parent;
        }
        /*
        平衡处理, 每个节点都要判断并处理
        由于插入是递归操作, 所以每插入一个元素, 都会进行一次平衡调整
        平衡调整由插入的叶子结点的父节点开始, 递归向上逐个判断, 一直判断到根节点
         */
        parent = balanceInsertion(parent);
        return parent;
    }

    @Override
    protected TreeNode<V> remove_recursive(TreeNode<V> parent, V v) {

        if (null == parent) {
            return parent;
        }
        /*
        1. 递归找到指定的节点s
        2. 找到s的直接前驱结点或者直接后继节点, 替代s即可
            1. 直接前驱结点: 就是s的左子树的右右..右子节点
            2. 直接后继节点: 就是s的右子树的左左..右子节点
         */

        if (less(v, parent.val)) {
            // 小于当前根节点
            parent.left = remove_recursive(parent.left, v);
        } else if (greater(v, parent.val)) {
            // 大于当前根节点
            parent.right = remove_recursive(parent.left, v);
        } else if (parent.left != null && parent.right != null) {
            // 找到右边最小的节点
            parent.val = getMinNode(parent.right).val;
            // 当前节点的右边等于原节点右边删除已经被选为的替代节点
            parent.right = remove_recursive(parent.right, parent.val);
        } else {
            parent = (parent.left != null) ? parent.left : parent.right;
        }
        parent = balanceInsertion(parent);
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
    1. 删除节点的过程, 假设删除的节点是x,
        如果x仅有一个子节点(没有孙节点), 则删除x之后, 让子节点直接接替x节点即可
        如果x有左右子节点(且有孙节点), 则删除x之后, 则需要找到x的直接前驱节点或直接后继节点, 让其替代当前这个位置
    2. 删除示例: 这里不分左右, 因为左右是相同的
        删除前
              父
              |
              x(待删除)
              |
              子(可有可无, 0-2个)
        删除后
              父
              |
              子
              |
              子(可有可无, 最多1个)
     3. 修正
        1) x=红, 子=空
            无需处理
        2) x=黑, 子=红
            直接将子变为黑
        3) x=黑, 子=黑
            分4种情况处理
            删除前, b是x兄弟, 不分左右
                  父
                 /  \
                b    x(待删除)
                     |
                     子(可有可无, 0-2个)

            分4种情况处理
                3.3.1 b=黑, b的子有红
                3.3.2 b=黑, b的子无红, 父=红
                3.3.3 b=黑, b的子无红, 父=黑
                3.3.4 b=红

     */

    @Override
    protected TreeNode<V> balanceInsertion(TreeNode<V> x) {
        if (x == null) {
            return x;
        }
        // 右红左黑: 左旋 == 情况 2.2
        if (isRed(x.right) && !isRed(x.left)) {
            x = leftRotate(x);
        }
        // 左红左左红: 右旋 == 情况 2.1
        if (isRed(x.left) && isRed(x.left.left)) {
            x = rightRotate(x);
        }
        // 左红右红: 变色 == 情况 1
        if (isRed(x.left) && isRed(x.right)) {
            flipColors(x);
        }

        return x;
    }

    @Override
    protected TreeNode<V> rightRotate(TreeNode<V> p) {
        // 复用 AVL 的右旋
        TreeNode<V> newParent = p.left;
        p.left = newParent.right;
        newParent.right = p;
        // 红黑树自己的处理
        newParent.red = p.red;
        p.red = RED;

        return newParent;
    }

    @Override
    protected TreeNode<V> leftRotate(TreeNode<V> p) {
        // 复用 AVL 的左旋
        TreeNode<V> newParent = p.right;
        p.right = newParent.left;
        newParent.left = p;
        // 红黑树自己的处理
        newParent.red = p.red;
        p.red = RED;

        return newParent;
    }

    /**
     * 将h节点设为红色, 其两个子节点设为黑色
     * 
     * @param node
     */
    protected void flipColors(TreeNode<V> node) {
        node.red = RED;
        node.left.red = BLACK;
        node.right.red = BLACK;
    }

    @Override
    public String toString() {
        return BTreeUtils.print(root, false);
    }
}
