package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

/*
23树
2-3树是比较早期的一个平衡树, 跟2-3-4树差不多, 编程起来稍微麻烦点, 也是被红黑树取代了. 
2-3查找树的原理很简单, 甚至说代码实现起来难度都不是很大, 但是却很繁琐, 因为它有很多种情况, 而在红黑树中, 用巧妙的方法使用了2个结点解决了3个结点的问题. 

但是, 我们和真正的实现还有一段距离. 
尽管我们可以用不同的数据类型表示2-结点和3-结点并写出变换所需的代码, 但用这种直白的表示方法实现大多数的操作并不方便, 
因为需要处理的情况实在太多. 我们需要维护两种不同类型的结点, 将被查找的键和结点中的每个键进行比较, 
将链接和其他信息从一种结点复制到另一种结点, 将结点从一种数据类型转换到另一种数据类型, 等等. 
实现这些不仅需要大量的代码, 而且它们所产生的额外开销可能会使算法比标准的二叉查找树更慢. 
平衡一棵树的初衷是为了消除最坏情况, 但我们希望这种保障所需的代码能够越少越好. 
幸运的是你将看到, 我们只需要一点点代价就能用一种统一的方式完成所有变换. 

这里放入23树的原因是, 红黑树是23树的一个变种, 23树的一些特性可以更好的理解红黑树的特性比如, 变色旋转等

具体不做测试
 */
/*
23树
1. 平衡状态下, 一个节点最多可以保存2个v, 每个节点最多可以有3个子节点c, 其他和二叉树一样
    他们可以由数组方式存储, 提高访问效率, [v1, v2] [c1, c2, c3]
    2个v和3个c示例如下, 
        v1,v2
        / | \
       c1 c2 c3
   按照顺序排列
        c1 < v1 < c2 < v2 < c3
2. 平衡状态下, 左右高度相同, 在极端情况下, 假设root, 所有左子节点都有2个v和3个c, 所有右子节点都是满二叉树
    那么: 
    左子树, 每层节点数=3^h, 每层v数量=2*3^h
    右子树, 每层节点数=2^h, 每层v数量=2^h
    左子树每层v数量-右子树每层v数量=
        h=1, 多 2
        h=2, 多 4
        h=3, 多 46
    由于高度相同, 所以查找效率相同, 从而达到平衡的目的
3. 23树变成2叉树, 方式有很多, 这里采用变成红黑树的方式
        v1,v2
        / | \
       c1 c2 c3
       将左节点分出, 转变为
         v1 == v2
        /  \    \
       c1  c2   c3
       最终转变为
           v2
          // \
         v1   c3
        /  \
       c1  c2
    上图中, // 双线链指向的子节点表示为红色节点, 其他节点表示为黑色节点
 */
public class Tree04_23_base<V extends Comparable<V>> {

    private static final int N = 3;
    /**
     * 根节点
     */
    private Tree04_23_base<V> root = new Tree04_23_base<>();
    // 该结点的父节点
    private Tree04_23_base<V> parent;
    // 获得某个位置的子树 子节点, 子节点有3个, 分别是左子节点, 中间子节点和右子节点, index 0为左指数, 1为中子树, 2为右子树
    private final Tree04_23_base<V>[] childNodes = new Tree04_23_base[N];
    // 代表结点保存的数据(为一个或者两个) index 0为左, 1为右
    private final V[] values = (V[]) new Object[N - 1];
    // 结点保存的数据个数 返回结点中键值对的数量, 空则返回-1
    private int itemNum = 0;

    /**
     * 查找含有v的键值对
     *
     * @param v
     * @return 返回键值对中的value
     */
    public V find(V v) {
        Tree04_23_base<V> curNode = root;
        int childNum;
        while (true) {
            if ((childNum = curNode.findItem(v)) != -1) {
                return curNode.values[childNum];
            }
            // 假如到了叶子节点还没有找到, 则树中不包含v
            else if (curNode.isLeaf()) {
                return null;
            } else {
                curNode = getNextChild(curNode, v);
            }
        }
    }

    /**
     * 最重要的插入函数
     *
     * @param v
     */
    public void insert(V v) {
        Tree04_23_base<V> curNode = root;
        // 一直找到叶节点
        while (true) {
            if (curNode.isLeaf()) {
                break;
            } else {
                curNode = getNextChild(curNode, v);
                for (int i = 0; i < curNode.itemNum; i++) {
                    // 假如v在node中则进行更新
                    if (curNode.values[i].compareTo(v) == 0) {
                        curNode.values[i] = v;
                        return;
                    }
                }
            }
        }

        // 若插入v的结点已经满了, 即3-结点插入
        if (curNode.isFull()) {
            split(curNode, v);
        }
        // 2-结点插入
        else {
            // 直接插入即可
            curNode.insertData(v);
        }
    }

    /**
     * 判断是否是叶子结点
     *
     * @return
     */
    private boolean isLeaf() {
        // 假如不是叶子结点. 必有左子树(可以想一想为什么?)
        return childNodes[0] == null;
    }

    /**
     * 判断结点储存数据是否满了
     * (也就是是否存了两个键值对)
     *
     * @return
     */
    private boolean isFull() {
        return itemNum == N - 1;
    }

    /**
     * 返回该节点的父节点
     *
     * @return
     */
    private Tree04_23_base<V> getParent() {
        return this.parent;
    }

    /**
     * 将子节点连接
     *
     * @param index 连接的位置(左子树, 中子树, 还是右子树)
     * @param child
     */
    private void connectChild(int index, Tree04_23_base<V> child) {
        childNodes[index] = child;
        if (child != null) {
            child.parent = this;
        }
    }

    /**
     * 解除该节点和某个结点之间的连接
     *
     * @param index 解除链接的位置
     * @return
     */
    private Tree04_23_base<V> disconnectChild(int index) {
        Tree04_23_base<V> temp = childNodes[index];
        childNodes[index] = null;
        return temp;
    }

    /**
     * 寻找v在结点的位置
     *
     * @return 结点没有v则放回-1
     */
    private int findItem(V v) {
        for (int i = 0; i < itemNum; i++) {
            if (values[i] == null) {
                break;
            } else if (values[i].compareTo(v) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 向结点插入键值对: 前提是结点未满
     *
     * @param v
     * @return 返回插入的位置 0或则1
     */
    private int insertData(V v) {
        itemNum++;
        for (int i = N - 2; i >= 0; i--) {
            if (values[i] == null) {
                continue;
            }
            if (v.compareTo(values[i]) < 0) {
                values[i + 1] = values[i];
            } else {
                values[i + 1] = v;
                return i + 1;
            }
        }
        values[0] = v;
        return 0;
    }

    /**
     * 移除最后一个键值对(也就是有右边的键值对则移右边的, 没有则移左边的)
     *
     * @return 返回被移除的键值对
     */
    private V removeItem() {
        V temp = values[itemNum - 1];
        values[itemNum - 1] = null;
        itemNum--;
        return temp;
    }

    /**
     * 在v的条件下获得结点的子节点（可能为左子结点, 中间子节点, 右子节点）
     *
     * @param node
     * @param v
     * @return 返回子节点, 若结点包含v,则返回传参结点
     */
    private Tree04_23_base<V> getNextChild(Tree04_23_base<V> node, V v) {
        for (int i = 0; i < node.itemNum; i++) {
            if (node.values[i].compareTo(v) > 0) {
                return node.childNodes[i];
            } else if (node.values[i].compareTo(v) == 0) {
                return node;
            }
        }
        return node.childNodes[node.itemNum];
    }

    /**
     * 这个函数是裂变函数, 主要是裂变结点. 
     * 这个函数有点复杂, 我们要把握住原理就好了
     *
     * @param node 被裂变的结点
     * @param v    要被保存的键值对
     */
    private void split(Tree04_23_base<V> node, V v) {
        Tree04_23_base<V> parent = node.getParent();
        // newNode用来保存最大的键值对
        Tree04_23_base<V> newNode = new Tree04_23_base<V>();
        // newNode2用来保存中间v的键值对
        Tree04_23_base<V> newNode2 = new Tree04_23_base<V>();
        V mid;

        if (v.compareTo(node.values[0]) < 0) {
            newNode.insertData(node.removeItem());
            mid = node.removeItem();
            node.insertData(v);
        } else if (v.compareTo(node.values[1]) < 0) {
            newNode.insertData(node.removeItem());
            mid = v;
        } else {
            mid = node.removeItem();
            newNode.insertData(v);
        }
        if (node == root) {
            root = newNode2;
        }
        /**
         * 将newNode2和node以及newNode连接起来
         * 其中node连接到newNode2的左子树, newNode
         * 连接到newNode2的右子树
         */
        newNode2.insertData(mid);
        newNode2.connectChild(0, node);
        newNode2.connectChild(1, newNode);
        /**
         * 将结点的父节点和newNode2结点连接起来
         */
        connectNode(parent, newNode2);
    }

    /**
     * 链接node和parent
     *
     * @param parent
     * @param node   node中只含有一个键值对结点
     */
    private void connectNode(Tree04_23_base<V> parent, Tree04_23_base<V> node) {
        V v = node.values[0];
        if (node == root) {
            return;
        }
        // 假如父节点为3-结点
        if (parent.isFull()) {
            // 爷爷结点（爷爷救葫芦娃）
            Tree04_23_base<V> gParent = parent.getParent();
            Tree04_23_base<V> newNode = new Tree04_23_base<V>();
            Tree04_23_base<V> temp1, temp2;
            V itemData;

            if (v.compareTo(parent.values[0]) < 0) {
                temp1 = parent.disconnectChild(1);
                temp2 = parent.disconnectChild(2);
                newNode.connectChild(0, temp1);
                newNode.connectChild(1, temp2);
                newNode.insertData(parent.removeItem());

                itemData = parent.removeItem();
                parent.insertData(itemData);
                parent.connectChild(0, node);
                parent.connectChild(1, newNode);
            } else if (v.compareTo(parent.values[1]) < 0) {
                temp1 = parent.disconnectChild(0);
                temp2 = parent.disconnectChild(2);
                Tree04_23_base<V> tempNode = new Tree04_23_base<V>();

                newNode.insertData(parent.removeItem());
                newNode.connectChild(0, newNode.disconnectChild(1));
                newNode.connectChild(1, temp2);

                tempNode.insertData(parent.removeItem());
                tempNode.connectChild(0, temp1);
                tempNode.connectChild(1, node.disconnectChild(0));

                parent.insertData(node.removeItem());
                parent.connectChild(0, tempNode);
                parent.connectChild(1, newNode);
            } else {
                itemData = parent.removeItem();

                newNode.insertData(parent.removeItem());
                newNode.connectChild(0, parent.disconnectChild(0));
                newNode.connectChild(1, parent.disconnectChild(1));
                parent.disconnectChild(2);
                parent.insertData(itemData);
                parent.connectChild(0, newNode);
                parent.connectChild(1, node);
            }
            // 进行递归
            connectNode(gParent, parent);
        }
        // 假如父节点为2结点
        else {
            if (v.compareTo(parent.values[0]) < 0) {
                Tree04_23_base<V> tempNode = parent.disconnectChild(1);
                parent.connectChild(0, node.disconnectChild(0));
                parent.connectChild(1, node.disconnectChild(1));
                parent.connectChild(2, tempNode);
            } else {
                parent.connectChild(1, node.disconnectChild(0));
                parent.connectChild(2, node.disconnectChild(1));
            }
            parent.insertData(node.values[0]);
        }
    }
}
