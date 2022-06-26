package org.song.algorithm.base._01datatype._02high.hashmap._01base;

import org.song.algorithm.base._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.base._01datatype._01base._04tree.binarytree._01model.Tree05RB01;
import org.song.algorithm.base.utils.Strings;

import java.util.Comparator;
import java.util.function.Predicate;

/**
 * 实现简单功能的 HashMap, 模仿JDK中的HashMap
 * 增加树化, 这里采用红黑树
 *
 * @param <K>
 * @param <V>
 */
public class HashMap_base_05<K, V> extends HashMap_base_04<K, V> {

    protected int treeCapacity = 8; // 变成树的阈值
    protected int linkedCapacity = 6; // 变成链表的阈值
    protected boolean dilatation = false; // 是否正在扩容
    protected Comparator<K> comparator;

    public static final boolean RED = true;
    public static final boolean BLACK = false;

    public HashMap_base_05(Comparator<K> comparator, int capacity) {
        super(comparator, capacity);
    }

    @Override
    public V get(K k) {
        int hash = hash(k);
        int index = getIndex(hash, datas.length);
        // 表示链表头
        Entry<K, V> head = datas[index];
        if (head == null) {
            return null;
        } else if (head instanceof TreeNode) {
            // 从树中查找
            return ((TreeNode<K, V>) head).get(k);
        } else {
            // 从链表中查找 遍历
            Entry<K, V> pre = head;
            while (pre != null) {
                // 找到了, 返回他
                if (hash == hash(pre.k) && (k == pre.k || pre.k.equals(k))) return pre.val;
                pre = pre.next;
            }
        }
        return null;
    }

    @Override
    public V put(K k, V v) {
        Entry<K, V> entry = new Entry<>(k, v, null);
        entry.hash = hash(k);
        return putNode(entry, datas);
    }

    /**
     * 单独提供一个方法是因为
     * 1. 新增的时候使用
     * 2. 扩容的时候使用
     *
     * @param entry
     * @param datas
     * @return
     */
    protected V putNode(Entry<K, V> entry, Entry<K, V>[] datas) {
        K k = entry.k;
        V v = entry.val;
        int hash = entry.hash;
        int index = getIndex(hash, datas.length);

        Entry<K, V> head = datas[index];
        boolean added = false;

        if (head == null) {
            // 没有 则新增
            datas[index] = new Entry<>(k, v, null, hash);
            added = true;
        } else if (head instanceof TreeNode) {
            // 向树中插入
            TreeNode<K, V> treeHead = (TreeNode<K, V>) head;
            if (!treeHead.add(k, v)) {
                return v; // 插入失败
            }
            datas[index] = treeHead.root; // 更新头结点
            added = true;
        } else {
            // 向链表中插入
            Entry<K, V> pre = head;
            int count = 1;
            while (pre != null) {
                count++;
                if (hash == hash(pre.k) && (k == pre.k || pre.k.equals(k))) {
                    // 找到了, 则替换
                    datas[index] = new Entry<>(k, v, pre.next, hash);
                    return pre.val;
                } else if (pre.next == null) {
                    // 放在链表尾部
                    pre.next = new Entry<>(k, v, null, hash);
                    added = true;
                    break;
                }
                pre = pre.next;
            }

            if (count >= treeCapacity) {
                // 树化并更新头结点
                datas[index] = intoTree(head);
            }
        }
        size++; // 容量增加
        ensureCapacity();
        return added ? null : v;

    }

    @Override
    public V remove(K k) {
        int hash = hash(k);
        int index = getIndex(hash, datas.length);

        Entry<K, V> head = datas[index];
        if (head == null) {
            // 没有 返回空
            return null;
        } else if (head instanceof TreeNode) {
            // 从树中删除
            TreeNode<K, V> treeHead = (TreeNode<K, V>) head;
            if (!treeHead.remove(k)) {
                return null;
            }
            size--;
            if (treeHead.size <= linkedCapacity) {
                // 变成链表并更新头结点
                datas[index] = intoLinked(treeHead);
            } else {
                datas[index] = treeHead.root; // 更新头结点
            }
        } else {
            // 在链表中查找
            Entry<K, V> pre = null, next = head;
            while (next != null) {
                // 找到了 删除并返回他
                if (hash == hash(next.k) && (k == next.k || next.k.equals(k))) {
                    if (pre == null) {
                        datas[index] = next.next;
                    } else {
                        pre.next = next.next;
                    }
                    size--;
                    return next.val;
                }
                pre = next;
                next = next.next;
            }
        }
        return null;
    }

    @Override
    public void clean() {
        super.clean();
        dilatation = false;
    }

    public ArrayBase01<Entry<K, V>> toList() {
        ArrayBase01<Entry<K, V>> list = new ArrayBase01<>();
        for (Entry<K, V> entry : datas) {
            if (entry == null) {
                continue;
            }
            if (entry instanceof HashMap_base_05.TreeNode) {
                traverse((TreeNode<K, V>) entry, e -> {
                    list.add(e);
                    return true;
                });
            } else {
                Entry<K, V> h = entry;
                while (h != null) {
                    list.add(entry);
                    h = h.next;
                }
            }
        }
        return list;
    }

    /**
     * 确保容量
     */
    protected void ensureCapacity() {
        if ((double) size / (double) datas.length > dilatationRatio) {
            // 防止扩容的时候循环扩容
            if (!dilatation) {
                dilatation = true;
                dilatation();
                dilatation = false;
            }
        }
    }

    /**
     * 扩容
     */
    @Override
    protected void dilatation() {
        // 扩容2倍
        Entry<K, V>[] newDatas = new Entry[datas.length << 1];
        // 遍历数组
        for (int i = 0; i < datas.length; i++) {
            Entry<K, V> head = datas[i];
            if (head == null) {
                continue;
            }
            if (head instanceof TreeNode) {
                // 树迁移
                TreeNode<K, V> treeHead = (TreeNode<K, V>) head;
                traverse(treeHead, newDatas);
            } else {
                // 链表迁移
                Entry<K, V> headOld = null, // 原位置头
                        tailOld = null, // 原位置尾
                        headNew = null, // 新位置头
                        tailNew = null, // 新位置尾
                        n = head;

                while (n != null) {
                    Entry<K, V> next = n.next;
                    int index = getIndex(n.hash, newDatas.length);
                    if (index == i) {
                        // 不需要移动
                        if (headOld == null) {
                            headOld = n;
                            tailOld = n;
                            headOld.next = null;
                        } else {
                            tailOld.next = n;
                            tailOld = tailOld.next;
                            tailOld.next = null;
                        }
                    } else {
                        // 需要移动
                        if (headNew == null) {
                            headNew = n;
                            tailNew = n;
                            headNew.next = null;
                        } else {
                            tailNew.next = n;
                            tailNew = tailNew.next;
                            tailNew.next = null;
                        }
                    }
                    n = next;
                }

                if (headOld != null) {
                    // 不需要移动的链表
                    newDatas[i] = headOld;
                }
                if (headNew != null) {
                    // 需要移动的链表
                    int index = getIndex(headNew.hash, newDatas.length);
                    newDatas[index] = headNew;
                }
            }
            // help GC
            datas[i] = null;
        }
        datas = newDatas;
    }

    protected void traverse(TreeNode<K, V> node, Entry<K, V>[] newDatas) {
        if (node == null) return;
        putNode(node, newDatas);
        traverse(node.left, newDatas);
        traverse(node.right, newDatas);
    }

    /**
     * 链表变成树
     *
     * @param head
     * @return
     */
    private TreeNode<K, V> intoTree(Entry<K, V> head) {
        TreeNode<K, V> treeHead = null;
        while (head != null) {
            TreeNode<K, V> treeNode = new TreeNode<>(this.comparator, head.k, head.val);
            treeNode.hash = head.hash;
            if (treeHead == null) {
                treeHead = treeNode;
            } else {
                treeHead.add(treeNode);
                treeHead = treeHead.root;
            }
            head = head.next;
        }
        return treeHead;
    }

    /**
     * 树变成链表
     *
     * @param treeHead
     * @return
     */
    protected Entry<K, V> intoLinked(TreeNode<K, V> treeHead) {
        Entry<K, V> headTemp = new Entry<>(null, null, null);
        traverse(treeHead, headTemp);
        return headTemp.next;
    }

    protected void traverse(TreeNode<K, V> node, Entry<K, V> head) {
        if (node == null) return;
        head.next = new Entry<>(node.k, node.val, null);
        traverse(node.left, head.next);
        traverse(node.right, head.next);
    }

    protected void traverse(TreeNode<K, V> node, Predicate<Entry<K, V>> goon) {
        if (node == null) return;
        if (!goon.test(node)) return;
        traverse(node.left, goon);
        traverse(node.right, goon);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size=").append(size);
        sb.append("\r\n");
        int count = 0;
        for (int i = 0; i < datas.length; i++) {
            Entry<K, V> data = datas[i];
            if (data != null) {
                if (data instanceof TreeNode) {
                    sb.append(count++).append("-").append(i).append(": ");
                    // 树的遍历
                    printFromNode((TreeNode) data, sb);
                } else {
                    // 遍历链表
                    Entry<K, V> pre = data, next;
                    sb.append(count++).append("-").append(i).append(": ");
                    while (pre != null) {
                        next = pre.next;
                        sb.append(pre.toString());
                        if (next == null) break;
                        sb.append(",");
                        pre = next;
                    }
                }
                sb.append("\r\n");
            }
        }

        return sb.toString();
    }

    private static void printFromNode(TreeNode node, StringBuilder tty) {
        tty.append("\r\n");
        tty.append(new InorderPrinter(node.root, true).printString());
    }

    /**
     * 红黑树, 具体参见 {@link Tree05RB01}
     * 由于HashMap中每个链表头都是一个树的root,
     * 所以 树的操作方法都在树节点中
     *
     * @param <K, V>
     */
    static class TreeNode<K, V> extends Entry<K, V> {

        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> parent;
        boolean red;
        int size;
        TreeNode<K, V> root;
        Comparator<K> comparator;

        TreeNode(Comparator<K> comparator, K k, V v) {
            super(k, v, null);
            this.comparator = comparator;
        }

        public String toBaseString() {
            return String.valueOf(val);
        }

        boolean add(TreeNode<K, V> node) {
            return add(node.k, node.val);
        }

        boolean add(K k, V v) {
            int size = this.size;
            if (root == null) {
                root = this;
            }
            TreeNode<K, V> node = this.insert_traverse(root, k, v);
            root.root = root;
            balanceInsertion(node);
            setBlack(root); // 根总为黑
            return size > this.size;
        }

        V get(K k) {
            TreeNode<K, V> node = this.search_traverse(root, k, null);
            return (node == null ? null : node.val);
        }

        boolean remove(K k) {
            TreeNode<K, V> node = this.search_traverse(root, k, null);
            remove(node);
            if (root != null) {
                root.root = root;
            }
            return node != null;
        }

        TreeNode<K, V> newNode(K k, V v) {
            TreeNode<K, V> node = new TreeNode<>(this.comparator, k, v);
            node.red = RED;
            size++;
            return node;
        }

        protected TreeNode<K, V> insert_traverse(TreeNode<K, V> parent, K k, V v) {
            if (parent == null) {
                return newNode(k, v);
            }

            TreeNode<K, V> xp = getParentNode(parent, v);
            if (xp == null) {
                return parent;
            }

            if ((xp.left != null && eq(xp.left.k, k))
                    || (xp.right != null && eq(xp.right.k, k))) {
                return parent;
            }

            TreeNode<K, V> x = newNode(k, v);
            x.parent = xp; // 记录parent指针
            if (less(k, xp.k)) {
                xp.left = x;
            } else if (greater(k, xp.k)) {
                xp.right = x;
            }
            return x;
        }

        protected TreeNode<K, V> getParentNode(TreeNode<K, V> tree, V v) {
            TreeNode<K, V> p = tree, pp = null;
            while (p != null) {
                if (eq(k, p.k)) {
                    return pp;
                }
                pp = p;
                p = less(k, p.k) ? p.left : p.right;
            }
            return pp;
        }

        protected void remove(TreeNode<K, V> x) {
            // 待删除节点
            if (x == null) return;

            // 删除 度为2的节点
            if (x.right != null && x.left != null) {
                TreeNode<K, V> successor = successor(x);
                x.val = successor.val;
                x = successor;
            }
            TreeNode<K, V> replacement = x.right != null ? x.right : x.left;

            if (replacement != null) {
                // 度为1
                if (x.parent == null) {
                    root = replacement;
                } else if (isLeft(x.parent, x)) {
                    x.parent.left = replacement;
                } else {
                    x.parent.right = replacement;
                }
                replacement.parent = x.parent;

                if (!x.red) {
                    balanceDeletion(replacement);
                }

            } else if (x.parent == null) {
                root = null; // 删除根节点, 则替换root
            } else {

                if (!x.red) {
                    balanceDeletion(x);
                }

                // 度为0
                if (isLeft(x.parent, x)) {
                    x.parent.left = null;
                } else {
                    x.parent.right = null;
                }
            }
            size--;
        }

        protected TreeNode<K, V> successor(TreeNode<K, V> node) {
            if (node == null) return null;
            if (node.right != null) {
                return getMinNode(node.right);
            }
            TreeNode<K, V> p = node.parent;
            TreeNode<K, V> ch = node;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }

        TreeNode<K, V> search_traverse(TreeNode<K, V> parent, K k, V v) {
            while (parent != null) {
                if (eq(k, parent.k)) return parent;
                parent = less(k, parent.k) ? parent.left : parent.right;
            }
            return parent;
        }

        TreeNode<K, V> getMinNode(TreeNode<K, V> tree) {
            TreeNode<K, V> p = tree, min = null;
            while (p != null) {
                min = p;
                p = p.left;
            }
            return min;
        }

        boolean less(K v1, K v2) {
            return comparator.compare(v1, v2) < 0;
        }

        boolean greater(K v1, K v2) {
            return comparator.compare(v1, v2) > 0;
        }

        boolean eq(K v1, K v2) {
            return comparator.compare(v1, v2) == 0;
        }

        protected TreeNode<K, V> balanceDeletion(TreeNode<K, V> x) {
            while (x != root && isBlack(x)) {
                if (x == left(parent(x))) {
                    if (isRed(right(parent(x)))) {
                        setBlack(right(parent(x)));
                        setRed(parent(x));
                        leftRotate(parent(x));
                    }
                    if (isBlack(left(right(parent(x)))) && isBlack(right(right(parent(x))))) {
                        setRed(right(parent(x)));
                        x = parent(x);
                    } else {
                        if (isBlack(right(right(parent(x))))) {
                            setBlack(left(right(parent(x))));
                            setRed(right(parent(x)));
                            rightRotate(right(parent(x)));
                            x = parent(x);
                        }
                        setColor(right(parent(x)), color(parent(x)));
                        setBlack(parent(x));
                        setBlack(right(right(parent(x))));
                        leftRotate(parent(x));
                        x = root;
                    }
                } else {
                    if (isRed(left(parent(x)))) {
                        setBlack(left(parent(x)));
                        setRed(parent(x));
                        rightRotate(parent(x));
                    }
                    if (isBlack(right(left(parent(x)))) && isBlack(left(left(parent(x))))) {
                        setRed(left(parent(x)));
                        x = parent(x);
                    } else {
                        if (isBlack(left(left(parent(x))))) {
                            setBlack(right(left(parent(x))));
                            setRed(left(parent(x)));
                            leftRotate(left(parent(x)));
                        }
                        setColor(left(parent(x)), color(parent(x)));
                        setBlack(parent(x));
                        setBlack(left(left(parent(x))));
                        rightRotate(parent(x));
                        x = root;
                    }
                }
            }
            x.red = BLACK;
            return x;
        }

        TreeNode<K, V> balanceInsertion(TreeNode<K, V> x) {
            while (x != null && x != root && isRed(parent(x))) {
                if (parent(x) == left(parent(parent(x)))) {
                    if (isRed(right(parent(parent(x))))) {
                        setBlack(parent(x));
                        setRed(parent(parent(x)));
                        setBlack(right(parent(parent(x))));
                        x = parent(parent(x));
                    } else {
                        if (x == right(parent(x))) {
                            x = parent(x);
                            leftRotate(x);
                        }
                        setBlack(parent(x));
                        setRed(parent(parent(x)));
                        rightRotate(parent(parent(x)));
                    }
                } else {
                    if (isRed(left(parent(parent(x))))) {
                        setBlack(parent(x));
                        setRed(parent(parent(x)));
                        setBlack(left(parent(parent(x))));
                        x = parent(parent(x));
                    } else {
                        if (x == left(parent(x))) {
                            x = parent(x);
                            rightRotate(x);
                        }
                        setBlack(parent(x));
                        setRed(parent(parent(x)));
                        leftRotate(parent(parent(x)));
                    }
                }
            }
            return x;
        }

        TreeNode<K, V> rightRotate(TreeNode<K, V> p) {
            if (p == null) {
                return p;
            }
            TreeNode<K, V> pLeft = p.left;
            p.left = pLeft.right;
            if (pLeft.right != null) {
                pLeft.right.parent = p;
            }
            pLeft.parent = p.parent;
            if (p.parent == null) {
                root = pLeft;
            } else {
                if (p == p.parent.left) {
                    p.parent.left = pLeft;
                } else {
                    p.parent.right = pLeft;
                }
            }
            pLeft.right = p;
            p.parent = pLeft;
            return p;
        }

        TreeNode<K, V> leftRotate(TreeNode<K, V> p) {
            if (p == null) {
                return p;
            }
            TreeNode<K, V> pRight = p.right;
            p.right = pRight.left;
            if (pRight.left != null) {
                pRight.left.parent = p;
            }
            pRight.parent = p.parent;
            if (p.parent == null) {
                root = pRight;
            } else {
                if (p == p.parent.left) {
                    p.parent.left = pRight;
                } else {
                    p.parent.right = pRight;
                }
            }
            pRight.left = p;
            p.parent = pRight;
            return pRight;
        }


        /***************************************** 工具 *****************************************************/


        public static boolean isLeft(TreeNode p, TreeNode x) {
            if (p != null && x != null && p.left != null) {
                return p.left.val == x.val;
            }
            return false;
        }

        final protected boolean isRed(TreeNode<K, V> p) {
            return p != null && p.red;
        }

        final protected void setRed(TreeNode<K, V> p) {
            setColor(p, RED);
        }

        final protected boolean isBlack(TreeNode<K, V> p) {
            return p == null || !p.red;
        }

        final protected boolean color(TreeNode<K, V> p) {
            return p != null ? p.red : BLACK;
        }

        final protected void setBlack(TreeNode<K, V> p) {
            setColor(p, BLACK);
        }

        final protected void setColor(TreeNode<K, V> p, boolean color) {
            if (p != null) p.red = color;
        }

        final protected TreeNode<K, V> parent(TreeNode<K, V> p) {
            return (p == null ? null : p.parent);
        }

        final protected TreeNode<K, V> left(TreeNode<K, V> p) {
            return (p == null) ? null : p.left;
        }

        final protected TreeNode<K, V> right(TreeNode<K, V> p) {
            return (p == null) ? null : p.right;
        }
    }

    static class InorderPrinter {
        private TreeNode tree;
        private boolean printColor;
        private static String rightAppend;
        private static String leftAppend;
        private static String blankAppend;
        private static String lineAppend;

        static {
            int length = 2;
            rightAppend = "┌" + Strings.repeat("─", length);
            leftAppend = "└" + Strings.repeat("─", length);
            blankAppend = Strings.blank(length + 1);
            lineAppend = "│" + Strings.blank(length);
        }

        InorderPrinter(TreeNode tree, boolean printColor) {
            this.tree = tree;
            this.printColor = printColor;
        }

        String printString() {
            StringBuilder string = new StringBuilder(
                    printString(tree, "", "", ""));
            string.deleteCharAt(string.length() - 1);
            return string.toString();
        }

        /**
         * 生成node节点的字符串
         *
         * @param nodePrefix  node那一行的前缀字符串
         * @param leftPrefix  node整棵左子树的前缀字符串
         * @param rightPrefix node整棵右子树的前缀字符串
         * @return
         */
        String printString(
                TreeNode node,
                String nodePrefix,
                String leftPrefix,
                String rightPrefix) {
            TreeNode left = node.left;
            TreeNode right = node.right;
            String string = node.toBaseString();
            int length = string.length();
            if (length % 2 == 0) {
                length--;
            }
            length >>= 1;
            String nodeString = "";
            if (right != null) {
                rightPrefix += Strings.blank(length);
                nodeString += printString(right,
                        rightPrefix + rightAppend,
                        rightPrefix + lineAppend,
                        rightPrefix + blankAppend);
            }
            if (printColor) {
                nodeString += nodePrefix + fill(node, string) + "\n";
            } else {
                nodeString += nodePrefix + string + "\n";
            }
            if (left != null) {
                leftPrefix += Strings.blank(length);
                nodeString += printString(left,
                        leftPrefix + leftAppend,
                        leftPrefix + blankAppend,
                        leftPrefix + lineAppend);
            }
            return nodeString;
        }

        static String fill(TreeNode node, Object v) {
            return isRed(node) ? "\033[31;4m" + v + "\033[0m" : String.valueOf(v);
        }

        static boolean isRed(TreeNode node) {
            return node != null && node.red;
        }
    }
}
