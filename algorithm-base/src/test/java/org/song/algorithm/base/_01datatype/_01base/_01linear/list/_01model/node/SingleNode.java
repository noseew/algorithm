package org.song.algorithm.base._01datatype._01base._01linear.list._01model.node;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SingleNode<T> {
    public SingleNode<T> next;
    public T value;

    @Override
    public String toString() {
        return value.toString();
    }
}