import java.util.List;

public class Node {
    int val;
    Node left;
    Node right;
    Node next;
    Node random;
    public List<Node> children;
    public List<Node> neighbors;

    public Node(int val) {
        this.val = val;
    }

    public Node(int _val, List<Node> _children) {
        val = _val;
        children = _children;
    }
}
