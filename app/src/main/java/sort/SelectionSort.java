package sort;

public class SelectionSort extends AbstractSort {

    public SelectionSort(int[] array) {
        super("SelectionSort", array);
    }

    @Override
    public void sortImpl() {
        int len = array.length;
        if (len <= 1) {
            return;
        }
        for (int i = 0; i < len - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < len; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            int tempVal = array[minIndex];
            array[minIndex] = array[i];
            array[i] = tempVal;
        }
    }
}
