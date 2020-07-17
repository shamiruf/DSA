import java.util.*;


interface DSAComparable<E extends DSAComparable<E>> {

    boolean less (E other);

    boolean greater (E other);

    boolean equal (E other);

}


class Node<E extends DSAComparable<E>> {

    E contents;

    private Node<E> parent;

    Node<E> left, right; 

    // Returns the successor (ie the node containing the nearest higher value) of this node.
    Node<E> succ() {

        if (right != null ) {

             Node<E> temp = right;

            while (temp.left != null ) temp = temp.left;

            return temp;

         } else {

             Node<E> temp = this;

             Node<E> parent = this.parent;

            while (parent != null && parent.right == temp) {

                 parent = parent.parent;

                 temp = temp.parent;

             }

            return parent;

        }

    } 

    Node(E elem, Node<E> parent) {

         this.contents = elem;

         this.parent = parent;

    } 

    void setParent(Node<E> p) { 

        parent = p; 

    }

}

// The Tree class represents a binary search tree in which n applies to each node
// n.left == null || n.left.contents.less(n.contents) a
// n.right == null || n.right.contents.greater(n.contents)
class Tree<E extends DSAComparable<E>> {

    Node<E> root = null;

    

    // Returns the minimum of this tree, or null if the tree is empty.
    E minimum() {
        return subtreeMin(root);
    }

    // Returns the minimum of the specified subtree, or null if the subtree is empty.
    E subtreeMin(Node<E> n) {
        if (n == null) {
            return null;
        }
        Node<E> min = n;
        
        while(min.left != null) {
            min = min.left;
        }

        return min.contents;
    }

    // Returns the maximum of this subtree, or null if the subtree is empty.
    E maximum() {
        return subtreeMax(root);
    }

    // Returns the maximum of the specified subtree, or null if the subtree is empty.
    E subtreeMax(Node<E> n) {
        if (n == null) {
            return null;
        }
        Node<E> max = n;
        
        while(max.right != null) {
            max = max.right;
        }

        return max.contents;
    }

    // Insert element into tree (duplicates are forbidden)
    void insert(E elem) {
        if (root == null) {
            root = new Node<E>(elem, null);
        } else {
            Node<E> insertElement = new Node<E>(elem, null);
            insertNode(root, insertElement);
        }
    }

    void insertNode(Node<E> rootNode, Node<E> newNode) {
        if (newNode.contents.less(rootNode.contents)) {
            if (rootNode.left == null) {
                rootNode.left = newNode;
                rootNode.left.setParent(rootNode);
            } else {
                insertNode(rootNode.left, newNode);
            }
        } else if (newNode.contents.greater(rootNode.contents)) {
            if (rootNode.right == null) {
                rootNode.right = newNode;
                rootNode.right.setParent(rootNode);
            } else {
                insertNode(rootNode.right, newNode);
            }
        }
    }


    // Go through the tree and return: // - node with elem value, if any, // - null if node with elem value exists now)
    Node<E> find(E elem) {
        Node<E> node = root;
        while (node != null && !elem.equal(node.contents)) {
            if (elem.greater(node.contents)) {
                node = node.right;
            } else if (elem.less(node.contents)) {
                node = node.left;
            }
        }
        return node;
    }

    // Returns true if this tree contains an elem element.
    boolean contains(E elem) {
        return find(elem) != null;
    }

    // Remove the occurrence of the element elem from this tree.
    void remove(E elem) {
        root = removeRec(root, elem, null);
    }

    Node<E> removeRec(Node<E> node, E elem, Node<E> nodeParent) {
        if (node == null)  {
            return node; 
        }
        if (elem.less(node.contents)) {
            node.left = removeRec(node.left, elem, node); 
        } else if (elem.greater(node.contents)) {
            node.right = removeRec(node.right, elem, node); 
        } else { 
            if (node.left == null) {
                if (node.right != null) {
                    node.right.setParent(nodeParent);
                }
                return node.right; 
            } else if (node.right == null) {
                node.left.setParent(nodeParent);
                return node.left; 
            }
            node.contents = subtreeMin(node.right); 
            node.right = removeRec(node.right, node.contents, node);
        } 
        return node; 
    }

    // Returns an iterator over the entire tree (from the smallest to the largest). 
    // The remove () method may not be implemented
    Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> leftMin = find(subtreeMin(root));
            Node<E> current = null;
            
            @Override
            public boolean hasNext() {
                if (leftMin != null && (current == null || current.succ() != null)) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public E next() {
                if (leftMin != null && current == null) {
                    current = leftMin;
                } else {
                    if(!hasNext()) return null;
                    current = current.succ();
                }
                return current.contents;
            }

            @Override
            public void remove() {
            }
        };
    }

    // Returns the root node of this tree.
    Node<E> getRootNode() {
        return root;
    }

}
