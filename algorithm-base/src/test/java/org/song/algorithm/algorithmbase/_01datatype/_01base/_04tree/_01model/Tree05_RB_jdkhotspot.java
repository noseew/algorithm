package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树

根据JDK9 hotspot工具中的红黑树 源码修改

 */
public class Tree05_RB_jdkhotspot<V extends Comparable<V>> extends Tree05_RB_abs<V> {

    public Tree05_RB_jdkhotspot(Comparator<V> comparator) {
        super(comparator);
    }

    public void put(V v) {
        insertNode(new TreeNode<>(v, true));
    }

    public void insertNode(TreeNode<V> x) {
        treeInsert(x);
        x.red = RED;

        // Loop invariant: x has been updated.
        while ((x != root) && (x.parent.red == RED)) {
            if (x.parent == x.parent.parent.left) {
                TreeNode<V> y = x.parent.parent.right;
                if ((y != null) && (y.red == RED)) {
                    // Case 1
                    x.parent.red = BLACK;
                    y.red = BLACK;
                    x.parent.parent.red = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        // Case 2
                        x = x.parent;
                        leftRotate(x);
                    }
                    // Case 3
                    x.parent.red = BLACK;
                    x.parent.parent.red = RED;
                    rightRotate(x.parent.parent);
                }
            } else {
                // Same as then clause with "right" and "left" exchanged
                TreeNode<V> y = x.parent.parent.left;
                if ((y != null) && (y.red == RED)) {
                    // Case 1
                    x.parent.red = BLACK;
                    y.red = BLACK;
                    x.parent.parent.red = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) {
                        // Case 2
                        x = x.parent;
                        rightRotate(x);
                    }
                    // Case 3
                    x.parent.red = BLACK;
                    x.parent.parent.red = RED;
                    leftRotate(x.parent.parent);
                }
            }
        }

        root.red = BLACK;
    }

    public void deleteNode(TreeNode<V> z) {
        // This routine splices out a node. Note that we may not actually
        // delete the TreeNode<V> z from the tree, but may instead remove
        // another node from the tree, copying its contents into z.

        // y is the node to be unlinked from the tree
        TreeNode<V> y;
        if ((z.left == null) || (z.right == null)) {
            y = z;
        } else {
            y = treeSuccessor(z);
        }
        // y is guaranteed to be non-null at this point
        TreeNode<V> x;
        if (y.left != null) {
            x = y.left;
        } else {
            x = y.right;
        }
        // x is the potential child of y to replace it in the tree.
        // x may be null at this point
        TreeNode<V> xParent;
        if (x != null) {
            x.parent = y.parent;
            xParent = x.parent;
        } else {
            xParent = y.parent;
        }
        if (y.parent == null) {
            root = x;
        } else {
            if (y == y.parent.left) {
                y.parent.left = x;
            } else {
                y.parent.right = x;
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
                x = x.left;
            } else {
                x = x.right;
            }
        }
        z.parent = y;
        if (y == null) {
            root = z;
        } else {
            if (comparator.compare(z.val, y.val) < 0) {
                y.left = z;
            } else {
                y.right = z;
            }
        }
    }

    private TreeNode<V> treeSuccessor(TreeNode<V> x) {
        if (x.right != null) {
            return treeMinimum(x.right);
        }
        TreeNode<V> y = x.parent;
        while ((y != null) && (x == y.right)) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    private TreeNode<V> treeMinimum(TreeNode<V> x) {
        while (x.left != null) {
            x = x.left;
        }
        return x;
    }

    // Insertion and deletion helpers

    protected TreeNode<V> leftRotate(TreeNode<V> x) {
        // Set y.
        TreeNode<V> y = x.right;
        // Turn y's left subtree into x's right subtree.
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        // Link x's parent to y.
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else {
            if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
        }
        // Put x on y's left.
        y.left = x;
        x.parent = y;
        // Update nodes in appropriate order (lowest to highest)
        return y;
    }

    protected TreeNode<V> rightRotate(TreeNode<V> y) {
        // Set x.
        TreeNode<V> x = y.left;
        // Turn x's right subtree into y's left subtree.
        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }
        // Link y's parent into x.
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else {
            if (y == y.parent.left) {
                y.parent.left = x;
            } else {
                y.parent.right = x;
            }
        }
        // Put y on x's right.
        x.right = y;
        y.parent = x;
        // Update nodes in appropriate order (lowest to highest)
        return x;
    }

    private void deleteFixup(TreeNode<V> x, TreeNode<V> xParent) {
        while ((x != root) && ((x == null) || (x.red == BLACK))) {
            if (x == xParent.left) {
                // NOTE: the text points out that w can not be null. The
                // reason is not obvious from simply examining the code; it
                // comes about because of properties of the red-black tree.
                TreeNode<V> w = xParent.right;
                if (w.red == RED) {
                    // Case 1
                    w.red = BLACK;
                    xParent.red = RED;
                    leftRotate(xParent);
                    w = xParent.right;
                }
                if (((w.left == null) || (w.left.red == BLACK)) &&
                        ((w.right == null) || (w.right.red == BLACK))) {
                    // Case 2
                    w.red = RED;
                    x = xParent;
                    xParent = x.parent;
                } else {
                    if ((w.right == null) || (w.right.red == BLACK)) {
                        // Case 3
                        w.left.red = BLACK;
                        w.red = RED;
                        rightRotate(w);
                        w = xParent.right;
                    }
                    // Case 4
                    w.red = xParent.red;
                    xParent.red = BLACK;
                    if (w.right != null) {
                        w.right.red = BLACK;
                    }
                    leftRotate(xParent);
                    x = root;
                    xParent = x.parent;
                }
            } else {
                // Same as clause above with "right" and "left"
                // exchanged

                // NOTE: the text points out that w can not be null. The
                // reason is not obvious from simply examining the code; it
                // comes about because of properties of the red-black tree.
                TreeNode<V> w = xParent.left;
                if (w.red == RED) {
                    // Case 1
                    w.red = BLACK;
                    xParent.red = RED;
                    rightRotate(xParent);
                    w = xParent.left;
                }
                if (((w.right == null) || (w.right.red == BLACK)) &&
                        ((w.left == null) || (w.left.red == BLACK))) {
                    // Case 2
                    w.red = RED;
                    x = xParent;
                    xParent = x.parent;
                } else {
                    if ((w.left == null) || (w.left.red == BLACK)) {
                        // Case 3
                        w.right.red = BLACK;
                        w.red = RED;
                        leftRotate(w);
                        w = xParent.left;
                    }
                    // Case 4
                    w.red = xParent.red;
                    xParent.red = BLACK;
                    if (w.left != null) {
                        w.left.red = BLACK;
                    }
                    rightRotate(xParent);
                    x = root;
                    xParent = x.parent;
                }
            }
        }
        if (x != null) {
            x.red = BLACK;
        }
    }
}
