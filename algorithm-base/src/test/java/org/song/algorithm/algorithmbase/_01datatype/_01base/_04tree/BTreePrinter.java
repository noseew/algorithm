package org.song.algorithm.algorithmbase._01datatype._01base._04tree;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.TreeNode;

import java.util.Collections;

public class BTreePrinter {

    public static void print(TreeNode root) {
        final int maxLevel = getDepth(root);
        System.out.println("maxLevel: " + maxLevel);

        // 满二叉树节点数为 2^maxLevel - 1;
        TreeNode[] nodes = new TreeNode[(int) Math.pow(2, maxLevel)];
        traverse(root, 1, nodes);


        // 定义，叶子节点的宽度
        final int leafNodeWidth = 4;
        for (int level = 1; level <= maxLevel; ++level) {
            // level 层节点的宽度 = 2^(maxLevel - level) * 叶子节点的宽度
            int nodeWidth = (int) Math.pow(2, maxLevel - level) * leafNodeWidth;
            String levelInfo = String.format("level[%s] 节点宽度[%3d]", level, nodeWidth);
            System.out.print(levelInfo);
            System.out.print("===|  ");

            int beginIndex = (int) Math.pow(2, level - 1);
            int endIndex = beginIndex * 2 - 1;
            for (int i = beginIndex; i <= endIndex; ++i) {
                System.out.print(getNodeText(nodes[i], nodeWidth));
            }
            System.out.println();

            // 最后一层不需要 edge 了
            if (level == maxLevel) break;

            // 中文比英文更宽，所以多加 3 个空格，以便对齐
            System.out.print(String.join("", Collections.nCopies(levelInfo.length() + 3, " ")));
            System.out.print("===|  ");
            for (int i = beginIndex; i <= endIndex; ++i) {
                System.out.print(getEdgeText(nodeWidth, nodes, i));
            }
            System.out.println();
        }
    }

    private static String getNodeText(TreeNode node, int nodeWidth) {
        int leftLength = nodeWidth / 2;
        String val = (node == null ? "" : String.valueOf(node.v));
        int rightLeght = nodeWidth - leftLength - val.length();

        String left = String.join("", Collections.nCopies(leftLength, " "));
        String right = String.join("", Collections.nCopies(rightLeght, " "));

        // 得到长度为 nodeWidth 的表示节点的字符串
        return left + val + right;
    }

    private static String getEdgeText(int nodeWidth, TreeNode[] nodes, int index) {
        TreeNode leftNode = nodes[index * 2];
        TreeNode rightNode = nodes[index * 2 + 1];

        // 左右子树都为空
        if (leftNode == null && rightNode == null) {
            return String.join("", Collections.nCopies(nodeWidth, " "));
        }

        int edgeWidth = nodeWidth / 2;
        String edge = getEdge(edgeWidth, leftNode, rightNode);

        String leftPadding = String.join("", Collections.nCopies(edgeWidth / 2, " "));
        int rightPaddingLength = nodeWidth - edge.length() - leftPadding.length();
        String rightPadding = String.join("", Collections.nCopies(rightPaddingLength, " "));

        // 得到长度为 nodeWidth 的表示树的两个边的字符串
        return leftPadding + edge + rightPadding;
    }

    private static String getEdge(int edgeWidth, TreeNode leftNode, TreeNode rightNode) {
        // 左子树为空
        if (leftNode == null) {
            String left = String.join("", Collections.nCopies(edgeWidth / 2, " "));
            String right = String.join("", Collections.nCopies(edgeWidth / 2 - 1, "─")) + "┐";

            return left + "└" + right;
        }

        // 右子树为空
        if (rightNode == null) {
            String left = "┌" + String.join("", Collections.nCopies(edgeWidth / 2 - 1, "─"));
            String right = String.join("", Collections.nCopies(edgeWidth / 2, " "));

            return left + "┘" + right;
        }

        String left = "┌" + String.join("", Collections.nCopies(edgeWidth / 2 - 1, "─"));
        String right = String.join("", Collections.nCopies(edgeWidth / 2 - 1, "─")) + "┐";
        return left + "┴" + right;
    }

    public static int getDepth(TreeNode root) {
        if (root == null) return 0;
        return Math.max(getDepth(root.left), getDepth(root.right)) + 1;
    }

    public static void traverse(TreeNode root, int index, TreeNode[] nodes) {
        if (root == null) return;
        // System.out.println("index:" + index);
        nodes[index] = root;
        traverse(root.left, index * 2, nodes);
        traverse(root.right, index * 2 + 1, nodes);
    }
}
