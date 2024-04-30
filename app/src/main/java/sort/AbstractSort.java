package sort;

import java.util.Locale;

abstract public class AbstractSort {
    private String tag = "abstract_sort";

    public int[] array;

    private long startTime;
    private long endTime;

    public AbstractSort(String tag, int[] array) {
        this.tag = tag;
        this.array = array.clone();
    }

    public void start() {
        startTime = System.nanoTime();
        sortImpl();
        endTime = System.nanoTime();
        printTimeCost();
    }

    public abstract void sortImpl();

    public boolean check() {
        int len = array.length;
        if (len == 1) {
            return true;
        }
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i-1]) return false;
        }
        return true;
    }

    public void printTimeCost() {
        System.out.printf(Locale.CHINA, "%-15s result: %-5s cost: %,d ns%n", tag, check(), endTime - startTime);
    }
}
