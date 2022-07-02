package org.song.algorithm.base._01datatype._01base._05graph._01model.listgraph;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._01base._05graph._01model.IGraph;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdjacencyListTest {

    private IGraph.EdgeOpr<Integer> integerEdgeOpr = new IGraph.EdgeOpr<Integer>() {
        @Override
        public int compare(Integer e1, Integer e2) {
            return e1 - e2;
        }

        @Override
        public Integer add(Integer e1, Integer e2) {
            return e1 + e2;
        }
    };
    
    

    @Test
    public void test01() {

        AdjacencyList<String, Integer> adJacencyList = new AdjacencyList<>(integerEdgeOpr);

        adJacencyList.addEdge("V1", "V2", 1);
        adJacencyList.addEdge("V2", "V3", 2);
        adJacencyList.addEdge("V3", "V4", 3);
        adJacencyList.addEdge("V4", "V5", 4);
        adJacencyList.addEdge("V5", "V1", 5);
        adJacencyList.addEdge("V1", "V3", 3);

        System.out.println(adJacencyList);

    }

    @Test
    public void test02() {

        AdjacencyList<String, Integer> adJacencyList = new AdjacencyList<>(integerEdgeOpr);

        adJacencyList.addEdge("V1", "V2", 1);
        adJacencyList.addEdge("V2", "V3", 2);
        adJacencyList.addEdge("V3", "V4", 3);
        adJacencyList.addEdge("V4", "V5", 4);
        adJacencyList.addEdge("V5", "V1", 5);
        adJacencyList.addEdge("V1", "V3", 3);
        System.out.println(adJacencyList);

        adJacencyList.removeEdge("V1", "V3");
        System.out.println("删除边 V1->V3 " + adJacencyList);

    }

    @Test
    public void test03() {

        AdjacencyList<String, Integer> adJacencyList = new AdjacencyList<>(integerEdgeOpr);

        adJacencyList.addEdge("V1", "V2", 1);
        adJacencyList.addEdge("V2", "V3", 2);
        adJacencyList.addEdge("V3", "V4", 3);
        adJacencyList.addEdge("V4", "V5", 4);
        adJacencyList.addEdge("V5", "V1", 5);
        adJacencyList.addEdge("V1", "V3", 3);
        System.out.println(adJacencyList);

        adJacencyList.removeVertex("V1");
        System.out.println("删除顶点 V1 " + adJacencyList);

    }

    @Test
    public void test04() {

        AdjacencyList<String, Integer> adJacencyList = new AdjacencyList<>(integerEdgeOpr);

        adJacencyList.addEdge("V1", "V2", 1);
        adJacencyList.addEdge("V2", "V3", 2);
        adJacencyList.addEdge("V3", "V4", 3);
        adJacencyList.addEdge("V4", "V5", 4);
        adJacencyList.addEdge("V5", "V1", 5);
        adJacencyList.addEdge("V1", "V3", 3);
        System.out.println(adJacencyList);

        adJacencyList.bfs("V1", e -> {
            System.out.println(e);
            return true;
        });
        
        System.out.println();

        adJacencyList.dfs("V1", e -> {
            System.out.println(e);
            return true;
        });

    }

    @Test
    public void test05() {

        AdjacencyList<String, Integer> adJacencyList = new AdjacencyList<>(integerEdgeOpr);

        adJacencyList.addEdge("V1", "V2", 1);
        adJacencyList.addEdge("V2", "V3", 2);
        adJacencyList.addEdge("V3", "V4", 3);
        adJacencyList.addEdge("V4", "V5", 4);
        adJacencyList.addEdge("V1", "V3", 3);
        System.out.println(adJacencyList);

        List<String> topologySort = adJacencyList.topologySort();
        System.out.println(topologySort);

    }

    @Test
    public void test06() {

        AdjacencyList<String, Integer> adJacencyList = new AdjacencyList<>(integerEdgeOpr);

        adJacencyList.addEdge("V1", "V2", 1);
        adJacencyList.addEdge("V2", "V3", 2);
        adJacencyList.addEdge("V3", "V4", 3);
        adJacencyList.addEdge("V4", "V5", 4);
        adJacencyList.addEdge("V1", "V3", 3);
        adJacencyList.addEdge("V2", "V5", 10);
        adJacencyList.addEdge("V4", "V1", 7);
        System.out.println(adJacencyList);

        Set<IGraph.EdgeInfo<String, Integer>> mst = adJacencyList.mst();
        System.out.println(mst);

    }

    @Test
    public void test07() {

        AdjacencyList<String, Integer> adJacencyList = new AdjacencyList<>(integerEdgeOpr);

        adJacencyList.addEdge("V1", "V2", 1);
        adJacencyList.addEdge("V2", "V3", 2);
        adJacencyList.addEdge("V1", "V3", 4);
        adJacencyList.addEdge("V3", "V4", 3);
        adJacencyList.addEdge("V4", "V5", 4);
        adJacencyList.addEdge("V2", "V5", 10);
        adJacencyList.addEdge("V4", "V1", 7);
        System.out.println(adJacencyList);

        Map<String, Integer> shortestPath = adJacencyList.shortestPath("V1");
        System.out.println(shortestPath);

    }
}
