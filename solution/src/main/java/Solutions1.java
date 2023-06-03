import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solutions1 {
    // region------------------------------------------------------------括号问题-------------------------------------------
//20 有效的括号
    public boolean isValidSe(String s) {
        Stack<Character> stack = new Stack<>();
        if (s.length() % 2 != 0) {
            return false;
        }
        for (char c : s.toCharArray()) {
            if (c == '{' || c == '[' || c == '(') {
                stack.push(c);
                continue;
            }
            if (c == '}' || c == ']' || c == ')') {
                if (stack.isEmpty()) {
                    return false;
                }
                char tmp = stack.peek();
                if ((c == '}' && tmp == '{') || (c == ']' && tmp == '[') || (c == ')' && tmp == '(')) {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    // 678有效的括号字符串
    // 贪心
    public boolean checkValidStringGreedy(String s) {
        // 待匹配的(的最小个数和最多个数
        int min = 0, max = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                min++;
                max++;
            } else if (c == ')') {
                min--;
                max--;
            } else {
                min--;
                max++;
            }
            min = Math.max(min, 0);
            if (min > max) return false;
        }
        return min == 0;
    }

    public boolean checkValidStringDP(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '*') {
                dp[i][i] = true;
            }
        }
        for (int i = 1; i < n; i++) {
            char c1 = s.charAt(i - 1), c2 = s.charAt(i);
            dp[i - 1][i] = (c1 == '(' || c1 == '*') && (c2 == ')' || c2 == '*');
        }
        for (int i = n - 3; i >= 0; i--) {
            char c1 = s.charAt(i);
            for (int j = i + 2; j < n; j++) {
                char c2 = s.charAt(j);
                if ((c1 == '(' || c1 == '*') && (c2 == ')' || c2 == '*')) {
                    dp[i][j] = dp[i + 1][j - 1];
                }
                for (int k = i; k < j && !dp[i][j]; k++) {
                    dp[i][j] = dp[i][k] && dp[k + 1][j];
                }
            }
        }
        return dp[0][n - 1];
    }

    public boolean checkValidStringStack(String s) {
        Deque<Integer> leftStack = new LinkedList<>();
        Deque<Integer> asteriskStack = new LinkedList<>();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                leftStack.push(i);
            } else if (c == '*') {
                asteriskStack.push(i);
            } else {
                if (!leftStack.isEmpty()) {
                    leftStack.pop();
                } else if (!asteriskStack.isEmpty()) {
                    asteriskStack.pop();
                } else {
                    return false;
                }
            }
        }
        while (!leftStack.isEmpty() && !asteriskStack.isEmpty()) {
            int leftIndex = leftStack.pop();
            int asteriskIndex = asteriskStack.pop();
            if (leftIndex > asteriskIndex) {
                return false;
            }
        }
        return leftStack.isEmpty();
    }

    //921. 使括号有效的最少添加
    public int minAddToMakeValid(String s) {
        int cnt = 0;
        int ans = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                cnt++;
            } else {
                if (cnt != 0) cnt--;
                else ans++;
            }
        }
        return ans + cnt;
    }

    // endregion
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
    int sum = 0;

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

    //endregion

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

    //region  ---------------------------------------------图论BFS/DFS-----------------------------------------------

    //1042. 不邻接植花 颜色标记法
    public int[] gardenNoAdj(int n, int[][] paths) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] path : paths) {
            add2map(map, path[0], path[1]);
            add2map(map, path[1], path[0]);
        }
        int[] ans = new int[n];
        for (int i = 1; i <= n; i++) {
            boolean[] colored = new boolean[5];
            for (int near : map.getOrDefault(i, new ArrayList<>())) {
                colored[ans[near - 1]] = true;
            }
            for (int j = 1; j <= 4; j++) {
                if (!colored[j]) {
                    ans[i - 1] = j;
                    break;
                }
            }
        }
        return ans;
    }

    private void add2map(Map<Integer, List<Integer>> map, int x, int y) {
        List<Integer> ls = map.getOrDefault(x, new ArrayList<>());
        ls.add(y);
        map.put(x, ls);
    }

    // 1971. 寻找图中是否存在路径
    public boolean validPath(int n, int[][] edges, int source, int destination) {
        if (source == destination) return true;
        List<Integer>[] g = new List[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            g[edge[0]].add(edge[1]);
            g[edge[1]].add(edge[0]);
        }
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(source);
        visited[source] = true;
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            for (int near : g[cur]) {
                if (near == destination) return true;
                if (!visited[near]) {
                    queue.offer(near);
                    visited[near] = true;
                }
            }
        }
        return false;
    }

    public boolean validPathUnionFind(int n, int[][] edges, int source, int destination) {
        UnionFind1 unionFind = new UnionFind1(n);
        for (int[] edge : edges) {
            unionFind.union(edge[0], edge[1]);
        }
        return unionFind.isConnect(source, destination);
    }

    //1615. 最大网络秩
    public int maximalNetworkRank(int n, int[][] roads) {
        boolean[][] connect = new boolean[n][n];
        int[] degree = new int[n];
        for (int[] road : roads) {
            connect[road[0]][road[1]] = true;
            connect[road[1]][road[0]] = true;
            degree[road[0]]++;
            degree[road[1]]++;
        }
        int max = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int sum = degree[i] + degree[j] - (connect[i][j] ? 1 : 0);
                max = Math.max(max, sum);
            }
        }
        return max;
    }

    public int maximalNetworkRank2(int n, int[][] roads) {
        boolean[][] connect = new boolean[n][n];
        int[] degree = new int[n];
        for (int[] road : roads) {
            connect[road[0]][road[1]] = true;
            connect[road[1]][road[0]] = true;
            degree[road[0]]++;
            degree[road[1]]++;
        }
        int first = -1, second = -1;
        List<Integer> firstList = new ArrayList<>();
        List<Integer> secondList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] > first) {
                second = first;
                secondList.clear();
                secondList.addAll(firstList);
                first = degree[i];
                firstList.clear();
                firstList.add(i);
            } else if (degree[i] == first) {
                firstList.add(i);
            } else if (degree[i] > second) {
                second = degree[i];
                secondList.clear();
                secondList.add(i);
            } else if (degree[i] == second) {
                secondList.add(i);
            }
        }
        if (firstList.size() == 1) {
            int u = firstList.get(0);
            for (int v : secondList) {
                if (!connect[u][v]) {
                    return first + second;
                }
            }
            return first + second - 1;
        } else {
            int m = roads.length;
            if (firstList.size() * (firstList.size() - 1) / 2 > m) {
                return first * 2;
            }
            for (int u : firstList) {
                for (int v : firstList) {
                    if (u != v && !connect[u][v]) {
                        return first * 2;
                    }
                }
            }
            return first * 2 - 1;
        }
    }

    //2059. 转化数字的最小运算数
    public int minimumOperations(int[] nums, int start, int goal) {
        if (start == goal) return 0;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        int ops = 0;
        // BFS求步数的问题，每一层步数一样，如果分层写，这里用set，最外层step
        // 如果用map，可以不分层写，每次从map中取保留的step
        Set<Integer> visited = new HashSet<>();
        visited.add(start);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int cur = queue.poll();
                for (int num : nums) {
                    // 进队的都是满足条件的
                    int[] result = new int[]{cur + num, cur - num, cur ^ num};
                    for (int next : result) {
                        if (next == goal) return ops + 1;
                        // 不等于goal,又不满足条件，不用进队
                        if (next < 0 || next > 1000) continue;
                        if (visited.contains(next)) continue;
                        queue.offer(next);
                        visited.add(next);
                    }
                }
            }
            ops++;
        }
        return -1;
    }

    // 不分层写法
    public int minimumOperations2(int[] nums, int s, int t) {
        Deque<Integer> d = new ArrayDeque<>();
        Map<Integer, Integer> map = new HashMap<>();
        d.addLast(s);
        map.put(s, 0);
        while (!d.isEmpty()) {
            int cur = d.pollFirst();
            int step = map.get(cur);
            for (int i : nums) {
                int[] result = new int[]{cur + i, cur - i, cur ^ i};
                for (int next : result) {
                    if (next == t) return step + 1;
                    if (next < 0 || next > 1000) continue;
                    if (map.containsKey(next)) continue;
                    map.put(next, step + 1);
                    d.addLast(next);
                }
            }
        }
        return -1;
    }

    public int minimumOperationsDualBFS(int[] nums, int start, int goal) {
        if (start == goal) return 0;
        Queue<Long> startQueue = new ArrayDeque<>();
        startQueue.offer((long) start);
        Queue<Long> goalQueue = new ArrayDeque<>();
        goalQueue.offer((long) goal);
        // 双向BFS 要用map记录步数
        Map<Long, Integer> startMap = new HashMap<>();
        startMap.put((long) start, 0);
        Map<Long, Integer> goalMap = new HashMap<>();
        goalMap.put((long) goal, 0);
        while (!startQueue.isEmpty() && !goalQueue.isEmpty()) {
            if (startQueue.size() < goalQueue.size()) {
                int size = startQueue.size();
                for (int i = 0; i < size; i++) {
                    long cur = startQueue.poll();
                    int step = startMap.get(cur);
                    // 当前值必须满足条件才能进行操作
                    if (cur >= 0 && cur <= 1000) {
                        for (int num : nums) {
                            long[] result = new long[]{cur + num, cur - num, cur ^ num};
                            for (long next : result) {
                                if (goalMap.containsKey(next)) return step + 1 + goalMap.get(next);
                                if (startMap.containsKey(next)) continue;
                                startQueue.offer(next);
                                startMap.put(next, step + 1);
                            }
                        }
                    }
                }
            } else {
                int size = goalQueue.size();
                for (int i = 0; i < size; i++) {
                    long cur = goalQueue.poll();
                    int step = goalMap.get(cur);
                    // 当前值是操作后的值
                    for (int num : nums) {
                        long[] result = new long[]{cur - num, cur + num, cur ^ num};
                        for (long next : result) {
                            // next是操作前的值,操作前必须满足条件才可以操作
                            if (next < 0 || next > 1000) continue;
                            // startMap中越界的值不可能进行操作,故这里next其实从startMap中未越界的值中找
                            if (startMap.containsKey(next)) return step + 1 + startMap.get(next);
                            if (goalMap.containsKey(next)) continue;
                            goalMap.put(next, step + 1);
                            goalQueue.offer(next);
                        }
                    }
                }
            }

        }
        return -1;
    }

    // 126 单词接龙2
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        // 结果集
        List<List<String>> res = new ArrayList<>();
        Set<String> distSet = new HashSet<>(wordList);
        // 字典中不包含目标单词
        if (!distSet.contains(endWord)) {
            return res;
        }
        // 已经访问过的单词集合：只找最短路径，所以之前出现过的单词不用出现在下一层
        Set<String> visited = new HashSet<>();
        // 累积每一层的结果队列(纵向)
        Queue<List<String>> queue = new LinkedList<>();
        List<String> list = new ArrayList<>(Arrays.asList(beginWord));
        queue.add(list);
        visited.add(beginWord);
        // 是否到达符合条件的层：如果该层添加的某一单词符合目标单词，则说明截止该层的所有解为最短路径，停止循环
        boolean flag = false;
        while (!queue.isEmpty() && !flag) {
            // 上一层的结果队列
            int size = queue.size();
            // 该层添加的所有元素：每层必须在所有结果都添加完新的单词之后，再将这些单词统一添加到已使用单词集合
            // 如果直接添加到 visited 中，会导致该层本次结果添加之后的相同添加行为失败
            // 如：该层遇到目标单词，有两条路径都可以遇到，但是先到达的将该单词添加进 visited 中，会导致第二条路径无法添加
            Set<String> subVisited = new HashSet<>();
            for (int i = 0; i < size; i++) {
                List<String> path = queue.poll();
                // 获取该路径上一层的单词
                String word = path.get(path.size() - 1);
                char[] chars = word.toCharArray();
                // 寻找该单词的下一个符合条件的单词
                for (int j = 0; j < chars.length; j++) {
                    char temp = chars[j];
                    for (char ch = 'a'; ch <= 'z'; ch++) {
                        chars[j] = ch;
                        if (temp == ch) {
                            continue;
                        }
                        String str = new String(chars);
                        // 符合条件：在 wordList 中 && 之前的层没有使用过
                        if (distSet.contains(str) && !visited.contains(str)) {
                            // 生成新的路径
                            List<String> pathList = new ArrayList<>(path);
                            pathList.add(str);
                            // 如果该单词是目标单词：将该路径添加到结果集中，查询截止到该层
                            if (str.equals(endWord)) {
                                flag = true;
                                res.add(pathList);
                            }
                            // 将该路径添加到该层队列中
                            queue.add(pathList);
                            // 将该单词添加到该层已访问的单词集合中
                            subVisited.add(str);
                        }
                    }
                    chars[j] = temp;
                }
            }
            // 将该层所有访问的单词添加到总的已访问集合中
            visited.addAll(subVisited);
        }
        return res;
    }

    public List<String> findLadders2(String beginWord, String endWord, List<String> wordList) {
        List<String> result = new ArrayList<>();
        if (beginWord.equals(endWord)) return result;
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord)) return result;
        Set<String> visited = new HashSet<>();
        Queue<Deque<String>> queue = new ArrayDeque<>();
        Deque<String> deque = new ArrayDeque<>();
        deque.offerLast(beginWord);
        queue.offer(deque);
        visited.add(beginWord);
        while (!queue.isEmpty()) {
            int size = queue.size();
            Set<String> levelVisited = new HashSet<>();
            for (int i = 0; i < size; i++) {
                Deque<String> level = queue.poll();
                String last = level.peekLast();
                List<String> nears = getNear(last, dict);
                for (String near : nears) {
                    if (near.equals(endWord)) {
                        level.offerLast(near);
                        return new ArrayList<>(level);
                    }
                    if (!visited.contains(near)) {
                        Deque<String> newPath = new ArrayDeque<>(level);
                        levelVisited.add(near);
                        newPath.offerLast(near);
                        queue.offer(newPath);
                    }
                }
            }
            visited.addAll(levelVisited);
        }
        return result;
    }

    private List<String> getNear(String word, Set<String> dict) {
        char[] chars = word.toCharArray();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            char origin = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == origin) continue;
                chars[i] = c;
                String tmp = new String(chars);
                if (dict.contains(tmp)) {
                    result.add(tmp);
                }
                chars[i] = origin;
            }
        }
        return result;
    }

    // 双向BFS
    public List<List<String>> findLaddersBFS(String beginWord, String endWord, List<String> wordList) {
        // 结果集
        List<List<String>> res = new ArrayList<>();
        Set<String> words = new HashSet<>(wordList);
        // 字典中不包含目标单词
        if (!words.contains(endWord)) {
            return res;
        }
        // 存放关系：每个单词可达的下层单词
        Map<String, List<String>> mapTree = new HashMap<>();
        Set<String> begin = new HashSet<>(), end = new HashSet<>();
        begin.add(beginWord);
        end.add(endWord);
        if (buildTree(words, begin, end, mapTree, true)) {
            dfs(res, mapTree, beginWord, endWord, new LinkedList<>());
        }
        return res;
    }

    // 双向BFS，构建每个单词的层级对应关系
    private boolean buildTree(Set<String> words, Set<String> begin, Set<String> end, Map<String, List<String>> mapTree, boolean isFront) {
        if (begin.size() == 0) {
            return false;
        }
        // 始终以少的进行探索
        if (begin.size() > end.size()) {
            return buildTree(words, end, begin, mapTree, !isFront);
        }
        // 在已访问的单词集合中去除
        words.removeAll(begin);
        // 标记本层是否已到达目标单词
        boolean isMeet = false;
        // 记录本层所访问的单词
        Set<String> nextLevel = new HashSet<>();
        for (String word : begin) {
            char[] chars = word.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char temp = chars[i];
                for (char ch = 'a'; ch <= 'z'; ch++) {
                    chars[i] = ch;
                    String str = String.valueOf(chars);
                    if (words.contains(str)) {
                        nextLevel.add(str);
                        // 根据访问顺序，添加层级对应关系：始终保持从上层到下层的存储存储关系
                        // true: 从上往下探索：word -> str
                        // false: 从下往上探索：str -> word（查找到的 str 是 word 上层的单词）
                        String key = isFront ? word : str;
                        String nextWord = isFront ? str : word;
                        // 判断是否遇见目标单词
                        if (end.contains(str)) {
                            isMeet = true;
                        }
                        if (!mapTree.containsKey(key)) {
                            mapTree.put(key, new ArrayList<>());
                        }
                        mapTree.get(key).add(nextWord);
                    }
                }
                chars[i] = temp;
            }
        }
        if (isMeet) {
            return true;
        }
        return buildTree(words, nextLevel, end, mapTree, isFront);
    }

    // DFS: 组合路径
    private void dfs(List<List<String>> res, Map<String, List<String>> mapTree, String beginWord, String endWord, LinkedList<String> list) {
        list.add(beginWord);
        if (beginWord.equals(endWord)) {
            res.add(new ArrayList<>(list));
            list.removeLast();
            return;
        }
        if (mapTree.containsKey(beginWord)) {
            for (String word : mapTree.get(beginWord)) {
                dfs(res, mapTree, word, endWord, list);
            }
        }
        list.removeLast();
    }

    // 127 单词接龙
    // 双向BFS
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (wordSet.size() <= 0 || !wordSet.contains(endWord)) return 0;
        Set<String> visited = new HashSet<>();
        Set<String> beginVisited = new HashSet<>();
        Set<String> endVisited = new HashSet<>();
        beginVisited.add(beginWord);
        endVisited.add(endWord);
        int step = 1;
        while (!beginVisited.isEmpty() && !endVisited.isEmpty()) {
            if (beginVisited.size() > endVisited.size()) {
                Set<String> tmp = beginVisited;
                beginVisited = endVisited;
                endVisited = tmp;
            }
            Set<String> nextLevelVisited = new HashSet<>();
            for (String word : beginVisited) {
                if (check(word, wordSet, endVisited, nextLevelVisited, visited)) return step + 1;
            }
            beginVisited = nextLevelVisited;
            step++;
        }
        return 0;
    }

    private boolean check(String word, Set<String> wordSet, Set<String> endVisited, Set<String> nextLevelVisited, Set<String> visited) {
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char origin = chars[i];
            for (char k = 'a'; k <= 'z'; k++) {
                if (k == origin) continue;
                chars[i] = k;
                String nextWord = new String(chars);
                if (!wordSet.contains(nextWord)) continue;
                if (endVisited.contains(nextWord)) return true;
                if (!visited.contains(nextWord)) {
                    nextLevelVisited.add(nextWord);
                    visited.add(nextWord);
                }
            }
            chars[i] = origin;
        }
        return false;
    }

    //433 最小基因变化 双向BFS
    public int minMutation(String start, String end, String[] bank) {
        Set<String> bankSet = new HashSet<>(Arrays.asList(bank));
        if (bankSet.size() <= 0 || !bankSet.contains(end)) return -1;
        Set<String> visited = new HashSet<>();
        Set<String> beginVisited = new HashSet<>();
        Set<String> endVisited = new HashSet<>();
        beginVisited.add(start);
        endVisited.add(end);

        int step = 0;
        while (!beginVisited.isEmpty() && !endVisited.isEmpty()) {
            if (beginVisited.size() > endVisited.size()) {
                Set<String> tmp = beginVisited;
                beginVisited = endVisited;
                endVisited = tmp;
            }
            Set<String> nextLevelVisited = new HashSet<>();
            for (String s : beginVisited) {
                if (check2(s, bankSet, endVisited, nextLevelVisited, visited)) return step + 1;
            }
            beginVisited = nextLevelVisited;
            step++;
        }
        return -1;
    }

    private boolean check2(String word, Set<String> bankSet, Set<String> endVisited, Set<String> nextLevelVisited, Set<String> visited) {
        char[] keys = new char[]{'A', 'C', 'G', 'T'};
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char origin = chars[i];
            for (char k : keys) {
                if (k == origin) continue;
                chars[i] = k;
                String nextWord = new String(chars);
                if (!bankSet.contains(nextWord)) continue;
                if (endVisited.contains(nextWord)) return true;
                if (!visited.contains(nextWord)) {
                    nextLevelVisited.add(nextWord);
                    visited.add(nextWord);
                }
            }
            chars[i] = origin;
        }
        return false;
    }

    // 752 打开转盘锁
    public int openLock(String[] deadends, String target) {
        Set<String> deadSet = new HashSet<>(Arrays.asList(deadends));
        if (deadSet.contains(target) || deadSet.contains("0000")) return -1;
        if ("0000".equals(target)) return 0;
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();
        queue.offer("0000");
        visited.add("0000");

        int step = 0;
        while (!queue.isEmpty()) {
            //用于区分层级,step跟层级有关
            int width = queue.size();
            for (int i = 0; i < width; i++) {
                String code = queue.poll();
                for (String nextCode : getOneDiff(code)) {
                    if (deadSet.contains(nextCode)) continue;
                    if (target.equals(nextCode)) return step + 1;
                    if (!visited.contains(nextCode)) {
                        queue.offer(nextCode);
                        visited.add(nextCode);
                    }
                }
            }
            step++;
        }
        return -1;
    }

    private List<String> getOneDiff(String code) {
        List<String> result = new ArrayList<>();
        char[] chars = code.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char origin = chars[i];
            char prev = origin == '0' ? '9' : (char) (origin - 1);
            char next = origin == '9' ? '0' : (char) (origin + 1);
            chars[i] = prev;
            String prevCode = new String(chars);
            result.add(prevCode);
            chars[i] = next;
            String nextCode = new String(chars);
            result.add(nextCode);
            chars[i] = origin;
        }
        return result;
    }
    //双向BFS 反而慢
//    双向 BFS 在无解的情况下不如单向 BFS。因此我们可以先使用「并查集」进行预处理，判断「起点」和「终点」是否连通，如果不联通，直接返回 -1−1，有解才调用双向 BFS

    public int openLockDuelBFS(String[] deadends, String target) {
        Set<String> deadSet = new HashSet<>(Arrays.asList(deadends));
        if (deadSet.contains(target) || deadSet.contains("0000")) return -1;
        if ("0000".equals(target)) return 0;
        Set<String> visited = new HashSet<>();
        Queue<String> beginVisited = new ArrayDeque<>();
        Queue<String> endVisited = new ArrayDeque<>();
        beginVisited.offer("0000");
        endVisited.offer(target);
        visited.add("0000");
        visited.add(target);

        int step = 0;
        while (!beginVisited.isEmpty() && !endVisited.isEmpty()) {
            boolean find = false;
            if (beginVisited.size() > endVisited.size()) {
                find = checkBfs(endVisited, beginVisited, deadSet, visited);
            } else {
                find = checkBfs(beginVisited, endVisited, deadSet, visited);
            }
            if (find) return step + 1;
            step++;
        }
        return -1;
    }

    private boolean checkBfs(Queue<String> smallerQueue, Queue<String> largerQueue, Set<String> deadSet, Set<String> visited) {
        int width = smallerQueue.size();
        for (int i = 0; i < width; i++) {
            String code = smallerQueue.poll();
            for (String nextCode : getOneDiff(code)) {
                if (deadSet.contains(nextCode)) continue;
                if (largerQueue.contains(nextCode)) return true;
                if (!visited.contains(nextCode)) {
                    smallerQueue.offer(nextCode);
                    visited.add(nextCode);
                }
            }
        }
        return false;
    }

    // 773 滑动谜题
    public int slidingPuzzle(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int[] b : board) {
            for (int i : b) {
                sb.append(i);
            }
        }
        String initial = sb.toString();
        if ("123450".equals(initial)) return 0;
        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.offer(initial);
        visited.add(initial);
        int step = 0;
        while (!queue.isEmpty()) {
            int width = queue.size();
            for (int i = 0; i < width; i++) {
                String cur = queue.poll();
                for (String next : getNearStatus(cur)) {
                    if (!visited.contains(next)) {
                        if ("123450".equals(next)) return step + 1;
                        queue.offer(next);
                        visited.add(next);
                    }
                }
            }
            step++;
        }
        return -1;
    }

    private List<String> getNearStatus(String cur) {
        // [0,1,2]
        // [3,4,5]
        int[][] neighbors = new int[][]{{1, 3}, {0, 2, 4}, {1, 5}, {0, 4}, {1, 3, 5}, {2, 4}};
        List<String> result = new ArrayList<>();
        char[] chars = cur.toCharArray();
        int idx = cur.indexOf('0');
        int[] neighbor = neighbors[idx];
        for (int n : neighbor) {
            chars[idx] = chars[n];
            chars[n] = '0';
            result.add(new String(chars));
            chars[n] = chars[idx];
            chars[idx] = '0';
        }
        return result;
    }

    //675. 为高尔夫比赛砍树
    //砍树的路线唯一确定，当我们求出每两个相邻的砍树点最短路径，并进行累加即是答案（整条砍树路径的最少步数）
    public int cutOffTree(List<List<Integer>> forest) {
        int m = forest.size(), n = forest.get(0).size();
        List<int[]> trees = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (forest.get(i).get(j) > 1) {
                    trees.add(new int[]{forest.get(i).get(j), i, j});
                }
            }
        }
        trees.sort(Comparator.comparingInt(o -> o[0]));
        int x = 0, y = 0;
        int newX, newY;
        int result = 0;
        for (int[] tree : trees) {
            newX = tree[1];
            newY = tree[2];
            int ans = bfs(x, y, newX, newY, forest);
            if (ans == -1) return -1;
            result += ans;
            x = newX;
            y = newY;
        }
        return result;
    }

    private int bfs(int sourceX, int sourceY, int targetX, int targetY, List<List<Integer>> forest) {
        if (sourceX == targetX && sourceY == targetY) return 0;
        int m = forest.size(), n = forest.get(0).size();
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{sourceX, sourceY});
        boolean[][] visited = new boolean[m][n];
        visited[sourceX][sourceY] = true;
        int[][] directions = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size-- > 0) {
                int[] cell = queue.poll();
                int x = cell[0], y = cell[1];
                for (int[] dire : directions) {
                    int newX = x + dire[0], newY = y + dire[1];
                    if (newX < 0 || newX >= m || newY < 0 || newY >= n) continue;
                    if (newX == targetX && newY == targetY) return step + 1;
                    // 大于1的可以往返走
                    if (visited[newX][newY] || forest.get(newX).get(newY) == 0) continue;
                    visited[newX][newY] = true;
                    queue.offer(new int[]{newX, newY});
                }
            }
            step++;
        }
        return -1;
    }

    //1210. 穿过迷宫的最少移动次数
    public int minimumMoves(int[][] grid) {
        int n = grid.length;
        int[][][] dist = new int[n][n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(dist[i][j], -1);
            }
        }
        dist[0][0][0] = 0;
        Queue<int[]> queue = new ArrayDeque<int[]>();
        queue.offer(new int[]{0, 0, 0});

        while (!queue.isEmpty()) {
            int[] arr = queue.poll();
            int x = arr[0], y = arr[1], status = arr[2];
            if (status == 0) {
                // 向右移动一个单元格
                if (y + 2 < n && dist[x][y + 1][0] == -1 && grid[x][y + 2] == 0) {
                    dist[x][y + 1][0] = dist[x][y][0] + 1;
                    queue.offer(new int[]{x, y + 1, 0});
                }
                // 向下移动一个单元格
                if (x + 1 < n && dist[x + 1][y][0] == -1 && grid[x + 1][y] == 0 && grid[x + 1][y + 1] == 0) {
                    dist[x + 1][y][0] = dist[x][y][0] + 1;
                    queue.offer(new int[]{x + 1, y, 0});
                }
                // 顺时针旋转 90 度
                if (x + 1 < n && y + 1 < n && dist[x][y][1] == -1 && grid[x + 1][y] == 0 && grid[x + 1][y + 1] == 0) {
                    dist[x][y][1] = dist[x][y][0] + 1;
                    queue.offer(new int[]{x, y, 1});
                }
            } else {
                // 向右移动一个单元格
                if (y + 1 < n && dist[x][y + 1][1] == -1 && grid[x][y + 1] == 0 && grid[x + 1][y + 1] == 0) {
                    dist[x][y + 1][1] = dist[x][y][1] + 1;
                    queue.offer(new int[]{x, y + 1, 1});
                }
                // 向下移动一个单元格
                if (x + 2 < n && dist[x + 1][y][1] == -1 && grid[x + 2][y] == 0) {
                    dist[x + 1][y][1] = dist[x][y][1] + 1;
                    queue.offer(new int[]{x + 1, y, 1});
                }
                // 逆时针旋转 90 度
                if (x + 1 < n && y + 1 < n && dist[x][y][0] == -1 && grid[x][y + 1] == 0 && grid[x + 1][y + 1] == 0) {
                    dist[x][y][0] = dist[x][y][1] + 1;
                    queue.offer(new int[]{x, y, 0});
                }
            }
        }

        return dist[n - 1][n - 2][0];
    }

    //815 公交线路
    // 给你一个数组 routes ，表示一系列公交线路，其中每个 routes[i] 表示一条公交线路，第 i 辆公交车将会在上面循环行驶。
//例如，路线 routes[0] = [1, 5, 7] 表示第 0 辆公交车会一直按序列 1 -> 5 -> 7 -> 1 -> 5 -> 7 -> 1-> ... 这样的车站路线行驶。
// 现在从 source 车站出发（初始时不在公交车上），要前往 target 车站。 期间仅可乘坐公交车。
// 求出 最少乘坐的公交车数量 。如果不可能到达终点车站，返回 -1 。
//输入：routes = [[1,2,7],[3,6,7]], source = 1, target = 6
//输出：2
//解释：最优策略是先乘坐第一辆公交车到达车站 7 , 然后换乘第二辆公交车到车站 6 。
    public int numBusesToDestination(int[][] routes, int source, int target) {
        if (source == target) return 0;
        //每个站可以坐哪些公交
        Map<Integer, Set<Integer>> stationMap = new HashMap<>();
        //坐某个公交时最少经过几步
        Map<Integer, Integer> busMap = new HashMap<>();
        //坐过的公交队列
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < routes.length; i++) {
            for (int station : routes[i]) {
                Set<Integer> busSet = stationMap.getOrDefault(station, new HashSet<>());
                busSet.add(i);
                stationMap.put(station, busSet);
                if (station == source) {
                    //当前要做的公交
                    queue.offer(i);
                    //坐到当前公交经历的步数
                    busMap.put(i, 1);
                }
            }
        }
        while (!queue.isEmpty()) {
            int currBus = queue.poll();
            //换到当前公交的步数
            int currStep = busMap.get(currBus);

            //只要当前公交能到达某站，就返回；不然换一次公交就多一次换乘次数 （不是公交经过几站，是人经过几次公交）
            for (int station : routes[currBus]) {
                if (station == target) return currStep;
                Set<Integer> busSet = stationMap.get(station);
                if (busSet == null) continue;
                for (int bus : busSet) {
                    if (!busMap.containsKey(bus)) {
                        busMap.put(bus, currStep + 1);
                        queue.offer(bus);
                    }
                }
            }
        }
        return -1;
    }

    // 841 钥匙和房间
    //有 n 个房间，房间按从 0 到 n - 1 编号。最初，除 0 号房间外的其余所有房间都被锁住。你的目标是进入所有的房间。然而，你不能在没有获得钥匙的时候进入锁住的房间。
// 当你进入一个房间，你可能会在里面找到一套不同的钥匙，每把钥匙上都有对应的房间号，即表示钥匙可以打开的房间。你可以拿上所有钥匙去解锁其他房间。
// 给你一个数组 rooms 其中 rooms[i] 是你进入 i 号房间可以获得的钥匙集合。如果能进入 所有 房间返回 true，否则返回 false。
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int n = rooms.size();
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(0);
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            if (visited[cur]) continue;
            visited[cur] = true;
            for (int next : rooms.get(cur)) {
                queue.offer(next);
            }
        }
        for (int i = 0; i < n; i++) {
            if (!visited[i]) return false;
        }
        return true;
    }

    int roomNum;

    public boolean canVisitAllRoomsDFS(List<List<Integer>> rooms) {
        int n = rooms.size();
        roomNum = 0;
        boolean[] visited = new boolean[n];
        dfs(rooms, 0, visited);
        return roomNum == n;
    }

    public void dfs(List<List<Integer>> rooms, int x, boolean[] visited) {
        visited[x] = true;
        roomNum++;
        for (int it : rooms.get(x)) {
            if (!visited[it]) {
                dfs(rooms, it, visited);
            }
        }
    }


    // 847 访问所有节点的最短距离
    // 状态压缩+BFS
//    一些状态压缩的基本操作如下：
//         （1）访问第 i 个点的状态：state=(1 << i) & mask
//        （2）更改第 i 个点状态为 1：mask = mask | (1 << i)
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

    // 854 相似度为K的字符串
    public int kSimilarity(String s1, String s2) {
        Queue<Pair<String, Integer>> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.offer(new Pair<>(s1, 0));
        visited.add(s1);
        int step = 0;
        int n = s1.length();
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Pair<String, Integer> pair = queue.poll();
                String cur = pair.getKey();
                int pos = pair.getValue();
                if (cur.equals(s2)) {
                    return step;
                }
                while (pos < n && cur.charAt(pos) == s2.charAt(pos)) {
                    pos++;
                }
                for (int j = pos; j < n; j++) {
                    if (cur.charAt(j) == s2.charAt(j)) {
                        continue;
                    }
                    if (cur.charAt(j) == s2.charAt(pos)) {
                        String next = swap(cur, pos, j);
                        if (!visited.contains(next)) {
                            visited.add(next);
                            queue.offer(new Pair<>(next, pos + 1));
                        }
                    }
                }
            }
            step++;
        }
        return -1;
    }

    private String swap(String s, int i, int j) {
        char[] chars = s.toCharArray();
        char tmp = chars[i];
        chars[i] = chars[j];
        chars[j] = tmp;
        return new String(chars);
    }

    // 864 获取所有钥匙的最短路径
    int m;

    public int shortestPathAllKeys(String[] grid) {
        this.m = grid.length;
        this.n = grid[0].length();
        Queue<int[]> queue = new ArrayDeque<>();
        int k = 0, sx = 0, sy = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i].charAt(j) == '@') {
                    sx = i;
                    sy = j;
                } else if (grid[i].charAt(j) >= 'a' && grid[i].charAt(j) <= 'f') {
                    k++;
                }
            }
        }
        int finalStatus = (1 << k) - 1;
        //状态位,某种钥匙组合的
        int[][][] dist = new int[m][n][1 << k];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(dist[i][j], -1);
            }
        }
        queue.offer(new int[]{sx, sy, 0});
        dist[sx][sy][0] = 0;
        while (!queue.isEmpty()) {
            int[] tmp = queue.poll();
            int x = tmp[0], y = tmp[1], mask = tmp[2];
            for (int[] dire : directions) {
                int newX = x + dire[0], newY = y + dire[1];
                if (inAreaRowCol(newX, newY)) {
                    char c = grid[newX].charAt(newY);
                    if (c == '#') continue;
                    if (c == '.' || c == '@') {
                        if (dist[newX][newY][mask] == -1) {
                            dist[newX][newY][mask] = dist[x][y][mask] + 1;
                            queue.offer(new int[]{newX, newY, mask});
                        }
                    } else if (c >= 'a' && c <= 'f') {
                        int idx = c - 'a';
                        int newMask = mask | (1 << idx);
                        if (dist[newX][newY][newMask] == -1) {
                            dist[newX][newY][newMask] = dist[x][y][mask] + 1;
                            if (newMask == finalStatus) return dist[newX][newY][newMask];
                            queue.offer(new int[]{newX, newY, newMask});
                        }
                    } else if (c >= 'A' && c <= 'F') {
                        int idx = c - 'A';
                        if ((mask & (1 << idx)) != 0 && dist[newX][newY][mask] == -1) {
                            dist[newX][newY][mask] = dist[x][y][mask] + 1;
                            queue.offer(new int[]{newX, newY, mask});
                        }
                    }
                }
            }
        }
        return -1;
    }

    //1377. T 秒后青蛙的位置
    public double frogPosition(int n, int[][] edges, int t, int target) {
        List<Integer>[] g = new ArrayList[n + 1];
        Arrays.setAll(g, e -> new ArrayList<>());
        g[1].add(0); // 减少额外判断的小技巧
        for (int[] e : edges) {
            int x = e[0], y = e[1];
            g[x].add(y);
            g[y].add(x); // 建树
        }
        long prod = dfs(g, target, 1, 0, t);
        return prod != 0 ? 1.0 / prod : 0;
    }

    private long dfs(List<Integer>[] g, int target, int x, int fa, int leftT) {
        // t 秒后必须在 target（恰好到达，或者 target 是叶子停在原地）
        if (leftT == 0) return x == target ? 1 : 0;
        if (x == target) return g[x].size() == 1 ? 1 : 0;
        for (int y : g[x]) { // 遍历 x 的儿子 y
            if (y != fa) { // y 不能是父节点
                long prod = dfs(g, target, y, x, leftT - 1); // 寻找 target
                if (prod != 0)
                    return prod * (g[x].size() - 1); // 乘上儿子个数，并直接返回
            }
        }
        return 0; // 未找到 target
    }

    //2045. 到达目的地的第二短时间
    public int secondMinimum(int n, int[][] edges, int time, int change) {
        List<Integer>[] graph = new List[n + 1];
        // path[i][0]是从1到i最短路径，path[i][1]是从1到i的次短路径
        int[][] path = new int[n + 1][2];

        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
            Arrays.fill(path[i], Integer.MAX_VALUE);
        }

        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }

        path[1][0] = 0;
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{1, 0});
        while (path[n][1] == Integer.MAX_VALUE) {
            int[] cell = queue.poll();
            int node = cell[0], len = cell[1];
            int nextLen = len + 1;
            for (int next : graph[node]) {
                if (nextLen < path[next][0]) {
                    path[next][0] = nextLen;
                    queue.offer(new int[]{next, nextLen});
                } else if (nextLen > path[next][0] && nextLen < path[next][1]) {
                    path[next][1] = nextLen;
                    queue.offer(new int[]{next, nextLen});
                }
            }
        }
        int ans = 0;
        // [0,change) 可以走，[change-2change) 等待
        for (int i = 0; i < path[n][1]; i++) {
            if (ans % (2 * change) >= change) {
                ans += ((2 * change) - ans % (2 * change));
            }
            ans += time;
        }
        return ans;

    }

    //2146 价格范围内最高排名的k样物品
    public List<List<Integer>> highestRankedKItems(int[][] grid, int[] pricing, int[] start, int k) {
        List<int[]> result = new ArrayList<>();
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int[][] directions = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        Queue<int[]> queue = new ArrayDeque<>();
        int dist = 0;
        queue.offer(start);
        visited[start[0]][start[1]] = true;
        int fk = k;
        while (!queue.isEmpty() && k > 0) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] tmp = queue.poll();
                int x = tmp[0], y = tmp[1];

                if (grid[x][y] >= pricing[0] && grid[x][y] <= pricing[1]) {
                    k--;
                    result.add(new int[]{dist, grid[x][y], x, y});
                }

                for (int[] dire : directions) {
                    int newX = x + dire[0], newY = y + dire[1];
                    if (newX < 0 || newX >= m || newY < 0 || newY >= n || visited[newX][newY] || grid[newX][newY] == 0)
                        continue;
                    visited[newX][newY] = true;
                    queue.offer(new int[]{newX, newY});
                }
            }
            dist++;
        }
        result.sort((o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];
            if (o1[1] != o2[1]) return o1[1] - o2[1];
            if (o1[2] != o2[2]) return o1[2] - o2[2];
            return o1[3] - o2[3];
        });
        return result.stream().map(p -> Arrays.asList(p[2], p[3])).limit(fk).collect(Collectors.toList());
    }

    //2385. 感染二叉树需要的总时间
    public int amountOfTime(TreeNode root, int start) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        dfs(root, null, map);
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        Set<Integer> visited = new HashSet<>();
        visited.add(start);
        int minute = -1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                List<Integer> nearList = map.getOrDefault(queue.poll(), new ArrayList<>());
                for (int near : nearList) {
                    if (!visited.contains(near)) {
                        visited.add(near);
                        queue.offer(near);
                    }
                }
            }
            minute++;
        }
        return minute;
    }

    private void dfs(TreeNode node, TreeNode parent, Map<Integer, List<Integer>> map) {
        if (node == null) return;
        List<Integer> nearList = map.getOrDefault(node.val, new ArrayList<>());
        if (parent != null) {
            nearList.add(parent.val);
        }
        if (node.left != null) {
            nearList.add(node.left.val);
        }
        if (node.right != null) {
            nearList.add(node.right.val);
        }
        map.put(node.val, nearList);
        dfs(node.left, node, map);
        dfs(node.right, node, map);
    }

    //1263. 推箱子 Hard
    public int minPushBox(char[][] grid) {
        int m = grid.length, n = grid[0].length;
        int sx = -1, sy = -1, bx = -1, by = -1; // 玩家、箱子的初始位置
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                if (grid[x][y] == 'S') {
                    sx = x;
                    sy = y;
                } else if (grid[x][y] == 'B') {
                    bx = x;
                    by = y;
                }
            }
        }

        int[] d = {0, -1, 0, 1, 0};

        int[][] dp = new int[m * n][m * n];
        for (int i = 0; i < m * n; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        Queue<int[]> queue = new ArrayDeque<int[]>();
        dp[sx * n + sy][bx * n + by] = 0; // 初始状态的推动次数为 0
        queue.offer(new int[]{sx * n + sy, bx * n + by});
        while (!queue.isEmpty()) {
            Queue<int[]> queue1 = new ArrayDeque<int[]>();
            while (!queue.isEmpty()) {
                int[] arr = queue.poll();
                int s1 = arr[0], b1 = arr[1];
                int sx1 = s1 / n, sy1 = s1 % n, bx1 = b1 / n, by1 = b1 % n;
                if (grid[bx1][by1] == 'T') { // 箱子已被推到目标处
                    return dp[s1][b1];
                }
                for (int i = 0; i < 4; i++) { // 玩家向四个方向移动到另一个状态
                    int sx2 = sx1 + d[i], sy2 = sy1 + d[i + 1], s2 = sx2 * n + sy2;
                    if (!ok(grid, m, n, sx2, sy2)) { // 玩家位置不合法
                        continue;
                    }
                    if (bx1 == sx2 && by1 == sy2) { // 推动箱子
                        int bx2 = bx1 + d[i], by2 = by1 + d[i + 1], b2 = bx2 * n + by2;
                        if (!ok(grid, m, n, bx2, by2) || dp[s2][b2] <= dp[s1][b1] + 1) { // 箱子位置不合法 或 状态已访问
                            continue;
                        }
                        dp[s2][b2] = dp[s1][b1] + 1;
                        queue1.offer(new int[]{s2, b2});
                    } else {
                        if (dp[s2][b1] <= dp[s1][b1]) { // 状态已访问
                            continue;
                        }
                        dp[s2][b1] = dp[s1][b1];
                        queue.offer(new int[]{s2, b1});
                    }
                }
            }
            queue = queue1;
        }
        return -1;
    }

    public boolean ok(char[][] grid, int m, int n, int x, int y) { // 不越界且不在墙上
        return x >= 0 && x < m && y >= 0 && y < n && grid[x][y] != '#';
    }

    //863 二叉树中所有距离为K的节点
    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        Map<Integer, TreeNode> parents = new HashMap<>();
        List<Integer> result = new ArrayList<>();
        findParentsDfs(root, parents);
        findAnsDfs(target, null, 0, k, parents, result);
        return result;
    }

    private void findAnsDfs(TreeNode node, TreeNode from, int dist, int k, Map<Integer, TreeNode> parents, List<Integer> result) {
        if (node == null) return;
        if (dist == k) {
            result.add(node.val);
            return;
        }
        //从子到父，避免再遍历父的子  target->parent->target
        if (node.left != from) {
            findAnsDfs(node.left, node, dist + 1, k, parents, result);
        }
        if (node.right != from) {
            findAnsDfs(node.right, node, dist + 1, k, parents, result);
        }
        //从上到下的无需再从下到上遍历
        if (parents.get(node.val) != from) {
            findAnsDfs(parents.get(node.val), node, dist + 1, k, parents, result);
        }
    }

    private void findParentsDfs(TreeNode root, Map<Integer, TreeNode> parents) {
        if (root.left != null) {
            parents.put(root.left.val, root);
            findParentsDfs(root.left, parents);
        }
        if (root.right != null) {
            parents.put(root.right.val, root);
            findParentsDfs(root.right, parents);
        }
    }

    // 994 腐烂的橘子
    public int orangesRotting(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        Queue<int[]> queue = new ArrayDeque<>();
        int cnt = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 2) {
                    queue.offer(new int[]{i, j});
                    visited[i][j] = true;
                } else if (grid[i][j] == 1) {
                    cnt++;
                }
            }
        }
        if (cnt == 0) return 0;
        int minutes = -1;
        int[][] directions = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cell = queue.poll();
                int x = cell[0], y = cell[1];
                for (int[] dire : directions) {
                    int newX = x + dire[0], newY = y + dire[1];
                    if (newX >= 0 && newX < m && newY >= 0 && newY < n && grid[newX][newY] == 1 && !visited[newX][newY]) {
                        queue.offer(new int[]{newX, newY});
                        visited[newX][newY] = true;
                        cnt--;
                    }
                }
            }
            minutes++;
        }
        return cnt == 0 ? minutes : -1;
    }

    // 面试04.01 节点间通路
    public boolean findWhetherExistsPathBFS(int n, int[][] graph, int start, int target) {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int[] g : graph) {
            Set<Integer> set = map.getOrDefault(g[0], new HashSet<>());
            set.add(g[1]);
            map.put(g[0], set);
        }
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        boolean[] visited = new boolean[n];
        visited[start] = true;
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            for (int near : map.getOrDefault(cur, new HashSet<>())) {
                if (near == target) return true;
                if (visited[near]) continue;
                queue.offer(near);
            }
        }
        return false;
    }

    // 130 被围绕的区域
    public void solve(char[][] board) {
        int n = board.length;
        if (n == 0) {
            return;
        }
        int m = board[0].length;
        for (int i = 0; i < n; i++) {
            dfs(board, i, 0, n, m);
            dfs(board, i, m - 1, n, m);
        }
        for (int i = 1; i < m - 1; i++) {
            dfs(board, 0, i, n, m);
            dfs(board, n - 1, i, n, m);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                } else if (board[i][j] == 'A') {
                    board[i][j] = 'O';
                }
            }
        }
    }

    public void dfs(char[][] board, int x, int y, int n, int m) {
        if (x < 0 || x >= n || y < 0 || y >= m || board[x][y] != 'O') {
            return;
        }
        board[x][y] = 'A';
        dfs(board, x + 1, y, n, m);
        dfs(board, x - 1, y, n, m);
        dfs(board, x, y + 1, n, m);
        dfs(board, x, y - 1, n, m);
    }

    public void solveBFS(char[][] board) {
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        int n = board.length;
        if (n <= 0) {
            return;
        }
        int m = board[0].length;
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (board[i][0] == 'O') {
                queue.offer(new int[]{i, 0});
                board[i][0] = 'A';
            }
            if (board[i][m - 1] == 'O') {
                queue.offer(new int[]{i, m - 1});
                board[i][m - 1] = 'A';
            }
        }
        for (int i = 1; i < m - 1; i++) {
            if (board[0][i] == 'O') {
                queue.offer(new int[]{0, i});
                board[0][i] = 'A';
            }
            if (board[n - 1][i] == 'O') {
                queue.offer(new int[]{n - 1, i});
                board[n - 1][i] = 'A';
            }
        }
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0], y = cell[1];
            for (int i = 0; i < 4; i++) {
                int mx = x + dx[i], my = y + dy[i];
                if (mx < 0 || mx >= n || my < 0 || my >= m || board[mx][my] != 'O') {
                    continue;
                }
                queue.offer(new int[]{mx, my});
                board[mx][my] = 'A';
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                } else if (board[i][j] == 'A') {
                    board[i][j] = 'O';
                }
            }
        }
    }

    // 200 岛屿数量
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int num = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    num++;
                    dfs(grid, i, j);
                }
            }
        }
        return num;
    }

    private void dfs(char[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] == '0') {
            return;
        }
        grid[i][j] = '0';
        dfs(grid, i - 1, j);
        dfs(grid, i + 1, j);
        dfs(grid, i, j - 1);
        dfs(grid, i, j + 1);
    }

    // 463 岛屿的周长
    public int islandPerimeter(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    return islandPerimeterDfs(grid, i, j);
                }
            }
        }
        return 0;
    }

    private int islandPerimeterDfs(int[][] grid, int x, int y) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
            return 1;
        }
        if (grid[x][y] == 0) return 1;
        if (grid[x][y] == 2) return 0;
        grid[x][y] = 2;
        return islandPerimeterDfs(grid, x + 1, y) +
                islandPerimeterDfs(grid, x - 1, y) +
                islandPerimeterDfs(grid, x, y + 1) +
                islandPerimeterDfs(grid, x, y - 1);
    }

    public int maxAreaOfIsland(int[][] grid) {
        int max = 0;
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    max = Math.max(max, maxAreaOfIslanddfs(grid, i, j, visited));
                }
            }
        }
        return max;
    }

    private int maxAreaOfIslanddfs(int[][] grid, int x, int y, boolean[][] visited) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length || grid[x][y] == 0 || visited[x][y]) {
            return 0;
        }
        visited[x][y] = true;
        return 1 + maxAreaOfIslanddfs(grid, x + 1, y, visited)
                + maxAreaOfIslanddfs(grid, x - 1, y, visited)
                + maxAreaOfIslanddfs(grid, x, y + 1, visited)
                + maxAreaOfIslanddfs(grid, x, y - 1, visited);
    }

    //并查集
    static class UnionFind {
        int count;
        int[] parent;
        int[] rank;

        public UnionFind(char[][] grid) {
            count = 0;
            int m = grid.length;
            int n = grid[0].length;
            parent = new int[m * n];
            rank = new int[m * n];
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (grid[i][j] == '1') {
                        parent[i * n + j] = i * n + j;
                        ++count;
                    }
                    rank[i * n + j] = 0;
                }
            }
        }

        public int find(int i) {
            if (parent[i] != i) parent[i] = find(parent[i]);
            return parent[i];
        }

        public void union(int x, int y) {
            int rootx = find(x);
            int rooty = find(y);
            if (rootx != rooty) {
                if (rank[rootx] > rank[rooty]) {
                    parent[rooty] = rootx;
                } else if (rank[rootx] < rank[rooty]) {
                    parent[rootx] = rooty;
                } else {
                    parent[rooty] = rootx;
                    rank[rootx] += 1;
                }
                --count;
            }
        }

        public int getCount() {
            return count;
        }
    }

    public int numIslandsUnionFind(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int nr = grid.length;
        int nc = grid[0].length;
        int num_islands = 0;
        UnionFind uf = new UnionFind(grid);
        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    grid[r][c] = '0';
                    if (r - 1 >= 0 && grid[r - 1][c] == '1') {
                        uf.union(r * nc + c, (r - 1) * nc + c);
                    }
                    if (r + 1 < nr && grid[r + 1][c] == '1') {
                        uf.union(r * nc + c, (r + 1) * nc + c);
                    }
                    if (c - 1 >= 0 && grid[r][c - 1] == '1') {
                        uf.union(r * nc + c, r * nc + c - 1);
                    }
                    if (c + 1 < nc && grid[r][c + 1] == '1') {
                        uf.union(r * nc + c, r * nc + c + 1);
                    }
                }
            }
        }

        return uf.getCount();
    }

    // 733 图像渲染
    public int[][] floodFill(int[][] image, int sr, int sc, int color) {
        int origin = image[sr][sc];
        if (origin == color) return image;
        dfs(image, sr, sc, origin, color);
        return image;
    }

    private void dfs(int[][] image, int x, int y, int origin, int color) {
        if (x < 0 || x >= image.length || y < 0 || y >= image[0].length || image[x][y] != origin) return;
        image[x][y] = color;
        dfs(image, x + 1, y, origin, color);
        dfs(image, x - 1, y, origin, color);
        dfs(image, x, y + 1, origin, color);
        dfs(image, x, y - 1, origin, color);
    }

    //面试题 16.19. 水域大小
    public int[] pondSizes(int[][] land) {
        List<Integer> ans = new ArrayList<>();
        int m = land.length, n = land[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (land[i][j] == 0) {
                    ans.add(pondSizesDfs(land, i, j));
                }
            }
        }
        return ans.stream().sorted().mapToInt(p -> p).toArray();

    }

    private int pondSizesDfs(int[][] land, int x, int y) {
        if (x >= land.length || x < 0 || y >= land[0].length || y < 0 || land[x][y] != 0) {
            return 0;
        }
        land[x][y] = -1;
        int ans = 1;
        ans += pondSizesDfs(land, x + 1, y);
        ans += pondSizesDfs(land, x - 1, y);
        ans += pondSizesDfs(land, x, y + 1);
        ans += pondSizesDfs(land, x, y - 1);
        ans += pondSizesDfs(land, x + 1, y - 1);
        ans += pondSizesDfs(land, x - 1, y - 1);
        ans += pondSizesDfs(land, x + 1, y + 1);
        ans += pondSizesDfs(land, x - 1, y + 1);
        return ans;
    }

    // 1034 边界着色
    // 存储边界list
    public int[][] colorBorderDFS(int[][] grid, int row, int col, int color) {
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        List<int[]> boards = new ArrayList<>();
        int originColor = grid[row][col];
        visited[row][col] = true;
        dfs(grid, row, col, visited, boards, originColor);
        for (int[] board : boards) {
            grid[board[0]][board[1]] = color;
        }
        return grid;
    }

    private void dfs(int[][] grid, int x, int y, boolean[][] visited, List<int[]> boards, int originColor) {
        int m = grid.length, n = grid[0].length;
        int[][] directions = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        boolean isBoard = false;
        for (int[] dire : directions) {
            int newX = x + dire[0], newY = y + dire[1];
            if (!(newX >= 0 && newX < m && newY >= 0 && newY < n && grid[newX][newY] == originColor)) {
                isBoard = true;
            } else if (!visited[newX][newY]) {
                visited[newX][newY] = true;
                dfs(grid, newX, newY, visited, boards, originColor);
            }
        }
        if (isBoard) {
            boards.add(new int[]{x, y});
        }
    }

    public int[][] colorBorderBFS(int[][] grid, int row, int col, int color) {
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        List<int[]> borders = new ArrayList<>();
        int originalColor = grid[row][col];
        int[][] direc = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        Deque<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{row, col});
        visited[row][col] = true;
        while (!q.isEmpty()) {
            int[] node = q.poll();
            int x = node[0], y = node[1];

            boolean isBorder = false;
            for (int i = 0; i < 4; i++) {
                int nx = direc[i][0] + x, ny = direc[i][1] + y;
                if (!(nx >= 0 && nx < m && ny >= 0 && ny < n && grid[nx][ny] == originalColor)) {
                    isBorder = true;
                } else if (!visited[nx][ny]) {
                    visited[nx][ny] = true;
                    q.offer(new int[]{nx, ny});
                }
            }
            if (isBorder) {
                borders.add(new int[]{x, y});
            }
        }
        for (int[] border : borders) {
            int x = border[0], y = border[1];
            grid[x][y] = color;
        }
        return grid;
    }

    // 934 最短的桥
    // 先dfs找其中一个岛的所有点放到队列，再BFS
    public int shortestBridge(int[][] grid) {
        row = grid.length;
        col = grid[0].length;
        boolean[][] visited = new boolean[row][col];
        Queue<int[]> queue = new ArrayDeque<>();
        boolean find = false;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 1) {
                    find = true;
                    dfs(grid, i, j, queue, visited);
                    break;
                }
            }
            if (find) break;
        }
        int len = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] tmp = queue.poll();
                for (int[] dire : directions) {
                    int newX = tmp[0] + dire[0];
                    int newY = tmp[1] + dire[1];
                    if (inAreaRowCol(newX, newY) && !visited[newX][newY]) {
                        if (grid[newX][newY] == 1) return len;
                        visited[newX][newY] = true;
                        queue.offer(new int[]{newX, newY});
                    }
                }
            }
            len++;
        }
        return len;
    }

    private void dfs(int[][] grid, int i, int j, Queue<int[]> queue, boolean[][] visited) {
        if (!inAreaRowCol(i, j) || grid[i][j] == 0 || visited[i][j]) return;
        visited[i][j] = true;
        queue.offer(new int[]{i, j});
        for (int[] dire : directions) {
            dfs(grid, i + dire[0], j + dire[1], queue, visited);
        }
    }

    //给你一个大小为 m x n 的矩阵 board 表示甲板，其中，每个单元格可以是一艘战舰 'X' 或者是一个空位 '.' ，返回在甲板 board 上放置的
// 战舰 的数量。
// 战舰 只能水平或者垂直放置在 board 上。换句话说，战舰只能按 1 x k（1 行，k 列）或 k x 1（k 行，1 列）的形状建造，其中 k 可以
//是任意大小。两艘战舰之间至少有一个水平或垂直的空位分隔 （即没有相邻的战舰）。
//输入：board = [["X",".",".","X"],[".",".",".","X"],[".",".",".","X"]]
//输出：2
    public int countBattleships(char[][] board) {
        if (board.length <= 0) return 0;
        int nums = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++)
                if (board[i][j] == 'X') {
                    nums++;
                    dfsMatrix(board, i, j);
                }
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++)
                if (board[i][j] == '-') {
                    board[i][j] = 'X';
                }
        }
        return nums;
    }

    private void dfsMatrix(char[][] board, int i, int j) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != 'X') {
            return;
        }
        board[i][j] = '-';
        dfsMatrix(board, i - 1, j);
        dfsMatrix(board, i + 1, j);
        dfsMatrix(board, i, j - 1);
        dfsMatrix(board, i, j + 1);
    }

    public int countBattleshipsOnce(char[][] board) {
        if (board.length <= 0) return 0;
        int nums = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != 'X') continue;
                if (i > 0 && board[i - 1][j] == 'X') continue;
                if (j > 0 && board[i][j - 1] == 'X') continue;
                nums++;
            }
        }
        return nums;
    }

    //1219 黄金矿工
    public int getMaximumGold(int[][] grid) {
        int max = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != 0) {
                    boolean[][] visited = new boolean[grid.length][grid[0].length];
                    int num = dfs(grid, i, j, visited);
                    max = Math.max(num, max);
                }
            }
        }
        return max;

    }

    private int dfs(int[][] grid, int i, int j, boolean[][] visited) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || visited[i][j] || grid[i][j] == 0) {
            return 0;
        }
        visited[i][j] = true;
        int ileft = dfs(grid, i - 1, j, visited);
        int iright = dfs(grid, i + 1, j, visited);
        int jtop = dfs(grid, i, j - 1, visited);
        int jbottom = dfs(grid, i, j + 1, visited);
        visited[i][j] = false;
        return grid[i][j] + Math.max(Math.max(ileft, iright), Math.max(jtop, jbottom));
    }

    //2658. 网格图中鱼的最大数目
    public int findMaxFish(int[][] grid) {
        this.m = grid.length;
        this.n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] > 0 && !visited[i][j]) {
                    max = Math.max(max, dfs(i, j, grid, visited));
                }
            }
        }
        return max;
    }

    private int dfs(int x, int y, int[][] grid, boolean[][] visited) {
        if (x < 0 || x >= m || y < 0 || y >= n || grid[x][y] == 0 || visited[x][y]) return 0;
        int fish = grid[x][y];
        visited[x][y] = true;
        for (int[] dire : directions) {
            int newX = x + dire[0];
            int newY = y + dire[1];
            fish += dfs(newX, newY, grid, visited);
        }
        return fish;
    }

    // 面试08.02 迷路的机器人
    public List<List<Integer>> pathWithObstacles(int[][] obstacleGrid) {
        List<List<Integer>> path = new ArrayList<>();
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        boolean[][] visited = new boolean[m][n];
        dfs(obstacleGrid, 0, 0, path, visited);
        return find ? path : new ArrayList<>();
    }

    private void dfs(int[][] grid, int x, int y, List<List<Integer>> path, boolean[][] visited) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length || grid[x][y] == 1 || visited[x][y] || find)
            return;
        visited[x][y] = true;
        path.add(Arrays.asList(x, y));
        if (x == grid.length - 1 && y == grid[0].length - 1) find = true;
        dfs(grid, x, y + 1, path, visited);
        dfs(grid, x + 1, y, path, visited);
        if (!find) path.remove(path.size() - 1);
    }

    //51 n皇后
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        Set<Integer> columns = new HashSet<>();
        Set<Integer> diaLeft = new HashSet<>();
        Set<Integer> diaRight = new HashSet<>();
        int[] rows = new int[n];
        solveNQueensDfs(result, columns, diaLeft, diaRight, n, rows, 0);
        return result;
    }

    private void solveNQueensDfs(List<List<String>> result, Set<Integer> columns, Set<Integer> diaLeft, Set<Integer> diaRight, int n, int[] rows, int row) {
        if (row == n) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                char[] chars = new char[n];
                Arrays.fill(chars, '.');
                chars[rows[i]] = 'Q';
                list.add(new String(chars));
            }
            result.add(list);
        } else {
            for (int i = 0; i < n; i++) {
                if (columns.contains(i)) {
                    continue;
                }
                if (diaLeft.contains(row + i)) {
                    continue;
                }
                if (diaRight.contains(row - i)) {
                    continue;
                }
                columns.add(i);
                diaLeft.add(row + i);
                diaRight.add(row - i);
                rows[row] = i;
                solveNQueensDfs(result, columns, diaLeft, diaRight, n, rows, row + 1);
                columns.remove(i);
                diaLeft.remove(row + i);
                diaRight.remove(row - i);
                rows[row] = 0;
            }
        }
    }

    // 417. 太平洋大西洋水流问题
    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> result = new ArrayList<>();
        int m = heights.length;
        int n = heights[0].length;
        boolean[][] pacific = new boolean[m][n];
        boolean[][] atlantic = new boolean[m][n];

        for (int i = 0; i < m; i++) {
            pacificAtlanticDfs(heights, i, 0, pacific);
            pacificAtlanticDfs(heights, i, n - 1, atlantic);
        }
        for (int i = 0; i < n; i++) {
            pacificAtlanticDfs(heights, 0, i, pacific);
            pacificAtlanticDfs(heights, m - 1, i, atlantic);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (pacific[i][j] && atlantic[i][j]) {
                    result.add(Arrays.asList(i, j));
                }
            }
        }
        return result;
    }

    private void pacificAtlanticBfs(int[][] heights, int row, int col, boolean[][] oceans) {
        Deque<int[]> deque = new ArrayDeque<>();
        deque.offer(new int[]{row, col});
        oceans[row][col] = true;
        while (!deque.isEmpty()) {
            int[] cell = deque.poll();
            for (int[] dire : directions) {
                int newRow = cell[0] + dire[0];
                int newCol = cell[1] + dire[1];
                if (newRow >= 0 && newRow < heights.length && newCol >= 0 && newCol < heights[0].length
                        && heights[newRow][newCol] >= heights[cell[0]][cell[1]] && !oceans[cell[0]][cell[1]]) {
                    oceans[newRow][newCol] = true;
                    deque.offer(new int[]{newRow, newCol});
                }
            }
        }
    }

    private void pacificAtlanticDfs(int[][] heights, int row, int col, boolean[][] oceans) {
        if (oceans[row][col]) return;
        oceans[row][col] = true;
        for (int[] dire : directions) {
            int newRow = row + dire[0];
            int newCol = col + dire[1];
            if (newRow >= 0 && newRow < heights.length && newCol >= 0 && newCol < heights[0].length
                    && heights[newRow][newCol] >= heights[row][col]) {
                pacificAtlanticDfs(heights, newRow, newCol, oceans);
            }
        }
    }

    //1020 飞地的数量
    public int numEnclavesDFS(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 0; i < m; i++) {
            if (grid[i][0] == 1) dfs(grid, i, 0);
            if (grid[i][n - 1] == 1) dfs(grid, i, n - 1);
        }
        for (int i = 0; i < n; i++) {
            if (grid[0][i] == 1) dfs(grid, 0, i);
            if (grid[m - 1][i] == 1) dfs(grid, m - 1, i);
        }
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) ans++;
            }
        }
        return ans;
    }

    private void dfs(int[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] != 1) {
            return;
        }
        grid[i][j] = 2;
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j - 1);
        dfs(grid, i, j + 1);
    }

    public int numEnclavesUnionFind(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        UnionFind2 uf = new UnionFind2(grid);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    int index = i * n + j;
                    if (j + 1 < n && grid[i][j + 1] == 1) {
                        uf.union(index, index + 1);
                    }
                    if (i + 1 < m && grid[i + 1][j] == 1) {
                        uf.union(index, index + n);
                    }
                }
            }
        }
        int enclaves = 0;
        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                if (grid[i][j] == 1 && !uf.isOnEdge(i * n + j)) {
                    enclaves++;
                }
            }
        }
        return enclaves;
    }

    static class UnionFind2 {
        private int[] parent;
        private boolean[] onEdge;
        private int[] rank;

        public UnionFind2(int[][] grid) {
            int m = grid.length, n = grid[0].length;
            parent = new int[m * n];
            onEdge = new boolean[m * n];
            rank = new int[m * n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == 1) {
                        int index = i * n + j;
                        parent[index] = index;
                        if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                            onEdge[index] = true;
                        }
                    }
                }
            }
        }

        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]);
            }
            return parent[i];
        }

        public void union(int x, int y) {
            int rootx = find(x);
            int rooty = find(y);
            if (rootx != rooty) {
                if (rank[rootx] > rank[rooty]) {
                    parent[rooty] = rootx;
                    onEdge[rootx] |= onEdge[rooty];
                } else if (rank[rootx] < rank[rooty]) {
                    parent[rootx] = rooty;
                    onEdge[rooty] |= onEdge[rootx];
                } else {
                    parent[rooty] = rootx;
                    onEdge[rootx] |= onEdge[rooty];
                    rank[rootx]++;
                }
            }
        }

        public boolean isOnEdge(int i) {
            return onEdge[find(i)];
        }
    }

    //2492. 两个城市间路径的最小分数
    // 1和n 保证连接，求1的连通最小权边
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

    //3中存图方式 N点 M边
    // 邻接矩阵数组：w[a][b] = c 代表从 a 到 b 有权重为 c 的边 M = N^2
    int Nodes, Edges;
    int[][] weigth = new int[Nodes][Nodes];

    // 加边操作
    void add(int a, int b, int c) {
        weigth[a][b] = c;
    }
    // 链式前向星存图 链表头插法
//    首先 idx 是用来对边进行编号的，然后对存图用到的几个数组作简单解释：
//    he 数组：存储是某个节点所对应的边的集合（链表）的头结点；
//    e 数组：由于访问某一条边指向的节点；
//    ne 数组：由于是以链表的形式进行存边，该数组就是用于找到下一条边；
//    w 数组：用于记录某条边的权重为多少

    int[] he = new int[Nodes], e = new int[Edges], ne = new int[Edges], w = new int[Edges];
    int idx;

    void add2(int a, int b, int c) {
        e[idx] = b;
        ne[idx] = he[a];
        he[a] = idx;
        w[idx] = c;
        idx++;
    }

    void traverse(int a) {
        for (int i = he[a]; i != -1; i = ne[i]) {
            int b = e[i], c = w[i]; // 存在由 a 指向 b 的边，权重为 c
        }
    }
// endregion ----------------------------------------------------------------------------------------------------------

    //region-------------------------------------------匈牙利算法/KM算法-------------------------------------------------
    //1947. 最大兼容性评分和
    int[] lx;
    int[] ly;
    boolean[] sx;
    boolean[] sy;
    int[] match;
    int[] slack;
    int[][] points;

    public int maxCompatibilitySum(int[][] students, int[][] mentors) {
        m = students.length;
        lx = new int[m];
        ly = new int[m];
        sx = new boolean[m];
        sy = new boolean[m];
        match = new int[m];
        slack = new int[m];
        points = new int[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                points[i][j] = samePoint(students[i], mentors[j]);
                lx[i] = Math.max(points[i][j], lx[i]);
            }
        }
        Arrays.fill(ly, 0);
        Arrays.fill(match, -1);
        for (int i = 0; i < m; i++) {
            Arrays.fill(slack, Integer.MAX_VALUE);
            while (true) {
                Arrays.fill(sx, false);
                Arrays.fill(sy, false);
                if (match(i)) break;
                int min = Integer.MAX_VALUE;
                for (int y = 0; y < m; y++) {
                    if (!sy[y]) min = Math.min(min, slack[y]);
                }
                for (int idx = 0; idx < m; idx++) {
                    if (sx[idx]) lx[idx] -= min;
                    if (sy[idx]) ly[idx] += min;
                    else slack[idx] -= min;
                }
            }
        }
        int ans = 0;
        for (int y = 0; y < m; y++) {
            if (match[y] != -1) ans += lx[match[y]] + ly[y];
        }
        return ans;
    }

    private boolean match(int x) {
        sx[x] = true;
        for (int y = 0; y < m; y++) {
            if (sy[y]) continue;
            int gap = lx[x] + ly[y] - points[x][y];
            if (gap == 0) {
                sy[y] = true;
                if (match[y] == -1 || match(match[y])) {
                    match[y] = x;
                    return true;
                }
            } else {
                slack[y] = Math.min(slack[y], gap);
            }
        }
        return false;
    }

    private int samePoint(int[] s, int[] m) {
        int n = s.length;
        int point = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == m[i]) point++;
        }
        return point;
    }

    //2172. 数组的最大与和
    int[][] edges;

    public int maximumANDSum(int[] nums, int numSlots) {
        m = nums.length;
        n = 2 * numSlots;
        lx = new int[m];
        ly = new int[n];
        sx = new boolean[m];
        sy = new boolean[n];
        p = new int[n];
        slack = new int[n];
        edges = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < numSlots; j++) {
                edges[i][j] = edges[i][j + numSlots] = (nums[i] & (j + 1));
                lx[i] = Math.max(edges[i][j], lx[i]);
            }
        }
        Arrays.fill(ly, 0);
        Arrays.fill(p, -1);
        for (int i = 0; i < m; i++) {
            Arrays.fill(slack, Integer.MAX_VALUE);
            while (true) {
                Arrays.fill(sx, false);
                Arrays.fill(sy, false);
                if (match(i)) break;
                int min = Integer.MAX_VALUE;
                for (int y = 0; y < n; y++) {
                    if (!sy[y]) min = Math.min(min, slack[y]);
                }
                for (int x = 0; x < m; x++) {
                    if (sx[x]) lx[x] -= min;
                }
                for (int y = 0; y < n; y++) {
                    if (sy[y]) ly[y] += min;
                    else slack[y] -= min;
                }
            }
        }
        int ans = 0;
        for (int y = 0; y < n; y++) {
            if (p[y] != -1) ans += lx[p[y]] + ly[y];
        }
        return ans;
    }

    private boolean match2172(int x) {
        sx[x] = true;
        for (int y = 0; y < n; y++) {
            if (sy[y]) continue;
            int gap = lx[x] + ly[y] - edges[x][y];
            if (gap == 0) {
                sy[y] = true;
                if (p[y] == -1 || match2172(p[y])) {
                    p[y] = x;
                    return true;
                }
            } else {
                slack[y] = Math.min(slack[y], gap);
            }
        }
        return false;
    }
    //endregion
    // region ------------------------------------------------并查集------------------------------------------------------

    // 399 计算除法
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        int equationsSize = equations.size();

        UnionFind3 unionFind = new UnionFind3(2 * equationsSize);
        // 第 1 步：预处理，将变量的值与 id 进行映射，使得并查集的底层使用数组实现，方便编码
        Map<String, Integer> hashMap = new HashMap<>(2 * equationsSize);
        int id = 0;
        for (int i = 0; i < equationsSize; i++) {
            List<String> equation = equations.get(i);
            String var1 = equation.get(0);
            String var2 = equation.get(1);

            if (!hashMap.containsKey(var1)) {
                hashMap.put(var1, id);
                id++;
            }
            if (!hashMap.containsKey(var2)) {
                hashMap.put(var2, id);
                id++;
            }
            unionFind.union(hashMap.get(var1), hashMap.get(var2), values[i]);
        }

        // 第 2 步：做查询
        int queriesSize = queries.size();
        double[] res = new double[queriesSize];
        for (int i = 0; i < queriesSize; i++) {
            String var1 = queries.get(i).get(0);
            String var2 = queries.get(i).get(1);

            Integer id1 = hashMap.get(var1);
            Integer id2 = hashMap.get(var2);

            if (id1 == null || id2 == null) {
                res[i] = -1.0d;
            } else {
                res[i] = unionFind.isConnected(id1, id2);
            }
        }
        return res;
    }

    private class UnionFind3 {

        private int[] parent;

        /**
         * 指向的父结点的权值
         */
        private double[] weight;


        public UnionFind3(int n) {
            this.parent = new int[n];
            this.weight = new double[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                weight[i] = 1.0d;
            }
        }

        public void union(int x, int y, double value) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return;
            }

            parent[rootX] = rootY;
            // 关系式的推导请见「参考代码」下方的示意图
            // x/y= value,y/root[y] = weight[y],x/root[x] = weight[x]
            // => root[x]/root[y] = (x/weight[x]) / (y/weight[y]) = x * weight[y]/ y*weight[x]
            weight[rootX] = weight[y] * value / weight[x];
        }

        /**
         * 路径压缩
         *
         * @param x
         * @return 根结点的 id
         */
        public int find(int x) {
            if (x != parent[x]) {
                int origin = parent[x];
                parent[x] = find(parent[x]);
                weight[x] *= weight[origin];
            }
            return parent[x];
        }

        public double isConnected(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                // x/rooX = weight[x]
                // y/rootY = weight[y]
                //x/y == weight[x]/weight[y]
                return weight[x] / weight[y];
            } else {
                return -1.0d;
            }
        }
    }

    //765 情侣牵手
    public int minSwapsCouples(int[] row) {
        int n = row.length, m = n / 2;
        UnionFind1 unionFind = new UnionFind1(m);
        for (int i = 0; i < n; i += 2) {
            // row[i]/2 是情侣对的下标，下标相同的属于同一情侣对
            unionFind.union(row[i] / 2, row[i + 1] / 2);
        }
        int count = 0;//连通环的个数  合并后，每有一个顶点相同的就有一个环
        for (int i = 0; i < m; i++) {
            if (unionFind.find(i) == i) count++;
        }
        // 重新遍历情侣对，这样会合并路径，最后每个环的情侣对-1，累加=情侣对-环数

//        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
//        for (int i = 0; i < m; i++) {
//            int fx = unionFind.find( i);
//            map.put(fx, map.getOrDefault(fx, 0) + 1);
//        }
//
//        int ret = 0;
//        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
//            ret += entry.getValue() - 1;
//        }
//        return ret
        return m - count;
    }

    // 323 无向图中连通分量的数目
    public int countComponents(int n, int[][] edges) {
        UnionFind1 unionFind = new UnionFind1(n);
        for (int[] edge : edges) {
            unionFind.union(edge[0], edge[1]);
        }
        return unionFind.getConnectedNum();
    }

    // 547 省份数量
    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        UnionFind1 unionFind = new UnionFind1(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isConnected[i][j] == 1) {
                    unionFind.union(i, j);
                }
            }
        }
        return unionFind.getConnectedNum();
    }

    // 1319 连通网络的操作次数 连通分量-1
    // 拓扑排序做法 makeConnectedDfs
    public int makeConnected(int n, int[][] connections) {
        //connections.length =线缆数量 +1>=节点数
        if (connections.length + 1 < n) return -1;
        UnionFind1 unionFind = new UnionFind1(n);
        for (int[] con : connections) {
            unionFind.union(con[0], con[1]);
        }
        return unionFind.getConnectedNum() - 1;
    }

    // 684 冗余链接 offer 118 多余的边
//    初始时，每个节点都属于不同的连通分量。遍历每一条边，判断这条边连接的两个顶点是否属于相同的连通分量。
//    如果两个顶点属于不同的连通分量，则说明在遍历到当前的边之前，这两个顶点之间不连通，因此当前的边不会导致环出现，合并这两个顶点的连通分量。
//    如果两个顶点属于相同的连通分量，则说明在遍历到当前的边之前，这两个顶点之间已经连通，因此当前的边导致环出现，为附加的边，将当前的边作为答案返回。
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        UnionFind1 unionFind = new UnionFind1(n);
        for (int[] edge : edges) {
            int x = edge[0], y = edge[1];
            if (unionFind.isConnect(x, y)) return edge;
            unionFind.union(x, y);
        }
        return new int[0];
    }

    // 261 以图判树
    public boolean validTree(int n, int[][] edges) {
        UnionFind1 unionFind = new UnionFind1(n);
        for (int[] edge : edges) {
            if (unionFind.isConnect(edge[0], edge[1])) {
                return false;
            }
            unionFind.union(edge[0], edge[1]);
        }
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (i == unionFind.find(i)) {
                cnt++;
            }
        }
        return cnt == 1;
    }

    // 839 相似字符串
    int[] parents;

    public int numSimilarGroups(String[] strs) {
        int n = strs.length;
        parents = new int[n];
        for (int i = 0; i < n; i++) {
            parents[i] = i;
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int rootI = find(i);
                int rootJ = find(j);
                if (rootI == rootJ) {
                    continue;
                }
                if (check(strs[i], strs[j])) {
                    parents[rootI] = rootJ;
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (parents[i] == i) {
                ans++;
            }
        }
        return ans;
    }

    public boolean check(String s1, String s2) {
        int n = s1.length();
        int diff = 0;
        for (int i = 0; i < n; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                diff++;
            }
            if (diff > 2) return false;
        }
        return true;
    }

    // 952 按公因式计算最大组件大小
    public int largestComponentSize(int[] nums) {
        int m = Arrays.stream(nums).max().getAsInt();
        //union的值，所以范围是值+1
        UnionFind1 uf = new UnionFind1(m + 1);
        for (int num : nums) {
            for (int i = 2; i * i <= num; i++) {
                if (num % i == 0) {
                    uf.union(num, i);
                    uf.union(num, num / i);
                }
            }
        }
        int[] counts = new int[m + 1];
        int ans = 0;
        for (int num : nums) {
            int root = uf.find(num);
            counts[root]++;
            ans = Math.max(ans, counts[root]);
        }
        return ans;
    }

    //解法2
    static int Node = 20010;
    static int[] p = new int[Node], sz = new int[Node];
    int ans952 = 1;

    int find(int x) {
        if (p[x] != x) p[x] = find(p[x]);
        return p[x];
    }

    void union(int a, int b) {
        if (find(a) == find(b)) return;
        sz[find(a)] += sz[find(b)];
        p[find(b)] = p[find(a)];
        ans = Math.max(ans, sz[find(a)]);
    }

    public int largestComponentSize2(int[] nums) {
        int n = nums.length;
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int cur = nums[i];
            for (int j = 2; j * j <= cur; j++) {
                if (cur % j == 0) add(map, j, i);
                while (cur % j == 0) cur /= j;
            }
            if (cur > 1) add(map, cur, i);
        }
        for (int i = 0; i <= n; i++) {
            p[i] = i;
            sz[i] = 1;
        }
        for (int key : map.keySet()) {
            List<Integer> list = map.get(key);
            for (int i = 1; i < list.size(); i++) union(list.get(0), list.get(i));
        }
        return ans952;
    }

    void add(Map<Integer, List<Integer>> map, int key, int val) {
        List<Integer> list = map.getOrDefault(key, new ArrayList<>());
        list.add(val);
        map.put(key, list);
    }

    static class UnionFind1 {
        int[] parents;
        int[] rank;
        int connectedNum; // 连通分量数

        public UnionFind1(int n) {
            parents = new int[n];
            rank = new int[n];
            connectedNum = n; //默认所有点都不连通
            for (int i = 0; i < n; i++) {
                parents[i] = i;
            }
        }

        public int find(int x) {
            return parents[x] == x ? x : (parents[x] = find(parents[x]));
        }

        public void union(int x, int y) {
            int rootx = find(x);
            int rooty = find(y);
            if (rootx != rooty) {
                connectedNum--; // union 两个，连通分量减少1个
                if (rank[rootx] > rank[rooty]) {
                    parents[rooty] = rootx;
                } else if (rank[rootx] < rank[rooty]) {
                    parents[rootx] = rooty;
                } else {
                    parents[rooty] = rootx;
                    rank[rootx]++;
                }
            }
        }

        public int getConnectedNum() {
            return connectedNum;
        }

        public boolean isConnect(int x, int y) {
            return find(x) == find(y);
        }
    }

    // 1697. 检查边长度限制的路径是否存在
    public boolean[] distanceLimitedPathsExist(int n, int[][] edgeList, int[][] queries) {
        // 将edgeList和queries分别按边的权重从小到大排序
        Arrays.sort(edgeList, Comparator.comparingInt(o -> o[2]));
        // 保存下标数组，用于ans的下标，避免排序完queries后ans找不到对应的idx
        Integer[] idx = new Integer[queries.length];
        for (int i = 0; i < idx.length; i++) {
            idx[i] = i;
        }
        Arrays.sort(idx, Comparator.comparingInt(o -> queries[o][2]));
        int k = 0;
        UnionFind1 unionFind = new UnionFind1(n);
        boolean[] ans = new boolean[queries.length];
        for (int i : idx) {
            // 离线查询：将小于当前query的limit的边合并，k指向下一个大于limit的边
            // 下一个查询时，利用已有的结果
            while (k < edgeList.length && edgeList[k][2] < queries[i][2]) {
                unionFind.union(edgeList[k][0], edgeList[k][1]);
                k++;
            }
            ans[i] = unionFind.isConnect(queries[i][0], queries[i][1]);
        }
        return ans;

    }

    //面试题 17.07. 婴儿名字 字符串并查集
    class UnionFindString {
        Map<String, String> parents;
        Map<String, Integer> frequency;

        public UnionFindString(String[] names) {
            parents = new HashMap<>();
            frequency = new HashMap<>();
            for (String s : names) {
                String name = s.substring(0, s.indexOf('('));
                int freq = Integer.parseInt(s.substring(s.indexOf('(') + 1, s.indexOf(')')));
                parents.put(name, name);
                frequency.put(name, freq);
            }
        }

        public String find(String name) {
            if (!parents.containsKey(name)) {
                parents.put(name, name);
                frequency.put(name, 0);
                return name;
            }
            if (parents.get(name).equals(name)) return name;
            parents.put(name, find(parents.get(name)));
            return parents.get(name);
        }

        public void union(String name1, String name2) {
            String root1 = find(name1);
            String root2 = find(name2);
            if (root1.equals(root2)) return;
            int freq1 = frequency.get(root1);
            int freq2 = frequency.get(root2);
            if (root1.compareTo(root2) < 0) {
                parents.put(root2, root1);
                frequency.put(root1, freq1 + freq2);
            } else {
                parents.put(root1, root2);
                frequency.put(root2, freq1 + freq2);
            }
        }

        public int getFreq(String name) {
            return frequency.get(find(name));
        }
    }

    public String[] trulyMostPopular(String[] names, String[] synonyms) {
        UnionFindString unionFind = new UnionFindString(names);
        for (String synonym : synonyms) {
            String[] ns = synonym.split(",");
            unionFind.union(ns[0].substring(1), ns[1].substring(0, ns[1].length() - 1));
        }
        List<String> res = new ArrayList<>();
        for (String s : names) {
            String name = s.substring(0, s.indexOf('('));
            if (name.equals(unionFind.find(name))) {
                res.add(name + "(" + unionFind.getFreq(name) + ")");
            }
        }
        return res.toArray(new String[0]);
    }

    // endregion -----------------------------------------------------------------------------------------
    //region -------------------------------------------------------------------最短路径------------------------------------------

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

    //1162 地图分析
//你现在手里有一份大小为 n x n 的 网格 grid，上面的每个 单元格 都用 0 和 1 标记好了。其中 0 代表海洋，1 代表陆地。
// 请你找出一个海洋单元格，这个海洋单元格到离它最近的陆地单元格的距离是最大的，并返回该距离。如果网格上只有陆地或者海洋，请返回 -1。
// 我们这里说的距离是「曼哈顿距离」（ Manhattan Distance）：(x0, y0) 和 (x1, y1) 这两个单元格之间的距离是 |x0 -x1| + |y0 - y1| 。
//输入：grid = [[1,0,1],[0,0,0],[1,0,1]]
//输出：2 解释：海洋单元格 (1, 1) 和所有陆地单元格之间的距离都达到最大，最大距离为 2。
    //单源BFS 从每一个海洋出发，由BFS搜出每一个海洋的最短的，最后再比较
    public int maxDistanceBFS(int[][] grid) {
        N = grid.length;
        int ans = -1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 0) {
                    ans = Math.max(ans, bfs(grid, i, j));
                }
            }
        }
        return ans;
    }

    private int bfs(int[][] grid, int i, int j) {
        //单源BFS，只比较上一个点的距离，因此无需额外数组存，和坐标一起存即可
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{i, j, 0});
        boolean[][] visited = new boolean[N][N];
        visited[i][j] = true;
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0], y = cell[1], distance = cell[2];//distance 不在队列内初始化，因为距离信息可能从上一层代入，所以需要map/array将此信息代入传递

            for (int[] direction : directions) {
                int newX = x + direction[0], newY = y + direction[1];
                if (inAreaN(newX, newY) && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    distance += Math.abs(newX - x) + Math.abs(newY - y);
                    queue.offer(new int[]{newX, newY, distance});
                    if (grid[newX][newY] == 1) {
                        return distance;
                    }
                }
            }
        }
        return -1;
    }

    //多源BFS  从陆地到海洋
    public int maxDistanceMultiSourceBFS(int[][] grid) {
        N = grid.length;
        //多源BFS 需要额外的数组存储多个源点的距离
        int[][] dist = new int[N][N];
        for (int[] row : dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 1) {
                    dist[i][j] = 0;
                    queue.offer(new int[]{i, j});
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0], y = cell[1];
            for (int[] direction : directions) {
                int newX = x + direction[0], newY = y + direction[1];
                int distance = dist[x][y] + Math.abs(newX - x) + Math.abs(newY - y);
                if (inAreaN(newX, newY) && distance < dist[newX][newY]) {
                    dist[newX][newY] = distance;
                    queue.offer(new int[]{newX, newY});
                }
            }
        }
        int ans = -1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 0) {
                    ans = Math.max(ans, dist[i][j]);
                }
            }
        }
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    public int maxDistanceDijkstra(int[][] grid) {
        N = grid.length;
        //存储总体最优的距离
        int[][] dist = new int[N][N];
        //存储当前最优点的距离
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o[2]));

        for (int[] row : dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 1) {
                    dist[i][j] = 0;
                    queue.offer(new int[]{i, j, 0});
                }
            }
        }
        // 从所有的海洋出发，每次更新dist的时候可能被其他海洋更新过，所以每次取最优的dist
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0], y = cell[1], distance = cell[2];
            for (int[] direction : directions) {
                int newX = x + direction[0], newY = y + direction[1];
                distance += Math.abs(newX - x) + Math.abs(newY - y);
                if (inAreaN(newX, newY) && distance < dist[newX][newY]) {
                    dist[newX][newY] = distance;
                    queue.offer(new int[]{newX, newY, distance});
                }
            }
        }
        int ans = -1;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 0) {
                    ans = Math.max(ans, dist[i][j]);
                }
            }
        }
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    // 542 01矩阵 offer 107 矩阵中的距离
    // 多源BFS,超级源点,反向BFS
    public int[][] updateMatrix(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[][] directions = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    queue.offer(new int[]{i, j});
                    visited[i][j] = true;
                }
            }
        }
        int[][] dist = new int[m][n];
        while (!queue.isEmpty()) {
            int[] tmp = queue.poll();
            int r = tmp[0], c = tmp[1];
            for (int[] d : directions) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    dist[nr][nc] = dist[r][c] + 1;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
        return dist;
    }

    // 单源最短路径算法Dijkstra 贪心思想
    // 多源最短路径算法Floyd DP思想

    // 743网络延迟时间
    // Floyd(邻接矩阵)
    public int networkDelayTimeFloyd(int[][] times, int n, int k) {
        int INF = Integer.MAX_VALUE / 2;
        // 初始化邻接矩阵
        int[][] w = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                w[i][j] = i == j ? 0 : INF;
            }
        }
        // 存图
        for (int[] t : times) {
            int x = t[0] - 1, y = t[1] - 1;
            w[x][y] = t[2];
        }

        // floyd 三层循环 求所有点到其他点的最短距离
        // 枚举中间点-枚举起点-枚举终点-松弛操作
        for (int i = 0; i < n; i++) {
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    w[x][y] = Math.min(w[x][y], w[x][i] + w[i][y]);
                }
            }
        }
        // 遍历k点的结果
        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans = Math.max(w[k - 1][i], ans);
        }
        return ans == INF ? -1 : ans;
    }

    //朴素 Dijkstra（邻接矩阵） 使用贪心策略优化后的广度优先搜索
    public int networkDelayTimeDijkstra(int[][] times, int n, int k) {
        int INF = Integer.MAX_VALUE / 2;
        int[][] matrix = new int[n][n];
        for (int[] row : matrix) {
            Arrays.fill(row, INF);
        }
        for (int[] t : times) {
            int x = t[0] - 1, y = t[1] - 1;
            matrix[x][y] = t[2];
        }
        // 距离源点的距离
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[k - 1] = 0;

        // 已确定的点
        boolean[] used = new boolean[n];
        //最外层循环，每次循环找到一个确定的点，n次循环找到所有点,i不对应点，只对应次数（used数组的长度）
        for (int i = 0; i < n; i++) {
            //从未确定的点中找到一个距离源点最近的点，y对应点
            int x = -1;
            for (int y = 0; y < n; y++) {
                //第一次一定找到源点(dist[k]=0)，下一次外层循环找到距离源点最近的未确定点，如果距离都是INF按顺序找第一个
                if (!used[y] && (x == -1 || dist[y] < dist[x])) {
                    x = y;
                }
            }
            //以上两个循环保证了每次找到距离源点最近的未确定的点
            //外层循环每次确定一个x，更新used[]
            used[x] = true;
            //通过刚刚找到的未确定的点更新其余所有点 整体贪心思路
            // 其实只能更新和y关联的点
            for (int y = 0; y < n; y++) {
                dist[y] = Math.min(dist[y], dist[x] + matrix[x][y]);
            }
        }
        int ans = Arrays.stream(dist).max().getAsInt();
        return ans == INF ? -1 : ans;
    }

    // Heap优化 (邻接表)
    public int networkDelayTimeHeap(int[][] times, int n, int k) {
        int INF = Integer.MAX_VALUE / 2;
        // 建图方法 类似实体x到y权值w
        List<int[]>[] g = new List[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }
        for (int[] t : times) {
            int x = t[0] - 1, y = t[1] - 1;
            g[x].add(new int[]{y, t[2]});
        }
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[k - 1] = 0;
        boolean[] used = new boolean[n];
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        priorityQueue.offer(new int[]{k - 1, 0});
        while (!priorityQueue.isEmpty()) {
            int[] tmp = priorityQueue.poll();
            int x = tmp[0], time = tmp[1];//time只是在加入队列是排序用
            if (used[x]) continue;
            used[x] = true;
            for (int[] e : g[x]) {
                // d是y到源点的距离=e[1](y到x)+x到源点dist[x]
                int y = e[0], d = dist[x] + e[1];
                if (d < dist[y]) {
                    dist[y] = d;
                    priorityQueue.offer(new int[]{y, d});
                }
            }
        }
        int ans = Arrays.stream(dist).max().getAsInt();
        return ans == INF ? -1 : ans;
    }

    // 有权图 单源BFS 从一个点到其余所有点的最短路径 更新dist数组
    public int networkDelayTimeBFS(int[][] times, int n, int k) {
        Map<Integer, List<Pair<Integer, Integer>>> map = new HashMap<>();
        Queue<Integer> queue = new ArrayDeque<>();
        for (int[] time : times) {
            List<Pair<Integer, Integer>> list = map.getOrDefault(time[0] - 1, new ArrayList<>());
            list.add(new Pair<>(time[1] - 1, time[2]));
            map.put(time[0] - 1, list);
        }
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        queue.offer(k - 1);
        dist[k - 1] = 0;
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            for (Pair<Integer, Integer> next : map.getOrDefault(cur, new ArrayList<>())) {
                int nextDist = dist[cur] + next.getValue();
                if (nextDist < dist[next.getKey()]) {
                    dist[next.getKey()] = nextDist;
                    queue.offer(next.getKey());
                }
            }
        }
        int max = 0;
        for (int i = 0; i < n; i++) {
            max = Math.max(max, dist[i]);
        }
        return max == Integer.MAX_VALUE ? -1 : max;
    }

    // 朴素Bellman Ford (类存图) 执行 N - 1 次松弛操作即可保证所有边达到最小值
    public int networkDelayTimeBF(int[][] times, int n, int k) {
        int INF = Integer.MAX_VALUE / 2;
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[k - 1] = 0;
        //Bellman Ford 需要遍历所有的边，邻接矩阵需要 i,j两层循环，类存图只需要一层
//        for (int limit = 0; limit < k; limit++) {
//            int[] clone = dist.clone();
//            for (int i = 0; i < n; i++) {
//                for (int j = 0; j < n; j++) {
//                    dist[j] = Math.min(dist[j], clone[i] + g[i][j]);
//                }
//            }
//        }
        for (int i = 0; i < n - 1; i++) {
            // times 等价于类存图
            for (int[] edge : times) {
                int x = edge[0] - 1, y = edge[1] - 1, w = edge[2];
                if (dist[y] > dist[x] + w) {
                    dist[y] = dist[x] + w;
                }
            }
        }
        int ans = Arrays.stream(dist).max().getAsInt();
        return ans == INF ? -1 : ans;
    }

    // SPFA (邻接表)
    public int networkDelayTime(int[][] times, int n, int k) {
        int INF = Integer.MAX_VALUE / 2;
        // 建图 - 邻接表
        Map<Integer, List<Pair<Integer, Integer>>> map = new HashMap<>();
        for (int[] edge : times) {
            List<Pair<Integer, Integer>> list = map.getOrDefault(edge[0] - 1, new ArrayList<>());
            list.add(new Pair<>(edge[1] - 1, edge[2]));
            map.put(edge[0] - 1, list);
        }
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[k - 1] = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(k - 1);
        boolean[] visited = new boolean[n];
        visited[k - 1] = true;
        while (!queue.isEmpty()) {
            int x = queue.poll();
            // 该层已经遍历过，x还可能作为其他层进队
            visited[x] = false;
            // 从源点往外一层一层更新
            if (map.containsKey(x)) {
                for (Pair<Integer, Integer> p : map.get(x)) {
                    int y = p.getKey(), w = p.getValue();
                    if (dist[x] + w < dist[y]) {
                        dist[y] = dist[x] + w;
                        // 同处于一层时不再添加到queue中
                        if (visited[y]) continue;
                        visited[y] = true;
                        queue.offer(y);
                    }
                }
            }
        }
        int max = -1;
        for (int d : dist) {
            max = Math.max(max, d);
        }
        return max == INF / 2 ? -1 : max;
    }

    // 787 K站中转内最便宜的航班
    public int findCheapestPriceBF(int n, int[][] flights, int src, int dst, int k) {
        int INF = Integer.MAX_VALUE / 2;
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;
        // 最多K个点=最多K+1条边=进行K+1次松弛操作
        for (int i = 0; i < k + 1; i++) {
            int[] copy = dist.clone();
            for (int[] f : flights) {
                int x = f[0], y = f[1], w = f[2];
                if (dist[y] > copy[x] + w) {
                    dist[y] = copy[x] + w;
                }
            }
        }
        return dist[dst] == INF ? -1 : dist[dst];
    }

    int INF = 1000007;

    public int findCheapestPriceDFS(int n, int[][] flights, int src, int dst, int k) {
        // k表示经过的节点，我们转成边数（步数），这样好计算一些
        int[][] memo = new int[n][k + 2];
        int ans = dfs(flights, src, dst, k + 1, memo);
        return ans >= INF ? -1 : ans;
    }

    // 表示从 i 到 dst 的走 k 步的最小价格
    private int dfs(int[][] flights, int i, int dst, int k, int[][] memo) {
        if (k < 0) return INF;
        if (i == dst) return 0;
        if (memo[i][k] != 0) return memo[i][k];
        int min = INF;
        for (int[] flight : flights) {
            // 遍历 i 的下一个节点
            if (flight[0] == i) {
                min = Math.min(min, dfs(flights, flight[1], dst, k - 1, memo) + flight[2]);
            }
        }
        memo[i][k] = min;
        return min;
    }

    public int findCheapestPriceDP(int n, int[][] flights, int src, int dst, int K) {
        // dp[i][k]表示从i点到dst走k步的最少价格
        // dp[i][k]=min(dp[i_next][k-1] + g[i][j])
        int[][] dp = new int[n][K + 2];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], INF);
        }
        dp[dst][0] = 0;
        for (int k = 1; k <= K + 1; k++) {
            for (int[] flight : flights) {
                dp[flight[0]][k] = Math.min(dp[flight[0]][k], dp[flight[1]][k - 1] + flight[2]);
            }
        }

        int ans = IntStream.of(dp[src]).min().getAsInt();

        return ans >= INF ? -1 : ans;
    }

    //1091. 二进制矩阵中的最短路径
    public int shortestPathBinaryMatrix(int[][] grid) {
        if (grid[0][0] != 0) return -1;
        int n = grid.length;
        if (n == 1) return 1;
        int[][] directions = new int[][]{{-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{0, 0});
        boolean[][] visited = new boolean[n][n];
        visited[0][0] = true;
        int distance = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cell = queue.poll();
                for (int[] d : directions) {
                    int newX = cell[0] + d[0];
                    int newY = cell[1] + d[1];
                    if (newX >= 0 && newX < n && newY >= 0 && newY < n && !visited[newX][newY] && grid[newX][newY] == 0) {
                        if (newX == n - 1 && newY == n - 1) return distance + 1;
                        queue.offer(new int[]{newX, newY});
                        visited[newX][newY] = true;
                    }
                }
            }
            distance += 1;
        }
        return -1;
    }

    //1129. 颜色交替的最短路径
    public int[] shortestAlternatingPaths(int n, int[][] redEdges, int[][] blueEdges) {
        Map<Integer, List<Integer>>[] edges = new Map[2];
        for (int i = 0; i < 2; i++) {
            edges[i] = new HashMap<>();
        }
        for (int[] edge : redEdges) {
            addEdge(edges[0], edge);
        }
        for (int[] edge : blueEdges) {
            addEdge(edges[1], edge);
        }
        int[][] dist = new int[2][n];
        for (int[] d : dist) {
            Arrays.fill(d, Integer.MAX_VALUE);
        }
        dist[0][0] = 0;
        dist[1][0] = 0;
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{0, 0});
        queue.offer(new int[]{1, 0});
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int curColor = cell[0], cur = cell[1];
            for (int near : edges[1 - curColor].getOrDefault(cur, new ArrayList<>())) {
                if (dist[1 - curColor][near] > dist[curColor][cur] + 1) {
                    dist[1 - curColor][near] = dist[curColor][cur] + 1;
                    queue.offer(new int[]{1 - curColor, near});
                }
            }
        }
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = Math.min(dist[0][i], dist[1][i]);
            if (ans[i] == Integer.MAX_VALUE) {
                ans[i] = -1;
            }
        }
        return ans;
    }

    private void addEdge(Map<Integer, List<Integer>> map, int[] edge) {
        List<Integer> ls = map.getOrDefault(edge[0], new ArrayList<>());
        ls.add(edge[1]);
        map.put(edge[0], ls);
    }


    //2662. 前往目标的最小代价
    public int minimumCost(int[] start, int[] target, int[][] specialRoads) {
        long t = (long) target[0] << 32 | target[1];
        Map<Long, Integer> dis = new HashMap<>();
        dis.put(t, Integer.MAX_VALUE);
        dis.put((long) start[0] << 32 | start[1], 0);
        Set<Long> vis = new HashSet<>();
        for (; ; ) {
            long v = -1;
            int dv = -1;
            for (Map.Entry<Long, Integer> e : dis.entrySet())
                if (!vis.contains(e.getKey()) && (dv < 0 || e.getValue() < dv)) {
                    v = e.getKey();
                    dv = e.getValue();
                }
            if (v == t) return dv; // 到终点的最短路已确定
            vis.add(v);
            int vx = (int) (v >> 32), vy = (int) (v & Integer.MAX_VALUE);
            // 更新到终点的最短路
            dis.merge(t, dv + target[0] - vx + target[1] - vy, Math::min);
            for (int[] r : specialRoads) {
                int d = dv + Math.abs(r[0] - vx) + Math.abs(r[1] - vy) + r[4];
                long w = (long) r[2] << 32 | r[3];
                if (d < dis.getOrDefault(w, Integer.MAX_VALUE))
                    dis.put(w, d);
            }
        }
    }
    // endregion-------------------------------------------------------------------------------------------------------
    // region -------------------------------------------------------最小生成树----------------------------------------------

    // 1631 最小体力消耗路径
    int row;
    int col;

    // 朴素BFS
    public int minimumEffortPath(int[][] heights) {
        int[][] directions = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        this.row = heights.length;
        this.col = heights[0].length;
        int[][] dist = new int[row][col];
        for (int[] d : dist) {
            Arrays.fill(d, Integer.MAX_VALUE);
        }
        dist[0][0] = 0;
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{0, 0});
        while (!queue.isEmpty()) {
            int[] tmp = queue.poll();
            int x = tmp[0], y = tmp[1];
            for (int[] dire : directions) {
                int newX = x + dire[0], newY = y + dire[1];
                if (inAreaRowCol(newX, newY) && Math.max(Math.abs(heights[newX][newY] - heights[x][y]), dist[x][y]) < dist[newX][newY]) {
                    dist[newX][newY] = Math.max(Math.abs(heights[newX][newY] - heights[x][y]), dist[x][y]);
                    queue.offer(new int[]{newX, newY});
                }
            }
        }
        return dist[row - 1][col - 1];
    }

    //二分+BFS
    public int minimumEffortPathBinarySearchBFS(int[][] heights) {
        this.row = heights.length;
        this.col = heights[0].length;
        // 格子数据值范围 1-10e6,高度差范围 0-99999，通过BFS，看mid高度能否走到最后点，能继续找更小的
        int left = 0, right = 99999, ans = 0;
        while (left <= right) {
            int mid = (left + right) / 2;
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, 0});
            boolean[] seen = new boolean[row * col];
            seen[0] = true;
            while (!queue.isEmpty()) {
                int[] cell = queue.poll();
                int currentX = cell[0], currentY = cell[1];
                for (int[] direction : directions) {
                    int newX = currentX + direction[0], newY = currentY + direction[1];
                    if (inAreaRowCol(newX, newY) && !seen[getIndexRowCol(newX, newY)]
                            && Math.abs(heights[newX][newY] - heights[currentX][currentY]) <= mid) { //当前高度差mid的情况下，[x,y]能否走到[newX,newY]
                        queue.offer(new int[]{newX, newY});
                        seen[getIndexRowCol(newX, newY)] = true;
                    }
                }
            }
            if (seen[row * col - 1]) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }

    //并查集
    public int minimumEffortPathUnionFind(int[][] heights) {
        this.row = heights.length;
        this.col = heights[0].length;
        // 并查集，根据某个点与它下方/右方的高度差排序并依次合并，从小绝对值到大，找到最小能合并到最后点的高度差值 （类似二分找到最小能满足的）
        UnionFind1 unionFind = new UnionFind1(row * col);
        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int x = getIndexRowCol(i, j);
                if (i + 1 < row) {
                    int y = getIndexRowCol(i + 1, j);
                    edges.add(new int[]{x, y, Math.abs(heights[i + 1][j] - heights[i][j])});
                }
                if (j + 1 < col) {
                    int y = getIndexRowCol(i, j + 1);
                    edges.add(new int[]{x, y, Math.abs(heights[i][j + 1] - heights[i][j])});
                }
            }
        }
        edges.sort((Comparator.comparingInt(o -> o[2])));

        int start = 0, end = row * col - 1;
        for (int[] edge : edges) {
            int x = edge[0], y = edge[1], v = edge[2];
            unionFind.union(x, y);
            if (unionFind.isConnect(start, end)) {
                return v;
            }
        }
        return 0;
    }

    //Dijkstra
    public int minimumEffortPathDijkstra(int[][] heights) {
        this.row = heights.length;
        this.col = heights[0].length;
        // 存储到达idx点时的最小绝对值
        int[] dist = new int[row * col];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;
        // 根据某点的最小绝对值排序，优先取最小绝对值点组成的路径看是否能到达最后点，当前点绝对值小不代表最终会小，局部最优
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o[2]));
        queue.offer(new int[]{0, 0, 0});
        // BFS 记忆化搜索辅助数组
        boolean[] seen = new boolean[row * col];
        while (!queue.isEmpty()) {
            int[] edge = queue.poll();
            int x = edge[0], y = edge[1], v = edge[2];
            int idx = getIndexRowCol(x, y);
            if (seen[idx]) {
                continue;
            }
            if (x == row - 1 && y == col - 1) {
                break;
            }
            seen[idx] = true;

            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];
                // 走到[newX,newY]时高度差要取[x,y]和[newX,newY]中的较大值
                // dist 记录的是当前走到[newX,newY]的路径的最小高度差（从各个路径中选出来）,如果[x,y]走到[newX,newY]更小的话就取该高度差值
                if (inAreaRowCol(newX, newY) && !seen[getIndexRowCol(newX, newY)]
                        && Math.max(v, Math.abs(heights[newX][newY] - heights[x][y])) < dist[getIndexRowCol(newX, newY)]) {
                    dist[getIndexRowCol(newX, newY)] = Math.max(v, Math.abs(heights[newX][newY] - heights[x][y]));
                    queue.offer(new int[]{newX, newY, dist[getIndexRowCol(newX, newY)]});
                }
            }
        }
        return dist[row * col - 1];
    }

    //778 水位上升的泳池中游泳
    int N;
    int[][] directions = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public int swimInWaterBinarySearch(int[][] grid) {
        this.N = grid.length;
        int left = 0, right = N * N - 1;
        while (left < right) {
            //能从[0,0]到[n-1,n-1]的最小值
            int mid = (left + right) / 2;
            if (checkBFS(grid, mid) || checkDFS(grid, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private boolean checkDFS(int[][] grid, int threshold) {
        if (grid[0][0] > threshold) return false;
        boolean[][] visited = new boolean[N][N];
        visited[0][0] = true;
        return dfs(grid, 0, 0, visited, threshold);
    }

    private boolean dfs(int[][] grid, int x, int y, boolean[][] visited, int threshold) {
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            if (inAreaN(newX, newY) && !visited[newX][newY] && grid[newX][newY] <= threshold) {
                if (newX == N - 1 && newY == N - 1) return true;
                visited[newX][newY] = true;
                if (dfs(grid, newX, newY, visited, threshold)) return true;
            }
        }
        return false;
    }

    private boolean checkBFS(int[][] grid, int threshold) {
        if (grid[0][0] > threshold) return false;
        return bfs(grid, threshold);
    }

    private boolean bfs(int[][] grid, int threshold) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        boolean[][] visited = new boolean[N][N];
        visited[0][0] = true;
        while (!queue.isEmpty()) {
            int[] edge = queue.poll();
            int x = edge[0], y = edge[1];
            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];
                if (inAreaN(newX, newY) && !visited[newX][newY] && grid[newX][newY] <= threshold) {
                    if (newX == N - 1 && newY == N - 1) {
                        return true;
                    }
                    queue.offer(new int[]{newX, newY});
                    visited[newX][newY] = true;
                }
            }
        }
        return false;
    }

    public int swimInWaterUnionFind(int[][] grid) {
        this.N = grid.length;
        UnionFind1 unionFind = new UnionFind1(N * N);

        int[] index = new int[N * N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                index[grid[i][j]] = getIndexN(i, j);
            }
        }

        for (int threshold = 0; threshold < N * N; threshold++) {
            int x = index[threshold] / N;
            int y = index[threshold] % N;
            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];
                if (inAreaN(newX, newY) && grid[newX][newY] <= threshold) {
                    unionFind.union(index[threshold], getIndexN(newX, newY));
                }
                if (unionFind.isConnect(0, N * N - 1)) {
                    return threshold;
                }
            }
        }
        return -1;
    }

    public int swimInWaterUnionFind2(int[][] grid) {
        this.N = grid.length;
        UnionFind1 unionFind = new UnionFind1(N * N);

        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int x = getIndexN(i, j);
                if (i + 1 < N) {
                    int y = getIndexRowCol(i + 1, j);
                    edges.add(new int[]{x, y, Math.max(grid[i + 1][j], grid[i][j])});
                }
                if (j + 1 < N) {
                    int y = getIndexRowCol(i, j + 1);
                    edges.add(new int[]{x, y, Math.max(grid[i][j + 1], grid[i][j])});
                }
            }
        }

        edges.sort((Comparator.comparingInt(o -> o[2])));
        int start = 0, end = row * col - 1;
        for (int[] edge : edges) {
            int x = edge[0], y = edge[1], v = edge[2];
            unionFind.union(x, y);
            if (unionFind.isConnect(start, end)) {
                return v;
            }
        }
        return -1;
    }

    public int swimInWaterDijkstra(int[][] grid) {
        this.N = grid.length;
        // 堆保证了局部最优，当前grid数值是否可以到达最后点，按当前最小开始搜
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o -> grid[o[0]][o[1]]));
        queue.offer(new int[]{0, 0});
        boolean[][] visited = new boolean[N][N];
        // dist[i][j] 表示：到顶点 [i, j] 须要等待的最少的时间
        int[][] dist = new int[N][N];
        for (int[] row : dist) {
            Arrays.fill(row, N * N);
        }
        dist[0][0] = grid[0][0];
        while (!queue.isEmpty()) {
            int[] edge = queue.poll();
            int x = edge[0], y = edge[1];
            if (visited[x][y]) continue;
            if (x == N - 1 && y == N - 1) return dist[N - 1][N - 1];
            visited[x][y] = true;
            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];
                // 经x,y到newX,newY的时间是到x,y的时间和到newX newY点时间的最大值
                if (inAreaN(newX, newY) && !visited[newX][newY] && Math.max(dist[x][y], grid[newX][newY]) < dist[newX][newY]) {
                    //到newX newY点的时间
                    dist[newX][newY] = Math.max(dist[x][y], grid[newX][newY]);
                    queue.offer(new int[]{newX, newY});
                }
            }
        }
        return -1;
    }

    private boolean inAreaN(int i, int j) {
        return i >= 0 && i < N && j >= 0 && j < N;
    }

    private boolean inAreaRowCol(int i, int j) {
        return i >= 0 && i < row && j >= 0 && j < col;
    }

    private int getIndexN(int i, int j) {
        return i * N + j;
    }

    private int getIndexRowCol(int i, int j) {
        return i * col + j;
    }

    // 2577. 在网格图中访问一个格子的最少时间
    // 存在反复横跳的情况，可以从终点一刻不停的往起点找
    int[][] grid;
    int[][] visited;// int 数组visited，记录某个时刻X,Y是否被遍历过

    public int minimumTime(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        if (grid[0][1] > 1 && grid[1][0] > 1) // 无法「等待」
            return -1;

        this.grid = grid;
        visited = new int[m][n];
        int left = Math.max(grid[m - 1][n - 1], m + n - 2) - 1;
        int right = (int) 1e5 + m + n; // 开区间
        while (left + 1 < right) {
            int mid = (left + right) >>> 1;
            if (check(mid)) right = mid;
            else left = mid;
        }
        return right + (right + m + n) % 2;
    }

    private boolean check(int endTime) {
        int m = grid.length, n = grid[0].length;
        visited[m - 1][n - 1] = endTime;
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{m - 1, n - 1});
        int t = endTime - 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cell = queue.poll();
                int x = cell[0], y = cell[1];
                for (int[] d : dirs) { // 枚举周围四个格子
                    int newX = x + d[0], newY = y + d[1];
                    if (inAreaRowCol(newX, newY) && visited[x][y] != endTime && grid[x][y] <= t) {
                        if (x == 0 && y == 0) return true;
                        visited[x][y] = endTime; // 用二分的值来标记，避免重复创建 vis 数组
                        queue.add(new int[]{x, y});
                    }
                }
            }
            t--;
        }
        return false;
    }

    // endregion-----------------------------------------------------------------------------------------------------------
    //region --------------------------------------------------------拓扑排序-------------------------------------------------
    // 565 嵌套数组
    public int arrayNesting(int[] nums) {
        boolean[] visited = new boolean[nums.length];
        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            int index = i, cnt = 0;
            while (!visited[index]) {
                cnt++;
                visited[index] = true;
                index = nums[index];
            }
            max = Math.max(max, cnt);
        }
        return max;
    }

    int arrayNestingMax = 0;

    public int arrayNestingDFS(int[] nums) {
        boolean[] visited = new boolean[nums.length];
        for (int i = 0; i < nums.length; i++) {
            arrayNestingDfs(nums, visited, 0, i);
        }
        return arrayNestingMax;
    }

    private void arrayNestingDfs(int[] nums, boolean[] visited, int depth, int idx) {
        if (visited[idx]) return;
        depth++;
        arrayNestingMax = Math.max(arrayNestingMax, depth);
        visited[idx] = true;
        arrayNestingDfs(nums, visited, depth, nums[idx]);
    }

    // 323 无向图中连通分量的数目
    public int countComponentsDfs(int n, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            graph.get(u).add(v); // 无向图边 (u,v)
            graph.get(v).add(u); // 无向图边 (v,u)
        }
        int count = 0;
        for (int u = 0; u < n; u++) {
            if (!visited[u]) {
                count++; // 只要顶点 u 此时尚未被访问，说明它不再此前的链路(连通分量)中，以它为新的连通分量起点
                countComponentsDfs(u, visited, graph);
            }
        }
        return count;
    }

    private void countComponentsDfs(int u, boolean[] visited, List<List<Integer>> graph) {
        visited[u] = true;
        for (int v : graph.get(u)) {
            if (!visited[v]) countComponentsDfs(v, visited, graph);
        }
    }

    private void countComponentsBfs(int u, boolean[] visited, List<List<Integer>> graph) {
        Queue<Integer> q = new ArrayDeque<>();
        q.add(u);
        visited[u] = true;
        while (!q.isEmpty()) {
            int v = q.remove();
            for (int w : graph.get(v)) {
                if (!visited[w]) {
                    q.add(w);
                    visited[w] = true;
                }
            }
        }
    }


    // 1319 连通网络的操作次数  连通分量-1
    public int makeConnectedDfs(int n, int[][] connections) {
        if (connections.length + 1 < n) return -1;
        boolean[] visited = new boolean[n];
        List<List<Integer>> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            edges.add(new ArrayList<>());
        }
        for (int[] con : connections) {
            edges.get(con[0]).add(con[1]);
            edges.get(con[1]).add(con[0]);
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(i, visited, edges);
                ans++;
            }
        }
        return ans - 1;
    }

    private void dfs(int i, boolean[] visited, List<List<Integer>> edges) {
        visited[i] = true;
        for (int near : edges.get(i)) {
            if (!visited[near]) {
                dfs(near, visited, edges);
            }
        }
    }

    // 2360 图中最长的环
    public int longestCycle(int[] edges) {
        int n = edges.length, ans = -1;
        int[] time = new int[n];
        for (int i = 0, clock = 1; i < n; ++i) {
            if (time[i] > 0) continue;
            for (int x = i, start_time = clock; x >= 0; x = edges[x]) {
                if (time[x] > 0) { // 重复访问
                    if (time[x] >= start_time) // 找到了一个新的环
                        ans = Math.max(ans, clock - time[x]);
                    break;
                }
                time[x] = clock++;
            }
        }
        return ans;
    }

// 207 课程表
    //拓扑排序
    //你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1 。
//
// 在选修某些课程之前需要一些先修课程。 先修课程按数组 prerequisites 给出，其中 prerequisites[i] = [ai, bi] ，表
//示如果要学习课程 ai 则 必须 先学习课程 bi 。
// 例如，先修课程对 [0, 1] 表示：想要学习课程 0 ，你需要先完成课程 1 。
// 请你判断是否可能完成所有课程的学习？如果可以，返回 true ；否则，返回 false 。
//输入：numCourses = 2, prerequisites = [[1,0]]
//输出：true
//解释：总共有 2 门课程。学习课程 1 之前，你需要完成课程 0 。这是可能的。

    //输入：numCourses = 2, prerequisites = [[1,0],[0,1]]
//输出：false
//解释：总共有 2 门课程。学习课程 1 之前，你需要先完成​课程 0 ；并且学习课程 0 之前，你还应先完成课程 1 。这是不可能的。
    // 第 数组下标 门课的后置课程
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> edges = new ArrayList<>();
        int[] color = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            edges.add(new ArrayList<>());
        }
        for (int[] pair : prerequisites) {
            edges.get(pair[1]).add(pair[0]);
        }
        for (int i = 0; i < numCourses; i++) {
            if (!courseDfs(edges, color, i)) {
                return false;
            }
        }
        return true;
    }

    private boolean courseDfs(List<List<Integer>> edges, int[] color, int i) {
        if (color[i] > 0) {
            return color[i] == 2;
        }
        color[i] = 1;
        for (int j : edges.get(i)) {
            if (!courseDfs(edges, color, j)) {
                return false;
            }
        }
        color[i] = 2;
        return true;
    }

    // 210 课程表2
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> edges = new ArrayList<>();
        int[] color = new int[numCourses];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < numCourses; i++) {
            edges.add(new ArrayList<>());
        }
        for (int[] pair : prerequisites) {
            edges.get(pair[1]).add(pair[0]);
        }
        for (int i = 0; i < numCourses; i++) {
            if (!courseDfs(edges, color, i, stack)) {
                return new int[0];
            }
        }
        int[] result = new int[stack.size()];
        int i = 0;
        while (!stack.isEmpty()) {
            result[i++] = stack.pop();
        }
        return result;
    }

    private boolean courseDfs(List<List<Integer>> edges, int[] color, int i, Stack<Integer> stack) {
        if (color[i] > 0) {
            return color[i] == 2;
        }
        color[i] = 1;
        for (int j : edges.get(i)) {
            if (!courseDfs(edges, color, j, stack)) {
                return false;
            }
        }
        color[i] = 2;
        stack.add(i);
        return true;
    }

    // 1462 课程表4 dfs
    public List<Boolean> checkIfPrerequisite(int numCourses, int[][] prerequisites, int[][] queries) {
        List<List<Integer>> edges = new ArrayList<>();
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int i = 0; i < numCourses; i++) {
            edges.add(new ArrayList<>());
            map.put(i, new HashSet<>());
        }
        for (int[] pre : prerequisites) {
            edges.get(pre[1]).add(pre[0]);
        }

        Set<Integer> visited = new HashSet<>();
        for (int i = 0; i < numCourses; i++) {
            if (!visited.contains(i)) {
                dfs(i, map, edges, visited);
            }
        }
        List<Boolean> result = new ArrayList<>();
        for (int[] q : queries) {
            if (map.get(q[1]).contains(q[0])) {
                result.add(true);
            } else {
                result.add(false);
            }
        }
        return result;
    }

    private void dfs(int idx, Map<Integer, Set<Integer>> map, List<List<Integer>> edges, Set<Integer> visited) {
        visited.add(idx);
        for (int near : edges.get(idx)) {
            if (!visited.contains(near)) {
                dfs(near, map, edges, visited);
            }
            map.get(idx).add(near);
            map.get(idx).addAll(map.get(near));
        }
    }

    // 785 判断二分图 染色法
    public boolean isBipartiteBFS(int[][] graph) {
        // 定义 visited 数组，初始值为 0 表示未被访问，赋值为 1 或者 -1 表示两种不同的颜色。
        int[] visited = new int[graph.length];
        Queue<Integer> queue = new LinkedList<>();
        // 因为图中可能含有多个连通域，所以我们需要判断是否存在顶点未被访问，若存在则从它开始再进行一轮 bfs 染色。
        for (int i = 0; i < graph.length; i++) {
            if (visited[i] != 0) {
                continue;
            }
            // 每出队一个顶点，将其所有邻接点染成相反的颜色并入队。
            queue.offer(i);
            visited[i] = 1;
            while (!queue.isEmpty()) {
                int v = queue.poll();
                for (int w : graph[v]) {
                    // 如果当前顶点的某个邻接点已经被染过色了，且颜色和当前顶点相同，说明此无向图无法被正确染色，返回 false。
                    if (visited[w] == visited[v]) {
                        return false;
                    }
                    if (visited[w] == 0) {
                        visited[w] = -visited[v];
                        queue.offer(w);
                    }
                }
            }
        }
        return true;
    }

    public boolean isBipartiteDFS(int[][] graph) {
        int n = graph.length;
        int[] visited = new int[n];
        for (int i = 0; i < n; i++) {
            if (visited[i] == 0 && !dfs(i, 1, visited, graph)) {
                return false;
            }
        }
        return true;
    }

    private boolean dfs(int v, int color, int[] visited, int[][] graph) {
        if (visited[v] != 0) {
            return visited[v] == color;
        }
        visited[v] = color;
        for (int w : graph[v]) {
            if (!dfs(w, -color, visited, graph)) {
                return false;
            }
        }
        return true;
    }

    public boolean isBipartiteUnionFind(int[][] graph) {
        // 初始化并查集
        UnionFind1 uf = new UnionFind1(graph.length);
        // 遍历每个顶点，将当前顶点的所有邻接点进行合并
        for (int i = 0; i < graph.length; i++) {
            int[] adjs = graph[i];
            for (int w : adjs) {
                // 若某个邻接点与当前顶点已经在一个集合中了，说明不是二分图，返回 false。
                if (uf.isConnect(i, w)) {
                    return false;
                }
                uf.union(adjs[0], w);
            }
        }
        return true;
    }


    // 886 可能的二分法
    public boolean possibleBipartition(int n, int[][] dislikes) {
        int[] color = new int[n + 1];
        List<Integer>[] g = new List[n + 1];
        for (int i = 0; i <= n; ++i) {
            g[i] = new ArrayList<>();
        }
        for (int[] p : dislikes) {
            g[p[0]].add(p[1]);
            g[p[1]].add(p[0]);
        }
        for (int i = 1; i <= n; ++i) {
            if (color[i] == 0 && !dfs(i, 1, color, g)) {
                return false;
            }
        }
        return true;
    }

    public boolean dfs(int curnode, int nowcolor, int[] color, List<Integer>[] g) {
        color[curnode] = nowcolor;
        for (int nextnode : g[curnode]) {
            if (color[nextnode] != 0 && color[nextnode] == color[curnode]) {
                return false;
            }
            if (color[nextnode] == 0 && !dfs(nextnode, 3 ^ nowcolor, color, g)) {
                return false;
            }
        }
        return true;
    }

    // 802 找到最终的安全状态 3色标记法
    //若起始节点位于一个环内，或者能到达一个环，则该节点不是安全的。否则，该节点是安全的。
    public List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        int[] color = new int[n];
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (safe(graph, i, color)) {
                result.add(i);
            }
        }
        return result;
    }

    private boolean safe(int[][] graph, int i, int[] color) {
        if (color[i] > 0) return color[i] == 2;
        color[i] = 1;
        for (int x : graph[i]) {
            if (!safe(graph, x, color)) return false;
        }
        color[i] = 2;
        return true;
    }

    // 反向图+拓扑排序
    public List<Integer> eventualSafeNodesReverseGraph(int[][] graph) {
        int n = graph.length;
        List<Integer> result = new ArrayList<>();
        //构造反向图
        List<List<Integer>> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            edges.add(new ArrayList<>());
        }
        //反向图某点入度 = 某点的出度
        int[] inDeg = new int[n];
        for (int x = 0; x < n; x++) {
            for (int y : graph[x]) {
                edges.get(y).add(x);
            }
            inDeg[x] = graph[x].length;
        }
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDeg[i] == 0) {
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            int y = queue.poll();
            for (int x : edges.get(y)) {
                if (--inDeg[x] == 0) {
                    queue.offer(x);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (inDeg[i] == 0) {
                result.add(i);
            }
        }
        return result;
    }

    // 851 喧闹和富有
    public int[] loudAndRich(int[][] richer, int[] quiet) {
        int n = quiet.length;
        List<List<Integer>> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            edges.add(new ArrayList<>());
        }
        for (int[] pair : richer) {
            edges.get(pair[1]).add(pair[0]);
        }
        int[] ans = new int[n];
        int[] visited = new int[n];
        for (int i = 0; i < n; i++) {
            dfs(edges, quiet, ans, visited, i);
        }
        return ans;
    }

    private void dfs(List<List<Integer>> edges, int[] quiet, int[] ans, int[] visited, int i) {
        if (visited[i] == 1) {
            return;
        }
        visited[i] = 1;
        ans[i] = i;
        for (int j : edges.get(i)) {
            dfs(edges, quiet, ans, visited, j);
            if (quiet[ans[j]] < quiet[ans[i]]) {
                ans[i] = ans[j];
            }
        }
    }

    public int[] loudAndRichTuopu(int[][] richer, int[] quiet) {
        int n = quiet.length;
        List<List<Integer>> edges = new ArrayList<>();
        int[] inDeg = new int[n];

        for (int i = 0; i < n; i++) {
            edges.add(new ArrayList<>());
        }
        for (int[] pair : richer) {
            edges.get(pair[0]).add(pair[1]);
            inDeg[pair[1]]++;
        }
        Queue<Integer> queue = new ArrayDeque<>();
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            if (inDeg[i] == 0) {
                queue.offer(i);
            }
            ans[i] = i;
        }
        while (!queue.isEmpty()) {
            int i = queue.poll();
            //反向图，比i穷的j
            for (int j : edges.get(i)) {
                //更新穷j的值
                if (quiet[ans[i]] < quiet[ans[j]]) {
                    ans[j] = ans[i];
                }
                if (--inDeg[j] == 0) {
                    queue.offer(j);
                }
            }
        }
        return ans;
    }

    // 329 offer 2 112 最长递增路径 拓扑排序   记忆化搜索搜longestIncreasingPath
    public int rows, columns;

    public int longestIncreasingPath(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        rows = matrix.length;
        columns = matrix[0].length;
        int[][] outdegrees = new int[rows][columns];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                for (int[] dir : dirs) {
                    int newRow = i + dir[0], newColumn = j + dir[1];
                    if (newRow >= 0 && newRow < rows && newColumn >= 0 && newColumn < columns && matrix[newRow][newColumn] > matrix[i][j]) {
                        ++outdegrees[i][j];
                    }
                }
            }
        }
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                // 出度为0的点 即最大的值的点
                if (outdegrees[i][j] == 0) {
                    queue.offer(new int[]{i, j});
                }
            }
        }
        int ans = 0;
        while (!queue.isEmpty()) {
            ++ans;
            int size = queue.size();
            for (int i = 0; i < size; ++i) {
                int[] cell = queue.poll();
                int row = cell[0], column = cell[1];
                for (int[] dir : dirs) {
                    int newRow = row + dir[0], newColumn = column + dir[1];
                    if (newRow >= 0 && newRow < rows && newColumn >= 0 && newColumn < columns && matrix[newRow][newColumn] < matrix[row][column]) {
                        // 注：这里是新row和新col的出度-1
                        --outdegrees[newRow][newColumn];
                        if (outdegrees[newRow][newColumn] == 0) {
                            queue.offer(new int[]{newRow, newColumn});
                        }
                    }
                }
            }
        }
        return ans;
    }

    // 269 外星词典
    // offer 114 外星文字典
    public String alienOrder(String[] words) {
        int n = words.length;
        Map<Character, List<Character>> edges = new HashMap<>();
        Map<Character, Integer> state = new HashMap<>();
        for (String word : words) {
            for (char c : word.toCharArray()) {
                edges.putIfAbsent(c, new ArrayList<>());
            }
        }
        for (int i = n - 1; i > 0; i--) {
            if (!addEdges(words[i - 1], words[i], edges)) return "";
        }
        Stack<Character> stack = new Stack<>();
        for (char c : edges.keySet()) {
            if (!dfs(edges, state, c, stack)) return "";
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }
        return sb.toString();
    }

    private boolean dfs(Map<Character, List<Character>> edges, Map<Character, Integer> state, char c, Stack<Character> stack) {
        if (state.containsKey(c)) {
            return state.get(c) == 2;
        }
        state.put(c, 1);
        for (char next : edges.get(c)) {
            if (!dfs(edges, state, next, stack)) return false;
        }
        state.put(c, 2);
        stack.push(c);
        return true;
    }

    private boolean addEdges(String before, String after, Map<Character, List<Character>> edges) {
        int idx = 0;
        int len = Math.min(before.length(), after.length());
        while (idx < len) {
            char c1 = before.charAt(idx), c2 = after.charAt(idx);
            if (c1 != c2) {
                edges.get(c1).add(c2);
                break;
            }
            idx++;
        }
        return idx != len || before.length() <= after.length();
    }

    // offer 034 外星语言是否排序
    public boolean isAlienSorted(String[] words, String order) {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < order.length(); i++) {
            map.put(order.charAt(i), i);
        }
        for (int i = 0; i < words.length - 1; i++) {
            if (!check(words[i], words[i + 1], map)) return false;
        }
        return true;
    }

    private boolean check(String s1, String s2, Map<Character, Integer> map) {
        int len = Math.min(s1.length(), s2.length());
        int idx = 0;
        while (idx < len) {
            if (map.get(s1.charAt(idx)) < map.get(s2.charAt(idx))) return true;
            if (map.get(s1.charAt(idx)) > map.get(s2.charAt(idx))) return false;
            idx++;
        }
        return s1.length() <= s2.length();
    }

    // offer 115 重建序列
    public boolean sequenceReconstruction(int[] nums, int[][] sequences) {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        // 入度,数值1-n,用下标存储数值
        int[] degrees = new int[nums.length + 1];
        for (int[] seq : sequences) {
            for (int i = 1; i < seq.length; i++) {
                Set<Integer> set = map.getOrDefault(seq[i - 1], new HashSet<>());
                set.add(seq[i]);
                map.put(seq[i - 1], set);
                degrees[seq[i]]++;
            }
        }
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 1; i <= nums.length; i++) {
            if (degrees[i] == 0) {
                deque.offerLast(i);
            }
        }
        while (!deque.isEmpty()) {
            if (deque.size() > 1) return false;
            for (int next : map.getOrDefault(deque.poll(), new HashSet<>())) {
                degrees[next]--;
                if (degrees[next] == 0) deque.offerLast(next);
            }
        }
        return true;
    }

    //1632. 矩阵转换后的秩 并查集+拓扑排序
    public int[][] matrixRankTransform(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        UnionFind1632 uf = new UnionFind1632(m, n);
        for (int i = 0; i < m; i++) {
            Map<Integer, List<int[]>> num2indexList = new HashMap<>();
            for (int j = 0; j < n; j++) {
                int num = matrix[i][j];
                num2indexList.putIfAbsent(num, new ArrayList<>());
                num2indexList.get(num).add(new int[]{i, j});
            }
            for (List<int[]> indexList : num2indexList.values()) {
                int[] arr1 = indexList.get(0);
                int i1 = arr1[0], j1 = arr1[1];
                for (int k = 1; k < indexList.size(); k++) {
                    int[] arr2 = indexList.get(k);
                    int i2 = arr2[0], j2 = arr2[1];
                    uf.union(i1, j1, i2, j2);
                }
            }
        }
        for (int j = 0; j < n; j++) {
            Map<Integer, List<int[]>> num2indexList = new HashMap<>();
            for (int i = 0; i < m; i++) {
                int num = matrix[i][j];
                num2indexList.putIfAbsent(num, new ArrayList<>());
                num2indexList.get(num).add(new int[]{i, j});
            }
            for (List<int[]> indexList : num2indexList.values()) {
                int[] arr1 = indexList.get(0);
                int i1 = arr1[0], j1 = arr1[1];
                for (int k = 1; k < indexList.size(); k++) {
                    int[] arr2 = indexList.get(k);
                    int i2 = arr2[0], j2 = arr2[1];
                    uf.union(i1, j1, i2, j2);
                }
            }
        }

        int[][] degree = new int[m][n];
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int i = 0; i < m; i++) {
            Map<Integer, int[]> num2index = new HashMap<>();
            for (int j = 0; j < n; j++) {
                int num = matrix[i][j];
                num2index.put(num, new int[]{i, j});
            }
            List<Integer> sortedArray = new ArrayList<>(num2index.keySet());
            Collections.sort(sortedArray);
            for (int k = 1; k < sortedArray.size(); k++) {
                int[] prev = num2index.get(sortedArray.get(k - 1));
                int[] curr = num2index.get(sortedArray.get(k));
                int i1 = prev[0], j1 = prev[1], i2 = curr[0], j2 = curr[1];
                int[] root1 = uf.find(i1, j1);
                int[] root2 = uf.find(i2, j2);
                int ri1 = root1[0], rj1 = root1[1], ri2 = root2[0], rj2 = root2[1];
                degree[ri2][rj2]++;
                adj.putIfAbsent(ri1 * n + rj1, new ArrayList<>());
                adj.get(ri1 * n + rj1).add(new int[]{ri2, rj2});
            }
        }
        for (int j = 0; j < n; j++) {
            Map<Integer, int[]> num2index = new HashMap<>();
            for (int i = 0; i < m; i++) {
                int num = matrix[i][j];
                num2index.put(num, new int[]{i, j});
            }
            List<Integer> sortedArray = new ArrayList<>(num2index.keySet());
            Collections.sort(sortedArray);
            for (int k = 1; k < sortedArray.size(); k++) {
                int[] prev = num2index.get(sortedArray.get(k - 1));
                int[] curr = num2index.get(sortedArray.get(k));
                int i1 = prev[0], j1 = prev[1], i2 = curr[0], j2 = curr[1];
                int[] root1 = uf.find(i1, j1);
                int[] root2 = uf.find(i2, j2);
                int ri1 = root1[0], rj1 = root1[1], ri2 = root2[0], rj2 = root2[1];
                degree[ri2][rj2]++;
                adj.putIfAbsent(ri1 * n + rj1, new ArrayList<int[]>());
                adj.get(ri1 * n + rj1).add(new int[]{ri2, rj2});
            }
        }

        Set<Integer> rootSet = new HashSet<Integer>();
        int[][] ranks = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int[] rootArr = uf.find(i, j);
                int ri = rootArr[0], rj = rootArr[1];
                rootSet.add(ri * n + rj);
                ranks[ri][rj] = 1;
            }
        }
        Queue<int[]> queue = new ArrayDeque<>();
        for (int val : rootSet) {
            if (degree[val / n][val % n] == 0) {
                queue.offer(new int[]{val / n, val % n});
            }
        }
        while (!queue.isEmpty()) {
            int[] arr = queue.poll();
            int i = arr[0], j = arr[1];
            for (int[] adjArr : adj.getOrDefault(i * n + j, new ArrayList<>())) {
                int ui = adjArr[0], uj = adjArr[1];
                degree[ui][uj]--;
                if (degree[ui][uj] == 0) {
                    queue.offer(new int[]{ui, uj});
                }
                ranks[ui][uj] = Math.max(ranks[ui][uj], ranks[i][j] + 1);
            }
        }
        int[][] res = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int[] rootArr = uf.find(i, j);
                int ri = rootArr[0], rj = rootArr[1];
                res[i][j] = ranks[ri][rj];
            }
        }
        return res;
    }


    static class UnionFind1632 {
        int m, n;
        int[][][] root;
        int[][] size;

        public UnionFind1632(int m, int n) {
            this.m = m;
            this.n = n;
            this.root = new int[m][n][2];
            this.size = new int[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    root[i][j][0] = i;
                    root[i][j][1] = j;
                    size[i][j] = 1;
                }
            }
        }

        public int[] find(int i, int j) {
            int[] rootArr = root[i][j];
            int ri = rootArr[0], rj = rootArr[1];
            if (ri == i && rj == j) {
                return rootArr;
            }
            return find(ri, rj);
        }

        public void union(int i1, int j1, int i2, int j2) {
            int[] rootArr1 = find(i1, j1);
            int[] rootArr2 = find(i2, j2);
            int ri1 = rootArr1[0], rj1 = rootArr1[1];
            int ri2 = rootArr2[0], rj2 = rootArr2[1];
            if (ri1 != ri2 || rj1 != rj2) {
                if (size[ri1][rj1] >= size[ri2][rj2]) {
                    root[ri2][rj2][0] = ri1;
                    root[ri2][rj2][1] = rj1;
                    size[ri1][rj1] += size[ri2][rj2];
                } else {
                    root[ri1][rj1][0] = ri2;
                    root[ri1][rj1][1] = rj2;
                    size[ri2][rj2] += size[ri1][rj1];
                }
            }
        }
    }

    //endregion------------------------------------------------------------------------------------------------------
    //region------------------------------------------------欧拉回路------------------------------------------
    //753. 破解保险箱
    // n位数，每位范围是k，那么一共有k^n个数
    // 先把n-1位数当作点，共有k^(n-1)个点，让每个点有k个出边，k个入边，边上连着[0,k)的一位数，
    // eg：点a1a2..an-1的第 x 条出边就连向数a2..an-1ax 这样我们从一个节点顺着第 x 条边走到另一个节点，就相当于输入了数字 x
    //在某个节点对应的数的末尾放上它的某条出边的编号，就形成了一个 n 位数，并且每个节点都能用这样的方式形成 k 个 n 位数。
    //这样共计有 k^(n-1)*k  个 n 位数，恰好就是所有可能的密码。

    //欧拉回路: 即可以从任意一个节点开始，一次性不重复地走完所有的边且回到该节点
    //我们可以用 Hierholzer  算法找出这条欧拉回路：
    //设起始节点对应的数为 u，欧拉回路中每条边的编号为 x1x2x3...  ，那么最终的字符串即为ux1x2x3...
    //Hierholzer  算法如下：
    //我们从节点 u 开始，任意地经过还未经过的边，直到我们「无路可走」。此时我们一定回到了节点 u，这是因为所有节点的入度和出度都相等。
    //回到节点 u 之后，我们得到了一条从 u 开始到 u 结束的回路，这条回路上仍然有些节点有未经过的出边。我么从某个这样的节点 v 开始，继续得到一条从 v 开始到 v 结束的回路，再嵌入之前的回路中，即
    //u...v...u
    //变为
    //u...v...v...u
    //以此类推，直到没有节点有未经过的出边，此时我们就找到了一条欧拉回路。
    Set<Integer> seen = new HashSet<Integer>();
    StringBuffer ans753 = new StringBuffer();
    int highest;
    int k;

    public String crackSafe(int n, int k) {
        highest = (int) Math.pow(10, n - 1);
        this.k = k;
        dfs(0);
        for (int i = 1; i < n; i++) {
            ans753.append('0');
        }
        return ans753.toString();
    }

    public void dfs(int node) {
        for (int x = 0; x < k; ++x) {
            int nei = node * 10 + x;
            if (!seen.contains(nei)) {
                seen.add(nei);
                dfs(nei % highest);
                ans753.append(x);
            }
        }
    }

    //332. 重新安排行程
    Map<String, PriorityQueue<String>> map332 = new HashMap<>();
    List<String> itinerary = new LinkedList<>();

    public List<String> findItinerary(List<List<String>> tickets) {
        for (List<String> ticket : tickets) {
            String src = ticket.get(0), dst = ticket.get(1);
            if (!map332.containsKey(src)) {
                map332.put(src, new PriorityQueue<>());
            }
            map332.get(src).offer(dst);
        }
        dfs("JFK");
        Collections.reverse(itinerary);
        return itinerary;
    }

    public void dfs(String curr) {
        while (map332.containsKey(curr) && map332.get(curr).size() > 0) {
            String tmp = map332.get(curr).poll();
            dfs(tmp);
        }
        itinerary.add(curr);
    }

    //endregion
    //region ------------------------------------------------------字典树---------------------------------------------------
    // 720 词典中最长的单词
    //返回 words 中最长的一个单词，该单词是由 words 词典中其他单词逐步添加一个字母组成
    public String longestWord720(String[] words) {
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(word);
        }
        String longest = "";
        for (String word : words) {
            if (trie.hasPrefix(word)) {
                if (word.length() > longest.length() || (word.length() == longest.length() && word.compareTo(longest) < 0)) {
                    longest = word;
                }
            }
        }
        return longest;
    }

    //17.15 给定一组单词words，编写一个程序，找出其中的最长单词，且该单词由这组单词中的其他单词组合而成
    public String longestWord(String[] words) {
        String res = "";
        List<String> wordList = Arrays.asList(words);
        //按字符长度从大到小排列，相同长度的字符，按字典序正序排列，这样第一个返回的是满足题意要求的字符
        wordList.sort((a, b) -> a.length() == b.length() ? a.compareTo(b) : b.length() - a.length());
        for (String target : wordList) {
            if (longestWordDfs(target, 0, wordList)) return target;
        }
        return res;

    }

    /**
     * @param target   待处理的目标单词
     * @param start    该目标单词目前处理到的的下标索引，初始化的时候是0，从该单词的第一个字符开始
     * @param wordList 包含这个目标单词的所有单词的列表
     * @return
     */
    private boolean longestWordDfs(String target, int start, List<String> wordList) {
        if (start == target.length()) return true;//当下标到达字符的结尾时，说明这个是满足条件的
        for (int end = start; end < target.length(); end++) {
            //下面这一行是为了排除目标单词target本身，题意要求由其他的至少两个单词组成
            //当遍历的时候只有一轮，一直没找到其他的目标单词，这个目标单词做为一个候选词，需要被排除掉
            if (end - start + 1 == target.length()) continue;
            String prev = target.substring(start, end + 1);//切出来[start,end]之间的字符作为一个候选单词进入下一轮递归
            //这个切出来的单词是在单词列表&&剩下的单词也在单词列表（可能需要再切）
            if (wordList.contains(prev) && longestWordDfs(target, end + 1, wordList)) return true;
        }
        return false;
    }

    public String longestWordTrie(String[] words) {
        Trie root = new Trie();
        String res = "";
        List<String> wordList = Arrays.asList(words);
        //排序好，第一个返回的即是结果
        wordList.sort((a, b) -> a.length() == b.length() ? a.compareTo(b) : b.length() - a.length());
        //构造字典树
        for (String word : wordList) root.insert(word);
        for (String word : wordList) {
            Trie cur = root;
            int n = word.length();
            for (int i = 0; i < n; i++) {
                char c = word.charAt(i);
                //排除掉自己组成自己，当前遍历到的字符是个单词，且剩余部分可以再次被切分
                if (i < n - 1 && cur.children[c - 'a'].isEnd && canSplitToWord(word.substring(i + 1), root)) {
                    return word;
                }
                cur = cur.children[c - 'a'];
            }
        }
        return res;
    }

    /**
     * 当前的单词可以被切分，在wordList中找到
     *
     * @param remain
     * @return
     */
    private boolean canSplitToWord(String remain, Trie root) {
        //当没有可以切分的了 返回True
        if (remain.equals("")) return true;
        Trie cur = root;
        for (int i = 0; i < remain.length(); i++) {
            char c = remain.charAt(i);//拿到当前的字符
            if (cur.children[c - 'a'] == null) return false;//这个节点找不到
            //当前的节点是个单词，且剩余部分可以再次被切分
            if (cur.children[c - 'a'].isEnd && canSplitToWord(remain.substring(i + 1), root)) {
                return true;
            }
            cur = cur.children[c - 'a'];
        }
        return false;
    }

    // 648单词替换
    public String replaceWords(List<String> dictionary, String sentence) {
        Trie648 trie = new Trie648();
        for (String s : dictionary) {
            trie.insert(s);
        }
        String[] words = sentence.split(" ");
        List<String> resultList = new ArrayList<>();
        for (String word : words) {
            resultList.add(trie.findRoot(word));
        }
        return String.join(" ", resultList);
    }

    class Trie648 {
        Trie648[] children;
        boolean isEnd;

        public Trie648() {
            children = new Trie648[26];
        }

        public void insert(String word) {
            Trie648 cur = this;
            for (int i = 0; i < word.length(); i++) {
                if (cur.children[word.charAt(i) - 'a'] == null) {
                    cur.children[word.charAt(i) - 'a'] = new Trie648();
                }
                cur = cur.children[word.charAt(i) - 'a'];
            }
            cur.isEnd = true;
        }

        public String findRoot(String word) {
            Trie648 cur = this;
            for (int i = 0; i < word.length(); i++) {
                if (cur.children[word.charAt(i) - 'a'] == null) return word;
                cur = cur.children[word.charAt(i) - 'a'];
                if (cur.isEnd) return word.substring(0, i + 1);
            }
            return word;
        }
    }

    // 2416 字符串的前缀分数和
    public int[] sumPrefixScores(String[] words) {
        int n = words.length;
        int[] answers = new int[n];
        Trie2416 trie = new Trie2416();
        for (String word : words) {
            trie.insert(word);
        }
        for (int i = 0; i < words.length; i++) {
            answers[i] = trie.totalCnt(words[i]);
        }
        return answers;
    }

    class Trie2416 {
        Trie2416[] children;
        boolean isEnd;
        int cnt;

        public Trie2416() {
            children = new Trie2416[26];
            isEnd = false;
            cnt = 0;
        }

        public void insert(String word) {
            Trie2416 trie = this;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (trie.children[idx] == null) {
                    trie.children[idx] = new Trie2416();
                }
                trie = trie.children[idx];
                trie.cnt++;
            }
            trie.isEnd = true;
        }

        public int totalCnt(String word) {
            Trie2416 trie = this;
            int total = 0;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (trie.children[idx] == null) {
                    return total;
                }
                trie = trie.children[idx];
                total += trie.cnt;
            }
            return total;
        }
    }

    //面试题 17.17. 多次搜索
    public int[][] multiSearch(String big, String[] smalls) {
        MultiSearchTrie trie = new MultiSearchTrie();
        for (String small : smalls) {
            trie.insert(small);
        }
        Map<String, List<Integer>> hits = new HashMap<>();
        for (int i = 0; i < big.length(); i++) {
            List<String> matches = trie.search(big.substring(i));
            for (String match : matches) {
                List<Integer> indexes = hits.getOrDefault(match, new ArrayList<>());
                indexes.add(i);
                hits.put(match, indexes);
            }
        }
        int[][] ans = new int[smalls.length][];
        for (int i = 0; i < smalls.length; i++) {
            String small = smalls[i];
            List<Integer> indexed = hits.getOrDefault(small, new ArrayList<>());
            if (indexed.size() == 0) {
                ans[i] = new int[0];
            }
            ans[i] = indexed.stream().mapToInt(p -> p).toArray();
        }
        return ans;

    }

    class MultiSearchTrie {
        MultiSearchTrie[] children;
        String word;

        public MultiSearchTrie() {
            children = new MultiSearchTrie[26];
        }

        public void insert(String word) {
            MultiSearchTrie node = this;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    node.children[c - 'a'] = new MultiSearchTrie();
                }
                node = node.children[c - 'a'];
            }
            node.word = word;
        }

        public List<String> search(String word) {
            MultiSearchTrie node = this;
            List<String> ans = new ArrayList<>();
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    break;
                }
                node = node.children[c - 'a'];
                if (node.word != null) {
                    ans.add(node.word);
                }
            }
            return ans;
        }
    }

    // 472 连接词
    public List<String> findAllConcatenatedWordsInADict(String[] words) {
        Trie trie = new Trie();
        Arrays.sort(words, Comparator.comparingInt(String::length));
        List<String> ans = new ArrayList<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                if (findAllConcatenatedWordsInADictDfs(word, 0, trie)) {
                    ans.add(word);
                } else {
                    trie.insert(word);
                }
            }
        }
        return ans;
    }

    private boolean findAllConcatenatedWordsInADictDfs(String word, int idx, Trie root) {
        if (idx == word.length()) return true;
        Trie node = root;
        for (int i = idx; i < word.length(); i++) {
            if (node.children[word.charAt(i) - 'a'] == null) return false;
            node = node.children[word.charAt(i) - 'a'];
            if (node.isEnd && findAllConcatenatedWordsInADictDfs(word, i + 1, root)) return true;
        }
        return false;
    }

    // 820 单词的压缩编码
    public int minimumLengthEncoding(String[] words) {
        TrieNode820 trie = new TrieNode820();
        Map<TrieNode820, Integer> nodes = new HashMap<>();

        for (int i = 0; i < words.length; ++i) {
            String word = words[i];
            TrieNode820 cur = trie;
            for (int j = word.length() - 1; j >= 0; --j) {
                cur = cur.get(word.charAt(j));
            }
            nodes.put(cur, i);
        }

        int ans = 0;
        for (TrieNode820 node : nodes.keySet()) {
            if (node.count == 0) {
                ans += words[nodes.get(node)].length() + 1;
            }
        }
        return ans;

    }

    class TrieNode820 {
        TrieNode820[] children;
        int count;

        TrieNode820() {
            children = new TrieNode820[26];
            count = 0;
        }

        public TrieNode820 get(char c) {
            if (children[c - 'a'] == null) {
                children[c - 'a'] = new TrieNode820();
                count++;
            }
            return children[c - 'a'];
        }
    }

    //421 数组中两个数的最大异或值
//    思路分析：这种题就不要暴力法，指名道姓说要O(n)。根据提示需要使用建树。
//    首先我们需要知道，二进制高位为1会大于低位的所有和，比如"11111111"最高位代表的"1"按权展开为128，
//    而后面的“1111111”按权展开的和也只是127。所以进行异或时应该尽量选择高位异或结果为“1”的。
//    第一步：遍历数组，我们按照二进制[31,30,…,1, 0]各位的状态进行建树，left放置0，right放置1。
//    比如某个int型数的二进制是"0110110…"，我们需要将其放置到[left,right,right,left,right,right,left…]。
//    第二步：遍历数组，按照贪心策略，尽量维持当前选择的方向能保证当前能位异或结果为1。
    // 最高位的二进制位编号为 30
    static final int HIGH_BIT = 30;

    public int findMaximumXOR(int[] nums) {
        int x = 0;
        for (int k = HIGH_BIT; k >= 0; --k) {
            Set<Integer> seen = new HashSet<>();
            // 将所有的 pre^k(a_j) 放入哈希表中
            for (int num : nums) {
                // 如果只想保留从最高位开始到第 k 个二进制位为止的部分
                // 只需将其右移 k 位
                seen.add(num >> k);
            }

            // 目前 x 包含从最高位开始到第 k+1 个二进制位为止的部分
            // 我们将 x 的第 k 个二进制位置为 1，即为 x = x*2+1
            int xNext = x * 2 + 1;
            boolean found = false;

            // 枚举 i
            for (int num : nums) {
                if (seen.contains(xNext ^ (num >> k))) {
                    found = true;
                    break;
                }
            }

            if (found) {
                x = xNext;
            } else {
                // 如果没有找到满足等式的 a_i 和 a_j，那么 x 的第 k 个二进制位只能为 0
                // 即为 x = x*2
                x = xNext - 1;
            }
        }
        return x;
    }

    public int findMaximumXORTrie(int[] nums) {
        int n = nums.length;
        int x = 0;
        BinaryTrie root = new BinaryTrie();
        for (int i = 1; i < n; ++i) {
            // 将 nums[i-1] 放入字典树，此时 nums[0 .. i-1] 都在字典树中
            root.insert(nums[i - 1]);
            // 将 nums[i] 看作 ai，找出最大的 x 更新答案
            x = Math.max(x, root.getMaxXor(nums[i]));
        }
        return x;
    }

    //1707 与数组中元素最大的异或值
    public int[] maximizeXor(int[] nums, int[][] queries) {
        Arrays.sort(nums);
        int numQ = queries.length;
        int[][] newQueries = new int[numQ][3];
        for (int i = 0; i < numQ; ++i) {
            newQueries[i][0] = queries[i][0];
            newQueries[i][1] = queries[i][1];
            newQueries[i][2] = i;
        }
        Arrays.sort(newQueries, Comparator.comparingInt(query -> query[1]));

        int[] ans = new int[numQ];
        BinaryTrie trie = new BinaryTrie();
        int idx = 0, n = nums.length;
        for (int[] query : newQueries) {
            int x = query[0], m = query[1], qid = query[2];
            while (idx < n && nums[idx] <= m) {
                trie.insert(nums[idx]);
                ++idx;
            }
            if (idx == 0) { // 字典树为空
                ans[qid] = -1;
            } else {
                ans[qid] = trie.getMaxXor(x);
            }
        }
        return ans;
    }

    class BinaryTrie {
        static final int L = 30;
        BinaryTrie[] children = new BinaryTrie[2];

        public void insert(int val) {
            BinaryTrie node = this;
            for (int i = L; i >= 0; --i) {
                int bit = (val >> i) & 1;
                if (node.children[bit] == null) {
                    node.children[bit] = new BinaryTrie();
                }
                node = node.children[bit];
            }
        }

        public int getMaxXor(int val) {
            int ans = 0;
            BinaryTrie node = this;
            for (int i = L; i >= 0; --i) {
                int bit = (val >> i) & 1;
                if (node.children[bit ^ 1] != null) {
                    ans |= 1 << i;
                    bit ^= 1;
                }
                node = node.children[bit];
            }
            return ans;
        }
    }

    class T9WordsTrie {
        T9WordsTrie[] children;
        boolean isEnd;
        char val;

        public T9WordsTrie() {
            children = new T9WordsTrie[26];
        }

        public void insert(String word, Map<Character, Character> map) {
            T9WordsTrie node = this;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    node.children[c - 'a'] = new T9WordsTrie();
                }
                node = node.children[c - 'a'];
                node.val = map.get(c);
            }
            node.isEnd = true;
        }

        public boolean search(String word, String num, Map<Character, Character> map) {
            int numIdx = 0;
            T9WordsTrie node = this;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null || node.children[c - 'a'].val != num.charAt(numIdx))
                    return false;
                node = node.children[c - 'a'];
                numIdx++;
            }
            return node.isEnd;
        }

    }

    public List<String> getValidT9Words(String num, String[] words) {

        Map<Character, Character> map = new HashMap<>();
        map.put('a', '2');
        map.put('b', '2');
        map.put('c', '2');
        map.put('d', '3');
        map.put('e', '3');
        map.put('f', '3');
        map.put('g', '4');
        map.put('h', '4');
        map.put('i', '4');
        map.put('j', '5');
        map.put('k', '5');
        map.put('l', '5');
        map.put('m', '6');
        map.put('n', '6');
        map.put('o', '6');
        map.put('p', '7');
        map.put('q', '7');
        map.put('r', '7');
        map.put('s', '7');
        map.put('t', '8');
        map.put('u', '8');
        map.put('v', '8');
        map.put('w', '9');
        map.put('x', '9');
        map.put('y', '9');
        map.put('z', '9');

        List<String> result = new ArrayList<>();
        T9WordsTrie trie = new T9WordsTrie();
        for (String word : words) {
            trie.insert(word, map);
        }
        for (String word : words) {
            if (trie.search(word, num, map)) {
                result.add(word);
            }
        }
        return result;
    }
    // endregion--------------------------------------------------------------------------------------------------
}
