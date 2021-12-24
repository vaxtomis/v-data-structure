package sort;

import java.util.HashMap;

/**
 * @author vaxtomis
 */
public class Sorter<V extends Comparable<V>> {
    private static final HashMap<Class<?>, Sort> sortMap = new HashMap<>();
    private static final QuickSort quickSort = new QuickSort();

    static {
        sortMap.put(QuickSort.class, quickSort);
    }

    public void sort(Class<?> clazz, V[] array) {
        sortMap.get(clazz).sort(array);
    }
}
