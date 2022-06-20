package org.song.algorithm.base._01datatype._01base._05graph;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._01base._05graph._01model.matrixgraph.AdjacencyMatrix;

public class MatrixGraphTest {
    
    /*
    图:
    1 -- 2
    |
    3 -- 4
    顶点:
    [1,2,3,4]
    邻接矩阵:
    [0,1,1,0]
    [1,0,0,0]
    [1,0,0,1]
    [0,0,1,0]
     */
    @Test
    public void test_AdJacentMatrix() {
        AdjacencyMatrix adJacencyMatrix = new AdjacencyMatrix(4);

    }
}
