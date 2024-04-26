import javafx.util.Pair;

import java.util.*;
import java.util.stream.IntStream;

public class SolutionGraphAdvance {
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

    //2368. 受限条件下可到达节点的数目
    public int reachableNodes(int n, int[][] edges, int[] restricted) {
        Map<Integer,List<Integer>> map = new HashMap<>();
        for(int[] edge:edges){
            List<Integer> list = map.getOrDefault(edge[0],new ArrayList<>());
            list.add(edge[1]);
            map.put(edge[0],list);

            List<Integer> list2 = map.getOrDefault(edge[1],new ArrayList<>());
            list2.add(edge[0]);
            map.put(edge[1],list2);
        }
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(0);
        boolean[] visited = new boolean[n];
        visited[0] = true;
        for(int r:restricted){
            visited[r] = true;
        }
        int cnt=1;
        while(!queue.isEmpty()){
            int cur = queue.poll();
            for(int next:map.getOrDefault(cur,new ArrayList<>())){
                if(!visited[next]){
                    visited[next] = true;
                    queue.offer(next);
                    cnt++;
                }
            }
        }
        return cnt;
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

    //1334. 阈值距离内邻居最少的城市
    //使用 Floyd 算法得到任意两点之间的最短路，然后统计满足条件的邻居数量
    public int findTheCity(int n, int[][] edges, int distanceThreshold) {
        int MAX = Integer.MAX_VALUE / 2;
        int[] ans = new int[]{MAX, -1};
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(matrix[i], MAX);
        }
        for (int[] edge : edges) {
            int from = edge[0], to = edge[1], weight = edge[2];
            matrix[from][to] = matrix[to][from] = weight;
        }
        for (int k = 0; k < n; k++) {
            matrix[k][k] = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = Math.min(matrix[i][j], matrix[i][k] + matrix[k][j]);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            int cnt = 0;
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] <= distanceThreshold) {
                    cnt++;
                }
            }
            if (cnt <= ans[0]) {
                ans[0] = cnt;
                ans[1] = i;
            }
        }
        return ans[1];
    }
    //对每一个节点求解单源最短路，即某一个节点到其它所有节点的最短距离。
    //朴素的 Dijkstra 算法不断使用距离最近的节点来松弛到其它节点的距离。
    public int findTheCity2(int n, int[][] edges, int distanceThreshold) {
        int MAX = Integer.MAX_VALUE / 2;
        int[] ans = new int[]{MAX, -1};
        int[][] dis = new int[n][n];
        boolean[][] visited = new boolean[n][n];
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dis[i], MAX);
            Arrays.fill(matrix[i], MAX);
        }
        for (int[] edge : edges) {
            int from = edge[0], to = edge[1], weight = edge[2];
            matrix[from][to] = matrix[to][from] = weight;
        }
        for (int i = 0; i < n; i++) {
            dis[i][i] = 0;
            for (int k = 0; k < n; k++) {
                // 寻找i周围最短的边
                int t = -1;//距离i最近的点t
                for (int j = 0; j < n; j++) {
                    if (!visited[i][j] && (t == -1 || dis[i][j] < dis[i][t])) {
                        t = j;
                    }
                }
                //
                for (int j = 0; j < n; j++) {
                    dis[i][j] = Math.min(dis[i][j], dis[i][t] + matrix[t][j]);
                }
                visited[i][t] = true;
            }
        }
        for (int i = 0; i < n; i++) {
            int cnt = 0;
            for (int j = 0; j < n; j++) {
                if (dis[i][j] <= distanceThreshold) {
                    cnt++;
                }
            }
            if (cnt <= ans[0]) {
                ans[0] = cnt;
                ans[1] = i;
            }
        }
        return ans[1];
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

    //2699. 修改图中的边权
    public int[][] modifiedGraphEdges(int n, int[][] edges, int source, int destination, int target) {
        List<int[]> g[] = new ArrayList[n];
        Arrays.setAll(g, e -> new ArrayList<>());
        for (int i = 0; i < edges.length; i++) {
            int x = edges[i][0], y = edges[i][1];
            g[x].add(new int[]{y, i});
            g[y].add(new int[]{x, i}); // 建图，额外记录边的编号
        }

        int[][] dis = new int[n][2];
        for (int i = 0; i < n; i++)
            if (i != source)
                dis[i][0] = dis[i][1] = Integer.MAX_VALUE;

        dijkstra(g, edges, destination, dis, 0, 0);
        int delta = target - dis[destination][0];
        if (delta < 0) // -1 全改为 1 时，最短路比 target 还大
            return new int[][]{};

        dijkstra(g, edges, destination, dis, delta, 1);
        if (dis[destination][1] < target) // 最短路无法再变大，无法达到 target
            return new int[][]{};

        for (int[] e : edges)
            if (e[2] == -1) // 剩余没修改的边全部改成 1
                e[2] = 1;
        return edges;
    }

    // 朴素 Dijkstra 算法
    // 这里 k 表示第一次/第二次
    private void dijkstra(List<int[]> g[], int[][] edges, int destination, int[][] dis, int delta, int k) {
        int n = g.length;
        boolean[] vis = new boolean[n];
        for (; ; ) {
            // 找到当前最短路，去更新它的邻居的最短路
            // 根据数学归纳法，dis[x][k] 一定是最短路长度
            int x = -1;
            for (int i = 0; i < n; ++i)
                if (!vis[i] && (x < 0 || dis[i][k] < dis[x][k]))
                    x = i;
            if (x == destination) // 起点 source 到终点 destination 的最短路已确定
                return;
            vis[x] = true; // 标记，在后续的循环中无需反复更新 x 到其余点的最短路长度
            for (int[] e : g[x]) {
                int y = e[0], eid = e[1];
                int wt = edges[eid][2];
                if (wt == -1)
                    wt = 1; // -1 改成 1
                if (k == 1 && edges[eid][2] == -1) {
                    // 第二次 Dijkstra，改成 w
                    int w = delta + dis[y][0] - dis[x][1];
                    if (w > wt)
                        edges[eid][2] = wt = w; // 直接在 edges 上修改
                }
                // 更新最短路
                dis[y][k] = Math.min(dis[y][k], dis[x][k] + wt);
            }
        }
    }

    //1976. 到达目的地的方案数
    public int countPaths(int n, int[][] roads) {
        int mod = (int) 1e9 + 7;
        Map<Integer, List<int[]>> map = new HashMap<>();
        for (int[] road : roads) {
            List<int[]> ls = map.getOrDefault(road[0], new ArrayList<>());
            ls.add(new int[]{road[1], road[2]});
            map.put(road[0], ls);

            List<int[]> ls2 = map.getOrDefault(road[1], new ArrayList<>());
            ls2.add(new int[]{road[0], road[2]});
            map.put(road[1], ls2);
        }
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(o -> o[1]));
        pq.offer(new long[]{0, 0});
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[0] = 0;
        int[] ways = new int[n];
        ways[0] = 1;
        while (!pq.isEmpty()) {
            long[] cell = pq.poll();
            int x = (int) cell[0];
            long d = cell[1];
            List<int[]> neighbors = map.getOrDefault(x, new ArrayList<>());
            for (int[] neighbor : neighbors) {
                int y = neighbor[0], t = neighbor[1];
                long nd = t + d;
                if (nd < dist[y]) {
                    dist[y] = nd;
                    ways[y] = ways[x];
                    pq.offer(new long[]{y, nd});
                } else if (nd == dist[y]) {
                    ways[y] = (int) ((long) ways[x] + (long) ways[y]) % mod;
                }
            }
        }
        return ways[n - 1];
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
        SolutionUnionFind.UnionFind1 unionFind = new SolutionUnionFind.UnionFind1(row * col);
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
        SolutionUnionFind.UnionFind1 unionFind = new SolutionUnionFind.UnionFind1(N * N);

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
        SolutionUnionFind.UnionFind1 unionFind = new SolutionUnionFind.UnionFind1(N * N);

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
    int[][] dirs = new int[][]{{1,0},{0,1},{-1,0},{0,-1}};
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

    //630. 课程表 III
    public int scheduleCourse(int[][] courses) {
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);
        PriorityQueue<Integer> q = new PriorityQueue<>((a, b) -> b - a);
        int sum = 0;
        for (int[] c : courses) {
            int d = c[0], e = c[1];
            sum += d;
            q.add(d);
            if (sum > e) sum -= q.poll();
        }
        return q.size();
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
        SolutionUnionFind.UnionFind1 uf = new SolutionUnionFind.UnionFind1(graph.length);
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

    //2050. 并行课程 III
    public int minimumTime(int n, int[][] relations, int[] time) {
        List<Integer>[] g = new List[n];
        Arrays.setAll(g, k -> new ArrayList<>());
        int[] indeg = new int[n];
        for (int[] e : relations) {
            int a = e[0] - 1, b = e[1] - 1;
            g[a].add(b);
            ++indeg[b];
        }
        Deque<Integer> q = new ArrayDeque<>();
        int[] f = new int[n];
        int ans = 0;
        for (int i = 0; i < n; ++i) {
            int v = indeg[i], t = time[i];
            if (v == 0) {
                q.offer(i);
                f[i] = t;
                ans = Math.max(ans, t);
            }
        }
        while (!q.isEmpty()) {
            int i = q.pollFirst();
            for (int j : g[i]) {
                f[j] = Math.max(f[j], f[i] + time[j]);
                ans = Math.max(ans, f[j]);
                if (--indeg[j] == 0) {
                    q.offer(j);
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

    //1761. 一个图中连通三元组的最小度数 枚举
    public int minTrioDegree(int n, int[][] edges) {
        // 原图
        Set<Integer>[] g = new Set[n];
        for (int i = 0; i < n; ++i) {
            g[i] = new HashSet<Integer>();
        }
        // 定向后的图
        List<Integer>[] h = new List[n];
        for (int i = 0; i < n; ++i) {
            h[i] = new ArrayList<Integer>();
        }
        int[] degree = new int[n];

        for (int[] edge : edges) {
            int x = edge[0] - 1, y = edge[1] - 1;
            g[x].add(y);
            g[y].add(x);
            ++degree[x];
            ++degree[y];
        }
        for (int[] edge : edges) {
            int x = edge[0] - 1, y = edge[1] - 1;
            if (degree[x] < degree[y] || (degree[x] == degree[y] && x < y)) {
                h[x].add(y);
            } else {
                h[y].add(x);
            }
        }

        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < n; ++i) {
            for (int j : h[i]) {
                for (int k : h[j]) {
                    if (g[i].contains(k)) {
                        ans = Math.min(ans, degree[i] + degree[j] + degree[k] - 6);
                    }
                }
            }
        }

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    //2127. 参加会议的最多员工数
    public int maximumInvitations(int[] favorite) {
        int n = favorite.length;
        int[] deg = new int[n];
        for (int f : favorite) {
            deg[f]++; // 统计基环树每个节点的入度
        }

        int[] maxDepth = new int[n];
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (deg[i] == 0) {
                q.add(i);
            }
        }
        while (!q.isEmpty()) { // 拓扑排序，剪掉图上所有树枝
            int x = q.poll();
            int y = favorite[x]; // x 只有一条出边
            maxDepth[y] = maxDepth[x] + 1;
            if (--deg[y] == 0) {
                q.add(y);
            }
        }

        int maxRingSize = 0, sumChainSize = 0;
        for (int i = 0; i < n; i++) {
            if (deg[i] == 0) continue;

            // 遍历基环上的点
            deg[i] = 0; // 将基环上的点的入度标记为 0，避免重复访问
            int ringSize = 1; // 基环长度
            for (int x = favorite[i]; x != i; x = favorite[x]) {
                deg[x] = 0; // 将基环上的点的入度标记为 0，避免重复访问
                ringSize++;
            }

            if (ringSize == 2) { // 基环长度为 2
                sumChainSize += maxDepth[i] + maxDepth[favorite[i]] + 2; // 累加两条最长链的长度
            } else {
                maxRingSize = Math.max(maxRingSize, ringSize); // 取所有基环长度的最大值
            }
        }
        return Math.max(maxRingSize, sumChainSize);
    }

    //2603. 收集树中金币
    public int collectTheCoins(int[] coins, int[][] edges) {
        int n = coins.length;
        List<Integer>[] g = new List[n];
        for (int i = 0; i < n; ++i) {
            g[i] = new ArrayList<Integer>();
        }
        int[] degree = new int[n];
        for (int[] edge : edges) {
            int x = edge[0], y = edge[1];
            g[x].add(y);
            g[y].add(x);
            ++degree[x];
            ++degree[y];
        }

        int rest = n;
        /* 删除树中所有无金币的叶子节点，直到树中所有的叶子节点都是含有金币的 */
        Queue<Integer> queue = new ArrayDeque<Integer>();
        for (int i = 0; i < n; ++i) {
            if (degree[i] == 1 && coins[i] == 0) {
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            int u = queue.poll();
            --degree[u];
            --rest;
            for (int v : g[u]) {
                --degree[v];
                if (degree[v] == 1 && coins[v] == 0) {
                    queue.offer(v);
                }
            }
        }
        /* 删除树中所有的叶子节点, 连续删除2次 */
        for (int x = 0; x < 2; ++x) {
            queue = new ArrayDeque<Integer>();
            for (int i = 0; i < n; ++i) {
                if (degree[i] == 1) {
                    queue.offer(i);
                }
            }
            while (!queue.isEmpty()) {
                int u = queue.poll();
                --degree[u];
                --rest;
                for (int v : g[u]) {
                    --degree[v];
                }
            }
        }

        return rest == 0 ? 0 : (rest - 1) * 2;
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


    //region-------------------------------------------匈牙利算法/KM算法-------------------------------------------------
    //1947. 最大兼容性评分和
    int[] lx;
    int[] ly;
    boolean[] sx;
    boolean[] sy;
    int[] match;
    int[] slack;
    int[][] points;
    int m;

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
    int n;
    int[] p;
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
}
