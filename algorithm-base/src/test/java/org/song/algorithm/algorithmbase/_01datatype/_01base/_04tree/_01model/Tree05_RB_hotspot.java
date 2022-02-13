package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;

/*
红黑树

根据JDK9 hotspot工具中的红黑树 源码修改

 */
public class Tree05_RB_hotspot<V extends Comparable<V>> {
    public RBNode root;
    private Comparator comparator;
    protected static final boolean DEBUGGING = true;
    protected static final boolean REALLY_VERBOSE = false;

    public Tree05_RB_hotspot(Comparator comparator) {
        this.comparator = comparator;
    }

    public void insertNode(RBNode x) {
        treeInsert(x);

        x.setRed(RBColor.RED);
        boolean shouldPropagate = x.update();

        if (DEBUGGING && REALLY_VERBOSE) {
            System.err.println("RBTree.insertNode");
        }

        RBNode propagateStart = x;

        // Loop invariant: x has been updated.
        while ((x != root) && (x.getParent().red == RBColor.RED)) {
            if (x.getParent() == x.getParent().getParent().getLeft()) {
                RBNode y = x.getParent().getParent().getRight();
                if ((y != null) && (y.red == RBColor.RED)) {
                    // Case 1
                    if (DEBUGGING && REALLY_VERBOSE) {
                        System.err.println("  Case 1/1");
                    }
                    x.getParent().setRed(RBColor.BLACK);
                    y.setRed(RBColor.BLACK);
                    x.getParent().getParent().setRed(RBColor.RED);
                    x.getParent().update();
                    x = x.getParent().getParent();
                    shouldPropagate = x.update();
                    propagateStart = x;
                } else {
                    if (x == x.getParent().getRight()) {
                        // Case 2
                        if (DEBUGGING && REALLY_VERBOSE) {
                            System.err.println("  Case 1/2");
                        }
                        x = x.getParent();
                        leftRotate(x);
                    }
                    // Case 3
                    if (DEBUGGING && REALLY_VERBOSE) {
                        System.err.println("  Case 1/3");
                    }
                    x.getParent().setRed(RBColor.BLACK);
                    x.getParent().getParent().setRed(RBColor.RED);
                    shouldPropagate = rightRotate(x.getParent().getParent());
                    propagateStart = x.getParent();
                }
            } else {
                // Same as then clause with "right" and "left" exchanged
                RBNode y = x.getParent().getParent().getLeft();
                if ((y != null) && (y.red == RBColor.RED)) {
                    // Case 1
                    if (DEBUGGING && REALLY_VERBOSE) {
                        System.err.println("  Case 2/1");
                    }
                    x.getParent().setRed(RBColor.BLACK);
                    y.setRed(RBColor.BLACK);
                    x.getParent().getParent().setRed(RBColor.RED);
                    x.getParent().update();
                    x = x.getParent().getParent();
                    shouldPropagate = x.update();
                    propagateStart = x;
                } else {
                    if (x == x.getParent().getLeft()) {
                        // Case 2
                        if (DEBUGGING && REALLY_VERBOSE) {
                            System.err.println("  Case 2/2");
                        }
                        x = x.getParent();
                        rightRotate(x);
                    }
                    // Case 3
                    if (DEBUGGING && REALLY_VERBOSE) {
                        System.err.println("  Case 2/3");
                    }
                    x.getParent().setRed(RBColor.BLACK);
                    x.getParent().getParent().setRed(RBColor.RED);
                    shouldPropagate = leftRotate(x.getParent().getParent());
                    propagateStart = x.getParent();
                }
            }
        }

        while (shouldPropagate && (propagateStart != root)) {
            if (DEBUGGING && REALLY_VERBOSE) {
                System.err.println("  Propagating");
            }
            propagateStart = propagateStart.getParent();
            shouldPropagate = propagateStart.update();
        }

        root.setRed(RBColor.BLACK);
    }

    public void deleteNode(RBNode z) {
        // This routine splices out a node. Note that we may not actually
        // delete the RBNode z from the tree, but may instead remove
        // another node from the tree, copying its contents into z.

        // y is the node to be unlinked from the tree
        RBNode y;
        if ((z.getLeft() == null) || (z.getRight() == null)) {
            y = z;
        } else {
            y = treeSuccessor(z);
        }
        // y is guaranteed to be non-null at this point
        RBNode x;
        if (y.getLeft() != null) {
            x = y.getLeft();
        } else {
            x = y.getRight();
        }
        // x is the potential child of y to replace it in the tree.
        // x may be null at this point
        RBNode xParent;
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
            z.data = y.data;
        }
        if (y.red == RBColor.BLACK) {
            deleteFixup(x, xParent);
        }
    }

    // Functionality overridable by subclass

    protected Object getNodeValue(RBNode node) {
        return node.getData();
    }

    // Internals only below this point

    // Vanilla binary search tree operations

    private void treeInsert(RBNode z) {
        RBNode y = null;
        RBNode x = root;

        while (x != null) {
            y = x;
            if (comparator.compare(getNodeValue(z), getNodeValue(x)) < 0) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }
        z.setParent(y);
        if (y == null) {
            root = z;
        } else {
            if (comparator.compare(getNodeValue(z), getNodeValue(y)) < 0) {
                y.setLeft(z);
            } else {
                y.setRight(z);
            }
        }
    }

    private RBNode treeSuccessor(RBNode x) {
        if (x.getRight() != null) {
            return treeMinimum(x.getRight());
        }
        RBNode y = x.getParent();
        while ((y != null) && (x == y.getRight())) {
            x = y;
            y = y.getParent();
        }
        return y;
    }

    private RBNode treeMinimum(RBNode x) {
        while (x.getLeft() != null) {
            x = x.getLeft();
        }
        return x;
    }

    // Insertion and deletion helpers

    private boolean leftRotate(RBNode x) {
        // Set y.
        RBNode y = x.getRight();
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
        boolean res = x.update();
        res = y.update() || res;
        return res;
    }

    private boolean rightRotate(RBNode y) {
        // Set x.
        RBNode x = y.getLeft();
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
        boolean res = y.update();
        res = x.update() || res;
        return res;
    }

    private void deleteFixup(RBNode x, RBNode xParent) {
        while ((x != root) && ((x == null) || (x.red == RBColor.BLACK))) {
            if (x == xParent.getLeft()) {
                // NOTE: the text points out that w can not be null. The
                // reason is not obvious from simply examining the code; it
                // comes about because of properties of the red-black tree.
                RBNode w = xParent.getRight();
                if (DEBUGGING) {
                    if (w == null) {
                        throw new RuntimeException("x's sibling should not be null");
                    }
                }
                if (w.red == RBColor.RED) {
                    // Case 1
                    w.setRed(RBColor.BLACK);
                    xParent.setRed(RBColor.RED);
                    leftRotate(xParent);
                    w = xParent.getRight();
                }
                if (((w.getLeft() == null) || (w.getLeft().red == RBColor.BLACK)) &&
                        ((w.getRight() == null) || (w.getRight().red == RBColor.BLACK))) {
                    // Case 2
                    w.setRed(RBColor.RED);
                    x = xParent;
                    xParent = x.getParent();
                } else {
                    if ((w.getRight() == null) || (w.getRight().red == RBColor.BLACK)) {
                        // Case 3
                        w.getLeft().setRed(RBColor.BLACK);
                        w.setRed(RBColor.RED);
                        rightRotate(w);
                        w = xParent.getRight();
                    }
                    // Case 4
                    w.setRed(xParent.red);
                    xParent.setRed(RBColor.BLACK);
                    if (w.getRight() != null) {
                        w.getRight().setRed(RBColor.BLACK);
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
                RBNode w = xParent.getLeft();
                if (DEBUGGING) {
                    if (w == null) {
                        throw new RuntimeException("x's sibling should not be null");
                    }
                }
                if (w.red == RBColor.RED) {
                    // Case 1
                    w.setRed(RBColor.BLACK);
                    xParent.setRed(RBColor.RED);
                    rightRotate(xParent);
                    w = xParent.getLeft();
                }
                if (((w.getRight() == null) || (w.getRight().red == RBColor.BLACK)) &&
                        ((w.getLeft() == null) || (w.getLeft().red == RBColor.BLACK))) {
                    // Case 2
                    w.setRed(RBColor.RED);
                    x = xParent;
                    xParent = x.getParent();
                } else {
                    if ((w.getLeft() == null) || (w.getLeft().red == RBColor.BLACK)) {
                        // Case 3
                        w.getRight().setRed(RBColor.BLACK);
                        w.setRed(RBColor.RED);
                        leftRotate(w);
                        w = xParent.getLeft();
                    }
                    // Case 4
                    w.setRed(xParent.red);
                    xParent.setRed(RBColor.BLACK);
                    if (w.getLeft() != null) {
                        w.getLeft().setRed(RBColor.BLACK);
                    }
                    rightRotate(xParent);
                    x = root;
                    xParent = x.getParent();
                }
            }
        }
        if (x != null) {
            x.setRed(RBColor.BLACK);
        }
    }

    @Data
    @AllArgsConstructor
    public static class RBColor {
        public static final boolean RED = true;
        public static final boolean BLACK = false;
    }

    @Data
    public static class RBNode {
        Object data;
        RBNode left;
        RBNode right;
        RBNode parent;
        boolean red;

        public RBNode(Object data) {
            this.data = data;
            red = RBColor.RED;
        }

        public Object getData() {
            return data;
        }

        public boolean update() {
            return false;
        }
    }
}
