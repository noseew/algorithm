package org.song.algorithm.base._01datatype._01base._05graph._01model.listgraph;

import org.song.algorithm.base._01datatype._01base._04tree.heap.Heap_base_01;
import org.song.algorithm.base._01datatype._01base._04tree.heap.Heap_base_03;
import org.song.algorithm.base._01datatype._02high.unionfindsets.UFS;
import org.song.algorithm.base._01datatype._02high.unionfindsets.common.UFSQuickUnion_PathHalve;

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
public class AdjacencyList<V, E> extends ListGraph<V, E> {
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

    protected AdjacencyList(EdgeOpr<E> edgeOpr) {
        super(edgeOpr);
    }

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
    /*
    参考卡恩算法实现的拓扑排序
    现实中不可能真的删除顶点, 这里采用模拟删除, 也就是记录每次删除后的变化
    删除后的变化其实就是, 其他顶点的入度数量发生了变化
    inDegreeMap: 记录动态的每个顶点的入度数量
    degree0: 每次遍历, 当有顶点入度数量变成0时, 进入次队列
    sorted: 从degree0队列中依次取出的顶点, 就是排好序的顶点
     */
    @Override
    public List<V> topologySort() {
        // 返回排序后的结果
        List<V> sorted = new ArrayList<>();
        // 用于记录每个顶点动态入度数量
        Map<Vertex<V, E>, Integer> inDegreeMap = new HashMap<>();
        // 用于记录所有入度为0的顶点, 入度为0的顶点意味着该顶点将要"删除", 同时也意味着接下来的排序是他们
        Queue<Vertex<V, E>> degree0 = new LinkedList<>();
        
        // 初始化入度的数量
        vertices.forEach((k, v) -> {
            int ind = v.inEdges.size();
            if (ind == 0) {
                // 找到初始入度为0的顶点
                degree0.offer(v);
            } else {
                // 记录所有顶点入度数量
                inDegreeMap.put(v, ind);
            } 
        });

        while (!degree0.isEmpty()) {
            Vertex<V, E> vertex = degree0.poll();
            sorted.add(vertex.value);

            // 模拟删除一个顶点
            vertex.outEdges.forEach(e -> {
                int newSize = inDegreeMap.get(e.to) - 1;
                if (newSize == 0) {
                    // 如果顶点数量变成0, 则放入队列, 等待队列取出并排序
                    degree0.offer(e.to);
                } else {
                    // 更新剩下顶点的入度数量
                    inDegreeMap.put(e.to, newSize);
                }
            });
        }
        return sorted;
    }

    @Override
    public Set<EdgeInfo<V, E>> mst() {

//        return prim();
        return kruskal();
    }

    /*
    prim算法, 计算最小生成树
    1. 切分定理
        用一条线将图分割成两个部分, 该条线所穿过的1条或多条边中, 权重最小的那条边一定是最小生成树中的一条边
        
    prim算法就是根据切分定理来实现的
    集合S是切分已计算的顶点集合, 
    1. 从某一个顶点开始切割, 将该顶点记录在S中, 则从S和该图其他顶点之间(非S)的边选出权重最小的边, 加入最小生成树集合中
    2. 以此类推, 重复步骤1, 直到最小生成树中的边数等于该图顶点数-1, 则最小生成树完成
    注意: 过程中需要防止边的重复添加
    
     */
    /**
     * prim 算法, 返回最小生成树
     * 
     * @return
     */
    private Set<EdgeInfo<V, E>> prim() {

        Set<Edge<V, E>> minEdges = new HashSet<>();

        // 从任意一个顶点开始 切分
        Vertex<V, E> nextVertex = vertices.values().stream().findFirst().get();
        // 采用堆进行排序, 提高效率
        Heap_base_03<Edge<V, E>> heap = new Heap_base_03<>(true, comparator, nextVertex.outEdges);

        while (!heap.isEmpty()) {
            Edge<V, E> edge = heap.pop();
            if (minEdges.contains(edge)) {
                // 过滤掉重复的边
                continue;
            }
            // 添加本次切分中, 最小的边
            minEdges.add(edge);
            // 增加下次需要切分边
            heap.addAll(edge.to.outEdges);
        }
        return minEdges.stream()
                .map(e -> new EdgeInfo<>(e.from.value, e.to.value, e.wight))
                .collect(Collectors.toSet());
        
    }

    /*
    kruskal算法, 计算最小生成树
    1. 根据prim的切分定理, 能得到
        1. 所有边的集合中, 权重最小的(和次小的)一定是最小生成树中的一条边
            根据切分定理, 最小的和次小的一定会在某次切分中出现且是该次切分中最小的边
        2. 去掉那些能够构成环的边, 则这些最小的边就是最小生成树的边(最小边数要等于顶点数-1)
            最小边中, 可能会出现环, 所以去掉那些环, 之后的边依然满足1
    
    kruskal算法, 就是将所有的边从小到大排序, 去除掉能构成环的边之后, 边数到达定点数-1, 既是最小生成树
    如何判断是否会构成环呢? 采用并查集
     */
    /**
     * kruskal 算法, 返回最小生成树
     * 
     * @return
     */
    private Set<EdgeInfo<V, E>> kruskal() {

        Set<Edge<V, E>> minEdges = new HashSet<>();
        
        // 采用堆进行排序, 提高效率
        Heap_base_03<Edge<V, E>> heap = new Heap_base_03<>(true, comparator, edges);
        // 将所有顶点加入并查集, 各自成为独立集合
        UFS<Vertex<V, E>> ufs = new UFSQuickUnion_PathHalve<>(vertices.values());

        while (!heap.isEmpty() && minEdges.size() < vertices.size() - 1) {
            Edge<V, E> edge = heap.pop();
            if (ufs.isSame(edge.from, edge.to)) {
                // 如果两个顶点是同一个集合, 则会成为环
                continue;
            }

            minEdges.add(edge);
            // 将两个顶点合并到一个集合
            ufs.union(edge.from, edge.to);
        }
        return minEdges.stream()
                .map(e -> new EdgeInfo<>(e.from.value, e.to.value, e.wight))
                .collect(Collectors.toSet());
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
