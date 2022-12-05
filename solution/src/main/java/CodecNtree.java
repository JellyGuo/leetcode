import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

public class CodecNtree {
    public String serialize(Node root) {
        return serializeDfs(root);
    }

    private String serializeDfs(Node root) {
        if (root == null) return "null";
        StringBuilder sb = new StringBuilder();
        sb.append(root.val).append(",").append(root.children.size());
        for (Node child : root.children) {
            sb.append(serializeDfs(child));
        }
        return sb.toString();
    }

    public Node deserialize(String data) {
        Queue<String> queue = new ArrayDeque<>(Arrays.asList(data.split(",")));
        return deserializeDfs(queue);
    }

    private Node deserializeDfs(Queue<String> queue) {
        String s = queue.poll();
        if ("null".equals(s)) return null;
        int size = Integer.parseInt(queue.poll());
        Node root = new Node(Integer.parseInt(s));
        root.children = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            root.children.add(deserializeDfs(queue));
        }
        return root;
    }
}
