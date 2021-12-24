package sort;

public interface Sort<V extends Comparable<V>> {
    void sort(V[] array);
}
