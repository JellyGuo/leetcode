import java.util.*;

public class SolutionDualPointer {
    //region----------------------------双指针Dual Pointer-------------------------------------
    //345. 反转字符串中的元音字母
    public String reverseVowels(String s) {
        Set<Character> set = new HashSet<>();
        set.add('a');set.add('A');
        set.add('e');set.add('E');
        set.add('i');set.add('I');
        set.add('o');set.add('O');
        set.add('u');set.add('U');
        char[] chars = s.toCharArray();
        int l = 0, r = s.length() - 1;
        while (l < r) {
            while (l < r && !set.contains(chars[l])) l++ ;
            while (l < r && !set.contains(chars[r])) r--;
            if (l < r) swap(l++, r--, chars);
        }
        return new String(chars);
    }

    private void swap(int l, int r, char[] chars) {
        char tmp = chars[l];
        chars[l] = chars[r];
        chars[r] = tmp;
    }

    // 350 两个数组的交集2
    public int[] intersect(int[] nums1, int[] nums2) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums1) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        List<Integer> list = new ArrayList<>();
        for (int num : nums2) {
            if (map.containsKey(num)) {
                list.add(num);
                map.put(num, map.get(num) - 1);
                if (map.get(num) == 0) {
                    map.remove(num);
                }
            }
        }
        return list.stream().mapToInt(p -> p.intValue()).toArray();
    }

    public int[] intersect2(int[] nums1, int[] nums2) {
        List<Integer> list = new ArrayList<>();
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int idx1 = 0, idx2 = 0;
        while (idx1 < nums1.length && idx2 < nums2.length) {
            if (nums1[idx1] < nums2[idx2]) {
                idx1++;
            } else if (nums1[idx1] > nums2[idx2]) {
                idx2++;
            } else {
                list.add(nums1[idx1]);
                idx1++;
                idx2++;
            }
        }
        return list.stream().mapToInt(p -> p.intValue()).toArray();
    }

    // 面试题 16.24. 数对和 类似盛水最多的容器
    public List<List<Integer>> pairSums(int[] nums, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        int n = nums.length;
        Arrays.sort(nums);
        int l = 0, r = n - 1;
        while (l < r) {
            int sum = nums[l] + nums[r];
            if (sum == target) {
                ans.add(Arrays.asList(nums[l++], nums[r--]));
            } else if (sum > target) {
                r--;
            } else {
                l++;
            }
        }
        return ans;
    }

    //466. 统计重复个数
    public int getMaxRepetitions(String s1, int n1, String s2, int n2) {
        if (n1 == 0) {
            return 0;
        }
        int s1cnt = 0, index = 0, s2cnt = 0;
        // recall 是我们用来找循环节的变量，它是一个哈希映射
        // 我们如何找循环节？假设我们遍历了 s1cnt 个 s1，此时匹配到了第 s2cnt 个 s2 中的第 index 个字符
        // 如果我们之前遍历了 s1cnt' 个 s1 时，匹配到的是第 s2cnt' 个 s2 中同样的第 index 个字符，那么就有循环节了
        // 我们用 (s1cnt', s2cnt', index) 和 (s1cnt, s2cnt, index) 表示两次包含相同 index 的匹配结果
        // 那么哈希映射中的键就是 index，值就是 (s1cnt', s2cnt') 这个二元组
        // 循环节就是；
        //    - 前 s1cnt' 个 s1 包含了 s2cnt' 个 s2
        //    - 以后的每 (s1cnt - s1cnt') 个 s1 包含了 (s2cnt - s2cnt') 个 s2
        // 那么还会剩下 (n1 - s1cnt') % (s1cnt - s1cnt') 个 s1, 我们对这些与 s2 进行暴力匹配
        // 注意 s2 要从第 index 个字符开始匹配
        Map<Integer, int[]> recall = new HashMap<Integer, int[]>();
        int[] preLoop = new int[2];
        int[] inLoop = new int[2];
        while (true) {
            // 我们多遍历一个 s1，看看能不能找到循环节
            ++s1cnt;
            for (int i = 0; i < s1.length(); ++i) {
                char ch = s1.charAt(i);
                if (ch == s2.charAt(index)) {
                    index += 1;
                    if (index == s2.length()) {
                        ++s2cnt;
                        index = 0;
                    }
                }
            }
            // 还没有找到循环节，所有的 s1 就用完了
            if (s1cnt == n1) {
                return s2cnt / n2;
            }
            // 出现了之前的 index，表示找到了循环节
            if (recall.containsKey(index)) {
                int[] value = recall.get(index);
                int s1cntPrime = value[0];
                int s2cntPrime = value[1];
                // 前 s1cnt' 个 s1 包含了 s2cnt' 个 s2
                preLoop = new int[]{s1cntPrime, s2cntPrime};
                // 以后的每 (s1cnt - s1cnt') 个 s1 包含了 (s2cnt - s2cnt') 个 s2
                inLoop = new int[]{s1cnt - s1cntPrime, s2cnt - s2cntPrime};
                break;
            } else {
                recall.put(index, new int[]{s1cnt, s2cnt});
            }
        }
        // ans 存储的是 S1 包含的 s2 的数量，考虑的之前的 preLoop 和 inLoop
        int ans = preLoop[1] + (n1 - preLoop[0]) / inLoop[0] * inLoop[1];
        // S1 的末尾还剩下一些 s1，我们暴力进行匹配
        int rest = (n1 - preLoop[0]) % inLoop[0];
        for (int i = 0; i < rest; ++i) {
            for (int j = 0; j < s1.length(); ++j) {
                char ch = s1.charAt(j);
                if (ch == s2.charAt(index)) {
                    ++index;
                    if (index == s2.length()) {
                        ++ans;
                        index = 0;
                    }
                }
            }
        }
        // S1 包含 ans 个 s2，那么就包含 ans / n2 个 S2
        return ans / n2;
    }

    //1813. 句子相似性 III
    public boolean areSentencesSimilar(String sentence1, String sentence2) {
        String[] s1 = sentence1.split(" ");
        String[] s2 = sentence2.split(" ");
        int m = s1.length, n = s2.length;
        if (m > n) return areSentencesSimilar(sentence2, sentence1);
        int i = 0;
        while (i < m && s1[i].equals(s2[i])) {
            i++;
        }
        if (i == m) return true;
        int j = 0;
        while (j < m - i && s1[m - 1 - j].equals(s2[n - 1 - j])) {
            j++;
        }
        return i + j == m;
    }

    //1023. 驼峰式匹配
    public List<Boolean> camelMatch(String[] queries, String pattern) {
        List<Boolean> ans = new ArrayList<>();
        for (String query : queries) {
            ans.add(checkCamelMatch(query, pattern));
        }
        return ans;
    }

    private boolean checkCamelMatch(String query, String pattern) {
        int m = query.length(), n = pattern.length();
        int i = 0, j = 0;
        while (i < m && j < n) {
            if (query.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else if (query.charAt(i) >= 'a' && query.charAt(i) <= 'z') {
                i++;
            } else {
                return false;
            }
        }
        if (j == n && i < m) {
            while (i < m && query.charAt(i) >= 'a' && query.charAt(i) <= 'z') i++;
        }
        return j == n && i == m;
    }

    //1156. 单字符重复子串的最大长度
    public int maxRepOpt1(String text) {
        int n = text.length();
        int[] cnt = new int[26];
        for (char c : text.toCharArray()) {
            cnt[c - 'a']++;
        }
        int ans = 0;
        for (int i = 0; i < n; ) {
            int j = i;
            while (j < n && text.charAt(j) == text.charAt(i)) {
                j++;
            }
            int l = j - i;
            int k = j + 1;
            while (k < n && text.charAt(k) == text.charAt(i)) {
                k++;
            }
            int r = k - 1 - (j + 1) + 1;
            ans = Math.max(Math.min(l + r + 1, cnt[text.charAt(i) - 'a']), ans);
            i = j;
        }
        return ans;
    }
    //844. 比较含退格的字符串 栈模拟
    // 双指针O(n)
    public boolean backspaceCompare(String S, String T) {
        int i = S.length() - 1, j = T.length() - 1;
        int skipS = 0, skipT = 0;

        while (i >= 0 || j >= 0) {
            while (i >= 0) {
                if (S.charAt(i) == '#') {
                    skipS++;
                    i--;
                } else if (skipS > 0) {
                    skipS--;
                    i--;
                } else {
                    break;
                }
            }
            while (j >= 0) {
                if (T.charAt(j) == '#') {
                    skipT++;
                    j--;
                } else if (skipT > 0) {
                    skipT--;
                    j--;
                } else {
                    break;
                }
            }
            if (i >= 0 && j >= 0) {
                if (S.charAt(i) != T.charAt(j)) {
                    return false;
                }
            } else {
                if (i >= 0 || j >= 0) {
                    return false;
                }
            }
            i--;
            j--;
        }
        return true;
    }


    //1616. 分割两个字符串得到回文串
    public boolean checkPalindromeFormation(String a, String b) {
        return checkConcatenation(a, b) || checkConcatenation(b, a);
    }

    public boolean checkConcatenation(String a, String b) {
        int n = a.length();
        int left = 0, right = n - 1;
        while (left < right && a.charAt(left) == b.charAt(right)) {
            left++;
            right--;
        }
        if (left >= right) {
            return true;
        }
        return checkSelfPalindrome(a, left, right) || checkSelfPalindrome(b, left, right);
    }

    public boolean checkSelfPalindrome(String a, int left, int right) {
        while (left < right && a.charAt(left) == a.charAt(right)) {
            left++;
            right--;
        }
        return left >= right;
    }

    //849. 到最近的人的最大距离
    public int maxDistToClosest(int[] seats) {
        int n = seats.length;
        int l = -1, r = 0;
        int max = 0;
        for (int i = 0; i < seats.length; i++) {
            if (seats[i] == 0) {
                r = i;
                while (r<n && seats[r] != 1) {
                    r++;
                }
                int min = Integer.MAX_VALUE;
                if (l >= 0 && l < i) min = Math.min(min, i - l);
                if (r > i && r < n) min = Math.min(min, r - i);
                max = Math.max(max, min);
            } else {
                l = i;
            }
        }
        return max;
    }

    //2337. 移动片段得到字符串
    public boolean canChange(String start, String target) {
        char[] startChars = start.toCharArray();
        char[] targetChars = target.toCharArray();
        if (cnt(startChars, 'L') != cnt(targetChars, 'L')) return false;
        if (cnt(startChars, 'R') != cnt(targetChars, 'R')) return false;
        int cntL = cnt(startChars, 'L');
        int cntR = cnt(startChars, 'R');
        int idx1 = 0, idx2 = 0;
        while (idx1 < startChars.length && idx2 < targetChars.length) {
            while (idx1 < startChars.length && startChars[idx1] == '_') {
                idx1++;
            }
            while (idx2 < targetChars.length && targetChars[idx2] == '_') {
                idx2++;
            }
            if (idx1 < startChars.length && idx2 < targetChars.length) {
                if (startChars[idx1] != targetChars[idx2]) return false;
                if (startChars[idx1] == 'R' && idx1 > idx2) return false;
                if (startChars[idx1] == 'L' && idx1 < idx2) return false;
                if (startChars[idx1] == 'R') cntR--;
                else cntL--;
                idx1++;
                idx2++;

            }
        }
        return cntL == 0 && cntR == 0;
    }

    private int cnt(char[] chars, char c) {
        int cnt = 0;
        for (char cc : chars) {
            if (cc == c) cnt++;
        }
        return cnt;
    }

    // 2486 追加字符以获得子序列
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

    //2609. 最长平衡子字符串
    public int findTheLongestBalancedSubstring(String s) {
        int n = s.length();
        int max = 0;
        for (int l = 0, r = 0; r < n; r++) {
            while (r < n && s.charAt(r) == '0') {
                r++;
            }
            int cntZero = r - l;
            l = r;
            while (r < n && s.charAt(r) == '1') {
                r++;
            }
            int cntOne = r - l;
            max = Math.max(max, Math.min(cntOne, cntZero));
            l = r;
        }
        return 2 * max;
    }

    //2697. 字典序最小回文串
    public static String makeSmallestPalindrome(String s) {
        int n = s.length();
        char[] chars = new char[n];
        int l = 0, r = n - 1;
        while (l <= r) {
            if (l == r) {
                chars[l] = s.charAt(l);
                l++;
                r--;
            } else if (s.charAt(l) == s.charAt(r)) {
                chars[l] = s.charAt(l++);
                chars[r] = s.charAt(r--);
            } else {
                if (s.charAt(l) < s.charAt(r)) {
                    chars[l] = s.charAt(l);
                    chars[r--] = s.charAt(l++);
                } else {
                    chars[l++] = s.charAt(r);
                    chars[r] = s.charAt(r--);
                }
            }
        }
        return new String(chars);
    }

    // 面试01.05 一次编辑
    public boolean oneEditAway(String first, String second) {
        int m = first.length(), n = second.length();
        if (m > n) return oneEditAway(second, first);
        if (n - m > 1) return false;
        int i = 0, j = 0, cnt = 0;
        boolean diff = false;
        while (i < m && j < n && cnt < 2) {
            if (first.charAt(i) == second.charAt(j)) {
                i++;
                j++;
            } else {
                if (m == n) {
                    i++;
                    j++;
                    cnt++;
                } else {
                    j++;
                    cnt++;
                }
            }
        }
        return cnt <= 1;
    }

    //面试01.06字符串压缩
    public String compressString(String S) {
        int n = S.length();
        if (n == 0) return S;
        StringBuilder sb = new StringBuilder();
        for (int l = 0, r = 0; r <= n; r++) {
            if (r == n) {
                sb.append(S.charAt(l)).append(r - l);
                break;
            }
            if (S.charAt(r) == S.charAt(l)) continue;
            sb.append(S.charAt(l)).append(r - l);
            l = r;
        }
        return sb.length() < n ? sb.toString() : S;
    }

    // 面试题16.06 最小差
    public int smallestDifference(int[] a, int[] b) {
        int m = a.length, n = b.length;
        Arrays.sort(a);
        Arrays.sort(b);
        int idx1 = 0, idx2 = 0;
        long min = Long.MAX_VALUE;
        while (idx1 < m && idx2 < n) {
            long diff = a[idx1] - b[idx2];
            min = Math.min(min, Math.abs(diff));
            if (diff < 0) {
                idx1++;
            } else {
                idx2++;
            }
        }
        return (int) min;
    }

    // 809 情感丰富的文字
    public int expressiveWords(String s, String[] words) {
        int ans = 0;
        for (String word : words) {
            if (expand(s, word)) {
                ans++;
            }
        }
        return ans;
    }

    private boolean expand(String s, String word) {
        int n = s.length(), m = word.length();
        int i = 0, j = 0;
        while (i < n && j < m) {
            if (s.charAt(i) != word.charAt(j)) return false;
            char c = s.charAt(i);
            int cnt1 = 0, cnt2 = 0;
            while (i < n && s.charAt(i) == c) {
                cnt1++;
                i++;
            }
            while (j < m && word.charAt(j) == c) {
                cnt2++;
                j++;
            }
            if (cnt1 < cnt2) return false;
            if (cnt1 != cnt2 && cnt1 < 3) return false;
        }
        return i == n && j == m;
    }

    //777. 在LR字符串中交换相邻字符
    //序号相同的 L : start 的下标不小于 end 的下标（即 L 不能往右移动）
    //序号相同的 R : start 的下标不大于 end 的下标（即 R 不能往左移动）
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
                i++;
                j++;
            }

        }
        while (i < n) {
            if (start.charAt(i++) != 'X') return false;
        }
        while (j < n) {
            if (end.charAt(j++) != 'X') return false;
        }
        return true;
    }

    // 977 有序数组的平方
    public int[] sortedSquares(int[] nums) {
        int n = nums.length;
        int[] ans = new int[n];
        int l = 0, r = n, idx = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] >= 0) {
                r = i;
                break;
            }
        }
        l = r - 1;

        while (idx < n) {
            if (l < 0) {
                ans[idx] = (int) Math.pow(nums[r++], 2);
            } else if (r >= n) {
                ans[idx] = (int) Math.pow(nums[l--], 2);
            } else if (Math.abs(nums[l]) < Math.abs(nums[r])) {
                ans[idx] = (int) Math.pow(nums[l--], 2);
            } else {
                ans[idx] = (int) Math.pow(nums[r++], 2);
            }
            idx++;
        }
        return ans;
    }

    // 611 有效三角形的个数 toreview
    public int triangleNumber(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        int ans = 0;
        for (int i = 0; i < n - 2; i++) {
            int k = i;
            for (int j = i + 1; j < n - 1; j++) {
                while (k + 1 < n && nums[k + 1] < nums[i] + nums[j]) {
                    k++;
                }
                ans += Math.max(0, k - j);
            }
        }
        return ans;
    }

    //1163. 按字典序排在最后的子串
    public String lastSubstring(String s) {
        int i = 0, j = 1, n = s.length();
        while (j < n) {
            int k = 0;
            while (j + k < n && s.charAt(i + k) == s.charAt(j + k)) {
                k++;
            }
            if (j + k < n && s.charAt(i + k) < s.charAt(j + k)) {
                int t = i;
                i = j;
                j = Math.max(j + 1, t + k + 1);
            } else {
                j = j + k + 1;
            }
        }
        return s.substring(i);
    }

    // 1209 删除字符串中的所有相邻重复项2
    // 双指针
    public String removeDuplicates(String s, int k) {
        int n = s.length();
        char[] chars = s.toCharArray();
        int[] cnt = new int[n];
        int j = 0;
        for (int i = 0; i < n; i++, j++) {
            chars[j] = chars[i];
            if (j == 0 || chars[j - 1] != chars[j]) {
                cnt[j] = 1;
            } else {
                cnt[j] = cnt[j - 1] + 1;
                if (cnt[j] == k) {
                    j = j - k;
                }
            }
        }
        return new String(chars, 0, j);
    }

    public String removeDuplicatesStack(String s, int k) {
        StringBuilder sb = new StringBuilder(s);
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < sb.length(); i++) {
            if (i == 0 || sb.charAt(i) != sb.charAt(i - 1)) {
                stack.push(1);
            } else {
                int inc = stack.pop() + 1;
                if (inc == k) {
                    sb.delete(i - k + 1, i + 1);
                    i -= k;
                } else {
                    stack.push(inc);
                }
            }
        }
        return sb.toString();
    }

    //1750. 删除字符串两端相同字符后的最短长度
    public int minimumLength(String s) {
        char[] chars = s.toCharArray();
        int l = 0, r = chars.length - 1;
        while (l < r) {
            if (chars[l] != chars[r]) break;
            char c = chars[l];
            while (l <= r && chars[l] == c) l++;
            while (l < r && chars[r] == c) r--;
        }
        return r - l + 1;
    }

    //1237. 找出给定方程的正整数解
    public List<List<Integer>> findSolution(CustomFunction customfunction, int z) {
        List<List<Integer>> res = new ArrayList<>();
        for (int x = 1, y = 1000; x <= 1000 && y >= 1; x++) {
            while (y >= 1 && customfunction.f(x, y) > z) {
                y--;
            }
            if (y >= 1 && customfunction.f(x, y) == z) {
                List<Integer> pair = new ArrayList<>();
                pair.add(x);
                pair.add(y);
                res.add(pair);
            }
        }
        return res;
    }

    //1807. 替换字符串中的括号内容
    public String evaluate(String s, List<List<String>> knowledge) {
        Map<String, String> dict = new HashMap<>();
        for (List<String> ls : knowledge) {
            dict.put(ls.get(0), ls.get(1));
        }
        Deque<String> deque = new ArrayDeque<>();
        int n = s.length();
        for (int l = 0, r = 0; r < n; ) {
            if (s.charAt(r) == ')') {
                String key = s.substring(l + 1, r);
                deque.offerLast(dict.getOrDefault(key, "?"));
                r++;
                l = r;
            } else if (s.charAt(l) == '(') {
                r++;
            } else {
                deque.offerLast("" + s.charAt(r));
                l++;
                r++;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String ss : deque) {
            sb.append(ss);
        }
        return sb.toString();
    }

    // 481 神奇字符串
    public int magicalString(int n) {
        if (n < 4) {
            return 1;
        }
        char[] s = new char[n];
        s[0] = '1';
        s[1] = '2';
        s[2] = '2';
        int res = 1;
        int i = 2;
        int j = 3;
        while (j < n) {
            int size = s[i] - '0';
            int num = 3 - (s[j - 1] - '0');
            while (size > 0 && j < n) {
                s[j] = (char) ('0' + num);
                if (num == 1) {
                    ++res;
                }
                ++j;
                --size;
            }
            ++i;
        }
        return res;
    }

    //2563. 统计公平数对的数目
    public long countFairPairs(int[] nums, int lower, int upper) {
        int n = nums.length;
        long cnt = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] + nums[j] >= lower && nums[i] + nums[j] <= upper) {
                    cnt++;
                }

            }
        }
        return cnt;
    }

    // 如果(i, j)数对满足以下情况，则认为它是一个 公平数对
    //0 <= i < j < n，且
    //lower <= nums[i] + nums[j] <= upper
    // i、j有先后顺序，排序后i、j会重复计算，最后答案/2即可
    // 暴力会TLE，需要复用每个i的结果：排序后从前往后遍历nums[i]递增，则 [lower-nums[i],upper-nums[i]]递减
    // 从后往前的指针[l,r]在i往后的时候可以从上一个i的位置继续往前
    public long countFairPairsDualPointer(int[] nums, int lower, int upper) {
        int n = nums.length;
        Arrays.sort(nums);
        long ans = 0;
        for (int i = 0, l = n - 1, r = n - 1; i < n; i++) {
            while (l >= 0 && nums[i] + nums[l] >= lower) {
                l--;
            }
            while (r >= 0 && nums[i] + nums[r] > upper) {
                r--;
            }
            ans += r - l - ((i > l && i <= r) ? 1 : 0);
        }
        return ans / 2;
    }

    //6317. 统计美丽子数组数目
    // 朴素双循环TLE
    public long beautifulSubarrays(int[] nums) {
        int n = nums.length;
        long ans = 0;
        for (int i = 0; i < n; i++) {
            int x = nums[i];
            if (x == 0) ans++;
            for (int j = i + 1; j < n; j++) {
                x ^= nums[j];
                if (x == 0) ans++;
            }
        }
        return ans;
    }

    public long beautifulSubarrays2(int[] nums) {
        long ans = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        int x = 0;
        for (int num : nums) {
            x ^= num;
            ans += map.getOrDefault(x, 0);
            map.put(x, map.getOrDefault(x, 0) + 1);
        }
        return ans;
    }

    //1793. 好子数组的最大分数
    public int maximumScore(int[] nums, int k) {
        int n = nums.length;
        int left = k - 1, right = k + 1;
        int ans = 0;
        for (int i = nums[k];;) {
            while (left >= 0 && nums[left] >= i) {
                --left;
            }
            while (right < n && nums[right] >= i) {
                ++right;
            }
            ans = Math.max(ans, (right - left - 1) * i);
            if (left == -1 && right == n) {
                break;
            }
            i = Math.max((left == -1 ? -1 : nums[left]), (right == n ? -1 : nums[right]));
            if (i == -1) {
                break;
            }
        }
        return ans;
    }

    //endregion---------------------------------------------------------------------------------------------------------
}
