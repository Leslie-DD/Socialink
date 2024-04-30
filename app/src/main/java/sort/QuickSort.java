package sort;

public class QuickSort extends AbstractSort {

    public QuickSort(int[] array) {
        super("QuickSort", array);
    }

    @Override
    public void sortImpl() {
        quickSort(0, array.length - 1, array);
    }

    public static void quickSort(int l, int r, int[] array) {
        if (l >= r) {
            return;
        }
        int mid = partition(l, r, array);
        quickSort(l, mid - 1, array);
        quickSort(mid + 1, r, array);
    }

    public static int partition(int l, int r, int[] array) {
        int j = r;
        int i = l;
        int valueMid = array[i];
        while (i < j) {
            while (j > i && array[j] > valueMid) {
                j--;
            }
            if (i < j) {
                array[i++] = array[j];
            }
            while (j > i && array[i] < valueMid) {
                i++;
            }
            if (i < j) {
                array[j--] = array[i];
            }
        }
        array[i] = valueMid;
        return i;
    }
}
