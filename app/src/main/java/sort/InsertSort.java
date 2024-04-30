package sort;

// TODO
public class InsertSort extends AbstractSort{
    public InsertSort(int[] array) {
        super("InsertSort", array);
    }

    @Override
    public void sortImpl() {
        insertSort();
    }

    private void insertSort() {
        int len = array.length;
        if (len <= 1) {
            return;
        }
        for (int i = 0; i < len - 1; i++) {
            int targetIndex = i + 1;
            int targetValue = array[targetIndex];
            int j;
            for (j = 0; j < targetIndex; j++) {
                if (targetValue < array[j]) {
                    break;
                }
            }

            for (int k = j; k < targetIndex; k++) {
                array[k + 1] = array[k];
            }
            array[j] = targetValue;

        }
    }
}
