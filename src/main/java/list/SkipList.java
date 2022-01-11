package list;

import java.util.Random;

/**
 * 模拟 Redis 中的 zskiplist 结构
 *
 * SkipList 结构
 * header, tail, level, length
 *
 * SkipListNode 结构
 * levels[], backward, score(这里替换为 key), obj(这里替换为 value)
 *
 * SkipListLevel 结构
 * forward, span
 *
 * @author vaxtomis
 * @date 2021-12-27
 */
public class SkipList<T> {
    private SkipListNode<T> header;
    private SkipListNode<T> tail;
    private Integer level;
    private Integer length;
    private static final Integer MAX_LEVEL = 16;

    public SkipList() {
        header = new SkipListNode<>(null, null, MAX_LEVEL);
        tail = null;
        level = 1;
        length = 0;
    }

    // 跳跃表节点
    class SkipListNode<T> {
        protected SkipListLevel<T>[] levels;
        protected SkipListNode<T> backward;
        protected String key;
        protected T value;

        public SkipListNode(String key, T value, int levelNum) {
            this.key = key;
            this.value = value;
            this.backward = null;
            // 每个跳跃表节点的层高都是 1 至 32 之间的随机数
            System.out.println("创建新 SkipListNode，层高为：" + levelNum + "，键{" + key + "}");
            this.levels = new SkipListLevel[levelNum];
            for (int i = 0; i < levelNum; i++) {
                this.levels[i] = new SkipListLevel<>();
            }
        }

        // 查询操作
        private T find(String key, Integer curLevel) {
            for (;curLevel >= 0; curLevel--) {
                // 跳过指向 null 的 level.forward
                if (levels[curLevel].forward == null) {
                    continue;
                }
                if (key.compareTo(levels[curLevel].forward.key) == 0) {
                    return levels[curLevel].forward.value;
                }
                if (key.compareTo(levels[curLevel].forward.key) > 0) {
                    return levels[curLevel].forward.find(key, curLevel);
                }
            }
            return null;
        }

        private SkipListNode<T> findPos(String key, Integer curLevel, Integer random, SkipListNode insertNode) {
            for (;curLevel >= 0; curLevel--) {
                // 跳过指向 null 的 level.forward
                if (levels[curLevel].forward == null) {
                    if (curLevel < random && curLevel > 0) {
                        insertNode.levels[curLevel].forward = levels[curLevel].forward;
                        levels[curLevel].forward = insertNode;
                    }
                    continue;
                }
                if (key.compareTo(levels[curLevel].forward.key) > 0) {
                    return levels[curLevel].forward.findPos(key, curLevel, random, insertNode);
                }
                if (curLevel < random && curLevel > 0) {
                    insertNode.levels[curLevel].forward = levels[curLevel].forward;
                    levels[curLevel].forward = insertNode;
                }
            }
            return this;
        }
    }

    // 跳跃表节点层
    class SkipListLevel<T> {
        protected SkipListNode<T> forward;
        // span 仅用于查找排位，与 score 并无关系
        //protected Integer span;

        public SkipListLevel() {
            this(null);
        }

        public SkipListLevel(SkipListNode<T> forward) {
            this.forward = forward;
            //this.span = span;
        }
    }

    public T find(String key) {
        return header.find(key, level - 1);
    }

    public void insert(String key, T value) {
        System.out.println("=== 开始插入键为 " + key + " 的节点 ===");

        // 先计算出节点的随机层高，创建新节点
        int random = randomLevel();
        SkipListNode insertNode = new SkipListNode(key, value, random);
        SkipListNode base = header.findPos(key, header.levels.length - 1, random, insertNode);
        System.out.println("前节点键为 {" + base.key + "}");

        // 赋值是否是当前最大 level
        if (insertNode.levels.length > level) {
            level = insertNode.levels.length;
        }

        // 前一个节点不为头结点
        if (base != header) {
            insertNode.backward = base;
        }

        // 找到下一个节点
        SkipListNode nextNode = base.levels[0].forward;

        if (nextNode != null) {
            nextNode.backward = insertNode;
            System.out.println("后节点键为 {" + nextNode.key + "}");
        } else {
            System.out.println("当前插入节点成为尾节点");
            tail = insertNode;
        }
        insertNode.levels[0].forward = nextNode;
        base.levels[0].forward = insertNode;

        length++;
        System.out.println("=== 节点 " + key + " 插入结束 ===");
        System.out.println();
    }

    private int randomLevel() {
        int level = 1;
        Random random = new Random();
        for (int i = 1; random.nextInt(2) == 1 && i < MAX_LEVEL; i++) {
            level++;
        }
        return level;
    }
}
