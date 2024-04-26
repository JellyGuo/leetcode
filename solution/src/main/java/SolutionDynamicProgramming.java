import java.util.*;
import java.util.stream.LongStream;

public class SolutionDynamicProgramming {
   //------------------------------------------DP------------------------------------------------------
    //region ------------------------------------------记忆化搜索 DFS/DP------------------------------------------------------
    // 87 扰乱字符串
    // 朴素DFS
    public boolean isScramble(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        if (s1.equals(s2)) return true;
        if (!check(s1, s2)) return false;
        int n = s1.length();
        for (int i = 1; i < n; i++) {
            if (isScramble(s1.substring(0, i), s2.substring(0, i)) && isScramble(s1.substring(i), s2.substring(i))) {
                return true;
            }
            if (isScramble(s1.substring(0, i), s2.substring(n - i)) && isScramble(s1.substring(i), s2.substring(0, n - i))) {
                return true;
            }
        }
        return false;
    }

    private boolean check(String s1, String s2) {
        char[] chars = new char[26];
        for (int i = 0; i < s1.length(); i++) {
            chars[s1.charAt(i) - 'a']++;
            chars[s2.charAt(i) - 'a']--;
        }
        for (int i = 0; i < 26; i++) {
            if (chars[i] != 0) return false;
        }
        return true;
    }

    String s1, s2;
    int[][][] memo;
    int stringLen;

    public boolean isScrambleMemo(String s1, String s2) {
        this.s1 = s1;
        this.s2 = s2;
        this.stringLen = s1.length();
        this.memo = new int[stringLen][stringLen][stringLen + 1];
        return dfs(0, 0, stringLen);
    }

    private boolean dfs(int i, int j, int len) {
        if (memo[i][j][len] != 0) return memo[i][j][len] == 1;
        if (s1.substring(i, i + len).equals(s2.substring(j, j + len))) {
            memo[i][j][len] = 1;
            return true;
        }
        if (!check(s1.substring(i, i + len), s2.substring(j, j + len))) {
            memo[i][j][len] = -1;
            return false;
        }
        for (int k = 1; k < len; k++) {
            // 对应了「s1 的 [0,i) & [i,n)」匹配「s2 的 [0,i) & [i,n)」
            if (dfs(i, j, k) && dfs(i + k, j + k, len - k)) {
                memo[i][j][len] = 1;
                return true;
            }
            // 对应了「s1 的 [0,i) & [i,n)」匹配「s2 的 [n-i,n) & [0,n-i)」
            // s1 前i个对应s2后i个
            if (dfs(i, j + len - k, k) && dfs(i + k, j, len - k)) {
                memo[i][j][len] = 1;
                return true;
            }
        }
        memo[i][j][len] = -1;
        return false;
    }

    public boolean isScrambleDP(String s1, String s2) {
        if (s1.equals(s2)) return true;
        if (s1.length() != s2.length()) return false;
        int n = s1.length();
        char[] cs1 = s1.toCharArray(), cs2 = s2.toCharArray();
        boolean[][][] f = new boolean[n][n][n + 1];

        // 先处理长度为 1 的情况
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                f[i][j][1] = cs1[i] == cs2[j];
            }
        }

        // 再处理其余长度情况
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                for (int j = 0; j <= n - len; j++) {
                    for (int k = 1; k < len; k++) {
                        boolean a = f[i][j][k] && f[i + k][j + k][len - k];
                        boolean b = f[i][j + len - k][k] && f[i + k][j][len - k];
                        if (a || b) {
                            f[i][j][len] = true;
                        }
                    }
                }
            }
        }
        return f[0][0][n];
    }

    //115 不同的子序列
    //给定一个字符串 s 和一个字符串 t ，计算在 s 的子序列中 t 出现的个数。
    // 朴素DFS
    public int numDistinct(String s, String t) {
        return dfs(s, t, s.length() - 1, t.length() - 1);
    }

    private int dfs(String s, String t, int i, int j) {
        if (j < 0) return 1;
        if (i < 0) return 0;
        if (s.charAt(i) == t.charAt(j)) {
            return dfs(s, t, i - 1, j) + dfs(s, t, i - 1, j - 1);
        } else {
            return dfs(s, t, i - 1, j);
        }
    }

    //记忆化搜索
    public int numDistinctMemo(String s, String t) {
        int[][] memo = new int[s.length()][t.length()];
        for (int[] mo : memo) {
            Arrays.fill(mo, -1);
        }
        return dfs(s, t, s.length() - 1, t.length() - 1, memo);
    }

    private int dfs(String s, String t, int i, int j, int[][] memo) {
        if (j < 0) return 1;
        if (i < 0) return 0;
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        if (s.charAt(i) == t.charAt(j)) {
            memo[i][j] = dfs(s, t, i - 1, j, memo) + dfs(s, t, i - 1, j - 1, memo);
        } else {
            memo[i][j] = dfs(s, t, i - 1, j, memo);
        }
        return memo[i][j];
    }

    public int numDistinctDP(String s, String t) {
        // s的前i个字符和t的前j个字符的匹配个数
        int[][] dp = new int[s.length() + 1][t.length() + 1];

        for (int i = 0; i <= t.length(); i++) {
            dp[0][i] = 0;
        }
        for (int i = 0; i <= s.length(); i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 1; j <= t.length(); j++) {
                // 考虑是否让s[i] 参与匹配
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[s.length()][t.length()];
    }

    //139单词拆分
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i < s.length() + 1; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && set.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }

    // 140 单词拆分
    public List<String> wordBreak2(String s, List<String> wordDict) {
        Map<Integer, List<List<String>>> map = new HashMap<>();
        List<List<String>> wordBreaks = backtrack(s, s.length(), new HashSet<>(wordDict), 0, map);
        List<String> breakList = new LinkedList<>();
        for (List<String> wordBreak : wordBreaks) {
            breakList.add(String.join(" ", wordBreak));
        }
        return breakList;
    }

    public List<List<String>> backtrack(String s, int length, Set<String> wordSet, int index, Map<Integer, List<List<String>>> map) {
        if (!map.containsKey(index)) {
            List<List<String>> wordBreaks = new LinkedList<>();
            if (index == length) {
                wordBreaks.add(new LinkedList<>());
            }
            for (int i = index + 1; i <= length; i++) {
                String word = s.substring(index, i);
                if (wordSet.contains(word)) {
                    List<List<String>> nextWordBreaks = backtrack(s, length, wordSet, i, map);
                    for (List<String> nextWordBreak : nextWordBreaks) {
                        List<String> wordBreak = new ArrayList<>(nextWordBreak);
                        wordBreak.add(0, word);
                        wordBreaks.add(wordBreak);
                    }
                }
            }
            map.put(index, wordBreaks);
        }
        return map.get(index);
    }

    //1079. 活字印刷
    public int numTilePossibilities(String tiles) {
        int[] cnt = new int[26];
        for (char c : tiles.toCharArray()) {
            cnt[c - 'A']++;
        }
        return dfs1079(cnt);
    }

    private int dfs1079(int[] cnt) {
        int sum = 0;
        for (int i = 0; i < 26; i++) {
            if (cnt[i] == 0) continue;
            sum++;
            // i处的值相同，相同的字母都在此处往下递归，所以只会有1种情况，i增加后，不会再有此时的字母
            cnt[i]--;
            sum += dfs1079(cnt);
            cnt[i]++;
        }
        return sum;
    }

    //1335. 工作计划的最低难度
    private int[] a1335;
    private int[][] memo1335;

    public int minDifficulty(int[] a, int d) {
        int n = a.length;
        if (n < d) return -1;

        this.a1335 = a;
        memo1335 = new int[d][n];
        for (int i = 0; i < d; ++i)
            Arrays.fill(memo1335[i], -1); // -1 表示还没有计算过
        return dfs1335(d - 1, n - 1);
    }

    //用 i+1 天时间完成 a[0] 到 a[j] 这些工作的答案
    private int dfs1335(int i, int j) {
        if (memo1335[i][j] != -1) // 之前计算过了
            return memo1335[i][j];
        if (i == 0) { // 只有一天，必须完成所有工作
            int mx = 0;
            for (int k = 0; k <= j; k++)
                mx = Math.max(mx, a1335[k]);
            return memo1335[i][j] = mx;
        }
        int res = Integer.MAX_VALUE;
        int mx = 0;
        for (int k = j; k >= i; k--) {
            mx = Math.max(mx, a1335[k]); // 从 a[k] 到 a[j] 的最大值
            res = Math.min(res, dfs1335(i - 1, k - 1) + mx);
        }
        return memo1335[i][j] = res;
    }

    public int minDifficultyDP(int[] a, int d) {
        int n = a.length;
        if (n < d) return -1;

        int[][] f = new int[d][n];
        f[0][0] = a[0];
        for (int i = 1; i < n; i++)
            f[0][i] = Math.max(f[0][i - 1], a[i]);
        for (int i = 1; i < d; i++) {
            for (int j = n - 1; j >= i; j--) {
                f[i][j] = Integer.MAX_VALUE;
                int mx = 0;
                for (int k = j; k >= i; k--) {
                    mx = Math.max(mx, a[k]); // 从 a[k] 到 a[j] 的最大值
                    f[i][j] = Math.min(f[i][j], f[i - 1][k - 1] + mx);
                }
            }
        }
        return f[d - 1][n - 1];
    }

    //2050. 并行课程 III
    public int minimumTime(int n, int[][] relations, int[] time) {
        int mx = 0;
        List<Integer>[] prev = new List[n + 1];
        for (int i = 0; i <= n; i++) {
            prev[i] = new ArrayList<Integer>();
        }
        for (int[] relation : relations) {
            int x = relation[0], y = relation[1];
            prev[y].add(x);
        }
        Map<Integer, Integer> memo = new HashMap<Integer, Integer>();
        for (int i = 1; i <= n; i++) {
            mx = Math.max(mx, dp2050(i, time, prev, memo));
        }
        return mx;
    }

    public int dp2050(int i, int[] time, List<Integer>[] prev, Map<Integer, Integer> memo) {
        if (!memo.containsKey(i)) {
            int cur = 0;
            for (int p : prev[i]) {
                cur = Math.max(cur, dp2050(p, time, prev, memo));
            }
            cur += time[i - 1];
            memo.put(i, cur);
        }
        return memo.get(i);
    }

    //1376. 通知所有员工所需的时间
    int headID;  // 公司总负责人 ID
    int[] manager;  // manager[i] 表示第 i 名员工的直属负责人
    int[] informTime;  // informTime[i] 表示第 i 名员工通知直属下属所需时间
    Map<Integer, Integer> memo1376 = new HashMap<>();  // 记忆化搜索缓存

    public int numOfMinutes(int n, int headID, int[] manager, int[] informTime) {
        this.headID = headID;
        this.manager = manager;
        this.informTime = informTime;
        int res = 0;  // 记录最长时间
        for (int i = 0; i < n; i++) {
            res = Math.max(res, dfs1376(i));  // 对每个员工遍历，更新最长时间
        }
        return res;
    }

    public int dfs1376(int cur) {
        if (cur == headID) {  // 当前节点为根节点
            return 0;
        }
        if (!memo1376.containsKey(cur)) {  // 检查缓存中是否已经存在当前节点的时间
            int res = dfs1376(manager[cur]) + informTime[manager[cur]];  // 递归遍历当前节点的直属上级节点，返回时间和
            memo1376.put(cur, res);  // 将当前节点到根节点的时间加入缓存中
        }
        return memo1376.get(cur);  // 返回当前节点到根节点的时间
    }

    //1043. 分隔数组以得到最大和
    public int maxSumAfterPartitioningMemo(int[] arr, int k) {
        int n = arr.length;
        int[] memo = new int[n];
        Arrays.fill(memo, -1);
        return maxSumAfterPartitioningDfs(n - 1, arr, memo, k);
    }

    private int maxSumAfterPartitioningDfs(int i, int[] arr, int[] memo, int k) {
        if (i < 0) return 0;
        if (memo[i] != -1) return memo[i];
        int max = arr[i];
        int ans = 0;
        for (int j = i; (j > i - k) && j >= 0; j--) {
            max = Math.max(max, arr[j]);
            ans = Math.max(ans, maxSumAfterPartitioningDfs(j - 1, arr, memo, k) + max * (i - j + 1));
        }
        return memo[i] = ans;
    }

    public int maxSumAfterPartitioning(int[] arr, int k) {
        int n = arr.length;
        int[] d = new int[n + 1];
        for (int i = 0; i < n; i++) {
            int maxValue = arr[i];
            for (int j = i; j >= 0 && j > i - k; j--) {
                maxValue = Math.max(maxValue, arr[j]);
                d[i + 1] = Math.max(d[i + 1], d[j] + maxValue * (i - j + 1));
            }
        }
        return d[n];
    }

    //1349. 参加考试的最大学生数
    Map<Integer, Integer> memo1349 = new HashMap<Integer, Integer>();

    public int maxStudents(char[][] seats) {
        int m = seats.length, n = seats[0].length;
        int mx = 0;
        for (int i = 0; i < 1 << n; i++) {
            mx = Math.max(mx, dp(seats, m - 1, i));
        }
        return mx;
    }

    public int dp(char[][] seats, int row, int status) {
        int n = seats[0].length;
        int key = (row << n) + status;
        if (!memo1349.containsKey(key)) {
            if (!isSingleRowCompliant(seats, status, n, row)) {
                memo1349.put(key, Integer.MIN_VALUE);
                return Integer.MIN_VALUE;
            }
            int students = Integer.bitCount(status);
            if (row == 0) {
                memo1349.put(key, students);
                return students;
            }
            int mx = 0;
            for (int upperRowStatus = 0; upperRowStatus < 1 << n; upperRowStatus++) {
                if (isCrossRowsCompliant(status, upperRowStatus, n)) {
                    mx = Math.max(mx, dp(seats, row - 1, upperRowStatus));
                }
            }
            memo1349.put(key, students + mx);
        }
        return memo1349.get(key);
    }

    public boolean isSingleRowCompliant(char[][] seats, int status, int n, int row) {
        for (int j = 0; j < n; j++) {
            if (((status >> j) & 1) == 1) {
                if (seats[row][j] == '#') {
                    return false;
                }
                if (j > 0 && ((status >> (j - 1)) & 1) == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isCrossRowsCompliant(int status, int upperRowStatus, int n) {
        for (int j = 0; j < n; j++) {
            if (((status >> j) & 1) == 1) {
                if (j > 0 && ((upperRowStatus >> (j - 1)) & 1) == 1) {
                    return false;
                }
                if (j < n - 1 && ((upperRowStatus >> (j + 1)) & 1) == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    //1595. 连通两组点的最小成本
    private List<List<Integer>> cost;
    private int[] minCost;
    private int[][] memo1595;

    public int connectTwoGroups(List<List<Integer>> cost) {
        this.cost = cost;
        int n = cost.size(), m = cost.get(0).size();
        minCost = new int[m];
        Arrays.fill(minCost, Integer.MAX_VALUE);
        for (int j = 0; j < m; j++)
            for (List<Integer> c : cost)
                minCost[j] = Math.min(minCost[j], c.get(j));

        memo1595 = new int[n][1 << m];
        for (int i = 0; i < n; i++)
            Arrays.fill(memo1595[i], -1); // -1 表示没有计算过
        return dfs1595(n - 1, (1 << m) - 1);
    }

    private int dfs1595(int i, int j) {
        if (i < 0) {
            int res = 0;
            for (int k = 0; k < minCost.length; k++)
                if ((j >> k & 1) == 1) // 第二组的点 k 未连接
                    res += minCost[k]; // 去第一组找个成本最小的点连接
            return res;
        }
        if (memo1595[i][j] != -1) return memo1595[i][j]; // 之前算过了
        int res = Integer.MAX_VALUE;
        for (int k = 0; k < minCost.length; k++) // 第一组的点 i 与第二组的点 k
            res = Math.min(res, dfs1595(i - 1, j & ~(1 << k)) + cost.get(i).get(k));
        return memo1595[i][j] = res; // 记忆化
    }

    //1641. 统计字典序元音字符串的数目
    int ans1641 = 0;

    public int countVowelStrings(int n) {
        dfs1641(0, 0, n);
        return ans1641;
    }

    private void dfs1641(int idx, int len, int n) {
        if (len >= n) {
            ans1641++;
            return;
        }
        for (int i = idx; i < 5; i++) {
            dfs1641(i, len + 1, n);
        }
    }

    public int countVowelStringsMemo(int n) {
        //表示当前已经选了 i 个元音字母，且最后一个元音字母是 j 的方案数
        int[][] memo = new int[n][5];
        return dfs1641memo(0, 0, n, memo);
    }

    private int dfs1641memo(int i, int j, int n, int[][] memo) {
        if (i >= n) {
            return 1;
        }
        if (memo[i][j] != 0) {
            return memo[i][j];
        }
        int ans = 0;
        for (int k = j; k < 5; ++k) {
            ans += dfs1641memo(i + 1, k, n, memo);
        }
        return memo[i][j] = ans;
    }

    //DP+前缀
    //记 dp[i][j] 表示长度为 i+1，以 j 结尾的按字典序排列的字符串数量，那么状态转移方程如下：
    //那么状态转移方程如下
    //dp[i][j]=  1,i=0
    // sum(dp[i−1][k])(k=[0,j])
    //因此长度为 n 的按字典序排列的字符串数量为 sum(dp[n−1][j]) k=[0,4]。因为 dp[i] 的计算只涉及 dp[i−1] 部分的数据，同时 dp[i] 等价于 dp[i−1] 的前缀和
    public int countVowelStringsDP(int n) {
        int[] dp = new int[5];
        Arrays.fill(dp, 1);
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < 5; j++) {
                dp[j] += dp[j - 1];
            }
        }
        return Arrays.stream(dp).sum();
    }

    //1039. 多边形三角剖分的最低得分
    public int minScoreTriangulation(int[] values) {
        int n = values.length;
        int[][] memo = new int[n][n];
        for (int i = 0; i < n; ++i) {
            Arrays.fill(memo[i], -1); // -1 表示没有访问过
        }
        return dfs1039(0, n - 1, memo, values);
    }

    private int dfs1039(int i, int j, int[][] memo, int[] values) {
        if (i + 1 == j) return 0; // 只有两个点，无法组成三角形
        if (memo[i][j] != -1) return memo[i][j];
        int res = Integer.MAX_VALUE;
        for (int k = i + 1; k < j; ++k) // 枚举顶点 k
            res = Math.min(res, dfs1039(i, k, memo, values) + dfs1039(k, j, memo, values) + values[i] * values[j] * values[k]);
        return memo[i][j] = res;
    }

    public int minScoreTriangulationDP(int[] v) {
        int n = v.length;
        int[][] f = new int[n][n];
        for (int i = n - 3; i >= 0; --i)
            for (int j = i + 2; j < n; ++j) {
                f[i][j] = Integer.MAX_VALUE;
                for (int k = i + 1; k < j; ++k)
                    f[i][j] = Math.min(f[i][j], f[i][k] + f[k][j] + v[i] * v[j] * v[k]);
            }
        return f[0][n - 1];
    }

    // 312 戳气球
    public int maxCoins(int[] nums) {
        int n = nums.length;
        int[] vals = new int[n + 2];
        for (int i = 1; i <= n; i++) {
            vals[i] = nums[i - 1];
        }
        vals[0] = vals[n + 1] = 1;
        int[][] memo = new int[n + 2][n + 2];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return solve(vals, memo, 0, n + 1);
    }

    private int solve(int[] vals, int[][] memo, int l, int r) {
        if (l + 1 >= r) return 0;
        if (memo[l][r] != -1) return memo[l][r];
        for (int i = l + 1; i < r; i++) {
            int value = vals[l] * vals[i] * vals[r];
            int sum = solve(vals, memo, l, i) + value + solve(vals, memo, i, r);
            memo[l][r] = Math.max(sum, memo[l][r]);
        }
        return memo[l][r];
    }

    public int maxCoinsDP(int[] nums) {
        int n = nums.length;
        int[] vals = new int[n + 2];
        for (int i = 1; i <= n; i++) {
            vals[i] = nums[i - 1];
        }
        vals[0] = vals[n + 1] = 1;
        int[][] dp = new int[n + 2][n + 2];
        for (int l = n - 1; l >= 0; l--) {
            for (int r = l + 2; r < n + 2; r++) {
                // k是(l,r) 最后一个被戳破的气球
                for (int k = l + 1; k < r; k++) {
                    dp[l][r] = Math.max(dp[l][r], dp[l][k] + vals[l] * vals[k] * vals[r] + dp[k][r]);
                }
            }
        }
//        for (int len = 2; len <= n + 2; len++) {
//            for (int l = 0; l + len - 1 < n + 2; l++) {
//                int r = l + len - 1;
//                for (int k = l + 1; k < r; k++) {
//                    dp[l][r] = Math.max(dp[l][r], dp[l][k] + arr[l] * arr[k] * arr[r] + dp[k][r]);
//                }
//            }
//        }
        return dp[0][n + 1];
    }

    //823. 带因子的二叉树
    public int numFactoredBinaryTrees(int[] arr) {
        int mod = (int) 1e9 + 7;
        Set<Integer> set = new HashSet<>();
        Map<Integer, Long> memo = new HashMap<>();
        for (int num : arr) {
            set.add(num);
        }
        long ans = 0;
        for (int x : arr) {
            ans += dfs(x, set, arr, memo);
        }
        return (int) (ans%mod);
    }

    private long dfs(int x, Set<Integer> set, int[] array, Map<Integer, Long> memo) {
        if (memo.containsKey(x)) {
            return memo.get(x);
        }
        long res = 1;
        for (int num : array) {
            if (x % num == 0 && set.contains(x / num)) {
                res += dfs(num, set, array, memo) * dfs(x / num, set, array, memo);
            }
        }
        memo.put(x, res);
        return memo.get(x);
    }

    public int numFactoredBinaryTreesDP(int[] arr) {
        final long MOD = (long) 1e9 + 7;
        Arrays.sort(arr);
        int n = arr.length;
        Map<Integer, Integer> idx = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            idx.put(arr[i], i);
        }
        long ans = 0;
        long[] f = new long[n];
        for (int i = 0; i < n; i++) {
            int val = arr[i];
            f[i] = 1;
            for (int j = 0; j < i; ++j) { // val 的因子一定比 val 小
                int x = arr[j];
                if (val % x == 0 && idx.containsKey(val / x)) { // 另一个因子 val/x 必须在 arr 中
                    f[i] += f[j] * f[idx.get(val / x)];
                }
            }
            ans += f[i];
        }
        return (int) (ans % MOD);
    }

    // 329 矩阵中最长的递增路径 Hard
    public int longestIncreasingPath(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] memo = new int[m][n];
        int max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                max = Math.max(max, dfs(i, j, matrix, memo));
            }
        }
        return max;
    }

    private int dfs(int r, int c, int[][] matrix, int[][] memo) {
        int[][] directions = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        if (memo[r][c] != 0) return memo[r][c];
        memo[r][c]++;
        for (int[] dire : directions) {
            int nr = r + dire[0], nc = c + dire[1];
            if (nr >= 0 && nr < matrix.length && nc >= 0 && nc < matrix[0].length && matrix[nr][nc] > matrix[r][c]) {
                memo[r][c] = Math.max(memo[r][c], dfs(nr, nc, matrix, memo) + 1);
            }
        }
        return memo[r][c];
    }

    //2312. 卖木头块
    public long sellingWood(int m, int n, int[][] prices) {
        Map<Long, Integer> value = new HashMap<>();
        for (int[] price : prices) {
            value.put(pairHash(price[0], price[1]), price[2]);
        }

        long[][] memo = new long[m + 1][n + 1];
        for (long[] row : memo) {
            Arrays.fill(row, -1);
        }
        return dfs2312(m, n, value, memo);
    }

    public long dfs2312(int x, int y, Map<Long, Integer> value, long[][] memo) {
        if (memo[x][y] != -1) {
            return memo[x][y];
        }

        long key = pairHash(x, y);
        long ret = value.containsKey(key) ? value.get(key) : 0;
        if (x > 1) {
            for (int i = 1; i < x; i++) {
                ret = Math.max(ret, dfs2312(i, y, value, memo) + dfs2312(x - i, y, value, memo));
            }
        }
        if (y > 1) {
            for (int j = 1; j < y; j++) {
                ret = Math.max(ret, dfs2312(x, j, value, memo) + dfs2312(x, y - j, value, memo));
            }
        }
        memo[x][y] = ret;
        return ret;
    }

    public long pairHash(int x, int y) {
        return (long) x << 16 ^ y;
    }

    //6196. 将字符串分割成值不超过 K 的子字符串  贪心算法搜minimumPartitionGreedy
    // TLE
    int ans6196 = Integer.MAX_VALUE;

    public int minimumPartition(String s, int k) {
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (c - '0' > k) return -1;
        }
        backtrack(chars, k, 0, 0);
        return ans6196;
    }

    private void backtrack(char[] chars, int k, int idx, int curCnt) {
        if (idx == chars.length) {
            ans6196 = Math.min(ans6196, curCnt);
            return;
        }
        int num = 0;
        for (int i = idx; i < chars.length; i++) {
            num = num * 10 + chars[i] - '0';
            if (num > k) break;
            backtrack(chars, k, i + 1, curCnt + 1);
        }
    }

    public int minimumPartition2(String s, int k) {
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (c - '0' > k) return -1;
        }
        Map<Integer, Integer> memo = new HashMap<>();
        int d = String.valueOf(k).length();
        return backtrack(s, k, d, 0, memo);
    }

    private int backtrack(String s, int k, int d, int idx, Map<Integer, Integer> memo) {
        if (idx == s.length()) {
            return 0;
        }
        if (memo.containsKey(idx)) return memo.get(idx);
        int ans = Integer.MAX_VALUE;
        for (int i = idx; i < Math.min(s.length(), idx + d); i++) {
            if (i - idx + 1 > d) break;
            int num = Integer.parseInt(s.substring(idx, i + 1));
            if (num > k) break;
            ans = Math.min(ans, 1 + backtrack(s, k, d, i + 1, memo));
        }
        memo.put(idx, ans);
        return ans;
    }


    // 688 骑士在棋盘上的概率
    int[][] directions = new int[][]{{1, 2}, {-1, 2}, {1, -2}, {-1, -2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1}};

    public double knightProbability(int n, int k, int row, int column) {
        double[][][] memo = new double[n][n][k + 1];
        return dfs(n, k, row, column, memo);
    }

    private double dfs(int n, int k, int x, int y, double[][][] memo) {
        if (x >= n || x < 0 || y >= n || y < 0) return 0;
        if (k == 0) return 1;
        if (memo[x][y][k] > 0) return memo[x][y][k];
        double ans = 0;
        for (int[] dire : directions) {
            ans += dfs(n, k - 1, x + dire[0], y + dire[1], memo) / 8;
        }
        return memo[x][y][k] = ans;
    }

    public double knightProbabilityDP(int n, int k, int row, int column) {
        //定义 dp[i][j][p] 为从位置 (i, j) 出发，使用步数不超过 p 步，最后仍在棋盘内的概率
        // 若下一步落点(nx,ny)在棋盘内，剩余可走步数是p-1，问题转为 从(nx,ny)出发，使用不超过p-1步依然在棋盘的概率
        // 下一步落点是1/8概率，故 dp[i][j][p] = dp[nx][ny][p-1]/8
        double[][][] dp = new double[n][n][k + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j][0] = 1;
            }
        }
        for (int p = 1; p <= k; p++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int[] dire : directions) {
                        int nx = i + dire[0];
                        int ny = j + dire[1];
                        if (nx >= 0 && nx < n && ny >= 0 && ny < n) {
                            dp[i][j][p] += dp[nx][ny][p - 1] / 8;
                        }
                    }
                }
            }
        }
        return dp[row][column][k];
    }

    //2596. 检查骑士巡视方案
    public boolean checkValidGrid(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int target = m * n - 1;
        boolean[][] visited = new boolean[m][n];
        visited[0][0] = true;
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{0, 0});
        int idx = 1;
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0], y = cell[1];
            for (int[] dire : directions) {
                int newX = x + dire[0], newY = y + dire[1];
                if (newX >= 0 && newX < m && newY >= 0 && newY < n && !visited[newX][newY] && grid[newX][newY] == idx) {
                    if (idx == target) return true;
                    visited[newX][newY] = true;
                    queue.offer(new int[]{newX, newY});
                    idx++;
                }
            }
        }
        return false;
    }

    //2746. 字符串连接删减字母
    public int minimizeConcatenatedLength(String[] words) {
        int n = words.length;
        int[][][] dp = new int[n][26][26];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 26; j++) {
                for (int k = 0; k < 26; k++) {
                    dp[i][j][k] = Integer.MAX_VALUE / 2;
                }
            }
        }
        int len = words[0].length();
        dp[0][words[0].charAt(0) - 'a'][words[0].charAt(len - 1) - 'a'] = len;
        for (int i = 1; i < n; i++) {
            String word = words[i];
            len = word.length();
            int x = word.charAt(0) - 'a', y = word.charAt(len - 1) - 'a';
            for (int j = 0; j < 26; j++) {
                for (int k = 0; k < 26; k++) {
                    if (y == j) dp[i][x][k] = Math.min(dp[i][x][k], dp[i - 1][j][k] + len - 1);
                    else dp[i][x][k] = Math.min(dp[i][x][k], dp[i - 1][j][k] + len);
                    if (x == k) dp[i][j][y] = Math.min(dp[i][j][y], dp[i - 1][j][k] + len - 1);
                    else dp[i][j][y] = Math.min(dp[i][j][y], dp[i - 1][j][k] + len);
                }
            }
        }
        int ans = Integer.MAX_VALUE;
        for (int j = 0; j < 26; j++) {
            for (int k = 0; k < 26; k++) {
                ans = Math.min(ans, dp[n - 1][j][k]);
            }
        }
        return ans;
    }

    // 面试08.14 布尔运算 自顶向下 记忆化DFS
    Integer[][][] countEvalMemo;

    public int countEval(String s, int result) {
        int n = s.length();
        countEvalMemo = new Integer[n][n][2];
        return dfs(0, n - 1, s, result);
    }

    private int dfs(int l, int r, String s, int result) { //区间[l, r]求result的括号方案数
        if (l > r) return 0;
        if (l == r) {
            return (s.charAt(l) - '0') == result ? 1 : 0;
        }
        if (countEvalMemo[l][r][result] != null) return countEvalMemo[l][r][result];
        int ans = 0;
        for (int i = l; i <= r; i++) {
            char c = s.charAt(i);
            if (result == 0) {
                if (c == '&')
                    ans += dfs(l, i - 1, s, 0) * dfs(i + 1, r, s, 0) + dfs(l, i - 1, s, 0) * dfs(i + 1, r, s, 1) + dfs(l, i - 1, s, 1) * dfs(i + 1, r, s, 0); //00、01、10
                if (c == '|') ans += dfs(l, i - 1, s, 0) * dfs(i + 1, r, s, 0); //00
                if (c == '^')
                    ans += dfs(l, i - 1, s, 0) * dfs(i + 1, r, s, 0) + dfs(l, i - 1, s, 1) * dfs(i + 1, r, s, 1); //00、11
            } else {
                if (c == '&') ans += dfs(l, i - 1, s, 1) * dfs(i + 1, r, s, 1); //11
                if (c == '|')
                    ans += dfs(l, i - 1, s, 0) * dfs(i + 1, r, s, 1) + dfs(l, i - 1, s, 1) * dfs(i + 1, r, s, 0) + dfs(l, i - 1, s, 1) * dfs(i + 1, r, s, 1); //01、10、11
                if (c == '^')
                    ans += dfs(l, i - 1, s, 0) * dfs(i + 1, r, s, 1) + dfs(l, i - 1, s, 1) * dfs(i + 1, r, s, 0); //10、01
            }
        }
        return countEvalMemo[l][r][result] = ans;
    }

    //自底向上 动规
    public int countEvalDP(String s, int result) {
        int n = s.length();
        int[][][] dp = new int[n][n][2];
        for (int i = 0; i < n; i++) {
            dp[i][i][0] = (s.charAt(i) - '0' == 0 ? 1 : 0);
            dp[i][i][1] = (s.charAt(i) - '0' == 1 ? 1 : 0);
        }
        for (int len = 2; len <= n; len++) {
            for (int l = 0; l + (len - 1) < n; l++) {
                int r = l + (len - 1);
                for (int i = l; i <= r; i++) {
                    char c = s.charAt(i);
                    if (c == '&') {
                        dp[l][r][0] += dp[l][i - 1][0] * dp[i + 1][r][0] + dp[l][i - 1][0] * dp[i + 1][r][1] + dp[l][i - 1][1] * dp[i + 1][r][0];
                        dp[l][r][1] += dp[l][i - 1][1] * dp[i + 1][r][1];
                    }
                    if (c == '|') {
                        dp[l][r][0] += dp[l][i - 1][0] * dp[i + 1][r][0];
                        dp[l][r][1] += dp[l][i - 1][0] * dp[i + 1][r][1] + dp[l][i - 1][1] * dp[i + 1][r][0] + dp[l][i - 1][1] * dp[i + 1][r][1];
                    }
                    if (c == '^') {
                        dp[l][r][0] += dp[l][i - 1][0] * dp[i + 1][r][0] + dp[l][i - 1][1] * dp[i + 1][r][1];
                        dp[l][r][1] += dp[l][i - 1][0] * dp[i + 1][r][1] + dp[l][i - 1][1] * dp[i + 1][r][0];
                    }
                }
            }
        }
        return dp[0][n - 1][result];
    }


    // 464 我能赢吗
    public boolean canIWin(int maxChoosableInteger, int desiredTotal) {
        if ((1 + maxChoosableInteger) * maxChoosableInteger / 2 < desiredTotal) {
            return false;
        }
        Map<Integer, Boolean> memo = new HashMap<>();
        return dfs(maxChoosableInteger, 0, desiredTotal, 0, memo);
    }

    private boolean dfs(int max, int usedNumber, int target, int currentSum, Map<Integer, Boolean> memo) {
        if (memo.containsKey(usedNumber)) {
            return memo.get(usedNumber);
        }
        boolean res = false;
        for (int i = 0; i < max; i++) {
            int num = i + 1;
            if (((usedNumber >> i) & 1) == 0) {
                if (num + currentSum >= target) {
                    res = true;
                    break;
                }
                if (!dfs(max, usedNumber | (1 << i), target, currentSum + num, memo)) {
                    res = true;
                    break;
                }
            }
        }
        memo.put(usedNumber, res);
        return res;
    }

    //1815. 得到新鲜甜甜圈的最多组数 状态压缩+记忆化搜索
    static final int K_WIDTH = 5;
    static final int K_WIDTH_MASK = (1 << K_WIDTH) - 1;

    public int maxHappyGroups(int batchSize, int[] groups) {
        int[] cnt = new int[batchSize];
        for (int x : groups) {
            ++cnt[x % batchSize];
        }

        long start = 0;
        for (int i = batchSize - 1; i >= 1; --i) {
            start = (start << K_WIDTH) | cnt[i];
        }

        Map<Long, Integer> memo = new HashMap<>();

        return dfs(memo, batchSize, start) + cnt[0];
    }

    public int dfs(Map<Long, Integer> memo, int batchSize, long mask) {
        if (mask == 0) {
            return 0;
        }

        if (!memo.containsKey(mask)) {
            long total = 0;
            for (int i = 1; i < batchSize; ++i) {
                long amount = ((mask >> ((i - 1) * K_WIDTH)) & K_WIDTH_MASK);
                total += i * amount;
            }

            int best = 0;
            for (int i = 1; i < batchSize; ++i) {
                long amount = ((mask >> ((i - 1) * K_WIDTH)) & K_WIDTH_MASK);
                if (amount > 0) {
                    int result = dfs(memo, batchSize, mask - (1L << ((i - 1) * K_WIDTH)));
                    if ((total - i) % batchSize == 0) {
                        ++result;
                    }
                    best = Math.max(best, result);
                }
            }

            memo.put(mask, best);
        }
        return memo.get(mask);
    }

    //552. 学生出勤记录 II Hard
    public int checkRecordDfs(int n) {
        int[][][] memo = new int[n][2][3];
        return dfs(0, n, 0, 0, memo);
    }

    private int dfs(int day, int n, int absent, int late, int[][][] memo) {
        int mod = 1000000007;
        if (day >= n) return 1;
        if (memo[day][absent][late] != 0) return memo[day][absent][late];
        int ans = 0;
        ans = (ans + dfs(day + 1, n, absent, 0, memo)) % mod;
        if (absent < 1) {
            ans = (ans + dfs(day + 1, n, 1, 0, memo)) % mod;
        }
        if (late < 2) {
            ans = (ans + dfs(day + 1, n, absent, late + 1, memo)) % mod;
        }
        memo[day][absent][late] = ans;
        return ans;
    }

    public int checkRecordDP1(int n) {
        int MOD = 1000000007;
        long[][][] dp = new long[n][2][3];
        // 初始值
        dp[0][0][0] = 1;
        dp[0][1][0] = 1;
        dp[0][0][1] = 1;

        for (int i = 1; i < n; i++) {
            // 本次填入P，分成前一天累计了0个A和1个A两种情况
            dp[i][0][0] = (dp[i - 1][0][0] + dp[i - 1][0][1] + dp[i - 1][0][2]) % MOD;
            dp[i][1][0] = (dp[i - 1][1][0] + dp[i - 1][1][1] + dp[i - 1][1][2]) % MOD;
            // 本次填入A，前一天没有累计A都能转移过来
            // 这行可以与上面一行合并计算，为了方便理解，我们分开，下面会合并
            dp[i][1][0] = (dp[i][1][0] + dp[i - 1][0][0] + dp[i - 1][0][1] + dp[i - 1][0][2]) % MOD;
            // 本次填入L，前一天最多只有一个连续的L，分成四种情况
            dp[i][0][1] = dp[i - 1][0][0];
            dp[i][0][2] = dp[i - 1][0][1];
            dp[i][1][1] = dp[i - 1][1][0];
            dp[i][1][2] = dp[i - 1][1][1];
        }

        // 计算结果，即最后一天的所有状态相加
        long ans = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                ans = (ans + dp[n - 1][i][j]) % MOD;
            }
        }

        return (int) ans;
    }

    public int checkRecordDP2(int n) {
        int MOD = 1000000007;
        long[][] dp = new long[2][6];
        // 初始值
        dp[0][0] = 1;
        dp[0][1] = 1;
        dp[0][3] = 1;

        for (int i = 1; i < n; i++) {
            // 当前使用的下标
            int cur = i & 1;
            // 上一次使用的下标
            int last = (i - 1) & 1;
            dp[cur][0] = (dp[last][0] + dp[last][1] + dp[last][2]) % MOD;
            dp[cur][1] = dp[last][0];
            dp[cur][2] = dp[last][1];
            dp[cur][3] = (dp[last][3] + dp[last][4] + dp[last][5] + dp[last][0] + dp[last][1] + dp[last][2]) % MOD;
            dp[cur][4] = dp[last][3];
            dp[cur][5] = dp[last][4];
        }

        return (int) (LongStream.of(dp[(n - 1) & 1]).sum() % MOD);
    }

    //576. 出界的路径数 Hard
    int m, n, maxMove;
    int MOD = 1000000007;
    int[][] direction = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    public int findPaths(int m, int n, int maxMove, int startRow, int startColumn) {
        this.m = m;
        this.n = n;
        this.maxMove = maxMove;
        if (!inArea(startRow, startColumn)) return 1;
        long[][][] memo = new long[maxMove + 1][m][n];
        for (int k = 0; k <= maxMove; k++) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    memo[k][i][j] = -1;
                }
            }
        }

        return (int) dfs(startRow, startColumn, 0, memo);
    }

    private long dfs(int row, int col, int step, long[][][] memo) {
        if (step > maxMove) return 0;
        if (!inArea(row, col)) return 1;
        if (memo[step][row][col] != -1) return memo[step][row][col];
        long ans = 0;
        for (int[] dirc : direction) {
            ans = (ans + dfs(row + dirc[0], col + dirc[1], step + 1, memo)) % MOD;
        }
        memo[step][row][col] = ans;
        return ans;
    }

    private boolean inArea(int x, int y) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    public int findPathsDP(int m, int n, int maxMove, int startRow, int startColumn) {
        int[][][] dp = new int[m][n][maxMove + 1];
        // 移动步数2的都是从移动步数1的转移来的
        // 移动步数3的都是从移动步数2的转移来的
        // 所以，要从移动步数从1开始递增
        for (int k = 1; k <= maxMove; k++) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    // 处理四条边
                    if (i == 0) dp[i][j][k]++;
                    if (j == 0) dp[i][j][k]++;
                    if (i == m - 1) dp[i][j][k]++;
                    if (j == n - 1) dp[i][j][k]++;

                    // 中间的位置，向四个方向延伸
                    for (int[] dir : direction) {
                        int nextI = i + dir[0];
                        int nextJ = j + dir[1];
                        if (inArea(nextI, nextJ)) {
                            dp[i][j][k] = (dp[i][j][k] + dp[nextI][nextJ][k - 1]) % MOD;
                        }
                    }
                }
            }
        }

        return dp[startRow][startColumn][maxMove];
    }

    // 698 划分为K个相等的子集 Hard
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int tot = 0;
        for (int x : nums) tot += x;
        if (tot % k != 0) return false; // 可行性剪枝
        Arrays.sort(nums);
        int n = nums.length;
        int t = tot / k;
        return dfs(n - 1, 0, 0, new boolean[n], k, t, nums);
    }

    boolean dfs(int idx, int cur, int cnt, boolean[] vis, int k, int t, int[] nums) {
        if (cnt == k) return true;
        if (cur == t) return dfs(nums.length - 1, 0, cnt + 1, vis, k, t, nums);
        if (idx == -1) return false;
        for (int i = idx; i >= 0; i--) {  // 顺序性剪枝
            if (vis[i] || cur + nums[i] > t) continue;
            vis[i] = true;
            if (dfs(i - 1, cur + nums[i], cnt, vis, k, t, nums)) return true;
            vis[i] = false;
            if (cur == 0) return false; // 可行性剪枝
        }
        return false;
    }

    // 691 贴纸拼词
    public int minStickers(String[] stickers, String target) {
        int n = target.length();
        //用n位表示当前状态，若某一位为1则为该字母还没被消费，为0表示已经被消费
        int[] memo = new int[1 << n];
        //初始化的操作;
        Arrays.fill(memo, -1);
        //全部都被消费掉的状态，为""字符串，不需要被消费，即需要的卡片为0
        memo[0] = 0;
        //(1 << n) - 1的状态就是111...111 (n个1)表示当前没有被消费掉的字母
        //即传入target字符串
        int res = dfs(stickers, target, memo, (1 << n) - 1);
        //由于初始化res为n + 1,又因为只有当前卡片消费了target字母才会递归消费，所以均不能消费后，res为n + 1
        return res <= n ? res : -1;
    }

    private int dfs(String[] stickers, String target, int[] memo, int mask) {
        int n = target.length();
        if (memo[mask] >= 0) return memo[mask];
        //作为最后判断的依据，若res比n+1小，说明有被消费掉的字母，如果没有变小，则说明不能被消费
        int res = n + 1;
        for (String sticker : stickers) {
            //获取剩余没被使用的字符left，这里初始化赋值
            int left = mask;
            int[] charCount = new int[26];
            for (int i = 0; i < sticker.length(); i++) {
                charCount[sticker.charAt(i) - 'a']++;
            }
            for (int i = 0; i < n; i++) {
                int idx = target.charAt(i) - 'a';
                //mask >> j & 1表示获取当前target的第j个字母，为1表示未被消费，为0表示被消费
                //cn[] > 0 表示当前卡片的字母是够的，所以可以在使用一张该卡片的情况下消费掉cn[]这么多的对应字母
                if (((mask >> i) & 1) == 1 && charCount[idx] > 0) {
                    charCount[idx]--;
                    //表示当前target的低位j位字母被消费掉了，如111...110。
                    left ^= (1 << i);
                }
            }
            //left如果小于mask，说明中间有的字母被消费掉了，所以可以继续消费其他卡片
            if (left < mask) {
                //当前剩余字母消费情况由二进制left确定，如110011...10
                //传入剩余字母情况left，进行递归
                //+1表示只消费了一张卡片，dfs返回的值为最少的卡片数
                res = Math.min(res, dfs(stickers, target, memo, left) + 1);
            }
        }
        //做记忆化处理
        memo[mask] = res;
        return res;
    }

    //2684. 矩阵中移动的最大次数
    int[][] grid2684;

    public int maxMoves(int[][] grid) {
        this.m = grid.length;
        this.n = grid[0].length;
        this.grid2684 = grid;
        int ans = 0;
        int[][] memo = new int[m][n];
        for (int i = 0; i < m; i++) {
            ans = Math.max(dfs2684(i, 0, memo), ans);
        }
        return ans;
    }

    private int dfs2684(int i, int j, int[][] memo) {
        if (j == n - 1) return 0;
        if (memo[i][j] > 0) return memo[i][j];
        int ans = 0;
        for (int k = Math.max(i - 1, 0); k <= Math.min(i + 1, m - 1); k++) {
            if (grid2684[k][j + 1] > grid2684[i][j]) {
                ans = Math.max(ans, dfs2684(k, j + 1, memo) + 1);
            }
        }
        return memo[i][j] = ans;
    }

    //2698. 求一个整数的惩罚数
    public int punishmentNumber(int n) {
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            char[] chars = String.valueOf(i * i).toCharArray();
            sum[i] = sum[i - 1] + (dfs(chars, i, 0, 0) ? i * i : 0);
        }
        return sum[n];
    }

    private boolean dfs(char[] chars, int i, int idx, int sum) {
        if (idx == chars.length) {
            return sum == i;
        }
        int x = 0;
        for (int k = idx; k < chars.length; k++) {
            x = x * 10 + chars[k] - '0';
            if (dfs(chars, i, k + 1, sum + x)) return true;
        }
        return false;
    }

    //2707. 字符串中的额外字符
    public int minExtraChar(String s, String[] dictionary) {
        Set<String> dict = new HashSet<>();
        for (String word : dictionary) {
            dict.add(word);
        }
        int n = s.length();
        int[] memo = new int[n];
        Arrays.fill(memo, -1);
        return dfs(s, n - 1, memo, dict);
    }

    // 从0到i的字符串最小剩几个字符
    private int dfs(String s, int i, int[] memo, Set<String> dict) {
        if (i < 0) return 0;
        if (memo[i] != -1) return memo[i];
        int ans = dfs(s, i - 1, memo, dict) + 1;// 不选当前字符i
        for (int j = 0; j <= i; j++) {
            String word = s.substring(j, i + 1);
            if (dict.contains(word)) {
                ans = Math.min(ans, dfs(s, j - 1, memo, dict));
            }
        }
        memo[i] = ans;
        return ans;
    }

    //1240. 铺瓷砖
    int ans1240;

    public int tilingRectangle(int n, int m) {
        ans1240 = Math.max(n, m);
        boolean[][] rect = new boolean[n][m];
        dfs(0, 0, rect, 0);
        return ans1240;
    }

    public void dfs(int x, int y, boolean[][] rect, int cnt) {
        int n = rect.length, m = rect[0].length;
        if (cnt >= ans1240) {
            return;
        }
        if (x >= n) {
            ans1240 = cnt;
            return;
        }
        /* 检测下一行 */
        if (y >= m) {
            dfs(x + 1, 0, rect, cnt);
            return;
        }
        /* 如当前已经被覆盖，则直接尝试下一个位置 */
        if (rect[x][y]) {
            dfs(x, y + 1, rect, cnt);
            return;
        }

        for (int k = Math.min(n - x, m - y); k >= 1 && isAvailable(rect, x, y, k); k--) {
            /* 将长度为 k 的正方形区域标记覆盖 */
            fillUp(rect, x, y, k, true);
            /* 跳过 k 个位置开始检测 */
            dfs(x, y + k, rect, cnt + 1);
            fillUp(rect, x, y, k, false);
        }
    }

    public boolean isAvailable(boolean[][] rect, int x, int y, int k) {
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (rect[x + i][y + j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void fillUp(boolean[][] rect, int x, int y, int k, boolean val) {
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                rect[x + i][y + j] = val;
            }
        }
    }

    //913 猫和老鼠
    public int catMouseGame(int[][] graph) {
        int n = graph.length;
        int[][][] memo = new int[n][n][2 * n * (n - 1)];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(memo[i][j], -1);
            }
        }
        return dfs(graph, memo, 2, 1, 0);
    }

    // 0 平局 1 mouseWin 2 catWin
    private int dfs(int[][] graph, int[][][] memo, int catPos, int mousePos, int turns) {
        int n = graph.length;
        if (turns >= 2 * n * (n - 1)) return 0;
        if (memo[catPos][mousePos][turns] >= 0) return memo[catPos][mousePos][turns];
        if (mousePos == 0) return memo[catPos][mousePos][turns] = 1;
        if (catPos == mousePos) return memo[catPos][mousePos][turns] = 2;
        // turns 为偶数是轮到老鼠走，为奇数是轮到猫走
        if (turns % 2 == 0) {
            // 老鼠走最坏情况猫赢
            int ans = 2;
            for (int near : graph[mousePos]) {
                int nearAns = dfs(graph, memo, catPos, near, turns + 1);
                //如果老鼠赢，直接返回
                if (nearAns == 1) return memo[catPos][mousePos][turns] = 1;
                // 如果平局，是当前最优，暂存结果，观察其他临近点结果，如果有赢直接返回
                if (nearAns == 0) ans = 0;
            }
            return memo[catPos][mousePos][turns] = ans;
        } else {
            int ans = 1;
            for (int near : graph[catPos]) {
                if (near == 0) continue;
                int nearAns = dfs(graph, memo, near, mousePos, turns + 1);
                if (nearAns == 2) return memo[catPos][mousePos][turns] = 2;
                if (nearAns == 0) ans = 0;
            }
            return memo[catPos][mousePos][turns] = ans;
        }
    }

    static final int MOUSE_TURN = 0, CAT_TURN = 1;
    static final int DRAW = 0, MOUSE_WIN = 1, CAT_WIN = 2;
    int[][] graph;
    int[][][] degrees;
    int[][][] results;

    public int catMouseGameTuppu(int[][] graph) {
        int n = graph.length;
        this.graph = graph;
        this.degrees = new int[n][n][2];
        this.results = new int[n][n][2];
        Queue<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < n; j++) {
                degrees[i][j][MOUSE_TURN] = graph[i].length;
                degrees[i][j][CAT_TURN] = graph[j].length;
            }
        }
        for (int node : graph[0]) {
            for (int i = 0; i < n; i++) {
                degrees[i][node][CAT_TURN]--;
            }
        }
        for (int j = 1; j < n; j++) {
            results[0][j][MOUSE_TURN] = MOUSE_WIN;
            results[0][j][CAT_TURN] = MOUSE_WIN;
            queue.offer(new int[]{0, j, MOUSE_TURN});
            queue.offer(new int[]{0, j, CAT_TURN});
        }
        for (int i = 1; i < n; i++) {
            results[i][i][MOUSE_TURN] = CAT_WIN;
            results[i][i][CAT_TURN] = CAT_WIN;
            queue.offer(new int[]{i, i, MOUSE_TURN});
            queue.offer(new int[]{i, i, CAT_TURN});
        }
        while (!queue.isEmpty()) {
            int[] state = queue.poll();
            int mouse = state[0], cat = state[1], turn = state[2];
            int result = results[mouse][cat][turn];
            List<int[]> prevStates = getPrevStates(mouse, cat, turn);
            for (int[] prevState : prevStates) {
                int prevMouse = prevState[0], prevCat = prevState[1], prevTurn = prevState[2];
                if (results[prevMouse][prevCat][prevTurn] == DRAW) {
                    boolean canWin = (result == MOUSE_WIN && prevTurn == MOUSE_TURN) || (result == CAT_WIN && prevTurn == CAT_TURN);
                    if (canWin) {
                        results[prevMouse][prevCat][prevTurn] = result;
                        queue.offer(new int[]{prevMouse, prevCat, prevTurn});
                    } else {
                        degrees[prevMouse][prevCat][prevTurn]--;
                        if (degrees[prevMouse][prevCat][prevTurn] == 0) {
                            int loseResult = prevTurn == MOUSE_TURN ? CAT_WIN : MOUSE_WIN;
                            results[prevMouse][prevCat][prevTurn] = loseResult;
                            queue.offer(new int[]{prevMouse, prevCat, prevTurn});
                        }
                    }
                }
            }
        }
        return results[1][2][MOUSE_TURN];
    }

    public List<int[]> getPrevStates(int mouse, int cat, int turn) {
        List<int[]> prevStates = new ArrayList<int[]>();
        int prevTurn = turn == MOUSE_TURN ? CAT_TURN : MOUSE_TURN;
        if (prevTurn == MOUSE_TURN) {
            for (int prev : graph[mouse]) {
                prevStates.add(new int[]{prev, cat, prevTurn});
            }
        } else {
            for (int prev : graph[cat]) {
                if (prev != 0) {
                    prevStates.add(new int[]{mouse, prev, prevTurn});
                }
            }
        }
        return prevStates;
    }

    static final int MAX_MOVES = 1000;
    int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    int rows, cols;
    String[] grid;
    int catJump, mouseJump;
    int food;
    int[][][][] results2;

    public boolean canMouseWin(String[] grid, int catJump, int mouseJump) {
        this.rows = grid.length;
        this.cols = grid[0].length();
        this.grid = grid;
        this.catJump = catJump;
        this.mouseJump = mouseJump;
        int startMouse = -1, startCat = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char c = grid[i].charAt(j);
                if (c == 'M') {
                    startMouse = getPos(i, j);
                } else if (c == 'C') {
                    startCat = getPos(i, j);
                } else if (c == 'F') {
                    food = getPos(i, j);
                }
            }
        }
        int total = rows * cols;
        degrees = new int[total][total][2];
        results2 = new int[total][total][2][2];
        Queue<int[]> queue = new ArrayDeque<int[]>();
        // 计算每个状态的度
        for (int mouse = 0; mouse < total; mouse++) {
            int mouseRow = mouse / cols, mouseCol = mouse % cols;
            if (grid[mouseRow].charAt(mouseCol) == '#') {
                continue;
            }
            for (int cat = 0; cat < total; cat++) {
                int catRow = cat / cols, catCol = cat % cols;
                if (grid[catRow].charAt(catCol) == '#') {
                    continue;
                }
                degrees[mouse][cat][MOUSE_TURN]++;
                degrees[mouse][cat][CAT_TURN]++;
                for (int[] dir : dirs) {
                    for (int row = mouseRow + dir[0], col = mouseCol + dir[1], jump = 1; row >= 0 && row < rows && col >= 0 && col < cols && grid[row].charAt(col) != '#' && jump <= mouseJump; row += dir[0], col += dir[1], jump++) {
                        int nextMouse = getPos(row, col), nextCat = getPos(catRow, catCol);
                        degrees[nextMouse][nextCat][MOUSE_TURN]++;
                    }
                    for (int row = catRow + dir[0], col = catCol + dir[1], jump = 1; row >= 0 && row < rows && col >= 0 && col < cols && grid[row].charAt(col) != '#' && jump <= catJump; row += dir[0], col += dir[1], jump++) {
                        int nextMouse = getPos(mouseRow, mouseCol), nextCat = getPos(row, col);
                        degrees[nextMouse][nextCat][CAT_TURN]++;
                    }
                }
            }
        }
        // 猫和老鼠在同一个单元格，猫获胜
        for (int pos = 0; pos < total; pos++) {
            int row = pos / cols, col = pos % cols;
            if (grid[row].charAt(col) == '#') {
                continue;
            }
            results2[pos][pos][MOUSE_TURN][0] = CAT_WIN;
            results2[pos][pos][MOUSE_TURN][1] = 0;
            results2[pos][pos][CAT_TURN][0] = CAT_WIN;
            results2[pos][pos][CAT_TURN][1] = 0;
            queue.offer(new int[]{pos, pos, MOUSE_TURN});
            queue.offer(new int[]{pos, pos, CAT_TURN});
        }
        // 猫和食物在同一个单元格，猫获胜
        for (int mouse = 0; mouse < total; mouse++) {
            int mouseRow = mouse / cols, mouseCol = mouse % cols;
            if (grid[mouseRow].charAt(mouseCol) == '#' || mouse == food) {
                continue;
            }
            results2[mouse][food][MOUSE_TURN][0] = CAT_WIN;
            results2[mouse][food][MOUSE_TURN][1] = 0;
            results2[mouse][food][CAT_TURN][0] = CAT_WIN;
            results2[mouse][food][CAT_TURN][1] = 0;
            queue.offer(new int[]{mouse, food, MOUSE_TURN});
            queue.offer(new int[]{mouse, food, CAT_TURN});
        }
        // 老鼠和食物在同一个单元格且猫和食物不在同一个单元格，老鼠获胜
        for (int cat = 0; cat < total; cat++) {
            int catRow = cat / cols, catCol = cat % cols;
            if (grid[catRow].charAt(catCol) == '#' || cat == food) {
                continue;
            }
            results2[food][cat][MOUSE_TURN][0] = MOUSE_WIN;
            results2[food][cat][MOUSE_TURN][1] = 0;
            results2[food][cat][CAT_TURN][0] = MOUSE_WIN;
            results2[food][cat][CAT_TURN][1] = 0;
            queue.offer(new int[]{food, cat, MOUSE_TURN});
            queue.offer(new int[]{food, cat, CAT_TURN});
        }
        // 拓扑排序
        while (!queue.isEmpty()) {
            int[] state = queue.poll();
            int mouse = state[0], cat = state[1], turn = state[2];
            int result = results2[mouse][cat][turn][0];
            int moves = results2[mouse][cat][turn][1];
            List<int[]> prevStates = getPrevStates2(mouse, cat, turn);
            for (int[] prevState : prevStates) {
                int prevMouse = prevState[0], prevCat = prevState[1], prevTurn = prevState[2];
                if (results2[prevMouse][prevCat][prevTurn][0] == DRAW) {
                    boolean canWin = (result == MOUSE_WIN && prevTurn == MOUSE_TURN) || (result == CAT_WIN && prevTurn == CAT_TURN);
                    if (canWin) {
                        results2[prevMouse][prevCat][prevTurn][0] = result;
                        results2[prevMouse][prevCat][prevTurn][1] = moves + 1;
                        queue.offer(new int[]{prevMouse, prevCat, prevTurn});
                    } else {
                        degrees[prevMouse][prevCat][prevTurn]--;
                        if (degrees[prevMouse][prevCat][prevTurn] == 0) {
                            int loseResult = prevTurn == MOUSE_TURN ? CAT_WIN : MOUSE_WIN;
                            results2[prevMouse][prevCat][prevTurn][0] = loseResult;
                            results2[prevMouse][prevCat][prevTurn][1] = moves + 1;
                            queue.offer(new int[]{prevMouse, prevCat, prevTurn});
                        }
                    }
                }
            }
        }
        return results2[startMouse][startCat][MOUSE_TURN][0] == MOUSE_WIN && results2[startMouse][startCat][MOUSE_TURN][1] <= MAX_MOVES;
    }

    public List<int[]> getPrevStates2(int mouse, int cat, int turn) {
        List<int[]> prevStates = new ArrayList<int[]>();
        int mouseRow = mouse / cols, mouseCol = mouse % cols;
        int catRow = cat / cols, catCol = cat % cols;
        int prevTurn = turn == MOUSE_TURN ? CAT_TURN : MOUSE_TURN;
        int maxJump = prevTurn == MOUSE_TURN ? mouseJump : catJump;
        int startRow = prevTurn == MOUSE_TURN ? mouseRow : catRow;
        int startCol = prevTurn == MOUSE_TURN ? mouseCol : catCol;
        prevStates.add(new int[]{mouse, cat, prevTurn});
        for (int[] dir : dirs) {
            for (int i = startRow + dir[0], j = startCol + dir[1], jump = 1; i >= 0 && i < rows && j >= 0 && j < cols && grid[i].charAt(j) != '#' && jump <= maxJump; i += dir[0], j += dir[1], jump++) {
                int prevMouseRow = prevTurn == MOUSE_TURN ? i : mouseRow;
                int prevMouseCol = prevTurn == MOUSE_TURN ? j : mouseCol;
                int prevCatRow = prevTurn == MOUSE_TURN ? catRow : i;
                int prevCatCol = prevTurn == MOUSE_TURN ? catCol : j;
                int prevMouse = getPos(prevMouseRow, prevMouseCol);
                int prevCat = getPos(prevCatRow, prevCatCol);
                prevStates.add(new int[]{prevMouse, prevCat, prevTurn});
            }
        }
        return prevStates;
    }

    public int getPos(int row, int col) {
        return row * cols + col;
    }

    //endregion-------------------------------------------------------------
    //region ---------------------------------线性DP------------------------------------------------------
    // 70 爬楼梯
    //假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
// 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
    public int climbStairs(int n) {
        int p = 0, q = 1, r = 0;
        for (int i = 1; i <= n; i++) {
            r = q + p;
            p = q;
            q = r;
        }
        return r;
    }

    // 91 解码方法
    //一条包含字母 A-Z 的消息通过以下映射进行了 编码 ：
//'A' -> "1"
//'B' -> "2"
//...
//'Z' -> "26"
// 要 解码 已编码的消息，所有数字必须基于上述映射的方法，反向映射回字母（可能有多种方法）。例如，"11106" 可以映射为：
// "AAJF" ，将消息分组为 (1 1 10 6)"KJF" ，将消息分组为 (11 10 6)
// 注意，消息不能分组为 (1 11 06) ，因为 "06" 不能映射为 "F" ，这是由于 "6" 和 "06" 在映射中并不等价。
// 给你一个只含数字的 非空 字符串 s ，请计算并返回 解码 方法的 总数 。
    public int numDecodings(String s) {
        char[] chars = s.toCharArray();
        int[] f = new int[chars.length + 1];
        f[0] = 1;
        for (int i = 1; i < f.length; i++) {
            if (chars[i - 1] != '0') {
                f[i] = f[i - 1];
            }
            if (i > 1 && chars[i - 2] != '0' && ((chars[i - 2] - '0') * 10 + (chars[i - 1] - '0')) <= 26) {
                f[i] += f[i - 2];
            }
        }
        return f[f.length - 1];
    }

    // offer 46
    //给定一个数字，我们按照如下规则把它翻译为字符串：0 翻译成 “a” ，1 翻译成 “b”，……，11 翻译成 “l”，……，25 翻译成 “z”。一个数字可
//能有多个翻译。请编程实现一个函数，用来计算一个数字有多少种不同的翻译方法。
// 输入: 12258
//输出: 5
//解释: 12258有5种不同的翻译，分别是"bccfi", "bwfi", "bczi", "mcfi"和"mzi"
    public int translateNum(int num) {
        String s = String.valueOf(num);
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= s.length(); i++) {
            String tmp = s.substring(i - 2, i);
            if (tmp.compareTo("10") >= 0 && tmp.compareTo("25") <= 0) {
                dp[i] = dp[i - 2] + dp[i - 1];
            } else {
                dp[i] = dp[i - 1];
            }
        }
        return dp[s.length()];
    }

    //96 不同的二叉搜索树
    public int numTrees(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        // 从2推导到n
        for (int i = 2; i <= n; i++) {
            //对当前计算的每一个i，都可能从头1到i作为根节点
            for (int j = 1; j <= i; j++) {
                dp[i] += dp[j - 1] * dp[i - j];
            }
        }
        return dp[n];
    }

    //offer 10-1 斐波那契数列
    public int fib(int n) {
        int mod = (int) 1e9 + 7;
        if (n < 2) return n;
        int p = 0, q = 1, r = 0;
        for (int i = 2; i <= n; i++) {
            r = (p + q) % mod;
            p = q % mod;
            q = r % mod;
        }
        return r % mod;
    }

    // 1137 第N个泰波那契数
    public int tribonacci(int n) {
        if (n <= 1) return n;
        if (n == 2) return 1;
        int p = 0, q = 1, r = 1;
        int ans = 0;
        for (int i = 3; i <= n; i++) {
            ans = p + q + r;
            p = q;
            q = r;
            r = ans;
        }
        return ans;
    }

    // 467 环绕字符串中唯一的子字符串
    //把字符串 s 看作 "abcdefghijklmnopqrstuvwxyz" 的无限环绕字符串，所以 s 看起来是这样的：
    // 现在给定另一个字符串 p 。返回 s 中 不同 的 p 的 非空子串 的数量 。
    // "abaab"  -> 3
    public int findSubstringInWraproundString(String p) {
        int n = p.length();
        // 以i位字符结尾的最大长度
        int[] dp = new int[26];
        dp[p.charAt(0) - 'a'] = 1;
        int num = 1;
        for (int i = 1; i < n; i++) {
            char c = p.charAt(i);
            if (c - p.charAt(i - 1) == 1 || (c - p.charAt(i - 1) == -25)) {
                num++;
            } else {
                num = 1;
            }
            dp[c - 'a'] = Math.max(num, dp[c - 'a']);
        }
        int sum = 0;
        // a b c a贡献1 b贡献2(b ab) c贡献3(c bc abc)
        for (int nm : dp) {
            sum += nm;
        }
        return sum;
    }


    //1800. 最大升序子数组和
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

    //1653. 使字符串平衡的最少删除次数
    public int minimumDeletions(String s) {
        int n = s.length();
        int[] dp = new int[n + 1];
        dp[0] = 0;
        int cntB = 0;
        for (int i = 1; i <= n; i++) {
            if (s.charAt(i - 1) == 'b') {
                dp[i] = dp[i - 1];
                cntB++;
            } else {
                dp[i] = Math.min(dp[i - 1] + 1, cntB);
            }
        }
        return dp[n];
    }

    // 2414 最长的字母序连续子字符串的长度
    // 字母序连续字符串 是由字母表中连续字母组成的字符串。换句话说，字符串 "abcdefghijklmnopqrstuvwxyz" 的任意子字符串都是 字母序连续字符串 。
// 例如，"abc" 是一个字母序连续字符串，而 "acb" 和 "za" 不是。
// 给你一个仅由小写英文字母组成的字符串 s ，返回其 最长 的 字母序连续子字符串 的长度。
// 输入：s = "abacaba" 输出：2
//解释：共有 4 个不同的字母序连续子字符串 "a"、"b"、"c" 和 "ab" 。"ab" 是最长的字母序连续子字符串。
    public int longestContinuousSubstring(String s) {
        int n = s.length();
        char[] chars = s.toCharArray();
        int[] dp = new int[n];
        dp[0] = 1;
        int max = 1;
        for (int i = 1; i < n; i++) {
            dp[i] = (chars[i] == chars[i - 1] + 1) ? dp[i - 1] + 1 : 1;
            max = Math.max(dp[i], max);
        }
        return max;
    }

    // 按位与最大的最长子数组
    public int longestSubarray(int[] nums) {
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

    // 两个元素进行与运算的结果肯定小于等于它两的最小值，最大值即为数组最大元素
    public int longestSubarray2(int[] nums) {
        int n = nums.length;
        int maxNum = Integer.MIN_VALUE;
        for (int num : nums) {
            maxNum = Math.max(maxNum, num);
        }
        int len = 0;
        int max = 0;
        for (int num : nums) {
            if (num == maxNum) {
                len++;
            } else {
                len = 0;
            }
            max = Math.max(max, len);
        }
        return max;
    }

    // 找到所有的好下标
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


    // 338 比特位计数
    //BrianKernighan  算法
    public int[] countBits(int n) {
        int[] bits = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            bits[i] = countOnes(i);
        }
        return bits;
    }

    public int countOnes(int x) {
        int ones = 0;
        while (x > 0) {
            // 消除最底位1
            x &= (x - 1);
            ones++;
        }
        return ones;
    }

    public int[] countBitsDP1(int n) {
        int[] bits = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            bits[i] = bits[i & (i - 1)] + 1;
        }
        return bits;
    }

    public int[] countBitsDP2(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 0;
        int hi = 0;
        for (int i = 1; i <= n; i++) {
            if ((i & (i - 1)) == 0) {
                hi = i;
            }
            dp[i] = dp[i - hi] + 1;
        }
        return dp;
    }

    // 746 使用最小花费爬楼梯
    //给你一个整数数组 cost ，其中 cost[i] 是从楼梯第 i 个台阶向上爬需要支付的费用。一旦你支付此费用，即可选择向上爬一个或者两个台阶。
// 你可以选择从下标为 0 或下标为 1 的台阶开始爬楼梯。请你计算并返回达到楼梯顶部的最低花费。
//输入：cost = [10,15,20] 输出：15
//解释：你将从下标为 1 的台阶开始。支付 15 ，向上爬两个台阶，到达楼梯顶部。总花费为 15 。
    public int minCostClimbingStairs(int[] cost) {
        //顶部是n
        int n = cost.length;
        int[] dp = new int[n + 1];
        dp[0] = cost[0];
        dp[1] = cost[1];
        for (int i = 2; i <= n; i++) {
            dp[i] = Math.min(dp[i - 1], dp[i - 2]) + (i < n ? cost[i] : 0);
        }
        return dp[n];
    }

    //931. 下降路径最小和
    public int minFallingPathSum(int[][] matrix) {
        int n = matrix.length;
        int[][] dp = new int[n][n];
        System.arraycopy(matrix[0], 0, dp[0], 0, n);
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int mn = dp[i - 1][j];
                if (j > 0) {
                    mn = Math.min(mn, dp[i - 1][j - 1]);
                }
                if (j < n - 1) {
                    mn = Math.min(mn, dp[i - 1][j + 1]);
                }
                dp[i][j] = mn + matrix[i][j];
            }
        }
        return Arrays.stream(dp[n - 1]).min().getAsInt();
    }

    //1289. 下降路径最小和 II
    public int minFallingPathSum2(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dp = new int[m][n];
        for (int j = 0; j < n; j++) {
            dp[0][j] = grid[0][j];
        }
        for (int i = 1; i < m; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (k == j) continue;
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][k] + grid[i][j]);
                }
            }
        }
        int min = Integer.MAX_VALUE;
        for (int j = 0; j < n; j++) {
            min = Math.min(min, dp[m - 1][j]);
        }
        return min;
    }
    public int minFallingPathSum22(int[][] grid) {
        int n = grid.length;
        int first_min_sum = 0;
        int second_min_sum = 0;
        int first_min_index = -1;

        for (int i = 0; i < n; i++) {
            int cur_first_min_sum = Integer.MAX_VALUE;
            int cur_second_min_sum = Integer.MAX_VALUE;
            int cur_first_min_index = -1;

            for (int j = 0; j < n; j++) {
                int cur_sum = (j != first_min_index ? first_min_sum : second_min_sum) + grid[i][j];
                if (cur_sum < cur_first_min_sum) {
                    cur_second_min_sum = cur_first_min_sum;
                    cur_first_min_sum = cur_sum;
                    cur_first_min_index = j;
                } else if (cur_sum < cur_second_min_sum) {
                    cur_second_min_sum = cur_sum;
                }
            }
            first_min_sum = cur_first_min_sum;
            second_min_sum = cur_second_min_sum;
            first_min_index = cur_first_min_index;
        }
        return first_min_sum;
    }

    //面试题 17.16. 按摩师
    public int massage(int[] nums) {
        int n = nums.length;
        if (n < 1) return 0;
        if (n < 2) return nums[0];
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        int max = Math.max(dp[0], dp[1]);
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
            max = Math.max(dp[i], max);
        }
        return max;
    }

    // 198 打家劫舍
    public int rob(int[] nums) {
        int[] dp = new int[nums.length + 1];
        dp[0] = 0;
        dp[1] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i + 1] = Math.max(dp[i - 1] + nums[i], dp[i]);
        }
        return dp[nums.length];
    }

    public int rob2(int[] nums) {
        int pre = 0;
        int today = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int tmp = today;
            today = Math.max(pre + nums[i], today);
            pre = tmp;
        }
        return today;
    }

    // 213 打家劫舍2
    // 环形
    public int rob213(int[] nums) {
        int length = nums.length;
        if (length == 1) {
            return nums[0];
        } else if (length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        return Math.max(robRange(nums, 0, length - 2), robRange(nums, 1, length - 1));
    }

    public int robRange(int[] nums, int start, int end) {
        int first = nums[start], second = Math.max(nums[start], nums[start + 1]);
        for (int i = start + 2; i <= end; i++) {
            int temp = second;
            second = Math.max(first + nums[i], second);
            first = temp;
        }
        return second;
    }


    // 337 打家劫舍 二叉树
    // f 偷当前root g不偷当前root
    Map<TreeNode, Integer> f = new HashMap<>();
    Map<TreeNode, Integer> g = new HashMap<>();

    public int rob337Map(TreeNode root) {
        dfs(root);
        return Math.max(f.getOrDefault(root, 0), g.getOrDefault(root, 0));
    }

    public void dfs(TreeNode node) {
        if (node == null) {
            return;
        }
        dfs(node.left);
        dfs(node.right);
        // 偷当前node+不偷左子+不偷右子
        f.put(node, node.val + g.getOrDefault(node.left, 0) + g.getOrDefault(node.right, 0));
        // 不偷当前node = 偷或不偷左子最大+偷或不偷右子最大
        g.put(node, Math.max(f.getOrDefault(node.left, 0), g.getOrDefault(node.left, 0)) + Math.max(f.getOrDefault(node.right, 0), g.getOrDefault(node.right, 0)));
    }

    public int rob337Array(TreeNode root) {
        int[] result = robDfs(root);
        return Math.max(result[0], result[1]);
    }

    private int[] robDfs(TreeNode root) {
        if (root == null) return new int[2];
        int[] left = robDfs(root.left);
        int[] right = robDfs(root.right);

        // 0 不偷当前=偷或不偷左子的最大+偷或不偷右子的最大
        // 1 偷当前= 不偷左子+不偷右子
        int[] result = new int[2];
        result[0] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        result[1] = root.val + left[0] + right[0];
        return result;
    }

    //1444. 切披萨的方案数
    public int ways1(String[] pizza, int k) {
        MatrixSum ms = new MatrixSum(pizza);
        int m = pizza.length, n = pizza[0].length();
        int[][][] memo = new int[k][m][n];
        for (int i = 0; i < k; i++)
            for (int j = 0; j < m; j++)
                Arrays.fill(memo[i][j], -1); // -1 表示没有计算过
        return dfs(k - 1, 0, 0, memo, ms, m, n);
    }

    private int dfs(int c, int i, int j, int[][][] memo, MatrixSum ms, int m, int n) {
        if (c == 0) // 递归边界：无法再切了
            return ms.query(i, j, m, n) > 0 ? 1 : 0;
        if (memo[c][i][j] != -1) // 之前计算过
            return memo[c][i][j];
        int res = 0;
        for (int j2 = j + 1; j2 < n; j2++) // 垂直切
            if (ms.query(i, j, m, j2) > 0) // 有苹果
                res = (res + dfs(c - 1, i, j2, memo, ms, m, n)) % MOD;
        for (int i2 = i + 1; i2 < m; i2++) // 水平切
            if (ms.query(i, j, i2, n) > 0) // 有苹果
                res = (res + dfs(c - 1, i2, j, memo, ms, m, n)) % MOD;
        return memo[c][i][j] = res; // 记忆化
    }

    public int ways2(String[] pizza, int k) {
        final int MOD = (int) 1e9 + 7;
        MatrixSum ms = new MatrixSum(pizza);
        int m = pizza.length, n = pizza[0].length();
        int[][][] f = new int[k][m][n];
        for (int c = 0; c < k; c++) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (c == 0) {
                        f[c][i][j] = ms.query(i, j, m, n) > 0 ? 1 : 0;
                        continue;
                    }
                    int res = 0;
                    for (int j2 = j; j2 < n; j2++) // 垂直切
                        if (ms.query(i, j, m, j2) > 0) // 有苹果
                            res = (res + f[c - 1][i][j2]) % MOD;
                    for (int i2 = i; i2 < m; i2++) // 水平切
                        if (ms.query(i, j, i2, n) > 0) // 有苹果
                            res = (res + f[c - 1][i2][j]) % MOD;
                    f[c][i][j] = res;
                }
            }
        }
        return f[k - 1][0][0];
    }

// 二维前缀和模板（'A' 的 ASCII 码最低位为 1，'.' 的 ASCII 码最低位为 0）
    static class MatrixSum {
        private final int[][] sum;

        public MatrixSum(String[] matrix) {
            int m = matrix.length, n = matrix[0].length();
            sum = new int[m + 1][n + 1];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    sum[i + 1][j + 1] = sum[i + 1][j] + sum[i][j + 1] - sum[i][j] + (matrix[i].charAt(j) & 1);
                }
            }
        }

        // 返回左上角在 (r1,c1) 右下角在 (r2-1,c2-1) 的子矩阵元素和（类似前缀和的左闭右开）
        public int query(int r1, int c1, int r2, int c2) {
            return sum[r2][c2] - sum[r2][c1] - sum[r1][c2] + sum[r1][c1];
        }
    }
    //1388. 3n 块披萨 环形问题
    public int maxSizeSlices(int[] slices) {
        int[] v1 = new int[slices.length - 1];
        int[] v2 = new int[slices.length - 1];
        System.arraycopy(slices, 1, v1, 0, slices.length - 1);
        System.arraycopy(slices, 0, v2, 0, slices.length - 1);
        int ans1 = calculate(v1);
        int ans2 = calculate(v2);
        return Math.max(ans1, ans2);
    }

    public int calculate(int[] slices) {
        int N = slices.length, n = (N + 1) / 3;
        int[][] dp = new int[N][n + 1];
        for (int i = 0; i < N; i++) {
            Arrays.fill(dp[i], Integer.MIN_VALUE);
        }
        dp[0][0] = 0;
        dp[0][1] = slices[0];
        dp[1][0] = 0;
        dp[1][1] = Math.max(slices[0], slices[1]);
        for (int i = 2; i < N; i++) {
            dp[i][0] = 0;
            for (int j = 1; j <= n; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i - 2][j - 1] + slices[i]);
            }
        }
        return dp[N - 1][n];
    }

    //1911. 最大子序列交替和
    public long maxAlternatingSum(int[] nums) {
        long even = nums[0], odd = 0;
        for (int i = 1; i < nums.length; i++) {
            even = Math.max(even, odd + nums[i]);
            odd = Math.max(odd, even - nums[i]);
        }
        return even;
    }

    //256 粉刷房子
    public int minCost(int[][] costs) {
        int n = costs.length;
        int[][] dp = new int[n][3];
        for (int i = 0; i < 3; i++) {
            dp[0][i] = costs[0][i];
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                dp[i][j] = Math.min(dp[i - 1][(j + 1) % 3], dp[i - 1][(j + 2) % 3]) + costs[i][j];
            }
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            min = Math.min(dp[n - 1][i], min);
        }
        return min;
    }

    //264 丑数
    //我们把只包含质因子 2、3 和 5 的数称作丑数（Ugly Number）。求按从小到大的顺序的第 n 个丑数。
// 输入: n = 10 输出: 12解释: 1, 2, 3, 4, 5, 6, 8, 9, 10, 12 是前 10 个丑数。
    // 优先队列做法搜nthUglyNumberPriorityQueue
    public int nthUglyNumber(int n) {
        int a = 0, b = 0, c = 0;
        int[] dp = new int[n];
        dp[0] = 1;
        //用还没乘过 2 的最小丑数乘以 2；用还没乘过 3 的最小丑数乘以 3；用还没乘过 5 的最小丑数乘以 5。然后在得到的数字中取最小，就是新的丑数。
        for (int i = 1; i < n; i++) {
            dp[i] = Math.min(Math.min(dp[a] * 2, dp[b] * 3), dp[c] * 5);
            if (dp[i] == dp[a] * 2) a++;
            if (dp[i] == dp[b] * 3) b++;
            if (dp[i] == dp[c] * 5) c++;
        }
        return dp[n - 1];
    }

    //313. 超级丑数 还有pq做法
    public int nthSuperUglyNumberDP(int n, int[] primes) {
        int[] dp = new int[n + 1];
        int m = primes.length;
        int[] pointers = new int[m];
        int[] nums = new int[m];
        Arrays.fill(nums, 1);
        for (int i = 1; i <= n; i++) {
            int minNum = Arrays.stream(nums).min().getAsInt();
            dp[i] = minNum;
            for (int j = 0; j < m; j++) {
                if (nums[j] == minNum) {
                    pointers[j]++;
                    nums[j] = dp[pointers[j]] * primes[j];
                }
            }
        }
        return dp[n];
    }

    //给定一个整数数组，a[1],a[2],...,a[n]，每一个元素a[i]可以和它右边的（a[i+1],a[i+2],...,a[n]）元素做差，
    // 求这个数组中最大的差值，例如a={0,3,9,1,3,5}这个数组最大的差值就是9-1=8;
    //求最大值-最小值的下标
    public int[] find(int[] nums) {
        if (nums == null || nums.length == 0) return new int[]{-1, -1};
        int[] dp = new int[nums.length];
        dp[nums.length - 1] = 0;
        int left = -1;
        int right = -1;
        int leftMax = Integer.MIN_VALUE;
        int rightMin = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = nums.length - 2; i >= 0; i--) {
            // 前一个差值是负数时，不用前面的累积的差值（总差值会变小）,否则继续累积
            dp[i] = dp[i + 1] < 0 ? nums[i] - nums[i + 1] : nums[i] - nums[i + 1] + dp[i + 1];
            max = Math.max(max, dp[i]);
            if (dp[i] > leftMax) {
                left = i;
            }
            if (dp[i] < rightMin) {
                right = i;
            }
        }
        return new int[]{left, right};
    }


    //在一个 m*n 的棋盘的每一格都放有一个礼物，每个礼物都有一定的价值（价值大于 0）。你可以从棋盘的左上角开始拿格子里的礼物，并每次向右或者向下移动一格、直
//到到达棋盘的右下角。给定一个棋盘及其上面的礼物的价值，请计算你最多能拿到多少价值的礼物？
//[
//  [1,3,1],
//  [1,5,1],
//  [4,2,1]
//]
//输出: 12
//解释: 路径 1→3→5→2→1 可以拿到最多价值的礼物
    public int maxValue(int[][] grid) {
        if (grid.length <= 0 || grid[0].length <= 0) return 0;
        int[][] dp = new int[grid.length][grid[0].length];
        dp[0][0] = grid[0][0];
        for (int i = 1; i < grid.length; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }
        for (int i = 1; i < grid[0].length; i++) {
            dp[0][i] = dp[0][i - 1] + grid[0][i];
        }
        for (int i = 1; i < grid.length; i++) {
            for (int j = 1; j < grid[0].length; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }
        return dp[grid.length - 1][grid[0].length - 1];
    }

    // 53 最大子数组和 dp
    // 53 最大子数组和 dp 523 连续子数组和 525 连续数组 560 和为k的连续子数组
    public int maxSubArray(int[] nums) {
        int sum = 0;
        int max = nums[0];
        for (int i : nums) {
            sum = Math.max(sum + i, i);
            max = Math.max(max, sum);
        }
        return max;
    }

    // dp
    public int maxSubArray2(int[] nums) {
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            // 以当前数结尾，加不加前一个结果
            dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
        }
        int max = Integer.MIN_VALUE;
        for (int i : dp) {
            max = Math.max(i, max);
        }
        return max;
    }

    // 分治
    public int maxSubArray3(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        return maxSubArraySum(nums, 0, nums.length - 1);
    }

    public int maxMidArraySum(int[] nums, int left, int right, int mid) {
        int sum = 0;
        int leftSum = Integer.MIN_VALUE;
        for (int i = mid; i >= left; i--) {
            sum += nums[i];
            leftSum = Math.max(leftSum, sum);
        }
        sum = 0;
        int rightSum = Integer.MIN_VALUE;
        for (int i = mid + 1; i <= right; i++) {
            sum += nums[i];
            rightSum = Math.max(rightSum, sum);
        }
        return rightSum + leftSum;
    }

    public int maxSubArraySum(int[] nums, int left, int right) {
        if (left >= right) {
            return nums[left];
        }
        int mid = (left + right) / 2;
        //连续子数组最大和 从左边，从右边，从中间到两边
        return Math.max(maxMidArraySum(nums, left, right, mid), Math.max(maxSubArraySum(nums, left, mid), maxSubArraySum(nums, mid + 1, right)));
    }

    //1749. 任意子数组和的绝对值的最大值
    public static int maxAbsoluteSum(int[] nums) {
        int n = nums.length;
        int max = Math.abs(nums[0]);
        int minSum = nums[0];
        int maxSum = nums[0];
        for (int i = 1; i < n; i++) {
            maxSum = Math.max(nums[i], maxSum + nums[i]);//=Math.max(maxSum,0)+nums[i]
            minSum = Math.min(nums[i], minSum + nums[i]);
            max = Math.max(Math.max(Math.abs(minSum), Math.abs(maxSum)), max);
        }
        return max;
    }
    //由于子数组和等于两个前缀和的差，那么取前缀和中的最大值与最小值，它俩的差就是答案。
    //如果最大值在最小值右边，那么算的是最大子数组和。
    //如果最大值在最小值左边，那么算的是最小子数组和的绝对值（相反数）
    public int maxAbsoluteSum2(int[] nums) {
        int s = 0, mx = 0, mn = 0;
        for (int x : nums) {
            s += x;
            mx = Math.max(mx, s);
            mn = Math.min(mn, s);
        }
        return mx - mn;
    }

    //2606. 找到最大开销的子字符串
    public int maximumCostSubstring(String s, String chars, int[] vals) {
        Map<Character, Integer> value = new HashMap<>();
        int n = chars.length();
        for (int i = 0; i < n; i++) {
            value.put(chars.charAt(i), vals[i]);
        }
        int m = s.length();
        int[] dp = new int[m + 1];
        int max = 0;
        for (int i = 1; i <= m; i++) {
            int v = value.getOrDefault(s.charAt(i - 1), s.charAt(i - 1) - 'a' + 1);
            dp[i] = Math.max(dp[i - 1] + v, v);
            max = Math.max(dp[i], max);
        }
        return max;
    }

    // 325 和等于k的最长子数组长度
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

    //32 最长有效括号
    public int longestValidParentheses(String s) {
        int max = 0;
        int n = s.length();
        int[] dp = new int[n];
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == ')') {
                if (s.charAt(i - 1) == '(') {
                    dp[i] = ((i >= 2) ? dp[i - 2] : 0) + 2;
                    //i_left,len=dp[i-1],i_right
                } else if (i - dp[i - 1] > 0 && s.charAt(i - dp[i - 1] - 1) == '(') {
                    dp[i] = ((i - dp[i - 1] >= 2) ? dp[i - dp[i - 1] - 2] : 0) + dp[i - 1] + 2;
                }
                max = Math.max(dp[i], max);
            }
        }
        return max;
    }

    //1031. 两个非重叠子数组的最大和 前缀和+DP+滑动窗口
    //dp[i][0]: 从 A[0]-A[i] 连续 L 长度子数组最大的元素和
    //dp[i][1]: 从 A[0]-A[i] 连续 M 长度子数组最大的元素和
    //dp[i][2]: 从 A[i]-A[A.size()-1] 连续 L 长度子数组最大的元素和
    //dp[i][3]: 从 A[i]-A[A.size()-1] 连续 M 长度子数组最大的元素和
    public int maxSumTwoNoOverlap(int[] nums, int firstLen, int secondLen) {
        int n = nums.length;
        int[] sum = new int[n];
        sum[0] = nums[0];
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + nums[i];
        }
        int[][] dp = new int[n][4];
        int max = 0;
        for (int l = 0, r = 0; r < n; r++) {
            if (r - l + 1 == firstLen) {
                max = Math.max(max, sum[r] - (l > 0 ? sum[l - 1] : 0));
                dp[r][0] = max;
                l++;
            }
        }
        max = 0;
        for (int l = 0, r = 0; r < n; r++) {
            if (r - l + 1 == secondLen) {
                max = Math.max(max, sum[r] - (l > 0 ? sum[l - 1] : 0));
                dp[r][1] = max;
                l++;
            }
        }
        max = 0;
        for (int l = n - 1, r = n - 1; l >= 0; l--) {
            if (r - l + 1 == firstLen) {
                max = Math.max(max, sum[r] - (l > 0 ? sum[l - 1] : 0));
                dp[l][2] = max;
                r--;
            }
        }
        max = 0;
        for (int l = n - 1, r = n - 1; l >= 0; l--) {
            if (r - l + 1 == secondLen) {
                max = Math.max(max, sum[r] - (l > 0 ? sum[l - 1] : 0));
                dp[l][3] = max;
                r--;
            }
        }
        int res = 0;
        for (int i = firstLen; i <= n - secondLen; i++) {
            res = Math.max(res, dp[i - 1][0] + dp[i][3]);
        }
        for (int i = secondLen; i <= n - firstLen; i++) {
            res = Math.max(res, dp[i - 1][1] + dp[i][2]);
        }
        return res;
    }

    //918. 环形子数组的最大和
    public int maxSubarraySumCircular(int[] nums) {
        // Math.max(中间连续最大,两边和最大)
        int n = nums.length;
        int prefixSum = nums[0];
        int dp = nums[0];
        // 每个位置的最大前缀和
        int[] leftMax = new int[n];
        leftMax[0] = nums[0];
        int max = nums[0];
        for (int i = 1; i < n; i++) {
            dp = Math.max(dp + nums[i], nums[i]);
            max = Math.max(dp, max);
            prefixSum += nums[i];
            leftMax[i] = Math.max(leftMax[i - 1], prefixSum);
        }
        // 从右向左枚举后缀，固定后缀，选择最大前缀
        int rightSum = 0;
        for (int i = n - 1; i > 0; i--) {
            rightSum += nums[i];
            max = Math.max(max, leftMax[i - 1] + rightSum);
        }
        return max;
    }
    // 两边反转中间连续
    public int maxSubarraySumCircular2(int[] nums) {
        // Math.max(中间连续最大,两边和最大=sum-中间连续最小)
        int n = nums.length;
        int[] dpMax = new int[n];
        dpMax[0] = nums[0];
        int[] dpMin = new int[n];
        dpMin[0] = nums[0];
        int max = nums[0];
        int min = nums[0];
        int sum = nums[0];
        for (int i = 1; i < n; i++) {
            dpMax[i] = Math.max(dpMax[i - 1] + nums[i], nums[i]);
            max = Math.max(max, dpMax[i]);
            dpMin[i] = Math.min(dpMin[i - 1] + nums[i], nums[i]);
            min = Math.min(min, dpMin[i]);
            sum += nums[i];
        }
        // 如果最大的都是负数，那么数组无正数，最小的肯定是全部元素
        if (max < 0) return max;
        return Math.max(max, sum - min);
    }

    public int maxSubarraySumCircular3(int[] nums) {
        int n = nums.length;
        Deque<int[]> deque = new ArrayDeque<>();
        deque.offer(new int[]{0, nums[0]});
        int prefixSum = nums[0];
        int max = nums[0];
        for (int i = 1; i < 2 * n; i++) {
            while (!deque.isEmpty() && i - deque.peekFirst()[0] > n) {
                deque.pollFirst();
            }
            prefixSum += nums[i % n];
            max = Math.max(max, prefixSum - deque.peekFirst()[1]);
            while (!deque.isEmpty() && deque.peekLast()[1] >= prefixSum) {
                deque.pollLast();
            }
            deque.offerLast(new int[]{i, prefixSum});
        }
        return max;
    }

    //514. 自由之路
    public int findRotateSteps(String ring, String key) {
        int n = ring.length(), m = key.length();
        List<Integer>[] pos = new List[26];
        for (int i = 0; i < 26; ++i) {
            pos[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < n; ++i) {
            pos[ring.charAt(i) - 'a'].add(i);
        }
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; ++i) {
            Arrays.fill(dp[i], 0x3f3f3f);
        }
        for (int i : pos[key.charAt(0) - 'a']) {
            dp[0][i] = Math.min(i, n - i) + 1;
        }
        for (int i = 1; i < m; ++i) {
            for (int j : pos[key.charAt(i) - 'a']) {
                for (int k : pos[key.charAt(i - 1) - 'a']) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][k] + Math.min(Math.abs(j - k), n - Math.abs(j - k)) + 1);
                }
            }
        }
        return Arrays.stream(dp[m - 1]).min().getAsInt();
    }

    //1696. 跳跃游戏 VI
    public int maxResult(int[] nums, int k) {
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offerLast(0);
        for (int i = 1; i < n; i++) {
            while (queue.peekFirst() < i - k) {
                queue.pollFirst();
            }
            dp[i] = dp[queue.peekFirst()] + nums[i];
            while (!queue.isEmpty() && dp[queue.peekLast()] <= dp[i]) {
                queue.pollLast();
            }
            queue.offerLast(i);
        }
        return dp[n - 1];
    }

    //1690. 石子游戏 VII
    //https://leetcode.cn/problems/stone-game-vii/solutions/2629582/jiao-ni-yi-bu-bu-si-kao-dong-tai-gui-hua-zktx/
    // 每一步都是最大化先手得分-后手得分 => A先手时 子问题最大化B'-A'=> A=ptA+A',B = B' => A-B=ptA+A'-B'=ptA-(B'-A')
    //dp[i][j] 从i到j选哪个可以最大化得分
    public int stoneGameVII(int[] stones) {
        int n = stones.length;
        int[] sum = new int[n + 1];
        int[][] dp = new int[n][n];

        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + stones[i];
        }
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                dp[i][j] = Math.max(sum[j + 1] - sum[i + 1] - dp[i + 1][j], sum[j] - sum[i] - dp[i][j - 1]);
            }
        }

        return dp[0][n - 1];
    }

    //887. 鸡蛋掉落
    public int superEggDrop(int k, int n) {
        int remainTestCount = 1;
        // 当扔f次覆盖的楼层数覆盖到n时跳出
        while (getConfirmFloor(remainTestCount, k) < n) {
            remainTestCount++;
        }
        return remainTestCount;
    }

    //扔remainTestCount次、eggCount个鸡蛋可以确定的楼层数
    private int getConfirmFloor(int remainTestCount, int eggCount) {
        if (remainTestCount == 1 || eggCount == 1) {
            //如果remainTestCount == 1你只能移动一次，则你只能确定第一楼是否，也就是说鸡蛋只能放在第一楼，如果碎了，则F == 0，如果鸡蛋没碎，则F == 1
            //如果eggsCount == 1鸡蛋数为1，它碎了你就没有鸡蛋了，为了保险，你只能从第一楼开始逐渐往上测试，如果第一楼碎了（同上），第一楼没碎继续测第i楼，蛋式你不可能无限制的测试，因为你只能测试remainTestCount次
            return remainTestCount;
        }
        return 1 + getConfirmFloor(remainTestCount - 1, eggCount - 1) + getConfirmFloor(remainTestCount - 1, eggCount);
    }

    public int superEggDrop2(int k, int n) {
        Map<Integer, Integer> memo = new HashMap<>();
        return dp(k, n, memo);
    }

    public int dp(int k, int n, Map<Integer, Integer> memo) {
        if (memo.containsKey(n * 100 + k)) return memo.get(n * 100 + k);
        // 0层楼只能是0
        if (n == 0) return 0;
        // 只能扔1次，从1楼扔，只能确定1和0
        if (k == 1) return n;
        int l = 1, r = n;
        int ans = Integer.MAX_VALUE;
        while (l <= r) {
            int mid = l + r >> 1;
            int broken = dp(k - 1, mid - 1, memo);
            int not_broken = dp(k, n - mid, memo);
            ans = Math.min(Math.max(broken, not_broken) + 1, ans);
            if (broken <= not_broken) {
                l = mid + 1;
                ans = Math.min(ans, not_broken + 1);
            } else {
                r = mid - 1;
                ans = Math.min(ans, broken + 1);
            }
        }
        memo.put(n * 100 + k, ans);
        return ans;
    }

    // 764 最大加号标志
    public int orderOfLargestPlusSign(int n, int[][] mines) {
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], n);
        }
        Set<Integer> banned = new HashSet<Integer>();
        for (int[] vec : mines) {
            banned.add(vec[0] * n + vec[1]);
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            int count = 0;
            /* left */
            for (int j = 0; j < n; j++) {
                if (banned.contains(i * n + j)) {
                    count = 0;
                } else {
                    count++;
                }
                dp[i][j] = Math.min(dp[i][j], count);
            }
            count = 0;
            /* right */
            for (int j = n - 1; j >= 0; j--) {
                if (banned.contains(i * n + j)) {
                    count = 0;
                } else {
                    count++;
                }
                dp[i][j] = Math.min(dp[i][j], count);
            }
        }
        for (int i = 0; i < n; i++) {
            int count = 0;
            /* up */
            for (int j = 0; j < n; j++) {
                if (banned.contains(j * n + i)) {
                    count = 0;
                } else {
                    count++;
                }
                dp[j][i] = Math.min(dp[j][i], count);
            }
            count = 0;
            /* down */
            for (int j = n - 1; j >= 0; j--) {
                if (banned.contains(j * n + i)) {
                    count = 0;
                } else {
                    count++;
                }
                dp[j][i] = Math.min(dp[j][i], count);
                ans = Math.max(ans, dp[j][i]);
            }
        }
        return ans;
    }

    // 1220 统计元音字母序列的数目
    public int countVowelPermutation(int n) {
        long[][] f = new long[n][5];
        Arrays.fill(f[0], 1);
        for (int i = 0; i < n - 1; i++) {
            // 每个元音 'a' 后面都只能跟着 'e'
            f[i + 1][1] += f[i][0];
            // 每个元音 'e' 后面只能跟着 'a' 或者是 'i'
            f[i + 1][0] += f[i][1];
            f[i + 1][2] += f[i][1];
            // 每个元音 'i' 后面 不能 再跟着另一个 'i'
            f[i + 1][0] += f[i][2];
            f[i + 1][1] += f[i][2];
            f[i + 1][3] += f[i][2];
            f[i + 1][4] += f[i][2];
            // 每个元音 'o' 后面只能跟着 'i' 或者是 'u'
            f[i + 1][2] += f[i][3];
            f[i + 1][4] += f[i][3];
            // 每个元音 'u' 后面只能跟着 'a'
            f[i + 1][0] += f[i][4];
            for (int j = 0; j < 5; j++) f[i + 1][j] %= MOD;
        }
        long ans = 0;
        for (int i = 0; i < 5; i++) ans += f[n - 1][i];
        return (int) (ans % MOD);
    }


    // 121 买卖股票最佳时机
//https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iv/solution/5xing-dai-ma-gao-ding-suo-you-gu-piao-ma-j6zo/
//只能买卖一次
    public int maxProfitOnce1(int[] prices) {
        int min_price = Integer.MAX_VALUE;
        int max_profit = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < min_price) {
                min_price = prices[i];
            } else if (prices[i] - min_price > max_profit) {
                max_profit = prices[i] - min_price;
            }
        }
        return max_profit;
    }

    public int maxProfitOnce2(int[] prices) {
        if (prices.length <= 1) {
            return 0;
        }
        int dp0 = 0;
        int dp1 = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            int newDp0 = Math.max(dp0, dp1 + prices[i]);
            int newDp1 = Math.max(dp1, -prices[i]);
            dp0 = newDp0;
            dp1 = newDp1;
        }
        return dp0;
    }

    public int maxProfitOnce3(int[] prices) {
        if (prices.length <= 1) {
            return 0;
        }
        int buy = -prices[0];
        int sell = 0;
        for (int i = 1; i < prices.length; i++) {
            buy = Math.max(buy, -prices[i]);
            sell = Math.max(sell, buy + prices[i]);
        }
        return sell;
    }

    //122 买卖股票最佳时机
    public int maxProfitNTimes(int[] prices) {
        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i = 1; i < n; ++i) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
        }
        return dp[n - 1][0];
    }

    ////空间O(1)
    public int maxProfitNTimes2(int[] prices) {
        int n = prices.length;
        int dp0 = 0, dp1 = -prices[0];
        for (int i = 1; i < n; ++i) {
            // 这里卖出的收益是 max(i-1买入)
            int newDp0 = Math.max(dp0, dp1 + prices[i]);
            int newDp1 = Math.max(dp1, dp0 - prices[i]);
            dp0 = newDp0;
            dp1 = newDp1;
        }
        return dp0;
    }

    public int maxProfitNTimes3(int[] prices) {
        int n = prices.length;
        int buy = -prices[0], sell = 0;
        for (int i = 1; i < n; ++i) {
            buy = Math.max(buy, sell - prices[i]);
            // 这里卖出是 max(i买入)，当天买当天卖利润0 不影响结果
            // 如果buy取的是i天的，那么i-1天的buy<i天的buy，buy[i-1]+prices[i]<buy[i]+prices[i]=0,sell最小是0
            sell = Math.max(sell, buy + prices[i]);
        }
        return sell;
    }

    //贪心
    public int maxProfitNTimes4(int[] prices) {
        int ans = 0;
        int n = prices.length;
        for (int i = 1; i < n; ++i) {
            ans += Math.max(0, prices[i] - prices[i - 1]);
        }
        return ans;
    }

    // 123 买卖股票最佳时机
    public int maxProfitTwice(int[] prices) {
        if (prices.length <= 1) {
            return 0;
        }
        int buy1 = -prices[0], buy2 = -prices[0];
        int sell1 = 0, sell2 = 0;
        for (int i = 1; i < prices.length; i++) {
            buy1 = Math.max(buy1, -prices[i]);
            sell1 = Math.max(sell1, buy1 + prices[i]);
            buy2 = Math.max(buy2, sell1 - prices[i]);
            sell2 = Math.max(sell2, buy2 + prices[i]);
        }
        return sell2;
    }

    // 188
    public int maxProfitKTimes(int k, int[] prices) {
        int n = prices.length;
        if (n == 0 || k == 0) return 0;
        int[][] dp = new int[2 * k][n];
        for (int i = 0; i < 2 * k; i += 2) {
            dp[i][0] = -prices[0];
            dp[i + 1][0] = 0;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < 2 * k; j += 2) {
                if (j == 0) {
                    dp[j][i] = Math.max(dp[j][i - 1], -prices[i]);
                } else {
                    dp[j][i] = Math.max(dp[j][i - 1], dp[j - 1][i] - prices[i]);
                }
                dp[j + 1][i] = Math.max(dp[j + 1][i - 1], dp[j][i] + prices[i]);
            }
        }
        return dp[2 * k - 1][n - 1];
    }

    public int maxProfitKTimes2(int k, int[] prices) {
        if (prices.length <= 1) {
            return 0;
        }
        int n = prices.length;
        //n天最多完成n/2笔交易
        k = Math.min(k, n / 2);
        int[] buy = new int[k + 1];
        int[] sell = new int[k + 1];
        for (int i = 0; i < k + 1; i++) {
            buy[i] = -prices[0];
            sell[i] = 0;
        }
        for (int i = 0; i < n; i++) {
            //对于每天的价格，计算从最多k次交易的最大值
            for (int j = 1; j < k + 1; j++) {
                buy[j] = Math.max(buy[j], sell[j - 1] - prices[i]);
                sell[j] = Math.max(sell[j], buy[j] + prices[i]);
            }
        }

        return sell[k];
    }

    //隔一天才能卖
    public int maxProfitFrozen(int[] prices) {
        int buy = -prices[0];
        int sell = 0;
        int sell_pre = 0;
        for (int i = 1; i < prices.length; i++) {
            buy = Math.max(buy, sell_pre - prices[i]);
            sell_pre = sell;
            sell = Math.max(sell, buy + prices[i]);
        }
        return sell;
    }

    //交易有fee
    public int maxProfitWithFee(int fee, int[] prices) {
        int buy = -prices[0];
        int sell = 0;
        for (int i = 1; i < prices.length; i++) {
            buy = Math.max(buy, sell - prices[i]);
            sell = Math.max(sell, buy + prices[i] - fee);
        }
        return sell;
    }

    //1186. 删除一次得到子数组最大和
    public int maximumSum(int[] arr) {
        int n = arr.length;
        // 以i结尾，删除0次和1次的最大值
        int[][] dp = new int[n][2];
        dp[0][0] = arr[0];//删除0次最大值就是arr[0]
        dp[0][1] = 0;//删除1次就是0
        int max = arr[0];
        for (int i = 1; i < n; i++) {
            // 以i结尾删除0次：前i-1次删除0次若小于0，则不选前面的i-1,从i开始选
            dp[i][0] = Math.max(dp[i - 1][0], 0) + arr[i];
            dp[i][1] = Math.max(dp[i - 1][0], dp[i - 1][1] + arr[i]);
            max = Math.max(max, Math.max(dp[i][0], dp[i][1]));
        }
        return max;
    }

    public int maximumSum2(int[] arr) {
        int n = arr.length;
        int dp0 = arr[0],dp1 = 0;
        int max = arr[0];
        for (int i = 1; i < n; i++) {
            dp1 = Math.max(dp0, dp1 + arr[i]);
            dp0 = Math.max(dp0, 0) + arr[i];
            max = Math.max(max, Math.max(dp0, dp1));
        }
        return max;
    }

    // 376. 摆动序列
    public int wiggleMaxLength(int[] nums) {
        int n = nums.length;
        int[] up = new int[n];//表示以前 i 个元素中的某一个为结尾的最长的「上升摆动序列」的长度。
        int[] down = new int[n];//表示以前 i 个元素中的某一个为结尾的最长的「下降摆动序列」的长度。
        up[0] = 1;
        down[0] = 1;
        for (int i = 1; i < n; i++) {
            if (nums[i] > nums[i - 1]) {
                //up[i]由up[i-1]转移：up是上升序列，nums[i]>nums[i-1]，对上升无贡献
                //设nums[j]是up[i-1]前最后一个上升的数
                //若nums[j]<=nums[i-1] 那么nums[i-1]是最后一个上升的数nums[i-1]代替nums[j]，由于nums[i]>nums[i-1],nums[i]代替nums[i-1],up[i]长度不变
                //若nums[j]>nums[i-1]，如果nums[j]>nums[i],nums[i]构成的up长度还是以j结尾up[i] = up[i-1],如果nums[j]<=nums[i]，nums[i]可以代替nums[j],长度不变
                //up[i]由down[i-1]转移：
                //设nums[j]是down[i-1]前最后一个下降的数
                //若nums[j]>=nums[i-1],nums[i-1]可代替nums[j]作为最后一个下降的数(down[i-1]=down[j])，nums[i]>nums[i-1]作为上升的数，up[i] = down[i-1]+1
                //若nums[j]<nums[i-1],nums[i-1]可以作为最后一个上升的数（此时down[i-1]不变=down[j],因为nums[i-1]是上升），nums[i]也可以作为最后一个上升的数，从nums[j](最后一个下降的)到nums[i]上升+1
                up[i] = Math.max(up[i - 1], down[i - 1] + 1);
                //nums[i-1]<nums[i],所以i对down的贡献可以完全由i-1替代
                down[i] = down[i - 1];
            } else if (nums[i] < nums[i - 1]) {
                up[i] = up[i - 1];
                down[i] = Math.max(up[i - 1] + 1, down[i - 1]);
            } else {
                up[i] = up[i - 1];
                down[i] = down[i - 1];
            }
        }
        return Math.max(up[n - 1], down[n - 1]);
    }

    public int wiggleMaxLength2(int[] nums) {
        int n = nums.length;
        int up = 1;
        int down = 1;
        for (int i = 1; i < n; i++) {
            if (nums[i] > nums[i - 1]) {
                up = down + 1;
            }
            if (nums[i] < nums[i - 1]) {
                down = up + 1;
            }
        }
        return Math.max(up, down);
    }

    public int wiggleMaxLengthGreedy(int[] nums) {
        int n = nums.length;
        if (n < 2) return n;
        int preDiff = nums[1] - nums[0];
        int ans = preDiff == 0 ? 1 : 2;
        for (int i = 2; i < n; i++) {
            int diff = nums[i] - nums[i - 1];
            if ((diff > 0 && preDiff <= 0) || (diff < 0 && preDiff >= 0)) {
                ans++;
                preDiff = diff;
            }
        }
        return ans;
    }

    // 801 使序列递增的最小交换次数
    public int minSwap(int[] nums1, int[] nums2) {
        int n = nums1.length;
        // 以i结尾，0是i位置不换 1是i位置换
        int[][] dp = new int[n][2];
        dp[0][1] = 1;
        for (int i = 1; i < n; i++) {
            dp[i][0] = n; // 答案不会超过 n，故初始化成 n 方便后面取 min
            dp[i][1] = n;
            if (nums1[i - 1] < nums1[i] && nums2[i - 1] < nums2[i]) {
                dp[i][0] = dp[i - 1][0];
                // i-1 也交换 i也交换
                dp[i][1] = dp[i - 1][1] + 1;
            }
            if (nums2[i - 1] < nums1[i] && nums1[i - 1] < nums2[i]) {
                // i-1交换完 i不交换,若此时f[i][0] = f[i - 1][0],说明两个条件都满足，取最小的
                dp[i][0] = Math.min(dp[i][0], dp[i - 1][1]);
                // i-1不交换 i交换,若此时f[i][1] = f[i - 1][1] + 1,说明两个条件都满足，取最小的
                dp[i][1] = Math.min(dp[i][1], dp[i - 1][0] + 1);
            }
        }
        return Math.min(dp[n - 1][0], dp[n - 1][1]);
    }

    //1105. 填充书架
    public int minHeightShelves(int[][] books, int shelfWidth) {
        int n = books.length;
        // 以i结尾的书架最小高度和
        int[] dp = new int[n + 1];
        Arrays.fill(dp, 1000000);
        dp[0] = 0;
        for (int i = 0; i < n; i++) {
            int curWidth = 0, maxHeight = 0;
            // 枚举i前面的j，对应[j,i]之间的k放在最后一层书架，高度即为[j,i]的最大高度
            for (int j = i; j >= 0; j--) {
                curWidth += books[j][0];
                if (curWidth > shelfWidth) break;
                maxHeight = Math.max(maxHeight, books[j][1]);
                dp[i + 1] = Math.min(dp[i + 1], dp[j] + maxHeight);
            }
        }
        return dp[n];
    }


    //1187. 使数组严格递增
    public int makeArrayIncreasing(int[] arr1, int[] arr2) {
        Arrays.sort(arr2);
        List<Integer> list = new ArrayList<>();
        int prev = -1;
        for (int num : arr2) {
            if (num != prev) {
                list.add(num);
                prev = num;
            }
        }
        int m = arr1.length, n = list.size();
        // 以i结尾，交换j次的最小值
        int[][] dp = new int[m + 1][Math.min(m, n) + 1];
        for (int[] d : dp) {
            Arrays.fill(d, Integer.MAX_VALUE);
        }
        dp[0][0] = -1;
        for (int i = 1; i <= m; i++) {
            for (int j = 0; j <= Math.min(i, n); j++) {
                // 保留arr1[i-1],不进行交换
                // 需要满足递增，此时最小值即为arr[i-1]
                if (arr1[i - 1] > dp[i - 1][j]) {
                    dp[i][j] = arr1[i - 1];
                }
                // 若前i-1个在j-1次交换后满足递增，那么i可以交换成一个大于dp[i-1][j-1]的最小值
                if (j > 0 && dp[i - 1][j - 1] != Integer.MAX_VALUE) {
                    // 每次找最小，找了j-1次，所以这次查找可以从j-1开始找
                    int idx = binarySearch(list, dp[i - 1][j - 1], j - 1);
                    if (idx < list.size()) {
                        dp[i][j] = Math.min(dp[i][j], list.get(idx));
                    }
                }
                if (i == m && dp[i][j] != Integer.MAX_VALUE) {
                    return j;
                }
            }
        }
        return -1;
    }

    private int binarySearch(List<Integer> list, int x, int l) {
        int r = list.size();
        while (l < r) {
            int mid = l + r >> 1;
            if (list.get(mid) > x) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    //1664. 生成平衡数组的方案数
    public int waysToMakeFair(int[] nums) {
        int n = nums.length;
        int allOdd = 0, allEven = 0;
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 0) {
                allEven += nums[i];
            } else {
                allOdd += nums[i];
            }
        }
        int oddPrefix = 0, evenPrefix = 0;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if ((i & 1) == 0) {
                // 移除i后所有奇数位置和odd：i是偶数，后面的奇偶位置改变，i后面的奇数和=所有偶数-i前面的偶数-i这个偶数=i后面的偶数和
                int odd = oddPrefix + allEven - evenPrefix - nums[i];
                // 移除i后所有偶数位置和even：i是偶数，后面的奇偶位置改变，i后面的偶数和=所有奇数-i前面的奇数和=i后面的奇数和
                int even = evenPrefix + allOdd - oddPrefix;
                if (odd == even) ans++;
                evenPrefix += nums[i];
            } else {
                int odd = oddPrefix + allEven - evenPrefix;
                int even = evenPrefix + allOdd - oddPrefix - nums[i];
                if (odd == even) ans++;
                oddPrefix += nums[i];
            }
        }
        return ans;
    }

    //1402. 做菜顺序 贪心做法见maxSatisfactionGreedy
    public int maxSatisfactionDP(int[] satisfaction) {
        int n = satisfaction.length;
        int[][] dp = new int[n + 1][n + 1];
        Arrays.sort(satisfaction);
        int res = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                dp[i][j] = dp[i - 1][j - 1] + satisfaction[i - 1] * j;
                if (j < i) {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j]);
                }
                res = Math.max(res, dp[i][j]);
            }
        }
        return res;
    }

    //2369. 检查数组是否存在有效划分
    public boolean validPartition(int[] nums) {
        int n = nums.length;
        //dp[i] 前i个数字能否等效划分
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        for (int i = 2; i <= n; i++) {
            if (dp[i - 2] && nums[i - 2] == nums[i - 1]) {
                dp[i] = true;
            }
            if (i > 2 && dp[i - 3] && nums[i - 1] == nums[i - 2] && nums[i - 1] == nums[i - 3]) {
                dp[i] = true;
            }
            if (i > 2 && dp[i - 3] && nums[i - 1] == nums[i - 2] + 1 && nums[i - 2] == nums[i - 3] + 1) {
                dp[i] = true;
            }
        }
        return dp[n];
    }

    //2008. 出租车的最大盈利
    public long maxTaxiEarnings(int n, int[][] rides) {
        Map<Integer, List<int[]>> map = new HashMap<>();
        for (int[] ride : rides) {
            List<int[]> endList = map.computeIfAbsent(ride[1], k -> new ArrayList<>());
            endList.add(ride);
        }
        long[] dp = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = dp[i - 1];
            for (int[] ride : map.getOrDefault(i, new ArrayList<>())) {
                dp[i] = Math.max(dp[i], dp[ride[0]] + ride[1] - ride[0] + ride[2]);
            }
        }
        return dp[n];
    }

    //2370. 最长理想子序列
    public int longestIdealString(String s, int k) {
        int n = s.length();
        int[] dp = new int[26];
        for (int i = 0; i < n; i++) {
            int c = s.charAt(i) - 'a';
            for (int j = Math.max(0, c - k); j <= Math.min(c + k, 25); j++) {
                dp[c] = Math.max(dp[j], dp[c]);
            }
            dp[c]++;
        }
        int max = 0;
        for (int i = 0; i < 26; i++) {
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    //2272. 最大波动的子字符串
    public int largestVariance(String s) {
        /*
        枚举+dp:
        1.出现次数最多与最少的字母,必定是a-z中的一对不同字母的组合,记出现次数最多的为x,出现次数少的为b
        2.枚举x与y的二维组合,记字符串中的x=1,y=1,然后其余的字母为0,那么其出现次数的差转化为子数组的和
            参考:最大子数组的和就可以用动态规划进行处理!
        3.有一点主要注意的是,x与y在子串中均需要出现(其实是保证y要出现),因此其转移方程就有不同
            记:dp[i][0]为以s[i-1]结尾的子串和的最大值
                dp[i][1]为以s[i-1]结尾的(且包含y)子串和的最大值
                记s[i-1]对应的数字为v,即s[i]=x,v=1;s[i]=y,v=-1;s[i]=其他,v=0
            3.1 显然:dp[i][0]=max(dp[i-1][0]+v,v)
            3.2 而dp[i-1]的转移就需要看s[i-1]是否为y决定的
                3.2.1 当s[i-1]==y时,有两种转移途径,要么自成一体,要么拉上前面的dp[i-1][0],两者取最大值
                (这种情况前面有没有y都行,必定是没有y的会更大,因此可以直接舍弃dp[i-1][1])
                    即dp[i][1]=max(dp[i-1][0]+v,v)
                3.2.2 当s[i-1]!=y时,就只能从前面有y的继承过来,dp[i][1]=dp[i-1][1]+v
            遍历过程中维护最大的dp[i][1]就是组合x与y的最大波动值max
        4.最后维护好每种组合对应的max就是答案
         */
        // 空间优化
        int len = s.length();
        int res = 0;
        int dp0, dp1;
        // 枚举x与y的字母组合
        for (int x = 'a'; x <= 'z'; x++) {
            for (int y = 'a'; y <= 'z'; y++) {
                if (x == y) continue;
                // 初始化为dp[i][0]与dp[i][1]不可能达到的值
                dp0 = -len;
                dp1 = -len;
                int max = 0;
                // 遍历字符串s
                for (int i = 1; i <= len; i++) {
                    // s[i-1]对应的数字
                    int v = (s.charAt(i - 1) == x) ? 1 : (s.charAt(i - 1) == y) ? -1 : 0;
                    if (v == -1) {
                        // s[i-1]==y
                        dp1 = Math.max(dp0 + v, v);
                    } else {
                        // s[i-1]!=y
                        dp1 = dp1 + v;
                    }
                    // 注意dp1运算需要用到旧的dp0因此dp0必须后面算
                    dp0 = Math.max(dp0 + v, v);
                    max = Math.max(max, dp1);
                }
                res = Math.max(res, max);
            }
        }
        return res;
    }

    //152 乘积最大子数组
    public int maxProduct1(int[] nums) {
        int[] maxF = new int[nums.length];
        int[] minF = new int[nums.length];
        System.arraycopy(nums, 0, maxF, 0, nums.length);
        System.arraycopy(nums, 0, minF, 0, nums.length);
        for (int i = 1; i < nums.length; i++) {
            maxF[i] = Math.max(nums[i], Math.max(nums[i] * maxF[i - 1], nums[i] * minF[i - 1]));
            minF[i] = Math.min(nums[i], Math.min(nums[i] * maxF[i - 1], nums[i] * minF[i - 1]));
        }
        int max = Integer.MIN_VALUE;
        for (int i : maxF) {
            max = Math.max(max, i);
        }
        return max;
    }

    public int maxProduct(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int max = Integer.MIN_VALUE;
        int preMax = nums[0];
        int preMin = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int currMax = Math.max(nums[i], Math.max(preMax * nums[i], preMin * nums[i]));
            int currMin = Math.min(nums[i], Math.min(preMin * nums[i], preMax * nums[i]));
            max = Math.max(max, currMax);
            preMax = currMax;
            preMin = currMin;
        }
        return max;
    }

    // 318 最大单词长度乘积
    //给你一个字符串数组 words ，找出并返回 length(words[i]) * length(words[j]) 的最大值，并且这两个单词不含有公共字母。如果不存在这样的两个单词，返回 0 。
    // offer 005
    public int maxProduct(String[] words) {
        Map<Integer, Integer> map = new HashMap<>();
        for (String s : words) {
            int bitMask = 0;
            for (char c : s.toCharArray()) {
                bitMask |= (1 << c - 'a');
            }
            map.put(bitMask, Math.max(s.length(), map.getOrDefault(bitMask, 0)));
        }
        int max = 0;
        for (int a : map.keySet()) {
            for (int b : map.keySet()) {
                if ((a & b) == 0) {
                    max = Math.max(max, map.get(a) * map.get(b));
                }
            }
        }
        return max;
    }

    // 926 将字符串反转到单调递增 LIS
    //如果一个二进制字符串，是以一些 0（可能没有 0）后面跟着一些 1（也可能没有 1）的形式组成的，那么该字符串是 单调递增 的。
// 给你一个二进制字符串 s，你可以将任何 0 翻转为 1 或者将 1 翻转为 0 。
// 返回使 s 单调递增的最小翻转次数。
    public int minFlipsMonoIncr(String s) {
        int n = s.length();
        //第i个翻转成0的最小次数和翻转成1 的最小次数
        int[][] dp = new int[n][2];
        if (s.charAt(0) == '0') {
            dp[0][0] = 0;
            dp[0][1] = 1;
        } else {
            dp[0][0] = 1;
            dp[0][1] = 0;
        }
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == '0') {
                dp[i][0] = dp[i - 1][0];
                dp[i][1] = Math.min(dp[i - 1][0], dp[i - 1][1]) + 1;
            } else {
                dp[i][0] = dp[i - 1][0] + 1;
                dp[i][1] = Math.min(dp[i - 1][0], dp[i - 1][1]);
            }
        }
        return Math.min(dp[n - 1][0], dp[n - 1][1]);
    }

    public int minFlipsMonoIncr2(String s) {
        int n = s.length();
        //当前反转成0满足单调递增的最小次数和当前反转成1满足单调递增的最小次数
        int dp0 = 0, dp1 = 0;
        int newDp0 = 0, newDp1 = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '1') {
                newDp0 = dp0 + 1;//当前翻转成0=之前翻转成0+1
                newDp1 = Math.min(dp0, dp1);// 当前不翻转,之前取小的即可满足i处单调递增
            } else {
                newDp0 = dp0; //当前不翻转
                newDp1 = Math.min(dp0, dp1) + 1;//当前翻转
            }
            dp0 = newDp0;
            dp1 = newDp1;
        }
        return Math.min(dp0, dp1);
    }

    // 前缀和做法 LIS
    public int minFlipsMonoIncrPrefixSum(String s) {
        int n = s.length();
        int[] leftOnes = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            leftOnes[i] = leftOnes[i - 1] + s.charAt(i - 1) - '0';
        }
        //              101
        // leftOnes[i] 0112  left[1] 就是 第1个字符的左边，不含第一个，含第0个
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i <= n; i++) {
            //枚举以i为界限，左边全反转成0，右边全反转成1
            //leftOnes[i]左边多少个1，全部反转成0；leftOnes[n] - leftOnes[i] [i,n-1]之间有多少个1
            ans = Math.min(ans, leftOnes[i] + ((n - i) - (leftOnes[n] - leftOnes[i])));
        }
        return ans;
    }

    //1997. 访问完所有房间的第一天
    public int firstDayBeenInAllRooms(int[] nextVisit) {
        final long MOD = 1_000_000_007;
        int n = nextVisit.length;
        long[] s = new long[n];
        for (int i = 0; i < n - 1; i++) {
            int j = nextVisit[i];
            s[i + 1] = (s[i] * 2 - s[j] + 2 + MOD) % MOD; // + MOD 避免算出负数
        }
        return (int) s[n - 1];
    }

    //1824. 最少侧跳次数
    public int minSideJumps(int[] obstacles) {
        int n = obstacles.length - 1;
        int[][] dp = new int[n + 1][3];
        for (int[] array : dp) {
            Arrays.fill(array, Integer.MAX_VALUE);
        }
        dp[0][1] = 0;
        dp[0][0] = dp[0][2] = 1;
        for (int i = 1; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < 3; j++) {
                if (obstacles[i] - 1 != j) {
                    dp[i][j] = dp[i - 1][j];
                }
                min = Math.min(min, dp[i][j]);
            }
            for (int j = 0; j < 3; j++) {
                if (obstacles[i] - 1 != j) {
                    dp[i][j] = Math.min(dp[i][j], min + 1);
                }
            }
        }
        return Math.min(dp[n][0], Math.min(dp[n][1], dp[n][2]));
    }

    //1883. 准时抵达会议现场的最小跳过休息次数
    // 可忽略误差
    static final double EPS = 1e-7;
    // 极大值
    static final double INFTY = 1e20;

    public int minSkips(int[] dist, int speed, int hoursBefore) {
        int n = dist.length;
        double[][] f = new double[n + 1][n + 1];
        for (int i = 0; i <= n; ++i) {
            Arrays.fill(f[i], INFTY);
        }
        f[0][0] = 0;
        for (int i = 1; i <= n; ++i) {
            for (int j = 0; j <= i; ++j) {
                if (j != i) {
                    f[i][j] = Math.min(f[i][j], Math.ceil(f[i - 1][j] + (double) dist[i - 1] / speed - EPS));
                }
                if (j != 0) {
                    f[i][j] = Math.min(f[i][j], f[i - 1][j - 1] + (double) dist[i - 1] / speed);
                }
            }
        }
        for (int j = 0; j <= n; ++j) {
            if (f[n][j] < hoursBefore + EPS) {
                return j;
            }
        }
        return -1;
    }


    //2708. 一个小组的最大实力值
    public long maxStrength(int[] nums) {
        long max = nums[0];
        long min = nums[0];
        long res = nums[0];
        for (int i = 1; i < nums.length; i++) {
            long tmp = max;
            max = Math.max(Math.max(nums[i] * max, nums[i] * min), Math.max(nums[i], max));
            min = Math.min(Math.min(nums[i] * min, nums[i] * tmp), Math.min(nums[i], min));
        }
        return Math.max(res, max);
    }

    //2712. 使所有字符相等的最小成本
    public long minimumCost(String s) {
        int n = s.length();
        if (n == 1) return 0L;
        long ans = Long.MAX_VALUE;
        char[] chars = s.toCharArray();
        // [0,i]全部翻成0的代价
        long[] left0 = new long[n];
        // [0,i]全部翻成1的代价
        long[] left1 = new long[n];
        left0[0] = chars[0] == '0' ? 0 : 1;
        left1[0] = chars[0] == '1' ? 0 : 1;
        for (int i = 1; i < n; i++) {
            if (chars[i] == '0') {
                left0[i] = left0[i - 1];
                // 翻转成1，需要前i-1个翻转成0，然后一起翻成1
                left1[i] = left0[i - 1] + i + 1;
            } else {
                left0[i] = left1[i - 1] + i + 1;
                left1[i] = left1[i - 1];
            }
        }
        long[] right0 = new long[n];
        long[] right1 = new long[n];
        right0[n - 1] = chars[n - 1] == '0' ? 0 : 1;
        right1[n - 1] = chars[n - 1] == '1' ? 0 : 1;
        for (int i = n - 2; i >= 0; i--) {
            if (chars[i] == '0') {
                right0[i] = right0[i + 1];
                right1[i] = right0[i + 1] + n - i;
            } else {
                right0[i] = right1[i + 1] + n - i;
                right1[i] = right1[i + 1];
            }
        }
        for (int i = 0; i < n - 1; i++) {
            ans = Math.min(ans, Math.min(left0[i] + right0[i + 1], left1[i] + right1[i + 1]));
        }
        return ans;
    }

    public long minimumCost2(String s) {
        char[] chars = s.toCharArray();
        if (chars.length == 1) return 0l;

        int end = chars.length, half = chars.length >> 1;
        long ans = 0l;
        char lastChar = chars[0];
        for (int i = 1; i < end; i++) {
            if (lastChar == chars[i]) continue;
            if (i <= half) ans += i;
            else ans += end - i;
            lastChar = chars[i];
        }

        return ans;
    }

    // 799 香槟塔 分酒
    public double champagneTower(int k, int n, int m) {
        double[][] f = new double[n + 10][n + 10];
        f[0][0] = k;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= i; j++) {
                if (f[i][j] <= 1) continue;
                f[i + 1][j] += (f[i][j] - 1) / 2;
                f[i + 1][j + 1] += (f[i][j] - 1) / 2;
            }
        }
        return Math.min(f[n][m], 1);
    }

    // 808 分汤
    public double soupServings(int n) {
        n = (int) Math.ceil((double) n / 25);
        if (n >= 179) {
            return 1.0;
        }
        double[][] dp = new double[n + 1][n + 1];
        dp[0][0] = 0.5;
        for (int i = 1; i <= n; i++) {
            dp[0][i] = 1.0;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = (dp[Math.max(0, i - 4)][j] + dp[Math.max(0, i - 3)][Math.max(0, j - 1)] + dp[Math.max(0, i - 2)][Math.max(0, j - 2)] + dp[Math.max(0, i - 1)][Math.max(0, j - 3)]) / 4.0;
            }
        }
        return dp[n][n];
    }

    // 741 摘樱桃
    public int cherryPickup(int[][] grid) {
        int n = grid.length;
        int[][][] f = new int[n * 2 - 1][n][n];
        for (int i = 0; i < n * 2 - 1; ++i) {
            for (int j = 0; j < n; ++j) {
                Arrays.fill(f[i][j], Integer.MIN_VALUE);
            }
        }
        f[0][0][0] = grid[0][0];
        for (int k = 1; k < n * 2 - 1; ++k) {
            for (int x1 = Math.max(k - n + 1, 0); x1 <= Math.min(k, n - 1); ++x1) {
                int y1 = k - x1;
                if (grid[x1][y1] == -1) {
                    continue;
                }
                for (int x2 = x1; x2 <= Math.min(k, n - 1); ++x2) {
                    int y2 = k - x2;
                    if (grid[x2][y2] == -1) {
                        continue;
                    }
                    int res = f[k - 1][x1][x2]; // 都往右
                    if (x1 > 0) {
                        res = Math.max(res, f[k - 1][x1 - 1][x2]); // 往下，往右
                    }
                    if (x2 > 0) {
                        res = Math.max(res, f[k - 1][x1][x2 - 1]); // 往右，往下
                    }
                    if (x1 > 0 && x2 > 0) {
                        res = Math.max(res, f[k - 1][x1 - 1][x2 - 1]); // 都往下
                    }
                    res += grid[x1][y1];
                    if (x2 != x1) { // 避免重复摘同一个樱桃
                        res += grid[x2][y2];
                    }
                    f[k][x1][x2] = res;
                }
            }
        }
        return Math.max(f[n * 2 - 2][n - 1][n - 1], 0);
    }

    //1255. 得分最高的单词集合
    // 1.状态压缩枚举单词->2.根据每个单词字母计算得分->3.取最大分
    // 将第（bit）种组合情况，所使用的单词中的字母数量统计出来
    public int maxScoreWords(String[] words, char[] letters, int[] score) {
        // 统计给出的字母的数量
        int[] lettercnt = new int[26];
        for (char c : letters) {
            lettercnt[c - 'a']++;
        }

        int ans = 0;
        //从0至2^n枚举状态
        for (int i = 0; i < (1 << words.length); i++) {
            int[] g = group(words, i);
            ans = Math.max(ans, calcScore(g, lettercnt, score));
        }
        return ans;
    }

    private int[] group(String[] words, int bit) {
        int[] g = new int[26];
        //当前bit状态哪些位是1就是用了哪些单词
        for (int i = 0; i < words.length; i++) {
            if ((bit & (1 << i)) == 0) continue;
            for (char c : words[i].toCharArray()) {
                g[c - 'a']++;
            }
        }
        return g;
    }

    // 根据规则计算得分
    private int calcScore(int[] group, int[] lettercnt, int[] score) {
        int s = 0;
        for (int j = 0; j < 26; j++) {
            if (lettercnt[j] < group[j]) return 0;
            s += group[j] * score[j];
        }
        return s;
    }

    //1799. N 次操作后的最大分数和 Hard toreview
    // 状态压缩 + 动态规划
    public int maxScore(int[] nums) {
        int m = nums.length;
        int[] dp = new int[1 << m];
        int[][] gcdTmp = new int[m][m];
        for (int i = 0; i < m; ++i) {
            for (int j = i + 1; j < m; ++j) {
                gcdTmp[i][j] = gcd(nums[i], nums[j]);
            }
        }
        int all = 1 << m;
        for (int s = 1; s < all; ++s) {
            int t = Integer.bitCount(s);
            if ((t & 1) != 0) {
                continue;
            }
            for (int i = 0; i < m; ++i) {
                if (((s >> i) & 1) != 0) {
                    for (int j = i + 1; j < m; ++j) {
                        if (((s >> j) & 1) != 0) {
                            dp[s] = Math.max(dp[s], dp[s ^ (1 << i) ^ (1 << j)] + t / 2 * gcdTmp[i][j]);
                        }
                    }
                }
            }
        }
        return dp[all - 1];
    }

    private int gcd(int x, int y) {
        return y > 0 ? gcd(y, x % y) : x;
    }

    //2741. 特别的排列
    // 状态压缩+dfs
    private int[][] f2741 = new int[1 << 14][14];

    //表示当前可以选的下标集合为 i，上一个选的数的下标是 j 时，可以构造出多少个特别排列。
    private int dfs(int i, int j, int[] nums) {
        if (i == 0) return 1;
        if (f2741[i][j] != -1) return f2741[i][j];
        int res = 0;
        for (int k = 0; k < n; ++k) {
            if (((i >> k) & 1) == 1 && (nums[j] % nums[k] == 0 || nums[k] % nums[j] == 0)) {
                res = (res + dfs(i ^ (1 << k), k, nums)) % MOD;
            }
        }
        return f2741[i][j] = res % MOD;
    }

    public int specialPerm(int[] nums) {
        n = nums.length;
        int ans = 0;
        // new f数组和初始化也可以按n来
        for (int i = 0; i < 1 << 14; ++i) Arrays.fill(f2741[i], -1);
        for (int i = 0; i < n; ++i) {
            ans = (ans + dfs(((1 << n) - 1) ^ (1 << i), i, nums) % MOD) % MOD;
        }
        return ans % MOD;
    }

    // 状态压缩+DP
    public int specialPerm2(int[] nums) {
        int n = nums.length;
        int[][] f = new int[1 << n][n];
        for (int i = 0; i < n; ++ i) f[0][i] = 1;
        for (int i = 0; i < 1 << n; ++ i) {
            for (int j = 0; j < n; ++ j) {
                for (int k = 0; k < n; ++ k) {
                    if (((i >> k) & 1) == 1 && (nums[j] % nums[k] == 0 || nums[k] % nums[j] == 0)) {
                        f[i][j] = (f[i][j] + f[i ^ (1 << k)][k]) % MOD;
                    }
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < n; ++ i) ans = (ans + f[((1 << n) - 1) ^ (1 << i)][i]) % MOD;
        return ans;
    }

    //2305. 公平分发饼干
    public int distributeCookies(int[] cs, int k) {
        int n = cs.length, mask = 1 << n, INF = 0x3f3f3f3f;
        int[] g = new int[mask];
        for (int s = 0; s < mask; s++) {
            int t = 0;
            for (int i = 0; i < n; i++) t += ((s >> i) & 1) == 1 ? cs[i] : 0;
            g[s] = t;
        }
        int[][] f = new int[k + 10][mask];
        for (int i = 0; i <= k; i++) Arrays.fill(f[i], INF);
        f[0][0] = 0;
        for (int i = 1; i <= k; i++) {
            for (int s = 0; s < mask; s++) {
                for (int p = s; p != 0; p = (p - 1) & s) {
                    f[i][s] = Math.min(f[i][s], Math.max(f[i - 1][s - p], g[p]));
                }
            }
        }
        return f[k][mask - 1];
    }

    //1659. 最大化网格幸福感
    static final int T = 243, N = 5, M = 6;
    int tot;
    int[][] maskBits;
    int[] ivCount;
    int[] evCount;
    int[] innerScore;
    int[][] interScore;
    int[][][][] d;
    // 邻居间的分数
    static int[][] score = {
            {0, 0, 0},
            {0, -60, -10},
            {0, -10, 40}
    };

    public int getMaxGridHappiness(int m, int n, int introvertsCount, int extrovertsCount) {
        this.n = n;
        this.m = m;
        // 状态总数为 3^n
        this.tot = (int) Math.pow(3, n);
        this.maskBits = new int[T][N];
        this.ivCount = new int[T];
        this.evCount = new int[T];
        this.innerScore = new int[T];
        this.interScore = new int[T][T];
        this.d = new int[N][T][M + 1][M + 1];

        initData();
        // 记忆化搜索数组，初始化为 -1，表示未赋值
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < T; j++) {
                for (int k = 0; k <= M; k++) {
                    Arrays.fill(d[i][j][k], -1);
                }
            }
        }
        return dfs1659(0, 0, introvertsCount, extrovertsCount);
    }

    public void initData() {
        // 计算行内分数
        for (int mask = 0; mask < tot; mask++) {
            int maskTmp = mask;
            for (int i = 0; i < n; i++) {
                int x = maskTmp % 3;
                maskBits[mask][i] = x;
                maskTmp /= 3;
                if (x == 1) {
                    ivCount[mask]++;
                    innerScore[mask] += 120;
                } else if (x == 2) {
                    evCount[mask]++;
                    innerScore[mask] += 40;
                }
                if (i > 0) {
                    innerScore[mask] += score[x][maskBits[mask][i - 1]];
                }
            }
        }
        // 计算行间分数
        for (int i = 0; i < tot; i++) {
            for (int j = 0; j < tot; j++) {
                interScore[i][j] = 0;
                for (int k = 0; k < n; k++) {
                    interScore[i][j] += score[maskBits[i][k]][maskBits[j][k]];
                }
            }
        }
    }

    public int dfs1659(int row, int premask, int iv, int ev) {
        if (row == m || (iv == 0 && ev == 0)) {
            return 0;
        }
        // 如果该状态已经计算过答案，则直接返回
        if (d[row][premask][iv][ev] != -1) {
            return d[row][premask][iv][ev];
        }
        // 合法状态，初始值为 0
        int res = 0;
        for (int mask = 0; mask < tot; mask++) {
            // mask 包含的内向人数不能超过 iv ，外向人数不能超过 ev
            if (ivCount[mask] > iv || evCount[mask] > ev) {
                continue;
            }
            res = Math.max(res, dfs1659(row + 1, mask, iv - ivCount[mask], ev - evCount[mask])
                    + innerScore[mask]
                    + interScore[premask][mask]);
        }
        d[row][premask][iv][ev] = res;
        return res;
    }


    //1494. 并行课程 II 子集枚举
    public int minNumberOfSemesters(int n, int[][] relations, int k) {
        int[] dp = new int[1 << n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        int[] need = new int[1 << n];
        for (int[] edge : relations) {
            need[(1 << (edge[1] - 1))] |= 1 << (edge[0] - 1);
        }
        dp[0] = 0;
        for (int i = 1; i < (1 << n); ++i) {
            need[i] = need[i & (i - 1)] | need[i & (-i)];
            if ((need[i] | i) != i) { // i 中有任意一门课程的前置课程没有完成学习
                continue;
            }
            int valid = i ^ need[i]; // 当前学期可以进行学习的课程集合
            if (Integer.bitCount(valid) <= k) { // 如果个数小于 k，则可以全部学习，不再枚举子集
                dp[i] = Math.min(dp[i], dp[i ^ valid] + 1);
            } else { // 否则枚举当前学期需要进行学习的课程集合
                for (int sub = valid; sub > 0; sub = (sub - 1) & valid) {
                    if (Integer.bitCount(sub) <= k) {
                        dp[i] = Math.min(dp[i], dp[i ^ sub] + 1);
                    }
                }
            }
        }
        return dp[(1 << n) - 1];
    }

    //1681. 最小不兼容性
    // 二进制、子集枚举
    public int minimumIncompatibility(int[] nums, int k) {
        int n = nums.length, group = n / k, inf = Integer.MAX_VALUE;
        int[] dp = new int[1 << n];
        Arrays.fill(dp, inf);
        dp[0] = 0;
        HashMap<Integer, Integer> values = new HashMap<>();

        for (int mask = 1; mask < (1 << n); mask++) {
            if (Integer.bitCount(mask) != group) {
                continue;
            }
            int mn = 20, mx = 0;
            HashSet<Integer> cur = new HashSet<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) > 0) {
                    if (cur.contains(nums[i])) {
                        break;
                    }
                    cur.add(nums[i]);
                    mn = Math.min(mn, nums[i]);
                    mx = Math.max(mx, nums[i]);
                }
            }
            if (cur.size() == group) {
                values.put(mask, mx - mn);
            }
        }

        for (int mask = 0; mask < (1 << n); mask++) {
            if (dp[mask] == inf) {
                continue;
            }
            HashMap<Integer, Integer> seen = new HashMap<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) {
                    seen.put(nums[i], i);
                }
            }
            if (seen.size() < group) {
                continue;
            }
            int sub = 0;
            for (int v : seen.values()) {
                sub |= (1 << v);
            }
            int nxt = sub;
            while (nxt > 0) {
                if (values.containsKey(nxt)) {
                    dp[mask | nxt] = Math.min(dp[mask | nxt], dp[mask] + values.get(nxt));
                }
                nxt = (nxt - 1) & sub;
            }
        }

        return (dp[(1 << n) - 1] < inf) ? dp[(1 << n) - 1] : -1;
    }

    //endregion---------------------------------------------------------------------------------
    //region -----------------------------------------背包DP-------------------------------------------
//    背包问题的分类https://blog.csdn.net/weixin_45746505/article/details/124543411
//    在我看来，背包问题可以总结为三类：01背包问题、完全背包问题以及分组背包问题。
//    01背包问题：每个元素最多取1次。具体来讲：一共有 N 件物品，第 i（i 从 1 开始）件物品的重量为 w[i]，价值为 v[i]。在总重量不超过背包承载上限 W 的情况下，能够装入背包的最大价值是多少？
//    完全背包问题：每个元素可以取多次。具体来讲：完全背包与 01 背包不同就是每种物品可以有无限多个：一共有 N 种物品，每种物品有无限多个，第 i（i 从 1 开始）种物品的重量为 w[i]，价值为 v[i]。在总重量不超过背包承载上限 W 的情况下，能够装入背包的最大价值是多少？
//    分组背包问题：有多个背包，需要对每个背包放入物品，每个背包的处理情况与完全背包完全相同。
//    在完全背包问题当中根据是否需要考虑排列组合问题（是否考虑物品顺序），可分为两种情况，我们可以通过内外循环的调换来处理排列组合问题，如果题目不是排列组合问题，则这两种方法都可以使用（推荐使用组合来解决）
//    而每个背包问题要求的也是不同的，按照所求问题分类，又可以分为以下几种：
//      1、最值问题：要求最大值/最小值
//      2、存在问题：是否存在…………，满足…………
//      3、组合问题：求所有满足……的排列组合
//
//    解题模板
//    背包问题大体的解题模板是两层循环，分别遍历物品nums和背包容量target，然后写转移方程，根据背包的分类我们确定物品和容量遍历的先后顺序，根据问题的分类我们确定状态转移方程的写法。
//    首先是背包分类的模板：
//1、0/1背包：外循环nums,内循环target,target倒序且target>=nums[i];           dp[i][j] = Math.max(dp[i-1][j],dp[i-1][j-v[i]]+w[i])
//2、完全背包（组合）：外循环nums,内循环target,target正序且target>=nums[i];   dp[i][j] = Math.max(dp[i-1][j],dp[i][j-v[i]]+w[i])
//3、完全背包（排列）：外循环target,内循环nums,target正序且target>=nums[i];
//4、分组背包：这个比较特殊，需要多重循环：外循环nums,内部循环根据题目的要求构建多重背包循环

    //region ---------------------------------------------------01背包 每件只能取1次------------------------------------------------------
    // 416 分割等和子集
    //给你一个 只包含正整数 的 非空 数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
//输入：nums = [1,5,11,5]
//输出：true
//解释：数组可以分割成 [1, 5, 5] 和 [11] 。
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int i : nums) {
            sum += i;
        }
        int target = sum / 2;
        if (target * 2 != sum) return false;
        // f[i][j] 代表考虑前 i 个数值，其选择数字总和不超过 j 的最大价值。
        int[][] dp = new int[nums.length][target + 1];
        for (int i = 0; i < target + 1; i++) {
            dp[0][i] = i >= nums[0] ? nums[0] : 0;
        }
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < target + 1; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], j >= nums[i] ? dp[i - 1][j - nums[i]] + nums[i] : 0);
            }
        }
        return dp[nums.length - 1][target] == target;
    }

    public boolean canPartition01(int[] nums) {
        // 01背包问题
        int le = nums.length;
        int sum = 0;
        for (int num : nums)
            sum += num;
        if (sum % 2 == 1 || le == 1)
            return false;
        int target = sum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;   // target等于0时为true

        for (int num : nums) {
            for (int i = target; i >= num; i--) {
                dp[i] |= dp[i - num];
            }
        }
        return dp[target];
    }

    // 494 目标和  回溯做法搜findTargetSumWaysDFS
    // 求方案数
    public int findTargetSumWaysDP(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // n个数，为负数的和neg，则其余数和为sum-neg
        // (sum-neg)-neg = target
        int diff = sum - target;
        if (diff < 0 || diff % 2 != 0) return 0;
        int n = nums.length, neg = diff / 2; //负数个数
        // 前i个数和为j的个数
        int[][] dp = new int[n + 1][neg + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            int num = nums[i - 1];
            for (int j = 0; j <= neg; j++) { //求和为neg的个数
                // 选不选当前的num
                dp[i][j] = dp[i - 1][j];//和为j，当前j小于当前num，只能上一个结果
                if (j >= num) {
                    dp[i][j] += dp[i - 1][j - num];
                }
            }
        }
        return dp[n][neg];
    }

    public int findTargetSumWays01(int[] nums, int target) {
        int le = nums.length;
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // 如果sum<target,即都为正数也不能组成target，则直接返回0
        if (sum < target || (sum - target) % 2 == 1)
            return 0;
        int X = (sum - target) / 2;     // 负数组的和为X，这里不能用正数组，因为target可能是负数
        int[] dp = new int[X + 1];
        dp[0] = 1;

        for (int num : nums) {
            for (int i = X; i >= num; i--) {
                dp[i] = dp[i] + dp[i - num];    // 负数组选num（dp[i-num]）和不选num（dp[i]）的数量相加为dp[i]
            }
        }
        return dp[X];
    }

    //1049 最后一块石头的重量 II
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

    public int lastStoneWeightII01(int[] stones) {
        int n = stones.length;
        int sum = 0;
        for (int stone : stones) {
            sum += stone;
        }
        int target = sum / 2;
        int[] dp = new int[target + 1];
        for (int stone : stones) {
            for (int i = target; i >= stone; i--) {
                dp[i] = Math.max(dp[i], dp[i - stone] + stone);
            }
        }
        return sum - 2 * dp[target];
    }

    // 1751 最多可以参加会议的次数
    public int maxValue(int[][] events, int k) {
        int n = events.length;
        // 对结束时间二分，根据结束时间排序
        Arrays.sort(events, Comparator.comparingInt(o -> o[1]));
        // 前n个会议参加k个的最大价值，包括不参加
        int[][] dp = new int[n + 1][k + 1];
        for (int i = 1; i <= n; i++) {
            int[] cur = events[i - 1];
            // 从第一个到i的前一个
            int l = 1, r = i - 1;
            while (l < r) {
                int mid = l + r + 1 >> 1;
                // 由于l和r都后移一位，此处mid前移
                if (events[mid - 1][1] < cur[0]) {
                    l = mid;
                } else {
                    r = mid - 1;
                }
            }
            // 对events的索引都要-1
            int last = events[l - 1][1] < cur[0] ? l : 0;
            // last=0代表只参加本次会议
            for (int j = 1; j <= k; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[last][j - 1] + cur[2]);
            }
        }
        return dp[n][k];
    }

    //1125. 最小的必要团队 状态压缩+01背包
    //状态压缩位运算技巧：
    // 1.把元素x变成集合{x} :1<<x
    // 2.判断元素x是否在集合A中：((A>>x)&1)==1
    // 3.集合A和集合B的并集 A|B
    // 4.计算A/B，表示从集合A中去掉集合B中的元素：A&~B
    // 5.全集：(1<<n)-1
    // 记忆化搜索
    public int[] smallestSufficientTeam(String[] reqSkills, List<List<String>> people) {
        Map<String, Integer> sid = new HashMap<>();
        int m = reqSkills.length;
        for (int i = 0; i < m; ++i)
            sid.put(reqSkills[i], i); // 字符串映射到下标

        int n = people.size();
        int[] mask = new int[n];
        for (int i = 0; i < n; ++i)
            for (String s : people.get(i)) // 把 people[i] 压缩成一个二进制数 mask[i]
                mask[i] |= 1 << sid.get(s);

        int u = 1 << m;
        long[][] memo = new long[n][u];
        for (int i = 0; i < n; i++)
            Arrays.fill(memo[i], -1); // -1 表示还没有计算过
        long res = dfs1125(n - 1, u - 1, memo, mask);

        int[] ans = new int[Long.bitCount(res)];
        for (int i = 0, j = 0; i < n; ++i)
            if (((res >> i) & 1) > 0)
                ans[j++] = i; // 所有在 res 中的下标
        return ans;
    }

    private long dfs1125(int i, int j, long[][] memo, int[] mask) {
        if (j == 0) return 0; // 背包已装满
        int n = mask.length;
        if (i < 0) return (1L << n) - 1; // 没法装满背包，返回全集，这样下面比较集合大小会取更小的
        if (memo[i][j] != -1) return memo[i][j];
        long res = dfs1125(i - 1, j, memo, mask); // 不选 mask[i]
        long res2 = dfs1125(i - 1, j & ~mask[i], memo, mask) | (1L << i); // 选 mask[i]
        return memo[i][j] = Long.bitCount(res) < Long.bitCount(res2) ? res : res2;
    }

    public int[] smallestSufficientTeamDP(String[] reqSkills, List<List<String>> people) {
        Map<String, Integer> map = new HashMap<>();
        int m = reqSkills.length;
        for (int i = 0; i < m; i++) {
            map.put(reqSkills[i], i); // 字符串映射到下标
        }
        int n = people.size();
        int u = 1 << m;
        long[][] dp = new long[n + 1][u];//前i个人中选择一些，并集=j，需要的最小人数(用集合表示 101 表示选0，2)
        Arrays.fill(dp[0], (1L << n) - 1); // 对应所有人，这样在后面取min的时候会取更小的
        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            int mask = 0;// 把 people[i] 压缩成一个二进制数 mask
            for (String s : people.get(i)) {
                mask |= 1 << map.get(s);
            }
            for (int j = 1; j < u; j++) {
                long res1 = dp[i][j]; //不选当前mask
                long res2 = dp[i][j & ~mask] | (1L << i); //选当前mask
                // 此处选的是人少的集合
                dp[i + 1][j] = Long.bitCount(res1) < Long.bitCount(res2) ? res1 : res2;
            }
        }
        long res = dp[n][u - 1];
        int[] ans = new int[Long.bitCount(res)];
        for (int i = 0, j = 0; i < n; i++) {
            if (((res >> i) & 1) == 1) {
                ans[j++] = i;
            }
        }
        return ans;
    }

    //一维空间优化
    public int[] smallestSufficientTeamOneDim(String[] reqSkills, List<List<String>> people) {
        Map<String, Integer> map = new HashMap<>();
        int m = reqSkills.length;
        for (int i = 0; i < m; i++) {
            map.put(reqSkills[i], i); // 字符串映射到下标
        }
        int n = people.size();
        int u = 1 << m;
        long[] dp = new long[u];//前i个人中选择一些，并集=j，需要的最小人数(用集合表示 101 表示选0，2)
        Arrays.fill(dp, (1L << n) - 1); // 对应所有人，这样在后面取min的时候会取更小的
        dp[0] = 0;
        for (int i = 0; i < n; i++) {
            int mask = 0;// 把 people[i] 压缩成一个二进制数 mask
            for (String s : people.get(i)) {
                mask |= 1 << map.get(s);
            }
            for (int j = u - 1; j > 0; j--) {
                long res1 = dp[j]; //不选当前mask
                long res2 = dp[j & ~mask] | (1L << i); //选当前mask
                // 此处选的是人少的集合
                dp[j] = Long.bitCount(res1) < Long.bitCount(res2) ? res1 : res2;
            }
        }
        long res = dp[u - 1];
        int[] ans = new int[Long.bitCount(res)];
        for (int i = 0, j = 0; i < n; i++) {
            if (((res >> i) & 1) == 1) {
                ans[j++] = i;
            }
        }
        return ans;
    }

    //2742. 给墙壁刷油漆
    // 01背包
    // 付费刷墙个数+免费刷墙个数 = n
    // 付费刷墙时间和>=免费刷墙时间和=免费刷墙个数
    // 付费刷墙时间和>=n-付费刷墙个数
    // （付费刷墙时间+1）之和>=n
    // 从n个物品中选体积和至少为n的物品，价值和最少
    public int paintWalls(int[] cost, int[] time) {
        int n = cost.length;
        int[] f = new int[n + 1];
        Arrays.fill(f, Integer.MAX_VALUE / 2); // 防止加法溢出
        f[0] = 0;
        for (int i = 0; i < n; i++) {
            int c = cost[i], t = time[i] + 1;  // 注意这里加一了
            for (int j = n; j > 0; j--)
                f[j] = Math.min(f[j], f[Math.max(j - t, 0)] + c);
        }
        return f[n];
    }

    //2809. 使数组和小于等于 x 的最少时间
    public int minimumTime(List<Integer> nums1, List<Integer> nums2, int x) {
        int n = nums1.size(), s1 = 0, s2 = 0;
        int[][] dp = new int[n + 1][n + 1];
        List<List<Integer>> nums = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int a = nums1.get(i), b = nums2.get(i);
            nums.add(Arrays.asList(b, a));
            s1 += a;
            s2 += b;
        }
        nums.sort(Comparator.comparingInt(o -> o.get(0)));

        ////如果对前 j 个元素进行 i 次操作，可以减少的最大总值
        for (int j = 1; j <= n; ++j) {
            int b = nums.get(j - 1).get(0), a = nums.get(j - 1).get(1);
            for (int i = j; i > 0; --i) {
                dp[j][i] = Math.max(dp[j - 1][i], dp[j - 1][i - 1] + i * b + a);
            }
        }
        for (int i = 0; i <= n; i++) {
            if (s2 * i + s1 - dp[n][i] <= x) {
                return i;
            }
        }
        return -1;
    }
    //endregion------------------------------------------------------------------------------------------

    //region----------------------------------------------- 完全背包 每件可以取无数次---------------------------------------
    public boolean wordBreakBeibao(String s, List<String> wordDict) {
        int len = s.length();
        // 根据字符串s建dp数组
        boolean[] dp = new boolean[len + 1];
        dp[0] = true;

        for (int i = 1; i <= len; i++) {
            for (String word : wordDict) {
                int wordLen = word.length();
                if (i - wordLen >= 0 && s.substring(i - wordLen, i).equals(word))
                    dp[i] |= dp[i - wordLen];
            }
        }
        return dp[len];
    }

    // 279 完全平方数
    //给你一个整数 n ，返回 和为 n 的完全平方数的最少数量 。
// 完全平方数 是一个整数，其值等于另一个整数的平方；换句话说，其值等于一个整数自乘的积。例如，1、4、9 和 16 都是完全平方数，而 3 和 11 不是。
//输入：n = 12 输出：3解释：12 = 4 + 4 + 4
// 完全平方数 背包
    public int numSquares(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i < n + 1; i++) {
            dp[i] = i;
            for (int j = 1; j * j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];
    }

    /*
        7
       / \
      6   3
    / \    \
   5   2    2
  / \   \    \
1    4   1    1
     */
    public int numSquaresBFS(int n) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.offer(n);
        int level = 0;
        while (!queue.isEmpty()) {
            level++;
            int width = queue.size();
            for (int i = 0; i < width; i++) {
                int curr = queue.poll();
                for (int j = 1; j * j <= curr; j++) {
                    int diff = curr - j * j;
                    if (diff == 0) {
                        return level;
                    }
                    if (!visited.contains(diff)) {
                        queue.offer(diff);
                    }
                    visited.add(diff);
                }
            }
        }
        return level;
    }

    //剪枝
    public int numSquaresBFS2(int n) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.offer(n);
        int level = 0;
        while (!queue.isEmpty()) {
            level++;
            int width = queue.size();
            for (int i = 0; i < width; i++) {
                int curr = queue.poll();
                for (int j = 1; j * j <= curr; j++) {
                    int diff = curr - j * j;
                    if (diff == 0) {
                        return level;
                    }
                    if (!visited.contains(diff)) {
                        queue.offer(diff);
                    }
                    visited.add(diff);
                }
            }
        }
        return level;
    }

    //322 零钱兑换 换硬币 完全背包（组合）
    //给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
// 计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1 。
    //超时
    public int coinChangeBFS(int[] coins, int amount) {
        if (amount == 0) return 0;
        if (coins.length <= 0) return -1;
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> set = new HashSet<>();
        queue.offer(amount);
        set.add(amount);
        int len = 0;
        while (!queue.isEmpty()) {
            if (len > amount) {
                return -1;
            }
            int width = queue.size();
            for (int i = 0; i < width; i++) {
                int curr = queue.poll();
                for (int j = 0; j < coins.length; j++) {
                    int diff = curr - coins[j];
                    if (diff == 0) return len + 1;
                    if (!set.contains(diff)) {
                        queue.offer(diff);
                    }
                    set.add(diff);
                }
            }
            len++;
        }
        return -1;
    }

    //超时
    int res = Integer.MAX_VALUE;

    public int coinChangeDFS(int[] coins, int amount) {
        if (coins.length <= 0) return -1;
        coinDfs(coins, amount, 0);
        return res;
    }

    private void coinDfs(int[] coins, int amount, int depth) {
        if (amount < 0) {
            return;
        }
        if (amount == 0) {
            res = Math.min(res, depth);
        }
        for (int coin : coins) {
            coinDfs(coins, amount - coin, depth + 1);
        }
    }

    // dfs+记忆化搜索
    public int coinChangeDFSWithMemo(int[] coins, int amount) {
        if (coins.length <= 0) return -1;
        int[] memo = new int[amount];
        return coinDfsWithMemo(coins, amount, memo);
    }

    private int coinDfsWithMemo(int[] coins, int amount, int[] memo) {
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        if (memo[amount - 1] != 0) {
            return memo[amount - 1];
        }
        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            int count = coinDfsWithMemo(coins, amount - coin, memo);
            if (count >= 0) {
                min = Math.min(min, count + 1);
            }
        }
        memo[amount - 1] = min < Integer.MAX_VALUE ? min : -1;
        return memo[amount - 1];
    }

    // 硬币 背包
    //dp[i] amount是i的情况下最少硬币数
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        dp[0] = 0;
        for (int i = 1; i < amount + 1; i++) {
            dp[i] = amount + 1;//不可能达到，以此为标记位标识无解
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i - coin] + 1, dp[i]);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    public int coinChange2(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            dp[i] = Integer.MAX_VALUE;
        }
        for (int coin : coins) {
            for (int i = 1; i <= amount; i++) {
                dp[i] = i >= coin ? Math.min(dp[i], dp[i - coin] + 1) : dp[i];
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // 518 零钱兑换2完全背包问题（组合）
    //给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
// 请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0 。
    public int change(int amount, int[] coins) {
        int n = coins.length;
        // i个硬币金额是j的组合数 = i-1个硬币金额是j(不拿当前硬币)+ i个硬币金额是j-coins[i]
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

    public int change2(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int coin : coins) {
            //对于每个硬币，取不取当前硬币对i的贡献
            // 当前coin对各个i贡献1次
            for (int i = 1; i <= amount; i++) {
                dp[i] += (i >= coin ? dp[i - coin] : 0);

            }
        }

        return dp[amount];
    }

    // 377 组合总和4 完全背包问题（排列）
    public int combinationSum4(int[] nums, int t) {
        // 因为 nums[i] 最小值为 1，因此构成答案的最大长度为 target
        int len = nums.length;
        //定义 f[i][j] 为组合长度为 i，凑成总和为 j 的方案数是多少。
        int[][] f = new int[len + 1][t + 1];
        f[0][0] = 1;
        int ans = 0;
        for (int i = 1; i <= len; i++) {
            for (int j = 0; j <= t; j++) {
                for (int u : nums) {
                    if (j >= u) f[i][j] += f[i - 1][j - u];
                }
            }
            ans += f[i][t];
        }
        return ans;
    }

    public int combinationSum42(int[] nums, int target) {
        //定义 f[i] 为凑成总和为 i 的方案数是多少。
        int[] dp = new int[target + 1];
        dp[0] = 1;
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                //对于当前i,最后一个选num的方案数,有顺序性(num在组成i的不同位置上对结果的贡献)
                //eg [1 2 3] target=4,当i=3时,最后选1 方案数(=dp[2])+最后选2的方案数(=dp[1])
                if (i >= num) {
                    dp[i] += dp[i - num];
                }
            }
        }
        return dp[target];
    }

    // 343 整数拆分
    //给定一个正整数 n ，将其拆分为 k 个 正整数 的和（ k >= 2 ），并使这些整数的乘积最大化。
// 返回 你可以获得的最大乘积 。
    public int integerBreak(int n) {
        if (n < 2) return n;
        int[] dp = new int[n + 1];
        dp[2] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                // 对每一个j,直接拆成两段j与i-j 和比较j 与 (i-j)拆分后的最大
                int currMax = Math.max(j * (i - j), j * dp[i - j]);
                dp[i] = Math.max(dp[i], currMax);
            }
        }
        return dp[n];
    }


    // offer 14 剪绳子
    public int cuttingRope(int n) {
        if (n < 2) return n;
        int[] dp = new int[n + 1];
        dp[2] = 1;
        // 如果只剪掉长度为1，对最后的乘积无任何增益，所以从长度为2开始剪
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                // 剪了第一段后，剩下(i - j)长度可以剪也可以不剪。如果不剪的话长度乘积即为j * (i - j)；如果剪的话长度乘积即为j * dp[i - j]。取两者最大值
                int nowBigger = Math.max(j * (i - j), j * dp[i - j]);
                // 对于同一个i，内层循环对不同的j都还会拿到一个max，所以每次内层循环都要更新max
                dp[i] = Math.max(dp[i], nowBigger);
            }
        }
        return dp[n];
    }

    public int cuttingRope2(int n) {
        if (n < 4) return n - 1;
        long res = 1;
        while (n > 4) {
            res = res * 3 % 1000000007;
            n -= 3;
        }
        return (int) (res * n % 1000000007);
    }

    // 面试题 17.13. 恢复空格
    public int respace(String[] dictionary, String sentence) {
        Set<String> dict = new HashSet<>(Arrays.asList(dictionary));
        int n = sentence.length();
        // 字符串前i个字符对应的最少匹配数
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            // i不参与匹配,dp[i]= 前i-1个最小匹配数+1
            dp[i] = dp[i - 1] + 1;
            //i对应的字符串[0,i-1]
            for (int j = 0; j < i; j++) {
                // [j,i-1]若在dict中(subString(j,i-1+1)),就匹配上,dp[i] 就等于 j-1对应的字符对应的结果=dp[j]
                if (dict.contains(sentence.substring(j, i))) {
                    dp[i] = Math.min(dp[i], dp[j]);
                }
            }
        }
        return dp[n];
    }

    public int respaceTrie(String[] dictionary, String sentence) {
        int n = sentence.length();
        SuffixTrie trie = new SuffixTrie();
        for (String word : dictionary) {
            trie.insert(word);
        }
        // 字符串前i个字符对应的最少匹配数
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = dp[i - 1] + 1;
            for (int j : trie.search(sentence, i - 1)) {
                dp[i] = Math.min(dp[i], dp[j]);
            }
        }
        return dp[n];
    }

    // 后缀树优化
    class SuffixTrie {
        SuffixTrie[] children;
        boolean isEnd;

        public SuffixTrie() {
            children = new SuffixTrie[26];
        }

        public void insert(String word) {
            SuffixTrie node = this;
            int n = word.length();
            char[] chars = word.toCharArray();
            for (int i = n - 1; i >= 0; i--) {
                if (node.children[chars[i] - 'a'] == null) {
                    node.children[chars[i] - 'a'] = new SuffixTrie();
                }
                node = node.children[chars[i] - 'a'];
            }
            node.isEnd = true;
        }

        public List<Integer> search(String sentence, int endPos) {
            SuffixTrie node = this;
            List<Integer> idxes = new ArrayList<>();
            for (int i = endPos; i >= 0; i--) {
                int idx = sentence.charAt(i) - 'a';
                if (node.children[idx] == null) {
                    break;
                }
                node = node.children[idx];
                if (node.isEnd) idxes.add(i);
            }
            return idxes;
        }
    }

    //endregion-----------------------------------------------------------------------------------------------------
    //region----------------------------------------多重背包 每件取有限次---------------------------------------
    // 有N种物品和容量位C的背包,每种物品数量有限
    // 第i件物品的体积是v[i],价值是w[i],数量是s[i]
    //问选择哪些物品，每件物品选择多少件，可使得总价值最大
    // 转化01背包
    public int maxValue(int N, int C, int[] s, int[] v, int[] w) {
        int[] dp = new int[C + 1];
        for (int i = 0; i < N; i++) {
            for (int j = C; j >= v[i]; j--) {
                for (int k = 0; k <= s[i] && j >= k * v[i]; k++) {
                    dp[j] = Math.max(dp[j], dp[j - k * v[i]] + k * w[i]);
                }
            }
        }
        return dp[C];
    }

    // 多重背包单调队列优化 Hard
    public int maxValueQueue(int N, int C, int[] s, int[] v, int[] w) {
        int[] dp = new int[C + 1];
        int[] g = new int[C + 1]; // 辅助队列，记录的是上一次的结果
        int[] q = new int[C + 1]; // 主队列，记录的是本次的结果

        // 枚举物品
        for (int i = 0; i < N; i++) {
            int vi = v[i];
            int wi = w[i];
            int si = s[i];

            // 将上次算的结果存入辅助数组中
            g = dp.clone();

            // 枚举余数
            for (int j = 0; j < vi; j++) {
                // 初始化队列，head 和 tail 分别指向队列头部和尾部
                int head = 0, tail = -1;
                // 枚举同一余数情况下，有多少种方案。
                // 例如余数为 1 的情况下有：1、vi + 1、2 * vi + 1、3 * vi + 1 ...
                for (int k = j; k <= C; k += vi) {
                    dp[k] = g[k];
                    // 将不在窗口范围内的值弹出
                    if (head <= tail && q[head] < k - si * vi) head++;
                    // 如果队列中存在元素，直接使用队头来更新
                    if (head <= tail) dp[k] = Math.max(dp[k], g[q[head]] + (k - q[head]) / vi * wi);
                    // 当前值比对尾值更优，队尾元素没有存在必要，队尾出队
                    while (head <= tail && g[q[tail]] - (q[tail] - j) / vi * wi <= g[k] - (k - j) / vi * wi) tail--;
                    // 将新下标入队
                    q[++tail] = k;
                }
            }
        }
        return dp[C];
    }

    //1774. 最接近目标价格的甜点成本
    public int closestCost(int[] baseCosts, int[] toppingCosts, int target) {
        int x = Arrays.stream(baseCosts).min().getAsInt();
        if (x >= target) {
            return x;
        }
        boolean[] can = new boolean[target + 1];
        int res = 2 * target - x;
        for (int b : baseCosts) {
            if (b <= target) {
                can[b] = true;
            } else {
                res = Math.min(res, b);
            }
        }
        for (int t : toppingCosts) {
            for (int count = 0; count < 2; ++count) {
                for (int i = target; i > 0; --i) {
                    if (can[i] && i + t > target) {
                        res = Math.min(res, i + t);
                    }
                    if (i - t > 0) {
                        can[i] = can[i] | can[i - t];
                    }
                }
            }
        }
        for (int i = 0; i <= res - target; ++i) {
            if (can[target - i]) {
                return target - i;
            }
        }
        return res;
    }

    //2585. 获得分数的方法数
    public int waysToReachTarget(int target, int[][] types) {
        int mod = (int) 1e9 + 7;
        int[][] dp = new int[types.length + 1][target + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= types.length; i++) {
            int score = types[i - 1][1];  //分数
            for (int j = 0; j < dp[0].length; j++) {
                for (int k = 0; k <= types[i - 1][0] && j - score * k >= 0; k++) {
                    dp[i][j] = dp[i][j] + dp[i - 1][j - k * score];
                    dp[i][j] = dp[i][j] % mod;
                }
            }
        }
        return dp[types.length][target];
    }

    //endregion-----------------------------------------------------------------------------------------
    //region--------------------------------分组背包---------------------------------------------------
    // 有N种物品和容量位C的背包
    // 第i个物品组有s[i]件物品,其中第i组第j件物品体积v[i],价值w[i],同一组只能取一件物品
    //求解将哪些物品装入背包可使这些物品的费用总和不超过背包容量，且价值总和最大。
    public int maxValue(int N, int C, int[] S, int[][] v, int[][] w) {
        int[][] dp = new int[N + 1][C + 1];
        for (int i = 1; i <= N; i++) {
            int[] vi = v[i - 1];
            int[] wi = w[i - 1];
            int si = S[i - 1];
            for (int j = 1; j <= C; j++) {
                dp[i][j] = dp[i - 1][j];
                for (int k = 0; k < si; k++) {
                    if (j >= vi[k]) {
                        dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - vi[k]] + wi[k]);
                    }
                }
            }
        }
        return dp[N][C];
    }

    // 一维空间优化
    public int maxValueOneDim(int N, int C, int[] S, int[][] v, int[][] w) {
        int[] dp = new int[C + 1];
        for (int i = 1; i <= N; i++) {
            int[] vi = v[i - 1];
            int[] wi = w[i - 1];
            int si = S[i - 1];
            //这里倒序遍历
            for (int j = C; j >= 0; j--) {
                for (int k = 0; k < si; k++) {
                    if (j >= vi[k]) {
                        dp[j] = Math.max(dp[j], dp[j - vi[k]] + wi[k]);
                    }
                }
            }
        }
        return dp[C];
    }

    //1155. 掷骰子的N种方法
    //这里有 n 个一样的骰子，每个骰子上都有 k 个面，分别标号为 1 到 k 。
// 给定三个整数 n , k 和 target ，返回可能的方式(从总共 kn 种方式中)滚动骰子的数量，使正面朝上的数字之和等于 target 。
    public int numRollsToTarget(int n, int k, int target) {
        int mod = (int) 1e9 + 7;
        // i个骰子和为j的方法数
        int[][] dp = new int[n + 1][target + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                // 从1到k个面
                // 前i-1个骰子 和为[j-1,j-k]的方案数和
                for (int f = 1; f <= k && j >= f; f++) {
                    dp[i][j] = (dp[i][j] + dp[i - 1][j - f]) % mod;
                }
            }
        }
        return dp[n][target] % mod;
    }

    public int numRollsToTargetOneDim(int n, int k, int target) {
        int mod = (int) 1e9 + 7;
        int[] dp = new int[target + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = target; j >= 0; j--) {
                dp[j] = 0;
                for (int p = 1; p <= k; p++) {
                    if (p <= j) {
                        dp[j] = (dp[j - p] + dp[j]) % mod;
                    }
                }
            }
        }
        return dp[target];
    }

    //endregion ------------------------------------------------------------------------------------
    //region------------------------多维背包------------------------------------------------------
    //474 一和零 多维背包 转换01背包
    //前 k 件物品，在数字 1 容量不超过 i，数字 0 容量不超过 j 的条件下的「最大价值」（每个字符串的价值均为 1）
    public int findMaxForm(String[] strs, int m, int n) {
        int len = strs.length;
        // 预处理每一个字符包含 0 和 1 的数量
        int[][] cnt = new int[len][2];
        for (int i = 0; i < len; i++) {
            cnt[i] = getZerosOnes(strs[i]);
        }

        // 处理只考虑第一件物品的情况
        int[][][] f = new int[len][m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                f[0][i][j] = (i >= cnt[0][0] && j >= cnt[0][1]) ? 1 : 0;
            }
        }

        // 处理考虑其余物品的情况
        for (int k = 1; k < len; k++) {
            int zero = cnt[k][0], one = cnt[k][1];
            for (int i = 0; i <= m; i++) {
                for (int j = 0; j <= n; j++) {
                    // 不选择第 k 件物品
                    int a = f[k - 1][i][j];
                    // 选择第 k 件物品（前提是有足够的 m 和 n 额度可使用）
                    int b = (i >= zero && j >= one) ? f[k - 1][i - zero][j - one] + 1 : 0;
                    f[k][i][j] = Math.max(a, b);
                }
            }
        }
        return f[len - 1][m][n];
    }

    // 一维空间优化
    public int findMaxForm2(String[] strs, int m, int n) {
        int length = strs.length;
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= length; i++) {
            int[] zerosOnes = getZerosOnes(strs[i - 1]);
            int zeros = zerosOnes[0], ones = zerosOnes[1];
            for (int j = m; j >= zeros; j--) {
                for (int k = n; k >= ones; k--) {
                    dp[j][k] = Math.max(dp[j][k], dp[j - zeros][k - ones] + 1);
                }
            }
        }
        return dp[m][n];
    }

    public int[] getZerosOnes(String str) {
        int[] zerosOnes = new int[2];
        int length = str.length();
        for (int i = 0; i < length; i++) {
            zerosOnes[str.charAt(i) - '0']++;
        }
        return zerosOnes;
    }

    // 879 盈利计划 toreview
    public int profitableSchemes(int n, int minProfit, int[] group, int[] profit) {
        int mod = (int) 1e9 + 7;
        int m = group.length;
        // 前i件任务,人数不超过j,最少盈利k的方案数
        int[][][] dp = new int[m + 1][n + 1][minProfit + 1];
        for (int i = 0; i <= n; i++) {
            dp[0][i][0] = 1;
        }
        for (int i = 1; i <= m; i++) {
            int g = group[i - 1], p = profit[i - 1];
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k <= minProfit; k++) {
                    dp[i][j][k] = dp[i - 1][j][k];
                    if (j >= g) {
                        // 当前i盈利p,找i-1盈利k-p的方案数,若<0则按0处理,这样满足至少为k的盈利
                        dp[i][j][k] += dp[i - 1][j - g][Math.max(0, k - p)];
                        dp[i][j][k] %= mod;
                    }
                }
            }
        }
        return dp[m][n][minProfit];
    }

    //endregion----------------------------------------------------------------------------------------
    //endregion-----------------------------------------------------------------------------------------
    //region ---------------------------序列DP------------------------------------------------
// 5 最长回文子串
    public String longestPalindrome(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        int max = 0;
        String ans = "";
        for (int len = 1; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                if (s.charAt(j) == s.charAt(i) && (len < 3 || dp[i + 1][j - 1])) {
                    dp[i][j] = true;
                    if (len > max) {
                        max = len;
                        ans = s.substring(i, j + 1);
                    }
                }
            }
        }
        return ans;
    }

    // 647回文字串
    public int countSubstrings(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        int ans = 0;
        for (int j = 0; j < n; j++) {
            for (int i = 0; i <= j; i++) {
                if (s.charAt(i) == s.charAt(j) && (j - i < 2 || dp[i + 1][j - 1])) {
                    dp[i][j] = true;
                    ans++;
                }
            }
        }
        return ans;
    }

    //Manacher算法 O(n)
    public int countSubstrings2(String s) {
        int n = s.length();
        StringBuilder t = new StringBuilder("*#");
        for (int i = 0; i < n; ++i) {
            t.append(s.charAt(i));
            t.append('#');
        }
        n = t.length();
        t.append('!');

        int[] f = new int[n];
        int iMax = 0, rMax = 0, ans = 0;
        for (int i = 1; i < n; ++i) {
            // 初始化 f[i]
            f[i] = i <= rMax ? Math.min(rMax - i + 1, f[2 * iMax - i]) : 1;
            // 中心拓展
            while (t.charAt(i + f[i]) == t.charAt(i - f[i])) {
                ++f[i];
            }
            // 动态维护 iMax 和 rMax
            if (i + f[i] - 1 > rMax) {
                iMax = i;
                rMax = i + f[i] - 1;
            }
            // 统计答案, 当前贡献为 (f[i] - 1) / 2 上取整
            ans += f[i] / 2;
        }

        return ans;
    }

    //不重叠回文子字符串的最大数目
    public int maxPalindromes(String s, int k) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        for (int len = 1; len <= n; len++) {
            for (int l = 0; l + len - 1 < n; l++) {
                int r = l + len - 1;
                if (s.charAt(l) == s.charAt(r) && (len < 3 || dp[l + 1][r - 1])) {
                    dp[l][r] = true;
                }
            }
        }
        //维护 f(i) 表示长度为 i 的前缀中能选出多少个长度大等于 k 且不重叠的子串，转移方程为
        // f(i) = f(i-1) 不选i作为结尾的子串
        // f(i) = f(j)+1 if(i-j>=k && dp[j+1][i]=true)
        int[] f = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            f[i] = f[i - 1];
            for (int j = i - k; j >= 0; j--) {
                if (dp[j][i - 1]) {
                    f[i] = Math.max(f[i], f[j] + 1);
                }
            }
        }
        return f[n];
    }

    // 214 最短回文串 KMP算法 Hard
    public String shortestPalindrome(String s) {
        int n = s.length();
        int[] fail = new int[n];
        Arrays.fill(fail, -1);
        for (int i = 1; i < n; ++i) {
            int j = fail[i - 1];
            while (j != -1 && s.charAt(j + 1) != s.charAt(i)) {
                j = fail[j];
            }
            if (s.charAt(j + 1) == s.charAt(i)) {
                fail[i] = j + 1;
            }
        }
        int best = -1;
        for (int i = n - 1; i >= 0; --i) {
            while (best != -1 && s.charAt(best + 1) != s.charAt(i)) {
                best = fail[best];
            }
            if (s.charAt(best + 1) == s.charAt(i)) {
                ++best;
            }
        }
        String add = (best == n - 1 ? "" : s.substring(best + 1));
        StringBuffer ans = new StringBuffer(add).reverse();
        ans.append(s);
        return ans.toString();
    }

    // 131 分割回文串1
    //给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是 回文串 。返回 s 所有可能的分割方案。
    // 132 分割回文串2 dp预处理
    public int minCut(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        for (int right = 0; right < n; right++) {
            for (int left = 0; left <= right; left++) {
                if (s.charAt(right) == s.charAt(left) && (right - left <= 2 || dp[left + 1][right - 1])) {
                    dp[left][right] = true;
                }
            }
        }
        //前i个字符串切分回文的最小次数
        int[] cnt = new int[n];
        Arrays.fill(cnt, Integer.MAX_VALUE);
        for (int i = 0; i < n; i++) {
            //从0到i是一个回文串的话，0次切割
            if (dp[0][i]) {
                cnt[i] = 0;
            } else {
                //枚举i前面的每个字符，如果[j+1,i]是回文的，那么最小次数=[0,j]的次数+[j+1,i]这一次
                for (int j = 0; j < i; j++) {
                    if (dp[j + 1][i]) {
                        cnt[i] = Math.min(cnt[i], cnt[j] + 1);
                    }
                }
            }
        }
        return cnt[n - 1];
    }

    // 516 最长回文子序列
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[0][n - 1];
    }

    // 1312 让字符串成为回文串的最少插入次数
    public int minInsertions(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        for (int len = 1; len <= n; len++) {
            for (int l = 0; l + len - 1 < n; l++) {
                int r = l + len - 1;
                if (len == 1) {
                    dp[l][r] = 0;
                } else if (s.charAt(l) == s.charAt(r)) {
                    dp[l][r] = len == 2 ? 0 : dp[l + 1][r - 1];
                } else {
                    dp[l][r] = Math.min(dp[l + 1][r], dp[l][r - 1]) + 1;
                }
            }
        }
        return dp[0][n - 1];
    }

    //44 通配符匹配
    public boolean isMatch(String s, String p) {
        int m = s.length();
        int n = p.length();
        boolean[][] match = new boolean[m + 1][n + 1];
        match[0][0] = true;
        for (int i = 1; i <= n; i++) {
            if (p.charAt(i - 1) == '*') {
                match[0][i] = true;
            } else {
                break;
            }
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '?') {
                    match[i][j] = match[i - 1][j - 1];
                } else if (p.charAt(j - 1) == '*') {
                    match[i][j] = match[i - 1][j] || match[i][j - 1];
                }
            }
        }
        return match[m][n];
    }

    //10 正则表达式匹配
    public boolean isMatchRegx(String s, String p) {
        int length1 = s.length();
        int length2 = p.length();

        boolean[][] match = new boolean[length1 + 1][length2 + 1];

        match[0][0] = true;
        for (int j = 1; j < length2 + 1; j++) {
            if (p.charAt(j - 1) == '*') match[0][j] = match[0][j - 2];
        }
        for (int i = 1; i < length1 + 1; i++) {
            for (int j = 1; j < length2 + 1; j++) {
                if (p.charAt(j - 1) == s.charAt(i - 1) || p.charAt(j - 1) == '.') {
                    match[i][j] = match[i - 1][j - 1];
                } else if (p.charAt(j - 1) == '*') {
                    //ab ab*
                    if (s.charAt(i - 1) == p.charAt(j - 2) || p.charAt(j - 2) == '.') {
                        match[i][j] = match[i - 1][j] || match[i][j - 2];
                    } else {
                        match[i][j] = match[i][j - 2];
                    }
                }
            }
        }
        return match[length1][length2];
    }

    // 72 编辑距离
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i <= n; i++) {
            dp[0][i] = i;
        }
        int diff;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                diff = (word1.charAt(i - 1) == word2.charAt(j - 1)) ? 0 : 1;
                // j到i-1需要a步，j到i需要a+1步
                // i-1到j-1需要a步，ij不同+1步，相同不加
                dp[i][j] = Math.min(dp[i - 1][j - 1] + diff, Math.min(dp[i - 1][j], dp[i][j - 1]) + 1);
            }
        }
        return dp[m][n];
    }

    // 161 相隔为1 的编辑距离
    // s = abxcd   s=abcd     s=abcd
    // t = abycd   t=abycd    t=abcdx
    public boolean isOneEditDistance(String s, String t) {
        int m = s.length(), n = t.length();
        if (m > n) return isOneEditDistance(t, s);
        if (n - m > 1) return false;
        for (int i = 0; i < m; i++) {
            if (s.charAt(i) != t.charAt(i)) {
                if (m == n) {
                    return s.substring(i + 1).equals(t.substring(i + 1));
                } else {
                    return s.substring(i).equals(t.substring(i + 1));
                }
            }
        }
        return m + 1 == n;
    }

    // 664 奇怪的打印机 String painter
    // aba 两端字符一样，打印次数=ab的打印次数
    // abab 两端字符不一样，打印次数 = min([a,bab],[ab,ab],[aba,b])
    // 控制长度写法
    public int strangePrinter(String s) {
        int n = s.length();
        // dp[l][r] 代表从l到r最小打印次数
        int[][] dp = new int[n][n];
        for (int len = 1; len <= n; len++) {
            for (int l = 0; l + len - 1 < n; l++) {
                int r = l + len - 1;
                if (len == 1) {
                    dp[l][r] = 1;
                } else if (s.charAt(l) == s.charAt(r)) {
                    dp[l][r] = dp[l][r - 1];
                } else {
                    int min = Integer.MAX_VALUE;
                    for (int k = l; k < r; k++) {
                        min = Math.min(min, dp[l][k] + dp[k + 1][r]);
                    }
                    dp[l][r] = min;
                }
            }
        }
        return dp[0][n - 1];
    }

    // 倒序写法
    public int strangePrinter2(String s) {
        int n = s.length();
        int[][] f = new int[n][n];
        for (int i = n - 1; i >= 0; i--) {
            f[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    f[i][j] = f[i][j - 1];
                } else {
                    int minn = Integer.MAX_VALUE;
                    for (int k = i; k < j; k++) {
                        minn = Math.min(minn, f[i][k] + f[k + 1][j]);
                    }
                    f[i][j] = minn;
                }
            }
        }
        return f[0][n - 1];
    }


    // 392 判断子序列
    public boolean isSubsequence(String s, String t) {
        int m = s.length(), n = t.length();
        int i = 0, j = 0;
        while (i < m && j < n) {
            if (s.charAt(i) == t.charAt(j)) {
                i++;
            }
            j++;
        }
        return i == m;
    }

    public boolean isSubsequenceDP(String s, String t) {
        int m = s.length(), n = t.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int i = 1; i <= n; i++) {
            dp[0][i] = true;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = dp[i][j - 1];
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[i][j] |= dp[i - 1][j - 1];
                }
            }
        }
        return dp[m][n];
    }

    // LIS LCS LUS LHS
    // 1143 最长公共子序列 LCS问题
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i < m + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    //1638. 统计只差一个字符的子串数目
    // 枚举
    public int countSubstrings(String s, String t) {
        int m = s.length(), n = t.length();
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int diff = 0;
                for (int k = 0; i + k < m && j + k < n; k++) {
                    diff += (s.charAt(i + k) == t.charAt(j + k)) ? 0 : 1;
                    if (diff > 1) break;
                    if (diff == 1) ans++;
                }
            }
        }
        return ans;
    }

    // DP 最长公共子串
    public int countSubstrings2(String s, String t) {
        int m = s.length(), n = t.length();
        int ans = 0;
        int[][] dpl = new int[m + 1][n + 1];
        int[][] dpr = new int[m + 1][n + 1];
        // 此处即为最长公共子串的dp做法
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dpl[i][j] = s.charAt(i - 1) == t.charAt(j - 1) ? dpl[i - 1][j - 1] + 1 : 0;
            }
        }
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                dpr[i][j] = s.charAt(i) == t.charAt(j) ? dpr[i + 1][j + 1] + 1 : 0;
            }
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s.charAt(i - 1) != t.charAt(j - 1)) {
                    ans += (dpl[i - 1][j - 1] + 1) * (dpr[i][j] + 1);
                }
            }
        }
        return ans;
    }


    // 1035 不相交的线 =>转换LCS问题
    public int maxUncrossedLines(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] + 1);
                }
                //也能AC
//                if (nums1[i - 1] == nums2[j - 1]) {
//                    dp[i][j] = dp[i - 1][j - 1] + 1;
//                }else {
//                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
//                }
            }
        }
        return dp[m][n];
    }

    // 718 最长重复子数组
    //给两个整数数组 nums1 和 nums2 ，返回 两个数组中 公共的 、长度最长的子数组的长度 。
//输入：nums1 = [1,2,3,2,1], nums2 = [3,2,1,4,7] 输出：3 解释：长度最长的公共子数组是 [3,2,1] 。
    public int findLength(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        // 以i,j 结尾的最长公共子数组
        int[][] dp = new int[m + 1][n + 1];
        int max = 0;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    max = Math.max(max, dp[i][j]);
                }
            }
        }
        return max;
    }

    // 滑动窗口
    public int findLengthSlideWindow(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int ans = 0;
        for (int i = 0; i < m; i++) {
            int len = Math.min(n, m - i);
            int maxLen = maxLen(nums1, nums2, i, 0, len);
            ans = Math.max(ans, maxLen);
        }

        for (int i = 0; i < n; i++) {
            int len = Math.min(m, n - i);
            int maxLen = maxLen(nums1, nums2, 0, i, len);
            ans = Math.max(ans, maxLen);
        }
        return ans;
    }


    private int maxLen(int[] nums1, int[] nums2, int idx1, int idx2, int len) {
        int max = 0, common = 0;
        for (int i = 0; i < len; i++) {
            if (nums1[idx1 + i] == nums2[idx2 + i]) {
                common++;
            } else {
                common = 0;
            }
            max = Math.max(max, common);
        }
        return max;
    }

    // Hard https://leetcode.cn/problems/maximum-length-of-repeated-subarray/solution/zui-chang-zhong-fu-zi-shu-zu-by-leetcode-solution/
    // Rabin-Karp 算法来对序列进行哈希
    int mod = 1000000009;
    int base = 113;

    public int findLengthBinarySearch(int[] A, int[] B) {
        int left = 1, right = Math.min(A.length, B.length) + 1;
        while (left < right) {
            int mid = (left + right) >> 1;
            if (check(A, B, mid)) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left - 1;
    }

    public boolean check(int[] A, int[] B, int len) {
        long hashA = 0;
        for (int i = 0; i < len; i++) {
            hashA = (hashA * base + A[i]) % mod;
        }
        Set<Long> bucketA = new HashSet<Long>();
        bucketA.add(hashA);
        long mult = qPow(base, len - 1);
        for (int i = len; i < A.length; i++) {
            hashA = ((hashA - A[i - len] * mult % mod + mod) % mod * base + A[i]) % mod;
            bucketA.add(hashA);
        }
        long hashB = 0;
        for (int i = 0; i < len; i++) {
            hashB = (hashB * base + B[i]) % mod;
        }
        if (bucketA.contains(hashB)) {
            return true;
        }
        for (int i = len; i < B.length; i++) {
            hashB = ((hashB - B[i - len] * mult % mod + mod) % mod * base + B[i]) % mod;
            if (bucketA.contains(hashB)) {
                return true;
            }
        }
        return false;
    }

    // 使用快速幂计算 x^n % mod 的值
    public long qPow(long x, long n) {
        long ret = 1;
        while (n != 0) {
            if ((n & 1) != 0) {
                ret = ret * x % mod;
            }
            x = x * x % mod;
            n >>= 1;
        }
        return ret;
    }

    // LIS LCS LUS LHS
    // 1092 最短公共超序列 LCS问题
    public String shortestCommonSupersequence(String str1, String str2) {
        int m = str1.length(), n = str2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        int i = m - 1, j = n - 1;
        StringBuilder sb = new StringBuilder();
        while (i >= 0 || j >= 0) {
            if (i < 0) {
                sb.append(str2.charAt(j--));
            } else if (j < 0) {
                sb.append(str1.charAt(i--));
            } else if (str1.charAt(i) == str2.charAt(j)) {
                sb.append(str1.charAt(i));
                i--;
                j--;
            } else if (dp[i + 1][j] == dp[i + 1][j + 1]) {
                sb.append(str2.charAt(j--));
            } else {
                sb.append(str1.charAt(i--));
            }
        }
        return sb.reverse().toString();
    }

    // 521 最长特殊序列1
    public int findLUSlength1(String a, String b) {
        return a.equals(b) ? -1 : Math.max(a.length(), b.length());
    }

    // LIS LCS LUS LHS
    // 522 最长特殊序列2 LUS
    public int findLUSlength2(String[] strs) {
        int ans = -1;
        for (int i = 0; i < strs.length; i++) {
            boolean uncommon = true;
            for (int j = 0; j < strs.length; j++) {
                if (i != j && isSubSeq(strs[i], strs[j])) {
                    uncommon = false;
                    break;
                }
            }
            if (uncommon) ans = Math.max(ans, strs[i].length());
        }
        return ans;
    }

    //str1是不是str2的子序列
    private boolean isSubSeq(String str1, String str2) {
        int pntS = 0, pntT = 0;
        while (pntS < str1.length() && pntT < str2.length()) {
            if (str1.charAt(pntS) == str2.charAt(pntT)) {
                pntS++;
            }
            pntT++;
        }
        return pntS == str1.length();
    }

    // LIS LCS LUS LHS
    //LCS 问题与 LIS 问题的相互关系，以及 LIS 问题的最优解证明
    // https://mp.weixin.qq.com/s?__biz=MzU4NDE3MTEyMA==&mid=2247487814&idx=1&sn=e33023c2d474ff75af83eda1c4d01892&chksm=fd9cba59caeb334f1fbfa1aefd3d9b2ab6abfccfcab8cb1dbff93191ae9b787e1b4681bbbde3&token=252055586&lang=zh_CN#rd
    // 674 最长连续递增序列
    // 连续
    public int findLengthOfLCIS(int[] nums) {
        int n = nums.length;
        int cnt = 1;
        int max = 1;
        for (int i = 1; i < n; i++) {
            if (nums[i] > nums[i - 1]) {
                cnt++;
            } else {
                cnt = 1;
            }
            max = Math.max(max, cnt);
        }
        return max;
    }

    // 300 最长递增子序列  LIS 问题
    // 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
    // 子序列 是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序列。
    //输入：nums = [10,9,2,5,3,7,101,18]
    //输出：4
    //解释：最长递增子序列是 [2,3,7,101]，因此长度为 4 。
    // dp[i] 以第i个数结束的最长子序列长度
    // 不连续
    public int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        dp[0] = 1;
        int max = 1;
        for (int i = 1; i < nums.length; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[j] + 1, dp[i]);
                }
            }
            max = Math.max(dp[i], max);
        }
        return max;
    }

    // nums = [10,9,2,5,3,7,101,18]
    // tails = [2,3,7,18,0,0,0,0]
    public int lengthOfLIS2(int[] nums) {
        int n = nums.length;
        // 表示长度为 i 的最长上升子序列的末尾元素的最小值，是单调递增的
        int[] tails = new int[n];
        int idx = 0;
        for (int x : nums) {
            //找到tails数组[0,idx]范围内第一个大于等于x的坐标
            int l = 0, r = idx;
            while (l < r) {
                int mid = l + r >> 1;
                if (tails[mid] >= x) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            //第一个大于等于x的坐标赋值x
            tails[l] = x;
            //如果就是本身的位置，idx往后移
            if (r == idx) idx++;
        }
        return idx;
    }

    //368. 最大整除子集
    public List<Integer> largestDivisibleSubset(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int[] dp = new int[n];
        int[] g = new int[n];
        dp[0] = 1;
        g[0] = 0;
        int maxLen = 1, maxIdx = 0;
        for (int i = 1; i < n; i++) {
            dp[i] = 1;
            g[i] = i;
            for (int j = 0; j < i; j++) {
                if (nums[i] % nums[j] == 0) {
                    if (dp[j] + 1 > dp[i]) {
                        dp[i] = dp[j] + 1;
                        g[i] = j;
                    }
                }
            }
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                maxIdx = i;
            }
        }
        List<Integer> result = new ArrayList<>();
        while (result.size() < maxLen) {
            result.add(0, nums[maxIdx]);
            maxIdx = g[maxIdx];
        }
        return result;
    }

    // 673 最长递增子序列的个数
    public int findNumberOfLIS(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        int[] cnt = new int[n];
        int max = 1;
        for (int j = 0; j < n; j++) {
            dp[j] = 1;
            cnt[j] = 1;
            for (int i = 0; i < j; i++) {
                if (nums[j] > nums[i]) {
                    if (dp[j] < dp[i] + 1) {
                        dp[j] = dp[i] + 1;
                        cnt[j] = cnt[i];
                    } else if (dp[j] == dp[i] + 1) {
                        cnt[j] += cnt[i];
                    }
                }
            }
            max = Math.max(max, dp[j]);
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] == max) ans += cnt[i];
        }
        return ans;
    }

    //1027. 最长等差数列
    public int longestArithSeqLength(int[] nums) {
        int n = nums.length;
        int ans = 0;
        // 以i结尾 公差为j的最长长度
        int[][] f = new int[n][1001];
        for (int i = 1; i < n; ++i) {
            //枚举i前面的元素
            for (int k = 0; k < i; ++k) {
                //公差范围[-500，500]，j>=0故+500
                int j = nums[i] - nums[k] + 500;
                f[i][j] = Math.max(f[i][j], f[k][j] + 1);
                ans = Math.max(ans, f[i][j]);
            }
        }
        return ans + 1;
    }

    //1048. 最长字符串链 LIS 问题
    public int longestStrChain(String[] words) {
        int n = words.length;
        Arrays.sort(words, Comparator.comparingInt(String::length));
        int[] dp = new int[n];
        int max = 0;
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (isPrev(words, j, i)) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            max = Math.max(dp[i], max);
        }
        return max;
    }

    private boolean isPrev(String[] words, int x, int y) {
        if (words[x].length() + 1 != words[y].length()) return false;
        int idx1 = 0, idx2 = 0;
        int diff = 0;
        while (idx1 < words[x].length() && idx2 < words[y].length()) {
            if (words[x].charAt(idx1) == words[y].charAt(idx2)) {
                idx1++;
                idx2++;
            } else {
                idx2++;
                diff++;
            }
        }
        if (diff == 0 && idx1 == words[x].length() && words[y].length() - idx2 == 1) return true;
        return diff == 1;
    }

    public int longestStrChain2(String[] words) {
        Arrays.sort(words, Comparator.comparingInt(String::length));
        Map<String, Integer> cnt = new HashMap<>();
        int max = 0;
        for (String word : words) {
            cnt.put(word, 1);
            for (int i = 0; i < word.length(); i++) {
                String prev = word.substring(0, i) + word.substring(i + 1);
                if (cnt.containsKey(prev)) {
                    cnt.put(word, Math.max(cnt.get(word), cnt.get(prev) + 1));
                }
            }
            max = Math.max(cnt.get(word), max);
        }
        return max;
    }

    //1671. 得到山形数组的最少删除次数
    public int minimumMountainRemovals(int[] nums) {
        int n = nums.length;
        int[] prefixLen = getLIS(nums);
        int[] suffixLex = reverseArray(getLIS(reverseArray(nums)));

        int max = 0;
        for (int i = 0; i < n; i++) {
            if(prefixLen[i]>1 && suffixLex[i]>1){
                int len = prefixLen[i] + suffixLex[i] - 1;
                max = Math.max(max, len);
            }
        }
        return n - max;
    }

    private int[] reverseArray(int[] nums) {
        int n = nums.length;
        int[] copy = new int[n];
        for (int i = 0; i < n; i++) {
            copy[n - i - 1] = nums[i];
        }
        return copy;
    }

    private int[] getLIS(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        return dp;
    }

//1223. 掷骰子模拟  高维dp
// 首先，我们创建一个二维dp数组。
//  dp[i][j]表示第i次掷出骰子时，数字j出现的可能的序列总数。
// （也就是说，第i次掷出的骰子数字是 j 所有可能的序列数)
// 其中 1 <= i <= n    1 <= j <= 6
//
// 明显，dp[1][1],dp[1][2]... dp[1][6]均为 1
// 所以，最后结果有效序列总数就是 sum (dp[n][1] + dp[n][2] + ... + dp[n][6])  | sum为求和函数
//
// 那么，如何计算第i次骰子掷出时，掷出数字为j的序列总数为多少呢?
// 仔细思考一下dp[i][j]和什么有关?
//
// 第一: dp[i][j] 和dp[i-1][j]有关，不仅如此，dp[i][j] 和 dp[i-1][1], dp[i-1][2],...dp[i-1][6]都有关
// 第二: 由于连续数字限制，dp[i][j]还和 dp[i-rollMax[j-1]][1],...,dp[i-rollMax[j-1]][6]均有关
// 即， 第i次掷出骰子的序列总数只和第i-1次掷出骰子的序列总数，以及第i-rollMax[j-1]次掷出骰子的序列总数有关。
//
// --------------------------------------------------举例----------------------------------------------------------------
//
// 这么说 可能还是不够清楚， 举个例子
//
// 假如现在是第 5 次掷出骰子， 且掷出的数字是 6, 而最多能连续出现 3 次 6， dp[5][6]如何计算?
//
// 序列  ___  ___  ___   ___   6
// 次数   1    2    3     4    5
//
//① 如果第 4 次掷出的数字是 1，2，3，4，5 中的一种， 会不会对第 5 次掷出的 6 产生影响?
//  答案是 不会，因为如果第 4 次不是 6，那么第 5 次掷出的 6 肯定是第一个 6， 肯定不会连续。
//  所以不用考虑限制数组rollMax。
//  也就是说，可以直接将 dp[4][1]，dp[4][2]，dp[4][3]，dp[4][4]，dp[4][5]加入到 dp[5][6] 中。
//
//  5种可能
//  序列  ___  ___  ___    1   6       序列  ___  ___  ___    2   6  ...  序列  ___  ___  ___    5   6
//  次数   1    2    3     4   5       次数   1    2    3     4   5  ...  次数   1    2    3     4   5
//
//② 如果第 4 次掷出的数字是 6 ，会不会对第 5 次掷出的 6 产生影响?
//  答案是 不一定。为什么是不一定? 因为第 4 次掷出的 6 加上第五次掷出的 6 可能都还没达到rollMax中所设置的上限。
//  那么，可以先将dp[4][6] 加入到dp[5][6]中去。只是后面需要去除不合法的序列罢了。（注意）
//
//  类似这种
//  序列  ___  ___  ___    6   6      // 序列  ___  ___   6    6   6
//  次数   1    2    3     4   5      // 次数   1    2    3    4   5
//
//③ 好的，第②步中我们多加了一些不合法的序列数目，那么，我们要将其减掉。那么到底需要减去多少呢?
//
//  我们先思考一个问题， 第 5 次掷出数字 6 时，掷出之前连续 6 的数量最大有多少?
//  答案是 rollMax[5]（数字 6 的上限），不可能超过该数字 ，
//  因为如果超过了rollMax[5]（6的上限），在第 4 次肯定就已经被处理了。
//
//  那么，现在又存在两种情况：
//  a. 第 5 次掷出数字 6 之前连续 6 的数量 < rollMax[5] （6的上限）
//  b. 第 5 次掷出数字 6 之前连续 6 的数量 == rollMax[5] （6的上限）
//  情况a. 我们不需要过多考虑，因为还没有达到上限，直接将dp[4][6]加入dp[5][6] 即可（前面已经加入）
//  情况b. 在第 5 次掷出之前连续 6 的数量就已经到达了上限，那么第 5 次掷出 6 是非法的，
//  这种情况下的序列数目就是我们步骤②中需要减去的数量
//
//  情况a. （合法的）                             情况b. （不合法的）
//  序列  ___  ___   ___   6   6                 序列  ___   6    6    6   6
//  次数   1    2     3    4   5                 次数   1    2    3    4   5

    //  仔细分析一下情况 b.
//  在第 5 次掷出之前连续 6 的数量就已经到达了上限，说明第 2 次，第 3 次，第 4 次掷出的数字一定都是6，
//  而且，第1次掷出的数字一定不是6。
//  结果也就很明显了吧，第 1 次不是 6 ，那就是 1，2，3，4，5 中的一种呗!!!
//  需要减去的序列数量为: sum (dp[1][1] + dp[1][2] + dp[1][3] + dp[1][4] + dp[1][5])
//  情况b.2 （不合法的）
//  序列   6    6     6    6   6
//  次数   1    2     3    4   5
//  情况b.2 rollMax[5] = 4
//  idx = i-1-rollMax[5] = 0，此时-1（即1234全是6这一种情况）即可
    public int dieSimulator(int n, int[] rollMax) {
        int mod = (int) 1e9 + 7;
        long[][] dp = new long[n + 1][6];
        long[] sum = new long[n + 1];
        Arrays.fill(dp[1], 1);
        sum[1] = 6;
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < 6; j++) {
                int idx = i - rollMax[j] - 1;
                if (idx > 0) {
                    dp[i][j] = ((((sum[i - 1] - sum[idx]) % mod + mod) % mod + dp[idx][j]) % mod + mod) % mod;
                } else if (idx == 0) {
                    dp[i][j] = (sum[i - 1] - 1) % mod;
                } else {
                    dp[i][j] = sum[i - 1] % mod;
                }
                sum[i] = (sum[i] + dp[i][j]) % mod;
            }
        }
        return (int) (sum[n] % mod);
    }

    //2501. 数组中最长的方波
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

    // 354 俄罗斯套娃信封问题
    public int maxEnvelopes(int[][] envelopes) {
        int n = envelopes.length;
        // 固定w维度
        Arrays.sort(envelopes, (o1, o2) -> {
            if (o1[0] != o2[0]) {
                return o1[0] - o2[0];
            }
            return o2[1] - o1[1];
        });

        int[] dp = new int[n];
        dp[0] = 1;
        int max = 1;
        // 根据h维度的LIS问题
        for (int i = 1; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (envelopes[j][1] < envelopes[i][1]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    public int maxEnvelopes2(int[][] envelopes) {
        if (envelopes.length == 0) {
            return 0;
        }

        int n = envelopes.length;
        Arrays.sort(envelopes, (e1, e2) -> {
            if (e1[0] != e2[0]) {
                return e1[0] - e2[0];
            } else {
                return e2[1] - e1[1];
            }
        });

        int[] tails = new int[n];
        int idx = 0;
        for (int[] ev : envelopes) {
            int l = 0, r = idx;
            while (l < r) {
                int mid = l + r >> 1;
                if (tails[mid] >= ev[1]) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            tails[l] = ev[1];
            if (r == idx) idx++;
        }
        return idx;
    }

    //1626. 无矛盾的最佳球队
    public int bestTeamScore(int[] scores, int[] ages) {
        int n = scores.length;
        int[][] array = new int[n][2];
        for (int i = 0; i < n; i++) {
            array[i] = new int[]{ages[i], scores[i]};
        }
        Arrays.sort(array, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];
            return o1[1] - o2[1];
        });
        int[] dp = new int[n];
        dp[0] = array[0][1];
        int max = dp[0];
        for (int i = 1; i < n; i++) {
            dp[i] = array[i][1];
            for (int j = 0; j < i; j++) {
                if (array[j][1] <= array[i][1]) {
                    dp[i] = Math.max(dp[i], dp[j] + array[i][1]);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;

    }

    //1691. 堆叠长方体的最大高度
    public int maxHeight(int[][] cuboids) {
        int n = cuboids.length;
        for (int[] v : cuboids) {
            // 把长宽高重新排列，每一个长方体都是高度最高
            Arrays.sort(v);
        }
        Arrays.sort(cuboids, Comparator.comparingInt(a -> (a[0] + a[1] + a[2])));
        int ans = 0;
        // LIS问题
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = cuboids[i][2];
            for (int j = 0; j < i; j++) {
                if (cuboids[i][0] >= cuboids[j][0] &&
                        cuboids[i][1] >= cuboids[j][1] &&
                        cuboids[i][2] >= cuboids[j][2]) {
                    dp[i] = Math.max(dp[i], dp[j] + cuboids[i][2]);
                }
            }
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }

    public int maxHeightDFS(int[][] cuboids) {
        int n = cuboids.length;
        for (int[] v : cuboids) {
            Arrays.sort(v);
        }
        Arrays.sort(cuboids, Comparator.comparingInt(a -> (a[0] + a[1] + a[2])));

        int[] memo = new int[n];
        Arrays.fill(memo, -1);
        return dfs(cuboids, memo, -1, 0);
    }

    public int dfs(int[][] cuboids, int[] memo, int top, int index) {
        if (index == cuboids.length) {
            return 0;
        }
        if (top != -1 && memo[top] != -1) {
            return memo[top];
        }
        int height = dfs(cuboids, memo, top, index + 1);
        if (top == -1 || check(cuboids[top], cuboids[index])) {
            height = Math.max(height, cuboids[index][2] + dfs(cuboids, memo, index, index + 1));
        }
        if (top != -1) {
            memo[top] = height;
        }
        return height;
    }

    public boolean check(int[] a, int[] b) {
        return a[0] <= b[0] && a[1] <= b[1] && a[2] <= b[2];
    }

    // 面试08.13 堆箱子
    //堆箱子。给你一堆n个箱子，箱子宽 wi、深 di、高 hi。箱子不能翻转，将箱子堆起来时，下面箱子的宽度、高度和深度必须大于上面的箱子。实现一种方法，搭出最
//高的一堆箱子。箱堆的高度为每个箱子高度的总和。
    public int pileBox(int[][] box) {
        int n = box.length;
        Arrays.sort(box, Comparator.comparingInt(o -> o[0]));
        int[] dp = new int[n];
        int ans = 0;
        for (int i = 0; i < n; i++) {
            dp[i] = box[i][2];
            for (int j = 0; j < i; j++) {
                if (box[i][0] > box[j][0] && box[i][1] > box[j][1] && box[i][2] > box[j][2]) {
                    dp[i] = Math.max(dp[i], dp[j] + box[i][2]);
                }
            }
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }

    //面试题 17.08. 马戏团人塔
    // TLE O(n^2)
    public int bestSeqAtIndex(int[] height, int[] weight) {
        int n = height.length;
        int[][] array = new int[n][2];
        for (int i = 0; i < n; i++) {
            array[i] = new int[]{height[i], weight[i]};
        }
        Arrays.sort(array, (o1, o2) -> {
            if (o1[0] != o2[0]) {
                return o1[0] - o2[0];
            }
            return o2[1] - o1[1];
        });
        int[] dp = new int[n];
        dp[0] = 1;
        int max = 1;

        for (int i = 1; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (array[j][1] < array[i][1]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    // O(nlogn)
    public int bestSeqAtIndex2(int[] height, int[] weight) {
        int n = height.length;
        int[][] array = new int[n][2];
        for (int i = 0; i < n; i++) {
            array[i] = new int[]{height[i], weight[i]};
        }
        // h正序排,w倒序排,避免h相同时选多个h一样的人,w倒序时,选中一个w时,后面的w都小于该w,不可能同时被选
        Arrays.sort(array, (o1, o2) -> {
            if (o1[0] != o2[0]) {
                return o1[0] - o2[0];
            }
            return o2[1] - o1[1];
        });
        int[] tails = new int[n];
        int idx = 0;
        for (int[] a : array) {
            int l = 0, r = idx;
            while (l < r) {
                int mid = l + r >> 1;
                if (tails[mid] >= a[1]) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            tails[l] = a[1];
            if (idx == l) idx++;
        }
        return idx;
    }

    //646. 最长数对链
    public int findLongestChainDP1(int[][] pairs) {
        int n = pairs.length;
        Arrays.sort(pairs, Comparator.comparingInt(o -> o[0]));
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int max = 1;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (pairs[j][1] < pairs[i][0]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    public int findLongestChainGreedyBS(int[][] pairs) {
        Arrays.sort(pairs, Comparator.comparingInt(o -> o[0]));
        List<Integer> list = new ArrayList<>();
        for (int[] pair : pairs) {
            int l = pair[0], r = pair[1];
            if (list.isEmpty() || list.get(list.size() - 1) < l) {
                list.add(r);
            } else {
                int idx = binarySearch(list, l);
                list.set(idx, Math.min(r, list.get(idx)));
            }
        }
        return list.size();
    }

    private int binarySearch(List<Integer> list, int x) {
        int l = 0, r = list.size() - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (list.get(mid) < x) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    public int findLongestChainGreedy(int[][] pairs) {
        int n = pairs.length;
        Arrays.sort(pairs, Comparator.comparingInt(o -> o[1]));
        int ans = 1;
        int right = pairs[0][1];
        for (int i = 1; i < n; i++) {
            if (pairs[i][0] > right) {
                ans++;
                right = pairs[i][1];
            }
        }
        return ans;
    }

    // 1668 最大重复子字符串  最长连续序列问题
    public int maxRepeating(String sequence, String word) {
        int n = sequence.length(), m = word.length();
        if (n < m) return 0;
        int[] dp = new int[n];
        for (int i = m - 1; i < n; i++) {
            boolean valid = true;
            for (int j = 0; j < m; j++) {
                if (sequence.charAt(i - m + 1 + j) != word.charAt(j)) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                dp[i] = (i == m - 1 ? 0 : dp[i - m]) + 1;
            }
        }
        return Arrays.stream(dp).max().getAsInt();
    }

    // 410 分割数组的最大值
    //「将数组分割为 m 段，求……」是动态规划题目常见的问法
    // 二分做法搜splitArray
    public int splitArrayDP(int[] nums, int m) {
        int n = nums.length;
        // 前i个元素分成j端，最大连续子数组和的最小值
        int[][] dp = new int[n + 1][m + 1];
        for (int[] array : dp) {
            Arrays.fill(array, Integer.MAX_VALUE);
        }
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + nums[i - 1];
        }
        dp[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= Math.min(m, i); j++) {
                for (int k = 0; k < i; k++) {
                    dp[i][j] = Math.min(dp[i][j], Math.max(dp[k][j - 1], sum[i] - sum[k]));
                }
            }
        }
        return dp[n][m];
    }

    // 435. 无重叠区间 TLE
    // 搜eraseOverlapIntervalsGreedy
    public int eraseOverlapIntervalsDP(int[][] intervals) {
        int n = intervals.length;
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));
        int[] dp = new int[n];//以i结尾的递增区间的最大长度
        Arrays.fill(dp, 1);
        int max = 1; //最小就是1个数组
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (intervals[j][1] <= intervals[i][0]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return n - max;
    }

    //2304. 网格中的最小路径代价
    public int minPathCost(int[][] grid, int[][] moveCost) {
        int m = grid.length, n = grid[0].length;
        int[][] dp = new int[m][n];
        for (int[] row : dp) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        System.arraycopy(grid[0], 0, dp[0], 0, n);
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    dp[i][j] = Math.min(dp[i][j], grid[i][j] + dp[i - 1][k] + moveCost[grid[i - 1][k]][j]);
                }
            }
        }
        int ans = Integer.MAX_VALUE;
        for (int j = 0; j < n; j++) {
            ans = Math.min(ans, dp[m - 1][j]);
        }
        return ans;
    }

    //940 不同的子序列
    public int distinctSubseqIITLE(String s) {
        Set<String> set = new HashSet<>();
        StringBuilder path = new StringBuilder();
        dfs(set, path, s, 0);
        return set.size() - 1;
    }

    private void dfs(Set<String> set, StringBuilder path, String s, int idx) {
        set.add(path.toString());

        for (int i = idx; i < s.length(); i++) {
            path.append(s.charAt(i));
            dfs(set, path, s, i + 1);
            path.deleteCharAt(path.length() - 1);
        }
    }

    public int distinctSubseqII(String s) {
        final int MOD = 1000000007;
        int[] last = new int[26];
        Arrays.fill(last, -1);

        int n = s.length();
        // 以i结尾的子序列数目
        // i前一位可以从0，i-1选 =》f[0]+..+f[i-1]
        // 为避免重复，选最后一个字符出现的位置
        int[] f = new int[n];
        Arrays.fill(f, 1);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < 26; ++j) {
                if (last[j] != -1) {
                    f[i] = (f[i] + f[last[j]]) % MOD;
                }
            }
            last[s.charAt(i) - 'a'] = i;
        }

        int ans = 0;
        for (int i = 0; i < 26; ++i) {
            if (last[i] != -1) {
                ans = (ans + f[last[i]]) % MOD;
            }
        }
        return ans;
    }

    public int distinctSubseqII2(String s) {
        final int MOD = 1000000007;
        int[] g = new int[26];
        int n = s.length();
        for (int i = 0; i < n; ++i) {
            int total = 1;
            for (int j = 0; j < 26; ++j) {
                total = (total + g[j]) % MOD;
            }
            g[s.charAt(i) - 'a'] = total;
        }

        int ans = 0;
        for (int i = 0; i < 26; ++i) {
            ans = (ans + g[i]) % MOD;
        }
        return ans;
    }

    public int distinctSubseqII3(String s) {
        final int MOD = 1000000007;
        int[] g = new int[26];
        int n = s.length(), total = 0;
        for (int i = 0; i < n; ++i) {
            int oi = s.charAt(i) - 'a';
            int prev = g[oi];
            g[oi] = (total + 1) % MOD;
            total = ((total + g[oi] - prev) % MOD + MOD) % MOD;
        }
        return total;
    }


    // 1713 得到子序列的最少操作次数 抽象出LCS-> 转化LIS问题 Hard
    public int minOperations(int[] t, int[] arr) {
        int n = t.length, m = arr.length;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(t[i], i);
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int x = arr[i];
            if (map.containsKey(x)) list.add(map.get(x));
        }
        int len = list.size();
        int[] f = new int[len], g = new int[len + 1];
        Arrays.fill(g, Integer.MAX_VALUE);
        int max = 0;
        for (int i = 0; i < len; i++) {
            int l = 0, r = len;
            while (l < r) {
                int mid = l + r + 1 >> 1;
                if (g[mid] < list.get(i)) l = mid;
                else r = mid - 1;
            }
            int clen = r + 1;
            f[i] = clen;
            g[clen] = Math.min(g[clen], list.get(i));
            max = Math.max(max, clen);
        }
        return n - max;
    }

    //873. 最长的斐波那契子序列的长度
    // 暴力遍历
    public int lenLongestFibSubseq(int[] arr) {
        int ans = 0;
        int n = arr.length;
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                int a = arr[i], b = arr[j], len = 2;
                for (int k = j + 1; k < n; k++) {
                    int c = arr[k];
                    // 满足斐波那契数列，迭代往后遍历累加
                    if (a + b == c) {
                        a = b;
                        b = c;
                        ans = Math.max(++len, ans);
                    } else if (a + b < c) {
                        break;
                    }
                }
            }
        }
        return ans;
    }

    public int lenLongestFibSubseqDP(int[] arr) {
        int n = arr.length;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(arr[i], i);
        }

        // 以j,i结尾的斐波那契数列长度
        int[][] dp = new int[n][n];
        int ans = 0;
        // 对每一个i，遍历i前面的j，找arr[i]-arr[j]是否存在，arr[k]+arr[j]==arr[i]的最小长度是3
        for (int i = 0; i < n; i++) {
            for (int j = i - 1; j >= 0 && arr[j] * 2 > arr[i]; j--) {
                int k = map.getOrDefault(arr[i] - arr[j], -1);
                if (k >= 0) {
                    dp[j][i] = Math.max(dp[k][j] + 1, 3);
                }
                ans = Math.max(ans, dp[j][i]);
            }
        }
        return ans;
    }

    // 813 最大平均值和的分组 子数组问题 结合前缀和
    public double largestSumOfAverages(int[] nums, int k) {
        int n = nums.length;
        double[] sum = new double[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + nums[i - 1];
        }
        double[][] dp = new double[n + 1][k + 1];
        for (int i = 1; i <= n; i++) {
            dp[i][1] = sum[i] / i;
        }
        for (int j = 2; j <= k; j++) {
            for (int i = j; i <= n; i++) {
                for (int x = j - 1; x < i; x++) {
                    dp[i][j] = Math.max(dp[i][j], dp[x][j - 1] + (sum[i] - sum[x]) / (i - x));
                }
            }
        }
        return dp[n][k];
    }

    // endregion--------------------------------------------------------------------
    //region------------------------------------------区间DP------------------------------------------------------
    //区间动态规划三部曲   回文子串
    //1.定义状态：dp[i, j]为区间[i, j]的最优解
    //2.定义状态转移方程：最常见的写法为：dp[i,j] = max/min{dp[i,j], dp[i, k] + dp[k+1, j] + cost}。
    //  选取[i, j]之间的一个分界点k，分别计算[i, k]和[k+1, j]的最优解，从而组合出[i, j]的最优解。
    //3.初始化：dp[i][i] = 常数。区间长度为1时的最优解应当是已知的。
    //写法1：
    //for (int i = n; i >= 1; --i) {
    //	for (int j = i + 1; j <= n; ++j) {
    //		for (int k = i; k < j; ++k) {
    //			dp[i,j] = max/min(dp[i,j], dp[i,k] + dp[k+1, j] + cost)
    //		}
    //	}
    //}
    //写法2：
    //for (int len = 2; len <= n; ++len) {
    //	for (int i = 1; i + len - 1  <= n; ++i) {
    //		int j = i + len - 1;
    //		for (int k = i; k < j; ++k) {
    //			dp[i,j] = max/min(dp[i,j], dp[i,k] + dp[k+1, j] + cost)
    //		}
    //	}
    //}
    //1000. 合并石头的最低成本
    //定义dp[i][j][k]为合并第i堆到第j堆石头为k堆的成本，状态转移方程有关键两点：
    //dp[i][j][1] = dp[i][j][k] + sum(i, j)。不能直接求出合并为1堆的成本，得先合并成k堆。
    //dp[i][j][m] = min(dp[i][p][1] + dp[p + 1][j][m - 1])，i <= p < j，2 <= m <= k。
    // 这里m为堆数，不能直接用k是因为右部分的存在，要对右部分继续划分求解的话，子问题就必须有合并成m堆的情况。
    public int mergeStones(int[] stones, int k) {
        int n = stones.length;
        if ((n - 1) % (k - 1) != 0) return -1;
        int MAX_VALUE = 9999999;
        int[][][] dp = new int[n + 1][n + 1][k + 1];
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + stones[i - 1];
        }
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                for (int m = 2; m <= k; m++) {
                    dp[i][j][m] = MAX_VALUE;
                }
            }
            dp[i][i][1] = 0;
        }
        for (int len = 2; len <= n; len++) {
            for (int i = 1; i + len - 1 <= n; i++) {
                int j = i + len - 1;
                //分成k堆时，从分成2堆开始自底向上
                for (int m = 2; m <= k; m++) {
                    for (int p = i; p < j; p += k - 1) {
                        // m堆只能是1堆和m-1堆
                        dp[i][j][m] = Math.min(dp[i][j][m], dp[i][p][1] + dp[p + 1][j][m - 1]);
                    }
                }
                // 合并成1堆要先分成k堆
                dp[i][j][1] = dp[i][j][k] + sum[j] - sum[i - 1];
            }
        }
        return dp[1][n][1];
    }

    public int mergeStones2(int[] stones, int k) {
        int n = stones.length;
        if ((n - 1) % (k - 1) != 0) return -1;
        int[][] dp = new int[n + 1][n + 1];
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; ++i) sum[i] = sum[i - 1] + stones[i - 1];
        for (int len = k; len <= n; ++len) { // 枚举区间长度
            for (int i = 1; i + len - 1 <= n; ++i) { // 枚举区间起点
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                for (int p = i; p < j; p += k - 1) {  // 枚举分界点
                    dp[i][j] = Math.min(dp[i][j], dp[i][p] + dp[p + 1][j]);
                }
                if ((j - i) % (k - 1) == 0) dp[i][j] += sum[j] - sum[i - 1];
            }
        }
        return dp[1][n];
    }

    // 62 不同路径
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }

    // 63 不同路径2
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        dp[0][0] = obstacleGrid[0][0] == 1 ? 0 : 1;
        for (int i = 1; i < m; i++) {
            dp[i][0] = obstacleGrid[i][0] == 1 ? 0 : dp[i - 1][0];
        }
        for (int i = 1; i < n; i++) {
            dp[0][i] = obstacleGrid[0][i] == 1 ? 0 : dp[0][i - 1];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = obstacleGrid[i][j] == 1 ? 0 : dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }

    //980. 不同路径 III
    public int uniquePathsIII(int[][] grid) {
        int r = grid.length, c = grid[0].length;
        int si = 0, sj = 0, n = 0;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (grid[i][j] == 0) {
                    n++;
                } else if (grid[i][j] == 1) {
                    n++;
                    si = i;
                    sj = j;
                }
            }
        }
        return dfs980(grid, si, sj, n);
    }

    public int dfs980(int[][] grid, int i, int j, int n) {
        if (grid[i][j] == 2) {
            return n == 0 ? 1 : 0;
        }
        int r = grid.length, c = grid[0].length;
        int t = grid[i][j];
        grid[i][j] = -1;
        int res = 0;
        for (int[] dir : dirs) {
            int ni = i + dir[0], nj = j + dir[1];
            if (ni >= 0 && ni < r && nj >= 0 && nj < c && (grid[ni][nj] == 0 || grid[ni][nj] == 2)) {
                res += dfs980(grid, ni, nj, n - 1);
            }
        }
        grid[i][j] = t;
        return res;
    }

    //记忆化+状态压缩
    Map<Integer, Integer> memo980 = new HashMap<>();
    public int uniquePathsIII2(int[][] grid) {
        int r = grid.length, c = grid[0].length;
        int si = 0, sj = 0, st = 0;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (grid[i][j] == 0 || grid[i][j] == 2) {
                    st |= 1 << (i * c + j);
                } else if (grid[i][j] == 1) {
                    si = i;
                    sj = j;
                }
            }
        }
        return dp980(grid, si, sj, st);
    }

    public int dp980(int[][] grid, int i, int j, int st) {
        if (grid[i][j] == 2) {
            return st == 0 ? 1 : 0;
        }
        int r = grid.length, c = grid[0].length;
        int key = ((i * c + j) << (r * c)) + st;
        if (!memo980.containsKey(key)) {
            int res = 0;
            for (int[] dir : dirs) {
                int ni = i + dir[0], nj = j + dir[1];
                if (ni >= 0 && ni < r && nj >= 0 && nj < c && (st & (1 << (ni * c + nj))) > 0) {
                    res += dp980(grid, ni, nj, st ^ (1 << (ni * c + nj)));
                }
            }
            memo980.put(key, res);
        }
        return memo980.get(key);
    }
    // 64 最小路径和
    public int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] dp = new int[m][n];
        dp[0][0] = grid[0][0];
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }
        for (int i = 1; i < n; i++) {
            dp[0][i] = dp[0][i - 1] + grid[0][i];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i - 1][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }


    // 97 交错字符串
    public boolean isInterleave(String s1, String s2, String s3) {
        int m = s1.length(), n = s2.length();
        if (m + n != s3.length()) return false;
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i > 0 && s1.charAt(i - 1) == s3.charAt(i + j - 1)) {
                    dp[i][j] = dp[i - 1][j];
                }
                if (j > 0 && s2.charAt(j - 1) == s3.charAt(i + j - 1)) {
                    dp[i][j] |= dp[i][j - 1];
                }
            }
        }
        return dp[m][n];
    }

    // offer 13 机器人的运动范围
    public int movingCount(int m, int n, int k) {
        boolean[][] visited = new boolean[m][n];
        return dfs(0, 0, m, n, k, visited);
    }

    private int dfs(int i, int j, int m, int n, int k, boolean[][] visited) {
        if (i >= m || j >= n || visited[i][j] || (getSum(i) + getSum(j)) > k) return 0;
        int ans = 1;
        visited[i][j] = true;
        ans += dfs(i + 1, j, m, n, k, visited);
        ans += dfs(i, j + 1, m, n, k, visited);
        return ans;
    }

    private int getSum(int x) {
        int res = 0;
        while (x != 0) {
            res += x % 10;
            x /= 10;
        }
        return res;
    }

    //118 杨辉三角
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> ret = new ArrayList<>();
        for (int i = 0; i < numRows; ++i) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j <= i; ++j) {
                if (j == 0 || j == i) {
                    row.add(1);
                } else {
                    row.add(ret.get(i - 1).get(j - 1) + ret.get(i - 1).get(j));
                }
            }
            ret.add(row);
        }
        return ret;
    }

    // 119杨辉三角2
    public List<Integer> getRow(int rowIndex) {
        List<List<Integer>> ret = new ArrayList<>();
        for (int i = 0; i <= rowIndex; ++i) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j <= i; ++j) {
                if (j == 0 || j == i) {
                    row.add(1);
                } else {
                    row.add(ret.get(i - 1).get(j - 1) + ret.get(i - 1).get(j));
                }
            }
            if (i == rowIndex) {
                return row;
            }
            ret.add(row);
        }
        return new ArrayList<>();
    }

    // 120 三角形的最小路径和
    public int minimumTotal(List<List<Integer>> triangle) {
        if (triangle.size() == 0) return 0;
        int n = triangle.size();
        int[][] dp = new int[n][n];
        dp[0][0] = triangle.get(0).get(0);
        for (int i = 1; i < n; i++) {
            dp[i][0] = dp[i - 1][0] + triangle.get(i).get(0);
            for (int j = 1; j < i; j++) {
                dp[i][j] = Math.min(dp[i - 1][j - 1], dp[i - 1][j]) + triangle.get(i).get(j);
            }
            dp[i][i] = dp[i - 1][i - 1] + triangle.get(i).get(i);
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            min = Math.min(min, dp[n - 1][i]);
        }
        return min;
    }

    //空间O（n）
    public int minimumTotal2(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[] f = new int[n];
        f[0] = triangle.get(0).get(0);
        for (int i = 1; i < n; ++i) {
            // 每一层，后n-i个是空的
            // 先放第i个，前i个还是上一层的值
            f[i] = f[i - 1] + triangle.get(i).get(i);
            for (int j = i - 1; j > 0; --j) {
                f[j] = Math.min(f[j - 1], f[j]) + triangle.get(i).get(j);
            }
            f[0] += triangle.get(i).get(0);
        }
        int minTotal = f[0];
        for (int i = 1; i < n; ++i) {
            minTotal = Math.min(minTotal, f[i]);
        }
        return minTotal;
    }

    //最大正方形
    public int maximalSquare(char[][] matrix) {
        if (matrix.length <= 0 || matrix[0].length <= 0) {
            return 0;
        }
        int[][] dp = new int[matrix.length][matrix[0].length];
        int max = 0;
        for (int i = 0; i < matrix.length; i++) {
            dp[i][0] = matrix[i][0] == '1' ? 1 : 0;
            max = Math.max(dp[i][0], max);
        }
        for (int j = 1; j < matrix[0].length; j++) {
            dp[0][j] = matrix[0][j] == '1' ? 1 : 0;
            max = Math.max(dp[0][j], max);
        }

        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][j] == '1') {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                    max = Math.max(dp[i][j], max);
                } else {
                    dp[i][j] = 0;
                }
            }
        }
        return max * max;
    }

    //1130. 叶值的最小代价生成树
    public int mctFromLeafValuesDP(int[] arr) {
        int n = arr.length;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE / 4);
        }
        int[][] mval = new int[n][n];
        for (int j = 0; j < n; j++) {
            mval[j][j] = arr[j];
            dp[j][j] = 0;
            for (int i = j - 1; i >= 0; i--) {
                mval[i][j] = Math.max(arr[i], mval[i + 1][j]);
                for (int k = i; k < j; k++) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j] + mval[i][k] * mval[k + 1][j]);
                }
            }
        }
        return dp[0][n - 1];
    }

    //1139. 最大的以 1 为边界的正方形
    public int largest1BorderedSquare(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] left = new int[m + 1][n + 1];
        int[][] up = new int[m + 1][n + 1];
        int max = 0;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (grid[i - 1][j - 1] == 1) {
                    left[i][j] = left[i][j - 1] + 1;
                    up[i][j] = up[i - 1][j] + 1;
                    int l = Math.min(left[i][j], up[i][j]);
                    while (left[i - l + 1][j] < l || up[i][j - l + 1] < l) {
                        l--;
                    }
                    max = Math.max(max, l);
                }
            }
        }
        return max * max;
    }

    // 面试题 17.23. 最大黑方阵
    public int[] findSquare(int[][] matrix) {
        int n = matrix.length;
        // 以i,j为左上角，向下[1]或向右[0]最多几个连续的0
        int[][][] dp = new int[n][n][2];
        int[] ans = new int[0];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (matrix[i][j] == 0) {
                    dp[i][j][1] = i == n - 1 ? 1 : dp[i + 1][j][1] + 1;
                    dp[i][j][0] = j == n - 1 ? 1 : dp[i][j + 1][0] + 1;
                    int len = Math.min(dp[i][j][0], dp[i][j][1]);
                    while (ans.length == 0 || len >= ans[2]) {
                        // [i,j]     ... [i,j+len-1] 判断[i,j+len-1]点向下[1]连续0的长度
                        // ...
                        // [i+len-1,j]判断[i,j+len-1]点向右[0]连续0的长度
                        if (dp[i + len - 1][j][0] >= len && dp[i][j + len - 1][1] >= len) {
                            ans = new int[]{i, j, len};
                            break;
                        }
                        len--;
                    }
                }
            }
        }
        return ans;
    }

    // 174 地下城游戏
    public int calculateMinimumHP(int[][] dungeon) {
        int m = dungeon.length;
        int n = dungeon[0].length;
        int[][] dp = new int[m][n];
        //从前往后不满足无后效性
        // 最后一格 最小是 1和1-dungeon[m - 1][n - 1] 中的最大值（如果为正，满足-血量即可，但是最小要1，如果为负，需要最小值-最后一格=1）
        dp[m - 1][n - 1] = Math.max(1, 1 - dungeon[m - 1][n - 1]);

        for (int i = m - 2; i >= 0; i--) {
            dp[i][n - 1] = Math.max(1, dp[i + 1][n - 1] - dungeon[i][n - 1]);
        }
        for (int i = n - 2; i >= 0; i--) {
            dp[m - 1][i] = Math.max(1, dp[m - 1][i + 1] - dungeon[m - 1][i]);
        }
        for (int i = m - 2; i >= 0; i--) {
            for (int j = n - 2; j >= 0; j--) {
                dp[i][j] = Math.max(1, Math.min(dp[i + 1][j], dp[i][j + 1]) - dungeon[i][j]);
            }
        }
        return dp[0][0];
    }

    // 486 预测赢家
    public boolean PredictTheWinner(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }
        for (int len = 2; len <= n; len++) {
            for (int l = 0; l + len - 1 < n; l++) {
                int r = l + len - 1;
                int left = nums[l] - dp[l + 1][r];
                int right = nums[r] - dp[l][r - 1];
                dp[l][r] = Math.max(left, right);
            }
        }
        return dp[0][n - 1] >= 0;
    }

    // 877 石子游戏
    public boolean stoneGame(int[] piles) {
        int n = piles.length;
        //从[i,j]取的最大收益 在下标范围 [i, j] 中，当前玩家与另一个玩家的石子数量之差的最大值，
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = piles[i];
        }
        for (int len = 2; len <= n; len++) {
            for (int l = 0; l + len - 1 < n; l++) {
                int r = l + len - 1;
                // 取r,剩余总价值是[l,r-1]
                int right = piles[r] - dp[l][r - 1];
                int left = piles[l] - dp[l + 1][r];
                dp[l][r] = Math.max(right, left);
            }
        }
        return dp[0][n - 1] > 0;
    }

    //1140. 石子游戏 II
    public int stoneGameII(int[] piles) {
        int len = piles.length, sum = 0;
        // dp[i][j]表示剩余[i,len-1]堆时，M=j的情况下，先取的人能获取的最多石子数
        int[][] dp = new int[len][len + 1];
        // 从后往前遍历：剩余的堆数可以一次性全拿，可以推导
        for (int i = len - 1; i >= 0; i--) {
            sum += piles[i];
            // 枚举M
            for (int M = 1; M <= len; M++) {
                // 可选范围大于长度时，一次性全拿
                if (i + 2 * M >= len) {
                    dp[i][M] = sum;
                } else {
                    // 小于总长时，枚举i后面的各位，减去i+x及当前M时拿取的石子，即为当前可取的最大石子
                    for (int x = 1; x <= 2 * M; x++) {
                        dp[i][M] = Math.max(dp[i][M], sum - dp[i + x][Math.max(M, x)]);
                    }
                }
            }
        }
        return dp[0][1];
    }

    // 375 猜数字大小
    public int getMoneyAmount(int n) {
        int[][] memo = new int[n + 2][n + 2];
        return dfs(1, n, memo);
    }

    private int dfs(int left, int right, int[][] memo) {
        if (left >= right) return 0;
        if (memo[left][right] != 0) return memo[left][right];
        int ans = Integer.MAX_VALUE;
        for (int i = left; i <= right; i++) {
            // 当前的i是猜错的话，需要支付i + [i+1,r]和[l,i-1]的最大值
            // 对猜的每一个i，找到最小的
            ans = Math.min(ans, Math.max(dfs(i + 1, right, memo), dfs(left, i - 1, memo)) + i);
        }
        return memo[left][right] = ans;
    }

    public int getMoneyAmountDP(int n) {
        // dp[i][j]表示选择的数在[i,j]之间时能够确保获胜的最小钱数
        // dp[i][j]=min{k=i->j}(max(dp[i][k-1], dp[k+1][j]) + k)
        // 0和n+1是哨兵
        int[][] dp = new int[n + 2][n + 2];
        for (int len = 2; len <= n; len++) {
            for (int i = 1; i <= n - len + 1; i++) {
                int j = i + len - 1;
                int min = Integer.MAX_VALUE;
                for (int k = i; k <= j; k++) {
                    min = Math.min(min, Math.max(dp[i][k - 1], dp[k + 1][j]) + k);
                }
                dp[i][j] = min;
            }
        }
        return dp[1][n];
    }

    // 2435 矩阵中和能被k整除的路径
    public int numberOfPaths(int[][] grid, int k) {
        final int mod = (int) 1e9 + 7;
        int m = grid.length, n = grid[0].length;
        int[][][] f = new int[m + 1][n + 1][k];
        // 到达i,j时取余k=v的路径数目
        f[0][1][0] = 1;
        for (int i = 0; i < m; ++i)
            for (int j = 0; j < n; ++j)
                for (int v = 0; v < k; ++v)
                    f[i + 1][j + 1][(v + grid[i][j]) % k] = (f[i + 1][j][v] + f[i][j + 1][v]) % mod;
        return f[m][n][0];
    }


    //endregion
    //region -------------------------------数位DP--------------------------------------------------
    //233 数字1的个数
    //输入一个整数 n ，求1～n这n个整数的十进制表示中1出现的次数。
// 例如，输入12，1～12这些整数中包含1 的数字有1、10、11和12，1一共出现了5次。
//输入：n = 12
//输出：5
//    我们以n=2021为例，所有小于等于 2021 的数中个位一共会出现多少个 1 呢？
//    我们可以很容易地发现，个位数出现1的频率是每10个数出现一次，对不对？
//    所以，个位数出现多少 1 就取决于，一个有多少个 10，比如 2021 一共用 202 个 10，所以，个位出现 1 的数一共有 202 次（1， 11， 21，2011）+ 1次（2021）。
//    为什么最后一个 1 次要单独拿出来计算呢？
//    因为这个 1 次是比较特殊的，如果把 n 换成 2020 ，这样最后的 1 次是没有的，你要仔细考虑一下。
//    只有 n 的个位数大于等于 1 的时候，才需要计算最后的这个 1 次。
//    同理，我们考虑十位数一个有多少个 1。
//    很简单，每 100 个数会出现 10 个十位数为 1 的数字，同样地，如果 n 的后面两位小于 10，则不用额外加次数，如果后两位大于等于 10，则需要额外加次数。
//    比如，n=2021时，最后要加 10 次，n=2009时，最后不要加 10次，而n=2015时，最后要加 15-10+1=6次，这一块，你仔细体会一下。
//    同样地道理，可以推断出千位数出现多少个 1，就很简单了，用公式统一表示为（n 表示题目指定的参数，i 为统计哪位上的1）：
//    count = (n / (i * 10) * i) + ?，?处的数量就要看 i 及其右边的位数，即n % (i * 10)（记为 x），是小于 i 、大于等于 i 了，具体大多少了：
//    x < i，? = 0
//    i <= x < 2 * i, ? = x - i + 1
//    x >= 2 * i，? = i
//    写成一行：? = min(max(x - i + 1, 0), i)，请仔细体会。
//    完整公式为：count = (n / (i * 10) * i) + min(max(n % (i * 10) - i + 1, 0), i)。
//    有了公式，我们很快就能计算出来 n = 2021时，百位数一共会出现 2 * 100 + min(max(21-100+1, 0), 100)=200 个1了，它们分别是100,101,..,199,1100,1101,1199。
//    链接：https://leetcode-cn.com/problems/number-of-digit-one/solution/tong-ge-lai-shua-ti-la-yi-ti-san-jie-bao-ivxe/
    public int countDigitOne(int n) {
        int ans = 0;
        for (int i = 1; i <= n; i *= 10) {
            ans += (n / (i * 10)) * i + Math.min(Math.max(n % (i * 10) - i + 1, 0), i);
        }
        return ans;
    }

    //数位DP https://leetcode-cn.com/problems/number-of-digit-one/solution/gong-shui-san-xie-jiang-shu-wei-dp-wen-t-c9oi/
//    回到本题，我们需要计算 [1, n][1,n] 范围内所有数中 1 出现的次数。
//    我们可以统计 1 在每一位出现的次数，将其累加起来即是答案。
//    举个 🌰，对于一个长度为 mm 的数字 nn，我们可以计算其在「个位（从右起第 1 位）」、「十位（第 2 位）」、「百位（第 3 位）」和「第 m 位」中 1 出现的次数。
//    假设有 n = abcden=abcde，即 m = 5，假设我们需要统计第 3 位中 1 出现的次数，即可统计满足 --1-- 形式，同时满足 1 <= --1-- <= abcde  1<=−−1−−<=abcde 要求的数有多少个，我们称 1 <= --1-- <= abcde关系为「大小要求」。
//    我们只需对 cc 前后出现的值进行分情况讨论：
//    当 c 前面的部分 < ab，即范围为 [0, ab)，此时必然满足「大小要求」，因此后面的部分可以任意取，即范围为 [0, 99]。根据「乘法原理」，可得知此时数量为 ab * 100；
//    当 c 前面的部分 = ab，这时候「大小关系」主要取决于 c：
//    当 c = 0，必然不满足「大小要求」，数量为 0；
//    当 c = 1，此时「大小关系」取决于后部分，后面的取值范围为 [0, de]，数量为 1 * (de + 1)；
//    当 c > 1，必然满足「大小关系」，后面的部分可以任意取，即范围为 [0, 99]，数量为 1 * 100；
//    当 c 前面的部分 > ab，必然不满足「大小要求」，数量为 0。
//    其他数位的分析同理。
    public int countDigitOneDP(int n) {
        String s = String.valueOf(n);
        int m = s.length();
        if (m == 1) return n > 0 ? 1 : 0;
        // 计算第 i 位前缀代表的数值，和后缀代表的数值
        // 例如 abcde 则有 ps[2] = ab; ss[2] = de
        int[] ps = new int[m], ss = new int[m];
        ss[0] = Integer.parseInt(s.substring(1));
        for (int i = 1; i < m - 1; i++) {
            ps[i] = Integer.parseInt(s.substring(0, i));
            ss[i] = Integer.parseInt(s.substring(i + 1));
        }
        ps[m - 1] = Integer.parseInt(s.substring(0, m - 1));
        int ans = 0;//总结果
        for (int i = 0; i < m; i++) {
            int total = 0;//每一位的结果（十位百位千位）
            int prefix = ps[i], suffix = ss[i];
            int len = m - (i + 1);//后缀的长度，len 为当前位后面长度为多少
            total += prefix * Math.pow(10, len);
            // x 为当前位数值
            int x = s.charAt(i) - '0';
            if (x == 0) {
                total += 0;
            } else if (x == 1) {
                total += (suffix + 1);
            } else {
                total += Math.pow(10, len);
            }
            ans += total;
        }
        return ans;
    }
    //--------------------数位DP模板------------------------------------------------

    public int countDigitOneDP2(int n) {
        char[] s = String.valueOf(n).toCharArray();
        int m = s.length;
        int[][] memo = new int[m][m];
        for (int[] array : memo) Arrays.fill(array, -1);
        return f233(0, 0, true, s, memo);
    }

    private int f233(int i, int cnt1, boolean isLimit, char[] s, int[][] memo) {
        if (i == s.length) return cnt1;
        if (!isLimit && memo[i][cnt1] > 0) return memo[i][cnt1];
        int ans = 0;
        for (int d = 0, up = isLimit ? s[i] - '0' : 9; d <= up; d++) {
            ans += f233(i + 1, cnt1 + (d == 1 ? 1 : 0), isLimit && d == up, s, memo);
        }
        if (!isLimit) memo[i][cnt1] = ans;
        return ans;
    }

    //面试题 17.06 2出现的次数
    //将 n 转换成字符串 s，定义 f(i,cnt2,isLimit,isNum)  表示构造从左往右第 i 位及其之后数位中的 2 的个数，
    // 其余参数的含义为：
    //
    //cnt2  表示前面填了多少个 2。
    //isLimit  表示当前是否受到了 n 的约束。若为真，则第 i 位填入的数字至多为 s[i]，否则可以是 9。如果在受到约束的情况下填了 s[i]，那么后续填入的数字仍会受到 n 的约束。
    //isNum  表示 i 前面的数位是否填了数字。若为假，则当前位可以跳过（不填数字），或者要填入的数字至少为 1；若为真，则要填入的数字可以从 0 开始。
    public int numberOf2sInRange(int n) {
        char[] s = Integer.toString(n).toCharArray();
        int m = s.length;
        // 从左到右第i位前已经填充cnt个2的个数时的个数
        // 从左往右一共m位，2的个数范围从0到m
        // eg:  0   1    进入个位的时候，前面填充了多少2，0和1的处理时等价的，所以用memo做记忆化
        //      0  0-9  memo[1][0]=0
        //      1  0-9
        //      2  0-9  memo[1][1] = 11
        //      3  0-3
        int[][] memo = new int[m][m];
        for (int i = 0; i < m; i++) Arrays.fill(memo[i], -1);
        return f(0, 0, true, s, memo);
    }

    int f(int i, int cnt2, boolean isLimit, char[] s, int[][] memo) {
        if (i == s.length) return cnt2;
        if (!isLimit && memo[i][cnt2] >= 0) return memo[i][cnt2];
        int res = 0;
        // 当前第i位，当不等于up时，后续的位数可以取到9(i是Limit=false)
        // cnt2时i前面2的个数，eg 22 i指向个位的2时，cnt2=1,i指向m时cnt2加上了个位的2(即cnt2=2)
        for (int d = 0, up = isLimit ? s[i] - '0' : 9; d <= up; ++d) // 枚举要填入的数字 d
            res += f(i + 1, cnt2 + (d == 2 ? 1 : 0), isLimit && d == up, s, memo);
        if (!isLimit) memo[i][cnt2] = res;
        return res;
    }

    //357. 统计各位数字都不同的数字个数
    public int countNumbersWithUniqueDigits(int n) {
        int upper = (int) Math.pow(10, n) - 1;
        String s = String.valueOf(upper);
        char[] chars = s.toCharArray();
        return dfs357(chars, 0, 0, true, false) + 1;
    }

    private int dfs357(char[] chars, int idx, int mask, boolean isLimit, boolean isNum) {
        if (idx == chars.length) {
            return isNum ? 1 : 0;
        }
        int res = 0;
        if (!isNum) {
            res = dfs357(chars, idx + 1, mask, false, false);
        }
        int low = isNum ? 0 : 1;
        int up = isLimit ? chars[idx] - '0' : 9;
        for (int d = low; d <= up; d++) {
            if ((mask >> d & 1) == 0) {
                res += dfs357(chars, idx + 1, mask | (1 << d), isLimit && d == up, true);
            }
        }
        return res;
    }

    public int countNumbersWithUniqueDigits2(int n) {
        if (n == 0) {
            return 1;
        }
        if (n == 1) {
            return 10;
        }
        int res = 10, cur = 9;
        for (int i = 0; i < n - 1; i++) {
            cur *= 9 - i;
            res += cur;
        }
        return res;
    }
    //600. 不含连续1的非负整数
    public int findIntegers(int n) {
        // 二进制位数从高到低
        char[] s = Integer.toBinaryString(n).toCharArray();
        int m = s.length;
        int[][] memo = new int[m][2];
        for (int[] array : memo) Arrays.fill(array, -1);
        return f600(0, false, true, s, memo);
    }

    /**
     * @param i       当前第i位
     * @param prev1   前一位是否是1
     * @param isLimit 当前位是否有上限
     * @param s
     * @param memo
     * @return
     */
    private int f600(int i, boolean prev1, boolean isLimit, char[] s, int[][] memo) {
        if (i == s.length) return 1;
        if (!isLimit && memo[i][prev1 ? 1 : 0] > 0) return memo[i][prev1 ? 1 : 0];
        // 当前位的上限
        int up = isLimit ? s[i] - '0' : 1;
        // 当前位最多两个选择 [0,up]
        // 当前位填充0
        int res = f600(i + 1, false, isLimit && up == 0, s, memo);
        // 前一个不是1，且上限是1时，可以填充1
        if (!prev1 && up == 1) res += f600(i + 1, true, isLimit, s, memo);
        if (!isLimit) memo[i][prev1 ? 1 : 0] = res;
        return res;
    }

    // 902 最大为N的数字组合
    //给定一个按 非递减顺序 排列的数字数组 digits 。你可以用任意次数 digits[i] 来写的数字。例如，如果 digits = ['1','3','
    //5']，我们可以写数字，如 '13', '551', 和 '1351315'。
    // 返回 可以生成的小于或等于给定整数 n 的正整数的个数 。
    //输入：digits = ["1","3","5","7"], n = 100 输出：20 解释：可写出的 20 个数字是：
    //1, 3, 5, 7, 11, 13, 15, 17, 31, 33, 35, 37, 51, 53, 55, 57, 71, 73, 75, 77.
    public int atMostNGivenDigitSet(String[] digits, int n) {
        char[] s = Integer.toString(n).toCharArray();
        int m = s.length;
        //对于一个固定的 i，它受到 isLimit 或 isNum 的约束在整个递归过程中至多会出现一次 eg:某一位前面不填数字；n=234，当前面填23时，i=2就受限最大为4
        int[] memo = new int[m];
        Arrays.fill(memo, -1);
        return f902(0, true, false, s, memo, digits);
    }

    private int f902(int i, boolean isLimit, boolean isNum, char[] s, int[] memo, String[] digits) {
        if (i == s.length) return isNum ? 1 : 0; // 前面填了数字，才为1种合法结果，不然就是"" 所有位都没填
        // 当i收到限制时，所求结果不能直接返回
        if (!isLimit && isNum && memo[i] > 0) return memo[i];
        int ans = 0;
        // 前面不填数字，那么可以跳过当前数位，也不填数字
        // isLimit 改为 false，因为没有填数字，位数都比 n 要短，自然不会受到 n 的约束
        // isNum 仍然为 false，因为没有填任何数字
        if (!isNum) ans = f902(i + 1, false, false, s, memo, digits);
        char up = isLimit ? s[i] : '9';
        // 注意：对于一般的题目而言，如果此时 isNum 为 false，则必须从 1 开始枚举，由于本题 digits 没有 0，所以无需处理这种情况
        for (String d : digits) {
            char c = d.charAt(0);
            if (c > up) break;// d 超过上限，由于 digits 是有序的，后面的 d 都会超过上限，故退出循环
            // isLimit：如果当前受到 n 的约束，且填的数字等于上限，那么后面仍然会受到 n 的约束
            // isNum 为 true，因为填了数字
            ans += f902(i + 1, isLimit && c == up, true, s, memo, digits);
        }
        if (!isLimit && isNum) memo[i] = ans;
        return ans;
    }


    //    我们称满足 X <= N 且仅包含 D 中出现的数字的 X 为合法的。我们的目标是找出所有合法的 X 的个数。
//    设 N 是一个 K 位数，那么对于任意一个小于 K（假设有 k 位，即 k < K）的数，如果它仅包含 D 中出现的数字，那么它就是合法的，并且 k 位数中，合法的数一共有 |D|^k∣个。
//    考虑完位数小于 K 的数，我们接下来考虑位数等于 K 的数，我们用 N = 2345 作为例子来考虑所有合法的 K = 4 位数。
//    如果第 1 个数位比 N 中对应的第 1 个数位（即 2）小，那么剩下的 3 个数位我们可以使用 D 中的任何一个数字，因此有 |D|^{k-1}个合法的数。
//    如果第 1 个数位和 N 中对应的第 1 个数位（即 2）相等，那么从第 2 个数位开始，它既可以比 N 中对应的第 2 个数位（即 3）小，也可以相等。此时相当于我们在考虑一个 K - 1 位数的问题。
    public int atMostNGivenDigitSet2(String[] D, int N) {
        String S = String.valueOf(N);
        int K = S.length();
        int[] dp = new int[K + 1];
        dp[K] = 1;

        for (int i = K - 1; i >= 0; --i) {
            // compute dp[i]
            int Si = S.charAt(i) - '0';
            for (String d : D) {
                if (Integer.parseInt(d) < Si)
                    dp[i] += Math.pow(D.length, K - i - 1);
                else if (Integer.parseInt(d) == Si)
                    dp[i] += dp[i + 1];
            }
        }

        for (int i = 1; i < K; ++i)
            dp[0] += Math.pow(D.length, i);
        return dp[0];
    }

    int[] nums;

    public int atMostNGivenDigitSetDP2(String[] digits, int max) {
        int n = digits.length;
        nums = new int[n];
        for (int i = 0; i < n; i++) nums[i] = Integer.parseInt(digits[i]);
        return dp(max);
    }

    int dp(int x) {
        List<Integer> list = new ArrayList<>();
        while (x != 0) {
            list.add(x % 10);
            x /= 10;
        }
        int n = list.size(), m = nums.length, ans = 0;
        // 位数和 x 相同
        for (int i = n - 1, p = 1; i >= 0; i--, p++) {
            int cur = list.get(i);
            int l = 0, r = m - 1;
            while (l < r) {
                int mid = l + r + 1 >> 1;
                if (nums[mid] <= cur) l = mid;
                else r = mid - 1;
            }
            if (nums[r] > cur) {
                break;
            } else if (nums[r] == cur) {
                ans += r * (int) Math.pow(m, (n - p));
                if (i == 0) ans++;
            } else if (nums[r] < cur) {
                ans += (r + 1) * (int) Math.pow(m, (n - p));
                break;
            }
        }
        // 位数比 x 少的
        for (int i = 1, last = 1; i < n; i++) {
            int cur = last * m;
            ans += cur;
            last = cur;
        }
        return ans;
    }


    // 788. 旋转数字
    static int[] diff = {0, 0, 1, -1, -1, 1, 1, -1, 0, 1};

    public int rotatedDigits(int n) {
        char[] s = Integer.toString(n).toCharArray();
        int m = s.length;
        int[][] memo = new int[m][2];
        for (int[] array : memo) Arrays.fill(array, -1);
        return f788(0, 0, true, s, memo);
    }

    private int f788(int i, int hasDiff, boolean isLimit, char[] s, int[][] memo) {
        if (i == s.length) return hasDiff; // 有2/3/5/9 才算好数
        if (!isLimit && memo[i][hasDiff] >= 0) return memo[i][hasDiff];
        int ans = 0;
        for (int d = 0, up = isLimit ? s[i] - '0' : 9; d <= up; d++) {
            if (diff[d] == -1) continue;
            // 用|关系把hasDiff带入下一位
            ans += f788(i + 1, hasDiff | diff[d], isLimit && d == up, s, memo);
        }
        if (!isLimit) memo[i][hasDiff] = ans;
        return ans;
    }

    public int rotatedDigits2(int n) {
        int ans = 0;
        for (int i = 1; i <= n; ++i) {
            String num = String.valueOf(i);
            boolean valid = true, isDiff = false;
            for (int j = 0; j < num.length(); ++j) {
                char ch = num.charAt(j);
                if (diff[ch - '0'] == -1) {
                    valid = false;
                } else if (diff[ch - '0'] == 1) {
                    isDiff = true;
                }
            }
            if (valid && isDiff) {
                ++ans;
            }
        }
        return ans;
    }

    //1012. 至少有 1 位重复的数字  n-600的结果
    public int numDupDigitsAtMostN(int n) {
        char[] s = Integer.toString(n).toCharArray();
        int m = s.length;
        int[][] memo = new int[m][1 << 10];
        for (int[] array : memo) Arrays.fill(array, -1);
        return n - f1012(0, 0, true, false, s, memo);
    }

    private int f1012(int i, int mask, boolean isLimit, boolean isNum, char[] s, int[][] memo) {
        if (i == s.length) return isNum ? 1 : 0;
        if (!isLimit && isNum && memo[i][mask] >= 0) return memo[i][mask];
        int ans = 0;
        if (!isNum) ans = f1012(i + 1, mask, false, false, s, memo);
        for (int d = isNum ? 0 : 1, up = isLimit ? s[i] - '0' : 9; d <= up; d++) {
            if ((mask >> d & 1) == 0) {
                ans += f1012(i + 1, mask | (1 << d), isLimit && d == up, true, s, memo);
            }
        }
        if (!isLimit && isNum) memo[i][mask] = ans;
        return ans;
    }

    //2376. 统计特殊整数
    public int countSpecialNumbers(int n) {
        char[] s = Integer.toString(n).toCharArray();
        int m = s.length;
        // mask 0-9 10位数
        int[][] memo = new int[m][1 << 10];
        for (int[] array : memo) Arrays.fill(array, -1);
        return f2376(0, 0, true, false, s, memo);
    }

    private int f2376(int i, int mask, boolean isLimit, boolean isNum, char[] s, int[][] memo) {
        if (i == s.length) return isNum ? 1 : 0;
        if (!isLimit && isNum && memo[i][mask] >= 0) return memo[i][mask];
        int ans = 0;
        if (!isNum) ans = f2376(i + 1, mask, false, false, s, memo);
        for (int d = isNum ? 0 : 1, up = isLimit ? s[i] - '0' : 9; d <= up; d++) {
            if ((mask >> d & 1) == 0) { // 当前要填入的d不在之前的数中
                ans += f2376(i + 1, mask | (1 << d), isLimit && d == up, true, s, memo);
            }
        }
        if (!isLimit && isNum) memo[i][mask] = ans;
        return ans;
    }

    //2719. 统计整数数目
    private int minSum, maxSum;

    public int count(String num1, String num2, int minSum, int maxSum) {
        this.minSum = minSum;
        this.maxSum = maxSum;
        int ans = count(num2) - count(num1) + MOD; // 避免负数
        int sum = 0;
        for (char c : num1.toCharArray()) sum += c - '0';
        if (minSum <= sum && sum <= maxSum) ans++; // x=num1 是合法的，补回来
        return ans % MOD;
    }

    private int count(String S) {
        char[] s = S.toCharArray();
        int n = s.length;
        int[][] memo = new int[n][Math.min(9 * n, maxSum) + 1];
        for (int i = 0; i < n; i++)
            Arrays.fill(memo[i], -1); // -1 表示没有计算过
        return f(s, memo, 0, 0, true);
    }

    private int f(char[] s, int[][] memo, int i, int sum, boolean isLimit) {
        if (sum > maxSum) return 0; // 非法数字
        if (i == s.length) return sum >= minSum ? 1 : 0;
        if (!isLimit && memo[i][sum] != -1) return memo[i][sum];
        int res = 0;
        int up = isLimit ? s[i] - '0' : 9;
        for (int d = 0; d <= up; ++d) // 枚举要填入的数字 d
            res = (res + f(s, memo, i + 1, sum + d, isLimit && d == up)) % MOD;
        if (!isLimit) memo[i][sum] = res;
        return res;
    }

    //1397. 找到所有好字符串
    // KMP+数位DP   KMP是算法的皇冠明珠，此题是KMP题目的巅峰
    char[] down;
    char[] up;
    char[] evil_chars;
    int[] next;
    int[][] memo1397;

    public int findGoodStrings(int n, String s1, String s2, String evil) {
        this.n = n;
        down = s1.toCharArray();
        up = s2.toCharArray();
        evil_chars = evil.toCharArray();
        next = getNext(evil);
        memo1397 = new int[n][evil.length()];
        for (int[] array : memo1397) Arrays.fill(array, -1);
        return f1397(0, 0, true, true);
    }

    private int f1397(int i, int matchEvil, boolean downLimit, boolean upLimit) {
        int mod = (int) 1e9 + 7;
        if (i == n) return 1;
        if (!downLimit && !upLimit && memo1397[i][matchEvil] >= 0) return memo1397[i][matchEvil];
        int ans = 0;
        char min = downLimit ? down[i] : 'a';
        char max = upLimit ? up[i] : 'z';
        for (char c = min; c <= max; c++) {
            int matchEvilLen = getLen(c, matchEvil);
            if (matchEvilLen == evil_chars.length) continue;
            ans = (ans + f1397(i + 1, matchEvilLen, downLimit && c == min, upLimit && c == max) % mod) % mod;
        }
        if (!downLimit && !upLimit) memo1397[i][matchEvil] = ans;
        return ans;
    }

    private int getLen(char c, int matchEvil) {
        if (c == evil_chars[matchEvil]) {
            return matchEvil + 1;
        }
        while (next[matchEvil] > 0 && evil_chars[next[matchEvil]] != c) {
            matchEvil = next[matchEvil];
        }
        if (evil_chars[next[matchEvil]] == c) {
            matchEvil = next[matchEvil] + 1;
        } else {
            matchEvil = 0;
        }
        return matchEvil;
    }


    public int[] getNext(String ps) {
        char[] p = ps.toCharArray();
        int[] next = new int[p.length];
        next[0] = -1;
        int j = 0;
        int k = -1;
        while (j < p.length - 1) {
            if (k == -1 || p[j] == p[k]) {
                next[++j] = ++k;
            } else {
                k = next[k];
            }
        }
        next[0] = 0;
        return next;
    }

    //endregion---------------------------------------------------------------------------------
    //region-------------------------树状DP---------------------------------------------------
    // 2477. 到达首都的最少油耗
    long ans = 0;

    public long minimumFuelCost(int[][] roads, int seats) {
        int n = roads.length + 1;
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] edge : roads) {
            List<Integer> list1 = map.getOrDefault(edge[0], new ArrayList<>());
            list1.add(edge[1]);
            map.put(edge[0], list1);
            List<Integer> list2 = map.getOrDefault(edge[1], new ArrayList<>());
            list2.add(edge[0]);
            map.put(edge[1], list2);
        }
        dfs(0, -1, map, seats);
        return ans;
    }

    // 以cur为根，往叶子节点遍历，到达cur时一共多少人
    private int dfs(int cur, int parent, Map<Integer, List<Integer>> map, int seats) {
        int curPeople = 1;
        for (int near : map.getOrDefault(cur, new ArrayList<>())) {
            if (near == parent) continue;
            //到达near时一共多少人，需要的车数量即为near->cur 消耗燃料的数量
            int nearPeople = dfs(near, cur, map, seats);
            ans += (nearPeople + seats - 1) / seats;
            curPeople += nearPeople;
        }
        return curPeople;
    }

    //834. 树中距离之和
    int[] ans834;
    int[] sz;
    int[] dp;
    List<List<Integer>> graph834;

    public int[] sumOfDistancesInTree(int n, int[][] edges) {
        ans834 = new int[n];
        sz = new int[n];
        dp = new int[n];
        graph834 = new ArrayList<List<Integer>>();
        for (int i = 0; i < n; ++i) {
            graph834.add(new ArrayList<Integer>());
        }
        for (int[] edge: edges) {
            int u = edge[0], v = edge[1];
            graph834.get(u).add(v);
            graph834.get(v).add(u);
        }
        dfs834(0, -1);
        dfs2_834(0, -1);
        return ans834;
    }

    public void dfs834(int u, int f) {
        sz[u] = 1;
        dp[u] = 0;
        for (int v: graph834.get(u)) {
            if (v == f) {
                continue;
            }
            dfs(v, u);
            dp[u] += dp[v] + sz[v];
            sz[u] += sz[v];
        }
    }

    public void dfs2_834(int u, int f) {
        ans834[u] = dp[u];
        for (int v: graph834.get(u)) {
            if (v == f) {
                continue;
            }
            int pu = dp[u], pv = dp[v];
            int su = sz[u], sv = sz[v];

            dp[u] -= dp[v] + sz[v];
            sz[u] -= sz[v];
            dp[v] += dp[u] + sz[u];
            sz[v] += sz[u];

            dfs2_834(v, u);

            dp[u] = pu;
            dp[v] = pv;
            sz[u] = su;
            sz[v] = sv;
        }
    }

    //1245. 树的直径
    //2246. 相邻字符不同的最长路径
    List<Integer>[] g2246;
    String s;
    int ans2246;

    public int longestPath(int[] parent, String s) {
        this.s = s;
        int n = parent.length;
        g2246 = new ArrayList[n];
        Arrays.setAll(g2246, e -> new ArrayList<>());
        for (int i = 1; i < n; i++) g2246[parent[i]].add(i);

        dfs2246(0);
        return ans2246 + 1;
    }

    int dfs2246(int x) {
        int maxLen = 0;
        for (int y : g2246[x]) {
            int len = dfs2246(y) + 1;
            if (s.charAt(y) != s.charAt(x)) {
                ans2246 = Math.max(ans2246, maxLen + len);
                maxLen = Math.max(maxLen, len);
            }
        }
        return maxLen;
    }

    //1617. 统计子树中城市之间最大距离
    int mask;
    int diameter;

    public int[] countSubgraphsForEachDiameterDFS(int n, int[][] edges) {
        List<Integer>[] adj = new List[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int[] edge : edges) {
            int x = edge[0] - 1;
            int y = edge[1] - 1;
            adj[x].add(y);
            adj[y].add(x);
        }

        int[] ans = new int[n - 1];
        for (int i = 1; i < (1 << n); i++) {
            int x = 32 - Integer.numberOfLeadingZeros(i) - 1;
            int mask = i;
            int y = -1;
            Queue<Integer> queue = new ArrayDeque<Integer>();
            queue.offer(x);
            mask &= ~(1 << x);
            while (!queue.isEmpty()) {
                y = queue.poll();
                for (int vertex : adj[y]) {
                    if ((mask & (1 << vertex)) != 0) {
                        mask &= ~(1 << vertex);
                        queue.offer(vertex);
                    }
                }
            }
            if (mask == 0) {
                int diameter = dfs(adj, -1, y, i);
                if (diameter > 0) {
                    ans[diameter - 1]++;
                }
            }
        }
        return ans;
    }

    public int dfs(List<Integer>[] adj, int parent, int u, int mask) {
        int depth = 0;
        for (int v : adj[u]) {
            if (v != parent && (mask & (1 << v)) != 0) {
                depth = Math.max(depth, 1 + dfs(adj, u, v, mask));
            }
        }
        return depth;
    }

    //枚举任意两点直径
    public int[] countSubgraphsForEachDiameter(int n, int[][] edges) {
        List<Integer>[] adj = new List[n];
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            dist[i][i] = 0;
        }
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int[] edge : edges) {
            int x = edge[0] - 1;
            int y = edge[1] - 1;
            adj[x].add(y);
            adj[y].add(x);
            dist[x][y] = dist[y][x] = 1;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (dist[j][i] != Integer.MAX_VALUE && dist[i][k] != Integer.MAX_VALUE) {
                        dist[j][k] = Math.min(dist[j][k], dist[j][i] + dist[i][k]);
                    }
                }
            }
        }
        int[] ans = new int[n - 1];
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                ans[dist[i][j] - 1] += dfs(adj, dist, i, -1, i, j);
            }
        }
        return ans;
    }

    public int dfs(List<Integer>[] adj, int[][] dist, int u, int parent, int x, int y) {
        if (dist[u][x] > dist[x][y] || dist[u][y] > dist[x][y]) {
            return 1;
        }
        if ((dist[u][y] == dist[x][y] && u < x) || (dist[u][x] == dist[x][y] && u < y)) {
            return 1;
        }
        int ret = 1;
        for (int v : adj[u]) {
            if (v != parent) {
                ret *= dfs(adj, dist, v, u, x, y);
            }
        }
        if (dist[u][x] + dist[u][y] > dist[x][y]) {
            ret++;
        }
        return ret;
    }
    //2581. 统计可能的树根数目
    int cnt = 0, res2581 = 0;
    int k;
    List<Integer>[] g2581;
    Set<Long> set;

    public int rootCount(int[][] edges, int[][] guesses, int k) {
        this.k = k;
        int n = edges.length + 1;
        g2581 = new List[n];
        for (int i = 0; i < n; i++) {
            g2581[i] = new ArrayList<Integer>();
        }
        set = new HashSet<Long>();
        for (int[] v : edges) {
            g2581[v[0]].add(v[1]);
            g2581[v[1]].add(v[0]);
        }
        for (int[] v : guesses) {
            set.add(h(v[0], v[1]));
        }

        dfs(0, -1);
        redfs(0, -1, cnt);
        return res2581;
    }

    public long h(int x, int y) {
        return (long) x << 20 | y;
    }

    public void dfs2581(int x, int fat) {
        for (int y : g2581[x]) {
            if (y == fat) {
                continue;
            }
            cnt += set.contains(h(x, y)) ? 1 : 0;
            dfs2581(y, x);
        }
    }

    public void redfs(int x, int fat, int cnt) {
        if (cnt >= k) {
            res2581++;
        }
        for (int y : g2581[x]) {
            if (y == fat) {
                continue;
            }
            redfs(y, x, cnt - (set.contains(h(x, y)) ? 1 : 0) + (set.contains(h(y, x)) ? 1 : 0));
        }
    }

    //2538. 最大价值和与最小价值和的差值
    private List<Integer>[] g2538;
    private int[] price;
    private long ans1;

    public long maxOutput(int n, int[][] edges, int[] price) {
        this.price = price;
        g2538 = new ArrayList[n];
        Arrays.setAll(g2538, e -> new ArrayList<>());
        for (int[] e : edges) {
            int x = e[0], y = e[1];
            g2538[x].add(y);
            g2538[y].add(x); // 建树
        }
        dfs(0, -1);
        return ans1;
    }

    // 返回带叶子的最大路径和，不带叶子的最大路径和
    private long[] dfs(int x, int fa) {
        long p = price[x], max_s1 = p, max_s2 = 0;
        for (int y : g2538[x])
            if (y != fa) {
                long[] res = dfs(y, x);
                long s1 = res[0], s2 = res[1];
                // 前面最大带叶子的路径和 + 当前不带叶子的路径和
                // 前面最大不带叶子的路径和 + 当前带叶子的路径和
                ans1 = Math.max(ans1, Math.max(max_s1 + s2, max_s2 + s1));
                max_s1 = Math.max(max_s1, s1 + p);
                max_s2 = Math.max(max_s2, s2 + p);
            }
        return new long[]{max_s1, max_s2};
    }
    //endregion

}

