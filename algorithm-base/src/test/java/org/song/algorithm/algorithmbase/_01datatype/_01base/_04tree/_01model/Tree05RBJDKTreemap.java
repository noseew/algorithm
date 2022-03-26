package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.printer.BTreeUtils;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.function.Consumer;

/*
红黑树

根据JDK8 TreeMap中的红黑树 源码修改

坑爹 源码红黑树中, RED = false, BLACK = true;

 */
public class Tree05RBJDKTreemap<V extends Comparable<V>> extends Tree05RBAbs<V> {

    private transient int modCount = 0;

    public Tree05RBJDKTreemap(Comparator<V> comparator) {
        super(comparator);
    }

    public boolean add(V v) {
        TreeNode<V> t = root;
        if (root == null) {
            root = new TreeNode(v, RED);
            size++;
            modCount++;
            return true;
        }
        int cmp;
        TreeNode<V> parent;

        do {
            parent = t;
            cmp = comparator.compare(v, t.val);
            if (cmp < 0) {
                t = t.left;
            } else if (cmp > 0) {
                t = t.right;
            } else {
                t.setVal(v);
                return true;
            }
        } while (t != null);

        TreeNode<V> e = new TreeNode<>(parent, v, RED);
        if (cmp < 0) {
            parent.left = e;
        } else {
            parent.right = e;
        }
        balanceInsertion(e);
        size++;
        modCount++;

        root.red = BLACK;
        return true;
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

//    private static final boolean RED = false;
//    private static final boolean BLACK = true;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

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

    /*
    p = 替代被删除节点的节点
    replacement = 替代者节点的原子节点
     */
    private void deleteEntry(TreeNode<V> p) {
        modCount++;
        size--;

        // 删除节点p, 度为2, 找到直接后继节点替代它
        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            TreeNode<V> s = successor(p);
            p.val = s.val;
            p = s;
        } // p has 2 children

        // 此时待删除节点p=替代后的节点, 删除替代后的节点p, 需要用其子节点来再次替代它, 也就是真正的替代者是 replacement
        // Start fixup at replacement node, if it exists.
        TreeNode<V> replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) { // 新的待删除节点 度为1
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
        } else {
            // 新的待删除节点 度为0
            //  No children. Use self as phantom replacement and unlink.
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

    protected TreeNode<V> successor(TreeNode<V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            // 向左取最小
            TreeNode<V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            TreeNode<V> p = t.parent;
            TreeNode<V> ch = t;
            // 没有右子节点, 找到其右父节点
            while (p != null && ch == p.right) {
                // 循环向上, 直到跳出右子链
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

//    private static <V> void setColor(TreeNode<V> p, boolean c) {
//        if (p != null)
//            p.red = c;
//    }

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

    public String toString(boolean printColor) {
        return BTreeUtils.simplePrint(root, printColor);
    }

}
