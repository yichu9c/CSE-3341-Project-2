import java.util.ArrayList;
import java.util.List;

public class ParseTree {
    private Node root;
    private Node currentNode;

    // The Node class for the parse tree
    public class Node {
        public Node() {
            this.children = new ArrayList<Node>();
        }

        private Node parent;
        private List<Node> children;

        private int nt;  // Non-terminal for this node
        private int alt;  // Alternative number (if needed)

        // ONLY FOR <id>'S
        private int idVal;
        private String idName;

        public void setIdName(String idName) {
            idName = idName;
        }

        public void setIdVal(String idName) {
            idName = idName;
        }
    }

    // Constructor for the ParseTree class.
    public ParseTree() {
        Node n = new Node();
        n.parent = null;

        this.root = n;
        this.currentNode = this.root;
    }

    // Move the cursor to the root of the tree
    public void goAllTheWayBackUp() {
        this.currentNode = this.root;
    }

    // Get the non-terminal at the current node
    public int currentNTNo() {
        return this.currentNode.nt;
    }

    // Get the alternative number used at the current node
    public int currentAlternative() {
        return this.currentNode.alt;
    }

    // Move the cursor to the first child
    public void goDownLeftBranch() {
        if (!this.currentNode.children.isEmpty()) {
            this.currentNode = this.currentNode.children.get(0);
        }
    }

    // Create a left child of the current node
    public void createLeftBranch() {
        Node n = new Node();
        n.parent = this.currentNode;
        this.currentNode.children.add(0, n);
    }

    // Move the cursor to the second child
    public void goDownRightBranch() {
        if (this.currentNode.children.size() > 1) {
            this.currentNode = this.currentNode.children.get(1);
        }
    }

    // Create a right child of the current node
    public void createRightBranch() {
        Node n = new Node();
        n.parent = this.currentNode;
        this.currentNode.children.add(1, n);
    }

    // Move the cursor to the third child
    public void goDownMiddleBranch() {
        if (this.currentNode.children.size() > 2) {
            this.currentNode = this.currentNode.children.get(2);
        }
    }

    // Create a middle branch child of the current node
    public void createMiddleBranch() {
        Node n = new Node();
        n.parent = this.currentNode;
        this.currentNode.children.add(2, n);
    }

    // Move the cursor to the parent node
    public void goUp() {
        if (this.currentNode.parent != null) {
            this.currentNode = this.currentNode.parent;
        }
    }

    // Set the non-terminal at the current node
    public void setNT(int nt) {
        this.currentNode.nt = nt;
    }



    // Set the alternative number for the current node
    public void setAltNo(int alt) {
        this.currentNode.alt = alt;
    }

    // Get the value of the current node if it's an <id>
    public int getCurrentIntVal() {
        return this.currentNode.idVal;
    }

    // Set the value of the current node if it's an <id>
    public void setCurrentIntVal(int x) {
        this.currentNode.idVal = x;
    }

    // Get the name of the current node if it's an <id>
    public String getCurrentIdName() {
        return this.currentNode.idName;
    }

    // Set the name of the current node if it's an <id>
    public void setCurrentIdName(String x) {
        this.currentNode.idName = x;
    }

    // Helper method to print the tree (for debugging purposes)
    public void printTree(Node node, String indent) {
        if (node != null) {
            System.out.println(indent + "Node: " + node.nt + ", idName: " + node.idName);
            for (Node child : node.children) {
                printTree(child, indent + "  ");
            }
        }
    }

    // Print the entire tree starting from the root
    public void printParseTree() {
        printTree(this.root, "");
    }
}
