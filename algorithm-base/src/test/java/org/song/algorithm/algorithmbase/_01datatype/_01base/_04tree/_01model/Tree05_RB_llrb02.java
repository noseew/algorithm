package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/**
 * 代码来自网络
 * 
 * @param <V>
 */
public class Tree05_RB_llrb02<V extends Comparable<V>> extends Tree05_RB_abs<V> {

    public Tree05_RB_llrb02(Comparator<V> comparator) {
        super(comparator);
    }

    public boolean add(V v) {
        if (root == null) {
            root = new TreeNode(null, v, BLACK);
            size++;
            return true;
        }
        TreeNode<V> x = root;
        while (x != null) {
            int cmp = v.compareTo(x.val);
            if (cmp < 0) {
                if (x.left == null) {
                    x.left = new TreeNode(x, v, RED);
                    while (x != null) x = rotate(x).parent;
                    size++;
                } else
                    x = x.left;
            } else if (cmp > 0) {
                if (x.right == null) {
                    x.right = new TreeNode(x, v, RED);
                    while (x != null) x = rotate(x).parent;
                    size++;
                } else
                    x = x.right;
            } else {
                x.val = v;
                break;
            }
        }
        return true;
    }

    public void delete(V k) {
        if (!isRed(root.left) && !isRed(root.right))
            root.red = RED;
        root = delete(root, k);
        if (root != null) {
            size--;
            root.red = BLACK;
        }
    }

    public V removeMin() {
        if (size == 0)
            return null;
        removeMin(root);
        if (root != null) {
            size--;
            root.red = BLACK;
        }
        return null;
    }

    public V removeMax() {
        if (size == 0)
            return null;
        removeMax(root);
        if (root != null) {
            size--;
            root.red = BLACK;
        }
        return null;
    }

    //---------- PRIVATE METHOD AND CLASS----------//

    private TreeNode<V> flipColors(TreeNode<V> h) {
        h.red = RED;
        h.left.red = BLACK;
        h.right.red = BLACK;
        return h;
    }

    private TreeNode<V> rotateLeft(TreeNode h) {
        TreeNode<V> x = h.right;
        if (h.parent != null) {
            if (h.parent.left == h) h.parent.left = x;
            else h.parent.right = x;
        }
        x.parent = h.parent;
        h.parent = x;
        h.right = x.left;
        if (x.left != null)
            x.left.parent = h;
        x.left = h;
        x.red = h.red;
        h.red = RED;
        root = (root == x.left) ? x : root;
        root.red = BLACK;
        return x;
    }

    private TreeNode<V> rotateRight(TreeNode<V> h) {
        TreeNode<V> x = h.left;
        if (h.parent != null) {
            if (h.parent.left == h) h.parent.left = x;
            else h.parent.right = x;
        }
        x.parent = h.parent;
        h.parent = x;
        h.left = x.right;
        if (x.right != null)
            x.right.parent = h;
        x.right = h;
        x.red = h.red;
        h.red = RED;
        root = (root == x.right) ? x : root;
        root.red = BLACK;
        return x;
    }

    private TreeNode<V> rotate(TreeNode<V> x) {
        if (isRed(x.right) && !isRed(x.left))
            return rotateLeft(x);
        if (isRed(x.left) && isRed(leftOf(x.left)))
            return flipColors(rotateRight(x));
        if (isRed(x.left) && isRed(x.right))
            flipColors(x);
        return x;
    }

    private TreeNode<V> max(TreeNode<V> x) {
        if (x == null)
            return null;
        while (x.right != null)
            x = x.right;
        return x;
    }

    protected TreeNode<V> getFloorNode(TreeNode<V> x, V k) {
        while (x != null) {
            int cmp = x.val.compareTo(k);
            if (cmp == 0) return x;
            else if (cmp > 0) x = x.left;
            else {
                if (x.right == null)
                    return x;
                TreeNode<V> t = getMinNode(x.right);
                int c = t.val.compareTo(k);
                if (c > 0) return x;
                else if (c == 0) return t;
                else x = x.right;
            }
        }
        return null;
    }

    protected TreeNode<V> getCeilingNode(TreeNode<V> x, V k) {
        while (x != null) {
            int cmp = x.val.compareTo(k);
            if (cmp == 0) return x;
            else if (cmp < 0) x = x.right;
            else {
                if (x.left == null)
                    return x;
                TreeNode<V> t = max(x.left);
                int c = t.val.compareTo(k);
                if (c < 0) return x;
                else if (c == 0) return t;
                else x = x.left;
            }
        }
        return null;
    }

    protected TreeNode<V> removeMin(TreeNode<V> h) {
        if (h.left == null)
            return null;
        if (!isRed(h.left) && !isRed(leftOf(h.left)))
            h = moveRedLeft(h);
        h.left = removeMin(h.left);
        return balance(h);
    }

    protected TreeNode<V> removeMax(TreeNode<V> h) {
        if (isRed(h.left))
            h = rotateRight(h);
        if (h.right == null)
            return null;
        if (!isRed(h.right) && !isRed(leftOf(h.right)))
            h = moveRedRight(h);
        h.right = removeMax(h.right);
        return balance(h);
    }

    private TreeNode<V> delete(TreeNode<V> h, V k) {
        if (k.compareTo(h.val) < 0) {
            if (isRed(h.left) && !isRed(leftOf(h.left)))
                h = moveRedLeft(h);
            h.left = delete(h.left, k);
        } else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (k.compareTo(h.val) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(leftOf(h.right)))
                h = moveRedRight(h);
            if (k.compareTo(h.val) == 0) {
                h.val = search_recursive(h.right, getMinNode(h.right).val).val;
                h.val = getMinNode(h.right).val;
                h.right = removeMin(h.right);
            } else {
                h.right = delete(h.right, k);
            }
        }
        return balance(h);
    }

    private void moveFlipColors(TreeNode<V> h) {
        h.red = BLACK;
        if (h.left != null) h.left.red = RED;
        if (h.right != null) h.right.red = RED;
    }

    private TreeNode<V> moveRedLeft(TreeNode<V> h) {
        moveFlipColors(h);
        if (isRed(leftOf(h.right))) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            moveFlipColors(h);
        }
        return h;
    }

    private TreeNode<V> moveRedRight(TreeNode<V> h) {
        moveFlipColors(h);
        if (isRed(leftOf(h.left))) {
            h = rotateRight(h);
            moveFlipColors(h);
        }
        return h;
    }

    private TreeNode<V> balance(TreeNode<V> h) {
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(leftOf(h.left))) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    private TreeNode<V> leftOf(TreeNode<V> x) {
        return x == null ? null : x.left;
    }
}
