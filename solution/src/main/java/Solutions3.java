import java.util.*;

public class Solutions3 {

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
    // offer 15 二进制中1的个数
    public int hammingWeight(int n) {
        int cnt = 0;
        for (int i = 0; i < 32; i++) {
            if (((n >> i) & 1) == 1) cnt++;
        }
        return cnt;
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
    //n=1 0,1
    //n=2 0,1->翻转1,0 ->前面加1 11,10
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

    // 面试题08.03 魔术索引 跳跃查询
    public int findMagicIndex(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; ) {
            if (nums[i] == i) return i;
            i = Math.max(nums[i], i + 1);
        }
        return -1;
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

    // 求最小公倍数
    private int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    //欧几里得算法又称辗转相除法
    //求最大公约数
    private int gcd(int x, int y) {
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

    public void rotate3(int[] nums, int k) {
        k %= nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
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


    //endregion -------------------------------------------------------------------end--------------------------------
    //region----------------------------------------博弈论----------------------------------------------------
    // 292 nim游戏，后手只有在4的倍数时才赢
    public boolean canWinNim(int n) {
        return n % 4 != 0;
    }

    // 319 灯泡开关
    //初始时有 n 个灯泡处于关闭状态。第一轮，你将会打开所有灯泡。接下来的第二轮，你将会每两个灯泡关闭第二个。
//
// 第三轮，你每三个灯泡就切换第三个灯泡的开关（即，打开变关闭，关闭变打开）。第 i 轮，你每 i 个灯泡就切换第 i 个灯泡的开关。直到第 n 轮，你只需要
//切换最后一个灯泡的开关。找出并返回 n 轮后有多少个亮着的灯泡。
//输入：n = 3输出：1
//初始时, 灯泡状态 [关闭, 关闭, 关闭].
//第一轮后, 灯泡状态 [开启, 开启, 开启].
//第二轮后, 灯泡状态 [开启, 关闭, 开启].
//第三轮后, 灯泡状态 [开启, 关闭, 关闭].
//你应该返回 1，因为只有一个灯泡还亮着。
    public int bulbSwitch(int n) {
        return (int) Math.sqrt(n);
    }

    // 672 灯泡开关2
    public int flipLights(int n, int k) {
        if (k == 0) return 1;
        if (n == 1) return 2;
        else if (n == 2) return k == 1 ? 3 : 4;
        else return k == 1 ? 4 : k == 2 ? 7 : 8;
    }
    //endregion-----------------------------------------------------------------------------------------------

    //region-----------------------------------------模拟---------------------------------------------------
    public String toLowerCase(String str) {
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            int num = charArray[i];
            if (num >= 65 && num <= 90) {
                num += 32;
            }
            charArray[i] = (char) num;
        }

        return String.valueOf(charArray);

    }

    // 6 Z字形变换
    public String convert(String s, int numRows) {
        if (numRows == 1) return s;

        List<StringBuilder> rows = new ArrayList<>();
        for (int i = 0; i < Math.min(numRows, s.length()); i++)
            rows.add(new StringBuilder());

        int curRow = 0;
        boolean goingDown = false;

        for (char c : s.toCharArray()) {
            rows.get(curRow).append(c);
            if (curRow == 0 || curRow == numRows - 1) goingDown = !goingDown;
            curRow += goingDown ? 1 : -1;
        }

        StringBuilder ret = new StringBuilder();
        for (StringBuilder row : rows) ret.append(row);
        return ret.toString();
    }

    // 7 整数翻转
    public int reverse(int x) {
        int rev = 0;
        while (x != 0) {
            if (rev < Integer.MIN_VALUE / 10 || rev > Integer.MAX_VALUE / 10) {
                return 0;
            }
            int digit = x % 10;
            x /= 10;
            rev = rev * 10 + digit;
        }
        return rev;
    }

    // 两个句子里不同的词
    public String[] uncommonFromSentences(String A, String B) {
        HashMap<String, Integer> map = new HashMap<>();
        for (String word : A.split(" ")) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }
        for (String word : B.split(" ")) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }
        List<String> list = new ArrayList<>();
        for (String word : map.keySet()) {
            if (map.get(word) == 1)
                list.add(word);
        }
        return list.toArray(new String[0]);
    }

    // 434 字符串中的单词数
    public int countSegments(String s) {
        int n = s.length();
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) != ' ' && (i == 0 || s.charAt(i - 1) == ' ')) {
                cnt++;
            }
        }
        return cnt;
    }

    // 389 找不同
    public char findTheDifference(String s, String t) {
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            cnt[c - 'a']++;
        }
        for (char c : t.toCharArray()) {
            cnt[c - 'a']--;
        }
        for (int i = 0; i < 26; i++) {
            if (cnt[i] != 0) return (char) ('a' + i);
        }
        return ' ';
    }

    // 806 写字符串需要的行数
    public int[] numberOfLines(int[] widths, String s) {
        int[] result = new int[]{1, 0};
        for (char c : s.toCharArray()) {
            if (100 - result[1] >= widths[c - 'a']) {
                result[1] += widths[c - 'a'];
            } else {
                result[0]++;
                result[1] = widths[c - 'a'];
            }
        }
        return result;
    }

    // offer 58 左旋转字符串
    public String reverseLeftWords(String s, int n) {
        StringBuilder res = new StringBuilder();
        for (int i = n; i < n + s.length(); i++)
            res.append(s.charAt(i % s.length()));
        return res.toString();
    }

    // 面试01.09 字符串轮转
    //字符串轮转。给定两个字符串s1和s2，请编写代码检查s2是否为s1旋转而成（比如，waterbottle是erbottlewat旋转后的字符串）。
    public boolean isFlipedString(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        int n = s1.length();
        if (n == 0) return true;
        for (int i = 0; i < n; i++) {
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (s1.charAt((i + j) % n) != s2.charAt(j)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return true;
            }
        }
        return false;
    }

    // 8 字符串转换整数
    public int myAtoi(String s) {
        int index = 0;
        while (index < s.length() && s.charAt(index) == ' ') {
            index++;
        }
        if (index == s.length()) {
            return 0;
        }

        int sign = 1;
        if (s.charAt(index) == '+') {
            index++;
        } else if (s.charAt(index) == '-') {
            sign = -1;
            index++;
        }
        int res = 0;
        while (index < s.length()) {
            if (s.charAt(index) > '9' || s.charAt(index) < '0') {
                break;
            }
            if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && (s.charAt(index) - '0') > Integer.MAX_VALUE % 10)) {
                return Integer.MAX_VALUE;
            }

            if (res < Integer.MIN_VALUE / 10 || (res == Integer.MIN_VALUE / 10 && (s.charAt(index) - '0') > -(Integer.MIN_VALUE % 10))) {
                return Integer.MIN_VALUE;
            }
            res = res * 10 + sign * (s.charAt(index) - '0');
            index++;
        }
        return res;
    }

    // offer 67 把字符串转换成整数
    public int strToInt(String str) {
        int len = str.length();
        // str.charAt(i) 方法回去检查下标的合法性，一般先转换成字符数组
        char[] charArray = str.toCharArray();

        // 1、去除前导空格
        int index = 0;
        while (index < len && charArray[index] == ' ') {
            index++;
        }

        // 2、如果已经遍历完成（针对极端用例 "      "）
        if (index == len) {
            return 0;
        }

        // 3、如果出现符号字符，仅第 1 个有效，并记录正负
        int sign = 1;
        char firstChar = charArray[index];
        if (firstChar == '+') {
            index++;
        } else if (firstChar == '-') {
            index++;
            sign = -1;
        }

        // 4、将后续出现的数字字符进行转换
        // 不能使用 long 类型，这是题目说的
        int res = 0;
        while (index < len) {
            char currChar = charArray[index];
            // 4.1 先判断不合法的情况
            if (currChar > '9' || currChar < '0') {
                break;
            }

            // 题目中说：环境只能存储 32 位大小的有符号整数，因此，需要提前判：断乘以 10 以后是否越界
            if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && (currChar - '0') > Integer.MAX_VALUE % 10)) {
                return Integer.MAX_VALUE;
            }
            if (res < Integer.MIN_VALUE / 10 || (res == Integer.MIN_VALUE / 10 && (currChar - '0') > -(Integer.MIN_VALUE % 10))) {
                return Integer.MIN_VALUE;
            }

            // 4.2 合法的情况下，才考虑转换，每一步都把符号位乘进去
            res = res * 10 + sign * (currChar - '0');
            index++;
        }
        return res;
    }

    //12整数转罗马数字
    public String intToRoman(int num) {
        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            int count = num / entry.getKey();
            for (int i = 0; i < count; i++) {
                sb.append(entry.getValue());
            }
            num = num % entry.getKey();
        }
        return sb.toString();
    }

    //13 罗马数字转整数
    public int romanToInt(String s) {
        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            int cur = map.get(s.charAt(i));
            if (i < s.length() - 1 && cur < map.get(s.charAt(i + 1))) {
                ans -= cur;
            } else {
                ans += cur;
            }
        }
        return ans;
    }

    // 面试05.02 二进制小数
    public String printBin(double num) {
        StringBuilder ans = new StringBuilder("0.");
        while (ans.length() < 32 && num != 0) {
            num *= 2;
            if (num >= 1) {
                ans.append("1");
                num -= 1;
            } else {
                ans.append("0");
            }
        }
        if (ans.length() >= 32 && num != 0) return "ERROR";
        return ans.toString();
    }

    // 36 有效的数独
    public boolean isValidSudoku(char[][] board) {
        int[][] rows = new int[9][10];
        int[][] cols = new int[9][10];
        int[][] box = new int[9][10];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') continue;
                int num = board[i][j] - '0';
                if (rows[i][num] == 1) return false;
                if (cols[j][num] == 1) return false;
                if (box[(i / 3) * 3 + j / 3][num] == 1) return false;
                rows[i][num] = 1;
                cols[j][num] = 1;
                box[(i / 3) * 3 + j / 3][num] = 1;
            }
        }
        return true;
    }

    // 38 外观数列
    public String countAndSay(int n) {
        String ans = "1";
        for (int i = 2; i <= n; i++) {
            StringBuilder cur = new StringBuilder();
            int len = ans.length();
            for (int j = 0; j < len; ) {
                int k = j + 1;
                while (k < len && ans.charAt(k) == ans.charAt(j)) {
                    k++;
                }
                int cnt = k - j;
                cur.append(cnt).append(ans.charAt(j));
                j = k;
            }
            ans = cur.toString();
        }
        return ans;
    }

    // 401 二进制手表
    public List<String> readBinaryWatch(int turnedOn) {
        List<String> ans = new ArrayList<>();
        for (int h = 0; h < 12; ++h) {
            for (int m = 0; m < 60; ++m) {
                if (Integer.bitCount(h) + Integer.bitCount(m) == turnedOn) {
                    ans.add(h + ":" + (m < 10 ? "0" : "") + m);
                }
            }
        }
        return ans;
    }

    // 1331 数组序号转换
    public int[] arrayRankTransform(int[] arr) {
        Map<Integer, Integer> map = new HashMap<>();
        int[] c = arr.clone();
        Arrays.sort(c);
        int idx = 1;
        for (int v : c) {
            if (!map.containsKey(v)) {
                map.put(v, idx++);
            }
        }
        int[] ans = new int[arr.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = map.get(arr[i]);
        }
        return ans;
    }

    //946 offer31栈的压入弹出序列
    public boolean validateStackSequences(int[] pushed, int[] popped) {
        Stack<Integer> stack = new Stack<>();
        int i = 0;
        for (int n : pushed) {
            stack.push(n);
            while (!stack.isEmpty() && stack.peek() == popped[i]) {
                stack.pop();
                i++;
            }
        }
        return stack.isEmpty();
    }

    // 1598 文件夹操作日志搜集器
    public int minOperations(String[] logs) {
        Stack<String> stack = new Stack<>();
        for (String log : logs) {
            if ("../".equals(log)) {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else if ("./".equals(log)) {

            } else {
                stack.push(log);
            }
        }
        return stack.size();
    }

    // 396 旋转数组
    //给定一个长度为 n 的整数数组 nums 。
// 假设 arrk 是数组 nums 顺时针旋转 k 个位置后的数组，我们定义 nums 的 旋转函数 F 为：
// F(k) = 0 * arrk[0] + 1 * arrk[1] + ... + (n - 1) * arrk[n - 1]
// 返回 F(0), F(1), ..., F(n-1)中的最大值 。
//F(0) = (0 * 4) + (1 * 3) + (2 * 2) + (3 * 6) = 0 + 3 + 4 + 18 = 25
//F(1) = (0 * 6) + (1 * 4) + (2 * 3) + (3 * 2) = 0 + 4 + 6 + 6 = 16
//F(2) = (0 * 2) + (1 * 6) + (2 * 4) + (3 * 3) = 0 + 6 + 8 + 9 = 23
//F(3) = (0 * 3) + (1 * 2) + (2 * 6) + (3 * 4) = 0 + 2 + 12 + 12 = 26
//所以 F(0), F(1), F(2), F(3) 中的最大值是 F(3) = 26 。
    public int maxRotateFunction(int[] nums) {
        int sum = 0, f = 0, n = nums.length;
        for (int i = 0; i < n; i++) {
            sum += nums[i];
            f += i * nums[i];
        }
        int max = f;
        for (int i = n - 1; i > 0; i--) {
            f = f + sum - n * nums[i];
            max = Math.max(f, max);
        }
        return max;
    }

    // 48 旋转图像
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        // n 为偶数时，n/2 n为奇数时 列多取一列
        // 若全部<n 对于一个数 会旋转4次回到原点
        for (int i = 0; i < n / 2; i++) {
            for (int j = 0; j < (n + 1) / 2; j++) {
                int tmp = matrix[i][j];
                // 行变列，列变n-1-col
                matrix[i][j] = matrix[n - 1 - j][i];
                matrix[n - 1 - j][i] = matrix[n - 1 - i][n - 1 - j];
                matrix[n - 1 - i][n - 1 - j] = matrix[j][n - 1 - i];
                matrix[j][n - 1 - i] = tmp;
            }
        }
    }

    // 54 螺旋矩阵
    //1 模拟路径
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> order = new ArrayList<Integer>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return order;
        }
        int rows = matrix.length, columns = matrix[0].length;
        boolean[][] visited = new boolean[rows][columns];
        int total = rows * columns;
        int row = 0, column = 0;
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int directionIndex = 0;
        for (int i = 0; i < total; i++) {
            order.add(matrix[row][column]);
            visited[row][column] = true;
            int nextRow = row + directions[directionIndex][0], nextColumn = column + directions[directionIndex][1];
            if (nextRow < 0 || nextRow >= rows || nextColumn < 0 || nextColumn >= columns || visited[nextRow][nextColumn]) {
                directionIndex = (directionIndex + 1) % 4;
            }
            row += directions[directionIndex][0];
            column += directions[directionIndex][1];
        }
        return order;
    }

    //2 分层模拟
    public List<Integer> spiralOrder1(int[][] matrix) {
        List<Integer> order = new ArrayList<>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return order;
        }
        int rows = matrix.length, columns = matrix[0].length;
        int top = 0, left = 0, right = columns - 1, bottom = rows - 1;
        while (top <= bottom && left <= right) {
            for (int column = left; column <= right; column++) {
                order.add(matrix[top][column]);
            }
            for (int row = top + 1; row <= bottom; row++) {
                order.add(matrix[row][right]);
            }
            if (right > left && bottom > top) {
                for (int column = right - 1; column > left; column--) {
                    order.add(matrix[bottom][column]);
                }
                for (int row = bottom; row > top; row--) {
                    order.add(matrix[row][left]);
                }
            }

            top++;
            bottom--;
            left++;
            right--;
        }
        return order;
    }

    // 59 螺旋矩阵2
    public int[][] generateMatrix(int n) {
        int total = n * n;
        int[][] matrix = new int[n][n];
        int[][] directions = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int directionIdx = 0;
        int x = 0, y = 0;
        boolean[][] visited = new boolean[n][n];
        for (int i = 1; i <= total; i++) {
            matrix[x][y] = i;
            visited[x][y] = true;
            int newX = x + directions[directionIdx][0], newY = y + directions[directionIdx][1];
            if (newX < 0 || newX >= n || newY < 0 || newY >= n || visited[newX][newY]) {
                directionIdx = (directionIdx + 1) % 4;
            }
            x = x + directions[directionIdx][0];
            y = y + directions[directionIdx][1];
        }
        return matrix;
    }

    public int[][] spiralMatrixIII(int R, int C, int r0, int c0) {
        int[][] dirt = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // east, south, west, north
        List<int[]> res = new ArrayList<>();
        int len = 0, d = 0; // move <len> steps in the <d> direction
        res.add(new int[]{r0, c0});
        while (res.size() < R * C) {
            if (d == 0 || d == 2) len++; // when move east or west, the length of path need plus 1
            for (int i = 0; i < len; i++) {
                r0 += dirt[d][0];
                c0 += dirt[d][1];
                if (r0 >= 0 && r0 < R && c0 >= 0 && c0 < C) // check valid
                    res.add(new int[]{r0, c0});
            }
            d = (d + 1) % 4; // turn to next direction
        }
        return res.toArray(new int[R * C][2]);
    }

    // 73 矩阵置0
    public void setZeroes(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        // 1. 扫描「首行」和「首列」记录「首行」和「首列」是否该被置零
        boolean r0 = false, c0 = false;
        for (int i = 0; i < m; i++) {
            if (mat[i][0] == 0) {
                r0 = true;
                break;
            }
        }
        for (int j = 0; j < n; j++) {
            if (mat[0][j] == 0) {
                c0 = true;
                break;
            }
        }
        // 2.1 扫描「非首行首列」的位置，如果发现零，将需要置零的信息存储到该行的「最左方」和「最上方」的格子内
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (mat[i][j] == 0) mat[i][0] = mat[0][j] = 0;
            }
        }
        // 2.2 根据刚刚记录在「最左方」和「最上方」格子内的置零信息，进行「非首行首列」置零
        for (int j = 1; j < n; j++) {
            if (mat[0][j] == 0) {
                for (int i = 1; i < m; i++) mat[i][j] = 0;
            }
        }
        for (int i = 1; i < m; i++) {
            if (mat[i][0] == 0) Arrays.fill(mat[i], 0);
        }
        // 3. 根据最开始记录的「首行」和「首列」信息，进行「首行首列」置零
        if (r0) for (int i = 0; i < m; i++) mat[i][0] = 0;
        if (c0) Arrays.fill(mat[0], 0);
    }

    // 468 验证ip地址
    public String validIPAddress(String queryIP) {
        if (queryIP.contains(".") && validIPv4(queryIP)) return "IPv4";
        if (queryIP.contains(":") && validIPv6(queryIP)) return "IPv6";
        return "Neither";
    }

    private boolean validIPv4(String queryIP) {
        String[] tmp = ("-1." + queryIP + ".-1").split("\\.");
        if (tmp.length != 6) return false;
        String[] ss = new String[4];
        System.arraycopy(tmp, 1, ss, 0, 4);
        for (String s : ss) {
            if (s.length() < 1) return false;
            if (s.length() > 1 && s.charAt(0) == '0') return false;
            int num = 0;
            for (char c : s.toCharArray()) {
                if (c < '0' || c > '9') return false;
                num = num * 10 + c - '0';
            }
            if (num < 0 || num > 255) return false;
        }
        return true;
    }

    private boolean validIPv6(String queryIP) {
        String[] tmp = ("-1:" + queryIP + ":-1").split(":");
        if (tmp.length != 10) return false;
        String[] ss = new String[8];
        System.arraycopy(tmp, 1, ss, 0, 8);
        for (String s : ss) {
            if (s.length() > 4 || s.length() < 1) return false;
            for (char c : s.toCharArray()) {
                if (Character.isLetter(c)) {
                    if (!((c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))) return false;
                }
            }
        }
        return true;
    }

    //1945. 字符串转化后的各位数字之和
    public int getLucky(String s, int k) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(c - 'a' + 1);
        }
        String digits = sb.toString();
        int ans = 0;
        while (k-- > 0) {
            ans = 0;
            for (char c : digits.toCharArray()) {
                ans += c - '0';
            }
            digits = String.valueOf(ans);

        }
        return ans;
    }

    //1781. 所有子字符串美丽值之和
    public int beautySum(String s) {
        int n = s.length();
        Map<Character, Integer> map = new HashMap<>();
        char[] chars = s.toCharArray();
        int max;
        final int[] min = new int[1];
        int ans = 0;
        for (int i = 0; i < n; i++) {
            map.clear();
            max = Integer.MIN_VALUE;
            for (int j = i; j < n; j++) {
                map.put(chars[j], map.getOrDefault(chars[j], 0) + 1);
                max = Math.max(map.get(chars[j]), max);
                min[0] = Integer.MAX_VALUE;
                map.forEach((k,v)-> min[0] = Math.min(min[0],v));
                ans += max - min[0];
            }
        }
        return ans;
    }

    //1832. 判断句子是否为全字母句
    public boolean checkIfPangram(String sentence) {
        int[] cnt = new int[26];
        int n = sentence.length();
        if (n < 26) return false;
        int count = 0;
        for (char c : sentence.toCharArray()) {
            if (cnt[c - 'a'] == 0) {
                cnt[c - 'a']++;
                count++;
            }
            if (count == 26) return true;
        }
        return false;
    }

    // 1742 盒子中小球的最大数量
    public int countBalls(int lowLimit, int highLimit) {
        Map<Integer, Integer> map = new HashMap<>();
        int max = 0;
        for (int i = lowLimit; i <= highLimit; i++) {
            int digitSum = getDigitSum(i);
            int cnt = map.getOrDefault(digitSum, 0);
            cnt++;
            max = Math.max(cnt, max);
            map.put(digitSum, cnt);
        }
        return max;
    }

    private int getDigitSum(int num) {
        int sum = 0;
        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }

    // 1260 二维网格迁移
    public List<List<Integer>> shiftGrid(int[][] grid, int k) {
        int m = grid.length, n = grid[0].length;
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            List<Integer> rows = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                rows.add(0);
            }
            result.add(rows);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int index = (i * n + j + k) % (m * n);
                result.get(index / n).set(index % n, grid[i][j]);
            }
        }
        return result;
    }

    // 2028. 找出缺失的观测数据
    public int[] missingRolls(int[] rolls, int mean, int n) {
        int m = rolls.length;
        int total = mean * (m + n);
        int sum = 0;
        for (int num : rolls) {
            sum += num;
        }
        int diff = total - sum;
        if (diff < n || diff > n * 6) return new int[0];
        int avg = diff / n, r = diff % n;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = avg + (i < r ? 1 : 0);
        }
        return ans;
    }

    //1805. 字符串中不同整数的数目
    public int numDifferentIntegers(String word) {
        Set<String> set = new HashSet<>();
        int idx = 0, n = word.length();
        char[] chars = word.toCharArray();
        while (idx < n) {
            if (Character.isLetter(chars[idx])) {
                idx++;
                continue;
            }
            StringBuilder sb = new StringBuilder();
            while (idx < n && Character.isDigit(chars[idx])) {
                sb.append(chars[idx++]);
            }
            String s = sb.toString();
            int i = 0;
            while (i < s.length() && s.charAt(i) == '0') {
                i++;
            }
            set.add(s.substring(i));
        }
        return set.size();
    }

    //1582. 二进制矩阵中的特殊位置
    public int numSpecial(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[] rows = new int[m];
        int[] cols = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                rows[i] += mat[i][j];
                cols[j] += mat[i][j];
            }
        }
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 1 && rows[i] == 1 && cols[j] == 1) {
                    ans++;
                }
            }
        }
        return ans;
    }

    //1592. 重新排列单词间的空格
    public String reorderSpaces(String text) {
        int n = text.length();
        int l = -1, r = 0;
        List<String> list = new ArrayList<>();
        int cnt = 0, wordSize = 0;
        while (r < n) {
            while (r < n && text.charAt(r) == ' ') {
                r++;
            }
            l = r;
            while (r < n && text.charAt(r) != ' ') {
                r++;
            }
            if (r != l) {
                cnt++;
                wordSize += r - l;
                list.add(text.substring(l, r));
            }
        }
        int num = cnt > 1 ? ((n - wordSize) / (cnt - 1)) : 0;
        int suffix = cnt > 1 ? ((n - wordSize) % (cnt - 1)) : n - wordSize;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            sb.append(list.get(i));
            if (i != cnt - 1) {
                for (int j = 0; j < num; j++) {
                    sb.append(" ");
                }
            }

        }
        for (int i = 0; i < suffix; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    // 1620 网络信号最好的坐标 枚举
    public int[] bestCoordinate(int[][] towers, int radius) {
        int xMax = Integer.MIN_VALUE, yMax = Integer.MIN_VALUE;
        for (int[] tower : towers) {
            int x = tower[0], y = tower[1];
            xMax = Math.max(xMax, x);
            yMax = Math.max(yMax, y);
        }
        int cx = 0, cy = 0;
        int maxQuality = 0;
        for (int x = 0; x <= xMax; x++) {
            for (int y = 0; y <= yMax; y++) {
                int[] coordinate = {x, y};
                int quality = 0;
                for (int[] tower : towers) {
                    int squaredDistance = getSquaredDistance(coordinate, tower);
                    if (squaredDistance <= radius * radius) {
                        double distance = Math.sqrt(squaredDistance);
                        quality += (int) Math.floor(tower[2] / (1 + distance));
                    }
                }
                if (quality > maxQuality) {
                    cx = x;
                    cy = y;
                    maxQuality = quality;
                }
            }
        }
        return new int[]{cx, cy};
    }

    public int getSquaredDistance(int[] coordinate, int[] tower) {
        return (tower[0] - coordinate[0]) * (tower[0] - coordinate[0]) + (tower[1] - coordinate[1]) * (tower[1] - coordinate[1]);
    }

    // 1640 能否连接形成数组
    public boolean canFormArray(int[] arr, int[][] pieces) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < pieces.length; i++) {
            map.put(pieces[i][0], i);
        }
        for (int i = 0; i < arr.length; i++) {
            if (!map.containsKey(arr[i])) {
                return false;
            }
            int[] piece = pieces[map.get(arr[i])];
            int j = i + 1, k = 1;
            while (j < arr.length && k < piece.length) {
                if (arr[j] == piece[k]) {
                    j++;
                    k++;
                } else {
                    return false;
                }
            }
            i = j - 1;
        }
        return true;
    }

    //1694. 重新格式化电话号码
    public String reformatNumber(String number) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (char c : number.toCharArray()) {
            if (c == ' ' || c == '-') continue;

            sb.append(c);

            if (++count % 3 == 0) {
                sb.append('-');
            }
        }

        if (count % 3 == 1) {
            sb.deleteCharAt(sb.length() - 2);
            sb.insert(sb.length() - 2, '-');
        }

        if (sb.charAt(sb.length() - 1) == '-') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    // 1700 无法吃午餐的学生数量
    public int countStudents(int[] students, int[] sandwiches) {
        int n = students.length;
        int idx1 = 0, idx2 = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        while (idx1 < n) {
            if (students[idx1] == sandwiches[idx2]) {
                idx1++;
                idx2++;
            } else {
                queue.offer(students[idx1++]);
            }
        }
        int cnt = queue.size();
        while (idx2 < n && queue.size() > 0 && cnt > 0) {
            if (queue.peek() == sandwiches[idx2]) {
                idx2++;
                queue.poll();
                cnt = queue.size();
            } else {
                cnt--;
                queue.offer(queue.poll());
            }
        }
        return queue.size();
    }

    // 学生顺序无关
    public int countStudents2(int[] students, int[] sandwiches) {
        int s1 = Arrays.stream(students).sum();
        int s0 = students.length - s1;
        for (int sandwich : sandwiches) {
            if (sandwich == 0 && s0 > 0) {
                s0--;
            } else if (sandwich == 1 && s1 > 0) {
                s1--;
            } else {
                break;
            }
        }
        return s0 + s1;
    }

    //1812. 判断国际象棋棋盘中一个格子的颜色
    public boolean squareIsWhite(String coordinates) {
        int col = coordinates.charAt(0) - 'a';
        int row = coordinates.charAt(1) - '1';
        return ((col & 1) == 1 && (row & 1) == 0) || ((col & 1) == 0 && (row & 1) == 1);
    }


    // 168 excel表列名称 XX进制模拟
    public String convertToTitle(int columnNumber) {
        StringBuilder sb = new StringBuilder();
        while (columnNumber != 0) {
            columnNumber--;
            sb.append((char) (columnNumber % 26 + 'A'));
            columnNumber /= 26;
        }
        return sb.reverse().toString();
    }

    // 58最后一个单词长度
    public int lengthOfLastWord(String s) {
        int index = s.length() - 1;
        while (index >= 0 && s.charAt(index) == ' ') {
            index--;
        }
        int index2 = index;
        while (index2 >= 0 && s.charAt(index2) != ' ') {
            index2--;
        }
        return index - index2;
    }

    // 43 字符串相乘
    public String multiply(String num1, String num2) {
        if ("0".equals(num1) || "0".equals(num2)) return "0";
        String ans = "0";
        int n = num1.length(), m = num2.length();
        for (int i = n - 1; i >= 0; i--) {
            StringBuilder cur = new StringBuilder();
            int add = 0;
            for (int j = n - 1; j > i; j--) {
                cur.append("0");
            }
            int y = num1.charAt(i) - '0';
            for (int j = m - 1; j >= 0; j--) {
                int x = num2.charAt(j) - '0';
                int product = x * y + add;
                cur.append(product % 10);
                add = product / 10;
            }
            if (add != 0) {
                cur.append(add % 10);
            }
            ans = addStrings(ans, cur.reverse().toString());
        }
        return ans;
    }

    public String multiply2(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        int[] res = new int[num1.length() + num2.length()];
        for (int i = num1.length() - 1; i >= 0; i--) {
            int n1 = num1.charAt(i) - '0';
            for (int j = num2.length() - 1; j >= 0; j--) {
                int n2 = num2.charAt(j) - '0';
                int sum = (res[i + j + 1] + n1 * n2);
                res[i + j + 1] = sum % 10;
                res[i + j] += sum / 10;
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < res.length; i++) {
            if (i == 0 && res[i] == 0) continue;
            result.append(res[i]);
        }
        return result.toString();
    }


    // 415 字符串相加
    public String addStrings(String num1, String num2) {
        int i = num1.length() - 1, j = num2.length() - 1;
        int add = 0;
        StringBuilder sb = new StringBuilder();
        while (i >= 0 || j >= 0 || add != 0) {
            int x = i >= 0 ? num1.charAt(i) - '0' : 0;
            int y = j >= 0 ? num2.charAt(j) - '0' : 0;
            int sum = x + y + add;
            sb.append(sum % 10);
            add = sum / 10;
            i--;
            j--;
        }
        sb.reverse();
        return sb.toString();
    }

    // 371 两整数之和 不用+ -符号
    public int getSum(int a, int b) {
        int ans = 0, r = 0;
        for (int i = 0; i < 32; i++) {
            int x = (a >> i) & 1, y = (b >> i) & 1;
            if (x == 1 && y == 1) {
                ans |= (r << i);
                r = 1;
            } else if (x == 1 || y == 1) {
                ans |= ((1 ^ r) << i);
            } else {
                ans |= (r << i);
                r = 0;
            }
        }
        return ans;
    }

    // 163 缺失的区间
    public List<String> findMissingRanges(int[] nums, int lower, int upper) {
        int n = nums.length;
        List<String> result = new ArrayList<>();
        if (n == 0) {
            result.add(helper(lower - 1, upper + 1));
            return result;
        }
        if (lower < nums[0]) result.add(helper(lower - 1, nums[0]));
        for (int i = 0; i < n - 1; i++) {
            if (nums[i] + 1 != nums[i + 1]) {
                result.add(helper(nums[i], nums[i + 1]));
            }
        }
        if (upper > nums[n - 1]) result.add(helper(nums[n - 1], upper + 1));
        return result;
    }

    private String helper(int left, int right) {
        StringBuilder sb = new StringBuilder();
        if (right - left == 2) sb.append(left + 1);
        else sb.append(left + 1).append("->").append(right - 1);
        return sb.toString();
    }

    // 69 x的平方根
    // 除法可以转换成乘法的二分
    public int mySqrt(int x) {
        if (x <= 1) return x;
        int l = 1, r = x / 2;
        while (l < r) {
            int mid = (l + r + 1) >> 1;
            if ((long) mid * mid > x) {
                r = mid - 1;
            } else {
                l = mid;
            }
        }
        return l;
    }

    // 29 两数相除
    public int divide(int dividend, int divisor) {
        long x = dividend, y = divisor;
        boolean sign = true;
        if (x < 0) {
            sign = !sign;
            x = -x;
        }
        if (y < 0) {
            sign = !sign;
            y = -y;
        }
        long l = 0, r = x;
        while (l < r) {
            long mid = (l + r + 1) >> 1;
            if (mul(mid, y) <= x) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        long ans = sign ? l : -l;
        if (ans > Integer.MAX_VALUE || ans < Integer.MIN_VALUE) return Integer.MAX_VALUE;
        return (int) ans;
    }

    // a b 快速相乘
    private long mul(long a, long b) {
        long ans = 0;
        while (b > 0) {
            if ((b & 1) == 1) {
                ans += a;
            }
            b >>= 1;
            a += a;
        }
        return ans;
    }

    // 快速幂
    private double quickMul2(double x, long n) {
        double ans = 1.0;
        while (n > 0) {
            if ((n & 1) == 1) {
                ans *= x;
            }
            n >>= 1;
            x *= x;
        }
        return ans;
    }

    // 50 pow
    public double myPowDFS(double x, int n) {
        if (n == 0) return 1;
        if (n == 1) return x;
        if (n == -1) return 1 / x;
        double half = myPowDFS(x, n / 2);
        double mod = myPowDFS(x, n % 2);
        return half * half * mod;
    }

    public double myPow(double x, int n) {
        return (long) n >= 0 ? quickMul(x, (long) n) : 1.0 / quickMul(x, -(long) n);
    }

    // 次方快速相乘
    private double quickMul(double x, long N) {
        if (N == 0) {
            return 1.0;
        }
        double y = quickMul(x, N / 2);
        return N % 2 == 0 ? y * y : y * y * x;
    }


    //offer 64  不使用if for while 求累加
    public int sumNums(int n) {
        boolean flag = n > 0 && (n += sumNums(n - 1)) > 0;
        return n;
    }

    // offer 65 不用加减乘除做加法
    public int add(int a, int b) {
        if (b == 0) return a;
        return add(a ^ b, (a & b) << 1);
    }

    // 166 分数到小数
    //输入：numerator = 1, denominator = 2
//输出："0.5"
//输入：numerator = 2, denominator = 1
//输出："2"
//输入：numerator = 4, denominator = 333
//输出："0.(012)"
    public String fractionToDecimal(int numerator, int denominator) {
        long a = numerator, b = denominator;
        if (a % b == 0) return String.valueOf(a / b);
        StringBuilder sb = new StringBuilder();
        if (a * b < 0) sb.append("-");
        a = Math.abs(a);
        b = Math.abs(b);
        sb.append(a / b).append(".");
        a %= b;
        Map<Long, Integer> map = new HashMap<>();
        while (a != 0) {
            map.put(a, sb.length());
            a *= 10;
            sb.append(a / b);
            a %= b;
            if (map.containsKey(a)) {
                int pos = map.get(a);
                return String.format("%s(%s)", sb.substring(0, pos), sb.substring(pos));
            }
        }
        return sb.toString();
    }

    // 592 分数加减运算
    //输入: expression = "-1/2+1/2"
//输出: "0/1"
    public String fractionAddition(String expression) {
        long x = 0, y = 1; // 分子，分母
        int index = 0, n = expression.length();
        while (index < n) {
            // 读取分子
            long x1 = 0, sign = 1;
            if (expression.charAt(index) == '-' || expression.charAt(index) == '+') {
                sign = expression.charAt(index) == '-' ? -1 : 1;
                index++;
            }
            while (index < n && Character.isDigit(expression.charAt(index))) {
                x1 = x1 * 10 + expression.charAt(index) - '0';
                index++;
            }
            x1 = sign * x1;
            index++;

            // 读取分母
            long y1 = 0;
            while (index < n && Character.isDigit(expression.charAt(index))) {
                y1 = y1 * 10 + expression.charAt(index) - '0';
                index++;
            }

            x = x * y1 + x1 * y;
            y *= y1;
        }
        if (x == 0) {
            return "0/1";
        }
        long g = gcd(Math.abs(x), y); // 获取最大公约数
        return Long.toString(x / g) + "/" + Long.toString(y / g);
    }

    public String fractionAddition2(String s) {
        int n = s.length();
        char[] cs = s.toCharArray();
        String ans = "";
        for (int i = 0; i < n; ) {
            int j = i + 1;
            while (j < n && cs[j] != '+' && cs[j] != '-') j++;
            String num = s.substring(i, j);
            if (cs[i] != '+' && cs[i] != '-') num = "+" + num;
            if (!ans.equals("")) ans = calc(num, ans);
            else ans = num;
            i = j;
        }
        return ans.charAt(0) == '+' ? ans.substring(1) : ans;
    }

    private String calc(String a, String b) {
        boolean fa = a.charAt(0) == '+', fb = b.charAt(0) == '+';
        if (!fa && fb) return calc(b, a);
        long[] p = parse(a), q = parse(b);
        long p1 = p[0] * q[1], q1 = q[0] * p[1];
        if (fa && fb) {
            long r1 = p1 + q1, r2 = p[1] * q[1], c = gcd(r1, r2);
            return "+" + (r1 / c) + "/" + (r2 / c);
        } else if (!fa && !fb) {
            long r1 = p1 + q1, r2 = p[1] * q[1], c = gcd(r1, r2);
            return "-" + (r1 / c) + "/" + (r2 / c);
        } else {
            long r1 = p1 - q1, r2 = p[1] * q[1], c = gcd(Math.abs(r1), r2);
            String ans = (r1 / c) + "/" + (r2 / c);
            if (p1 >= q1) ans = "+" + ans;
            return ans;
        }
    }

    private long[] parse(String s) {
        int n = s.length(), idx = 1;
        while (idx < n && s.charAt(idx) != '/') idx++;
        long a = Long.parseLong(s.substring(1, idx)), b = Long.parseLong(s.substring(idx + 1));
        return new long[]{a, b};
    }

    private long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // 66 加一
    //输入：digits = [1,2,3]
//输出：[1,2,4]
    public int[] plusOne(int[] digits) {
        int len = digits.length;
        for (int i = len - 1; i >= 0; i--) {
            digits[i] = (digits[i] + 1) % 10;
            if (digits[i] != 0) {
                return digits;
            }
        }
        digits = new int[len + 1];
        digits[0] = 1;
        return digits;
    }

    // 67 二进制求和
    //输入：a = "1010", b = "1011"
//输出："10101"
    public String addBinary(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int n = Math.max(a.length(), b.length());
        int carry = 0;
        for (int i = 0; i < n; i++) {
            carry += i < a.length() ? (a.charAt(a.length() - 1 - i) - '0') : 0;
            carry += i < b.length() ? (b.charAt(b.length() - 1 - i) - '0') : 0;
            sb.append((char) (carry % 2 + '0'));
            carry /= 2;
        }
        if (carry > 0) {
            sb.append("1");
        }
        return sb.reverse().toString();
    }

    // 640 求解方程式
    //输入: equation = "x+5-3+x=6+x-2"
//输出: "x=2"
    public String solveEquation(String s) {
        int x = 0, num = 0, n = s.length();
        char[] cs = s.toCharArray();
        for (int i = 0, op = 1; i < n; ) {
            if (cs[i] == '+') {
                op = 1;
                i++;
            } else if (cs[i] == '-') {
                op = -1;
                i++;
            } else if (cs[i] == '=') {
                x *= -1;
                num *= -1;
                op = 1;
                i++;
            } else {
                int j = i;
                while (j < n && cs[j] != '+' && cs[j] != '-' && cs[j] != '=') j++;
                if (cs[j - 1] == 'x') x += (i < j - 1 ? Integer.parseInt(s.substring(i, j - 1)) : 1) * op;
                else num += Integer.parseInt(s.substring(i, j)) * op;
                i = j;
            }
        }
        if (x == 0) return num == 0 ? "Infinite solutions" : "No solution";
        else return "x=" + (num / -x);
    }

    // 989 数组形式的整数加法
    //输入：num = [1,2,0,0], k = 34
//输出：[1,2,3,4]
//解释：1200 + 34 = 1234
    public List<Integer> addToArrayForm(int[] num, int k) {
        List<Integer> result = new ArrayList<>();
        int n = num.length;
        for (int i = n - 1; i >= 0 || k > 0; i--, k /= 10) {
            k += (i >= 0 ? num[i] : 0);
            result.add(0, k % 10);
        }
        return result;
    }

    // 复数乘法
    public String complexNumberMultiply(String a, String b) {
        int num1a = Integer.parseInt(a.substring(0, a.indexOf('+')));
        int num1b = Integer.parseInt(a.substring(a.indexOf('+') + 1, a.indexOf('i')));
        int num2a = Integer.parseInt(b.substring(0, b.indexOf('+')));
        int num2b = Integer.parseInt(b.substring(b.indexOf('+') + 1, b.indexOf('i')));
        int resulta = num1a * num2a - num1b * num2b;
        int resultb = num1a * num2b + num1b * num2a;
        return resulta + "+" + resultb + 'i';
    }

    // 728 自除数
    //自除数 是指可以被它包含的每一位数整除的数
// 例如，128 是一个 自除数 ，因为 128 % 1 == 0，128 % 2 == 0，128 % 8 == 0。
// 自除数 不允许包含 0 。
// 给定两个整数 left 和 right ，返回一个列表，列表的元素是范围 [left, right] 内所有的 自除数 。
    public List<Integer> selfDividingNumbers(int left, int right) {
        List<Integer> result = new ArrayList<>();
        for (int i = left; i <= right; i++) {
            if (isSelfDividingNumber(i))
                result.add(i);
        }
        return result;
    }

    private boolean isSelfDividingNumber(int number) {
        if (number <= 9 && number >= 1) {
            return true;
        }
        int original = number;
        while (number > 0) {
            int divide = number % 10;
            if ((divide == 0) || (original % divide) != 0) {
                return false;
            }
            number = number / 10;
        }
        return true;
    }

    // 804 唯一摩尔斯密码词
    public int uniqueMorseRepresentations(String[] words) {
        String[] MorseCodeList = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        HashMap<String, String> MorseCodeMap = new HashMap<>();
        for (int i = 0; i < letters.length; i++) {
            MorseCodeMap.put(letters[i], MorseCodeList[i]);
        }
        Set<String> morseCodes = new HashSet<>();
        for (String word : words) {
            StringBuilder morseCode = new StringBuilder();
            for (char letter : word.toCharArray()) {
                morseCode.append(MorseCodeMap.get(String.valueOf(letter)));
            }
            morseCodes.add(morseCode.toString());
        }
        return morseCodes.size();
    }

    // 68 文本左右对齐
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> resultList = new ArrayList<>();

        // 每次取出来满足maxWidth的数量最多的单词
        int count = 0;
        int start = 0;
        for (int i = 0; i < words.length; i++) {
            count += words[i].length();
            if (count > maxWidth) {
                resultList.add(helper(words, start, i - 1, maxWidth));
                start = i;
                count = words[i].length();
            }
            // 一个单词结束至少要有一个空格
            count++;
        }

        // 处理最后一截
        resultList.add(helper(words, start, words.length - 1, maxWidth));

        return resultList;
    }

    private String helper(String[] words, int start, int end, int maxWidth) {
        StringBuilder sb = new StringBuilder();
        if (start == end) {
            // 一行一词
            oneWordOneRow(words, start, maxWidth, sb);
        } else if (end == words.length - 1) {
            // 最后一行
            lastRow(words, start, end, maxWidth, sb);
        } else {
            // 一行多词
            normal(words, start, end, maxWidth, sb);
        }

        return sb.toString();
    }

    private void oneWordOneRow(String[] words, int start, int maxWidth, StringBuilder sb) {
        // 一行只有一个单词的情况：右边添加多余的空格
        sb.append(words[start]);
        int num = maxWidth - words[start].length();
        for (int i = 0; i < num; i++) {
            sb.append(" ");
        }
    }

    private void lastRow(String[] words, int start, int end, int maxWidth, StringBuilder sb) {
        // 最后一行：单词之间不用添加额外的空格，多余的空格全部在右边
        for (int i = start; i <= end; i++) {
            sb.append(words[i]);
            if (i != end) {
                // 单词之间只有一个空格
                sb.append(" ");
            } else {
                // 判断要加几个空格
                int num = maxWidth - sb.length();
                for (int j = 0; j < num; j++) {
                    sb.append(" ");
                }
            }
        }
    }

    private void normal(String[] words, int start, int end, int maxWidth, StringBuilder sb) {
        // 正常情况：单词间的空格尽量均匀分配，可能左边的空格会多一
        // 先计算出单词总长度
        int wordsLength = 0;
        for (int i = start; i <= end; i++) {
            wordsLength += words[i].length();
        }
        // 再看均匀分配能分配几个
        int seperate = (maxWidth - wordsLength) / (end - start);
        // 多余出来的空格，即不能平均的部分，这部分要按照从左到右依次分配
        // 比如，有4个单词，即3个间隔，一共有5个空格的话
        // 平均的话是每个间隔一个空格，还多了2个空格，从左到右分配
        // 最后的间隔就是前两个各占2个空格，最后一个占1个空格
        int remain = (maxWidth - wordsLength) % (end - start);

        for (int i = start; i <= end; i++) {
            sb.append(words[i]);
            if (i != end) {
                // 先加上平均分配的空格
                for (int j = 0; j < seperate; j++) {
                    sb.append(" ");
                }
                // 再看还有没有多余的空格
                if (remain-- > 0) {
                    sb.append(" ");
                }
            }
        }
    }

    //1106 解析布尔表达式 toreview
    public boolean parseBoolExpr(String s) {
        Deque<Character> nums = new ArrayDeque<>(), ops = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == ',') continue;
            if (c == 't' || c == 'f') nums.addLast(c);
            if (c == '|' || c == '&' || c == '!') ops.addLast(c);
            if (c == '(') nums.addLast('-');
            if (c == ')') {
                char op = ops.pollLast(), cur = ' ';
                while (!nums.isEmpty() && nums.peekLast() != '-') {
                    char top = nums.pollLast();
                    cur = cur == ' ' ? top : calc(top, cur, op);
                }
                if (op == '!') cur = cur == 't' ? 'f' : 't';
                nums.pollLast();
                nums.addLast(cur);
            }
        }
        return nums.peekLast() == 't';
    }

    char calc(char a, char b, char op) {
        boolean x = a == 't', y = b == 't';
        boolean ans = op == '|' ? x | y : x & y;
        return ans ? 't' : 'f';
    }

    // 224 基本计算器
    public int calculate(String s) {
        s = s.replace(" ", "");
        Stack<Character> opts = new Stack<>();
        Stack<Integer> nums = new Stack<>();
        nums.push(0);
        char[] chars = s.toCharArray();
        int n = chars.length;
        for (int i = 0; i < n; i++) {
            char c = chars[i];
            if (c == '(') {
                opts.push(c);
            } else if (c == ')') {
                while (!opts.isEmpty() && opts.peek() != '(') {
                    calc(opts, nums);
                }
                if (!opts.isEmpty()) {
                    opts.pop();
                }
            } else {
                if (Character.isDigit(c)) {
                    int j = i, num = 0;
                    while (j < n && Character.isDigit(chars[j])) {
                        num = num * 10 + chars[j] - '0';
                        j++;
                    }
                    i = j - 1;
                    nums.push(num);
                } else {
                    if (i > 0 && (chars[i - 1] == '(' || chars[i - 1] == '+' || chars[i - 1] == '-')) {
                        nums.push(0);
                    }
                    while (!opts.isEmpty() && opts.peek() != '(') {
                        calc(opts, nums);
                    }
                    opts.push(c);
                }
            }
        }
        while (!opts.isEmpty()) calc(opts, nums);
        return nums.peek();

    }

    private void calc(Stack<Character> opts, Stack<Integer> nums) {
        if (nums.isEmpty() || nums.size() < 2) return;
        if (opts.isEmpty()) return;
        int prev = nums.pop();
        int preprev = nums.pop();
        char opt = opts.pop();
        nums.push(opt == '+' ? preprev + prev : preprev - prev);
    }

    // 227 基本计算器2
    Map<Character, Integer> map = new HashMap<Character, Integer>() {{
        put('-', 1);
        put('+', 1);
        put('*', 2);
        put('/', 2);
        put('%', 2);
        put('^', 3);
    }};

    public int calculate2(String s) {
        s = s.replace(" ", "");
        Stack<Character> opts = new Stack<>();
        Stack<Integer> nums = new Stack<>();
        nums.push(0);
        char[] chars = s.toCharArray();
        int n = chars.length;
        for (int i = 0; i < n; i++) {
            char c = chars[i];
            if (c == '(') {
                opts.push(c);
            } else if (c == ')') {
                while (!opts.isEmpty() && opts.peek() != '(') {
                    calc2(opts, nums);
                }
                if (!opts.isEmpty()) {
                    opts.pop();
                }
            } else {
                if (Character.isDigit(c)) {
                    int j = i, num = 0;
                    while (j < n && Character.isDigit(chars[j])) {
                        num = num * 10 + chars[j] - '0';
                        j++;
                    }
                    i = j - 1;
                    nums.push(num);
                } else {
                    if (i > 0 && (chars[i - 1] == '(' || chars[i - 1] == '+' || chars[i - 1] == '-')) {
                        nums.push(0);
                    }
                    while (!opts.isEmpty() && opts.peek() != '(') {
                        char prev = opts.peek();
                        if (map.get(prev) >= map.get(c)) {
                            calc2(opts, nums);
                        } else {
                            break;
                        }
                    }
                    opts.push(c);
                }
            }
        }
        while (!opts.isEmpty()) calc2(opts, nums);
        return nums.peek();
    }

    private void calc2(Stack<Character> opts, Stack<Integer> nums) {
        if (nums.isEmpty() || nums.size() < 2) return;
        if (opts.isEmpty()) return;
        int prev = nums.pop();
        int preprev = nums.pop();
        char opt = opts.pop();
        int ans = 0;
        if (opt == '+') ans = preprev + prev;
        else if (opt == '-') ans = preprev - prev;
        else if (opt == '*') ans = preprev * prev;
        else if (opt == '/') ans = preprev / prev;
        else if (opt == '^') ans = (int) Math.pow(preprev, prev);
        else if (opt == '%') ans = preprev % prev;
        nums.push(ans);
    }

    // 273 整数转换英文表示
    // 单个数字，0，1，2，3，4，5，6，7，8，9
    private static String[] OneNum = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
    // 整十，10，20，30，40，50，60，70，80，90
    private static String[] AnyTen = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    // 十几，11，12，13，14，15，16，17，18，19
    private static String[] TenNum = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    // 三位一组，几千，几百万，几十亿
    private static String[] ThreeNum = {"", "Thousand", "Million", "Billion"};

    public String numberToWords(int num) {
        if (num == 0) return "Zero";
        // 每三位一组，比如对于1234567891，在英文里面1,234,567,891表示 1 Billion 234 Million 567 Thousand 891
        StringBuilder sb = new StringBuilder();
        int idx = 3;
        for (int x = 1000000000; x > 0; x /= 1000) {
            if (num / x % 1000 != 0) {
                // 三位一组进行计算，从高到低
                sb.append(calcThreeNum(num / x % 1000)).append(" ").append(ThreeNum[idx]).append(" ");
            }
            idx--;
        }

        return sb.toString().trim();
    }

    private String calcThreeNum(int num) {
        // 计算三位数，比如 001 或者 012 或者 456
        return num / 100 == 0 ? calcTwoNum(num) : calcOneNum(num / 100) + " Hundred" + (num % 100 == 0 ? "" : " " + calcTwoNum(num % 100));
    }

    private String calcTwoNum(int num) {
        // 计算两位数，比如 01 或者 12 或者 23
        if (num >= 10 && num < 20) {
            return TenNum[num % 10];
        }
        return num / 10 == 0 ? calcOneNum(num % 10) : AnyTen[num / 10] + (num % 10 == 0 ? "" : " " + calcOneNum(num % 10));
    }

    private String calcOneNum(int num) {
        // 计算三位数，比如 4
        return OneNum[num];
    }

    // 289 生命游戏
    public void gameOfLife(int[][] board) {
        int[] neighbors = {0, 1, -1};

        int rows = board.length;
        int cols = board[0].length;

        // 创建复制数组 copyBoard
        int[][] copyBoard = new int[rows][cols];

        // 从原数组复制一份到 copyBoard 中
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                copyBoard[row][col] = board[row][col];
            }
        }

        // 遍历面板每一个格子里的细胞
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                // 对于每一个细胞统计其八个相邻位置里的活细胞数量
                int liveNeighbors = 0;

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {

                        if (!(neighbors[i] == 0 && neighbors[j] == 0)) {
                            int r = (row + neighbors[i]);
                            int c = (col + neighbors[j]);

                            // 查看相邻的细胞是否是活细胞
                            if ((r < rows && r >= 0) && (c < cols && c >= 0) && (copyBoard[r][c] == 1)) {
                                liveNeighbors += 1;
                            }
                        }
                    }
                }

                // 规则 1 或规则 3
                if ((copyBoard[row][col] == 1) && (liveNeighbors < 2 || liveNeighbors > 3)) {
                    board[row][col] = 0;
                    // -1 代表这个细胞过去是活的现在死了
//                    board[row][col] = -1;
                }
                // 规则 4
                if (copyBoard[row][col] == 0 && liveNeighbors == 3) {
                    board[row][col] = 1;
                    // 2 代表这个细胞过去是死的现在活了
//                    board[row][col] = 2;
                }
            }
        }

//        // 遍历 board 得到一次更新后的状态
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                if (board[row][col] > 0) {
//                    board[row][col] = 1;
//                } else {
//                    board[row][col] = 0;
//                }
//            }
//        }
    }

    // 812 最大三角形面积
    public double largestTriangleArea(int[][] points) {
        int n = points.length;
        double ans = 0;
        for (int i = 0; i < n - 2; i++) {
            for (int j = 0; j < n - 1; j++) {
                for (int[] point : points) {
                    ans = Math.max(ans, calcArea(points[i][0], points[i][1], points[j][0], points[j][1], point[0], point[1]));
                }
            }
        }
        return ans;
    }

    //https://leetcode.cn/problems/largest-triangle-area/solutions/1494969/by-fuxuemingzhu-czdh/
    private double calcArea(int x1, int y1, int x2, int y2, int x3, int y3) {
        return 0.5 * Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
    }

    // 223 矩形面积
    public int computeArea(int ax1, int ay1, int ax2, int ay2, int bx1, int by1, int bx2, int by2) {
        int area_a = (ay2 - ay1) * (ax2 - ax1);
        int area_b = (by2 - by1) * (bx2 - bx1);
        if (ax1 >= bx2 || ax2 <= bx1 || ay1 >= by2 || ay2 <= by1) {
            return area_a + area_b;
        }
        int width = Math.min(Math.min(ax2 - ax1, bx2 - bx1), Math.min(ax2 - bx1, bx2 - ax1));
        int height = Math.min(Math.min(ay2 - ay1, by2 - by1), Math.min(ay2 - by1, by2 - ay1));
        return area_a + area_b - width * height;
    }

    // 593 有效的正方形
    public boolean validSquare(int[] p1, int[] p2, int[] p3, int[] p4) {
        if (Arrays.equals(p1, p2)) return false;
        if (check(p1, p2, p3, p4)) return true;
        if (Arrays.equals(p1, p3)) return false;
        if (check(p1, p3, p2, p4)) return true;
        if (Arrays.equals(p1, p4)) return true;
        if (check(p1, p4, p2, p3)) return true;
        return false;
    }

    private boolean check(int[] p1, int[] p2, int[] p3, int[] p4) {
        int[] v1 = new int[]{p1[0] - p2[0], p1[1] - p2[1]};
        int[] v2 = new int[]{p3[0] - p4[0], p3[1] - p4[1]};
        return checkLength(v1, v2) && checkMidPoint(p1, p2, p3, p4) && validCos(v1, v2);
    }

    private boolean checkLength(int[] v1, int[] v2) {
        return (v1[0] * v1[0] + v1[1] * v1[1]) == (v2[0] * v2[0] + v2[1] * v2[1]);
    }

    private boolean checkMidPoint(int[] p1, int[] p2, int[] p3, int[] p4) {
        return (p1[0] + p2[0]) == (p3[0] + p4[0]) && (p1[1] + p2[1]) == (p3[1] + p4[1]);
    }

    private boolean validCos(int[] v1, int[] v2) {
        return (v1[0] * v2[0] + v1[1] * v2[1]) == 0;
    }

    // 498 对角线遍历
    //输入：mat = [[1,2,3],
    // [4,5,6],
    // [7,8,9]]
//输出：[1,2,4,7,5,3,6,8,9]
    public int[] findDiagonalOrder(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[] res = new int[m * n];
        int idx = 0;
        for (int i = 0; i <= m + n - 2; i++) {
            if (i % 2 == 0) {
                // 大于边界的时候，在最后一行开始，i变道m-1减少了i-m+1,对应的y从0加这么多
                int x = i >= m ? m - 1 : i;
                int y = i >= m ? i - m + 1 : 0;
                while (x >= 0 && y < n) {
                    res[idx++] = mat[x--][y++];
                }
            } else if (i % 2 == 1) {
                int x = i >= n ? i - n + 1 : 0;
                int y = i >= n ? n - 1 : i;
                while (x < m && y >= 0) {
                    res[idx++] = mat[x++][y--];
                }
            }
        }
        return res;
    }

    //636. 函数的独占时间
    public int[] exclusiveTime(int n, List<String> logs) {
        Stack<Integer> stack = new Stack<>();
        int[] result = new int[n];
        int cur = -1;
        for (String log : logs) {
            String[] arr = log.split(":");
            int idx = Integer.parseInt(arr[0]);
            int ts = Integer.parseInt(arr[2]);
            if ("start".equals(arr[1])) {
                if (!stack.isEmpty()) {
                    result[stack.peek()] += ts - cur;
                }
                cur = ts;
                stack.push(idx);
            } else {
                stack.pop();
                result[idx] += ts - cur + 1;
                cur = ts + 1;
            }
        }
        return result;
    }

    //766 托普利茨矩阵
    public boolean isToeplitzMatrix(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] != matrix[i - 1][j - 1]) {
                    return false;
                }
            }
        }
        return true;
    }

    // 735 行星碰撞
    public int[] asteroidCollision(int[] ats) {
        Deque<Integer> d = new ArrayDeque<>();
        for (int t : ats) {
            // ok 当前t是否可以压进栈
            boolean ok = true;
            // 只比较栈顶为正，t为负的情况
            while (ok && !d.isEmpty() && d.peekLast() > 0 && t < 0) {
                int a = Math.abs(d.peekLast()), b = Math.abs(t);
                if (a <= b) d.pollLast();//相等时也出栈
                if (a >= b) ok = false;//相等时不可压栈
            }
            if (ok) d.addLast(t);
        }
        int sz = d.size();
        int[] ans = new int[sz];
        while (!d.isEmpty()) ans[--sz] = d.pollLast();
        return ans;
    }

    //539 最小时间差
    public int findMinDifference(List<String> timePoints) {
        timePoints.sort(String::compareTo);
        int min = Integer.MAX_VALUE;
        int n = timePoints.size();
        for (int i = 0; i < n - 1; i++) {
            String prev = timePoints.get(i);
            String next = timePoints.get(i + 1);
            min = Math.min(min, getDiff(next, prev));
        }
        int last = getDiff("24:00", timePoints.get(n - 1)) + getDiff(timePoints.get(0), "00:00");
        min = Math.min(last, min);
        return min;
    }

    private int getDiff(String next, String prev) {
        int hour1 = 0;
        int hour2 = 0;
        int m1 = 0;
        int m2 = 0;
        for (int i = 0; i < 2; i++) {
            hour1 = hour1 * 10 + next.charAt(i) - '0';
            hour2 = hour2 * 10 + prev.charAt(i) - '0';
            m1 = m1 * 10 + next.charAt(i + 3) - '0';
            m2 = m2 * 10 + prev.charAt(i + 3) - '0';
        }
        return (hour1 - hour2) * 60 + m1 - m2;
    }

    // 867 转置矩阵
    public int[][] transpose(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] mat = new int[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                mat[j][i] = matrix[i][j];
            }
        }
        return mat;
    }

    // 883 三维形体投影面积
    public int projectionArea(int[][] grid) {
        int x = 0, y = 0, z = 0;
        for (int i = 0; i < grid.length; i++) {
            int max = 0, max1 = 0;
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != 0) {
                    z++;
                }
                if (max <= grid[i][j]) {
                    max = grid[i][j];
                }
                if (max1 <= grid[j][i]) {
                    max1 = grid[j][i];
                }
            }
            y += max;
            x += max1;
        }
        return x + y + z;
    }

    // 896 单调队列
    public boolean isMonotonic(int[] nums) {
        int n = nums.length;
        if (nums[0] < nums[n - 1]) {
            for (int i = 0; i < n - 1; i++) {
                if (nums[i] > nums[i + 1]) return false;
            }
        } else {
            for (int i = 0; i < n - 1; i++) {
                if (nums[i] < nums[i + 1]) return false;
            }
        }
        return true;
    }

    // 1104 二叉树寻路
    public List<Integer> pathInZigZagTree(int label) {
        int row = 1, rowStart = 1;
        while (rowStart * 2 <= label) {
            row++;
            rowStart *= 2;
        }
        if (row % 2 == 0) {
            label = getReverse(label, row);
        }
        List<Integer> path = new ArrayList<>();
        while (row > 0) {
            if (row % 2 == 0) {
                path.add(getReverse(label, row));
            } else {
                path.add(label);
            }
            row--;
            label >>= 1;
        }
        Collections.reverse(path);
        return path;
    }

    public int getReverse(int label, int row) {
        return (1 << row - 1) + (1 << row) - 1 - label;
    }

    // 1184 公交站间的距离
    public int distanceBetweenBusStops(int[] distance, int start, int destination) {
        int i = start, j = start;
        int cost1 = 0, cost2 = 0;
        int n = distance.length;
        while (i != destination) {
            cost1 += distance[i++];
            i %= n;
        }
        while (j != destination) {
            if (--j < 0) j = n - 1;
            cost2 += distance[j];
        }
        return Math.min(cost1, cost2);
    }

    // 1200最小绝对值差
    public List<List<Integer>> minimumAbsDifference(int[] arr) {
        Arrays.sort(arr);
        List<List<Integer>> ans = new ArrayList<>();
        int n = arr.length, min = arr[1] - arr[0];
        for (int i = 0; i < n - 1; i++) {
            int cur = arr[i + 1] - arr[i];
            if (cur < min) {
                ans.clear();
                min = cur;
            }
            if (cur == min) {
                List<Integer> temp = new ArrayList<>();
                temp.add(arr[i]);
                temp.add(arr[i + 1]);
                ans.add(temp);
            }
        }
        return ans;
    }

    // 1252 奇数值单元格的数目
    public int oddCells(int m, int n, int[][] indices) {
        int[][] matrix = new int[m][n];
        for (int[] indice : indices) {
            int row = indice[0];
            int col = indice[1];
            for (int i = 0; i < m; i++) {
                matrix[i][col] += 1;
            }
            for (int i = 0; i < n; i++) {
                matrix[row][i] += 1;
            }
        }
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] % 2 != 0) ans++;
            }
        }
        return ans;
    }

    public int oddCells2(int m, int n, int[][] indices) {
        int[] rows = new int[m];
        int[] cols = new int[n];
        for (int[] index : indices) {
            rows[index[0]]++;
            cols[index[1]]++;
        }
        int oddx = 0, oddy = 0;
        for (int i = 0; i < m; i++) {
            if ((rows[i] & 1) != 0) {
                oddx++;
            }
        }
        for (int i = 0; i < n; i++) {
            if ((cols[i] & 1) != 0) {
                oddy++;
            }
        }
        // 对于奇数行，只有偶数列才最终是奇数
        return oddx * (n - oddy) + (m - oddx) * oddy;
    }

    // 1282 用户分组
    public List<List<Integer>> groupThePeople(int[] groupSizes) {
        List<List<Integer>> result = new ArrayList<>();
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < groupSizes.length; i++) {
            List<Integer> group = map.getOrDefault(groupSizes[i], new ArrayList<>());
            if (group.size() == groupSizes[i]) {
                result.add(group);
                group = new ArrayList<>();
            }
            group.add(i);
            map.put(groupSizes[i], group);
        }
        result.addAll(map.values());
        return result;
    }

    // 1295 统计位数为偶数的数字
    public int findNumbers(int[] nums) {
        int ans = 0;
        for (int num : nums) {
            if (String.valueOf(num).length() % 2 == 0) {
                ans++;
            }
        }
        return ans;
    }


    //6152. 赢得比赛需要的最少训练时长
    public int minNumberOfHours(int initialEnergy, int initialExperience, int[] energy, int[] experience) {
        int n = energy.length;
        int leastEnergy = energy[n - 1] + 1;
        for (int i = n - 2; i >= 0; i--) {
            leastEnergy += energy[i];
        }
        int energyHour = leastEnergy > initialEnergy ? leastEnergy - initialEnergy : 0;
        int exp = initialExperience, sum = 0;
        for (int i = 0; i < n; i++) {
            sum = Math.max(sum, Math.max(experience[i] + 1 - exp, 0));
            exp += experience[i];
        }
        int expHour = sum;
        return energyHour + expHour;
    }

    //6166. 最大回文数字
    public String largestPalindromic(String num) {
        // 定义暂存数组
        int[] dig = new int[10];
        // 定义插入的索引
        int index = 0;
        StringBuilder s = new StringBuilder();
        // 统计各个数字出现的次数
        for (int i = 0; i < num.length(); i++) {
            dig[num.charAt(i) - '0']++;
        }
        // 从9开始遍历各个数字
        for (int i = 9; i >= 0; i--) {
            // 如果是偶数个的，依次填入两个值，0最后填入
            if ((dig[i] > 0 && dig[i] % 2 == 0 && i != 0) ||
                    (i == 0 && s.length() != 0 && dig[i] > 0 && dig[i] % 2 == 0)) {
                while (dig[i] != 0) {
                    s.insert(index, i);
                    s.insert(index + 1, i);
                    index++;
                    dig[i] -= 2;
                }
            }
            // 如果是奇数个，依次填入两个值，直到为1，0最后填入
            if ((dig[i] > 1 && dig[i] % 2 == 1 && i != 0) ||
                    (i == 0 && s.length() != 0 && dig[i] > 1 && dig[i] % 2 == 1)) {
                while (dig[i] != 1) {
                    s.insert(index, i);
                    s.insert(index + 1, i);
                    index++;
                    dig[i] -= 2;
                }
            }
        }
        // 填入单个的数字
        for (int i = 9; i >= 0; i--) {
            if (dig[i] == 1) {
                s.insert(index, i);
                break;
            }
        }
        return s.length() == 0 ? "0" : s.toString();
    }


    public int maxEqualFreq(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        Map<Integer, Integer> freq = new HashMap<>();
        int maxFreq = 0, ans = 0;
        for (int i = 0; i < nums.length; i++) {
            int cnt = count.getOrDefault(nums[i], 0);
            if (cnt > 0) {
                freq.put(cnt, freq.get(cnt) - 1);
            }
            cnt++;
            count.put(nums[i], cnt);
            freq.put(cnt, freq.getOrDefault(cnt, 0) + 1);
            maxFreq = Math.max(maxFreq, cnt);
            if (maxFreq == 1 ||
                    maxFreq * freq.get(maxFreq) + 1 == i + 1 ||
                    maxFreq + (maxFreq - 1) * freq.get(maxFreq - 1) == i + 1) {
                ans = Math.max(ans, i + 1);
            }
        }
        return ans;
    }

    //endregion ----------------------------------------------------------------------------------------------

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

    // 1785. 构成特定和需要添加的最少元素
    public int minElements(int[] nums, int limit, int goal) {
        long sum = 0;
        for (int num : nums) {
            sum += num;
        }
        long diff = goal - sum;
        long ans = (Math.abs(diff) + limit - 1) / limit;
        return (int)ans;
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

    // endregion--------------------------------------------------------------------------------------------------

    //region---------------------------------------------排序-----------------------------------------------
    //region -------------------------------------------------堆排序/快速选择/桶排序-------------------------------------------
// offer 54 BST的第k大节点
//给定一棵二叉搜索树，请找出其中第 k 大的节点的值。
    int reskthLargest, kthLargest;

    public int kthLargest(TreeNode root, int k) {
        this.kthLargest = k;
        dfskthLargest(root);
        return reskthLargest;
    }

    private void dfskthLargest(TreeNode node) {
        if (node == null) return;
        dfskthLargest(node.right);
        if (kthLargest == 0) return;
        if (--kthLargest == 0) reskthLargest = node.val;
        dfskthLargest(node.left);
    }

    // 215 数组中的第K个最大元素
    //快排-》快速选择
    public int findKthLargest(int[] nums, int k) {
        int length = nums.length;
        int left = 0;
        int right = nums.length - 1;
        int target = nums.length - k;
        while (true) {
            int index = partition(nums, left, right);
            if (index == target) {
                return nums[index];
            } else if (index < target) {
                left = index + 1;
            } else {
                right = index - 1;
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

    private int partition2(int[] nums, int left, int right) {
        int pivot = nums[left];
        int j = left;
        for (int i = left + 1; i <= right; i++) {
            if (nums[i] < pivot) {
                j++;
                swap(nums, j, i);
            }
        }
        swap(nums, j, left);
        return j;
    }

    //堆排序
    public int findKthLargestMaxHeap(int[] nums, int k) {
        int length = nums.length;
        buildMaxHeap(nums, length);
        for (int i = nums.length - 1; i >= nums.length - k + 1; i--) {
            swap(nums, i, 0);
            length--;
            maxheapify(nums, 0, length);
        }
        return nums[0];
    }

    public int findKthLargestMinHeap(int[] nums, int k) {
        int max = 0;
        buildMinheap(nums, k);
        for (int i = k; i < nums.length; i++) {
            if (nums[i] > nums[0]) {
                swap(nums, 0, i);
                minheapify(nums, 0, k);
            }
        }
        return nums[0];
    }

    private void buildMinheap(int[] nums, int heapsize) {
        for (int i = (heapsize >> 1) - 1; i >= 0; i--) {
            minheapify(nums, i, heapsize);
        }
    }

    private void minheapify(int[] nums, int i, int heapsize) {
        int l = 2 * i + 1, r = 2 * i + 2;
        int min = i;
        if (l < heapsize && nums[l] < nums[min]) {
            min = l;
        }
        if (r < heapsize && nums[r] < nums[min]) {
            min = r;
        }
        if (min != i) {
            swap(nums, min, i);
            minheapify(nums, min, heapsize);
        }
    }


    private void buildMaxHeap(int[] nums, int heapsize) {
        for (int i = heapsize / 2 - 1; i >= 0; i--) {
            maxheapify(nums, i, heapsize);
        }
    }

    private void maxheapify(int[] nums, int i, int heapsize) {
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        int largest = i;
        if (l < heapsize && nums[l] > nums[largest]) {
            largest = l;
        }
        if (r < heapsize && nums[r] > nums[largest]) {
            largest = r;
        }
        if (largest != i) {
            swap(nums, largest, i);
            maxheapify(nums, largest, heapsize);
        }
    }

    // 347 前K个高频元素
    //给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按 任意顺序 返回答案。
//输入: nums = [1,1,1,2,2,3], k = 2
//输出: [1,2]
    public int[] topKFrequentHeapSort(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }
        int[][] array = new int[2][map.size()];
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            array[0][i] = entry.getKey();
            array[1][i] = entry.getValue();
            i++;
        }
        buildMinHeap(array, k);
        for (int j = k; j < map.size(); j++) {
            if (array[1][j] > array[1][0]) {
                swap(array, j, 0);
                minheapify(array, 0, k);
            }
        }
        int[] result = new int[k];
        System.arraycopy(array[0], 0, result, 0, k);
        return result;
    }

    public void buildMinHeap(int[][] nums, int heapsize) {
        for (int i = ((heapsize >>> 1) - 1); i >= 0; i--) {
            minheapify(nums, i, heapsize);
        }
    }

    public void minheapify(int[][] nums, int i, int heapsize) {
        int l = 2 * i + 1, r = 2 * i + 2, min = i;
        if (l < heapsize && nums[1][l] < nums[1][min]) {
            min = l;
        }
        if (r < heapsize && nums[1][r] < nums[1][min]) {
            min = r;
        }
        if (min != i) {
            swap(nums, min, i);
            minheapify(nums, min, heapsize);
        }
    }

    private void swap(int[][] nums, int p, int q) {
        int tmp = nums[0][p];
        nums[0][p] = nums[0][q];
        nums[0][q] = tmp;
        int tmp2 = nums[1][p];
        nums[1][p] = nums[1][q];
        nums[1][q] = tmp2;
    }

    public int[] topKFrequentQuickSelect(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }
        int[][] array = new int[2][map.size()];
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            array[0][i] = entry.getKey();
            array[1][i] = entry.getValue();
            i++;
        }
        int[] result = new int[k];

        int target = map.size() - k;
        int left = 0, right = map.size() - 1;
        while (true) {
            int idx = partitionId(array, left, right);
            if (idx == target) {
                for (int j = target; j < map.size(); j++) {
                    result[j - target] = array[0][j];
                }
                break;
            } else if (idx < target) {
                left = idx + 1;
            } else {
                right = idx - 1;
            }
        }
        return result;
    }

    public int partitionId(int[][] nums, int left, int right) {
        int pivot_0 = nums[0][left];
        int pivot = nums[1][left];
        int l = left, r = right;
        while (l < r) {
            while (r > l && nums[1][r] >= pivot) {
                r--;
            }
            swap(nums, l, r);
            while (l < r && nums[1][l] <= pivot) {
                l++;
            }
            swap(nums, l, r);
        }
        nums[0][l] = pivot_0;
        nums[1][l] = pivot;
        return l;
    }

    //桶排序
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }
        List<Integer>[] array = new ArrayList[nums.length + 1];
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (array[entry.getValue()] == null) {
                array[entry.getValue()] = new ArrayList<>();
            }
            array[entry.getValue()].add(entry.getKey());
        }
        List<Integer> result = new ArrayList<>();
        for (int i = nums.length; i >= 0 && result.size() < k; i--) {
            if (array[i] == null) continue;
            if (array[i].size() <= k) result.addAll(array[i]);
        }
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    //最小k个数  快速选择
    public int[] getLeastNumbers(int[] arr, int k) {
        if (arr.length == 0 || k == 0) return new int[0];
        int left = 0, right = arr.length - 1;
        while (true) {
            int idx = quickSelect(arr, left, right);
            if (idx == k - 1) {
                return Arrays.copyOf(arr, k);
            } else if (idx < k) {
                left = idx + 1;
            } else {
                right = idx - 1;
            }
        }
    }

    private int quickSelect(int[] arr, int left, int right) {
        int l = left, r = right;
        int pivot = arr[l];
        while (l < r) {
            while (r > l && arr[r] >= pivot) {
                r--;
            }
            arr[l] = arr[r];
            while (l < r && arr[l] <= pivot) {
                l++;
            }
            arr[r] = arr[l];
        }
        arr[l] = pivot;
        return l;
    }

    //优先队列
    public int[] getLeastNumbersPriorityQueue(int[] arr, int k) {
        if (arr.length == 0 || k == 0) return new int[0];
        PriorityQueue<Integer> queue = new PriorityQueue<>((o1, o2) -> o2 - o1);
        for (int i = 0; i < k; i++) {
            queue.offer(arr[i]);
        }
        for (int i = k; i < arr.length; i++) {
            if (arr[i] < queue.peek()) {
                queue.poll();
                queue.offer(arr[i]);
            }
        }
        int[] res = new int[k];
        for (int i = 0; i < k; i++) {
            res[i] = queue.poll();
        }
        return res;
    }

    //堆排序
    public int[] getLeastNumbersHeap(int[] arr, int k) {
        buildMaxHeap(arr, k);
        int[] res = new int[k];
        for (int i = k; i < arr.length; i++) {
            if (arr[i] < arr[0]) {
                swap(arr, 0, i);
                maxheapify(arr, 0, k);
            }
        }
        return Arrays.copyOfRange(arr, 0, k);
    }

    // 264 丑数
    // dp做法搜nthUglyNumber
    public int nthUglyNumberPriorityQueue(int n) {
        PriorityQueue<Long> priorityQueue = new PriorityQueue<>();
        int[] nums = new int[]{2, 3, 5};
        priorityQueue.offer(1L);
        Set<Long> set = new HashSet<>();
        set.add(1L);
        long ans = 0;
        while (n-- > 0 && !priorityQueue.isEmpty()) {
            ans = priorityQueue.poll();
            for (int num : nums) {
                if (!set.contains(ans * num)) {
                    priorityQueue.offer(ans * num);
                    set.add(ans * num);
                }
            }
        }
        return (int) ans;
    }


    // 373 查找和最小的K对数字
    // 状态压缩
    // 二分法查kSmallestPairsBinarySearch
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(o -> (nums1[o[0]] + nums2[o[1]])));
        for (int i = 0; i < Math.min(k, nums1.length); i++) {
            priorityQueue.offer(new int[]{i, 0});
        }
        //数量小于k，priorityQueue的size小于k 提前变为空
        while (k-- > 0 && !priorityQueue.isEmpty()) {
            int[] pos = priorityQueue.poll();
            ans.add(Arrays.asList(nums1[pos[0]], nums2[pos[1]]));
            if (++pos[1] < nums2.length) {
                priorityQueue.offer(pos);
            }
        }
        return ans;
    }

    // 378 有序矩阵中第K小的元素
    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o[0]));
        for (int i = 0; i < n; i++) {
            priorityQueue.offer(new int[]{matrix[0][i], 0, i});
        }
        int ans = 0;
        while (k-- > 0 && !priorityQueue.isEmpty()) {
            int[] tmp = priorityQueue.poll();
            ans = tmp[0];
            if (tmp[1] != n - 1) {
                tmp[0] = matrix[tmp[1] + 1][tmp[2]];
                tmp[1] = tmp[1] + 1;
                priorityQueue.offer(tmp);
            }
        }
        return ans;
    }

    // 937 重新排列日志文件
    // 自定义排序
    public String[] reorderLogFiles(String[] logs) {
        List<Log> list = new ArrayList<>();
        for (int i = 0; i < logs.length; i++) {
            list.add(new Log(i, logs[i]));
        }
        list.sort((o1, o2) -> {
            if (o1.type != o2.type) return o1.type - o2.type;
            if (o1.type == 1) return o1.idx - o2.idx;
            if (!o1.content.equals(o2.content)) return o1.content.compareTo(o2.content);
            return o1.sign.compareTo(o2.sign);
        });
        String[] result = new String[list.size()];
        int idx = 0;
        for (Log log : list) {
            result[idx++] = log.origin;
        }
        return result;
    }

    static class Log {
        int idx, type;
        String sign, content, origin;

        public Log(int _idx, String s) {
            this.idx = _idx;
            int n = s.length(), i = 0;
            while (i < n && s.charAt(i) != ' ') i++;
            sign = s.substring(0, i);
            content = s.substring(i + 1);
            origin = s;
            type = Character.isDigit(content.charAt(0)) ? 1 : 0;

        }
    }

    //endregion--------------------------------------------------------------------
    // 简单选择排序
    public void selectSort(int[] arr) {
        //每次从剩下的元素中选择最小值放到第一个位置
        for (int i = 0; i < arr.length - 1; i++) {
            //记录每一趟最小值坐标
            int min = i;
            //寻找每一趟的最小值 先找到坐标 最后再进行交换 减少交换次数
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }
            //元素交换
            if (min != i) {
                int temp = arr[min];
                arr[min] = arr[i];
                arr[i] = temp;
            }
        }
    }

    //插入排序
    public int[] insertionSort(int[] array) {
        if (array.length == 0)
            return array;
        int current;
        for (int i = 0; i < array.length - 1; i++) {
            current = array[i + 1];
            int preIndex = i;
            while (preIndex >= 0 && current < array[preIndex]) {
                array[preIndex + 1] = array[preIndex];
                preIndex--;
            }
            array[preIndex + 1] = current;
        }
        return array;
    }

    public int[] insertionSort2(int[] array) {
        if (array.length == 0)
            return array;
        int current;
        for (int i = 1; i < array.length; i++) {
            current = array[i];
            int preIndex = i - 1;
            while (preIndex >= 0 && current < array[preIndex]) {
                array[preIndex + 1] = array[preIndex];
                preIndex--;
            }
            array[preIndex + 1] = current;
        }
        return array;
    }

    // 希尔排序 针对有序序列在插入时采用交换法
    public static void sort(int[] arr) {
        //增量gap，并逐步缩小增量
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            //从第gap个元素，逐个对其所在组进行直接插入排序操作
            for (int i = gap; i < arr.length; i++) {
                int j = i;
                while (j - gap >= 0 && arr[j] < arr[j - gap]) {
                    //插入排序采用交换法
                    swap1(arr, j, j - gap);
                    j -= gap;
                }
            }
        }
    }

    // 希尔排序 针对有序序列在插入时采用移动法。
    public static void sort1(int[] arr) {
        //增量gap，并逐步缩小增量
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            //从第gap个元素，逐个对其所在组进行直接插入排序操作
            for (int i = gap; i < arr.length; i++) {
                int j = i;
                int temp = arr[j];
                if (arr[j] < arr[j - gap]) {
                    while (j - gap >= 0 && temp < arr[j - gap]) {
                        //移动法
                        arr[j] = arr[j - gap];
                        j -= gap;
                    }
                    arr[j] = temp;
                }
            }
        }
    }

    public static void swap1(int[] arr, int a, int b) {
        arr[a] = arr[a] + arr[b];
        arr[b] = arr[a] - arr[b];
        arr[a] = arr[a] - arr[b];
    }

    /**
     * 归并排序
     */
    public int[] MergeSort(int[] array) {
        if (array.length < 2) return array;
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(MergeSort(left), MergeSort(right));
    }

    private int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length)
                result[index] = right[j++];
            else if (j >= right.length)
                result[index] = left[i++];
            else if (left[i] > right[j])
                result[index] = right[j++];
            else
                result[index] = left[i++];
        }
        return result;
    }

    /**
     * @param arr        待排序列
     * @param leftIndex  待排序列起始位置
     * @param rightIndex 待排序列结束位置
     */
    public void quickSort(int[] arr, int leftIndex, int rightIndex) {
        if (leftIndex >= rightIndex) {
            return;
        }

        int left = leftIndex;
        int right = rightIndex;
        //待排序的第一个元素作为基准值
        int key = arr[left];

        //从左右两边交替扫描，直到left = right
        while (left < right) {
            while (right > left && arr[right] >= key) {
                //从右往左扫描，找到第一个比基准值小的元素
                right--;
            }

            //找到这种元素将arr[right]放入arr[left]中
            arr[left] = arr[right];

            while (left < right && arr[left] <= key) {
                //从左往右扫描，找到第一个比基准值大的元素
                left++;
            }

            //找到这种元素将arr[left]放入arr[right]中
            arr[right] = arr[left];
        }
        //基准值归位
        arr[left] = key;
        //对基准值左边的元素进行递归排序
        quickSort(arr, leftIndex, left - 1);
        //对基准值右边的元素进行递归排序。
        quickSort(arr, right + 1, rightIndex);
    }

    //贪心+快排模板 offer 45
    public String minNumber(int[] nums) {
        String[] strs = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            strs[i] = String.valueOf(nums[i]);
        }
        quickSort(strs, 0, strs.length - 1);
        return String.join("", strs);
    }

    private void quickSort(String[] strings, int left, int right) {
        if (left >= right) return;
        int middle = quickSelect(strings, left, right);
        quickSort(strings, left, middle - 1);
        quickSort(strings, middle + 1, right);
    }

    private int quickSelect(String[] strings, int left, int right) {
        String pivot = strings[left];
        while (left < right) {
            while (left < right && (strings[right] + pivot).compareTo(pivot + strings[right]) >= 0) {
                right--;
            }
            strings[left] = strings[right];
            while (left < right && (strings[left] + pivot).compareTo(pivot + strings[left]) <= 0) {
                left++;
            }
            strings[right] = strings[left];
        }
        strings[left] = pivot;
        return left;
    }

    //1051. 高度检查器
    public int heightChecker(int[] heights) {
        int[] copy = Arrays.copyOfRange(heights, 0, heights.length);
        Arrays.sort(copy);
        int ans = 0;
        for (int i = 0; i < heights.length; i++) {
            if (heights[i] != copy[i]) {
                ans++;
            }
        }
        return ans;
    }

    // 桶排序计数
    public int heightCheckerCount(int[] heights) {
        // 值的范围是1 <= heights[i] <= 100，因此需要1,2,3,...,99,100，共101个桶
        int[] arr = new int[101];
        // 遍历数组heights，计算每个桶中有多少个元素，也就是数组heights中有多少个1，多少个2，。。。，多少个100
        // 将这101个桶中的元素，一个一个桶地取出来，元素就是有序的
        for (int height : heights) {
            arr[height]++;
        }

        int count = 0;
        for (int i = 1, j = 0; i < arr.length; i++) {
            // arr[i]，i就是桶中存放的元素的值，arr[i]是元素的个数
            // arr[i]-- 就是每次取出一个，一直取到没有元素，成为空桶
            while (arr[i]-- > 0) {
                // 从桶中取出元素时，元素的排列顺序就是非递减的，然后与heights中的元素比较，如果不同，计算器就加1
                if (heights[j++] != i) count++;
            }
        }
        return count;
    }

    // 88 合并两个有序数组
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int idx = m + n - 1;
        int idx1 = m - 1, idx2 = n - 1;
        while (idx >= 0) {
            if (idx1 == -1) {
                nums1[idx--] = nums2[idx2--];
            } else if (idx2 == -1) {
                nums1[idx--] = nums1[idx1--];
            } else if (nums1[idx1] < nums2[idx2]) {
                nums1[idx--] = nums2[idx2--];
            } else {
                nums1[idx--] = nums1[idx1--];
            }
        }
    }

    // 164 最大间距
    // 基数排序
    public int maximumGap(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return 0;
        }
        long exp = 1;
        int[] buf = new int[n];
        int maxVal = Arrays.stream(nums).max().getAsInt();

        while (maxVal >= exp) {
            int[] cnt = new int[10];
            for (int i = 0; i < n; i++) {
                int digit = (nums[i] / (int) exp) % 10;
                cnt[digit]++;
            }
            for (int i = 1; i < 10; i++) {
                cnt[i] += cnt[i - 1];
            }
            for (int i = n - 1; i >= 0; i--) {
                int digit = (nums[i] / (int) exp) % 10;
                buf[cnt[digit] - 1] = nums[i];
                cnt[digit]--;
            }
            System.arraycopy(buf, 0, nums, 0, n);
            exp *= 10;
        }

        int ret = 0;
        for (int i = 1; i < n; i++) {
            ret = Math.max(ret, nums[i] - nums[i - 1]);
        }
        return ret;
    }

    //桶排序
    public int maximumGap2(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return 0;
        }
        int minVal = Arrays.stream(nums).min().getAsInt();
        int maxVal = Arrays.stream(nums).max().getAsInt();
        int d = Math.max(1, (maxVal - minVal) / (n - 1));
        int bucketSize = (maxVal - minVal) / d + 1;

        int[][] bucket = new int[bucketSize][2];
        for (int i = 0; i < bucketSize; ++i) {
            Arrays.fill(bucket[i], -1); // 存储 (桶内最小值，桶内最大值) 对， (-1, -1) 表示该桶是空的
        }
        for (int i = 0; i < n; i++) {
            int idx = (nums[i] - minVal) / d;
            if (bucket[idx][0] == -1) {
                bucket[idx][0] = bucket[idx][1] = nums[i];
            } else {
                bucket[idx][0] = Math.min(bucket[idx][0], nums[i]);
                bucket[idx][1] = Math.max(bucket[idx][1], nums[i]);
            }
        }

        int ret = 0;
        int prev = -1;
        for (int i = 0; i < bucketSize; i++) {
            if (bucket[i][0] == -1) {
                continue;
            }
            if (prev != -1) {
                ret = Math.max(ret, bucket[i][0] - bucket[prev][1]);
            }
            prev = i;
        }
        return ret;
    }

    // 324 摆动排序
//输入：nums = [1,5,1,1,6,4]
//输出：[1,6,1,5,1,4]
//解释：[1,4,1,5,1,6] 同样是符合题目要求的结果，可以被判题程序接受。
//输入：nums = [1,3,2,2,3,1]
//输出：[2,3,1,3,1,2]
    public void wiggleSort(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        int[] arr = nums.clone();
        for (int i = 0, j = (n - 1) / 2, k = n - 1; i < n; i += 2) {
            nums[i] = arr[j--];
            if (i + 1 < n) {
                nums[i + 1] = arr[k--];
            }
        }
    }

    //O(n)
    public void wiggleSort2(int[] nums) {
        // 1. 找到中位数
        int n = nums.length;
        int median = getKthSmaller(nums, (n - 1) / 2);
        // 2. 三向切分
        for (int smallIdx = 0, idx = 0, largeIdx = n - 1; idx < largeIdx; idx++) {
            if (nums[idx] > median) {
                swap(nums, idx--, largeIdx--);
            } else if (nums[idx] < median) {
                swap(nums, idx, smallIdx++);
            }
        }
        // 3.倒序重组
        int[] arr = nums.clone();
        for (int i = 0, j = (n - 1) / 2, k = n - 1; i < n; i += 2, j--, k--) {
            nums[i] = arr[j];
            if (i + 1 < n) {
                nums[i + 1] = arr[k];
            }
        }
    }

    private int getKthSmaller(int[] nums, int k) {
        int left = 0, right = nums.length - 1;
        while (true) {
            int idx = partitionId(nums, left, right);
            if (idx == k) {
                return nums[idx];
            } else if (idx > k) {
                right = idx - 1;
            } else {
                left = idx + 1;
            }
        }
    }

    private int partitionId(int[] nums, int l, int r) {
        int left = l, right = r;
        int pivot = nums[left];
        while (left < right) {
            while (left < right && nums[right] >= pivot) {
                right--;
            }
            nums[left] = nums[right];
            while (left < right && nums[left] <= pivot) {
                left++;
            }
            nums[right] = nums[left];
        }
        nums[left] = pivot;
        return left;
    }

    // 三向切分 把小于中位数的放到前面，大于中位数的放到后面
    private void threeWayPartition(int[] nums, int median) {
        int l = 0, r = nums.length - 1, i = 0;
        // 类似3色问题
        while (i <= r) {
            if (nums[i] > median) {
                // 换完继续判断当前i
                swap(nums, r--, i);
            } else if (nums[i] < median) {
                // 和当前l一样，同时加1
                // 比l大，此时l指向的一定是median，换完继续往后移
                swap(nums, l++, i++);
            } else {
                i++;
            }
        }
    }
    // 三向切分+倒序重组
    private void threeWayPartition2(int[] nums, int median) {
        int l = 0, r = nums.length - 1, i = 0;
        // 类似3色问题
        while (i <= r) {
            if (nums[getIdx(i)] < median) {
                swap(nums, getIdx(r--), getIdx(i));
            } else if (nums[getIdx(i)] > median) {
                swap(nums, getIdx(l++), getIdx(i++));
            } else {
                i++;
            }
        }
    }

    public int getIdx(int i) {
        int n=0;
        return (1 + 2 * (i)) % (n | 1);
    }

    // 406 根据身高重建队列
    public int[][] reconstructQueue(int[][] people) {
        Arrays.sort(people, (p1, p2) -> p1[0] != p2[0] ? Integer.compare(p2[0], p1[0]) : Integer.compare(p1[1], p2[1]));
        List<int[]> list = new ArrayList<>();

        for (int[] ppl : people) list.add(ppl[1], ppl);
        return list.toArray(new int[people.length][]);
    }


    // 581 最短无序连续子数组  双指针
    //Order Check -> 检查数组中的数是否有序，返回错误排序的元素个数，ex 【1,1，3,4,1】return 3 因为3,4,1排序不对
    //给你一个整数数组 nums ，你需要找出一个 连续子数组 ，如果对这个子数组进行升序排序，那么整个数组都会变为升序排序。
// 请你找出符合题意的 最短 子数组，并输出它的长度。
//输入：nums = [2,6,4,8,10,9,15]
//输出：5
//解释：你只需要对 [6, 4, 8, 10, 9] 进行升序排序，那么整个表都会变为升序排序。
//
    public int orderCheckOrfindUnsortedSubarray(int[] nums) {
        int n = nums.length;
        int[] arr = nums.clone();
        Arrays.sort(arr);
        int i = 0, j = n - 1;
        while (i <= j && nums[i] == arr[i]) i++;
        while (i <= j && nums[j] == arr[j]) j--;
        return j - i + 1;
    }

    int MIN = -100005, MAX = 100005;

    public int findUnsortedSubarray(int[] nums) {
        int n = nums.length;
        int i = 0, j = n - 1;
        while (i < j && nums[i] <= nums[i + 1]) i++;
        while (i < j && nums[j] >= nums[j - 1]) j--;
        int l = i, r = j;
        int min = nums[i], max = nums[j];
        for (int u = l; u <= r; u++) {
            // 1 3 5 4 2 8 6 7 9
            if (nums[u] < min) {
                //i从u往0找，找到第一个小于u的i1,从i1+1到u都要重新排列
                while (i >= 0 && nums[i] > nums[u]) i--;
                min = i >= 0 ? nums[i] : MIN;
            }
            //从j往后找到第一个比u大的，j-1的需要重新排列
            if (nums[u] > max) {
                while (j < n && nums[j] < nums[u]) j++;
                max = j < n ? nums[j] : MAX;
            }
        }
        return j == i ? 0 : (j - 1) - (i + 1) + 1;
    }

    // O(n)做法
    public int findUnsortedSubarray2(int[] nums) {
        int n = nums.length;
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        // r从前往后找，l从后往前找
        int r = 0, l = n - 1;
        for (int i = 0; i < n; i++) {
            //max：从前往后的最大值,只要小于max的值，都应该被排列
            if (nums[i] < max) {
                r = i;
            } else {
                max = nums[i];
            }
            //min是从后往前的最小值，大于该值的都应该被排列
            if (nums[n - 1 - i] > min) {
                l = n - 1 - i;
            } else {
                min = nums[n - 1 - i];
            }
        }
        return r > l ? r - l + 1 : 0;
    }

    //逐层排序二叉树所需的最少操作数目
    // 经典问题：给一个序列，序列两两元素可以任意交换，求最少的交换次数使得序列有序
    //这是一个经典问题，一般有两种做法：
    //1.从1到n枚举下标i,设当前序列第i个数是ai，目标序列第i个数是bi，若ai!=bi，不断将ai交换到目标位置直到ai=bi，交换次数就是答案
    //2.求整个序列中置换环的数量，答案就是序列长度减去置换环的数量（并查集 连通分量）
    private int getSwapCnt(int[] nums) {
        int n = nums.length;
        int[][] arr = new int[n][2];
        for (int i = 0; i < n; i++) {
            arr[i] = new int[]{nums[i], i};
        }
        Arrays.sort(arr, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];
            return o1[1] - o2[1];
        });
        Solutions1.UnionFind1 unionFind = new Solutions1.UnionFind1(n);
        for (int i = 0; i < n; i++) {
            unionFind.union(i, arr[i][1]);
        }
//        for (int i = 0; i < arr.length; i++) map.put(temp[i], i);
//        for (int i = 0; i < arr.length; i++) {
//            while (arr[i] != temp[i]) {
//                int j = map.get(arr[i]);
//                int t = arr[i];
//                arr[i] = arr[j];
//                arr[j] = t;
//                ans++;
//            }
//        }
        return n - unionFind.getConnectedNum();
    }

    // 621 任务调度器
    // 计数排序
    public int leastInterval(char[] tasks, int n) {
        char[] chars = new char[26];
        int max = 0;
        for (char task : tasks) {
            max = Math.max(max, ++chars[task - 'A']);
        }
        int result = (max - 1) * (n + 1);
        for (int i = 0; i < 26; i++) {
            if (chars[i] == max) {
                result++;
            }
        }
        return Math.max(result, tasks.length);
    }

    // 1403 非递增顺序的最小子序列
    public List<Integer> minSubsequence(int[] nums) {
        Arrays.sort(nums);
        int sum = 0;
        for (int n : nums) {
            sum += n;
        }
        int tmp = 0;
        List<Integer> result = new ArrayList<>();
        for (int i = nums.length - 1; i >= 0; i--) {
            tmp += nums[i];
            result.add(nums[i]);
            if (tmp > sum - tmp) return result;
        }
        return result;
    }

    // 计数排序
    public List<Integer> minSubsequence2(int[] nums) {
        List<Integer> ans = new ArrayList<>();

        int sum = 0;
        int[] count = new int[101];
        for (int num : nums) {
            count[num]++;
            sum += num;
        }

        int num = 100;
        int x = 0;
        while (x <= sum - x) {
            if (count[num] != 0) {
                ans.add(num);
                x += num;
                count[num]--;
            }
            if (count[num] == 0) {
                num--;
            }
        }

        return ans;
    }

    // 1122 数组的相对排序
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        int[] cnt = new int[1001];
        for (int num : arr1) {
            cnt[num]++;
        }
        int idx = 0;
        for (int num : arr2) {
            while (cnt[num]-- > 0) {
                arr1[idx++] = num;
            }
        }
        for (int i = 0; i <= 1000; i++) {
            while (cnt[i]-- > 0) {
                arr1[idx++] = i;
            }
        }
        return arr1;
    }

    // 899 有序队列
    //给定一个字符串 s 和一个整数 k 。你可以从 s 的前 k 个字母中选择一个，并把它加到字符串的末尾。
// 返回 在应用上述步骤的任意数量的移动后，字典上最小的字符串 。
//输入：s = "cba", k = 1
//输出："acb"
    // 朴素双循环
    public String orderlyQueue(String s, int k) {
        if (k == 1) {
            String smallest = s;
            StringBuilder sb = new StringBuilder(s);
            int n = s.length();
            for (int i = 1; i < n; i++) {
                char c = sb.charAt(0);
                sb.deleteCharAt(0);
                sb.append(c);
                if (sb.toString().compareTo(smallest) < 0) {
                    smallest = sb.toString();
                }
            }
            return smallest;
        } else {
            char[] arr = s.toCharArray();
            Arrays.sort(arr);
            return new String(arr);
        }
    }

    // 最小表示法
    public String orderlyQueue2(String s, int _k) {
        char[] cs = s.toCharArray();
        if (_k == 1) {
            int i = 0, j = 1, k = 0, n = cs.length;
            while (i < n && j < n && k < n) {
                char a = cs[(i + k) % n], b = cs[(j + k) % n];
                if (a == b) k++;
                else {
                    if (a > b) i += k + 1;
                    else j += k + 1;
                    if (i == j) i++;
                    k = 0;
                }
            }
            i = Math.min(i, j);
            return s.substring(i) + s.substring(0, i);
        } else {
            Arrays.sort(cs);
            return String.valueOf(cs);
        }
    }

    //offer 51 数组中的逆序对
    //在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。输入一个数组，求出这个数组中的逆序对的总数。
// 输入: [7,5,6,4]
//输出: 5
    public int reversePairs(int[] nums) {
        if (nums.length < 2) return 0;
        return reversePairs(nums, 0, nums.length - 1, new int[nums.length]);
    }

    private int reversePairs(int[] nums, int left, int right, int[] temp) {
        if (left >= right) {
            return 0;
        }

        int mid = (left + right) / 2;
        int leftPairs = reversePairs(nums, left, mid, temp);
        int rightPairs = reversePairs(nums, mid + 1, right, temp);

        if (nums[mid] <= nums[mid + 1]) {
            return leftPairs + rightPairs;
        }

        int crossPairs = mergeAndCount(nums, left, mid, right, temp);
        return leftPairs + rightPairs + crossPairs;
    }

    private int mergeAndCount(int[] nums, int left, int mid, int right, int[] temp) {
        for (int i = left; i <= right; i++) {
            temp[i] = nums[i];
        }

        int i = left;
        int j = mid + 1;

        int count = 0;
        for (int k = left; k <= right; k++) {
            // 左边数组已经遍历完
            if (i == mid + 1) {
                nums[k] = temp[j++];
            } else if (j == right + 1) {   // 右边数组已经遍历完
                nums[k] = temp[i++];
            } else if (temp[i] <= temp[j]) { //左边小于右边
                nums[k] = temp[i++];
            } else {
                nums[k] = temp[j++]; //左边大于右边时计算i到mid的个数[i-mid]都大于j
                count += (mid - i + 1);
            }
        }
        return count;
    }

    // 1710卡车上的最大单元数
    public int maximumUnits(int[][] boxTypes, int truckSize) {
        Arrays.sort(boxTypes, (o1, o2) -> {
            if (o2[1] != o1[1]) {
                return o2[1] - o1[1];
            }
            return o1[0] - o2[0];
        });
        int ans = 0;
        for (int[] box : boxTypes) {
            if (truckSize <= 0) break;
            if (box[0] > truckSize) {
                ans += box[1] * truckSize;
                truckSize = 0;
            } else {
                ans += box[1] * box[0];
                truckSize -= box[0];
            }
        }
        return ans;
    }

    //endregion---------------------------------------------------------------------------------------------

    //region---------------------------------------------------二分-----------------------------------------------
    //二分模板
    public void binary() {
        long l = 0, r = 1000009;
        while (l < r) {
            long mid = l + r + 1 >> 1;
            if (check(mid)) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }

        while (l < r) {
            long mid = l + r >> 1;
            if (check(mid)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
    }

    public boolean check(long mid) {
        return false;
    }

    // 34 在排序数组中查找元素的第一个和最后一个位置
    public int[] searchRange(int[] nums, int target) {
        if (nums.length == 0) return new int[]{-1, -1};
        int l = 0, r = nums.length - 1;
        while (l < r) {
            // >=target的最小的位置
            int mid = l + r >> 1;
            if (nums[mid] >= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        int left = nums[l] == target ? l : -1;
        l = 0;
        r = nums.length - 1;
        while (l < r) {
            // <=target的最大的位置
            int mid = l + r + 1 >> 1;
            if (nums[mid] <= target) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        int right = nums[l] == target ? l : -1;
        return new int[]{left, right};
    }

    //35. 搜索插入位置
    public int searchInsert(int[] nums, int target) {
        int l = 0, r = nums.length;
        while (l < r) {
            // 查找>=target的最小的位置,全都<target的时候数组长度可加1，故r=nums.length
            int mid = (l + r) >> 1;
            if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    // 74 搜索二维矩阵
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        if (target < matrix[0][0] || target > matrix[m - 1][n - 1]) return false;
        int l = 0, r = m - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (matrix[mid][0] <= target) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        int row = l;
        l = 0;
        r = n - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (matrix[row][mid] >= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        int col = l;
        return matrix[row][col] == target;
    }

    // 240 搜索二维矩阵
    public boolean searchMatrix2(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        if (target < matrix[0][0] || target > matrix[m - 1][n - 1]) return false;
        for (int[] row : matrix) {
            int l = 0, r = n - 1;
            while (l < r) {
                int mid = l + r >> 1;
                if (row[mid] >= target) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            if (row[l] == target) return true;
        }
        return false;
    }

    public boolean searchMatrix2BST(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int x = 0, y = n - 1;
        while (x < m && y >= 0) {
            if (matrix[x][y] == target) {
                return true;
            }
            if (matrix[x][y] > target) {
                y--;
            } else {
                x++;
            }
        }
        return false;
    }

    //33 搜索旋转数组
    public int search(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            int mid = l + r >> 1;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[l] <= nums[mid]) {
                if (nums[l] <= target && nums[mid] > target) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            } else {
                if (nums[mid] < target && nums[r] >= target) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
        }
        return -1;
    }

    //分治
    public int searchDivideConquer(int[] nums, int target) {
        if (nums.length == 0) {
            return -1;
        }
        if (nums.length == 1) {
            return nums[0] == target ? 0 : -1;
        }
        return searchlr(nums, 0, nums.length - 1, target);

    }

    private int searchlr(int[] nums, int left, int right, int target) {
        if (left > right) {
            return -1;
        }

        int mid = (right + left) / 2;
        if (nums[mid] == target) {
            return mid;
        }

        if (nums[left] <= nums[mid]) {//left有序
            if (nums[left] <= target && target < nums[mid]) {
                return searchlr(nums, left, mid - 1, target);
            } else {
                return searchlr(nums, mid + 1, right, target);
            }
        } else {//right有序
            if (nums[mid] < target && target <= nums[right]) {
                return searchlr(nums, mid + 1, right, target);
            } else {
                return searchlr(nums, left, mid - 1, target);
            }
        }
    }

    public int search33(int[] nums, int target) {
        int n = nums.length;
        if (n == 0) return -1;
        if (n == 1) return nums[0] == target ? 0 : -1;

        // 第一次「二分」：从中间开始找，找到满足 >=nums[0] 的分割点（旋转点）
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            // l向右收缩，找到>=nums[0]的最右的值，即是旋转点
            if (nums[mid] >= nums[0]) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }

        // 第二次「二分」：通过和 nums[0] 进行比较，得知 target 是在旋转点的左边还是右边
        if (target >= nums[0]) {
            l = 0;
        } else {
            l = l + 1;
            r = n - 1;
        }
        while (l < r) {
            int mid = l + r >> 1;
            if (nums[mid] >= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }

        return nums[r] == target ? r : -1;
    }


    //已知存在一个按非降序排列的整数数组 nums ，数组中的值不必互不相同。
//
// 在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了 旋转 ，使数组变为 [nums[k], nums
//[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]（下标 从 0 开始 计数）。例如， [0,1,
//2,4,4,4,5,6,6,7] 在下标 5 处经旋转后可能变为 [4,5,6,6,7,0,1,2,4,4] 。
// 给你 旋转后 的数组 nums 和一个整数 target ，请你编写一个函数来判断给定的目标值是否存在于数组中。如果 nums 中存在这个目标值 targ
//et ，则返回 true ，否则返回 false 。
//输入：nums = [2,5,6,0,0,1,2], target = 0
//输出：true
    public boolean search2(int[] nums, int target) {
        if (nums.length == 0) {
            return false;
        }
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) {
                return true;
            }
            if (nums[left] == nums[mid]) {
                left++;
            } else if (nums[mid] == nums[right]) {
                right--;
            } else if (nums[left] < nums[mid]) {
                if (target >= nums[left] && nums[mid] > target) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            } else if (nums[mid] < nums[right]) {
                if (target > nums[mid] && nums[right] >= target) {
                    left = mid + 1;
                } else {
                    right = mid;
                }

            }
        }
        return false;
    }

    //81 搜索旋转数组  带重复数据 [3,1,2,3,3,3,3,3]
    public boolean searchWithDuplicate(int[] nums, int target) {
        if (nums.length == 0) return false;
        if (nums.length == 1) return nums[0] == target;
        return searchWithDuplicate(nums, target, 0, nums.length - 1);
    }

    private boolean searchWithDuplicate(int[] nums, int target, int left, int right) {
        if (right < left) {
            return false;
        }
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) {
                return true;
            }
            //2 2 5 0 2 2 2 2 2
            if (nums[mid] == nums[left] && nums[mid] == nums[right]) {
                left++;
                right--;
            } else if (nums[mid] >= nums[left]) {
                if (target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (target > nums[mid] && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return false;
    }

    public boolean search81(int[] nums, int target) {
        if (nums.length == 0) return false;
        if (nums.length == 1) return nums[0] == target;
        int l = 0, r = nums.length - 1;
        while (l < r && nums[r] == nums[l]) {
            r--;
        }
        int end = r;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (nums[mid] >= nums[0]) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        if (target >= nums[0]) {
            l = 0;
        } else {
            l = l + 1;
            r = end;
        }
        while (l < r) {
            int mid = l + r >> 1;
            if (nums[mid] >= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return nums[r] == target;
    }

    //153 寻找旋转排序数组中的最小值
    public int findMin(int[] nums) {
        return findMin(nums, 0, nums.length - 1);
    }

    private int findMin(int[] nums, int left, int right) {
        if (left >= right) {
            return nums[left];
        }
        int mid = (left + right) / 2;
        if (nums[mid] > nums[mid + 1]) {
            return nums[mid + 1];
        }
        int leftMin = findMin(nums, left, mid);
        int rightMin = findMin(nums, mid + 1, right);
        return Math.min(leftMin, rightMin);
    }

    public int findMin2(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] > nums[mid + 1]) {
                return nums[mid + 1];
            }
            //left要变化，所以判mid+1和right的关系
            if (nums[mid + 1] > nums[right]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return nums[left];
    }

    public int findMin3(int[] nums) {
        int n = nums.length;
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r >> 1;
            //找小于nums[r]最远的数
            if (nums[mid] <= nums[r]) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return nums[l];
    }

    public int findMin4(int[] nums) {
        int n = nums.length;
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            // 找大于等于nums[0]最远的数
            if (nums[mid] >= nums[0]) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return nums[(l + 1) % n];
    }

    // 154 寻找旋转排序数组中的最小值
    // 数组可重复。
//输入：nums = [2,2,2,0,1]
//输出：0
    public int findMinWithDupi(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else if (nums[mid] < nums[right]) {
                right = mid;
            } else {
                right--;
            }
        }
        return nums[left];
    }

    public int findMinWithDupi2(int[] nums) {
        int n = nums.length;
        int l = 0, r = n - 1;
        while (l < r && nums[l] == nums[l + 1]) {
            l++;
        }
        while (l < r && nums[r] == nums[r - 1]) {
            r--;
        }
        while (l < r) {
            int mid = l + r >> 1;
            if (nums[mid] <= nums[r]) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return nums[l];
    }

    public int findMinWithDupi3(int[] numbers) {
        int n = numbers.length;
        int l = 0, r = n - 1;
        while (r >= 0 && numbers[l] == numbers[r]) r--;
        while (l < r) {
            int mid = l + r >> 1;
            if (numbers[mid] <= numbers[r]) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return numbers[l];
    }

    //搜索旋转数组。给定一个排序后的数组，包含n个整数，但这个数组已被旋转过很多次了，次数不详。请编写代码找出数组中的某个元素，假设数组元素原先是按升序排列的。若
//有多个相同元素，返回索引值最小的一个。
//  输入: arr = [15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14], target = 5
// 输出: 8（元素5在该数组中的索引）
    //搜索旋转数组最左边索引
    public int searchLeftIndex(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            //最左相等直接返回
            if (arr[left] == target) {
                return left;
            }
            //中间相等 右指针指向中间，继续搜左边
            if (arr[mid] == target) {
                right = mid;
            } else if (arr[left] < arr[mid]) {
                if (target >= arr[left] && target < arr[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else if (arr[left] > arr[mid]) {
                if (target > arr[mid] && target <= arr[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else {
                //最左和中间相等，左加一继续搜
                left++;
            }
        }
        return -1;
    }


    public int searchTwoBinarySearch(int[] nums, int target) {
        int i = 0, j = nums.length - 1;
        //找右边界
        while (i <= j) { //相等时进入，保证右边界>target
            int mid = (i + j) / 2;
            if (nums[mid] <= target) i = mid + 1; //相等继续向右探索，直到>target
            else j = mid - 1;
        }
        int right = i;
        if (j > 0 && nums[j] != target) return 0;
        //重置，找左边界
        i = 0;
        j = nums.length - 1;
        while (i <= j) {
            int mid = (i + j) / 2;
            if (nums[mid] < target) i = mid + 1;
            else j = mid - 1;
        }
        int left = j;
        return right - left - 1;
    }

    // 162 寻找峰值
    //峰值元素是指其值严格大于左右相邻值的元素。
// 给你一个整数数组 nums，找到峰值元素并返回其索引。数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。
// 你可以假设 nums[-1] = nums[n] = -∞ 。
    public int findPeakElement(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int mid = l + r >> 1;
            // mid严格小于mid+1的时候往后找
            // 大于等于nums[mid+1]最近的值，每次mid+1都在变，每次往后找大于mid+1的值
            // 如果到了边界就取边界，如果有拐点，就能找到大于等于nums[mid+1]最近的值
            if (nums[mid] < nums[mid + 1]) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    public int findPeakElement2(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (nums[mid - 1] <= nums[mid]) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return l;
    }

    // 852 山峰数组的封顶索引
    public int peakIndexInMountainArray(int[] arr) {
        int l = 0, r = arr.length - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (arr[mid] > arr[mid + 1]) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    public int findInMountainArray(int target, MountainArray mountainArr) {
        int l = 0, r = mountainArr.length() - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (mountainArr.get(mid) < mountainArr.get(mid + 1)) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        int peak = l;
        l = 0;
        r = peak;
        while (l < r) {
            int mid = l + r >> 1;
            if (mountainArr.get(mid) >= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        if (mountainArr.get(l) == target) return l;
        l = peak + 1;
        r = mountainArr.length() - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (mountainArr.get(mid) <= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return mountainArr.get(l) == target ? l : -1;
    }

    // 面试10.05 稀疏数组搜索
    //稀疏数组搜索。有个排好序的字符串数组，其中散布着一些空字符串，编写一种方法，找出给定字符串的位置。
    public int findString1(String[] words, String s) {
        int n = words.length;
        int l = 0, r = n - 1;
        while (l < r) {
            while (l < r && words[l].length() == 0) l++;
            while (l < r && words[r].length() == 0) r--;
            int mid = l + ((r - l) >> 1);
            // l是变动的，mid向l收缩；否则mid收缩到r，下一轮若r=mid r不变mid不变死循环
            while (mid >= l && words[mid].length() == 0) mid--;
            if (s.compareTo(words[mid]) > 0) {
                l = mid + 1;
            } else if (s.compareTo(words[mid]) < 0) {
                r = mid;
            } else {
                return mid;
            }
        }
        return s.equals(words[l]) ? l : -1;
    }

    public int findString2(String[] words, String s) {
        int n = words.length;
        int l = 0, r = n - 1;
        while (l <= r) {
            while (l <= r && words[l].length() == 0) l++;
            while (l <= r && words[r].length() == 0) r--;
            int mid = l + ((r - l) >> 1);
            while (mid >= l && words[mid].length() == 0) mid--;
            //while (mid <= r && words[mid].length() == 0) mid++;
            if (s.compareTo(words[mid]) > 0) {
                l = mid + 1;
            } else if (s.compareTo(words[mid]) < 0) {
                r = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    // 274 H指数
    //引用次数至少为 x 次的 x 篇论文
    public int hIndex(int[] cs) {
        int n = cs.length;
        int l = 0, r = n;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            // 大于等于mid篇的数量最远的数
            if (check(cs, mid)) l = mid;
            else r = mid - 1;
        }
        return r;
    }

    boolean check(int[] cs, int mid) {
        int ans = 0;
        for (int i : cs) if (i >= mid) ans++;
        return ans >= mid;
    }

    // // 275 H指数 数组有序
    public int hIndex2(int[] cs) {
        int n = cs.length;
        int l = 0, r = n - 1;
        //寻找引用次数至少为 x 次的 x 篇论文
        while (l < r) {
            int mid = l + r >> 1;
            // n-mid即为大于等于mid的个数，坐标越小个数越多
            if (cs[mid] >= n - mid) r = mid;
            else l = mid + 1;
        }
        return cs[r] >= n - r ? n - r : 0;
    }

    // 475 供暖气
    // 暴力遍历
    public int findRadius(int[] houses, int[] heaters) {
        int min = Integer.MIN_VALUE;
        for (int house : houses) {
            int dist = Integer.MAX_VALUE;
            for (int heater : heaters) {
                dist = Math.min(dist, Math.abs(heater - house));
            }
            min = Math.max(min, dist);
        }
        return min;
    }

    // 二分
    public int findRadiusBinarySearch(int[] houses, int[] heaters) {
        int min = Integer.MIN_VALUE;
        int dist;

        Arrays.sort(heaters);
        //对于每个房屋，要么用前面的暖气，要么用后面的，二者取近的，得到距离
        //排序后求出离每个房屋最近的两个暖气
        for (int house : houses) {
            if (house <= heaters[0]) {
                dist = heaters[0] - house;
            } else if (house >= heaters[heaters.length - 1]) {
                dist = house - heaters[heaters.length - 1];
            } else {
                //找到小于house的最大的heater
                int l = 0, r = heaters.length - 1;
                while (l < r) {
                    // mid取中间后一个
                    int mid = (l + r + 1) / 2;
                    //严格大于时往前一个的范围找
                    if (heaters[mid] > house) {
                        r = mid - 1;
                    } else {
                        l = mid;
                    }
                }
//              找大于target的最小的坐标的模板
//                int l = 0, r = heaters.length - 1;
//                while (l < r) {
//                    int mid = (l + r) / 2;
//                    if (heaters[mid] < target) {
//                        l = mid + 1;
//                    } else {
//                        r = mid;
//                    }
//                }
//                return l;
                dist = Math.min(house - heaters[l], heaters[l + 1] - house);
            }
            min = Math.max(min, dist);
        }
        return min;
    }

    // 双指针
    public int findRadiusDualPointer(int[] houses, int[] heaters) {
        int min = Integer.MIN_VALUE;
        int dist;
        //不排序houses，对每个house从0开始找heaters也可以，但是这样等于暴力遍历
        //排序house后，下一个house可以服复用前一个j的值，j不是从0开始，提升了效率
        Arrays.sort(houses);
        Arrays.sort(heaters);
        for (int i = 0, j = 0; i < houses.length; i++) {
            while (j < heaters.length && heaters[j] < houses[i]) j++;
            if (j == 0) {
                dist = heaters[0] - houses[i];
            } else if (j == heaters.length) {
                dist = houses[i] - heaters[heaters.length - 1];
            } else {
                dist = Math.min(houses[i] - heaters[j - 1], heaters[j] - houses[i]);
            }
            min = Math.max(dist, min);
        }
        return min;
    }

    // 540 有序数组中的单一元素
    //给你一个仅由整数组成的有序数组，其中每个元素都会出现两次，唯有一个数只会出现一次。
// 请你找出并返回只出现一次的那个数。
// 你设计的解决方案必须满足 O(log n) 时间复杂度和 O(1) 空间复杂度。
    //输入: nums = [1,1,2,3,3,4,4,8,8] 输出: 2
    // O(n)
    public int singleNonDuplicate(int[] nums) {
        int i = 0;
        while (i < nums.length - 1) {
            if (nums[i + 1] - nums[i] != 0) {
                return nums[i];
            }
            i += 2;
            if (i >= nums.length - 1) return nums[i];
        }
        return nums[0];

    }

    //O(logN)
    public int singleNonDuplicateBinarySearch(int[] nums) {
        int n = nums.length;
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (mid % 2 == 0) {
                if (mid + 1 < n && nums[mid] == nums[mid + 1]) l = mid + 1;
                else r = mid;
            } else {
                if (mid - 1 >= 0 && nums[mid - 1] == nums[mid]) l = mid + 1;
                else r = mid;
            }
        }
        return nums[r];
    }


    // 875 爱吃香蕉的珂珂
    public int minEatingSpeed(int[] piles, int h) {
        int low = 1;
        int high = 0;
        for (int pile : piles) {
            high = Math.max(high, pile);
        }
        while (low < high) {
            int mid = (low + high) / 2;
            // mid速率的时间严格大于h时,提高速率
            if (getTime(piles, mid) > h) {
                low = mid + 1;
            } else {
                // 小于等于h时,求最小的速率
                high = mid;
            }
        }
        return low;
    }

    private int getTime(int[] piles, int speed) {
        int time = 0;
        for (int pile : piles) {
            // 向上取整
            time += (pile + speed - 1) / speed;
        }
        return time;
    }

    public int shipWithinDays(int[] weights, int days) {
        int n = weights.length;
        int sum = 0, max = 0;
        for (int w : weights) {
            sum += w;
            max = Math.max(w, max);
        }
        int l = Math.max(max, (sum + days - 1) / days), r = sum;
        while (l < r) {
            int mid = l + r >> 1;
            // 当前load需要的天数 往days收缩
            if (getLoadDays(mid, weights) <= days) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    private int getLoadDays(int load, int[] weights) {
        int days = 0;
        int diff = load;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] <= diff) {
                diff -= weights[i];
            } else {
                days++;
                diff = load;
                i--;
            }
        }
        return days + 1;
    }

    // 410 分割数组的最大值
    // DP做法搜splitArrayDP
    public int splitArray(int[] nums, int m) {
        int sum = 0, max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        int l = max, r = sum;
        while (l < r) {
            int mid = l + r >> 1;
            int cnt = getCnt(nums, mid);
            // [l,r]和mid对应的是和，要使和尽可能小，在满足条件的情况下向左收缩
            if (cnt <= m) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    private int getCnt(int[] nums, int x) {
        int sum = 0, cnt = 1;
        for (int num : nums) {
            if (sum + num > x) {
                cnt++;
                sum = num;
            } else {
                sum += num;
            }
        }
        return cnt;
    }

    // 1539 第k个缺失的正整数
    // 第i位缺失的个数是arr[i]-(i+1)
    // 找到小于k的最大的坐标l(往左压缩至小于k)
    // 那么第k个就是k-l缺失的个数+arr[l]
    public int findKthPositive(int[] arr, int k) {
        if (arr[0] > k) {
            return k;
        }
        int n = arr.length;
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            int x = mid < n ? arr[mid] : Integer.MAX_VALUE;
            if (arr[mid] - (mid + 1) < k) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return k - (arr[l] - (l + 1)) + arr[l];
    }

    // 1608 特殊数组的特征值
    //给你一个非负整数数组 nums 。如果存在一个数 x ，使得 nums 中恰好有 x 个元素 大于或者等于 x ，那么就称 nums 是一个 特殊数组 ，而x 是该数组的 特征值 。
// 注意： x 不必 是 nums 的中的元素。
// 如果数组 nums 是一个 特殊数组 ，请返回它的特征值 x 。否则，返回 -1 。可以证明的是，如果 nums 是特殊数组，那么其特征值 x 是 唯一的
// 输入：nums = [3,5]
//输出：2
//解释：有 2 个元素（3 和 5）大于或等于 2 。
    public int specialArray(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int l = 0, r = nums[n - 1];
        while (l < r) {
            int mid = l + r >> 1;
            int cnt = getCnt(mid, nums);
            // 大于mid的数量太多
            if (cnt > mid) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return getCnt(l, nums) == l ? l : -1;
    }

    private int getCnt(int x, int[] nums) {
        int n = nums.length;
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (nums[mid] >= x) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return n - r;
    }

    // 6245 找出中枢整数
    public int pivotInteger(int n) {
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + i;
        }
        int l = 1, r = n;
        while (l < r) {
            int mid = l + r >> 1;
            if (sum[n] <= sum[mid] + sum[mid - 1]) {
                r = mid;
            } else {
                l = mid + 1;
            }
//            int mid = l + r+1 >> 1;
//            if (sum[n] >= sum[mid] + sum[mid - 1]) {
//                l = mid;
//            } else {
//                r = mid - 1;
//            }
        }
        return sum[l] + sum[l - 1] == sum[n] ? l : -1;
    }

    // 373 查找和最小的K对数字
    // 优先队列查kSmallestPairs
    public List<List<Integer>> kSmallestPairsBinarySearch(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> result = new ArrayList<>();
        int l = nums1[0] + nums2[0], r = nums1[nums1.length - 1] + nums2[nums2.length - 1];
        while (l < r) {
            int mid = (int) ((long) l + r >> 1);
            int cnt = getCnt(nums1, nums2, mid, k);
            //个数大于等于k的最小的和
            if (cnt < k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        int value = l;
        //把所有小于 最小和(此和是第k或k+i小的值，第k小的一定在小于value的里面)的组合先添加到result
        for (int n1 : nums1) {
            for (int n2 : nums2) {
                if (n1 + n2 < value) {
                    result.add(Arrays.asList(n1, n2));
                } else {
                    break;
                }
            }
        }
        // 此时result的数量可能大于k，需要选k个
        // eg 和的数组是[1,2,3,3,3,4]，求第4小，第四小的3有3个，求最左最右坐标加到result.size=k为止
        for (int i = 0; i < nums1.length && result.size() < k; i++) {
            int target = value - nums1[i];

            int left = 0, right = nums2.length - 1;
            while (left < right) {
                int mid = (int) ((long) left + right) >> 1;
                // 大于等于target的最小的nums2的值
                if (nums2[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            int x = left;
            if (nums2[x] != target) continue;
            left = 0;
            right = nums2.length - 1;
            while (left < right) {
                int mid = (int) ((long) left + right + 1) >> 1;
                //小于等于target的最大的值
                if (nums2[mid] > target) {
                    right = mid - 1;
                } else {
                    left = mid;
                }
            }
            int y = left;
            for (int p = x; p <= y && result.size() < k; p++) {
                result.add(Arrays.asList(nums1[i], nums2[p]));
            }
        }
        return result;
    }

    private int getCnt(int[] nums1, int[] nums2, int mid, int k) {
        int cnt = 0;
        for (int i = 0; i < nums1.length && cnt < k; i++) {
            for (int j = 0; j < nums2.length && cnt < k; j++) {
                if (nums1[i] + nums2[j] <= mid) cnt++;
                else break;
            }
        }
        return cnt;
    }

    //378有序矩阵中第K小的元素
    // 优先队列查kthSmallest
    public int kthSmallestBinarySearch(int[][] matrix, int k) {
        int n = matrix.length;
        int l = matrix[0][0], r = matrix[n - 1][n - 1];
        while (l < r) {
            int mid = (l + r) >> 1;
            // 数量大于等于k的最小的值，小于这个值的数量不足k个
            int cnt = getCnt(matrix, mid);
            if (cnt < k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    private int getCnt(int[][] matrix, int mid) {
        int n = matrix.length;
        int i = n - 1, j = 0;
        int cnt = 0;
        while (i >= 0 && j < n) {
            if (matrix[i][j] <= mid) {
                cnt += (i + 1);
                j++;
            } else {
                i--;
            }
        }
        return cnt;
    }

    // 658 找到K个最接近的元素
    public List<Integer> findClosestElementsPriorityQueue(int[] arr, int k, int x) {
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>((o1, o2) -> Math.abs(o1[0] - x) - Math.abs(o2[0] - x) == 0 ? o1[1] - o2[1] : Math.abs(o1[0] - x) - Math.abs(o2[0] - x));
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            priorityQueue.offer(new int[]{arr[i], i});
        }
        while (k-- > 0 && !priorityQueue.isEmpty()) {
            result.add(priorityQueue.poll()[0]);
        }
        result.sort(Comparator.comparingInt(o -> o));
        return result;
    }

    public List<Integer> findClosestElementsSort(int[] arr, int k, int x) {
        int l = Integer.MAX_VALUE, r = Integer.MIN_VALUE;
        for (int value : arr) {
            l = Math.min(l, Math.abs(value - x));
            r = Math.max(r, Math.abs(value - x));
        }
        while (l < r) {
            int mid = l + r >> 1;
            //cnt arr中满足和x的差值<=mid的个数
            int cnt = getCnt(arr, x, mid);
            // 例如 arr[3 4 5] 和x的差值  个数排列是[1 2 2]
            //那么我们需要 数量为k 当中最小的差值，l就是差值
            //diff 1 2 3
            //cnt  2 4 4  k=3 diff=2就是cnt大于等于k的最小差值
            if (cnt < k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        int close = l;
        List<Integer> result = new ArrayList<>();
        for (int value : arr) {
            if (Math.abs(value - x) < close) result.add(value);
        }
        for (int i = 0; i < arr.length && result.size() < k; i++) {
            if (Math.abs(arr[i] - x) == close) result.add(arr[i]);
        }
        result.sort(Comparator.comparingInt(o -> o));
        return result;
    }


    private int getCnt(int[] arr, int x, int target) {
        int cnt = 0;
        for (int value : arr) {
            if (Math.abs(value - x) <= target) cnt++;
        }
        return cnt;
    }

    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        int l = 0, r = arr.length - 1;
        int removeNum = arr.length - k;
        while (removeNum-- > 0) {
            if (x - arr[l] > arr[r] - x) {
                l++;
            } else {
                r--;
            }
        }
        List<Integer> result = new ArrayList<>();
        for (int i = l; i < l + k; i++) {
            result.add(arr[i]);
        }
        return result;
    }

    public List<Integer> findClosestElementsBinarySearch(int[] arr, int k, int x) {
        int l = 0, r = arr.length - k;
        //查找与x距离最接近的左区间，相等时取小的，即向左收缩
        while (l < r) {
            int mid = l + r >> 1;
            if (x - arr[mid] > arr[mid + k] - x) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        List<Integer> result = new ArrayList<>();
        for (int i = l; i < l + k; i++) {
            result.add(arr[i]);
        }
        return result;
    }

    //668乘法表中第K小的数
    //PriorityQueue MLE
    public int findKthNumber(int m, int n, int k) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (priorityQueue.size() < k) {
                    priorityQueue.offer(i * j);
                } else if (i * j < priorityQueue.peek()) {
                    priorityQueue.poll();
                    priorityQueue.offer(i * j);
                }
            }
        }
        return priorityQueue.poll();
    }

    // 二分法
    public int findKthNumberBinarySearch(int m, int n, int k) {
        int l = 1, r = m * n;
        while (l < r) {
            int mid = (l + r) >> 1;
            //数量大于等于k的最小乘积
            // eg 乘积小于等于20的有3个，乘积小于等于25的有6个，求第5小
            int cnt = getCnt(m, n, mid);
            if (cnt >= k) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    private int getCnt(int m, int n, int x) {
        int count = x / n * n;
        for (int i = x / n + 1; i <= m; ++i) {
            count += x / i;
        }
        return count;
//        int res = 0;
//        // 统计每行小于等于 k 的数目
//        for (int i = 1; i <= m; ++i) {
//            res += Math.min(k / i, n);
//        }
//        return res;
    }

    // 878 第N个神奇数字
    public int nthMagicalNumber(int n, int a, int b) {
        int mod = (int) 1e9 + 7;
        long l = Math.min(a, b);
        long r = (long) n * Math.min(a, b);
        long c = lcm(a, b);
        while (l < r) {
            long mid = l + r >> 1;
            if (getCnt(mid, a, b, c) < n) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return (int) (l % mod);
    }

    private long getCnt(long x, int a, int b, long c) {
        return x / a + x / b - x / c;
    }

    //719 找出第K小的数对距离
    public int smallestDistancePair(int[] nums, int k) {
        Arrays.sort(nums);
        int l = 0, r = nums[nums.length - 1] - nums[0];
        //计划查找 >=k的最小值
        //第k个ak 右侧的值都>=k，所以即找>=k的最小的值
        while (l < r) {
            int mid = (l + r) >> 1;
            // cnt <=mid的个数
            int cnt = 0;
            for (int j = 0; j < nums.length; j++) {
                int i = binarySearch(nums, j, nums[j] - mid);
                //[i,j]一共j-i+1个数，一共j-i对儿
                cnt += j - i;
            }
            if (cnt < k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    //查找>=target中最小的坐标，[i,j]满足距离<= mid
    private int binarySearch(int[] nums, int end, int target) {
        int l = 0, r = end;
        while (l < r) {
            int mid = (l + r) >> 1;
            if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    // 双指针
    public int smallestDistancePairDualPointer(int[] nums, int k) {
        Arrays.sort(nums);
        int l = 0, r = nums[nums.length - 1] - nums[0];
        //计划查找<=k的最大值 />=k的最小值
        while (l < r) {
            int mid = (l + r) >> 1;
            // cnt <=mid的个数
            int cnt = 0;
            for (int i = 0, j = 0; j < nums.length; j++) {
                //i跟j都大于mid，跟j后面的距离更大，i往后移
                while (nums[j] - nums[i] > mid) i++;
                cnt += j - i;
            }
            if (cnt < k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    // 1175 质数排列
    int mod = (int) 1e9 + 7;

    public int numPrimeArrangements(int n) {
        int cnt = 0;
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                cnt++;
            }
        }
        return (int) (factorial(cnt) * factorial(n - cnt) % mod);
    }

    // 技巧 判断是否是质数
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

    private long factorial(int n) {
        long ans = 1;
        for (int i = 1; i <= n; i++) {
            ans *= i;
            ans %= mod;
        }
        return ans;
    }

    // 打表+二分做法
    public int numPrimeArrangementsBinarySearch(int n) {
        List<Integer> primeList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            if (isPrime(i)) primeList.add(i);
        }
        int l = 0, r = primeList.size() - 1;
        //找到第一个小于等于n的质数的坐标，+1即为小于等于n的质数个数
        while (l < r) {
            int mid = (l + r + 1) >> 1;
            if (primeList.get(mid) <= n) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        int idx = l;
        int cnt = idx + 1;

        return (int) (factorial(cnt) * factorial(n - cnt) % mod);
    }

    // 786 第K小的素数分数
    // 自定义排序
    public int[] kthSmallestPrimeFraction(int[] arr, int k) {
        int n = arr.length;
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                result.add(new int[]{arr[i], arr[j]});
            }
        }
        result.sort((o1, o2) -> o1[0] * o2[1] - o2[0] * o1[1]);
        return result.get(k - 1);
    }

    //多路归并
    public int[] kthSmallestPrimeFractionPriorityQueue(int[] arr, int k) {
        int n = arr.length;
        PriorityQueue<int[]> queue = new PriorityQueue<>((o1, o2) -> arr[o1[0]] * arr[o2[1]] - arr[o1[1]] * arr[o2[0]]);
        for (int i = 1; i < n; i++) {
            queue.offer(new int[]{0, i});
        }
        //arr = [1,2,3,5], k = 3
        while (--k > 0) {
            int[] min = queue.poll();
            int x = min[0], y = min[1];
            if (x + 1 < y) {
                queue.offer(new int[]{x + 1, y});
            }
        }
        return new int[]{arr[queue.peek()[0]], arr[queue.peek()[1]]};
    }

    // 二分
    int a, b;

    public int[] kthSmallestPrimeFractionBinarySearch(int[] arr, int k) {
        double eps = 1e-8;
        double l = 0, r = 1;
        while (r - l > eps) {
            double mid = (l + r) / 2;
            // 大于等于k个的最小值，第k个就是该值
            if (check(arr, mid) >= k) r = mid;
            else l = mid;
        }
        return new int[]{a, b};
    }

    int check(int[] arr, double x) {
        double eps = 1e-8;
        int ans = 0;
        for (int i = 0, j = 1; j < arr.length; j++) {
            while (arr[i + 1] * 1.0 / arr[j] <= x) i++;
            if (arr[i] * 1.0 / arr[j] <= x) ans += i + 1;
            if (Math.abs(arr[i] * 1.0 / arr[j] - x) < eps) {
                a = arr[i];
                b = arr[j];
            }
        }
        return ans;
    }

    // 1818 绝对差值和
    public int minAbsoluteSumDiff(int[] nums1, int[] nums2) {
        int mod = (int) 1e9 + 7;
        int n = nums1.length;
        int[] sorted = nums1.clone();
        Arrays.sort(sorted);
        long sum = 0, maxDiff = 0;
        for (int i = 0; i < n; i++) {
            int x = Math.abs(nums1[i] - nums2[i]);
            if (x == 0) continue;
            sum += x;
            int l = 0, r = n - 1;
            while (l < r) {
                int mid = l + r >> 1;
                if (sorted[mid] >= nums2[i]) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            int newAbs = Math.abs(sorted[l] - nums2[i]);
            if (l > 0) newAbs = Math.min(newAbs, Math.abs(sorted[l - 1] - nums2[i]));
            if (newAbs < x) maxDiff = Math.max(maxDiff, x - newAbs);
        }
        return (int) ((sum - maxDiff) % mod);
    }

    // 1894 找到需要补充粉笔的学生编号
    public int chalkReplacer(int[] chalk, int k) {
        int n = chalk.length;
        if (chalk[0] > k) {
            return 0;
        }
        int[] preSum = new int[n];
        preSum[0] = chalk[0];
        for (int i = 1; i < n; i++) {
            preSum[i] = preSum[i - 1] + chalk[i];
            if (preSum[i] > k) {
                return i;
            }
        }
        k = k % preSum[n - 1];
        // 二分找到第一个比k大的前缀和
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = (l + r) >> 1;
            if (preSum[mid] <= k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    public int chalkReplacer2(int[] chalk, int k) {
        int n = chalk.length;
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + chalk[i - 1];
            if (sum[i] > k) return i - 1;
        }
        k %= sum[n];
        int l = 0, r = n;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            //<=k的最大的数
            if (sum[mid] <= k) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        // l>0?l-1+1  -1对应chalk的坐标，+1找下一个，如果l是0 没有比k小的，就取第一个;如果是n也取第一个 %n
        return l % n;
    }

    // 2055 蜡烛之间的盘子
    // 前缀和做法搜platesBetweenCandles
    public int[] platesBetweenCandles(String s, int[][] queries) {
        int[] ans = new int[queries.length];
        int n = s.length();
        int[] preSum = new int[n];
        int sum = 0;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '|') {
                list.add(i);
            }
            if (s.charAt(i) == '*') {
                sum++;
            }
            preSum[i] = sum;
        }
        if (list.size() == 0) return ans;
        for (int i = 0; i < queries.length; i++) {
            int a = queries[i][0], b = queries[i][1];
            int c = -1, d = -1;
            int l = 0, r = list.size() - 1;
            // 找到 a 右边最近的蜡烛
            while (l < r) {
                int mid = (l + r) >> 1;
                if (list.get(mid) >= a) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            if (list.get(r) >= a) c = list.get(r);
            else continue;
            // 找到 b 左边最近的蜡烛
            l = 0;
            r = list.size() - 1;
            while (l < r) {
                int mid = (l + r + 1) >> 1;
                if (list.get(mid) <= b) {
                    l = mid;
                } else {
                    r = mid - 1;
                }
            }
            if (list.get(r) <= b) d = list.get(r);
            else continue;
            if (c < d) ans[i] = preSum[d] - preSum[c];
        }
        return ans;
    }

    // 6133 分组的最大数量
    public int maximumGroups(int[] grades) {
        int n = grades.length;
        long l = 1, r = n;
        while (l < r) {
            long mid = l + r + 1 >> 1;
            //分成m组至少需要mid * (mid + 1) / 2 个人
            if (mid * (mid + 1) / 2 <= n) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return (int) l;
    }

    //endregion-------------------------------------------------------------------------------------------
}
