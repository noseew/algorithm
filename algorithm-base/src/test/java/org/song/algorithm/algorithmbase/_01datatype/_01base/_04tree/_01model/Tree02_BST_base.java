package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack.stack.Stack_base_01;

import java.util.Comparator;
import java.util.List;

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
    public void push(V v) {
        if (root == null) {
            root = new TreeNode<>( null, null, v);
            size++;
            return;
        }

        TreeNode<V> parent = root;
        while (true) {
            TreeNode<V> next;
            if (less(v, parent.val)) {
                next = parent.left;
            } else if (greater(v, parent.val)) {
                next = parent.right;
            } else {
                return;
            }
            if (next == null) {
                break;
            }
            parent = next;
        }

        put(parent, v);
        size++;
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
        // TODO
        return null;
    }

    @Override
    public int size() {
        return size;
    }

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
    public V floor(V v) {
        Stack_base_01<TreeNode<V>> stack = new Stack_base_01<>();
        TreeNode<V> parent = root;
        while (true) {
            if (parent.val == v) {
                // v == 当前
                return parent.val;
            } else if (less(v, parent.val) && parent.left != null) {
                // v < 当前, 向左移动, 等待下次判断
                parent = parent.left;
            } else if (parent.right != null && less(parent.right.val, v)) {
                // v > 当前.right, 向右移动, 等待下次判断
                parent = parent.right;
            } else {
                // floor 介于 parent 和 parent.right 之间, 将满足条件的 node 放入队列, 然后单独比较
                if (less(parent.val, v)) {
                    stack.push(parent);
                } 
                parent = parent.right;
            }
            if (parent == null) {
                break;
            }
        }
        if (stack.isEmpty()) {
            return null;
        }
        // 取出距离v最近的node
        TreeNode<V> floor = stack.pop();
        while (!stack.isEmpty()) {
            TreeNode<V> pop = stack.pop();
            if (greater(pop.val, v)) {
                continue;
            }
            if (greater(pop.val, floor.val)) {
                floor = pop;
            }
        }
        return floor.val;
    }

    @Override
    public V ceiling(V v) {
        // TODO
        Stack_base_01<TreeNode<V>> stack = new Stack_base_01<>();
        TreeNode<V> parent = root;
        while (true) {
            if (parent.val == v) {
                // v == 当前
                return parent.val;
            } else if (greater(v, parent.val)) {
                // v > 当前
                if (parent.right == null) {
                    break;
                }
                // 向右移动, 等待下次判断
                parent = parent.right;
            } else if (parent.left != null && less(parent.left.val, v)) {
                // v < 当前.left, 向左移动, 等待下次判断
                parent = parent.left;
            } else {
                // floor 介于 parent 和 parent.left 之间, 将满足条件的 node 放入队列, 然后单独比较
                stack.push(parent);
                parent = parent.left;
            }
            if (parent == null) {
                break;
            }
        }
        if (stack.isEmpty()) {
            return null;
        }
        // 取出距离v最近的node
        TreeNode<V> ceiling = stack.pop();
        while (!stack.isEmpty()) {
            TreeNode<V> pop = stack.pop();
            if (less(pop.val, v)) {
                continue;
            }
            if (less(pop.val, ceiling.val)) {
                ceiling = pop;
            }
        }
        return ceiling.val;
    }

    @Override
    public int rank(V v) {
        return 0;
    }

    @Override
    public List<V> range(V min, V max) {
        return null;
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
