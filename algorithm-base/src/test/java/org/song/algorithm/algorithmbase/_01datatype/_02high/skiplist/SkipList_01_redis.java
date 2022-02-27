package org.song.algorithm.algorithmbase._01datatype._02high.skiplist;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 跳表, 模拟redis的跳表实现
 * TODO 未完成
 * 
 * @param <T>
 */
public class SkipList_01_redis<T extends Comparable<T>> {

    private Node<T> header, tail;

    private Node<T>[] level;

    private long length;

    private int maxLevel;

    public T put(T v) {
        Node<T> h = header;
        Node<T> l = header, r;
        for (int i = 0; i < level.length && level[i].forward != null; i++) {
            r = level[i].forward;
            
        }
        return null;
    }


    @Data
    @AllArgsConstructor
    public static class Node<T> {
        // 前进指针
        public Node<T> forward;
        // 后退指针
        public Node<T> backwoard;
        // 值
        public T value;
    }
}
