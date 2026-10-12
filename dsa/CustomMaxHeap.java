
package dsa;

import java.util.ArrayList;
import java.util.Comparator;

public class CustomMaxHeap<T> {

    private ArrayList<T> heap;
    private Comparator<? super T> comparator;

    // Constructor receives the priority comparison rule
    public CustomMaxHeap(Comparator<? super T> comparator) {

        if (comparator == null) {
            throw new IllegalArgumentException(
                    "Comparator cannot be null");
        }

        heap = new ArrayList<>();
        this.comparator = comparator;
    }

    // Swap two elements in the heap
    private void swap(int i, int j) {

        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // Move an element upward until heap property is restored
    private void heapifyUp(int index) {

        while (index > 0) {

            int parent = (index - 1) / 2;

            if (comparator.compare(
                    heap.get(index), heap.get(parent)) <= 0) {
                break;
            }

            swap(index, parent);
            index = parent;
        }
    }

    // Move an element downward until heap property is restored
    private void heapifyDown(int index) {

        int n = heap.size();

        while (true) {

            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;

            if (left < n &&
                    comparator.compare(
                            heap.get(left), heap.get(largest)) > 0) {

                largest = left;
            }

            if (right < n &&
                    comparator.compare(
                            heap.get(right), heap.get(largest)) > 0) {

                largest = right;
            }

            if (largest == index) {
                break;
            }

            swap(index, largest);
            index = largest;
        }
    }

    // Insert a new element into the max-heap
    public void insert(T item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "Heap item cannot be null");
        }

        heap.add(item);
        heapifyUp(heap.size() - 1);
    }

    // View the highest-priority element without removing it
    public T peek() {

        if (heap.isEmpty()) {
            return null;
        }

        return heap.get(0);
    }

    // Remove and return the highest-priority element
    public T extractMax() {

        if (heap.isEmpty()) {
            return null;
        }

        T max = heap.get(0);
        int lastIndex = heap.size() - 1;

        if (lastIndex == 0) {
            heap.remove(lastIndex);
            return max;
        }

        // Replace the root with the last element
        heap.set(0, heap.remove(lastIndex));

        heapifyDown(0);

        return max;
    }

    // Remove a specific element from anywhere in the heap
    public boolean remove(T item) {

        int index = -1;

        // Find the requested element
        for (int i = 0; i < heap.size(); i++) {

            if (heap.get(i).equals(item)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        int lastIndex = heap.size() - 1;

        // If it is the last element, simply remove it
        if (index == lastIndex) {
            heap.remove(lastIndex);
            return true;
        }

        // Replace removed element with the last element
        heap.set(index, heap.remove(lastIndex));

        // Restore heap property in the required direction
        if (index > 0 &&
                comparator.compare(
                        heap.get(index),
                        heap.get((index - 1) / 2)) > 0) {

            heapifyUp(index);
        } else {
            heapifyDown(index);
        }

        return true;
    }

    // Return a copy for displaying all heap elements
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
