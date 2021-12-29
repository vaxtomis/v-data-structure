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
 * forward, span(去掉了)
 *
 * @author vaxtomis
 * @date 2021-12-27
 */
public class SkipList<T> {
    private SkipListNode<T> header;
    private SkipListNode<T> tail;
    private Integer level;
    private Integer length;
    private static final Integer MAX_LEVEL = 32;

    public SkipList() {
        header = new SkipListNode<>(null, null, null, MAX_LEVEL);
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

        public SkipListNode(String key, T value, SkipListNode<T> backward, int levelNum) {
            this.key = key;
            this.value = value;
            this.backward = backward;
            // 每个跳跃表节点的层高都是 1 至 32 之间的随机数
            System.out.println("创建新 SkipListNode，层高为：" + levelNum + "，键{" + key + "}");
            this.levels = new SkipListLevel[levelNum];
            for (int i = 0; i < levelNum; i++) {
                this.levels[i] = new SkipListLevel<>();
            }
        }

        // find 和 findPos 是否可以考虑复用呢？
        private T find(String key) {
            int maxLevel = Math.min(level, levels.length) - 1;
            for (;maxLevel >= 0; maxLevel--) {
                // 跳过指向 null 的 level.forward
                if (levels[maxLevel].forward == null) {
                    continue;
                }
                if (levels[maxLevel].forward.key.equals(key)) {
                    return levels[maxLevel].forward.value;
                }
                if (key.compareTo(levels[maxLevel].forward.key) > 0) {
                    return levels[maxLevel].forward.find(key);
                }
            }
            return null;
        }

        private SkipListNode findPos(String key) {
            int maxLevel = Math.min(level, levels.length) - 1;
            for (;maxLevel >= 0; maxLevel--) {
                // 跳过指向 null 的 level.forward
                if (levels[maxLevel].forward == null) {
                    continue;
                }
                if (key.compareTo(levels[maxLevel].forward.key) > 0) {
                    return levels[maxLevel].forward.findPos(key);
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
        return header.find(key);
    }

    public void insert(String key, T value) {
        System.out.println("=== 开始插入键为 " + key + " 的节点 ===");
        SkipListNode base = header.findPos(key);
        System.out.println("前节点键为 {" + base.key + "}");
        int randomLevel = randomLevel();
        SkipListNode insertNode = new SkipListNode(key, value, base, randomLevel);
        SkipListNode nextNode = base.levels[0].forward;
        if (nextNode != null) {
            nextNode.backward = insertNode;
            System.out.println("后节点键为 {" + nextNode.key + "}");
        } else {
            tail = insertNode;
        }
        for (int i = 0; i < base.levels.length; i++) {
            if (i < randomLevel) {
                insertNode.levels[i].forward = base.levels[i].forward;
                base.levels[i].forward = insertNode;
            }
        }
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
