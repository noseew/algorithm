package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
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
            root = new TreeNode<>( null, null, v);
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
        if (root == null || v == null) {
            return null;
        }

        TreeNode<V> parent = root;
        while (true) {
            TreeNode<V> next;
            if (less(v, parent.val)) {
                next = parent.left;
            } else if (greater(v, parent.val)) {
                next = parent.right;
            } else {
                return parent.val;
            }
            if (next == null) {
                return null;
            }
            parent = next;
        }
    }

    @Override
    public V remove(V v) {
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

    protected TreeNode<V> floor(TreeNode<V> parent, V v) {
        TreeNode<V> floor = null;
        while (true) {
            if (parent == null) {
                break;
            }
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

    protected TreeNode<V> ceiling(TreeNode<V> parent, V v) {
        TreeNode<V> ceiling = null;
        while (true) {
            if (parent == null) {
                break;
            }
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

    /***************************************** 工具 *****************************************************/

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

    private void put(TreeNode<V> parent, V v) {
        TreeNode<V> newNode = new TreeNode<>( null, null, v);
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
