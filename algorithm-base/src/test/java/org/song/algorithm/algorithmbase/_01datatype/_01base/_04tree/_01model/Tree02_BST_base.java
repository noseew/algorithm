package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import java.util.Comparator;

public class Tree02_BST_base<V> {

    private int size;

    private Comparator<V> comparator;

    public TreeNode<V> root;

    public void push(V v) {
        if (root == null) {
            root = new TreeNode<>( null, null, v);
            size++;
            return;
        }

        TreeNode<V> parent = root;
        while (true) {
            TreeNode<V> next;
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

    private void put(TreeNode<V> parent, V v) {
        TreeNode<V> newNode = new TreeNode<>( null, null, v);
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

}
