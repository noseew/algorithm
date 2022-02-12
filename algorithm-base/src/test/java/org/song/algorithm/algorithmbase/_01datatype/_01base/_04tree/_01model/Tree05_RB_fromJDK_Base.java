package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树

根据JDK源码修改

 */
public class Tree05_RB_fromJDK_Base<V extends Comparable<V>> extends Tree03_AVL_base<V>  {

    public TreeNode<V> root;

    public Tree05_RB_fromJDK_Base(Comparator<V> comparator) {
        super(comparator);
    }

    public TreeNode<V> get(V k, TreeNode<V> parent) {
        TreeNode<V> p = parent;
        do {
            TreeNode<V> pleft = p.left, pright = p.right, q;
            if (p.val.compareTo(k) > 0)
                p = pleft;
            else if (p.val.compareTo(k) < 0)
                p = pright;
            else if (p.val.compareTo(k) == 0)
                return p;
            else if ((q = get(k, p)) != null)
                return q;
            else
                p = pleft;
        } while (p != null);
        return null;
    }
    public boolean push(V k) {
        put(k);
        root.color = false;
        return true;
    }

    public TreeNode<V> put(V k) {
        if (root == null) {
            root = new TreeNode<>(k, true);
        }
        
        for (TreeNode<V> p = root; ; ) {
            if (p.val.compareTo(k) == 0) {
                return p;
            }
            int dir = k.compareTo(p.val);

            TreeNode<V> xp = p;
            if ((p = (dir <= 0) ? p.left : p.right) == null) {
                // 新建 树节点
                TreeNode<V> x = new TreeNode<>(k, true);
                x.parent = xp;
                if (dir <= 0)
                    xp.left = x;
                else
                    xp.right = x;
                balanceInsertion(root, x);
                return null;
            }
        }
    }

    public void remove(TreeNode<V> p) {
        TreeNode<V> pleft = p.left, pright = p.right, replacement;
        if (pleft != null && pright != null) {
            TreeNode<V> s = pright, sl;
            while ((sl = s.left) != null) // find successor
                s = sl;
            boolean c = s.color;
            s.color = p.color;
            p.color = c; // swap colors
            TreeNode<V> sr = s.right;
            TreeNode<V> pp = p.parent;
            if (s == pright) { // p was s's direct parent
                p.parent = s;
                s.right = p;
            } else {
                TreeNode<V> sp = s.parent;
                if ((p.parent = sp) != null) {
                    if (s == sp.left)
                        sp.left = p;
                    else
                        sp.right = p;
                }
                if ((s.right = pright) != null)
                    pright.parent = s;
            }
            p.left = null;
            if ((p.right = sr) != null)
                sr.parent = p;
            if ((s.left = pleft) != null)
                pleft.parent = s;
            if ((s.parent = pp) == null)
                root = s;
            else if (p == pp.left)
                pp.left = s;
            else
                pp.right = s;
            if (sr != null)
                replacement = sr;
            else
                replacement = p;
        } else if (pleft != null)
            replacement = pleft;
        else if (pright != null)
            replacement = pright;
        else
            replacement = p;
        if (replacement != p) {
            TreeNode<V> pp = replacement.parent = p.parent;
            if (pp == null)
                root = replacement;
            else if (p == pp.left)
                pp.left = replacement;
            else
                pp.right = replacement;
            p.left = p.right = p.parent = null;
        }

        if (!p.color) balanceDeletion(root, replacement);

        if (replacement == p) {  // detach
            TreeNode<V> pp = p.parent;
            p.parent = null;
            if (pp != null) {
                if (p == pp.left)
                    pp.left = null;
                else if (p == pp.right)
                    pp.right = null;
            }
        }
    }

    /**
     * 插入平衡
     * 
     * @param root
     * @param x
     * @return
     */
    private TreeNode<V> balanceInsertion(TreeNode<V> root, TreeNode<V> x) {
        for (TreeNode<V> xp, xpp, xppl, xppr; ; ) {
            if ((xp = x.parent) == null) {
                x.color = false;
                return x;
            } else if (!xp.color || (xpp = xp.parent) == null)
                return root;
            if (xp == (xppl = xpp.left)) {
                if ((xppr = xpp.right) != null && xppr.color) {
                    xppr.color = false;
                    xp.color = false;
                    xpp.color = true;
                    x = xpp;
                } else {
                    if (x == xp.right) {
                        root = rotateLeft(root, x = xp);
                        xpp = (xp = x.parent) == null ? null : xp.parent;
                    }
                    if (xp != null) {
                        xp.color = false;
                        if (xpp != null) {
                            xpp.color = true;
                            root = rotateRight(root, xpp);
                        }
                    }
                }
            } else {
                if (xppl != null && xppl.color) {
                    xppl.color = false;
                    xp.color = false;
                    xpp.color = true;
                    x = xpp;
                } else {
                    if (x == xp.left) {
                        root = rotateRight(root, x = xp);
                        xpp = (xp = x.parent) == null ? null : xp.parent;
                    }
                    if (xp != null) {
                        xp.color = false;
                        if (xpp != null) {
                            xpp.color = true;
                            root = rotateLeft(root, xpp);
                        }
                    }
                }
            }
        }
    }

    /**
     * 插入删除
     * 
     * @param root
     * @param x
     * @return
     */
    private TreeNode<V> balanceDeletion(TreeNode<V> root, TreeNode<V> x) {
        for (TreeNode<V> xp, xpl, xpr; ; ) {
            if (x == null || x == root)
                return root;
            else if ((xp = x.parent) == null) {
                x.color = false;
                return x;
            } else if (x.color) {
                x.color = false;
                return root;
            } else if ((xpl = xp.left) == x) {
                if ((xpr = xp.right) != null && xpr.color) {
                    xpr.color = false;
                    xp.color = true;
                    root = rotateLeft(root, xp);
                    xpr = (xp = x.parent) == null ? null : xp.right;
                }
                if (xpr == null)
                    x = xp;
                else {
                    TreeNode<V> sl = xpr.left, sr = xpr.right;
                    if ((sr == null || !sr.color) &&
                            (sl == null || !sl.color)) {
                        xpr.color = true;
                        x = xp;
                    } else {
                        if (sr == null || !sr.color) {
                            if (sl != null)
                                sl.color = false;
                            xpr.color = true;
                            root = rotateRight(root, xpr);
                            xpr = (xp = x.parent) == null ?
                                    null : xp.right;
                        }
                        if (xpr != null) {
                            xpr.color = (xp == null) ? false : xp.color;
                            if ((sr = xpr.right) != null)
                                sr.color = false;
                        }
                        if (xp != null) {
                            xp.color = false;
                            root = rotateLeft(root, xp);
                        }
                        x = root;
                    }
                }
            } else { // symmetric
                if (xpl != null && xpl.color) {
                    xpl.color = false;
                    xp.color = true;
                    root = rotateRight(root, xp);
                    xpl = (xp = x.parent) == null ? null : xp.left;
                }
                if (xpl == null)
                    x = xp;
                else {
                    TreeNode<V> sl = xpl.left, sr = xpl.right;
                    if ((sl == null || !sl.color) &&
                            (sr == null || !sr.color)) {
                        xpl.color = true;
                        x = xp;
                    } else {
                        if (sl == null || !sl.color) {
                            if (sr != null)
                                sr.color = false;
                            xpl.color = true;
                            root = rotateLeft(root, xpl);
                            xpl = (xp = x.parent) == null ?
                                    null : xp.left;
                        }
                        if (xpl != null) {
                            xpl.color = (xp == null) ? false : xp.color;
                            if ((sl = xpl.left) != null)
                                sl.color = false;
                        }
                        if (xp != null) {
                            xp.color = false;
                            root = rotateRight(root, xp);
                        }
                        x = root;
                    }
                }
            }
        }
    }

    /**
     * 左旋
     * 
     * @param root
     * @param p
     * @return
     */
    private TreeNode<V> rotateLeft(TreeNode<V> root, TreeNode<V> p) {
        TreeNode<V> r, pp, rl;
        if (p != null && (r = p.right) != null) {
            if ((rl = p.right = r.left) != null)
                rl.parent = p;
            if ((pp = r.parent = p.parent) == null)
                (root = r).color = false;
            else if (pp.left == p)
                pp.left = r;
            else
                pp.right = r;
            r.left = p;
            p.parent = r;
        }
        return root;
    }

    /**
     * 右旋
     * 
     * @param root
     * @param p
     * @return
     */
    private TreeNode<V> rotateRight(TreeNode<V> root, TreeNode<V> p) {
        TreeNode<V> l, pp, lr;
        if (p != null && (l = p.left) != null) {
            if ((lr = p.left = l.right) != null)
                lr.parent = p;
            if ((pp = l.parent = p.parent) == null)
                (root = l).color = false;
            else if (pp.right == p)
                pp.right = l;
            else
                pp.left = l;
            l.right = p;
            p.parent = l;
        }
        return root;
    }

}
