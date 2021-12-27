package tree;

/**
 * 每个节点最多可以有 m 个元素
 * 除了根节点外，每个节点最少有 (m/2) 个元素
 * 如果根节点不是叶节点，那么它最少有 2 个孩子节点
 * 所有的叶子节点都在同一层
 * 一个有 k 个孩子节点的非叶子节点有 (k-1) 个元素，按升序排列
 * 某个元素的左子树中的元素都比它小，右子树的元素都大于或等于它
 * 非叶子节点只存放关键字和指向下一个孩子节点的索引，记录只存放在叶子节点中
 * 相邻的叶子节点之间用指针相连
 *
 * @author vaxtomis
 * @date 2021-12-20
 */
public class BPlusTree<V extends Comparable<V>, T> {
    // 阶数
    private Integer degree;

    // 用于初始化数组长度（在节点拥有 degree 个元素后插入，在拆分前要留出容纳插入元素的空间）
    // 所以为 degree + 1
    private Integer maxNumber;

    private Node<V, T> root;
    private LeafNode<V, T> start;

    public BPlusTree() {
        this(3);
    }

    public BPlusTree(Integer degree) {
        this.degree = degree;
        this.maxNumber = degree + 1;
        this.start = new LeafNode<>();
        this.root = start;
    }

    public Node<V, T> insert(V key, T value) {
        return root.insert(key, value);
    }

    public T find(V key) {
        return root.find(key);
    }

    // 打印叶节点
    public void printLeaf() {
        start.printLine();
    }

    // 分层打印整棵 B+树
    public void printTree() {
        System.out.println("====== 打印整棵树 ======");
        int lineNo = 1;
        Node<V, T> headNode = root;
        while (headNode.childs[0] != null) {
            System.out.println("打印第 " + lineNo++ + " 层");
            headNode.printLine();
            headNode = headNode.childs[0];
            System.out.println();
        }
        System.out.println("打印叶节点：");
        headNode.printLine();
    }

    public Node<V, T> getRoot() {
        return root;
    }

    abstract class Node<V extends Comparable<V>, T> {
        protected Node<V, T> parent;
        protected Node<V, T>[] childs;
        protected Integer keyNumber;
        protected Object[] keys;

        // 一般只要求叶节点相连，为了更方便的打印整颗 B+树，这里选择每一层都连接起来
        protected Node next;

        public Node() {
            this.keys = new Object[maxNumber];
            this.childs = new Node[maxNumber];
            this.keyNumber = 0;
            this.parent = null;
            this.next = null;
        }

        /**
         * 根据 key 下标找到对象
         * @param key
         * @return T
         */
        abstract T find(V key);

        /**
         * 插入节点
         * @param key
         * @param value
         * @return Node<V, T>
         */
        abstract Node<V, T> insert(V key, T value);

        void print() {
            StringBuilder res = new StringBuilder();
            res.append("{");
            for (int i = 0; i < this.keyNumber - 1; i++) {
                res.append(keys[i].toString()).append(",");
            }
            res.append(keys[keyNumber - 1].toString()).append("} ");
            System.out.print(res.toString());
        }

        void printLine() {
            Node<V, T> cur = this;
            while (cur.next != null) {
                cur.print();
                cur = cur.next;
            }
            cur.print();
            System.out.println();
        }

    }

    class LeafNode<V extends Comparable<V>, T> extends Node<V, T> {
        protected Object[] values;

        public LeafNode() {
            super();
            this.values = new Object[maxNumber];
            this.next = null;
        }

        /**
         * 二分查找下标
         * @param key
         * @return
         */
        int findIndex(V key) {
            if (keyNumber <= 0) {
                return -1;
            }
            int left = 0;
            int right = keyNumber - 1;
            int mid;
            while (left <= right) {
                mid = left + (right - left)/2;
                V midKey = (V) keys[mid];
                if (key.compareTo(midKey) < 0) {
                    right = mid - 1;
                }
                else if (key.compareTo(midKey) > 0) {
                    left = mid + 1;
                }
                else {
                    return mid;
                }
            }
            return -1;
        }

        @Override
        T find(V key) {
            int index = findIndex(key);
            if (index == -1) {
                System.out.print("不存在此对象: ");
                return null;
            }
            return (T) values[index];
        }

        @Override
        Node<V, T> insert(V key, T value) {
            int index = 0;
            V tempKey = null;
            if (keyNumber > 0) {
                tempKey = (V) keys[keyNumber - 1];
                while (index < keyNumber && ((V)keys[index]).compareTo(key) < 0) {
                    index++;
                }
                if (index < keyNumber) {
                    System.arraycopy(keys, index, keys, index + 1, keyNumber - index);
                    System.arraycopy(values, index, values, index + 1, keyNumber - index);
                }
            }
            keys[index] = key;
            values[index] = value;
            keyNumber++;
            // 没有溢出，无需拆分,但是最大值变化
            if (keyNumber <= degree) {
                if (index == keyNumber - 1) {
                    Node node = this;
                    while (node.parent != null) {
                        node.parent.keys[node.parent.keyNumber - 1] = key;
                        node = node.parent;
                    }
                }

                System.out.print("叶节点插入：");
                this.print();
                System.out.println();

                return null;
            }
            // 需要拆分
            int mid = this.keyNumber / 2;
            LeafNode<V, T> newLeaf = new LeafNode<>();
            newLeaf.keyNumber = this.keyNumber - mid;
            if (this.parent == null) {
                BPlusNode<V, T> newParent = new BPlusNode<>();
                this.parent = newParent;
                tempKey = null;
            }
            newLeaf.parent = this.parent;
            System.arraycopy(keys, mid, newLeaf.keys, 0, newLeaf.keyNumber);
            System.arraycopy(values, mid, newLeaf.values, 0, newLeaf.keyNumber);
            keyNumber = mid;
            // 同一层链式关系
            newLeaf.next = this.next;
            this.next = newLeaf;

            BPlusNode<V, T> parent = (BPlusNode<V, T>) this.parent;

            System.out.print("叶节点拆分：");
            this.print();
            newLeaf.print();
            System.out.println();

            return parent.insertParent(this, newLeaf, tempKey);
        }
    }

    class BPlusNode<V extends Comparable<V>, T> extends Node<V, T> {

        public BPlusNode() {
            super();
        }

        /**
         * 考虑节点数量超过阶数的拆分情况，向父节点插入拆分后的新节点并递归处理
         * @param node1
         * @param node2
         * @param key
         * @return
         */
        Node<V, T> insertParent(Node<V, T> node1, Node<V, T> node2, V key) {
            V tempKey = null;
            if (this.keyNumber > 0) {
                tempKey = (V)keys[keyNumber - 1];
            }

            // key == null 表示新创建父节点，可以直接往里添加
            if (key == null) {
                keys[this.keyNumber] = node1.keys[node1.keyNumber - 1];
                childs[this.keyNumber++] = node1;
                keys[this.keyNumber] = node2.keys[node2.keyNumber - 1];
                childs[this.keyNumber++] = node2;
                // 更新 Root
                Node<V, T> freshRoot = this;
                while (freshRoot.parent != null) {
                    freshRoot = freshRoot.parent;
                }
                root = (BPlusNode)freshRoot;
                return this;
            }

            int keyIndex = -1;
            for (int i = 0; i < keyNumber; i++) {
                if (key.compareTo((V)keys[i]) == 0) {
                    keyIndex = i;
                }
            }
            System.arraycopy(keys, keyIndex, keys, keyIndex + 1, keyNumber - keyIndex);
            keys[keyIndex] = node1.keys[node1.keyNumber - 1];
            keys[keyIndex + 1] = node2.keys[node2.keyNumber - 1];
            System.arraycopy(childs, keyIndex, childs, keyIndex + 1, keyNumber - keyIndex);
            childs[keyIndex] = node1;
            childs[keyIndex + 1] = node2;
            this.keyNumber++;
            if (this.keyNumber <= degree) {
                return null;
            }
            // 超出了阶数，对节点进行拆分
            int mid = this.keyNumber / 2;
            BPlusNode<V, T> newBPlusNode = new BPlusNode<>();
            if (parent == null) {
                BPlusNode<V, T> newParent = new BPlusNode<>();
                this.parent = newParent;
                tempKey = null;
            }
            newBPlusNode.parent = this.parent;
            newBPlusNode.keyNumber = this.keyNumber - mid;
            System.arraycopy(keys, mid, newBPlusNode.keys, 0, newBPlusNode.keyNumber);
            System.arraycopy(childs, mid, newBPlusNode.childs, 0, newBPlusNode.keyNumber);

            // ！！！关键点！！！ -> 拆分后，要修改原节点子节点的父节点关系
            for (int i = 0; i < newBPlusNode.keyNumber; i++) {
                newBPlusNode.childs[i].parent = newBPlusNode;
            }

            keyNumber = mid;
            // 更新 Root
            Node<V, T> freshRoot = this;
            while (freshRoot.parent != null) {
                freshRoot = freshRoot.parent;
            }
            root = (BPlusNode)freshRoot;

            //
            newBPlusNode.next = this.next;
            this.next = newBPlusNode;

            BPlusNode<V, T> parent = (BPlusNode<V, T>) this.parent;

            System.out.print("非叶节点插入：");
            print();
            System.out.println();

            return parent.insertParent(this, newBPlusNode, tempKey);
        }

        /**
         * 遍历对比获取下标
         * @param key
         * @return
         */
        int findIndex(V key) {
            int index = 0;
            for (int i = 0; i < this.keyNumber - 1; i++) {
                if (key.compareTo((V)keys[i]) > 0) {
                    index++;
                } else {
                    break;
                }
            }
            return index;
        }

        @Override
        T find(V key) {
            return childs[findIndex(key)].find(key);
        }

        @Override
        Node<V, T> insert(V key, T value) {
            return childs[findIndex(key)].insert(key, value);
        }
    }
}
