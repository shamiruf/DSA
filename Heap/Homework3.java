
interface DSAComparable<E> { 

    // Returns true if this is (sharply) smaller than other, false otherwise.
    boolean less(E other); 

    // Returns the smallest element of all E-type elements (an element that is 
    // smaller than any other E-type element). The return value does not depend on this, only on E.
    E getLeastElement(); 

    // Returns the largest element of all E-type elements (an element that is 
    // larger than any other E-type element). The return value does not depend on this, only on E.
    E getGreatestElement(); 

    // Returns the value of E. 
    int getValue();

} 

// The HeapStorage interface represents an array in which the heap stores its elements.
interface HeapStorage<E extends DSAComparable<E>> { 

    // Returns the current size of HeapStorage.
    int getSize(); 

    // Returns true if the current HeapStorage size is zero.
    boolean isEmpty(); 

    // Returns an element at index index (1 <= index <= getSize ()). If the index is 
    // less than or equal to zero, it returns the largest element. If the index is larger 
    // than the array size, it returns the smallest element. In both cases, you can assume 
    // that there is at least one element in HeapStorage.
    E getElement (int index);

    // Swap elements on indexes index and index2 (1 <= index, index2 <= getSize ())
    void swap (int index, int index2); 

    // Remove the last element (ie reduce the size of this HeapStorage) and return its value.
    E extractLast(); 

    // Inserts an element and returns its index. You can assume that there is enough space in the lower field.
    int insertLast (E element); 
} 

class Homework3<E extends DSAComparable<E>> implements HeapStorage<E> { 

    private E[] elements;
    private int size;

    // Creates a new HeapStorage object above the elements array, its size is the same as the length of the array.
    Homework3(E[] elements) { 
        this.elements = elements;
        this.size = elements.length;
    } 

    @Override
    public int getSize() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public E getElement (int index) {
        E min = elements[size-1];
        E max = elements[0];
        if (index <= 0) {
            for (int i = 0; i < elements.length; i++) {
                if (max.less(elements[i])) {
                    max = elements[i];
                }
            }
            return max;
        } else if (index > size) {
            for (int i = 0; i < elements.length; i++) {
                if (elements[i].less(min)) {
                    min = elements[i];
                }
            }
            return min;
        } else {
            index = index - 1;
            return elements[index];
        }
    }

    @Override
    public void swap (int index, int index2) {
        E first = elements[index - 1];
        E second = elements[index2 - 1];
        elements[index - 1] = second;
        elements[index2 - 1] = first;
    }

    @Override
    public E extractLast(){
        E last = elements[size - 1];
        elements[size - 1] = null;
        size--;
        return last;
    }
    
    @Override
    public int insertLast (E element){
        elements[size] = element;
        size++;
        return size;
    }
    
} 

// Class Heap represents a heap (with a maximum at the top).
class Heap<E extends DSAComparable<E>> {
    
    HeapStorage<E> storage; 
    int size;

    // Create a heap over the given HeapStorage (ie call the build heap algorithm).
    Heap(HeapStorage<E> storage) { 
        this.storage = storage;
        this.size = storage.getSize();

        for (int i = size/2; i > 0; i--){
            heapify(i);
        }
    } 

    // Call the heapify algorithm over the node on the index.
    void heapify(int index) { 
        int largest = index;
        int left = 2 * index;
        int right = 2 * index + 1;

        if( left <= size && storage.getElement(largest).less(storage.getElement(left))) {
            largest = left;
        }

        if( right <= size && storage.getElement(largest).less(storage.getElement(right))) {
            largest = right;
        }

        if (largest != index) {
            storage.swap(index, largest);
            heapify(largest);
        }
    } 

    // Inserts a new element into the heap. You can assume that there is a place in the field inside the HeapStorage.
    void insert(E element) { 
        storage.insertLast(element);
        size++;

        for (int i = size; i > 0; i--) {
            heapify(i);
        }
    } 

    // Remove and return the maximum element from the heap.
    E extractMax() {  
        int left = 1;
        E max = storage.getElement(left);
        storage.swap(1, size);
        storage.extractLast();
        size--;
        heapify(1);
        return max;
    } 

    // Returns true if the heap is empty
    boolean isEmpty() { 
        return storage.isEmpty();

    } 
    
    // Using the heap sort algorithm, sorts the array array in ascending order.
    static <E extends DSAComparable<E>> void heapsort(E[] array) { 
        HeapStorage<E> storage = new Homework3<>(array);
        Heap<E> heap = new Heap<>(storage);

        for (int i = heap.size; i > 1; i--) {
            storage.swap(1, heap.size);
            heap.size--;
            heap.heapify(1);

        }
    } 
    
}

