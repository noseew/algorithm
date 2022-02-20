//package org.song.algorithm.algorithmbase.from_love.datatype._15data二叉树的非递归遍历;
// //
// //import org.song.algorithm.algorithmbase.from_love.datatype._07dataAVL树.tree.BST;
// //import org.song.algorithm.algorithmbase.from_love.datatype._09data集合.set.Set;
// //
// //public class Main {
// //	public static void main(String[] args) {
// //		// 创建BST
// //		Integer data[] = new Integer[] {
// //				7, 4, 9, 2, 5, 8, 11
// //		};
// //		BST<Integer> bst = new BST<>();
// //		for (int i = 0; i < data.length; i++) {
// //			bst.add(data[i]);
// //		}
// //		// 树状打印
// ////		BinaryTrees.println(bst);
// //		// 遍历器
// //		StringBuilder sb = new StringBuilder();
// //		Set.Visitor<Integer> visitor = new Set.Visitor<Integer>() {
// //			@Override
// //			public boolean visit(Integer element) {
// //				sb.append(element).append(" ");
// //				return false;
// //			}
// //		};
// //		// 遍历
// //		sb.delete(0, sb.length());
// //		bst.preorder(visitor);
// //		System.out.println("前序：" + sb );
// //		Asserts.test(sb.toString().equals("7 4 2 5 9 8 11 "));
// //
// //		sb.delete(0, sb.length());
// //		bst.inorder(visitor);
// //		System.out.println("中序：" + sb );
// //		Asserts.test(sb.toString().equals("2 4 5 7 8 9 11 "));
// //
// //		sb.delete(0, sb.length());
// //		bst.postorder(visitor);
// //		System.out.println("后序：" + sb );
// //		Asserts.test(sb.toString().equals("2 5 4 8 11 9 7 "));
// //	}
// //}
// 