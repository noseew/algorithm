package org.song.algorithm.base._01datatype._01base._05graph._01model.demo;

import org.junit.jupiter.api.Test;

public class ListGraphTest {

    @Test
    public void test01() {

        ListGraph<String, Integer> listGraph = new ListGraph<>();

        listGraph.addEdge("V1", "V2", 1);
        listGraph.addEdge("V2", "V3", 2);
        listGraph.addEdge("V3", "V4", 3);
        listGraph.addEdge("V4", "V5", 4);
        listGraph.addEdge("V5", "V1", 5);
        listGraph.addEdge("V1", "V3", 3);

        System.out.println(listGraph);

    }

    @Test
    public void test02() {

        ListGraph<String, Integer> listGraph = new ListGraph<>();

        listGraph.addEdge("V1", "V2", 1);
        listGraph.addEdge("V2", "V3", 2);
        listGraph.addEdge("V3", "V4", 3);
        listGraph.addEdge("V4", "V5", 4);
        listGraph.addEdge("V5", "V1", 5);
        listGraph.addEdge("V1", "V3", 3);
        System.out.println(listGraph);

        listGraph.removeEdge("V1", "V3");
        System.out.println("删除边 V1->V3 " + listGraph);

    }

    @Test
    public void test03() {

        ListGraph<String, Integer> listGraph = new ListGraph<>();

        listGraph.addEdge("V1", "V2", 1);
        listGraph.addEdge("V2", "V3", 2);
        listGraph.addEdge("V3", "V4", 3);
        listGraph.addEdge("V4", "V5", 4);
        listGraph.addEdge("V5", "V1", 5);
        listGraph.addEdge("V1", "V3", 3);
        System.out.println(listGraph);

        listGraph.removeVertex("V1");
        System.out.println("删除顶点 V1 " + listGraph);

    }
}
