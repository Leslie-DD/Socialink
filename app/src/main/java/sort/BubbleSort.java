package sort;

public class BubbleSort extends AbstractSort {
    public BubbleSort(int[] array) {
        super("BubbleSort", array);
    }

    @Override
    public void sortImpl() {
        int len = array.length;
        if (len <= 1) {
            return;
        }
        for (int i = 0; i < len - 1; i++) {
            for (int j = len - 1; j > i; j--) {
                if (array[j - 1] > array[j]) {
                    int tempVal = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = tempVal;
                }
            }
        }
    }
}
