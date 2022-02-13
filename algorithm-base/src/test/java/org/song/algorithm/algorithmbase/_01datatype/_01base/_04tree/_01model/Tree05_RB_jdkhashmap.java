package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树

根据JDK8 HashMap中的红黑树 源码修改

 */
public class Tree05_RB_jdkhashmap<V extends Comparable<V>> extends Tree03_AVL_base<V>  {

    public Tree05_RB_jdkhashmap(Comparator<V> comparator) {
        super(comparator);
    }

    public TreeNode<V> get(V v, TreeNode<V> parent) {
        do {
            TreeNode<V> pLeft = parent.left, pRight = parent.right, q;
            if (parent.val.compareTo(v) > 0)
                parent = pLeft;
            else if (parent.val.compareTo(v) < 0)
                parent = pRight;
            else if (parent.val.compareTo(v) == 0)
                return parent;
            else if ((q = get(v, parent)) != null)
                return q;
            else
                parent = pLeft;
        } while (parent != null);
        return null;
    }

    @Override
    public boolean push(V v) {
        int oSize = size;
        insert_traverse(root, v);
        root.red = false;
        return size > oSize;
    }

    @Override
    protected TreeNode<V> insert_traverse(TreeNode<V> parent, V v) {
        if (parent == null) {
            root = new TreeNode<>(v, true);
            size++;
            return root;
        }
        
        for (TreeNode<V> p = parent; ; ) {
            if (p.val.compareTo(v) == 0) {
                return p;
            }
            int com = v.compareTo(p.val);

            TreeNode<V> xp = p;
            if ((p = (com <= 0) ? p.left : p.right) == null) {
                // 新建 树节点
                TreeNode<V> x = new TreeNode<>(v, true);
                size++;
                x.parent = xp;
                if (com <= 0) {
                    xp.left = x;
                } else {
                    xp.right = x;
                }
                balanceInsertion(x);
                return null;
            }
        }
    }

    /**
     * TODO 未完成
     * 
     * @param v
     * @return
     */
    @Override
    public V remove(V v) {
        remove(get(v, root));
        return null;
    }

    private void remove(TreeNode<V> p) {
        if (p == null) {
            return;
        }
        TreeNode<V> pLeft = p.left, pRight = p.right, replacement;
        if (pLeft != null && pRight != null) {
            TreeNode<V> s = pRight, sLeft;
            while ((sLeft = s.left) != null) { // find successor
                s = sLeft;
            }
            boolean red = s.red;
            s.red = p.red;
            p.red = red; // swap colors
            TreeNode<V> sRight = s.right;
            TreeNode<V> pParent = p.parent;
            if (s == pRight) { // p was s's direct parent
                p.parent = s;
                s.right = p;
            } else {
                TreeNode<V> sParent = s.parent;
                if ((p.parent = sParent) != null) {
                    if (s == sParent.left) {
                        sParent.left = p;
                    } else {
                        sParent.right = p;
                    }
                }
                if ((s.right = pRight) != null)
                    pRight.parent = s;
            }
            p.left = null;
            if ((p.right = sRight) != null) {
                sRight.parent = p;
            }
            if ((s.left = pLeft) != null) {
                pLeft.parent = s;
            }
            if ((s.parent = pParent) == null) {
                root = s;
            } else if (p == pParent.left) {
                pParent.left = s;
            } else {
                pParent.right = s;
            }
            if (sRight != null) {
                replacement = sRight;
            } else {
                replacement = p;
            }
        } else if (pLeft != null) {
            replacement = pLeft;
        } else if (pRight != null) {
            replacement = pRight;
        } else {
            replacement = p;
        }
        if (replacement != p) {
            TreeNode<V> pp = replacement.parent = p.parent;
            if (pp == null) {
                root = replacement;
            } else if (p == pp.left) {
                pp.left = replacement;
            } else {
                pp.right = replacement;
            }
            p.left = p.right = p.parent = null;
        }

        if (!p.red) balanceDeletion(replacement);

        if (replacement == p) {  // detach
            TreeNode<V> pp = p.parent;
            p.parent = null;
            if (pp != null) {
                if (p == pp.left) {
                    pp.left = null;
                } else if (p == pp.right)
                    pp.right = null;
            }
        }
    }

    /**
     * 插入平衡
     *
     * @param x
     * @return
     */
    @Override
    protected TreeNode<V> balanceInsertion(TreeNode<V> x) {
        for (TreeNode<V> xp, xpp, xppl, xppr; ; ) {
            if ((xp = x.parent) == null) {
                x.red = false;
                return x;
            } else if (!xp.red || (xpp = xp.parent) == null)
                return root;
            if (xp == (xppl = xpp.left)) {
                if ((xppr = xpp.right) != null && xppr.red) {
                    xppr.red = false;
                    xp.red = false;
                    xpp.red = true;
                    x = xpp;
                } else {
                    if (x == xp.right) {
                        root = rotateLeft(x = xp);
                        xpp = (xp = x.parent) == null ? null : xp.parent;
                    }
                    if (xp != null) {
                        xp.red = false;
                        if (xpp != null) {
                            xpp.red = true;
                            root = rotateRight(xpp);
                        }
                    }
                }
            } else {
                if (xppl != null && xppl.red) {
                    xppl.red = false;
                    xp.red = false;
                    xpp.red = true;
                    x = xpp;
                } else {
                    if (x == xp.left) {
                        root = rotateRight(x = xp);
                        xpp = (xp = x.parent) == null ? null : xp.parent;
                    }
                    if (xp != null) {
                        xp.red = false;
                        if (xpp != null) {
                            xpp.red = true;
                            root = rotateLeft(xpp);
                        }
                    }
                }
            }
        }
    }

    /**
     * 删除平衡
     *
     * @param x
     * @return
     */
    @Override
    protected TreeNode<V> balanceDeletion(TreeNode<V> x) {
        for (TreeNode<V> xp, xpl, xpr; ; ) {
            if (x == null || x == root)
                return root;
            else if ((xp = x.parent) == null) {
                x.red = false;
                return x;
            } else if (x.red) {
                x.red = false;
                return root;
            } else if ((xpl = xp.left) == x) {
                if ((xpr = xp.right) != null && xpr.red) {
                    xpr.red = false;
                    xp.red = true;
                    root = rotateLeft(xp);
                    xpr = (xp = x.parent) == null ? null : xp.right;
                }
                if (xpr == null)
                    x = xp;
                else {
                    TreeNode<V> sl = xpr.left, sr = xpr.right;
                    if ((sr == null || !sr.red) &&
                            (sl == null || !sl.red)) {
                        xpr.red = true;
                        x = xp;
                    } else {
                        if (sr == null || !sr.red) {
                            if (sl != null)
                                sl.red = false;
                            xpr.red = true;
                            root = rotateRight(xpr);
                            xpr = (xp = x.parent) == null ?
                                    null : xp.right;
                        }
                        if (xpr != null) {
                            xpr.red = (xp == null) ? false : xp.red;
                            if ((sr = xpr.right) != null)
                                sr.red = false;
                        }
                        if (xp != null) {
                            xp.red = false;
                            root = rotateLeft(xp);
                        }
                        x = root;
                    }
                }
            } else { // symmetric
                if (xpl != null && xpl.red) {
                    xpl.red = false;
                    xp.red = true;
                    root = rotateRight(xp);
                    xpl = (xp = x.parent) == null ? null : xp.left;
                }
                if (xpl == null)
                    x = xp;
                else {
                    TreeNode<V> sl = xpl.left, sr = xpl.right;
                    if ((sl == null || !sl.red) &&
                            (sr == null || !sr.red)) {
                        xpl.red = true;
                        x = xp;
                    } else {
                        if (sl == null || !sl.red) {
                            if (sr != null)
                                sr.red = false;
                            xpl.red = true;
                            root = rotateLeft(xpl);
                            xpl = (xp = x.parent) == null ?
                                    null : xp.left;
                        }
                        if (xpl != null) {
                            xpl.red = (xp == null) ? false : xp.red;
                            if ((sl = xpl.left) != null)
                                sl.red = false;
                        }
                        if (xp != null) {
                            xp.red = false;
                            root = rotateRight(xp);
                        }
                        x = root;
                    }
                }
            }
        }
    }

    @Override
    protected TreeNode<V> rotateLeft(TreeNode<V> p) {
        TreeNode<V> r, pp, rl;
        if (p != null && (r = p.right) != null) {
            if ((rl = p.right = r.left) != null)
                rl.parent = p;
            if ((pp = r.parent = p.parent) == null)
                (root = r).red = false;
            else if (pp.left == p)
                pp.left = r;
            else
                pp.right = r;
            r.left = p;
            p.parent = r;
        }
        return root;
    }

    @Override
    protected TreeNode<V> rotateRight(TreeNode<V> p) {
        TreeNode<V> l, pp, lr;
        if (p != null && (l = p.left) != null) {
            if ((lr = p.left = l.right) != null)
                lr.parent = p;
            if ((pp = l.parent = p.parent) == null)
                (root = l).red = false;
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
