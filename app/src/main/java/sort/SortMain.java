package sort;

import java.util.Random;

public class SortMain {

    private static final int CAPACITY = 2000;

    private static int[] randomArray(int capacity) {
        if (capacity < 0) {
            throw new IndexOutOfBoundsException();
        }
        int[] array = new int[capacity];
        Random random = new Random();
        for (int i = 0; i < capacity; i++) {
            array[i] = random.nextInt(capacity);
        }
        return array;
    }

    public static void main(String[] args) {
        int[] array = randomArray(CAPACITY);
//        System.out.println("origin array: " + Arrays.stream(array).boxed().collect(Collectors.toList()));
        System.out.println("origin array.length = " + array.length);
        new QuickSort(array).start();
        new MergeSort(array).start();
        new SelectionSort(array).start();
        new BubbleSort(array).start();
        new InsertSort(array).start();
        new ShellSort(array).start();
    }

}
