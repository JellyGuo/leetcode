import java.util.*;

public class SolutionBacktrack {
    //region------------------------------------------------------------------回溯-------------------------------------------------
    // 22 括号生成
    //输入：n = 3
//输出：["((()))","(()())","(())()","()(())","()()()"]
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        dfs(0, 0, n, "", res);
        return res;
    }

    public void dfs(int left, int right, int n, String path, List<String> res) {
        if (left < right) {
            return;
        }
        if (left == n && right == n) {
            res.add(path);
            return;
        }
        if (left < n) {
            dfs(left + 1, right, n, path + '(', res);
        }
        if (right < n) {
            dfs(left, right + 1, n, path + ')', res);
        }
    }

    //301 删除无效的括号
    public List<String> removeInvalidParentheses(String s) {
        List<String> result = new ArrayList<>();
        int lremove = 0;
        int rremove = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                lremove++;
            } else if (s.charAt(i) == ')') {
                if (lremove == 0) {
                    rremove++;
                } else {
                    lremove--;
                }
            }
        }
        helper(s, 0, lremove, rremove, result);

        return result;
    }

    private void helper(String str, int index, int lremove, int rremove, List<String> result) {
        if (lremove == 0 && rremove == 0) {
            if (isValid(str)) {
                result.add(str);
            }
            return;
        }

        for (int i = index; i < str.length(); i++) {
            //我们在每次进行搜索时，如果遇到连续相同的括号我们只需要搜索一次即可，比如当前遇到的字符串为"(((())"，去掉前四个左括号中的任意一个，
            // 生成的字符串是一样的，均为 "((())"，因此我们在尝试搜索时，只需去掉一个左括号进行下一轮搜索，不需要将前四个左括号都尝试一遍。
            if (i > index && str.charAt(i) == str.charAt(i - 1)) {
                continue;
            }
            // 如果剩余的字符无法满足去掉的数量要求，直接返回
            if (lremove + rremove > str.length() - i) {
                return;
            }
            // 尝试去掉一个左括号
            if (lremove > 0 && str.charAt(i) == '(') {
                helper(str.substring(0, i) + str.substring(i + 1), i, lremove - 1, rremove, result);
            }
            // 尝试去掉一个右括号
            if (rremove > 0 && str.charAt(i) == ')') {
                helper(str.substring(0, i) + str.substring(i + 1), i, lremove, rremove - 1, result);
            }
        }
    }

    private boolean isValid(String str) {
        int cnt = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                cnt++;
            } else if (str.charAt(i) == ')') {
                cnt--;
                if (cnt < 0) {
                    return false;
                }
            }
        }

        return cnt == 0;
    }

    //1096. 花括号展开 II
    private TreeSet<String> s = new TreeSet<>();

    public List<String> braceExpansionII(String expression) {
        dfs1096(expression);
        return new ArrayList<>(s);
    }

    private void dfs1096(String exp) {
        int j = exp.indexOf('}');
        if (j == -1) {
            s.add(exp);
            return;
        }
        int i = j;
        while (exp.charAt(i) != '{') {
            --i;
        }
        String a = exp.substring(0, i);
        String c = exp.substring(j + 1);
        for (String b : exp.substring(i + 1, j).split(",")) {
            dfs1096(a + b + c);
        }
    }

    // 761 特殊的二进制序列
    public String makeLargestSpecial(String s) {
        if (s.length() <= 2) {
            return s;
        }
        int cnt = 0, left = 0;
        List<String> subs = new ArrayList<>();
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '1') {
                ++cnt;
            } else {
                --cnt;
                if (cnt == 0) {
                    subs.add("1" + makeLargestSpecial(s.substring(left + 1, i)) + "0");
                    left = i + 1;
                }
            }
        }

        subs.sort(Comparator.reverseOrder());
        StringBuilder ans = new StringBuilder();
        for (String sub : subs) {
            ans.append(sub);
        }
        return ans.toString();
    }

    //46 组合
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Deque<Integer> path = new ArrayDeque<>();
        dfs(nums, 0, path, result);
        return result;
    }

    private void dfs(int[] nums, int depth, Deque<Integer> path, List<List<Integer>> result) {
        if (depth == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }

        for (int num : nums) {
            if (path.contains(num)) {
                continue;
            }
            path.add(num);
            dfs(nums, depth + 1, path, result);
            path.removeLast();
        }
    }

    //
    //给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。
//输入：nums = [1,1,2]
//输出：
//[[1,1,2],
// [1,2,1],
// [2,1,1]]
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Deque<Integer> path = new ArrayDeque<>();
        boolean[] visited = new boolean[nums.length];
        Arrays.sort(nums);
        dfs(nums, visited, 0, path, result);
        return result;
    }

    private void dfs(int[] nums, boolean[] visited, int depth, Deque<Integer> path, List<List<Integer>> result) {
        if (depth == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) {
                continue;
            }
            if (i > 0 && nums[i] == nums[i - 1] && !visited[i - 1]) {
                continue;
            }
            path.add(nums[i]);
            visited[i] = true;
            dfs(nums, visited, depth + 1, path, result);
            visited[i] = false;
            path.removeLast();
        }
    }

    // 60 第K个排列
    public String getPermutation(int n, int k) {
        boolean[] used = new boolean[n + 1];
        int[] factorial = new int[n + 1];
        factorial[0] = 1;
        for (int i = 1; i <= n; i++) {
            factorial[i] = factorial[i - 1] * i;
        }
        StringBuilder sb = new StringBuilder();
        dfs(0, sb, n, k, factorial, used);
        return sb.toString();
    }

    private void dfs(int index, StringBuilder sb, int n, int k, int[] factorial, boolean[] used) {
        if (index == n) {
            return;
        }
        // 该层选完还剩下的全排列
        int cnt = factorial[n - 1 - index];
        for (int i = 1; i <= n; i++) {
            if (used[i]) continue;
            if (cnt < k) {
                k -= cnt;
                continue;
            }
            sb.append(i);
            used[i] = true;
            dfs(index + 1, sb, n, k, factorial, used);
            return;
        }
    }

    //77 组合
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if (k <= 0 || n < k) {
            return res;
        }
        Deque<Integer> path = new ArrayDeque<>();
        dfs(n, k, 1, path, res);
        return res;
    }

    public void dfs(int n, int k, int index, Deque<Integer> path, List<List<Integer>> res) {
        if (path.size() == k) {
            res.add(new ArrayList<>(path));
            return;
        }
        //只取index后面k-paht.size()个
        for (int i = index; i <= (n - (k - path.size()) + 1); i++) {
            path.addLast(i);
            dfs(n, k, i + 1, path, res);
            path.removeLast();
        }
    }

    public List<List<Integer>> combine2(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        Deque<Integer> path = new ArrayDeque<>();
        dfs(result, path, n, k, 1);
        return result;
    }

    private void dfs(List<List<Integer>> result, Deque<Integer> path, int n, int k, int index) {
        if (path.size() > k) {
            return;
        }
        if (path.size() == k) {
            result.add(new ArrayList<>(path));
        }
        for (int i = index; i <= n; i++) {
            path.addLast(i);
            dfs(result, path, n, k, i + 1);
            path.removeLast();
        }
    }

    // 39 组合总和 无重复元素 的整数数组
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (candidates == null || candidates.length < 1) {
            return result;
        }
        Deque<Integer> path = new ArrayDeque<>();
        dfs(candidates, target, candidates.length - 1, path, result);
        return result;
    }

    public void dfs(int[] candidates, int diff, int index, Deque<Integer> path, List<List<Integer>> result) {
        if (diff == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = index; i >= 0; i--) {

            int temp = diff - candidates[i];
            if (temp < 0) {
                continue;
            }
            path.add(candidates[i]);
            if (temp < candidates[i]) {
                dfs(candidates, temp, i - 1, path, result);
            } else {
                dfs(candidates, temp, i, path, result);
            }
            path.removeLast();
        }
    }

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Deque<Integer> path = new ArrayDeque<>();
        combinationSum2Dfs(candidates, result, path, 0, target, 0);
        return result;
    }

    private void combinationSum2Dfs(int[] candidates, List<List<Integer>> result, Deque<Integer> deque, int sum, int target, int index) {
        if (sum > target) return;
        if (sum == target) {
            result.add(new ArrayList<>(deque));
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            int tmp = sum + candidates[i];
            int nextIdx = tmp < target ? i : i + 1;
            deque.addLast(candidates[i]);
            combinationSum2Dfs(candidates, result, deque, tmp, target, nextIdx);
            deque.removeLast();
        }
    }

    //40 组合总和2 有重复元素 的整数数组 每个数字在每个组合中只能使用 一次
    public List<List<Integer>> combinationSumDuplicate(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (candidates == null || candidates.length < 1) {
            return result;
        }
        Arrays.sort(candidates);
        Deque<Integer> path = new ArrayDeque<>();
        dfs1(candidates, target, 0, path, result);
        return result;
    }

    public void dfs1(int[] candidates, int diff, int index, Deque<Integer> path, List<List<Integer>> result) {
        if (diff == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            int temp = diff - candidates[i];
            if (temp < 0) {
                break;
            }
            if (i > index && candidates[i] == candidates[i - 1]) {
                continue;
            }
            path.add(candidates[i]);
            dfs(candidates, temp, i + 1, path, result);
            path.removeLast();
        }
    }

    // 216 组合总和3
    public List<List<Integer>> combinationSum3(int k, int n) {
        int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        List<List<Integer>> result = new ArrayList<>();
        Deque<Integer> path = new ArrayDeque<>();
        dfs(nums, result, path, n, k, 0);
        return result;
    }

    private void dfs(int[] nums, List<List<Integer>> result, Deque<Integer> path, int diff, int count, int index) {
        if (diff == 0 && path.size() == count) {
            result.add(new ArrayList<>(path));
            return;
        }
        if (path.size() > count) return;
        if (index >= nums.length) return;
        for (int i = index; i < nums.length; i++) {
            int tmp = diff - nums[i];
            if (tmp < 0) break;
            path.addLast(nums[i]);
            dfs(nums, result, path, tmp, count, i + 1);
            path.removeLast();
        }
    }

    //78 子集
    //给你一个整数数组 nums ，数组中的元素 互不相同 。返回该数组所有可能的子集（幂集）。
// 解集 不能 包含重复的子集。你可以按 任意顺序 返回解集。
//输入：nums = [1,2,3]
//输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        dfs(nums, new ArrayDeque<>(), result, 0);
        return result;
    }

    private void dfs(int[] nums, Deque<Integer> path, List<List<Integer>> result, int depth) {
        result.add(new ArrayList<>(path));
        for (int i = depth; i < nums.length; i++) {
            path.add(nums[i]);
            dfs(nums, path, result, i + 1);
            path.removeLast();
        }
    }
//90 子集2
//给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。
//
// 解集 不能 包含重复的子集。返回的解集中，子集可以按 任意顺序 排列。

    //输入：nums = [1,2,2]
//输出：[[],[1],[1,2],[1,2,2],[2],[2,2]]
    public List<List<Integer>> subsetsWithDup1(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Deque<Integer> path = new ArrayDeque<>();
        Arrays.sort(nums);
        dfs(result, path, nums, 0);
        return result;
    }

    private void dfs(List<List<Integer>> result, Deque<Integer> path, int[] nums, int index) {
        result.add(new ArrayList<>(path));
        for (int i = index; i < nums.length; i++) {
            if (i > index && nums[i] == nums[i - 1]) continue;
            path.addLast(nums[i]);
            dfs(result, path, nums, i + 1);
            path.removeLast();
        }
    }

    public List<List<Integer>> subsetsWithDup2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Deque<Integer> path = new ArrayDeque<>();
        boolean[] used = new boolean[nums.length];
        Arrays.sort(nums);
        dfs(nums, result, path, 0, used);
        return result;
    }

    private void dfs(int[] nums, List<List<Integer>> result, Deque<Integer> path, int index, boolean[] used) {
        result.add(new ArrayList<>(path));
        if (index >= nums.length) {
            return;
        }
        for (int i = index; i < nums.length; i++) {
            // 如果当前元素和前一个元素相同，而且前一个元素没有被访问，说明前一个相同的元素在当前层已经被用过了
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }
            path.add(nums[i]);
            used[i] = true;
            dfs(nums, result, path, i + 1, used);
            //同一层，后面的i判断这一个i是false，但是已经被回溯过
            used[i] = false;
            path.removeLast();
        }
    }

    //2437. 有效时间的数目 枚举
    // 分开枚举小时和分钟，最终结果相乘
    public int countTime(String time) {
        return f(time.substring(0, 2), 24) * f(time.substring(3), 60);
    }

    private int f(String s, int m) {
        int cnt = 0;
        for (int i = 0; i < m; ++i) {
            boolean a = s.charAt(0) == '?' || s.charAt(0) - '0' == i / 10;
            boolean b = s.charAt(1) == '?' || s.charAt(1) - '0' == i % 10;
            cnt += a && b ? 1 : 0;
        }
        return cnt;
    }

    //2397. 被列覆盖的最多行数
    int max2397;
    int m;
    public int maximumRows(int[][] matrix, int numSelect) {
        m = matrix.length;
        n = matrix[0].length;
        boolean[] visited = new boolean[n];
        dfs2397(matrix, numSelect, visited, 0, 0);
        return max2397;
    }

    private void dfs2397(int[][] matrix, int numSelect, boolean[] visited, int idx, int cnt) {
        if (cnt == numSelect) {
            max2397 = Math.max(max2397, count(matrix, visited));
            return;
        }
        for (int i = idx; i < n; i++) {
            visited[i] = true;
            dfs2397(matrix, numSelect, visited, i + 1, cnt + 1);
            visited[i] = false;
        }
    }

    private int count(int[][] matrix, boolean[] visited) {
        int num = 0;
        for (int[] ints : matrix) {
            if (satisfy(ints, visited)) num++;
        }
        return num;
    }

    private boolean satisfy(int[] nums, boolean[] visited) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 1 && !visited[i]) return false;
        }
        return true;
    }

    //282. 给表达式添加运算符
    public List<String> addOperators(String num, int target) {
        int n = num.length();
        List<String> result = new ArrayList<>();
        dfs282(num, target, n, 0, 0, 0, result, "");
        return result;
    }

    private void dfs282(String num, int target, int n, int idx, long sum, long prev, List<String> result, String path) {
        if (idx == n) {
            if (sum == target) {
                result.add(path);
            }
            return;
        }
        for (int i = idx; i < n; i++) {
            if (i != idx && num.charAt(idx) == '0') break;
            long next = Long.parseLong(num.substring(idx, i + 1));
            if (idx == 0) {
                dfs282(num, target, n, i + 1, next, next, result, "" + next);
            } else {
                dfs282(num, target, n, i + 1, sum + next, next, result, path + "+" + next);
                dfs282(num, target, n, i + 1, sum - next, -next, result, path + "-" + next);
                long x = prev * next;
                dfs282(num, target, n, i + 1, sum - prev + x, x, result, path + "*" + next);
            }
        }
    }

    //2597. 美丽子集的数目
    int ans2597;

    public int beautifulSubsets(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        dfs(nums, 0, k, map);
        return ans2597 - 1;//减去空集这一情况
    }

    private void dfs(int[] nums, int idx, int k, Map<Integer, Integer> map) {
        ans2597++;
        if (idx == nums.length) {
            return;
        }
        for (int i = idx; i < nums.length; i++) {
            int prev = nums[i] - k, next = nums[i] + k;
            if (!map.containsKey(prev) && !map.containsKey(next)) {
                map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
                dfs(nums, i + 1, k, map);
                map.put(nums[i], map.get(nums[i]) - 1);
                if (map.get(nums[i]) == 0) map.remove(nums[i]);
            }
        }
    }

    //offer 38 字符串排列
    //输入一个字符串，打印出该字符串中字符的所有排列。
// 输入：s = "abc"
//输出：["abc","acb","bac","bca","cab","cba"]
    public String[] permutation(String s) {
        List<String> result = new ArrayList<>();
        StringBuilder path = new StringBuilder();
        boolean[] visited = new boolean[s.length()];
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        dfs(result, path, chars, visited);
        return result.toArray(new String[0]);
    }

    private void dfs(List<String> result, StringBuilder path, char[] chars, boolean[] visited) {
        if (path.length() == chars.length) {
            result.add(path.toString());
            return;
        }
        for (int i = 0; i < chars.length; i++) {//从0开始
            if (visited[i]) {
                continue;
            }
            //从0开始的写法 if (i > 0 && chars[i] == chars[i - 1] && !visited[i - 1]) eg:aab的排列里，baa这种情况
            //从index开始的写法  if (i > index && nums[i] == nums[i - 1]) continue; 已经排好序，只和后面的有关,后面的与前面的比较
            if (i > 0 && chars[i] == chars[i - 1] && !visited[i - 1]) {
                continue;
            }
            path.append(chars[i]);
            visited[i] = true;
            dfs(result, path, chars, visited);
            visited[i] = false;
            path.deleteCharAt(path.length() - 1);
        }
    }

    // 491 递增子序列
    public List<List<Integer>> findSubsequences(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Deque<Integer> deque = new ArrayDeque<>();
        backtrack(nums, 0, deque, result);
        return result;
    }

    private void backtrack(int[] nums, int idx, Deque<Integer> path, List<List<Integer>> result) {
        if (path.size() > 1) result.add(new ArrayList<>(path));
        // 在该层，相同的数只取一次
        Set<Integer> set = new HashSet<>();
        for (int i = idx; i < nums.length; i++) {
            if (path.isEmpty() || nums[i] >= path.peekLast()) {
                if (set.contains(nums[i])) continue;
                set.add(nums[i]);
                path.offerLast(nums[i]);
                backtrack(nums, i + 1, path, result);
                path.removeLast();
            }
        }
    }

    //offer 17 大数打印
    //输入数字 n，按顺序打印出从 1 到最大的 n 位十进制数。比如输入 3，则打印出 1、2、3 一直到最大的 3 位数 999。
// 输入: n = 1
//输出: [1,2,3,4,5,6,7,8,9]
    int nine = 0, count = 0, start, n;

    public int[] printNumbers(int n) {
        this.n = n;
        int[] result = new int[(int) Math.pow(10, n) - 1];
        char[] num = new char[n];
        char[] loop = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        start = n - 1;
        dfsBigNum(0, result, num, loop, n);
        return result;
    }

    void dfsBigNum(int x, int[] result, char[] num, char[] loop, int n) {
        if (x == n) {
            String s = String.valueOf(num).substring(start);
            if (!s.equals("0")) result[count++] = Integer.parseInt(s);
            if (n - start == nine) start--;
            return;
        }
        for (char i : loop) {
            if (i == '9') nine++;
            num[x] = i;
            dfsBigNum(x + 1, result, num, loop, n);
        }
        nine--;
    }

    //386. 字典序排数
    public List<Integer> lexicalOrderIterator(int n) {
        List<Integer> result = new ArrayList<>();
        int number = 1;
        for (int i = 0; i < n; i++) {
            result.add(number);
            if (number * 10 <= n) {
                number *= 10;
            } else {
                // 1 10 11 ... 13 2
                // 个位数是9 或者 number=n 搜索到头
                while (number % 10 == 9 || number + 1 > n) number /= 10;
                number++;
            }
        }
        return result;
    }

    public List<Integer> lexicalOrderDFS(int n) {
        List<Integer> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        for (int i = 1; i <= n; i++) lexicalOrderDfs(i, n, result, visited);
        return result;
    }

    private void lexicalOrderDfs(int cur, int limit, List<Integer> result, Set<Integer> visited) {
        if (cur > limit) return;
        if (!visited.contains(cur)) {
            result.add(cur);
            visited.add(cur);
        }
        for (int i = 0; i <= 9; i++) lexicalOrderDfs(cur * 10 + i, limit, result, visited);
    }

    // 面试04.09二叉搜索树序列
    //从左向右遍历一个数组，通过不断将其中的元素插入树中可以逐步地生成一棵二叉搜索树。
    // 给定一个由不同节点组成的二叉搜索树 root，输出所有可能生成此树的数组。
    public List<List<Integer>> BSTSequences(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        Deque<Integer> path = new ArrayDeque<>();
        List<TreeNode> level = new ArrayList<>();
        if (root == null) {
            result.add(new ArrayList<>(path));
            return result;
        }
        level.add(root);
        backtrack(result, path, level);
        return result;
    }

    private void backtrack(List<List<Integer>> result, Deque<Integer> path, List<TreeNode> curLevel) {
        if (curLevel.isEmpty()) {
            result.add(new ArrayList<>(path));
            return;
        }
        //    4
        //  2   5  选2时,[1,3,5]作为下一层;选5时,2作为下一层
        // 1 3
        List<TreeNode> nextLevel = new ArrayList<>(curLevel);
        int size = curLevel.size();
        for (int i = 0; i < size; i++) {
            // 选第i个作为当前层，其余全部当前层+i的子节点作为下一层
            TreeNode tmp = nextLevel.get(i);
            nextLevel.remove(i);
            path.addLast(tmp.val);
            if (tmp.left != null) nextLevel.add(tmp.left);
            if (tmp.right != null) nextLevel.add(tmp.right);
            backtrack(result, path, nextLevel);
            nextLevel = new ArrayList<>(curLevel); //恢复成当前层进入下一个循环
            path.removeLast();
        }
    }

    //给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。
// 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
    //输入：digits = "23"
//输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
    public List<String> letterCombinations(String digits) {
        int depth = digits.length();
        List<String> res = new ArrayList<>();
        if (depth <= 0) {
            return res;
        }
        Deque<Character> path = new ArrayDeque<>();
        Map<Character, char[]> map = new HashMap<>();
        map.put('2', new char[]{'a', 'b', 'c'});
        map.put('3', new char[]{'d', 'e', 'f'});
        map.put('4', new char[]{'g', 'h', 'i'});
        map.put('5', new char[]{'j', 'k', 'l'});
        map.put('6', new char[]{'m', 'n', 'o'});
        map.put('7', new char[]{'p', 'q', 'r', 's'});
        map.put('8', new char[]{'t', 'u', 'v'});
        map.put('9', new char[]{'w', 'x', 'y', 'z'});
        dfs(map, digits, 0, depth, path, res);
        return res;
    }

    public void dfs(Map<Character, char[]> map, String digits, int index, int depth, Deque<Character> path, List<String> res) {
        if (path.size() == depth) {
            StringBuilder sb = new StringBuilder();
            for (Character c : path) {
                sb.append(c);
            }
            res.add(sb.toString());
            return;
        }
        if (index >= digits.length()) {
            return;
        }
        char[] chars = map.get(digits.charAt(index));
        for (char c : chars) {
            path.addLast(c);
            dfs(map, digits, index + 1, depth, path, res);
            path.removeLast();
        }
    }

    // 95 不同的二叉搜索树2
    public List<TreeNode> generateTrees(int n) {
        if (n == 0) {
            return new ArrayList<>();
        }
        return generateTrees(1, n);
    }

    public List<TreeNode> generateTrees(int start, int end) {
        List<TreeNode> list = new ArrayList<>();
        if (start > end) {
            list.add(null);
            return list;
        }
        for (int i = start; i <= end; i++) {
            List<TreeNode> leftTrees = generateTrees(start, i - 1);
            List<TreeNode> rightTrees = generateTrees(i + 1, end);

            for (TreeNode left : leftTrees) {
                for (TreeNode right : rightTrees) {
                    TreeNode cur = new TreeNode(i);
                    cur.left = left;
                    cur.right = right;
                    list.add(cur);
                }
            }
        }
        return list;
    }

    // 647 回文子串 回溯写法
    // 还有DP写法
    int num;

    public int countSubstrings(String s) {
        int n = s.length();
        boolean[] visited = new boolean[n];
        dfs(0, s, visited);
        return num;
    }

    private void dfs(int idx, String s, boolean[] visited) {
        if (idx == s.length()) return;
        if (visited[idx]) return;
        visited[idx] = true;
        for (int i = idx; i < s.length(); i++) {
            String subString = s.substring(idx, i + 1);
            if (!isPalindrome(subString)) continue;
            num++;
            dfs(i + 1, s, visited);
        }
    }

    //1147. 段式回文
    public int longestDecomposition(String text) {
        if (text.equals("")) return 0;
        int n = text.length();
        for (int i = 1; i <= n / 2; i++) {
            if (text.substring(0, i).equals(text.substring(n - i))) {
                return 2 + longestDecomposition(text.substring(i, n - i));
            }
        }
        return 1;
    }

    // 131 分割回文串
    // 131 分割回文串1
    // 132 分割回文串2 dp预处理
    public List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        Deque<String> path = new LinkedList<>();
        dfs(result, path, 0, s);
        return result;
    }

    private void dfs(List<List<String>> result, Deque<String> path, int depth, String s) {
        if (depth == s.length()) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = depth; i < s.length(); i++) {
            String tmp = s.substring(depth, i + 1);
            if (!isPalindrome(tmp)) continue;
            path.addLast(tmp);
            dfs(result, path, i + 1, s);
            path.removeLast();
        }
    }

    private boolean isPalindrome(String s) {
        for (int l = 0, r = s.length() - 1; l < r; l++, r--) {
            if (s.charAt(l) != s.charAt(r)) return false;
        }
        return true;
    }

    // 93 复原IP地址
    public List<String> restoreIpAddresses(String s) {
        int len = s.length();
        List<String> res = new ArrayList<>();
        // 如果长度不够，不搜索
        if (len < 4 || len > 12) {
            return res;
        }

        Deque<String> path = new ArrayDeque<>(4);
        int splitTimes = 0;
        dfs(s, len, splitTimes, 0, path, res);
        return res;
    }

    private int judgeIfIpSegment(String s, int left, int right) {
        int len = right - left + 1;

        // 大于 1 位的时候，不能以 0 开头
        if (len > 1 && s.charAt(left) == '0') {
            return -1;
        }

        // 转成 int 类型
        int res = 0;
        for (int i = left; i <= right; i++) {
            res = res * 10 + s.charAt(i) - '0';
        }

        if (res > 255) {
            return -1;
        }
        return res;
    }

    private void dfs(String s, int len, int split, int begin, Deque<String> path, List<String> res) {
        if (begin == len) {
            if (split == 4) {
                res.add(String.join(".", path));
            }
            return;
        }

        // 看到剩下的不够了，就退出（剪枝），len - begin 表示剩余的还未分割的字符串的位数
        if (len - begin < (4 - split) || len - begin > 3 * (4 - split)) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            if (begin + i >= len) {
                break;
            }

            int ipSegment = judgeIfIpSegment(s, begin, begin + i);
            if (ipSegment != -1) {
                // 在判断是 ip 段的情况下，才去做截取
                path.addLast(ipSegment + "");
                dfs(s, len, split + 1, begin + i + 1, path, res);
                path.removeLast();
            }
        }
    }

    // 816 模糊坐标 toreview
    public List<String> ambiguousCoordinates(String s) {
        int n = s.length() - 2;
        List<String> res = new ArrayList<>();
        s = s.substring(1, s.length() - 1);
        for (int l = 1; l < n; ++l) {
            List<String> lt = getPos(s.substring(0, l));
            if (lt.isEmpty()) {
                continue;
            }
            List<String> rt = getPos(s.substring(l));
            if (rt.isEmpty()) {
                continue;
            }
            for (String i : lt) {
                for (String j : rt) {
                    res.add("(" + i + ", " + j + ")");
                }
            }
        }
        return res;
    }

    public List<String> getPos(String s) {
        List<String> pos = new ArrayList<>();
        if (s.charAt(0) != '0' || "0".equals(s)) {
            pos.add(s);
        }
        for (int i = 1; i < s.length(); ++i) {
            if ((i != 1 && s.charAt(0) == '0') || s.charAt(s.length() - 1) == '0') {
                continue;
            }
            pos.add(s.substring(0, i) + "." + s.substring(i));
        }
        return pos;
    }

    // 241 为运算表达式设计优先级
    public List<Integer> diffWaysToCompute(String expression) {
        return dfs(expression.toCharArray(), 0, expression.length() - 1);
    }

    public List<Integer> dfs(char[] chars, int l, int r) {
        List<Integer> res = new ArrayList<>();
        for (int i = l; i <= r; i++) {
            if (chars[i] >= '0' && chars[i] <= '9') continue;
            List<Integer> left = dfs(chars, l, i - 1);
            List<Integer> right = dfs(chars, i + 1, r);
            for (int n : left) {
                for (int m : right) {
                    if (chars[i] == '+') res.add(n + m);
                    if (chars[i] == '-') res.add(n - m);
                    if (chars[i] == '*') res.add(n * m);
                }
            }
        }
        if (res.isEmpty()) {
            int num = 0;
            for (int i = l; i <= r; i++) {
                num = num * 10 + chars[i] - '0';
            }
            res.add(num);
        }
        return res;
    }

    // 254 因子的组合
    public List<List<Integer>> getFactors(int n) {
        return getFactorsDfs(2, n);
    }

    private List<List<Integer>> getFactorsDfs(int start, int num) {
        if (num == 1) {
            return new ArrayList<>();
        }

        int end = (int) Math.sqrt(num);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            if (num % i == 0) {
                List<Integer> simpleList = new ArrayList<>();
                simpleList.add(i);
                simpleList.add(num / i);
                result.add(simpleList);
                // 检查mulNum能怎么拆
                List<List<Integer>> nextLists = getFactorsDfs(i, num / i);
                for (List<Integer> list : nextLists) {
                    list.add(i);
                    result.add(list);
                }
            }
        }
        return result;
    }

    // 246中心对称数
    public boolean isStrobogrammatic(String num) {
        Map<Character, Character> map = new HashMap<Character, Character>();
        map.put('6', '9');
        map.put('9', '6');
        map.put('1', '1');
        map.put('8', '8');
        map.put('0', '0');//特别注意0如果在开头或者末尾一定不符合题意
        int left = 0;
        int right = num.length() - 1;
        while (left <= right) {
            if (map.get(num.charAt(left)) == null || map.get(num.charAt(right)) == null) {
                return false;
            }
            if (map.get(num.charAt(left)) != num.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    // 247中心对称数2
    public List<String> findStrobogrammatic(int n) {
        return dfs(n, n);
    }

    private List<String> dfs(int n, int m) {
        if (n == 0) return new ArrayList<>(Arrays.asList(""));
        if (n == 1) return new ArrayList<>(Arrays.asList("0", "1", "8"));
        List<String> inner = dfs(n - 2, m);
        List<String> result = new ArrayList<>();
        for (String s : inner) {
            if (n != m) {
                result.add("0" + s + "0");
            }
            result.add("1" + s + "1");
            result.add("8" + s + "8");
            result.add("6" + s + "9");
            result.add("9" + s + "6");
        }
        return result;
    }

    // 679 24点游戏
    //    首先从 4 个数字中有序地选出 2 个数字，共有 4*3 = 12  种选法，并选择加、减、乘、除 4 种运算操作之一，用得到的结果取代选出的 2 个数字，剩下 3 个数字。
//    然后在剩下的 3 个数字中有序地选出 2 个数字，共有 3*2=6  种选法，并选择 4 种运算操作之一，用得到的结果取代选出的 2 个数字，剩下 2 个数字。
//    最后剩下 2 个数字，有 2 种不同的顺序，并选择 4 种运算操作之一。
    static final int TARGET = 24;
    static final double EPSILON = 1e-6;
    static final int ADD = 0, MULTIPLY = 1, SUBTRACT = 2, DIVIDE = 3;

    public boolean solve(List<Double> list) {
        if (list.size() == 0) {
            return false;
        }
        if (list.size() == 1) {
            return Math.abs(list.get(0) - TARGET) < EPSILON;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    List<Double> list2 = new ArrayList<>();
                    // 不是当前i\j的其余数字加入list2中
                    for (int k = 0; k < size; k++) {
                        if (k != i && k != j) {
                            list2.add(list.get(k));
                        }
                    }
                    // 遍历运算符
                    for (int k = 0; k < 4; k++) {
                        if (k < 2 && i > j) {
                            continue;
                        }
                        if (k == ADD) {
                            list2.add(list.get(i) + list.get(j));
                        } else if (k == MULTIPLY) {
                            list2.add(list.get(i) * list.get(j));
                        } else if (k == SUBTRACT) {
                            list2.add(list.get(i) - list.get(j));
                        } else if (k == DIVIDE) {
                            if (Math.abs(list.get(j)) < EPSILON) {
                                continue;
                            } else {
                                list2.add(list.get(i) / list.get(j));
                            }
                        }
                        if (solve(list2)) {
                            return true;
                        }
                        // 当前操作符不符合结果，remove掉，继续下一个操作符的循环
                        list2.remove(list2.size() - 1);
                    }
                }
            }
        }
        return false;
    }

    // 面试题08.06 汉诺塔问题
    public void hanota(List<Integer> A, List<Integer> B, List<Integer> C) {
        int n = A.size();
        move(n, A, B, C);
    }

    private void move(int n, List<Integer> A, List<Integer> B, List<Integer> C) {
        if (n == 1) {
            C.add(A.get(A.size() - 1));
            A.remove(A.size() - 1);
            return;
        }
        move(n - 1, A, C, B);
        C.add(A.get(A.size() - 1));
        A.remove(A.size() - 1);
        move(n - 1, B, A, C);
    }

    //869 重新排列得到2 的幂
    public boolean reorderedPowerOf2(int n) {
        char[] chars = String.valueOf(n).toCharArray();
        Arrays.sort(chars);
        boolean[] visited = new boolean[chars.length];
        return dfs(chars, 0, 0, visited);
    }

    private boolean dfs(char[] chars, int depth, int num, boolean[] visited) {
        if (depth == chars.length) {
            return isPowOfTwo(num);
        }

        for (int i = 0; i < chars.length; i++) {
            if (visited[i]) continue;
            if (num == 0 && chars[i] == '0') continue;
            if (i > 0 && !visited[i - 1] && chars[i] == chars[i - 1]) continue;
            visited[i] = true;
            if (dfs(chars, depth + 1, num * 10 + chars[i] - '0', visited)) return true;
            visited[i] = false;
        }
        return false;
    }

    private boolean isPowOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    //1239. 串联字符串的最大长度
    // 状态压缩+DFS
    int ans = 0;

    public int maxLength(List<String> arr) {
        List<Integer> masks = new ArrayList<>();
        for (String s : arr) {
            int mask = 0;
            for (int i = 0; i < s.length(); ++i) {
                int ch = s.charAt(i) - 'a';
                if (((mask >> ch) & 1) != 0) { // 若 mask 已有 ch，则说明 s 含有重复字母，无法构成可行解
                    mask = 0;
                    break;
                }
                mask |= 1 << ch; // 将 ch 加入 mask 中
            }
            if (mask > 0) {
                masks.add(mask);
            }
        }

        backtrack(masks, 0, 0);
        return ans;
    }

    // 循环和回溯的区别：循环两两元素只匹配一次，回溯可多次(跳过之间的元素、搭配之前的元素)
    public void backtrack(List<Integer> masks, int pos, int mask) {
        if (pos == masks.size()) {
            ans = Math.max(ans, Integer.bitCount(mask));
            return;
        }
        if ((mask & masks.get(pos)) == 0) { // mask 和 masks[pos] 无公共元素
            backtrack(masks, pos + 1, mask | masks.get(pos));
        }
        backtrack(masks, pos + 1, mask);
    }

    // 797 所有可能的路径
    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        List<List<Integer>> result = new ArrayList<>();
        Deque<Integer> path = new ArrayDeque<>();
        path.addLast(0);
        allPathsSourceTargetDfs(result, path, graph, 0, graph.length - 1);
        return result;
    }

    private void allPathsSourceTargetDfs(List<List<Integer>> result, Deque<Integer> path, int[][] graph, int idx, int target) {
        if (idx == target) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i : graph[idx]) {
            path.addLast(i);
            allPathsSourceTargetDfs(result, path, graph, i, target);
            path.removeLast();
        }
    }

    //37 解数独
    private boolean[][] line = new boolean[9][9];
    private boolean[][] column = new boolean[9][9];
    private boolean[][][] block = new boolean[3][3][9];
    private boolean validSudoku = false;
    private List<int[]> spaces = new ArrayList<>();

    public void solveSudoku(char[][] board) {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (board[i][j] == '.') {
                    spaces.add(new int[]{i, j});
                } else {
                    int digit = board[i][j] - '0' - 1;
                    line[i][digit] = column[j][digit] = block[i / 3][j / 3][digit] = true;
                }
            }
        }

        dfs(board, 0);
    }

    public void dfs(char[][] board, int pos) {
        if (pos == spaces.size()) {
            validSudoku = true;
            return;
        }

        int[] space = spaces.get(pos);
        int i = space[0], j = space[1];
        for (int digit = 0; digit < 9 && !validSudoku; ++digit) {
            if (!line[i][digit] && !column[j][digit] && !block[i / 3][j / 3][digit]) {
                line[i][digit] = column[j][digit] = block[i / 3][j / 3][digit] = true;
                board[i][j] = (char) (digit + '0' + 1);
                dfs(board, pos + 1);
                line[i][digit] = column[j][digit] = block[i / 3][j / 3][digit] = false;
            }
        }
    }


    //306 累加数 暴力搜索
    public boolean isAdditiveNumber(String num) {
        return dfs(num, 0, 0, 0, 0);
    }

    private boolean dfs(String num, int index, int count, long prevprev, long prev) {
        if (index >= num.length()) {
            return count > 2;
        }

        long current = 0;
        for (int i = index; i < num.length(); i++) {
            char c = num.charAt(i);

            if (num.charAt(index) == '0' && i > index) {
                // 剪枝1：不能做为前导0，但是它自己是可以单独做为0来使用的
                return false;
            }

            current = current * 10 + c - '0';

            if (count >= 2) {
                long sum = prevprev + prev;
                if (current > sum) {
                    // 剪枝2：如果当前数比之前两数的和大了，说明不合适
                    return false;
                }
                if (current < sum) {
                    // 剪枝3：如果当前数比之前两数的和小了，说明还不够，可以继续添加新的字符进来
                    continue;
                }
            }

            // 当前满足条件了，或者还不到两个数，向下一层探索
            if (dfs(num, i + 1, count + 1, prev, current)) {
                return true;
            }
        }

        return false;
    }

    // 79 单词搜索
    public boolean exist(char[][] board, String word) {
        boolean[][] visited = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (dfs(board, word.toCharArray(), i, j, 0, visited)) return true;
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, char[] chars, int i, int j, int index, boolean[][] visited) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j] || chars[index] != board[i][j])
            return false;
        if (index == chars.length - 1) return true;
        visited[i][j] = true;
        // 将ij 置空 可达到和visited一样的效果
//        board[i][j] = ' ';
        boolean res = dfs(board, chars, i - 1, j, index + 1, visited) || dfs(board, chars, i + 1, j, index + 1, visited)
                || dfs(board, chars, i, j - 1, index + 1, visited) || dfs(board, chars, i, j + 1, index + 1, visited);
        visited[i][j] = false;
//        board[i][j] = chars[index];
        return res;
    }

    //212 单词搜索
    public List<String> findWords(char[][] board, String[] words) {
        int m = board.length;
        int n = board[0].length;
        List<String> list = new ArrayList<>();
        Set<String> set = new HashSet<>(Arrays.asList(words));
        int max = set.stream().max(Comparator.comparingInt(String::length)).map(String::length).get();
        StringBuilder path = new StringBuilder();
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dfs(list, path, board, set, i, j, visited, max);
            }
        }
        return list;
    }

    private void dfs(List<String> list, StringBuilder path, char[][] board, Set<String> set, int i, int j, boolean[][] visited, int max) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j] || path.length() > max) {
            return;
        }
        path.append(board[i][j]);
        visited[i][j] = true;
        if (set.contains(path.toString())) {
            list.add(path.toString());
            set.remove(path.toString());
        }
        dfs(list, path, board, set, i - 1, j, visited, max);
        dfs(list, path, board, set, i + 1, j, visited, max);
        dfs(list, path, board, set, i, j - 1, visited, max);
        dfs(list, path, board, set, i, j + 1, visited, max);
        path.deleteCharAt(path.length() - 1);
        visited[i][j] = false;
    }

    // 上下左右移动的方向
    int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public List<String> findWordsTrie(char[][] board, String[] words) {
        // 结果集，去重
        Set<String> resultSet = new HashSet<>();

        // 构建字典树
        Trie root = new Trie();
        for (String word : words) {
            root.insert(word);
        }

        int m = board.length;
        int n = board[0].length;
        // 记录某个下标是否访问过
        boolean[][] visited = new boolean[m][n];
        // 记录沿途遍历到的元素
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                // 从每个元素开始遍历
                dfs(resultSet, result, board, i, j, root, visited);
            }
        }

        // 题目要求返回List
        return new ArrayList<>(resultSet);
    }

    private void dfs(Set<String> resultSet, StringBuilder result, char[][] board,
                     int i, int j, Trie node, boolean[][] visited) {
        // 判断越界，或者访问过，或者不在字典树中，直接返回
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || visited[i][j]
                || node.children[board[i][j] - 'a'] == null) {
            return;
        }

        // 记录当前字符
        result.append(board[i][j]);

        // 如果有结束字符，加入结果集中
        if (node.children[board[i][j] - 'a'].isEnd) {
            resultSet.add(result.toString());
        }

        // 记录当前元素已访问
        visited[i][j] = true;

        // 按四个方向去遍历
        for (int[] dir : dirs) {
            dfs(resultSet, result, board, i + dir[0], j + dir[1], node.children[board[i][j] - 'a'], visited);
        }

        // 还原状态
        visited[i][j] = false;
        result.deleteCharAt(result.length() - 1);
    }

    // 面试题 17.25. 单词矩阵
    TreeMap<Integer, Set<String>> map = new TreeMap<>((o1, o2) -> o2 - o1);
    int maxArea = 0;
    List<String> result = null;

    public String[] maxRectangle(String[] words) {
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(word);
            map.putIfAbsent(word.length(), new HashSet<>());
            map.get(word.length()).add(word);
        }
        for (int len : map.keySet()) {
            // 长度为len的单词 每一位从上到下维护一个字典树
            Trie[] nodes = new Trie[len];
            for (int i = 0; i < len; i++) {
                nodes[i] = trie;
            }
            Set<String> dict = map.get(len);
            List<String> path = new ArrayList<>();
            dfs(len, path, dict, nodes);
        }
        return result.toArray(new String[0]);
    }

    private void dfs(int len, List<String> path, Set<String> dict, Trie[] nodes) {
        if (len * len <= maxArea || path.size() == len) return;
        search:
        for (String word : dict) {
            Trie[] next = new Trie[len];
            boolean allValid = true;
            // 遍历word的每一位，每一位和从上一层带来的nodes连起来（纵向的）看是否在字典中
            for (int i = 0; i < len; i++) {
                int idx = word.charAt(i) - 'a';
                if (nodes[i].children[idx] == null) continue search;
                if (!nodes[i].children[idx].isEnd) allValid = false;
                next[i] = nodes[i].children[idx];
            }
            path.add(word);
            if (allValid && maxArea < len * path.size()) {
                maxArea = len * path.size();
                result = new ArrayList<>(path);
            }
            dfs(len, path, dict, next);
            path.remove(path.size() - 1);
        }
    }
    //endregion
}
