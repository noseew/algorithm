package org.song.algorithm.base._01datatype._01base._05graph._01model.listgraph;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 采用邻接表实现的图
 * 注意不是严格意义上标准的邻接表实现, 这里是线上使用了一些更方便的实现写法;
 * 算法本身就这样, 理论上的实现只是一个模板, 不同语言不同功能的实现会有自己的调整
 *
 * @param <V>
 * @param <E>
 */
public class AdjacencyList<V, E extends Comparable<E>> extends ListGraph<V, E> {
    /**
     * 顶点的集合
     * key=顶点的值
     * val=顶点Node
     */
    private Map<V, Vertex<V, E>> vertices = new HashMap<>();
    /**
     * 边的集合
     * 元素=边的Node
     */
    private Set<Edge<V, E>> edges = new HashSet<>();

    /**
     * 添加一个顶点
     * 由于不用考虑顶点和边的关系, 所以直接添加到顶点集合就行
     * 
     * @param v
     */
    @Override
    public void addVertex(V v) {
        if (vertices.containsKey(v)) {
            return;
        }
        vertices.put(v, new Vertex(v));
    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    /**
     * 添加一条边
     * 描述一条边最主要两个元素, 就是from顶点和to顶点
     * 
     * @param from
     * @param to
     * @param wight
     */
    @Override
    public void addEdge(V from, V to, E wight) {
        // 先找到连个顶点, 如果顶点不存在, 则创建
        
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null) {
            fromVertex = new Vertex(from);
            vertices.put(from, fromVertex);
        }
        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) {
            toVertex = new Vertex(to);
            vertices.put(to, toVertex);
        }

        // 构建这条边
        Edge<V, E> edge = new Edge(fromVertex, toVertex);
        edge.wight = wight;

        // 更新这条边的信息(权值)
        
        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }

        fromVertex.outEdges.add(edge);
        toVertex.inEdges.add(edge);
        edges.add(edge);
    }

    /**
     * 删除一个顶点
     * 
     * 需要删除当前顶点所关联的边, 该边的引用存储在3个地方, 1:当前顶点,2:当前顶点边所关联的另一个顶点,3:边的集合
     * 删除只需要删除,2和3即可, 1的指针会随着1的删除而删除
     * 
     * @param v
     */
    @Override
    public void removeVertex(V v) {
        Vertex<V, E> vertex = vertices.remove(v);
        if (vertex == null) {
            return;
        }

        // 删除这个顶点的  入度边 和 入度边所关联顶点的出度边, 当前顶点出入度所关联的边会随着当前顶点删除而断开引用
        vertex.inEdges.forEach(e -> {
            edges.remove(e);
            e.from.outEdges.remove(e);
        });
        // 删除这个顶点的  出度边 和 出度边所关联顶点的入度边, 当前顶点出入度所关联的边会随着当前顶点删除而断开引用
        vertex.outEdges.forEach(e -> {
            edges.remove(e);
            e.to.inEdges.remove(e);
        });
        
    }

    /**
     * 删除一条边
     * 
     * @param from
     * @param to
     */
    @Override
    public void removeEdge(V from, V to) {
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null) {
            return;
        }
        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) {
            return;
        }
        // 构建这条边
        Edge<V, E> edge = new Edge(fromVertex, toVertex);

        // 从这条边两个顶点 度列表中删除这条边
        fromVertex.outEdges.remove(edge);
        toVertex.inEdges.remove(edge);
        // 从边集合中删除这条边
        edges.remove(edge);

    }

    @Override
    public int edgeSize() {
        return edges.size();
    }

    @Override
    public int vertices() {
        return vertices.size();
    }

    @Override
    public void dfs(V begin, Predicate<V> goon) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;
        
        dfsStack(beginVertex, goon);
//        dfsRecursion(beginVertex, goon);
    }
    
    private void dfsStack(Vertex<V, E> beginVertex, Predicate<V> goon) {
        Set<Vertex<V, E>> visited = new HashSet<>();
        Stack<Vertex<V, E>> stack = new Stack<>();
        stack.push(beginVertex);
        visited.add(beginVertex);
        if (!goon.test(beginVertex.value)) return;
        
        while (!stack.isEmpty()) {
            Vertex<V, E> vertex = stack.pop();
            
            for (Edge<V, E> outEdge : vertex.outEdges) {
                // 重复访问校验
                if (visited.contains(outEdge.to)) continue;
                
                stack.push(vertex);
                stack.push(outEdge.to);
                visited.add(outEdge.to);
                if (!goon.test(outEdge.to.value)) return;
                break;
            }
        }
    }
    
    private void dfsRecursion(Vertex<V, E> beginVertex, Predicate<V> goon) {
        // 已访问记录
        Set<Vertex<V, E>> visited = new HashSet<>();
        visited.add(beginVertex);
        dfsRecursion(beginVertex, visited, goon);
    }

    /**
     * 采用递归的方式, 深度优先搜索
     * 
     * @param vertex
     * @param visited
     * @param goon
     */
    private void dfsRecursion(Vertex<V, E> vertex, Set<Vertex<V, E>> visited, Predicate<V> goon) {
        if (!goon.test(vertex.value)) {
            // 遍历的具体操作
            return;
        }
        // visited.add(vertex); // 放在这个地方也可以
        for (Edge<V, E> outEdge : vertex.outEdges) {
            // 重复访问校验
            if (visited.contains(outEdge.to)) {
                continue;
            }
            visited.add(outEdge.to);
            dfsRecursion(outEdge.to, visited, goon);
        }
    }

    @Override
    public void bfs(V begin, Predicate<V> goon) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) {
            return;
        }

        // 已访问记录
        Set<Vertex<V, E>> visited = new HashSet<>();
        // 协调队列
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        // 从给定的顶点开始遍历
        queue.offer(beginVertex);
        visited.add(beginVertex);

        while (!queue.isEmpty()) {
            // 出队
            Vertex<V, E> vertex = queue.poll();
            if (!goon.test(vertex.value)) {
                // 遍历的具体操作
                break;
            }
            // visited.add(vertex); // 放在这个地方也可以
            for (Edge<V, E> outEdge : vertex.outEdges) {
                // 重复访问校验
                if (visited.contains(outEdge.to)) {
                    continue;
                }
                // 将下一步直接连接顶点入队
                queue.offer(outEdge.to);
                visited.add(outEdge.to);
            }
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("顶点:");
        String vertexSet = vertices.keySet().stream().map(Object::toString).collect(Collectors.joining(","));
        sb.append("[").append(vertexSet).append("]\r\n");
        sb.append("边:\r\n");
        AtomicInteger count = new AtomicInteger(1);
        edges.forEach(e -> sb.append(count.getAndIncrement()).append(":").append(e).append("\r\n"));

        return sb.toString();
    }

}
