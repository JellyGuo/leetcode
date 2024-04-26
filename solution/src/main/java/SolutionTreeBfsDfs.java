import javafx.util.Pair;

import java.util.*;

public class SolutionTreeBfsDfs {
    //region---------------------------------------------------------BFS/DFS-----------------------------------------------------------
    //二叉树层序遍历
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            List<Integer> level = new LinkedList<>();
            int width = queue.size();

            for (int i = 0; i < width; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            result.add(level);
        }
        return result;
    }

    //N叉树层序遍历
    public List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Deque<Node> deque = new ArrayDeque<>();
        deque.offer(root);
        while (!deque.isEmpty()) {
            int width = deque.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < width; i++) {
                Node tmp = deque.poll();
                level.add(tmp.val);
                for (Node node : tmp.children) {
                    deque.offer(node);
                }
            }
            result.add(level);
        }
        return result;
    }

    // 二叉树倒序遍历
    public List<List<Integer>> levelOrderBottomBFS(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Stack<List<Integer>> stack = new Stack<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<>();
            int width = queue.size();
            for (int i = 0; i < width; i++) {
                TreeNode temp = queue.poll();
                level.add(temp.val);
                if (temp.left != null) {
                    queue.offer(temp.left);
                }
                if (temp.right != null) {
                    queue.offer(temp.right);
                }
            }
            stack.push(level);
        }
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    //二叉树锯齿遍历
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        boolean left2right = true;
        while (!queue.isEmpty()) {
            Deque<Integer> levelList = new LinkedList<>();
            int size = queue.size();
            for (int i = 0; i < size; ++i) {
                TreeNode cuNode = queue.poll();
                if (left2right) levelList.offerLast(cuNode.val);
                else levelList.offerFirst(cuNode.val);
                if (cuNode.left != null) queue.offer(cuNode.left);
                if (cuNode.right != null) queue.offer(cuNode.right);
            }
            result.add(new LinkedList<>(levelList));
            left2right = !left2right;
        }
        return result;
    }

    // 314 二叉树的垂直遍历 相同行列左右排列
    public List<List<Integer>> verticalOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new ArrayDeque<>();
        Queue<Integer> cols = new ArrayDeque<>();
        queue.offer(root);
        cols.offer(0);
        Map<Integer, List<Integer>> map = new HashMap<>();
        int minCol = 0;
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            int col = cols.poll();
            List<Integer> list = map.getOrDefault(col, new ArrayList<>());
            list.add(cur.val);
            map.put(col, list);
            if (cur.left != null) {
                queue.offer(cur.left);
                cols.offer(col - 1);
            }
            if (cur.right != null) {
                queue.offer(cur.right);
                cols.offer(col + 1);
            }
            minCol = Math.min(minCol, col);
        }
        for (int i = minCol; i < minCol + map.size(); i++) {
            result.add(map.get(i));
        }
        return result;
    }

    // 987 二叉树的垂序遍历  相同行列 按值大小
    public List<List<Integer>> verticalTraversal(TreeNode root) {
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>((o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];
            if (o1[1] != o2[1]) return o1[1] - o2[1];
            return o1[2] - o2[2];
        });
        dfs(root, 0, 0, priorityQueue);
        List<List<Integer>> result = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            int[] top = priorityQueue.peek();
            List<Integer> level = new ArrayList<>();
            while (!priorityQueue.isEmpty() && priorityQueue.peek()[0] == top[0]) level.add(priorityQueue.poll()[2]);
            result.add(level);
        }
        return result;
    }

    private void dfs(TreeNode node, int col, int row, PriorityQueue<int[]> queue) {
        queue.offer(new int[]{col, row, node.val});
        if (node.left != null) {
            dfs(node.left, col - 1, row + 1, queue);
        }
        if (node.right != null) {
            dfs(node.right, col + 1, row + 1, queue);
        }
    }

    // 701 BST中的插入操作
    public TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) return new TreeNode(val);
        if (root.val > val) root.left = insertIntoBST(root.left, val);
        if (root.val < val) root.right = insertIntoBST(root.right, val);
        return root;
    }

    // 814 二叉树剪枝
    public TreeNode pruneTree(TreeNode root) {
        if (root == null) return null;
        root.left = pruneTree(root.left);
        root.right = pruneTree(root.right);
        if (root.val == 0 && root.left == null && root.right == null) return null;
        return root;
    }

    // 找到树最左下节点值
    int maxDepth = 0;
    Integer value = null;

    public int findBottomLeftValueDFS(TreeNode root) {
        value = root.val;
        visit(root, 1);
        return value;
    }

    void visit(TreeNode n, int depth) {
        if (n == null) return;

        // 左边先更新maxDepth
        if (depth > this.maxDepth) {
            this.maxDepth = depth;
            this.value = n.val;
        }
        visit(n.left, depth + 1);
        visit(n.right, depth + 1);
    }

    public int findBottomLeftValueBFS(TreeNode root) {
        int res = 0;
        Queue<TreeNode> q = new LinkedList();
        q.add(root);

        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                TreeNode cur = q.poll();
                if (i == 0) res = cur.val;
                if (cur.left != null) q.add(cur.left);
                if (cur.right != null) q.add(cur.right);
            }
        }

        return res;
    }

    public List<Double> averageOfLevels(TreeNode root) {
        List<Double> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.empty()) {
            double sum = 0;
            double count = stack.size();
            for (int i = 0; i < count; i++) {
                TreeNode temp = stack.pop();
                sum += temp.val;
                if (temp.right != null) stack.push(temp.right);
                if (temp.left != null) stack.push(temp.left);
            }
            result.add(sum / count);
        }
        return result;
    }

    public List<Double> averageOfLevels2(TreeNode root) {
        List<Double> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            double sum = 0;
            double count = queue.size();
            for (int i = 0; i < count; i++) {
                TreeNode temp = queue.poll();
                sum += temp.val;
                if (temp.left != null) queue.offer(temp.left);
                if (temp.right != null) queue.offer(temp.right);
            }
            result.add(sum / count);
        }
        return result;
    }

    //2583. 二叉树中的第 K 大层和
    // 竞赛int相加溢出，开long
    public long kthLargestLevelSum(TreeNode root, int k) {
        if (root == null) return -1;
        PriorityQueue<Long> pq = new PriorityQueue<>(Comparator.reverseOrder());
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            long sum = 0;
            for (int i = 0; i < size; i++) {
                TreeNode tmp = queue.poll();
                sum += tmp.val;
                if (tmp.left != null) queue.offer(tmp.left);
                if (tmp.right != null) queue.offer(tmp.right);
            }
            pq.offer(sum);
        }
        if (pq.size() < k) return -1;
        long ans = -1;
        for (int i = 0; i < k; i++) {
            ans = pq.poll().longValue();
        }
        return ans;
    }

    // 面试04.03 特定深度节点链表
    public ListNode[] listOfDepth(TreeNode tree) {
        List<ListNode> list = new ArrayList<>();
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(tree);
        while (!queue.isEmpty()) {
            int size = queue.size();
            ListNode prev = null;
            for (int i = 0; i < size; i++) {
                TreeNode t = queue.poll();
                ListNode node = new ListNode(t.val);
                if (prev != null) {
                    prev.next = node;
                } else {
                    list.add(node);
                }
                prev = node;
                if (t.left != null) queue.offer(t.left);
                if (t.right != null) queue.offer(t.right);
            }
        }
        return list.toArray(new ListNode[0]);
    }

    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode node = new TreeNode(root.val);
        node.left = invertTree(root.right);
        node.right = invertTree(root.left);
        return node;
    }

    // offer 27 二叉树镜像
    public TreeNode mirrorTree(TreeNode root) {
        if (root == null) return null;
        TreeNode mirror = new TreeNode(root.val);
        mirror.left = mirrorTree(root.right);
        mirror.right = mirrorTree(root.left);
        return mirror;
    }

    // offer 28 对称二叉树
    //给你一个二叉树的根节点 root ， 检查它是否轴对称。
    public boolean isSymmetric(TreeNode root) {
        return check(root, root);
    }

    private boolean check(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        return p.val == q.val && check(p.left, q.right) && check(p.right, q.left);
    }

    public boolean isSymmetric2(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode p = queue.poll();
            TreeNode q = queue.poll();
            if (p == null && q == null) {
                continue;
            }
            if ((p == null || q == null) || (p.val != q.val)) {
                return false;
            }
            queue.offer(p.right);
            queue.offer(q.left);

            queue.offer(p.left);
            queue.offer(q.right);
        }
        return true;
    }

    //相同的树
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        return p.val == q.val && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    //给定一个二叉树，判断它是否是高度平衡的二叉树。
// 本题中，一棵高度平衡二叉树定义为：
// 一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1 。
    public boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }
        return Math.abs(height(root.left) - height(root.right)) <= 1 && isBalanced(root.right) && isBalanced(root.left);
    }

    private int height(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(height(root.right), height(root.left)) + 1;
    }

    //1379. 找出克隆二叉树中的相同节点
    public final TreeNode getTargetCopy(final TreeNode original, final TreeNode cloned, final TreeNode target) {
        if (original == null) {
            return null;
        }
        if (original == target) {
            return cloned;
        }
        TreeNode left = getTargetCopy(original.left, cloned.left, target);
        if (left != null) {
            return left;
        }
        return getTargetCopy(original.right, cloned.right, target);
    }

    //2415. 反转二叉树的奇数层
    public TreeNode reverseOddLevels(TreeNode root) {
        dfs2415(root.left, root.right, true);
        return root;
    }

    private void dfs2415(TreeNode root1, TreeNode root2, boolean isOdd) {
        if (root1 == null) {
            return;
        }
        if (isOdd) {
            int temp = root1.val;
            root1.val = root2.val;
            root2.val = temp;
        }
        dfs2415(root1.left, root2.right, !isOdd);
        dfs2415(root1.right, root2.left, !isOdd);
    }

    public TreeNode reverseOddLevelsBFS(TreeNode root) {
        if (root == null) return null;
        Deque<TreeNode> deque = new ArrayDeque<>();
        deque.offer(root);
        boolean flag = false;
        while (!deque.isEmpty()) {
            int size = deque.size();
            List<TreeNode> level = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                TreeNode cur = deque.pollFirst();
                level.add(cur);
                if(cur.left!=null) deque.offerLast(cur.left);
                if(cur.right!=null) deque.offerLast(cur.right);
            }
            if (flag) {
                swap(level);
            }
            flag = !flag;
        }
        return root;
    }

    private void swap(List<TreeNode> list) {
        int l = 0, r = list.size() - 1;
        while (l < r) {
            int lv = list.get(l).val;
            list.get(l).val = list.get(r).val;
            list.get(r).val = lv;
            l++;
            r--;
        }
    }

    //1448. 统计二叉树中好节点的数目
    int ans1448 = 0;

    public int goodNodes(TreeNode root) {
        dfs1448(root, root.val);
        return ans1448;
    }

    private void dfs1448(TreeNode node, int last) {
        if (node == null) return;
        if (last <= node.val) {
            ans1448++;
        }
        dfs1448(node.left, Math.max(node.val, last));
        dfs1448(node.right, Math.max(node.val, last));
    }

    //1038. 从二叉搜索树到更大和树
    int sum = 0;

    public TreeNode bstToGst(TreeNode root) {
        return dfs1038(root);
    }

    private TreeNode dfs1038(TreeNode treeNode) {
        if (treeNode == null) return null;
        TreeNode right = dfs1038(treeNode.right);
        sum += treeNode.val;
        TreeNode node = new TreeNode(sum);
        TreeNode left = dfs1038(treeNode.left);
        node.right = right;
        node.left = left;
        return node;

    }

    //1080. 根到叶路径上的不足节点
    public TreeNode sufficientSubset(TreeNode root, int limit) {
        limit -= root.val;
        if (root.left == root.right) // root 是叶子
            // 如果 limit > 0 说明从根到叶子的路径和小于 limit，删除叶子，否则不删除
            return limit > 0 ? null : root;
        if (root.left != null) root.left = sufficientSubset(root.left, limit);
        if (root.right != null) root.right = sufficientSubset(root.right, limit);
        // 如果儿子都被删除，就删 root，否则不删 root
        return root.left == null && root.right == null ? null : root;
    }

    //1110. 删点成林
    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
        Set<Integer> set = new HashSet<>();
        for (int d : to_delete) {
            set.add(d);
        }
        List<TreeNode> ans = new ArrayList<>();
        if (dfs(root, set, ans) != null) ans.add(root);
        return ans;
    }

    private TreeNode dfs(TreeNode node, Set<Integer> set, List<TreeNode> ans) {
        if (node == null) return null;
        node.left = dfs(node.left, set, ans);
        node.right = dfs(node.right, set, ans);
        if (!set.contains(node.val)) return node;
        if (node.left != null) ans.add(node.left);
        if (node.right != null) ans.add(node.right);
        return null;
    }

    //1457. 二叉树中的伪回文路径
    int ans1457 = 0;

    public int pseudoPalindromicPaths(TreeNode root) {
        int[] cnt = new int[10];
        dfs1457(root, cnt);
        return ans1457;
    }

    private void dfs1457(TreeNode root, int[] cnt) {
        if (root == null) {
            return;
        }
        cnt[root.val]++;
        if (root.left != null) dfs1457(root.left, cnt);
        if (root.right != null) dfs1457(root.right, cnt);
        if (root.left == null && root.right == null) {
            int num = 0;
            for (int i = 1; i <= 9; i++) {
                if (cnt[i] % 2 != 0) num++;
            }
            if (num <= 1) ans1457++;
        }
        cnt[root.val]--;
    }

    //2331. 计算布尔二叉树的值
    public boolean evaluateTree(TreeNode root) {
        return dfs2331(root);
    }

    private boolean dfs2331(TreeNode root) {
        if (root.left == null && root.right == null) {
            return root.val == 1;
        }
        boolean left = dfs2331(root.left);
        boolean right = dfs2331(root.right);
        if (root.val == 2) {
            return left || right;
        }
        return left && right;
    }


    // 98 验证BST
    long prev = Long.MIN_VALUE;

    public boolean isValidBST(TreeNode root) {
        if (root == null) return true;
        boolean l = isValidBST(root.left);
        if (root.val <= prev) return false;
        prev = root.val;
        boolean r = isValidBST(root.right);
        return l && r;
    }

    // 99 恢复二叉搜索树
    TreeNode x = null;
    TreeNode y = null;
    TreeNode pre = null;

    public void recoverTree(TreeNode root) {
        inorderDfs(root);
        if (x != null && y != null) {
            int tmp = x.val;
            x.val = y.val;
            y.val = tmp;
        }
    }

    private void inorderDfs(TreeNode node) {
        if (node == null) return;
        inorderDfs(node.left);
        // y 取后一个小的 x取第一个大的，y更新多次，x只更新一次
        // 1 2 3 4 5 6 -> 1 3 2 4 5 6
        // -> 1 5 3 4 2 6
        if (pre != null && node.val <= pre.val) {
            y = node;
            if (x == null) {
                x = pre;
            }
        }
        pre = node;
        inorderDfs(node.right);
    }

    // 543 二叉树的直径
    int max = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        dfs1(root);
        return max;
    }

    private int dfs1(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int left = dfs1(node.left);
        int right = dfs1(node.right);
        max = Math.max(left + right, max);
        return Math.max(left, right) + 1;
    }

    //124 二叉树中的最大路径和
    int maxPathSum = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        maxGain(root);
        return maxPathSum;
    }

    private int maxGain(TreeNode root) {
        if (root == null) return 0;
        int leftSum = Math.max(0, maxGain(root.left));
        int rightSum = Math.max(0, maxGain(root.right));
        maxPathSum = Math.max(maxPathSum, root.val + leftSum + rightSum);
        return Math.max(leftSum, rightSum) + root.val;
    }
    //270 最接近的二叉搜索树值
    //给定一个不为空的BST和target，找出最接近target的节点值

    int minValue;

    public int closestValueDFS(TreeNode root, double target) {
        this.minValue = root.val;
        dfs(root, target);
        return minValue;
    }

    private void dfs(TreeNode root, double target) {
        minValue = Math.abs(root.val - target) < Math.abs(minValue - target) ? root.val : minValue;
        if (root.val < target && root.right != null) dfs(root.right, target);
        if (root.val > target && root.left != null) dfs(root.left, target);
    }

    public int closestValueIte(TreeNode root, double target) {
        int closest = root.val;
        int curVal;
        while (root != null) {
            curVal = root.val;
            closest = Math.abs(curVal - target) < Math.abs(closest - target) ? curVal : closest;
            root = root.val < target ? root.right : root.left;
        }
        return closest;
    }

    //687 最长同值路径
    int longestUnivaluePathValue = 0;
    int ans = 0;

    public int longestUnivaluePath(TreeNode root) {
        longestUnivaluePathDfs(root);
        return ans;
    }

    private int longestUnivaluePathDfs(TreeNode root) {
        if (root == null) return 0;
        int left = longestUnivaluePathDfs(root.left);
        int right = longestUnivaluePathDfs(root.right);
        if (root.left != null && root.left.val == root.val) {
            left += 1;
        } else {
            left = 0;
        }
        if (root.right != null && root.right.val == root.val) {
            right += 1;
        } else {
            right = 0;
        }
        longestUnivaluePathValue = Math.max(longestUnivaluePathValue, right + left);
        return Math.max(right, left);
    }

    // 250 统计同值子树
//给定一个二叉树，统计该二叉树数值相同的子树个数。
// 同值子树是指该子树的所有节点都拥有相同的数值。
// 输入: root = [5,1,5,5,5,null,5]
//              5
//             / \
//            1   5
//           / \   \
//          5   5   5
//输出: 4
    int cnt250 = 0;

    public int countUnivalSubtrees(TreeNode root) {
        same(root);
        return cnt250;
    }

    private boolean same(TreeNode root) {
        if (root == null) return true;
        boolean left = same(root.left);
        boolean right = same(root.right);
        if (root.left != null) {
            left &= (root.val == root.left.val);
        }
        if (root.right != null) {
            right &= (root.val == root.right.val);
        }
        if (left && right) {
            cnt250++;
        }
        return left && right;
    }

    // 508出现次数最多的子树元素和
    int maxSum = 0;

    public int[] findFrequentTreeSum(TreeNode root) {
        Map<Integer, Integer> map = new HashMap<>();
        dfs(root, map);
        List<Integer> res = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == maxSum) {
                res.add(entry.getKey());
            }
        }
        int[] ans = new int[res.size()];
        for (int i = 0; i < res.size(); i++) {
            ans[i] = res.get(i);
        }
        return ans;
    }

    private int dfs(TreeNode root, Map<Integer, Integer> map) {
        if (root == null) return 0;
        int left = dfs(root.left, map);
        int right = dfs(root.right, map);
        int val = root.val + left + right;
        int cnt = map.getOrDefault(val, 0) + 1;
        maxSum = Math.max(maxSum, cnt);
        map.put(val, cnt);
        return val;
    }

    //979. 在二叉树中分配硬币
    int move = 0;

    public int distributeCoins(TreeNode root) {
        dfs979(root);
        return move;
    }

    private int dfs979(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int moveRight = 0;
        int moveLeft = 0;
        if (root.left != null) {
            moveLeft = dfs979(root.left);
        }
        if (root.right != null) {
            moveRight = dfs979(root.right);
        }
        // moveLeft/right为正，从子往父移动，为负，从父往子移动
        move += Math.abs(moveLeft) + Math.abs(moveRight);
        // 移动之后本节点还剩的硬币数量
        return moveLeft + moveRight + root.val - 1;
    }

    //1373. 二叉搜索子树的最大键值和
    private int ans1373; // 二叉搜索树可以为空

    public int maxSumBST(TreeNode root) {
        dfs1373(root);
        return ans1373;
    }

    private int[] dfs1373(TreeNode node) {
        if (node == null)
            return new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, 0};

        int[] left = dfs1373(node.left); // 递归左子树
        int[] right = dfs1373(node.right); // 递归右子树
        int x = node.val;
        if (x <= left[1] || x >= right[0]) // 不是二叉搜索树
            return new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, 0};

        int s = left[2] + right[2] + x; // 这棵子树的所有节点值之和
        ans = Math.max(ans, s);

        return new int[]{Math.min(left[0], x), Math.max(right[1], x), s};
    }

    //1145. 二叉树着色游戏
    // 以x为根节点的左子树和右子树大小
    int xLeftSize;
    int xRightSize;
    int x1145;

    public boolean btreeGameWinningMove(TreeNode root, int n, int x) {
        this.x1145 = x;
        dfs1145(root);
        return Math.max(Math.max(xLeftSize, xRightSize), n - 1 - xLeftSize - xRightSize) * 2 > n;
    }

    private int dfs1145(TreeNode root) {
        if (root == null) return 0;
        int lf = dfs1145(root.left);
        int rt = dfs1145(root.right);
        if (root.val == x1145) {
            xLeftSize = lf;
            xRightSize = rt;
        }
        return lf + rt + 1;
    }

    //   272 给定一个不为空的二叉搜索树和一个目标值 target，请在该二叉搜索树中找到最接近目标值 target 的 k 个值。
    public int[] closestValue(TreeNode root, double target, int k) {
        PriorityQueue<double[]> queue = new PriorityQueue<>(Comparator.comparingDouble(o -> o[0]));
        dfs(root, target, queue);
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            double[] tmp = queue.poll();
            result[i] = Integer.parseInt(String.valueOf(tmp[1]));
        }
        return result;
    }

    private void dfs(TreeNode root, double target, PriorityQueue<double[]> queue) {
        if (root == null) return;
        queue.offer(new double[]{Math.abs(root.val - target), root.val});
        dfs(root.left, target, queue);
        dfs(root.right, target, queue);
    }

    // 2454 移除子树后的二叉树高度
    private Map<TreeNode, Integer> height = new HashMap<>(); // 每棵子树的高度
    private int[] res; // 每个节点的答案

    public int[] treeQueries(TreeNode root, int[] queries) {
        getHeight(root);
        height.put(null, 0); // 简化 dfs 的代码，这样不用写 getOrDefault
        res = new int[height.size()];
        dfs(root, -1, 0);
        for (int i = 0; i < queries.length; i++)
            queries[i] = res[queries[i]];
        return queries;
    }

    private int getHeight(TreeNode node) {
        if (node == null) return 0;
        int h = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        height.put(node, h);
        return h;
    }

    private void dfs(TreeNode node, int depth, int restH) {
        if (node == null) return;
        ++depth;
        res[node.val] = restH;
        dfs(node.left, depth, Math.max(restH, depth + height.get(node.right)));
        dfs(node.right, depth, Math.max(restH, depth + height.get(node.left)));
    }

    //559 N叉树最大深度
    public int maxDepth(Node root) {
        if (root == null) return 0;
        int max = 0;
        for (Node child : root.children) {
            max = Math.max(max, maxDepth(child));
        }
        // +1 的逻辑不能在循环内，否则不会更新叶子节点depth(叶子节点不进入循环)
        return 1 + max;
    }

    // 104 二叉树最大深度
    public int maxDepthBinaryTree(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepthBinaryTree(root.left), maxDepthBinaryTree(root.right));
    }

    // 111 二叉树最小深度
    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.right == null && root.left == null) {
            return 1;
        }
        int leftDepth = minDepth(root.left);
        int rightDepth = minDepth(root.right);
        if (leftDepth == 0) return rightDepth + 1;
        if (rightDepth == 0) return leftDepth + 1;
        return Math.min(rightDepth, leftDepth) + 1;
    }

    public int minDepthBFS(TreeNode root) {
        if (root == null) return 0;
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int level = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            level++;
            TreeNode node;
            for (int i = 0; i < size; i++) {
                node = queue.poll();
                if (node.left == null && node.right == null) {
                    return level;
                }
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        return level;
    }

    // 662 二叉树最大宽度
    public int widthOfBinaryTreeBFS(TreeNode root) {
        Queue<Pair<TreeNode, Integer>> queue = new ArrayDeque<>();
        queue.offer(new Pair<>(root, 1));
        int ans = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            int min = -1, max = -1;
            for (int i = 0; i < size; i++) {
                Pair<TreeNode, Integer> pair = queue.poll();
                if (min == -1) {
                    min = pair.getValue();
                }
                max = pair.getValue();
                if (pair.getKey().left != null) {
                    queue.offer(new Pair<>(pair.getKey().left, 2 * pair.getValue()));
                }
                if (pair.getKey().right != null) {
                    queue.offer(new Pair<>(pair.getKey().right, 2 * pair.getValue() + 1));
                }
            }
            ans = Math.max(ans, max - min + 1);
        }
        return ans;
    }

    int maxWidth;

    public int widthOfBinaryTreeDFS(TreeNode root) {
        Map<Integer, Integer> map = new HashMap<>();
        widthOfBinaryTreeDfs(root, 0, 1, map);
        return maxWidth;
    }

    private void widthOfBinaryTreeDfs(TreeNode root, int depth, int num, Map<Integer, Integer> map) {
        if (root == null) return;
        if (!map.containsKey(depth)) {
            map.put(depth, num);
        }
        maxWidth = Math.max(maxWidth, num - map.get(depth) + 1);
        widthOfBinaryTreeDfs(root.left, depth + 1, 2 * num, map);
        widthOfBinaryTreeDfs(root.right, depth + 1, 2 * num + 1, map);
    }

    //给你一个二叉树的根节点 root ，树中每个节点都存放有一个 0 到 9 之间的数字。
// 每条从根节点到叶节点的路径都代表一个数字：
// 例如，从根节点到叶节点的路径 1 -> 2 -> 3 表示数字 123 。
// 计算从根节点到叶节点生成的 所有数字之和 。
// 叶节点 是指没有子节点的节点。
//输入：root = [1,2,3]
//输出：25
//解释：
//从根到叶子节点路径 1->2 代表数字 12
//从根到叶子节点路径 1->3 代表数字 13
//因此，数字总和 = 12 + 13 = 25
//输入：root = [4,9,0,5,1]
//输出：1026
//解释：
//从根到叶子节点路径 4->9->5 代表数字 495
//从根到叶子节点路径 4->9->1 代表数字 491
//从根到叶子节点路径 4->0 代表数字 40
//因此，数字总和 = 495 + 491 + 40 = 1026
    public int sumNumbers(TreeNode root) {
        if (root == null) {
            return 0;
        }
        List<String> list = new ArrayList<>();
        dfs(root, list, "");
        int sum = 0;
        for (String s : list) {
            sum += Integer.valueOf(s);
        }
        return sum;
    }

    public void dfs(TreeNode tree, List<String> list, String last) {
        if (tree == null) {
            return;
        }
        String cur = last + tree.val;
        if (tree.left == null && tree.right == null) {
            list.add(cur);
            return;
        }
        dfs(tree.left, list, cur);
        dfs(tree.right, list, cur);
    }

    public int sumNumbers2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return dfs(root, 0);
    }

    public int dfs(TreeNode tree, int prevSum) {
        if (tree == null) {
            return 0;
        }
        int sum = prevSum * 10 + tree.val;
        if (tree.left == null && tree.right == null) {
            return sum;
        } else {
            return dfs(tree.left, sum) + dfs(tree.right, sum);
        }
    }

    //133 克隆图
    public Node cloneGraph(Node node) {
        Map<Node, Node> visited = new HashMap<>();
        return cloneGraphDfs(node, visited);
    }

    private Node cloneGraphDfs(Node node, Map<Node, Node> map) {
        if (node == null) {
            return null;
        }
        if (map.containsKey(node)) {
            return map.get(node);
        }
        Node clone = new Node(node.val, new ArrayList<>());
        map.put(node, clone);
        for (Node n : node.neighbors) {
            clone.neighbors.add(cloneGraphDfs(n, map));
        }
        return clone;
    }

    public Node cloneGraphBFS(Node node) {
        if (node == null) {
            return null;
        }
        Map<Node, Node> visited = new HashMap<>();
        Stack<Node> stack = new Stack<>();
        Node clone = new Node(node.val, new ArrayList<>());
        stack.push(clone);
        visited.put(node, clone);
        while (!stack.isEmpty()) {
            Node top = stack.pop();
            for (Node n : top.neighbors) {
                if (!visited.containsKey(n)) {
                    Node tmp = new Node(n.val, new ArrayList<>());
                    stack.push(n);
                    visited.put(n, tmp);
                }
                visited.get(top).neighbors.add(visited.get(n));
            }
        }
        return clone;
    }


    // 138复制带随机指针的链表
    public Node copyRandomList(Node head) {
        Map<Node, Node> map = new HashMap<>();
        return dfs(head, map);
    }

    private Node dfs(Node node, Map<Node, Node> map) {
        if (node == null) {
            return null;
        }
        if (map.containsKey(node)) {
            return map.get(node);
        }
        Node Node = new Node(node.val);
        map.put(node, Node);
        Node.next = dfs(node.next, map);
        Node.random = dfs(node.random, map);
        return Node;
    }

    public Node copyRandomList2(Node head) {
        if (head == null) {
            return null;
        }
        for (Node node = head; node != null; node = node.next.next) {
            Node nodeNew = new Node(node.val);
            nodeNew.next = node.next;
            node.next = nodeNew;
        }
        for (Node node = head; node != null; node = node.next.next) {
            Node nodeNew = node.next;
            nodeNew.random = node.random != null ? node.random.next : null;
        }
        Node headNew = head.next;
        for (Node node = head; node != null; node = node.next) {
            Node nodeNew = node.next;
            node.next = node.next.next;
            nodeNew.next = (nodeNew.next != null) ? nodeNew.next.next : null;
        }
        return headNew;
    }

    public Node copyRandomList3(Node head) {
        if (head == null) return null;
        Node newHead = new Node(head.val);
        Node dummy = new Node(0);
        Node dummyNew = new Node(0);
        dummy.next = head;
        dummyNew.next = newHead;
        Map<Node, Node> map = new HashMap<>();
        map.put(head, newHead);
        while (head.next != null) {
            newHead.next = new Node(head.next.val);
            head = head.next;
            newHead = newHead.next;
            map.put(head, newHead);
        }
        head = dummy.next;
        newHead = dummyNew.next;
        while (head != null) {
            newHead.random = map.getOrDefault(head.random, null);
            head = head.next;
            newHead = newHead.next;
        }
        return dummyNew.next;
    }

    // 222 完全二叉树的节点个数
    int cnt = 0;

    public int countNodes(TreeNode root) {
        countNodesDfs(root);
        return cnt;
    }

    private void countNodesDfs(TreeNode root) {
        if (root == null) return;
        cnt++;
        countNodesDfs(root.left);
        countNodesDfs(root.right);
    }

    public int countNodes2(TreeNode root) {
        if (root == null) return 0;
        int left = countLevel(root.left);
        int right = countLevel(root.right);
        if (left != right) {
            return countNodes2(root.left) + (1 << right);
        } else {
            return countNodes2(root.right) + (1 << left);
        }
    }

    private int countLevel(TreeNode root) {
        int cnt = 0;
        while (root != null) {
            cnt++;
            root = root.left;
        }
        return cnt;
    }

    //617 合并二叉树
    public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
        if (root1 == null && root2 == null) {
            return null;
        }
        int sum = (root1 == null ? 0 : root1.val) + (root2 == null ? 0 : root2.val);
        TreeNode root = new TreeNode(sum);
        root.left = mergeTrees(root1 == null ? null : root1.left, root2 == null ? null : root2.left);
        root.right = mergeTrees(root1 == null ? null : root1.right, root2 == null ? null : root2.right);
        return root;
    }

    // 669 修剪BST 是tree所有值在[low,high]中
    public TreeNode trimBST(TreeNode root, int low, int high) {
        if (root == null) return null;
        if (root.val < low) return trimBST(root.right, low, high);
        if (root.val > high) return trimBST(root.left, low, high);
        root.left = trimBST(root.left, low, root.val);
        root.right = trimBST(root.right, root.val, high);
        return root;
    }

    //前序遍历
    //递归实现
    public void preOrderTraverse(TreeNode root) {
        if (root == null) return;
        System.out.println("" + root.val);
        preOrderTraverse(root.left);
        preOrderTraverse(root.right);
    }

    //非递归实现
    public void preOrderTraverse2(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode node = root;
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                System.out.println(node.val);
                stack.push(node);
                node = node.left;
            }
            node = stack.pop();
            node = node.right;
        }
    }

    //N叉树前序遍历
    public List<Integer> preorderNode(Node root) {
        List<Integer> result = new ArrayList<>();
        preorderDfs(root, result);
        return result;
    }

    private void preorderDfs(Node root, List<Integer> result) {
        if (root == null) return;
        result.add(root.val);
        for (Node child : root.children) {
            preorderDfs(child, result);
        }
    }

    //N叉树前序遍历 迭代
    public List<Integer> preorderNode2(Node root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            result.add(node.val);
            for (int i = node.children.size() - 1; i >= 0; i--) {
                stack.push(node.children.get(i));
            }
        }
        return result;
    }

    //中序遍历
    public void inOrderTraverse(TreeNode root) {
        if (root == null) return;
        inOrderTraverse1(root.left);
        System.out.println(root.val);
        inOrderTraverse1(root.right);
    }

    public void inOrderTraverse1(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode preNode = root;
        while (preNode != null || !stack.isEmpty()) {
            while (preNode != null) {
                stack.push(preNode);
                preNode = preNode.left;
            }
            TreeNode node = stack.pop();
            System.out.println(node.val);
            preNode = node.right;
        }
    }

    //后序遍历
    public void postOrderTraverse(TreeNode root) {
        if (root == null) return;
        postOrderTraverse(root.left);
        postOrderTraverse(root.right);
        System.out.println(root.val);
    }

    public void postOrderTraverse1(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode node = root;
        TreeNode pre = null;
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                //访问左子树
                stack.push(node);
                node = node.left;
            }
            //判断栈顶元素（根）
            node = stack.peek();
            //1.如果此时根的右子树为空
            //2.如果此时根的右子树已经访问过了pre记录的是上次访问打印的节点
            if (node.right == null || node.right == pre) {
                //打印根节点并出栈，将打印过的j节点从栈中删除
                System.out.println(node.val);
                stack.pop();
                //记录pre，表示以当前pre为根的子树已经访问过了
                pre = node;
                //node置null就不会再次访问以node为根节点的左右子树，这里的node既然已经打印，说明它的左右子树早已访问完毕
                node = null;
            } else {
                node = node.right;
            }
        }
    }

    // N叉树后序遍历
    public List<Integer> postorder(Node root) {
        List<Integer> result = new ArrayList<>();
        postorderDfs(root, result);
        return result;
    }

    private void postorderDfs(Node root, List<Integer> result) {
        if (root == null) return;
        for (Node child : root.children) {
            postorderDfs(child, result);
        }
        result.add(root.val);
    }

    public List<Integer> postorder2(Node root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<Node> stack = new Stack<>();
        Set<Node> visited = new HashSet<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node node = stack.peek();
            /* 如果当前节点为叶子节点或者当前节点的子节点已经遍历过 */
            if (node.children.size() == 0 || visited.contains(node)) {
                stack.pop();
                res.add(node.val);
                continue;
            }
            for (int i = node.children.size() - 1; i >= 0; --i) {
                stack.push(node.children.get(i));
            }
            visited.add(node);
        }
        return res;
    }

    // 105 从前序和中序 构造二叉树
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < preorder.length; i++) {
            indexMap.put(inorder[i], i);
        }
        return myBuildTree(indexMap, preorder, inorder, 0, preorder.length - 1, 0, inorder.length - 1);
    }

    public TreeNode myBuildTree(Map<Integer, Integer> index, int[] preorder, int[] inorder, int preorder_left, int preorder_right, int inorder_left, int inorder_right) {
        if (preorder_right < preorder_left) {
            return null;
        }
        int inorder_root = index.get(preorder[preorder_left]);
        int left_size = inorder_root - inorder_left;
        TreeNode root = new TreeNode(preorder[preorder_left]);
        root.left = myBuildTree(index, preorder, inorder, preorder_left + 1, preorder_left + left_size, inorder_left, inorder_root - 1);
        root.right = myBuildTree(index, preorder, inorder, preorder_left + left_size + 1, preorder_right, inorder_root + 1, inorder_right);
        return root;
    }

    public TreeNode buildTree2(int[] preorder, int[] inorder) {
        if (preorder == null || preorder.length == 0) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[0]);
        Deque<TreeNode> stack = new LinkedList<>();
        stack.push(root);
        int inorderIndex = 0;
        for (int i = 1; i < preorder.length; i++) {
            int preorderVal = preorder[i];
            TreeNode node = stack.peek();
            if (node.val != inorder[inorderIndex]) {
                node.left = new TreeNode(preorderVal);
                stack.push(node.left);
            } else {
                while (!stack.isEmpty() && stack.peek().val == inorder[inorderIndex]) {
                    node = stack.pop();
                    inorderIndex++;
                }
                node.right = new TreeNode(preorderVal);
                stack.push(node.right);
            }
        }
        return root;
    }

    //889. 根据前序和后序遍历构造二叉树
    public TreeNode constructFromPrePost(int[] preorder, int[] postorder) {
        int n = preorder.length;
        Map<Integer, Integer> postMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < n; i++) {
            postMap.put(postorder[i], i);
        }
        return dfs889(preorder, postorder, postMap, 0, n - 1, 0, n - 1);
    }

    public TreeNode dfs889(int[] preorder, int[] postorder, Map<Integer, Integer> postMap, int preLeft, int preRight, int postLeft, int postRight) {
        if (preLeft > preRight) {
            return null;
        }
        int leftCount = 0;
        if (preLeft < preRight) {
            leftCount = postMap.get(preorder[preLeft + 1]) - postLeft + 1;
        }
        TreeNode treeNode = new TreeNode(preorder[preLeft]);
        treeNode.left = dfs889(preorder, postorder, postMap, preLeft + 1, preLeft + leftCount, postLeft, postLeft + leftCount - 1);
        treeNode.right = dfs889(preorder, postorder, postMap, preLeft + leftCount + 1, preRight, postLeft + leftCount, postRight - 1);
        return treeNode;
    }
    // 106
    public TreeNode buildTreeIP(int[] inorder, int[] postorder) {
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < postorder.length; i++) {
            indexMap.put(inorder[i], i);
        }
        return myBuildTreeIP(indexMap, inorder, postorder, 0, inorder.length - 1, 0, postorder.length - 1);
    }

    public TreeNode myBuildTreeIP(Map<Integer, Integer> index, int[] inorder, int[] postorder, int inorder_left, int inorder_right, int postorder_left, int postorder_right) {
        if (postorder_right < postorder_left || inorder_right < inorder_left) {
            return null;
        }
        int inorder_root = index.get(postorder[postorder_right]);
        int left_size = inorder_root - inorder_left;
        TreeNode root = new TreeNode(postorder[postorder_right]);
        root.left = myBuildTreeIP(index, inorder, postorder, inorder_left, inorder_root - 1, postorder_left, postorder_left + left_size - 1);
        root.right = myBuildTreeIP(index, inorder, postorder, inorder_root + 1, inorder_right, postorder_left + left_size, postorder_right - 1);
        return root;
    }

    // 255 验证前序遍历二叉树
    public boolean verifyPreorder(int[] preorder) {
        return verifyPreorderDfs(preorder, 0, preorder.length - 1);
    }

    private boolean verifyPreorderDfs(int[] preorder, int l, int r) {
        if (l >= r) return true;
        int p = r;
        while (p > l && preorder[p] > preorder[l]) p--;
        int q = p;
        while (q > l && preorder[q] < preorder[l]) q--;
        return q == l && verifyPreorderDfs(preorder, l + 1, p) && verifyPreorderDfs(preorder, p + 1, r);
    }

    // offer 33 二叉搜索树的后序遍历序列
//https://leetcode-cn.com/problems/er-cha-sou-suo-shu-de-hou-xu-bian-li-xu-lie-lcof/solution/mian-shi-ti-33-er-cha-sou-suo-shu-de-hou-xu-bian-6/

    public boolean verifyPostorder(int[] postorder) {
        return dfs(postorder, 0, postorder.length - 1);
    }

    private boolean dfs(int[] postorder, int left, int right) {
        if (left >= right) return true;
        int p = left;
        while (postorder[p] < postorder[right]) p++;
        int m = p;
        while (postorder[p] > postorder[right]) p++;
        return p == right && dfs(postorder, left, m - 1) && dfs(postorder, m, right - 1);
    }

    //单调栈 倒序中 递减值向左找到最近的大于他的值，即为该递减值的父节点
    public boolean verifyPostorderStack(int[] postorder) {
        Stack<Integer> stack = new Stack<>();
        int root = Integer.MAX_VALUE;
        //注意for循环是倒叙遍历的
        for (int i = postorder.length - 1; i >= 0; i--) {
            if (postorder[i] > root) return false;
            //当如果前节点小于栈顶元素，说明栈顶元素和当前值构成了倒叙，
            //说明当前节点是前面某个节点的左子节点，我们要找到他的父节点
            while (!stack.isEmpty() && stack.peek() > postorder[i]) {
                root = stack.pop();
                //只要遇到了某一个左子节点，才会执行上面的代码，才会更
                //新parent的值，否则parent就是一个非常大的值，也就
                //是说如果一直没有遇到左子节点，那么右子节点可以非常大
            }
            stack.add(postorder[i]);
        }
        return true;
    }


    //331. 验证二叉树的前序序列化
    //输入: preorder = "9,3,4,#,#,1,#,#,2,#,6,#,#"
//输出: true
    // 自底向上 消除法：遇到 x,#,# 时它变为 #
    public static boolean isValidSerialization(String s) {
        if (s == null || s.length() == 0) return true;
        String[] str = s.split(",");
        int n = str.length;
        if ("#".equals(str[0]) && n > 1) return false;
        if ("#".equals(str[0])) return true;

        Deque<String> stack = new LinkedList<>();
        int i = 0;
        while (i < n) {
            String c = str[i];
            if (!"#".equals(c)) { // 遇到的不是'#'，直接入栈
                stack.push(c);
                i++;
            } else {
                if ("#".equals(stack.peek())) { // 遇到'#'，当前栈顶也是'#'，弹出2个字符，压入1个'#'
                    stack.pop();
                    if (stack.isEmpty()) return false;
                    stack.pop();
                    // i 位置不动，继续处理当前字符'#'，循环判断是否依旧满足2）
                } else { // 栈顶不是'#'，直接入栈
                    stack.push(c);
                    i++;
                }
            }
        }
        return stack.size() == 1 && "#".equals(stack.peek());
    }

    public boolean isValidSerialization2(String preorder) {
        int n = preorder.length();
        int num = 0;//记录#的个数
        for (int i = n - 1; i >= 0; i--) {
            if (preorder.charAt(i) == ',')
                continue;
            if (preorder.charAt(i) == '#')
                num++;
            else {
                while (i >= 0 && preorder.charAt(i) != ',')//节点数字可能有多位
                    i--;
                if (num >= 2)//#的个数>=2，消除2个#，消除一个节点数字并转换成#，即num-1
                    num--;
                else
                    return false;//#的个数不足2，证明false
            }
        }
        //最终#的个数须==1
        return num == 1;
    }

    // 108 有序数组转平衡BST  1308 BST转平衡(先中序遍历转有序数组)
    public TreeNode sortedArrayToBST(int[] nums) {
        return buildTree(nums, 0, nums.length - 1);
    }

    private TreeNode buildTree(int[] nums, int start, int end) {
        if (start > end) {
            return null;
        }
        if (start == end) {
            return new TreeNode(nums[start]);
        }
        int mid = (start + end) >> 1;
        TreeNode node = new TreeNode(nums[mid]);
        node.left = buildTree(nums, start, mid - 1);
        node.right = buildTree(nums, mid + 1, end);
        return node;
    }

    // 链表转平衡BST
    public TreeNode sortedListToBST(ListNode head) {
        return buildTree(head, null);
    }

    public TreeNode buildTree(ListNode left, ListNode right) {
        //相等时返回null
        if (left == right) {
            return null;
        }
        ListNode mid = getMid(left, right);
        TreeNode midNode = new TreeNode(mid.val);
        midNode.left = buildTree(left, mid);
        midNode.right = buildTree(mid.next, right);
        return midNode;
    }

    private ListNode getMid(ListNode left, ListNode right) {
        ListNode fast = left;
        ListNode slow = left;
        while (fast != right && fast.next != right) {
            fast = fast.next;
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    // 430 扁平化多级双向链表
    public Node flatten(Node head) {
        dfs(head);
        return head;
    }

    public Node dfs(Node node) {
        Node cur = node;
        Node last = null;

        while (cur != null) {
            Node next = cur.next;
            if (cur.child != null) {
                Node childLast = dfs(cur.child);
                childLast.next = next;
                if (null != next) {
                    next.prev = childLast;
                }
                cur.next = cur.child;
                cur.child.prev = cur;
                cur.child = null;
                last = childLast;
            } else {
                last = cur;
            }
            cur = next;
        }
        return last;
    }

    // 450 删除BST中的节点
    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) return null;
        if (root.val > key) {
            root.left = deleteNode(root.left, key);
            return root;
        } else if (root.val < key) {
            root.right = deleteNode(root.right, key);
            return root;
        } else {
            if (root.left == null && root.right == null) {
                return null;
            }
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            TreeNode next = root.right;
            while (next.left != null) {
                next = next.left;
            }
            root.right = deleteNode(root.right, next.val);
            next.left = root.left;
            next.right = root.right;
            return next;
        }
    }

    public TreeNode deleteNodeIterator(TreeNode root, int key) {
        TreeNode cur = root, curParent = null;
        while (cur != null && cur.val != key) {
            curParent = cur;
            if (cur.val > key) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        if (cur == null) {
            return root;
        }
        if (cur.left == null && cur.right == null) {
            cur = null;
        } else if (cur.right == null) {
            cur = cur.left;
        } else if (cur.left == null) {
            cur = cur.right;
        } else {
            TreeNode successor = cur.right, successorParent = cur;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }
            // 删除后继节点原本的位置，如果后继的父节点就是当前节点，右子树取后继的右（后继没有左),否则父的左取后继的右
            if (successorParent.val == cur.val) {
                successorParent.right = successor.right;
            } else {
                successorParent.left = successor.right;
            }
            successor.right = cur.right;
            successor.left = cur.left;
            cur = successor;
        }
        if (curParent == null) {
            return cur;
        } else {
            // cur变成了后继节点，此时要删除原本的cur（=key）的节点
            if (curParent.left != null && curParent.left.val == key) {
                curParent.left = cur;
            } else {
                curParent.right = cur;
            }
            return root;
        }
    }

    //230 BST中第k小的数
    public int kthSmallest(TreeNode root, int k) {
        List<Integer> list = new ArrayList<>();
        inorder(root, list);
        return list.get(k - 1);
    }

    public int kthSmallestIterator(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            k--;
            if (k == 0) return root.val;
            root = root.right;
        }
        return -1;
    }

    public int kthSmallestPriorityQueue(TreeNode root, int k) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode tmp = queue.poll();
            if (priorityQueue.size() < k) {
                priorityQueue.offer(tmp.val);
            } else if (priorityQueue.peek() > tmp.val) {
                priorityQueue.poll();
                priorityQueue.offer(tmp.val);
            }
            if (tmp.left != null) queue.offer(tmp.left);
            if (tmp.right != null) queue.offer(tmp.right);
        }
        return priorityQueue.peek();
    }

    // 返回二叉搜索树中第k小的元素
    public int kthSmallestSubCount(TreeNode root, int k) {
        Map<TreeNode, Integer> map = new HashMap<>();
        countNodeNum(root, map);
        TreeNode node = root;
        while (node != null) {
            int left = map.get(node.left);
            if (left < k - 1) { //左子树节点数量小于k-1
                node = node.right;
                k -= left + 1;  //减去左子树数量+根节点数量1
            } else if (left == k - 1) {  //左子树节点数量等于k-1
                break;
            } else {
                node = node.left;
            }
        }
        return node.val;
    }

    // 统计以node为根结点的子树的结点数
    private int countNodeNum(TreeNode node, Map<TreeNode, Integer> map) {
        if (node == null) return 0;
        map.put(node, 1 + countNodeNum(node.left, map) + countNodeNum(node.right, map));
        return map.get(node);
    }


    // 236 二叉树最近公共祖先
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 如果最近公共祖先是p或q直接返回
        if (root == null || root == q || root == p) return root;
        // p,q分别在root两侧
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) return root;
        return left == null ? right : left;
    }

    // 235 BST最近公共祖先
    public TreeNode lowestCommonAncestorBSTDFS(TreeNode root, TreeNode p, TreeNode q) {
        if (root.val < p.val && root.val < q.val) {
            return lowestCommonAncestorBSTDFS(root.right, p, q);
        } else if (root.val > p.val && root.val > q.val) {
            return lowestCommonAncestorBSTDFS(root.left, p, q);
        }
        return root;
    }

    public TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
        while (root != null) {
            if (root.val > p.val && root.val > q.val) {
                root = root.left;
            } else if (root.val < p.val && root.val < q.val) {
                root = root.right;
            } else {
                break;
            }
        }
        return root;
    }

    // 1676 所有node的最近公共祖先
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode[] nodes) {
        if (root == null) {
            return null;
        }

        if (isIn(nodes, root)) {
            return root;
        } else {
            TreeNode res = null;
            TreeNode left = lowestCommonAncestor(root.left, nodes);
            TreeNode right = lowestCommonAncestor(root.right, nodes);
            if (left != null && right != null) {
                return root;
            }
            res = (left == null) ? right : left;
            return res;
        }

    }

    public boolean isIn(TreeNode[] nodes, TreeNode root) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == root) {
                return true;
            }
        }
        return false;
    }

    //1026. 节点与其祖先之间的最大差值
    public int maxAncestorDiff(TreeNode root) {
        return maxAncestorDiffDfs(root, root.val, root.val);
    }

    private int maxAncestorDiffDfs(TreeNode root, int min, int max) {
        if (root == null) return 0;
        int diff = Math.max(Math.abs(root.val - min), Math.abs(max - root.val));
        min = Math.min(min, root.val);
        max = Math.max(max, root.val);
        diff = Math.max(diff, maxAncestorDiffDfs(root.left, min, max));
        diff = Math.max(diff, maxAncestorDiffDfs(root.right, min, max));
        return diff;
    }

    //1123. 最深叶节点的最近公共祖先
    List<TreeNode> maxList = new ArrayList<>();

    public TreeNode lcaDeepestLeaves1(TreeNode root) {
        int depth = 0;
        getDepth(root, depth + 1);
        if (maxList.size() == 1) return maxList.get(0);
        TreeNode p = maxList.get(0);
        TreeNode q = maxList.get(maxList.size() - 1);
        return parent(root, p, q);
    }

    private void getDepth(TreeNode node, int depth) {
        if (node == null) return;
        if (depth > max) {
            maxList.clear();
            maxList.add(node);
            max = depth;
        } else if (depth == max) {
            maxList.add(node);
        }
        getDepth(node.left, depth + 1);
        getDepth(node.right, depth + 1);
    }

    private TreeNode parent(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode left = parent(root.left, p, q);
        TreeNode right = parent(root.right, p, q);
        if (left != null && right != null) return root;
        return left == null ? right : left;
    }

    private TreeNode ans1123;
    private int maxDepth1123 = -1; // 全局最大深度

    public TreeNode lcaDeepestLeaves2(TreeNode root) {
        dfs1123(root, 0);
        return ans1123;
    }

    private int dfs1123(TreeNode node, int depth) {
        if (node == null) {
            maxDepth = Math.max(maxDepth, depth); // 维护全局最大深度
            return depth;
        }
        int leftMaxDepth = dfs1123(node.left, depth + 1); // 获取左子树最深叶节点的深度
        int rightMaxDepth = dfs1123(node.right, depth + 1); // 获取右子树最深叶节点的深度
        if (leftMaxDepth == rightMaxDepth && leftMaxDepth == maxDepth)
            ans1123 = node;
        return Math.max(leftMaxDepth, rightMaxDepth); // 当前子树最深叶节点的深度
    }

    //2192. 有向无环图中一个节点的所有祖先
    public List<List<Integer>> getAncestors(int n, int[][] edges) {
        Set<Integer>[] anc = new Set[n];   // 存储每个节点祖先的辅助数组
        for (int i = 0; i < n; ++i) {
            anc[i] = new HashSet<Integer>();
        }
        List<Integer>[] e = new List[n];   // 邻接表
        for (int i = 0; i < n; ++i) {
            e[i] = new ArrayList<Integer>();
        }
        int[] indeg = new int[n];   // 入度表
        // 预处理
        for (int[] edge : edges) {
            e[edge[0]].add(edge[1]);
            ++indeg[edge[1]];
        }
        // 广度优先搜索求解拓扑排序
        Queue<Integer> q = new ArrayDeque<Integer>();
        for (int i = 0; i < n; ++i) {
            if (indeg[i] == 0) {
                q.offer(i);
            }
        }
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : e[u]) {
                // 更新子节点的祖先哈希表
                anc[v].add(u);
                for (int i : anc[u]) {
                    anc[v].add(i);
                }
                --indeg[v];
                if (indeg[v] == 0) {
                    q.offer(v);
                }
            }
        }
        // 转化为答案数组
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        for (int i = 0; i < n; ++i) {
            res.add(new ArrayList<Integer>());
            for (int j : anc[i]) {
                res.get(i).add(j);
            }
            Collections.sort(res.get(i));
        }
        return res;
    }

    //865. 具有所有最深节点的最小子树
    public TreeNode subtreeWithAllDeepest(TreeNode root) {
        return f(root).getKey();

    }

    private Pair<TreeNode, Integer> f(TreeNode root) {
        if (root == null) {
            return new Pair<>(root, 0);
        }

        Pair<TreeNode, Integer> left = f(root.left);
        Pair<TreeNode, Integer> right = f(root.right);

        if (left.getValue() > right.getValue()) {
            return new Pair<>(left.getKey(), left.getValue() + 1);
        }
        if (left.getValue() < right.getValue()) {
            return new Pair<>(right.getKey(), right.getValue() + 1);
        }
        return new Pair<>(root, left.getValue() + 1);
    }

    //563 二叉树坡度
    int tilt = 0;

    public int findTilt(TreeNode root) {
        dfs(root);
        return tilt;
    }

    private int findTiltDfs(TreeNode root) {
        if (root == null) return 0;
        int left = findTiltDfs(root.left);
        int right = findTiltDfs(root.right);
        tilt += Math.abs(left - right);
        return root.val + left + right;
    }

    //606 根据二叉树创建字符串
    public String tree2str(TreeNode root) {
        return tree2strDfs(root);
    }

    private String tree2strDfs(TreeNode root) {
        if (root == null) return "";
        if (root.left == null && root.right == null) return String.valueOf(root.val);
        if (root.right == null) return root.val + "(" + tree2strDfs(root.left) + ")";
        return root.val + "(" + tree2strDfs(root.left) + ")(" + tree2strDfs(root.right) + ")";
    }

    // 654 最大二叉树
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        return construct(nums, 0, nums.length - 1);
    }

    private TreeNode construct(int[] nums, int l, int r) {
        if (l > r) return null;
        if (l == r) return new TreeNode(nums[l]);
        int idx = -1;
        int max = 0;
        for (int i = l; i <= r; i++) {
            if (max < nums[i]) {
                max = nums[i];
                idx = i;
            }
        }
        TreeNode root = new TreeNode(max);
        root.left = construct(nums, l, idx - 1);
        root.right = construct(nums, idx + 1, r);
        return root;
    }

    // 156 上下翻转二叉树
    TreeNode head;

    public TreeNode upsideDownBinaryTree(TreeNode root) {
        upsideDownBinaryTreedfs(root, null);
        return head;
    }

    private void upsideDownBinaryTreedfs(TreeNode root, TreeNode prev) {
        if (root == null) return;
        dfs(root.left, root);
        if (head == null) {
            head = root;
        }
        if (prev != null) {
            root.left = prev.right;
            root.right = prev;
            prev.left = null;
            prev.right = null;
        }
    }

    // 623 在二叉树中增加一行
    public TreeNode addOneRow(TreeNode root, int val, int depth) {
        if (depth == 1) {
            TreeNode node = new TreeNode(val);
            node.left = root;
            return node;
        }
        dfs(root, val, depth, 1);
        return root;
    }

    private void dfs(TreeNode root, int val, int depth, int level) {
        if (root == null) return;
        if (level == depth - 1) {
            TreeNode left = new TreeNode(val);
            left.left = root.left;
            TreeNode right = new TreeNode(val);
            right.right = root.right;
            root.left = left;
            root.right = right;
            return;
        }

        dfs(root.left, val, depth, level + 1);
        dfs(root.right, val, depth, level + 1);
    }

    // 690 员工的重要性
    public int getImportance(List<Employee> employees, int id) {
        List<Integer> list = new ArrayList<>();
        Map<Integer, Employee> map = new HashMap<>();
        for (Employee employee : employees) {
            map.put(employee.id, employee);
        }
        Queue<Employee> queue = new ArrayDeque<>();
        queue.offer(map.get(id));
        while (!queue.isEmpty()) {
            Employee employee = queue.poll();
            list.add(employee.importance);
            for (Integer eid : employee.subordinates) {
                queue.offer(map.get(eid));
            }
        }
        return list.stream().mapToInt(Integer::intValue).sum();
    }

    //2571. 将整数减少到零需要的最少操作数
    public int minOperations(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        int x = 0;
        while (Math.pow(2, x) < n) {
            x++;
        }
        int diff = (int) Math.min(n - Math.pow(2, x - 1), Math.pow(2, x) - n);
        return 1 + minOperations(diff);
    }

    // 655 输出二叉树
    public List<List<String>> printTree(TreeNode root) {
        int height = maxDepth(root) - 1;
        int m = height + 1;
        int n = (int) (Math.pow(2, m) - 1);
        String[][] matrix = new String[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(matrix[i], "");
        }
        dfs(root, 0, (n - 1) / 2, height, matrix);
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            result.add(new ArrayList<>(Arrays.asList(matrix[i])));
        }
        return result;
    }

    private void dfs(TreeNode node, int row, int col, int height, String[][] matrix) {
        if (node == null) return;
        matrix[row][col] = node.val + "";
        dfs(node.left, row + 1, col - (int) Math.pow(2, height - row - 1), height, matrix);
        dfs(node.right, row + 1, col + (int) Math.pow(2, height - row - 1), height, matrix);
    }

    private int maxDepth(TreeNode root) {
        if (root == null) return 0;
        int left = maxDepth(root.left);
        int right = maxDepth(root.right);
        return 1 + Math.max(left, right);
    }

    // 1382 平衡BST
    public TreeNode balanceBST(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inorder(root, list);
        return buildTree(list, 0, list.size() - 1);
    }

    private TreeNode buildTree(List<Integer> list, int l, int r) {
        if (l > r) return null;
        int mid = l + r >> 1;
        TreeNode root = new TreeNode(list.get(mid));
        root.left = buildTree(list, l, mid - 1);
        root.right = buildTree(list, mid + 1, r);
        return root;
    }


    // 894 所有可能的真二叉树
    Map<Integer, List<TreeNode>> memo = new HashMap<>();

    public List<TreeNode> allPossibleFBT(int n) {
        if (memo.containsKey(n)) return memo.get(n);
        List<TreeNode> result = new ArrayList<>();
        if (n == 1) {
            result.add(new TreeNode(0));
            memo.put(n, result);
            return result;
        }
        for (int i = 1; i < n - 1; i += 2) {
            // n是奇数才能构成 左子树也是奇数，右子树也是奇数
            List<TreeNode> lefts = allPossibleFBT(i);
            List<TreeNode> rights = allPossibleFBT(n - i - 1);
            for (TreeNode left : lefts) {
                for (TreeNode right : rights) {
                    TreeNode cur = new TreeNode(0);
                    cur.left = left;
                    cur.right = right;
                    result.add(cur);
                }
            }
        }
        memo.put(n, result);
        return result;
    }

    //1609 奇偶树
    public boolean isEvenOddTreeBFS(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        boolean even = true;
        while (!queue.isEmpty()) {
            int width = queue.size();
            int prev = even ? 0 : Integer.MAX_VALUE;
            for (int i = 0; i < width; i++) {
                TreeNode tmp = queue.poll();
                if (tmp != null) {
                    if (even && (tmp.val % 2 == 0 || tmp.val <= prev)) {
                        return false;
                    }
                    if (!even && (tmp.val % 2 != 0 || tmp.val >= prev)) {
                        return false;
                    }
                    prev = tmp.val;
                    queue.offer(tmp.left);
                    queue.offer(tmp.right);
                }
            }
            even = !even;
        }
        return true;
    }

    public boolean isEvenOddTreeDFS(TreeNode root) {
        Map<Integer, Integer> map = new HashMap<>();
        return dfs(root, 0, map);
    }

    private boolean dfs(TreeNode root, int level, Map<Integer, Integer> map) {
        boolean even = level % 2 == 0;
        int prev = map.getOrDefault(level, even ? 0 : Integer.MAX_VALUE);
        if (even && (root.val % 2 == 0 || root.val <= prev)) return false;
        if (!even && (root.val % 2 != 0 || root.val >= prev)) return false;
        map.put(level, root.val);
        if (root.left != null && !dfs(root.left, level + 1, map)) return false;
        if (root.right != null && !dfs(root.right, level + 1, map)) return false;
        return true;
    }

    // 112 路径总和

    public boolean hasPathSumDfs(TreeNode root, int targetSum) {
        if (root == null) return false;
        if (root.left == null && root.right == null) {
            return targetSum - root.val == 0;
        }
        return hasPathSumDfs(root.left, targetSum - root.val) || hasPathSumDfs(root.right, targetSum - root.val);
    }

    public boolean hasPathSumBfs(TreeNode root, int sum) {
        if (root == null) {
            return false;
        }
        Queue<TreeNode> queNode = new LinkedList<>();
        Queue<Integer> queVal = new LinkedList<>();
        queNode.offer(root);
        queVal.offer(root.val);
        while (!queNode.isEmpty()) {
            TreeNode now = queNode.poll();
            int temp = queVal.poll();
            if (now.left == null && now.right == null) {
                if (temp == sum) {
                    return true;
                }
                continue;
            }
            if (now.left != null) {
                queNode.offer(now.left);
                queVal.offer(now.left.val + temp);
            }
            if (now.right != null) {
                queNode.offer(now.right);
                queVal.offer(now.right.val + temp);
            }
        }
        return false;
    }


    // 113 路径总和
    public List<List<Integer>> pathSumDFS(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();
        Deque<Integer> path = new ArrayDeque<>();
        dfs(root, result, path, targetSum);
        return result;
    }

    public void dfs(TreeNode tree, List<List<Integer>> reuslt, Deque<Integer> path, int target) {
        if (tree == null) {
            return;
        }
        path.offerLast(tree.val);
        if (tree.val == target && tree.right == null && tree.left == null) {
            reuslt.add(new ArrayList<>(path));
            // 优化
            path.pollLast();
            return;
        }
        dfs(tree.left, reuslt, path, target - tree.val);
        dfs(tree.right, reuslt, path, target - tree.val);
        path.pollLast();
    }

    public List<List<Integer>> pathSumDFS2(TreeNode root, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Deque<Integer> path = new LinkedList<>();
        path.add(root.val);
        dfs(root, root.val, target, result, path);
        return result;
    }

    private void dfs(TreeNode root, int sum, int target, List<List<Integer>> result, Deque<Integer> path) {
        if (sum == target && root.left == null && root.right == null) {
            result.add(new ArrayList<>(path));
            return;
        }
        if (root.left != null) {
            path.addLast(root.left.val);
            dfs(root.left, sum + root.left.val, target, result, path);
            path.removeLast();
        }
        if (root.right != null) {
            path.addLast(root.right.val);
            dfs(root.right, sum + root.right.val, target, result, path);
            path.removeLast();
        }
    }

    public List<List<Integer>> pathSumBFS(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();

        if (root == null) {
            return result;
        }
        Deque<TreeNode> deque = new ArrayDeque<>();
        Deque<Integer> sumDeque = new ArrayDeque<>();
        Map<TreeNode, TreeNode> map = new HashMap<>();
        deque.offer(root);
        sumDeque.offer(0);
        while (!deque.isEmpty()) {
            TreeNode temp = deque.poll();
            int n = sumDeque.poll() + temp.val;
            if (temp.left == null && temp.right == null) {
                if (n == targetSum) {
                    result.add(getPath(temp, map));
                }
            } else {
                if (temp.left != null) {
                    deque.offer(temp.left);
                    sumDeque.offer(n);
                    map.put(temp.left, temp);
                }
                if (temp.right != null) {
                    deque.offer(temp.right);
                    sumDeque.offer(n);
                    map.put(temp.right, temp);
                }
            }
        }
        return result;
    }

    private List<Integer> getPath(TreeNode temp, Map<TreeNode, TreeNode> map) {
        List<Integer> path = new ArrayList<>();
        while (temp != null) {
            path.add(temp.val);
            temp = map.get(temp);
        }
        Collections.reverse(path);
        return path;
    }

    // 257 二叉树的所有路径
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<>();
        Deque<String> path = new ArrayDeque<>();
        dfs(result, path, root);
        return result;
    }

    private void dfs(List<String> result, Deque<String> path, TreeNode root) {
        if (root == null) return;
        path.addLast(root.val + "");
        if (root.left == null && root.right == null) {
            result.add(String.join("->", path));
            path.removeLast();
            return;
        }
        dfs(result, path, root.left);
        dfs(result, path, root.right);
        path.removeLast();
    }

    //offer 26 树的子结构
    // B是A的子结构， 即 A中有出现和B相同的结构和节点值。
    public boolean isSubStructure(TreeNode A, TreeNode B) {
        if (A == null || B == null) return false;
        return dfs(A, B) || isSubStructure(A.left, B) || isSubStructure(A.right, B);
    }

    private boolean dfs(TreeNode A, TreeNode B) {
        if (B == null) return true;
        if (A == null || A.val != B.val) return false;
        return dfs(A.left, B.left) && dfs(A.right, B.right);
    }

    // 572 另一棵树的子树
    // 二叉树 tree 的一棵子树包括 tree 的某个节点和这个节点的所有后代节点。tree 也可以看做它自身的一棵子树。
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if (root == null) return false;
        return isSubtreeDfs(root, subRoot) || isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }

    private boolean isSubtreeDfs(TreeNode s, TreeNode t) {
        if (s == null && t == null) return true;
        if (s == null || t == null || s.val != t.val) return false;
        return isSubtreeDfs(s.left, t.left) && isSubtreeDfs(s.right, t.right);
    }

    //652 寻找重复子树
    //给定一棵二叉树 root，返回所有重复的子树。
// 对于同一类的重复子树，你只需要返回其中任意一棵的根结点即可。
// 如果两棵树具有相同的结构和相同的结点值，则它们是重复的。
//输入：root = [1,2,3,4,null,2,4,null,null,4]
//输出：[[2,4],[4]]
    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        List<TreeNode> result = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        dfs(root, map, result);
        return result;
    }

    private String dfs(TreeNode node, Map<String, Integer> map, List<TreeNode> result) {
        if (node == null) return "None";
        String left = dfs(node.left, map, result);
        String right = dfs(node.right, map, result);
        String string = node.val + "," + left + "," + right;
        map.put(string, map.getOrDefault(string, 0) + 1);
        if (map.get(string) == 2) {
            result.add(node);
        }
        return string;
    }

    //1233. 删除子文件夹
    public List<String> removeSubfolders1(String[] folder) {
        Arrays.sort(folder);
        List<String> ans = new ArrayList<>();
        ans.add(folder[0]);
        for (int i = 1; i < folder.length; i++) {
            String prev = ans.get(ans.size() - 1);
            String cur = folder[i];
            if (prev.length() < cur.length()
                    && prev.equals(cur.substring(0, prev.length()))
                    && cur.charAt(prev.length()) == '/') {
                continue;
            }
            ans.add(cur);
        }
        return ans;
    }

    public List<String> removeSubfolders2(String[] folder) {
        List<String> ans = new ArrayList<>();
        Trie1233 trie = new Trie1233();
        for (int i = 0; i < folder.length; i++) {
            List<String> path = Arrays.asList(folder[i].split("/"));
            trie.insert(path, i);
        }
        dfs(trie, folder, ans);

        return ans;
    }

    private void dfs(Trie1233 trie, String[] folder, List<String> ans) {
        if (trie.ref > -1) {
            ans.add(folder[trie.ref]);
            return;
        }
        for (Trie1233 child : trie.children.values()) {
            dfs(child, folder, ans);
        }
    }

    class Trie1233 {
        int ref;
        Map<String, Trie1233> children;

        public Trie1233() {
            children = new HashMap<>();
            ref = -1;
        }

        public void insert(List<String> path, int idx) {
            Trie1233 cur = this;
            for (String p : path) {
                cur.children.putIfAbsent(p, new Trie1233());
                cur = cur.children.get(p);
            }
            cur.ref = idx;
        }
    }

    //1948
    class Triee {
        Map<String, Triee> children;
        boolean isEnd;
        String word;
        List<List<String>> paths;

        public Triee(String word) {
            this.word = word;
            paths = new ArrayList<>();
            children = new HashMap<>();
        }

        public void insert(List<String> path) {
            Triee node = this;
            for (String word : path) {
                Triee child = node.children.get(word);
                if (child == null) {
                    child = new Triee(word);
                    node.children.put(word, child);
                }
                child.paths.add(path);
                node = child;
            }
            node.isEnd = true;
        }
    }

    public List<List<String>> deleteDuplicateFolder(List<List<String>> paths) {
        Map<String, List<Triee>> map = new HashMap<>();
        Triee trie = new Triee("");
        for (List<String> path : paths) {
            trie.insert(path);
        }
        dfs(trie, map);
        Set<Integer> idx = new HashSet<>();
        for (Map.Entry<String, List<Triee>> entry : map.entrySet()) {
            if (entry.getValue().size() == 1) continue;
            for (Triee each : entry.getValue()) {
                for (List<String> p : each.paths) {
                    idx.add(paths.indexOf(p));
                }
            }
        }
        List<List<String>> ans = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            if (idx.contains(i)) continue;
            ans.add(paths.get(i));
        }
        return ans;
    }

    private String dfs(Triee trie, Map<String, List<Triee>> map) {
        if (trie.children.isEmpty()) return "(" + trie.word + ")";
        List<String> childStr = new ArrayList<>();
        for (Triee child : trie.children.values()) {
            childStr.add(dfs(child, map));
        }
        childStr.sort(String::compareTo);
        String key = String.join("", childStr);
        List<Triee> ls = map.getOrDefault(key, new ArrayList<>());
        ls.add(trie);
        map.put(key, ls);
        return "(" + key + trie.word + ")";
    }

    public List<List<String>> deleteDuplicateFolder1(List<List<String>> paths) {
        TrieNode root = new TrieNode();
        for (List<String> path : paths) {
            TrieNode curr = root;
            for (String folder : path) {
                if (!curr.children.containsKey(folder)) {
                    curr.children.put(folder, new TrieNode());
                }
                curr = curr.children.get(folder);
            }
        }
        delete(root, new HashMap<>());
        List<List<String>> ans = new ArrayList<>();
        dfs(root, new ArrayList<>(), ans);
        return ans;
    }

    String delete(TrieNode root, Map<String, TrieNode> map) {
        if (root.children.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, TrieNode> e : root.children.entrySet()) {
            String folder = e.getKey();
            TrieNode child = e.getValue();
            sb.append('(').append(folder).append(delete(child, map)).append(')');
        }
        String serialized = sb.toString();
        if (map.containsKey(serialized)) {// 已经存在此序列化值，两者都删除
            map.get(serialized).deleted = true;
            root.deleted = true;
        } else {
            map.put(serialized, root);
        }
        return serialized;
    }

    void dfs(TrieNode root, List<String> path, List<List<String>> ans) {
        for (Map.Entry<String, TrieNode> e : root.children.entrySet()) {
            String folder = e.getKey();
            TrieNode child = e.getValue();
            if (child.deleted) continue;
            path.add(folder);
            dfs(child, path, ans);
            path.remove(path.size() - 1);
        }
        if (!path.isEmpty()) ans.add(new ArrayList<>(path));
    }

    static class TrieNode {
        Map<String, TrieNode> children = new TreeMap<>();
        boolean deleted;
    }

    Folder root = new Folder("");
    Map<String, Integer> keys = new HashMap<>();

    public List<List<String>> deleteDuplicateFolder2(List<List<String>> paths) {
        for (List<String> path : paths) {
            addPath(path);
        }

        for (Folder f : root.list) {
            generateKey(f);
        }

        for (Folder f : root.list) {
            updateDeleteStatus(f);
        }

        List<List<String>> results = new ArrayList<>();

        for (List<String> path : paths) {
            if (isValid(path))
                results.add(path);
        }

        return results;
    }

    private boolean isValid(List<String> path) {
        Folder current = root;

        for (String f : path) {
            current = current.map.get(f);

            if (current.del)
                return false;
        }

        return true;
    }

    private void updateDeleteStatus(Folder f) {
        if (f.list.size() > 0 && keys.get(f.key) > 1) {
            f.del = true;
            return;
        }

        for (Folder fold : f.list) {
            updateDeleteStatus(fold);
        }
    }

    private String generateKey(Folder fold) {
        StringBuilder sb = new StringBuilder();

        if (fold.list.size() == 0)
            return sb.toString();

        // sort to order matches
        Collections.sort(fold.list, (a, b) -> a.name.compareTo(b.name));

        for (Folder f : fold.list) {
            sb.append('(');
            sb.append(f.name + generateKey(f));
            sb.append(')');
        }

        String key = sb.toString();
        fold.key = key;
        keys.put(key, keys.getOrDefault(key, 0) + 1);

        return key;
    }

    private void addPath(List<String> path) {
        Folder current = root;

        for (String f : path) {
            if (!current.map.containsKey(f)) {
                Folder fold = new Folder(f);
                current.map.put(f, fold);
                current.list.add(fold);
            }

            current = current.map.get(f);
        }
    }

    class Folder {
        String name;
        Map<String, Folder> map;
        List<Folder> list;
        String key;
        boolean del;

        Folder(String name) {
            this.name = name;
            map = new HashMap<>();
            list = new ArrayList<>();
            key = "";
            del = false;
        }
    }

    // 473 火柴拼正方形
    public boolean makesquare(int[] matchsticks) {
        int sum = 0;
        for (int len : matchsticks) {
            sum += len;
        }
        if (sum % 4 != 0) return false;
        Arrays.sort(matchsticks);
        int[] edges = new int[4];
        return dfs(matchsticks.length - 1, matchsticks, edges, sum / 4);
    }

    // 1 2 4 5
    private boolean dfs(int idx, int[] matchsticks, int[] edges, int len) {
        if (idx < 0) return true;
        for (int i = 0; i < edges.length; i++) {
            edges[i] += matchsticks[idx];
            if (edges[i] <= len && dfs(idx - 1, matchsticks, edges, len)) {
                return true;
            }
            edges[i] -= matchsticks[idx];
        }
        return false;
    }

    public boolean makesquareDP(int[] matchsticks) {
        int totalLen = Arrays.stream(matchsticks).sum();
        if (totalLen % 4 != 0) {
            return false;
        }
        int len = totalLen / 4, n = matchsticks.length;
        int[] dp = new int[1 << n];
        Arrays.fill(dp, -1);
        dp[0] = 0;
        //下面两行用来找第k个火柴
        for (int s = 1; s < (1 << n); s++) {
            for (int k = 0; k < n; k++) {
                if ((s & (1 << k)) == 0) {
                    continue;
                }
                //除去第k个火柴的其他火柴
                int s1 = s & ~(1 << k);
                if (dp[s1] >= 0 && dp[s1] + matchsticks[k] <= len) {
                    dp[s] = (dp[s1] + matchsticks[k]) % len;
                    break;
                }
            }
        }
        return dp[(1 << n) - 1] == 0;
    }


    // 538 二叉搜索树转换为累加树
    //反序中序遍历

    public TreeNode convertBST(TreeNode root) {
        if (root != null) {
            convertBST(root.right);
            sum += root.val;
            root.val = sum;
            convertBST(root.left);
        }
        return root;
    }

    // 114 二叉树展开为链表
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        List<TreeNode> list = new ArrayList<>();
        dfsflatten(root, list);
        for (int i = 1; i < list.size(); i++) {
            TreeNode prev = list.get(i - 1);
            TreeNode curr = list.get(i);
            prev.right = curr;
        }
    }

    public void dfsflatten(TreeNode root, List<TreeNode> list) {
        if (root == null) return;
        list.add(root);
        dfsflatten(root.left, list);
        dfsflatten(root.right, list);
    }

    //O(1)
    public void flatten2(TreeNode root) {
        TreeNode curr = root;
        while (curr != null) {
            if (curr.left != null) {
                TreeNode next = curr.left;
                TreeNode pre = next;
                while (pre.right != null) {
                    pre = pre.right;
                }
                pre.right = curr.right;
                curr.right = next;
                curr.left = null;
            }

            curr = curr.right;
        }
    }

    // 116 填充每个节点的下一个右侧节点指针
    public Node connect(Node root) {
        if (root == null) {
            return null;
        }
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int width = queue.size();
            for (int i = 0; i < width; i++) {
                Node tmp = queue.poll();
                if (i < width - 1) {
                    tmp.next = queue.peek();
                }
                if (tmp.left != null) {
                    queue.offer(tmp.left);
                }
                if (tmp.right != null) {
                    queue.offer(tmp.right);
                }
            }
        }
        return root;
    }

    public Node connect2(Node root) {
        if (root == null) {
            return null;
        }
        Node leftmost = root;
        while (leftmost.left != null) {
            Node head = leftmost;
            while (head != null) {
                head.left.next = head.right;
                if (head.next != null) {
                    head.right = head.next.left;
                }
                head = head.next;
            }
            leftmost = leftmost.left;
        }
        return root;
    }

    //117. 填充每个节点的下一个右侧节点指针 II
    public Node connectNormal2(Node root) {
        if (root == null) {
            return null;
        }
        Node cur = root;
        while (cur != null) {
            Node dump = new Node(0);
            Node pre = dump;
            while (cur != null) {
                if (cur.left != null) {
                    pre.next = cur.left;
                    pre = pre.next;
                }
                if (cur.right != null) {
                    pre.next = cur.right;
                    pre = pre.next;
                }
                cur = cur.next;
            }
            cur = dump.next;
        }
        return root;
    }

    //397 整数替换
    public int integerReplacement(int n) {
        Map<Long, Integer> visited = new HashMap<>();
        return dfs(n, visited);
    }

    private int dfs(long n, Map<Long, Integer> visited) {
        if (n == 1) return 0;
        if (visited.containsKey(n)) return visited.get(n);
        int ans = 0;

        if (n % 2 == 0) {
            ans = dfs(n / 2, visited) + 1;
        } else {
            ans = Math.min(dfs(n + 1, visited), dfs(n - 1, visited)) + 1;
        }
        visited.put(n, ans);
        return ans;
    }

    // 403 青蛙过河
    //可以假定它第一步只能跳跃 1 个单位（即只能从单元格 1 跳至单元格 2 ）。
    // idx=0 只能跳1 idx1 最多跳2  idx i 最多跳i+1
    public boolean canCross(int[] stones) {
        int n = stones.length;
        Boolean[][] cache = new Boolean[n][n];
        // 将石子信息存入哈希表
        // 为了快速判断是否存在某块石子，以及快速查找某块石子所在下标
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < stones.length; i++) {
            map.put(stones[i], i);
        }
        // check first step
        // 根据题意，第一步是固定经过步长 1 到达第一块石子（下标为 1）
        if (!map.containsKey(1)) return false;
        return dfs(stones, 0, 0, map, cache);
    }

    /**
     * 判定是否能够跳到最后一块石子
     *
     * @param stones   石子列表【不变】
     * @param i        当前所在的石子的下标
     * @param lastDist 上一次是经过多少步跳到当前位置的
     * @param map      根据 距离 快速查找下标 可替换为[i+1,len-1]区间查找stones[i]+curDist值的二分查找
     * @param cache    记忆化搜索缓存 null没有计算过，false 计算后不能到达 true计算后能到达
     * @return 是否能跳到最后一块石子
     */
    private boolean dfs(int[] stones, int i, int lastDist, Map<Integer, Integer> map, Boolean[][] cache) {
        if (i == stones.length - 1) return true;
        if (cache[i][lastDist] != null) return cache[i][lastDist];
        for (int curDist = lastDist - 1; curDist <= lastDist + 1; curDist++) {
            if (curDist == 0) continue;
            int next = stones[i] + curDist;
            if (map.containsKey(next)) {
                boolean curBool = dfs(stones, map.get(next), curDist, map, cache);
                cache[i][lastDist] = curBool;
                if (curBool) return true;
            }
        }
        cache[i][lastDist] = false;
        return false;
    }

    public boolean canCrossDP(int[] stones) {
        int n = stones.length;
        boolean[][] dp = new boolean[n][n];
        dp[0][0] = true;
        for (int i = 1; i < n; ++i) {
            if (stones[i] - stones[i - 1] > i) {
                return false;
            }
        }
        for (int i = 1; i < n; ++i) {
            for (int j = 0; j <= i - 1; j++) {
                int k = stones[i] - stones[j];
                // 我们知道从位置 j 到位置 i 是需要步长为 k 的跳跃

                // 而从位置 j 发起的跳跃最多不超过 j + 1
                // 因为每次跳跃，下标至少增加 1，而步长最多增加 1
                if (k <= j + 1) {
                    dp[i][k] = dp[j][k - 1] || dp[j][k] || dp[j][k + 1];
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (dp[n - 1][i]) return true;
        }
        return false;
    }

    // 821字符的最短距离
    public int[] shortestToChar(String s, char c) {
        int n = s.length();
        int[] answers = new int[n];
        Arrays.fill(answers, n + 1);
        int idx = -1;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == c) idx = i;
            if (idx != -1) answers[i] = i - idx;
        }
        idx = -1;
        for (int i = n - 1; i >= 0; i--) {
            if (s.charAt(i) == c) idx = i;
            if (idx != -1) answers[i] = Math.min(answers[i], idx - i);
        }
        return answers;
    }

    public int[] shortestToChar2(String s, char c) {
        int n = s.length();
        int[] ans = new int[n];
        for (int i = 0, idx = -n; i < n; i++) {
            if (s.charAt(i) == c) {
                idx = i;
            }
            ans[i] = i - idx;
        }
        for (int i = n - 1, idx = 2 * n; i >= 0; i--) {
            if (s.charAt(i) == c) {
                idx = i;
            }
            ans[i] = Math.min(ans[i], idx - i);
        }
        return ans;
    }

    public int[] shortestToCharBfs(String s, char c) {
        int n = s.length();
        int[] ans = new int[n];
        Arrays.fill(ans, -1);
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == c) {
                deque.offer(i);
                ans[i] = 0;
            }
        }
        int[] directions = new int[]{1, -1};
        while (!deque.isEmpty()) {
            int idx = deque.poll();
            for (int d : directions) {
                int near = idx + d;
                if (near >= 0 && near < n && ans[near] == -1) {
                    ans[near] = ans[idx] + 1;
                    deque.offer(near);
                }
            }
        }
        return ans;
    }


    //394 字符串解码
    //输入：s = "3[a]2[bc]"
    //输出："aaabcbc"
    public String decodeString(String s) {
        Stack<Character> stack = new Stack<>();
        for (Character c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                stack.push(c);
            } else if (c == ']') {
                StringBuilder letter = new StringBuilder();
                while (!stack.isEmpty() && Character.isLetter(stack.peek())) {
                    letter.insert(0, stack.pop());
                }
                stack.pop();
                StringBuilder number = new StringBuilder();
                while (!stack.isEmpty() && Character.isDigit(stack.peek())) {
                    number.insert(0, stack.pop());
                }
                int count = Integer.valueOf(number.toString());
                for (int i = 0; i < count; i++) {
                    for (Character cc : letter.toString().toCharArray()) {
                        stack.push(cc);
                    }
                }
            } else {
                stack.push(c);
            }
        }
        StringBuilder retv = new StringBuilder();
        while (!stack.isEmpty())
            retv.insert(0, stack.pop());

        return retv.toString();
    }


    public String decodeStringDFS(String s) {
        return dfs(s, 0)[0];
    }

    private String[] dfs(String s, int i) {
        StringBuilder res = new StringBuilder();
        int num = 0;
        while (i < s.length()) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                num = num * 10 + Integer.valueOf(String.valueOf(s.charAt(i)));
            } else if (s.charAt(i) == '[') {
                String[] tmp = dfs(s, i + 1);
                i = Integer.valueOf(tmp[0]);
                while (num > 0) {
                    res.append(tmp[1]);
                    num--;
                }
            } else if (s.charAt(i) == ']') {
                return new String[]{String.valueOf(i), res.toString()};
            } else {
                res.append(s.charAt(i));
            }
            i++;
        }
        return new String[]{res.toString()};
    }

    // 838 推多米诺
    public String pushDominoes(String dominoes) {
        int n = dominoes.length();
        char[] s = dominoes.toCharArray();
        char left = 'L';
        int i = 0;
        // 原本是L/R的最终还是L/R，只用判断.的结果
        while (i < n) {
            int j = i;
            while (j < n && s[j] == '.') {
                j++;
            }
            char right = j < n ? s[j] : 'R';//j多加1
            if (left == right) { // 左右一致 单边倒
                while (i < j) {
                    s[i++] = left;
                }
            } else if (left == 'R' && right == 'L') { //左右不一致，从两侧往中间倒
                int k = j - 1;
                while (i < k) {
                    s[i++] = left;
                    s[k--] = right;
                }
            }
            left = right; //计算下一个区间，left变成当前right的值
            i = j + 1;
        }
        return new String(s);
    }

    public String pushDominoesBFS(String dominoes) {
        int n = dominoes.length();
        int[] times = new int[n];
        char[] chars = dominoes.toCharArray();
        Deque<int[]> deque = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (chars[i] == '.') continue;
            int dire = chars[i] == 'L' ? -1 : 1;
            times[i] = 1;
            deque.offer(new int[]{i, 1, dire});
        }
        while (!deque.isEmpty()) {
            int[] tmp = deque.poll();
            int x = tmp[0], time = tmp[1], dire = tmp[2];
            int next = x + dire;
            if (chars[x] == '.' || (next < 0 || next >= n)) continue;
            if (times[next] == 0) {
                deque.offer(new int[]{next, time + 1, dire});
                times[next] = time + 1;
                chars[next] = dire == -1 ? 'L' : 'R';
            } else if (times[next] == time + 1) {
                chars[next] = '.';
            }
        }
        return new String(chars);
    }


    //671 二叉树第二小的节点
    public int findSecondMinimumValue(TreeNode root) {
        Set<Integer> set = new HashSet<>();
        dfs(root, set);
        if (set.size() < 2) return -1;
        int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
        for (int i : set) {
            if (i <= first) {
                second = first;
                first = i;
            } else if (i <= second) {
                second = i;
            }
        }
        return second;
    }

    private void dfs(TreeNode root, Set<Integer> set) {
        if (root == null) return;
        set.add(root.val);
        dfs(root.left, set);
        dfs(root.right, set);
    }

    int secondMin = -1;
    int firstMin = 0;

    public int findSecondMinimumValueDFS(TreeNode root) {
        // root是最小的
        firstMin = root.val;
        findSecondMinimumValueDfs(root);
        return secondMin;
    }

    private void findSecondMinimumValueDfs(TreeNode node) {
        if (node == null) return;
        if (secondMin != -1 && node.val >= secondMin) return;
        if (node.val > firstMin) secondMin = node.val;
        findSecondMinimumValueDfs(node.left);
        findSecondMinimumValueDfs(node.right);
    }

    // 700 BST中的搜索
///给定二叉搜索树（BST）的根节点 root 和一个整数值 val。
// 你需要在 BST 中找到节点值等于 val 的节点。 返回以该节点为根的子树。 如果节点不存在，则返回 null 。
//输入：root = [4,2,7,1,3], val = 2
//输出：[2,1,3]
    public TreeNode searchBST(TreeNode root, int val) {
        if (root == null) return null;
        if (root.val < val) return searchBST(root.right, val);
        if (root.val > val) return searchBST(root.left, val);
        return root;
    }

    // 530 783 BST节点最小距离
    //给你一个二叉搜索树的根节点 root ，返回 树中任意两不同节点值之间的最小差值 。
// 差值是一个正数，其数值等于两值之差的绝对值。
//输入：root = [4,2,6,1,3]
//输出：1
    int min = Integer.MAX_VALUE;
    int preNum = -1;

    public int minDiffInBST(TreeNode root) {
        dfs(root);
        return min;
    }

    private void dfs(TreeNode root) {
        if (root == null) return;
        dfs(root.left);
        if (preNum != -1) min = Math.min(min, root.val - preNum);
        preNum = root.val;
        dfs(root.right);
    }

    //872 叶子相似的树
    public boolean leafSimilar(TreeNode root1, TreeNode root2) {
        List<Integer> list1 = new ArrayList<>();
        dfs(root1, list1);
        List<Integer> list2 = new ArrayList<>();
        dfs(root2, list2);
        if (list1.size() != list2.size()) return false;
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i))) return false;
        }
        return true;
    }

    private void dfs(TreeNode root, List<Integer> list) {
        if (root == null) return;
        if (root.left == null && root.right == null) list.add(root.val);
        dfs(root.left, list);
        dfs(root.right, list);
    }

    //897 递增顺序BST
    //Approach 1: In-Order Traversal
    public TreeNode increasingBST(TreeNode root) {
        List<Integer> vals = new ArrayList<>();
        inorder(root, vals);
        TreeNode ans = new TreeNode(0), cur = ans;
        for (int v : vals) {
            cur.right = cur = new TreeNode(v);
        }
//        for (int v : vals) {
//            cur.right = new TreeNode(v);
//            cur = cur.right;
//        }
        return ans.right;
    }

    public void inorder(TreeNode node, List<Integer> vals) {
        if (node == null) return;
        inorder(node.left, vals);
        vals.add(node.val);
        inorder(node.right, vals);
    }

    //Approach 2: Traversal with Relinking
    TreeNode newRoot = null;
    TreeNode prevTree = null;

    public TreeNode increasingBST2(TreeNode root) {
        inorder(root);
        return newRoot;
    }

    private void inorder(TreeNode root) {
        if (root == null) return;
        inorder(root.left);
        if (newRoot == null) newRoot = root;
        if (prevTree != null) {
            root.left = null;
            prevTree.right = root;
        }
        prevTree = root;
        inorder(root.right);
    }

    // 285 二叉搜索树中的中序后继
    // 面试题 04.06. 后继者
    TreeNode successor;
    boolean find = false;

    public TreeNode inorderSuccessor1(TreeNode root, TreeNode p) {
        inorderSuccessorDfs(root, p);
        return successor;
    }

    private void inorderSuccessorDfs(TreeNode root, TreeNode p) {
        if (find) return;
        if (root == null) return;
        inorderSuccessorDfs(root.right, p);
        if (root.val == p.val) {
            find = true;
            return;
        }
        if (!find) successor = root;
        inorderSuccessorDfs(root.left, p);
    }

    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        Stack<TreeNode> stack = new Stack<>();
        boolean find = false;
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            if (find) return root;
            if (root == p) find = true;
            root = root.right;
        }
        return null;
    }

    public TreeNode inorderSuccessor2(TreeNode root, TreeNode p) {
        if (root == null) return null;
        if (root.val <= p.val) return inorderSuccessor2(root.right, p);
        TreeNode ans = inorderSuccessor2(root.left, p);
        return ans == null ? root : ans;
    }

    //653 BST两数之和
    public boolean findTarget(TreeNode root, int k) {
        Set<Integer> set = new HashSet<>();
        return findTargetDfs(root, k, set);
    }

    private boolean findTargetDfs(TreeNode root, int k, Set<Integer> set) {
        if (root == null) return false;
        if (set.contains(k - root.val)) return true;
        set.add(root.val);
        return findTargetDfs(root.left, k, set) || findTargetDfs(root.right, k, set);
    }

    // 938 BST的范围和
    public int rangeSumBST(TreeNode root, int low, int high) {
        if (root == null) return 0;
        if (root.val < low) {
            return rangeSumBST(root.right, low, high);
        } else if (root.val > high) {
            return rangeSumBST(root.left, low, high);
        } else {
            return root.val + rangeSumBST(root.left, low, high) + rangeSumBST(root.right, low, high);
        }
    }

    //993 二叉树堂兄弟节点
    public boolean isCousins(TreeNode root, int x, int y) {
        int xdepth = getDepth(root, x, 0);
        int ydepth = getDepth(root, y, 0);
        if (xdepth != ydepth) return false;
        TreeNode xroot = getRoot(root, x);
        TreeNode yroot = getRoot(root, y);
        return xroot != yroot;
    }

    private int getDepth(TreeNode node, int n, int depth) {
        if (node == null) return 0;
        if (node.val == n) return depth;
        int left = getDepth(node.left, n, depth + 1);
        int right = getDepth(node.right, n, depth + 1);
        return left > 0 ? left : right;
    }

    private TreeNode getRoot(TreeNode node, int n) {
        if (node == null) return null;
        if (node.left != null && node.left.val == n) return node;
        if (node.right != null && node.right.val == n) return node;
        TreeNode left = getRoot(node.left, n);
        TreeNode right = getRoot(node.right, n);
        return left != null ? left : right;
    }

    //2641. 二叉树的堂兄弟节点 II
    public TreeNode replaceValueInTree(TreeNode root) {
        root.val = 0;
        Queue<List<TreeNode>> queue = new ArrayDeque<>();
        List<TreeNode> ls = new ArrayList<>();
        ls.add(root);
        queue.offer(ls);
        while (!queue.isEmpty()) {
            List<TreeNode> curLevel = queue.poll();
            List<TreeNode> nextLevel = new ArrayList<>();
            int nextLevelSum = 0;
            for (TreeNode node : curLevel) {
                if (node.left != null) {
                    nextLevelSum += node.left.val;
                    nextLevel.add(node.left);
                }
                if (node.right != null) {
                    nextLevelSum += node.right.val;
                    nextLevel.add(node.right);
                }
            }
            for (TreeNode node : curLevel) {
                int childrenSum = (node.left == null ? 0 : node.left.val) + (node.right == null ? 0 : node.right.val);
                if (node.left != null) node.left.val = nextLevelSum - childrenSum;
                if (node.right != null) node.right.val = nextLevelSum - childrenSum;
            }
            if (nextLevel.size() > 0) {
                queue.offer(nextLevel);
            }
        }
        return root;
    }

    //310 最小高度树
    // 度为1的叶子节点入队，从外围往中间遍历，最后遍历到的是作为根节点的点
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        List<Integer> ans = new ArrayList<>();

        if (n <= 1) {
            ans.add(0);
            return ans;
        }
        int[] degrees = new int[n];
        List<List<Integer>> map = new ArrayList<>();
        Arrays.fill(degrees, 0);
        for (int i = 0; i < n; i++) {
            map.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            degrees[edge[0]]++;
            degrees[edge[1]]++;
            map.get(edge[0]).add(edge[1]);
            map.get(edge[1]).add(edge[0]);
        }
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (degrees[i] == 1) queue.offer(i);
        }
        while (!queue.isEmpty()) {
            int width = queue.size();
            ans = new ArrayList<>();
            for (int i = 0; i < width; i++) {
                int tmp = queue.poll();
                ans.add(tmp);
                List<Integer> neibors = map.get(tmp);
                /*这里就是经典的bfs了，把当前节点的相邻接点都拿出来，
                 * 把它们的出度都减1，因为当前节点已经不存在了，所以，
                 * 它的相邻节点们就有可能变成叶子节点*/
                for (int j : neibors) {
                    degrees[j]--;
                    if (degrees[j] == 1) queue.offer(j);
                }
            }
        }
        return ans;
    }

    public Node construct(int[][] grid) {
        return formNode(grid, 0, grid.length - 1, 0, grid.length - 1);
    }

    // 968 监控二叉树
    // 每个node3中状态
    // 0 无覆盖
    // 1 有摄像头
    // 2 有覆盖
    int result968 = 0;

    public int minCameraCover(TreeNode root) {
        int rootState = traversal(root);
        if (rootState == 0) result968++;
        return result968;
    }

    // 贪心判断：叶节点的父节点放置摄像头最少
    // 后序遍历，已知两子状态来决定父的状态
    private int traversal(TreeNode root) {
        // 若空节点state=0,那么叶节点需放置
        // 若空节点state=1,那么叶节点的父节点无需放置(因为叶节点已被覆盖,父节点无必要性)
        if (root == null) return 2;
        int left = traversal(root.left);
        int right = traversal(root.right);
        // 子有0的判断>子有1的判断 因为一旦有一个子是无覆盖，父一定是1
        if (left == 0 || right == 0) {
            result968++;
            return 1;
        }
        // 子有一个有摄像头，且此时另一个子肯定至少被覆盖，父即使2
        if (left == 1 || right == 1) return 2;
        if (left == 2 && right == 2) return 0;
        return -1;
    }

    //427 建立四叉树
    public Node formNode(int[][] grid, int l, int r, int low, int high) {
        if (allSame(grid, l, r, low, high)) {
            return new Node(grid[l][low] == 1 ? 1 : 0, true);
        }
        int mid1 = (l + r) / 2, mid2 = (low + high) / 2;
        Node topLeft = formNode(grid, l, mid1, low, mid2);
        Node topRight = formNode(grid, l, mid1, mid2 + 1, high);
        Node bottomLeft = formNode(grid, mid1 + 1, r, low, mid2);
        Node bottomRight = formNode(grid, mid1 + 1, r, mid2 + 1, high);
        return new Node(1, false, topLeft, topRight, bottomLeft, bottomRight);
    }

    public boolean allSame(int[][] grid, int l, int r, int low, int high) {
        //判断矩阵某区域是否同值
        for (int i = l; i <= r; i++) {
            for (int j = low; j <= high; j++) {
                if (grid[i][j] != grid[l][low]) {
                    return false;
                }
            }
        }
        return true;
    }

    //638 大礼包
    public int shoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
        int n = price.size();
        List<List<Integer>> filterSpecial = new ArrayList<>();
        for (List<Integer> sp : special) {
            int totalCount = 0, totalPrice = 0;
            for (int i = 0; i < n; i++) {
                totalCount += sp.get(i);
                totalPrice += sp.get(i) * price.get(i);
            }
            if (totalCount > 0 && totalPrice > sp.get(n)) {
                filterSpecial.add(sp);
            }
        }
        Map<List<Integer>, Integer> memo = new HashMap<>();
        return dfs(price, needs, memo, filterSpecial);
    }

    private int dfs(List<Integer> price, List<Integer> curNeeds, Map<List<Integer>, Integer> memo, List<List<Integer>> filterSpecial) {
        int n = price.size();
        if (memo.containsKey(curNeeds)) return memo.get(curNeeds);
        int minPrice = 0;
        for (int i = 0; i < n; i++) {
            minPrice += price.get(i) * curNeeds.get(i);
        }
        for (List<Integer> curSpecial : filterSpecial) {
            int specialPrice = curSpecial.get(n);
            List<Integer> nextNeeds = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                // 提前break,某个礼包某个物品多于需求
                if (curSpecial.get(i) > curNeeds.get(i)) {
                    break;
                }
                nextNeeds.add(curNeeds.get(i) - curSpecial.get(i));
            }
            if (nextNeeds.size() == n) {
                minPrice = Math.min(minPrice, dfs(price, nextNeeds, memo, filterSpecial) + specialPrice);
            }
        }
        memo.put(curNeeds, minPrice);
        return minPrice;
    }

    // 736 Lisp 语法解析
    public int evaluate(String expression) {
        return calculate(new HashMap<>(), expression);
    }

    int solve(Map<String, Integer> map, String s) {
        //计算在外层变量赋值为map的情况下，求s的表达式值，本方法计算的都是开头有let的式子
        //先把映射复制一份：
        Map<String, Integer> newMap = new HashMap<>();
        for (String t : map.keySet()) {
            newMap.put(t, map.get(t));
        }
        //先把结果的表达式摘出来：
        int r = nextSpace(s, s.length() - 1, -1);
        String ans = s.substring(r + 1, s.length() - 1);//存储结果的表达式
        //此时的r应该是赋值操作后的那个空格的位置，也即是赋值表达式终止的位置
        //下边处理赋值的若干表达式：
        for (int i = 4; i < r; ) {
            int j = i;
            i = nextSpace(s, i, 1);
            String a = s.substring(j + 1, i);
            j = i;
            i = nextSpace(s, i, 1);
            String b = s.substring(j + 1, i);
            newMap.put(a, calculate(newMap, b));
        }
        return calculate(newMap, ans);
    }

    int calculate(Map<String, Integer> map, String s) {
        if (s.charAt(0) != '(') {
            if (map.containsKey(s)) {
                return map.get(s);
            }
            return Integer.parseInt(s);
        }
        char c = s.charAt(1);
        if (c == 'l') {
            return solve(map, s);
        }
        //add：加，mult：乘，，首先开头有括号括着的肯定是这两种运算
        int i = s.indexOf(' ');//定位在要处理的两个变量之前的空格；
        int j = i;
        i = nextSpace(s, i, 1);
        int a = calculate(map, s.substring(j + 1, i)), b = calculate(map, s.substring(i + 1, s.length() - 1));
        return c == 'a' ? a + b : a * b;
    }

    int nextSpace(String s, int k, int plus) {
        //在此时k定位某式子之前的时候，找到跳过下边完整表达式的下一个空格的位置
        k += plus;
        char c = s.charAt(k);
        if (c == '(' || c == ')') {
            //第一个是表达式
            int d = 0;
            for (; ; k += plus) {
                c = s.charAt(k);
                if (c == '(') {
                    d += plus;
                } else if (c == ')') {
                    d -= plus;
                }
                if (d == 0) {
                    k += plus;
                    break;
                }
            }
        } else {
            while (s.charAt(k) != ' ') {
                k += plus;
            }
        }
        return k;
    }

    // 749 隔离病毒
    int[][] dirs = new int[][]{{0,1},{1,0},{0,-1},{-1,0}};
    public int containVirus(int[][] isInfected) {
        int m = isInfected.length, n = isInfected[0].length;
        int ans = 0;
        while (true) {
            List<Set<Integer>> neighbors = new ArrayList<Set<Integer>>();
            List<Integer> firewalls = new ArrayList<Integer>();
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (isInfected[i][j] == 1) {
                        Queue<int[]> queue = new ArrayDeque<int[]>();
                        queue.offer(new int[]{i, j});
                        Set<Integer> neighbor = new HashSet<Integer>();
                        int firewall = 0, idx = neighbors.size() + 1;
                        isInfected[i][j] = -idx;

                        while (!queue.isEmpty()) {
                            int[] arr = queue.poll();
                            int x = arr[0], y = arr[1];
                            for (int d = 0; d < 4; ++d) {
                                int nx = x + dirs[d][0], ny = y + dirs[d][1];
                                if (nx >= 0 && nx < m && ny >= 0 && ny < n) {
                                    if (isInfected[nx][ny] == 1) {
                                        queue.offer(new int[]{nx, ny});
                                        isInfected[nx][ny] = -idx;
                                    } else if (isInfected[nx][ny] == 0) {
                                        ++firewall;
                                        neighbor.add(getHash(nx, ny));
                                    }
                                }
                            }
                        }
                        neighbors.add(neighbor);
                        firewalls.add(firewall);
                    }
                }
            }

            if (neighbors.isEmpty()) {
                break;
            }

            int idx = 0;
            for (int i = 1; i < neighbors.size(); ++i) {
                if (neighbors.get(i).size() > neighbors.get(idx).size()) {
                    idx = i;
                }
            }
            ans += firewalls.get(idx);
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (isInfected[i][j] < 0) {
                        if (isInfected[i][j] != -idx - 1) {
                            isInfected[i][j] = 1;
                        } else {
                            isInfected[i][j] = 2;
                        }
                    }
                }
            }
            for (int i = 0; i < neighbors.size(); ++i) {
                if (i != idx) {
                    for (int val : neighbors.get(i)) {
                        int x = val >> 16, y = val & ((1 << 16) - 1);
                        isInfected[x][y] = 1;
                    }
                }
            }
            if (neighbors.size() == 1) {
                break;
            }
        }
        return ans;
    }

    public int getHash(int x, int y) {
        return (x << 16) ^ y;
    }

    // 1569. 将子数组重新排序得到同一个二叉查找树的方案数 排列组合
    // 根节点确定，左子树和右子树的元素互换位置，逐个插入，最终结果不变
    // eg：[3,4,5,1,2]  3是根节点，左子树相对位置 [1 2]不变（左子树排列数=1） 右子树[4，5]相对位置不变（右子树排列数=1）
    // 除去3后，从剩余4个位置里挑size(l)（左子树数量）个位置放左子树 C(size-1,size(l)) 剩下位置自然放右子树
    // 如果左子树排列=F(l),右子树=F(r),在每个组合的位置内，分别乘各自组合数，最终F(3) = C(size-1,size(l))*F(l)*F(r)
    // 最后-1 减去题干中的nums这一种组合
    static final int MOD = 1000000007;
    long[][] combinations;

    public int numOfWays(int[] nums) {
        int n = nums.length;
        if (n == 1) {
            return 0;
        }

        // 预处理 c[i][j]从i个中取j个的组合数 C(n,i) = C(n-1,i)+C(n-1,i-1)
        // 从n个取i个=从n-1个取i个(不取第i个)+从n-1个取i-1个(再加取第i个这1个)
        combinations = new long[n][n];
        combinations[0][0] = 1;
        for (int i = 1; i < n; ++i) {
            combinations[i][0] = 1;
            for (int j = 1; j < n; ++j) {
                combinations[i][j] = (combinations[i - 1][j - 1] + combinations[i - 1][j]) % MOD;
            }
        }

        TreeNode1569 root = new TreeNode1569(nums[0]);
        for (int i = 1; i < n; ++i) {
            int val = nums[i];
            insert(root, val);
        }

        numOfWaysDfs(root);
        return (root.ans - 1 + MOD) % MOD;
    }

    public void insert(TreeNode1569 root, int value) {
        TreeNode1569 cur = root;
        while (true) {
            ++cur.size;
            if (value < cur.value) {
                if (cur.left == null) {
                    cur.left = new TreeNode1569(value);
                    return;
                }
                cur = cur.left;
            } else {
                if (cur.right == null) {
                    cur.right = new TreeNode1569(value);
                    return;
                }
                cur = cur.right;
            }
        }
    }

    public void numOfWaysDfs(TreeNode1569 node) {
        if (node == null) {
            return;
        }
        numOfWaysDfs(node.left);
        numOfWaysDfs(node.right);
        int lsize = node.left != null ? node.left.size : 0;
        int rsize = node.right != null ? node.right.size : 0;
        int lans = node.left != null ? node.left.ans : 1;
        int rans = node.right != null ? node.right.ans : 1;
        node.ans = (int) (combinations[lsize + rsize][lsize] % MOD * lans % MOD * rans % MOD);
    }

    class TreeNode1569 {
        TreeNode1569 left;
        TreeNode1569 right;
        int value;
        int size;
        int ans;

        TreeNode1569(int value) {
            this.value = value;
            this.size = 1;
            this.ans = 0;
        }
    }

    //2867. 统计树中的合法路径数目
    private final static int MX = (int) 1e5;
    private final static boolean[] np = new boolean[MX + 1]; // 质数=false 非质数=true

    static {
        np[1] = true;
        for (int i = 2; i * i <= MX; i++) {
            if (!np[i]) {
                for (int j = i * i; j <= MX; j += i) {
                    np[j] = true;
                }
            }
        }
    }

    public long countPaths(int n, int[][] edges) {
        List<Integer>[] g = new ArrayList[n + 1];
        Arrays.setAll(g, e -> new ArrayList<>());
        for (int[] e : edges) {
            int x = e[0], y = e[1];
            g[x].add(y);
            g[y].add(x);
        }

        long ans = 0;
        int[] size = new int[n + 1];
        List<Integer> nodes = new ArrayList<Integer>();
        for (int x = 1; x <= n; x++) {
            if (np[x]) { // 跳过非质数
                continue;
            }
            int sum = 0;
            for (int y : g[x]) { // 质数 x 把这棵树分成了若干个连通块
                if (!np[y]) {
                    continue;
                }
                if (size[y] == 0) { // 尚未计算过
                    nodes.clear();
                    dfs(y, -1, g, nodes); // 遍历 y 所在连通块，在不经过质数的前提下，统计有多少个非质数
                    for (int z : nodes) {
                        size[z] = nodes.size();
                    }
                }
                // 这 size[y] 个非质数与之前遍历到的 sum 个非质数，两两之间的路径只包含质数 x
                ans += (long) size[y] * sum;
                sum += size[y];
            }
            ans += sum; // 从 x 出发的路径
        }
        return ans;
    }

    private void dfs(int x, int fa, List<Integer>[] g, List<Integer> nodes) {
        nodes.add(x);
        for (int y : g[x]) {
            if (y != fa && np[y]) {
                dfs(y, x, g, nodes);
            }
        }
    }

    //endregion
}
