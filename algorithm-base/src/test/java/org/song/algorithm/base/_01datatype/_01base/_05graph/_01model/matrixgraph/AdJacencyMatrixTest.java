package org.song.algorithm.base._01datatype._01base._05graph._01model.matrixgraph;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._01base._05graph._01model.listgraph.AdJacencyList;

public class AdJacencyMatrixTest {

    @Test
    public void test01() {

        AdJacentMatrix<String, Integer> adJacencyMatrix = new AdJacentMatrix<>(5);

        adJacencyMatrix.addEdge("V1", "V2", 1);
        adJacencyMatrix.addEdge("V2", "V3", 2);
        adJacencyMatrix.addEdge("V3", "V4", 3);
        adJacencyMatrix.addEdge("V4", "V5", 4);
        adJacencyMatrix.addEdge("V5", "V1", 5);
        adJacencyMatrix.addEdge("V1", "V3", 3);
        System.out.println(adJacencyMatrix);

    }

    @Test
    public void test02() {

        AdJacentMatrix<String, Integer> adJacencyMatrix = new AdJacentMatrix<>(5);

        adJacencyMatrix.addEdge("V1", "V2", 1);
        adJacencyMatrix.addEdge("V2", "V3", 2);
        adJacencyMatrix.addEdge("V3", "V4", 3);
        adJacencyMatrix.addEdge("V4", "V5", 4);
        adJacencyMatrix.addEdge("V5", "V1", 5);
        adJacencyMatrix.addEdge("V1", "V3", 3);
        System.out.println(adJacencyMatrix);

        adJacencyMatrix.removeEdge("V1", "V3");
        System.out.println("删除边 V1->V3 " + adJacencyMatrix);

    }

    @Test
    public void test03() {

        AdJacentMatrix<String, Integer> adJacencyMatrix = new AdJacentMatrix<>(5);

        adJacencyMatrix.addEdge("V1", "V2", 1);
        adJacencyMatrix.addEdge("V2", "V3", 2);
        adJacencyMatrix.addEdge("V3", "V4", 3);
        adJacencyMatrix.addEdge("V4", "V5", 4);
        adJacencyMatrix.addEdge("V5", "V1", 5);
        adJacencyMatrix.addEdge("V1", "V3", 3);
        System.out.println(adJacencyMatrix);

        adJacencyMatrix.removeVertex("V1");
        System.out.println("删除顶点 V1 " + adJacencyMatrix);

    }
}
