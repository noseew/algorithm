package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree.printer.BTreeUtils;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/*
一棵二叉查找树(BST)是一棵二叉树, 
其中每个结点都含有一个Comparable的键(以及相关联的值)且每个结点的键都大于其左子树中的任意结点的键而小于右子树的任意结点的键
没有平衡功能

node 节点没有使用parent指针
 */
public class Tree02BST01<V extends Comparable<V>> extends AbsBSTTree<V> {

    public int size;

    public TreeNode<V> root;

    public Tree02BST01(Comparator<V> comparator) {
        super(comparator);
    }

    @Override
    public boolean add(V v) {
        int oldSize = this.size;
        TreeNode<V> node = insert_traverse(root, v);
        if (root == null) {
            root = node;
        }
        return oldSize < size;
    }

    @Override
    public V get(V v) {
        TreeNode<V> node = this.search_traverse(root, v);
        return (node == null ? null : node.val);
    }

    @Override
    public V remove(V v) {
        root = remove_traverse(root, v);
        return null;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * 最大值
     *
     * @return
     */
    @Override
    public V max() {
        return keyOrNull(getMaxNode(root));
    }

    /**
     * 最小值
     *
     * @return
     */
    @Override
    public V min() {
        return keyOrNull(getMinNode(root));
    }

    @Override
    public V removeMax() {
        TreeNode<V> max = removeAndReturnMax(root);
        if (max != null) {
            return max.val;
        }
        return null;
    }

    @Override
    public V removeMin() {
        TreeNode<V> min = removeAndReturnMin(root);
        if (min != null) {
            return min.val;
        }
        return null;
    }

    /**
     * 地板
     *
     * @param v
     * @return
     */
    @Override
    public V floor(V v) {
        TreeNode<V> floor = getFloorNode(root, v);
        return floor != null ? floor.val : null;
    }

    /**
     * 天花板
     *
     * @param v
     * @return
     */
    @Override
    public V ceiling(V v) {
        TreeNode<V> ceiling = getCeilingNode(root, v);
        return ceiling != null ? ceiling.val : null;
    }

    /**
     * 排名
     *
     * @param v
     * @return
     */
    @Override
    public int rank(V v) {
        AtomicInteger rank = new AtomicInteger(1);
        // 采用中序遍历, 并计数
        traverse(root, Order.MidOrder, e -> {
            int cpr = compare(v, e.val);
            if (cpr > 0) {
                rank.incrementAndGet();
                return true;
            }
            return false;
        });
        return rank.get();
    }

    /**
     * 范围
     * 左开右闭
     *
     * @param min >= min
     * @param max < max
     * @return
     */
    @Override
    public List<V> range(V min, V max) {
        List<V> list = new ArrayList<>();
        int cpr = compare(min, max);
        if (cpr > 0) {
            return list;
        }
        // 先找到 min
        TreeNode<V> minNode = getCeilingNode(root, min);
        cpr = compare(minNode.val, max);
        if (minNode == null || cpr > 0) {
            return list;
        }
        /*
        遍历整棵树 e 要在 min max 之间 
            可不可以通过ceiling先找到 min, 然后再遍历到 max呢? 
            不行, 因为这里的树没有parent指针, 直接通过min节点遍历会丢失parent指针的其他节点数据
        为了降低遍历的范围, 这里将数据分为3段
            [step=1 (v<min)], [step=2 (min<=v<max)], [step=3 (max>v)]
            当到达第三段时, 遍历停止
         */
        AtomicInteger step = new AtomicInteger(1);
        traverse(root, Order.MidOrder, e -> {
            // 遍历整棵树 e 要在 min max 之间 
            if (compare(e.val, max) < 0 && compare(e.val, min) >= 0 && step.get() < 3) {
                list.add(e.val);
                step.set(2);
                return true;
            }
            if (step.get() == 2) {
                step.set(3);
                return false;
            }
            return true;
        });
        return list;
    }

    @Override
    public TreeNode<V> newNode(V v) {
        TreeNode<V> node = new TreeNode<>(v);
        node.height = 1;
        size++;
        return node;
    }

    @Override
    public String toString() {
        return BTreeUtils.simplePrint(root, false);
    }

    /***************************************** 通用方法 可重写 *****************************************************/

    /**
     * 采用递归的方式, 插入节点
     *
     * @param parent 以 parent 为root
     * @param v
     * @return 返回新的 parent 节点
     */
    protected TreeNode<V> insert_recursive(TreeNode<V> parent, V v) {
        if (parent == null) {
            return newNode(v);
        }

        int cpr = compare(v, parent.val);
        if (cpr < 0) {
            // 向左插入
            parent.left = insert_recursive(parent.left, v);
        } else if (cpr > 0) {
            // 向右插入
            parent.right = insert_recursive(parent.right, v);
        } else {
            parent.val = v; // 重复元素不处理 直接替换值
            return parent;
        }
        return parent;
    }

    /**
     * 采用递归方式, 查找节点
     *
     * @param parent 以 parent 为root
     * @param v
     * @return
     */
    protected TreeNode<V> search_recursive(TreeNode<V> parent, V v) {
        if (parent == null) {
            return null;
        }
        int cpr = compare(v, parent.val);
        if (cpr < 0) {
            return search_recursive(parent.left, v);
        } else if (cpr > 0) {
            return search_recursive(parent.right, v);
        } else {
            return parent;
        }
    }

    /**
     * 采用递归方式, 删除节点
     *
     * @param parent 以 parent 为root
     * @param v
     * @return 返回新的 parent/root 节点
     */
    protected TreeNode<V> remove_recursive(TreeNode<V> parent, V v) {

        if (null == parent) {
            return parent;
        }
        /*
        1. 递归找到指定待删除的节点s
        2. 如果s是叶子结点, 那么直接删除即可
            但是如果s是非叶子结点, 则找到s的直接前驱结点或者直接后继节点s2, 替代s即可
            1. 直接前驱结点: 就是s的左子树的右右..右子节点
            2. 直接后继节点: 就是s的右子树的左左..右子节点
        3. 但是s2也需要被删除, 所以将s2的值赋值给s之后, 还需要将s2删除, 此时s2肯定是叶子结点, 此时删除叶子节点即可
        4. 删除叶子结点s2, 递归当前删除方法定位到待删除的叶子结点, 将叶子结点的父节点的叶子节点置空即可
         */

        int cpr = compare(v, parent.val);
        if (cpr < 0) {
            // 小于当前根节点
            parent.left = remove_recursive(parent.left, v);
        } else if (cpr > 0) {
            // 大于当前根节点
            parent.right = remove_recursive(parent.right, v);
        } else if (parent.right != null && parent.left != null) {
            // 删除左子节点的最大节点(直接去前驱节点) 或 右子节点的最小节点(直接后驱节点) 都可以
            // 找到右边最小的节点
            parent.val = getMinNode(parent.right).val;
            // 当前节点的右边等于原节点右边删除已经被选为的替代节点
            parent.right = remove_recursive(parent.right, parent.val);
        } else {
            // 删除待删除的叶子节点
            parent = (parent.left != null) ? parent.left : parent.right;
            size--;
        }
        return parent;
    }

    /**
     * 采用循环的方式, 插入节点
     *
     * @param parent 以 parent 为root
     * @param v
     * @return 返回该节点
     */
    protected TreeNode<V> insert_traverse(TreeNode<V> parent, V v) {
        if (parent == null) {
            return newNode(v);
        }

        // 获取v的父节点, 如果v存在就是v的父节点, 如果v不存在就是v应该插入的父节点
        TreeNode<V> xp = getParentNode(parent, v);
        if (xp == null) {
            // v == parent
            return parent;
        }

        if ((xp.left != null && compare(xp.left.val, v) == 0)
                || (xp.right != null && compare(xp.right.val, v) == 0)) {
            // 等值不处理
            return parent;
        }

        TreeNode<V> x = newNode(v);
        int cpr = compare(v, xp.val);
        if (cpr < 0) {
            xp.left = x;
        } else if (cpr > 0) {
            xp.right = x;
        }
        return x;
    }

    /**
     * 采用循环遍历方式, 查找节点
     *
     * @param parent 以 parent 为root
     * @param v
     * @return
     */
    protected TreeNode<V> search_traverse(TreeNode<V> parent, V v) {
        while (parent != null) {
            int cpr = compare(v, parent.val);
            if (cpr == 0) return parent;
            parent = cpr < 0 ? parent.left : parent.right;
        }
        return parent;
    }

    /**
     * 采用循环遍历方式, 删除节点
     *
     * @param parent
     * @param v
     * @return 返回新的 parent/root 节点
     */
    protected TreeNode<V> remove_traverse(TreeNode<V> parent, V v) {
        if (parent == null) {
            return null;
        }
        // 待删除的节点x, x的父节点xp
        TreeNode<V> x = parent, xp = null;
        do {
            int cpr = compare(v, x.val);
            if (cpr == 0) break;
            xp = x;
            x = cpr < 0 ? x.left : x.right;
        } while (x != null);
        
        if (x == null) return parent; // 无需删除, 原样返回
        
        // 度为0的节点, 待删除x是叶子结点, 直接将叶子节点删除即可
        if (x.right == null && x.left == null) {
            if (xp == null) {
                // 待删除节点是根节点, 直接返回新的根节点
                size--;
                return null;
            }
            // 但删除节点不是根节点, 返回原来根节点
            if (xp.left == x) {
                xp.left = null;
            } else {
                xp.right = null;
            }
            size--;
            return root;
        }

        if (x.right == null || x.left == null) {
            // 度为1的节点
            TreeNode<V> replacement = x.right != null ? x.right : x.left;
            if (xp == null) {
                size--;
                return replacement;
            }
            if (xp.left == x) {
                xp.left = replacement; // 删除的是左子节点
            } else {
                xp.right = replacement; // 删除的是右子节点
            }
        } else {
            // 度为2的节点, 要找到其前驱或后继节点代替它
            TreeNode<V> minNode = getMinNode(x.right);
            x.val = minNode.val;
            // 叶子节点删除
            x.right = removeMinReturnNewParent(x.right);
        }
        size--;
        return root;
    }

    /***************************************** 工具 *****************************************************/

    /**
     * 由于没有parent指针, 所以不支持删除自己
     *
     * @param parent
     * @return 返回被删除的节点
     */
    protected TreeNode<V> removeAndReturnMax(TreeNode<V> parent) {
        if (parent == null) {
            return null;
        }
        TreeNode<V> max = parent;
        while (max.right != null) {
            parent = max;
            max = max.right;
        }
        // 如果最小节点有左叶子节点, 则需要将它们连接上
        parent.right = max.left;
        return max;
    }

    /**
     * 由于没有parent指针, 所以不支持删除自己
     *
     * @param parent
     * @return 返回被删除的节点
     */
    protected TreeNode<V> removeAndReturnMin(TreeNode<V> parent) {
        if (parent == null) {
            return null;
        }
        TreeNode<V> min = parent;
        while (min.left != null) {
            parent = min;
            min = min.left;
        }
        // 如果最小节点有右叶子节点, 则需要将它们连接上
        parent.left = min.right;
        return min;
    }

    /**
     * 由于没有parent指针, 所以不支持删除自己
     *
     * @param parent
     * @return 返回的父节点
     */
    protected TreeNode<V> removeMaxReturnNewParent(TreeNode<V> parent) {
        if (parent == null) {
            // 删除自己就相当于返回null
            return null;
        }
        TreeNode<V> max = parent, maxParent = null;
        while (max.right != null) {
            maxParent = max;
            max = max.right;
        }
        // 如果最小节点有左叶子节点, 则需要将它们连接上
        if (maxParent != null) maxParent.right = max.left;
        // 父节点如果有变化, 返回新的父节点
        return maxParent != null ? parent : max.left;
    }

    /**
     * 由于没有parent指针, 所以不支持删除自己
     *
     * @param parent
     * @return 返回的父节点
     */
    protected TreeNode<V> removeMinReturnNewParent(TreeNode<V> parent) {
        if (parent == null) {
            // 删除自己就相当于返回null
            return null;
        }
        TreeNode<V> min = parent, minParent = null;
        while (min.left != null) {
            minParent = min;
            min = min.left;
        }
        // 如果最小节点有右叶子节点, 则需要将它们连接上
        if (minParent != null) minParent.left = min.right;
        // 父节点如果有变化, 返回新的父节点
        return minParent != null ? parent : min.right;
    }

    /**
     * 获取指定树中, 天花板node
     * 
     * @param parent
     * @param v
     * @return
     */
    protected TreeNode<V> getFloorNode(TreeNode<V> parent, V v) {
        TreeNode<V> floor = null;
        while (parent != null) {
            int cpr = compare(v, parent.val);
            if (parent.val == v) {
                // v == 当前
                return parent;
            } else if (cpr < 0 && parent.left != null) {
                // v < 当前, 向左移动, 等待下次判断
                parent = parent.left;
            } else if (parent.right != null && compare(parent.right.val, v) < 0) {
                // v > 当前.right, 向右移动, 等待下次判断
                parent = parent.right;
            } else {
                // floor 介于 parent 和 parent.right 之间, 将满足条件的 node 放入候选单独比较
                if (compare(parent.val, v) < 0) {
                    if (floor == null || compare(floor.val, parent.val) < 0) {
                        floor = parent;
                    }
                }
                parent = parent.right;
            }
        }
        return floor;
    }

    /**
     * 获取指定树中, 地板node
     * 
     * @param parent
     * @param v
     * @return
     */
    protected TreeNode<V> getCeilingNode(TreeNode<V> parent, V v) {
        TreeNode<V> ceiling = null;
        while (parent != null) {
            if (parent.val == v) {
                // v == 当前
                return parent;
            } else if (compare(v, parent.val) > 0 && parent.right != null) {
                // v > 当前, 向右移动, 等待下次判断
                parent = parent.right;
            } else if (parent.left != null && compare(parent.left.val, v) > 0) {
                // v < 当前.left, 向左移动, 等待下次判断
                parent = parent.left;
            } else {
                // floor 介于 parent 和 parent.left 之间, 将满足条件的 node 放入候选单独比较
                if (compare(parent.val, v) > 0) {
                    if (ceiling == null || compare(ceiling.val, parent.val) > 0) {
                        ceiling = parent;
                    }
                }
                parent = parent.left;
            }
        }
        return ceiling;
    }

    /**
     * 返回指定树中最小的节点
     * 
     * @param tree
     * @return
     */
    protected TreeNode<V> getMinNode(TreeNode<V> tree) {
        TreeNode<V> p = tree, min = null;
        while (p != null) {
            min = p;
            p = p.left;
        }
        return min;
    }

    /**
     * 返回指定树中最大的节点
     *
     * @param tree
     * @return
     */
    protected TreeNode<V> getMaxNode(TreeNode<V> tree) {
        TreeNode<V> p = tree, max = null;
        while (p != null) {
            max = p;
            p = p.right;
        }
        return max;
    }

    /**
     * 返回指定树中, 指定值v的父节点, 如果v不在树中, 那么返回v可以插入位置的父节点
     * 
     * @param tree
     * @param v
     * @return
     */
    protected TreeNode<V> getParentNode(TreeNode<V> tree, V v) {
        TreeNode<V> p = tree, pp = null;
        while (p != null) {
            int cpr = compare(v, p.val);
            if (cpr == 0) {
                return pp;
            }
            pp = p;
            p = cpr < 0 ? p.left : p.right;
        }
        return pp;
    }

    /**
     * 获取树的高度
     */
    public static int getHeight_recursive(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return Math.max(getHeight_recursive(node.left), getHeight_recursive(node.right)) + 1;
    }

    /**
     * 获取树的高度
     */
    public static int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    protected static <V> V keyOrNull(TreeNode<V> e) {
        return (e == null) ? null : e.val;
    }

    public static boolean isLeft(TreeNode p, TreeNode x) {
        if (p != null && x != null && p.left != null) {
            return p.left.val == x.val;
        }
        return false;
    }

    public static boolean isRight(TreeNode p, TreeNode x) {
        if (p != null && x != null && p.right != null) {
            return p.right.val == x.val;
        }
        return false;
    }
}
