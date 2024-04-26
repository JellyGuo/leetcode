import java.util.*;

public class SolutionSlideWindow {
    //region--------------------------------------------------------------------滑动窗口---------------------------------------------

    // 30 串联所有单词的字串
    public List<Integer> findSubstring(String s, String[] words) {
        ArrayList<Integer> result = new ArrayList<>();
        int wordNum = words.length;
        if (wordNum <= 0) {
            return result;
        }
        int length = words[0].length();
        HashMap<String, Integer> allWords = new HashMap<>();
        for (String word : words) {
            int count = allWords.getOrDefault(word, 0);
            allWords.put(word, (count + 1));
        }
        for (int i = 0; i + wordNum * length <= s.length(); i++) {
            int num = 0;
            HashMap<String, Integer> temp = new HashMap<>();
            while (num < wordNum) {
                String subString = s.substring(i + num * length, i + (num + 1) * length);
                if (allWords.containsKey(subString)) {
                    int ct = temp.getOrDefault(subString, 0);
                    temp.put(subString, ct + 1);
                    if (allWords.get(subString) < temp.get(subString)) {
                        break;
                    }
                } else {
                    break;
                }
                num++;
            }
            if (num == wordNum) {
                result.add(i);
            }
        }
        return result;
    }

    public List<Integer> findSubstringSW(String s, String[] words) {
        int n = s.length(), len = words[0].length();
        Map<String, Integer> map = new HashMap<>();
        for (String w : words) {
            map.put(w, map.getOrDefault(w, 0) + 1);
        }
        List<Integer> result = new ArrayList<>();
        // 从第一个单词的每个字母遍历，下一个单词的开始位置往后截取的单词会重复
        for (int i = 0; i < len; i++) {
            Map<String, Integer> tmp = new HashMap<>();
            for (int l = i, r = i; r + len <= n; r += len) {
                String cur = s.substring(r, r + len);
                //优化
//                if (!map.containsKey(cur)) {
//                    l = r + len;
//                    tmp.clear();
//                    continue;
//                }
                tmp.put(cur, tmp.getOrDefault(cur, 0) + 1);
                // 根据map中的数量移动l
                while (tmp.getOrDefault(cur, 0) > map.getOrDefault(cur, 0)) {
                    String remove = s.substring(l, l + len);
                    tmp.put(remove, tmp.get(remove) - 1);
                    if (tmp.get(remove) == 0) tmp.remove(remove);
                    l += len;
                }
                if (tmp.equals(map)) result.add(l);
            }
        }

        return result;
    }

    // 159 至多包含两个不同字符的最长串
    public int lengthOfLongestSubstringTwoDistinct(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int max = Integer.MIN_VALUE;
        for (int l = 0, r = 0; r < s.length(); r++) {
            char rc = s.charAt(r);
            map.put(rc, map.getOrDefault(rc, 0) + 1);
            while (map.size() > 2) {
                char lc = s.charAt(l++);
                map.put(lc, map.getOrDefault(lc, 0) - 1);
                if (map.get(lc) == 0) map.remove(lc);
            }
            max = Math.max(max, r - l + 1);
        }
        return max;
    }

    //面试题 17.18. 最短超串
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

    // 713 乘积小于k 的子数组
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        int res = 0;
        int mul = 1;
        if (k <= 1) return 0;
        for (int left = 0, right = 0; right < nums.length; right++) {
            mul *= nums[right];
            while (mul >= k) {
                mul /= nums[left];
                left++;
            }
            //长度即是贡献值
            // 2 3 4
            // 1+2+3 (连续，24不算)
            res += right - left + 1;
        }
        return res;
    }

    //283 移动零 双指针
    //给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
    // 1 0 1 1 0 1
    public void moveZeroes(int[] nums) {
        // l表示非0元素的个数，r是遍历元素的个数
        for (int l = 0, r = 0; r < nums.length; r++) {
            if (nums[r] != 0) {
                if (l != r) {
                    swap(nums, l, r);
                }
                l++;
            }
        }
    }

    public void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    //1423 可获得的最大点数
    public int maxScore(int[] cardPoints, int k) {
        int[] validNum = new int[2 * k];
        System.arraycopy(cardPoints, cardPoints.length - k, validNum, 0, k);
        System.arraycopy(cardPoints, 0, validNum, k, k);
        for (int i = cardPoints.length - k; i < cardPoints.length + k; i++) {
            validNum[i - cardPoints.length + k] = cardPoints[i % cardPoints.length];
        }
        int sum = 0, max = 0;
        for (int l = 0, r = 0; r < 2 * k; r++) {
            sum += validNum[r];
            if (r - l + 1 > k) {
                sum -= validNum[l];
                l++;
            }
            max = Math.max(max, sum);
        }
        return max;
    }

    // 求最大转化为求连续的最小 k个循环连续最大 = n-k个连续最小
    public int maxScore2(int[] cardPoints, int k) {
        int n = cardPoints.length;
        int sum = 0, min = Integer.MAX_VALUE;
        for (int l = 0, r = 0; r < n; r++) {
            sum += cardPoints[r];
            if (r - l + 1 > n - k) {
                sum -= cardPoints[l];
                l++;
            }
            if (r - l + 1 == n - k) min = Math.min(sum, min);
        }
        return Arrays.stream(cardPoints).sum() - min;
    }

    // 1658 将x减到0的最小操作数 前后取转换为求当中连续的
    public int minOperations(int[] nums, int x) {
        int n = nums.length;
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        int target = sum - x;
        if (target < 0) return -1;
        if (target == 0) return n;
        sum = 0;
        int max = 0;
        for (int l = 0, r = 0; r < n; r++) {
            sum += nums[r];
            while (sum > target) {
                sum -= nums[l++];
            }
            if (sum == target) {
                max = Math.max(max, r - l + 1);
            }
        }
        return max == 0 ? -1 : n - max;
    }

    // 2516. 每种字符至少取 K 个
    // 至少取k个=》剩余的最多保留cnt-k个
    // 求取的长度最小=》剩余的长度最大
    public int takeCharacters(String s, int k) {
        int n = s.length();
        char[] chars = s.toCharArray();
        int[] cnt = new int[3];
        for (char c : chars) {
            cnt[c - 'a']++;
        }
        int ta = cnt[0] - k, tb = cnt[1] - k, tc = cnt[2] - k;
        if (ta < 0 || tb < 0 || tc < 0) return -1;
        int max = 0;
        cnt = new int[3];
        for (int l = 0, r = 0; r < n; r++) {
            cnt[chars[r] - 'a']++;
            while (!takeCharactersCheck(cnt, ta, tb, tc)) {
                cnt[chars[l++] - 'a']--;
            }
            max = Math.max(max, r - l + 1);

        }
        return n - max;
    }

    private boolean takeCharactersCheck(int[] cnt, int ta, int tb, int tc) {
        return cnt[0] <= ta && cnt[1] <= tb && cnt[2] <= tc;
    }

    // 1052 爱生气的书店老板
    public int maxSatisfied(int[] customers, int[] grumpy, int minutes) {
        int n = customers.length;
        int sum = 0, max = 0;
        for (int i = 0; i < n; i++) {
            if (grumpy[i] == 0) {
                sum += customers[i];
                customers[i] = 0;
            }
        }
        for (int l = 0, r = 0; r < n; r++) {
            sum += customers[r];
            if (r - l + 1 > minutes) {
                sum -= customers[l++];
            }
            max = Math.max(sum, max);
        }
        return max;
    }

    // 904 水果成篮
    public int totalFruit(int[] fruits) {
        int n = fruits.length;
        Map<Integer, Integer> map = new HashMap<>();
        int max = 0;
        for (int l = 0, r = 0; r < n; r++) {
            map.put(fruits[r], map.getOrDefault(fruits[r], 0) + 1);
            while (map.size() > 2) {
                map.put(fruits[l], map.get(fruits[l]) - 1);
                if (map.get(fruits[l]) == 0) {
                    map.remove(fruits[l]);
                }
                l++;
            }
            max = Math.max(max, r - l + 1);
        }
        return max;
    }

    // 1438 绝对差不超过限制的最长连续子数组
// 给你一个整数数组 nums ，和一个表示限制的整数 limit，请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit 。
// 如果不存在满足条件的子数组，则返回 0 。
// 输入：nums = [8,2,4,7], limit = 4
// 输出：2
    public int longestSubarrayTreeMap(int[] nums, int limit) {
        int max = 0;
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int l = 0, r = 0; r < nums.length; r++) {
            map.put(nums[r], map.getOrDefault(nums[r], 0) + 1);
            while (map.lastKey() - map.firstKey() > limit) {
                map.put(nums[l], map.get(nums[l]) - 1);
                if (map.get(nums[l]) == 0) {
                    map.remove(nums[l]);
                }
                l++;
            }
            max = Math.max(max, r - l + 1);
        }
        return max;
    }

    public int longestSubarrayQueue(int[] nums, int limit) {
        int max = 0;
        Deque<Integer> maxQueue = new LinkedList<>();
        Deque<Integer> minQueue = new LinkedList<>();
        for (int l = 0, r = 0; r < nums.length; r++) {
            //单调队列模板
            while (!maxQueue.isEmpty() && maxQueue.peekLast() < nums[r]) {
                maxQueue.pollLast();
            }
            while (!minQueue.isEmpty() && minQueue.peekLast() > nums[r]) {
                minQueue.pollLast();
            }
            maxQueue.offerLast(nums[r]);
            minQueue.offerLast(nums[r]);
            while (!maxQueue.isEmpty() && !minQueue.isEmpty() && maxQueue.peekFirst() - minQueue.peekFirst() > limit) {
                // 单调队列，保证小于最大值的下标对应的值都不会存在
                // eg：7 8 5 3 不会存在 8移除后7还在队列的情况
                if (nums[l] == maxQueue.peekFirst()) {
                    maxQueue.pollFirst();
                }
                if (nums[l] == minQueue.peekFirst()) {
                    minQueue.pollFirst();
                }
                l++;
            }
            max = Math.max(max, r - l + 1);
        }
        return max;
    }

    //1759. 统计同构子字符串的数目
    public int countHomogenous(String s) {
        int mod = (int) 1e9 + 7;
        int n = s.length();
        long ans = 0;
        for (int l = 0, r = 0; r < n; r++) {
            while (s.charAt(l) != s.charAt(r)) {
                l++;
            }
            ans = (ans + (r - l + 1) % mod) % mod;
        }
        return (int) ans;
    }

    //2379. 得到 K 个黑块的最少涂色次数
    public int minimumRecolors(String blocks, int k) {
        int n = blocks.length();
        char[] chars = blocks.toCharArray();
        int min = Integer.MAX_VALUE;
        int cnt = 0;
        for (int l = 0, r = 0; r < n; r++) {
            if (chars[r] == 'W') {
                cnt++;
            }
            while (r - l + 1 > k) {
                if (chars[l] == 'W') {
                    cnt--;
                }
                l++;
            }
            if (r - l + 1 == k) {
                min = Math.min(min, cnt);
            }
        }
        return min;
    }

    // 485 最大连续1的个数
    public int findMaxConsecutiveOnes(int[] nums) {
        int maxCount = 0, count = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) {
                count++;
            } else {
                maxCount = Math.max(maxCount, count);
                count = 0;
            }
        }
        maxCount = Math.max(maxCount, count);
        return maxCount;
    }


    //1004 最大连续1的个数 LIS
    //给定一个二进制数组 nums 和一个整数 k，如果可以翻转最多 k 个 0 ，则返回 数组中连续 1 的最大个数 。
    public int longestOnes(int[] nums, int k) {
        int maxLength = Integer.MIN_VALUE;
        int count = 0;
        for (int l = 0, r = 0; r < nums.length; r++) {
            if (nums[r] == 0) count++;
            while (count > k) {
                if (nums[l] == 0) count--;
                l++;
            }
            maxLength = Math.max(maxLength, r - l + 1);
        }
        return maxLength;
    }

    public int longestOnesBinarySearch(int[] nums, int k) {
        int n = nums.length;
        int ans = 0;
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) sum[i] = sum[i - 1] + nums[i - 1];
        for (int i = 0; i < n; i++) {
            //枚举右端点
            int l = 0, r = i;
            while (l < r) {
                int mid = l + r >> 1;
                // 找满足0的个数小于等于k的最远左端点
                if (check(sum, mid, i, k)) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            if (check(sum, r, i, k)) ans = Math.max(ans, i - r + 1);
        }
        return ans;
    }

    boolean check(int[] sum, int l, int r, int k) {
        int tol = sum[r + 1] - sum[l], len = r - l + 1;
        return len - tol <= k;
    }

    // 面试05.03 翻转数位
    public int reverseBits(int num) {
        if (num >= 0) return getMaxChar(Integer.toBinaryString(~num), '1');
        return getMaxChar(Integer.toBinaryString(num), '0');
    }

    private int getMaxChar(String s, char c) {
        int max = 0, cnt = 0;
        for (int l = 0, r = 0; r < s.length(); r++) {
            if (s.charAt(r) == c) cnt++;
            while (cnt > 1) {
                if (s.charAt(l) == c) cnt--;
                l++;
            }
            max = Math.max(max, r - l + 1);
        }
        return max;
    }

    // 2024 考试的最大困惑度
    public int maxConsecutiveAnswers(String answerKey, int k) {
        char[] chars = answerKey.toCharArray();
        int max = 0, f_count = 0;
        for (int l = 0, r = 0; r < chars.length; r++) {
            if (chars[r] == 'F') f_count++;
            //if 也可以
            while (f_count > k && r - l + 1 - f_count > k) {
                if (chars[l] == 'F') f_count--;
                l++;
            }
            max = Math.max(max, r - l + 1);
        }
        return max;
    }

    //1208 尽可能使字符串相等
    public int equalSubstring(String s, String t, int maxCost) {
        char[] s_chars = s.toCharArray(), t_chars = t.toCharArray();
        int max = 0;
        int cost = 0;
        for (int l = 0, r = 0; r < s.length(); r++) {
            cost += Math.abs(s_chars[r] - t_chars[r]);
            while (cost > maxCost) {
                cost -= Math.abs(s_chars[l] - t_chars[l]);
                l++;
            }
            max = Math.max(max, r - l + 1);
        }
        return max;
    }

    public int equalSubstringBinarySearch(String s, String t, int maxCost) {
        int n = s.length();
        int[] accDiff = new int[n + 1];
        for (int i = 0; i < n; i++) {
            accDiff[i + 1] = accDiff[i] + Math.abs(s.charAt(i) - t.charAt(i));
        }
        int maxLength = 0;
        for (int i = 1; i <= n; i++) {
            // target>=accDiff[i] - maxCost => accDiff[i]-target<= maxCost 最远的start
            int start = binarySearch(accDiff, i, accDiff[i] - maxCost);
            maxLength = Math.max(maxLength, i - start);
        }
        return maxLength;
    }

    private int binarySearch(int[] accDiff, int endIndex, int target) {
        int low = 0, high = endIndex;
        while (low < high) {
            int mid = (high - low) / 2 + low;
            if (accDiff[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    //2106. 摘水果 hard
    public int maxTotalFruits(int[][] fruits, int startPos, int k) {
        int n = fruits.length;
        int sum = 0;
        int max = 0;
        for (int l = 0, r = 0; r < n; r++) {
            sum += fruits[r][1];
            while (l <= r && step(fruits, startPos, l, r) > k) {
                sum -= fruits[l][1];
                l++;
            }
            max = Math.max(max, sum);
        }
        return max;
    }

    // 覆盖[l,r]区间的最小步数
    // s<l => r-s  s>r => s-l
    // l<s<r => min(s-l,r-s)+r-l
    private int step(int[][] fruits, int startPos, int l, int r) {
        return Math.min(Math.abs(fruits[r][0] - startPos), Math.abs(startPos - fruits[l][0])) + fruits[r][0] - fruits[l][0];
    }

    //1838. 最高频元素的频数
    // sum = [1,3,7] r=2的时候，r前面有r-l个数字，全都需要补成nums[r] 的话需要nums[r]*(r-l)-sum次
    public int maxFrequency(int[] nums, int k) {
        Arrays.sort(nums);
        int max = 0, sum = 0;
        for (int l = 0, r = 0; r < nums.length; r++) {
            while (nums[r] * (r - l) - sum > k) {
                sum -= nums[l];
                l++;
            }
            sum += nums[r];
            max = Math.max(r - l + 1, max);
        }
        return max;
    }

    //1984 学生分数的最小差值
    public int minimumDifference(int[] nums, int k) {
        Arrays.sort(nums);
        int min = Integer.MAX_VALUE;
        for (int l = 0, r = k - 1; r < nums.length; l++, r++) {
            min = Math.min(min, nums[r] - nums[l]);
        }
        return min;
    }


    //424 替换后的最长重复字符
    //给你一个字符串 s 和一个整数 k 。你可以选择字符串中的任一字符，并将其更改为任何其他大写英文字符。该操作最多可执行 k 次。
// 在执行上述操作后，返回包含相同字母的最长子字符串的长度。
    public int characterReplacement(String s, int k) {
        int left = 0, right = 0, n = s.length();
        int[] count = new int[26];
        int maxCount = 0;
        while (right < n) {
            int idx = s.charAt(right) - 'A';
            count[idx]++;
            maxCount = Math.max(maxCount, count[idx]);

            int len = right - left + 1;
            //现在窗口长度>最多出现的字符X的次数+k (把X外的全换K次也不够现在的长度），左窗口移动
            //虽然这样的操作会导致部分区间不符合条件，即该区间内非最长重复字符超过了 k 个。但是这样的区间也同样不可能对答案产生贡献
            // AAABCA k=1
            if (len > maxCount + k) {
                count[s.charAt(left) - 'A']--;
                left++;
            }
            right++;
        }
        return right - 1 - left + 1;
    }

    public int characterReplacement2(String s, int k) {
        int n = s.length();
        int[] cnt = new int[26];
        int maxCnt = 0, maxLen = 0;
        for (int l = 0, r = 0; r < n; r++) {
            cnt[s.charAt(r) - 'A']++;
            maxCnt = Math.max(maxCnt, cnt[s.charAt(r) - 'A']);
            int len = r - l + 1;
            if (len > maxCnt + k) {
                // l 右移会改变maxCnt，但是不会改变结果，原因是 maxLen取决于maxCnt，如果maxCnt减小，所得Len不可能是maxLen
                cnt[s.charAt(l++) - 'A']--;
            }
            maxLen = Math.max(maxLen, r - l + 1);
        }
        return maxLen;
    }

    //480 滑动窗口中位数
    //中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
// [2,3,4]，中位数是 3
// [2,3]，中位数是 (2 + 3) / 2 = 2.5
// 给你一个数组 nums，有一个长度为 k 的窗口从最左端滑动到最右端。窗口中有 k 个数，每次窗口向右移动 1 位。你的任务是找出每次窗口移动后得到的新窗
//口中元素的中位数，并输出由它们组成的数组。
// 给出 nums = [1,3,-1,-3,5,3,6,7]，以及 k = 3。
//窗口位置                      中位数
//---------------               -----
//[1  3  -1] -3  5  3  6  7       1
// 1 [3  -1  -3] 5  3  6  7      -1
// 1  3 [-1  -3  5] 3  6  7      -1
// 1  3  -1 [-3  5  3] 6  7       3
// 1  3  -1  -3 [5  3  6] 7       5
// 1  3  -1  -3  5 [3  6  7]      6
    //暴力解法
    public double[] medianSlidingWindow(int[] nums, int k) {
        double[] result = new double[nums.length - k + 1];
        for (int i = 0; i <= nums.length - k; i++) {
            int[] copy = Arrays.copyOfRange(nums, i, i + k);
            Arrays.sort(copy);
            if (k % 2 == 0) {
                result[i] = ((long) copy[k / 2] + (long) copy[k / 2 - 1]) / 2.0;
            } else {
                result[i] = copy[k / 2];
            }
        }
        return result;
    }

    //双优先队列
    public double[] medianSlidingWindowDualHeap(int[] nums, int k) {
        DualHeap dh = new DualHeap(k);
        for (int i = 0; i < k; ++i) {
            dh.insert(nums[i]);
        }
        double[] ans = new double[nums.length - k + 1];
        ans[0] = dh.getMedian();
        for (int i = k; i < nums.length; ++i) {
            dh.insert(nums[i]);
            dh.erase(nums[i - k]);
            ans[i - k + 1] = dh.getMedian();
        }
        return ans;
    }

    // 532 数组中的k-diff数对
    public int findPairs(int[] nums, int k) {
        int n = nums.length;
        Arrays.sort(nums);
        Map<Integer, Integer> map = new HashMap<>();
        for (int l = 0, r = 0; r < n; r++) {
            while (Math.abs(nums[r] - nums[l]) > k) l++;
            if (l < r && Math.abs(nums[r] - nums[l]) == k) map.put(nums[l], nums[r]);
        }
        return map.size();
    }

    // O(n) 哈希做法
    public int findPairsHash(int[] nums, int k) {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> res = new HashSet<>();
        for (int num : nums) {
            if (visited.contains(num - k)) {
                res.add(num - k);
            }
            if (visited.contains(num + k)) {
                res.add(num);
            }
            visited.add(num);
        }
        return res.size();
    }

    // 14 最长公共前缀 LCS
    // 横向扫描O(mn)
    public String longestCommonPrefix(String[] strs) {
        if (strs.length <= 0) return "";
        if (strs.length == 1) return strs[0];
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            prefix = getCommon(prefix, strs[i]);
            if (prefix.length() == 0) break;
        }
        return prefix;
    }

    private String getCommon(String str1, String str2) {
        int len = Math.min(str1.length(), str2.length());
        int index = 0;
        while (index < len && str1.charAt(index) == str2.charAt(index)) {
            index++;
        }
        return str1.substring(0, index);
    }

    // 纵向扫描
    public String longestCommonPrefix2(String[] strs) {
        int n = strs.length;
        if (n <= 0) return "";
        int length = strs[0].length();
        for (int i = 0; i < length; i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < n; j++) {
                if (i == strs[j].length() || strs[j].charAt(i) != c) {
                    return strs[0].substring(0, i);
                }
            }
        }
        return strs[0];
    }

    // 分治
    public String longestCommonPrefix3(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        } else {
            return longestCommonPrefix(strs, 0, strs.length - 1);
        }
    }

    public String longestCommonPrefix(String[] strs, int start, int end) {
        if (start == end) {
            return strs[start];
        } else {
            int mid = (end - start) / 2 + start;
            String lcpLeft = longestCommonPrefix(strs, start, mid);
            String lcpRight = longestCommonPrefix(strs, mid + 1, end);
            return commonPrefix(lcpLeft, lcpRight);
        }
    }

    public String commonPrefix(String lcpLeft, String lcpRight) {
        int minLength = Math.min(lcpLeft.length(), lcpRight.length());
        for (int i = 0; i < minLength; i++) {
            if (lcpLeft.charAt(i) != lcpRight.charAt(i)) {
                return lcpLeft.substring(0, i);
            }
        }
        return lcpLeft.substring(0, minLength);
    }

    // 二分
    public String longestCommonPrefix4(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        int minLength = Integer.MAX_VALUE;
        for (String str : strs) {
            minLength = Math.min(minLength, str.length());
        }
        int low = 0, high = minLength;
        while (low < high) {
            // 小于等于长度的最大值
            int mid = (high - low + 1) / 2 + low;
            if (isCommonPrefix(strs, mid)) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return strs[0].substring(0, low);
    }

    public boolean isCommonPrefix(String[] strs, int length) {
        String str0 = strs[0].substring(0, length);
        int count = strs.length;
        for (int i = 1; i < count; i++) {
            String str = strs[i];
            for (int j = 0; j < length; j++) {
                if (str0.charAt(j) != str.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }


    // // LIS LCS LUS LHS
    //594 最长和谐子序列 LHS
    //和谐数组是指一个数组里元素的最大值和最小值之间的差别 正好是 1 。
// 现在，给你一个整数数组 nums ，请你在所有可能的子序列中找到最长的和谐子序列的长度。
// 数组的子序列是一个由数组派生出来的序列，它可以通过删除一些元素或不删除元素、且不改变其余元素的顺序而得到。
//输入：nums = [1,3,2,2,5,2,3,7] 输出：5
//解释：最长的和谐子序列是 [3,2,2,2,3]
    public int findLHSSort(int[] nums) {
        Arrays.sort(nums);
        int max = 0;
        for (int l = 0, r = 0; r < nums.length; r++) {
            if (nums[r] - nums[l] > 1) {
                l++;
            }
            if (nums[r] - nums[l] == 1) {
                max = Math.max(max, r - l + 1);
            }
        }
        return max;
    }

    public int findLHSHash(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        int max = 0;
        for (int num : map.keySet()) {
            if (map.containsKey(num + 1)) {
                max = Math.max(map.get(num) + map.get(num + 1), max);
            }
        }
        return max;
    }

    //76 最小覆盖子串
    //给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
    public String minWindow(String s, String t) {
        Map<Character, Integer> tmap = new HashMap<>();
        for (Character c : t.toCharArray()) {
            tmap.put(c, tmap.getOrDefault(c, 0) + 1);
        }
        Map<Character, Integer> smap = new HashMap<>();
        int count = 0;
        int minLen = Integer.MAX_VALUE;
        int start = -1;
        for (int l = 0, r = 0; r < s.length(); r++) {
            Character c = s.charAt(r);
            if (tmap.containsKey(c)) {
                smap.put(c, smap.getOrDefault(c, 0) + 1);
                if (smap.get(c).intValue() == tmap.get(c).intValue()) {
                    count++;
                }
            }
            while (count == tmap.size()) {
                int curLen = r - l + 1;
                if (curLen < minLen) {
                    minLen = curLen;
                    start = l;
                }
                Character lc = s.charAt(l);
                if (tmap.containsKey(lc)) {
                    if (tmap.get(lc).intValue() == smap.get(lc).intValue()) {
                        count--;
                    }
                    smap.put(lc, smap.get(lc) - 1);
                }
                l++;
            }
        }
        return start != -1 ? s.substring(start, start + minLen) : "";
    }

    public String minWindow2(String s, String t) {
        char[] chars = s.toCharArray(), chart = t.toCharArray();
        int n = chars.length, m = chart.length;

        int[] hash = new int[128];
        for (char ch : chart) hash[ch]--;

        String res = "";
        for (int i = 0, j = 0, cnt = 0; i < n; i++) {
            hash[chars[i]]++;
            //为正说明s有t没有，为负说明t的个数不止一个，为0说明t只有一个，<=0说明t都有
            if (hash[chars[i]] <= 0) cnt++;
            while (cnt == m && hash[chars[j]] > 0) hash[chars[j++]]--;
            if (cnt == m)
                if (res.equals("") || res.length() > i - j + 1)
                    res = s.substring(j, i + 1);
        }
        return res;
    }

    // 187 重复的DNA序列
    // 给定一个表示 DNA序列 的字符串 s ，返回所有在 DNA 分子中出现不止一次的 长度为 10 的序列(子字符串)。你可以按 任意顺序 返回答案。
    public List<String> findRepeatedDnaSequences(String s) {
        Set<String> result = new HashSet<>();
        Set<String> set = new HashSet<>();
        for (int l = 0, r = 0; r < s.length(); r++) {
            int len = r - l + 1;
            if (len == 10) {
                String tmp = s.substring(l, r + 1);
                if (set.contains(tmp)) result.add(tmp);
                set.add(tmp);
                l++;
            }
        }
        return new ArrayList<>(result);
    }

    //689. 三个无重叠子数组的最大和
    //单个子数组最大和
    public int[] maxSumOfOneSubarray(int[] nums, int k) {
        int[] ans = new int[1];
        int sum1 = 0, maxSum1 = 0;
        for (int i = 0; i < nums.length; ++i) {
            sum1 += nums[i];
            if (i >= k - 1) {
                if (sum1 > maxSum1) {
                    maxSum1 = sum1;
                    ans[0] = i - k + 1;
                }
                sum1 -= nums[i - k + 1];
            }
        }
        return ans;
    }

    //两个无重叠子数组最大和
    public int[] maxSumOfTwoSubarrays(int[] nums, int k) {
        int[] ans = new int[2];
        int sum1 = 0, maxSum1 = 0, maxSum1Idx = 0;
        int sum2 = 0, maxSum12 = 0;
        for (int i = k; i < nums.length; ++i) {
            sum1 += nums[i - k];
            sum2 += nums[i];
            if (i >= k * 2 - 1) {
                if (sum1 > maxSum1) {
                    maxSum1 = sum1;
                    maxSum1Idx = i - k * 2 + 1;
                }
                if (maxSum1 + sum2 > maxSum12) {
                    maxSum12 = maxSum1 + sum2;
                    ans[0] = maxSum1Idx;
                    ans[1] = i - k + 1;
                }
                sum1 -= nums[i - k * 2 + 1];
                sum2 -= nums[i - k + 1];
            }
        }
        return ans;
    }

    public int[] maxSumOfThreeSubarrays(int[] nums, int k) {
        int[] ans = new int[3];
        int sum1 = 0, maxSum1 = 0, maxSum1Idx = 0;
        int sum2 = 0, maxSum12 = 0, maxSum12Idx1 = 0, maxSum12Idx2 = 0;
        int sum3 = 0, maxTotal = 0;
        for (int i = k * 2; i < nums.length; ++i) {
            sum1 += nums[i - k * 2];
            sum2 += nums[i - k];
            sum3 += nums[i];
            if (i >= k * 3 - 1) {
                if (sum1 > maxSum1) {
                    maxSum1 = sum1;
                    maxSum1Idx = i - k * 3 + 1;
                }
                if (maxSum1 + sum2 > maxSum12) {
                    maxSum12 = maxSum1 + sum2;
                    maxSum12Idx1 = maxSum1Idx;
                    maxSum12Idx2 = i - k * 2 + 1;
                }
                if (maxSum12 + sum3 > maxTotal) {
                    maxTotal = maxSum12 + sum3;
                    ans[0] = maxSum12Idx1;
                    ans[1] = maxSum12Idx2;
                    ans[2] = i - k + 1;
                }
                sum1 -= nums[i - k * 3 + 1];
                sum2 -= nums[i - k * 2 + 1];
                sum3 -= nums[i - k + 1];
            }
        }
        return ans;
    }

    //992 K个不同整数的子数组 Hard
    //给定一个正整数数组 nums和一个整数 k ，返回 num 中 「好子数组」 的数目。
// 如果 nums 的某个子数组中不同整数的个数恰好为 k，则称 nums 的这个连续、不一定不同的子数组为 「好子数组 」。
// 例如，[1,2,3,1,2] 中有 3 个不同的整数：1，2，以及 3。子数组 是数组的 连续 部分。
//输入：nums = [1,2,1,2,3], k = 2 输出：7
//解释：恰好由 2 个不同整数组成的子数组：[1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2].
    public int subarraysWithKDistinct(int[] nums, int k) {
        return atMostKDistinct(nums, k) - atMostKDistinct(nums, k - 1);
    }

    /**
     * @param A
     * @param K
     * @return 最多包含 K 个不同整数的子区间的个数
     */
    public int atMostKDistinct(int[] A, int K) {
        int len = A.length;
        int[] freq = new int[len + 1];

        int left = 0;
        int right = 0;
        // [left, right] 里不同整数的个数
        int count = 0;
        int res = 0;
        // [left, right] 包含不同整数的个数小于等于 K
        while (right < len) {
            if (freq[A[right]] == 0) {
                count++;
            }
            freq[A[right]]++;

            while (count > K) {
                freq[A[left]]--;
                if (freq[A[left]] == 0) {
                    count--;
                }
                left++;
            }
            // [left, right] 区间的长度就是对结果的贡献
            res += right - left + 1;
            right++;
        }
        return res;
    }

    public int subarraysWithKDistinct2(int[] nums, int k) {
        return helper(nums, k) - helper(nums, k - 1);
    }

    // 长度不超过k的所有子数组
    private int helper(int[] nums, int k) {
        int n = nums.length;
        Map<Integer, Integer> map = new HashMap<>();
        int ans = 0;
        for (int l = 0, r = 0; r < n; r++) {
            map.put(nums[r], map.getOrDefault(nums[r], 0) + 1);
            while (map.size() > k) {
                map.put(nums[l], map.get(nums[l]) - 1);
                if (map.get(nums[l]) == 0) {
                    map.remove(nums[l]);
                }
                l++;
            }
            ans += r - l + 1;
        }
        return ans;
    }

    //1234. 替换子串得到平衡字符串
    public int balancedString(String s) {
        int n = s.length();
        int t = n / 4;
        char[] chars = s.toCharArray();
        int[] cnt = new int[26];
        for (char c : chars) {
            cnt[c - 'A']++;
        }
        if (check(cnt, t)) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        for (int l = 0, r = 0; r < n; r++) {
            cnt[chars[r] - 'A']--;
            while (check(cnt, t)) {
                min = Math.min(min, r - l + 1);
                cnt[chars[l++] - 'A']++;
            }
        }
        return min;
    }

    private boolean check(int[] cnt, int t) {
        if (cnt['Q' - 'A'] > t || cnt['W' - 'A'] > t || cnt['E' - 'A'] > t || cnt['R' - 'A'] > t) {
            return false;
        }
        return true;
    }
    //2009. 使数组连续的最少操作数
    public int minOperations(int[] nums) {
        int n = nums.length;
        Set<Integer> set = new HashSet<Integer>();
        for (int num : nums) {
            set.add(num);
        }
        List<Integer> sortedUniqueNums = new ArrayList<Integer>(set);
        Collections.sort(sortedUniqueNums);
        int res = n;
        int j = 0;
        for (int i = 0; i < sortedUniqueNums.size(); i++) {
            int left = sortedUniqueNums.get(i);
            int right = left + n - 1;
            while (j < sortedUniqueNums.size() && sortedUniqueNums.get(j) <= right) {
                res = Math.min(res, n - (j - i + 1));
                j++;
            }
        }
        return res;
    }

    //2760. 最长奇偶子数组
    public int longestAlternatingSubarray(int[] nums, int threshold) {
        int n = nums.length;
        int max = 0;
        for (int l = 0, r = 0; r < n; r++) {
            if (nums[l] % 2 != 0 || nums[l] > threshold
                    || (r > l && nums[r] % 2 == nums[r - 1] % 2) || nums[r] > threshold) {
                l = r;
            }
            if (nums[l] % 2 == 0 && nums[l] <= threshold) {
                max = Math.max(r - l + 1, max);
            }
        }
        return max;
    }

    //2537. 统计好子数组的数目
    public long countGood(int[] nums, int k) {
        int n = nums.length;
        long ans = 0;
        for (int l = 0; l < n; l++) {
            Map<Integer, Integer> map = new HashMap<>();
            map.put(nums[l], 1);
            int cnt = 0;
            for (int r = l + 1; r < n; r++) {
                if (map.containsKey(nums[r])) cnt += map.get(nums[r]);
                map.put(nums[r], map.getOrDefault(nums[r], 0) + 1);
                if (cnt >= k) {
                    ans += n - r;
                    break;
                }
            }
        }
        return ans;
    }

    public long countGoodSW(int[] nums, int k) {
        int n = nums.length;
        long ans = 0;
        Map<Integer, Integer> map = new HashMap<>();
        int cnt = 0;
        for (int l = 0, r = 0; r < n; r++) {
            if (map.containsKey(nums[r])) cnt += map.get(nums[r]);
            map.put(nums[r], map.getOrDefault(nums[r], 0) + 1);
            while (cnt >= k) {
                ans += n - r;
                cnt -= map.get(nums[l]) - 1;
                map.put(nums[l], map.get(nums[l]) - 1);
                if (map.get(nums[l]) == 0) {
                    map.remove(nums[l]);
                }
                l++;
            }
        }
        return ans;
    }


    //2824. 统计和小于目标的下标对数目
    public int countPairs(List<Integer> nums, int target) {
        int n = nums.size();
        Collections.sort(nums);
        int ans = 0;
        for (int l = 0, r = n - 1; l < r; l++) {
            while (l < r && nums.get(l) + nums.get(r) >= target) {
                r--;
            }
            ans += r - l;
        }
        return ans;
    }

    // 1703 得到连续K个1的最少相邻交换次数 Hard toreview
    //https://leetcode.cn/problems/minimum-adjacent-swaps-for-k-consecutive-ones/solution/duo-tu-xin-shou-jiao-cheng-yi-bu-bu-dai-6bps4/
    public int minMoves(int[] nums, int k) {
        int n = nums.length;
        int[] pos = new int[n];
        int index = 0;
        // 例如：[1,0,0,1,0,1,1,1,0,1,1] => [0,3,5,6,7,9,10]
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) {
                pos[index++] = i;
            }
        }
        int ans = 0, count = 0, mid = k / 2;
        // 定长滑窗模板:
        // 1.计算第一个长度为k的窗口, k = 5, pos[i] - pos[i - 1] - 1表示相邻1之间0的个数
        // 把0移出窗口 = 0的个数 * 往左往右最小步数(左右1的个数)
        // [0,3,5,6,7] => (3-0-1)*1 + (5-3-1)*2 + (6-5-1)*2 + (7-6-1)*1 = 4
        for (int i = 1; i < k; i++) {
            count += (pos[i] - pos[i - 1] - 1) * Math.min(i, k - i);
        }
        ans = count;
        // 2.窗口按步长滑动 [0,3,5,6,7] => [3,5,6,7,9]
        // [0,3,5,6,7] => (3-0-1)*1 + (5-3-1)*2 + (6-5-1)*2 + (7-6-1)*1 = 4
        // [3,5,6,7,9] =>             (5-3-1)*1 + (6-5-1)*2 + (7-6-1)*2 + (9-7-1)*1 = 2
        // 通过对比发现：count -= (3-0-1)*1 + (5-3-1)*1 + (6-5-1)*0 = 3 - 0 + 5 - 3 + 1 + 1= 5 - 0 + 2
        //             count += (7-6-1)*1 + (9-7-1)*1 = 7 - 6 + 9 - 7 - 1 - 1 = 9 - 6 - 2
        // 类似于差分和前缀和的关系，加减2相互抵消，所以最后结果与窗口内两端点和中位数有关
        for (int i = k; i < index; i++) {
            count -= pos[i - k + mid] - pos[i - k]; // 上个区间中位数下标 - 上个区间左端点
            count += pos[i] - pos[i - mid]; // 当前区间右端点 - 当前区间中位数
            ans = Math.min(ans, count);
        }
        return ans;
    }

    //2367. 算术三元组的数目
    public int arithmeticTriplets(int[] nums, int diff) {
        int cnt = 0;
        Set<Integer> set = new HashSet<>();
        for (int n : nums) {
            set.add(n);
            if (set.contains(n - diff) && set.contains(n - 2 * diff)) {
                cnt++;
            }
        }
        return cnt;
    }

    // 128 最长连续序列
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }

        int longest = 0;
        for (int i : set) {
            if (!set.contains(i - 1)) {
                int cur = i;
                int curDistance = 1;
                while (set.contains(cur + 1)) {
                    cur += 1;
                    curDistance += 1;
                }
                longest = Math.max(curDistance, longest);
            }
        }
        return longest;
    }

    //无重复最长字串
    //给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
//输入: s = "abcabcbb"
//输出: 3
//解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3
    //滑动窗口
    public int lengthOfLongestSubstring(String s) {
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            Set<Character> set = new HashSet<>();
            int len = 0;
            for (int j = i; j < s.length(); j++) {
                if (!set.contains(s.charAt(j))) {
                    len++;
                    set.add(s.charAt(j));
                } else {
                    break;
                }
            }
            ans = Math.max(len, ans);
        }
        return ans;
    }

    public int lengthOfLongestSubstringSlidingWindow(String s) {
        int n = s.length();
        Set<Character> set = new HashSet<>();
        int max = 0;
        for (int l = 0, r = 0; r < n; r++) {
            while (!set.isEmpty() && set.contains(s.charAt(r))) {
                set.remove(s.charAt(l));
                l++;
            }
            set.add(s.charAt(r));
            max = Math.max(max, r - l + 1);
        }
        return max;
    }

    public int lengthOfLongestSubstringSlidingWindow2(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int len = 0;
        int left = 0;
        for (int i = 0; i < s.length(); i++) {
            //多个重复的时候取最右边的left
            if (map.containsKey(s.charAt(i))) left = Math.max(left, map.get(s.charAt(i)) + 1);
            map.put(s.charAt(i), i);
            len = Math.max(len, i - left + 1);
        }
        return len;
    }

    // 340 至多包含k个不同字符的最长子串
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        Map<Character, Integer> map = new HashMap<>();
        int n = s.length();
        char[] chars = s.toCharArray();
        int max = 0;
        for (int l = 0, r = 0; r < n; r++) {
            map.put(chars[r], map.getOrDefault(chars[r], 0) + 1);
            while (map.size() > k) {
                map.put(chars[l], map.get(chars[l]) - 1);
                if (map.get(chars[l]) == 0) {
                    map.remove(chars[l]);
                }
                l++;
            }
            max = Math.max(max, r - l + 1);
        }
        return max;
    }

    // 395 至少有k个重复字符的最长子串
    public int longestSubstring(String s, int k) {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            map.put(s.charAt(i), map.getOrDefault(s.charAt(i), 0) + 1);
        }
        for (char c : map.keySet()) {
            if (map.get(c) < k) {
                int max = 0;
                for (String ss : s.split(String.valueOf(c))) {
                    max = Math.max(longestSubstring(ss, k), max);
                }
                return max;
            }
        }
        return s.length();
    }

    public int longestSubstringSlidingWindow(String s, int k) {
        int ans = 0;
        int n = s.length();
        char[] cs = s.toCharArray();
        int[] cnt = new int[26];
        // 枚举26中可能性 答案子串的左边界左侧的字符以及右边界右侧的字符一定不会出现在子串中，否则就不会是最优解
        for (int p = 1; p <= 26; p++) {
            Arrays.fill(cnt, 0);
            // tot 代表 [j, i] 区间所有的字符种类数量；sum 代表满足「出现次数不少于 k」的字符种类数量
            for (int i = 0, j = 0, tot = 0, sum = 0; i < n; i++) {
                int u = cs[i] - 'a';
                cnt[u]++;
                // 如果添加到 cnt 之后为 1，说明字符总数 +1
                if (cnt[u] == 1) tot++;
                // 如果添加到 cnt 之后等于 k，说明该字符从不达标变为达标，达标数量 + 1
                if (cnt[u] == k) sum++;
                // 当区间所包含的字符种类数量 tot 超过了当前限定的数量 p，那么我们要删除掉一些字母，即「左指针」右移
                while (tot > p) {
                    int t = cs[j++] - 'a';
                    cnt[t]--;
                    // 如果添加到 cnt 之后为 0，说明字符总数-1
                    if (cnt[t] == 0) tot--;
                    // 如果添加到 cnt 之后等于 k - 1，说明该字符从达标变为不达标，达标数量 - 1
                    if (cnt[t] == k - 1) sum--;
                }
                // 当所有字符都符合要求，更新答案
                if (tot == sum) ans = Math.max(ans, i - j + 1);
            }
        }
        return ans;
    }


    // 643子数组最大平均数1
    public double findMaxAverage(int[] nums, int k) {
        double max = Double.NEGATIVE_INFINITY;
        double sum = 0;
        for (int l = 0, r = 0; r < nums.length; r++) {
            sum += nums[r];
            if (r - l + 1 > k) {
                sum -= nums[l++];
            }
            if (r - l + 1 == k) {
                max = Math.max(sum / k, max);
            }
        }
        return max;
    }

    // 219 存在重复元素2
    //给你一个整数数组 nums 和一个整数 k ，判断数组中是否存在两个 不同的索引 i 和 j ，满足 nums[i] == nums[j] 且 abs(i- j) <= k 。如果存在，返回 true ；否则，返回 false 。
    //滑动窗口
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (set.contains(nums[i])) {
                return true;
            }
            set.add(nums[i]);
            if (i >= k) {
                set.remove(nums[i - k]);
            }
        }
        return false;
    }

    public boolean containsNearbyDuplicate2(int[] nums, int k) {
        Set<Integer> set = new HashSet<>();
        for (int l = 0, r = 0; r < nums.length; r++) {
            if (set.contains(nums[r])) return true;
            set.add(nums[r]);
            if (r - l >= k) {
                set.remove(nums[l++]);
            }
        }
        return false;
    }

    //220 存在重复元素3
    //给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j，使得 abs(nums[i] - nums[j]) <=t ，同时又满足 abs(i - j) <= k 。
    public boolean containsNearbyAlmostDuplicateSW(int[] nums, int k, int t) {
        int n = nums.length;
        for (int l = 0, r = 1; l < n; r++) {
            if (r < n && r - l <= k) {
                if (Math.abs((long) nums[r] - (long) nums[l]) <= (long) t) return true;
            } else {
                l++;
                r = l;
            }
        }
        return false;
    }

    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        TreeSet<Long> set = new TreeSet<>();
        for (int i = 0; i < nums.length; i++) {
            long curr = nums[i];

            Long floor = set.floor(curr);
            Long celling = set.ceiling(curr);
            if (floor != null && curr - floor <= t) return true;
            if (celling != null && celling - curr <= t) return true;
            set.add(curr);
            if (i >= k) {
                set.remove((long) nums[i - k]);
            }
        }
        return false;
    }

    //分桶 桶排序
    public boolean containsNearbyAlmostDuplicate2(int[] nums, int k, int t) {
        long size = t + 1;
        Map<Long, Long> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            long curr = (long) nums[i];
            long idx = getIdx(curr, size);
            if (map.containsKey(idx)) return true;
            long left = idx - 1;
            long right = idx + 1;
            if (map.containsKey(left) && curr - map.get(left) <= t) return true;
            if (map.containsKey(right) && map.get(right) - curr <= t) return true;
            map.put(idx, curr);
            if (i >= k) {
                map.remove(getIdx((long) nums[i - k], size));
            }
        }
        return false;
    }

    // 负数 从-1开始 t=3时 -4..-1在一个桶 整体加1计算，序号整体左移
    private long getIdx(long num, long size) {
        return num >= 0 ? num / size : (num + 1) / size - 1;
    }

    // 209 长度最小的子数组
    //给定一个含有 n 个正整数的数组和一个正整数 target 。
// 找出该数组中满足其和 ≥ target 的长度最小的 连续子数组 [numsl, numsl+1, ..., numsr-1, numsr] ，并返回其长
//度。如果不存在符合条件的子数组，返回 0 。
//输入：target = 7, nums = [2,3,1,2,4,3]
//输出：2
//解释：子数组 [4,3] 是该条件下的长度最小的子数组。
    //滑动窗口
    public int minSubArrayLen(int target, int[] nums) {
        int n = nums.length;
        int min = Integer.MAX_VALUE;
        int sum = 0;
        for (int l = 0, r = 0; r < n; r++) {
            sum += nums[r];
            while (sum >= target) {
                min = Math.min(min, r - l + 1);
                sum -= nums[l];
                l++;
            }
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    // sum[i]-sum[j-1]>=t => sum[j-1]<=sum[i]-t
    public int minSubArrayLenBinarySearch(int target, int[] nums) {
        int n = nums.length;
        int min = Integer.MAX_VALUE;
        int[] sum = new int[n];
        sum[0] = nums[0];
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + nums[i];
        }
        for (int i = 0; i < n; i++) {
            // 找右边大于等于leftSum+target的最小的值
            int nearestRight = binarySearch(i, sum, target);
            if (nearestRight < i) continue;
            min = Math.min(nearestRight - i + 1, min);
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    private int binarySearch(int i, int[] sum, int target) {
        int leftSum = i == 0 ? 0 : sum[i - 1];
        int l = 0, r = sum.length - 1;
        while (l < r) {
            int mid = (l + r) >> 1;
            if (sum[mid] - leftSum < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return sum[l] - leftSum >= target ? l : -1;
    }

    public int minSubArrayLenBinarySearch2(int t, int[] nums) {
        int n = nums.length, ans = n + 10;
        int[] sum = new int[n + 10];
        for (int i = 1; i <= n; i++) sum[i] = sum[i - 1] + nums[i - 1];
        for (int i = 1; i <= n; i++) {
            int s = sum[i], d = s - t;
            int l = 0, r = i;
            while (l < r) {
                int mid = l + r + 1 >> 1;
                if (sum[mid] <= d) l = mid;
                else r = mid - 1;
            }
            if (sum[r] <= d) ans = Math.min(ans, i - r);
        }
        return ans == n + 10 ? 0 : ans;
    }

    //字符串所有字母异位词
    //滑动窗口  用全排列找所有异位词会超时 可统计字符次数，一致即为异位词
    public List<Integer> findAnagramsDFSTimeLimit(String s, String p) {
        List<String> combination = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean[] vis = new boolean[p.length()];
        dfs(p, 0, combination, sb, vis);
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i <= s.length() - p.length(); i++) {
            if (combination.contains(s.substring(i, i + p.length()))) result.add(i);
        }
        return result;
    }

    private void dfs(String p, int depth, List<String> set, StringBuilder sb, boolean[] vis) {
        if (depth == p.length()) {
            set.add(sb.toString());
            return;
        }
        for (int i = 0; i < p.length(); i++) {
            if (vis[i]) {
                continue;
            }
            if (i > 0 && p.charAt(i) == p.charAt(i - 1) && !vis[i - 1]) {
                continue;
            }
            sb.append(p.charAt(i));
            vis[i] = true;
            dfs(p, depth + 1, set, sb, vis);
            vis[i] = false;
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    //滑动窗口 统计字符次数
    public List<Integer> findAnagrams(String s, String p) {
        char[] s_count = new char[26];
        char[] p_count = new char[26];
        for (int i = 0; i < p.length(); i++) {
            p_count[p.charAt(i) - 'a']++;
        }
        List<Integer> result = new ArrayList<>();
        for (int l = 0, r = 0; r < s.length(); r++) {
            s_count[s.charAt(r) - 'a']++;
            if (r - l + 1 > p.length()) {
                s_count[s.charAt(l) - 'a']--;
                l++;
            }
            if (check(s_count, p_count)) result.add(l);
        }
        return result;
    }

    private boolean check(char[] s_count, char[] p_count) {
        for (int i = 0; i < 26; i++) {
            if (s_count[i] != p_count[i]) return false;
        }
        return true;
    }

    //优化
    public List<Integer> findAnagrams2(String s, String p) {
        List<Integer> ans = new ArrayList<>();
        int n = s.length(), m = p.length();
        int[] cnt = new int[26];
        for (int i = 0; i < m; i++) cnt[p.charAt(i) - 'a']++;
        int a = 0;
        for (int i = 0; i < 26; i++) if (cnt[i] != 0) a++;
        for (int l = 0, r = 0, b = 0; r < n; r++) {
            // 往窗口增加字符，进行词频的抵消操作，如果抵消后词频为 0，说明有一个新的字符词频与 p 完全相等
            if (--cnt[s.charAt(r) - 'a'] == 0) b++;
            // 若窗口长度超过规定，将窗口左端点右移，执行词频恢复操作，如果恢复后词频为 1（恢复前为 0），说明少了一个词频与 p 完全性相等的字符
            if (r - l + 1 > m && ++cnt[s.charAt(l++) - 'a'] == 1) b--;
            if (b == a) ans.add(l);
        }
        return ans;
    }

    // 242 有效的字母异位词
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        int[] cnt = new int[26];
        for (int i = 0; i < s.length(); i++) {
            cnt[s.charAt(i) - 'a']++;
            cnt[t.charAt(i) - 'a']--;
        }
        for (int i = 0; i < 26; i++) {
            if (cnt[i] != 0) return false;
        }
        return true;
    }

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);

            String ns = new String(chars);
            List<String> value = map.getOrDefault(ns, new ArrayList<>());
            value.add(str);
            map.put(ns, value);
        }
        return new ArrayList<>(map.values());
    }

    //567 字符串排列
    public boolean checkInclusion(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[] chars = new int[26];
        for (int i = 0; i < m; i++) {
            chars[s1.charAt(i) - 'a']--;
        }
        for (int l = 0, r = 0; r < n; r++) {
            chars[s2.charAt(r) - 'a']++;
            int len = r - l + 1;
            if (len == m) {
                if (check(chars)) return true;
                else chars[s2.charAt(l++) - 'a']--;
            }
        }
        return false;
    }

    private boolean check(int[] chars) {
        for (int i = 0; i < 26; i++) {
            if (chars[i] != 0) return false;
        }
        return true;
    }

    public boolean checkInclusionOptimize(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        if (n > m) {
            return false;
        }
        int[] cnt = new int[26];
        for (int i = 0; i < n; ++i) {
            --cnt[s1.charAt(i) - 'a'];
            ++cnt[s2.charAt(i) - 'a'];
        }
        int diff = 0;
        for (int c : cnt) {
            if (c != 0) {
                ++diff;
            }
        }
        if (diff == 0) {
            return true;
        }
        for (int i = n; i < m; ++i) {
            int x = s2.charAt(i) - 'a', y = s2.charAt(i - n) - 'a';
            if (x == y) {
                continue;
            }
            if (cnt[x] == 0) {
                ++diff;
            }
            ++cnt[x];
            if (cnt[x] == 0) {
                --diff;
            }
            if (cnt[y] == 0) {
                ++diff;
            }
            --cnt[y];
            if (cnt[y] == 0) {
                --diff;
            }
            if (diff == 0) {
                return true;
            }
        }
        return false;
    }

    // 249 移位字符串分组
    // 给定一个字符串，对该字符串可以进行 “移位” 的操作，也就是将字符串中每个字母都变为其在字母表中后续的字母，比如："abc" -> "bcd"。这样，我们可
//以持续进行 “移位” 操作，从而生成如下移位序列："abc" -> "bcd" -> ... -> "xyz"
// 给定一个包含仅小写字母字符串的列表，将该列表中所有满足 “移位” 操作规律的组合进行分组并返回。
    public List<List<String>> groupStrings(String[] strings) {
        if (strings == null || strings.length == 0) return new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strings) {
            StringBuilder sb = new StringBuilder();
            // 每组的每个字符串，都跟该字符串头字母的距离一致
            for (char c : str.toCharArray()) {
                sb.append("#");
                int shift = (c - str.charAt(0) + 26) % 26;
                sb.append(shift);
            }
            String key = sb.toString();
            if (!map.containsKey(key)) map.put(key, new ArrayList<>());
            map.get(key).add(str);
        }
        return new ArrayList<>(map.values());
    }

    //228 汇总区间
    public List<String> summaryRanges(int[] nums) {
        List<String> result = new ArrayList<>();
        for (int l = 0, r = 0; r < nums.length; r++) {
            if (r == nums.length - 1 || nums[r + 1] != nums[r] + 1) {
                if (r == l) {
                    result.add("" + nums[l]);
                } else {
                    result.add(nums[l] + "->" + nums[r]);
                }
                l = r + 1;
            }
        }
        return result;
    }

    //offer57 滑动窗口
    //输入一个正整数 target ，输出所有和为 target 的连续正整数序列（至少含有两个数）。
// 序列内的数字由小到大排列，不同序列按照首个数字从小到大排列。
// 输入：target = 9
//输出：[[2,3,4],[4,5]]
    public int[][] findContinuousSequence(int target) {
        List<int[]> list = new ArrayList<>();
        for (int l = 1, r = 2; l < r; ) {
            int sum = (l + r) * (r - l + 1) / 2;
            if (sum >= target) {
                if (sum == target) {
                    int[] tmp = new int[r - l + 1];
                    for (int i = l; i <= r; i++) {
                        tmp[i - l] = i;
                    }
                    list.add(tmp);
                }
                l++;
            } else {
                r++;
            }
        }
        return list.toArray(new int[list.size()][]);
    }

    // 829 连续整数求和
    //给定一个正整数 n，返回 连续正整数满足所有数字之和为 n 的组数 。
    //前缀和+滑动窗口 Memory Exceed
    public int consecutiveNumbersSum(int n) {
        if (n <= 1) return n;
        int cnt = 0;
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + i;
        }
        for (int l = 1, r = 2; r <= n; r++) {
            while (sum[r] - sum[l - 1] > n) l++;
            if (sum[r] - sum[l - 1] == n) cnt++;
        }
        return cnt;
    }

    // 数学
    public int consecutiveNumbersSumMath(int n) {
        int ans = 0;
        int bound = 2 * n;
        for (int k = 1; k * (k + 1) <= bound; k++) {
            if (isKConsecutive(n, k)) {
                ans++;
            }
        }
        return ans;
    }

    public boolean isKConsecutive(int n, int k) {
        if (k % 2 == 1) {
            return n % k == 0;
        } else {
            return n % k != 0 && 2 * n % k == 0;
        }
    }

    //1610 可见点的最大数目
    // 每个点的极坐标
    double eps = 1e-9;

    public int visiblePoints(List<List<Integer>> points, int angle, List<Integer> location) {
        int x = location.get(0), y = location.get(1);
        List<Double> list = new ArrayList<>();
        int cnt = 0;
        double pi = Math.PI, t = angle * pi / 180;
        for (List<Integer> p : points) {
            int a = p.get(0), b = p.get(1);
            if (a == x && b == y && ++cnt >= 0) continue;
            list.add(Math.atan2(b - y, a - x) + pi);
        }
        Collections.sort(list);
        int n = list.size(), max = 0;
        for (int i = 0; i < n; i++) list.add(list.get(i) + 2 * pi);
        for (int i = 0, j = 0; j < 2 * n; j++) {
            while (i < j && list.get(j) - list.get(i) > t + eps) i++;
            max = Math.max(max, j - i + 1);
        }
        return cnt + max;
    }

    //2653. 滑动子数组的美丽值
    public int[] getSubarrayBeauty(int[] nums, int k, int x) {
        int n = nums.length;
        int[] ans = new int[n - k + 1];
        TreeMap<Integer, Integer> cnt = new TreeMap<>();
        for (int l = 0, r = 0; r < n; r++) {
            cnt.put(nums[r], cnt.getOrDefault(nums[r], 0) + 1);
            if (r - l + 1 == k) {
                int tmp = x;
                for (Map.Entry<Integer, Integer> entry : cnt.entrySet()) {
                    if (entry.getValue() >= tmp) {
                        ans[l] = entry.getKey() < 0 ? entry.getKey() : 0;
                        break;
                    }
                    tmp -= entry.getValue();
                }
                cnt.put(nums[l], cnt.get(nums[l]) - 1);
                if (cnt.get(nums[l]) == 0) {
                    cnt.remove(nums[l]);
                }
                l++;
            }
        }
        return ans;
    }

    //2730. 找到最长的半重复子字符串
    public int longestSemiRepetitiveSubstring(String s) {
        int n = s.length();
        if (n == 1) return 1;
        char[] chars = s.toCharArray();
        Set<Integer> set = new HashSet<>();
        int ans = 0;
        for (int l = 0, r = 1; r < n; r++) {
            if (chars[r] == chars[r - 1]) {
                while (set.size() > 0) {
                    set.remove(l++);
                }
                set.add(r - 1);
            }
            ans = Math.max(r - l + 1, ans);
        }
        return ans;
    }

    // endregion ---------------------------------------------------------------------------------
}
