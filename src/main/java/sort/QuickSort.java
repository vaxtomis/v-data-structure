package sort;

/**
 * @author vaxtomis
 */
public class QuickSort {
    public void sort(int[] array) {
        __quickSort(array, 0, array.length - 1);
    }

    private void __quickSort(int[] array, int l, int r) {
        int i = l, j = r;
        if (i >= j) return;
        while (i < j) {
            while (i < j && array[j] >= array[l]) j--;
            while (i < j && array[i] <= array[r]) i++;
            swap(array, i, j);
        }
        swap(array, l, i);
        __quickSort(array, l, i-1);
        __quickSort(array, i+1, r);
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
