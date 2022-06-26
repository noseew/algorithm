package org.song.algorithm.base._01datatype._02high.unionfindsets.common;

import org.song.algorithm.base._01datatype._02high.hashmap._01base.HashMap_base_05;
import org.song.algorithm.base._01datatype._02high.unionfindsets.UFS;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
采用泛型对象作为并查集的元素 TODO 未完成

采用链表而非数组实现来实现集合
 */
public abstract class AbstractUFS<T> implements UFS<T> {

    protected HashMap_base_05<T, Node<T>> data;

    protected AbstractUFS(int capacity) {
        data = new HashMap_base_05<T, Node<T>>(null, capacity);
    }

    protected AbstractUFS(Collection<T> collection) {
        data = new HashMap_base_05<T, Node<T>>(null, collection.size());
        for (T t : collection) {
            add(t);
        }
    }

    public void add(T t) {
        Node<T> node = new Node<>();
        node.val = t;
        data.put(t, node);
    }

    public abstract T findRoot(T t);

    protected abstract Node<T> findRootNode(T t);

    public abstract void union(T t1, T t2);
    
    public boolean isSame(T t1, T t2) {
        return Objects.equals(findRoot(t1), findRoot(t2));
    }
    
    public int size() {
        return data.size();
    }

    protected void validRange(T n) {
        Assert.isTrue(data.get(n) != null, "数据不存在");
    }

    protected T parentOf(T n) {
        return parentNodeOf(n).val;
    }

    protected Node<T> parentNodeOf(T n) {
        validRange(n);
        return data.get(n).parent;
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

    @Override
    public String toString() {
        return data.toString(); 
    }

    public class Node<T> {
        T val;
        Node<T> parent = this;
        int rank = 1;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{").append(val).append("->").append(parent.val).append(", rank:").append(rank).append("}");
            return sb.toString();
        }
    }
}
