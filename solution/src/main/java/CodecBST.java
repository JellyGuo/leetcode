import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class CodecBST {
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
        String[] datas = data.split(",");
        return deserializeDfs(0, datas.length - 1, datas);
    }

    private TreeNode deserializeDfs(int l, int r, String[] datas) {
        if (l > r) return null;
        if (l == r) return new TreeNode(Integer.parseInt(datas[l]));
        int left = l + 1, right = r;
        int t = Integer.parseInt(datas[l]);
        TreeNode node  = new TreeNode(t);
        while (left < right) {
            int mid = left + right >> 1;
            int val = Integer.parseInt(datas[mid]);
            if (val >= t) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        if(Integer.parseInt(datas[right])<=t) right++;
        node.left = deserializeDfs(l+1,right-1,datas);
        node.right = deserializeDfs(right,r,datas);
        return node;
    }
}
