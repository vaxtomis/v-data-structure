package utils;

import base.TreeNode;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 创造树结构的类
 * @author vaxtomis
 */
public class TreeUtil {
    public static TreeNode createTree(int nodeNumber) {
        int nodeCount = 1;
        TreeNode root = new TreeNode(nodeCount);
        Deque<TreeNode> deque = new LinkedList<>();
        deque.add(root);
        while (nodeCount < nodeNumber && !deque.isEmpty()) {
            TreeNode cur = deque.pollFirst();
            TreeNode left = new TreeNode(++nodeCount);
            cur.setLeft(left);
            deque.add(left);
            if (nodeCount == nodeNumber) break;
            TreeNode right = new TreeNode(++nodeCount);
            cur.setRight(right);
            deque.add(right);
        }
        return root;
    }

    public static void printTree(TreeNode root) {
        Deque<TreeNode> deque = new LinkedList<>();
        deque.add(root);
        TreeNode cur;
        while (!deque.isEmpty()) {
            cur = deque.pollFirst();
            System.out.println(cur.toString());
            if (cur.getLeft() != null && cur.getIndex() != -1) {
                deque.add(cur.getLeft());
            }
            if (cur.getRight() != null && cur.getIndex() != -1) {
                deque.add(cur.getRight());
            }
        }
    }
}
