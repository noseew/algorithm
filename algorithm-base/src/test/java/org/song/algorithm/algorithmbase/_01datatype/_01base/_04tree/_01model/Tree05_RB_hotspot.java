package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树

根据JDK9 hotspot工具中的红黑树 源码修改

 */
public class Tree05_RB_hotspot<V extends Comparable<V>> {
    public TreeNode<V> root;
    private Comparator comparator;
    public static final boolean RED = true;
    public static final boolean BLACK = false;

    public Tree05_RB_hotspot(Comparator comparator) {
        this.comparator = comparator;
    }

    public void put(V v) {
        insertNode(new TreeNode<>(v, true));
    }

    public void insertNode(TreeNode<V> x) {
        treeInsert(x);

        x.setRed(RED);
        boolean shouldPropagate = false;

        TreeNode<V> propagateStart = x;

        // Loop invariant: x has been updated.
        while ((x != root) && (x.getParent().red == RED)) {
            if (x.getParent() == x.getParent().getParent().getLeft()) {
                TreeNode<V> y = x.getParent().getParent().getRight();
                if ((y != null) && (y.red == RED)) {
                    // Case 1
                    x.getParent().setRed(BLACK);
                    y.setRed(BLACK);
                    x.getParent().getParent().setRed(RED);
                    x = x.getParent().getParent();
                    shouldPropagate = false;
                    propagateStart = x;
                } else {
                    if (x == x.getParent().getRight()) {
                        // Case 2
                        x = x.getParent();
                        leftRotate(x);
                    }
                    // Case 3
                    x.getParent().setRed(BLACK);
                    x.getParent().getParent().setRed(RED);
                    shouldPropagate = rightRotate(x.getParent().getParent());
                    propagateStart = x.getParent();
                }
            } else {
                // Same as then clause with "right" and "left" exchanged
                TreeNode<V> y = x.getParent().getParent().getLeft();
                if ((y != null) && (y.red == RED)) {
                    // Case 1
                    x.getParent().setRed(BLACK);
                    y.setRed(BLACK);
                    x.getParent().getParent().setRed(RED);
                    x = x.getParent().getParent();
                    shouldPropagate = false;
                    propagateStart = x;
                } else {
                    if (x == x.getParent().getLeft()) {
                        // Case 2
                        x = x.getParent();
                        rightRotate(x);
                    }
                    // Case 3
                    x.getParent().setRed(BLACK);
                    x.getParent().getParent().setRed(RED);
                    shouldPropagate = leftRotate(x.getParent().getParent());
                    propagateStart = x.getParent();
                }
            }
        }

        while (shouldPropagate && (propagateStart != root)) {
            propagateStart = propagateStart.getParent();
            shouldPropagate = false;
        }

        root.setRed(BLACK);
    }

    public void deleteNode(TreeNode<V> z) {
        // This routine splices out a node. Note that we may not actually
        // delete the TreeNode<V> z from the tree, but may instead remove
        // another node from the tree, copying its contents into z.

        // y is the node to be unlinked from the tree
        TreeNode<V> y;
        if ((z.getLeft() == null) || (z.getRight() == null)) {
            y = z;
        } else {
            y = treeSuccessor(z);
        }
        // y is guaranteed to be non-null at this point
        TreeNode<V> x;
        if (y.getLeft() != null) {
            x = y.getLeft();
        } else {
            x = y.getRight();
        }
        // x is the potential child of y to replace it in the tree.
        // x may be null at this point
        TreeNode<V> xParent;
        if (x != null) {
            x.setParent(y.getParent());
            xParent = x.getParent();
        } else {
            xParent = y.getParent();
        }
        if (y.getParent() == null) {
            root = x;
        } else {
            if (y == y.getParent().getLeft()) {
                y.getParent().setLeft(x);
            } else {
                y.getParent().setRight(x);
            }
        }
        if (y != z) {
            z.val = y.val;
        }
        if (y.red == BLACK) {
            deleteFixup(x, xParent);
        }
    }

    // Internals only below this point

    // Vanilla binary search tree operations

    private void treeInsert(TreeNode<V> z) {
        TreeNode<V> y = null;
        TreeNode<V> x = root;

        while (x != null) {
            y = x;
            if (comparator.compare(z.val, x.val) < 0) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }
        z.setParent(y);
        if (y == null) {
            root = z;
        } else {
            if (comparator.compare(z.val, y.val) < 0) {
                y.setLeft(z);
            } else {
                y.setRight(z);
            }
        }
    }

    private TreeNode<V> treeSuccessor(TreeNode<V> x) {
        if (x.getRight() != null) {
            return treeMinimum(x.getRight());
        }
        TreeNode<V> y = x.getParent();
        while ((y != null) && (x == y.getRight())) {
            x = y;
            y = y.getParent();
        }
        return y;
    }

    private TreeNode<V> treeMinimum(TreeNode<V> x) {
        while (x.getLeft() != null) {
            x = x.getLeft();
        }
        return x;
    }

    // Insertion and deletion helpers

    private boolean leftRotate(TreeNode<V> x) {
        // Set y.
        TreeNode<V> y = x.getRight();
        // Turn y's left subtree into x's right subtree.
        x.setRight(y.getLeft());
        if (y.getLeft() != null) {
            y.getLeft().setParent(x);
        }
        // Link x's parent to y.
        y.setParent(x.getParent());
        if (x.getParent() == null) {
            root = y;
        } else {
            if (x == x.getParent().getLeft()) {
                x.getParent().setLeft(y);
            } else {
                x.getParent().setRight(y);
            }
        }
        // Put x on y's left.
        y.setLeft(x);
        x.setParent(y);
        // Update nodes in appropriate order (lowest to highest)
        return false;
    }

    private boolean rightRotate(TreeNode<V> y) {
        // Set x.
        TreeNode<V> x = y.getLeft();
        // Turn x's right subtree into y's left subtree.
        y.setLeft(x.getRight());
        if (x.getRight() != null) {
            x.getRight().setParent(y);
        }
        // Link y's parent into x.
        x.setParent(y.getParent());
        if (y.getParent() == null) {
            root = x;
        } else {
            if (y == y.getParent().getLeft()) {
                y.getParent().setLeft(x);
            } else {
                y.getParent().setRight(x);
            }
        }
        // Put y on x's right.
        x.setRight(y);
        y.setParent(x);
        // Update nodes in appropriate order (lowest to highest)
        return false;
    }

    private void deleteFixup(TreeNode<V> x, TreeNode<V> xParent) {
        while ((x != root) && ((x == null) || (x.red == BLACK))) {
            if (x == xParent.getLeft()) {
                // NOTE: the text points out that w can not be null. The
                // reason is not obvious from simply examining the code; it
                // comes about because of properties of the red-black tree.
                TreeNode<V> w = xParent.getRight();
                if (w.red == RED) {
                    // Case 1
                    w.setRed(BLACK);
                    xParent.setRed(RED);
                    leftRotate(xParent);
                    w = xParent.getRight();
                }
                if (((w.getLeft() == null) || (w.getLeft().red == BLACK)) &&
                        ((w.getRight() == null) || (w.getRight().red == BLACK))) {
                    // Case 2
                    w.setRed(RED);
                    x = xParent;
                    xParent = x.getParent();
                } else {
                    if ((w.getRight() == null) || (w.getRight().red == BLACK)) {
                        // Case 3
                        w.getLeft().setRed(BLACK);
                        w.setRed(RED);
                        rightRotate(w);
                        w = xParent.getRight();
                    }
                    // Case 4
                    w.setRed(xParent.red);
                    xParent.setRed(BLACK);
                    if (w.getRight() != null) {
                        w.getRight().setRed(BLACK);
                    }
                    leftRotate(xParent);
                    x = root;
                    xParent = x.getParent();
                }
            } else {
                // Same as clause above with "right" and "left"
                // exchanged

                // NOTE: the text points out that w can not be null. The
                // reason is not obvious from simply examining the code; it
                // comes about because of properties of the red-black tree.
                TreeNode<V> w = xParent.getLeft();
                if (w.red == RED) {
                    // Case 1
                    w.setRed(BLACK);
                    xParent.setRed(RED);
                    rightRotate(xParent);
                    w = xParent.getLeft();
                }
                if (((w.getRight() == null) || (w.getRight().red == BLACK)) &&
                        ((w.getLeft() == null) || (w.getLeft().red == BLACK))) {
                    // Case 2
                    w.setRed(RED);
                    x = xParent;
                    xParent = x.getParent();
                } else {
                    if ((w.getLeft() == null) || (w.getLeft().red == BLACK)) {
                        // Case 3
                        w.getRight().setRed(BLACK);
                        w.setRed(RED);
                        leftRotate(w);
                        w = xParent.getLeft();
                    }
                    // Case 4
                    w.setRed(xParent.red);
                    xParent.setRed(BLACK);
                    if (w.getLeft() != null) {
                        w.getLeft().setRed(BLACK);
                    }
                    rightRotate(xParent);
                    x = root;
                    xParent = x.getParent();
                }
            }
        }
        if (x != null) {
            x.setRed(BLACK);
        }
    }
}
