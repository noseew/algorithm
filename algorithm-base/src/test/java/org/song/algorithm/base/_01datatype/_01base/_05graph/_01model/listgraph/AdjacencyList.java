package org.song.algorithm.base._01datatype._01base._05graph._01model.listgraph;

import org.song.algorithm.base._01datatype._01base._04tree.heap.BinaryHeap_base_03;
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

        /*
        复杂度分析
        prim O(ElogE), E 表示顶点数量
        不过稍微比 kruskal 好点
        kruskal O(ElogE), E 表示顶点数量
        
        效率主要体现在, 堆的选择上, 使用什么堆效果是不一样的, 这里采用的是二叉堆, 其他的还有菲波那切堆
         */
//        return prim();
        return kruskal();
    }

    @Override
    public Map<V, E> shortestPathWight(V begin) {
        Vertex<V, E> vertex = vertices.get(begin);
//        return dijkstraWight(vertex);
        return bellmanFordWight(vertex);
    }

    /*
    从一个顶点到其他顶点的最短路径, 不支持有负权边的图, 该算法不支持
    效率 O(ElogV)
    
    算法思路
        算法思路模拟: 将多个石头用绳线连起来, 线的长度就是权重, 将其平放在桌面上, 如此就构成了一个图
        1. 以其中一个石头(顶点)为起点V1, 慢慢向上提起, 直到有下一个石头V2被带起来; 此时顶点和下一个石头就构成了两个顶点间的最短路径
            下一个被提起的石头, 权重一定是接下来中最小的
        2. 重复第一步, 下一个被带起来的石头V3, 就构成了V1到V3的最短路径
    
    算法记录: 
    1. 记录一个顶点V1到其他顶点的所有最短路径的表
    
        开始顶点 | 目标顶点 | 最短路径 | 最短路径值
        V1     | V2      | V1-V2   | 1
        V1     | V3      | 无穷大   | 无穷大
        其他省略 ...
    
    2. 按照算法思路, 先找到V1第一个提起的顶点V2, 然后将表中的V1-V2路径记录下路径, 并标记为最短路径, 
        1. 标记为最短路径的标志是, 该顶点被提起离开桌面; 
            算法中确定下一个被提起的石头就是在上表中, 除了已经标记了最短路径的其他最短路路径值中取最小的 
        2. 更新最短路径条件是, 随着每次新的石头被提起, 发现新的路径指向目标顶点, 且新的路径长度小于原来路径长度
            比如 V1-V3 = 10, 随着V2被提起, 发现 V1-V2-V3 = 7, 则V1-V3的路径更新为 V1-V2-V3
    
    松弛操作:
        松弛操作其实就是更新最短路径和其权值
        优先能确定的路径权值, (比如V1-V3, 他们是直接相连的, 但是他们的权值并不一定是最短路径), 
        在提起石头的过程中, 新的路径被发现, 发现 V1-V2-V3 比 V1-V3 权值更短, 相当于 V1-V3 这根线边送了, 取而代之的是 V1-V2-V3 这根线变紧了

     */

    /**
     * 返回路径总权重
     *
     * @return
     */
    private Map<V, E> dijkstraWight(Vertex<V, E> beginVertex) {
        /*
        路径表
        key表示 beginVertex->V的路径
        val表示 beginVertex->V的权重, 如果没有表示路径权重无穷大
        初始的时候
         */
        // 路径表
        Map<Vertex<V, E>, E> paths = new HashMap<>();
        beginVertex.outEdges.forEach(edge -> {
            // 初始化路径表, 将起点能直接找到的路径放入, 其他路径未知, 为null, 也就是默认权重无穷大
            paths.put(edge.to, edge.wight);
        });
        // 已经提起来的顶点, 也就是已经确认了是最短路径的顶点
        Map<V, E> selected = new HashMap<>();
        while (!paths.isEmpty()) {
            // 找到下一个被提起来的顶点(本次离开桌面的), 也就是在历史路径权重集合中, 找到最小的权重
            Map.Entry<Vertex<V, E>, E> minPath = minPathWight(paths);
            // 将提起的顶点和其新的路径权重放入, 选中集合中, 相当于标记了该顶点最短路已经确定
            selected.put(minPath.getKey().value, minPath.getValue());
            // 将该顶点从待选择集合中去掉
            paths.remove(minPath.getKey());

            // 随着新的顶点的提起, 将会有更多的路径被发现, 那这些新的路径可能会更新原有的路径权值
            for (Edge<V, E> outEdge : minPath.getKey().outEdges) {
                // 新的路径重复, 忽略
//                if (selected.containsKey(outEdge.to.value) || beginVertex.equals(outEdge.to)) {
                if (selected.containsKey(outEdge.to.value)) {
                    continue;
                }

                dijkstraWightRelax(paths, minPath.getValue(), outEdge);
            }
        }
        // 删除自己的权重
        selected.remove(beginVertex.value);

        return selected;
    }

    /**
     * 松弛操作
     * 松弛就是对 起点->edge.to 这条路径的权重进行重新计算
     * 表现上就是 起点->当前点->当前点.to, 这条路径的权重进行重新计算
     * 由于 顶点->当前点 已经算出, 所以就是对 当前点->当前点.to 进行松弛
     * <p>
     * 起点: begen
     * 当前点: paths.key
     * 当前点.to: edge.to
     * 松弛边就是对 edge 这条边进行松弛, 主义不是字面意思的松弛, 而是 (松弛就是对 起点->edge.to 这条路径的权重进行重新计算)
     *
     * @param paths   待选择路径
     * @param minPath 本次提起的顶点
     * @param edge    本次提起的顶点和目标边
     */
    private void dijkstraWightRelax(Map<Vertex<V, E>, E> paths, E minPath, Edge<V, E> edge) {
        /*
        接下来就是松弛操作, 
        松弛 Edge 表示:
        更新 beginVertex -> Edge.to 的路径的权值
         */
        // 新的路径所产生的新的权值, 
        E newWight = edgeOpr.add(minPath, edge.wight);
        // 判断新的路径有没有对应老的权值, 如果该边的to是新的顶点, 原本没有记录, 则这里直接放入
        E oldWight = paths.get(edge.to);
        if (oldWight == null || edgeOpr.compare(newWight, oldWight) < 0) {
            // 更新路径权值
            paths.put(edge.to, newWight);
        }
    }

    private Map<V, E> bellmanFordWight(Vertex<V, E> beginVertex) {
        Map<V, E> selected = new HashMap<>();
        // 将初始的起始节点加进去, 并赋值权重为0
        selected.put(beginVertex.value, edgeOpr.zero());
        // 循环次数, 边数-1
        boolean relax = true; // 类似排序, 是否提前退出循环
        for (int i = 0; i < edges.size() - 1 && relax; i++) {
            boolean relaxEveryOne = false;
            // 遍历每条边, 进行松弛操作
            for (Edge<V, E> edge : edges) {
                // 改编的from节点, 还没有确定路径, 松弛失败, 等待下次
                E oldWight = selected.get(edge.from.value);
                if (oldWight == null) {
                    continue;
                }
                // 一次遍历, 只要有一次松弛成功, 说明还可以继续松弛
                relaxEveryOne |= bellmanFordWightRelax(selected, oldWight, edge);
            }
            relax = relaxEveryOne;
        }

        // 在松弛一次, 如果还能成功, 说明存在 负权环
        for (Edge<V, E> edge : edges) {
            E oldWight = selected.get(edge.from.value);
            if (oldWight == null) {
                continue;
            }
            boolean relaxEveryOne = bellmanFordWightRelax(selected, oldWight, edge);
            if (relaxEveryOne) {
                System.out.println("存在 负权环");
                return null;
            }
        }


        // 删除自己的权重
        selected.remove(beginVertex.value);
        return selected;
    }

    private boolean bellmanFordWightRelax(Map<V, E> paths, E minPath, Edge<V, E> edge) {
        /*
        接下来就是松弛操作, 
        松弛 Edge 表示:
        更新 beginVertex -> Edge.to 的路径的权值
         */
        // 新的路径所产生的新的权值, 
        E newWight = edgeOpr.add(minPath, edge.wight);
        // 判断新的路径有没有对应老的权值, 如果该边的to是新的顶点, 原本没有记录, 则这里直接放入
        E oldWight = paths.get(edge.to.value);
        if (oldWight == null || edgeOpr.compare(newWight, oldWight) < 0) {
            // 更新路径权值
            paths.put(edge.to.value, newWight);
            return true;
        }
        return false;
    }

    @Override
    public Map<V, PathInfo<V, E>> shortestPath(V begin) {
        Vertex<V, E> vertex = vertices.get(begin);
//        return dijkstraPath(vertex);
        return bellmanFordPath(vertex);
    }

    /**
     * 返回路径加总权重
     * dijkstraWight 的 增强版
     *
     * @param beginVertex
     * @return
     */
    private Map<V, PathInfo<V, E>> dijkstraPath(Vertex<V, E> beginVertex) {
        // 只要进入 paths, 就说明了记录了起点到指定点的权重值
        Map<Vertex<V, E>, PathInfo<V, E>> paths = new HashMap<>();
        beginVertex.outEdges.forEach(edge -> {
            PathInfo<V, E> pathInfo = new PathInfo<>();
            pathInfo.setWight(edge.wight);
            pathInfo.getEdgeInfos().add(edge.toEdgeInfo());
            paths.put(edge.to, pathInfo);
        });
        Map<V, PathInfo<V, E>> selected = new HashMap<>();
        while (!paths.isEmpty()) {
            Map.Entry<Vertex<V, E>, PathInfo<V, E>> minPath = minPathInfo(paths);
            selected.put(minPath.getKey().value, minPath.getValue());
            paths.remove(minPath.getKey());
            for (Edge<V, E> outEdge : minPath.getKey().outEdges) {
                if (selected.containsKey(outEdge.to.value)) {
                    continue;
                }
                // 松弛操作
                dijkstraPathRelax(paths, minPath.getValue(), outEdge);
            }
        }
        selected.remove(beginVertex.value);

        return selected;
    }

    /**
     * 松弛操作
     * 松弛就是对 起点->edge.to 这条路径的权重进行重新计算
     * 表现上就是 起点->当前点->当前点.to, 这条路径的权重进行重新计算
     * 由于 顶点->当前点 已经算出, 所以就是对 当前点->当前点.to 进行松弛
     * <p>
     * 起点: begen
     * 当前点: paths.key
     * 当前点.to: edge.to
     * 松弛边就是对 edge 这条边进行松弛, 主义不是字面意思的松弛, 而是 (松弛就是对 起点->edge.to 这条路径的权重进行重新计算)
     *
     * @param paths   待选择路径
     * @param minPath 本次提起的顶点
     * @param edge    本次提起的顶点和目标边
     */
    private void dijkstraPathRelax(Map<Vertex<V, E>, PathInfo<V, E>> paths, PathInfo<V, E> minPath, Edge<V, E> edge) {
        /*
        新的权重=当前扫描边权重+开始顶点到当前顶点权重
         */
        E newWight = edgeOpr.add(edge.wight, minPath.getWight());
        /*
        松弛操作
        看看原来有没有记录旧的权重, 只要进入 paths, 就说明了记录了起点到指定点的权重值 如果新的权重值小于旧值, 则进行松弛操作
         */
        PathInfo<V, E> oldPathInfo = paths.get(edge.to);
        if (oldPathInfo != null && edgeOpr.compare(newWight, oldPathInfo.getWight()) >= 0) {
            // 新发现的路径并不比原来的路径更短, 忽略
            return;
        }
        if (oldPathInfo == null) {
            // 新发现的路径, 之前没有记录, 所以要重新记录
            oldPathInfo = new PathInfo<>();
            // 表示 起点->edge.to 这条路径
            paths.put(edge.to, oldPathInfo);
        } else {
            /*
            使用新的路径覆盖老的路径, 所以老的路径要清空, 完全采用新的路径
             */
            oldPathInfo.getEdgeInfos().clear();
        }

        // 新的权值
        oldPathInfo.setWight(newWight);
        // 加上当前点确定的已有路径
        oldPathInfo.getEdgeInfos().addAll(minPath.getEdgeInfos());
        // 新增的路径
        oldPathInfo.getEdgeInfos().add(edge.toEdgeInfo());
    }
    
    /*
    算法原理, 基于 dijkstra 的松弛操作实现: 假设图有n条边, 则对图 每条边 进行最多n-1次松弛操作即可
    
    原理讲解
    1. 如果第一次松弛的边from还没有确定边权值, 则松弛失败, 等待下次松弛
    2. 由于每次松弛都是对所有边进行松弛, 因此任何一点总会在某一次中计算到权值(顶点到当前点), 类似于冒牌排序, 
    3. 类似于冒牌排序, 最终所有的边都会进行一次类似"正序"的松弛操作, 从而计算出所有的最短路径
     */
    /**
     * 返回路径加总权重
     * bellmanFord 算法
     *
     * @param beginVertex
     * @return
     */
    private Map<V, PathInfo<V, E>> bellmanFordPath(Vertex<V, E> beginVertex) {
        // bellmanFord 每次循环都是在更新路径权重表
        Map<V, PathInfo<V, E>> selected = new HashMap<>();
        // 将初始的起始节点加进去, 并赋值权重为0
        PathInfo<V, E> b = new PathInfo<>();
        b.setWight(edgeOpr.zero());
        selected.put(beginVertex.value, b);
        // 循环次数, 边数-1
        boolean relax = true; // 类似排序, 是否提前退出循环
        for (int i = 0; i < edges.size() - 1 && relax; i++) {
            boolean relaxEveryOne = false;
            // 遍历每条边, 进行松弛操作
            for (Edge<V, E> edge : edges) {
                // 改编的from节点, 还没有确定路径, 松弛失败, 等待下次
                PathInfo<V, E> pathInfo = selected.get(edge.from.value);
                if (pathInfo == null) {
                    continue;
                }
                // 一次遍历, 只要有一次松弛成功, 说明还可以继续松弛
                relaxEveryOne |= bellmanFordPathRelax(selected, pathInfo, edge);
            }
            relax = relaxEveryOne;
        }

        // 在松弛一次, 如果还能成功, 说明存在 负权环
        for (Edge<V, E> edge : edges) {
            PathInfo<V, E> pathInfo = selected.get(edge.from.value);
            if (pathInfo == null) {
                continue;
            }
            boolean relaxEveryOne = bellmanFordPathRelax(selected, pathInfo, edge);
            if (relaxEveryOne) {
                System.out.println("存在 负权环");
                return null;
            }
        }

        selected.remove(beginVertex.value);
        return selected;
    }

    /**
     * 松弛操作 BellmanFord
     * 和 Dijkstra 相比, 简单调整
     *
     * @param paths
     * @param minPath
     * @param edge
     * @return
     */
    private boolean bellmanFordPathRelax(Map<V, PathInfo<V, E>> paths, PathInfo<V, E> minPath, Edge<V, E> edge) {
        /*
        新的权重=当前扫描边权重+开始顶点到当前顶点权重
         */
        E newWight = edgeOpr.add(edge.wight, minPath.getWight());
        /*
        松弛操作
        看看原来有没有记录旧的权重, 只要进入 paths, 就说明了记录了起点到指定点的权重值 如果新的权重值小于旧值, 则进行松弛操作
         */
        PathInfo<V, E> oldPathInfo = paths.get(edge.to.value);
        if (oldPathInfo != null && edgeOpr.compare(newWight, oldPathInfo.getWight()) >= 0) {
            // 新发现的路径并不比原来的路径更短, 忽略
            return false;
        }
        if (oldPathInfo == null) {
            // 新发现的路径, 之前没有记录, 所以要重新记录
            oldPathInfo = new PathInfo<>();
            // 表示 起点->edge.to 这条路径
            paths.put(edge.to.value, oldPathInfo);
        } else {
            /*
            使用新的路径覆盖老的路径, 所以老的路径要清空, 完全采用新的路径
             */
            oldPathInfo.getEdgeInfos().clear();
        }

        // 新的权值
        oldPathInfo.setWight(newWight);
        // 加上当前点确定的已有路径
        oldPathInfo.getEdgeInfos().addAll(minPath.getEdgeInfos());
        // 新增的路径
        oldPathInfo.getEdgeInfos().add(edge.toEdgeInfo());

        return true;
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
        BinaryHeap_base_03<Edge<V, E>> heap = new BinaryHeap_base_03<>(true, comparator, nextVertex.outEdges);

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
        BinaryHeap_base_03<Edge<V, E>> heap = new BinaryHeap_base_03<>(true, comparator, edges);
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

    /**
     * 遍历map, 返回权重最小的值
     * 
     * @param paths
     * @return
     */
    private Map.Entry<Vertex<V, E>, E> minPathWight(Map<Vertex<V, E>, E> paths) {
        Map.Entry<Vertex<V, E>, E> minEntry = paths.entrySet().iterator().next();
        E minEdge = minEntry.getValue();
        for (Map.Entry<Vertex<V, E>, E> entry : paths.entrySet()) {
            if (edgeOpr.compare(entry.getValue(), minEdge) < 0) {
                minEntry = entry;
            }
        }
        return minEntry;
    }

    /**
     * 遍历map, 返回权重最小的值
     * 
     * @param paths
     * @return
     */
    private Map.Entry<Vertex<V, E>, PathInfo<V, E>> minPathInfo(Map<Vertex<V, E>, PathInfo<V, E>> paths) {
        Map.Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = paths.entrySet().iterator().next();
        PathInfo<V, E> pathInfo = minEntry.getValue();
        for (Map.Entry<Vertex<V, E>, PathInfo<V, E>> entry : paths.entrySet()) {
            if (edgeOpr.compare(entry.getValue().getWight(), pathInfo.getWight()) < 0) {
                minEntry = entry;
            }
        }
        return minEntry;
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
