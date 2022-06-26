package org.song.algorithm.base._01datatype._02high.hashmap._01base;

import org.song.algorithm.base._01datatype._01base._04tree.binarytree._01model.Tree03AVL01;
import org.song.algorithm.base.utils.Strings;

import java.util.Comparator;

/**
 * 实现简单功能的 HashMap, 模仿JDK中的HashMap
 * 增加树化, 这里采用AVL树
 *
 * @param <K>
 * @param <V>
 */
public class HashMap_base_04<K, V> extends HashMap_base_03<K, V> {

    protected int treeCapacity = 8; // 变成树的阈值
    protected int linkedCapacity = 6; // 变成链表的阈值
    protected boolean dilatation = false;// 是否正在扩容
    protected Comparator<K> comparator;
    protected Comparator<K> NOP_CPR = Comparator.comparing(Object::hashCode);

    public HashMap_base_04(Comparator<K> comparator, int capacity) {
        super(capacity);
        this.comparator = comparator == null ? NOP_CPR : comparator;
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
            TreeNode<K, V> treeNode = new TreeNode<>(comparator, head.k, head.val);
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
        tty.append(new InorderPrinter(node.root).printString());
    }

    /**
     * AVL树, 具体参见 {@link Tree03AVL01}
     * 由于HashMap中每个链表头都是一个树的root,
     * 所以 树的操作方法都在树节点中
     *
     * @param <K, V>
     */
    static class TreeNode<K, V> extends Entry<K, V> {

        TreeNode<K, V> left;
        TreeNode<K, V> right;
        int height;

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
            root = insert_recursive(root, k, v);
            root.root = root;
            return size > this.size;
        }

        V get(K k) {
            TreeNode<K, V> node = this.search_traverse(root, k, null);
            return (node == null ? null : node.val);
        }

        boolean remove(K k) {
            TreeNode<K, V> node = this.search_traverse(root, k, null);
            root = remove_recursive(root, k, null);
            if (root != null) {
                root.root = root;
            }
            return node != null;
        }

        TreeNode<K, V> newNode(K k, V v) {
            TreeNode<K, V> node = new TreeNode<>(this.comparator, k, v);
            node.height = 1;
            size++;
            return node;
        }

        TreeNode<K, V> insert_recursive(TreeNode<K, V> parent, K k, V v) {
            if (parent == null) {
                return newNode(k, v);
            }

            if (less(k, parent.k)) {
                parent.left = insert_recursive(parent.left, k, v);
            } else if (greater(k, parent.k)) {
                parent.right = insert_recursive(parent.right, k, v);
            } else {
                parent.val = v; // 重复元素不处理 直接替换值
                return parent;
            }
            parent = balanceInsertion(parent);
            return parent;
        }

        TreeNode<K, V> remove_recursive(TreeNode<K, V> parent, K k, V v) {
            if (null == parent) {
                return parent;
            }
            // 复用 BST 的删除
            if (less(k, parent.k)) {
                parent.left = remove_recursive(parent.left, k, v);
            } else if (greater(k, parent.k)) {
                parent.right = remove_recursive(parent.right, k, v);
            } else if (parent.right != null && parent.left != null) {
                parent.val = getMinNode(parent.right).val;
                parent.right = remove_recursive(parent.right, parent.k, parent.val);
            } else {
                parent = (parent.left != null) ? parent.left : parent.right;
            }
            // 需要调整
            parent = balanceInsertion(parent);
            return parent;
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

        TreeNode<K, V> balance(TreeNode<K, V> x) {
            if (x == null) {
                return x;
            }

            // 左高右低
            if (getHeight(x.left) - getHeight(x.right) > 1) {
                if (getHeight(x.left.left) >= getHeight(x.left.right)) {
                    x = rightRotate(x);
                } else {
                    x.left = leftRotate(x.left);
                    x = rightRotate(x);
                }
            } else if (getHeight(x.right) - getHeight(x.left) > 1) {
                if (getHeight(x.right.right) >= getHeight(x.right.left)) {
                    x = leftRotate(x);
                } else {
                    x.right = rightRotate(x.right);
                    x = leftRotate(x);
                }
            }
            x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
            return x;

        }

        static int getHeight(TreeNode node) {
            if (node == null) {
                return 0;
            }
            return node.height;
        }

        TreeNode<K, V> balanceInsertion(TreeNode<K, V> x) {
            return balance(x);
        }

        TreeNode<K, V> rightRotate(TreeNode<K, V> p) {
            TreeNode<K, V> newParent = p.left;
            p.left = newParent.right;
            newParent.right = p;
            p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
            newParent.height = Math.max(getHeight(newParent.left), getHeight(newParent)) + 1;
            return newParent;
        }

        TreeNode<K, V> leftRotate(TreeNode<K, V> p) {
            TreeNode<K, V> newParent = p.right;
            p.right = newParent.left;
            newParent.left = p;
            p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
            newParent.height = Math.max(getHeight(newParent.left), getHeight(newParent)) + 1;
            return newParent;
        }

    }

    static class InorderPrinter {
        private TreeNode tree;
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

        InorderPrinter(TreeNode tree) {
            this.tree = tree;
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
            nodeString += nodePrefix + string + "\n";
            if (left != null) {
                leftPrefix += Strings.blank(length);
                nodeString += printString(left,
                        leftPrefix + leftAppend,
                        leftPrefix + blankAppend,
                        leftPrefix + lineAppend);
            }
            return nodeString;
        }
    }
}
