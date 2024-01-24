import java.util.*;

public class SolutionAdvance {

//region-----------------------------------折半搜索meet in middle------------------------------------
//    这里整理一下 [ 在数组中选取子集，达到某一目标 ] 这类问题的通用解法。
//
//    类型1 : 目标值明确，可以把目标值看出背包容量，数组值看做物品，转成背包问题
//    类型2 : 目标值不明确，容量不知道，不能用背包，只能枚举子集的和

    //805 数组的均值分割
    //折半搜索+二进制枚举
    public boolean splitArraySameAverage(int[] nums) {
        if (nums.length == 1) {
            return false;
        }
        int n = nums.length, m = n / 2;
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        for (int i = 0; i < n; i++) {
            nums[i] = nums[i] * n - sum;
        }

        Set<Integer> left = new HashSet<>();
        for (int i = 1; i < (1 << m); i++) {
            int tot = 0;
            for (int j = 0; j < m; j++) {
                if ((i & (1 << j)) != 0) {
                    tot += nums[j];
                }
            }
            if (tot == 0) {
                return true;
            }
            left.add(tot);
        }
        int rsum = 0;
        for (int i = m; i < n; i++) {
            rsum += nums[i];
        }
        for (int i = 1; i < (1 << (n - m)); i++) {
            int tot = 0;
            for (int j = m; j < n; j++) {
                if ((i & (1 << (j - m))) != 0) {
                    tot += nums[j];
                }
            }
            if (tot == 0 || (rsum != tot && left.contains(-tot))) {
                return true;
            }
        }
        return false;
    }

    // 转换为01背包问题
    public boolean splitArraySameAverageDP(int[] nums) {
        if (nums.length == 1) {
            return false;
        }
        int n = nums.length, m = n / 2;
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        boolean isPossible = false;
        for (int i = 1; i <= m; i++) {
            if (sum * i % n == 0) {
                isPossible = true;
                break;
            }
        }
        if (!isPossible) {
            return false;
        }
        Set<Integer>[] dp = new Set[m + 1];
        for (int i = 0; i <= m; i++) {
            dp[i] = new HashSet<>();
        }
        dp[0].add(0);
        for (int num : nums) {
            for (int i = m; i >= 1; i--) {
                for (int x : dp[i - 1]) {
                    int curr = x + num;
                    if (curr * n == sum * i) {
                        return true;
                    }
                    dp[i].add(curr);
                }
            }
        }
        return false;
    }

    //1755 最接近目标值的子序列和
    public int minAbsDifference(int[] nums, int goal) {
        int n = nums.length;
        int ln = n / 2, rn = n - n / 2;
        int[] lsum = new int[1 << ln];
        for (int i = 1; i < (1 << ln); i++) {
            for (int j = 0; j < ln; j++) {
                if ((i & (1 << j)) == 0) continue;
                lsum[i] = lsum[i - (1 << j)] + nums[j];
                break;
            }
        }
        int[] rsum = new int[1 << rn];
        for (int i = 1; i < (1 << rn); i++) {
            for (int j = 0; j < rn; j++) {
                if ((i & (1 << j)) == 0) continue;
                rsum[i] = rsum[i - (1 << j)] + nums[ln + j];
                break;
            }
        }
        Arrays.sort(lsum);
        Arrays.sort(rsum);
        int ans = Integer.MAX_VALUE;
        for (int x : lsum) {
            ans = Math.min(ans, Math.abs(goal - x));
        }
        for (int x : rsum) {
            ans = Math.min(ans, Math.abs(goal - x));
        }
        for (int i = 0, j = rsum.length - 1; i < lsum.length && j >= 0; ) {
            int sum = lsum[i] + rsum[j];
            ans = Math.min(ans, Math.abs(goal - sum));
            if (sum > goal) {
                j--;
            } else {
                i++;
            }
        }
        return ans;
    }

    // 2035 将数组分成两个数组并最小化数组和的差 Hard toreview
    public int minimumDifference(int[] nums) {
        // 状态压缩。
        int n = nums.length >> 1;
        int mask = 1 << n;
        /** 考虑到数据范围，不可能直接枚举整个数组的位编码，因此考虑将数组分成前后两部分分别枚举。
         * 将 [0, n) 部分记为左边部分；[n, 2n) 记为右边部分，分别枚举两边的编码，编码位为 0 表
         * 示改位属于第一个子数组，为 1 则表示属于第二个子数组。由于最终结果是两个子数组的差的最
         * 小值，可以记录所有编码代表的子数组的差，方法很简单，如果记编码 mask 对应的子数组分配
         * 情况的差为 f(mask)，将 1 的位加在 f(mask) 上，0 的位则做减法。这样，在左边枚举一个
         * 1 的个数为 x 个的编码 mask1，在右边枚举一个 1 的个数为 n - y 的编码 mask2，f(mask1)
         * + f(mask2) 就是由这两个编码共同构成的两个子数组的差。
         * 例：[3, 9, 7, 3]，分成两部分 [3, 9] 和 [7, 3] 处理。如果左边枚举 01，表示 3 分配给
         * 第一个子数组，9 分配给第二个子数组；那么右边自然应该枚举 10 或者 01（左边是 1 个 1
         * 的编码，右边就必须是 2 - 1 = 1 个 1 的编码）。左边的编码 01 代表的和是 -3 + 9 = 6；
         * 右边如果枚举 10 代表的和是 7 - 3 = 4，那么相加就是 6 + 4 = 10，是 [9, 7] 和 [3, 3]
         * 的差。用哈希表将编码代表的和保存到对应的 1 的个数中去。
         **/
        int[] sum = new int[mask];
        // 设置初始值 sum[0]，全部分配给第一个子数组，全部做减法。
        for (int i = 0; i < n; i++)
            sum[0] -= nums[i];
        Map<Integer, HashSet<Integer>> left = new HashMap<>();
        left.put(0, new HashSet<>());
        left.get(0).add(sum[0]);
        for (int i = 1; i < mask; i++) {
            /** 枚举一个先前的状态 prev = i ^ (1 << j)，相当于在 prev 的基础上将第 j 位分配给
             * 了第一个数组，原先 nums[j] 是减法的，现在要改成加法，则递推式为 sum[i] = sum[prev]
             * + nums[j] + nums[j]。**/
            int bits = Integer.bitCount(i);
            /** 计算后导零个数，1 << j 应该是一个 1，且 i - (1 << j) 是更新过的状态，可以利用该
             * 状态更新sums[i]。（也可以枚举一个包含在 i 内的位 j 来更新）**/
            int j = Integer.numberOfTrailingZeros(i);
            sum[i] = sum[i ^ (1 << j)] + 2 * nums[j];
            // 将当前的和记录到对应的 1 的个数下面。
            left.putIfAbsent(bits, new HashSet<>());
            left.get(bits).add(sum[i]);
        }
        // 以相同的方法处理右半部分数组。
        Arrays.fill(sum, 0);
        for (int i = 0; i < n; i++)
            sum[0] -= nums[i + n];
        Map<Integer, TreeSet<Integer>> right = new HashMap<>();
        right.put(0, new TreeSet<>());
        right.get(0).add(sum[0]);
        for (int i = 1; i < mask; i++) {
            int bits = Integer.bitCount(i);
            int j = Integer.numberOfTrailingZeros(i);
            sum[i] = sum[i ^ (1 << j)] + 2 * nums[j + n];
            right.putIfAbsent(bits, new TreeSet<>());
            right.get(bits).add(sum[i]);
        }
        int res = Integer.MAX_VALUE;
        /** 首先在左半边枚举 key 个 1 和 n - key 个 0 的编码；则相应地，右半边需要找到 n - key
         * 个 1 和 key 个 0 的编码，它们共同构成两个子数组。**/
        for (int key : left.keySet()) {
            if (!right.containsKey(n - key))
                continue;
            for (int x : left.get(key)) {
                /** 为了加快计算，直接通过有序集合的 ceiling(-x) 和 floor(-x) 找有可能成为最小
                 * 值的两个数字。**/
                Integer y = right.get(n - key).ceiling(-x);
                if (y != null)
                    res = Math.min(Math.abs(x + y), res);
                y = right.get(n - key).floor(-x);
                if (y != null)
                    res = Math.min(Math.abs(x + y), res);
            }
        }
        return res;
    }
    //endregion----------------------------------------------------------------------------------
    //region -------------------------------扫描线----------------------------------------
    // 218 天际线问题
    // 离散化 + 扫描线
    public List<List<Integer>> getSkyline(int[][] buildings) {
        List<List<Integer>> result = new ArrayList<>();
        List<int[]> list = new ArrayList<>();
        for (int[] bs : buildings) {
            list.add(new int[]{bs[0], -bs[2]});
            list.add(new int[]{bs[1], bs[2]});
        }
        list.sort((o1, o2) -> {
            if (o1[0] != o2[0]) {
                return o1[0] - o2[0];
            }
            return o1[1] - o2[1];
        });
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);
        priorityQueue.offer(0);
        int prev = 0;
        for (int[] ls : list) {
            int height = ls[1];
            if (height < 0) {
                priorityQueue.offer(-height);
            } else {
                priorityQueue.remove(height);
            }

            int cur = priorityQueue.peek();
            if (prev != cur) {
                result.add(Arrays.asList(ls[0], cur));
                prev = cur;
            }
        }
        return result;
    }

    // 391 完美矩形
    public boolean isRectangleCover(int[][] rectangles) {
        int n = rectangles.length;
        int[][] rs = new int[n * 2][4];
        for (int i = 0, idx = 0; i < n; i++) {
            int[] re = rectangles[i];
            rs[idx++] = new int[]{re[0], re[1], re[3], 1};
            rs[idx++] = new int[]{re[2], re[1], re[3], -1};
        }
        Arrays.sort(rs, (a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            return a[1] - b[1];
        });
        n *= 2;
        // 分别存储相同的横坐标下「左边的线段」和「右边的线段」 (y1, y2)
        List<int[]> l1 = new ArrayList<>(), l2 = new ArrayList<>();
        for (int l = 0; l < n; ) {
            int r = l;
            l1.clear();
            l2.clear();
            // 找到横坐标相同部分
            while (r < n && rs[r][0] == rs[l][0]) r++;
            for (int i = l; i < r; i++) {
                int[] cur = new int[]{rs[i][1], rs[i][2]};
                List<int[]> list = rs[i][3] == 1 ? l1 : l2;
                if (list.isEmpty()) {
                    list.add(cur);
                } else {
                    int[] prev = list.get(list.size() - 1);
                    if (cur[0] < prev[1]) return false; // 存在重叠
                    else if (cur[0] == prev[1]) prev[1] = cur[1]; // 首尾相连
                    else list.add(cur);
                }
            }
            if (l > 0 && r < n) {
                // 若不是完美矩形的边缘竖边，检查是否成对出现
                if (l1.size() != l2.size()) return false;
                for (int i = 0; i < l1.size(); i++) {
                    if (l1.get(i)[0] == l2.get(i)[0] && l1.get(i)[1] == l2.get(i)[1]) continue;
                    return false;
                }
            } else {
                // 若是完美矩形的边缘竖边，检查是否形成完整一段
                if (l1.size() + l2.size() != 1) return false;
            }
            l = r;
        }
        return true;
    }

    // 850 矩形面积2 Hard
    public int rectangleArea(int[][] rectangles) {
        final int MOD = 1000000007;
        int n = rectangles.length;
        Set<Integer> set = new HashSet<Integer>();
        for (int[] rect : rectangles) {
            // 下边界
            set.add(rect[1]);
            // 上边界
            set.add(rect[3]);
        }
        List<Integer> hbound = new ArrayList<Integer>(set);
        Collections.sort(hbound);
        int m = hbound.size();
        // 「思路与算法部分」的 length 数组并不需要显式地存储下来
        // length[i] 可以通过 hbound[i+1] - hbound[i] 得到
        int[] seg = new int[m - 1];

        List<int[]> sweep = new ArrayList<int[]>();
        for (int i = 0; i < n; ++i) {
            // 左边界
            sweep.add(new int[]{rectangles[i][0], i, 1});
            // 右边界
            sweep.add(new int[]{rectangles[i][2], i, -1});
        }
        Collections.sort(sweep, (a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0];
            } else if (a[1] != b[1]) {
                return a[1] - b[1];
            } else {
                return a[2] - b[2];
            }
        });

        long ans = 0;
        for (int i = 0; i < sweep.size(); ++i) {
            int j = i;
            while (j + 1 < sweep.size() && sweep.get(i)[0] == sweep.get(j + 1)[0]) {
                ++j;
            }
            if (j + 1 == sweep.size()) {
                break;
            }
            // 一次性地处理掉一批横坐标相同的左右边界
            for (int k = i; k <= j; ++k) {
                int[] arr = sweep.get(k);
                int idx = arr[1], diff = arr[2];
                int left = rectangles[idx][1], right = rectangles[idx][3];
                for (int x = 0; x < m - 1; ++x) {
                    if (left <= hbound.get(x) && hbound.get(x + 1) <= right) {
                        seg[x] += diff;
                    }
                }
            }
            int cover = 0;
            for (int k = 0; k < m - 1; ++k) {
                if (seg[k] > 0) {
                    cover += (hbound.get(k + 1) - hbound.get(k));
                }
            }
            ans += (long) cover * (sweep.get(j + 1)[0] - sweep.get(j)[0]);
            i = j;
        }
        return (int) (ans % MOD);
    }

    //endregion--------------------------------------------------------------------------------
    //region---------------------------------------红黑树------------------------------------------
//1606. 找到处理最多请求的服务器
    public List<Integer> busiestServers(int k, int[] arrival, int[] load) {
        TreeSet<Integer> available = new TreeSet<>();
        for (int i = 0; i < k; i++) {
            available.add(i);
        }
        PriorityQueue<int[]> busy = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        int[] requests = new int[k];
        for (int i = 0; i < arrival.length; i++) {
            while (!busy.isEmpty() && busy.peek()[0] <= arrival[i]) {
                available.add(busy.poll()[1]);
            }
            if (available.isEmpty()) {
                continue;
            }
            Integer idx = available.ceiling(i % k);
            if (idx == null) {
                idx = available.first();
            }
            requests[idx]++;
            busy.offer(new int[]{arrival[i] + load[i], idx});
            available.remove(idx);
        }
        int maxRequest = Arrays.stream(requests).max().getAsInt();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            if (requests[i] == maxRequest) {
                list.add(i);
            }
        }
        return list;
    }
    //endregion----------------------------------------------------------------------------------
}
