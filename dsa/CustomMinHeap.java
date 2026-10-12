
package dsa;

import java.util.ArrayList;
import java.util.Comparator;

public class CustomMinHeap<T> {

    private ArrayList<T> heap;
    private Comparator<? super T> comparator;

    // Constructor receives the ordering rule
    public CustomMinHeap(Comparator<? super T> comparator) {

        if (comparator == null) {
            throw new IllegalArgumentException(
                    "Comparator cannot be null");
        }

        heap = new ArrayList<>();
        this.comparator = comparator;
    }

    // Swap two elements
    private void swap(int i, int j) {

        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // Restore min-heap property after insertion
    private void heapifyUp(int index) {

        while (index > 0) {

            int parent = (index - 1) / 2;

            if (comparator.compare(
                    heap.get(index), heap.get(parent)) >= 0) {
                break;
            }

            swap(index, parent);
            index = parent;
        }
    }

    // Restore min-heap property after removing the root
    private void heapifyDown(int index) {

        int n = heap.size();

        while (true) {

            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < n &&
                    comparator.compare(
                            heap.get(left), heap.get(smallest)) < 0) {

                smallest = left;
            }

            if (right < n &&
                    comparator.compare(
                            heap.get(right), heap.get(smallest)) < 0) {

                smallest = right;
            }

            if (smallest == index) {
                break;
            }

            swap(index, smallest);
            index = smallest;
        }
    }

    // Insert a vacant quarter into the heap
    public void insert(T item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "Heap item cannot be null");
        }

        heap.add(item);
        heapifyUp(heap.size() - 1);
    }

    // View the smallest element without removing it
    public T peek() {

        if (heap.isEmpty()) {
            return null;
        }

        return heap.get(0);
    }

    // Remove and return the smallest element
    public T extractMin() {

        if (heap.isEmpty()) {
            return null;
        }

        T min = heap.get(0);
        int lastIndex = heap.size() - 1;

        if (lastIndex == 0) {
            heap.remove(lastIndex);
            return min;
        }

        // Move the last element to the root
        heap.set(0, heap.remove(lastIndex));

        heapifyDown(0);

        return min;
    }

    // Return a copy of the elements for viewing
    public ArrayList<T> getAllElements() {
        return new ArrayList<>(heap);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }
}
