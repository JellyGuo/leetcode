import java.util.*;

public class Solution {
    //    882. 细分图中的可到达节点
    // Dijkstra 求最短路径
    public int reachableNodes(int[][] edges, int maxMoves, int n) {
        int[][] matrix = new int[n][n];
        for (int[] row : matrix) {
            Arrays.fill(row, -1);
        }
        for (int[] edge : edges) {
            matrix[edge[0]][edge[1]] = matrix[edge[1]][edge[0]] = edge[2] + 1;
        }
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(o -> o[1]));
        pq.offer(new int[]{0, 0});
        while (!pq.isEmpty()) {
            int[] tmp = pq.poll();
            int x = tmp[0], d = tmp[1];
            for (int y = 0; y < n; y++) {
                if (y == x || matrix[x][y] == -1) continue;
                int newDist = d + matrix[x][y];
                if (newDist < dist[y]) {
                    dist[y] = newDist;
                    pq.offer(new int[]{y, newDist});
                }
            }
        }
        int ans = 0;
        for (int d : dist) {
            if (d <= maxMoves) ans++;
        }
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], cnt = edge[2];
            int a = Math.max(maxMoves - dist[u], 0);
            int b = Math.max(maxMoves - dist[v], 0);
            ans += Math.min(a + b, cnt);
        }
        return ans;
    }

    // 1752. 检查数组是否经排序和轮转得到
    public boolean check(int[] nums) {
        int t = 0, n = nums.length;
        for (int i = 1; i < n; i++) {
            if (t > 1) return false;
            if (nums[i - 1] > nums[i]) t++;
        }
        return t == 0 || (t == 1 && nums[0] >= nums[n - 1]);
    }

    //6245. 找出中枢整数
    public int pivotInteger(int n) {
        int r = (1 + n) * n / 2;
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
            if (sum == r - sum + i) return i;
        }
        return -1;
    }

    //6246. 追加字符以获得子序列 双指针
    public int appendCharacters(String s, String t) {
        int m = s.length(), n = t.length();
        int idx1 = 0, idx2 = 0;
        while (idx1 < m && idx2 < n) {
            while (idx1 < m && s.charAt(idx1) != t.charAt(idx2)) {
                idx1++;
            }
            if (idx1 == m) return n - idx2;
            idx1++;
            idx2++;
        }
        return n - idx2;
    }

    //6247. 从链表中移除节点 单调队列
    public ListNode removeNodes(ListNode head) {
        Deque<ListNode> deque = new ArrayDeque<>();
        ListNode cur = head;
        while (cur != null) {
            while (!deque.isEmpty() && deque.peekLast().val < cur.val) {
                deque.pollLast();
            }
            deque.push(cur);
            cur = cur.next;
        }
        ListNode dummy = new ListNode();
        ListNode prev = dummy;
        for (ListNode node : deque) {
            prev.next = node;
            prev = prev.next;
        }
        return dummy.next;
    }

    //6248. 统计中位数为 K 的子数组
    //把比 k 大的数变成 1，比 k 小的数变成 -1，k 变成 0。
    //设 k 的下标为 pos，k 为子数组的中位数，等价于：
    //子数组包含下标 pos；
    //子数组的元素和等于 0 或 1。
    //提示 3
    //统计子数组 nums[pos..i] 中比 k 大的数的个数，减去比 k 小的数的个数，记作 ci
    //用哈希表 cnt 统计 ci的个数。
    //然后对于子数组nums[i..pos]，统计比 k 小的数的个数，减去比 k 大的数的个数，记作 ci
    //对于每个 i：cnt[ci] 就是符合提示 2 的奇数长度子数组的个数；
    //cnt[ci+1] 就是符合提示 2 的偶数长度子数组的个数
    public int countSubarrays(int[] nums, int k) {
        int n = nums.length;
        int pos = -1;
        for (int i = 0; i < n; i++) {
            if (nums[i] == k) {
                pos = i;
                break;
            }
        }
        int ans = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        int sum = 0;
        for (int i = pos + 1; i < n; i++) {
            sum += nums[i] > k ? 1 : -1;
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        sum = 0;
        ans += map.get(0) + map.getOrDefault(1, 0);
        for (int i = pos - 1; i >= 0; i--) {
            sum += nums[i] < k ? 1 : -1;
            ans += map.getOrDefault(sum, 0) + map.getOrDefault(sum + 1, 0);
        }
        return ans;
    }

    public boolean isCircularSentence(String sentence) {
        String[] sentences = sentence.split(" ");
        int n = sentences.length;
        for (int i = 0; i < n; i++) {
            int inLen = sentences[i].length();
            if (i == n - 1) {
                if (sentences[i].charAt(inLen - 1) != sentences[0].charAt(0)) {
                    return false;
                }
            } else {
                if (sentences[i].charAt(inLen - 1) != sentences[i + 1].charAt(0)) {
                    return false;
                }
            }
        }
        return true;
    }

    public long dividePlayers(int[] skill) {
        Map<Integer, Integer> map = new HashMap<>();
        int n = skill.length;
        int groupNum = n / 2;
        int sum = 0;
        for (int sk : skill) {
            sum += sk;
            map.put(sk, map.getOrDefault(sk, 0) + 1);
        }
        int groupSum = sum / groupNum;
        if (groupSum * groupNum != sum) return -1;
        long ans = 0;
        for (int sk : skill) {
            if (sk > groupSum) return -1;
            int left = groupSum - sk;
            if (!map.containsKey(left)) return -1;
            map.put(left, map.get(left) - 1);
            if (map.get(left) == 0) {
                map.remove(left);
            }
            ans += (long) left * sk;
        }
        return ans / 2;
    }

    public int minScore(int n, int[][] roads) {
        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, List<Integer>> edges = new HashMap<>();
        for (int[] road : roads) {
            add(dist, edges, road[0], road[1], road[2]);
            add(dist, edges, road[1], road[0], road[2]);
        }
        int min = Integer.MAX_VALUE;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(1);
        boolean[] visited = new boolean[n + 1];
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            for (int near : edges.getOrDefault(cur, new ArrayList<>())) {
                if (visited[near]) continue;
                visited[near] = true;
                queue.offer(near);
                min = Math.min(min, dist.getOrDefault(near, Integer.MAX_VALUE));
            }
        }
        return min;
    }

    private void add(Map<Integer, Integer> dist, Map<Integer, List<Integer>> edges, int x, int y, int d) {
        List<Integer> list = edges.getOrDefault(x, new ArrayList<>());
        list.add(y);
        edges.put(x, list);
        if (!dist.containsKey(x) || (dist.containsKey(x) && dist.get(x) > d)) {
            dist.put(x, d);
        }
    }


    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.dividePlayers(new int[]{3,2,5,1,3,4});
        ListNode l1 = new ListNode(5);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(13);
        ListNode l4 = new ListNode(3);
        ListNode l5 = new ListNode(8);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;
        solution.removeNodes(l1);
    }
}
