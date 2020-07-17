
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

// Class Pair represents a pair (key, value).
class Pair<K,V> { 

    K key;

    V value;

    Pair(K k, V v) {

         key = k;

         value = v;

    }

    public int hashCode() {

        return key.hashCode();

    }

    public boolean equals(Object other) {

        if (! (other instanceof Pair<?,?>)) return false;

         Pair<?,?> o = (Pair<?,?>) other;

        return key.equals(o.key) && value.equals(o.value);

    }

}

// The DSAHashTable class represents a hash table with concatenation (the first variant in the textbook).
class DSAHashTable<K,V> {

    Set<Pair<K, V>>[] hashTable;
    int quantity;
    int size;

    // Creates an empty DSAHashTable instance, the length of the inner field is set to size, the contents 
    // of the inner field are initialized to empty sets.
    DSAHashTable(int size) { 
        
        hashTable = (Set<Pair<K, V>>[]) new Set<?>[size];

        for (int i = 0; i < size; i++) {
            hashTable[i] = new HashSet<Pair<K, V>>();
        }
        this.size = size;
        quantity = 0;
    }

    // Store the pair (key, value) in the hash table. If there is already another pair 
    // with the same key in the table, it is deleted. The key or the value mustn't be null. 
    // If the number of pairs in the table after performing a put should increase above twice 
    // the length of the inner field, the inner field doubles.
    void put(K key, V value) { 

        if(key==null || value==null){
            return;
        }
        
        remove(key);
        quantity++;

        Pair<K, V> pair = new Pair<K, V>(key, value);
        int index = getIndexOf(key);
        hashTable[index].add(pair);

        int twiceTheAmount = hashTable.length * 2;
        if (quantity > twiceTheAmount) {
            resize(twiceTheAmount);
        }

    }

    // Returns the value associated with a given key, or null if the given key is not in the table.
    V get(K key) { 

        if (key == null) {
            return null;
        }

        int index = this.getIndexOf(key);
        Set<Pair<K, V>> s = hashTable[index];

        Iterator<Pair<K, V>> iterator = s.iterator();

        while (iterator.hasNext()) {
            Pair<K, V> p = iterator.next(); 
            V value = null;
            if (p.key.equals(key)) {
                value = p.value;
                return value;
            }
        }
        return null;
    }
    
    // Deletes the pair with the given key. If there is no given key in the table, it does nothing.
    void remove(K key) { 
        int index = this.getIndexOf(key);
        Set<Pair<K, V>> s = hashTable[index];

        Iterator<Pair<K, V>> iterator = s.iterator();

        while (iterator.hasNext()) {
            Pair<K, V> p = iterator.next(); 
            if (p.key.equals(key)) {
                s.remove(p);
                quantity--;
                break;
            }
        }

    }

    // Returns the inner field. The elements of the inner array can be instances of classes 
    // in the java.util package, ie. you do not need to write your own implementation of the java.util.Set interface.
    Set<Pair<K,V>>[] getArray() { 
        return hashTable;
    }

    // Returns the index in the array for the given key. The key.hashCode is used as the hash function.
    int getIndexOf(K key) { 
        int hashCode = key.hashCode();
        int index = hashCode % hashTable.length;
        return index;
    }

    // If the number of elements is less than or equal to twice the length of the inner array, returns true, 
    // otherwise returns false.
    boolean isBigEnough() { 
        if(quantity <= hashTable.length * 2) {
            return true;
        }
        return false;
    }

    // Changes the length of the inner array, initializes it with empty sets, and copies all pairs into it.
    void resize(int newSize) { 
        DSAHashTable<K, V> newHashTable = new DSAHashTable<K, V>(newSize);
    
        for (int i = 0; i < hashTable.length; i++) {

            for (Pair<K, V> pair : hashTable[i]) {
                K key = pair.key;
                V value = pair.value;

                newHashTable.put(key, value);
            }
        }

        hashTable = newHashTable.hashTable;
    }

    // Returns an iterator over all pairs in the table. The iterator does not have to have the remove method implemented.
    Iterator<Pair<K,V>> iterator() { 
        Set<Pair<K, V>> set = new HashSet<Pair<K, V>>();

        for (int i = 0; i < hashTable.length; i++) {
            
            for (Pair<K, V> pair : hashTable[i]) {
                set.add(pair);
            }
        }

        return set.iterator();
    }

}
