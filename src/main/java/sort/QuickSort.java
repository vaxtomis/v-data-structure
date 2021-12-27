package sort;

import java.util.Arrays;

/**
 * @author vaxtomis
 */
public class QuickSort implements Sort {

    @Override
    public void sort(Comparable[] array) {
        long startTime = System.currentTimeMillis();
        quickSort(array, 0, array.length - 1);
        long endTime = System.currentTimeMillis();
        System.out.println(Arrays.toString(array));
        System.out.println("快排用时： " + (startTime - endTime) + " ms");

    }

    private void quickSort(Comparable[] array, int l, int r) {
        int i = l, j = r;
        if (i >= j) return;
        while (i < j) {
            while (i < j && array[j].compareTo(array[l]) >= 0) j--;
            while (i < j && array[i].compareTo(array[r]) <= 0) i++;
            swap(array, i, j);
        }
        swap(array, l, i);
        quickSort(array, l, i-1);
        quickSort(array, i+1, r);
    }

    private void swap(Comparable[] array, int i, int j) {
        Comparable temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}
