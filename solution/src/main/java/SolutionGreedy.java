import java.util.*;

public class SolutionGreedy {
    //region---------------------------------------------------greedy 贪心 ----------------------------------------
    // 11 盛水最多的容器
    public int maxArea(int[] height) {
        int l = 0, r = height.length - 1;
        int area = Integer.MIN_VALUE;
        while (l < r) {
            int tempArea = Math.min(height[r], height[l]) * (r - l);
            if (area < tempArea) area = tempArea;
            if (height[r] < height[l]) {
                r--;
            } else {
                l++;
            }
        }
        return area;
    }


    public int maxArea2(int[] height) {
        int l = 0, r = height.length - 1;
        int max = Integer.MIN_VALUE;
        while (l < r) {
            int product = r - l;
            product = height[r] <= height[l] ? product * height[r--] : product * height[l++];
            max = Math.max(product, max);
        }
        return max;
    }

    // 45 跳跃游戏
    // 输入: [2,3,1,1,4]
//输出: 2
    public int jump(int[] nums) {
        int position = nums.length - 1;
        int step = 0;
        while (position > 0) {
            for (int i = 0; i < position; i++) {
                if (i + nums[i] >= position) {
                    position = i;
                    step++;
                    break;
                }
            }
        }
        return step;
    }

    //O(n)
    public int jump2(int[] nums) {
        int maxPosition = 0;
        int step = 0;
        int index = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            maxPosition = Math.max(maxPosition, i + nums[i]);
            if (i == index) {
                index = maxPosition;
                step++;
            }
        }
        return step;
    }

    // 55 跳跃游戏
    public boolean canJump(int[] nums) {
        int maxPosition = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            if (i <= maxPosition) {
                maxPosition = Math.max(maxPosition, i + nums[i]);
                if (maxPosition >= nums.length - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canJump2(int[] nums) {
        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > max) return false;
            if (max >= nums.length - 1) return true;
            max = Math.max(max, i + nums[i]);
        }
        return false;
    }

    //1144. 递减元素使数组呈锯齿状
    public int movesToMakeZigzag(int[] nums) {
        int odd = 0, even = 0;
        for (int i = 0; i < nums.length; i++) {
            int left = i > 0 ? nums[i - 1] : 1001;
            int right = i < nums.length - 1 ? nums[i + 1] : 1001;
            if (i % 2 == 0) {
                even += Math.max(0, nums[i] - Math.min(left, right) + 1);
            } else {
                odd += Math.max(0, nums[i] - Math.min(left, right) + 1);
            }
        }
        return Math.min(odd, even);
    }

    //1326. 灌溉花园的最少水龙头数目
    public int minTapsDP(int n, int[] ranges) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, n + 2);
        dp[0] = 0;
        for (int i = 0; i <= n; i++) {
            int left = Math.max(i - ranges[i], 0);
            int right = Math.min(i + ranges[i], n);
            for (int j = left; j <= right; j++) {
                dp[j] = Math.min(dp[j], dp[left] + 1);
            }
        }
        return dp[n] == n + 2 ? -1 : dp[n];
    }

    public int minTaps(int n, int[] ranges) {
        int[] rightMost = new int[n + 1];
        for (int i = 0; i <= n; ++i) {
            if (rightMost[i] == 0) {
                rightMost[i] = i;
            }
            int start = Math.max(0, i - ranges[i]);
            int end = Math.min(n, i + ranges[i]);
            rightMost[start] = Math.max(rightMost[start], end);
        }
        int res = 0, right = 0, cur = 0;
        for (int i = 0; i < n; i++) {
            right = Math.max(right, rightMost[i]);
            if (right == i) return -1;
            if (i == cur) {
                res++;
                cur = right;
            }
        }
        return res;
    }

    public int minTaps2(int n, int[] ranges) {
        int[] rightMost = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            int start = Math.max(0, i - ranges[i]);
            int end = Math.min(n, i + ranges[i]);
            // 这里不能等于end，要保留有效的rightMost：
            // 1.start=end，水龙头是无法覆盖土地的，此时的rightMost其实是无效的
            // 2.rightMax[end]本身=end 也是无效的覆盖
            for (int j = start; j < end; j++) {
                rightMost[j] = Math.max(rightMost[j], end);
            }
        }
        int cur = 0;
        int cnt = 0;
        while (cur < n) {
            // 如果是无效覆盖
            if (rightMost[cur] == 0) return -1;
            cur = rightMost[cur];
            cnt++;
        }
        return cnt;
    }

    // 135 分发糖果
    public int candy1(int[] ratings) {
        int n = ratings.length;
        int[] left = new int[n];
        for (int i = 0; i < n; i++) {
            if (i > 0 && ratings[i] > ratings[i - 1]) {
                left[i] = left[i - 1] + 1;
            } else {
                left[i] = 1;
            }
        }
        int[] right = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            if (i < n - 1 && ratings[i] > ratings[i + 1]) {
                right[i] = right[i + 1] + 1;
            } else {
                right[i] = 1;
            }
        }
        int min = 0;
        for (int i = 0; i < n; i++) {
            min += Math.max(left[i], right[i]);
        }
        return min;
    }

    public int candy2(int[] ratings) {
        int n = ratings.length;
        int[] left = new int[n];
        for (int i = 0; i < n; i++) {
            if (i > 0 && ratings[i] > ratings[i - 1]) {
                left[i] = left[i - 1] + 1;
            } else {
                left[i] = 1;
            }
        }
        int right = 0, ret = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (i < n - 1 && ratings[i] > ratings[i + 1]) {
                right++;
            } else {
                right = 1;
            }
            ret += Math.max(left[i], right);
        }
        return ret;
    }

    //134 加油站
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int n = gas.length;
        int i = 0;
        // 从0开始遍历每一站
        while (i < n) {
            int sumOfGas = 0, sumOfCost = 0;
            // 从i往后能到达几站
            int cnt = 0;
            while (cnt < n) {
                int j = (i + cnt) % n;
                sumOfGas += gas[j];
                sumOfCost += cost[j];
                if (sumOfCost > sumOfGas) {
                    break;
                }
                cnt++;
            }
            //到达了n站直接返回
            if (cnt == n) {
                return i;
            } else {
                //从能到达的最远的站的下一站继续遍历
                i = i + cnt + 1;
            }
        }
        return -1;
    }

    public int canCompleteCircuit2(int[] gas, int[] cost) {
        int len = gas.length;
        int spare = 0;
        // 总油量剩余
        int minSpare = Integer.MAX_VALUE;
        int minIndex = 0;

        for (int i = 0; i < len; i++) {
            spare += gas[i] - cost[i];
            if (spare < minSpare) {
                minSpare = spare;
                minIndex = i;
            }
        }

        // 如果能跑玩全程，总油量剩余最低的点应该是结束点，从下一个点开始出发
        return spare < 0 ? -1 : (minIndex + 1) % len;
    }

    // 455 分发饼干 排序+双指针+贪心
    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int g_idx = 0, s_idx = 0;
        int ans = 0;
        while (s_idx < s.length && g_idx < g.length) {
            if (g[g_idx] <= s[s_idx]) {
                ans++;
                s_idx++;
                g_idx++;
            } else {
                s_idx++;
            }
        }
        return ans;
    }

    public int findContentChildren2(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int m = g.length, n = s.length;
        int count = 0;
        for (int i = 0, j = 0; i < m && j < n; i++, j++) {
            while (j < n && g[i] > s[j]) {
                j++;
            }
            if (j < n) {
                count++;
            }
        }
        return count;
    }


    // 409 最长回文串
    public int longestPalindrome(String s) {
        int[] cnt = new int[128];
        for (char c : s.toCharArray()) {
            cnt[c]++;
        }
        int ans = 0;
        for (int ct : cnt) {
            ans += ct / 2 * 2;
            if ((ct & 1) == 1 && (ans & 1) == 0) ans++;
        }
        return ans;
    }

    // 179 最大数
    //给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。
// 注意：输出结果可能非常大，所以你需要返回一个字符串而不是整数。
//输入：nums = [10,2]
//输出："210"
//输入：nums = [3,30,34,5,9]
//输出："9534330"
    public String largestNumber(int[] nums) {
        int n = nums.length;
        String[] ss = new String[n];
        for (int i = 0; i < n; i++) ss[i] = "" + nums[i];
        Arrays.sort(ss, (a, b) -> {
            String sa = a + b, sb = b + a;
            return sb.compareTo(sa);
        });

        StringBuilder sb = new StringBuilder();
        for (String s : ss) sb.append(s);
        int len = sb.length();
        int k = 0;
        while (k < len - 1 && sb.charAt(k) == '0') k++;
        return sb.substring(k);
    }

    // 243 最短单词距离
    public int shortestDistance(String[] wordsDict, String word1, String word2) {
        int idx1 = -1, idx2 = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < wordsDict.length; i++) {
            if (wordsDict[i].equals(word1)) {
                idx1 = i;
            } else if (wordsDict[i].equals(word2)) {
                idx2 = i;
            }
            if (idx1 >= 0 && idx2 >= 0) {
                min = Math.min(min, Math.abs(idx1 - idx2));
            }
        }
        return min;
    }

    // 245 最短单词距离3 word1和word2可能相同，相同时代表独立单词
//输入：wordsDict = ["practice", "makes", "perfect", "coding", "makes"], word1 = "makes", word2 = "makes"
//输出：3
    public int shortestWordDistance(String[] wordsDict, String word1, String word2) {
        int length = wordsDict.length;
        int ans = length;
        if (word1.equals(word2)) {
            int prev = -1;
            for (int i = 0; i < length; i++) {
                String word = wordsDict[i];
                if (word.equals(word1)) {
                    if (prev >= 0) {
                        ans = Math.min(ans, i - prev);
                    }
                    prev = i;
                }
            }
            return ans;
        }
        return shortestDistance(wordsDict, word1, word2);
    }


    //面试题 17.11. 单词距离
    public int findClosest(String[] words, String word1, String word2) {
        int n = words.length;
        int ans = n, p = -1, q = -1;
        for (int i = 0; i < n; i++) {
            if (word1.equals(words[i])) p = i;
            if (word2.equals(words[i])) q = i;
            if (p != -1 && q != -1) ans = Math.min(ans, Math.abs(p - q));
        }
        return ans;
    }

    // 605 种花问题
    // [0,l] 种花位置数=(l-0+1-2),最大花数=l/2
    // [l,r] 种花位置数=(r-l+1-2-2),最大花数=(r-l-2)/2
    // [r,len) 种花位置数=(len-1-r+1-2), 最大花数=(len-r-1)/2
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        int len = flowerbed.length;
        int cnt = 0;
        for (int l = -1, r = 0; r <= len; r++) {
            if (r == len) {
                if (l < 0) {
                    cnt += (len + 1) / 2;
                } else {
                    cnt += (len - l - 1) / 2;
                }
                break;
            } else if (flowerbed[r] == 1) {
                if (l < 0) {
                    cnt += r / 2;
                } else {
                    cnt += (r - l - 2) / 2;
                }
                l = r;
            }
        }
        return cnt >= n;
    }
    //2136. 全部开花的最早一天 贪心+排序
    public int earliestFullBloom(int[] plantTime, int[] growTime) {
        int n = plantTime.length;
        Integer[] id = new Integer[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
        Arrays.sort(id, (i, j) -> growTime[j] - growTime[i]);
        int ans = 0, days = 0;
        for (int i : id) {
            days += plantTime[i]; // 累加播种天数
            ans = Math.max(ans, days + growTime[i]); // 再加上生长天数，就是这个种子的开花时间
        }
        return ans;
    }


    // 738 单调递增的数字
    public int monotoneIncreasingDigits(int n) {
        char[] chars = String.valueOf(n).toCharArray();
        int len = chars.length;
        int idx = 1;
        while (idx < len && chars[idx - 1] <= chars[idx]) {
            idx++;
        }
        if (idx < len) {
            //从后往前找到递增转折点，该点-1 后面变99
            while (idx > 0 && chars[idx - 1] > chars[idx]) {
                chars[idx - 1]--;
                idx--;
            }
            for (int i = idx + 1; i < len; i++) {
                chars[i] = '9';
            }
        }
        return Integer.parseInt(String.valueOf(chars));
    }

    //860. 柠檬水找零
    public boolean lemonadeChange(int[] bills) {
        int[] have = new int[3];
        for (int bill : bills) {
            if (bill == 5) {
                have[0]++;
            } else if (bill == 10) {
                if (have[0] < 1) return false;
                have[0]--;
                have[1]++;
            } else if (bill == 20) {
                if (have[1] >= 1 && have[0] >= 1) {
                    have[1]--;
                    have[0]--;
                    have[2]++;
                } else if (have[0] >= 3) {
                    have[0]-=3;
                    have[2]++;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    //942. 增减字符串匹配
    public int[] diStringMatch(String s) {
        int n = s.length(), l = 0, r = n, idx = 0;
        int[] ans = new int[n + 1];
        for (int i = 0; i < n; i++) {
            ans[idx++] = s.charAt(i) == 'I' ? l++ : r--;
        }
        ans[idx] = l;
        return ans;
    }

    //2027. 转换字符串的最少操作次数
    public int minimumMoves(String s) {
        int ans = 0;
        int covered = -1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'X' && i > covered) {
                ans++;
                covered = i + 2;
            }
        }
        return ans;
    }

    //1465. 切割后面积最大的蛋糕 横向和纵向最大区域相乘
    public int maxArea(int h, int w, int[] horizontalCuts, int[] verticalCuts) {
        int mod = (int) 1e9+7;
        int m = horizontalCuts.length,n = verticalCuts.length;
        Arrays.sort(horizontalCuts);
        Arrays.sort(verticalCuts);
        int maxH = Math.max(horizontalCuts[0],h-horizontalCuts[m-1]);
        int maxV = Math.max(verticalCuts[0],w-verticalCuts[n-1]);
        for(int i=1;i<m;i++){
            maxH = Math.max(maxH,horizontalCuts[i]-horizontalCuts[i-1]);
        }
        for(int i=1;i<n;i++){
            maxV = Math.max(maxV,verticalCuts[i]-verticalCuts[i-1]);

        }
        return (int)((1L*maxH*maxV)%mod);

    }

    //1488. 避免洪水泛滥
    public int[] avoidFlood(int[] rains) {
        int n = rains.length;
        int[] ans = new int[n];
        Arrays.fill(ans,1);
        List<Integer> workDays = new ArrayList<>();
        Map<Integer, Integer> rainMoreThanOnce = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (rains[i] > 0) {
                if (rainMoreThanOnce.containsKey(rains[i])) {
                    int lastIdx = rainMoreThanOnce.get(rains[i]);
                    int nextWorkDay = binarySearch1488(workDays, lastIdx);
                    if (nextWorkDay == -1) return new int[0];
                    ans[nextWorkDay] = rains[i];
                }
                ans[i] = -1;
                rainMoreThanOnce.put(rains[i], i);
            } else {
                workDays.add(i);
            }
        }
        return ans;
    }

    //找到 idx后第一个大于
    private int binarySearch1488(List<Integer> ls, int idx) {
        if (ls.size() == 0) {
            return -1;
        }
        int l = 0, r = ls.size() - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (ls.get(mid) > idx) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        int next = ls.get(l);
        if (next > idx) {
            ls.remove(l);
            return next;
        }
        return -1;
    }

    //2578. 最小和分割
    public int splitNum(int num) {
        char[] stnum = Integer.toString(num).toCharArray();
        Arrays.sort(stnum);
        int num1 = 0, num2 = 0;
        for (int i = 0; i < stnum.length; ++i) {
            if (i % 2 == 0) {
                num1 = num1 * 10 + (stnum[i] - '0');
            } else {
                num2 = num2 * 10 + (stnum[i] - '0');
            }
        }
        return num1 + num2;
    }

    //1402. 做菜顺序
    public int maxSatisfactionGreedy(int[] satisfaction) {
        Arrays.sort(satisfaction);
        for (int i = 0, j = satisfaction.length - 1; i < j; i++, j--) {
            int temp = satisfaction[i];
            satisfaction[i] = satisfaction[j];
            satisfaction[j] = temp;
        }
        int presum = 0, ans = 0;
        for (int si : satisfaction) {
            if (presum + si > 0) {
                presum += si;
                ans += presum;
            } else {
                break;
            }
        }
        return ans;
    }

    //1262. 可被三整除的最大和
    public int maxSumDivThree(int[] nums) {
        int[] f = {0, Integer.MIN_VALUE, Integer.MIN_VALUE};
        for (int num : nums) {
            int[] g = new int[3];
            System.arraycopy(f, 0, g, 0, 3);
            for (int i = 0; i < 3; ++i) {
                g[(i + num % 3) % 3] = Math.max(g[(i + num % 3) % 3], f[i] + num);
            }
            f = g;
        }
        return f[0];
    }

    //1253. 重构 2 行二进制矩阵
    public List<List<Integer>> reconstructMatrix(int upper, int lower, int[] colsum) {
        int n = colsum.length;
        int sum = 0, two = 0;
        for (int i = 0; i < n; ++i) {
            if (colsum[i] == 2) {
                ++two;
            }
            sum += colsum[i];
        }
        if (sum != upper + lower || Math.min(upper, lower) < two) {
            return new ArrayList<List<Integer>>();
        }
        upper -= two;
        lower -= two;
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        for (int i = 0; i < 2; ++i) {
            res.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < n; ++i) {
            if (colsum[i] == 2) {
                res.get(0).add(1);
                res.get(1).add(1);
            } else if (colsum[i] == 1) {
                if (upper > 0) {
                    res.get(0).add(1);
                    res.get(1).add(0);
                    --upper;
                } else {
                    res.get(0).add(0);
                    res.get(1).add(1);
                }
            } else {
                res.get(0).add(0);
                res.get(1).add(0);
            }
        }
        return res;
    }

    //2734. 执行子串操作后的字典序最小字符串
    public String smallestString(String s) {
        int n = s.length();
        char[] chars = s.toCharArray();
        int idx = 0;
        while (idx < n && chars[idx] == 'a') {
            idx++;
        }
        if (idx == n) {
            chars[n - 1] = 'z';
            return new String(chars);
        }
        while (idx < n && s.charAt(idx) > 'a') {
            chars[idx++]--;
        }
        return new String(chars);
    }

    //1921. 消灭怪物的最大数量
    public int eliminateMaximum(int[] dist, int[] speed) {
        int n = dist.length;
        int[] arrivalTimes = new int[n];
        for (int i = 0; i < n; i++) {
            arrivalTimes[i] = (dist[i] - 1) / speed[i] + 1;
        }
        Arrays.sort(arrivalTimes);
        for (int i = 0; i < n; i++) {
            if (arrivalTimes[i] <= i) {
                return i;
            }
        }
        return n;
    }

    //1605. 给定行和列的和求可行矩阵
    public int[][] restoreMatrix(int[] rowSum, int[] colSum) {
        int m = rowSum.length, n = colSum.length;
        int[][] matrix = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = Math.min(rowSum[i], colSum[j]);
                rowSum[i] -= matrix[i][j];
                colSum[j] -= matrix[i][j];
            }
        }
        return matrix;
    }

    //1663. 具有给定数值的最小字符串
    public String getSmallestString(int n, int k) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            int lower = Math.max(1, k - (n - i) * 26);
            k -= lower;
            sb.append((char) ('a' + lower - 1));
        }
        return sb.toString();
    }

    //1330. 翻转子数组得到最大的数组值
    public int maxValueAfterReverse(int[] nums) {
        int value = 0, n = nums.length;
        for (int i = 0; i < n - 1; i++) {
            value += Math.abs(nums[i] - nums[i + 1]);
        }
        int mx1 = 0;
        for (int i = 1; i < n - 1; i++) {
            mx1 = Math.max(mx1, Math.abs(nums[0] - nums[i + 1]) - Math.abs(nums[i] - nums[i + 1]));
            mx1 = Math.max(mx1, Math.abs(nums[n - 1] - nums[i - 1]) - Math.abs(nums[i] - nums[i - 1]));
        }
        int mx2 = Integer.MIN_VALUE, mn2 = Integer.MAX_VALUE;
        for (int i = 0; i < n - 1; i++) {
            int x = nums[i], y = nums[i + 1];
            mx2 = Math.max(mx2, Math.min(x, y));
            mn2 = Math.min(mn2, Math.max(x, y));
        }
        return value + Math.max(mx1, 2 * (mx2 - mn2));
    }

    //1754. 构造字典序最大的合并字符串
    public String largestMerge(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int i = 0, j = 0;
        StringBuilder sb = new StringBuilder();
        while (i < m || j < n) {
            if (i < m && word1.substring(i).compareTo(word2.substring(j)) > 0) {
                sb.append(word1.charAt(i++));
            } else {
                sb.append(word2.charAt(j++));
            }
        }
        return sb.toString();
    }

    //后缀数组
    //https://leetcode.cn/problems/largest-merge-of-two-strings/solution/gou-zao-zi-dian-xu-zui-da-de-he-bing-zi-g6az1/
    public String largestMergeSuffixArray(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        String str = word1 + "@" + word2 + "*";
        int[] suffixArray = buildSuffixArray(str);
        int[] rank = new int[m + n + 2];
        for (int i = 0; i < m + n + 2; i++) {
            rank[suffixArray[i]] = i;
        }

        StringBuilder merge = new StringBuilder();
        int i = 0, j = 0;
        while (i < m || j < n) {
            if (i < m && rank[i] > rank[m + 1 + j]) {
                merge.append(word1.charAt(i));
                i++;
            } else {
                merge.append(word2.charAt(j));
                j++;
            }
        }
        return merge.toString();
    }

    public int[] buildSuffixArray(String text) {
        int[] order = sortCharacters(text);
        int[] classfiy = computeCharClasses(text, order);
        int len = 1;
        int n = text.length();
        for (int i = 1; i < n; i <<= 1) {
            order = sortDoubled(text, i, order, classfiy);
            classfiy = updateClasses(order, classfiy, i);
        }
        return order;
    }

    public int[] sortCharacters(String text) {
        int n = text.length();
        int[] count = new int[128];
        int[] order = new int[n];
        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);
            count[c]++;
        }
        for (int i = 1; i < 128; i++) {
            count[i] += count[i - 1];
        }
        for (int i = n - 1; i >= 0; i--) {
            count[text.charAt(i)]--;
            order[count[text.charAt(i)]] = i;
        }
        return order;
    }

    public int[] computeCharClasses(String text, int[] order) {
        int n = text.length();
        int[] res = new int[n];
        res[order[0]] = 0;
        for (int i = 1; i < n; i++) {
            if (text.charAt(order[i]) != text.charAt(order[i - 1])) {
                res[order[i]] = res[order[i - 1]] + 1;
            } else {
                res[order[i]] = res[order[i - 1]];
            }
        }
        return res;
    }

    public int[] sortDoubled(String text, int len, int[] order, int[] classfiy) {
        int n = text.length();
        int[] count = new int[n];
        int[] newOrder = new int[n];
        for (int i = 0; i < n; i++) {
            count[classfiy[i]]++;
        }
        for (int i = 1; i < n; i++) {
            count[i] += count[i - 1];
        }
        for (int i = n - 1; i >= 0; i--) {
            int start = (order[i] - len + n) % n;
            int cl = classfiy[start];
            count[cl]--;
            newOrder[count[cl]] = start;
        }
        return newOrder;
    }

    public int[] updateClasses(int[] newOrder, int[] classfiy, int len) {
        int n = newOrder.length;
        int[] newClassfiy = new int[n];
        newClassfiy[newOrder[0]] = 0;
        for (int i = 1; i < n; i++) {
            int curr = newOrder[i];
            int prev = newOrder[i - 1];
            int mid = curr + len;
            int midPrev = (prev + len) % n;
            if (classfiy[curr] != classfiy[prev] || classfiy[mid] != classfiy[midPrev]) {
                newClassfiy[curr] = newClassfiy[prev] + 1;
            } else {
                newClassfiy[curr] = newClassfiy[prev];
            }
        }
        return newClassfiy;
    }

    // 1785. 构成特定和需要添加的最少元素
    public int minElements(int[] nums, int limit, int goal) {
        long sum = 0;
        for (int num : nums) {
            sum += num;
        }
        long diff = goal - sum;
        long ans = (Math.abs(diff) + limit - 1) / limit;
        return (int) ans;
    }

    //1798. 你能构造出连续值的最大数目
    public int getMaximumConsecutive(int[] coins) {
        int m = 0; // 一开始只能构造出 0
        Arrays.sort(coins);
        for (int c : coins) {
            if (c > m + 1) // coins 已排序，后面没有比 c 更小的数了
                break; // 无法构造出 m+1，继续循环没有意义
            m += c; // 可以构造出区间 [0,m+c] 中的所有整数
        }
        return m + 1; // [0,m] 中一共有 m+1 个整数
    }

    //1827. 最少操作使数组递增
    public int minOperations(int[] nums) {
        int ans = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1]) continue;

            ans += nums[i - 1] + 1 - nums[i];
            nums[i] = nums[i - 1] + 1;
        }
        return ans;
    }

    //2178. 拆分成最多数目的正偶数之和
    public List<Long> maximumEvenSplit(long finalSum) {
        List<Long> result = new ArrayList<>();
        if (finalSum % 2 != 0) {
            return result;
        }
        for (long i = 2; i <= finalSum; i += 2) {
            result.add(i);
            finalSum -= i;
        }
        result.set(result.size() - 1, result.get(result.size() - 1) + finalSum);
        return result;
    }

    // 857 雇佣K名工人的最低成本
    public double mincostToHireWorkers(int[] quality, int[] wage, int k) {
        int n = quality.length;
        Integer[] h = new Integer[n];
        for (int i = 0; i < n; i++) {
            h[i] = i;
        }
        Arrays.sort(h, (a, b) -> quality[b] * wage[a] - quality[a] * wage[b]);
        double res = 1e9;
        double totalq = 0.0;
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
        for (int i = 0; i < k - 1; i++) {
            totalq += quality[h[i]];
            pq.offer(quality[h[i]]);
        }
        for (int i = k - 1; i < n; i++) {
            int idx = h[i];
            totalq += quality[idx];
            pq.offer(quality[idx]);
            double totalc = ((double) wage[idx] / quality[idx]) * totalq;
            res = Math.min(res, totalc);
            totalq -= pq.poll();
        }
        return res;
    }

    public double mincostToHireWorkers2(int[] qs, int[] ws, int k) {
        int n = qs.length;
        double[][] ds = new double[n][2];
        for (int i = 0; i < n; i++) {
            ds[i][0] = ws[i] * 1.0 / qs[i];
            ds[i][1] = i * 1.0;
        }
        Arrays.sort(ds, Comparator.comparingDouble(a -> a[0]));
        PriorityQueue<Integer> q = new PriorityQueue<>((a, b) -> b - a);
        double ans = 1e18;
        for (int i = 0, tot = 0; i < n; i++) {
            int cur = qs[(int) ds[i][1]];
            tot += cur;
            q.add(cur);
            if (q.size() > k) tot -= q.poll();
            if (q.size() == k) ans = Math.min(ans, tot * ds[i][0]);
        }
        return ans;
    }

    //822. 翻转卡片游戏
    public int flipgame(int[] fronts, int[] backs) {
        Set<Integer> same = new HashSet();
        for (int i = 0; i < fronts.length; ++i) {
            if (fronts[i] == backs[i]) {
                same.add(fronts[i]);
            }
        }
        int res = 3000;
        for (int x : fronts) {
            if (x < res && !same.contains(x)){
                res = x;
            }
        }
        for (int x : backs) {
            if (x < res && !same.contains(x)) {
                res = x;
            }
        }
        return res % 3000;
    }

    //1090. 受标签影响的最大值
    public int largestValsFromLabels(int[] values, int[] labels, int numWanted, int useLimit) {
        int n = values.length;
        int[][] pairs = new int[n][2];
        for (int i = 0; i < n; ++i) {
            pairs[i] = new int[]{values[i], labels[i]};
        }
        Arrays.sort(pairs, (a, b) -> b[0] - a[0]);
        Map<Integer, Integer> cnt = new HashMap<>();
        int ans = 0, num = 0;
        for (int i = 0; i < n && num < numWanted; ++i) {
            int v = pairs[i][0], l = pairs[i][1];
            if (cnt.getOrDefault(l, 0) < useLimit) {
                cnt.merge(l, 1, Integer::sum);
                num += 1;
                ans += v;
            }
        }
        return ans;

    }

    //LCP 33. 蓄水
    public int storeWater(int[] bucket, int[] vat) {
        int n = bucket.length;
        int maxk = Arrays.stream(vat).max().getAsInt();
        if (maxk == 0) {
            return 0;
        }
        int res = Integer.MAX_VALUE;
        for (int k = 1; k <= maxk && k < res; ++k) {
            int t = 0;
            for (int i = 0; i < bucket.length; ++i) {
                t += Math.max(0, (vat[i] + k - 1) / k - bucket[i]);
            }
            res = Math.min(res, t + k);
        }
        return res;
    }

    //2680. 最大或值
    public long maximumOr(int[] nums, int k) {
        int n = nums.length;
        int[] suf = new int[n + 1];
        for (int i = n - 1; i > 0; i--)
            suf[i] = suf[i + 1] | nums[i];
        long ans = 0;
        for (int i = 0, pre = 0; i < n; i++) {
            ans = Math.max(ans, pre | ((long) nums[i] << k) | suf[i + 1]);
            pre |= nums[i];
        }
        return ans;
    }

    //1033. 移动石子直到连续
    public int[] numMovesStones(int a, int b, int c) {
        int x = Math.min(Math.min(a, b), c);
        int z = Math.max(Math.max(a, b), c);
        int y = a + b + c - x - z;

        int[] res = new int[2];
        res[0] = 2;
        if (z - y == 1 && y - x == 1) {
            res[0] = 0;
        } else if (z - y <= 2 || y - x <= 2) {
            res[0] = 1;
        }
        res[1] = z - x - 2;
        return res;
    }

    // 1154 一年中的第几天
    public int dayOfYear(String date) {
        String[] strings = date.split("-");
        int[] monthDays = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (isLeapYear(strings[0])) {
            monthDays[1]++;
        }
        int ans = 0;
        int idx = Integer.parseInt(strings[1]) - 1;
        for (int i = 0; i < idx; i++) {
            ans += monthDays[i];
        }
        ans += Integer.parseInt(strings[2]);
        return ans;
    }

    private boolean isLeapYear(String yearStr) {
        int year = Integer.parseInt(yearStr);
        if (year % 4 == 0 && year % 100 != 0) {
            return true;
        }
        if (year % 400 == 0) {
            return true;
        }
        return false;
    }

    //1185. 一周中的第几天
    public String dayOfTheWeek(int day, int month, int year) {
        String[] week = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30};
        /* 输入年份之前的年份的天数贡献 */
        int days = 365 * (year - 1971) + (year - 1969) / 4;
        /* 输入年份中，输入月份之前的月份的天数贡献 */
        for (int i = 0; i < month - 1; ++i) {
            days += monthDays[i];
        }
        if ((year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) && month >= 3) {
            days += 1;
        }
        /* 输入月份中的天数贡献 */
        days += day;
        return week[(days + 3) % 7];
    }



    // 1481 不同整数的最少数目
    public int findLeastNumOfUniqueInts(int[] arr, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : arr) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        List<Integer> list = new ArrayList<>(map.values());
        list.sort(Comparator.comparingInt(o -> o));
        int ans = list.size();
        for (int num : list) {
            if (k < num) return ans;
            k -= num;
            ans--;
        }
        return ans;
    }

    // 1675 数组的最小偏移量
    //给你一个由 n 个正整数组成的数组 nums 。你可以对数组的任意元素执行任意次数的两类操作：
// 如果元素是 偶数 ，除以 2如果元素是 奇数 ，乘上 2
// 数组的 偏移量 是数组中任意两个元素之间的 最大差值 。返回数组在执行某些操作之后可以拥有的 最小偏移量 。

//  一个数的变化范围有限, 比如所有的奇数都只能做一次乘 2 操作(*2后变偶数), 偶数可以做若干次除以 2 的操作.
// 数值可以双向变化不好处理, 我们先变成单向的: 把所有数都变成自己可变化范围的最大值.
// 那么现在剩下的操作就只有把数缩小了.
//而偏移量 = 最大值 - 最小值, 所以我们要做的就是缩小最大值. (缩小其他数值也无法优化偏移量)

    public int minimumDeviation(int[] nums) {
        TreeSet<Integer> set = new TreeSet<>();
        for (int num : nums) {
            set.add(num % 2 == 0 ? num : num * 2);
        }
        int res = set.last() - set.first();
        while (res > 0 && set.last() % 2 == 0) {
            int max = set.last();
            set.remove(max);
            set.add(max / 2);
            res = Math.min(res, set.last() - set.first());
        }
        return res;
    }

    //1775. 通过最少操作次数使数组的和相等
    public int minOperations(int[] nums1, int[] nums2) {
        int n = nums1.length, m = nums2.length;
        if (6 * n < m || 6 * m < n) {
            return -1;
        }
        int[] cnt1 = new int[7];
        int[] cnt2 = new int[7];
        int diff = 0;
        for (int i : nums1) {
            ++cnt1[i];
            diff += i;
        }
        for (int i : nums2) {
            ++cnt2[i];
            diff -= i;
        }
        if (diff == 0) {
            return 0;
        }
        if (diff > 0) {
            return help(cnt2, cnt1, diff);
        }
        return help(cnt1, cnt2, -diff);
    }

    public int help(int[] h1, int[] h2, int diff) {
        int[] h = new int[7];
        for (int i = 1; i < 7; ++i) {
            h[6 - i] += h1[i];
            h[i - 1] += h2[i];
        }
        int res = 0;
        for (int i = 5; i > 0 && diff > 0; --i) {
            int t = Math.min((diff + i - 1) / i, h[i]);
            res += t;
            diff -= t * i;
        }
        return res;
    }

    //1790 仅执行一次字符串交换能否使两个字符串相等
    public boolean areAlmostEqual(String s1, String s2) {
        List<Integer> diffIdx = new ArrayList<>();
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                diffIdx.add(i);
            }
            if (diffIdx.size() > 2) return false;
        }
        if (diffIdx.size() == 0) return true;
        if (diffIdx.size() != 2) return false;
        return s1.charAt(diffIdx.get(0)) == s2.charAt(diffIdx.get(1)) && s1.charAt(diffIdx.get(1)) == s2.charAt(diffIdx.get(0));
    }

    //6196. 将字符串分割成值不超过 K 的子字符串  记忆化搜索搜minimumPartition
    public int minimumPartitionGreedy(String s, int k) {
        int n = s.length();
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (c - '0' > k) return -1;
        }
        int d = String.valueOf(k).length();
        int prev = 0, i = 0;
        int cnt = 0;
        while (i < n) {
            long num = Long.parseLong(s.substring(prev, i + 1));
            if (num > k || i - prev >= d) {
                prev = i;
                cnt++;
            }
            i++;
        }
        return cnt;
    }

    //2645. 构造有效字符串的最少插入数
    public int addMinimum(String word) {
        int n = word.length();
        int k = 0;
        for (int i = 1; i < n; i++) {
            if (word.charAt(i) <= word.charAt(i - 1)) {
                k++;
            }
        }
        return 3 * (k + 1) - n;

    }

    //334 递增的三元子序列 O(n)
    public boolean increasingTriplet(int[] nums) {
        int n = nums.length;
        // [0,i]最小的值
        int[] leftMin = new int[n];
        leftMin[0] = nums[0];
        for (int i = 1; i < n; i++) {
            leftMin[i] = Math.min(leftMin[i - 1], nums[i]);
        }
        //[i,n-1]最大的值
        int[] rightMax = new int[n];
        rightMax[n - 1] = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], nums[i]);
        }
        for (int i = 1; i < n - 1; i++) {
            if (nums[i] < rightMax[i + 1] && nums[i] > leftMin[i - 1]) {
                return true;
            }
        }
        return false;
    }

    public boolean increasingTripletGreedy(int[] nums) {
        int n = nums.length;
        if (n < 3) return false;
        int first = nums[0], second = Integer.MAX_VALUE;
        for (int i = 1; i < n; i++) {
            if (nums[i] > second) {
                return true;
            } else if (nums[i] > first) {
                second = nums[i];
            } else {
                // 这里不更新second=first
                // second的坐标小于新first，下次循环如果num>sencond，(旧first，senond，num)也满足
                first = nums[i];
            }
        }
        return false;
    }

    // 转换LIS问题 O(nlogn)
    public boolean increasingTripletLIS(int[] nums) {
        int n = nums.length, ans = 1;
        int[] f = new int[n + 1];
        Arrays.fill(f, 0x3f3f3f3f);
        for (int i = 0; i < n; i++) {
            int t = nums[i];
            int l = 1, r = i + 1;
            while (l < r) {
                int mid = l + r >> 1;
                if (f[mid] >= t) r = mid;
                else l = mid + 1;
            }
            f[r] = t;
            ans = Math.max(ans, r);
        }
        return ans >= 3;
    }

    //2591. 将钱分给最多的儿童
    public int distMoney(int money, int children) {
        money -= children;
        if (money < 0) return -1;
        int ans = Math.min(money / 7, children);
        money -= ans * 7;
        children -= ans;
        if ((children == 0 && money > 0) || (children == 1 && money == 3)) ans--;
        return ans;
    }

    //2600. K 件物品的最大和
    public int kItemsWithMaximumSum(int numOnes, int numZeros, int numNegOnes, int k) {
        if (k <= numOnes) return k;
        if (k <= numOnes + numZeros) return numOnes;
        return numOnes - (k - numOnes - numZeros);
    }

    //2607. 使子数组元素和相等
    //中位数贪心+裴蜀定理
    public long makeSubKSumEqual(int[] arr, int k) {
        int n = arr.length;
        int g = gcd(n, k);
        long ans = 0;
        for (int i = 0; i < g; i++) {
            List<Integer> ls = new ArrayList<>();
            for (int j = i; j < n; j += g) {
                ls.add(arr[j]);
            }
            Collections.sort(ls);
            int mid = ls.get(ls.size() / 2);
            for (int num : ls) {
                ans += Math.abs(num - mid);
            }
        }
        return ans;
    }

    private int gcd(int x, int y) {
        return y > 0 ? gcd(y, x % y) : x;
    }

    //2605. 从两个数字数组里生成最小数字
    public int minNumber(int[] nums1, int[] nums2) {
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
        Set<Integer> set1 = new HashSet<>();
        TreeSet<Integer> treeSet = new TreeSet<>();
        for (int num : nums1) {
            min1 = Math.min(min1, num);
            set1.add(num);
        }
        for (int num : nums2) {
            min2 = Math.min(min2, num);
            if (set1.contains(num)) {
                treeSet.add(num);
            }
        }
        if (!treeSet.isEmpty()) return treeSet.first();
        if (min1 < min2) return min1 * 10 + min2;
        return min2 * 10 + min1;
    }

    //2182. 构造限制重复的字符串
    public String repeatLimitedString(String s, int repeatLimit) {
        int N = 26;
        int[] count = new int[N];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
        }
        StringBuilder ret = new StringBuilder();
        int m = 0;
        for (int i = N - 1, j = N - 2; i >= 0 && j >= 0;) {
            if (count[i] == 0) { // 当前字符已经填完，填入后面的字符，重置 m
                m = 0;
                i--;
            } else if (m < repeatLimit) { // 当前字符未超过限制
                count[i]--;
                ret.append((char)('a' + i));
                m++;
            } else if (j >= i || count[j] == 0) { // 当前字符已经超过限制，查找可填入的其他字符
                j--;
            } else { // 当前字符已经超过限制，填入其他字符，并且重置 m
                count[j]--;
                ret.append((char)('a' + j));
                m = 0;
            }
        }
        return ret.toString();
    }

    //2216. 美化数组的最少删除数
    public int minDeletion(int[] nums) {
        int n = nums.length, cnt = 0;
        for (int i = 0; i < n; i++) {
            if ((i - cnt) % 2 == 0 && i + 1 < n && nums[i] == nums[i + 1]) cnt++;
        }
        return (n - cnt) % 2 != 0 ? cnt + 1 : cnt;
    }

    //1702. 修改后的最大二进制字符串
    public String maximumBinaryString(String binary) {
        int k = binary.indexOf('0');
        if (k == -1) {
            return binary;
        }
        int n = binary.length();
        for (int i = k + 1; i < n; ++i) {
            if (binary.charAt(i) == '0') {
                ++k;
            }
        }
        char[] ans = binary.toCharArray();
        Arrays.fill(ans, '1');
        ans[k] = '0';
        return String.valueOf(ans);
    }

    //1969. 数组元素的最小非零乘积
    public int minNonZeroProduct(int p) {
        if (p == 1) {
            return 1;
        }
        long mod = 1000000007;
        long x = fastPow(2, p, mod) - 1;
        long y = (long) 1 << (p - 1);
        return (int) (fastPow(x - 1, y - 1, mod) * x % mod);
    }

    public long fastPow(long x, long n, long mod) {
        long res = 1;
        for (; n != 0; n >>= 1) {
            if ((n & 1) != 0) {
                res = res * x % mod;
            }
            x = x * x % mod;
        }
        return res;
    }

    //2789. 合并后数组中的最大元素
    public long maxArrayValue(int[] nums) {
        long sum = nums[nums.length - 1];
        for (int i = nums.length - 2; i >= 0; i--) {
            sum = nums[i] <= sum ? nums[i] + sum : nums[i];
        }
        return sum;
    }

    //2834. 找出美丽数组的最小和
    public int minimumPossibleSum(int n, int target) {
        final int MOD = (int) 1e9 + 7;
        int m = target / 2;
        if (n <= m) {
            return (int) ((long) (1 + n) * n / 2 % MOD);
        }
        return (int) (((long) (1 + m) * m / 2 +
                ((long) target + target + (n - m) - 1) * (n - m) / 2) % MOD);
    }
    //LCP 30. 魔塔游戏
    public int magicTower(int[] nums) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        long sum = 1;
        long delay = 0;
        int ans = 0;
        for (int num : nums) {
            sum += num;
            if (num < 0) {
                pq.offer(num);
            }
            if (sum <= 0) {
                ans++;
                int min = pq.poll();
                sum -= min;
                delay += min;
            }
        }
        sum += delay;
        return sum <= 0 ? -1 : ans;
    }

    //1686. 石子游戏 VI
    public int stoneGameVI(int[] aliceValues, int[] bobValues) {
        int n = aliceValues.length;
        int[][] values = new int[n][3];
        for (int i = 0; i < n; i++) {
            values[i][0] = aliceValues[i] + bobValues[i];
            values[i][1] = aliceValues[i];
            values[i][2] = bobValues[i];
        }
        Arrays.sort(values, (a, b) -> b[0] - a[0]);
        int aliceSum = 0, bobSum = 0;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                aliceSum += values[i][1];
            } else {
                bobSum += values[i][2];
            }
        }
        if (aliceSum > bobSum) {
            return 1;
        } else if (aliceSum == bobSum) {
            return 0;
        } else {
            return -1;
        }
    }

    //330. 按要求补齐数组
    public int minPatches(int[] nums, int n) {
        //累加的总和
        long total = 0;
        //需要补充的数字个数
        int count = 0;
        //访问的数组下标索引
        int index = 0;
        while (total < n) {
            if (index < nums.length && nums[index] <= total + 1) {
                //如果数组能组成的数字范围是[1,total]，那么加上nums[index]
                //就变成了[1,total]U[nums[index],total+nums[index]]
                //结果就是[1,total+nums[index]]
                total += nums[index++];
            } else {
                //添加一个新数字，并且count加1
                total = total + (total + 1);
                count++;
            }
        }
        return count;
    }

    //2592. 最大化数组的伟大值 田忌赛马+双指针+贪心
    public int maximizeGreatness(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        int ans = 0;
        for (int l = 0, r = 0; r < n; r++) {
            if (nums[r] > nums[l]) {
                ans++;
                l++;
            }
        }
        return ans;
    }

    //870. 优势洗牌 田忌赛马
    //给定两个大小相等的数组 nums1 和 nums2，nums1 相对于 nums2 的优势可以用满足 nums1[i] > nums2[i] 的索引 i 的数目来描述。
// 返回 nums1 的任意排列，使其相对于 nums2 的优势最大化。
//输入：nums1 = [2,7,11,15], nums2 = [1,10,4,11] 输出：[2,11,7,15]
// 输入：nums1 = [12,24,8,32], nums2 = [13,25,32,11] 输出：[24,32,8,12]
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
            //当前num比nums2下一个最小值还小的时候，对位nums2最大
            if (num <= nums2[idx2[l]]) {
                ans[idx2[r--]] = num;
            } else {
                ans[idx2[l++]] = num;
            }
        }
        return ans;
    }

    // 553最优除法
    public String optimalDivision(int[] nums) {
        if (nums.length == 1)
            return nums[0] + "";
        if (nums.length == 2)
            return nums[0] + "/" + nums[1];
        StringBuilder res = new StringBuilder(nums[0] + "/(" + nums[1]);
        for (int i = 2; i < nums.length; i++) {
            res.append("/").append(nums[i]);
        }
        res.append(")");
        return res.toString();
    }

    //561数组拆分
    // 原答案 = nums[k] + nums[k + 2] + ... + nums[n - 1]
    //调整后答案 = nums[k + 1] + nums[k + 3] + ... + nums[n - 2] + min(nums[n], nums[k])
    // 由于 min(nums[n], nums[k]) 中必然是 nums[k] 被选择。
    // 因此： 调整后答案 = nums[k] + nums[k + 1] + nums[k + 3] + ... + nums[n - 2]
    public int arrayPairSum(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        int ans = 0;
        for (int i = 0; i < n; i += 2) ans += nums[i];
        return ans;
    }

    // 670 最大交换
    //给定一个非负整数，你至多可以交换一次数字中的任意两位。返回你能得到的最大值。
//输入: 2736输出: 7236 解释: 交换数字2和数字7。
    public int maximumSwap(int num) {
        char[] charArray = String.valueOf(num).toCharArray();
        int n = charArray.length;
        int maxIdx = n - 1;
        int idx1 = -1, idx2 = -1;
        for (int i = n - 1; i >= 0; i--) {
            if (charArray[i] > charArray[maxIdx]) {
                maxIdx = i;
            } else if (charArray[i] < charArray[maxIdx]) {
                idx1 = i;
                idx2 = maxIdx;
            }
        }
        if (idx1 >= 0) {
            swap(charArray, idx1, idx2);
            return Integer.parseInt(new String(charArray));
        } else {
            return num;
        }
    }

    public void swap(char[] charArray, int i, int j) {
        char temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
    }

    // 763 划分字母区间
    //字符串 S 由小写字母组成。我们要把这个字符串划分为尽可能多的片段，同一字母最多出现在一个片段中。返回一个表示每个字符串片段的长度的列表。
//输入：S = "ababcbacadefegdehijhklij"
//输出：[9,7,8]
    public List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        int length = s.length();
        for (int i = 0; i < length; i++) {
            last[s.charAt(i) - 'a'] = i;
        }
        List<Integer> partition = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < length; i++) {
            end = Math.max(end, last[s.charAt(i) - 'a']);
            if (i == end) {
                partition.add(end - start + 1);
                start = end + 1;
            }
        }
        return partition;
    }

    // 769最多能完成排序的块
    //给定一个长度为 n 的整数数组 arr ，它表示在 [0, n - 1] 范围内的整数的排列。
// 我们将 arr 分割成若干 块 (即分区)，并对每个块单独排序。将它们连接起来后，使得连接的结果和按升序排序后的原数组相同。
// 返回数组能分成的最多块数量。
    public int maxChunksToSorted(int[] arr) {
        int ans = 0, max = 0;
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
            if (max == i) ans++;
        }
        return ans;
    }

    // 768 最多能完成排序的块2
    //这个问题和“最多能完成排序的块”相似，但给定数组中的元素可以重复，输入数组最大长度为2000，其中的元素最大为10**8。
// arr是一个可能包含重复元素的整数数组，我们将这个数组分割成几个“块”，并将这些块分别进行排序。之后再连接起来，使得连接的结果和按升序排序后的原数组相同。
    public int maxChunksToSorted2(int[] arr) {
        // 保存每一块的最大值
        Stack<Integer> stack = new Stack<>();
        for (int num : arr) {
            // 正常递增，遇到递减时，记录前一个最大值
            if (!stack.isEmpty() && stack.peek() > num) {
                int lastLarge = stack.pop();
                //所有其余大于当前值的清空
                while (!stack.isEmpty() && stack.peek() > num) {
                    stack.pop();
                }
                //放入该块的最大值
                stack.push(lastLarge);
            } else {
                stack.push(num);
            }
        }
        return stack.size();
    }

    // 915 分割数组
    public int partitionDisjoint(int[] nums) {
        int n = nums.length;
        int[] minRight = new int[n];
        minRight[n - 1] = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            minRight[i] = Math.min(nums[i], minRight[i + 1]);
        }

        int maxLeft = 0;
        for (int i = 0; i < n - 1; i++) {
            maxLeft = Math.max(maxLeft, nums[i]);
            if (maxLeft <= minRight[i + 1]) {
                return i + 1;
            }
        }
        return n - 1;
    }

    //  807 保持城市天际线
    public int maxIncreaseKeepingSkyline(int[][] grid) {
        int n = grid.length;
        int[] rowMax = new int[n];
        int[] colMax = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rowMax[i] = Math.max(rowMax[i], grid[i][j]);
                colMax[i] = Math.max(colMax[i], grid[j][i]);
            }
        }
        int max = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                max += Math.min(rowMax[i], colMax[j]) - grid[i][j];
            }
        }
        return max;
    }

    // 861 翻转矩阵后的得分
    //有一个二维矩阵 A 其中每个元素的值为 0 或 1 。
// 移动是指选择任一行或列，并转换该行或列中的每一个值：将所有 0 都更改为 1，将所有 1 都更改为 0。
// 在做出任意次数的移动后，将该矩阵的每一行都按照二进制数来解释，矩阵的得分就是这些数字的总和。
// 返回尽可能高的分数。
    public int matrixScore(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        // m行首列全变成1,对总分的贡献值是m * (1 << (n - 1))
        int ret = m * (1 << (n - 1));

        // 对每一列,决定是否翻转
        for (int j = 1; j < n; j++) {
            int nOnes = 0;
            for (int i = 0; i < m; i++) {
                if (grid[i][0] == 1) {
                    nOnes += grid[i][j];
                } else {
                    nOnes += (1 - grid[i][j]); // 如果这一行进行了行反转，则该元素的实际取值为 1 - grid[i][j]
                }
            }
            int k = Math.max(nOnes, m - nOnes);
            ret += k * (1 << (n - j - 1));
        }
        return ret;
    }

    //954 二倍数对数组
    // 题目本质上是问 n个元素  能否分成 n/2  对元素，每对元素中一个数是另一个数的两倍。
    public boolean canReorderDoubled(int[] arr) {
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int x : arr) {
            cnt.put(x, cnt.getOrDefault(x, 0) + 1);
        }
        if (cnt.getOrDefault(0, 0) % 2 != 0) {
            return false;
        }

        List<Integer> vals = new ArrayList<>(cnt.keySet());
        vals.sort(Comparator.comparingInt(Math::abs));

        for (int x : vals) {
            if (cnt.getOrDefault(2 * x, 0) < cnt.get(x)) { // 无法找到足够的 2x 与 x 配对
                return false;
            }
            cnt.put(2 * x, cnt.getOrDefault(2 * x, 0) - cnt.get(x));
        }
        return true;
    }


    // 484 寻找排列
    public int[] findPermutation(String s) {
        int n = s.length();
        int[] nums = new int[n + 1];
        int i = 0, cur = 1;
        while (i < n) {
            if (i > 0 && s.charAt(i) == 'I') i++;
            for (; i < n && s.charAt(i) == 'I'; i++) nums[i] = cur++;
            int i0 = i;
            while (i < n && s.charAt(i) == 'D') i++;
            for (int j = i; j >= i0; j--) nums[j] = cur++;
        }
        return nums;
    }

    // 6150 根据模式串构造最小数字
    public String smallestNumber(String pattern) {
        int i = 0, n = pattern.length();
        char cur = '1';
        char[] ans = new char[n + 1];
        while (i < n) {
            // DI 模式中I已经排过
            if (i > 0 && pattern.charAt(i) == 'I') ++i;
            for (; i < n && pattern.charAt(i) == 'I'; ++i) ans[i] = cur++;
            int i0 = i;
            while (i < n && pattern.charAt(i) == 'D') ++i;
            // DI模式，倒序排
            for (int j = i; j >= i0; --j) ans[j] = cur++;
        }
        return new String(ans);
    }

    // 2170 使数组变成交替数组的最小操作数
    public int minimumOperations(int[] nums) {
        int n = nums.length;
        int[] oddCnt = new int[100010];
        int[] evenCnt = new int[100010];
        int a = 0, b = 0, c = 0, d = 0;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                evenCnt[nums[i]]++;
                if (a == 0 || evenCnt[nums[i]] > evenCnt[a]) {
                    b = a;
                    a = nums[i];
                } else if (nums[i] != a && (b == 0 || evenCnt[nums[i]] > evenCnt[b])) {
                    b = nums[i];
                }
            } else {
                oddCnt[nums[i]]++;
                if (c == 0 || oddCnt[nums[i]] > oddCnt[c]) {
                    d = c;
                    c = nums[i];
                } else if (nums[i] != c && (d == 0 || oddCnt[nums[i]] > oddCnt[d])) {
                    d = nums[i];
                }
            }
        }
        if (a != c) return n - evenCnt[a] - oddCnt[c];
        else return n - Math.max(evenCnt[a] + oddCnt[d], oddCnt[c] + evenCnt[b]);
    }


    //2765. 最长交替子数组
    public int alternatingSubarray(int[] nums) {
        int n = nums.length;
        int max = -1;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int len = j - i + 1;
                if (nums[j] - nums[i] == (len - 1) % 2) {
                    max = Math.max(max, len);
                } else {
                    break;
                }
            }
        }
        return max;
    }

    public int alternatingSubarray2(int[] nums) {
        int res = -1;
        int n = nums.length;
        int firstIndex = 0;
        for (int i = 1; i < n; i++) {
            int length = i - firstIndex + 1;
            if (nums[i] - nums[firstIndex] == (length - 1) % 2) {
                res = Math.max(res, length);
            } else {
                if (nums[i] - nums[i - 1] == 1) {
                    firstIndex = i - 1;
                    res = Math.max(res, 2);
                } else {
                    firstIndex = i;
                }
            }
        }
        return res;
    }
    //2598. 执行操作后的最大 MEX
    public int findSmallestInteger(int[] nums, int value) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            int r = (num % value + value) % value;
            map.put(r, map.getOrDefault(r, 0) + 1);
        }
        int ans = 0;
        int r = ans % value;
        while (map.containsKey(r)) {
            map.put(r, map.get(r) - 1);
            if (map.get(r) == 0) map.remove(r);
            ans++;
            r = ans % value;
        }
        return ans;
    }

    //2673. 使二叉树所有路径值相等的最小代价
    public int minIncrements(int n, int[] cost) {
        int ans = 0;
        for (int i = n / 2; i > 0; i--) { // 从最后一个非叶节点开始算
            ans += Math.abs(cost[i * 2 - 1] - cost[i * 2]); // 两个子节点变成一样的
            cost[i - 1] += Math.max(cost[i * 2 - 1], cost[i * 2]); // 累加路径和
        }
        return ans;
    }

    // endregion--------------------------------------------------------------------------------------------------
}
