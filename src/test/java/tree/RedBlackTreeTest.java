package tree;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author vaxtomis
 */
public class RedBlackTreeTest {
    @Test
    public void main() {
        RedBlackTree<Integer, String> tree = new RedBlackTree<>();
        LinkedList<Integer> list = new LinkedList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int randomNum = random.nextInt(1000);
            list.add(randomNum);
            tree.insert(randomNum, String.valueOf(randomNum));
        }

        System.out.println(Arrays.toString(list.toArray()));
        for (int i : list) {
            System.out.println(tree.find(i));
        }
    }
}
