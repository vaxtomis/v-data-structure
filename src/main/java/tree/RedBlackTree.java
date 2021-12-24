package tree;

/**
 * 本实现是 LLRBT，等价于 2-3 树
 *
 * 定义：
 * > 节点为红与黑
 * > 根节点为黑
 * > 所有叶节点为黑
 * > 红色节点的子节点为黑
 * > 每个节点到其子孙节点的所有路径上包含相同数目的黑节点
 *
 * 插入规则：
 * 插入新节点都为红色
 *
 * 节点结构：
 * color, key, value, left, right, parent
 *
 * @author vaxtomis
 * @date 2021-12-22
 */
public class RedBlackTree<V extends Comparable<V>, T> {
    private Node<V, T> root;

    class Node<V extends Comparable<V>, T> {
        protected V key;
        protected Object value;
        protected boolean isRed;
        protected Node left;
        protected Node right;
        protected Node parent;

        public Node(V key, T value) {
            this.key = key;
            this.value = value;
            this.isRed = true;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        // 左旋
        private void rotateLeft(boolean isLeft) {
            // 右节点为空无法左旋
            if (this.right == null) {
                return;
            }
            // 取到父节点和右节点
            Node parent = this.parent;
            Node right = this.right;
            // 如果当前节点为根节点，则左旋后右节点变为根节点
            if (this == root) {
                root = right;
            }
            // 当前节点的右节点，连接右节点的左子树
            this.right = right.left;
            if (right.left != null) {
                right.left.parent = this;
            }
            // 右节点的左子树连接当前节点
            right.left = this;
            this.parent = right;
            // 如果父节点不为空
            if (parent != null && isLeft) {
                parent.left = right;
                right.parent = parent;
                return;
            }
            if (parent != null) {
                parent.right = right;
                right.parent = parent;
            }
        }

        // 右旋
        private void rotateRight(boolean isLeft) {
            // 左节点为空则无法右旋
            if (this.left == null) {
                return;
            }
            // 取到父节点和左节点
            Node parent = this.parent;
            Node left = this.left;
            // 如果当前节点为根节点，则右旋后左节点变为根节点
            if (this == root) {
                root = left;
            }
            // 当前节点的左节点，连接左节点的右子树
            this.left = left.right;
            if (left.right != null) {
                left.right.parent = this;
            }
            // 左节点的右子树连接当前节点
            left.right = this;
            this.parent = left;
            if (parent != null && isLeft) {
                parent.left = left;
                left.parent = parent;
                return;
            }
            if (parent != null) {
                parent.right = left;
                left.parent = parent;
            }
        }

        private void balance() {
            // ！！！关键点！！！ -> 刷新 root 的状态
            root.isRed = false;
            root.parent = null;

            if (left != null && right != null && !isRed && left.isRed && right.isRed) {
                // 黑色节点的左右子节点都为红，调整颜色（情况4）
                System.out.println("情况4：黑色节点的左右子节点都为红，颜色翻转。");
                left.isRed = false;
                right.isRed = false;
                if (parent != null && parent.isRed) {
                    parent.balance();
                }
                return;
            }
            // 插入在红色左子节点的左侧，右旋（情况3）
            if (parent != null && !parent.isRed && isRed && left != null && left.isRed) {
                System.out.println("情况3：插入在红色左子子节点的左侧，父节点右旋。");
                parent.rotateRight(parent.isLeft());
                isRed = false;
                right.isRed = true;
                balance();
                return;
            }
            // 插入在红色左子节点的右侧，左旋（情况2）
            if (parent != null && !parent.isRed && isRed && right != null && right.isRed) {
                System.out.println("情况2：插入在红色左子节点的右侧，左旋。");
                rotateLeft(true);
                parent.balance();
                return;
            }
            if (right != null && !isRed && right.isRed) {
                // 直接插在节点右侧，左旋（情况1）
                System.out.println("情况1：直接插在节点右侧，左旋。");
                this.right.isRed = false;
                this.isRed = true;
                rotateLeft(isLeft());
            }
        }

        // 判断当前节点是否是其父节点的左子节点
        private boolean isLeft() {
            Node<V, T> parent = this.parent;
            if (parent == null || this == parent.left) {
                return true;
            }
            return false;
        }

        // 给定 key 找对应的 value（平衡树的查找逻辑很简单）
        private T find(V key) {
            int cmp = key.compareTo(this.key);
            if (cmp > 0) {
                if (this.right != null) {
                    return (T) this.right.find(key);
                }
                return null;
            }
            if (cmp < 0) {
                if (this.left != null) {
                    return (T) this.left.find(key);
                }
                return null;
            }
            return (T) this.value;
        }

        // 给定 key，返回有合适插入位置的父节点
        private Node<V, T> findNode(V key) {
            if (key.compareTo(this.key) > 0 && this.right != null) {
                return this.right.findNode(key);
            }
            if (key.compareTo(this.key) < 0 && this.left != null) {
                return this.left.findNode(key);
            }
            return this;
        }
    }

    public Node<V, T> insert(V key, T value) {
        if (root == null) {
            System.out.println("插入根节点：" + key.toString());
            Node<V, T> newNode = new Node<>(key, value);
            newNode.isRed = false;
            this.root = newNode;
            return this.root;
        }
        Node<V, T> insertP = root.findNode(key);
        int cmp = key.compareTo(insertP.key);
        if (cmp == 0) {
            System.out.println("key 相同，覆盖值。");
            insertP.value = value;
            return insertP;
        }
        Node<V, T> newNode = new Node<>(key, value);
        if (cmp > 0) {
            System.out.println(insertP.key + " 的右侧插入新节点：" + key.toString());
            insertP.right = newNode;
        } else {
            System.out.println(insertP.key + " 的左侧插入新节点：" + key.toString());
            insertP.left = newNode;
        }
        newNode.parent = insertP;
        insertP.balance();
        return newNode;
    }

    public T find(V key) {
        return root.find(key);
    }
}
