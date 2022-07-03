package org.song.algorithm.base._01datatype._01base._05graph._01model;

import lombok.Data;
import org.song.algorithm.base._01datatype._01base._05graph._01model.listgraph.ListGraph;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

/*

图的存储
数组
    邻接矩阵, 
    优点: 简单直观, 图像更好理解
    缺点: 空间占用大, 编写稍微复杂 
链表
    邻接表
    优点: 空间占用小, 实现稍微简单
    缺点: 图像不太好理解
    邻接表存储概念上比较复杂, 这里有简单的实现方式, 具体参见 IListGraph
    
    邻接表
    十字链表, 解决有向图邻接表度计算问题
    邻接多重表, 解决无向图邻接表重复边问题, 有点抽象

    链表存储
    邻接表方式存储
    注意具体的定义方式根据语言等有所区别, 能表达意思即可
    
    十字链表
    解决有向图邻接表/逆邻接表, 计算度不方便的问题
    将邻接表的出度和入度结合起来, 则每个链表节点都表示出度链表的一个节点和入度链表的一个节点, 从而形成十字形状链表
    从顶点出发形成两个链表, 一个是出度链表, 一个是入度链表
 */
public interface IGraph<V, E> {
    /**
     * 添加一个顶点
     * 
     * @param v
     */
    void addVertex(V v);

    /**
     * 添加一条边
     * 
     * @param from
     * @param to
     */
    void addEdge(V from, V to);

    /**
     * 添加一条边, 加权重
     * 
     * @param from
     * @param to
     * @param wight
     */
    void addEdge(V from, V to, E wight);

    /**
     * 删除一个顶点
     * 
     * @param v
     */
    void removeVertex(V v);

    /**
     * 删除一条边
     * 
     * @param from
     * @param to
     */
    void removeEdge(V from, V to);

    /**
     * 边的数量
     * 
     * @return
     */
    int edgeSize();

    /**
     * 顶点数量
     * 
     * @return
     */
    int vertices();

    /**
     * 深度优先遍历
     * 
     * 深度优先遍历秘籍：后被访问的顶点，其邻接点先被访问。
     * 
     * @param goon 是否继续遍历
     * @param begin 开始顶点
     */
    void dfs(V begin, Predicate<V> goon);

    /*
    广度优先搜索, 
    应用: 树中的层序遍历
    如何确定层呢?
        假设层数从0开始, 
        则顶点处在第0层, 
        顶点需要1步达到的点, 处在第1层;
        顶点需要2步到达的点, 处在第2层;
        以此类推, 图中思路也是这样
     */
    /**
     * 广度优先遍历
     * 
     * 广度优先遍历秘籍：先被访问的顶点，其邻接点先被访问。
     * 
     * @param goon 是否继续遍历
     * @param begin 开始顶点
     */
    void bfs(V begin, Predicate<V> goon);

    /*
    拓扑排序
        前置知识:
            AOV网: 
            大工程分为多个子工程的集合, 而这些子工程之间有相互依赖关系, 只有子工程A完成后才能进行子工程B
            使用有向无环图来表示这样的工程关系, 这样的图称为AOV网
            其中, 图的顶点表示一个子工程, 有向图的方向表示子工程的依赖关系
            拓扑排序就是将 AOV网中, 按照依赖关系排除并输出
        条件: 1. 有向无环图; 2. 排序结果并不唯一, 因为会存在没有相互依赖关系的顶点
    实现
        一 卡恩算法
        思路: 
            1. 找到图中所有入度为0的顶点的集合, 删除并记录他们
            2. 剩下的顶点集合中, 继续执行1的步骤; 此顺序删除的顶点就是拓扑排序的结果
            注意: 如果删除的顶点数量不等于原图顶点数, 则说明图中存在环
            具体的实现参照卡恩算法
     */
    /**
     * 拓扑排序, 返回拓扑排序结果
     * 
     * @return
     */
    List<V> topologySort();
    
    /*
    最小生成树, 适用于有权连通图
    背景:
    1. 生成树:
        别名:支撑树, 也就是图的骨架, 
        也就是连通图的极小连通子图, 也就是将一个连通图去掉尽可能多的边, 这个图依然连通; 
            边数不能再少了, 再少的话图就不连通的, 也就是有顶点无法相连了
    2. 最小生成树 minimal spanning tree, MST
        别名: 最小权重生成树, 最小支撑树
        所有生成树中总权值最小的生成树
        如果图中存在多条相同权重的边, 则最小生成树可能有多个
    应用:
        1. 在各个城市中架设道路, 能将所有城市连接且总里程最少的方案
    
    最小生成树算法
    1. prim
    2. kruskal
    
     */

    /**
     * 返回最小生成树的边集
     * minimal spanning tree
     * 
     * @return
     */
    Set<EdgeInfo<V, E>> mst();

    /**
     * 返回单源最短路径集合
     * 返回 k-v, 
     * k= begin到v的标识
     * v= begin到v的最短路径权值
     *
     * @param begin 开始顶点
     * @return
     */
    Map<V, E> shortestPathWight(V begin);
    
    Map<V, PathInfo<V, E>> shortestPath(V begin);
    
    @Data
    class EdgeInfo<V, E> {
        V from; // 表示该边出度的顶点
        V to; // 表示该边入度的顶点
        E wight; // 边的权重

        public EdgeInfo(V from, V to, E wight) {
            this.from = from;
            this.to = to;
            this.wight = wight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EdgeInfo)) return false;
            EdgeInfo<?, ?> edge = (EdgeInfo<?, ?>) o;
            return Objects.equals(from, edge.from) &&
                    Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
        
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(from).append(" -").append(wight).append("-> ").append(to);
            return sb.toString();
        }
    }

    @Data
    class PathInfo<V, E> {
        E wight;
        List<EdgeInfo<V, E>> edgeInfos = new ArrayList<>();

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("wight=").append(wight).append("\r\n");
            edgeInfos.forEach(e -> {
                sb.append(e.toString()).append("; ");
            });
            return sb.toString();
        }
    }

    abstract class EdgeOpr<E> {

        public abstract int compare(E e1, E e2);

        public abstract E add(E e1, E e2);

        public abstract E zero();
    }
}
