import java.util.*;

public class SolutionStackQueue {
    //region ----------------------------------栈模拟------------------------------------
    // 逆波兰表达式的值。
    public int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        List<String> operations = Arrays.asList("+", "-", "*", "/");
        for (String s : tokens) {
            if (operations.contains(s)) {
                Integer top = stack.pop();
                Integer top2 = stack.pop();
                switch (s) {
                    case "+":
                        stack.push(top + top2);
                        break;
                    case "-":
                        stack.push(top2 - top);
                        break;
                    case "*":
                        stack.push(top * top2);
                        break;
                    case "/":
                        stack.push(top2 / top);
                        break;
                }
            } else {
                stack.push(Integer.valueOf(s));
            }
        }
        return stack.pop();
    }

    public int compareVersion(String version1, String version2) {
        String[] array1 = version1.split("\\.");
        String[] array2 = version2.split("\\.");

        for (int i = 0; i < array1.length || i < array2.length; i++) {
            int x = 0;
            int y = 0;
            if (i < array1.length) {
                x = Integer.valueOf(array1[i]);
            }
            if (i < array2.length) {
                y = Integer.valueOf(array2[i]);
            }

            if (x < y) {
                return -1;
            }
            if (x > y) {
                return 1;
            }
        }
        return 0;
    }

    public int compareVersion2(String version1, String version2) {
        int v1 = 0, v2 = 0;
        while (v1 < version1.length() || v2 < version2.length()) {
            int x = 0;
            while (v1 < version1.length() && version1.charAt(v1) != '.') {
                x = x * 10 + version1.charAt(v1) - '0';
                v1++;
            }
            v1++;
            int y = 0;
            while (v2 < version2.length() && version2.charAt(v2) != '.') {
                y = y * 10 + version2.charAt(v2) - '0';
                v2++;
            }
            v2++;
            if (x != y) {
                return x > y ? 1 : -1;
            }
        }
        return 0;
    }

    //1003. 检查替换后的词是否有效
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == 'a' || c == 'b') {
                stack.push(c);
            } else if (stack.size() >= 2 && stack.peek() == 'b') {
                char b = stack.pop();
                if (stack.peek() == 'a') {
                    stack.pop();
                } else {
                    stack.push(b);
                }
            } else {
                return false;
            }
        }
        return stack.isEmpty();
    }

    public int calPoints(String[] ops) {
        Stack<Integer> stack = new Stack<>();

        for (String op : ops) {
            switch (op) {
                case "+":
                    int top = stack.pop();
                    int newtop = top + stack.peek();
                    stack.push(top);
                    stack.push(newtop);
                    break;
                case "C":
                    stack.pop();
                    break;
                case "D":
                    stack.push(2 * stack.peek());
                    break;
                default:
                    stack.push(Integer.valueOf(op));
                    break;
            }
        }

        int ans = 0;
        for (int score : stack) ans += score;
        return ans;
    }

    // 1441 用栈操作构建数组
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

    //2696. 删除子串后的字符串最小长度
    public int minLength(String s) {
        Stack<Character> stack = new Stack<>();
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (c == 'B') {
                if (!stack.isEmpty() && stack.peek() == 'A') {
                    stack.pop();
                } else {
                    stack.push(c);
                }
            } else if (c == 'D') {
                if (!stack.isEmpty() && stack.peek() == 'C') {
                    stack.pop();
                } else {
                    stack.push(c);
                }
            } else {
                stack.push(c);
            }
        }
        return stack.size();
    }
//endregion-----------------------------------------------------------------
    int mod = (int) 1e9 + 7;
    //region-----------------------------------------------单调栈------------------------------
    //42 接雨水
    //给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
//输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
//输出：6
//解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。
    // 预处理
    public int trap(int[] height) {
        int[] leftMax = new int[height.length];
        int[] rightMax = new int[height.length];
        leftMax[0] = height[0];
        rightMax[height.length - 1] = height[height.length - 1];
        for (int i = 1; i < height.length; i++) {
            leftMax[i] = Math.max(height[i], leftMax[i - 1]);
        }
        for (int i = height.length - 2; i >= 0; i--) {
            rightMax[i] = Math.max(height[i], rightMax[i + 1]);
        }
        int ans = 0;
        for (int i = 0; i < height.length; i++) {
            int curr_i = Math.min(leftMax[i], rightMax[i]) - height[i];
            if (curr_i >= 0) ans += curr_i;
        }
        return ans;
    }

    public int trap2(int[] height) {
        int n = height.length;
        int ans = 0;
        for (int i = 1; i < n - 1; i++) {
            int cur = height[i];

            // 获取当前位置的左边最大值
            int l = Integer.MIN_VALUE;
            for (int j = i - 1; j >= 0; j--) l = Math.max(l, height[j]);
            if (l <= cur) continue;

            // 获取当前位置的右边边最大值
            int r = Integer.MIN_VALUE;
            for (int j = i + 1; j < n; j++) r = Math.max(r, height[j]);
            if (r <= cur) continue;

            // 计算当前位置可接的雨水
            ans += Math.min(l, r) - cur;
        }
        return ans;
    }

    // 单调队列  找i左边最高和右边最高 i处的储水量 = min(h[l],h[r])-h[i]
    public int trap3(int[] height) {
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
            right[i] = !deque.isEmpty() ? deque.peekFirst() : -1;
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

    //单调栈 找i左边第一个高，右边第一个高 统计高于i处的横向的储水量 = (min(h[l],h[r])-h[i]) * (r-l+1-2)
    public int trap4(int[] height) {
        int n = height.length;
        int[] left = new int[n];
        int[] right = new int[n];
        Arrays.fill(right, n);
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
                right[stack.pop()] = i;
            }
            left[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            int l = left[i], r = right[i];
            if (l == -1 || r == n) continue;
            int h = Math.min(height[l], height[r]) - height[i];
            ans += h * (r - l + 1 - 2);
        }
        return ans;
    }

    public int trap5(int[] height) {
        int n = height.length;
        int ans = 0;
        Deque<Integer> d = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!d.isEmpty() && height[i] > height[d.peekLast()]) {
                int cur = d.pollLast();

                // 如果栈内没有元素，说明当前位置左边没有比其高的柱子，跳过
                if (d.isEmpty()) continue;

                // 左右位置，并有左右位置得出「宽度」和「高度」
                int l = d.peekLast(), r = i;
                int w = r - l + 1 - 2;
                int h = Math.min(height[l], height[r]) - height[cur];
                ans += w * h;
            }
            d.addLast(i);
        }
        return ans;
    }

    //84 柱状图最大矩形
    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        int[] left = new int[n];
        int[] right = new int[n];
        Arrays.fill(right, n);//-1,[0,n-1],n
        // 单调递增栈
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                right[stack.peek()] = i;
                stack.pop();
            }
            left[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            // 右边第一个小的坐标-左边第一个小的坐标-1 =剔除两个小的坐标后余下的宽度（>=heights[i]的高度）
            ans = Math.max((right[i] - left[i] - 1) * heights[i], ans);
        }
        return ans;
    }

    // 85 最大矩形
    public int maximalRectangle(char[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;

        int[][] heights = new int[row][col];
        for (int j = 0; j < col; j++) {
            for (int i = 0; i < row; i++) {
                if (matrix[i][j] == '1') {
                    heights[i][j] += (i > 0 ? heights[i - 1][j] : 0) + 1;
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < row; i++) {
            Stack<Integer> stack = new Stack<>();
            int[] left = new int[col];
            int[] right = new int[col];
            Arrays.fill(right, col);
            for (int j = 0; j < col; j++) {
                while (!stack.isEmpty() && heights[i][stack.peek()] >= heights[i][j]) {
                    right[stack.peek()] = j;
                    stack.pop();
                }
                left[j] = stack.isEmpty() ? -1 : stack.peek();
                stack.push(j);
            }
            int max = 0;
            for (int j = 0; j < col; j++) {
                int width = right[j] - left[j] - 1;
                int area = width * heights[i][j];
                max = Math.max(max, area);
            }
            ans = Math.max(ans, max);
        }
        return ans;
    }

    // 1501 统计全1子矩形
    public int numSubmat(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        //预处理mat[i][j]上边有多少个连续的1
        int[][] upCnt = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 1) {
                    upCnt[i][j] = i == 0 ? mat[i][j] & 1 : upCnt[i - 1][j] + 1;
                }
            }
        }
        //单调递增栈维护列的长度
        Deque<Integer> stack = new ArrayDeque<>();
        int res = 0;
        for (int i = 0; i < m; i++) {
            stack.clear();
            int ijCnt = 0; //以i,j为右下角的矩形的cnt
            for (int j = 0; j < n; j++) {
                ijCnt += upCnt[i][j];
                while (!stack.isEmpty() && upCnt[i][stack.peek()] > upCnt[i][j]) {
                    int cur = stack.pop();
                    int left = stack.isEmpty() ? -1 : stack.peek();
                    //减去多的部分  左边比j大的k的宽度(和前一个比k小的)*差值
                    ijCnt -= (cur - left) * (upCnt[i][cur] - upCnt[i][j]);
                }
                stack.push(j);
                res += ijCnt;
            }
        }
        return res;
    }

    //1944. 队列中可以看到的人数
    public int[] canSeePersonsCount(int[] heights) {
        int n = heights.length;
        int[] ans = new int[n];
        Stack<Integer> stack = new Stack<>();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[i] > stack.peek()) {
                stack.pop();
                ans[i]++;
            }
            ans[i] = stack.isEmpty() ? ans[i] : ans[i] + 1;
            stack.push(heights[i]);
        }
        return ans;
    }

    // -------------------------小专题：乘法原理 贡献法
    // 795 区间子数组的个数
    public int numSubarrayBoundedMax(int[] nums, int left, int right) {
        int n = nums.length;
        int[] leftMax = new int[n];
        int[] rightMax = new int[n];
        Arrays.fill(rightMax, n);
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                rightMax[stack.pop()] = i;
            }
            leftMax[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] <= right && nums[i] >= left) {
                ans += (rightMax[i] - i) * (i - leftMax[i]);
            }
        }
        return ans;
    }

    // 双指针做法
    public int numSubarrayBoundedMaxDualPointer(int[] nums, int left, int right) {
        int n = nums.length;
        int last1 = -1, last2 = -1;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] >= left && nums[i] <= right) {
                last1 = i;
            } else if (nums[i] > right) {
                last2 = i;
                last1 = -1;
            }
            if (last1 != -1) {
                ans += last1 - last2;
            }
        }
        return ans;
    }

    //计数做法
    public int numSubarrayBoundedMaxCount(int[] nums, int left, int right) {
        return count(nums, right) - count(nums, left - 1);
    }

    // 求小于等于lower的子数组个数
    private int count(int[] nums, int lower) {
        int cnt = 0;
        int ans = 0;
        for (int num : nums) {
            // 小于等于lower的连续个数
            cnt = num <= lower ? cnt + 1 : 0;
            // 对于每一个新的右边界，对结果的贡献就是连续个数
            // eg： 0 0
            //      0(1)
            //      00(1)
            //       0(1)
            ans += cnt;
        }
        return ans;
    }

    // 828 统计字符串中的唯一字符 乘法原理 Hard
    public int uniqueLetterString(String s) {
        int[] idx = new int[26];
        char[] chars = s.toCharArray();
        int n = s.length();
        int[] left = new int[n];
        int[] right = new int[n];

        Arrays.fill(idx, -1);
        for (int i = 0; i < n; i++) {
            // 前一个相同字符的下标
            left[i] = idx[chars[i] - 'A'];
            // 当前字符的下标
            idx[chars[i] - 'A'] = i;
        }
        Arrays.fill(idx, n);
        for (int i = n - 1; i >= 0; i--) {
            // 后一个相同字符的下标
            right[i] = idx[chars[i] - 'A'];
            // 当前字符的下标
            idx[chars[i] - 'A'] = i;
        }
        int ans = 0;
        // 1 2 i 4 5
        //12i45  2i45  i45
        //12i4 2i4 i4
        //12i 2i i
        for (int i = 0; i < n; i++) {
            int leftSize = i - left[i];
            int rightSize = right[i] - i;
            ans += leftSize * rightSize;
        }
        return ans;
    }

    // 1856 子数组最小乘积的最大值
    public int maxSumMinProduct(int[] nums) {
        int mod = (int) 1e9 + 7;
        int n = nums.length;
        long[] sum = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + nums[i - 1];
        }
        // 左右第一个比i小的
        int[] left = new int[n];
        int[] right = new int[n];
        Arrays.fill(right, n);
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
                right[stack.pop()] = i;
            }
            left[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }
        long max = 0;
        // i左边第一个比它小的+1 即是都比i大的
        for (int i = 0; i < n; i++) {
            int l = left[i] + 1;
            int r = right[i] - 1;
            long t = sum[r + 1] - sum[l];
            max = Math.max(max, t * nums[i]);
        }
        return (int) (max % mod);
    }

    // 907 子数组的最小值之和
    // 单调栈+乘法原理
    public int sumSubarrayMins(int[] arr) {
        int n = arr.length;
        int mod = (int) 1e9 + 7;
        // 左右小于i的第一个坐标
        int[] left = new int[n];
        int[] right = new int[n];
        Arrays.fill(right, n);
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                right[stack.pop()] = i;
            }
            left[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }
        long ans = 0;
        //对于每个i 作为最小值对所在子数组的贡献
        for (int i = 0; i < n; i++) {
            int leftSize = i - left[i];
            int rightSize = right[i] - i;
            ans += ((((long) leftSize * rightSize) % mod) * arr[i]) % mod;
            ans %= mod;
        }
        return (int) ans;
    }


    // 2104 子数组范围和
    public long subArrayRanges(int[] nums) {
        int n = nums.length;
        long ans = 0;
        for (int i = 0; i < n - 1; i++) {
            int min = nums[i], max = nums[i];
            for (int j = i + 1; j < n; j++) {
                max = Math.max(max, nums[j]);
                min = Math.min(min, nums[j]);
                ans += max - min;
            }
        }
        return ans;
    }

    public long subArrayRangesStack(int[] nums) {
        int n = nums.length;
        // nums[i]作为区间最小值，两边的边界
        int[] minLeft = new int[n];
        int[] minRight = new int[n];
        //单调递增，找到nums[i]两边比nums[i]小的位置，中间部分(都大于nums[i])即为nums[i]作为最小的区间
        Stack<Integer> increaseStack = new Stack<>();
        int[] maxLeft = new int[n];
        int[] maxRight = new int[n];
        Stack<Integer> decreaseStack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!increaseStack.isEmpty() && nums[i] <= nums[increaseStack.peek()]) {
                increaseStack.pop();
            }
            minLeft[i] = increaseStack.isEmpty() ? -1 : increaseStack.peek();//左边第一个比nums[i]小的数
            increaseStack.push(i);

            while (!decreaseStack.isEmpty() && nums[i] >= nums[decreaseStack.peek()]) {
                decreaseStack.pop();
            }
            maxLeft[i] = decreaseStack.isEmpty() ? -1 : decreaseStack.peek();//左边第一个比nums[i]大的数
            decreaseStack.push(i);
        }
        increaseStack.clear();
        decreaseStack.clear();

        for (int i = n - 1; i >= 0; i--) {
            while (!increaseStack.isEmpty() && nums[i] < nums[increaseStack.peek()]) {
                increaseStack.pop();
            }
            minRight[i] = increaseStack.isEmpty() ? n : increaseStack.peek();
            increaseStack.push(i);

            while (!decreaseStack.isEmpty() && nums[i] > nums[decreaseStack.peek()]) {
                decreaseStack.pop();
            }
            maxRight[i] = decreaseStack.isEmpty() ? n : decreaseStack.peek();
            decreaseStack.push(i);
        }
        long sumMax = 0, sumMin = 0;
        // i作为区间最大值，左右第一个大于i的值分别是[l,r],则[l+1,i-1]有i-1-(l+1)+1=i-l-1个数，这么多数共有[0,i-l-1]种选择(选择0个到全部选择),共i-l个选择
        for (int i = 0; i < n; i++) {
            sumMin += (long) (minRight[i] - i) * (i - minLeft[i]) * nums[i];
            sumMax += (long) (maxRight[i] - i) * (i - maxLeft[i]) * nums[i];
        }
        return sumMax - sumMin;
    }

    public long subArrayRangesStack2(int[] nums) {
        int n = nums.length;
        int[] minLeft = new int[n];
        int[] minRight = new int[n];
        Arrays.fill(minRight, n);
        int[] maxLeft = new int[n];
        int[] maxRight = new int[n];
        Arrays.fill(maxRight, n);
        Stack<Integer> stack1 = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();
        for (int i = 0; i < n; i++) {
            // 左右一边是小于等于的第一个，一边是严格小于的第一个
            while (!stack1.isEmpty() && nums[stack1.peek()] >= nums[i]) {
                minRight[stack1.peek()] = i;
                stack1.pop();
            }
            minLeft[i] = stack1.isEmpty() ? -1 : stack1.peek();
            stack1.push(i);

            while (!stack2.isEmpty() && nums[stack2.peek()] <= nums[i]) {
                maxRight[stack2.peek()] = i;
                stack2.pop();
            }
            maxLeft[i] = stack2.isEmpty() ? -1 : stack2.peek();
            stack2.push(i);
        }
        long sumMax = 0, sumMin = 0;
        // i作为最大值对各个子数组的贡献-i作为最小值对各个子数组的贡献
        for (int i = 0; i < n; i++) {
            sumMin += (long) (minRight[i] - i) * (i - minLeft[i]) * nums[i];
            sumMax += (long) (maxRight[i] - i) * (i - maxLeft[i]) * nums[i];
        }
        return sumMax - sumMin;
    }

    //2736. 最大和查询
    public int[] maximumSumQueries(int[] nums1, int[] nums2, int[][] queries) {
        int n = nums1.length;
        int[][] sortedNums = new int[n][2];
        for (int i = 0; i < n; i++) {
            sortedNums[i][0] = nums1[i];
            sortedNums[i][1] = nums2[i];
        }
        Arrays.sort(sortedNums, (a, b) -> b[0] - a[0]);
        int q = queries.length;
        int[][] sortedQueries = new int[q][3];
        for (int i = 0; i < q; i++) {
            sortedQueries[i][0] = i;
            sortedQueries[i][1] = queries[i][0];
            sortedQueries[i][2] = queries[i][1];
        }
        Arrays.sort(sortedQueries, (a, b) -> b[1] - a[1]);
        List<int[]> stack = new ArrayList<int[]>();
        int[] answer = new int[q];
        Arrays.fill(answer, -1);
        int j = 0;
        for (int[] query : sortedQueries) {
            int i = query[0], x = query[1], y = query[2];
            while (j < n && sortedNums[j][0] >= x) {
                int[] pair = sortedNums[j];
                int num1 = pair[0], num2 = pair[1];
                while (!stack.isEmpty() && stack.get(stack.size() - 1)[1] <= num1 + num2) {
                    stack.remove(stack.size() - 1);
                }
                if (stack.isEmpty() || stack.get(stack.size() - 1)[0] < num2) {
                    stack.add(new int[]{num2, num1 + num2});
                }
                j++;
            }
            int k = binarySearch(stack, y);
            if (k < stack.size()) {
                answer[i] = stack.get(k)[1];
            }
        }
        return answer;
    }

    public int binarySearch(List<int[]> list, int target) {
        int low = 0, high = list.size();
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (list.get(mid)[0] >= target) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    // 2281 巫师的总力量和
    public int totalStrength(int[] strength) {
        int n = strength.length;
        int[] left = new int[n];
        int[] right = new int[n];
        Arrays.fill(right, n);
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && strength[stack.peek()] > strength[i]) {
                right[stack.pop()] = i;
            }
            left[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }
        int[] sum = new int[n];
        sum[0] = strength[0];
        int[] ssum = new int[n];
        ssum[0] = sum[0];
        for (int i = 1; i < n; i++) {
            sum[i] = (sum[i - 1] + strength[i]) % mod;
            ssum[i] = (ssum[i - 1] + sum[i]) % mod;
        }

        long ans = 0;
        for (int i = 0; i < n; i++) {
            int l = left[i] + 1, r = right[i] - 1;
            long sumRight = (long) (i - l + 1) * getSum(r, i - 1, ssum) % mod;
            long sumLeft = (long) (r - i + 1) * getSum(i - 1, l - 2, ssum) % mod;
            long allSum = ((sumRight - sumLeft) + mod) % mod;
            ans += (allSum * strength[i]) % mod;
            ans %= mod;
        }
        return (int) ans;
    }

    private int getSum(int r, int l, int[] nums) {
        if (r < 0) return 0;
        if (l < 0) return nums[r];
        return (nums[r] - nums[l] + mod) % mod;
    }

    //2865. 美丽塔 I
    public long maximumSumOfHeights1(List<Integer> maxHeights) {
        int n = maxHeights.size();
        long res = 0;
        for (int i = 0; i < n; i++) {
            int pre = maxHeights.get(i);
            long sum = pre;
            for (int j = i - 1; j >= 0; j--) {
                pre = Math.min(pre, maxHeights.get(j));
                sum += pre;
            }
            int suf = maxHeights.get(i);
            for (int j = i + 1; j < n; j++) {
                suf = Math.min(suf, maxHeights.get(j));
                sum += suf;
            }
            res = Math.max(res, sum);
        }
        return res;
    }

    public long maximumSumOfHeights2(List<Integer> maxHeights) {
        int n = maxHeights.size();
        long res = 0;
        long[] prefix = new long[n];
        long[] suffix = new long[n];
        Deque<Integer> stack1 = new ArrayDeque<Integer>();
        Deque<Integer> stack2 = new ArrayDeque<Integer>();

        for (int i = 0; i < n; i++) {
            while (!stack1.isEmpty() && maxHeights.get(i) < maxHeights.get(stack1.peek())) {
                stack1.pop();
            }
            if (stack1.isEmpty()) {
                prefix[i] = (long) (i + 1) * maxHeights.get(i);
            } else {
                // 比 i 小的，每次一个一个计算
                // 比 i 大的，最多取 manHeight[i]一起计算
                // 5 4 3 7 8 i
                prefix[i] = prefix[stack1.peek()] + (long) (i - stack1.peek()) * maxHeights.get(i);
            }
            stack1.push(i);
        }
        for (int i = n - 1; i >= 0; i--) {
            while (!stack2.isEmpty() && maxHeights.get(i) < maxHeights.get(stack2.peek())) {
                stack2.pop();
            }
            if (stack2.isEmpty()) {
                suffix[i] = (long) (n - i) * maxHeights.get(i);
            } else {
                suffix[i] = suffix[stack2.peek()] + (long) (stack2.peek() - i) * maxHeights.get(i);
            }
            stack2.push(i);
            res = Math.max(res, prefix[i] + suffix[i] - maxHeights.get(i));
        }
        return res;
    }

    //2866. 美丽塔 II
    public long maximumSumOfHeights(List<Integer> maxHeights) {
        int n = maxHeights.size();
        long[] prefix = new long[n];
        long[] suffix = new long[n];
        Stack<Integer> stack1 = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();

        for (int i = 0; i < n; i++) {
            while (!stack1.isEmpty() && maxHeights.get(stack1.peek()) > maxHeights.get(i)) {
                stack1.pop();
            }
            if (stack1.isEmpty()) {
                prefix[i] = (long) (i + 1) * maxHeights.get(i);
            } else {
                prefix[i] = prefix[stack1.peek()] + (long) (i - stack1.peek()) * maxHeights.get(i);
            }
            stack1.push(i);
        }
        for (int i = n - 1; i >= 0; i--) {
            while (!stack2.isEmpty() && maxHeights.get(stack2.peek()) > maxHeights.get(i)) {
                stack2.pop();
            }
            if (stack2.isEmpty()) {
                suffix[i] = (long) (n - i) * maxHeights.get(i);
            } else {
                suffix[i] = suffix[stack2.peek()] + (long) (stack2.peek() - i) * maxHeights.get(i);
            }
            stack2.push(i);
        }
        long max = 0;
        for (int i = 0; i < n; i++) {
            long sum = prefix[i] + suffix[i] - maxHeights.get(i);
            max = Math.max(sum, max);
        }
        return max;
    }

    //------------------------------------------------------------------------
    // 901 股票价格跨度
    static class StockSpanner {

        Stack<int[]> stack;
        int idx;

        public StockSpanner() {
            stack = new Stack<>();
            stack.push(new int[]{-1, Integer.MAX_VALUE});
            idx = -1;
        }

        public int next(int price) {
            idx++;
            while (price >= stack.peek()[1]) {
                stack.pop();
            }
            int ans = idx - stack.peek()[0];
            stack.push(new int[]{idx, price});
            return ans;
        }
    }

    // 402 移掉k位数字
    public String removeKdigits(String num, int k) {
        Deque<Character> deque = new ArrayDeque<>();
        for (char c : num.toCharArray()) {
            // 比当前字符大的全部移除，只留递增顺序
            while (!deque.isEmpty() && k > 0 && deque.peekLast() > c) {
                deque.pollLast();
                k--;
            }
            deque.offerLast(c);
        }
        while (k-- > 0) {
            deque.pollLast();
        }
        boolean initZero = true;
        StringBuilder sb = new StringBuilder();
        while (!deque.isEmpty()) {
            char c = deque.pollFirst();
            if (c == '0' && initZero) continue;
            sb.append(c);
            initZero = false;
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }

    public String removeDuplicateLetters(String s) {
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            cnt[c - 'a']++;
        }
        Deque<Character> deque = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (!deque.contains(c)) {
                // deque的前一个 如果没有剩余了则不可移除
                // 位于i前且比i大且还有多余的情况下弹出
                while (!deque.isEmpty() && deque.peekLast() > c && cnt[deque.peekLast() - 'a'] > 0) {
                    deque.pollLast();
                }
                deque.offerLast(c);
            }
            // c在队列中已经有了，减1
            cnt[c - 'a']--;
        }
        StringBuilder sb = new StringBuilder();
        while (!deque.isEmpty()) {
            sb.append(deque.pollFirst());
        }
        return sb.toString();
    }

    // 321 拼接最大数
    public int[] maxNumber(int[] nums1, int[] nums2, int k) {
        int maxPick1 = Math.min(nums1.length, k);
        int minPick1 = Math.max(0, k - nums2.length);
        int[] maxSubsequence = new int[k];
        for (int i = minPick1; i <= maxPick1; i++) {
            int[] sequence1 = pickMax(nums1, i);
            int[] sequence2 = pickMax(nums2, k - i);
            int[] curMax = merge(sequence1, sequence2);
            if (compare(curMax, 0, maxSubsequence, 0) > 0) {
                System.arraycopy(curMax, 0, maxSubsequence, 0, k);
            }
        }
        return maxSubsequence;
    }

    private int[] pickMax(int[] nums, int pick) {
        Deque<Integer> deque = new ArrayDeque<>();
        int remove = nums.length - pick;
        for (int num : nums) {
            while (!deque.isEmpty() && deque.peekLast() < num && remove > 0) {
                deque.pollLast();
                remove--;
            }
            deque.offerLast(num);
        }
        while (remove-- > 0) {
            deque.pollLast();
        }
        int[] result = new int[deque.size()];
        int idx = 0;
        while (!deque.isEmpty()) {
            result[idx++] = deque.pollFirst();
        }
        return result;
    }

    public int[] merge(int[] subsequence1, int[] subsequence2) {
        int x = subsequence1.length, y = subsequence2.length;
        if (x == 0) {
            return subsequence2;
        }
        if (y == 0) {
            return subsequence1;
        }
        int mergeLength = x + y;
        int[] merged = new int[mergeLength];
        int index1 = 0, index2 = 0;
        for (int i = 0; i < mergeLength; i++) {
            if (compare(subsequence1, index1, subsequence2, index2) > 0) {
                merged[i] = subsequence1[index1++];
            } else {
                merged[i] = subsequence2[index2++];
            }
        }
        return merged;
    }

    public int compare(int[] subsequence1, int index1, int[] subsequence2, int index2) {
        int x = subsequence1.length, y = subsequence2.length;
        while (index1 < x && index2 < y) {
            int difference = subsequence1[index1] - subsequence2[index2];
            if (difference != 0) {
                return difference;
            }
            index1++;
            index2++;
        }
        return (x - index1) - (y - index2);
    }

    // 962 最大宽度坡 首先把A数组中的以A[0]开头的递减序列抽取出来
    //其中 [2, 0, 5] 也是一个满足条件的坡并且宽度为 2，但是为什么在计算的时候没有算它呢？
    // 因为该数组从 A[0] 开始的递减序列为 (6, 1, 0) 并没有元素 2，是因为在元素 2 的左边有比它还要小的元素 1。当
    // 计算最大宽度坡时 1 和 2 相比，不管是元素值还是元素的下标都更小，所以若以 2 为坡底能计算出某一坡的宽度时同样的以 1 为坡底也能计算出相应的坡的宽度并且宽度更大
    public int maxWidthRamp(int[] nums) {
        int n = nums.length;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            // 递减栈
            if (stack.isEmpty() || nums[stack.peek()] > nums[i]) {
                stack.push(i);
            }
        }
        int max = 0;
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] <= nums[i]) {
                max = Math.max(i - stack.pop(), max);
            }
        }
        return max;
    }

    // 1124 表现良好的最长时间段
    public int longestWPI(int[] hours) {
        int n = hours.length;
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + (hours[i - 1] > 8 ? 1 : -1);
        }
        int max = 0;
        // 双循环遍历各种可能长度
        for (int i = 1; i <= n; i++) {
            for (int j = i; j <= n; j++) {
                if (sum[j] - sum[i - 1] > 0) {
                    max = Math.max(max, j - i + 1);
                }
            }
        }
        return max;
    }

    public int longestWPI2(int[] hours) {
        int n = hours.length;
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + (hours[i - 1] > 8 ? 1 : -1);
        }
        // 优化1：j从n开始遍历
        // 优化2：外层循环用递减数组减少size：
        // 在遍历外层循环i的过程中, 在对于任意的一个i < i1 < j, 如果prefixSum[i1] >= prefixSum[i],那么(i1, j)一定不会是答案.因为:
        //如果prefixSum[j] > prefixSum[i1], 那么(i1, j)一定不会是答案,因为(i, j)更长.
        //如果prefixSum[j] < prefixSum[i1], 那么(i1, j)也一定不会是答案,因为我们要找prefixSum[j] -prefixSum[i1] > 0的(i, j)
        //这时我们需要从头遍历一遍prefixSum, 找到一个严格单调递减的数组.
        // 优化3：栈保存外层递减数组，保证j可以不重复从n开始遍历
        //对于一个j, 如果它满足prefixSum[j] > prefixSum[stk[0]], 那么(0, j)是候选项, 但是由于stk是单调递减的,所以prefixSum[j]也是>prefixSum[stk[0 + x]],那么(stk[0 + x], j)也是候选项.
        //对于一个j, 如果它满足prefixSum[j] < prefixSum[stk[0]], 那么(0, j)不是候选项, 但是prefixSum[j]和prefixSum[stk[0 + x]]的大小关系无法判断,所以(stk[0 + x], j)也是候选项.
        //但是如果反过来, 反向遍历stk, 对于一个j 如果它满足prefixSum[j] < prefixSum[stk[-1]], 因为是单调递减的,所以stk中的其他元素都不会再小于prefixSum[j] , 所以j就可以直接被排除掉.
        //再然后, 如果对于一个j 如果它满足prefixSum[j] > prefixSum[stk[-1]], 那么(stk[-1], j)就是候选项,此时再根据7.1, 对于stk[-1]来说, j再继续向左遍历已经没有意义了,所以就可以把stk[-1]排除掉了.
        // 而stk[-2]及后面的元素还需要继续判断,但也不必回溯到prefixSum的最右端继续遍历j了.因为prefixSum[j] > prefixSum[stk[-1]],j是从右往左第一个满足的j，j右边的都小于prefixSum[stk[-1]] 而prefixSum[stk[-1]]<prefixSum[stk[-2]]，j右边的对stk[-2]也没有意义
        // 单调递减栈
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i <= n; i++) {
            if (stack.isEmpty() || sum[stack.peek()] > sum[i]) {
                stack.push(i);
            }
        }
        int max = 0;
        for (int j = n; j > 0; j--) {
            while (!stack.isEmpty() && sum[j] > sum[stack.peek()]) {
                max = Math.max(max, j - stack.pop());
            }
        }

        return max;
    }

    //1130. 叶值的最小代价生成树 DP做法搜dp
    public int mctFromLeafValues(int[] arr) {
        int res = 0;
        Deque<Integer> stk = new ArrayDeque<>();
        for (int x : arr) {
            while (!stk.isEmpty() && stk.peek() <= x) {
                int y = stk.pop();
                if (stk.isEmpty() || stk.peek() > x) {
                    res += y * x;
                } else {
                    res += stk.peek() * y;
                }
            }
            stk.push(x);
        }
        while (stk.size() >= 2) {
            int x = stk.pop();
            res += stk.peek() * x;
        }
        return res;
    }

    // 862 和至少为K的最短子数组
    // 前缀和+单调队列
    public int shortestSubarray(int[] nums, int k) {
        int n = nums.length;
        long[] sum = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + nums[i - 1];
        }
        int ans = n + 1;
        Deque<Integer> deque = new ArrayDeque<>();
        // 本质维护单调递增的队列，和队列最前面最小的和做差
        for (int i = 0; i <= n; i++) {
            // 如果s[i]-s[j]>=k,此时i-j最小,i右边的-j 距离更大，j可以弹出
            while (!deque.isEmpty() && sum[i] - sum[deque.peekFirst()] >= k) {
                ans = Math.min(ans, i - deque.pollFirst());
            }
            // 如果s[i]<=s[j]，i后面-j>=k的话，-i肯定也>=k且距离更小，j可弹出
            while (!deque.isEmpty() && sum[i] <= sum[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
        }
        return ans == n + 1 ? -1 : ans;
    }

    //496. 下一个更大元素 I
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> map = new HashMap<>();
        Stack<Integer> stack = new Stack<>();
        for (int i = nums2.length - 1; i >= 0; i--) {
            int num = nums2[i];
            while (!stack.isEmpty() && num > stack.peek()) {
                stack.pop();
            }
            map.put(num, stack.isEmpty() ? -1 : stack.peek());
            stack.add(num);
        }
        int[] result = new int[nums1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = map.get(nums1[i]);
        }
        return result;
    }

    // 503 下一个更大元素 2
    public int[] nextGreaterElements(int[] nums) {
        int n = nums.length;
        int[] ret = new int[n];
        Arrays.fill(ret, -1);
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n * 2 - 1; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i % n]) {
                ret[stack.pop()] = nums[i % n];
            }
            stack.push(i % n);
        }
        return ret;
    }

    // 556 下一个更大的元素3
    // 思路参考nextPermutation
    public int nextGreaterElement(int n) {
        char[] chars = String.valueOf(n).toCharArray();
        int i = chars.length - 2;
        while (i >= 0 && chars[i] >= chars[i + 1]) {
            i--;
        }
        if (i < 0) return -1;
        int j = chars.length - 1;
        while (j >= 0 && chars[i] >= chars[j]) {
            j--;
        }
        swap(chars, i, j);
        reverse(chars, i + 1, chars.length - 1);
        long ans = Long.parseLong(new String(chars));
        return ans > Integer.MAX_VALUE ? -1 : (int) ans;

    }

    private void swap(char[] chars, int i, int j) {
        char tmp = chars[i];
        chars[i] = chars[j];
        chars[j] = tmp;
    }

    private void reverse(char[] chars, int start, int end) {
        while (start < end) {
            swap(chars, start++, end--);
        }
    }

    //1019. 链表中的下一个更大节点
    public int[] nextLargerNodes(ListNode head) {
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }
        int[] ans = new int[list.size()];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < list.size(); i++) {
            while (!stack.isEmpty() && list.get(stack.peek()) < list.get(i)) {
                ans[stack.pop()] = list.get(i);
            }
            stack.push(i);
        }
        return ans;
    }

    // 456 132模式 枚举3
    public boolean find132pattern(int[] nums) {
        int n = nums.length;
        if (n < 3) return false;
        int leftMin = nums[0];
        TreeMap<Integer, Integer> rightAll = new TreeMap<>();
        for (int i = 2; i < n; i++) {
            rightAll.put(nums[i], rightAll.getOrDefault(nums[i], 0) + 1);
        }
        for (int i = 1; i < n - 1; i++) {
            if (nums[i] > leftMin) {
                //ceilingKey>= 因此不能ceilingKey(leftMin)
                Integer rightMin = rightAll.ceilingKey(leftMin + 1);
                if (rightMin != null && rightMin < nums[i]) {
                    return true;
                }
            }
            leftMin = Math.min(leftMin, nums[i]);
            rightAll.put(nums[i + 1], rightAll.get(nums[i + 1]) - 1);
            if (rightAll.get(nums[i + 1]) == 0) {
                rightAll.remove(nums[i + 1]);
            }
        }
        return false;
    }

    // 枚举1
    public boolean find132pattern2(int[] nums) {
        int n = nums.length;
        Deque<Integer> candidateK = new LinkedList<Integer>();
        candidateK.push(nums[n - 1]);
        int maxK = Integer.MIN_VALUE;

        for (int i = n - 2; i >= 0; --i) {
            if (nums[i] < maxK) {
                return true;
            }
            //maxK 是单调递减栈 的前一个栈顶，比栈顶小，当前栈顶是j
            while (!candidateK.isEmpty() && nums[i] > candidateK.peek()) {
                maxK = candidateK.pop();
            }
            // idx 小于 maxK但是值比maxK大,就是j
            if (nums[i] > maxK) {
                candidateK.push(nums[i]);
            }
        }

        return false;
    }

    //枚举2
    public boolean find132pattern3(int[] nums) {
        int n = nums.length;
        List<Integer> candidateI = new ArrayList<Integer>();
        candidateI.add(nums[0]);
        List<Integer> candidateJ = new ArrayList<Integer>();
        candidateJ.add(nums[0]);

        for (int k = 1; k < n; ++k) {
            int idxI = binarySearchFirst(candidateI, nums[k]);
            int idxJ = binarySearchLast(candidateJ, nums[k]);
            if (idxI >= 0 && idxJ >= 0) {
                if (idxI <= idxJ) {
                    return true;
                }
            }

            if (nums[k] < candidateI.get(candidateI.size() - 1)) {
                candidateI.add(nums[k]);
                candidateJ.add(nums[k]);
            } else if (nums[k] > candidateJ.get(candidateJ.size() - 1)) {
                int lastI = candidateI.get(candidateI.size() - 1);
                while (!candidateJ.isEmpty() && nums[k] > candidateJ.get(candidateJ.size() - 1)) {
                    candidateI.remove(candidateI.size() - 1);
                    candidateJ.remove(candidateJ.size() - 1);
                }
                candidateI.add(lastI);
                candidateJ.add(nums[k]);
            }
        }

        return false;
    }

    public int binarySearchFirst(List<Integer> candidate, int target) {
        int low = 0, high = candidate.size() - 1;
        if (candidate.get(high) >= target) {
            return -1;
        }
        while (low < high) {
            int mid = (high - low) / 2 + low;
            int num = candidate.get(mid);
            if (num >= target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    public int binarySearchLast(List<Integer> candidate, int target) {
        int low = 0, high = candidate.size() - 1;
        if (candidate.get(low) <= target) {
            return -1;
        }
        while (low < high) {
            int mid = (high - low + 1) / 2 + low;
            int num = candidate.get(mid);
            if (num <= target) {
                high = mid - 1;
            } else {
                low = mid;
            }
        }
        return low;
    }

    // 739 每日温度
    //单调栈
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        Stack<Integer> stack = new Stack<>();
        int[] right = new int[n];
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
                right[stack.peek()] = i;
                stack.pop();
            }
            stack.push(i);
        }
//        for (int i = n - 1; i >= 0; i--) {
//            while (!stack.isEmpty() && temperatures[stack.peek()] <= temperatures[i]) {
//                stack.pop();
//            }
//            right[i] = stack.isEmpty() ? 0 : stack.peek();
//            stack.push(i);
//        }
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = right[i] > 0 ? right[i] - i : 0;
        }
        return result;
    }

    // 1475 商品折扣后的最终价格
    public int[] finalPrices(int[] prices) {
        int n = prices.length;
        int[] right = new int[n];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && prices[i] <= prices[stack.peek()]) {
                right[stack.peek()] = prices[i];
                stack.pop();
            }
            stack.push(i);
        }
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = prices[i] - right[i];
        }
        return ans;
    }


    //239 滑动窗口最大值
    //给定一个数组 nums 和滑动窗口的大小 k，请找出所有滑动窗口里的最大值。
// 输入: nums = [1,3,-1,-3,5,3,6,7], 和 k = 3
//输出: [3,3,5,5,6,7]
    //优先队列TLE
    public int[] maxSlidingWindow(int[] nums, int k) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(((o1, o2) -> o2 - o1));
        for (int i = 0; i < k; i++) {
            priorityQueue.offer(nums[i]);
        }
        int[] result = new int[nums.length - k + 1];
        result[0] = priorityQueue.peek();
        for (int i = k; i < nums.length; i++) {
            priorityQueue.offer(nums[i]);
            priorityQueue.remove(nums[i - k]);
            result[i - k + 1] = priorityQueue.peek();
        }
        return result;
    }

    //单调队列
    public int[] maxSlidingWindowQueue(int[] nums, int k) {
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < k; i++) {
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }
            deque.offerLast(i);
        }
        int[] result = new int[nums.length - k + 1];
        result[0] = nums[deque.peekFirst()];
        for (int i = k; i < nums.length; i++) {
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }
            deque.offerLast(i);
            while (deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
            result[i - k + 1] = nums[deque.peekFirst()];
        }
        return result;
    }

    // 238 除自身以外数组的乘积
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        left[0] = 1;
        for (int i = 1; i < n; i++) {
            left[i] = nums[i - 1] * left[i - 1];
        }
        int[] right = new int[n];
        right[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            right[i] = nums[i + 1] * right[i + 1];
        }
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = left[i] * right[i];
        }
        return result;
    }

    public int[] productExceptSelf2(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        int left = 1;
        for (int i = 0; i < n; i++) {
            result[i] = left;
            left = nums[i] * left;
        }
        int right = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= right;
            right = nums[i] * right;
        }
        return result;
    }

    // 2434 使用机器人打印字典序最小的字符串
    // 问题相当于从左到右遍历 s，在允许用一个辅助栈的前提下，计算能得到的字典序最小的字符串。
    public String robotWithString(String s) {
        Stack<Character> p_stack = new Stack<>();
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            cnt[c - 'a']++;
        }
        // 余下字符串中最小的字符
        int min = 0;
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            cnt[c - 'a']--;
            while (min < 26 && cnt[min] == 0) min++;
            p_stack.push(c);
            while (!p_stack.isEmpty() && p_stack.peek() - 'a' <= min) {
                sb.append(p_stack.pop());
            }
        }
        return sb.toString();
    }

    //endregion----------------------------------------------------------------------------------
    //region ----------------------------------------队列-----------------------------------------
    // 区间问题 会议室 435无重叠区间
    // 56 合并区间
    public int[][] merge(int[][] intervals) {
        Deque<int[]> result = new ArrayDeque<>();
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));
        for (int[] interval : intervals) {
            if (result.size() == 0 || result.peekLast()[1] < interval[0]) {
                result.offerLast(interval);
            } else {
                result.peekLast()[1] = Math.max(result.peekLast()[1], interval[1]);
            }
        }
        return result.toArray(new int[result.size()][]);
    }

    // 57 插入区间 原区间已经排好序
    //给你一个 无重叠的 ，按照区间起始端点排序的区间列表。
// 在列表中插入一个新的区间，你需要确保列表中的区间仍然有序且不重叠（如果有必要的话，可以合并区间）。
    public int[][] insert(int[][] intervals, int[] newInterval) {
        int[][] newIntervals = new int[intervals.length + 1][2];
        System.arraycopy(intervals, 0, newIntervals, 0, intervals.length);
        newIntervals[intervals.length] = newInterval;
        return merge(newIntervals);
    }

    public int[][] insert2(int[][] intervals, int[] newInterval) {
        int left = newInterval[0];
        int right = newInterval[1];
        boolean placed = false;
        List<int[]> result = new ArrayList<>();
        for (int[] interval : intervals) {
            if (interval[0] > right) {
                // 在插入区间的右侧且无交集
                if (!placed) {
                    result.add(new int[]{left, right});
                    placed = true;
                }
                result.add(interval);
            } else if (interval[1] < left) {
                // 在插入区间的左侧且无交集
                result.add(interval);
            } else {
                // 与插入区间有交集，计算它们的并集
                left = Math.min(left, interval[0]);
                right = Math.max(right, interval[1]);
            }
        }
        if (!placed) {
            result.add(new int[]{left, right});
        }
        return result.toArray(new int[result.size()][]);
    }

    // 435 无重叠区间贪心做法
    public int eraseOverlapIntervalsGreedy(int[][] intervals) {
        int n = intervals.length;
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[1]));
        int ans = 1;
        int right = intervals[0][1];
        for (int i = 1; i < n; i++) {
            if (intervals[i][0] >= right) {
                ans++;
                right = intervals[i][1];
            }
        }
        return n - ans;
    }

    // 436 寻找右区间
    public int[] findRightInterval(int[][] intervals) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < intervals.length; i++) {
            map.put(intervals[i][0], i);
        }
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));
        int[] ans = new int[intervals.length];
        for (int i = 0; i < intervals.length; i++) {
            int l = i, r = intervals.length - 1;
            while (l < r) {
                int mid = l + r >> 1;
                if (intervals[mid][0] >= intervals[i][1]) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            ans[map.get(intervals[i][0])] = intervals[r][0] >= intervals[i][1] ? map.get(intervals[r][0]) : -1;
        }
        return ans;
    }

    // 简化路径
    public String simplifyPath(String path) {
        Deque<String> deque = new ArrayDeque<>();
        String[] strings = path.split("/");
        for (String s : strings) {
            if (s.equals("") || s.equals(".")) continue;
            if (s.equals("..")) {
                deque.pollLast();
                continue;
            }
            deque.offerLast(s);
        }
        StringBuilder sb = new StringBuilder();
        while (!deque.isEmpty()) {
            sb.append("/").append(deque.pollFirst());
        }
        return sb.toString().equals("") ? "/" : sb.toString();
    }

    // 871 最低加油次数
    public int minRefuelStops(int target, int startFuel, int[][] stations) {
        int ans = 0, loc = 0, remain = startFuel, idx = 0;
        PriorityQueue<Integer> queue = new PriorityQueue<>((o1, o2) -> o2 - o1);
        while (loc < target) {
            if (remain == 0) {
                if (!queue.isEmpty()) {
                    remain = queue.poll();
                    ans++;
                } else {
                    return -1;
                }
            }
            loc += remain;
            remain = 0;
            while (idx < stations.length && stations[idx][0] <= loc) {
                queue.offer(stations[idx++][1]);
            }
        }
        return ans;
    }

    // 767 重构字符串
    public String reorganizeString(String s) {
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            cnt[c - 'a']++;
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> {
            if (o1[0] != o2[0]) {
                return o2[0] - o1[0];
            }
            return o1[1] - o2[1];
        });
        for (int i = 0; i < 26; i++) {
            if (cnt[i] > 0) {
                pq.offer(new int[]{cnt[i], i});
            }
        }
        if (pq.peek()[0] > (s.length() + 1) / 2) return "";
        StringBuilder ans = new StringBuilder();

        while (pq.size() > 1) {
            int[] tmp1 = pq.poll();
            int[] tmp2 = pq.poll();
            ans.append((char) (tmp1[1] + 'a'));
            tmp1[0]--;
            if (tmp1[0] > 0) {
                pq.offer(tmp1);
            }
            ans.append((char) (tmp2[1] + 'a'));
            tmp2[0]--;
            if (tmp2[0] > 0) {
                pq.offer(tmp2);
            }
        }
        while (!pq.isEmpty()) {
            ans.append((char) (pq.poll()[1] + 'a'));
        }
        return ans.toString();
    }

    // 1047  删除字符串中的所有相邻重复项
    public String removeDuplicates(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (!stack.isEmpty() && stack.peek() == c) {
                stack.pop();
                continue;
            }
            stack.add(c);
        }
        StringBuilder sb = new StringBuilder("");
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }
        return sb.reverse().toString();
    }

    //1190. 反转每对括号间的子串
    public String reverseParentheses1(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (!stack.isEmpty() && c == ')') {
                StringBuilder sb = new StringBuilder();
                while (!stack.isEmpty() && stack.peek() != '(') {
                    sb.append(stack.pop());
                }
                stack.pop();
                for (char cc : sb.toString().toCharArray()) {
                    stack.add(cc);
                }
            } else {
                stack.push(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) sb.append(stack.pop());
        return sb.reverse().toString();
    }

    public String reverseParentheses2(String s) {
        int n = s.length();
        int[] pair = new int[n];
        Deque<Integer> stack = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else if (s.charAt(i) == ')') {
                int j = stack.pop();
                pair[i] = j;
                pair[j] = i;
            }
        }

        StringBuilder sb = new StringBuilder();
        int index = 0, step = 1;
        while (index < n) {
            if (s.charAt(index) == '(' || s.charAt(index) == ')') {
                index = pair[index];
                step = -step;
            } else {
                sb.append(s.charAt(index));
            }
            index += step;
        }
        return sb.toString();
    }

    //1499. 满足不等式的最大值
    public int findMaxValueOfEquation(int[][] points, int k) {
        int res = Integer.MIN_VALUE;
        PriorityQueue<int[]> heap = new PriorityQueue<int[]>(Comparator.comparingInt(a -> a[0]));
        for (int[] point : points) {
            int x = point[0], y = point[1];
            while (!heap.isEmpty() && x - heap.peek()[1] > k) {
                heap.poll();
            }
            if (!heap.isEmpty()) {
                res = Math.max(res, x + y - heap.peek()[0]);
            }
            heap.offer(new int[]{x - y, x});
        }
        return res;
    }

    //双端队列
    public int findMaxValueOfEquationDeque(int[][] points, int k) {
        int res = Integer.MIN_VALUE;
        Deque<int[]> queue = new ArrayDeque<int[]>();
        for (int[] point : points) {
            int x = point[0], y = point[1];
            while (!queue.isEmpty() && x - queue.peekFirst()[1] > k) {
                queue.pollFirst();
            }
            if (!queue.isEmpty()) {
                res = Math.max(res, x + y + queue.peekFirst()[0]);
            }
            while (!queue.isEmpty() && y - x >= queue.peekLast()[0]) {
                queue.pollLast();
            }
            queue.offer(new int[]{y - x, x});
        }
        return res;
    }

    // 1801. 积压订单中的订单总数
    public int getNumberOfBacklogOrders(int[][] orders) {
        int mod = (int) 1e9 + 7;
        PriorityQueue<int[]> sellOrders = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);
        PriorityQueue<int[]> buyOrders = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);
        for (int[] order : orders) {
            if (order[2] == 0) {//buy
                if (sellOrders.isEmpty() || sellOrders.peek()[0] > order[0]) {
                    buyOrders.offer(order);
                    continue;
                }
                while (!sellOrders.isEmpty() && sellOrders.peek()[0] <= order[0] && order[1] > 0) {
                    int[] sellOrder = sellOrders.poll();
                    if (sellOrder[1] > order[1]) {
                        sellOrder[1] -= order[1];
                        order[1] = 0;
                        sellOrders.offer(sellOrder);
                    } else {
                        order[1] -= sellOrder[1];
                    }
                }
                if (order[1] > 0) {
                    buyOrders.offer(order);
                }
            } else {
                if (buyOrders.isEmpty() || buyOrders.peek()[0] < order[0]) {
                    sellOrders.offer(new int[]{order[0], order[1]});
                    continue;
                }
                while (!buyOrders.isEmpty() && buyOrders.peek()[0] >= order[0] && order[1] > 0) {
                    int[] buyOrder = buyOrders.poll();
                    if (buyOrder[1] > order[1]) {
                        buyOrder[1] -= order[1];
                        order[1] = 0;
                        buyOrders.offer(buyOrder);
                    } else {
                        order[1] -= buyOrder[1];
                    }
                }
                if (order[1] > 0) {
                    sellOrders.offer(order);
                }
            }
        }
        int ans = 0;
        while (!sellOrders.isEmpty()) {
            ans = (ans + sellOrders.poll()[1]) % mod;
        }
        while (!buyOrders.isEmpty()) {
            ans = (ans + buyOrders.poll()[1]) % mod;
        }
        return ans;
    }

    //1792. 最大平均通过率
    public double maxAverageRatio(int[][] classes, int extraStudents) {
        PriorityQueue<double[]> pq = new PriorityQueue<>((a, b) -> {
            double x = (a[0] + 1) / (a[1] + 1) - a[0] / a[1];
            double y = (b[0] + 1) / (b[1] + 1) - b[0] / b[1];
            return Double.compare(y, x);
        });
        for (int[] e : classes) {
            pq.offer(new double[]{e[0], e[1]});
        }
        while (extraStudents-- > 0) {
            double[] e = pq.poll();
            double a = e[0] + 1, b = e[1] + 1;
            pq.offer(new double[]{a, b});
        }
        double ans = 0;
        while (!pq.isEmpty()) {
            double[] e = pq.poll();
            ans += e[0] / e[1];
        }
        return ans / classes.length;
    }

    //1687. 从仓库到码头运输箱子
    // 单调队列+滑动窗口 Hard
    //https://leetcode.cn/problems/delivering-boxes-from-storage-to-ports/solutions/2006449/by-tizzi-4ubq/
    public int boxDelivering1(int[][] boxes, int portsCount, int maxBoxes, int maxWeight) {
        int n = boxes.length;
        int[] dp = new int[n + 5];
        Arrays.fill(dp, 0x3f3f3f3f);
        dp[0] = 0; //初始状态为0
        for (int i = 1; i <= n; i++) {
            int sum = 0;
            for (int j = i; j >= 1 && j >= i - maxBoxes + 1; j--) {
                sum += boxes[j - 1][1]; //累加箱子的种类之和
                if (sum > maxWeight) break; //超过了最大重量
                dp[i] = Math.min(dp[i], dp[j - 1] + cost(boxes, j, i));
            }
        }
        return dp[n];
    }

    int cost(int[][] boxes, int l, int r) {
        int ans = 2, port = boxes[l - 1][0]; //初始话为2,因为返回仓库算一次行程
        while (++l <= r) {
            if (boxes[l - 1][0] == port) continue; //只要相同，那么次数不会增加
            ans++;  //码头不相同运输次数增加1
            port = boxes[l - 1][0];
        }
        return ans;
    }

    public int boxDelivering2(int[][] boxes, int portsCount, int maxBoxes, int maxWeight) {
        int n = boxes.length;
        int[] dp = new int[n + 5];
        Arrays.fill(dp, 0x3f3f3f3f);
        dp[0] = 0;
        Deque<int[]> q = new ArrayDeque<int[]>(); //双端队列
        int dif = 0, wei = 0;
        for (int i = 1; i <= n; i++) {
            int cur = dp[i - 1] + 2;//cur为每次滑动窗口增加的值即dp[i-1]+cost[i,i]
            dif += i >= 2 && boxes[i - 1][0] != boxes[i - 2][0] ? 1 : 0;//dif为运输累加值，由于我们无法直接在队列中进行修改，那么可以考虑增加一个累加值
            wei += boxes[i - 1][1]; //重量要加上当前箱子的重量
            while (!q.isEmpty() && q.peekLast()[1] + dif >= cur) q.pollLast(); //构造一个单调递增的队列
            q.add(new int[]{i, cur - dif, boxes[i - 1][1] - wei});
            //判断左端队头是否在窗口外 并且重量不能超过最大重量
            while (q.peekFirst()[0] <= i - maxBoxes || q.peekFirst()[2] + wei > maxWeight) q.pollFirst();
            dp[i] = q.peekFirst()[1] + dif;
        }
        return dp[n];
    }

    //1851. 包含每个查询的最小区间
    public int[] minInterval(int[][] intervals, int[] queries) {
        Integer[] qindex = new Integer[queries.length];
        for (int i = 0; i < queries.length; i++) {
            qindex[i] = i;
        }
        Arrays.sort(qindex, Comparator.comparingInt(i -> queries[i]));
        Arrays.sort(intervals, Comparator.comparingInt(i -> i[0]));
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        int[] res = new int[queries.length];
        Arrays.fill(res, -1);
        int i = 0;
        for (int qi : qindex) {
            while (i < intervals.length && intervals[i][0] <= queries[qi]) {
                pq.offer(new int[]{intervals[i][1] - intervals[i][0] + 1, intervals[i][0], intervals[i][1]});
                i++;
            }
            while (!pq.isEmpty() && pq.peek()[2] < queries[qi]) {
                pq.poll();
            }
            if (!pq.isEmpty()) {
                res[qi] = pq.peek()[0];
            }
        }
        return res;
    }

    //1962. 移除石子使总数最小
    public int minStoneSum(int[] piles, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((o1, o2) -> o2 - o1);
        int sum = 0;
        for (int pile : piles) {
            sum += pile;
            pq.offer(pile);
        }
        while (!pq.isEmpty() && k-- > 0) {
            int max = pq.poll();
            int take = max / 2;
            sum -= take;
            pq.offer(max - take);
        }
        return sum;
    }

    //2208. 将数组和减半的最少操作次数
    public int halveArray(int[] nums) {
        PriorityQueue<Double> pq = new PriorityQueue<Double>((a, b) -> b.compareTo(a));
        for (int num : nums) {
            pq.offer((double) num);
        }
        int res = 0;
        double sum = 0;
        for (int num : nums) {
            sum += num;
        }
        double sum2 = 0.0;
        while (sum2 < sum / 2) {
            double x = pq.poll();
            sum2 += x / 2;
            pq.offer(x / 2);
            res++;
        }
        return res;
    }
    //2558 从数量最多的堆取走礼物
    public long pickGifts(int[] gifts, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((o1, o2) -> o2 - o1);
        long sum = 0;
        for (int gift : gifts) {
            pq.offer(gift);
            sum += gift;
        }
        long take = 0;
        while (k-- > 0 && !pq.isEmpty()) {
            int max = pq.poll();
            int remain = (int) Math.sqrt(max);
            take += max - remain;
            pq.offer(remain);
        }
        return sum - take;
    }

    //2551. 将珠子放入背包中
    public long putMarbles(int[] weights, int k) {
        int n = weights.length;
        PriorityQueue<Integer> small = new PriorityQueue<>();
        PriorityQueue<Integer> large = new PriorityQueue<>((o1, o2) -> o2 - o1);
        // 所有挡板数=n-1,分成k个背包,只需取k-1个挡板
        // n-1个和入队
        for (int i = 0; i < n - 1; i++) {
            small.offer(weights[i] + weights[i + 1]);
            large.offer(weights[i] + weights[i + 1]);
        }
        long ans = 0;
        // k-1个挡板(k个背包)和相减
        while (--k > 0) {
            ans += large.poll() - small.poll();
        }
        return ans;
    }

    //2593. 标记所有元素后数组的分数
    public long findScore(int[] nums) {
        int n = nums.length;
        Set<Integer> set = new HashSet<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> {
            if (o1[0] != o2[0]) {
                return o1[0] - o2[0];
            }
            return o1[1] - o2[1];
        });
        for (int i = 0; i < n; i++) {
            pq.offer(new int[]{nums[i], i});
        }
        long ans = 0;
        while (!pq.isEmpty() && set.size() < n) {
            int[] cell = pq.poll();
            if (set.contains(cell[1])) continue;
            ans += cell[0];
            set.add(cell[1]);
            if (cell[1] > 0) set.add(cell[1] - 1);
            if (cell[1] < n - 1) set.add(cell[1] + 1);
        }
        return ans;
    }

    //2532. 过桥的时间
    public int findCrossingTime(int n, int k, int[][] time) {
        // 定义等待中的工人优先级比较规则，时间总和越高，效率越低，优先级越低，越优先被取出
        PriorityQueue<Integer> waitLeft = new PriorityQueue<Integer>((x, y) -> {
            int timeX = time[x][0] + time[x][2];
            int timeY = time[y][0] + time[y][2];
            return timeX != timeY ? timeY - timeX : y - x;
        });
        PriorityQueue<Integer> waitRight = new PriorityQueue<Integer>((x, y) -> {
            int timeX = time[x][0] + time[x][2];
            int timeY = time[y][0] + time[y][2];
            return timeX != timeY ? timeY - timeX : y - x;
        });

        PriorityQueue<int[]> workLeft = new PriorityQueue<int[]>((x, y) -> {
            if (x[0] != y[0]) {
                return x[0] - y[0];
            } else {
                return x[1] - y[1];
            }
        });
        PriorityQueue<int[]> workRight = new PriorityQueue<int[]>((x, y) -> {
            if (x[0] != y[0]) {
                return x[0] - y[0];
            } else {
                return x[1] - y[1];
            }
        });

        int remain = n, curTime = 0;
        for (int i = 0; i < k; i++) {
            waitLeft.offer(i);
        }
        while (remain > 0 || !workRight.isEmpty() || !waitRight.isEmpty()) {
            // 1. 若 workLeft 或 workRight 中的工人完成工作，则将他们取出，并分别放置到 waitLeft 和 waitRight 中。
            while (!workLeft.isEmpty() && workLeft.peek()[0] <= curTime) {
                waitLeft.offer(workLeft.poll()[1]);
            }
            while (!workRight.isEmpty() && workRight.peek()[0] <= curTime) {
                waitRight.offer(workRight.poll()[1]);
            }

            if (!waitRight.isEmpty()) {
                // 2. 若右侧有工人在等待，则取出优先级最低的工人并过桥
                int id = waitRight.poll();
                curTime += time[id][2];
                workLeft.offer(new int[]{curTime + time[id][3], id});
            } else if (remain > 0 && !waitLeft.isEmpty()) {
                // 3. 若右侧还有箱子，并且左侧有工人在等待，则取出优先级最低的工人并过桥
                int id = waitLeft.poll();
                curTime += time[id][0];
                workRight.offer(new int[]{curTime + time[id][1], id});
                remain--;
            } else {
                // 4. 否则，没有人需要过桥，时间过渡到 workLeft 和 workRight 中的最早完成时间
                int nextTime = Integer.MAX_VALUE;
                if (!workLeft.isEmpty()) {
                    nextTime = Math.min(nextTime, workLeft.peek()[0]);
                }
                if (!workRight.isEmpty()) {
                    nextTime = Math.min(nextTime, workRight.peek()[0]);
                }
                if (nextTime != Integer.MAX_VALUE) {
                    curTime = Math.max(nextTime, curTime);
                }
            }
        }
        return curTime;
    }

    //2611. 老鼠和奶酪
    public int miceAndCheese(int[] reward1, int[] reward2, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);
        int n = reward1.length;
        for (int i = 0; i < n; i++) {
            pq.offer(new int[]{reward1[i] - reward2[i], i});
        }
        int ans = 0;
        while (k-- > 0 && !pq.isEmpty()) {
            ans += reward1[pq.poll()[1]];
        }
        while (!pq.isEmpty()) {
            ans += reward2[pq.poll()[1]];
        }
        return ans;
    }

    // 差分
    public int miceAndCheese2(int[] reward1, int[] reward2, int k) {
        int ans = 0;
        int n = reward1.length;
        int[] diffs = new int[n];
        for (int i = 0; i < n; i++) {
            ans += reward2[i];
            diffs[i] = reward1[i] - reward2[i];
        }
        Arrays.sort(diffs);
        for (int i = 1; i <= k; i++) {
            ans += diffs[n - i];
        }
        return ans;
    }

    //2679. 矩阵中的和
    public int matrixSum(int[][] nums) {
        int m = nums.length, n = nums[0].length;
        PriorityQueue<Integer>[] pqs = new PriorityQueue[m];
        for (int i = 0; i < m; i++) {
            PriorityQueue<Integer> pq = new PriorityQueue<>();
            pqs[i] = pq;
            for (int j = 0; j < n; j++) {
                pq.offer(nums[i][j]);
            }
        }
        int score = 0;
        while (n-- > 0) {
            int max = 0;
            for (int i = 0; i < m; i++) {
                max = Math.max(max, pqs[i].poll());
            }
            score += max;
        }
        return score;
    }

    //endregion--------------------------------------------------------------------------------------------------------
}
