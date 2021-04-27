package org.song.algorithm.algorithmbase.datatype.list;

public class ListPrinter {

    public static void printSingleList(Linked_single_01.Node<?> linked) {
        if (linked == null) {
            return;
        }
        while (linked != null) {
            System.out.println("<" + linked.value + ">");
            System.out.println("↓");
            linked = linked.next;
        }
    }
}
