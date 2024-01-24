import java.util.*;

public class SolutionSkill {

    //region ----------------------------------------------------------技巧---------------------------------
    // 负数在计算机中存储是补码形式
    // ~x：按位取反
    // -x : x按位取反，末尾+1  (得到补码=反码+1)  所以负数的二进制得到原码 = 二进制(补码)-1 再取反
    // x&-x 取最低位的1
    // x&(x-1) 最低位1变0
    // x/y向上取整：(x+y-1)/y

    // 是否是2的幂
    private boolean isPowOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    // 是否是3的幂
    public boolean isPowerOfThree(int n) {
        if (n <= 0) return false;
        while (n % 3 == 0) n /= 3;
        return n == 1;
    }

    //342. 4的幂
    public boolean isPowerOfFour(int n) {
        if (n == 0) return false;
        if (n == 1) return true;
        if (n % 4 != 0) return false;
        return isPowerOfFour(n / 4);

    }

    // 1780. 判断一个数字是否可以表示成三的幂的和
    //给你一个整数 n ，如果你可以将 n 表示成若干个[不同]的三的幂之和，请你返回 true ，否则请返回 false 。
    //输入：n = 91
    //输出：true
    //解释：91 = 3^0 + 3^2 + 3^4
    // 要么可以除以3 要么多1,如果多一个1就是相同
    public boolean checkPowersOfThree(int n) {
        while (n != 0) {
            if (n % 3 == 2) {
                return false;
            }
            n /= 3;
        }
        return true;
    }

    // 27 移除元素
    public int removeElement(int[] nums, int val) {
        int idx = 0;
        for (int x : nums) {
            if (x != val) nums[idx++] = x;
        }
        return idx;
    }

    // 26 删除有序数组中的重复项
    public int removeDuplicates(int[] nums) {
        if (nums.length <= 1) return nums.length;
        int slow = 0, fast = 1;
        while (fast < nums.length) {
            if (nums[slow] != nums[fast]) {
                slow++;
                nums[slow] = nums[fast];
            }
            fast++;
        }
        return slow + 1;
    }

    public int removeDuplicatesSL2(int[] nums) {
        int idx = 0;
        for (int x : nums) {
            if (idx < 1 || nums[idx - 1] != x) {
                nums[idx] = x;
                idx++;
            }
        }
        return idx;
    }

    //给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使每个元素 最多出现两次 ，返回删除后数组的新长度。
////输入：nums = [1,1,1,2,2,3]
////输出：5, nums = [1,1,2,2,3]
    public int removeDuplicates2(int[] nums) {
        int idx = 0;
        for (int x : nums) {
            if (idx < 2 || nums[idx - 2] != x) {
                nums[idx] = x;
                idx++;
            }
        }
        return idx;
    }

    public int removeDuplicates2SL2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int k = 1;
        if (nums.length < 2) {
            return 1;
        }
        int index = nums.length - 1;
        for (int i = 1; i <= index; i++) {
            if (nums[i] == nums[i - 1]) {
                if (k >= 2) {
                    move(nums, i, index);
                    index--;
                    i--;
                    k--;
                }
                k++;
            } else {
                k = 1;
            }
        }
        return index + 1;
    }

    private void move(int[] nums, int start, int end) {
        int tmp = nums[start];
        if (end - start >= 0) System.arraycopy(nums, start + 1, nums, start, end - start);
        nums[end] = tmp;
    }

    public int removeDuplicates2SL3(int[] nums) {
        int n = nums.length;
        if (n <= 2) {
            return n;
        }
        int slow = 2, fast = 2;
        while (fast < n) {
            if (nums[slow - 2] != nums[fast]) {
                nums[slow] = nums[fast];
                ++slow;
            }
            ++fast;
        }
        return slow;
    }

    // 775 全局倒置与局部倒置
    // 找非局部倒置
    public boolean isIdealPermutation(int[] nums) {
        int n = nums.length;
        int minSuffix = nums[n - 1];
        for (int i = n - 3; i >= 0; i--) {
            if (nums[i] > minSuffix) {
                return false;
            }
            minSuffix = Math.min(minSuffix, nums[i + 1]);
        }
        return true;
    }

    // 83 删除排序链表中的重复元素
    public ListNode deleteDuplicates1(ListNode head) {
        if (head == null) return null;
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode cur = head;
        while (cur.next != null) {
            if (cur.val == cur.next.val) {
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        return dummy.next;
    }

    // 82 删除排序链表中的重复元素2
    public ListNode deleteDuplicates2(ListNode head) {
        ListNode pre = new ListNode(0);
        pre.next = head;
        ListNode cur = pre;
        while (cur.next != null && cur.next.next != null) {
            if (cur.next.val == cur.next.next.val) {
                int tmp = cur.next.val;
                while (cur.next != null && cur.next.val == tmp) {
                    cur.next = cur.next.next;
                }
            } else {
                cur = cur.next;
            }
        }
        return pre.next;
    }

    // 面试02.01移除重复节点
    // // 输入：[1, 2, 3, 3, 2, 1]
    //// 输出：[1, 2, 3]
    public ListNode removeDuplicateNodes(ListNode head) {
        Set<Integer> set = new HashSet<>();
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode cur = dummy;
        while (cur.next != null) {
            if (set.contains(cur.next.val)) {
                cur.next = cur.next.next;
            } else {
                set.add(cur.next.val);
                cur = cur.next;
            }
        }
        return dummy.next;
    }

    //offer 21 奇数位于偶数前面
    public int[] exchange(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            while (nums[l] % 2 != 0 && l < r) {
                l++;
            }
            while (nums[r] % 2 == 0 && l < r) {
                r--;
            }
            int tmp = nums[r];
            nums[r] = nums[l];
            nums[l] = tmp;
        }
        return nums;
    }

    // 1758 生成交替二进制字符串的最少操作次数
    public int minOperations(String s) {
        int n = s.length();
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c - '0' != i % 2) cnt++;
        }
        return Math.min(cnt, n - cnt);
    }

    // 位运算
    // 面试16.01
    public int[] swapNumbers(int[] numbers) {
        numbers[0] = numbers[0] ^ numbers[1];
        numbers[1] = numbers[0] ^ numbers[1];
        numbers[0] = numbers[1] ^ numbers[0];
        return numbers;
    }

    //191 编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数（也被称为 汉明重量).）。
    public int reverseBits(int n) {
        int ans = 0;
        for (int i = 31; i >= 0 && n != 0; i--) {
            ans |= (n & 1) << i;
            n = (n >>> 1);
        }
        return ans;
    }

    // offer 15 二进制中1的个数
    public int hammingWeight(int n) {
        int cnt = 0;
        for (int i = 0; i < 32; i++) {
            if (((n >> i) & 1) == 1) cnt++;
        }
        return cnt;
    }

    //982. 按位与为零的三元组
    public int countTriplets(int[] nums) {
        int[] cnt = new int[1 << 16];
        for (int x : nums) {
            for (int y : nums) {
                ++cnt[x & y];
            }
        }
        int ans = 0;
        for (int x : nums) {
            for (int mask = 0; mask < (1 << 16); ++mask) {
                if ((x & mask) == 0) {
                    ans += cnt[mask];
                }
            }
        }
        return ans;
    }

    //2546. 执行逐位运算使字符串相等
    public boolean makeStringsEqual(String s, String target) {
        return s.contains("1") == target.contains("1");
    }

    // 面试 05.06 //整数转换。编写一个函数，确定需要改变几个位才能将整数A转成整数B。
    public int convertInteger(int A, int B) {
        int n = A ^ B;
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            if ((n >> i & 1) == 1) ans++;
        }
        return ans;
    }

    // 面试题 05.07 配对交换
    public int exchangeBits(int num) {
        int i = 0, j = 1;
        while (i <= 30) {
            int a = num >> i & 1, b = num >> j & 1;
            if (a != b) {
                num ^= 1 << i;
                num ^= 1 << j;
            }
            i += 2;
            j += 2;
        }
        return num;
    }

    // 面试05.04 下一个数
    public int[] findClosedNumbers(int num) {
        int[] res = new int[2];
        if (num <= 0 || num >= Integer.MAX_VALUE) {
            res[0] = -1;
            res[1] = -1;
        } else {
            res[0] = getNext(num);
            res[1] = getPrev(num);
        }
        return res;
    }

    private int getNext(int num) {
        int c = num;
        int c0 = 0, c1 = 0;
        while ((c & 1) == 0 && (c != 0)) {
            c0++;
            c >>= 1;
        }
        while ((c & 1) == 1) {
            c1++;
            c >>= 1;
        }
        if (c0 + c1 == 31 || c0 + c1 == 0) return -1;
        int p = c0 + c1;
        num |= (1 << p);
        num &= ~((1 << p) - 1);
        num |= (1 << (c1 - 1)) - 1;
        return num;
    }

    private int getPrev(int n) {
        int c = n;
        int c0 = 0, c1 = 0;
        while ((c & 1) == 1) {
            c1++;
            c >>= 1;
        }
        if (c == 0) return -1;
        while (((c & 1) == 0) && (c != 0)) {
            c0++;
            c >>= 1;
        }
        int p = c0 + c1;
        n &= ((~0) << (p + 1)); // 将位0到位p清零

        int mask = (1 << (c1 + 1)) - 1; // (c1+1)个1
        n |= mask << (c0 - 1);
        return n;
    }

    // 面试题 05.08 绘制直线
    public int[] drawLine(int length, int w, int x1, int x2, int y) {
        StringBuilder sb = new StringBuilder();
        int start = y * w + x1, end = y * w + x2;
        for (int i = 0; i < 32 * length; i++) {
            if (i >= start && i <= end) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        int[] ans = new int[length];
        for (int i = 0; i < length; i++) {
            ans[i] = Integer.parseUnsignedInt(sb.substring(i * 32, i * 32 + 32), 2);
        }
        return ans;
    }

    // 面试题 16.07 最大数值
    // n输入为 0或1
    // 输出一定为 0 -> 1 或 1 -> 0
    public static int filp(int n) {
        return n ^ 1;
    }

    // n是非负数, 返回1
    // n是负数， 返回0
    public static int sign(int n) {
        // 相当于判断n的符号位
        return filp((n >> 31) & 1);
    }

    // 这种写法可以防止溢出
    public int maximum(int a, int b) {
        int c = a - b;
        // 依次取出a，b，c的符号位
        int sa = sign(a);
        int sb = sign(b);
        int sc = sign(c);
        // 判断a和b符号是否相同, 不相同则记录为1
        int disSab = sa ^ sb;
        // 相同则记录为1， 与disSab互斥
        int sameSab = filp(disSab);
        // 返回A的情况有两种：
        // 第一种：ab符号相同且a-b大于0（即sameSab = 1 && sc = 1）
        // 第二种：ab符号不相同且a大于0（即disSab = 1 && sa = 1）
        // 注意这两种情况是互斥的（只会出现1+0或者0+1或者0+0）
        int returnA = disSab * sa + sameSab * sc;
        int returnB = filp(returnA);
        return a * returnA + b * returnB;
    }

    public int hammingWeight2(int n) {
        int res = 0;
        while (n != 0) {
            n &= (n - 1);
            res++;
        }
        return res;
    }

    // 面试01.01 判定字符是否唯一 不使用额外数据结构
    public boolean isUnique(String astr) {
        int mask = 0;
        for (char c : astr.toCharArray()) {
            if (((mask >> (c - 'a')) & 1) == 1) return false;
            mask |= (1 << (c - 'a'));
        }
        return true;
    }

    // 面试01.04 回文排列
    public boolean canPermutePalindrome(String s) {
        long first = 0, second = 0;
        for (char c : s.toCharArray()) {
            if (c >= 64) {
                first ^= (1L << (c - 64));
            } else {
                second ^= (1L << c);
            }
        }
        return Long.bitCount(first) + Long.bitCount(second) <= 1;
    }

    // 面试 05.01 插入
    public int insertBits(int N, int M, int i, int j) {
        int left = N >> j >> 1;
        left = left << j << 1;
        int middle = M << i;
        int right = N & ((1 << i) - 1);
        return left | middle | right;
    }

    // 462 最小操作次数使数组元素相等2
    public int minMoves2(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int ans = 0, t = nums[(n - 1) / 2];
        for (int num : nums) {
            ans += Math.abs(num - t);
        }
        return ans;
    }

    // 779 第k个语法符号
    // 第n行只和父num有关，奇数和父一致，偶数和父相反
    public int kthGrammar(int n, int k) {
        if (n == 1) return 0;
        int ret = kthGrammar(n - 1, (k + 1) / 2);
        if (ret == 0) {
            return (k & 1) == 1 ? 0 : 1;
        } else {
            return (k & 1) == 1 ? 1 : 0;
        }
    }

    public int kthGrammar2(int n, int k) {
        if (n == 1) return 0;
        return (k & 1) ^ 1 ^ kthGrammar(n - 1, (k + 1) / 2);
    }

    //1625. 执行操作后字典序最小的字符串 裴蜀定理
    public String findLexSmallestString(String s, int a, int b) {
        int n = s.length();
        String res = s;
        s = s + s;
        int g = gcd(b, n);

        for (int i = 0; i < n; i += g) {
            char[] t = s.substring(i, i + n).toCharArray();
            add(t, n, a, 1);
            if (b % 2 != 0) {
                add(t, n, a, 0);
            }
            String tStr = new String(t);
            if (tStr.compareTo(res) < 0) {
                res = tStr;
            }
        }
        return res;
    }

    public void add(char[] t, int n, int a, int start) {
        int minVal = 10, times = 0;
        for (int i = 0; i < 10; i++) {
            int added = ((t[start] - '0') + i * a) % 10;
            if (added < minVal) {
                minVal = added;
                times = i;
            }
        }
        for (int i = start; i < n; i += 2) {
            t[i] = (char) ('0' + ((t[i] - '0') + times * a) % 10);
        }
    }


    //1784. 检查二进制字符串字段
    //给你一个二进制字符串 s ，该字符串 不含前导零 。
// 如果 s 包含 零个或一个由连续的 '1' 组成的字段 ，返回 true 。否则，返回 false 。
// 如果 s 中 由连续若干个 '1' 组成的字段 数量不超过 1，返回 true 。否则，返回 false 。
    public boolean checkOnesSegment(String s) {
        int n = s.length();
        if (n <= 2) return true;
        boolean flag = false;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '0') {
                flag = true;
            } else if (flag) {
                return false;
            }
        }
        return true;
    }

    // 89 格雷编码
    //n = 1  [0, 1]
    //n = 2  [00，01，11，10]
    //n = 3  [000, 001, 011, 010, 110, 111, 101, 100]
    //....
    //一位格雷码只有两个元素，【1， 0】
    //因为格雷码 n 每增加1，包含的数字会翻倍，这里我们设n位格雷码包含c个数，前一个n为n'，所以c = 2c'
    //所以这时n中的前c'个数是n'中的所有数字前面补0，相当于全部都是n`中的数字
    //n = 2  [ 00,  01,  11,  10]
    //n = 3  [000, 001, 011, 010] (前四个数)
    //这时n中的后c'个数是n'中的所有数字前面补1，然后变为逆序
    //n = 2  [ 00,  01,  11,  10]
    //补   1 [100, 101, 111, 110]
    //逆  序 [110, 111, 101, 100] （后四个数）
    //结果拼接
    // n = 3  [000, 001, 011, 010, 110, 111, 101, 100]
    public List<Integer> grayCode(int n) {
        List<Integer> gray = new ArrayList<>();
        gray.add(0); //初始化 n = 0 的解
        for (int i = 0; i < n; i++) {
            int add = 1 << i; //要加的数
            //倒序遍历，并且加上一个值添加到结果中
            for (int j = gray.size() - 1; j >= 0; j--) {
                gray.add(gray.get(j) + add);
            }
        }
        return gray;
    }

    //1238. 循环码排列
    public List<Integer> circularPermutation(int n, int start) {
        List<Integer> ret = new ArrayList<>();
        ret.add(start);
        for (int i = 1; i <= n; i++) {
            int m = ret.size();
            for (int j = m - 1; j >= 0; j--) {
                ret.add(((ret.get(j) ^ start) | (1 << (i - 1))) ^ start);
            }
        }
        return ret;
    }

    //先枚举再截取
    public List<Integer> circularPermutation2(int n, int start) {
        int[] g = new int[1 << n];
        int j = 0;
        for (int i = 0; i < 1 << n; ++i) {
            g[i] = i ^ (i >> 1);// 二进制码转换成二进制格雷码
            if (g[i] == start) {
                j = i;
            }
        }
        List<Integer> ans = new ArrayList<>();
        for (int i = j; i < j + (1 << n); ++i) {
            ans.add(g[i % (1 << n)]);
        }
        return ans;
    }

    //第一个不重复的字符
    public char firstUniqChar(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        for (char c : s.toCharArray()) {
            if (count[c - 'a'] == 1) return c;
        }
        return ' ';
    }

    // 第一个重复的字符
    public char repeatedCharacter(String s) {
        Set<Character> set = new HashSet<>();
        for (char c : s.toCharArray()) {
            if (set.contains(c)) return c;
            set.add(c);
        }
        return ' ';
    }

    // 136 只出现一次的数字
    public int singleNumber(int[] nums) {
        int sum1 = 0;
        int sum2 = 0;
        HashSet<Integer> set = new HashSet<>();
        for (int i : nums) {
            sum1 += i;
            set.add(i);
        }
        for (int i : set) {
            sum2 += i;
        }
        return 2 * sum2 - sum1;
    }

    public int singleNumber2(int[] nums) {
        int x = 0;
        for (int i : nums) {
            x ^= i;
        }
        return x;
    }

    // 137 只出现一次的数字 [2,2,3,2]
    public int singleNumberTwo(int[] nums) {
        int[] counts = new int[32];
        for (int num : nums) {
            for (int j = 0; j < 32; j++) {
                counts[j] += num & 1;
                num >>>= 1;
            }
        }
        int res = 0, m = 3;
        for (int i = 0; i < 32; i++) {
            res <<= 1;
            res |= counts[31 - i] % m;
        }
        return res;
    }

    public int singleNumberTwo2(int[] nums) {
        int ones = 0, twos = 0;
        for (int num : nums) {
            ones = ones ^ num & ~twos;
            twos = twos ^ num & ~ones;
        }
        return ones;
    }

    // 260 只出现一次的数字
    // 两个数字只出现一次，其余出现2次
    public int[] singleTwoNumbers(int[] nums) {
        int x = 0;
        // x = a^b 找最低位的1，该位置a和b一个是0一个是1
        for (int i : nums) {
            x ^= i;
        }
        // 找x的最低位的1
        // int mask = (x == Integer.MIN_VALUE ? x : x & (-x))
        int mask = 1;
        while ((x & mask) == 0) {
            mask = mask << 1;
        }
        int a = 0, b = 0;
        for (int i : nums) {
            if ((i & mask) == 0) {
                a ^= i;
            } else {
                b ^= i;
            }
        }
        return new int[]{a, b};
    }

    // 268 丢失的数字
    // 输入: [0,1,3]
//输出: 2
// 输入: [0,1,2,3,4,5,6,7,9]
//输出: 8
    public int missingNumber1(int[] nums) {
        int n = nums.length;
        boolean[] hash = new boolean[n + 1];
        for (int num : nums) {
            hash[num] = true;
        }

        for (int i = 0; i < n; i++) {
            if (!hash[i]) return i;
        }
        return n;
    }

    // 原地hash
    public int missingNumber2(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (nums[i] != i && nums[i] < n) swap(nums, nums[i], i--);
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i) return i;
        }
        return n;
    }

    // 做差法
    public int missingNumber3(int[] nums) {
        int n = nums.length;
        int cur = 0, sum = n * (n + 1) / 2;
        for (int i : nums) cur += i;
        return sum - cur;
    }

    //异或 找缺失数、找出现一次数都是异或的经典应用
    public int missingNumber4(int[] nums) {
        int n = nums.length;
        int ans = 0;
        for (int i = 0; i <= n; i++) ans ^= i;
        for (int i : nums) ans ^= i;
        return ans;
    }


    public int missingNumber6(int[] nums) {
        int n = nums.length;
        int l = 0, r = n;
        while (l < r) {
            int mid = l + r >> 1;
            //找第一个大于自己idx的数
            if (nums[mid] > mid) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    // 41 缺失的第一个正数
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        // n位数字，如果不是从1到n，那么缺失的一定在1-n中间，可以用下标作为标识
        // 找到所有<=n的数打上标记，负数赋值n+1不影响打标
        for (int i = 0; i < n; i++) {
            if (nums[i] <= 0) nums[i] = n + 1;
        }
        for (int i = 0; i < n; i++) {
            // nums[i]如果被打标会变成负数，所以取绝对值
            int num = Math.abs(nums[i]);
            if (num <= n) {
                // nums中有相同数字时，nums[num - 1]会被多次打标
                nums[num - 1] = -Math.abs(nums[num - 1]);
            }
        }
        // 找到没有打标的坐标，就是第一个缺失的
        for (int i = 0; i < n; i++) {
            if (nums[i] > 0) return i + 1;
        }
        // 都被打标，则是数组长度下一个数缺失
        return n + 1;
    }

    //1803. 统计异或值在范围内的数对有多少
    public int countPairs(int[] nums, int low, int high) {
        int ans = 0;
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int x : nums) cnt.put(x, cnt.getOrDefault(x, 0) + 1);
        for (++high; high > 0; high >>= 1, low >>= 1) {
            Map<Integer, Integer> nxt = new HashMap<>();
            for (Map.Entry<Integer, Integer> e : cnt.entrySet()) {
                int x = e.getKey(), c = e.getValue();
                ans += c * (high % 2 * cnt.getOrDefault((high - 1) ^ x, 0) -
                        low % 2 * cnt.getOrDefault((low - 1) ^ x, 0));
                nxt.put(x >> 1, nxt.getOrDefault(x >> 1, 0) + c);
            }
            cnt = nxt;
        }
        return ans / 2;
    }

    // 面试题08.03 魔术索引 跳跃查询
    public int findMagicIndex(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; ) {
            if (nums[i] == i) return i;
            i = Math.max(nums[i], i + 1);
        }
        return -1;
    }

    //2032. 至少在两个数组中出现的值  倒排索引
    public List<Integer> twoOutOfThree(int[] nums1, int[] nums2, int[] nums3) {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int num : nums1) {
            Set<Integer> set = map.computeIfAbsent(num, k -> new HashSet<>());
            set.add(1);
        }
        for (int num : nums2) {
            Set<Integer> set = map.computeIfAbsent(num, k -> new HashSet<>());
            set.add(2);
        }
        for (int num : nums3) {
            Set<Integer> set = map.computeIfAbsent(num, k -> new HashSet<>());
            set.add(3);
        }
        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, Set<Integer>> entry : map.entrySet()) {
            if (entry.getValue().size() >= 2) result.add(entry.getKey());
        }
        return result;
    }

    //2563. 子字符串异或查询
    // 字符串匹配TLE
    public int[][] substringXorQueries(String s, int[][] queries) {
        int n = queries.length;
        int[][] ans = new int[n][2];
        for (int i = 0; i < n; i++) {
            String target = Integer.toBinaryString(queries[i][0] ^ queries[i][1]);
            int idx = s.indexOf(target);
            if (idx != -1) {
                ans[i] = new int[]{idx, idx + target.length() - 1};
            } else {
                ans[i] = new int[]{-1, -1};
            }
        }
        return ans;
    }

    // 预处理
    public int[][] substringXorQueries2(String s, int[][] queries) {
        HashMap<Integer, int[]> map = new HashMap<>();
        for (int len = 30; len > 0; len--) {
            for (int l = s.length() - len; l >= 0; l--) {
                map.put(Integer.valueOf(s.substring(l, l + len), 2), new int[]{l, l + len - 1});
            }
        }
        int[][] result = new int[queries.length][];
        for (int i = 0; i < queries.length; i++) {
            result[i] = map.getOrDefault(queries[i][0] ^ queries[i][1], new int[]{-1, -1});
        }
        return result;
    }

    //172 阶乘后的0
    // n的阶乘中有几个5相乘 5!只有1个  10! 有2个 25！有6个
    public int trailingZeroes(int n) {
        int ans = 0;
        while (n > 0) {
            ans += n / 5;
            n /= 5;
        }
        return ans;
    }

    //793. 阶乘函数后 K 个零
    // 答案不是5就是0
    // k=0 0! 1! 2! 3! 4! 阶乘里5的个数是0,共5个数
    // k=1 5! - 9! 共5个数
    // k=5 不存在阶乘里只有5 个5 的情况  20!-24! 4个5，25!-29! 6个5
    public int preimageSizeFZF(int k) {
        if (k <= 1) return 5;
        return f(k) - f(k - 1);
    }

    private int f(int x) {
        long l = 0, r = (long) 1e10;
        while (l < r) {
            long mid = l + r + 1 >> 1;
            if (getCnt(mid) <= x) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return (int) l;
    }

    private long getCnt(long x) {
        long ans = 0;
        while (x > 0) {
            ans += x / 5;
            x /= 5;
        }
        return ans;
    }

    // 75 颜色分类
    public void sortColors(int[] nums) {
        int p0 = 0;
        int p1 = 0;
        for (int i = 0; i < nums.length; i++) {
            int tmp = nums[i];
            if (tmp == 1) {
                swap(nums, i, p1);
                p1++;
            } else if (tmp == 0) {
                swap(nums, i, p0);
                if (p0 < p1) {
                    swap(nums, i, p1);
                }
                p1++;
                p0++;
            }
        }
    }

    public void sortColors2(int[] nums) {
        if (nums.length < 2) {
            return;
        }
        int zero = 0;
        int two = nums.length - 1;
        int i = 0;
        while (i <= two) {
            int tmp = nums[i];
            if (tmp == 0) {
                swap(nums, i, zero);
                zero++;
                i++;
            } else if (tmp == 2) {
                swap(nums, i, two);
                two--;
            } else if (tmp == 1) {
                i++;
            }

        }
    }

    //1796 字符串中第二大的数字
    public int secondHighest(String s) {
        int first = -1, second = -1;
        for (char c : s.toCharArray()) {
            if (c >= '0' && c <= '9') {
                int idx = c - '0';
                if (idx == first) {
                } else if (idx > first) {
                    second = first;
                    first = idx;
                } else if (idx > second) {
                    second = idx;
                }
            }
        }
        return second;
    }

    // 414. 第三大的数
    public int thirdMax(int[] nums) {
        Integer first = null, second = null, third = null;
        for (int num : nums) {
            if (first == null || num > first) {
                third = second;
                second = first;
                first = num;
            } else if (num < first && (second == null || num > second)) {
                third = second;
                second = num;
            } else if (second != null && num < second && (third == null || num > third)) {
                third = num;
            }
        }
        return third == null ? first : third;
    }

    // 1523 在区间范围内统计奇数数目
    public int countOdds(int low, int high) {
        int len = high - low + 1;
        if ((low & 1) == 1 && (high & 1) == 1) {
            return len + 1 >> 1;
        }
        return len >> 1;
    }

    // 754 到达终点的数字
    public int reachNumber(int target) {
        target = Math.abs(target);
        int k = 0;
        while (target > 0) {
            k++;
            target -= k;
        }
        return target % 2 == 0 ? k : k + 1 + k % 2;
    }

    // 151 反转句子/单词
    public String reverseWords(String s) {
        String[] words = s.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(new StringBuilder().append(word).reverse().toString()).append(" ");
        }
        return result.toString().trim();
    }

    public String reverseWords2(String s) {
        StringBuilder sb = trimBlankSpace(s);
        reverse(sb, 0, sb.length() - 1);
        reverseEachWords(sb);
        return sb.toString();
    }

    private StringBuilder trimBlankSpace(String s) {
        int left = 0;
        int right = s.length() - 1;
        while (left <= right && s.charAt(left) == ' ') {
            left++;
        }
        while (left <= right && s.charAt(right) == ' ') {
            right--;
        }
        StringBuilder sb = new StringBuilder();
        while (left <= right) {
            char c = s.charAt(left);
            if (c != ' ') {
                sb.append(c);
            } else if (sb.charAt(sb.length() - 1) != ' ') {
                sb.append(c);
            }
            left++;
        }
        return sb;
    }

    private void reverse(StringBuilder sb, int left, int right) {
        while (left < right) {
            char c = sb.charAt(left);
            sb.setCharAt(left++, sb.charAt(right));
            sb.setCharAt(right--, c);
        }
    }

    private void reverseEachWords(StringBuilder sb) {
        int length = sb.length();
        int start = 0;
        int end = 0;
        while (start < length) {
            while (end < length && sb.charAt(end) != ' ') {
                end++;
            }
            reverse(sb, start, end - 1);
            start = end + 1;
            end++;
        }
    }

    // 186 反转字符串中的单词2
    public void reverseWords(char[] s) {
        reverse(s, 0, s.length - 1);
        for (int l = 0, r = 0; r < s.length; r++) {
            if (s[r] == ' ') {
                reverse(s, l, r - 1);
                l = r + 1;
            } else if (r == s.length - 1) {
                reverse(s, l, r);
            }
        }
    }

    private void reverse(char[] chars, int l, int r) {
        while (l < r) {
            char c = chars[l];
            chars[l++] = chars[r];
            chars[r--] = c;
        }
    }

    // 201数字范围按位与
    public int rangeBitwiseAnd1(int left, int right) {
        int cnt = 0;
        //找left right 的公共前缀
        while (left < right) {
            left >>= 1;
            right >>= 1;
            cnt++;
        }
        return left << cnt;
    }

    public int rangeBitwiseAnd2(int left, int right) {
        // n&(n-1)每次消除n最右边的1
        while (left < right) {
            right &= (right - 1);
        }
        return right;
    }

    // 520 检测大写字母
    public boolean detectCapitalUse(String word) {
        int[] chars = new int[58];
        for (char c : word.toCharArray()) {
            chars[c - 'A']++;
        }
        int capNum = 0;
        for (int i = 0; i < 26; i++) {
            capNum += chars[i];
        }
        if (capNum == word.length() || capNum == 0) {
            return true;
        }
        if (capNum == 1 && word.charAt(0) <= 'Z') {
            return true;
        }
        return false;
    }

    // 693 交替位二进制数
    //给定一个正整数，检查它的二进制表示是否总是 0、1 交替出现：换句话说，就是二进制表示中相邻两位的数字永不相同。
    public boolean hasAlternatingBits(int n) {
        int i = 0;
        while ((n >> i) > 0) {
            if (((n >> i) & 1) == ((n >> (i + 1)) & 1)) {
                return false;
            }
            i++;
        }
        return true;
    }

    // 868 二进制间距
    public int binaryGap(int n) {
        int last = -1, ans = 0;
        for (int i = 0; i < 32; i++) {
            if (((n >> i) & 1) > 0) {
                if (last >= 0) ans = Math.max(ans, i - last);
                last = i;
            }
        }
        return ans;
    }


    // 202 快乐数
    // 快慢指针找循环
    public boolean isHappy(int n) {
        int fast = n;
        int slow = n;
        do {
            fast = squareNum(fast);
            fast = squareNum(fast);
            slow = squareNum(slow);
        } while (fast != slow);
        return fast == 1;
    }

    private int squareNum(int n) {
        int sum = 0;
        while (n != 0) {
            sum += (n % 10) * (n % 10);
            n /= 10;
        }
        return sum;
    }

    // 204 计数质数
    public int countPrimes(int n) {
        if (n <= 2) return 0;
        int cnt = 0;
        for (int i = 2; i < n; i++) {
            cnt += isPrime(i) ? 1 : 0;
        }
        return cnt;
    }

    //埃氏筛
//    假定我们要求100以内的质数，我们从2开始遍历。
//    遍历到2，2是质数，于是我们把2的所有倍数全部删除，比如4、6、8、10…
//    遍历到3，3是质数，于是我们把3的所有倍数全都删除，比如6、9、12、15…
//    遍历到4，4是合数已经删除，继续遍历，以此类推即可找到范围内所有的素数
    public int countPrimes2(int n) {
        int[] isPrime = new int[n];
        Arrays.fill(isPrime, 1);
        int ans = 0;
        for (int i = 2; i < n; ++i) {
            if (isPrime[i] == 1) {
                ans += 1;
                // i是质数，把i的倍数全部置为非质数
                if ((long) i * i < n) {
                    for (int j = i * i; j < n; j += i) {
                        isPrime[j] = 0;
                    }
                }
            }
        }
        return ans;
    }

    public int erichsen() {
        int n = (int) 1e5;
        //是否是质数，1-质数 0-合数
        int[] isPrime = new int[n];
        Arrays.fill(isPrime, 1);
        int count = 0;

        //采用i < n / i 防止i*i超范围
        for (int i = 2; i < n / i; i++) {
            if (isPrime[i] == 1) {
                count++;
                //删除i的倍数
                for (int j = i * i; j < n; j += i) {
                    isPrime[j] = 0;
                }
            }
        }
        return count;
    }

    // 欧拉筛
    //欧拉筛是埃氏筛的改进版本，由于某些合数会有很多质因数，因此在删除的过程中会重复的删除，比如6，遍历到2时会进行删除，遍历到3时也会进行删除，所以为了避免这样的开销，欧拉筛将筛选出来的质数进行记录，在删除的过程中，只通过数的最小质因数筛数。
    //链接：https://leetcode.cn/problems/largest-component-size-by-common-factor/solution/an-gong-yin-shu-ji-suan-zui-da-zu-jian-d-0tm8/

    public int euler() {
        int n = (int) 1e5;
        //判断是否是质数
        int[] isPrime = new int[n];
        //存放质数
        int[] primes = new int[n];
        int k = 0;
        Arrays.fill(isPrime, 1);
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime[i] == 1) {
                primes[k++] = i;
                count++;
            }
            for (int j = 0; primes[j] * i < n; j++) {
                //每个质数都和i相乘 得到合数
                isPrime[primes[j] * i] = 0;
                //primes[j]是i的一个质因数
                if (i % primes[j] == 0) {
                    break;
                }
            }
        }
        return count;
    }

    //2507. 使用质因数之和替换后可以取到的最小值
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

    // 质因数分解
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

    public int smallestValue2(int n) {
        int nxt = facSum(n);
        while (nxt < n) {
            n = nxt;
            nxt = facSum(n);
        }
        return n;
    }

    // 合数由质数组成，不缺分质数或是合数因子
    private int facSum(int n) {
        int i = 2, sum = 0;
        while (i * i <= n) {
            if (n % i == 0) {
                sum += i;
                n /= i;
            } else {
                i++;
            }
        }
        if (n != 1) sum += n;
        return sum;
    }

    //2521. 数组乘积中的不同质因数数目 考察质因数分解
    public int distinctPrimeFactors(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.addAll(fac(num));
        }
        return set.size();
    }

    //2523. 范围内最接近的两个质数  考察质数
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

    //6309. 分割数组使乘积互质 质因数分解
    public int findValidSplit(int[] nums) {
        int n = nums.length;
        Map<Integer, List<Integer>> map = new HashMap<>();
        Map<Integer, Integer> suffixCnt = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<Integer> f = fac(nums[i]);
            map.put(i, f);
            for (int num : f) {
                suffixCnt.put(num, suffixCnt.getOrDefault(num, 0) + 1);
            }
        }
        Map<Integer, Integer> prefixCnt = new HashMap<>();
        for (int i = 0; i < n - 1; i++) {
            List<Integer> f = map.get(i);
            for (int num : f) {
                prefixCnt.put(num, prefixCnt.getOrDefault(num, 0) + 1);
                suffixCnt.put(num, suffixCnt.getOrDefault(num, 0) - 1);
                if (suffixCnt.get(num) <= 0) suffixCnt.remove(num);
            }
            if (noCommon(prefixCnt, suffixCnt)) return i;
        }
        return -1;
    }

    private boolean noCommon(Map<Integer, Integer> prefixCnt, Map<Integer, Integer> suffixCnt) {
        for (Map.Entry<Integer, Integer> entry : prefixCnt.entrySet()) {
            if (suffixCnt.containsKey(entry.getKey())) return false;
        }
        return true;
    }

    // 双指针
    public int findValidSplitDualPointer(int[] nums) {
        int l = 0;
        int max = 1;
        while (max < nums.length && l < max) {
            for (int index = max; index < nums.length; index++) {
                if (gcd(nums[l], nums[index]) != 1) {
                    max = Math.max(max, index);
                }
            }
            l++;
        }
        if (max >= nums.length - 1) {
            return -1;
        } else {
            return l;
        }
    }

    // 258 各位相加
    public int addDigits(int num) {
        while (num >= 10) {
            int sum = 0;
            while (num != 0) {
                sum += num % 10;
                num /= 10;
            }
            num = sum;
        }
        return num;
    }

    public int addDigitsMath(int num) {
        return (num - 1) % 9 + 1;
    }


    // 287 寻找重复数
    public int findDuplicate(int[] nums) {
        int n = nums.length;
        for (int num : nums) {
            nums[num % n] += n;
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] >= 2 * n) return i;
        }
        return 0;
    }

    //不修改原数组
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

    // 448 找到所有数组中消失的数字(没有出现的)
    public List<Integer> findDisappearedNumbers(int[] nums) {
        int n = nums.length;
        for (int num : nums) {
            nums[(num - 1) % n] += n;
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (nums[i] <= n) result.add(i + 1);
        }
        return result;
    }

    // 442 数组中重复的数据 有一些出现2次，有一些出现1次，找出出现2次的
    public List<Integer> findDuplicates(int[] nums) {
        List<Integer> list = new ArrayList<>();
        int n = nums.length;
        for (int num : nums) {
            nums[(num - 1) % n] = nums[(num - 1) % n] + n;
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] > 2 * n && nums[i] <= 3 * n) {
                list.add(i + 1);
            }
        }
        return list;
    }


    // 125 验证回文串
    public boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            while (l < r && !Character.isLetterOrDigit(s.charAt(l))) {
                l++;
            }
            while (l < r && !Character.isLetterOrDigit(s.charAt(r))) {
                r--;
            }

            if (l < r) {
                if (Character.toLowerCase(s.charAt(l++)) != Character.toLowerCase(s.charAt(r--))) return false;
            }
        }
        return true;
    }

    // 680 验证回文字符串
    public boolean validPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            if (s.charAt(l) == s.charAt(r)) {
                l++;
                r--;
            } else {
                return check(s, l, r - 1) || check(s, l + 1, r);
            }
        }
        return true;
    }

    private boolean check(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left++) != s.charAt(right--)) return false;
        }
        return true;
    }

    // 205 同构字符串
    // st aa
    public boolean isIsomorphic(String s, String t) {
        Map<Character, Character> s2t = new HashMap<>();
        Map<Character, Character> t2s = new HashMap<>();
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            char x = s.charAt(i), y = t.charAt(i);
            if ((s2t.containsKey(x) && s2t.get(x) != y) || (t2s.containsKey(y) && t2s.get(y) != x)) {
                return false;
            }
            s2t.put(x, y);
            t2s.put(y, x);
        }
        return true;
    }

    // 290单词规律
    public boolean wordPattern(String pattern, String s) {
        String[] ss = s.split(" ");
        if (pattern.length() != ss.length) return false;
        Map<Character, String> map = new HashMap<>();
        Map<String, Character> map2 = new HashMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            if (map.containsKey(pattern.charAt(i))) {
                if (!map.get(pattern.charAt(i)).equals(ss[i])) return false;
            } else {
                map.put(pattern.charAt(i), ss[i]);
            }
            if (map2.containsKey(ss[i])) {
                if (!map2.get(ss[i]).equals(pattern.charAt(i))) return false;
            } else {
                map2.put(ss[i], pattern.charAt(i));
            }

        }
        return true;
    }

    // 890 查找和替换模式
    public List<String> findAndReplacePattern(String[] words, String pattern) {
        List<String> ans = new ArrayList<>();
        for (String word : words) {
            if (match(word, pattern) && match(pattern, word)) {
                ans.add(word);
            }
        }
        return ans;
    }

    public boolean match(String word, String pattern) {
        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < word.length(); ++i) {
            char x = word.charAt(i), y = pattern.charAt(i);
            if (!map.containsKey(x)) {
                map.put(x, y);
            } else if (map.get(x) != y) { // word 中的同一字母必须映射到 pattern 中的同一字母上
                return false;
            }
        }
        return true;
    }

    // 893 特殊等价字符串组
    public int numSpecialEquivGroups(String[] words) {
        Set<String> set = new HashSet<>();
        for (String word : words) {
            int[] cnt = new int[52];
            for (int i = 0; i < word.length(); i++) {
                cnt[word.charAt(i) - 'a' + 26 * (i % 2)]++;
            }
            set.add(Arrays.toString(cnt));
        }
        return set.size();
    }

    // 1684 统计一致字符串的数目
    public int countConsistentStrings(String allowed, String[] words) {
        int[] cnt = new int[26];
        for (char c : allowed.toCharArray()) {
            cnt[c - 'a']++;
        }
        int ans = 0;
        for (String word : words) {
            boolean isConsis = true;
            for (char c : word.toCharArray()) {
                if (cnt[c - 'a'] == 0) {
                    isConsis = false;
                    break;
                }
            }
            if (isConsis) ans++;
        }
        return ans;
    }

    //1657. 确定两个字符串是否接近
    public boolean closeStrings(String word1, String word2) {
        if (word1.length() != word2.length()) return false;
        int n = word1.length();
        int[] cnt1 = new int[26];
        int[] cnt2 = new int[26];
        int[] ccnt = new int[26];
        char[] c1 = word1.toCharArray();
        char[] c2 = word2.toCharArray();
        for (char c : c1) {
            cnt1[c - 'a']++;
            if (ccnt[c - 'a'] == 0) ccnt[c - 'a']++;
        }
        for (char c : c2) {
            cnt2[c - 'a']++;
            if (ccnt[c - 'a'] > 0) ccnt[c - 'a']--;
        }
        int[] cnt = new int[n + 1];
        for (int i = 0; i < 26; i++) {
            if (ccnt[i] != 0) return false;
            if (cnt1[i] > 0) cnt[cnt1[i]]++;
            if (cnt2[i] > 0) cnt[cnt2[i]]--;
        }
        for (int i = 0; i < n; i++) {
            if (cnt[i] > 0) return false;
        }
        return true;

    }

    //667 优美的排列2
    public int[] constructArray(int n, int k) {
        int[] res = new int[n];

        // 第 1 步：构造等差数列，把 1 到 n - k - 1 赋值结果数组的前面
        for (int i = 0; i < n - k - 1; i++) {
            res[i] = i + 1;
        }

        // 第 2 步：构造交错数列，下标从 n - k - 1 开始，数值从 n - k 开始
        // 控制交错的变量
        int j = 0;
        // k+1个数有k个不同差值，从1到k,等差的1和最后的1重复
        int left = n - k;
        int right = n;
        for (int i = n - k - 1; i < n; i++) {
            if (j % 2 == 0) {
                res[i] = left;
                left++;
            } else {
                res[i] = right;
                right--;
            }
            j++;
        }
        return res;
    }

    // 1704 判断字符串的两半是否相似
    public boolean halvesAreAlike(String s) {
        Set<Character> set = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'));
        int n = s.length();
        int cnt = 0;
        for (int i = 0; i < n / 2; i++) {
            if (set.contains(s.charAt(i))) {
                cnt++;
            }
            if (set.contains(s.charAt(i + n / 2))) {
                cnt--;
            }
        }
        return cnt == 0;
    }

    //1375. 二进制字符串前缀一致的次数
    public int numTimesAllBlue(int[] flips) {
        int n = flips.length;
        int[] cnt = new int[n + 1];
        int ans = 0;
        int max = 0;
        for (int f : flips) {
            max = Math.max(max, f);
            cnt[f]++;
            if (check1375(cnt, max)) ans++;
        }
        return ans;
    }

    private boolean check1375(int[] cnt, int end) {
        for (int i = 1; i <= end; i++) {
            if (cnt[i] == 0) return false;
        }
        return true;
    }

    public int numTimesAllBlue2(int[] flips) {
        int n = flips.length;
        int ans = 0, right = 0;
        for (int i = 0; i < n; ++i) {
            right = Math.max(right, flips[i]);
            if (right == i + 1) {
                ++ans;
            }
        }
        return ans;
    }

    //2735. 收集巧克力 枚举
    public long minCost(int[] nums, int x) {
        int n = nums.length;
        long[] sum = new long[n];
        for (int i = 0; i < n; i++)
            sum[i] = (long) i * x; // 操作 i 次
        for (int i = 0; i < n; i++) { // 子数组左端点
            int mn = nums[i];
            for (int j = i; j < n + i; j++) { // 子数组右端点（把数组视作环形的）
                mn = Math.min(mn, nums[j % n]); // 从 nums[i] 到 nums[j%n] 的最小值
                sum[j - i] += mn; // 累加操作 j-i 次的成本
            }
        }
        long ans = Long.MAX_VALUE;
        for (long s : sum) ans = Math.min(ans, s);
        return ans;
    }

    //2745. 构造最长的新字符串
    public int longestString(int x, int y, int z) {
        return (Math.min(x, y) * 2 + (x != y ? 1 : 0) + z) * 2;
    }

    //2731. 移动机器人
    public int sumDistance(int[] nums, String s, int d) {
        final long MOD = (long) 1e9 + 7;
        int n = nums.length;
        long[] a = new long[n];
        for (int i = 0; i < n; i++) // 注意 2e9+1e9 溢出了
            a[i] = (long) nums[i] + d * ((s.charAt(i) & 2) - 1); // L=-1, R=1
        long ans = 0, sum = 0;
        Arrays.sort(a);
        for (int i = 0; i < n; i++) {
            ans = (ans + i * a[i] - sum) % MOD;
            sum += a[i];
        }
        return (int) ans;
    }

    // 791 自定义字符串排序
    public String customSortString(String order, String s) {
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            cnt[c - 'a']++;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : order.toCharArray()) {
            while (cnt[c - 'a']-- > 0) {
                sb.append(c);
            }
        }
        for (char c = 'a'; c <= 'z'; c++) {
            while (cnt[c - 'a']-- > 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // 1002 查找共用字符
    // 技巧：min求公共字符
    public List<String> commonChars(String[] words) {
        int[] cnt = new int[26];
        if (words.length == 0) return new ArrayList<>();
        Arrays.fill(cnt, Integer.MAX_VALUE);
        for (String word : words) {
            int[] freq = new int[26];
            for (char c : word.toCharArray()) {
                freq[c - 'a']++;
            }
            for (int i = 0; i < 26; i++) {
                cnt[i] = Math.min(cnt[i], freq[i]);
            }
        }
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < cnt[i]; j++) {
                ans.add(String.valueOf((char) (i + 'a')));
            }
        }
        return ans;
    }

    // 905 按奇偶排序数组
    public int[] sortArrayByParity(int[] nums) {
        int n = nums.length;
        for (int i = 0, j = n - 1; i < j; i++) {
            if (nums[i] % 2 != 0) {
                int tmp = nums[j];
                nums[j--] = nums[i];
                nums[i--] = tmp;
            }
        }
        return nums;
    }

    // 1089 复写0
    //双指针模拟栈
    public void duplicateZeros(int[] arr) {
        int top = 0;
        int i = -1;
        int n = arr.length;
        while (top < n) {
            i++;
            if (arr[i] == 0) {
                top += 2;
            } else {
                top += 1;
            }
        }
        int j = n - 1;
        // 最后一个是0top=n+1
        // 最后一个不是0 top=n
        if (top == n + 1) {
            arr[j] = 0;
            j--;
            i--;
        }
        while (j >= 0) {
            arr[j] = arr[i];
            j--;
            if (arr[i] == 0) {
                arr[j] = 0;
                j--;
            }
            i--;
        }
    }

    public void duplicateZeros2(int[] arr) {
        int n = arr.length, i = 0, j = 0;
        while (j < n) {
            if (arr[i] == 0) j++;
            i++;
            j++;
        }
        i--;
        j--;
        while (i >= 0) {
            if (j < n) arr[j] = arr[i];
            if (arr[i] == 0 && --j >= 0) arr[j] = 0;
            i--;
            j--;
        }
    }

    // 1217玩筹码
    //转换为求奇偶少的一个
    public int minCostToMoveChips(int[] position) {
        int odd = 0, even = 0;
        for (int pos : position) {
            if ((pos & 1) == 0) {
                odd++;
            } else {
                even++;
            }
        }
        return Math.min(odd, even);
    }

    // 2354. 优质数对的数目
    public long countExcellentPairs(int[] nums, int k) {
        long res = 0;
        HashMap<Integer, Set<Integer>> map = new HashMap<>();
        for (int num : nums) {
            int cnt = Integer.bitCount(num);
            map.putIfAbsent(cnt, new HashSet<>());
            map.get(cnt).add(num);
        }
        for (Integer i : map.keySet()) {
            for (Integer j : map.keySet()) {
                if (i + j >= k) {
                    res += map.get(i).size() * map.get(j).size();
                }
            }
        }
        return res;
    }

    //1619. 删除某些元素后的数组均值
    public double trimMean(int[] arr) {
        int n = arr.length;
        Arrays.sort(arr);
        int l = (int) (n * 0.05);
        int r = n - l - 1;
        double sum = 0;
        for (int i = l; i <= r; i++) {
            sum += arr[i];
        }
        return sum / (r - l + 1);
    }

    //927. 三等分 Hard
    public int[] threeEqualParts(int[] arr) {
        int n = arr.length;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += arr[i];
        }
        if (sum % 3 != 0) return new int[]{-1, -1};
        if (sum == 0) return new int[]{0, 2};
        int partial = sum / 3;
        int first = 0, second = 0, third = 0, cur = 0;
        for (int i = 0; i < n; i++) {
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
        int len = n - third;
        if (first + len <= second && second + len <= third) {
            int idx = 0;
            while (third + idx < n) {
                if (arr[first + idx] != arr[second + idx] || arr[first + idx] != arr[third + idx]) {
                    return new int[]{-1, -1};
                }
                idx++;
            }
            return new int[]{first + len - 1, second + len};
        }
        return new int[]{-1, -1};
    }


    /**
     * 整数划分
     */
    public int partitionInteger(int n, int m) {
        if (n < 1 || m < 1) return 0;
        if (n == 1 || m == 1) return 1;
        if (n < m) return partitionInteger(n, n);
        if (n == m) return partitionInteger(n, m - 1) + 1;
        return partitionInteger(n, m - 1) + partitionInteger(n - m, m);
    }

    // 4 寻找两个正序数组的中位数
    // O(n)
    public double findMedianSortedArrays1(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int k = (m + n) / 2;
        int idx1 = 0, idx2 = 0;
        int cnt = 0;
        int kth = 0;
        int tmp = 0;
        while (cnt <= k) {
            if (idx1 >= m) {
                kth = nums2[idx2++];
            } else if (idx2 >= n) {
                kth = nums1[idx1++];
            } else if (nums1[idx1] < nums2[idx2]) {
                kth = nums1[idx1++];
            } else {
                kth = nums2[idx2++];
            }
            cnt++;
            if (cnt == k) tmp = kth;
        }
        if ((m + n) % 2 == 0) return (tmp + kth) * 0.5;
        return (double) kth;
    }

    //O(logN)
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        if ((m + n) % 2 == 1) {
            return getKthNum(nums1, nums2, (m + n) / 2 + 1);
        } else {
            return (getKthNum(nums1, nums2, (m + n) / 2) + getKthNum(nums1, nums2, (m + n) / 2 + 1)) / 2;
        }
    }

    private double getKthNum(int[] nums1, int[] nums2, int k) {
        /* 主要思路：要找到第 k (k>1) 小的元素，那么就取 pivot1 = nums1[k/2-1] 和 pivot2 = nums2[k/2-1] 进行比较
         * 这里的 "/" 表示整除
         * nums1 中小于等于 pivot1 的元素有 nums1[0 .. k/2-2] 共计 k/2-1 个
         * nums2 中小于等于 pivot2 的元素有 nums2[0 .. k/2-2] 共计 k/2-1 个
         * 取 pivot = min(pivot1, pivot2)，两个数组中小于等于 pivot 的元素共计不会超过 (k/2-1) + (k/2-1) <= k-2 个
         * 这样 pivot 本身最大也只能是第 k-1 小的元素
         * 如果 pivot = pivot1，那么 nums1[0 .. k/2-1] 都不可能是第 k 小的元素。把这些元素全部 "删除"，剩下的作为新的 nums1 数组
         * 如果 pivot = pivot2，那么 nums2[0 .. k/2-1] 都不可能是第 k 小的元素。把这些元素全部 "删除"，剩下的作为新的 nums2 数组
         * 由于我们 "删除" 了一些元素（这些元素都比第 k 小的元素要小），因此需要修改 k 的值，减去删除的数的个数
         */
        int index1 = 0, index2 = 0;
        while (true) {
            // 边界情况
            if (index1 >= nums1.length) {
                return nums2[index2 + k - 1];
            }
            if (index2 >= nums2.length) {
                return nums1[index1 + k - 1];
            }

            if (k == 1) {
                return Math.min(nums1[index1], nums2[index2]);
            }
            int newIndex1 = Math.min(nums1.length, index1 + k / 2) - 1;
            int newIndex2 = Math.min(nums2.length, index2 + k / 2) - 1;
            int pivot1 = nums1[newIndex1], pivot2 = nums2[newIndex2];
            if (pivot1 <= pivot2) {
                k -= (newIndex1 - index1 + 1);
                index1 = newIndex1 + 1;
            } else {
                k -= (newIndex2 - index2 + 1);
                index2 = newIndex2 + 1;
            }
        }
    }

    //1093. 大样本统计
    // 找中位数技巧
    public double[] sampleStats(int[] count) {
        int n = count.length;
        int total = Arrays.stream(count).sum();
        double mean = 0.0;
        double median = 0.0;
        int minnum = 256;
        int maxnum = 0;
        int mode = 0;

        int left = (total + 1) / 2;
        int right = (total + 2) / 2;
        int cnt = 0;
        int maxfreq = 0;
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += (long) count[i] * i;
            if (count[i] > maxfreq) {
                maxfreq = count[i];
                mode = i;
            }
            if (count[i] > 0) {
                if (minnum == 256) {
                    minnum = i;
                }
                maxnum = i;
            }
            if (cnt < right && cnt + count[i] >= right) {
                median += i;
            }
            if (cnt < left && cnt + count[i] >= left) {
                median += i;
            }
            cnt += count[i];
        }
        mean = (double) sum / total;
        median = median / 2.0;
        return new double[]{minnum, maxnum, mean, median, mode};
    }

    // 15 三数之和
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int k = 0; k < nums.length - 2; k++) {
            if (nums[k] > 0) break;
            if (k > 0 && nums[k] == nums[k - 1]) continue;
            int i = k + 1, j = nums.length - 1;
            while (i < j) {
                int sum = nums[k] + nums[i] + nums[j];
                if (sum < 0) {
                    while (i < j && nums[i] == nums[++i]) ;
                } else if (sum > 0) {
                    while (i < j && nums[j] == nums[--j]) ;
                } else {
                    res.add(new ArrayList<>(Arrays.asList(nums[k], nums[i], nums[j])));
                    while (i < j && nums[i] == nums[++i]) ;
                    while (i < j && nums[j] == nums[--j]) ;
                }
            }
        }
        return res;
    }

    // 16 最接近的三数之和
    public int threeSumClosest(int[] nums, int target) {
        if (nums.length < 3) {
            return 0;
        }
        Arrays.sort(nums);
        int best = 10000;
        for (int k = 0; k < nums.length - 2; k++) {
            if (k > 0 && nums[k] == nums[k - 1]) {
                continue;
            }

            int i = k + 1, j = nums.length - 1;

            while (i < j) {

                int sum = nums[k] + nums[i] + nums[j];
                if (Math.abs(target - sum) < Math.abs(target - best)) {
                    best = sum;
                }
                if (sum > target) {
                    while (i < j && nums[j] == nums[--j]) ;
                } else if (sum < target) {
                    while (i < j && nums[i] == nums[++i]) ;
                } else {
                    return sum;
                }
            }
        }

        return best;
    }

    // 259 较小的三数之和
    //给定一个长度为 n 的整数数组和一个目标值 target ，寻找能够使条件 nums[i] + nums[j] + nums[k] < target 成立的
//三元组 i, j, k 个数（0 <= i < j < k < n）。
    public static int threeSumSmallerBS(int[] nums, int target) {
        int n = nums.length;
        if (n < 3) return 0;
        Arrays.sort(nums);
        int ans = 0;
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                int l = j + 1, r = n - 1, x = target - nums[i] - nums[j];
                while (l < r) {
                    // 小于x的最靠右的下标
                    int mid = l + r + 1 >> 1;
                    if (nums[mid] >= x) {
                        r = mid - 1;
                    } else {
                        l = mid;
                    }
                }
                if (nums[i] + nums[j] + nums[l] < target) {
                    ans += l - j;
                }
            }
        }
        return ans;
    }

    // 双指针
    public int threeSumSmallerDualPointer(int[] nums, int target) {
        Arrays.sort(nums);
        int n = nums.length;
        int ans = 0;
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1, k = n - 1; j < k; ) {
                int sum = nums[i] + nums[j] + nums[k];
                if (sum >= target) {
                    k--;
                } else {
                    ans += k - j;
                    j++;
                }
            }
        }
        return ans;
    }

    // 18 四数之和
    //输入：nums = [1,0,-1,0,-2,2], target = 0
//输出：[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
// 0 <= nums.length <= 200
// -109 <= nums[i] <= 109
// -109 <= target <= 109
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums.length < 4) {
            return result;
        }
        Arrays.sort(nums);
        for (int k = 0; k < nums.length - 3; k++) {
            if (k > 0 && nums[k] == nums[k - 1]) continue;

            long kmin = (long) nums[k] + nums[k + 1] + nums[k + 2] + nums[k + 3];
            long kmax = (long) nums[k] + nums[nums.length - 1] + nums[nums.length - 2] + nums[nums.length - 3];
            if (kmin > target) break;
            if (kmax < target) continue;
            for (int f = k + 1; f < nums.length - 2; f++) {
                if (f > k + 1 && nums[f] == nums[f - 1]) continue;
                long fmin = (long) nums[k] + nums[f] + nums[f + 1] + nums[f + 2];
                long fmax = (long) nums[k] + nums[f] + nums[nums.length - 2] + nums[nums.length - 1];
                if (fmin > target) break;
                if (fmax < target) continue;
                int l = f + 1, r = nums.length - 1;
                while (l < r) {
                    int sum = nums[k] + nums[f] + nums[l] + nums[r];
                    if (sum < target) {
                        while (l < r && nums[l + 1] == nums[l]) {
                            l++;
                        }
                        l++;
                    } else if (sum > target) {
                        while (l < r && nums[r - 1] == nums[r]) {
                            r--;
                        }
                        r--;
                    } else {
                        result.add(new ArrayList<>(Arrays.asList(nums[k], nums[f], nums[l], nums[r])));
                        while (l < r && nums[r - 1] == nums[r]) {
                            r--;
                        }
                        r--;
                        while (l < r && nums[l + 1] == nums[l]) {
                            l++;
                        }
                        l++;
                    }
                }
            }
        }
        return result;
    }

    // 454 四数相加2
    public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int a : nums1) {
            for (int b : nums2) {
                map.put(a + b, map.getOrDefault(a + b, 0) + 1);
            }
        }
        int ans = 0;
        for (int c : nums3) {
            for (int d : nums4) {
                if (map.containsKey(-c - d)) ans += map.get(-c - d);
            }
        }
        return ans;
    }


    //31 下一个排列
    //[1,3,5,4,1]-》[1, 4, 1, 3, 5]
//输入：nums = [1,2,3]
//输出：[1,3,2]
//输入：nums = [3,2,1]
//输出：[1,2,3]
//输入：nums = [1,1,5]
//输出：[1,5,1]
    public void nextPermutation(int[] nums) {
        int left = nums.length - 2;
        // 1.从后往前找到第一个严格递增的起始位置
        while (left >= 0 && nums[left] >= nums[left + 1]) {
            left--;
        }
        // left = -1无需swap
        if (left >= 0) {
            // 2.从后往前找到第一个严格大于left的位置
            int right = nums.length - 1;
            while (right >= 0 && nums[right] <= nums[left]) {
                right--;
            }
            swap(nums, left, right);
        }
        // 3 重新排列left后面的数组(已经非递增)
        reverse(nums, left + 1, nums.length - 1);
    }

    private void reverse(int[] nums, int left, int right) {
        int a = left, b = right;
        while (a < b) {
            swap(nums, a++, b--);
        }
    }

    private void swap(int[] nums, int k, int m) {
        int temp = nums[k];
        nums[k] = nums[m];
        nums[m] = temp;
    }

    //1053. 交换一次的先前排列  贪心
    public int[] prevPermOpt1(int[] arr) {
        int n = arr.length;
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] <= arr[i + 1]) continue;
            int j = n - 1;
            while (arr[j] >= arr[i] || arr[j] == arr[j - 1]) {
                j--;
            }
            swap(arr, i, j);
            break;
        }
        return arr;
    }


    //575. 分糖果
    public int distributeCandies(int[] candies) {
        Set<Integer> set = new HashSet<>();
        for (int i : candies) {
            set.add(i);
        }
        return Math.min(set.size(), candies.length / 2);
    }


    //189 轮转数组 旋转数组
    //给你一个数组，将数组中的元素向右轮转 k 个位置，其中 k 是非负数。
//输入: nums = [1,2,3,4,5,6,7], k = 3
//输出: [5,6,7,1,2,3,4]
//解释:
//向右轮转 1 步: [7,1,2,3,4,5,6]
//向右轮转 2 步: [6,7,1,2,3,4,5]
//向右轮转 3 步: [5,6,7,1,2,3,4]

    public void rotate1(int[] nums, int k) {
        int[] tmp = nums.clone();
        for (int i = 0; i < nums.length; i++) {
            nums[(i + k) % nums.length] = tmp[i];
        }
    }

    public void rotate2(int[] nums, int k) {
        int n = nums.length;
        int count = gcd(k, n);
        for (int start = 0; start < count; start++) {
            int curIdx = start;
            int prevNum = nums[start];
            do {
                int nextIdx = (curIdx + k) % n;
                int nextNum = nums[nextIdx];
                nums[nextIdx] = prevNum;
                prevNum = nextNum;
                curIdx = nextIdx;
            } while (curIdx != start);
        }
    }


    public void rotate3(int[] nums, int k) {
        k %= nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }


    // 求最小公倍数
    private int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    //欧几里得算法又称辗转相除法
    //求最大公约数
    private int gcd(int x, int y) {
        return y > 0 ? gcd(y, x % y) : x;
    }

    private long gcd(long x, long y) {
        return y > 0 ? gcd(y, x % y) : x;
    }


    // 求最小公倍数为 K 的子数组数目
    public int subarrayLCM(int[] nums, int k) {
        int n = nums.length;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            long l = nums[i];
            for (int j = i; j < n; j++) {
                long g = gcd(l, nums[j]);
                l = l / g * nums[j];
                if (l == k) ans++;
                if (l > k) break;
            }
        }
        return ans;
    }

    //2427. 公因子的数目
    public int commonFactors(int a, int b) {
        int g = gcd(a, b);
        int ans = 0;
        for (int i = 1; i * i <= g; i++) {
            if (g % i == 0) {
                ans++;
                if (i * i != g) ans++;

            }
        }
        return ans;
    }


    //offer 60 n个骰子
    //把n个骰子扔在地上，所有骰子朝上一面的点数之和为s。输入n，打印出s的所有可能的值出现的概率。
// 你需要用一个浮点数数组返回答案，其中第 i 个元素代表这 n 个骰子所能掷出的点数集合中第 i 小的那个的概率。
// 输入: 1
//输出: [0.16667,0.16667,0.16667,0.16667,0.16667,0.16667]
    public double[] dicesProbability(int n) {
        //因为最后的结果只与前一个动态转移数组有关，所以这里只需要设置一个一维的动态转移数组
        //原本dp[i][j]表示的是前i个骰子的点数之和为j的概率，现在只需要最后的状态的数组，所以就只用一个一维数组dp[j]表示n个骰子下每个结果的概率。
        //初始是1个骰子情况下的点数之和情况，就只有6个结果，所以用dp的初始化的size是6个
        double[] dp = new double[6];
        //只有一个数组
        Arrays.fill(dp, 1.0 / 6.0);
        //从第2个骰子开始，这里n表示n个骰子，先从第二个的情况算起，然后再逐步求3个、4个···n个的情况
        //i表示当总共i个骰子时的结果
        for (int i = 2; i <= n; i++) {
            //每次的点数之和范围会有点变化，点数之和的值最大是i*6，最小是i*1，i之前的结果值是不会出现的；
            //比如i=3个骰子时，最小就是3了，不可能是2和1，所以点数之和的值的个数是6*i-(i-1)，化简：5*i+1
            //当有i个骰子时的点数之和的值数组先假定是temp
            //[1,1,..1]-[6,6,..6] n*n-1*n+1 = 5*n+1
            double[] temp = new double[5 * i + 1];
            //从i-1个骰子的点数之和的值数组入手，计算i个骰子的点数之和数组的值
            //先拿i-1个骰子的点数之和数组的第j个值，它所影响的是i个骰子时的temp[j+k]的值
            for (int j = 0; j < dp.length; j++) {
                //比如只有1个骰子时，dp[1]是代表当骰子点数之和为2时的概率，它会对当有2个骰子时的点数之和为3、4、5、6、7、8产生影响，因为当有一个骰子的值为2时，另一个骰子的值可以为1~6，产生的点数之和相应的就是3~8；比如dp[2]代表点数之和为3，它会对有2个骰子时的点数之和为4、5、6、7、8、9产生影响；所以k在这里就是对应着第i个骰子出现时可能出现六种情况，这里可能画一个K神那样的动态规划逆推的图就好理解很多
                for (int k = 0; k < 6; k++) {
                    //这里记得是加上dp数组值与1/6的乘积，1/6是第i个骰子投出某个值的概率
                    temp[j + k] += dp[j] * (1.0 / 6.0);
                }
            }
            //i个骰子的点数之和全都算出来后，要将temp数组移交给dp数组，dp数组就会代表i个骰子时的可能出现的点数之和的概率；用于计算i+1个骰子时的点数之和的概率
            dp = temp;
        }
        return dp;
    }

    //28.实现 strStr() 函数。
//  字符串匹配算法 ： KMP
    public int strStr(String haystack, String needle) {
        int n = haystack.length(), m = needle.length();
        if (m == 0) {
            return 0;
        }
        int[] pi = new int[m];
        for (int i = 1, j = 0; i < m; i++) {
            while (j > 0 && needle.charAt(i) != needle.charAt(j)) {
                j = pi[j - 1];
            }
            if (needle.charAt(i) == needle.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        for (int i = 0, j = 0; i < n; i++) {
            while (j > 0 && haystack.charAt(i) != needle.charAt(j)) {
                j = pi[j - 1];
            }
            if (haystack.charAt(i) == needle.charAt(j)) {
                j++;
            }
            if (j == m) {
                return i - m + 1;
            }
        }
        return -1;
    }

    // KMP 算法
    // ss: 原串(string)  pp: 匹配串(pattern)
    public int strStr2(String ss, String pp) {
        if (pp.isEmpty()) return 0;

        // 分别读取原串和匹配串的长度
        int n = ss.length(), m = pp.length();
        // 原串和匹配串前面都加空格，使其下标从 1 开始
        ss = " " + ss;
        pp = " " + pp;

        char[] s = ss.toCharArray();
        char[] p = pp.toCharArray();

        // 构建 next 数组，数组长度为匹配串的长度（next 数组是和匹配串相关的）
        int[] next = new int[m + 1];
        // 构造过程 i = 2，j = 0 开始，i 小于等于匹配串长度 【构造 i 从 2 开始】
        for (int i = 2, j = 0; i <= m; i++) {
            // 匹配不成功的话，j = next(j)
            while (j > 0 && p[i] != p[j + 1]) j = next[j];
            // 匹配成功的话，先让 j++
            if (p[i] == p[j + 1]) j++;
            // 更新 next[i]，结束本次循环，i++
            next[i] = j;
        }

        // 匹配过程，i = 1，j = 0 开始，i 小于等于原串长度 【匹配 i 从 1 开始】
        for (int i = 1, j = 0; i <= n; i++) {
            // 匹配不成功 j = next(j)
            while (j > 0 && s[i] != p[j + 1]) j = next[j];
            // 匹配成功的话，先让 j++，结束本次循环后 i++
            if (s[i] == p[j + 1]) j++;
            // 整一段匹配成功，直接返回下标
            if (j == m) return i - m;
        }

        return -1;
    }

    // 459 重复的子字符串
    public boolean repeatedSubstringPattern(String s) {
        int n = s.length();
        for (int i = 1; i * 2 <= n; ++i) {
            if (n % i == 0) {
                boolean match = true;
                for (int j = i; j < n; ++j) {
                    if (s.charAt(j) != s.charAt(j - i)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean repeatedSubstringPattern2(String s) {
        return (s + s).indexOf(s, 1) != s.length();
    }

    public boolean repeatedSubstringPatternKMP(String s) {
        return kmp(s + s, s);
    }

    public boolean kmp(String query, String pattern) {
        int n = query.length();
        int m = pattern.length();
        int[] fail = new int[m];
        Arrays.fill(fail, -1);
        for (int i = 1; i < m; ++i) {
            int j = fail[i - 1];
            while (j != -1 && pattern.charAt(j + 1) != pattern.charAt(i)) {
                j = fail[j];
            }
            if (pattern.charAt(j + 1) == pattern.charAt(i)) {
                fail[i] = j + 1;
            }
        }
        int match = -1;
        for (int i = 1; i < n - 1; ++i) {
            while (match != -1 && pattern.charAt(match + 1) != query.charAt(i)) {
                match = fail[match];
            }
            if (pattern.charAt(match + 1) == query.charAt(i)) {
                ++match;
                if (match == m - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    // 1764. 通过连接另一个数组的子数组得到一个数组
    public boolean canChooseGreedy(int[][] groups, int[] nums) {
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

    public boolean canChooseKMP(int[][] groups, int[] nums) {
        int k = 0;
        for (int i = 0; i < groups.length; i++) {
            k = find(nums, k, groups[i]);
            if (k == -1) {
                return false;
            }
            k += groups[i].length;
        }
        return true;
    }

    // 数组KMP:nums 原串 g 匹配串 k从nums k开始
    public int find(int[] nums, int k, int[] g) {
        int m = g.length, n = nums.length;
        if (k + g.length > nums.length) {
            return -1;
        }
        int[] pi = new int[m];
        for (int i = 1, j = 0; i < m; i++) {
            while (j > 0 && g[i] != g[j]) {
                j = pi[j - 1];
            }
            if (g[i] == g[j]) {
                j++;
            }
            pi[i] = j;
        }
        for (int i = k, j = 0; i < n; i++) {
            while (j > 0 && nums[i] != g[j]) {
                j = pi[j - 1];
            }
            if (nums[i] == g[j]) {
                j++;
            }
            if (j == m) {
                return i - m + 1;
            }
        }
        return -1;
    }

    //686. 重复叠加字符串匹配
    public int repeatedStringMatch(String a, String b) {
        int m = a.length(), n = b.length();
        int cnt = (n + m - 1) / m;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= cnt; i++) {
            sb.append(a);
        }
        int idx = sb.toString().indexOf(b);
        if (idx == -1) return -1;
        return ((idx + n) > m * cnt) ? cnt + 1 : cnt;
    }

    // Rabin-Karp 算法
    // 链接：https://leetcode.cn/problems/repeated-string-match/solution/zhong-fu-die-jia-zi-fu-chuan-pi-pei-by-l-vnye/
    static final int kMod1 = 1000000007;
    static final int kMod2 = 1337;

    public int repeatedStringMatchRabinKarp(String a, String b) {
        int an = a.length(), bn = b.length();
        int index = strStrRabinKarp(a, b);
        if (index == -1) {
            return -1;
        }
        if (an - index >= bn) {
            return 1;
        }
        return (bn + index - an - 1) / an + 2;
    }

    public int strStrRabinKarp(String haystack, String needle) {
        int n = haystack.length(), m = needle.length();
        if (m == 0) {
            return 0;
        }

        int k1 = 1000000009;
        int k2 = 1337;
        Random random = new Random();
        int kMod1 = random.nextInt(k1) + k1;
        int kMod2 = random.nextInt(k2) + k2;

        long hashNeedle = 0;
        for (int i = 0; i < m; i++) {
            char c = needle.charAt(i);
            hashNeedle = (hashNeedle * kMod2 + c) % kMod1;
        }
        long hashHaystack = 0, extra = 1;
        for (int i = 0; i < m - 1; i++) {
            hashHaystack = (hashHaystack * kMod2 + haystack.charAt(i % n)) % kMod1;
            extra = (extra * kMod2) % kMod1;
        }
        for (int i = m - 1; (i - m + 1) < n; i++) {
            hashHaystack = (hashHaystack * kMod2 + haystack.charAt(i % n)) % kMod1;
            if (hashHaystack == hashNeedle) {
                return i - m + 1;
            }
            hashHaystack = (hashHaystack - extra * haystack.charAt((i - m + 1) % n)) % kMod1;
            hashHaystack = (hashHaystack + kMod1) % kMod1;
        }
        return -1;
    }

    public int repeatedStringMatchKMP(String a, String b) {
        int an = a.length(), bn = b.length();
        int index = strStrKMP(a, b);
        if (index == -1) {
            return -1;
        }
        if (an - index >= bn) {
            return 1;
        }
        return (bn + index - an - 1) / an + 2;
    }

    public int strStrKMP(String haystack, String needle) {
        int n = haystack.length(), m = needle.length();
        if (m == 0) {
            return 0;
        }
        int[] pi = new int[m];
        for (int i = 1, j = 0; i < m; i++) {
            while (j > 0 && needle.charAt(i) != needle.charAt(j)) {
                j = pi[j - 1];
            }
            if (needle.charAt(i) == needle.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        for (int i = 0, j = 0; i - j < n; i++) { // b 开始匹配的位置是否超过第一个叠加的 a
            while (j > 0 && haystack.charAt(i % n) != needle.charAt(j)) { // haystack 是循环叠加的字符串，所以取 i % n
                j = pi[j - 1];
            }
            if (haystack.charAt(i % n) == needle.charAt(j)) {
                j++;
            }
            if (j == m) {
                return i - m + 1;
            }
        }
        return -1;
    }

    //1016. 子串能表示从 1 到 N 数字的二进制串
    public boolean queryString(String s, int n) {
        for (int i = 1; i <= n; i++) {
            String sub = Integer.toBinaryString(i);
            if (s.indexOf(sub) == -1) return false;
        }
        return true;
    }

    public boolean queryString2(String s, int n) {
        Set<Integer> set = new HashSet<>();
        int len = s.length();
        char[] chars = s.toCharArray();
        for (int i = 0; i < len; i++) {
            int c = chars[i] - '0';
            if (c == 0) continue;
            for (int j = i + 1; c <= n; j++) {
                set.add(c);
                if (j == len) break;
                c = ((c << 1) | (chars[j] - '0'));
            }
        }
        return set.size() == n;
    }

    //1044. 最长重复子串 字符串哈希
    //https://leetcode.cn/problems/longest-duplicate-substring/solution/zui-chang-zhong-fu-zi-chuan-by-leetcode-0i9rd/
    //我们可以使用 Rabin-Karp 算法对固定长度的字符串进行编码。当两个字符串的编码相同时，则这两个字符串也相同。
    // 在 s 中n−L+1 个长度为 L 的子串中，有两个子串的编码相同时，则说明存在长度为 L 的重复子串。具体步骤如下：
    //首先，我们需要对 s 的每个字符进行编码，得到一个数组 arr。因为本题中 s 仅包含小写字母，
    // 我们可按照arr[i] = (int)s.charAt(i) - (int)‘a’，将所有字母编码为 0−25 之间的数字。
    // 比如字符串“abcde" 可以编码为数组 [0,1,2,3,4]。
    //我们将子串看成一个 26 进制的数，它对应的 10 进制数就是它的编码。假设此时我们需要求长度为 3 的子串的编码。
    // 那么第一个子串 “abc” 的编码就是:h0=0×26^2+1×26^1+2×26^0=28。
    // 更一般地，设ci为 s 的第 i 个字符编码后的数字，a(a≥26) 为编码的进制，那么有
    // h0=c0*a^L−1+c1*a^L−2+...+cL−1*a^1
    //上一步我们只求了第一个子串 “abc” 的编码。当我们要求第二个子串 “bcd” 的编码时，也可以按照上一步的方法求：
    // h1=1×26^2+2×26^1+3×26^0=731，但是这样时间复杂度是O(L)。我们可以在h0的基础上，更快地求出它的编码：
    //h1=(h0−0×26^2)×26+3×26^0=731。更一般的表达式是：h1=(h0×a−c0×a^L)+c(L+1)
    // 。这样，我们只需要在常数时间内就可以根据上一个子串的编码求出下一个子串的编码。我们用一个哈希表 \textit{seen}seen 来存储子串的编码。在求子串的编码时，如果某个子串的编码出现过，则表示存在长度为 LL 的重复子串，否则，我们将当前的编码放入 \textit{seen}seen 中。如果所有编码都不重复，则说明不存在长度为 LL 的重复子串。
    //还有一点需要考虑的是，本题中 a^L会非常大。一般的做法是需要对编码进行取模来防止溢出，模一般选取编码的信息量的平方的数量级。而取模则会带来哈希碰撞。本题中为了避免碰撞，我们使用双哈希，即用两套进制和模的组合，来对字符串进行编码。只有两种编码都相同时，我们才认为字符串相同。
    //本题要求返回最长重复子串而不是最长重复子串长度。因此，当存在长度为 LL 的子串时，我们的判断函数可以返回重复子串的起点。而当不存在时，可以返回 -1−1 用做区分。

    int a1, a2;
    int mod1, mod2;
    int n;

    private void init() {
        Random random = new Random();
        // 生成两个进制
        a1 = random.nextInt(75) + 26;
        a2 = random.nextInt(75) + 26;
        // 生成两个模
        mod1 = random.nextInt(Integer.MAX_VALUE - 1000000007 + 1) + 1000000007;
        mod2 = random.nextInt(Integer.MAX_VALUE - 1000000007 + 1) + 1000000007;
    }

    public String longestDupSubstring(String s) {
        this.n = s.length();
        init();
        // 先对所有字符进行编码
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = s.charAt(i) - 'a';
        }
        // 对字符串长度进行二分,重复子串最短长度1,最长长度n-1,但是为了避免n-1=l的情况("aa"),l初始值取0
        // 小于等于时的最大长度,大于时无重复子串=> 小于等于的最大值,右移l
        int l = 0, r = n - 1;
        int start = -1, len = 0;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            int idx = check(mid, arr);
            // 无重复子串,移动右边界
            if (idx == -1) {
                r = mid - 1;
            } else {
                // 有重复子串,收缩左边界
                l = mid;
                len = mid;
                start = idx;
            }
        }
        return start == -1 ? "" : s.substring(start, start + len);
    }

    private int check(int len, int[] arr) {
        long aL1 = pow(a1, len, mod1);
        long aL2 = pow(a2, len, mod2);
        long h1 = 0, h2 = 0;
        // ((arr[0])*a1+arr[1])*a1+arr[2] ...)*a1+arr[len-1] = arr[0]*a1^(len-1)+arr[1]*a1^(len-2)...+ arr[len-1]*a1^0
        for (int i = 0; i < len; i++) {
            h1 = (h1 * a1 % mod1 + arr[i]) % mod1;
            if (h1 < 0) h1 += mod1;
            h2 = (h2 * a2 % mod2 + arr[i]) % mod2;
            if (h2 < 0) h2 += mod2;
        }
        // 存储一个编码组合是否出现过
        Set<Long> seen = new HashSet<>();
        long hash0 = h1 * mod2 + h2;
        seen.add(hash0);
        // h1 h2 滚动数组
        for (int i = 1; i + len - 1 < n; i++) {
            h1 = (h1 * a1 % mod1 - arr[i - 1] * aL1 % mod1 + arr[i + len - 1]) % mod1;
            h2 = (h2 * a2 % mod2 - arr[i - 1] * aL2 % mod2 + arr[i + len - 1]) % mod2;
            if (h1 < 0) h1 += mod1;
            if (h2 < 0) h2 += mod2;
            long hash = h1 * mod2 + h2;
            // 如果重复，则返回重复串的起点
            if (!seen.add(hash)) return i;
        }
        return -1;
    }

    // 快速幂
    private long pow(int x, int n, int mod) {
        long ans = 1;
        long contribute = x;
        while (n > 0) {
            if ((n & 1) == 1) {
                ans = ans * contribute % mod;
                if (ans < 0) ans += mod;
            }
            contribute = contribute * contribute % mod;
            if (contribute < 0) contribute += mod;
            n /= 2;
        }
        return ans;
    }

    //372. 超级次方
    public int superPow(int a, int[] b) {
        int mod = 1337;
        int ans = 1;
        for(int e:b){
            ans = (int) ((long)pow(ans,10)*pow(a,e)%mod);
        }
        return ans;
    }

    public int pow(int x, int n) {
        int MOD = 1337;
        int res = 1;
        while (n != 0) {
            if (n % 2 != 0) {
                res = (int) ((long) res * x % MOD);
            }
            x = (int) ((long) x * x % MOD);
            n /= 2;
        }
        return res;
    }

    // 961 在长度2N的数组中找出重复N次的元素
    // n+1个数,x重复了n次 => 其余数字出现1次
    public int repeatedNTimes(int[] nums) {
        int[] cnt = new int[10010];
        for (int num : nums) {
            if (++cnt[num] > 1) return num;
        }
        return -1;
    }

    //169 多数元素
    // 找超过一半元素众数
    public int majorityElementSort(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }
//Boyer-Moore 算法 摩尔投票法
//    推论一： 若记 众数 的票数为 +1 ，非众数 的票数为 -1 ，则一定有所有数字的 票数和 > 0 。
    //  推论二： 若数组的前 a 个数字的 票数和 =0 ，则 数组剩余 (n-a) 个数字的 票数和一定仍 >0 ，即后 (n−a) 个数字的 众数仍为 x 。

    public int majorityElement(int[] nums) {
        Integer candidate = null;
        int count = 0;
        for (int n : nums) {
            if (count == 0) candidate = n;
            count += (n == candidate) ? 1 : -1;
        }
        return candidate;
    }

    // 题目不一定有解的情况 [1,2,3]
    public int majorityElement2(int[] nums) {
        Integer candidate = majorityElement(nums);
        int count = 0;
        for (int num : nums) {
            if (num == candidate) count++;
        }
        return count > nums.length / 2 ? candidate : -1;
    }

    //分治
    public int majorityElementDivideConquer(int[] nums) {
        return majorityElementRec(nums, 0, nums.length - 1);
    }

    private int countInRange(int[] nums, int num, int lo, int hi) {
        int count = 0;
        for (int i = lo; i <= hi; i++) {
            if (nums[i] == num) {
                count++;
            }
        }
        return count;
    }

    private int majorityElementRec(int[] nums, int lo, int hi) {
        // base case; the only element in an array of size 1 is the majority
        // element.
        if (lo == hi) {
            return nums[lo];
        }

        // recurse on left and right halves of this slice.
        int mid = (hi - lo) / 2 + lo;
        int left = majorityElementRec(nums, lo, mid);
        int right = majorityElementRec(nums, mid + 1, hi);

        // if the two halves agree on the majority element, return it.
        if (left == right) {
            return left;
        }

        // otherwise, count each element and return the "winner".
        int leftCount = countInRange(nums, left, lo, hi);
        int rightCount = countInRange(nums, right, lo, hi);

        return leftCount > rightCount ? left : right;
    }

    // 229 多数元素2
    // 求n/3众数
    public List<Integer> majorityElement229(int[] nums) {
        int element1 = 0;
        int element2 = 0;
        int vote1 = 0;
        int vote2 = 0;

        for (int num : nums) {
            if (vote1 > 0 && num == element1) { //如果该元素为第一个元素，则计数加1
                vote1++;
            } else if (vote2 > 0 && num == element2) { //如果该元素为第二个元素，则计数加1
                vote2++;
            } else if (vote1 == 0) { // 选择第一个元素
                element1 = num;
                vote1++;
            } else if (vote2 == 0) { // 选择第二个元素
                element2 = num;
                vote2++;
            } else { //如果三个元素均不相同，则相互抵消1次
                vote1--;
                vote2--;
            }
        }

        int cnt1 = 0;
        int cnt2 = 0;
        for (int num : nums) {
            if (vote1 > 0 && num == element1) {
                cnt1++;
            }
            if (vote2 > 0 && num == element2) {
                cnt2++;
            }
        }
        // 检测元素出现的次数是否满足要求
        List<Integer> ans = new ArrayList<>();
        if (vote1 > 0 && cnt1 > nums.length / 3) {
            ans.add(element1);
        }
        if (vote2 > 0 && cnt2 > nums.length / 3) {
            ans.add(element2);
        }

        return ans;
    }

    //400 https://leetcode-cn.com/problems/shu-zi-xu-lie-zhong-mou-yi-wei-de-shu-zi-lcof/solution/mian-shi-ti-44-shu-zi-xu-lie-zhong-mou-yi-wei-de-6/
    //第N位数字
    public int findNthDigit(int n) {
        int digit = 1;
        long start = 1, count = 9;
        while (n > count) {
            n -= count;
            digit += 1;
            start *= 10;
            //[10-99]*2 共180个数位 [100,999]
            count = 9 * digit * start;
        }
        long num = start + (n - 1) / digit;
        return Long.toString(num).charAt((n - 1) % digit) - '0';
    }

    // 440 字典序的第k小数字
    public int findKthNumber(int n, int k) {
        int curr = 1;
        k--;
        while (k > 0) {
            int steps = getSteps(curr, n);
            if (steps <= k) {
                k -= steps;
                curr++;
            } else {
                curr *= 10;
                k--;
            }
        }
        return curr;
    }

    private int getSteps(int curr, int n) {
        int steps = 0;
        long first = curr;
        long last = curr;
        while (first <= n) {
            steps += Math.min(last, n) - first + 1;
            first *= 10;
            last = last * 10 + 9;
        }
        return steps;
    }

    //offer 61  扑克牌顺子
//从若干副扑克牌中随机抽 5 张牌，判断是不是一个顺子，即这5张牌是不是连续的。2～10为数字本身，A为1，J为11，Q为12，K为13，而大、小王为 0 ，
//可以看成任意数字。A 不能视为 14。
//输入: [1,2,3,4,5]
//输出: True
    public boolean isStraight(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int n : nums) {
            if (n == 0) continue;
            if (set.contains(n)) return false;
            max = Math.max(n, max);
            min = Math.min(n, min);
            set.add(n);
        }
        return max - min < 5;
    }

    public boolean isStraight2(int[] nums) {
        Arrays.sort(nums);
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if (nums[i] == 0) count++;
            else if (nums[i] == nums[i + 1]) return false;
        }
        return nums[4] - nums[count] < 5;
    }

    //offer 62 约瑟夫环
    //0,1,···,n-1这n个数字排成一个圆圈，从数字0开始，每次从这个圆圈里删除第m个数字（删除后从下一个数字开始计数）。求出这个圆圈里剩下的最后一个数字。
// 例如，0、1、2、3、4这5个数字组成一个圆圈，从数字0开始每次删除第3个数字，则删除的前4个数字依次是2、0、4、1，因此最后剩下的数字是3。
    public int lastRemaining(int n, int m) {
        return f(n, m);
    }

    private int f(int n, int m) {
        if (n == 1) return 0;
        int x = f(n - 1, m);
        return (m + x) % n;
    }

    // 0 1 2 3 4
    // 0 1 3.4
    // 1.3 4
    // 1.3
    // 3
    public int lastRemaining2(int n, int m) {
        int ans = 0;
        for (int i = 2; i <= n; i++) {
            ans = (ans + m) % i;
        }
        return ans;
    }

    // 1823 找出游戏的获胜者
    public int findTheWinner(int n, int k) {
        int ans = 0;
        for (int i = 2; i <= n; i++) {
            ans = (ans + k) % i;
        }
        return ans + 1;
    }

    public int findTheWinnerQueue(int n, int k) {
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 1; i <= n; i++) {
            queue.offer(i);
        }
        while (queue.size() > 1) {
            for (int i = 1; i < k; i++) {
                queue.offer(queue.poll());
            }
            queue.poll();
        }
        return queue.peek();
    }

    //2549. 统计桌面上的不同数字
    public int distinctIntegers(int n) {
        return Math.max(1, n - 1);
    }

    //2718. 查询后矩阵的和
    //逆向思维 正难则反
    public long matrixSumQueries(int n, int[][] queries) {
        int m = queries.length;
        Set<Integer>[] vis = new Set[]{new HashSet<>(), new HashSet<>()};
        long ans = 0;
        for (int i = m - 1; i >= 0; i--) {
            if (queries[i][0] == 0) {
                if (vis[0].contains(queries[i][1])) continue;
                ans += (n - vis[1].size()) * queries[i][2];
                vis[0].add(queries[i][1]);
            } else {
                if (vis[1].contains(queries[i][1])) continue;
                ans += (n - vis[0].size()) * queries[i][2];
                vis[1].add(queries[i][1]);
            }
        }
        return ans;
    }

    //endregion -------------------------------------------------------------------end--------------------------------
}
