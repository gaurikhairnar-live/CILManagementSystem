
package dsa;

public class CustomHashSet<K> {

    // Store each unique key in our custom hash table
    private CustomHashTable<K, Boolean> hashTable;

    // Constructor
    public CustomHashSet() {
        hashTable = new CustomHashTable<>();
    }

    // Add a key only if it is not already present
    public boolean add(K key) {

        if (key == null || hashTable.containsKey(key)) {
            return false;
        }

        hashTable.put(key, true);
        return true;
    }

    // Check whether a key exists in the set
    public boolean contains(K key) {
        return hashTable.containsKey(key);
    }

    // Remove a key from the set
    public boolean remove(K key) {

        if (!hashTable.containsKey(key)) {
            return false;
        }

        hashTable.remove(key);
        return true;
    }

    // Return the number of unique keys
    public int size() {
        return hashTable.size();
    }

    // Check whether the set is empty
    public boolean isEmpty() {
        return hashTable.isEmpty();
    }
}
