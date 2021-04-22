package org.song.algorithm.algorithmbase.datatype.tree.model;

import java.util.Comparator;

public class BSTree_base<V> {

    private int size;

    private Comparator<V> comparator;

    private BSTreeNode<V> root;

    public void push(V v) {
        if (root == null) {
            root = new BSTreeNode<>(null, null, null, v);
            size++;
            return;
        }

        BSTreeNode<V> parent = root;
        while (true) {
            BSTreeNode<V> next;
            if (comparator != null) {
                if (comparator.compare(v, parent.v) < 0) {
                    next = parent.left;
                } else {
                    next = parent.right;
                }
            } else {
                if (((Comparable) v).compareTo(((Comparable) parent.v)) < 0) {
                    next = parent.left;
                } else {
                    next = parent.right;
                }
            }
            if (next == null) {
                break;
            }
            parent = next;
        }

        put(parent, v);
        size++;
    }

    private void put(BSTreeNode<V> parent, V v) {
        BSTreeNode<V> newNode = new BSTreeNode<>(parent, null, null, v);
        if (comparator != null) {
            if (comparator.compare(v, parent.v) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        } else {
            if (((Comparable) v).compareTo(((Comparable) parent.v)) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }
    }

    static class BSTreeNode<V> {

        BSTreeNode<V> parent;
        BSTreeNode<V> left;
        BSTreeNode<V> right;
        V v;

        BSTreeNode(BSTreeNode<V> parent, BSTreeNode<V> left, BSTreeNode<V> right, V v) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.v = v;
        }

    }

}
