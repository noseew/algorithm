package org.song.algorithm.base._01datatype._01base._04tree.binarytree.printer;

import lombok.experimental.UtilityClass;
import org.song.algorithm.base._01datatype._01base._04tree.binarytree._01model.AbsBSTTree;
import org.song.algorithm.base._01datatype._01base._04tree.binarytree._01model.Tree02BST01;
import org.song.algorithm.base._01datatype._01base._04tree.binarytree._01model.TreeNode;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@UtilityClass
public class BTreeUtils {

    public static boolean eq(Collection c, Tree02BST01 tree) {
        if (c.size() != tree.size) {
            System.err.println("size diff: c.size=" + c.size());
            System.err.println("size diff: tree.size=" + tree.size());
            return false;
        }
        Collection set = new HashSet(tree.size);
        Tree02BST01.traverse(tree.root, AbsBSTTree.Order.MidOrder, e -> set.add(e.val));
        return set.equals(c);
    }

    public static boolean eq(Tree02BST01 tree1, Tree02BST01 tree2) {
        List<Object> list1 = new ArrayList<>(tree1.size);
        List<Object> list2 = new ArrayList<>(tree2.size);

        Tree02BST01.traverse(tree1.root, AbsBSTTree.Order.MidOrder, e -> list1.add(e.val));
        Tree02BST01.traverse(tree2.root, AbsBSTTree.Order.MidOrder, e -> list2.add(e.val));

        if (list1.size() != list2.size()) {
            System.err.println("size diff: list1.size=" + list1.size());
            System.err.println("size diff: list2.size=" + list2.size());
            return false;
        }
        return list1.equals(list2);
    }

    public static <V extends Comparable<V>> boolean cycleCheck(TreeNode<V> root) {
        Set<V> set = new HashSet<>();
        AtomicBoolean terminal = new AtomicBoolean(false);
        V[] v = (V[]) new Integer[2];
        AbsBSTTree.traverse(root, AbsBSTTree.Order.MidOrder, e -> {
            if (terminal.get()) {
                return false;
            }
            v[0] = v[1];
            v[1] = e.val;
            if (set.add(e.val)) {
                return true;
            }
            terminal.set(true);
            return false;
        });
        if (terminal.get()) {
            System.err.println("循环val=" + v[1]);
            System.err.println("val.pre=" + v[0]);
            return true;
        }
        return false;
    }
    
    public static String simplePrint(TreeNode root, boolean printColor) {
        InorderPrinter printer = new InorderPrinter(root, printColor);
        return printer.printString();
    }

    // 来自网络

    public static String print(TreeNode root, boolean print) {
        CycleRecursionCheck<TreeNode> check = new CycleRecursionCheck<>();

        StringBuilder sb = new StringBuilder();
        final int maxLevel = getDepth(root, check);
        sb.append("maxLevel: " + maxLevel).append("\r\n");
        if (print) {
            System.out.println("maxLevel: " + maxLevel);
        }

        // 满二叉树节点数为 2^maxLevel - 1;
        TreeNode[] nodes = new TreeNode[(int) Math.pow(2, maxLevel)];
        check = new CycleRecursionCheck<>();
        traverse(root, 1, nodes, check);


        // 定义, 叶子节点的宽度
        final int leafNodeWidth = 4;
        for (int level = 1; level <= maxLevel; ++level) {
            // level 层节点的宽度 = 2^(maxLevel - level) * 叶子节点的宽度
            int nodeWidth = (int) Math.pow(2, maxLevel - level) * leafNodeWidth;
            String levelInfo = String.format("level[%s] 节点宽度[%3d]", level, nodeWidth);
            sb.append(levelInfo).append("===|  ");
            if (print) {
                System.out.print(levelInfo);
                System.out.print("===|  ");
            }

            int beginIndex = (int) Math.pow(2, level - 1);
            int endIndex = beginIndex * 2 - 1;
            for (int i = beginIndex; i <= endIndex; ++i) {
                try {
                    String nodeText = getNodeText(nodes[i], nodeWidth);
                    sb.append(nodeText);
                    if (print) {
                        System.out.print(nodeText);
                    }
                } catch (Exception e) {
                    System.out.println();
                    System.err.println("BTreePrinter.print error: " + e.getMessage());
                }
            }
            sb.append("\r\n");
            if (print) {
                System.out.println();
            }

            // 最后一层不需要 edge 了
            if (level == maxLevel) break;

            // 中文比英文更宽, 所以多加 3 个空格, 以便对齐

            String content = String.join("", Collections.nCopies(levelInfo.length() + 3, " "));
            sb.append(content);
            sb.append("===|  ");
            if (print) {
                System.out.print(content);
                System.out.print("===|  ");
            }
            for (int i = beginIndex; i <= endIndex; ++i) {
                String edgeText = getEdgeText(nodeWidth, nodes, i);
                sb.append(edgeText);
                if (print) {
                    System.out.print(edgeText);
                }
            }
            sb.append("\r\n");
            if (print) {
                System.out.println();
            }
        }
        return sb.toString();
    }

    private static String getNodeText(TreeNode node, int nodeWidth) {
        int leftLength = nodeWidth / 2;
        String val = (node == null ? "" : String.valueOf(node.val));
        int rightLength = nodeWidth - leftLength - val.length();

        String left = String.join("", Collections.nCopies(leftLength, " "));
        String right = String.join("", Collections.nCopies(rightLength, " "));

        // 得到长度为 nodeWidth 的表示节点的字符串
        return left + fill(node, val) + right;
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

    private static int getDepth(TreeNode root, CycleRecursionCheck<TreeNode> check) {
        if (root == null || !check.add(root)) return 0;
        return Math.max(getDepth(root.left, check), getDepth(root.right, check)) + 1;
    }

    private static void traverse(TreeNode root, int index, TreeNode[] nodes, CycleRecursionCheck<TreeNode> check) {
        if (root == null || !check.add(root)) return;
        // System.out.println("index:" + index);
        nodes[index] = root;
        traverse(root.left, index * 2, nodes, check);
        if (!check.parentPrint && check.cycle) {
            System.err.println(String.format("出现循环: parent = %s, left = %s", root.val, root.left.val));
            check.parentPrint = true;
        }
        traverse(root.right, index * 2 + 1, nodes, check);
        if (!check.parentPrint && check.cycle) {
            System.err.println(String.format("出现循环: parent = %s, right = %s", root.val, root.right.val));
            check.parentPrint = true;
        }
    }

    public static String fill(TreeNode node, Object v) {
        return isRed(node) ? "\033[31;4m" + v + "\033[0m" : String.valueOf(v);
    }

    private static boolean isRed(TreeNode node) {
        return node != null && node.red;
    }

    private static class CycleRecursionCheck<V extends TreeNode> {
        Set set = new HashSet<>();
        boolean parentPrint = false;
        boolean cycle = false;

        /**
         * return false, 说明 V 已存在
         *
         * @param v
         * @return false, 说明 V 已存在
         */
        public boolean add(V v) {
            boolean check = set.add(v.val);
            if (!check) {
                cycle = true;
            }
            return check;
        }
    }

    // 来自JDK9 sun.jvm.hotspot.utilities.RBTree

    public static String printJDK9(TreeNode root) {
        return printFromNode(root, new StringBuilder(), 0);
    }

    private static String printFromNode(TreeNode node, StringBuilder tty, int indentDepth) {
        for (int i = 0; i < indentDepth; i++) {
            tty.append(" ");
        }

        tty.append("-");
        if (node == null) {
            tty.append("\r\n");
            return "";
        }

        tty.append(" " + fill(node, node.val)).append("\r\n");
        printFromNode(node.getLeft(), tty.append("L"), indentDepth + 1);
        printFromNode(node.getRight(), tty.append("R"), indentDepth + 1);
        return tty.toString();
    }


}
