
package dsa;

import java.util.ArrayList;

public class CustomStack<T> {

    // Each node stores one element and a link to the next node
    private static class Node<T> {

        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> top;
    private int count;

    // Constructor
    public CustomStack() {
        top = null;
        count = 0;
    }

    // Add an element to the top of the stack
    public void push(T item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "Stack item cannot be null");
        }

        Node<T> newNode = new Node<>(item);

        newNode.next = top;
        top = newNode;

        count++;
    }

    // Remove and return the top element
    public T pop() {

        if (isEmpty()) {
            return null;
        }

        T item = top.data;
        top = top.next;

        count--;

        return item;
    }

    // View the top element without removing it
    public T peek() {

        if (isEmpty()) {
            return null;
        }

        return top.data;
    }

    // Check whether the stack is empty
    public boolean isEmpty() {
        return top == null;
    }

    // Return the number of elements
    public int size() {
        return count;
    }

    // Return all elements without modifying the stack
    public ArrayList<T> getAllElements() {

        ArrayList<T> elements = new ArrayList<>();

        Node<T> temp = top;

        while (temp != null) {
            elements.add(temp.data);
            temp = temp.next;
        }

        return elements;
    }
}
