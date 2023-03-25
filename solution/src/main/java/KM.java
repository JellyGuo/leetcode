import java.util.Arrays;

public class KM {
    int m;
    int n;
    // 有权重的二分图
    int[] weightX; //左侧最大权值
    int[] weightY; //右侧权重，起始为0
    boolean[] visitedX; // 左侧是否访问过
    boolean[] visitedY; // 右侧是否访问过
    int[] match; // 每一个右侧元素匹配的左侧元素
    int[] slack; // 匹配矛盾时，左侧元素减少的最小值，即右侧元素增加值
    int[][] matrix;

    public KM(int[][] grid) {
        this.matrix = grid;
        this.m = grid.length;
        this.n = grid[0].length;
        weightX = new int[m];
        weightY = new int[n];
        visitedX = new boolean[m];
        visitedY = new boolean[n];
        match = new int[n];
        slack = new int[n];
    }

    // 获取最大匹配值
    public int getKM() {
        Arrays.fill(weightX, Integer.MAX_VALUE);
        Arrays.fill(weightY, 0);
        Arrays.fill(match, -1);
        for (int i = 0; i < m; i++) {
            weightX[i] = Arrays.stream(matrix[i]).max().getAsInt();
        }
        for (int i = 0; i < m; i++) {
            while (true) {
                Arrays.fill(visitedX, false);
                Arrays.fill(visitedY, false);
                if (matchKM(i)) break;
                int min = Integer.MAX_VALUE;
                //写法1 双循环
                // 一开始，最大匹配值=每个X的最大权重和（此时不一定都能匹配上)
                // 当有匹配不上的时候，最大权重值必然减少
                // 双重循环，遍历左边可以减少的最小值（枚举当前路径上的每一个X，可以减少的最小值=最大权重-次大权重
//                for (int x = 0; x < m; x++) {
//                    if (visitedX[x]) {
//                        for (int y = 0; y < n; y++) {
//                            if (!visitedY[y]) {// 已经访问过的y已经是满足最大的，减vis过的y会使min=0
//                                min = Math.min(min, weightX[x] + weightY[y] - matrix[x][y]);
//                            }
//                        }
//                    }
//                }
                for (int y = 0; y < n; y++) {
                    if (!visitedY[y]) min = Math.min(slack[y], min);
                }
                for (int x = 0; x < m; x++) {
                    if (visitedX[x]) weightX[x] -= min;
                }
                for (int y = 0; y < n; y++) {
                    if (visitedY[y]) weightY[y] += min;
                    else slack[y] -= min;// 写法1移除改行
                }
            }
        }
        int maxMatchWeight = 0;
        for (int y = 0; y < m; y++) {
            if (match[y] != -1) maxMatchWeight += matrix[match[y]][y];
        }
        return maxMatchWeight;
    }

    private boolean matchKM(int x) {
        visitedX[x] = true;
        for (int y = 0; y < n; y++) {
            if (visitedY[y]) continue;
            int gap = weightX[x] + weightY[y] - matrix[x][y];
            if (gap == 0) {
                visitedY[y] = true;
                if (match[y] == -1 || matchKM(match[y])) { //递归路径
                    match[y] = x;
                    return true;
                }
            } else {
                slack[y] = Math.min(slack[y], gap);//右侧要满足最大匹配，需要增长的最小值(满足匹配的最小期望值）
            }
        }
        return false;
    }

    // 匈牙利算法又称KM算法 求二分图最大匹配度
    // 一个二分图的最大匹配度=这个图中的最小点覆盖数
    public int hungarian(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] p = new int[n];
        Arrays.fill(p, -1);
        int ans = 0;
        for (int i = 0; i < m; i++) {
            boolean[] vis = new boolean[n];//记录右侧元素是否已经访问过
            if (match(i, grid, p, vis)) ans++;
        }
        return ans;
    }

    private boolean match(int i, int[][] grid, int[] p, boolean[] vis) {
        for (int j = 0; j < grid.length; j++) {
            if (grid[i][j] == 0) continue;
            if (vis[j]) continue;
            vis[j] = true;
            if (p[j] == -1 || match(p[j], grid, p, vis)) {
                p[j] = i;
                return true;
            }
        }
        return false;
    }

}
