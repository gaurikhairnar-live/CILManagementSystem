
package dsa;

import java.util.Comparator;

public class QuickSort {

    // Sort an array using the given comparison rule
    public static <T> void sort(T[] array,
            Comparator<? super T> comparator) {

        if (array == null || comparator == null ||
                array.length <= 1) {
            return;
        }

        quickSort(array, 0, array.length - 1, comparator);
    }

    // Recursive Quick Sort
    private static <T> void quickSort(T[] array, int low, int high,
            Comparator<? super T> comparator) {

        if (low < high) {

            // Place the pivot at its correct position
            int pivotIndex = partition(
                    array, low, high, comparator);

            // Sort elements before the pivot
            quickSort(array, low, pivotIndex - 1, comparator);

            // Sort elements after the pivot
            quickSort(array, pivotIndex + 1, high, comparator);
        }
    }

    // Partition the array around the last element as pivot
    private static <T> int partition(T[] array, int low, int high,
            Comparator<? super T> comparator) {

        T pivot = array[high];

        int i = low - 1;

        for (int j = low; j < high; j++) {

            // Elements <= pivot are moved to the left
            if (comparator.compare(array[j], pivot) <= 0) {

                i++;
                swap(array, i, j);
            }
        }

        // Move pivot to its correct sorted position
        swap(array, i + 1, high);

        return i + 1;
    }

    // Swap two array elements
    private static <T> void swap(T[] array, int i, int j) {

        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
