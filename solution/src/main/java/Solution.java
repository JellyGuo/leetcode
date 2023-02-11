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


    public int deleteGreatestValue(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int cnt = n, ans = 0;
        while (cnt-- > 0) {
            int max = 0;
            for (int i = 0; i < m; i++) {
                int colMax = 0, col = -1;
                for (int j = 0; j < n; j++) {
                    if (!visited[i][j] && grid[i][j] > colMax) {
                        colMax = grid[i][j];
                        col = j;
                    }
                }
                visited[i][col] = true;
                max = Math.max(max, colMax);
            }
            ans += max;
        }
        return ans;
    }

    public int longestSquareStreak(int[] nums) {
        int n = nums.length;
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num * num);
        }
        Arrays.sort(nums);
        int[] dp = new int[n];
        int max = 1;
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            if (!set.contains(nums[i])) continue;
            for (int j = 0; j < i; j++) {
                if (nums[j] * nums[j] == nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                    break;
                }
            }
            max = Math.max(dp[i], max);
        }
        return max == 1 ? -1 : max;
    }

    public int longestSquareStreak2(int[] nums) {
        int n = nums.length;
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        Arrays.sort(nums);
        int max = 1;
        for (int num : nums) {
            int cur = 1;
            int x = num;
            while (set.contains(x * x)) {
                x *= x;
                cur++;
            }
            if (cur > 1) {
                max = Math.max(max, cur);
            }
        }
        return max == 1 ? -1 : max;
    }

    public int minOperations(int[] nums) {
        int ans = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1]) continue;

            ans += nums[i - 1] + 1 - nums[i];
            nums[i] = nums[i - 1] + 1;
        }
        return ans;
    }

    int n;

    public void wiggleSort(int[] nums) {
        this.n = nums.length;
        int k = n >> 1;
        int median = findKthLargest(nums, k);
        threeWayPartition(nums, median);
    }

    private void threeWayPartition(int[] nums, int median) {
        int l = 0, r = nums.length - 1, i = 0;
        // 类似3色问题
        while (i <= r) {
            if (nums[getIdx(i)] > median) {
                // 换完继续判断当前i
                swap(nums, getIdx(r--), getIdx(i));
            } else if (nums[getIdx(i)] < median) {
                // 和当前l一样，同时加1
                // 比l大，此时l指向的一定是median，换完继续往后移
                swap(nums, getIdx(l++), getIdx(i++));
            } else {
                i++;
            }
        }
    }

    public int getIdx(int i) {
        return (1 + 2 * (i)) % (n | 1);
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    private int findKthLargest(int[] nums, int target) {
        int l = 0, r = n - 1;
        while (true) {
            int k = quickPartition(nums, l, r);
            if (k == target) return nums[k];
            if (k < target) {
                l = k + 1;
            } else {
                r = k - 1;
            }
        }
    }

    private int quickPartition(int[] nums, int l, int r) {
        int pivot = nums[l];
        while (l < r) {
            while (l < r && nums[r] >= pivot) {
                r--;
            }
            nums[l] = nums[r];
            while (l < r && nums[l] <= pivot) {
                l++;
            }
            nums[r] = nums[l];
        }
        nums[l] = pivot;
        return l;
    }

    public boolean canChoose(int[][] groups, int[] nums) {
        int i = 0;
        for (int k = 0; i < groups.length && k < nums.length; ) {
            if (check(nums, groups[i], k)) {
                k += groups[i].length;
                i++;
            } else {
                k++;
            }
        }
        return i == groups.length;
    }

    private boolean check(int[] nums, int[] group, int k) {
        if (k + group.length > nums.length) return false;
        for (int j = 0; j < group.length; j++) {
            if (nums[k + j] != group[j]) return false;
        }
        return true;
    }

    public int similarPairs(String[] words) {
        int n = words.length;
        int ans = 0;
        for (int i = 0; i < n - 1; i++) {
            int[] cnt = new int[26];
            for (char c : words[i].toCharArray()) {
                if (cnt[c - 'a'] == 0) {
                    cnt[c - 'a']++;
                }
            }
            for (int j = i + 1; j < n; j++) {
                int[] cnt2 = new int[26];
                for (char cc : words[j].toCharArray()) {
                    if (cnt2[cc - 'a'] == 0) {
                        cnt2[cc - 'a']++;
                    }
                }
                if (check(cnt, cnt2)) {
                    ans++;
                }
            }
        }
        return ans;
    }

    private boolean check(int[] cnt1, int[] cnt2) {
        for (int i = 0; i < 26; i++) {
            if (cnt1[i] != cnt2[i]) return false;
        }
        return true;
    }


    public int smallestValue(int n) {
        int sum = n;
        while (!isPrime(sum)) {
            int cur = sum;
            List<Integer> res = fac(cur);
            sum = 0;
            for (int num : res) {
                sum += num;
            }
            if (cur == sum) return cur;
        }
        return sum;
    }

    private List<Integer> fac(int n) {
        List<Integer> ans = new ArrayList<>();
        if (isPrime(n)) {
            ans.add(n);
            return ans;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (isPrime(i)) {
                while (n % i == 0) {
                    n /= i;
                    ans.add(i);
                    if (isPrime(n)) {
                        ans.add(n);
                        break;
                    }
                }
            }
        }
        return ans;
    }

    private boolean isPrime(int n) {
        if (n == 1) return false;
        // n/i 当i大于sqrt(n)时另一个因子肯定小于sqrt(n),所以只遍历到sqrt(n)
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public int[] shortestSeq(int[] big, int[] small) {
        int n = small.length;
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : small) {
            map.put(num, 1);
        }
        Map<Integer, Integer> map2 = new HashMap<>();
        int min = Integer.MAX_VALUE;
        int idx = -1;
        int meets = 0;
        for (int l = 0, r = 0; r < big.length; r++) {
            if (map.containsKey(big[r])) {
                map2.put(big[r], map2.getOrDefault(big[r], 0) + 1);
                if (map.get(big[r]).intValue() == map2.get(big[r]).intValue()) {
                    meets++;
                }
            }
            while (meets == n) {
                if (r - l + 1 < min) {
                    idx = l;
                    min = r - l + 1;
                }
                if (map.containsKey(big[l])) {
                    map2.put(big[l], map2.get(big[l]) - 1);
                    if (map2.get(big[l]) == 0) {
                        map2.remove(big[l]);
                        meets--;
                    }
                }
                l++;
            }
        }
        return idx == -1 ? new int[0] : new int[]{idx, idx + min - 1};
    }

    public int closetTarget(String[] words, String target, int startIndex) {
        int n = words.length;
        if (words[startIndex].equals(target)) return 0;
        Set<String> dict = new HashSet<>(Arrays.asList(words));
        if (!dict.contains(target)) return -1;
        int dist1 = 1, idx1 = startIndex, dist2 = 1, idx2 = startIndex;
        while (!words[(idx1 + 1) % n].equals(target)) {
            idx1 = (idx1 + 1) % n;
            dist1++;
        }
        while (!words[(idx2 - 1 + n) % n].equals(target)) {
            idx2 = (idx2 - 1 + n) % n;
            dist2++;
        }
        return Math.min(dist1, dist2);
    }


    public int shortestPathLength(int[][] graph) {
        int n = graph.length;
        // 1.初始化队列及标记数组，存入起点
        // 三个属性分别为 idx, mask, dist
        Queue<int[]> queue = new LinkedList<>();
        // 节点编号及当前状态
        // [i,mask] 从i出发已经遍历过mask
        boolean[][] seen = new boolean[n][1 << n];
        for (int i = 0; i < n; ++i) {
            // 把0-n-1全部入队
            queue.offer(new int[]{i, 1 << i, 0});
            seen[i][1 << i] = true;
        }

        int ans = 0;
        while (!queue.isEmpty()) {
            int[] tuple = queue.poll();
            int u = tuple[0], mask = tuple[1], dist = tuple[2];
            // 所有节点依次BFS，哪个mask全部遍历完即是最短
            if (mask == (1 << n) - 1) {
                ans = dist;
                break;
            }
            // 搜索相邻的节点
            for (int v : graph[u]) {
                // 将 mask 的第 v 位置为 1
                int maskV = mask | (1 << v);
                if (!seen[v][maskV]) {
                    queue.offer(new int[]{v, maskV, dist + 1});
                    seen[v][maskV] = true;
                }
            }
        }
        return ans;
    }

    public int shortestPathLength2(int[][] graph) {
        int n = graph.length;
        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] seen = new boolean[n][1 << n];
        for (int i = 0; i < n; i++) {
            queue.offer(new int[]{i, i << i, 0});
            seen[i][1 << i] = true;
        }
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int v = cur[0], mask = cur[1], dist = cur[2];
            if (mask == ((1 << n) - 1)) {
                return dist;
            }
            for (int u : graph[v]) {
                int maskU = mask | (1 << u);
                if (!seen[u][maskU]) {
                    seen[u][maskU] = true;
                    queue.offer(new int[]{u, maskU, dist + 1});
                }
            }
        }
        return 0;
    }

    public int countDigits(int num) {
        int c = num;
        int cnt = 0;
        while (c > 0) {
            int d = c % 10;
            if (num % d == 0) cnt++;
            c /= 10;
        }
        return cnt;
    }

    public int distinctPrimeFactors(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.addAll(fac(num));
        }
        return set.size();
    }


    public int[] closestPrimes(int left, int right) {
        Integer prev = null;
        int min = Integer.MAX_VALUE;
        int[] ans = new int[]{-1, -1};
        for (int i = left; i <= right; i++) {
            if (isPrime(i)) {
                if (prev != null) {
                    if (i - prev < min) {
                        ans[0] = prev;
                        ans[1] = i;
                        min = i - prev;
                    }
                }
                prev = i;
            }
        }
        return ans;
    }

    public int minOperations(int[] nums, int x) {
        int n = nums.length;
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        int t = sum - x;
        if (t < 0) return -1;
        if (t == 0) return n;
        int s = 0, max = 0;
        for (int l = 0, r = 0; r < n; r++) {
            s += nums[r];
            while (s > t) {
                s -= nums[l++];
            }
            if (s == t) max = Math.max(r - l + 1, max);
        }
        return max == 0 ? -1 : n - max;
    }

    public int prefixCount(String[] words, String pref) {
        int cnt = 0;
        for (String word : words) {
            if (isPrefix(word, pref)) cnt++;
        }
        return cnt;
    }

    private boolean isPrefix(String word, String pref) {
        if (pref.length() > word.length()) return false;
        for (int i = 0; i < pref.length(); i++) {
            if (word.charAt(i) != pref.charAt(i)) return false;
        }
        return true;
    }

    public int alternateDigitSum(int n) {
        int size = String.valueOf(n).length();
        boolean flag = size % 2 != 0;
        int ans = 0;
        while (n != 0) {
            ans += (flag ? 1 : -1) * n % 10;
            flag = !flag;
            n /= 10;
        }
        return ans;
    }

    public int[][] sortTheStudents(int[][] score, int k) {
        Arrays.sort(score, (o1, o2) -> o2[k] - o1[k]);
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> o2[k] - o1[k]);
        for (int[] sc : score) {
            pq.offer(sc);
        }
        int[][] ans = new int[score.length][score[0].length];
        int idx = 0;
        while (!pq.isEmpty()) {
            ans[idx++] = pq.poll();
        }
        return ans;
    }

    //00011 01010
    public boolean makeStringsEqual(String s, String target) {
        int n = s.length();
        int idx1 = n - 1, idx2 = n - 1;
        while (idx1 >= 0 && s.charAt(idx1) == '0') {
            idx1--;
        }
        while (idx2 >= 0 && target.charAt(idx2) == '0') {
            idx2--;
        }
        if (idx1 > idx2) return false;
        return true;
    }

    public long pickGifts(int[] gifts, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((o1, o2) -> o2 - o1);
        long sum = 0;
        for (int gift : gifts) {
            pq.offer(gift);
            sum += gift;
        }
        long take = 0;
        while (k-- > 0 && !pq.isEmpty()) {
            int max = pq.poll();
            int remain = (int) Math.sqrt(max);
            take += max - remain;
            pq.offer(remain);
        }
        return sum - take;
    }

    public int[] vowelStrings(String[] words, int[][] queries) {
        int n = words.length;
        int[] prefixSum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            prefixSum[i] = prefixSum[i - 1] + (check(words[i - 1]) ? 1 : 0);
        }
        int[] ans = new int[queries.length];
        int idx = 0;
        for (int[] query : queries) {
            ans[idx++] = prefixSum[query[1] + 1] - prefixSum[query[0]];
        }
        return ans;
    }

    private boolean check(String word) {
        Set<Character> a = new HashSet<>();
        a.add('a');
        a.add('e');
        a.add('i');
        a.add('o');
        a.add('u');
        return a.contains(word.charAt(0)) && a.contains(word.charAt(word.length() - 1));
    }

    //[24,1,55,46,4,61,21,52]
    //3
    // 21
    //[24,109,117,142,98,94,91,130,73,48,107,77]
    //5
    //98
    public int minCapability(int[] nums, int k) {
        int n = nums.length;
        int min = Integer.MAX_VALUE;
        if (k == 1) {
            for (int num : nums) {
                min = Math.min(num, min);
            }
            return min;
        }
        for (int i = 0; i+2*(k-1) < n; i++) {

            int f = 2;
            while (f<n){
                PriorityQueue<Integer> pq = new PriorityQueue<>((o1, o2) -> o2 - o1);
                pq.offer(nums[i]);
                int j = i;
                for (int p = 0; p < k - 2; p++) {
                    j += f;
                    if (j >= n) break;
                    pq.offer(nums[j]);
                }
                j += 2;
                while (j < n) {
                    min = Math.min(Math.max(nums[j++], pq.peek()), min);
                }
                f++;
            }

        }
        return min;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.minCapability(new int[]{24,1,55,46,4,61,21,52},3);
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
