package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.function.Consumer;

/*
红黑树

根据JDK8 TreeMap中的红黑树 源码修改

 */
public class Tree05_RB_treemap<K extends Comparable<K>> {

    private final Comparator<? super K> comparator;

    public transient TreeNode<K> root;

    private transient int size = 0;

    private transient int modCount = 0;

    public Tree05_RB_treemap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }


    // Query Operations

    public int size() {
        return size;
    }

    public K get(Object key) {
        TreeNode<K> p = getEntry(key);
        return (p == null ? null : p.val);
    }

    final TreeNode<K> getEntry(Object key) {
        if (key == null) {
            // 没有比较器, key为null, 不允许
            throw new NullPointerException();
        }
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        // 从根节点开始遍历
        TreeNode<K> p = root;
        while (p != null) {
            int cmp = k.compareTo(p.val);
            if (cmp < 0) {
                p = p.left;
            } else if (cmp > 0) {
                p = p.right;
            } else {
                return p;
            }
        }
        return null;
    }

    final TreeNode<K> getCeilingEntry(K key) {
        TreeNode<K> p = root;
        while (p != null) {
            int cmp = comparator.compare(key, p.val);
            if (cmp < 0) {
                if (p.left != null)
                    p = p.left;
                else
                    return p;
            } else if (cmp > 0) {
                if (p.right != null) {
                    p = p.right;
                } else {
                    TreeNode<K> parent = p.parent;
                    TreeNode<K> ch = p;
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

    final TreeNode<K> getFloorEntry(K key) {
        TreeNode<K> p = root;
        while (p != null) {
            int cmp = comparator.compare(key, p.val);
            if (cmp > 0) {
                if (p.right != null)
                    p = p.right;
                else
                    return p;
            } else if (cmp < 0) {
                if (p.left != null) {
                    p = p.left;
                } else {
                    TreeNode<K> parent = p.parent;
                    TreeNode<K> ch = p;
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

    final TreeNode<K> getHigherEntry(K key) {
        TreeNode<K> p = root;
        while (p != null) {
            int cmp = comparator.compare(key, p.val);
            if (cmp < 0) {
                if (p.left != null)
                    p = p.left;
                else
                    return p;
            } else {
                if (p.right != null) {
                    p = p.right;
                } else {
                    TreeNode<K> parent = p.parent;
                    TreeNode<K> ch = p;
                    while (parent != null && ch == parent.right) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            }
        }
        return null;
    }

    final TreeNode<K> getLowerEntry(K key) {
        TreeNode<K> p = root;
        while (p != null) {
            int cmp = comparator.compare(key, p.val);
            if (cmp > 0) {
                if (p.right != null)
                    p = p.right;
                else
                    return p;
            } else {
                if (p.left != null) {
                    p = p.left;
                } else {
                    TreeNode<K> parent = p.parent;
                    TreeNode<K> ch = p;
                    while (parent != null && ch == parent.left) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            }
        }
        return null;
    }

    public K put(K key) {
        TreeNode<K> t = root;
        // 如果只有一个元素, 初始化根节点后直接返回
        if (t == null) {
            // 初始化根节点
            root = new TreeNode(key, true);
            size = 1;
            modCount++;
            return null;
        }
        int cmp;
        // 临时节点
        TreeNode<K> parent;

        // 1. 替换并返回
        do {
            // 遍历树, 临时节点逐渐遍历
            parent = t;
            cmp = comparator.compare(key, t.val);
            if (cmp < 0) {
                // 放在节点的左子节点上
                t = t.left;
            } else if (cmp > 0) {
                // 放在节点的右子节点上
                t = t.right;
            } else {
                // 覆盖设置节点本身
                t.setVal(key);
                return key;
            }
            // 二分查找的方式, 遍历到叶子节点
        } while (t != null);

        // 2. 新建 并返回
        TreeNode<K> e = new TreeNode(parent, key, true);
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
        fixAfterInsertion(e);
        size++;
        modCount++;
        return null;
    }

    public K remove(Object key) {
        TreeNode<K> p = getEntry(key);
        if (p == null)
            return null;

        K oldValue = p.val;
        deleteEntry(p);
        return oldValue;
    }

    public void clear() {
        modCount++;
        size = 0;
        root = null;
    }

    // NavigableMap API methods

    public TreeNode<K> pollFirstEntry() {
        TreeNode<K> p = getFirstEntry();
        if (p != null)
            deleteEntry(p);
        return p;
    }

    public TreeNode<K> pollLastEntry() {
        TreeNode<K> p = getLastEntry();
        if (p != null)
            deleteEntry(p);
        return p;
    }

    public K lowerKey(K key) {
        return keyOrNull(getLowerEntry(key));
    }

    public K floorKey(K key) {
        return keyOrNull(getFloorEntry(key));
    }

    public K ceilingKey(K key) {
        return keyOrNull(getCeilingEntry(key));
    }

    public K higherKey(K key) {
        return keyOrNull(getHigherEntry(key));
    }

    public void forEach(Consumer<? super K> action) {
        Objects.requireNonNull(action);
        int expectedModCount = modCount;
        for (TreeNode<K> e = getFirstEntry(); e != null; e = successor(e)) {
            action.accept(e.val);

            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    // Little utilities

    static <K> K keyOrNull(TreeNode<K> e) {
        return (e == null) ? null : e.val;
    }

    // Red-black mechanics

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    final TreeNode<K> getFirstEntry() {
        TreeNode<K> p = root;
        if (p != null)
            while (p.left != null)
                p = p.left;
        return p;
    }

    final TreeNode<K> getLastEntry() {
        TreeNode<K> p = root;
        if (p != null)
            while (p.right != null)
                p = p.right;
        return p;
    }

    static <K> TreeNode<K> successor(TreeNode<K> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            TreeNode<K> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            TreeNode<K> p = t.parent;
            TreeNode<K> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    static <K> TreeNode<K> predecessor(TreeNode<K> t) {
        if (t == null)
            return null;
        else if (t.left != null) {
            TreeNode<K> p = t.left;
            while (p.right != null)
                p = p.right;
            return p;
        } else {
            TreeNode<K> p = t.parent;
            TreeNode<K> ch = t;
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

    private static <K> boolean colorOf(TreeNode<K> p) {
        return (p == null ? BLACK : p.red);
    }

    private static <K> TreeNode<K> parentOf(TreeNode<K> p) {
        return (p == null ? null : p.parent);
    }

    private static <K> void setColor(TreeNode<K> p, boolean c) {
        if (p != null)
            p.red = c;
    }

    private static <K> TreeNode<K> leftOf(TreeNode<K> p) {
        return (p == null) ? null : p.left;
    }

    private static <K> TreeNode<K> rightOf(TreeNode<K> p) {
        return (p == null) ? null : p.right;
    }

    private void rotateLeft(TreeNode<K> p) {
        if (p != null) {
            // 获取P的右子节点, 其实这里就相当于新增节点N(情况四而言)
            TreeNode<K> r = p.right;
            // 将R的左子树设置为P的右子树
            p.right = r.left;
            // 若R的左子树不为空, 则将P设置为R左子树的父亲
            if (r.left != null) {
                r.left.parent = p;
            }
            // 将P的父亲设置R的父亲
            r.parent = p.parent;
            // 如果P的父亲为空, 则将R设置为跟节点
            if (p.parent == null) {
                root = r;
            }
            // 如果P为其父节点(G)的左子树, 则将R设置为P父节点(G)左子树
            else if (p.parent.left == p) {
                p.parent.left = r;
            }
            // 否则R设置为P的父节点(G)的右子树
            else {
                p.parent.right = r;
            }
            // 将P设置为R的左子树
            r.left = p;
            // 将R设置为P的父节点
            p.parent = r;
        }
    }

    private void rotateRight(TreeNode<K> p) {
        if (p != null) {
            // 将L设置为P的左子树
            TreeNode<K> l = p.left;
            // 将L的右子树设置为P的左子树
            p.left = l.right;
            // 若L的右子树不为空, 则将P设置L的右子树的父节点
            if (l.right != null) {
                l.right.parent = p;
            }
            // 将P的父节点设置为L的父节点
            l.parent = p.parent;
            // 如果P的父节点为空, 则将L设置根节点
            if (p.parent == null) {
                root = l;
            }
            // 若P为其父节点的右子树, 则将L设置为P的父节点的右子树
            else if (p.parent.right == p) {
                p.parent.right = l;
            }
            // 否则将L设置为P的父节点的左子树
            else {
                p.parent.left = l;
            }
            // 将P设置为L的右子树
            l.right = p;
            // 将L设置为P的父节点
            p.parent = l;
        }
    }

    private void fixAfterInsertion(TreeNode<K> x) {
        //新增节点的颜色为红色
        x.red = RED;

        // 循环 直到 x不是根节点, 且x的父节点不为红色
        while (x != null && x != root && x.parent.red == RED) {
            // 如果X的父节点(P)是其父节点的父节点(G)的左节点
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                // 获取X的叔节点(U)
                TreeNode<K> y = rightOf(parentOf(parentOf(x)));
                // 如果X的叔节点(U) 为红色(情况三)
                if (colorOf(y) == RED) {
                    // 将X的父节点(P)设置为黑色
                    setColor(parentOf(x), BLACK);
                    // 将X的叔节点(U)设置为黑色
                    setColor(y, BLACK);
                    // 将X的父节点的父节点(G)设置红色
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                }
                // 如果X的叔节点(U为黑色)；这里会存在两种情况(情况四/情况五)
                else {
                    // 如果X节点为其父节点(P)的右子树, 则进行左旋转(情况四)
                    if (x == rightOf(parentOf(x))) {
                        // 将X的父节点作为X
                        x = parentOf(x);
                        // 右旋转
                        rotateLeft(x);
                    }
                    //(情况五)
                    // 将X的父节点(P)设置为黑色
                    setColor(parentOf(x), BLACK);
                    // 将X的父节点的父节点(G)设置红色
                    setColor(parentOf(parentOf(x)), RED);
                    // 以X的父节点的父节点(G)为中心右旋转
                    rotateRight(parentOf(parentOf(x)));
                }
            }
            // 如果X的父节点(P)是其父节点的父节点(G)的右节点
            else {
                // 获取X的叔节点(U)
                TreeNode<K> y = leftOf(parentOf(parentOf(x)));
                // 如果X的叔节点(U) 为红色(情况三)
                if (colorOf(y) == RED) {
                    // 将X的父节点(P)设置为黑色
                    setColor(parentOf(x), BLACK);
                    // 将X的叔节点(U)设置为黑色
                    setColor(y, BLACK);
                    // 将X的父节点的父节点(G)设置红色
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                }
                // 如果X的叔节点(U为黑色)；这里会存在两种情况(情况四/情况五)
                else {
                    // 如果X节点为其父节点(P)的右子树, 则进行左旋转(情况四)
                    if (x == leftOf(parentOf(x))) {
                        // 将X的父节点作为X
                        x = parentOf(x);
                        // 右旋转
                        rotateRight(x);
                    }
                    // (情况五)
                    // 将X的父节点(P)设置为黑色
                    setColor(parentOf(x), BLACK);
                    // 将X的父节点的父节点(G)设置红色
                    setColor(parentOf(parentOf(x)), RED);
                    // 以X的父节点的父节点(G)为中心右旋转
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        // 将根节点G强制设置为黑色
        root.red = BLACK;
    }

    private void deleteEntry(TreeNode<K> p) {
        modCount++;
        size--;

        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            TreeNode<K> s = successor(p);
            p.val = s.val;
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        TreeNode<K> replacement = (p.left != null ? p.left : p.right);

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
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            if (p.red == BLACK)
                fixAfterDeletion(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    private void fixAfterDeletion(TreeNode<K> x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                TreeNode<K> sib = rightOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
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
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // symmetric
                TreeNode<K> sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
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
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, BLACK);
    }

}
