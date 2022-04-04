package org.song.algorithm.algorithmbase._01datatype._01base._04tree.binarytree._01model;

import lombok.AllArgsConstructor;

/*
  BTree 类表示泛型键值对的有序符号表. 它支持 put, get, 包含, size, 和 is-empty 方法.
  符号表实现了 关联数组 抽象:当一个值与符号表中已经存在的键相关联时, 约定是用新值替换旧值.
  与Map, 这个类使用了“值不能是null”的约定——将一个键的值设置为{@code null}相当于将该键从符号表中删除.
  该实现使用b-树. 它要求键类型实现Comparable接口, 并调用compareTo和方法来比较两个键.
  它既不调用 equals 也不调用 hashCode.
  在最坏的情况下,  get, put, 包含 操作, 分别使log_mn探针, 其中 n 是键值对的数目,
  m 是分支因子.  size, is-empty 操作需要常数时间. 施工需要恒定的时间.
  <p>
  附加的文档 https://algs4.cs.princeton.edu/62btree
  算法, 第四版
 */
/**
 * B 树的实现, 代码来自网络
 * https://blog.csdn.net/jimo_lonely/article/details/82716142
 * 
 * https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/BTree.java.html
 */
public class Tree04B02<V extends Comparable<V>> {
    // 每个b树节点的最大子节点数= M-1
    // (必须为偶数且大于2)
    private static final int M = 4;

    private Node root;       // 根节点
    private int height;      // 树的高度
    private int n;           // 元素数量

    public Tree04B02() {
        root = new Node(0);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return n;
    }

    public int height() {
        return height;
    }

    public V get(V v) {
        if (v == null) throw new IllegalArgumentException("argument to get() is null");
        return search(root, v, height);
    }

    private V search(Node x, V v, int ht) {
        Entry[] children = x.children;

        // 外部节点
        if (ht == 0) {
            for (int j = 0; j < x.m; j++) {
                if (eq(v, children[j].V)) return (V) children[j].V;
            }
        }
        // 内部节点
        else {
            for (int j = 0; j < x.m; j++) {
                if (j + 1 == x.m || less(v, children[j + 1].V))
                    return search(children[j].next, v, ht - 1);
            }
        }
        return null;
    }


    /**
     * 将V-value对插入到符号表中，如果V已经在符号表中，则用新值覆盖旧值。如果V的值是{@code null}，这将有效地从符号表中删除V。
     */
    public void put(V v) {
        if (v == null) throw new IllegalArgumentException("argument V to put() is null");
        Node u = insert(root, v, height);
        n++;
        if (u == null) return;

        // 需要拆分根节点
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].V, root);
        t.children[1] = new Entry(u.children[0].V, u);
        root = t;
        height++;
    }

    private Node insert(Node h, V v, int ht) {
        int j;
        Entry t = new Entry(v, null);

        // 外部节点
        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                if (less(v, h.children[j].V)) break;
            }
        }
        // 内部节点
        else {
            for (j = 0; j < h.m; j++) {
                if ((j + 1 == h.m) || less(v, h.children[j + 1].V)) {
                    Node u = insert(h.children[j++].next, v, ht - 1);
                    if (u == null) return null;
                    t.V = u.children[0].V;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.m; i > j; i--)
            h.children[i] = h.children[i - 1];
        h.children[j] = t;
        h.m++;
        if (h.m < M) return null;
        else return split(h);
    }

    // 将节点一分为二
    private Node split(Node h) {
        Node t = new Node(M / 2);
        h.m = M / 2;
        for (int j = 0; j < M / 2; j++)
            t.children[j] = h.children[M / 2 + j];
        return t;
    }

    public String toString() {
        return toString(root, height, "") + "\n";
    }

    private String toString(Node h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                s.append(indent + children[j].V + "\n");
            }
        } else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0) s.append(indent + "(" + children[j].V + ")\n");
                s.append(toString(children[j].next, ht - 1, indent + "     "));
            }
        }
        return s.toString();
    }


    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }

    // B-tree节点数据类型
    private static final class Node {
        private int m;                             // 子节点数量
        private Entry[] children = new Entry[M];   // 子节点数组

        private Node(int k) {
            m = k;
        }
    }

    // 内部节点:只使用key和next
    // 外部节点:只使用key和value
    @AllArgsConstructor
    private static class Entry {
        private Comparable V;
        private Node next;     // helper field to iterate over array entries
    }
}
