import java.util.ArrayDeque;
import java.util.Queue;

public class CBTInserter {
    TreeNode root;
    TreeNode cur;

    public CBTInserter(TreeNode root) {
        this.root = root;
        bfs(root);
    }

    private void bfs(TreeNode root) {
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode tmp = queue.poll();
            if (tmp.left == null || tmp.right == null) {
                cur = tmp;
                break;
            }
            queue.offer(tmp.left);
            queue.offer(tmp.right);
        }
    }

    public int insert(int v) {
        if (cur.left == null) {
            cur.left = new TreeNode(v);
            return cur.val;
        }
        cur.right = new TreeNode(v);
        int val = cur.val;
        bfs(root);
        return val;
    }

    public TreeNode getRoot() {
        return root;
    }
}
