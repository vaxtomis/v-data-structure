package tree;

import org.junit.Test;

import java.util.Random;

/**
 * @author vaxtomis
 * @date 2021-12-21
 */
public class BPlusTreeTest {
    @Test
    public void test() {
        BPlusTree<Integer, String> tree = new BPlusTree<>(10);
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int randomNum = random.nextInt(1000);
            tree.insert(randomNum, String.valueOf(randomNum));
        }
        tree.printTree();
    }
}
