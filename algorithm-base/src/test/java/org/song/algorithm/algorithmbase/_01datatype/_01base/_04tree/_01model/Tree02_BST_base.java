package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/*
一棵二叉查找树(BST)是一棵二叉树, 
其中每个结点都含有一个Comparable的键(以及相关联的值)且每个结点的键都大于其左子树中的任意结点的键而小于右子树的任意结点的键
没有平衡功能
 */
public class Tree02_BST_base<V extends Comparable<V>> extends _02BSTTreeBase<V> {

    public int size;

    public TreeNode<V> root;

    public Tree02_BST_base(Comparator<V> comparator) {
        super(comparator);
    }

    @Override
    public boolean push(V v) {
        if (root == null) {
            root = new TreeNode<>(v);
            size++;
            return true;
        }

        TreeNode<V> parent = root;
        while (true) {
            TreeNode<V> next;
            if (less(v, parent.val)) {
                next = parent.left;
            } else if (greater(v, parent.val)) {
                next = parent.right;
            } else {
                return false;
            }
            if (next == null) {
                break;
            }
            parent = next;
        }

        put(parent, v);
        size++;
        return true;
    }

    @Override
    public V get(V v) {
        TreeNode<V> node = this.search_traverse(root, v);
        if (node != null) {
            return node.val;
        }
        return null;
    }

    @Override
    public V remove(V v) {
        root = remove_recursive(root, v);
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * 最大值
     *
     * @return
     */
    @Override
    public V max() {
        if (root == null) {
            return null;
        }

        TreeNode<V> max = root;
        while (max.right != null) {
            max = max.right;
        }
        return max.val;
    }

    /**
     * 最小值
     *
     * @return
     */
    @Override
    public V min() {
        if (root == null) {
            return null;
        }

        TreeNode<V> max = root;
        while (max.left != null) {
            max = max.left;
        }
        return max.val;
    }

    @Override
    public V removeMax() {
        TreeNode<V> max = removeMax(root);
        if (max != null) {
            return max.val;
        }
        return null;
    }

    @Override
    public V removeMin() {
        TreeNode<V> min = removeMin(root);
        if (min != null) {
            return min.val;
        }
        return null;
    }

    /**
     * 地板
     *
     * @param v
     * @return
     */
    @Override
    public V floor(V v) {
        TreeNode<V> floor = floor(root, v);
        return floor != null ? floor.val : null;
    }

    /**
     * 天花板
     *
     * @param v
     * @return
     */
    @Override
    public V ceiling(V v) {
        TreeNode<V> ceiling = ceiling(root, v);
        return ceiling != null ? ceiling.val : null;
    }

    /**
     * 排名
     *
     * @param v
     * @return
     */
    @Override
    public int rank(V v) {
        AtomicInteger rank = new AtomicInteger(1);
        // 采用中序遍历, 并计数
        traverse(root, Order.MidOrder, e -> {
            if (greater(v, e)) {
                rank.incrementAndGet();
                return true;
            }
            return false;
        });
        return rank.get();
    }

    /**
     * 范围
     * 左开右闭
     * 
     * @param min >= min
     * @param max < max
     * @return
     */
    @Override
    public List<V> range(V min, V max) {
        List<V> list = new ArrayList<>();
        if (greater(min, max)) {
            return list;
        }
        // 先找到 min
        TreeNode<V> minNode = ceiling(root, min);
        if (minNode == null || greater(minNode.val, max)) {
            return list;
        }
        /*
        遍历整棵树 e 要在 min max 之间 
            可不可以通过ceiling先找到 min, 然后再遍历到 max呢? 
            不行, 因为这里的树没有parent指针, 直接通过min节点遍历会丢失parent指针的其他节点数据
        为了降低遍历的范围, 这里将数据分为3段
            [step=1 (v<min)], [step=2 (min<=v<max)], [step=3 (max>v)]
            当到达第三段时, 遍历停止
         */
        AtomicInteger step = new AtomicInteger(1);
        traverse(root, Order.MidOrder, e -> {
            // 遍历整棵树 e 要在 min max 之间 
            if (less(e, max) && !less(e, min) && step.get() < 3) {
                list.add(e);
                step.set(2);
                return true;
            }
            if (step.get() == 2) {
                step.set(3);
                return false;
            }
            return true;
        });
        return list;
    }

    @Override
    public String toString() {
        return BTreePrinter.print(root, false);
    }

    /***************************************** 通用方法 可重写 *****************************************************/

    /**
     * 采用递归的方式, 插入节点
     *
     * @param parent 以 parent 为root
     * @param v
     * @return 返回新的 parent 节点
     */
    protected TreeNode<V> insert_recursive(TreeNode<V> parent, V v) {
        if (parent == null) {
            // 新建节点, 高度默认1
            parent = new TreeNode<>(v);
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
        return parent;
    }

    /**
     * 采用递归方式, 查找节点
     *
     * @param parent 以 parent 为root
     * @param v
     * @return
     */
    protected TreeNode<V> search_recursive(TreeNode<V> parent, V v) {
        if (parent == null) {
            return null;
        }
        if (less(v, parent.val)) {
            return search_recursive(parent.left, v);
        } else if (greater(v, parent.val)) {
            return search_recursive(parent.right, v);
        } else {
            return parent;
        }
    }

    /**
     * 采用递归方式, 删除节点
     *
     * @param parent 以 parent 为root
     * @param v 返回新的 parent 节点
     * @return
     */
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
            parent.val = min(parent.right).val;
            // 当前节点的右边等于原节点右边删除已经被选为的替代节点
            parent.right = remove_recursive(parent.right, parent.val);
        } else {
            parent = (parent.left != null) ? parent.left : parent.right;
        }
        return parent;
    }

    /**
     * 采用循环遍历方式, 查找节点
     *
     * @param parent 以 parent 为root
     * @param v
     * @return
     */
    protected TreeNode<V> search_traverse(TreeNode<V> parent, V v) {
        while (parent != null) {
            if (less(v, parent.val)) {
                parent = parent.left;
            } else if (greater(v, parent.val)) {
                parent = parent.right;
            } else {
                return parent;
            }
        }
        return parent;
    }

    /***************************************** 工具 *****************************************************/
   
    /**
     * 由于没有parent指针, 所以不支持删除自己
     * 
     * @param parent
     * @return
     */
    protected TreeNode<V> removeMax(TreeNode<V> parent) {
        if (parent == null) {
            return null;
        }
        TreeNode<V> max = parent;
        while (max.right != null) {
            parent = max;
            max = max.right;
        }
        parent.right = max.left;
        return max;
    }

    /**
     * 由于没有parent指针, 所以不支持删除自己
     *
     * @param parent
     * @return
     */
    protected TreeNode<V> removeMin(TreeNode<V> parent) {
        if (parent == null) {
            return null;
        }
        TreeNode<V> max = parent;
        while (max.left != null) {
            parent = max;
            max = max.left;
        }
        parent.left = max.right;
        return max;
    }
    
    protected TreeNode<V> floor(TreeNode<V> parent, V v) {
        TreeNode<V> floor = null;
        while (parent != null) {
            if (parent.val == v) {
                // v == 当前
                return parent;
            } else if (less(v, parent.val) && parent.left != null) {
                // v < 当前, 向左移动, 等待下次判断
                parent = parent.left;
            } else if (parent.right != null && less(parent.right.val, v)) {
                // v > 当前.right, 向右移动, 等待下次判断
                parent = parent.right;
            } else {
                // floor 介于 parent 和 parent.right 之间, 将满足条件的 node 放入候选单独比较
                if (less(parent.val, v)) {
                    if (floor == null || less(floor.val, parent.val)) {
                        floor = parent;
                    }
                }
                parent = parent.right;
            }
        }
        return floor;
    }

    protected TreeNode<V> ceiling(TreeNode<V> parent, V v) {
        TreeNode<V> ceiling = null;
        while (parent != null) {
            if (parent.val == v) {
                // v == 当前
                return parent;
            } else if (greater(v, parent.val) && parent.right != null) {
                // v > 当前, 向右移动, 等待下次判断
                parent = parent.right;
            } else if (parent.left != null && greater(parent.left.val, v)) {
                // v < 当前.left, 向左移动, 等待下次判断
                parent = parent.left;
            } else {
                // floor 介于 parent 和 parent.left 之间, 将满足条件的 node 放入候选单独比较
                if (greater(parent.val, v)) {
                    if (ceiling == null || greater(ceiling.val, parent.val)) {
                        ceiling = parent;
                    }
                }
                parent = parent.left;
            }
        }
        return ceiling;
    }

    /**
     * 查找最小结点
     */
    protected TreeNode<V> min(TreeNode<V> tree) {
        if (tree == null) {
            return null;
        }

        while (tree.left != null) {
            tree = tree.left;
        }
        return tree;
    }

    /**
     * 获取树的高度
     */
    protected int getHeight(TreeNode<V> node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    /**
     * 获取树的高度
     */
    protected int getHeight_recursive(TreeNode<V> node) {
        if (node == null) {
            return 0;
        }
        return Math.max(getHeight_recursive(node.left), getHeight_recursive(node.right)) + 1;
    }

    private void put(TreeNode<V> parent, V v) {
        TreeNode<V> newNode = new TreeNode<>(v);
        if (comparator != null) {
            if (comparator.compare(v, parent.val) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        } else {
            if (((Comparable) v).compareTo(((Comparable) parent.val)) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }
    }
}
