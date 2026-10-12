
package dsa;

public class CustomHashTable<K, V> {

    // Each node stores a key-value pair
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Node<K, V>[] table;
    private int capacity;
    private int count;

    // Constructor
    @SuppressWarnings("unchecked")
    public CustomHashTable() {
        capacity = 101;
        table = (Node<K, V>[]) new Node[capacity];
        count = 0;
    }

    // Calculate the array index for a key
    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % capacity;
    }

    // Insert a new key-value pair or update an existing key
    public void put(K key, V value) {

        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = hash(key);
        Node<K, V> temp = table[index];

        // Search the chain for an existing key
        while (temp != null) {

            if (temp.key.equals(key)) {
                temp.value = value;
                return;
            }

            temp = temp.next;
        }

        // Insert the new node at the beginning of the chain
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;

        count++;
    }

    // Retrieve a value using its key
    public V get(K key) {

        if (key == null) {
            return null;
        }

        int index = hash(key);
        Node<K, V> temp = table[index];

        while (temp != null) {

            if (temp.key.equals(key)) {
                return temp.value;
            }

            temp = temp.next;
        }

        return null;
    }

    // Check whether a key exists
    public boolean containsKey(K key) {

        if (key == null) {
            return false;
        }

        int index = hash(key);
        Node<K, V> temp = table[index];

        while (temp != null) {

            if (temp.key.equals(key)) {
                return true;
            }

            temp = temp.next;
        }

        return false;
    }

    // Remove a key-value pair
    public V remove(K key) {

        if (key == null) {
            return null;
        }

        int index = hash(key);

        Node<K, V> temp = table[index];
        Node<K, V> previous = null;

        while (temp != null) {

            if (temp.key.equals(key)) {

                if (previous == null) {
                    table[index] = temp.next;
                } else {
                    previous.next = temp.next;
                }

                count--;
                return temp.value;
            }

            previous = temp;
            temp = temp.next;
        }

        return null;
    }

    // Return the number of stored key-value pairs
    public int size() {
        return count;
    }

    // Check whether the hash table is empty
    public boolean isEmpty() {
        return count == 0;
    }
}
