import java.util.*;
import java.util.stream.IntStream;

public class SolutionPrefixSum {
    //region --------------------------------------------前缀和-----------------------------------

    // 437 路径总和 任意节点-任意节点
    //前缀和
    public int pathSum(TreeNode root, int targetSum) {
        // key是前缀和, value是大小为key的前缀和出现的次数
        Map<Integer, Integer> map = new HashMap<>();
        // 前缀和为0的一条路径
        map.put(0, 1);
        // 前缀和的递归回溯思路
        return dfs(root, 0, targetSum, map);
    }

    /**
     * 前缀和的递归回溯思路
     * 从当前节点反推到根节点(反推比较好理解，正向其实也只有一条)，有且仅有一条路径，因为这是一棵树
     * 如果此前有和为currSum-target,而当前的和又为currSum,两者的差就肯定为target了
     * 所以前缀和对于当前路径来说是唯一的，当前记录的前缀和，在回溯结束，回到本层时去除，保证其不影响其他分支的结果
     *
     * @param root      树节点
     * @param currSum   前缀和Map
     * @param targetSum 目标值
     * @param currSum   当前路径和
     * @return 满足题意的解
     */
    private int dfs(TreeNode root, int currSum, int targetSum, Map<Integer, Integer> map) {
        // 1.递归终止条件
        if (root == null) {
            return 0;
        }
        // 当前路径上的和
        int sum = root.val + currSum;
        // 2.本层要做的事情
        int ans = 0;
        //---核心代码
        // 看看root到当前节点这条路上是否存在节点前缀和加target为currSum的路径
        // 当前节点->root节点反推，有且仅有一条路径，如果此前有和为currSum-target,而当前的和又为currSum,两者的差就肯定为target了
        // currSum-target相当于找路径的起点，起点的sum+target=currSum，当前点到起点的距离就是target
        if (map.containsKey(sum - targetSum)) ans += map.get(sum - targetSum);
        // 更新路径上当前节点前缀和的个数
        map.put(sum, map.getOrDefault(sum, 0) + 1);
        // 3.进入下一层
        ans += dfs(root.left, sum, targetSum, map);
        ans += dfs(root.right, sum, targetSum, map);
        // 4.回到本层，恢复状态，去除当前节点的前缀和数量
        map.put(sum, map.get(sum) - 1);
        return ans;
    }

    //2588. 重排数组以得到最大前缀分数
    // 竞赛int相加溢出，开long
    public int maxScore(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        long[] sum = new long[n];
        sum[0] = nums[n - 1];
        int ans = sum[0] > 0 ? 1 : 0;
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + nums[n - 1 - i];
            if (sum[i] > 0) ans++;
        }
        return ans;
    }

    public long beautifulSubarrays(int[] nums) {
        long ans = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        int x = 0;
        for (int num : nums) {
            x ^= num;
            // 之前有多少个异或和为x的，就能与当前组成异或和=0的子数组
            ans += map.getOrDefault(x, 0);
            map.put(x, map.getOrDefault(x, 0) + 1);
        }
        return ans;
    }

    //1915. 最美子字符串的数目
    public long wonderfulSubstrings(String word) {
        int[] cnt = new int[1024];
        cnt[0] = 1; // 初始前缀和为 0，需将其计入出现次数
        long ans = 0L;
        for (int i = 0, sum = 0; i < word.length(); ++i) {
            sum ^= 1 << (word.charAt(i) - 'a'); // 计算当前前缀和
            ans += cnt[sum]; // 所有字母均出现偶数次
            for (int j = 1; j < 1024; j <<= 1) // 枚举其中一个字母出现奇数次
                ans += cnt[sum ^ j]; // 反转该字母的出现次数的奇偶性
            ++cnt[sum]; // 更新前缀和出现次数
        }
        return ans;
    }

    // 560 和为K的子数组(和为k的子数组个数)
    // 53 最大子数组和 dp   523 连续子数组和 325 和等于k的最长子数组长度 525 连续数组 560 和为k的子数组
// 前缀和
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        return prefixSum(nums, 0, 0, k, map);
    }

    private int prefixSum(int[] nums, int index, int cur, int k, Map<Integer, Integer> map) {
        if (index >= nums.length) {
            return 0;
        }
        int ans = 0;
        int sum = cur + nums[index];
        if (map.containsKey(sum - k)) ans += map.get(sum - k);
        map.put(sum, map.getOrDefault(sum, 0) + 1);
        ans += prefixSum(nums, index + 1, sum, k, map);
        map.put(sum, map.get(sum) - 1);
        return ans;
    }

    public int subarraySum2(int[] nums, int k) {
        int n = nums.length, ans = 0;
        int[] sum = new int[n];
        sum[0] = nums[0];
        for (int i = 1; i < n; i++) sum[i] = sum[i - 1] + nums[i];
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        for (int i = 0; i < n; i++) {
            int t = sum[i], d = t - k;
            ans += map.getOrDefault(d, 0);
            map.put(t, map.getOrDefault(t, 0) + 1);
        }
        return ans;
    }


    //523 连续子数组和    // 53 最大子数组和 dp 523 连续子数组和 325 和等于k的最长子数组长度 525 连续数组 560 和为k的子数组
    //给你一个整数数组 nums 和一个整数 k ，编写一个函数来判断该数组是否含有同时满足下述条件的连续子数组：子数组大小 至少为 2 ，且子数组元素总和为 k 的倍数。
    //前缀和 同余定理 sum[j]-sum[i-1] = n*k
    public boolean checkSubarraySum(int[] nums, int k) {
        int[] sumArray = new int[nums.length];
        sumArray[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            sumArray[i] = sumArray[i - 1] + nums[i];
            if (sumArray[i] % k == 0) return true;
        }
        Set<Integer> set = new HashSet<>();
        for (int i = 2; i < sumArray.length; i++) {
            //一趟循环，[0,num.lenght-3]每个位置对应的余数都被加入set
            set.add(sumArray[i - 2] % k);
            //当前i位置最近的比较是i-2，sumArray[i-3]..更早的数据在上一轮被加入set
            if (set.contains(sumArray[i] % k)) return true;
        }
        return false;
    }

    public boolean checkSubarraySum2(int[] nums, int k) {
        int m = nums.length;
        if (m < 2) {
            return false;
        }
        Map<Integer, Integer> map = new HashMap<>();
        //余数为0的坐标是-1,这样sum[i]是n*k可以直接计算[0,i]的距离

        map.put(0, -1);
        int remainder = 0;
        for (int i = 0; i < m; i++) {
            remainder = (remainder + nums[i]) % k;
            if (map.containsKey(remainder)) {
                int prevIndex = map.get(remainder);
                // 如果已有remainder,但是长度不够,继续保留原index,这样后面算出的长度才满足最长
                if (i - prevIndex >= 2) {
                    return true;
                }
            } else {
                map.put(remainder, i);
            }
        }
        return false;
    }

    //1171. 从链表中删去总和值为零的连续节点
    public ListNode removeZeroSumSublists(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        Map<Integer, ListNode> seen = new HashMap<>();
        int prefix = 0;
        for (ListNode node = dummy; node != null; node = node.next) {
            prefix += node.val;
            seen.put(prefix, node);
        }
        prefix = 0;
        for (ListNode node = dummy; node != null; node = node.next) {
            prefix += node.val;
            node.next = seen.get(prefix).next;
        }
        return dummy.next;
    }

    //1590. 使数组和能被 P 整除
    public int minSubarray(int[] nums, int p) {
        int n = nums.length;
        int x = 0;
        // 前n个数对p的余数
        for (int num : nums) {
            x = (x + num) % p;
        }
        if (x == 0) return 0;
        // y%p=x,(y-z)%p=0 => z%p=x
        //sum[n]=y,找到区间和%p=x的子数组：(sum[r]-sum[l]=z)%p=x
        //(y-z)%p=x => z%p=(y-x)%p
        // sum[l]%p = (sum[r]-x)%p
        Map<Integer, Integer> map = new HashMap<>();
        int y = 0;
        int min = n;
        for (int i = 0; i < n; i++) {
            map.put(y, i);
            y = (y + nums[i]) % p;
            int t = (y - x + p) % p;
            if (map.containsKey(t)) {
                min = Math.min(min, i - map.get(t) + 1);
            }
        }
        return min == n ? -1 : min;
    }

    // 325 和等于k的最长子数组长度
    //// 53 最大子数组和 dp 523 连续子数组和 325 和等于k的最长子数组长度 525 连续数组 560 和为k的子数组
    //给定一个数组 nums 和一个目标值 k，找到和等于 k 的最长连续子数组长度。如果不存在任意一个符合要求的子数组，则返回 0。
//输入: nums = [1,-1,5,-2,3], k = 3输出: 4解释: 子数组 [1, -1, 5, -2] 和等于 3，且长度最长。
    //输入: nums = [-2,-1,2,1], k = 1输出: 2 解释: 子数组 [-1, 2] 和等于 1，且长度最长。
// 1 <= nums.length <= 2 * 105
// -104 <= nums[i] <= 104
// -109 <= k <= 109
    public int maxSubArrayLen(int[] nums, int k) {
        int n = nums.length;
        int[] sum = new int[n];
        sum[0] = nums[0];
        int max = 0;
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + nums[i];
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        for (int i = 0; i < n; i++) {
            if (map.containsKey(sum[i] - k)) {
                max = Math.max(max, i - map.get(sum[i] - k));
            }
            if (!map.containsKey(sum[i])) {
                map.put(sum[i], i);
            }
        }
        return max;
    }

    //525 连续数组 (和为k的最大子数组长度，k=0)
    //// 53 最大子数组和 dp 523 连续子数组和 325 和等于k的最长子数组长度 525 连续数组 560 和为k的子数组
    public int findMaxLength(int[] nums) {
        int n = nums.length;
        int[] sum = new int[n];
        sum[0] = (nums[0] == 0 ? -1 : 1);
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + (nums[i] == 0 ? -1 : 1);
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int len = 0;
        for (int i = 0; i < n; i++) {
            if (map.containsKey(sum[i])) {
                // ifelse不更新map,目的是保留前一个idx
                len = Math.max(len, i - map.get(sum[i]));
            } else {
                map.put(sum[i], i);
            }
        }
        return len;
    }

    //面试题 17.05.  字母与数字
    public String[] findLongestSubarray(String[] array) {
        int n = array.length;
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            char c = array[i].charAt(0);
            if (Character.isDigit(c)) {
                nums[i] = 1;
            } else {
                nums[i] = -1;
            }
        }
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + nums[i - 1];
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 0);
        int len = 0, l = 0;
        for (int i = 1; i <= n; i++) {
            if (map.containsKey(sum[i])) {
                if (i - map.get(sum[i]) > len) {
                    l = map.get(sum[i]);
                    len = i - map.get(sum[i]);
                }
            } else {
                map.put(sum[i], i);
            }
        }
        String[] ans = new String[len];
        System.arraycopy(array, l, ans, 0, len);
        return ans;
    }

    // 1248 统计优美子数组
//给你一个整数数组 nums 和一个整数 k。如果某个连续子数组中恰好有 k 个奇数数字，我们就认为这个子数组是「优美子数组」。
// 请返回这个数组中 「优美子数组」 的数目。
//输入：nums = [1,1,2,1,1], k = 3
//输出：2
//解释：包含 3 个奇数的子数组是 [1,1,2,1] 和 [1,2,1,1] 。
    public int numberOfSubarrays(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        return numberOfSubarraysDFS(nums, 0, 0, k, map);
    }

    private int numberOfSubarraysDFS(int[] nums, int index, int currCount, int k, Map<Integer, Integer> map) {
        if (index >= nums.length) {
            return 0;
        }
        int ans = 0;
        int sumCount = currCount + (nums[index] % 2 == 0 ? 0 : 1);
        if (map.containsKey(sumCount - k)) ans += map.get(sumCount - k);
        map.put(sumCount, map.getOrDefault(sumCount, 0) + 1);
        ans += numberOfSubarraysDFS(nums, index + 1, sumCount, k, map);
        map.put(sumCount, map.get(sumCount) - 1);
        return ans;
    }

    public int numberOfSubarraysMath(int[] nums, int k) {
        int n = nums.length;
        int[] odd = new int[n + 2];
        int ans = 0, cnt = 0;
        for (int i = 0; i < n; ++i) {
            if ((nums[i] & 1) != 0) {
                odd[++cnt] = i;
            }
        }
        odd[0] = -1;
        odd[++cnt] = n;
        //第i个奇数 和第i+k-1个奇数之间有k个奇数
        // 第i个前面有odd[i] - odd[i - 1]个偶数，第i+k-1 后面有odd[i + k] - odd[i + k - 1]个偶数
        for (int i = 1; i + k <= cnt; ++i) {
            ans += (odd[i] - odd[i - 1]) * (odd[i + k] - odd[i + k - 1]);
        }
        return ans;
    }

    public int numberOfSubarraysPrefixSum1(int[] nums, int k) {
        int n = nums.length;
        int[] odd = new int[n];
        for (int i = 0; i < n; i++) {
            if ((nums[i] & 1) == 1) odd[i] = 1;
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + odd[i - 1];
            map.put(sum[i], map.getOrDefault(sum[i], 0) + 1);
        }
        int cnt = 0;
        for (int i = 1; i <= n; i++) {
            cnt += map.getOrDefault(sum[i] - k, 0);
        }
        return cnt;
    }

    public int numberOfSubarraysPrefixSum2(int[] nums, int k) {
        int n = nums.length;
        int[] cnt = new int[n + 1];
        int odd = 0, ans = 0;
        cnt[0] = 1;
        for (int num : nums) {
            odd += num & 1;
            ans += odd >= k ? cnt[odd - k] : 0;
            cnt[odd] += 1;
        }
        return ans;
    }

    //2389. 和有限的最长子序列 前缀和+二分
    public int[] answerQueries(int[] nums, int[] queries) {
        Arrays.sort(nums);
        int n = nums.length;
        int m = queries.length;
        int[] sum = new int[n];
        sum[0] = nums[0];
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + nums[i];
        }
        int[] ans = new int[m];
        for (int i = 0; i < m; i++) {
            int idx = binarySearch(sum, queries[i]);
            ans[i] = sum[idx] <= queries[i] ? idx + 1 : 0;
        }
        return ans;
    }

    private int binarySearch(int[] sum, int x) {
        int l = 0, r = sum.length - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (sum[mid] <= x) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return l;
    }

    //面试题 17.24. 最大子矩阵   53的二维拓展
    public int[] getMaxMatrix(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[] ans = new int[4];
        int[] sum = new int[n];
        int maxAns = matrix[0][0];
        for (int i = 0; i < m; i++) {
            Arrays.fill(sum, 0);
            for (int j = i; j < m; j++) {
                int maxSum = 0, start = -1;
                for (int k = 0; k < n; k++) {
                    sum[k] += matrix[j][k];
                    if (maxSum > 0) {
                        maxSum += sum[k];
                    } else {
                        maxSum = sum[k];
                        start = k;
                    }
                    if (maxSum > maxAns) {
                        ans[0] = i;
                        ans[1] = start;
                        ans[2] = j;
                        ans[3] = k;
                        maxAns = maxSum;
                    }
                }
            }
        }
        return ans;
    }

    public int[] getMaxMatrix2(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[] ans = new int[4];
        int[][] prefixSum = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                prefixSum[i][j] = prefixSum[i][j - 1] + prefixSum[i - 1][j] - prefixSum[i - 1][j - 1] + matrix[i - 1][j - 1];
            }
        }
        int globalMax = Integer.MIN_VALUE;
        for (int top = 1; top <= m; top++) {
            for (int bottom = top; bottom <= m; bottom++) {
                int localMax, left = 1;
                for (int right = 1; right <= n; right++) {
                    localMax = prefixSum[bottom][right] - prefixSum[bottom][left - 1] - prefixSum[top - 1][right] + prefixSum[top - 1][left - 1];
                    if (localMax > globalMax) {
                        ans[0] = top - 1;
                        ans[1] = left - 1;
                        ans[2] = bottom - 1;
                        ans[3] = right - 1;
                        globalMax = localMax;
                    }
                    if (localMax < 0) {
                        localMax = 0;
                        left = right + 1;
                    }
                }
            }
        }
        return ans;
    }

    // 1 两数之和
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) return new int[]{map.get(target - nums[i]), i};
            map.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    // 167 两数之和
    //排序后两数之和
    // 二分
    public int[] twoSumBinarySearch(int[] numbers, int target) {
        for (int i = 0; i < numbers.length; i++) {
            int l = i + 1, r = numbers.length - 1;
            while (l < r) {
                int mid = (l + r) / 2;
                if (numbers[mid] < target - numbers[i]) {
                    l = mid + 1;
                } else {
                    r = mid;
                }
            }
            if (numbers[i] + numbers[l] == target) return new int[]{i + 1, l + 1};
        }
        return new int[0];
    }

    //双指针
    public int[] twoSumSorted(int[] nums, int target) {
        int i = 0, j = nums.length - 1;
        while (i < j) {
            if (nums[i] + nums[j] < target) {
                i++;
            } else if (nums[i] + nums[j] > target) {
                j--;
            } else {
                return new int[]{nums[i], nums[j]};
            }
        }
        return new int[0];
    }

    //1010. 总持续时间可被 60 整除的歌曲
    public int numPairsDivisibleBy60(int[] time) {
        int ans = 0;
        int[] cnt = new int[60];
        for (int t : time) {
            ans += cnt[(60 - t % 60) % 60];
            cnt[t % 60]++;
        }
        return ans;
    }

    //面试题 16.21. 交换和
    public int[] findSwapValues(int[] array1, int[] array2) {
        int sum1 = 0, sum2 = 0;
        Set<Integer> set = new HashSet<>();
        for (int num : array1) {
            sum1 += num;
        }
        for (int num : array2) {
            sum2 += num;
            set.add(num);
        }
        int diff = sum1 - sum2;
        if (diff % 2 != 0) return new int[0];
        diff /= 2;
        for (int num : array1) {
            if (set.contains(num - diff)) return new int[]{num, num - diff};
        }
        return new int[0];
    }

    //653 BST两数之和
    public boolean findTarget2(TreeNode root, int k) {
        List<Integer> list = new ArrayList<>();
        inorder(root, list);
        int[] result = twoSumSorted(list.stream().mapToInt(p -> p).toArray(), k);
        return result.length == 2;
    }
    public void inorder(TreeNode node, List<Integer> vals) {
        if (node == null) return;
        inorder(node.left, vals);
        vals.add(node.val);
        inorder(node.right, vals);
    }

    // 迭代做法
    public boolean findTarget3(TreeNode root, int k) {
        Stack<TreeNode> lstack = new Stack<>(), rstack = new Stack<>();
        TreeNode temp = root;
        while (temp != null) {
            lstack.push(temp);
            temp = temp.left;
        }
        temp = root;
        while (temp != null) {
            rstack.push(temp);
            temp = temp.right;
        }
        TreeNode l = lstack.peek(), r = rstack.peek();
        while (l.val < r.val) {
            int t = l.val + r.val;
            if (t == k) return true;
            if (t < k) l = getNext(lstack, true);
            else r = getNext(rstack, false);
        }
        return false;
    }

    TreeNode getNext(Stack<TreeNode> d, boolean isLeft) {
        TreeNode node = isLeft ? d.pop().right : d.pop().left;
        while (node != null) {
            d.push(node);
            node = isLeft ? node.left : node.right;
        }
        return d.peek();
    }

    //494 目标和
    //给你一个整数数组 nums 和一个整数 target 。
// 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：
// 例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，然后串联起来得到表达式 "+2-1" 。
// 返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。
//输入：nums = [1,1,1,1,1], target = 3
//输出：5
//解释：一共有 5 种方法让最终目标和为 3 。
//-1 + 1 + 1 + 1 + 1 = 3
//+1 - 1 + 1 + 1 + 1 = 3
//+1 + 1 - 1 + 1 + 1 = 3
//+1 + 1 + 1 - 1 + 1 = 3
//+1 + 1 + 1 + 1 - 1 = 3
    // 背包dp findTargetSumWaysDP
    public int findTargetSumWaysDFS(int[] nums, int target) {
        Map<String, Integer> map = new HashMap<>();
        return dfs(nums, 0, 0, target, map);
    }

    private int dfs(int[] nums, int depth, int currSum, int target, Map<String, Integer> map) {
        if (map.containsKey(depth + "_" + currSum)) return map.get(depth + "_" + currSum);
        if (depth == nums.length) {
            map.put(depth + "_" + currSum, currSum == target ? 1 : 0);
            return currSum == target ? 1 : 0;
        }
        int ans = 0;
        ans += dfs(nums, depth + 1, nums[depth] + currSum, target, map);
        ans += dfs(nums, depth + 1, currSum - nums[depth], target, map);
        map.put(depth + "_" + currSum, ans);
        return ans;
    }


    //825 适龄的朋友
    //从中间向两边扩散
    public int numFriendRequests(int[] ages) {
        Arrays.sort(ages);
        int ans = 0;
        for (int i = 0; i < ages.length; i++) {
            int l = i;
            while (l >= 0 && relationships(ages[l], ages[i])) l--;
            int r = i;
            while (r < ages.length && relationships(ages[r], ages[i])) r++;
            if ((r - 1) > (l + 1)) ans += (r - 1) - (l + 1);
        }
        return ans;
    }

    private boolean relationships(int x, int y) {
        if (y <= 0.5 * x + 7) return false;
        if (y > x) return false;
        if (y > 100 && x < 100) return false;
        return true;
    }

    //桶排序+前缀和
    public int numFriendRequests1(int[] ages) {
        int[] nums = new int[121];
        for (int age : ages) {
            nums[age]++;
        }
        for (int i = 1; i <= 120; i++) {
            nums[i] += nums[i - 1];
        }
        int ans = 0;
        for (int i = 1; i <= 120; i++) {
            int a = nums[i] - nums[i - 1];
            if (a == 0) continue;
            int j = i;
            while (j <= 120 && relationships(j, i)) j++;
            //为什么减y-1？因为是前缀和，这样子就统计了y位置的个数，y+1..x-1的个数，总共的个数减去1，
            // 是对每个y而言减去他本身得到的个数是每个y可以和他一样年龄的人交朋友的个数+y+1..那些个数，
            // 每个y可以交朋友的个数b乘以y的个数a，就是当前i（这里是下标y）位置，最后遍历i在N的范围
            int b = nums[j - 1] - nums[i - 1] - 1;
            if (b > 0) ans += b * a;
        }
        return ans;
    }

    //offer 66 构建乘积数组
    //给定一个数组 A[0,1,…,n-1]，请构建一个数组 B[0,1,…,n-1]，其中 B[i] 的值是数组 A 中除了下标 i 以外的元素的积, 即 B[
//i]=A[0]×A[1]×…×A[i-1]×A[i+1]×…×A[n-1]。不能使用除法。
//输入: [1,2,3,4,5]
//输出: [120,60,40,30,24]
//
    public int[] constructArr(int[] a) {
        if (a.length <= 0) return new int[0];
        int[] answers = new int[a.length];
        answers[0] = 1;// i 左边元素乘积
        for (int i = 1; i < a.length; i++) {
            answers[i] = a[i - 1] * answers[i - 1]; //i-1 乘以 i-1左边
        }
        int R = 1;
        for (int i = a.length - 1; i >= 0; i--) {
            answers[i] = answers[i] * R;
            R *= a[i];
        }
        return answers;
    }

    // 1413 逐步求和得到正数的最小值
    public int minStartValue(int[] nums) {
        int n = nums.length;
        int[] sum = new int[n + 1];
        int min = Integer.MIN_VALUE;
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + nums[i - 1];
            min = Math.max(min, Math.max(1, 1 - sum[i]));
        }
        return min;
    }

    //724 寻找数组的中心下标
    // 数组 中心下标 是数组的一个下标，其左侧所有元素相加的和等于右侧所有元素相加的和。 nums = [1, 7, 3, 6, 5, 6] 3
    public int pivotIndex(int[] nums) {
        int n = nums.length;
        int[] sum = new int[n];
        sum[0] = nums[0];
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + nums[i];
        }
        for (int i = 0; i < n; i++) {
            int leftSum = i == 0 ? 0 : sum[i - 1];
            int rightSum = i == n - 1 ? 0 : (sum[n - 1] - sum[i]);
            if (leftSum == rightSum) return i;
        }
        return -1;
    }

    // 1422 分割字符串的最大得分
    public int maxScore(String s) {
        int n = s.length();
        int[] sum = new int[n];
        char[] chars = s.toCharArray();
        sum[0] = chars[0] - '0';
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + chars[i] - '0';
        }
        int max = 0;
        for (int i = 0; i < n - 1; i++) {
            int left = i + 1 - sum[i];
            int right = sum[n - 1] - sum[i];
            max = Math.max(left + right, max);
        }
        return max;
    }

    // 1769 移动所有球到每个盒子所需要的最小操作数
    public int[] minOperations(String boxes) {
        int n = boxes.length();
        int left = boxes.charAt(0) - '0', right = 0, operation = 0;
        for (int i = 1; i < n; i++) {
            if (boxes.charAt(i) == '1') {
                right++;
                operation += i;
            }
        }
        int[] ans = new int[n];
        ans[0] = operation;
        for (int i = 1; i < n; i++) {
            operation += left - right;
            ans[i] = operation;
            if (boxes.charAt(i) == '1') {
                left++;
                right--;
            }
        }
        return ans;
    }

    //1893 检查是否区域内的所有整数都被覆盖
//给你一个二维整数数组 ranges 和两个整数 left 和 right 。每个 ranges[i] = [starti, endi] 表示一个从 starti 到 endi 的 闭区间 。
// 如果闭区间 [left, right] 内每个整数都被 ranges 中 至少一个 区间覆盖，那么请你返回 true ，否则返回 false 。
// 已知区间 ranges[i] = [starti, endi] ，如果整数 x 满足 starti <= x <= endi ，那么我们称整数x 被覆盖了
//输入：ranges = [[1,2],[3,4],[5,6]], left = 2, right = 5
//输出：true
    public boolean isCovered(int[][] ranges, int left, int right) {
        boolean result = true;
        for (int i = left; i <= right; i++) {
            boolean istatus = false;
            for (int[] range : ranges) {
                if (i >= range[0] && i <= range[1]) {
                    istatus = true;
                    break;
                }
            }
            result = result & istatus;
        }
        return result;
    }

    // 差分+前缀和
    public boolean isCoveredPrefixSum(int[][] ranges, int left, int right) {
        int[] diff = new int[52];
        //对差分数组进行处理
        for (int[] range : ranges) {
            diff[range[0]]++;
            diff[range[1] + 1]--;
        }
        //根据差分数组处理前缀和，为理解方便单独定义sum，可以原地做
        int[] sum = new int[52];
        for (int i = 1; i <= 51; i++) {
            sum[i] = sum[i - 1] + diff[i];
        }
        //从left到right判断是否满足sum > 0
        for (int i = left; i <= right; i++) {
            if (sum[i] <= 0) return false;
        }
        return true;
    }

    public boolean isCovered2(int[][] ranges, int left, int right) {
        int[] diff = new int[52];   // 差分数组
        for (int[] range : ranges) {
            ++diff[range[0]];
            --diff[range[1] + 1];
        }
        // 前缀和
        int curr = 0;
        for (int i = 1; i <= 50; ++i) {
            curr += diff[i];
            if (i >= left && i <= right && curr <= 0) {
                return false;
            }
        }
        return true;
    }

    // 1588 所有奇数长度子数组的和
    public int sumOddLengthSubarrays(int[] arr) {
        int n = arr.length;
        int[] sum = new int[n];
        sum[0] = arr[0];
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }
        int ans = 0;
        for (int len = 1; len <= n; len += 2) {
            for (int l = 0; l + len - 1 < n; l++) {
                int r = l + len - 1;
                ans += sum[r] - (l > 0 ? sum[l - 1] : 0);
            }
        }
        return ans;
    }

    // 数学
    public int sumOddLengthSubarraysMath(int[] arr) {
        int sum = 0;
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int leftCount = i, rightCount = n - i - 1;
            int leftOdd = (leftCount + 1) / 2;
            int rightOdd = (rightCount + 1) / 2;
            int leftEven = leftCount / 2 + 1;//+1是0这种情况
            int rightEven = rightCount / 2 + 1;
            sum += arr[i] * (leftOdd * rightOdd + leftEven * rightEven);
        }
        return sum;
    }

    //1177. 构建回文串检测
    public List<Boolean> canMakePaliQueries(String s, int[][] queries) {
        List<Boolean> ans = new ArrayList<>();
        int n = s.length();
        int[][] sum = new int[n + 1][26];
        char[] chars = s.toCharArray();
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i].clone();
            sum[i + 1][chars[i] - 'a']++;
        }
        for (int[] query : queries) {
            int l = query[0], r = query[1], k = query[2];
            int m = 0;
            for (int i = 0; i < 26; i++) {
                m += ((sum[r + 1][i] - sum[l][i]) % 2);
            }
            ans.add((m / 2) <= k);
        }
        return ans;
    }

    //1737. 满足三条件之一需改变的最少字符数 字符前缀和
    public int minCharacters(String a, String b) {
        int m = a.length(), n = b.length();
        int[] c1 = new int[26], c2 = new int[26];
        for (int i = 0; i < m; i++) {
            c1[a.charAt(i) - 'a']++;
        }
        for (int i = 0; i < n; i++) {
            c2[b.charAt(i) - 'a']++;
        }
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < 26 && ans != 0; i++) {
            int same = m - c1[i] + n - c2[i];
            ans = Math.min(ans, same);
            if (i == 0) continue;//没法变成严格小于的情况
            int r1 = 0, r2 = 0;
            for (int j = i; j < 26; j++) r1 += c1[j];// a 的[i,m-1]全变成小于i的字符
            for (int j = 0; j < i; j++) r1 += c2[j];// b的[0,i)全变成大于等于i的字符
            for (int j = i; j < 26; j++) r2 += c2[j];
            for (int j = 0; j < i; j++) r2 += c1[j];
            ans = Math.min(ans, Math.min(r1, r2));
        }
        return ans;
    }

    public int minCharacters2(String a, String b) {
        int m = a.length(), n = b.length();
        char[] a_chars = new char[26];
        char[] b_chars = new char[26];
        for (char c : a.toCharArray()) a_chars[c - 'a']++;
        for (char c : b.toCharArray()) b_chars[c - 'a']++;
        int min = Integer.MAX_VALUE;
        int asum = 0, bsum = 0;
        for (int i = 0; i < 26; i++) {
            asum += a_chars[i];
            bsum += b_chars[i];
            int same = m - a_chars[i] + n - b_chars[i];
            //i=25 就是字母z
            int alessb = i == 25 ? m + n : m - asum + bsum;//m-asum (i,m-1]的字符全变成i或i前面的字符，bsum [0,i]的字符全变成i后面的字符
            int blessa = i == 25 ? m + n : n - bsum + asum;
            min = Math.min(Math.min(alessb, blessa), Math.min(same, min));
        }
        return min;
    }

    // 1744 吃糖果
    // 第favoriteDayi天，最少需要吃（favoriteDayi+1）*1，最多（favoriteDayi+1）*dailyCapi
    //对于第favoriteTypei个，吃到它需要在第[sum[typei-1]+1,sum[typei]]之中，求两个集合是否有交集，有交集就可以吃到
    public boolean[] canEat(int[] candiesCount, int[][] queries) {
        long[] sum = new long[candiesCount.length];
        sum[0] = candiesCount[0];
        for (int i = 1; i < candiesCount.length; i++) {
            sum[i] = sum[i - 1] + candiesCount[i];
        }
        boolean[] result = new boolean[queries.length];
        for (int i = 0; i < queries.length; i++) {
            long min_to_eat = queries[i][1] + 1;
            long max_to_eat = (long) (queries[i][1] + 1) * queries[i][2];
            int type = queries[i][0];
            long range_left = type == 0 ? 1 : (sum[type - 1] + 1);
            long range_right = sum[type];
            result[i] = max_to_eat >= range_left && min_to_eat <= range_right;
        }
        return result;
    }

    // 2055 蜡烛之间的盘子
    // 预处理+前缀和
    public int[] platesBetweenCandles(String s, int[][] queries) {
        int n = s.length();
        int[] preSum = new int[n];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '*') {
                sum++;
            }
            preSum[i] = sum;
        }
        int[] left = new int[n];
        for (int i = 0, l = -1; i < n; i++) {
            if (s.charAt(i) == '|') {
                l = i;
            }
            left[i] = l;
        }
        int[] right = new int[n];
        for (int i = n - 1, r = -1; i >= 0; i--) {
            if (s.charAt(i) == '|') {
                r = i;
            }
            right[i] = r;
        }
        int[] ans = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int a = queries[i][0], b = queries[i][1];
            int c = right[a], d = left[b];
            // [a,b] 内求[c,d] a右侧第一个蜡烛c，b左侧第一个蜡烛d
            // preSum[d] - preSum[c-1] c是蜡烛 所以也可以表示成 preSum[d] - preSum[c]
            ans[i] = c == -1 || d == -1 || c > d ? 0 : preSum[d] - preSum[c];
        }
        return ans;
    }

    // 1524 和为奇数的子数组数目 toreview
    public int numOfSubarrays(int[] arr) {
        final int MODULO = 1000000007;
        int odd = 0, even = 1;
        int subarrays = 0;
        int sum = 0;
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            sum += arr[i];
            //[0,i]为偶,如果[0,j]多少个奇数和,[j+1,i]就多少个奇数和,以i结尾就+odd
            subarrays = (subarrays + (sum % 2 == 0 ? odd : even)) % MODULO;
            if (sum % 2 == 0) {
                even++;
            } else {
                odd++;
            }
        }
        return subarrays;
    }

    //2488. 统计中位数为 K 的子数组
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

    //2559. 统计范围内的元音字符串数
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

    //2602. 使数组元素全部相等的最少操作次数
    public List<Long> minOperationsTLE(int[] nums, int[] queries) {
        List<Long> ans = new ArrayList<>();
        for (int query : queries) {
            long res = 0;
            for (int num : nums) {
                res += Math.abs(num - query);
            }
            ans.add(res);
        }
        return ans;
    }

    public List<Long> minOperations(int[] nums, int[] queries) {
        Arrays.sort(nums);
        List<Long> ans = new ArrayList<>();
        int n = nums.length;
        long[] sum = new long[n];
        sum[0] = nums[0];
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + nums[i];
        }
        for (int query : queries) {
            if (query < nums[0]) {
                ans.add(sum[n - 1] - (long) n * query);
                continue;
            }
            int l = 0, r = n - 1;
            while (l < r) {
                int mid = l + r + 1 >> 1;
                if (nums[mid] <= query) {
                    l = mid;
                } else {
                    r = mid - 1;
                }
            }
            long leftSize = l + 1, rightSize = n - l - 1;
            long res = leftSize * query - sum[l] + (sum[n - 1] - sum[l] - rightSize * query);
            ans.add(res);
        }
        return ans;
    }

    //2615. 等值距离和
    public long[] distance(int[] nums) {
        int n = nums.length;
        Map<Integer, List<Integer>> map = new HashMap<>();
        long[] ans = new long[n];
        for (int i = 0; i < n; i++) {
            List<Integer> ls = map.getOrDefault(nums[i], new ArrayList<>());
            ls.add(i);
            map.put(nums[i], ls);
        }
        for (List<Integer> ls : map.values()) {
            int m = ls.size();
            long[] sum = new long[m + 1];
            for (int i = 1; i <= m; i++) {
                sum[i] = sum[i - 1] + ls.get(i - 1);
            }
            for (int i = 0; i < m; i++) {
                int cur = ls.get(i);
                // cur-ls[0]+cur-ls[1]...+cur-ls[i-1]
                int prevCnt = i;
                long leftSum = prevCnt * (long) cur - sum[i];
                // ls[i+1]-cur+..ls[m-1]-cur
                int afterCnt = m - 1 - (i + 1) + 1;
                long rightSum = sum[m] - sum[i + 1] - afterCnt * (long) cur;
                ans[cur] = leftSum + rightSum;
            }
        }
        return ans;
    }

    //2640. 一个数组所有前缀的分数
    public long[] findPrefixScore(int[] nums) {
        int n = nums.length;
        int max = 0;
        long sum = 0;
        long[] ans = new long[n];
        for (int i = 0; i < n; i++) {
            max = Math.max(max, nums[i]);
            sum += (max + nums[i]);
            ans[i] = sum;
        }
        return ans;
    }
    // endregion ---------------------------------------------------------------------------------------------------


    //region---------------------------------------------------差分---------------------------------------

    //1094 拼车
    public boolean carPooling(int[][] trips, int capacity) {
        int[] diff = new int[1010];
        for (int[] trip : trips) {
            diff[trip[1]] += trip[0];
            diff[trip[2]] -= trip[0];
        }
        int sum = 0;
        for (int value : diff) {
            sum += value;
            if (sum > capacity) return false;
        }
        return true;
    }

    // 252 会议室
    public boolean canAttendMeetings(int[][] intervals) {
        int n = intervals.length;
        int count = 0;
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int[] tmp : intervals) {
            map.put(tmp[0], map.getOrDefault(tmp[0], 0) + 1);
            map.put(tmp[1], map.getOrDefault(tmp[1], 0) - 1);
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            count += entry.getValue();
            if (count > 1) return false;
        }
        return true;
    }

    //会议室
    //给你一个会议时间安排的数组 intervals ，每个会议时间都会包括开始和结束的时间 intervals[i] = [starti, endi] ，返回
//所需会议室的最小数量 。
//输入：intervals = [[0,30],[5,10],[15,20]]
//输出：2
    public int minMeetingRoomsDiff(int[][] intervals) {
        int count = 0;
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int[] tmp : intervals) {
            map.put(tmp[0], map.getOrDefault(tmp[0], 0) + 1);
            map.put(tmp[1], map.getOrDefault(tmp[1], 0) - 1);
        }
        int max = Integer.MIN_VALUE;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            count += entry.getValue();
            max = Math.max(max, count);
        }
        return max;
    }


    public int minMeetingRooms(int[][] intervals) {
        // 不用treemap的思路就是把start和end 都排序，start递增，每经过一个start就+1，每个start有小于它的end就-1
        int[] start = new int[intervals.length];
        int[] end = new int[intervals.length];
        for (int i = 0; i < intervals.length; i++) {
            start[i] = intervals[i][0];
            end[i] = intervals[i][1];
        }
        Arrays.sort(start);
        Arrays.sort(end);
        int startPointer = 0, endPointer = 0;
        int usedRooms = 0;
        while (startPointer < intervals.length) {

            if (start[startPointer] >= end[endPointer]) {
                usedRooms -= 1;
                endPointer += 1;
            }

            usedRooms += 1;
            startPointer += 1;

        }

        return usedRooms;
    }

    //(1, 10), (2, 7), (3, 19), (8, 12), (10, 20), (11, 30)
    public int minMeetingRooms2(int[][] intervals) {
        if (intervals.length == 0) {
            return 0;
        }
        //建小顶堆，存放结束时间，取最早结束
        PriorityQueue<Integer> allocator = new PriorityQueue<>(intervals.length);
        //根据开始时间排序
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        allocator.add(intervals[0][1]);
        for (int i = 1; i < intervals.length; i++) {
            //开始时间晚于堆中最早的，共用会议室 （先取出，下面会加回）
            if (intervals[i][0] >= allocator.peek()) {
                allocator.poll();
            }
            //放入结束时间，堆自动排序为堆顶最小
            allocator.add(intervals[i][1]);
        }
        return allocator.size();
    }

    // 1109. 航班预订统计
    //这里有 n 个航班，它们分别从 1 到 n 进行编号。
//
// 有一份航班预订表 bookings ，表中第 i 条预订记录 bookings[i] = [firsti, lasti, seatsi] 意味着在从 fi
//rsti 到 lasti （包含 firsti 和 lasti ）的 每个航班 上预订了 seatsi 个座位。
// 请你返回一个长度为 n 的数组 answer，里面的元素是每个航班预定的座位总数。
//输入：bookings = [[1,2,10],[2,3,20],[2,5,25]], n = 5
//输出：[10,55,45,25,25]
//解释：
//航班编号        1   2   3   4   5
//预订记录 1 ：   10  10
//预订记录 2 ：       20  20
//预订记录 3 ：       25  25  25  25
//总座位数：      10  55  45  25  25
//因此，answer = [10,55,45,25,25]

    //差分
    public int[] corpFlightBookings(int[][] bookings, int n) {
        //开辟n位差分数组，表示第i位的变动情况（bookings从1开始，c从0开始）
        int[] c = new int[n];
        //从l开始增加v,那么[l,r]都会增加v (公交车l站上v人，r+1站下v人)
        for (int[] b : bookings) {
            int l = b[0] - 1, r = b[1], ct = b[2];
            c[l] += ct;
            //第n+1位下人，此时所求的第n位还是有v这个增量，所以越界需要减去的不用处理不影响第n位
            if (r < n) c[r] -= ct;
        }
        for (int i = 1; i < n; i++) {
            c[i] = c[i - 1] + c[i];
        }
        return c;
    }

    // 面试题 16.10 生存人数
    public int maxAliveYear(int[] birth, int[] death) {
        int[] diff = new int[101];
        int[] sum = new int[101];
        int n = birth.length;
        for (int i = 0; i < n; i++) {
            diff[birth[i] - 1900]++;
            if (death[i] + 1 <= 2000) {
                diff[death[i] + 1 - 1900]--;
            }
        }
        sum[0] = diff[0];
        int max = sum[0], year = -1;
        for (int i = 1; i <= 100; i++) {
            sum[i] = sum[i - 1] + diff[i];
            if (max < sum[i]) {
                year = i + 1900;
                max = sum[i];
            }
        }
        return year;
    }

    // 732 我的日程表安排
    public class MyCalendarThree {
        TreeMap<Integer, Integer> calendar;

        public MyCalendarThree() {
            calendar = new TreeMap<>();
        }

        public int book(int start, int end) {
            calendar.put(start, calendar.getOrDefault(start, 0) + 1);
            calendar.put(end, calendar.getOrDefault(end, 0) - 1);
            int concurrent = 0, ans = 0;
            for (int v : calendar.values()) {
                concurrent += v;
                ans = Math.max(concurrent, ans);
            }
            return ans;
        }
    }

    //2536. 子矩阵元素加 1  二维差分
    //https://leetcode.cn/problems/increment-submatrices-by-one/solution/er-wei-cha-fen-tu-jie-by-newhar-4tch/
    // 前缀和矩阵[x1,y1]的值就是差分矩阵[0,0]-[x1,y1]的和
    // 使矩阵[x1,y1]-[x2,y2]都+n,则使差分矩阵[x1,y1]+n,[x1+1,y1]-n,[x2,y1+1]-n,[x2+1,y2+1]+n
    public int[][] rangeAddQueries(int n, int[][] queries) {
        int[][] matrix = new int[n][n];
        for (int[] query : queries) {
            for (int i = query[0]; i <= query[2]; i++) {
                for (int j = query[1]; j <= query[3]; j++) {
                    matrix[i][j]++;
                }
            }
        }
        return matrix;
    }

    public int[][] rangeAddQueriesDiff(int n, int[][] queries) {
        int[][] diff = new int[n + 1][n + 1];
        int[][] matrix = new int[n][n];
        for (int[] query : queries) {
            int x1 = query[0], y1 = query[1], x2 = query[2] + 1, y2 = query[3] + 1;
            diff[x1][y1]++;
            diff[x2][y1]--;
            diff[x1][y2]--;
            diff[x2][y2]++;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = diff[i][j];
                if (i != 0) matrix[i][j] += matrix[i - 1][j];
                if (j != 0) matrix[i][j] += matrix[i][j - 1];
                if (i != 0 && j != 0) matrix[i][j] -= matrix[i - 1][j - 1];
            }
        }
        return matrix;
    }

    //2132. 用邮票贴满网格图
    public boolean possibleToStamp(int[][] grid, int h, int w) {
        int m = grid.length, n = grid[0].length;
        int[][] sum = new int[m + 1][n + 1];
        int[][] diff = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                sum[i][j] = sum[i][j - 1] + sum[i - 1][j] - sum[i - 1][j - 1] + grid[i - 1][j - 1];
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    int x = i + h, y = j + w; // x2=i+h-1 y2=j+w-1
                    if (x <= m && y <= n && (sum[x][y] - sum[x][j] - sum[i][y] + sum[i][j] == 0)) {
                        diff[i][j]++;
                        diff[i][y]--;
                        diff[x][j]--;
                        diff[x][y]++;
                    }
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i != 0) diff[i][j] += diff[i - 1][j];
                if (j != 0) diff[i][j] += diff[i][j - 1];
                if (i != 0 && j != 0) diff[i][j] -= diff[i - 1][j - 1];
                if (diff[i][j] == 0 && grid[i][j] == 0) return false;
            }
        }
        return true;
    }

    //2251. 花期内花的数目
    public int[] fullBloomFlowers(int[][] flowers, int[] persons) {
        TreeMap<Integer, Integer> cnt = new TreeMap<>();
        for (int[] flower : flowers) {
            cnt.put(flower[0], cnt.getOrDefault(flower[0], 0) + 1);
            cnt.put(flower[1] + 1, cnt.getOrDefault(flower[1] + 1, 0) - 1);
        }
        int m = persons.length;
        Integer[] indices = IntStream.range(0, m).boxed().toArray(Integer[]::new);
        Arrays.sort(indices, (i, j) -> persons[i] - persons[j]);
        int[] ans = new int[m];
        int curr = 0;
        for (int x : indices) {
            while (!cnt.isEmpty() && cnt.firstKey() <= persons[x]) {
                curr += cnt.pollFirstEntry().getValue();
            }
            ans[x] = curr;
        }
        return ans;
    }

    //endregion------------------------------------------------------------------------------------------
}
