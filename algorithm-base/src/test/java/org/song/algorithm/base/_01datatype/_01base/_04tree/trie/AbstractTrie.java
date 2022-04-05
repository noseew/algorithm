package org.song.algorithm.base._01datatype._01base._04tree.trie;

import org.song.algorithm.base._01datatype._01base._01linear.list._01model.ArrayBase01;

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
public abstract class AbstractTrie<V> {

    /**
     * 根据前缀key获取val
     *
     * @param key
     * @return
     */
    public abstract V get(String key);

    /**
     * 是否存在以key开头的字符串, 前缀匹配
     *
     * @param key
     * @return
     */
    public abstract boolean startWith(String key);

    /**
     * 前缀模糊搜索
     *
     * @param key
     * @return
     */
    public abstract ArrayBase01<String> startMatch(String key);

    /**
     * 存储 k-v
     *
     * @param key
     * @param val
     * @return
     */
    public abstract V put(String key, V val);

    public abstract V remove(String key);

    public abstract int size();
}
