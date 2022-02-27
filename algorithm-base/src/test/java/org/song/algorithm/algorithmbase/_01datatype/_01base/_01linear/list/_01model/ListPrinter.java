package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.node.SingleNode;

public class ListPrinter {

    public static String printSingleList(SingleNode head, boolean print) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        SingleNode n = head;
        while (n != null) {
            sb.append(n.value).append(",");
            n = n.next;
        }
        sb.append("]");
        if (print) {
            System.out.println(sb.toString());
        }
        return sb.toString();
    }
}
