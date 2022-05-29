package org.song.algorithm.base._01datatype._02high.unionfindsets.common;

import org.song.algorithm.base._01datatype._02high.unionfindsets.UFS;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
采用泛型对象作为并查集的元素 TODO 未完成

采用链表而非数组实现来实现集合
 */
public abstract class AbstractUFS<T> implements UFS<T> {

    protected List<Node<T>> dataList;
    protected Map<T, Node<T>> data;
    protected int capacity;

    protected AbstractUFS(int capacity) {
        data = new HashMap<>(capacity);
        this.capacity = capacity;
    }

    public abstract T findRoot(T t);

    public abstract void union(T t1, T t2);
    
    public boolean isSame(T t1, T t2) {
        return findRoot(t1) == findRoot(t2);
    }

    protected void validRange(T n) {
        Assert.isTrue(data.containsKey(n), "数据不存在");
    }

    protected T parentOf(T n) {
        validRange(n);
        return data.get(n).parent.val;
    }
    
    protected void setParent(T n, T p) {
        data.get(n).parent = data.get(p);
    }

    protected int rankOf(T n) {
        return data.get(n).rank;
    }

    protected void addRank(T n, int add) {
        data.get(n).rank += add;
    }
    
    public class Node<T> {
        T val;
        Node<T> parent;
        int rank;
    }
}
