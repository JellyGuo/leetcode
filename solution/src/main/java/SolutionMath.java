import java.util.*;

public class SolutionMath {
    //region----------------------------------------博弈论/数学----------------------------------------------------
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

    //1641. 统计字典序元音字符串的数目
    //https://leetcode.cn/problems/count-sorted-vowel-strings/solution/zhong-xue-shu-xue-ke-pu-n-ge-xiao-qiu-fang-dao-m-g/
    // n个小球放到m个盒子里：
    // 1.每个盒子不能为空：把n分成m份，n-1个位置中间放m-1个隔板C(n-1,m-1)
    // 2.每个盒子可以为空：m个盒子每个先放1个小球，剩余的随便放=》n+m个小球不能为空的放到m个盒子=》C(n+m-1,m-1)
    public int countVowelStrings(int n) {
        return (n + 4) * (n + 3) * (n + 2) * (n + 1) / 24;
    }

    // 458. 可怜的小猪  香农熵 lg(n)/lg(t+1)<=k
    // 8 只砝码 7轻1重,最多2次称重一定找出重的 lg(8)/lg(2+1)<=2 3 3 2
    // 只有1轮，1只猪 死 / 不死 2种状态，x只 2^x种状态，验证2^x桶水
    // 2轮 1只猪  第一轮死 / 第二轮死 / 不死 3种状态，x只 3^x
    // t轮 1只 t+1种状态，x只 (t+1)^x >= buckets  => ln(buckets)/ln(t+1)<=x
    public int poorPigs(int buckets, int minutesToDie, int minutesToTest) {
        int turns = minutesToTest / minutesToDie;
        int k = turns + 1;
        return (int) Math.ceil(Math.log(buckets) / Math.log(k) - 1e-5);
    }

    // 2只小猪 4轮一共可以校验5^2 = 25桶水
    //       t1  t2  t3  t4  不喝
    //  t1    1   2   3   4    5
    //  t2    6   7   8   9   10
    //  t3   11  12  13  14   15
    //  t4   16  17  18  19   20
    //  不喝  21  22  23  24  25
    // 每一轮，猪1按行对应的桶号喝，猪2按列对应的行号喝，各喝4轮
    // 如果8有毒 猪1t2死，猪2 t3死
    // 排列组合
    public int poorPigsDP(int buckets, int minutesToDie, int minutesToTest) {
        if (buckets == 1) {
            return 0;
        }
        int[][] combinations = new int[buckets + 1][buckets + 1];
        combinations[0][0] = 1;
        int iterations = minutesToTest / minutesToDie;
        int[][] f = new int[buckets][iterations + 1];
        for (int i = 0; i < buckets; i++) {
            f[i][0] = 1;
        }
        for (int j = 0; j <= iterations; j++) {
            f[0][j] = 1;
        }
        for (int i = 1; i < buckets; i++) {
            combinations[i][0] = 1;
            combinations[i][i] = 1;
            for (int j = 1; j < i; j++) {
                combinations[i][j] = combinations[i - 1][j - 1] + combinations[i - 1][j];
            }
            for (int j = 1; j <= iterations; j++) {
                for (int k = 0; k <= i; k++) {
                    f[i][j] += f[k][j - 1] * combinations[i][i - k];
                }
            }
            if (f[i][iterations] >= buckets) {
                return i;
            }
        }
        return 0;
    }

    // 672 灯泡开关2
    public int flipLights(int n, int k) {
        if (k == 0) return 1;
        if (n == 1) return 2;
        else if (n == 2) return k == 1 ? 3 : 4;
        else return k == 1 ? 4 : k == 2 ? 7 : 8;
    }

    //1753. 移除石子的最大得分
    // 设a<=b<=c
    //1. 若a+b<=c,用c分别和a,b匹,直到把a,b取完,此时ans=a+b
    //2. 若a+b>c, 理解1: c肯定能被取完,ans=c+ 每次取a,b 1次,共取k次 直到a+b<=c
    // 递归做法
    // 数学推导: 最后得分 :a-k+b-k+k=s a,b取k次得分k,满足a-k+b-k<=c时的得分a-k+b-k => k=a+b-s
    //          a+b减了2k后比c小:a+b-2k<=c => k>=(a+b-c)/2
    //  => a+b-s>=(a+b-c)/2   =>s<=(a+b+c)/2
    // 理解2: c每次和a,b中多的一个匹配,这样匹配完的时候,a,b相等或相差1
    // (c先和多的b的匹,直到b=a,下一次和a匹,这样a比b少1,再下一次和b匹,这样ab相等),
    // a,b两两匹配得分是 (a_remain+b_remain)/2,设c和a匹了k1次,和b匹了k2次,k1+k2=c
    // k1+k2+(a-k1+b-k2)/2 = s  => (a+b+c)/2 = s
    public int maximumScore1(int a, int b, int c) {
        int[] nums = new int[]{a, b, c};
        Arrays.sort(nums);
        if (nums[0] + nums[1] <= nums[2]) {
            return nums[0] + nums[1];
        }
        return maximumScore1(nums[0] - 1, nums[1] - 1, nums[2]) + 1;
    }

    public int maximumScore(int a, int b, int c) {
        int sum = a + b + c;
        int max = Math.max(Math.max(a, b), c);
        // sum-max = 较小的两数之和
        return Math.min(sum / 2, sum - max);
    }

    public int maximumScorePriorityQueue(int a, int b, int c) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((o1, o2) -> o2 - o1);
        pq.offer(a);
        pq.offer(b);
        pq.offer(c);
        int ans = 0;
        // pq是未空的堆的数量,>1说明空堆数小于2,不满足游戏结束条件
        while (pq.size() > 1) {
            int t1 = pq.poll();
            int t2 = pq.poll();
            t1--;
            t2--;
            ans++;
            if (t1 > 0) pq.offer(t1);
            if (t2 > 0) pq.offer(t2);
        }
        return ans;
    }

    // 2335. 装满杯子需要的最短总时长
    public int fillCups(int[] amount) {
        Arrays.sort(amount);
        if (amount[2] > amount[1] + amount[0]) {
            return amount[2];
        }
        return (amount[0] + amount[1] + amount[2] + 1) / 2;
    }

    //1040 移动石子直到连续 II 思维题 medium top1
    public int[] numMovesStonesII(int[] stones) {
        int n = stones.length;
        Arrays.sort(stones);
        if (stones[n - 1] - stones[0] + 1 - n == 0) return new int[]{0, 0};
        // 每次把最左侧/右侧 移到最近的坑位，这样s[0]和s[1]间/s[n-1]和s[n-2]的坑位被丢弃
        // 最左侧/右侧连续两个相连，滚动把最左侧/右侧的依次往中间坑位移动，等价于剩余坑位数 = 一共可以移stones[n - 1] - stones[1] + 1-(n-1)次
        // 等价于总坑位数-左右侧两个最小的坑位数
        int max = Math.max(stones[n - 2] - stones[0] + 1, stones[n - 1] - stones[1] + 1) - (n - 1);
//        max = (stones[n - 1] - stones[0] + 1 - n) - Math.min(stones[2] - stones[1] + 1 - 2, stones[n - 1] - stones[n - 2] + 1 - 2);
        int min = n;
        // 设定一个大小为n的滑动窗口，使得窗口内石子最多，剩余的坑位 即为需要移动的最小次数
        for (int i = 0, j = 0; i < n; i++) {
            while (j + 1 < n && stones[j + 1] - stones[i] + 1 <= n) {
                j++;
            }
            int cost = n - (j - i + 1);
            // 特殊情况 n-1个连续,此时要移2次
            if (j - i + 1 == n - 1 && stones[j] - stones[i] + 1 == n - 1) {
                cost = 2;
            }
            min = Math.min(cost, min);
        }
        return new int[]{min, max};
    }

    //2654. 使数组所有元素变成 1 的最少操作次数
    public int minOperations2654(int[] nums) {
        int n = nums.length, gcdAll = 0, cnt1 = 0;
        for (int x : nums) {
            gcdAll = gcd(gcdAll, x);
            if (x == 1) ++cnt1;
        }
        if (gcdAll > 1) return -1;
        if (cnt1 > 0) return n - cnt1;

        int minSize = n;
        for (int i = 0; i < n; ++i) {
            int g = 0;
            for (int j = i; j < n; ++j) {
                g = gcd(g, nums[j]);
                if (g == 1) {
                    // 这里本来是 j-i+1，把 +1 提出来合并到 return 中
                    minSize = Math.min(minSize, j - i + 1);
                    break;
                }
            }
        }
        return (minSize - 1) + (n - 1);
    }

    private int gcd(int x, int y) {
        return y > 0 ? gcd(y, x % y) : x;
    }


    //2683. 相邻值的按位异或
    // a^a=0;
    //a^b=c 两边同时异或a=》 b= c^a
    public boolean doesValidArrayExist(int[] derived) {
        int x = 0;
        for (int num : derived) {
            x ^= num;
        }
        return x == 0;
    }

    //2681. 英雄的力量
    // (a+b) mod m = ((a mod m) + (b mod m)) mod m
    // (a*b) mod m = ((a mod m) * (b mod m)) mod m
    // 证明：设a=k1m+r1,b=k2m+r2
    // (a+b) % m = ((k1+k2)m+(r1+r2)) %m = (r1+r2) % m = ((a%m)+(b%m))%m
    // (a*b)%m = (k1k2m^2+(k1r2+k2r1)m+r1r2)%m = (r1r2)%m = (a%m)(b%m)%m
    // 先排序，a,b,c,d,e 设枚举到d,此时a作为最小值，贡献有：b、c选与不选 2^2种方案
    // c 有 2^1,d有2^0
    // 设s = a*2^2+b*2^1+c*2^0
    // 那么d及其左侧元素对答案的贡献=d^3+d^2*s=d^2*(d+s)
    // 枚举到e：newS = a*2^3+b*2^2+c*2^1+d^0 = 2(a*2^2+b*2^1+c*2^0)+d = 2*s+d
    public int sumOfPower(int[] nums) {
        final long MOD = (long) 1e9 + 7;
        Arrays.sort(nums);
        long ans = 0, s = 0;
        for (long x : nums) {
            ans = (ans + x * x % MOD * (x + s)) % MOD; // 中间模一次防止溢出
            s = (s * 2 + x) % MOD;
        }
        return (int) ans;
    }
    //----------------------------------------------------- 数学--------------------------------------------

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

    // long 大数越界问题
    //2550. 猴子碰撞的方法数
    public int monkeyMove(int n) {
        int mod = (int) 1e9 + 7;
        long ans = 1;
        long x = 2;
        while (n > 0) {
            if ((n & 1) == 1) {
                ans = ans * x % mod;
            }
            n >>= 1;
            x = x * x % mod;
        }
        ans = (ans - 2 + mod) % mod;
        return (int) ans;
    }

    //2575. 找出字符串的可整除数组
    public int[] divisibilityArray(String word, int m) {
        int n = word.length();
        int[] div = new int[n];
        long s = 0;
        for (int i = 0; i < n; i++) {
            s = (s * 10 + word.charAt(i) - '0') % m;
            if (s == 0) div[i] = 1;
        }
        return div;
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

    //335. 路径交叉
    public boolean isSelfCrossing(int[] d) {
        int n = d.length;
        if (n < 4) return false;
        for (int i = 3; i < n; i++) {
            if (d[i] >= d[i - 2] && d[i - 1] <= d[i - 3]) return true;
            if (i >= 4 && d[i - 1] == d[i - 3] && d[i] + d[i - 4] >= d[i - 2]) return true;
            if (i >= 5 && d[i - 1] <= d[i - 3] && d[i - 2] > d[i - 4] && d[i] + d[i - 4] >= d[i - 2] && d[i - 1] + d[i - 5] >= d[i - 3]) return true;
        }
        return false;
    }

    //1401. 圆和矩形是否有重叠
    public boolean checkOverlap(int radius, int xCenter, int yCenter, int x1, int y1, int x2, int y2) {
        /* 圆心在矩形内部 */
        if (x1 <= xCenter && xCenter <= x2 && y1 <= yCenter && yCenter <= y2) {
            return true;
        }
        /* 圆心在矩形上部 */
        if (x1 <= xCenter && xCenter <= x2 && y2 <= yCenter && yCenter <= y2 + radius) {
            return true;
        }
        /* 圆心在矩形下部 */
        if (x1 <= xCenter && xCenter <= x2 && y1 - radius <= yCenter && yCenter <= y1) {
            return true;
        }
        /* 圆心在矩形左部 */
        if (x1 - radius <= xCenter && xCenter <= x1 && y1 <= yCenter && yCenter <= y2) {
            return true;
        }
        /* 圆心在矩形右部 */
        if (x2 <= xCenter && xCenter <= x2 + radius && y1 <= yCenter && yCenter <= y2) {
            return true;
        }
        /* 矩形左上角 */
        if (distance(xCenter, yCenter, x1, y2) <= radius * radius) {
            return true;
        }
        /* 矩形左下角 */
        if (distance(xCenter, yCenter, x1, y1) <= radius * radius) {
            return true;
        }
        /* 矩形右上角 */
        if (distance(xCenter, yCenter, x2, y2) <= radius * radius) {
            return true;
        }
        /* 矩形右下角 */
        if (distance(xCenter, yCenter, x1, y2) <= radius * radius) {
            return true;
        }
        /* 无交点 */
        return false;
    }

    public long distance(int ux, int uy, int vx, int vy) {
        return (long) Math.pow(ux - vx, 2) + (long) Math.pow(uy - vy, 2);
    }

    //1828. 统计一个圆中点的数目
    public int[] countPoints(int[][] points, int[][] queries) {
        int m = points.length, n = queries.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; ++i) {
            int cx = queries[i][0], cy = queries[i][1], cr = queries[i][2];
            for (int j = 0; j < m; ++j) {
                int px = points[j][0], py = points[j][1];
                if ((cx - px) * (cx - px) + (cy - py) * (cy - py) <= cr * cr) {
                    ++ans[i];
                }
            }
        }
        return ans;
    }

    //1250. 检查「好数组」
    public boolean isGoodArray(int[] nums) {
        int divisor = nums[0];
        for (int num : nums) {
            divisor = gcd(divisor, num);
            if (divisor == 1) {
                break;
            }
        }
        return divisor == 1;
    }

    //LCP 06. 拿硬币
    public int minCount(int[] coins) {
        int ans = 0;
        for (int coin : coins) {
            ans += (coin + 1) / 2;
        }
        return ans;
    }

    //1726. 同积元组
    public int tupleSameProduct(int[] nums) {
        int n = nums.length;
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for(int j = i + 1; j < n; j++) {
                int key = nums[i] * nums[j];
                cnt.put(key, cnt.getOrDefault(key, 0) + 1);
            }
        }
        int ans = 0;
        for (Integer v : cnt.values()) {
            ans += v * (v - 1) * 4;
        }
        return ans;
    }

    //1276. 不浪费原料的汉堡制作方案
    public List<Integer> numOfBurgers(int tomatoSlices, int cheeseSlices) {
        int x = (tomatoSlices - 2 * cheeseSlices) / 2;
        int y = (4 * cheeseSlices - tomatoSlices) / 2;
        if(x>=0 && y>=0){
            if ((4 * x + 2 * y) == tomatoSlices && (x + y) == cheeseSlices) {
                return Arrays.asList(x, y);
            }
        }

        return new ArrayList<>();
    }

    //1954. 收集足够苹果的最小花园周长
    public long minimumPerimeter(long neededApples) {
        long n = 1;
        while (2 * n * (n + 1) * (2 * n + 1) < neededApples) {
            n++;
        }
        return n * 8;
    }

    //2171. 拿出最少数目的魔法豆
    public long minimumRemoval(int[] beans) {
        int n = beans.length;
        Arrays.sort(beans);
        long sum = 0;
        for (int bean : beans) {
            sum += bean;
        }
        long min = Long.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            long tmp = sum - ((long) (n - i)) * ((long) beans[i]);
            min = Math.min(min, tmp);
        }
        return min;
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


    // 面试题 16.13. 平分正方形
    public double[] cutSquares(int[] square1, int[] square2) {
        //第一个正方形的中心点，x,y坐标及正方形边长
        double x1 = square1[0] + square1[2] / 2.0;
        double y1 = square1[1] + square1[2] / 2.0;
        int d1 = square1[2];
        //第二个正方形的中心点，x,y坐标及正方形边长
        double x2 = square2[0] + square2[2] / 2.0;
        double y2 = square2[1] + square2[2] / 2.0;
        int d2 = square2[2];
        //结果集
        double[] res = new double[4];
        //两个中心坐标在同一条x轴上，此时两条直线的斜率都是无穷大
        if (x1 == x2) {
            res[0] = x1;
            res[1] = Math.min(square1[1], square2[1]);
            res[2] = x1;
            res[3] = Math.max(square1[1] + d1, square2[1] + d2);
        } else {
            //斜率存在，则计算斜率和系数，y = kx + b;
            double k = (y1 - y2) / (x1 - x2);//斜率计算公式
            double b = y1 - k * x1;
            //斜率绝对值大于1，说明与正方形的上边和下边相交
            if (Math.abs(k) > 1) {
                //先计算底边，也就是两个正方形左下坐标y的最小值
                res[1] = Math.min(square1[1], square2[1]);
                res[0] = (res[1] - b) / k;
                //再计算顶边，也就是两个正方形左下坐标y+边长的最大值
                res[3] = Math.max(square1[1] + d1, square2[1] + d2);
                res[2] = (res[3] - b) / k;
            } else {
                //斜率绝对值小于等于1，说明与正方形的左边和右边相交，同理
                res[0] = Math.min(square1[0], square2[0]);
                res[1] = res[0] * k + b;
                res[2] = Math.max(square1[0] + d1, square2[0] + d2);
                res[3] = res[2] * k + b;
            }
        }
        //题目要求x1 < x2,如果结果不满足，我们交换两个点的坐标即可
        if (res[0] > res[2]) {
            swap(res, 0, 2);
            swap(res, 1, 3);
        }
        return res;
    }

    public void swap(double[] res, int x, int y) {
        double temp = res[x];
        res[x] = res[y];
        res[y] = temp;
    }

    // 面试16.14 最佳直线
    // 两个点(a1,b1)和(a2,b2)的斜率k1=(b2-b1)/(a2-a1),记x1= a2-a1,y1=b2-b1 => k1=y1/x1
    // 第三个点(a3,b3)和(a1,b1)的斜率k2=(b3-b1)/(a3-a1),记x2=a3-a1,y2=b3-b1 => k2=y2/x2
    // 若三个点在直线上,则k1=k2 => x1y2=x2y1 乘法避免除数为0的情况
    public int[] bestLine(int[][] points) {
        int n = points.length;
        int[] ans = new int[2];
        int max = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int cnt = 2;
                long x1 = points[i][0] - points[j][0];
                long y1 = points[i][1] - points[j][1];
                for (int k = j + 1; k < n; k++) {
                    long x2 = points[i][0] - points[k][0];
                    long y2 = points[i][1] - points[k][1];
                    if (x1 * y2 == x2 * y1) {
                        cnt++;
                    }
                }
                if (cnt > max) {
                    max = cnt;
                    ans[0] = i;
                    ans[1] = j;
                }
            }
        }
        return ans;
    }

    //面试题 16.03. 交点
    double[] ans = new double[0];

    public double[] intersection(int[] start1, int[] end1, int[] start2, int[] end2) {
        int x1 = start1[0], y1 = start1[1];
        int x2 = end1[0], y2 = end1[1];
        int x3 = start2[0], y3 = start2[1];
        int x4 = end2[0], y4 = end2[1];

        // 判断 (x1, y1)~(x2, y2) 和 (x3, y3)~(x4, y4) 是否平行
        if ((y4 - y3) * (x2 - x1) == (y2 - y1) * (x4 - x3)) {
            // 若平行，则判断 (x3, y3) 是否在「直线」(x1, y1)~(x2, y2) 上
            if ((y2 - y1) * (x3 - x1) == (y3 - y1) * (x2 - x1)) {
                // 判断 (x3, y3) 是否在「线段」(x1, y1)~(x2, y2) 上
                if (inside(x1, y1, x2, y2, x3, y3)) {
                    update(x3, y3);
                }
                // 判断 (x4, y4) 是否在「线段」(x1, y1)~(x2, y2) 上
                if (inside(x1, y1, x2, y2, x4, y4)) {
                    update(x4, y4);
                }
                // 判断 (x1, y1) 是否在「线段」(x3, y3)~(x4, y4) 上
                if (inside(x3, y3, x4, y4, x1, y1)) {
                    update(x1, y1);
                }
                // 判断 (x2, y2) 是否在「线段」(x3, y3)~(x4, y4) 上
                if (inside(x3, y3, x4, y4, x2, y2)) {
                    update(x2, y2);
                }
            }
            // 在平行时，其余的所有情况都不会有交点
        } else {
            // 联立方程得到 t1 和 t2 的值
            double t1 = (double) (x3 * (y4 - y3) + y1 * (x4 - x3) - y3 * (x4 - x3) - x1 * (y4 - y3)) / ((x2 - x1) * (y4 - y3) - (x4 - x3) * (y2 - y1));
            double t2 = (double) (x1 * (y2 - y1) + y3 * (x2 - x1) - y1 * (x2 - x1) - x3 * (y2 - y1)) / ((x4 - x3) * (y2 - y1) - (x2 - x1) * (y4 - y3));
            // 判断 t1 和 t2 是否均在 [0, 1] 之间
            if (t1 >= 0.0 && t1 <= 1.0 && t2 >= 0.0 && t2 <= 1.0) {
                ans = new double[]{x1 + t1 * (x2 - x1), y1 + t1 * (y2 - y1)};
            }
        }
        return ans;
    }

    // 判断 (xk, yk) 是否在「线段」(x1, y1)~(x2, y2) 上
    // 这里的前提是 (xk, yk) 一定在「直线」(x1, y1)~(x2, y2) 上
    public boolean inside(int x1, int y1, int x2, int y2, int xk, int yk) {
        // 若与 x 轴平行，只需要判断 x 的部分
        // 若与 y 轴平行，只需要判断 y 的部分
        // 若为普通线段，则都要判断
        return (x1 == x2 || (Math.min(x1, x2) <= xk && xk <= Math.max(x1, x2))) && (y1 == y2 || (Math.min(y1, y2) <= yk && yk <= Math.max(y1, y2)));
    }

    public void update(double xk, double yk) {
        // 将一个交点与当前 ans 中的结果进行比较
        // 若更优则替换
        if (ans.length == 0 || xk < ans[0] || (xk == ans[0] && yk < ans[1])) {
            ans = new double[]{xk, yk};
        }
    }

    //1739. 放置盒子
    public int minimumBoxes(int n) {
        int cur = 0, i = 0, j = 0;
        // cur 总个数  i地面的个数  j每次地面增加的个数
        // 1 + (1+2) + (1+2+3)
        while (cur < n) {
            j++;
            i += j;
            cur += i;
        }
        if (cur == n) return i;
        // 此时cur>n,先恢复到cur<n的情况
        cur -= i;
        i -= j;
        j = 0;
        // 此时添加方块参考
        while (cur < n) {
            j++;
            cur += j;
        }
        return i + j;
    }

    //1819. 序列中不同最大公约数的数目
    public int countDifferentSubsequenceGCDs(int[] nums) {
        int maxVal = Arrays.stream(nums).max().getAsInt();
        boolean[] occured = new boolean[maxVal + 1];
        for (int num : nums) {
            occured[num] = true;
        }
        int ans = 0;
        for (int i = 1; i <= maxVal; i++) {
            int subGcd = 0;
            for (int j = i; j <= maxVal; j += i) {
                if (occured[j]) {
                    if (subGcd == 0) {
                        subGcd = j;
                    } else {
                        subGcd = gcd(subGcd, j);
                    }
                    if (subGcd == i) {
                        ans++;
                        break;
                    }
                }
            }
        }
        return ans;
    }

    //endregion-----------------------------------------------------------------------------------------------
}
