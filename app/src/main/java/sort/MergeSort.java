package sort;

public class MergeSort extends AbstractSort {

    public MergeSort(int[] array) {
        super("MergeSort", array);
    }

    @Override
    public void sortImpl() {
        mergeSort(0, array.length - 1);
    }

    public void mergeSort(int l, int r) {
        if (l >= r) return;

        int mid = (l + r) / 2;
        mergeSort(l, mid);
        mergeSort(mid + 1, r);

        int[] tmp = new int[r - l + 1];
        int i = l;
        int j = mid + 1;
        int k = 0;

        while (i <= mid && j <= r) {
            if (array[i] <= array[j]) {
                tmp[k++] = array[i++];
            } else {
                tmp[k++] = array[j++];
            }
        }

        while (i <= mid) {
            tmp[k++] = array[i++];
        }

        while (j <= r) {
            tmp[k++] = array[j++];
        }

        // 将排序后的元素，全部都整合到数组a中。
        for (i = 0; i < k; i++) {
            array[l + i] = tmp[i];
        }

        tmp = null;
    }
}
