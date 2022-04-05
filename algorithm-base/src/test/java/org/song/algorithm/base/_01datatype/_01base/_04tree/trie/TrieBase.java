package org.song.algorithm.base._01datatype._01base._04tree.trie;

import org.song.algorithm.base._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.base._01datatype._02high.hashmap._01base.AbstractMap;
import org.song.algorithm.base._01datatype._02high.hashmap._01base.HashMap_base_05;

/*
Trie/字典树/前缀树
属于多叉树, 作用主要用于前缀搜索, 非前缀搜索不建议使用
缺点: 空间换时间, 空间占用比较多

使用方式: 
1. 向前缀树中存储字符串,
2. 从前缀树中获取字符串, 查询效率只和字符串的长度成正比, O(k), k=字符串长度
3. 从前缀树中删除字符串, 
4. 通过字符串前缀搜索

这里采用 k-v 形式, 其中的字典就是k, v可以不用, 如果存储了v, 可以通过k来获取, 类似于HashMap
 */
public class TrieBase<V> {

    protected Node<V> root = new Node<>(null, null);
    protected int size;

    /**
     * 根据前缀key获取val
     * 
     * @param key
     * @return
     */
    public V get(String key) {
        Node<V> node = getNode(key);
        return node != null && node.word ? node.val : null;
    }

    /**
     * 是否存在以key开头的字符串, 前缀匹配
     * 
     * @param key
     * @return
     */
    public boolean startWith(String key) {
        Node<V> node = getNode(key);
        return node != null;
    }

    /**
     * 前缀模糊搜索
     * 
     * @param key
     * @return
     */
    public ArrayBase01<String> startMatch(String key) {
        int limit = 10; // 最多匹配数, 防止查询数据过多, 或递归过深
        ArrayBase01<String> array = new ArrayBase01<>();
        Node<V> node = getNode(key);
        if (node == null) {
            return array;
        }
        appendChildren(key, node, array, limit, 3);
        return array;
    }

    /**
     * 存储k\v
     * 
     * @param key
     * @param val
     * @return
     */
    public V put(String key, V val) {
        char[] chars = key.toCharArray();
        Node<V> node = root;
        for (int i = 0; i < chars.length; i++) {
            HashMap_base_05<Character, Node<V>> children = node.children;
            Node<V> childNode = children.get(chars[i]);
            if (childNode == null) {
                children.put(chars[i], node = new Node<>(node, chars[i]));
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
    
    public V remove(String key) {
        Node<V> node = getNode(key);
        if (node == null || !node.word) {
            // key 不存在
            return null;
        }
        size--;
        V oldVal = node.val;
        node.val = null; // 删除val
        node.word = false; // 删除标记

        Node<V> parent = node;
        while (parent != null) {
            if (node.children.size() > 0) {
                // 该节点存在子节点, 不能删除
                break;
            }
            // 把该节点从父节点中删除
            parent.children.remove(node.c);
            // 向上查找
            parent = parent.parent;
        }
        return oldVal;
    }
    
    public int size() {
        return size;
    }
    
    protected Node<V> getNode(String key) {
        char[] chars = key.toCharArray();
        Node<V> node = root;
        for (int i = 0; i < chars.length; i++) {
            node = node.children.get(chars[i]);
            if (node == null) {
                return null;
            }
        }
        return node;

    }

    /**
     * 递归拼接模糊的前缀匹配key
     * 待优化
     *
     * @param key       前缀key
     * @param node      待处理的node
     * @param array     存储结果的数组
     * @param limitSize 限制最大数量
     * @param limitDeep 限制最大深度, 防止递归过深
     */
    protected void appendChildren(String key, Node<V> node, ArrayBase01<String> array, int limitSize, int limitDeep) {
        if (array.length() >= limitSize || node == null || limitDeep <= 0) {
            return;
        }
        if (node.word) {
            // 如果本身是key, 则直接添加
            limitSize--;
            array.add(key);
        }

        ArrayBase01<AbstractMap.Entry<Character, Node<V>>> cArray = node.children.toList();
        for (int i = 0; i < cArray.length() && i < limitSize; i++) {
            if (cArray.get(i).val.word) {
                limitSize--;
                array.add(key + cArray.get(i).val.c);
            }
        }
        // 广度优先递归
        for (int i = 0; i < cArray.length(); i++) {
            appendChildren(key + cArray.get(i).val.c, cArray.get(i).val, array, limitSize, limitDeep - 1);
        }
    }


    /*
    字典树的node
    
    一个node表示字符串中的一个字符, 但是这个字符并没有显示的存储在node中, 而是存储在其父节点的Hash的key中
    删除的时候, 为了方便还是需要将其显示的存储在node中
    同时为了删除方便, node需要增加一个parent指针
    
    为什么使用HashMap, 主要是效率较高且编码方便, 缺点是空间占用变高了
    当然也可以使用数组来存储字符, 降低空间占用, 这样可以通过字符的顺序二分查找来定位他, 编码稍微麻烦些
    
    word表示截止到当前节点, 存在一个完整的字符串, 用于匹配字符串, 防止公共前缀字符串无法区分的情况
     */
    public static class Node<V> {
        HashMap_base_05<Character, Node<V>> children = new HashMap_base_05<>();
        boolean word;
        V val;
        // 当前节点所表示的字符, 方便删除
        Character c;
        // 父节点指针, 方便删除
        Node<V> parent;

        Node(Node<V> parent, Character c) {
            this.parent = parent;
            this.c = c;
        }
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
