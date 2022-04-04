package org.song.algorithm.base._01datatype._01base._01linear.list._01model.node;

public class DuplexNode<T> extends SingleNode<T> {
    public SingleNode<T> prev;

    public DuplexNode(SingleNode<T> prev, SingleNode<T> next, T value) {
        super(next, value);
        this.prev = prev;
    }
}