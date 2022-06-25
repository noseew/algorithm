package org.song.algorithm.base._01datatype._01base._05graph._01model.listgraph;

import org.junit.jupiter.api.Test;

public class AdjacencyListTest {

    @Test
    public void test01() {

        AdjacencyList<String, Integer> adJacencyList = new AdjacencyList<>();

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

        AdjacencyList<String, Integer> adJacencyList = new AdjacencyList<>();

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

        AdjacencyList<String, Integer> adJacencyList = new AdjacencyList<>();

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

        AdjacencyList<String, Integer> adJacencyList = new AdjacencyList<>();

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
}
