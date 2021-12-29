package list;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author vaxtomis
 */
public class SkipListTest {
    @Test
    public void test() {
        SkipList<String> skipList = new SkipList<>();
        LinkedList<Integer> list = new LinkedList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randomNum = random.nextInt(100);
            list.add(randomNum);
            skipList.insert(String.valueOf(randomNum), String.valueOf(randomNum));
        }

        System.out.println(Arrays.toString(list.toArray()));
        for (int i : list) {
            System.out.print("[" + skipList.find(String.valueOf(i)) + "] ");
        }
    }
}
