package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

/*
红黑树

根据JDK8 TreeMap中的红黑树 源码修改

 */
public class Tree05_RB_jdktreemap<V extends Comparable<V>> extends Tree05_RB_abs<V> {

    private transient int modCount = 0;

    public Tree05_RB_jdktreemap(Comparator<V> comparator) {
        super(comparator);
    }

    public V put(V v) {
        TreeNode<V> t = root;
        // 如果只有一个元素, 初始化根节点后直接返回
        if (t == null) {
            // 初始化根节点
            root = new TreeNode(v, root != null);
            size = 1;
            modCount++;
            return null;
        }
        int cmp;
        // 临时节点
        TreeNode<V> parent;

        // 1. 替换并返回
        do {
            // 遍历树, 临时节点逐渐遍历
            parent = t;
            cmp = comparator.compare(v, t.val);
            if (cmp < 0) {
                // 放在节点的左子节点上
                t = t.left;
            } else if (cmp > 0) {
                // 放在节点的右子节点上
                t = t.right;
            } else {
                // 覆盖设置节点本身
                t.setVal(v);
                return v;
            }
            // 二分查找的方式, 遍历到叶子节点
        } while (t != null);

        // 2. 新建 并返回
        TreeNode<V> e = new TreeNode(parent, v, true);
        if (cmp < 0) {
            parent.left = e;
        } else {
            parent.right = e;
        }
        /**
         * 调整红黑树
         * 上面已经完成了排序二叉树的的构建, 将新增节点插入该树中的合适位置
         * 下面fixAfterInsertion()方法就是对这棵树进行调整/平衡, 具体过程参考上面的五种情况
         */
        balanceInsertion(e);
        size++;
        modCount++;

        root.red = BLACK;
        return null;
    }

    @Override
    public V remove(V v) {
        TreeNode<V> p = search_traverse(root, v);
        if (p == null)
            return null;

        V oldValue = p.val;
        deleteEntry(p);
        return oldValue;
    }

    @Override
    public void clear() {
        super.clear();
        modCount++;
    }

    // NavigableMap API methods

    @Override
    public V removeMin() {
        TreeNode<V> p = getFirstEntry();
        if (p != null) {
            deleteEntry(p);
        }
        return keyOrNull(p);
    }

    @Override
    public V removeMax() {
        TreeNode<V> p = getLastEntry();
        if (p != null) {
            deleteEntry(p);
        }
        return keyOrNull(p);
    }

    @Override
    public V floor(V v) {
        return keyOrNull(getFloorNode(v));
    }

    @Override
    public V ceiling(V v) {
        return keyOrNull(getCeilingNode(v));
    }

    final TreeNode<V> getCeilingNode(V v) {
        TreeNode<V> p = root;
        while (p != null) {
            int cmp = comparator.compare(v, p.val);
            if (cmp < 0) {
                if (p.left != null)
                    p = p.left;
                else
                    return p;
            } else if (cmp > 0) {
                if (p.right != null) {
                    p = p.right;
                } else {
                    TreeNode<V> parent = p.parent;
                    TreeNode<V> ch = p;
                    while (parent != null && ch == parent.right) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            } else
                return p;
        }
        return null;
    }

    final TreeNode<V> getFloorNode(V v) {
        TreeNode<V> p = root;
        while (p != null) {
            int cmp = comparator.compare(v, p.val);
            if (cmp > 0) {
                if (p.right != null)
                    p = p.right;
                else
                    return p;
            } else if (cmp < 0) {
                if (p.left != null) {
                    p = p.left;
                } else {
                    TreeNode<V> parent = p.parent;
                    TreeNode<V> ch = p;
                    while (parent != null && ch == parent.left) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            } else
                return p;

        }
        return null;
    }

    public void forEach(Consumer<? super V> action) {
        Objects.requireNonNull(action);
        int expectedModCount = modCount;
        for (TreeNode<V> e = getFirstEntry(); e != null; e = successor(e)) {
            action.accept(e.val);

            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    // Red-black mechanics

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    final TreeNode<V> getFirstEntry() {
        TreeNode<V> p = root;
        if (p != null)
            while (p.left != null)
                p = p.left;
        return p;
    }

    final TreeNode<V> getLastEntry() {
        TreeNode<V> p = root;
        if (p != null)
            while (p.right != null)
                p = p.right;
        return p;
    }

    private void deleteEntry(TreeNode<V> p) {
        modCount++;
        size--;

        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            TreeNode<V> s = successor(p);
            p.val = s.val;
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        TreeNode<V> replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            // Link replacement to parent
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left = replacement;
            else
                p.parent.right = replacement;

            // Null out links so they are OK to use by fixAfterDeletion.
            p.left = p.right = p.parent = null;

            // Fix replacement
            if (p.red == BLACK)
                balanceDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            if (p.red == BLACK)
                balanceDeletion(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    static <V> TreeNode<V> successor(TreeNode<V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            TreeNode<V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            TreeNode<V> p = t.parent;
            TreeNode<V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    static <V> TreeNode<V> predecessor(TreeNode<V> t) {
        if (t == null)
            return null;
        else if (t.left != null) {
            TreeNode<V> p = t.left;
            while (p.right != null)
                p = p.right;
            return p;
        } else {
            TreeNode<V> p = t.parent;
            TreeNode<V> ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    /**
     * Balancing operations.
     * <p>
     * Implementations of rebalancings during insertion and deletion are
     * slightly different than the CLR version.  Rather than using dummy
     * nilnodes, we use a set of accessors that deal properly with null.  They
     * are used to avoid messiness surrounding nullness checks in the main
     * algorithms.
     */

    private static <V> boolean colorOf(TreeNode<V> p) {
        return (p == null ? BLACK : p.red);
    }

    private static <V> TreeNode<V> parentOf(TreeNode<V> p) {
        return (p == null ? null : p.parent);
    }

    private static <V> void setColor(TreeNode<V> p, boolean c) {
        if (p != null)
            p.red = c;
    }

    private static <V> TreeNode<V> leftOf(TreeNode<V> p) {
        return (p == null) ? null : p.left;
    }

    private static <V> TreeNode<V> rightOf(TreeNode<V> p) {
        return (p == null) ? null : p.right;
    }

    @Override
    protected TreeNode<V> leftRotate(TreeNode<V> p) {
        if (p != null) {
            TreeNode<V> r = p.right;
            p.right = r.left;
            if (r.left != null) {
                r.left.parent = p;
            }
            r.parent = p.parent;
            if (p.parent == null) {
                root = r;
            } else if (p.parent.left == p) {
                p.parent.left = r;
            } else {
                p.parent.right = r;
            }
            r.left = p;
            p.parent = r;
        }
        return root;
    }

    @Override
    protected TreeNode<V> rightRotate(TreeNode<V> p) {
        if (p != null) {
            TreeNode<V> l = p.left;
            p.left = l.right;
            if (l.right != null) {
                l.right.parent = p;
            }
            l.parent = p.parent;
            if (p.parent == null) {
                root = l;
            } else if (p.parent.right == p) {
                p.parent.right = l;
            } else {
                p.parent.left = l;
            }
            l.right = p;
            p.parent = l;
        }
        return root;
    }

    @Override
    protected TreeNode<V> balanceInsertion(TreeNode<V> x) {
        x.red = RED;

        while (x != null && x != root && x.parent.red == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                TreeNode<V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        leftRotate(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rightRotate(parentOf(parentOf(x)));
                }
            } else {
                TreeNode<V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rightRotate(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    leftRotate(parentOf(parentOf(x)));
                }
            }
        }
        root.red = BLACK;

        return null;
    }

    @Override
    protected TreeNode<V> balanceDeletion(TreeNode<V> x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                TreeNode<V> sib = rightOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    leftRotate(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib)) == BLACK &&
                        colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rightRotate(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    leftRotate(parentOf(x));
                    x = root;
                }
            } else { // symmetric
                TreeNode<V> sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rightRotate(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK &&
                        colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        leftRotate(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rightRotate(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, BLACK);
        return null;
    }

}
