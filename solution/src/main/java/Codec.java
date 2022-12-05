import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class Codec {
    public String serialize(TreeNode root) {
        return serializeDfs(root);
    }

    private String serializeDfs(TreeNode root) {
        if (root == null) return "None";
        String left = serializeDfs(root.left);
        String right = serializeDfs(root.right);
        return root.val + "," + left + "," + right;
    }

    public TreeNode deserialize(String data) {
        Queue<String> queue = new ArrayDeque<>(Arrays.asList(data.split(",")));
        return deserializeDfs(queue);
    }

    private TreeNode deserializeDfs(Queue<String> queue) {
        String s = queue.poll();
        if ("None".equals(s)) return null;
        TreeNode node = new TreeNode(Integer.parseInt(s));
        node.left = deserializeDfs(queue);
        node.right = deserializeDfs(queue);
        return node;
    }
}
