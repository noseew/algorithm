package org.song.algorithm.base._01datatype._01base._05graph._01model.listgraph;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._01base._05graph.DemoData;
import org.song.algorithm.base._01datatype._01base._05graph._01model.IGraph;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdjacencyListTest02 {


    @Test
    public void test01() {

        IGraph<Object, Double> adJacencyList = DemoData.undirectedGraph(DemoData.BF_SP);;

        System.out.println(adJacencyList);
        System.out.println(adJacencyList.mst());
        System.out.println(adJacencyList.shortestPath());

    }
}
