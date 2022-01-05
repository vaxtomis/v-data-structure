import list.SkipList;
import org.junit.Test;
import tree.BPlusTree;
import tree.RedBlackTree;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author vaxtomis
 */
public class CompareTest {
    @Test
    public void test() {
        ArrayList<String> list = create(10000);
        ArrayList<Long> time = new ArrayList<>();
        testBPT(list, time);
        testRBT(list, time);
        testSL(list, time);

        System.out.println("BPlusTree 插入用时： " + time.get(0) + " ms");
        System.out.println("BPlusTree 查询用时： " + time.get(1) + " ms");

        System.out.println("RedBlackTree 插入用时： " + time.get(2) + " ms");
        System.out.println("RedBlackTree 查询用时： " + time.get(3) + " ms");

        System.out.println("SkipList 插入用时： " + time.get(4) + " ms");
        System.out.println("SkipList 查询用时： " + time.get(5) + " ms");


    }

    private ArrayList<String> create(int n) {
        ArrayList<String> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int randomNum = random.nextInt(9 * n);
            list.add(String.valueOf(randomNum));
        }
        return list;
    }

    private void testBPT(ArrayList<String> list, ArrayList<Long> time) {
        BPlusTree<String, String> bPlusTree = new BPlusTree(10);
        // BPT
        long BPT_I_S = System.currentTimeMillis();
        for (String i : list) {
            bPlusTree.insert(i, i);
        }
        long BPT_I_E = System.currentTimeMillis();

        long BPT_F_S = System.currentTimeMillis();
        for (String i : list) {
            bPlusTree.find(i);
        }
        long BPT_F_E = System.currentTimeMillis();
        time.add(BPT_I_E - BPT_I_S);
        time.add(BPT_F_E - BPT_F_S);
    }

    public void testRBT(ArrayList<String> list, ArrayList<Long> time) {
        RedBlackTree<String, String> redBlackTree = new RedBlackTree();
        // RBT
        long RBT_I_S = System.currentTimeMillis();
        for (String i : list) {
            redBlackTree.insert(i, i);
        }
        long RBT_I_E = System.currentTimeMillis();


        long RBT_F_S = System.currentTimeMillis();
        for (String i : list) {
            redBlackTree.find(i);
        }
        long RBT_F_E = System.currentTimeMillis();
        time.add(RBT_I_E - RBT_I_S);
        time.add(RBT_F_E - RBT_F_S);
    }

    public void testSL(ArrayList<String> list, ArrayList<Long> time) {
        SkipList<String> skipList = new SkipList<>();
        // SL
        long SL_I_S = System.currentTimeMillis();
        for (String i : list) {
            skipList.insert(i, i);
        }
        long SL_I_E = System.currentTimeMillis();

        long SL_F_S = System.currentTimeMillis();
        for (String i : list) {
            skipList.find(i);
        }
        long SL_F_E = System.currentTimeMillis();
        time.add(SL_I_E - SL_I_S);
        time.add(SL_F_E - SL_F_S);
    }
}
