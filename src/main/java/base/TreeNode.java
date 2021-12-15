package base;

/**
 * @author vaxtomis
 */
public class TreeNode {
    private int index;
    private TreeNode node;
    private TreeNode left;
    private TreeNode right;

    public TreeNode() {
        this.index = -1;
        this.left = null;
        this.right = null;
    }

    public TreeNode(int index) {
        this.index = index;
        this.left = null;
        this.right = null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public TreeNode getNode() {
        return node;
    }

    public void setNode(TreeNode node) {
        this.node = node;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "TreeNode$" + index;
    }
}
