package tree;

import org.junit.Test;

/**
 * @author vaxtomis
 * @date 2021-12-21
 */
public class BPlusTreeTest {
    @Test
    public void test() {
        BPlusTree<Integer, String> tree = new BPlusTree<>();
        tree.insert(1,"1");
        tree.insert(2,"2");
        tree.insert(4,"4");
        tree.insert(5,"5");
        tree.insert(7,"7");
        tree.insert(12,"12");
        tree.insert(45,"45");
        tree.insert(3,"3");
        tree.insert(36,"36");
        tree.insert(9,"9");
        tree.insert(19,"19");
        tree.insert(27,"27");
        tree.insert(14,"14");
        tree.insert(8,"8");
        tree.insert(46,"46");

        tree.printTree();

//        System.out.println(tree.find(12));
//        System.out.println(tree.find(36));
//        System.out.println(tree.find(9));
//        System.out.println(tree.find(3));
//        System.out.println(tree.find(4));
//        System.out.println(tree.find(8));
    }
}
