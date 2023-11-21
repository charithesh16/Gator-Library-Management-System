import java.util.ArrayList;
import java.util.List;

enum COLOR {RED, BLACK}
class Node {
    Book data;
    Node left;
    Node right;
    COLOR color;
    Node parent;

    Node(Book data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.color = COLOR.RED;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" BookID: ").append(data != null ? data.getId() : -1);
        sb.append(" Color: ").append(color);
        sb.append(" Parent: ").append(parent!=null ? parent.data.getId() : "null");
        sb.append(" Left: ").append(left!=null ? left.data.getId() : "null");
        sb.append(" Right: ").append(right!=null ? right.data.getId() : "null");
        return sb.toString();
    }
}

class ExternalNode extends Node {
     ExternalNode() {
         super(new Book());
         this.color = COLOR.BLACK;
    }
}

class RedBlackTree {
    public Node root;
    public int noOfColorFlips;
    public RedBlackTree(){
        this.root = null;
        this.noOfColorFlips = 0;
    }
    private void rotateRight(Node node) {
        Node parent = node.parent;
        Node leftChild = node.left;
        node.left = leftChild.right;
        if(leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.right = node;
        node.parent = leftChild;

        changeParentsChild(parent,node,leftChild);
    }
    private void changeParentsChild(Node parent,Node oldChild,Node newChild) {
        if(parent == null) {
            root = newChild;
        } else if(parent.left == oldChild) {
            parent.left = newChild;
        }else if(parent.right == oldChild) {
            parent.right = newChild;
        }else {
            throw new IllegalStateException("Node is not a child of its parent");
        }
        if(newChild != null) {
            newChild.parent = parent;
        }
    }
    private void rotateLeft(Node node) {
        Node parent = node.parent;
        Node rightChild = node.right;
        node.right = rightChild.left;
        if(rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.left = node;
        node.parent = rightChild;

        changeParentsChild(parent,node,rightChild);
    }

    public Node searchNode(int key) {
        Node node = root;
        while(node!=null) {
            if(key == node.data.getId()){
                return node;
            }else if(key < node.data.getId()) {
                node = node.left;
            }else {
                node = node.right;
            }
        }
        return null;
    }

    public Node insertNode(Book data) {
        Node node = root;
        Node parent = null;
        int key = data.getId();

        while(node!=null) {
            parent = node;
            if(key < node.data.getId()) {
                node = node.left;
            }else if(key > node.data.getId()) {
                node = node.right;
            }else {
                throw new IllegalStateException("BST already contains a node with the key "+key);
            }
        }

        Node newNode = new Node(data);
        if(parent==null) {
            newNode.color = COLOR.BLACK;
            root = newNode;
        }else if(key < parent.data.getId()){
            parent.left = newNode;
        }else {
            parent.right = newNode;
        }
        newNode.parent = parent;
        fixRedBlackPropertiesAfterInsert(newNode);
        return newNode;
    }

    private void changeColor(Node node, COLOR color) {
        if(node.parent==null) return;
        if((color==COLOR.BLACK && node.color == COLOR.RED) ||
                (color==COLOR.RED && node.color == COLOR.BLACK)){
            noOfColorFlips++;
        }
        node.color = color;
    }

    private void fixRedBlackPropertiesAfterInsert(Node node) {
        Node parent = node.parent;

        if(parent == null) {
            return;
        }
        if(parent.color == COLOR.BLACK) {
            return;
        }
        Node grandParent = parent.parent;
        if(grandParent == null) {
            changeColor(parent,COLOR.BLACK);
            return;
        }
        Node uncle = getUncle(parent);
        if(uncle !=null && uncle.color == COLOR.RED) {
            changeColor(parent,COLOR.BLACK);
            changeColor(grandParent,COLOR.RED);
            changeColor(uncle,COLOR.BLACK);
            fixRedBlackPropertiesAfterInsert(grandParent);
        } else if (parent == grandParent.left) {
            if(node == parent.right) {
                rotateLeft(parent);
                parent = node;
            }
            rotateRight(grandParent);
            changeColor(parent,COLOR.BLACK);
            changeColor(grandParent,COLOR.RED);
        }else {
            if(node == parent.left) {
                rotateRight(parent);
                parent = node;
            }
            rotateLeft(grandParent);
            changeColor(parent,COLOR.BLACK);
            changeColor(grandParent,COLOR.RED);
        }
    }

    private Node getUncle(Node parent) {
        Node grandparent = parent.parent;
        if (grandparent.left == parent) {
            return grandparent.right;
        } else if (grandparent.right == parent) {
            return grandparent.left;
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }
    public void deleteNode(int key) {
        Node node = root;

        while (node != null && node.data.getId() != key) {
            if (key < node.data.getId()) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (node == null) {
            return;
        }

        // At this point, "node" is the node to be deleted

        // In this variable, we'll store the node at which we're going to start to fix the R-B
        // properties after deleting a node.
        Node movedUpNode;
        COLOR deletedNodeColor;

        // Node has zero or one child
        if (node.left == null || node.right == null) {
            movedUpNode = deleteNodeWithZeroOrOneChild(node);
            deletedNodeColor = node.color;
        }

        // Node has two children
        else {
            // Find maximum node of left subtree ("inorder successor" of current node)
            Node inOrderSuccessor = findMaximum(node.left);

            // Copy inorder successor's data to current node (keep its color!)
            node.data = inOrderSuccessor.data;

            // Delete inorder successor just as we would delete a node with 0 or 1 child
            movedUpNode = deleteNodeWithZeroOrOneChild(inOrderSuccessor);
            deletedNodeColor = inOrderSuccessor.color;
        }

        if (deletedNodeColor == COLOR.BLACK) {
            fixRedBlackPropertiesAfterDelete(movedUpNode);

            // Remove the temporary NIL node
            if (movedUpNode.getClass() == ExternalNode.class) {
                changeParentsChild(movedUpNode.parent, movedUpNode, null);
            }
        }
    }

    private Node deleteNodeWithZeroOrOneChild(Node node) {
        // Node has ONLY a left child --> replace by its left child
        if (node.left != null) {
            changeParentsChild(node.parent, node, node.left);
            return node.left; // moved-up node
        }

        // Node has ONLY a right child --> replace by its right child
        else if (node.right != null) {
            changeParentsChild(node.parent, node, node.right);
            return node.right; // moved-up node
        }

        // Node has no children -->
        // * node is red --> just remove it
        // * node is black --> replace it by a temporary NIL node (needed to fix the R-B rules)
        else {
            Node newChild = node.color == COLOR.BLACK ? new ExternalNode() : null;
            changeParentsChild(node.parent, node, newChild);
            return newChild;
        }
    }

    private Node findMaximum(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private void fixRedBlackPropertiesAfterDelete(Node node) {
        // Case 1: Examined node is root, end of recursion
        if (node == root) {
            // Uncomment the following line if you want to enforce black roots (rule 2):
            // node.color = BLACK;
            return;
        }

        Node sibling = getSibling(node);

        // Case 2: Red sibling
        if (sibling.color == COLOR.RED) {
            handleRedSibling(node, sibling);
            sibling = getSibling(node); // Get new sibling for fall-through to cases 3-6
        }

        // Cases 3+4: Black sibling with two black children
        if (isBlack(sibling.left) && isBlack(sibling.right)) {
            changeColor(sibling,COLOR.RED);

            // Case 3: Black sibling with two black children + red parent
            if (node.parent.color == COLOR.RED) {
                changeColor(node.parent,COLOR.BLACK);
            }

            // Case 4: Black sibling with two black children + black parent
            else {
                fixRedBlackPropertiesAfterDelete(node.parent);
            }
        }

        // Case 5+6: Black sibling with at least one red child
        else {
            handleBlackSiblingWithAtLeastOneRedChild(node, sibling);
        }
    }

    private Node getSibling(Node node) {
        Node parent = node.parent;
        if (node == parent.left) {
            return parent.right;
        } else if (node == parent.right) {
            return parent.left;
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    private boolean isBlack(Node node) {
        return node == null || node.color == COLOR.BLACK;
    }

    private void handleRedSibling(Node node, Node sibling) {
        // Recolor...
        changeColor(sibling,COLOR.BLACK);
        changeColor(node.parent,COLOR.RED);

        // ... and rotate
        if (node == node.parent.left) {
            rotateLeft(node.parent);
        } else {
            rotateRight(node.parent);
        }
    }

    public List<Node> printBooks(int from,int to){
        List<Node> books = new ArrayList<>();
        traverseAndCollect(root,from,to,books);
        return books;
    }
    public void traverseAndCollect(Node node, int from, int to, List<Node> books){
        if(node == null)
            return;
        if(node.data.getId() >= from) {
            traverseAndCollect(node.left,from,to,books);
        }
        if(node.data.getId() >= from && node.data.getId() <=to){
            books.add(node);
        }
        if(node.data.getId() <= to) {
            traverseAndCollect(node.right,from,to,books);
        }
    }

    private void handleBlackSiblingWithAtLeastOneRedChild(Node node, Node sibling) {
        boolean nodeIsLeftChild = node == node.parent.left;

        // Case 5: Black sibling with at least one red child + "outer nephew" is black
        // --> Recolor sibling and its child, and rotate around sibling
        if (nodeIsLeftChild && isBlack(sibling.right)) {
            changeColor(sibling.left,COLOR.BLACK);
            changeColor(sibling,COLOR.RED);
            rotateRight(sibling);
            sibling = node.parent.right;
        } else if (!nodeIsLeftChild && isBlack(sibling.left)) {
            changeColor(sibling.right,COLOR.BLACK);
            changeColor(sibling,COLOR.RED);
            rotateLeft(sibling);
            sibling = node.parent.left;
        }

        // Fall-through to case 6...

        // Case 6: Black sibling with at least one red child + "outer nephew" is red
        // --> Recolor sibling + parent + sibling's child, and rotate around parent
        changeColor(sibling,node.parent.color);
        changeColor(node.parent,COLOR.BLACK);
        if (nodeIsLeftChild) {
            changeColor(sibling.right,COLOR.BLACK);
            rotateLeft(node.parent);
        } else {
            changeColor(sibling.left,COLOR.BLACK);
            rotateRight(node.parent);
        }
    }


    public Node[] findCeilAndFloor(int id) {
        Node node = root;
        Node[] ceilAndFloor = new Node[2];
        while(node!=null){
            if(node.data.getId()==id){
                ceilAndFloor[0] = node;
                ceilAndFloor[1] = node;
                break;
            }else if(id < node.data.getId()){
                ceilAndFloor[1] = node;
                node = node.left;
            }else{
                ceilAndFloor[0] = node;
                node  = node.right;
            }
        }
        return ceilAndFloor;
    }
}