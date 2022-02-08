package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import java.util.LinkedList;
import java.util.List;

/*
B树 (平衡树, 包括2叉树和多叉树)
    多路平衡搜索树, 又称为B-树, 或者B树. 一棵m阶B-树, 或为空树, 或满足以下特性. 
    1. 每个节点最多有m棵子树. 
    2. 根节点至少有两棵子树. 
    3. 内部节点(除根和叶子之外的节点)至少有floor(m/2)棵子树. 
    4. 终端节点(叶子)在同一层上, 并且不带信息(空指针), 通常称为失败节点. 
    5. 非终端节点的关键字个数比子树个数少1. 
    也就是说, 根节点至少有一个关键字和两棵子树, 其他非终端节点关键字个数范围为[floor(m/2)-1, m-1], 子树个数范围为[floor(m/2), m]. 

说明:
    1. B-树具有平衡/有序/多路的特点. 在B-树中, 所有的叶子都在最后一层, 因此左右子树的高差为0, 体现了平衡的特性. B-树具有中序有序的特性, 即左子树<根<右子树. 多路是指可以有多个分支, m阶B-树中的节点最多可以有m个分支, 所以称为m路平衡搜索树. 
    2. 层和节点的关系: h+1层至少有2*floor(m/2)^(h-1)个节点
    3. B树所有叶子到根节点的高度都相同
    4. 节点可以存储多个key, 多个key可以由链表存储, 也可以有数组存储
    5, 新添加的元素必须添加到叶子结点
    6. 应用, 多用于文件索引, 多大成百上千阶

阶数m: m阶B树
    根节点中key的数量x, 1 <= x <= m-1
    非根节点中key的数量x, floor(m/2)-1 <= x <= m-1
    页子节点数量y, y = x+1
    以2-3树为例, 非叶子结点和其子节点的关系, 
        要么 1个父节点(1个key)带2个子节点
        要么 1个父节点(2个key)带3个子节点
        
    m=2, 每个节点最多1个值, 最多2个子节点, 也就是普通二叉树
    m=3, 每个节点最多2个值, 最多3个子节点, 就是23树
    m=4, 每个节点最多3个值, 最多4个子节点, 就是234树, 简称24树

性能分析
    1. 单个节点内最多有m-1个key, 搜索到这些key, 可以通过遍历或者二分法, 由于都是内存操作, 所以通常复杂度忽略不计
    2. 复杂度仅仅与树的高度和阶数有关, O(log_mN), 当m=2(2阶), 复杂度=O(logN)
    3. 插入复杂度: O(log_mN)

调整
    1. 上溢
        当节点key>m-1, 则需要分裂节点并将某个key上溢, 直到满足 key<=m-1
        1) 上溢到根节点如果还需要上溢, 则树的整体高度会增高, 且只有这种方式能让B树增高
    2. 下溢
        非叶子结点子节点<=x, 则需要将父节点分裂并下溢, 直到满足  y = x+1
        1) 如果父节点直到根节点都不够下溢, 则会出现父子节点合并, 从而B树的高度降低, 且只有这种方式能让B树降低
    3. 上溢或者下溢过程中, 为了满足B的有序性, 可能会出现(当前节点/父节点/兄弟节点)相互交换的情况, 这其实就等价于二叉树的旋转

 */

/**
 * B 树的实现, 代码来自网络
 * 
 * @param <V>
 */
public class Tree04_B_base<V extends Comparable<V>> {
    public BTree_Node<V> root;
    public int degree;    // 度
    public int number;    // 树种结点的数量

    public Tree04_B_base(int degree) {
        if (degree < 2) {
            throw new IllegalArgumentException("degree must >= 2");
        }
        root = null;
        number = 0;
        this.degree = degree;
    }

    /**
     * 返回是否为空
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 返回容量大小
     */
    public int size() {
        return number;
    }

    /**
     * 查找方法
     * B树的查找和二叉树查找类似, 首先在当前节点中查找, 如果没有并且存在孩子节点,
     * 就递归的到可能存在该key的孩子节点中查找.
     * 不同的是, B树节点有多个key, 需要每个都比较, 为了提高性能, 可以使用二分法加速节点中的查找.
     */
    public V search(BTree_Node<V> x, V key) {
        int i = 0;
        V entry, next;
        while (i < x.getKeyNum()) {
            entry = x.children.get(i);
            next = (i == x.getKeyNum() - 1) ? null : x.children.get(i + 1);
// 遍历数组, 如果数组内的元素包含了目标元素, return
            if (key.equals(entry)) {
                return entry;
            }
            if (key.compareTo(x.children.get(x.getKeyNum() - 1)) > 0) {
                i = x.getKeyNum();
                break;
            } else {
// 如果没有包括, 则需要递归寻找
                if (key.compareTo(entry) < 0) {
                    i = 0;
                    break;
                } else if (key.compareTo(entry) > 0 && key.compareTo(next) < 0) {
                    i = i + 1;
                    break;
                }
            }
            i++;
        }
// i对应的是子树指向的链表的索引位置,用一个"尾递归"
        if (x.isLeafNode()) {
            return null;
        }
        return search(x.pointers.get(i), key);
    }

    /**
     * 1、先判断当前节点的Entry是否已满
     * 2、如果已满, 就分裂
     * 3、如果结点是叶子结点做插入操作, 不是则向下递归
     */
    private void insertAction(BTree_Node<V> current, V entry) {
        // 先判断root结点是否为空
        if (root == null) {
            root = new BTree_Node<V>();
            current = root;
        }
        // 如果当前结点已满, 需要分裂
        if (current.isFull()) {
            // 分裂
            current = split(current);
        }
        // 如果当前结点是叶子结点
        if (current.isLeafNode()) {
            // 插入
            int index = getPosition(current, entry);
            // System.out.println(index+","+current.getClass());
            current.children.add(index, entry);
            number++;
        } else {
            // 否则, 向下递归寻找
            int index = getPosition(current, entry);
            // System.out.println("向下递归的索引"+index);
            insertAction(current.pointers.get(index), entry);
        }
    }

    public void insert(V key) {
        insertAction(root, key);
    }

    /**
     * 该方法用于定位在链表中, 新增Entry结点适合插入的位置
     */
    private int getPosition(BTree_Node<V> x, V entry) {
        List<V> list = x.children;
        int index = 0;
        V ent, next;
        for (int i = 0; i < list.size(); i++) {
            ent = x.children.get(i);
            next = (i == x.getKeyNum() - 1) ? null : x.children.get(i + 1);
            if (entry.compareTo(list.get(list.size() - 1)) > 0) {
                index = list.size();
                break;
            } else {
// 在左边
                if (entry.compareTo(ent) <= 0) {
                    index = 0;
                    break;
                }
// 在中间
                else if (entry.compareTo(next) <= 0) {
                    index = i + 1;
                    break;
                }
            }
        }
        return index;
    }

    /**
     * 分裂
     * B树的插入需要考虑的一个问题就是当节点以满时, 需要将该节点分裂成两个节点.
     * 一个满的节点有2*t-1个key, 内部节点有2*t 个孩子, 分裂将其分成两个各有t-1个key,
     * 内部节点各t个孩子, 多余的一个节点插入到父节点中, 作为分裂之后两个节点的分割key.
     */
    /*
     * 步骤: 1、将当前结点的中间Entry结点插入到父节点中, 并将剩余的B_TNode拆分
     * 难点: 绑定父结点和子结点的关系；
     *       定义pointer指针
     */
    private BTree_Node<V> split(BTree_Node<V> x) {
        BTree_Node<V> parent;
        BTree_Node<V> left = new BTree_Node<V>();
        BTree_Node<V> right = new BTree_Node<V>();
        int len = x.getKeyNum();
        V mid = x.children.get(len / 2);
        for (int i = 0; i < len; i++) {
            if (i < len / 2) {
                left.children.add(x.children.get(i));
            }
            if (i > len / 2) {
                right.children.add(x.children.get(i));
            }
        }
        if (x.isLeafNode()) {
// 如果parent为null,说明对根结点做split操作
            if (x == root) {
                parent = new BTree_Node<V>();
                parent.children.add(mid);
                parent.pointers.add(left);
                parent.pointers.add(right);
                left.parent = parent;
                right.parent = parent;
                root = parent;
                x = root;
            } else {
                parent = x.parent;
                int position = this.getPosition(parent, mid);
                parent.children.add(position, mid);
                parent.pointers.remove(position);
                parent.pointers.add(position, left);
                parent.pointers.add(position + 1, right);
                left.parent = parent;
                right.parent = parent;
                x = parent;
            }
        } else {
            if (x == root) {
// 绑定父节点
                parent = new BTree_Node<V>();
                parent.children.add(mid);
                parent.pointers.add(left);
                parent.pointers.add(right);
                left.parent = parent;
                right.parent = parent;
                root = parent;
// 绑定子节点
                for (int i = 0; i < x.pointers.size(); i++) {
                    BTree_Node<V> node = x.pointers.get(i);
                    if (i <= len / 2) {
                        left.pointers.add(node);
                        node.parent = left;
                    } else {
                        right.pointers.add(node);
                        node.parent = right;
                    }
                }
                x = root;
            } else {
// 绑定父节点
                parent = x.parent;
                int position = this.getPosition(parent, mid);
                parent.children.add(position, mid);
                parent.pointers.remove(position);
                parent.pointers.add(position, left);
                parent.pointers.add(position + 1, right);
                left.parent = parent;
                right.parent = parent;
// 绑定子节点
                for (int i = 0; i < x.pointers.size(); i++) {
                    BTree_Node<V> node = x.pointers.get(i);
                    if (i <= len / 2) {
                        left.pointers.add(node);
                        node.parent = left;
                    } else {
                        right.pointers.add(node);
                        node.parent = right;
                    }
                }
                x = parent;
            }
        }
        return x;
    }

    /**
     * 删除
     */
    public V remove(V key) {
        V entry = search(root, key);
        if (entry == null) {
            return null;
        }
        return removeAction(root, entry);
    }

    /**
     * 删除动作
     * 第一步: 先判断根节点是否只有一个元素, 并且根节点的左右子节点的keyNum均等于Min. 如果是merge,否则到第二步
     * 第二步: 通过key, 定位到Entry在当前结点的索引位, 并根据该索引位得到左子结点和右子节点
     * 例如:  C    G    N     --------- BT_Node1
     * AB   DE   JK  OP   --------- BT_Node2
     * 在BT_Node1中, 通过key(G)找到Entry G所在的位置为1, 然后得到G的左右子结点分别为: DE(左) 和 JK(右)
     * 第三步: 如果待删除的结点是父节点, 判断当前结点的左子结点和右子结点
     * case1: 如果左和右子节点的keyNum均大于最小数Min(degree-1),那么取出左子树最大元素替换待删除元素
     * case2: 如果左子结点的KeyNum>Min, 但是右子结点的keyNum =Min,取出左子树的最大元素替换待删除元素
     * case3: 如果左子结点的KeyNum=Min, 但是右子结点的keyNum > Min,取出右子树点的最小元素替换待删除元素
     * case4: 如果左和右子节点的keyNum均等于Min, 那么做merge操作
     * 第四步: 递归到叶子结点, 如果叶子结点的keyNum>Min, 则直接删除, 反之需要做merge操作
     */
    private V removeAction(BTree_Node<V> parent, V entry) {
        BTree_Node<V> left, right;
        V replace;
        int index;
        if (parent == root && parent.getKeyNum() == 1) {
            left = parent.pointers.get(0);
            right = parent.pointers.get(1);
            if (left.getKeyNum() == degree - 1 && right.getKeyNum() == degree - 1) {
                parent = merge(root, entry, 0);
            }
        }
        // 判断是否是叶子结点, 如果是叶子结点且keyNum>Min,则直接删除
        // 如果keyNum=Min,需要对其父结点做merge操作
        if (parent.isLeafNode()) {
            if (parent.getKeyNum() > degree - 1) {
                parent.children.remove(entry);
            } else {
                BTree_Node<V> grand = parent.parent;
                index = grand.pointers.indexOf(parent);
                System.out.println("index is " + index);
                V ent = grand.children.get(index);
                grand = merge(grand, ent, index);
                parent = grand == root ? root : grand.pointers.get(index);
                parent.children.remove(entry);
            }
            return entry;
        }
        // 判断parent是否包含entry
        if (!parent.isLeafNode() && parent.children.contains(entry)) {
            index = parent.children.indexOf(entry);
            // 4中不同的条件
            left = parent.pointers.get(index);
            right = parent.pointers.get(index + 1);
            if (left.getKeyNum() > degree - 1 && right.getKeyNum() >= degree - 1) {
                replace = findLeftReplaceEntry(left);
                parent.children.set(index, replace);
                left.children.remove(replace);
                return entry;
            } else if (left.getKeyNum() == degree - 1 && right.getKeyNum() > degree - 1) {
                replace = findRightReplaceEntry(right);
                parent.children.set(index, replace);
                right.children.remove(replace);
                return entry;
            } else {
                parent = merge(parent, entry, index);
            }
        }
        index = getPosition(parent, entry);
        return removeAction(parent.pointers.get(index), entry);
    }

    /**
     * 向左子树遍历, 找左子树中最大的元素
     */
    private V findLeftReplaceEntry(BTree_Node<V> node) {
        BTree_Node<V> current = node;
        int keyNum;
        while (!current.isLeafNode()) {
            keyNum = current.getKeyNum();
            current = current.pointers.get(keyNum);
        }
        return current.children.get(current.getKeyNum() - 1);
    }

    /**
     * 向右子树遍历, 找右子树中最小的元素
     */
    private V findRightReplaceEntry(BTree_Node<V> node) {
        BTree_Node<V> current = node;
        while (!current.isLeafNode()) {
            current = current.pointers.get(0);
        }
        return current.children.get(0);
    }

    /**
     * merge方法: entry会从parent中移动到merge后的新节点当中
     * 其中parent为当前结点, entry是待删除的元素, index是entry在parent中的索引位置
     * 例如:       C G L
     * /  / \ \
     * AB  DE JK NO
     * 对DE和JK做merge操作, 结果:
     * 结果:    C   L
     * /  /  \
     * AB DEGJK NO
     */
    private BTree_Node<V> merge(BTree_Node<V> parent, V entry, int index) {
        BTree_Node<V> node;
        BTree_Node<V> left = parent.pointers.get(index);
        BTree_Node<V> right = parent.pointers.get(index + 1);
        if (parent == root) {
            // 创建一个新的BT_Node
            node = new BTree_Node<V>();
            // 绑定children键值对
            node.children.addAll(left.children);
            node.children.add(entry);
            node.children.addAll(right.children);
            // 如果左右结点不是叶子结点
            if (!left.isLeafNode() && !right.isLeafNode()) {
                // 绑定pointers指针引用
                node.pointers.addAll(left.pointers);
                node.pointers.addAll(right.pointers);
                // 重新定义父子关系
                for (int i = 0; i < left.getKeyNum(); i++) {
                    left.pointers.get(i).parent = node;
                }
                for (int i = 0; i < right.getKeyNum(); i++) {
                    right.pointers.get(i).parent = node;
                }
            }
            // 干掉所有的引用
            parent = null;
            left = null;
            right = null;
            // 重新赋值root
            root = node;
            return root;
        } else {
            node = new BTree_Node<V>();
            // 绑定children键值对
            node.children.addAll(left.children);
            node.children.add(entry);
            node.children.addAll(right.children);
            // 如果左右不是叶子结点
            if (!left.isLeafNode() && !right.isLeafNode()) {
                // 绑定pointers指针引用
                node.pointers.addAll(left.pointers);
                node.pointers.addAll(right.pointers);
                // 重新定义父子关系
                for (int i = 0; i < left.getKeyNum(); i++) {
                    left.pointers.get(i).parent = node;
                }
                for (int i = 0; i < right.getKeyNum(); i++) {
                    right.pointers.get(i).parent = node;
                }
            }
            // 删除parent中的entry
            parent.children.remove(entry);
            parent.pointers.remove(left);
            parent.pointers.remove(right);
            // 重新赋值parent
            parent.pointers.add(index, node);
            return parent;
        }
    }

    /**
     * 树的遍历
     * 先遍历本节点的键值对元素，然后再递归遍历pointer指针里的结点元素
     */
    public void frontIterator(BTree_Node<V> node) {
        if(node.isLeafNode()) {
            print(node);
        }else {
            print(node);
            for(int i=0;i<node.pointers.size();i++) {
                frontIterator(node.pointers.get(i));
            }
        }
    }

    /**
     * 打印BT_Node结点
     */
    public void print(BTree_Node<V> node) {
        System.out.println(node);
    }

    /**
     * B树的结点
     */
    class BTree_Node<V extends Comparable<V>> {
        BTree_Node<V> parent;
        /*
         *注: 这里的children和pointers应该用定义成数组的（因为链表的增加和删除操作比数组方便, 所以引用链表）,
         *但是实际上, 只有B+树的内部结点才定义了链表
         */
        final List<V> children;   // 存放的键值对链表
        final List<BTree_Node<V>> pointers;  // 子树指针的链表

        /**
         * 除根节点外, 每个节点必须有至少t-1个key, t个孩子. 树不为空时, 根节点至少有一个key.
         * 每个节点至多有2*t-1个key, 每个内部节点至多有2*t个孩子. 当一个节点有2*t-1个key时, 称其为满节点.
         */
        public BTree_Node() {
            parent = null;
            children = new LinkedList<V>();
            pointers = new LinkedList<BTree_Node<V>>();
        }

        /**
         * 返回键值对的数量
         */
        public int getKeyNum() {
            return children.size();
        }

        /**
         * 判断当前结点是否已满
         */
        public boolean isFull() {
            return this.getKeyNum() == 2 * degree - 1;
        }

        /**
         * 判断当前结点是否是叶子结点
         */
        public boolean isLeafNode() {
            return this.pointers.size() == 0;
        }

        /**
         * 判断当前结点的键值对数目是否符合约束条件
         */
        public boolean isQualified() {
            int keyNum = this.getKeyNum();
            if (this != root) {
                if (keyNum < (degree - 1)) {
                    throw new IllegalArgumentException("The least keyNum is (degree-1)");
                }
                if (keyNum > (2 * degree - 1)) {
                    throw new IllegalArgumentException("The most keyNum is (2*degree-1)");
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return "BT_Node [children=" + children + "]" + " count:" + this.getKeyNum();
        }
    }
}
