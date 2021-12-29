package sort;

import org.junit.Test;

import java.util.Random;

/**
 * @author vaxtomis
 */
public class QuickSortTest {
    @Test
    public void test() {
        Sorter sorter = new Sorter();
        Random random = new Random();
        Integer[] array = new Integer[1000];
        for (int i = 0; i < 1000; i++) {
            array[i] = random.nextInt(1000000);
        }
        sorter.sort(QuickSort.class, array);
    }
}
