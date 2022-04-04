package org.song.algorithm.base._01datatype._01base._04tree.trie;

import org.song.algorithm.base._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.base._01datatype._02high.hashmap._01base.AbstractMap;
import org.song.algorithm.base._01datatype._02high.hashmap._01base.HashMap_base_05;

public class TrieBase<V> {

    protected Node<V> root = new Node<>();
    protected int size;

    public V get(String key) {
        char[] chars = key.toCharArray();
        Node<V> node = root;
        for (int i = 0; i < chars.length; i++) {
            node = node.children.get(chars[i]);
            if (node == null) {
                return null;
            }
        }
        return node.word ? node.val : null;
    }

    public V put(String key, V val) {
        char[] chars = key.toCharArray();
        Node<V> node = root;
        for (int i = 0; i < chars.length; i++) {
            HashMap_base_05<Character, Node<V>> children = node.children;
            Node<V> childNode = children.get(chars[i]);
            if (childNode == null) {
                children.put(chars[i], node = new Node<>());
            } else {
                node = childNode;
            }
        }
        if (node.word) {
            V oldVal = node.val;
            node.val = val;
            return oldVal;
        }
        size++;
        node.word = true;
        node.val = val;
        return null;
    }
    
    public static class Node<V> {
        HashMap_base_05<Character, Node<V>> children = new HashMap_base_05<>();
        boolean word;
        V val;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size=").append(size).append("\r\n");
        toString(root, 0, sb);
        return sb.toString();
    }

    public void toString(Node<V> node, int level, StringBuilder sb) {
        if (node == null) {
            return ;
        }
        ArrayBase01<AbstractMap.Entry<Character, Node<V>>> array = node.children.toList();
        for (int i = 0; i < array.length(); i++) {
            AbstractMap.Entry<Character, Node<V>> nodeEntry = array.get(i);
            String append = append(level * 2, " ");
            sb.append(append)
                    .append(level).append("=").append(nodeEntry.k);
            if (nodeEntry.val.word) {
                sb.append(" {").append(nodeEntry.val.val).append("}");
            }
            toString(nodeEntry.val, level + 1, sb.append("\r\n"));
        }
    }

    private String append(int times, String content) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(content);
        }
        return sb.toString();
    }
}
