
package dsa;

import java.util.Comparator;

public class BinarySearch {

    // Search for an element in a sorted array
    public static <T> int search(T[] array, T target,
            Comparator<? super T> comparator) {

        if (array == null || target == null || comparator == null) {
            return -1;
        }

        int low = 0;
        int high = array.length - 1;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            int comparison = comparator.compare(
                    array[mid], target);

            // Target found
            if (comparison == 0) {
                return mid;
            }

            // Search in the right half
            else if (comparison < 0) {
                low = mid + 1;
            }

            // Search in the left half
            else {
                high = mid - 1;
            }
        }

        // Target not found
        return -1;
    }

    // Find the first matching element in a sorted array
    public static <T> int searchFirst(T[] array, T target,
            Comparator<? super T> comparator) {

        if (array == null || target == null || comparator == null) {
            return -1;
        }

        int low = 0;
        int high = array.length - 1;
        int result = -1;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            int comparison = comparator.compare(
                    array[mid], target);

            if (comparison == 0) {

                result = mid;

                // Continue searching towards the left
                high = mid - 1;
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return result;
    }
}
