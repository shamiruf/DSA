import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Tree class instances represent a B-tree.
class Tree {

    // The root of this tree.
    Node root;

    // minimal degree
    int t;
    int nodeCapacity;

    // Creates an empty B-tree with the specified node capacity. 
    // Node capacity is the maximum number of keys that can appear in one node.
    Tree(int nodeCapacity) {
        this.root = null;
        this.nodeCapacity = nodeCapacity;
        this.t = (nodeCapacity + 1) / 2;
    }

    // Indicates whether this tree contains the specified key.
    boolean contains(Integer key) {
        if (this.root == null) {
            return false;
        }
        Node node = this.root.find(key);
        
        return node != null;
        
    }
    
    // Add the specified key to the tree. If this tree already contains the specified key, it did nothing.
    void add(Integer key) {
        if (contains(key)) {
            return;
        }
        
        if (root == null) {
            root = new Node(t, true);
            root.keys.add(key);
            root.numberOfKeys = 1;
        } else {
            if (root.numberOfKeys == nodeCapacity) {
                Node node = new Node(t, false);
                node.childPointers.add(0, root);
                node.splitChild(0, root);
                int i = 0;
                if (node.keys.get(0).intValue() < key.intValue()) {
                    i++;
                }
                node.childPointers.get(i).addNonFull(key);
                root = node;
            } else {
                root.addNonFull(key);
            }
        }
        
    } 
    
    
    // Removes the specified key from the tree. If this tree does not contain the specified key, it did nothing.
    void remove(Integer key) {
        if (root == null || !contains(key)) {
            return;
        }
        root.remove(key);
        
        if(root.numberOfKeys == 0) {
            if(root.leaf) {
                root = null;
            } else {
                root = root.childPointers.get(0);
            }
            
        }
    } 
}


class Node {

    int t; // Minimum degree (defines the range for number of keys) 
    List<Integer> keys; // An array of keys 
    int numberOfKeys; // Current number of keys 
    List<Node> childPointers; // An array of child pointers 
    boolean leaf; // Is true when node is leaf. Otherwise false 

    // Constructor
    Node(int t, boolean leaf) {
        this.t = t; 
        this.leaf = leaf; 
        this.keys = new ArrayList<>(2*t);
        this.numberOfKeys = 0;         
        this.childPointers = new ArrayList<Node>(2*t+1);
    } 
  
    // Returns the keys stored in this node sorted in ascending order.
    List<Integer> getKeys() {
        List<Integer> list = new ArrayList<Integer>();
        // Add all keys in list
        for (int i = 0; i < numberOfKeys; i++) {
            list.add(keys.get(i));
        }
        // Sort list
        Collections.sort(list);
        
        return list;
    }

    // Returns pointers to the subtrees stored in this node, sorted in the order 
    // corresponding to the order of the key. Attention: it must always be the case 
    // that the number_of_points_on_subtrees = number_of_key + 1, so you must return a list of zeros for leaf nodes.
    List<Node> getSubtrees() {
        List<Node> list = new ArrayList<Node>();
        list.addAll(childPointers);
        
        for (int i = childPointers.size(); i < numberOfKeys+1; i++) {
            list.add(null);
        }
        
        return list;
    }
    
    Node find(Integer key) {
        for (int j = 0; j < numberOfKeys; j++) {
            if (keys.get(j).equals(key)) {
                return this;
            }
        }
        
        if (!leaf) {
            for (int i = 0; i < numberOfKeys; i++) {
                if ((key.intValue() < keys.get(i)) && (childPointers.get(i) != null)) {
                    Node node = childPointers.get(i).find(key.intValue());
                    if (node != null) {
                        return node;
                    }
                }   
            }
            return childPointers.get(numberOfKeys).find(key.intValue());
        }
        return null;
    }
    
    // i - the position of nodeToSplit in parent
    void splitChild(int i, Node nodeToSplit) {
        Node newNode = new Node(nodeToSplit.t, nodeToSplit.leaf);
        newNode.leaf = nodeToSplit.leaf;
        newNode.numberOfKeys = t - 1;
        
        for (int j = 0; j < t-1; j++) {
            newNode.keys.add(j, nodeToSplit.keys.get(j+t));
        }
        
        if (!nodeToSplit.leaf) {
            for (int j = 0; j < t; j++) {
                if (j+t < nodeToSplit.childPointers.size()) {
                    newNode.childPointers.add(j, nodeToSplit.childPointers.get(j+t));
                }
            }
        }

        nodeToSplit.numberOfKeys = t-1;

        childPointers.add(1+i, newNode);
        numberOfKeys = numberOfKeys + 1;
        keys.add(i, nodeToSplit.keys.get(t-1));
    }
    
    
    void addNonFull(Integer key) {
        int index = numberOfKeys - 1;
        if (leaf == false) {
            while ( index >=0 && (key.intValue() < keys.get(index).intValue())) {
                index--;
            }
            
            if (childPointers.get(index+1).numberOfKeys == 2*t-1) {
                splitChild(index+1, childPointers.get(index+1));
                
                if (key.intValue() > keys.get(index+1).intValue()) {
                    index++;
                }
            }
            
            Node n = childPointers.get(index+1);
            n.addNonFull(key);
        } else {
            if (key.intValue() > keys.get(index).intValue()) {
                keys.add(index+1, key);
            } else {
                while( index >= 0 && key.intValue() < keys.get(index).intValue()) {
                    index--;
                }
                keys.add(index + 1, key);
            }
            numberOfKeys = numberOfKeys + 1;
        }
        
    } 
    
    int findKey(Integer key) {
        int i = 0;
        
        while (i < numberOfKeys && key.intValue() > keys.get(i).intValue()) {
            
            i++;
        }
        
        return i;
    }
    
    void remove(Integer key) {
        
        int i = findKey(key);
        
        boolean check;
        
        
        if (i < numberOfKeys && keys.get(i).equals(key)) {
            
            if (!leaf) {
                
                removeFromNonLeaf(i);
            } else {
                
                removeFromLeaf(i);
            }
        } else {
            
            if (leaf) {
                
                return;
            }
            
            if (i == numberOfKeys) {
                check = true;
            } else {
                check = false;
            }
            
            if(t > childPointers.get(i).numberOfKeys) {
                fillChild(i);
            }
            
            if (i > numberOfKeys && check) {
                childPointers.get(i-1).remove(key);
            } else {
                childPointers.get(i).remove(key);
            }
            
            
        }
    }
    
    void removeFromLeaf(int i) {
        keys.remove(i);
        numberOfKeys--;
    }
    
    void removeFromNonLeaf(int i) {
        Integer key = keys.get(i);
        Integer parent;
        Integer successor;
        
        if (t <= childPointers.get(i).numberOfKeys) {
            Node current = childPointers.get(i);
            while(!current.leaf) {
                current = current.childPointers.get(current.numberOfKeys);
            }
            parent = current.keys.get(current.numberOfKeys - 1);
            keys.set(i, parent);
            childPointers.get(i).remove(parent);
        } else if (t <= childPointers.get(i+1).numberOfKeys){
            Node current = childPointers.get(i+1);

            while(!current.leaf) {
                current = current.childPointers.get(0);
            }
            successor = current.keys.get(0);
            keys.set(i, successor);
            childPointers.get(i+1).remove(successor);
        } else {
            merge(i);
            childPointers.get(i).remove(key);
        }
    }

    void saveSetInteger(List<Integer> list, int i, Integer value) {
        if (i < 0 || i >= list.size()) {
            list.add(i, value);
        } else {
            list.set(i, value);
        }
    }

    void saveSetNode(List<Node> list, int i, Node value) {
        if (i < 0 || i >= list.size()) {
            list.add(i, value);
        } else {
            list.set(i, value);
        }
    }
    
    void fillChild(int i) {
        if (i != 0 && t <= childPointers.get(i-1).numberOfKeys) {
            Node child = childPointers.get(i);
            Node sibling = childPointers.get(i-1);
            for (int j=child.numberOfKeys-1; j>=0; j--) {
                saveSetInteger(child.keys, j+1, child.keys.get(j));
            }
            if(!child.leaf) {
               for (int j=child.numberOfKeys; j>=0; j--) {
                    saveSetNode(child.childPointers, j+1, child.childPointers.get(j));
                }
            }
            child.keys.set(0, keys.get(i-1));
            if (!child.leaf) {
                saveSetNode(child.childPointers, 0, sibling.childPointers.get(sibling.numberOfKeys));
            }
            
            keys.set(i-1, sibling.keys.get(sibling.numberOfKeys-1));
            child.numberOfKeys = child.numberOfKeys + 1;
            sibling.numberOfKeys = sibling.numberOfKeys - 1;
        } else if (i != numberOfKeys && t <= childPointers.get(i+1).numberOfKeys ) {
            
            Node child = childPointers.get(i);
            Node sibling = childPointers.get(i+1);
            saveSetInteger(child.keys, child.numberOfKeys, keys.get(i));
            if(!child.leaf) {
                saveSetNode(child.childPointers, child.numberOfKeys +1, sibling.childPointers.get(0));
                
            }
            keys.set(i, sibling.keys.get(0));

            for (int j=1; j < sibling.numberOfKeys; j++) {
               saveSetInteger(sibling.keys, j-1, sibling.keys.get(j));
            }

            if (!sibling.leaf) {
                for (int j=1; j <= sibling.numberOfKeys; j++) {
                    saveSetNode(sibling.childPointers, j-1, sibling.childPointers.get(j));
                }
            }

            child.numberOfKeys = child.numberOfKeys + 1;
            sibling.numberOfKeys = sibling.numberOfKeys - 1;
        } else {
            if(i != numberOfKeys) {
                merge(i);
            } else {
                merge(i-1);
            }
        }
    }
    
    void merge(int i) {
        Node child = childPointers.get(i);
        Node sibling = childPointers.get(i+1);
        saveSetInteger(child.keys, t-1, keys.get(i));
        
        for (int j=0; j< sibling.numberOfKeys; j++) {
            saveSetInteger(child.keys, j+t, sibling.keys.get(j));
            
        }
        if(!child.leaf) {
            for(int j=0; j<=sibling.numberOfKeys; j++) {
                saveSetNode(child.childPointers, j+t, sibling.childPointers.get(j));
            }
        }

        for (int j = i + 1; j < numberOfKeys; j++) {
            saveSetInteger(keys, j-1, keys.get(j));
        }

        for (int j=i+2; j<=numberOfKeys; j++) {
            childPointers.set(j-1, childPointers.get(j));
        }
        
        child.numberOfKeys += sibling.numberOfKeys+1;
        numberOfKeys--;
    }
}