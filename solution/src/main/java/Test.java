import java.util.*;

public class Test {

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return dfs(map, preorder, inorder, 0, preorder.length - 1, 0, inorder.length - 1);
    }

    private TreeNode dfs(Map<Integer, Integer> map, int[] preorder, int[] inorder, int preorderLeft, int preorderRight, int inorderLeft, int inorderRight) {
        if (preorderRight < preorderLeft) {
            return null;
        }
        int inorderRoot = map.get(preorder[preorderLeft]);
        int leftSize = inorderRoot - inorderLeft;
        TreeNode node = new TreeNode(preorder[preorderLeft]);
        node.left = dfs(map, preorder, inorder, preorderLeft + 1, preorderLeft + leftSize, inorderLeft, inorderRoot - 1);
        node.right = dfs(map, preorder, inorder, preorderLeft + leftSize + 1, preorderRight, inorderRoot + 1, inorderRight);
        return node;
    }

    public int longestSubarray(int[] nums) {
        int n = nums.length;
        int max = nums[0];
        int len = 1;
        for (int l = 0, r = 1; r < n; r++) {
            if ((max & nums[r]) >= max) {
                max = max & nums[r++];
            } else {
                max = Math.max(max, nums[r]);
                l++;
            }
            len = Math.max(len, r - l + 1);
        }
        return len;
    }

    public int longestSubarray2(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        int max = 1;
        int maxNum = nums[0];
        for (int i = 1; i < n; i++) {
            dp[i] = Math.max(dp[i - 1] & nums[i], nums[i]);
            maxNum = Math.max(maxNum, dp[i]);
        }
        int[] len = new int[n];
        for (int i = 0; i < n; i++) {
            if (dp[i] == maxNum) {
                len[i] = i > 0 ? len[i - 1] + 1 : 1;
            }
            max = Math.max(len[i], max);
        }
        return max;
    }

    public List<Integer> goodIndices(int[] nums, int k) {
        int n = nums.length;
        int[] notIncreaseDp = new int[n];
        notIncreaseDp[0] = 1;
        int[] notDecreaseDp = new int[n];
        notDecreaseDp[n - 1] = 1;
        for (int i = 1; i < n; i++) {
            notIncreaseDp[i] = nums[i] <= nums[i - 1] ? notIncreaseDp[i - 1] + 1 : 1;
            notDecreaseDp[n - 1 - i] = nums[n - 1 - i] <= nums[n - i] ? notDecreaseDp[n - i] + 1 : 1;
        }
        List<Integer> result = new ArrayList<>();
        for (int i = k; i < n - k; i++) {
            if (notIncreaseDp[i - 1] >= k && notDecreaseDp[i + 1] >= k) {
                result.add(i);
            }
        }
        return result;
    }

    public String reformatNumber(String number) {
        StringBuilder sb = new StringBuilder();
        for (char c : number.toCharArray()) {
            if (c >= '0' && c <= '9') {
                sb.append(c);
            }
        }
        int n = sb.length();
        if (n < 4) return sb.toString();
        if (n < 5) return sb.substring(0, 2) + "-" + sb.substring(2);
        int remain = n % 3;
        List<String> list = new ArrayList<>();
        if (remain == 1) {
            for (int i = 0; i < n - 4; i += 3) {
                list.add(sb.substring(i, i + 3));
            }
            list.add(sb.substring(n - 4, n - 2));
            list.add(sb.substring(n - 2));
        } else if (remain == 2) {
            for (int i = 0; i < n - 2; i += 3) {
                list.add(sb.substring(i, i + 3));
            }
            list.add(sb.substring(n - 2));
        } else {
            for (int i = 0; i < n; i += 3) {
                list.add(sb.substring(i, i + 3));
            }
        }
        return String.join("-", list);
    }

    public boolean canTransform(String start, String end) {
        int i = 0, j = 0;
        int n = start.length();
        while (i < n && j < n) {
            while (i < n && start.charAt(i) == 'X') {
                i++;
            }
            while (j < n && end.charAt(j) == 'X') {
                j++;
            }
            if (i < n && j < n) {
                if (start.charAt(i) != end.charAt(j)) return false;
                if (start.charAt(i) == 'L' && i < j) return false;
                if (start.charAt(i) == 'R' && i > j) return false;
            }
            i++;
            j++;
        }
        while (i < n) {
            if (start.charAt(i++) != 'X') return false;
        }
        while (j < n) {
            if (end.charAt(j++) != 'X') return false;
        }
        return true;
    }

    public int[] dailyTemperatures(int[] temperatures) {
        Stack<Integer> stack = new Stack<>();
        int n = temperatures.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
                int top = stack.pop();
                ans[top] = i - top;
            }
            stack.push(i);
        }
        return ans;
    }

    public int commonFactors(int a, int b) {
        int ans = 0;
        for (int i = 1; i <= Math.min(a, b); i++) {
            if (a % i == 0 && b % i == 0) {
                ans++;
            }
        }
        return ans;
    }

    public int maxSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] sum = new int[m][n];
        sum[0][0] = grid[0][0];
        for (int i = 1; i < m; i++) {
            sum[i][0] = sum[i - 1][0] + grid[i][0];
        }
        for (int i = 1; i < n; i++) {
            sum[0][i] = sum[0][i - 1] + grid[0][i];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                sum[i][j] = sum[i - 1][j] + sum[i][j - 1] + grid[i][j] - sum[i - 1][j - 1];
            }
        }
        int max = 0;
        for (int i = 2; i < m; i++) {
            for (int j = 2; j < n; j++) {
                int a1 = (i >= 3 ? sum[i - 3][j] : 0);
                int a2 = (j >= 3 ? sum[i][j - 3] : 0);
                int a3 = (i >= 3 && j >= 3) ? sum[i - 3][j - 3] : 0;
                int a = sum[i][j] - a1 - a2 + a3 - grid[i - 1][j] - grid[i - 1][j - 2];
                max = Math.max(max, a);
            }
        }
        return max;
    }

    public int minimizeXor(int num1, int num2) {
        int cnt2 = Integer.bitCount(num2);
        int cnt1 = Integer.bitCount(num1);
        int x = num1;
        if (cnt1 < cnt2) {
            // x最低位补1
            x = ~x;
            int cnt = cnt2 - cnt1;
            while (cnt-- > 0) {
                x = (x & (x - 1));
            }
            return ~x;
        } else if (cnt1 > cnt2) {
            int cnt = cnt1 - cnt2;
            while (cnt-- > 0) {
                x = (x & (x - 1));
            }
            return x;
        } else {
            return num1;
        }
    }

    public int deleteString(String s) {
        Set<Character> set = new HashSet<>();
        for (char c : s.toCharArray()) {
            set.add(c);
        }
        if (set.size() == 1) {
            return s.length();
        }
        Map<String, Integer> memo = new HashMap<>();
        return dfs(s, 0, s.length() - 1, memo);
    }

    private int dfs(String s, int l, int r, Map<String, Integer> memo) {
        if (memo.containsKey(s.substring(l, r + 1))) {
            return memo.get(s.substring(l, r + 1));
        }
        if (l == r) {
            memo.put(s.substring(l, r + 1), 1);
            return 1;
        }
        int len = r - l + 1;
        int max = 1;
        for (int i = l + 1; i <= l + len / 2; i++) {
            String s1 = s.substring(l, i);
            String s2 = s.substring(i, i - l + i);
            if (s1.equals(s2)) {
                max = Math.max(max, 1 + dfs(s, i, r, memo));
            }
        }
        memo.put(s.substring(l, r + 1), max);
        return max;
    }

    public int maximalRectangle(char[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] heights = new int[m][n];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                heights[i][j] = i > 0 ? heights[i - 1][j] + matrix[i][j] - '0' : matrix[i][j] - '0';
            }
        }
        int max = 0;
        for (int i = 0; i < m; i++) {
            Stack<Integer> stack = new Stack<>();
            int[] left = new int[n];
            int[] right = new int[n];
            Arrays.fill(right, n);
            for (int j = 0; j < n; j++) {
                while (!stack.isEmpty() && heights[i][stack.peek()] > heights[i][j]) {
                    right[stack.pop()] = j;
                }
                left[j] = stack.isEmpty() ? -1 : stack.peek();
                stack.push(j);
            }
            for (int j = 0; j < n; j++) {
                int width = right[j] - left[j] - 1;
                max = Math.max(max, width * heights[i][j]);
            }
        }
        return max;
    }

    public int findUnsortedSubarray(int[] nums) {
        int n = nums.length;
        Stack<Integer> stack = new Stack<>();
        int l = n - 1, r = 0;
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] > nums[i]) {
                l = Math.min(l, stack.pop());
            }
            stack.push(i);
        }
        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                r = Math.max(r, stack.pop());
            }
            stack.push(i);
        }
        return r > l ? r - l + 1 : 0;
    }

    public int findUnsortedSubarray2(int[] nums) {
        int n = nums.length;
        int l = n - 1, r = 0;
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if (nums[i] < max) {
                r = i;
            } else {
                max = nums[i];
            }
            if (nums[n - 1 - i] > min) {
                l = n - 1 - i;
            } else {
                min = nums[n - 1 - i];
            }
        }

        return r > l ? r - l + 1 : 0;
    }

    public List<String> subdomainVisits(String[] cpdomains) {
        Map<String, Integer> map = new HashMap<>();
        for (String s : cpdomains) {
            int cnt = Integer.parseInt(s.split(" ")[0]);
            List<String> domains = Arrays.asList(s.split(" ")[1].split("\\."));
            for (int i = 0; i < domains.size(); i++) {
                String domain = String.join(".", domains.subList(i, domains.size()));
                map.put(domain, map.getOrDefault(domain, 0) + cnt);
            }
        }
        List<String> result = new ArrayList<>();
        map.forEach((k, v) -> result.add(v + " " + k));
        return result;
    }

    public int maxArea(int[] height) {
        int n = height.length;
        int l = 0, r = n - 1;
        int max = 0;
        while (l < r) {
            if (height[l] > height[r]) {
                max = Math.max(max, (r - l) * height[r]);
                r--;
            } else {
                max = Math.max(max, (r - l) * height[l]);
                l++;
            }
        }
        return max;
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null) return null;
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode first = head;
        ListNode second = dummy;
        for (int i = 0; i < n; i++) {
            first = first.next;
            if (first == null) return null;
        }
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }


    public int[] threeEqualParts(int[] arr) {
        int sum = Arrays.stream(arr).sum();
        if (sum % 3 != 0) {
            return new int[]{-1, -1};
        }
        if (sum == 0) {
            return new int[]{0, 2};
        }

        int partial = sum / 3;
        int first = 0, second = 0, third = 0, cur = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1) {
                if (cur == 0) {
                    first = i;
                } else if (cur == partial) {
                    second = i;
                } else if (cur == 2 * partial) {
                    third = i;
                }
                cur++;
            }
        }

        int len = arr.length - third;
        if (first + len <= second && second + len <= third) {
            int i = 0;
            while (third + i < arr.length) {
                if (arr[first + i] != arr[second + i] || arr[first + i] != arr[third + i]) {
                    return new int[]{-1, -1};
                }
                i++;
            }
            return new int[]{first + len - 1, second + len};
        }
        return new int[]{-1, -1};
    }

    public int maxAscendingSum(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        int max = dp[0];
        for (int i = 1; i < n; i++) {
            dp[i] = nums[i] > nums[i - 1] ? dp[i - 1] + nums[i] : nums[i];
            max = Math.max(dp[i], max);
        }
        return max;
    }

    public int findDuplicate(int[] nums) {
        int len = nums.length;
        int n = len - 1;
        for (int num : nums) {
            nums[num % len] += n;
        }
        for (int i = 0; i < len; i++) {
            if (nums[i] > 2 * n) return i;
        }
        return -1;
    }

    public int findDuplicate2(int[] nums) {
        int slow = 0, fast = 0;
        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);
        fast = 0;
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }

    public int[] advantageCount(int[] nums1, int[] nums2) {
        int n = nums1.length;
        Integer[] idx2 = new Integer[n];
        for (int i = 0; i < n; i++) {
            idx2[i] = i;
        }
        Arrays.sort(nums1);
        Arrays.sort(idx2, Comparator.comparingInt(o -> nums2[o]));
        int[] ans = new int[n];
        int l = 0, r = n - 1;
        for (int num : nums1) {
            if (num <= nums2[idx2[l]]) {
                ans[idx2[r--]] = num;
            } else {
                ans[idx2[l++]] = num;
            }
        }
        return ans;
    }

    public boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    public ListNode detectCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) break;
        }
        if (fast == null || fast.next == null) return null;
        fast = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    public List<List<Integer>> threeSum(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < n - 2; i++) {
            if (nums[i] > 0) break;
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int j = i + 1, k = n - 1;
            while (j < k) {
                int sum = nums[i] + nums[j] + nums[k];
                if (sum < 0) {
                    while (j < k && nums[j + 1] == nums[j]) {
                        j++;
                    }
                    j++;
                } else if (sum > 0) {
                    while (j < k && nums[k - 1] == nums[k]) {
                        k--;
                    }
                    k--;
                } else {
                    result.add(Arrays.asList(nums[i], nums[j], nums[k]));
                    while (j < k && nums[j + 1] == nums[j]) {
                        j++;
                    }
                    j++;
                    while (j < k && nums[k - 1] == nums[k]) {
                        k--;
                    }
                    k--;
                }
            }
        }
        return result;
    }

    public int numComponents(ListNode head, int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int n : nums) {
            set.add(n);
        }
        ListNode cur = head;
        int ans = 0;
        while (cur != null) {
            if (set.contains(cur.val)) {
                if (cur.next == null || !set.contains(cur.next.val) || cur.val + 1 != cur.next.val) {
                    ans++;
                }
            }
            cur = cur.next;
        }
        return ans;
    }

    public int trap(int[] height) {
        int n = height.length;
        Deque<Integer> deque = new ArrayDeque<>();
        int[] left = new int[n];
        int[] right = new int[n];
        for (int i = 0; i < n; i++) {
            while (!deque.isEmpty() && deque.peekLast() < height[i]) {
                deque.pollLast();
            }
            left[i] = !deque.isEmpty() ? deque.peekFirst() : -1;
            deque.offerLast(height[i]);
        }
        deque.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!deque.isEmpty() && deque.peekLast() < height[i]) {
                deque.pollLast();
            }
            right[i] = !deque.isEmpty() ? deque.peekFirst() : n;
            deque.offerLast(height[i]);
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (left[i] == -1 || right[i] == -1) continue;
            int h = Math.min(left[i], right[i]);
            ans += h - height[i];
        }
        return ans;
    }

    public List<String> buildArray(int[] target, int n) {
        int i = 1;
        int idx = 0;
        List<String> result = new ArrayList<>();
        while (idx < target.length && i <= n) {
            if (target[idx] == i) {
                result.add("Push");
                idx++;
                i++;
            } else if (target[idx] > i) {
                result.add("Push");
                result.add("Pop");
                i++;
            } else {
                break;
            }
        }
        return result;
    }

    public int findTargetSumWays(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        int neg = (sum - target) / 2;
        if (neg < 0 || neg % 2 != 0) return 0;
        int n = nums.length;
        int[][] dp = new int[n + 1][neg + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            int num = nums[i - 1];
            for (int j = 0; j <= neg; j++) {
                dp[i][j] = (j >= num ? dp[i - 1][j - num] : 0) + dp[i - 1][j];
            }
        }
        return dp[n][neg];
    }

    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % 2 != 0) return false;
        int target = sum / 2;
        int n = nums.length;
        int[][] dp = new int[n][target + 1];
        for (int j = 0; j <= target; j++) {
            dp[0][j] = j >= nums[0] ? nums[0] : 0;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= target; j++) {
                dp[i][j] = Math.max((j >= nums[i] ? dp[i - 1][j - nums[i]] + nums[i] : 0), dp[i - 1][j]);
            }
        }
        return dp[n - 1][target] == target;
    }

    public int lastStoneWeightII(int[] stones) {
        int sum = 0;
        for (int stone : stones) {
            sum += stone;
        }
        int target = sum / 2;
        int n = stones.length;
        int[][] dp = new int[n][target + 1];
        for (int j = 0; j <= target; j++) {
            dp[0][j] = j >= stones[0] ? stones[0] : 0;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= target; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], j >= stones[i] ? dp[i - 1][j - stones[i]] + stones[i] : 0);
            }
        }
        return sum - 2 * dp[n - 1][target];
    }

    public int numRollsToTarget(int n, int k, int target) {
        int[][] dp = new int[n + 1][target + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                for (int f = 1; f <= k && j >= f; f++) {
                    dp[i][j] = dp[i - 1][j - f];
                }
            }
        }
        return dp[n][target];
    }

    public int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int coin : coins) {
            for (int i = 1; i <= amount; i++) {
                dp[i] += (i >= coin ? dp[i - coin] : 0);

            }
        }

        return dp[amount];
    }

    public int change2(int amount, int[] coins) {
        int n = coins.length;
        int[][] dp = new int[n + 1][amount + 1];
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i <= n; i++) {
            int coin = coins[i - 1];
            for (int j = 0; j <= amount; j++) {
                dp[i][j] = (j >= coin ? dp[i][j - coin] : 0) + dp[i - 1][j];
            }
        }
        return dp[n][amount];
    }

    public ListNode mergeKLists(ListNode[] lists) {
        return mergeKLists(lists, 0, lists.length - 1);
    }

    private ListNode mergeKLists(ListNode[] lists, int l, int r) {
        if (l > r) return null;
        if (r == l) return lists[l];
        int mid = l + r >> 1;
        return merge(mergeKLists(lists, l, mid), mergeKLists(lists, mid + 1, r));
    }

    private ListNode merge(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null) return null;
        if (l1 == null || l2 == null) return l1 == null ? l2 : l1;
        ListNode dummy = new ListNode();
        ListNode cur = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                cur.next = l1;
                l1 = l1.next;
            } else {
                cur.next = l2;
                l2 = l2.next;
            }
            cur = cur.next;
        }
        cur.next = l1 == null ? l2 : l1;
        return dummy.next;
    }

    public boolean possibleBipartition(int n, int[][] dislikes) {
        if (dislikes.length == 0) return true;
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();
        for (int[] dislike : dislikes) {
            if (set1.contains(dislike[0])) {
                if (set1.contains(dislike[1])) return false;
                set2.add(dislike[1]);
            } else if (set1.contains(dislike[1])) {
                if (set1.contains(dislike[0])) return false;
                set2.add(dislike[0]);
            } else if (set2.contains(dislike[0])) {
                if (set2.contains(dislike[1])) return false;
                set1.add(dislike[1]);
            } else if (set2.contains(dislike[1])) {
                if (set2.contains(dislike[0])) return false;
                set1.add(dislike[0]);
            } else {
                set1.add(dislike[0]);
                set2.add(dislike[1]);
            }
        }
        return true;
    }

    public int findMaxK(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int max = -1;
        for (int num : nums) {
            if (num > 0 && set.contains(-num)) {
                max = Math.max(num, max);
            }
        }
        return max;
    }

    public int countDistinctIntegers(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
            int x = 0;
            while (num != 0) {
                x = x * 10 + num % 10;
                num /= 10;
            }
            set.add(x);
        }
        return set.size();
    }

    public boolean sumOfNumberAndReverse(int num) {
        for (int i = 0; i <= num; i++) {
            if (i + reverse(i) == num) return true;
        }
        return false;
    }

    private int reverse(int x) {
        int k = 0;
        while (x != 0) {
            k = k * 10 + x % 10;
            x /= 10;
        }
        return k;
    }

    public long countSubarrays(int[] nums, int minK, int maxK) {
        int n = nums.length;
        int maxIdx = -1, minIdx = -1;
        int ans = 0;
        for (int l = 0, r = 0; r < n; r++) {
            if (nums[r] == minK) {
                minIdx = r;
            }
            if (nums[r] == maxK) {
                maxIdx = r;
            }
            if (nums[r] < minK || nums[r] > maxK) {
                l = r + 1;
            }
            ans += Math.max(0, Math.min(maxIdx, minIdx) - l + 1);
        }
        return ans;
    }

    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;
        int[][] job = new int[n][3];
        for (int i = 0; i < n; i++) {
            job[i] = new int[]{startTime[i], endTime[i], profit[i]};
        }
        Arrays.sort(job, Comparator.comparingInt(o -> o[1]));
        int[] dp = new int[n + 1];
        dp[0] = 0;
        for (int i = 1; i <= n; i++) {
            dp[i] = Math.max(dp[i - 1], job[i - 1][2]);
            int k = binarySearch(job, i - 1, job[i - 1][0]);
            if (k > 0) dp[i] = Math.max(dp[i], dp[k + 1] + job[i - 1][2]);
        }
        return dp[n];
    }

    private int binarySearch(int[][] job, int right, int target) {
        int l = 0, r = right;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (job[mid][1] > target) {
                r = mid - 1;
            } else {
                l = mid;
            }
        }
        return job[l][1] <= target ? l : -1;
    }

    public int findKthLargest(int[] nums, int k) {
        int n = nums.length;
        int l = 0, r = n - 1;
        int target = n - k;
        while (true) {
            int t = partition(nums, l, r);
            if (t < target) {
                l = t + 1;
            } else if (t > target) {
                r = t - 1;
            } else {
                return nums[t];
            }
        }
    }

    private int partition(int[] nums, int l, int r) {
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

    public boolean haveConflict(String[] event1, String[] event2) {
        return !(compare(event1[1], event2[0]) < 0 || compare(event2[1], event1[0]) < 0);
    }

    private int compare(String s1, String s2) {
        int h1 = Integer.parseInt(s1.substring(0, 2));
        int h2 = Integer.parseInt(s2.substring(0, 2));
        if (h1 < h2) return -1;
        if (h1 > h2) return 1;
        int m1 = Integer.parseInt(s1.substring(3, 5));
        int m2 = Integer.parseInt(s2.substring(3, 5));
        if (m1 < m2) return -1;
        if (m1 > m2) return 1;
        return 0;
    }

    public int subarrayGCD(int[] nums, int k) {
        int n = nums.length;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            int gcd = nums[i];
            for (int j = i; j < n; j++) {
                gcd = gcd(gcd, nums[j]);
                if (gcd == k) ans++;
                else if (gcd < k) break;
            }
        }
        return ans;
    }

    private int gcd(int x, int y) {
        return x % y == 0 ? y : gcd(y, x % y);
    }

    public List<String> letterCasePermutation(String s) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        backtrack(result, sb, s, 0);
        return result;
    }

    private void backtrack(List<String> result, StringBuilder path, String s, int idx) {
        if (idx == s.length()) {
            result.add(path.toString());
            return;
        }
        if (Character.isLowerCase(s.charAt(idx))) {
            path.append(s.charAt(idx));
            backtrack(result, path, s, idx + 1);
            path.deleteCharAt(path.length() - 1);

            path.append(Character.toUpperCase(s.charAt(idx)));
            backtrack(result, path, s, idx + 1);
            path.deleteCharAt(path.length() - 1);
        } else if (Character.isUpperCase(s.charAt(idx))) {
            path.append(s.charAt(idx));
            backtrack(result, path, s, idx + 1);
            path.deleteCharAt(path.length() - 1);

            path.append(Character.toLowerCase(s.charAt(idx)));
            backtrack(result, path, s, idx + 1);
            path.deleteCharAt(path.length() - 1);
        } else {
            path.append(s.charAt(idx));
            backtrack(result, path, s, idx + 1);
            path.deleteCharAt(path.length() - 1);
        }
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        Deque<Integer> path = new ArrayDeque<>();
        backtrack(result, path, candidates, 0, target, candidates.length - 1);
        return result;
    }

    private void backtrack(List<List<Integer>> result, Deque<Integer> path, int[] candidates, int curSum, int target, int idx) {
        if (curSum > target) return;
        if (curSum == target) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = idx; i >= 0; i--) {
            path.addLast(candidates[i]);
            backtrack(result, path, candidates, curSum + candidates[i], target, i);
            path.removeLast();
        }
    }

    public int averageValue(int[] nums) {
        int sum = 0, cnt = 0;
        for (int num : nums) {
            if (num % 6 == 0) {
                sum += num;
                cnt++;
            }
        }
        return cnt == 0 ? 0 : sum / cnt;
    }

    static class Video {
        long totalView;
        List<String[]> ids = new ArrayList<>();

        public String getId() {
            ids.sort((o1, o2) -> {
                if (!o1[1].equals(o2[1])) {
                    return Integer.parseInt(o2[1]) - Integer.parseInt(o1[1]);
                }
                return o1[0].compareTo(o2[0]);
            });
            return ids.get(0)[0];
        }
    }

    public List<List<String>> mostPopularCreator(String[] creators, String[] ids, int[] views) {
        int n = creators.length;
        Map<String, Video> map = new HashMap<>();
        long max = 0;
        for (int i = 0; i < n; i++) {
            Video video = map.getOrDefault(creators[i], new Video());
            video.totalView += views[i];
            video.ids.add(new String[]{ids[i], String.valueOf(views[i])});
            map.put(creators[i], video);
            max = Math.max(video.totalView, max);
        }
        List<List<String>> ans = new ArrayList<>();
        long finalMax = max;
        map.forEach((k, v) -> {
            if (v.totalView == finalMax) {
                List<String> list = new ArrayList<>();
                list.add(k);
                list.add(v.getId());
                ans.add(list);
            }
        });
        return ans;

    }


    public int[] treeQueries(TreeNode root, int[] queries) {
        int n = queries.length;
        int[] ans = new int[n];
        for (int i = 0; i < queries.length; i++) {
            ans[i] = dfs(root, queries[i]) - 1;
        }
        return ans;
    }

    private int dfs(TreeNode root, int query) {
        if (root == null) return 0;
        if (root.left != null && root.left.val == query) {
            return 1 + dfs(root.right, query);
        }
        if (root.right != null && root.right.val == query) {
            return 1 + dfs(root.left, query);
        }
        int left = dfs(root.left, query);
        int right = dfs(root.right, query);
        return 1 + Math.max(left, right);
    }

    // 109 10
    // 110 2
    // 99 101
    // 30
    public long makeIntegerBeautiful(long n, int target) {

        if (getSum(n) <= target) {
            return 0;
        }
        int digit = String.valueOf(n).length();
        long top = (long) Math.pow(10, digit);
        long a = 10, ans = 0;
        while (n < top) {
            long diff = a - n % a;
            n += diff;
            ans += diff;
            if (getSum(n) <= target) return ans;
            a *= 10;
        }
        return -1;
    }

    private int getSum(long n) {
        int sum = 0;
        while (n > 0) {
            sum += n % 10;
            n /= 10;
        }
        return sum;
    }

    public void nextPermutation(int[] nums) {
        int i = nums.length - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }
        if (i >= 0) {
            int j = nums.length - 1;
            if (j > i && nums[j] <= nums[i]) {
                j--;
            }
            swap(nums, i, j);
        }

        reverse(nums, i + 1, nums.length - 1);
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    private void reverse(int[] nums, int i, int j) {
        while (i < j) {
            swap(nums, i++, j--);
        }
    }

    public int[] applyOperations(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                nums[i] *= 2;
                nums[i + 1] = 0;
            }
        }
        int first = 0, second = 0;
        while (second < n) {
            while (first < second && nums[first] != 0) {
                first++;
            }
            if (nums[second] != 0 && first < second) {
                swap(nums, first, second);
            }
            second++;
        }
        return nums;
    }

    public long maximumSubarraySum(int[] nums, int k) {
        int n = nums.length;
        Map<Integer, Integer> map = new HashMap<>();
        long sum = 0, ans = 0;
        for (int l = 0, r = 0; r < n; r++) {
            sum += nums[r];
            map.put(nums[r], map.getOrDefault(nums[r], 0) + 1);
            while (r - l + 1 > k) {
                sum -= nums[l];
                map.put(nums[l], map.get(nums[l]) - 1);
                if (map.get(nums[l]) == 0) {
                    map.remove(nums[l]);
                }
                l++;
            }
            if (r - l + 1 == k && map.size() == k) ans = Math.max(sum, ans);
        }
        return ans;
    }

    public long totalCost(int[] costs, int k, int candidates) {
        int n = costs.length;
        long ans = 0;
        if (n <= 2 * candidates) {
            PriorityQueue<Integer> pq = new PriorityQueue<>();
            for (int c : costs) {
                pq.offer(c);
            }
            while (k-- > 0 && !pq.isEmpty()) {
                ans += pq.poll();
            }
            return ans;
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];
            if (o1[1] != o2[1]) return o1[1] - o2[1];
            return o1[2] - o2[2];
        });
        int l = 0, r = n - 1;
        while (l < candidates) {
            pq.offer(new int[]{costs[l], l, 0});
            l++;
            pq.offer(new int[]{costs[r], r, 1});
            r--;
        }
        while (k-- > 0 && !pq.isEmpty()) {
            int[] tmp = pq.poll();
            ans += tmp[0];
            if (l < r) {
                if (tmp[2] == 0) {
                    pq.offer(new int[]{costs[l], l, 0});
                    l++;
                } else {
                    pq.offer(new int[]{costs[r], r, 1});
                    r--;
                }
            } else if (l == r) {
                pq.offer(new int[]{costs[l], l, 0});
                l++;
            }
        }
        return ans;
    }

    public String interpret(String command) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < command.length(); i++) {
            if (command.charAt(i) == 'G') {
                sb.append('G');
            } else {
                StringBuilder tmp = new StringBuilder();
                while (command.charAt(i) != ')') {
                    sb.append(command.charAt(i));
                    i++;
                }
                if (tmp.length() == 1) sb.append('o');
                if (tmp.length() == 3) sb.append("al");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.totalCost(new int[]{31, 25, 72, 79, 74, 65, 84, 91, 18, 59, 27, 9, 81, 33, 17, 58}, 11, 2);
        test.subdomainVisits(new String[]{"9001 discuss.leetcode.com"});
    }
}
