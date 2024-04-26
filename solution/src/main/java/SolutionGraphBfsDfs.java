import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class SolutionGraphBfsDfs {

    //region  ---------------------------------------------图论BFS/DFS-----------------------------------------------
    int n;
    int[][] directions = new int[][]{{0,1},{1,0},{-1,0},{0,-1}};
    int row;
    int col;
    //1042. 不邻接植花 颜色标记法
    public int[] gardenNoAdj(int n, int[][] paths) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] path : paths) {
            add2map(map, path[0], path[1]);
            add2map(map, path[1], path[0]);
        }
        int[] ans = new int[n];
        for (int i = 1; i <= n; i++) {
            boolean[] colored = new boolean[5];
            for (int near : map.getOrDefault(i, new ArrayList<>())) {
                colored[ans[near - 1]] = true;
            }
            for (int j = 1; j <= 4; j++) {
                if (!colored[j]) {
                    ans[i - 1] = j;
                    break;
                }
            }
        }
        return ans;
    }

    private void add2map(Map<Integer, List<Integer>> map, int x, int y) {
        List<Integer> ls = map.getOrDefault(x, new ArrayList<>());
        ls.add(y);
        map.put(x, ls);
    }

    //1466. 重新规划路线
    public int minReorder(int n, int[][] connections) {
        List<int[]>[] e = new List[n];
        for (int i = 0; i < n; i++) {
            e[i] = new ArrayList<int[]>();
        }
        for (int[] edge : connections) {
            e[edge[0]].add(new int[]{edge[1], 1});
            e[edge[1]].add(new int[]{edge[0], 0});
        }
        return dfs(0, -1, e);
    }

    public int dfs(int x, int parent, List<int[]>[] e) {
        int res = 0;
        for (int[] edge : e[x]) {
            if (edge[0] == parent) {
                continue;
            }
            res += edge[1] + dfs(edge[0], x, e);
        }
        return res;
    }
    // 1971. 寻找图中是否存在路径
    public boolean validPath(int n, int[][] edges, int source, int destination) {
        if (source == destination) return true;
        List<Integer>[] g = new List[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            g[edge[0]].add(edge[1]);
            g[edge[1]].add(edge[0]);
        }
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(source);
        visited[source] = true;
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            for (int near : g[cur]) {
                if (near == destination) return true;
                if (!visited[near]) {
                    queue.offer(near);
                    visited[near] = true;
                }
            }
        }
        return false;
    }

    public boolean validPathUnionFind(int n, int[][] edges, int source, int destination) {
        SolutionUnionFind.UnionFind1 unionFind = new SolutionUnionFind.UnionFind1(n);
        for (int[] edge : edges) {
            unionFind.union(edge[0], edge[1]);
        }
        return unionFind.isConnect(source, destination);
    }

    //1615. 最大网络秩
    public int maximalNetworkRank(int n, int[][] roads) {
        boolean[][] connect = new boolean[n][n];
        int[] degree = new int[n];
        for (int[] road : roads) {
            connect[road[0]][road[1]] = true;
            connect[road[1]][road[0]] = true;
            degree[road[0]]++;
            degree[road[1]]++;
        }
        int max = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int sum = degree[i] + degree[j] - (connect[i][j] ? 1 : 0);
                max = Math.max(max, sum);
            }
        }
        return max;
    }

    public int maximalNetworkRank2(int n, int[][] roads) {
        boolean[][] connect = new boolean[n][n];
        int[] degree = new int[n];
        for (int[] road : roads) {
            connect[road[0]][road[1]] = true;
            connect[road[1]][road[0]] = true;
            degree[road[0]]++;
            degree[road[1]]++;
        }
        int first = -1, second = -1;
        List<Integer> firstList = new ArrayList<>();
        List<Integer> secondList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] > first) {
                second = first;
                secondList.clear();
                secondList.addAll(firstList);
                first = degree[i];
                firstList.clear();
                firstList.add(i);
            } else if (degree[i] == first) {
                firstList.add(i);
            } else if (degree[i] > second) {
                second = degree[i];
                secondList.clear();
                secondList.add(i);
            } else if (degree[i] == second) {
                secondList.add(i);
            }
        }
        if (firstList.size() == 1) {
            int u = firstList.get(0);
            for (int v : secondList) {
                if (!connect[u][v]) {
                    return first + second;
                }
            }
            return first + second - 1;
        } else {
            int m = roads.length;
            if (firstList.size() * (firstList.size() - 1) / 2 > m) {
                return first * 2;
            }
            for (int u : firstList) {
                for (int v : firstList) {
                    if (u != v && !connect[u][v]) {
                        return first * 2;
                    }
                }
            }
            return first * 2 - 1;
        }
    }

    //365. 水壶问题
    public boolean canMeasureWater(int x, int y, int z) {
        if (z <= 0) return true;
        if (x + y < z) return false;
        State initState = new State(0, 0);
        Queue<State> queue = new ArrayDeque<>();
        queue.offer(initState);
        Set<State> visited = new HashSet<>();
        visited.add(initState);
        while (!queue.isEmpty()) {
            State cur = queue.poll();
            int curX = cur.getX();
            int curY = cur.getY();
            if (curX == z || curY == z || curX + curY == z) return true;
            List<State> nextStates = nextStates(curX, curY, x, y);
            for (State state : nextStates) {
                if (visited.contains(state)) continue;
                queue.offer(state);
                visited.add(state);
            }
        }
        return false;
    }

    private List<State> nextStates(int curX, int curY, int x, int y) {
        List<State> result = new ArrayList<>();
        // 把A倒满
        if (curX < x) result.add(new State(x, curY));
        // 把B倒满
        if (curY < y) result.add(new State(curX, y));
        // 把A 清空
        if (curX > 0) result.add(new State(0, curY));
        // 把B 清空
        if (curY > 0) result.add(new State(curX, 0));
        // 把A倒入B，B装满，A剩余
        if (curX > y - curY) result.add(new State(curX - (y - curY), y));
        // 把A倒入B，B未满，A清空
        if (curX + curY < y) result.add(new State(0, curX + curY));
        // 把B倒入A，A装满，B剩余
        if (x - curX < curY) result.add(new State(x, curY - (x - curX)));
        // 把B倒入A，A未满，B清空
        if (curX + curY < x) result.add(new State(curX + curY, 0));
        return result;
    }

    private class State {
        private int x;
        private int y;

        public State(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            State state = (State) o;
            return state.getX() == x && state.getY() == y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "State{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    //1766. 互质树
    List<Integer>[] gcds;
    List<Integer>[] tmp;
    List<Integer>[] g;
    int[] dep;
    int[] ans;

    public int[] getCoprimes(int[] nums, int[][] edges) {
        int n = nums.length;

        // 初始化
        gcds = new List[51];
        tmp = new List[51];
        for (int i = 0; i <= 50; i++) {
            gcds[i] = new ArrayList<Integer>();
            tmp[i] = new ArrayList<Integer>();
        }
        ans = new int[n];
        dep = new int[n];
        Arrays.fill(ans, -1);
        Arrays.fill(dep, -1);
        g = new List[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<Integer>();
        }

        for (int i = 1; i <= 50; i++) {
            for (int j = 1; j <= 50; j++) {
                if (gcd(i, j) == 1) {
                    gcds[i].add(j);
                }
            }
        }

        for (int[] val : edges) {
            g[val[0]].add(val[1]);
            g[val[1]].add(val[0]);
        }

        dfs1766(nums, 0, 1);

        return ans;
    }

    public int gcd(int x, int y) {
        while (y != 0) {
            int temp = x;
            x = y;
            y = temp % y;
        }
        return x;
    }

    public void dfs1766(int[] nums, int x, int depth) {
        dep[x] = depth;
        for (int val : gcds[nums[x]]) {
            if (tmp[val].isEmpty()) {
                continue;
            }

            int las = tmp[val].get(tmp[val].size() - 1);
            if (ans[x] == -1 || dep[las] > dep[ans[x]]) {
                ans[x] = las;
            }
        }
        tmp[nums[x]].add(x);

        for (int val : g[x]) {
            if (dep[val] == -1) { // 被访问过的点dep不为-1
                dfs1766(nums, val, depth + 1);
            }
        }

        tmp[nums[x]].remove(tmp[nums[x]].size() - 1);
    }

    //924. 尽量减少恶意软件的传播
    public int minMalwareSpread(int[][] graph, int[] initial) {
        // 1. Color each component.
        // colors[node] = the color of this node.

        int N = graph.length;
        int[] colors = new int[N];
        Arrays.fill(colors, -1);
        int C = 0;

        for (int node = 0; node < N; ++node)
            if (colors[node] == -1)
                dfs924(graph, colors, node, C++);

        // 2. Size of each color.
        int[] size = new int[C];
        for (int color: colors)
            size[color]++;

        // 3. Find unique colors.
        int[] colorCount = new int[C];
        for (int node: initial)
            colorCount[colors[node]]++;

        // 4. Answer
        int ans = Integer.MAX_VALUE;
        for (int node: initial) {
            int c = colors[node];
            if (colorCount[c] == 1) {
                if (ans == Integer.MAX_VALUE)
                    ans = node;
                else if (size[c] > size[colors[ans]])
                    ans = node;
                else if (size[c] == size[colors[ans]] && node < ans)
                    ans = node;
            }
        }

        if (ans == Integer.MAX_VALUE)
            for (int node: initial)
                ans = Math.min(ans, node);

        return ans;
    }

    public void dfs924(int[][] graph, int[] colors, int node, int color) {
        colors[node] = color;
        for (int nei = 0; nei < graph.length; ++nei)
            if (graph[node][nei] == 1 && colors[nei] == -1)
                dfs924(graph, colors, nei, color);
    }

    //928. 尽量减少恶意软件的传播 II
    public int minMalwareSpread2(int[][] graph, int[] initial) {
        int n = graph.length;
        boolean[] initialSet = new boolean[n];
        for (int v : initial) {
            initialSet[v] = true;
        }
        List<Integer>[] infectedBy = new List[n];
        for (int i = 0; i < n; i++) {
            infectedBy[i] = new ArrayList<Integer>();
        }
        for (int v : initial) {
            boolean[] infectedSet = new boolean[n];
            dfs928(graph, initialSet, infectedSet, v);
            for (int u = 0; u < n; u++) {
                if (infectedSet[u]) {
                    infectedBy[u].add(v);
                }
            }
        }
        int[] count = new int[n];
        for (int u = 0; u < n; u++) {
            if (infectedBy[u].size() == 1) {
                count[infectedBy[u].get(0)]++;
            }
        }
        int res = initial[0];
        for (int v : initial) {
            if (count[v] > count[res] || count[v] == count[res] && v < res) {
                res = v;
            }
        }
        return res;
    }

    public void dfs928(int[][] graph, boolean[] initialSet, boolean[] infectedSet, int v) {
        int n = graph.length;
        for (int u = 0; u < n; u++) {
            if (graph[v][u] == 0 || initialSet[u] || infectedSet[u]) {
                continue;
            }
            infectedSet[u] = true;
            dfs928(graph, initialSet, infectedSet, u);
        }
    }

    //2003. 每棵子树内缺失的最小基因值
    public int[] smallestMissingValueSubtree(int[] parents, int[] nums) {
        int n = parents.length;
        int[] ans = new int[n];
        Arrays.fill(ans, 1);
        int node = -1;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) {
                node = i; // 出发点
                break;
            }
        }
        if (node < 0) { // 不存在基因值为 1 的点
            return ans;
        }

        // 建树
        List<Integer>[] g = new ArrayList[n];
        Arrays.setAll(g, e -> new ArrayList<>());
        for (int i = 1; i < n; ++i) {
            g[parents[i]].add(i);
        }

        Set<Integer> vis = new HashSet<>();
        int mex = 2; // 缺失的最小基因值
        while (node >= 0) {
            dfs(node, g, vis, nums);
            while (vis.contains(mex)) { // node 子树包含这个基因值
                mex++;
            }
            ans[node] = mex; // 缺失的最小基因值
            node = parents[node]; // 往上走
        }
        return ans;
    }

    // 遍历 x 子树
    private void dfs(int x, List<Integer>[] g, Set<Integer> vis, int[] nums) {
        vis.add(nums[x]); // 标记基因值
        for (int son : g[x]) {
            if (!vis.contains(nums[son])) {
                dfs(son, g, vis, nums);
            }
        }
    }


    //2846. 边权重均等查询
    static final int W = 26;

    public int[] minOperationsQueries(int n, int[][] edges, int[][] queries) {
        int m = queries.length;
        Map<Integer, Integer>[] neighbors = new Map[n];
        for (int i = 0; i < n; i++) {
            neighbors[i] = new HashMap<Integer, Integer>();
        }
        for (int[] edge : edges) {
            neighbors[edge[0]].put(edge[1], edge[2]);
            neighbors[edge[1]].put(edge[0], edge[2]);
        }
        List<int[]>[] queryArr = new List[n];
        for (int i = 0; i < n; i++) {
            queryArr[i] = new ArrayList<int[]>();
        }
        for (int i = 0; i < m; i++) {
            queryArr[queries[i][0]].add(new int[]{queries[i][1], i});
            queryArr[queries[i][1]].add(new int[]{queries[i][0], i});
        }

        int[][] count = new int[n][W + 1];
        boolean[] visited = new boolean[n];
        int[] uf = new int[n];
        int[] lca = new int[m];
        tarjan(0, -1, neighbors, queryArr, count, visited, uf, lca);
        int[] res = new int[m];
        for (int i = 0; i < m; i++) {
            int totalCount = 0, maxCount = 0;
            for (int j = 1; j <= W; j++) {
                int t = count[queries[i][0]][j] + count[queries[i][1]][j] - 2 * count[lca[i]][j];
                maxCount = Math.max(maxCount, t);
                totalCount += t;
            }
            res[i] = totalCount - maxCount;
        }
        return res;
    }

    public void tarjan(int node, int parent, Map<Integer, Integer>[] neighbors, List<int[]>[] queryArr, int[][] count, boolean[] visited, int[] uf, int[] lca) {
        if (parent != -1) {
            System.arraycopy(count[parent], 0, count[node], 0, W + 1);
            count[node][neighbors[node].get(parent)]++;
        }
        uf[node] = node;
        for (int child : neighbors[node].keySet()) {
            if (child == parent) {
                continue;
            }
            tarjan(child, node, neighbors, queryArr, count, visited, uf, lca);
            uf[child] = node;
        }
        for (int[] pair : queryArr[node]) {
            int node1 = pair[0], index = pair[1];
            if (node != node1 && !visited[node1]) {
                continue;
            }
            lca[index] = find(uf, node1);
        }
        visited[node] = true;
    }

    public int find(int[] uf, int i) {
        if (uf[i] == i) {
            return i;
        }
        uf[i] = find(uf, uf[i]);
        return uf[i];
    }


    //2059. 转化数字的最小运算数
    public int minimumOperations(int[] nums, int start, int goal) {
        if (start == goal) return 0;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        int ops = 0;
        // BFS求步数的问题，每一层步数一样，如果分层写，这里用set，最外层step
        // 如果用map，可以不分层写，每次从map中取保留的step
        Set<Integer> visited = new HashSet<>();
        visited.add(start);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int cur = queue.poll();
                for (int num : nums) {
                    // 进队的都是满足条件的
                    int[] result = new int[]{cur + num, cur - num, cur ^ num};
                    for (int next : result) {
                        if (next == goal) return ops + 1;
                        // 不等于goal,又不满足条件，不用进队
                        if (next < 0 || next > 1000) continue;
                        if (visited.contains(next)) continue;
                        queue.offer(next);
                        visited.add(next);
                    }
                }
            }
            ops++;
        }
        return -1;
    }

    // 不分层写法
    public int minimumOperations2(int[] nums, int s, int t) {
        Deque<Integer> d = new ArrayDeque<>();
        Map<Integer, Integer> map = new HashMap<>();
        d.addLast(s);
        map.put(s, 0);
        while (!d.isEmpty()) {
            int cur = d.pollFirst();
            int step = map.get(cur);
            for (int i : nums) {
                int[] result = new int[]{cur + i, cur - i, cur ^ i};
                for (int next : result) {
                    if (next == t) return step + 1;
                    if (next < 0 || next > 1000) continue;
                    if (map.containsKey(next)) continue;
                    map.put(next, step + 1);
                    d.addLast(next);
                }
            }
        }
        return -1;
    }

    public int minimumOperationsDualBFS(int[] nums, int start, int goal) {
        if (start == goal) return 0;
        Queue<Long> startQueue = new ArrayDeque<>();
        startQueue.offer((long) start);
        Queue<Long> goalQueue = new ArrayDeque<>();
        goalQueue.offer((long) goal);
        // 双向BFS 要用map记录步数
        Map<Long, Integer> startMap = new HashMap<>();
        startMap.put((long) start, 0);
        Map<Long, Integer> goalMap = new HashMap<>();
        goalMap.put((long) goal, 0);
        while (!startQueue.isEmpty() && !goalQueue.isEmpty()) {
            if (startQueue.size() < goalQueue.size()) {
                int size = startQueue.size();
                for (int i = 0; i < size; i++) {
                    long cur = startQueue.poll();
                    int step = startMap.get(cur);
                    // 当前值必须满足条件才能进行操作
                    if (cur >= 0 && cur <= 1000) {
                        for (int num : nums) {
                            long[] result = new long[]{cur + num, cur - num, cur ^ num};
                            for (long next : result) {
                                if (goalMap.containsKey(next)) return step + 1 + goalMap.get(next);
                                if (startMap.containsKey(next)) continue;
                                startQueue.offer(next);
                                startMap.put(next, step + 1);
                            }
                        }
                    }
                }
            } else {
                int size = goalQueue.size();
                for (int i = 0; i < size; i++) {
                    long cur = goalQueue.poll();
                    int step = goalMap.get(cur);
                    // 当前值是操作后的值
                    for (int num : nums) {
                        long[] result = new long[]{cur - num, cur + num, cur ^ num};
                        for (long next : result) {
                            // next是操作前的值,操作前必须满足条件才可以操作
                            if (next < 0 || next > 1000) continue;
                            // startMap中越界的值不可能进行操作,故这里next其实从startMap中未越界的值中找
                            if (startMap.containsKey(next)) return step + 1 + startMap.get(next);
                            if (goalMap.containsKey(next)) continue;
                            goalMap.put(next, step + 1);
                            goalQueue.offer(next);
                        }
                    }
                }
            }

        }
        return -1;
    }

    // 126 单词接龙2
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        // 结果集
        List<List<String>> res = new ArrayList<>();
        Set<String> distSet = new HashSet<>(wordList);
        // 字典中不包含目标单词
        if (!distSet.contains(endWord)) {
            return res;
        }
        // 已经访问过的单词集合：只找最短路径，所以之前出现过的单词不用出现在下一层
        Set<String> visited = new HashSet<>();
        // 累积每一层的结果队列(纵向)
        Queue<List<String>> queue = new LinkedList<>();
        List<String> list = new ArrayList<>(Arrays.asList(beginWord));
        queue.add(list);
        visited.add(beginWord);
        // 是否到达符合条件的层：如果该层添加的某一单词符合目标单词，则说明截止该层的所有解为最短路径，停止循环
        boolean flag = false;
        while (!queue.isEmpty() && !flag) {
            // 上一层的结果队列
            int size = queue.size();
            // 该层添加的所有元素：每层必须在所有结果都添加完新的单词之后，再将这些单词统一添加到已使用单词集合
            // 如果直接添加到 visited 中，会导致该层本次结果添加之后的相同添加行为失败
            // 如：该层遇到目标单词，有两条路径都可以遇到，但是先到达的将该单词添加进 visited 中，会导致第二条路径无法添加
            Set<String> subVisited = new HashSet<>();
            for (int i = 0; i < size; i++) {
                List<String> path = queue.poll();
                // 获取该路径上一层的单词
                String word = path.get(path.size() - 1);
                char[] chars = word.toCharArray();
                // 寻找该单词的下一个符合条件的单词
                for (int j = 0; j < chars.length; j++) {
                    char temp = chars[j];
                    for (char ch = 'a'; ch <= 'z'; ch++) {
                        chars[j] = ch;
                        if (temp == ch) {
                            continue;
                        }
                        String str = new String(chars);
                        // 符合条件：在 wordList 中 && 之前的层没有使用过
                        if (distSet.contains(str) && !visited.contains(str)) {
                            // 生成新的路径
                            List<String> pathList = new ArrayList<>(path);
                            pathList.add(str);
                            // 如果该单词是目标单词：将该路径添加到结果集中，查询截止到该层
                            if (str.equals(endWord)) {
                                flag = true;
                                res.add(pathList);
                            }
                            // 将该路径添加到该层队列中
                            queue.add(pathList);
                            // 将该单词添加到该层已访问的单词集合中
                            subVisited.add(str);
                        }
                    }
                    chars[j] = temp;
                }
            }
            // 将该层所有访问的单词添加到总的已访问集合中
            visited.addAll(subVisited);
        }
        return res;
    }

    public List<String> findLadders2(String beginWord, String endWord, List<String> wordList) {
        List<String> result = new ArrayList<>();
        if (beginWord.equals(endWord)) return result;
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord)) return result;
        Set<String> visited = new HashSet<>();
        Queue<Deque<String>> queue = new ArrayDeque<>();
        Deque<String> deque = new ArrayDeque<>();
        deque.offerLast(beginWord);
        queue.offer(deque);
        visited.add(beginWord);
        while (!queue.isEmpty()) {
            int size = queue.size();
            Set<String> levelVisited = new HashSet<>();
            for (int i = 0; i < size; i++) {
                Deque<String> level = queue.poll();
                String last = level.peekLast();
                List<String> nears = getNear(last, dict);
                for (String near : nears) {
                    if (near.equals(endWord)) {
                        level.offerLast(near);
                        return new ArrayList<>(level);
                    }
                    if (!visited.contains(near)) {
                        Deque<String> newPath = new ArrayDeque<>(level);
                        levelVisited.add(near);
                        newPath.offerLast(near);
                        queue.offer(newPath);
                    }
                }
            }
            visited.addAll(levelVisited);
        }
        return result;
    }

    private List<String> getNear(String word, Set<String> dict) {
        char[] chars = word.toCharArray();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            char origin = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == origin) continue;
                chars[i] = c;
                String tmp = new String(chars);
                if (dict.contains(tmp)) {
                    result.add(tmp);
                }
                chars[i] = origin;
            }
        }
        return result;
    }

    // 双向BFS
    public List<List<String>> findLaddersBFS(String beginWord, String endWord, List<String> wordList) {
        // 结果集
        List<List<String>> res = new ArrayList<>();
        Set<String> words = new HashSet<>(wordList);
        // 字典中不包含目标单词
        if (!words.contains(endWord)) {
            return res;
        }
        // 存放关系：每个单词可达的下层单词
        Map<String, List<String>> mapTree = new HashMap<>();
        Set<String> begin = new HashSet<>(), end = new HashSet<>();
        begin.add(beginWord);
        end.add(endWord);
        if (buildTree(words, begin, end, mapTree, true)) {
            dfs(res, mapTree, beginWord, endWord, new LinkedList<>());
        }
        return res;
    }

    // 双向BFS，构建每个单词的层级对应关系
    private boolean buildTree(Set<String> words, Set<String> begin, Set<String> end, Map<String, List<String>> mapTree, boolean isFront) {
        if (begin.size() == 0) {
            return false;
        }
        // 始终以少的进行探索
        if (begin.size() > end.size()) {
            return buildTree(words, end, begin, mapTree, !isFront);
        }
        // 在已访问的单词集合中去除
        words.removeAll(begin);
        // 标记本层是否已到达目标单词
        boolean isMeet = false;
        // 记录本层所访问的单词
        Set<String> nextLevel = new HashSet<>();
        for (String word : begin) {
            char[] chars = word.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char temp = chars[i];
                for (char ch = 'a'; ch <= 'z'; ch++) {
                    chars[i] = ch;
                    String str = String.valueOf(chars);
                    if (words.contains(str)) {
                        nextLevel.add(str);
                        // 根据访问顺序，添加层级对应关系：始终保持从上层到下层的存储存储关系
                        // true: 从上往下探索：word -> str
                        // false: 从下往上探索：str -> word（查找到的 str 是 word 上层的单词）
                        String key = isFront ? word : str;
                        String nextWord = isFront ? str : word;
                        // 判断是否遇见目标单词
                        if (end.contains(str)) {
                            isMeet = true;
                        }
                        if (!mapTree.containsKey(key)) {
                            mapTree.put(key, new ArrayList<>());
                        }
                        mapTree.get(key).add(nextWord);
                    }
                }
                chars[i] = temp;
            }
        }
        if (isMeet) {
            return true;
        }
        return buildTree(words, nextLevel, end, mapTree, isFront);
    }

    // DFS: 组合路径
    private void dfs(List<List<String>> res, Map<String, List<String>> mapTree, String beginWord, String endWord, LinkedList<String> list) {
        list.add(beginWord);
        if (beginWord.equals(endWord)) {
            res.add(new ArrayList<>(list));
            list.removeLast();
            return;
        }
        if (mapTree.containsKey(beginWord)) {
            for (String word : mapTree.get(beginWord)) {
                dfs(res, mapTree, word, endWord, list);
            }
        }
        list.removeLast();
    }

    // 127 单词接龙
    // 双向BFS
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (wordSet.size() <= 0 || !wordSet.contains(endWord)) return 0;
        Set<String> visited = new HashSet<>();
        Set<String> beginVisited = new HashSet<>();
        Set<String> endVisited = new HashSet<>();
        beginVisited.add(beginWord);
        endVisited.add(endWord);
        int step = 1;
        while (!beginVisited.isEmpty() && !endVisited.isEmpty()) {
            if (beginVisited.size() > endVisited.size()) {
                Set<String> tmp = beginVisited;
                beginVisited = endVisited;
                endVisited = tmp;
            }
            Set<String> nextLevelVisited = new HashSet<>();
            for (String word : beginVisited) {
                if (check(word, wordSet, endVisited, nextLevelVisited, visited)) return step + 1;
            }
            beginVisited = nextLevelVisited;
            step++;
        }
        return 0;
    }

    private boolean check(String word, Set<String> wordSet, Set<String> endVisited, Set<String> nextLevelVisited, Set<String> visited) {
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char origin = chars[i];
            for (char k = 'a'; k <= 'z'; k++) {
                if (k == origin) continue;
                chars[i] = k;
                String nextWord = new String(chars);
                if (!wordSet.contains(nextWord)) continue;
                if (endVisited.contains(nextWord)) return true;
                if (!visited.contains(nextWord)) {
                    nextLevelVisited.add(nextWord);
                    visited.add(nextWord);
                }
            }
            chars[i] = origin;
        }
        return false;
    }

    //433 最小基因变化 双向BFS
    public int minMutation(String start, String end, String[] bank) {
        Set<String> bankSet = new HashSet<>(Arrays.asList(bank));
        if (bankSet.size() <= 0 || !bankSet.contains(end)) return -1;
        Set<String> visited = new HashSet<>();
        Set<String> beginVisited = new HashSet<>();
        Set<String> endVisited = new HashSet<>();
        beginVisited.add(start);
        endVisited.add(end);

        int step = 0;
        while (!beginVisited.isEmpty() && !endVisited.isEmpty()) {
            if (beginVisited.size() > endVisited.size()) {
                Set<String> tmp = beginVisited;
                beginVisited = endVisited;
                endVisited = tmp;
            }
            Set<String> nextLevelVisited = new HashSet<>();
            for (String s : beginVisited) {
                if (check2(s, bankSet, endVisited, nextLevelVisited, visited)) return step + 1;
            }
            beginVisited = nextLevelVisited;
            step++;
        }
        return -1;
    }

    private boolean check2(String word, Set<String> bankSet, Set<String> endVisited, Set<String> nextLevelVisited, Set<String> visited) {
        char[] keys = new char[]{'A', 'C', 'G', 'T'};
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char origin = chars[i];
            for (char k : keys) {
                if (k == origin) continue;
                chars[i] = k;
                String nextWord = new String(chars);
                if (!bankSet.contains(nextWord)) continue;
                if (endVisited.contains(nextWord)) return true;
                if (!visited.contains(nextWord)) {
                    nextLevelVisited.add(nextWord);
                    visited.add(nextWord);
                }
            }
            chars[i] = origin;
        }
        return false;
    }

    // 752 打开转盘锁
    public int openLock(String[] deadends, String target) {
        Set<String> deadSet = new HashSet<>(Arrays.asList(deadends));
        if (deadSet.contains(target) || deadSet.contains("0000")) return -1;
        if ("0000".equals(target)) return 0;
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();
        queue.offer("0000");
        visited.add("0000");

        int step = 0;
        while (!queue.isEmpty()) {
            //用于区分层级,step跟层级有关
            int width = queue.size();
            for (int i = 0; i < width; i++) {
                String code = queue.poll();
                for (String nextCode : getOneDiff(code)) {
                    if (deadSet.contains(nextCode)) continue;
                    if (target.equals(nextCode)) return step + 1;
                    if (!visited.contains(nextCode)) {
                        queue.offer(nextCode);
                        visited.add(nextCode);
                    }
                }
            }
            step++;
        }
        return -1;
    }

    private List<String> getOneDiff(String code) {
        List<String> result = new ArrayList<>();
        char[] chars = code.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char origin = chars[i];
            char prev = origin == '0' ? '9' : (char) (origin - 1);
            char next = origin == '9' ? '0' : (char) (origin + 1);
            chars[i] = prev;
            String prevCode = new String(chars);
            result.add(prevCode);
            chars[i] = next;
            String nextCode = new String(chars);
            result.add(nextCode);
            chars[i] = origin;
        }
        return result;
    }
    //双向BFS 反而慢
//    双向 BFS 在无解的情况下不如单向 BFS。因此我们可以先使用「并查集」进行预处理，判断「起点」和「终点」是否连通，如果不联通，直接返回 -1−1，有解才调用双向 BFS

    public int openLockDuelBFS(String[] deadends, String target) {
        Set<String> deadSet = new HashSet<>(Arrays.asList(deadends));
        if (deadSet.contains(target) || deadSet.contains("0000")) return -1;
        if ("0000".equals(target)) return 0;
        Set<String> visited = new HashSet<>();
        Queue<String> beginVisited = new ArrayDeque<>();
        Queue<String> endVisited = new ArrayDeque<>();
        beginVisited.offer("0000");
        endVisited.offer(target);
        visited.add("0000");
        visited.add(target);

        int step = 0;
        while (!beginVisited.isEmpty() && !endVisited.isEmpty()) {
            boolean find = false;
            if (beginVisited.size() > endVisited.size()) {
                find = checkBfs(endVisited, beginVisited, deadSet, visited);
            } else {
                find = checkBfs(beginVisited, endVisited, deadSet, visited);
            }
            if (find) return step + 1;
            step++;
        }
        return -1;
    }

    private boolean checkBfs(Queue<String> smallerQueue, Queue<String> largerQueue, Set<String> deadSet, Set<String> visited) {
        int width = smallerQueue.size();
        for (int i = 0; i < width; i++) {
            String code = smallerQueue.poll();
            for (String nextCode : getOneDiff(code)) {
                if (deadSet.contains(nextCode)) continue;
                if (largerQueue.contains(nextCode)) return true;
                if (!visited.contains(nextCode)) {
                    smallerQueue.offer(nextCode);
                    visited.add(nextCode);
                }
            }
        }
        return false;
    }

    // 773 滑动谜题
    public int slidingPuzzle(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int[] b : board) {
            for (int i : b) {
                sb.append(i);
            }
        }
        String initial = sb.toString();
        if ("123450".equals(initial)) return 0;
        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.offer(initial);
        visited.add(initial);
        int step = 0;
        while (!queue.isEmpty()) {
            int width = queue.size();
            for (int i = 0; i < width; i++) {
                String cur = queue.poll();
                for (String next : getNearStatus(cur)) {
                    if (!visited.contains(next)) {
                        if ("123450".equals(next)) return step + 1;
                        queue.offer(next);
                        visited.add(next);
                    }
                }
            }
            step++;
        }
        return -1;
    }

    private List<String> getNearStatus(String cur) {
        // [0,1,2]
        // [3,4,5]
        int[][] neighbors = new int[][]{{1, 3}, {0, 2, 4}, {1, 5}, {0, 4}, {1, 3, 5}, {2, 4}};
        List<String> result = new ArrayList<>();
        char[] chars = cur.toCharArray();
        int idx = cur.indexOf('0');
        int[] neighbor = neighbors[idx];
        for (int n : neighbor) {
            chars[idx] = chars[n];
            chars[n] = '0';
            result.add(new String(chars));
            chars[n] = chars[idx];
            chars[idx] = '0';
        }
        return result;
    }

    //675. 为高尔夫比赛砍树
    //砍树的路线唯一确定，当我们求出每两个相邻的砍树点最短路径，并进行累加即是答案（整条砍树路径的最少步数）
    public int cutOffTree(List<List<Integer>> forest) {
        int m = forest.size(), n = forest.get(0).size();
        List<int[]> trees = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (forest.get(i).get(j) > 1) {
                    trees.add(new int[]{forest.get(i).get(j), i, j});
                }
            }
        }
        trees.sort(Comparator.comparingInt(o -> o[0]));
        int x = 0, y = 0;
        int newX, newY;
        int result = 0;
        for (int[] tree : trees) {
            newX = tree[1];
            newY = tree[2];
            int ans = bfs(x, y, newX, newY, forest);
            if (ans == -1) return -1;
            result += ans;
            x = newX;
            y = newY;
        }
        return result;
    }

    private int bfs(int sourceX, int sourceY, int targetX, int targetY, List<List<Integer>> forest) {
        if (sourceX == targetX && sourceY == targetY) return 0;
        int m = forest.size(), n = forest.get(0).size();
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{sourceX, sourceY});
        boolean[][] visited = new boolean[m][n];
        visited[sourceX][sourceY] = true;
        int[][] directions = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size-- > 0) {
                int[] cell = queue.poll();
                int x = cell[0], y = cell[1];
                for (int[] dire : directions) {
                    int newX = x + dire[0], newY = y + dire[1];
                    if (newX < 0 || newX >= m || newY < 0 || newY >= n) continue;
                    if (newX == targetX && newY == targetY) return step + 1;
                    // 大于1的可以往返走
                    if (visited[newX][newY] || forest.get(newX).get(newY) == 0) continue;
                    visited[newX][newY] = true;
                    queue.offer(new int[]{newX, newY});
                }
            }
            step++;
        }
        return -1;
    }

    //1210. 穿过迷宫的最少移动次数
    public int minimumMoves(int[][] grid) {
        int n = grid.length;
        int[][][] dist = new int[n][n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(dist[i][j], -1);
            }
        }
        dist[0][0][0] = 0;
        Queue<int[]> queue = new ArrayDeque<int[]>();
        queue.offer(new int[]{0, 0, 0});

        while (!queue.isEmpty()) {
            int[] arr = queue.poll();
            int x = arr[0], y = arr[1], status = arr[2];
            if (status == 0) {
                // 向右移动一个单元格
                if (y + 2 < n && dist[x][y + 1][0] == -1 && grid[x][y + 2] == 0) {
                    dist[x][y + 1][0] = dist[x][y][0] + 1;
                    queue.offer(new int[]{x, y + 1, 0});
                }
                // 向下移动一个单元格
                if (x + 1 < n && dist[x + 1][y][0] == -1 && grid[x + 1][y] == 0 && grid[x + 1][y + 1] == 0) {
                    dist[x + 1][y][0] = dist[x][y][0] + 1;
                    queue.offer(new int[]{x + 1, y, 0});
                }
                // 顺时针旋转 90 度
                if (x + 1 < n && y + 1 < n && dist[x][y][1] == -1 && grid[x + 1][y] == 0 && grid[x + 1][y + 1] == 0) {
                    dist[x][y][1] = dist[x][y][0] + 1;
                    queue.offer(new int[]{x, y, 1});
                }
            } else {
                // 向右移动一个单元格
                if (y + 1 < n && dist[x][y + 1][1] == -1 && grid[x][y + 1] == 0 && grid[x + 1][y + 1] == 0) {
                    dist[x][y + 1][1] = dist[x][y][1] + 1;
                    queue.offer(new int[]{x, y + 1, 1});
                }
                // 向下移动一个单元格
                if (x + 2 < n && dist[x + 1][y][1] == -1 && grid[x + 2][y] == 0) {
                    dist[x + 1][y][1] = dist[x][y][1] + 1;
                    queue.offer(new int[]{x + 1, y, 1});
                }
                // 逆时针旋转 90 度
                if (x + 1 < n && y + 1 < n && dist[x][y][0] == -1 && grid[x][y + 1] == 0 && grid[x + 1][y + 1] == 0) {
                    dist[x][y][0] = dist[x][y][1] + 1;
                    queue.offer(new int[]{x, y, 0});
                }
            }
        }

        return dist[n - 1][n - 2][0];
    }

    //815 公交线路
    // 给你一个数组 routes ，表示一系列公交线路，其中每个 routes[i] 表示一条公交线路，第 i 辆公交车将会在上面循环行驶。
//例如，路线 routes[0] = [1, 5, 7] 表示第 0 辆公交车会一直按序列 1 -> 5 -> 7 -> 1 -> 5 -> 7 -> 1-> ... 这样的车站路线行驶。
// 现在从 source 车站出发（初始时不在公交车上），要前往 target 车站。 期间仅可乘坐公交车。
// 求出 最少乘坐的公交车数量 。如果不可能到达终点车站，返回 -1 。
//输入：routes = [[1,2,7],[3,6,7]], source = 1, target = 6
//输出：2
//解释：最优策略是先乘坐第一辆公交车到达车站 7 , 然后换乘第二辆公交车到车站 6 。
    public int numBusesToDestination(int[][] routes, int source, int target) {
        if (source == target) return 0;
        //每个站可以坐哪些公交
        Map<Integer, Set<Integer>> stationMap = new HashMap<>();
        //坐某个公交时最少经过几步
        Map<Integer, Integer> busMap = new HashMap<>();
        //坐过的公交队列
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < routes.length; i++) {
            for (int station : routes[i]) {
                Set<Integer> busSet = stationMap.getOrDefault(station, new HashSet<>());
                busSet.add(i);
                stationMap.put(station, busSet);
                if (station == source) {
                    //当前要做的公交
                    queue.offer(i);
                    //坐到当前公交经历的步数
                    busMap.put(i, 1);
                }
            }
        }
        while (!queue.isEmpty()) {
            int currBus = queue.poll();
            //换到当前公交的步数
            int currStep = busMap.get(currBus);

            //只要当前公交能到达某站，就返回；不然换一次公交就多一次换乘次数 （不是公交经过几站，是人经过几次公交）
            for (int station : routes[currBus]) {
                if (station == target) return currStep;
                Set<Integer> busSet = stationMap.get(station);
                if (busSet == null) continue;
                for (int bus : busSet) {
                    if (!busMap.containsKey(bus)) {
                        busMap.put(bus, currStep + 1);
                        queue.offer(bus);
                    }
                }
            }
        }
        return -1;
    }

    // 841 钥匙和房间
    //有 n 个房间，房间按从 0 到 n - 1 编号。最初，除 0 号房间外的其余所有房间都被锁住。你的目标是进入所有的房间。然而，你不能在没有获得钥匙的时候进入锁住的房间。
// 当你进入一个房间，你可能会在里面找到一套不同的钥匙，每把钥匙上都有对应的房间号，即表示钥匙可以打开的房间。你可以拿上所有钥匙去解锁其他房间。
// 给你一个数组 rooms 其中 rooms[i] 是你进入 i 号房间可以获得的钥匙集合。如果能进入 所有 房间返回 true，否则返回 false。
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int n = rooms.size();
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(0);
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            if (visited[cur]) continue;
            visited[cur] = true;
            for (int next : rooms.get(cur)) {
                queue.offer(next);
            }
        }
        for (int i = 0; i < n; i++) {
            if (!visited[i]) return false;
        }
        return true;
    }

    int roomNum;

    public boolean canVisitAllRoomsDFS(List<List<Integer>> rooms) {
        int n = rooms.size();
        roomNum = 0;
        boolean[] visited = new boolean[n];
        dfs(rooms, 0, visited);
        return roomNum == n;
    }

    public void dfs(List<List<Integer>> rooms, int x, boolean[] visited) {
        visited[x] = true;
        roomNum++;
        for (int it : rooms.get(x)) {
            if (!visited[it]) {
                dfs(rooms, it, visited);
            }
        }
    }

    //1654. 到家的最少跳跃次数
    public int minimumJumps(int[] forbidden, int a, int b, int x) {
        Queue<int[]> queue = new ArrayDeque<int[]>();
        Set<Integer> visited = new HashSet<Integer>();
        queue.offer(new int[]{0, 1, 0});
        visited.add(0);
        int lower = 0, upper = Math.max(Arrays.stream(forbidden).max().getAsInt() + a, x) + b;
        Set<Integer> forbiddenSet = new HashSet<Integer>();
        for (int position : forbidden) {
            forbiddenSet.add(position);
        }
        while (!queue.isEmpty()) {
            int[] arr = queue.poll();
            int position = arr[0], direction = arr[1], step = arr[2];
            if (position == x) {
                return step;
            }
            int nextPosition = position + a;
            int nextDirection = 1;
            if (lower <= nextPosition && nextPosition <= upper && !visited.contains(nextPosition * nextDirection) && !forbiddenSet.contains(nextPosition)) {
                visited.add(nextPosition * nextDirection);
                queue.offer(new int[]{nextPosition, nextDirection, step + 1});
            }
            if (direction == 1) {
                nextPosition = position - b;
                nextDirection = -1;
                if (lower <= nextPosition && nextPosition <= upper && !visited.contains(nextPosition * nextDirection) && !forbiddenSet.contains(nextPosition)) {
                    visited.add(nextPosition * nextDirection);
                    queue.offer(new int[]{nextPosition, nextDirection, step + 1});
                }
            }
        }
        return -1;
    }

    // 847 访问所有节点的最短距离
    // 状态压缩+BFS
//    一些状态压缩的基本操作如下：
//         （1）访问第 i 个点的状态：state=(1 << i) & mask
//        （2）更改第 i 个点状态为 1：mask = mask | (1 << i)
    public int shortestPathLength(int[][] graph) {
        int n = graph.length;
        // 1.初始化队列及标记数组，存入起点
        // 三个属性分别为 idx, mask, dist
        Queue<int[]> queue = new LinkedList<>();
        // 节点编号及当前状态
        // [i,mask] 从i出发已经遍历过mask
        boolean[][] seen = new boolean[n][1 << n];
        for (int i = 0; i < n; ++i) {
            // 把0-n-1全部入队
            queue.offer(new int[]{i, 1 << i, 0});
            seen[i][1 << i] = true;
        }

        int ans = 0;
        while (!queue.isEmpty()) {
            int[] tuple = queue.poll();
            int u = tuple[0], mask = tuple[1], dist = tuple[2];
            // 所有节点依次BFS，哪个mask全部遍历完即是最短
            if (mask == (1 << n) - 1) {
                ans = dist;
                break;
            }
            // 搜索相邻的节点
            for (int v : graph[u]) {
                // 将 mask 的第 v 位置为 1
                int maskV = mask | (1 << v);
                if (!seen[v][maskV]) {
                    queue.offer(new int[]{v, maskV, dist + 1});
                    seen[v][maskV] = true;
                }
            }
        }
        return ans;
    }

    // 854 相似度为K的字符串
    public int kSimilarity(String s1, String s2) {
        Queue<Pair<String, Integer>> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.offer(new Pair<>(s1, 0));
        visited.add(s1);
        int step = 0;
        int n = s1.length();
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Pair<String, Integer> pair = queue.poll();
                String cur = pair.getKey();
                int pos = pair.getValue();
                if (cur.equals(s2)) {
                    return step;
                }
                while (pos < n && cur.charAt(pos) == s2.charAt(pos)) {
                    pos++;
                }
                for (int j = pos; j < n; j++) {
                    if (cur.charAt(j) == s2.charAt(j)) {
                        continue;
                    }
                    if (cur.charAt(j) == s2.charAt(pos)) {
                        String next = swap(cur, pos, j);
                        if (!visited.contains(next)) {
                            visited.add(next);
                            queue.offer(new Pair<>(next, pos + 1));
                        }
                    }
                }
            }
            step++;
        }
        return -1;
    }

    private String swap(String s, int i, int j) {
        char[] chars = s.toCharArray();
        char tmp = chars[i];
        chars[i] = chars[j];
        chars[j] = tmp;
        return new String(chars);
    }

    // 864 获取所有钥匙的最短路径
    int m;

    public int shortestPathAllKeys(String[] grid) {
        this.m = grid.length;
        this.n = grid[0].length();
        Queue<int[]> queue = new ArrayDeque<>();
        int k = 0, sx = 0, sy = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i].charAt(j) == '@') {
                    sx = i;
                    sy = j;
                } else if (grid[i].charAt(j) >= 'a' && grid[i].charAt(j) <= 'f') {
                    k++;
                }
            }
        }
        int finalStatus = (1 << k) - 1;
        //状态位,某种钥匙组合的
        int[][][] dist = new int[m][n][1 << k];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(dist[i][j], -1);
            }
        }
        queue.offer(new int[]{sx, sy, 0});
        dist[sx][sy][0] = 0;
        while (!queue.isEmpty()) {
            int[] tmp = queue.poll();
            int x = tmp[0], y = tmp[1], mask = tmp[2];
            for (int[] dire : directions) {
                int newX = x + dire[0], newY = y + dire[1];
                if (inAreaRowCol(newX, newY)) {
                    char c = grid[newX].charAt(newY);
                    if (c == '#') continue;
                    if (c == '.' || c == '@') {
                        if (dist[newX][newY][mask] == -1) {
                            dist[newX][newY][mask] = dist[x][y][mask] + 1;
                            queue.offer(new int[]{newX, newY, mask});
                        }
                    } else if (c >= 'a' && c <= 'f') {
                        int idx = c - 'a';
                        int newMask = mask | (1 << idx);
                        if (dist[newX][newY][newMask] == -1) {
                            dist[newX][newY][newMask] = dist[x][y][mask] + 1;
                            if (newMask == finalStatus) return dist[newX][newY][newMask];
                            queue.offer(new int[]{newX, newY, newMask});
                        }
                    } else if (c >= 'A' && c <= 'F') {
                        int idx = c - 'A';
                        if ((mask & (1 << idx)) != 0 && dist[newX][newY][mask] == -1) {
                            dist[newX][newY][mask] = dist[x][y][mask] + 1;
                            queue.offer(new int[]{newX, newY, mask});
                        }
                    }
                }
            }
        }
        return -1;
    }
    private boolean inAreaRowCol(int i, int j) {
        return i >= 0 && i < row && j >= 0 && j < col;
    }
    //1377. T 秒后青蛙的位置
    public double frogPosition(int n, int[][] edges, int t, int target) {
        List<Integer>[] g = new ArrayList[n + 1];
        Arrays.setAll(g, e -> new ArrayList<>());
        g[1].add(0); // 减少额外判断的小技巧
        for (int[] e : edges) {
            int x = e[0], y = e[1];
            g[x].add(y);
            g[y].add(x); // 建树
        }
        long prod = dfs(g, target, 1, 0, t);
        return prod != 0 ? 1.0 / prod : 0;
    }

    private long dfs(List<Integer>[] g, int target, int x, int fa, int leftT) {
        // t 秒后必须在 target（恰好到达，或者 target 是叶子停在原地）
        if (leftT == 0) return x == target ? 1 : 0;
        if (x == target) return g[x].size() == 1 ? 1 : 0;
        for (int y : g[x]) { // 遍历 x 的儿子 y
            if (y != fa) { // y 不能是父节点
                long prod = dfs(g, target, y, x, leftT - 1); // 寻找 target
                if (prod != 0)
                    return prod * (g[x].size() - 1); // 乘上儿子个数，并直接返回
            }
        }
        return 0; // 未找到 target
    }

    //2045. 到达目的地的第二短时间
    public int secondMinimum(int n, int[][] edges, int time, int change) {
        List<Integer>[] graph = new List[n + 1];
        // path[i][0]是从1到i最短路径，path[i][1]是从1到i的次短路径
        int[][] path = new int[n + 1][2];

        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
            Arrays.fill(path[i], Integer.MAX_VALUE);
        }

        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }

        path[1][0] = 0;
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{1, 0});
        while (path[n][1] == Integer.MAX_VALUE) {
            int[] cell = queue.poll();
            int node = cell[0], len = cell[1];
            int nextLen = len + 1;
            for (int next : graph[node]) {
                if (nextLen < path[next][0]) {
                    path[next][0] = nextLen;
                    queue.offer(new int[]{next, nextLen});
                } else if (nextLen > path[next][0] && nextLen < path[next][1]) {
                    path[next][1] = nextLen;
                    queue.offer(new int[]{next, nextLen});
                }
            }
        }
        int ans = 0;
        // [0,change) 可以走，[change-2change) 等待
        for (int i = 0; i < path[n][1]; i++) {
            if (ans % (2 * change) >= change) {
                ans += ((2 * change) - ans % (2 * change));
            }
            ans += time;
        }
        return ans;

    }

    //2146 价格范围内最高排名的k样物品
    public List<List<Integer>> highestRankedKItems(int[][] grid, int[] pricing, int[] start, int k) {
        List<int[]> result = new ArrayList<>();
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int[][] directions = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        Queue<int[]> queue = new ArrayDeque<>();
        int dist = 0;
        queue.offer(start);
        visited[start[0]][start[1]] = true;
        int fk = k;
        while (!queue.isEmpty() && k > 0) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] tmp = queue.poll();
                int x = tmp[0], y = tmp[1];

                if (grid[x][y] >= pricing[0] && grid[x][y] <= pricing[1]) {
                    k--;
                    result.add(new int[]{dist, grid[x][y], x, y});
                }

                for (int[] dire : directions) {
                    int newX = x + dire[0], newY = y + dire[1];
                    if (newX < 0 || newX >= m || newY < 0 || newY >= n || visited[newX][newY] || grid[newX][newY] == 0)
                        continue;
                    visited[newX][newY] = true;
                    queue.offer(new int[]{newX, newY});
                }
            }
            dist++;
        }
        result.sort((o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];
            if (o1[1] != o2[1]) return o1[1] - o2[1];
            if (o1[2] != o2[2]) return o1[2] - o2[2];
            return o1[3] - o2[3];
        });
        return result.stream().map(p -> Arrays.asList(p[2], p[3])).limit(fk).collect(Collectors.toList());
    }

    //LCP 41. 黑白翻转棋
    public int flipChess(String[] chessboard) {
        int res = 0;
        for (int i = 0; i < chessboard.length; i++) {
            for (int j = 0; j < chessboard[i].length(); j++) {
                if (chessboard[i].charAt(j) == '.') {
                    res = Math.max(res, bfs(chessboard, i, j));
                }
            }
        }
        return res;
    }

    private int bfs(String[] chessboard, int x, int y) {
        char[][] board = new char[chessboard.length][chessboard[0].length()];
        int[][] directions = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int i = 0; i < chessboard.length; i++) {
            for (int j = 0; j < chessboard[0].length(); j++) {
                board[i][j] = chessboard[i].charAt(j);
            }
        }
        int cnt = 0;
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{x, y});
        board[x][y] = 'X';
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] d : directions) {
                if (judge(board, cell[0], cell[1], d[0], d[1])) {
                    int nx = cell[0] + d[0], ny = cell[1] + d[1];
                    while (board[nx][ny] != 'X') {
                        queue.offer(new int[]{nx, ny});
                        board[nx][ny] = 'X';
                        nx += d[0];
                        ny += d[1];
                        cnt++;
                    }
                }
            }
        }
        return cnt;
    }

    private boolean judge(char[][] board, int x, int y, int dx, int dy) {
        x += dx;
        y += dy;
        while (x >= 0 && x < board.length && y >= 0 && y < board[0].length) {
            if (board[x][y] == 'X') {
                return true;
            } else if (board[x][y] == '.') {
                return false;
            }
            x += dx;
            y += dy;
        }
        return false;
    }

    //2385. 感染二叉树需要的总时间
    public int amountOfTime(TreeNode root, int start) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        dfs(root, null, map);
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        Set<Integer> visited = new HashSet<>();
        visited.add(start);
        int minute = -1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                List<Integer> nearList = map.getOrDefault(queue.poll(), new ArrayList<>());
                for (int near : nearList) {
                    if (!visited.contains(near)) {
                        visited.add(near);
                        queue.offer(near);
                    }
                }
            }
            minute++;
        }
        return minute;
    }

    private void dfs(TreeNode node, TreeNode parent, Map<Integer, List<Integer>> map) {
        if (node == null) return;
        List<Integer> nearList = map.getOrDefault(node.val, new ArrayList<>());
        if (parent != null) {
            nearList.add(parent.val);
        }
        if (node.left != null) {
            nearList.add(node.left.val);
        }
        if (node.right != null) {
            nearList.add(node.right.val);
        }
        map.put(node.val, nearList);
        dfs(node.left, node, map);
        dfs(node.right, node, map);
    }

    //1263. 推箱子 Hard
    public int minPushBox(char[][] grid) {
        int m = grid.length, n = grid[0].length;
        int sx = -1, sy = -1, bx = -1, by = -1; // 玩家、箱子的初始位置
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                if (grid[x][y] == 'S') {
                    sx = x;
                    sy = y;
                } else if (grid[x][y] == 'B') {
                    bx = x;
                    by = y;
                }
            }
        }

        int[] d = {0, -1, 0, 1, 0};

        int[][] dp = new int[m * n][m * n];
        for (int i = 0; i < m * n; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        Queue<int[]> queue = new ArrayDeque<int[]>();
        dp[sx * n + sy][bx * n + by] = 0; // 初始状态的推动次数为 0
        queue.offer(new int[]{sx * n + sy, bx * n + by});
        while (!queue.isEmpty()) {
            Queue<int[]> queue1 = new ArrayDeque<int[]>();
            while (!queue.isEmpty()) {
                int[] arr = queue.poll();
                int s1 = arr[0], b1 = arr[1];
                int sx1 = s1 / n, sy1 = s1 % n, bx1 = b1 / n, by1 = b1 % n;
                if (grid[bx1][by1] == 'T') { // 箱子已被推到目标处
                    return dp[s1][b1];
                }
                for (int i = 0; i < 4; i++) { // 玩家向四个方向移动到另一个状态
                    int sx2 = sx1 + d[i], sy2 = sy1 + d[i + 1], s2 = sx2 * n + sy2;
                    if (!ok(grid, m, n, sx2, sy2)) { // 玩家位置不合法
                        continue;
                    }
                    if (bx1 == sx2 && by1 == sy2) { // 推动箱子
                        int bx2 = bx1 + d[i], by2 = by1 + d[i + 1], b2 = bx2 * n + by2;
                        if (!ok(grid, m, n, bx2, by2) || dp[s2][b2] <= dp[s1][b1] + 1) { // 箱子位置不合法 或 状态已访问
                            continue;
                        }
                        dp[s2][b2] = dp[s1][b1] + 1;
                        queue1.offer(new int[]{s2, b2});
                    } else {
                        if (dp[s2][b1] <= dp[s1][b1]) { // 状态已访问
                            continue;
                        }
                        dp[s2][b1] = dp[s1][b1];
                        queue.offer(new int[]{s2, b1});
                    }
                }
            }
            queue = queue1;
        }
        return -1;
    }

    public boolean ok(char[][] grid, int m, int n, int x, int y) { // 不越界且不在墙上
        return x >= 0 && x < m && y >= 0 && y < n && grid[x][y] != '#';
    }

    //863 二叉树中所有距离为K的节点
    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        Map<Integer, TreeNode> parents = new HashMap<>();
        List<Integer> result = new ArrayList<>();
        findParentsDfs(root, parents);
        findAnsDfs(target, null, 0, k, parents, result);
        return result;
    }

    private void findAnsDfs(TreeNode node, TreeNode from, int dist, int k, Map<Integer, TreeNode> parents, List<Integer> result) {
        if (node == null) return;
        if (dist == k) {
            result.add(node.val);
            return;
        }
        //从子到父，避免再遍历父的子  target->parent->target
        if (node.left != from) {
            findAnsDfs(node.left, node, dist + 1, k, parents, result);
        }
        if (node.right != from) {
            findAnsDfs(node.right, node, dist + 1, k, parents, result);
        }
        //从上到下的无需再从下到上遍历
        if (parents.get(node.val) != from) {
            findAnsDfs(parents.get(node.val), node, dist + 1, k, parents, result);
        }
    }

    private void findParentsDfs(TreeNode root, Map<Integer, TreeNode> parents) {
        if (root.left != null) {
            parents.put(root.left.val, root);
            findParentsDfs(root.left, parents);
        }
        if (root.right != null) {
            parents.put(root.right.val, root);
            findParentsDfs(root.right, parents);
        }
    }

    // 994 腐烂的橘子
    public int orangesRotting(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        Queue<int[]> queue = new ArrayDeque<>();
        int cnt = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 2) {
                    queue.offer(new int[]{i, j});
                    visited[i][j] = true;
                } else if (grid[i][j] == 1) {
                    cnt++;
                }
            }
        }
        if (cnt == 0) return 0;
        int minutes = -1;
        int[][] directions = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cell = queue.poll();
                int x = cell[0], y = cell[1];
                for (int[] dire : directions) {
                    int newX = x + dire[0], newY = y + dire[1];
                    if (newX >= 0 && newX < m && newY >= 0 && newY < n && grid[newX][newY] == 1 && !visited[newX][newY]) {
                        queue.offer(new int[]{newX, newY});
                        visited[newX][newY] = true;
                        cnt--;
                    }
                }
            }
            minutes++;
        }
        return cnt == 0 ? minutes : -1;
    }

    // 面试04.01 节点间通路
    public boolean findWhetherExistsPathBFS(int n, int[][] graph, int start, int target) {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int[] g : graph) {
            Set<Integer> set = map.getOrDefault(g[0], new HashSet<>());
            set.add(g[1]);
            map.put(g[0], set);
        }
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        boolean[] visited = new boolean[n];
        visited[start] = true;
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            for (int near : map.getOrDefault(cur, new HashSet<>())) {
                if (near == target) return true;
                if (visited[near]) continue;
                queue.offer(near);
            }
        }
        return false;
    }

    // 130 被围绕的区域
    public void solve(char[][] board) {
        int n = board.length;
        if (n == 0) {
            return;
        }
        int m = board[0].length;
        for (int i = 0; i < n; i++) {
            dfs(board, i, 0, n, m);
            dfs(board, i, m - 1, n, m);
        }
        for (int i = 1; i < m - 1; i++) {
            dfs(board, 0, i, n, m);
            dfs(board, n - 1, i, n, m);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                } else if (board[i][j] == 'A') {
                    board[i][j] = 'O';
                }
            }
        }
    }

    public void dfs(char[][] board, int x, int y, int n, int m) {
        if (x < 0 || x >= n || y < 0 || y >= m || board[x][y] != 'O') {
            return;
        }
        board[x][y] = 'A';
        dfs(board, x + 1, y, n, m);
        dfs(board, x - 1, y, n, m);
        dfs(board, x, y + 1, n, m);
        dfs(board, x, y - 1, n, m);
    }

    public void solveBFS(char[][] board) {
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        int n = board.length;
        if (n <= 0) {
            return;
        }
        int m = board[0].length;
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (board[i][0] == 'O') {
                queue.offer(new int[]{i, 0});
                board[i][0] = 'A';
            }
            if (board[i][m - 1] == 'O') {
                queue.offer(new int[]{i, m - 1});
                board[i][m - 1] = 'A';
            }
        }
        for (int i = 1; i < m - 1; i++) {
            if (board[0][i] == 'O') {
                queue.offer(new int[]{0, i});
                board[0][i] = 'A';
            }
            if (board[n - 1][i] == 'O') {
                queue.offer(new int[]{n - 1, i});
                board[n - 1][i] = 'A';
            }
        }
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0], y = cell[1];
            for (int i = 0; i < 4; i++) {
                int mx = x + dx[i], my = y + dy[i];
                if (mx < 0 || mx >= n || my < 0 || my >= m || board[mx][my] != 'O') {
                    continue;
                }
                queue.offer(new int[]{mx, my});
                board[mx][my] = 'A';
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                } else if (board[i][j] == 'A') {
                    board[i][j] = 'O';
                }
            }
        }
    }

    // 200 岛屿数量
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int num = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    num++;
                    dfs(grid, i, j);
                }
            }
        }
        return num;
    }

    private void dfs(char[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] == '0') {
            return;
        }
        grid[i][j] = '0';
        dfs(grid, i - 1, j);
        dfs(grid, i + 1, j);
        dfs(grid, i, j - 1);
        dfs(grid, i, j + 1);
    }

    // 463 岛屿的周长
    public int islandPerimeter(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    return islandPerimeterDfs(grid, i, j);
                }
            }
        }
        return 0;
    }

    private int islandPerimeterDfs(int[][] grid, int x, int y) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
            return 1;
        }
        if (grid[x][y] == 0) return 1;
        if (grid[x][y] == 2) return 0;
        grid[x][y] = 2;
        return islandPerimeterDfs(grid, x + 1, y) +
                islandPerimeterDfs(grid, x - 1, y) +
                islandPerimeterDfs(grid, x, y + 1) +
                islandPerimeterDfs(grid, x, y - 1);
    }

    public int maxAreaOfIsland(int[][] grid) {
        int max = 0;
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    max = Math.max(max, maxAreaOfIslanddfs(grid, i, j, visited));
                }
            }
        }
        return max;
    }

    private int maxAreaOfIslanddfs(int[][] grid, int x, int y, boolean[][] visited) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length || grid[x][y] == 0 || visited[x][y]) {
            return 0;
        }
        visited[x][y] = true;
        return 1 + maxAreaOfIslanddfs(grid, x + 1, y, visited)
                + maxAreaOfIslanddfs(grid, x - 1, y, visited)
                + maxAreaOfIslanddfs(grid, x, y + 1, visited)
                + maxAreaOfIslanddfs(grid, x, y - 1, visited);
    }

    //并查集
    static class UnionFind {
        int count;
        int[] parent;
        int[] rank;

        public UnionFind(char[][] grid) {
            count = 0;
            int m = grid.length;
            int n = grid[0].length;
            parent = new int[m * n];
            rank = new int[m * n];
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (grid[i][j] == '1') {
                        parent[i * n + j] = i * n + j;
                        ++count;
                    }
                    rank[i * n + j] = 0;
                }
            }
        }

        public int find(int i) {
            if (parent[i] != i) parent[i] = find(parent[i]);
            return parent[i];
        }

        public void union(int x, int y) {
            int rootx = find(x);
            int rooty = find(y);
            if (rootx != rooty) {
                if (rank[rootx] > rank[rooty]) {
                    parent[rooty] = rootx;
                } else if (rank[rootx] < rank[rooty]) {
                    parent[rootx] = rooty;
                } else {
                    parent[rooty] = rootx;
                    rank[rootx] += 1;
                }
                --count;
            }
        }

        public int getCount() {
            return count;
        }
    }

    public int numIslandsUnionFind(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int nr = grid.length;
        int nc = grid[0].length;
        int num_islands = 0;
        UnionFind uf = new UnionFind(grid);
        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    grid[r][c] = '0';
                    if (r - 1 >= 0 && grid[r - 1][c] == '1') {
                        uf.union(r * nc + c, (r - 1) * nc + c);
                    }
                    if (r + 1 < nr && grid[r + 1][c] == '1') {
                        uf.union(r * nc + c, (r + 1) * nc + c);
                    }
                    if (c - 1 >= 0 && grid[r][c - 1] == '1') {
                        uf.union(r * nc + c, r * nc + c - 1);
                    }
                    if (c + 1 < nc && grid[r][c + 1] == '1') {
                        uf.union(r * nc + c, r * nc + c + 1);
                    }
                }
            }
        }

        return uf.getCount();
    }

    //1254. 统计封闭岛屿的数目
    static int[][] dir = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public int closedIsland(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int ans = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    Queue<int[]> qu = new ArrayDeque<int[]>();
                    grid[i][j] = 1;
                    boolean closed = true;

                    qu.offer(new int[]{i, j});
                    while (!qu.isEmpty()) {
                        int[] arr = qu.poll();
                        int cx = arr[0], cy = arr[1];
                        if (cx == 0 || cy == 0 || cx == m - 1 || cy == n - 1) {
                            closed = false;
                        }
                        for (int d = 0; d < 4; d++) {
                            int nx = cx + dir[d][0];
                            int ny = cy + dir[d][1];
                            if (nx >= 0 && nx < m && ny >= 0 && ny < n && grid[nx][ny] == 0) {
                                grid[nx][ny] = 1;
                                qu.offer(new int[]{nx, ny});
                            }
                        }
                    }
                    if (closed) {
                        ans++;
                    }
                }
            }
        }
        return ans;
    }

    // 733 图像渲染
    public int[][] floodFill(int[][] image, int sr, int sc, int color) {
        int origin = image[sr][sc];
        if (origin == color) return image;
        dfs(image, sr, sc, origin, color);
        return image;
    }

    private void dfs(int[][] image, int x, int y, int origin, int color) {
        if (x < 0 || x >= image.length || y < 0 || y >= image[0].length || image[x][y] != origin) return;
        image[x][y] = color;
        dfs(image, x + 1, y, origin, color);
        dfs(image, x - 1, y, origin, color);
        dfs(image, x, y + 1, origin, color);
        dfs(image, x, y - 1, origin, color);
    }

    //面试题 16.19. 水域大小
    public int[] pondSizes(int[][] land) {
        List<Integer> ans = new ArrayList<>();
        int m = land.length, n = land[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (land[i][j] == 0) {
                    ans.add(pondSizesDfs(land, i, j));
                }
            }
        }
        return ans.stream().sorted().mapToInt(p -> p).toArray();

    }

    private int pondSizesDfs(int[][] land, int x, int y) {
        if (x >= land.length || x < 0 || y >= land[0].length || y < 0 || land[x][y] != 0) {
            return 0;
        }
        land[x][y] = -1;
        int ans = 1;
        ans += pondSizesDfs(land, x + 1, y);
        ans += pondSizesDfs(land, x - 1, y);
        ans += pondSizesDfs(land, x, y + 1);
        ans += pondSizesDfs(land, x, y - 1);
        ans += pondSizesDfs(land, x + 1, y - 1);
        ans += pondSizesDfs(land, x - 1, y - 1);
        ans += pondSizesDfs(land, x + 1, y + 1);
        ans += pondSizesDfs(land, x - 1, y + 1);
        return ans;
    }

    // 1034 边界着色
    // 存储边界list
    public int[][] colorBorderDFS(int[][] grid, int row, int col, int color) {
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        List<int[]> boards = new ArrayList<>();
        int originColor = grid[row][col];
        visited[row][col] = true;
        dfs(grid, row, col, visited, boards, originColor);
        for (int[] board : boards) {
            grid[board[0]][board[1]] = color;
        }
        return grid;
    }

    private void dfs(int[][] grid, int x, int y, boolean[][] visited, List<int[]> boards, int originColor) {
        int m = grid.length, n = grid[0].length;
        int[][] directions = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        boolean isBoard = false;
        for (int[] dire : directions) {
            int newX = x + dire[0], newY = y + dire[1];
            if (!(newX >= 0 && newX < m && newY >= 0 && newY < n && grid[newX][newY] == originColor)) {
                isBoard = true;
            } else if (!visited[newX][newY]) {
                visited[newX][newY] = true;
                dfs(grid, newX, newY, visited, boards, originColor);
            }
        }
        if (isBoard) {
            boards.add(new int[]{x, y});
        }
    }

    public int[][] colorBorderBFS(int[][] grid, int row, int col, int color) {
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        List<int[]> borders = new ArrayList<>();
        int originalColor = grid[row][col];
        int[][] direc = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        Deque<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{row, col});
        visited[row][col] = true;
        while (!q.isEmpty()) {
            int[] node = q.poll();
            int x = node[0], y = node[1];

            boolean isBorder = false;
            for (int i = 0; i < 4; i++) {
                int nx = direc[i][0] + x, ny = direc[i][1] + y;
                if (!(nx >= 0 && nx < m && ny >= 0 && ny < n && grid[nx][ny] == originalColor)) {
                    isBorder = true;
                } else if (!visited[nx][ny]) {
                    visited[nx][ny] = true;
                    q.offer(new int[]{nx, ny});
                }
            }
            if (isBorder) {
                borders.add(new int[]{x, y});
            }
        }
        for (int[] border : borders) {
            int x = border[0], y = border[1];
            grid[x][y] = color;
        }
        return grid;
    }

    // 934 最短的桥
    // 先dfs找其中一个岛的所有点放到队列，再BFS
    public int shortestBridge(int[][] grid) {
        row = grid.length;
        col = grid[0].length;
        boolean[][] visited = new boolean[row][col];
        Queue<int[]> queue = new ArrayDeque<>();
        boolean find = false;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 1) {
                    find = true;
                    dfs(grid, i, j, queue, visited);
                    break;
                }
            }
            if (find) break;
        }
        int len = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] tmp = queue.poll();
                for (int[] dire : directions) {
                    int newX = tmp[0] + dire[0];
                    int newY = tmp[1] + dire[1];
                    if (inAreaRowCol(newX, newY) && !visited[newX][newY]) {
                        if (grid[newX][newY] == 1) return len;
                        visited[newX][newY] = true;
                        queue.offer(new int[]{newX, newY});
                    }
                }
            }
            len++;
        }
        return len;
    }

    private void dfs(int[][] grid, int i, int j, Queue<int[]> queue, boolean[][] visited) {
        if (!inAreaRowCol(i, j) || grid[i][j] == 0 || visited[i][j]) return;
        visited[i][j] = true;
        queue.offer(new int[]{i, j});
        for (int[] dire : directions) {
            dfs(grid, i + dire[0], j + dire[1], queue, visited);
        }
    }

    //给你一个大小为 m x n 的矩阵 board 表示甲板，其中，每个单元格可以是一艘战舰 'X' 或者是一个空位 '.' ，返回在甲板 board 上放置的
// 战舰 的数量。
// 战舰 只能水平或者垂直放置在 board 上。换句话说，战舰只能按 1 x k（1 行，k 列）或 k x 1（k 行，1 列）的形状建造，其中 k 可以
//是任意大小。两艘战舰之间至少有一个水平或垂直的空位分隔 （即没有相邻的战舰）。
//输入：board = [["X",".",".","X"],[".",".",".","X"],[".",".",".","X"]]
//输出：2
    public int countBattleships(char[][] board) {
        if (board.length <= 0) return 0;
        int nums = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++)
                if (board[i][j] == 'X') {
                    nums++;
                    dfsMatrix(board, i, j);
                }
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++)
                if (board[i][j] == '-') {
                    board[i][j] = 'X';
                }
        }
        return nums;
    }

    private void dfsMatrix(char[][] board, int i, int j) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != 'X') {
            return;
        }
        board[i][j] = '-';
        dfsMatrix(board, i - 1, j);
        dfsMatrix(board, i + 1, j);
        dfsMatrix(board, i, j - 1);
        dfsMatrix(board, i, j + 1);
    }

    public int countBattleshipsOnce(char[][] board) {
        if (board.length <= 0) return 0;
        int nums = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != 'X') continue;
                if (i > 0 && board[i - 1][j] == 'X') continue;
                if (j > 0 && board[i][j - 1] == 'X') continue;
                nums++;
            }
        }
        return nums;
    }

    //1219 黄金矿工
    public int getMaximumGold(int[][] grid) {
        int max = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != 0) {
                    boolean[][] visited = new boolean[grid.length][grid[0].length];
                    int num = dfs(grid, i, j, visited);
                    max = Math.max(num, max);
                }
            }
        }
        return max;

    }

    private int dfs(int[][] grid, int i, int j, boolean[][] visited) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || visited[i][j] || grid[i][j] == 0) {
            return 0;
        }
        visited[i][j] = true;
        int ileft = dfs(grid, i - 1, j, visited);
        int iright = dfs(grid, i + 1, j, visited);
        int jtop = dfs(grid, i, j - 1, visited);
        int jbottom = dfs(grid, i, j + 1, visited);
        visited[i][j] = false;
        return grid[i][j] + Math.max(Math.max(ileft, iright), Math.max(jtop, jbottom));
    }

    //2658. 网格图中鱼的最大数目
    public int findMaxFish(int[][] grid) {
        this.m = grid.length;
        this.n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] > 0 && !visited[i][j]) {
                    max = Math.max(max, dfs(i, j, grid, visited));
                }
            }
        }
        return max;
    }

    private int dfs(int x, int y, int[][] grid, boolean[][] visited) {
        if (x < 0 || x >= m || y < 0 || y >= n || grid[x][y] == 0 || visited[x][y]) return 0;
        int fish = grid[x][y];
        visited[x][y] = true;
        for (int[] dire : directions) {
            int newX = x + dire[0];
            int newY = y + dire[1];
            fish += dfs(newX, newY, grid, visited);
        }
        return fish;
    }

    // 面试08.02 迷路的机器人
    boolean find = false;
    public List<List<Integer>> pathWithObstacles(int[][] obstacleGrid) {
        List<List<Integer>> path = new ArrayList<>();
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        boolean[][] visited = new boolean[m][n];
        dfs(obstacleGrid, 0, 0, path, visited);
        return find ? path : new ArrayList<>();
    }

    private void dfs(int[][] grid, int x, int y, List<List<Integer>> path, boolean[][] visited) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length || grid[x][y] == 1 || visited[x][y] || find)
            return;
        visited[x][y] = true;
        path.add(Arrays.asList(x, y));
        if (x == grid.length - 1 && y == grid[0].length - 1) find = true;
        dfs(grid, x, y + 1, path, visited);
        dfs(grid, x + 1, y, path, visited);
        if (!find) path.remove(path.size() - 1);
    }

    //51 n皇后
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        Set<Integer> columns = new HashSet<>();
        Set<Integer> diaLeft = new HashSet<>();
        Set<Integer> diaRight = new HashSet<>();
        int[] rows = new int[n];
        solveNQueensDfs(result, columns, diaLeft, diaRight, n, rows, 0);
        return result;
    }

    private void solveNQueensDfs(List<List<String>> result, Set<Integer> columns, Set<Integer> diaLeft, Set<Integer> diaRight, int n, int[] rows, int row) {
        if (row == n) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                char[] chars = new char[n];
                Arrays.fill(chars, '.');
                chars[rows[i]] = 'Q';
                list.add(new String(chars));
            }
            result.add(list);
        } else {
            for (int i = 0; i < n; i++) {
                if (columns.contains(i)) {
                    continue;
                }
                if (diaLeft.contains(row + i)) {
                    continue;
                }
                if (diaRight.contains(row - i)) {
                    continue;
                }
                columns.add(i);
                diaLeft.add(row + i);
                diaRight.add(row - i);
                rows[row] = i;
                solveNQueensDfs(result, columns, diaLeft, diaRight, n, rows, row + 1);
                columns.remove(i);
                diaLeft.remove(row + i);
                diaRight.remove(row - i);
                rows[row] = 0;
            }
        }
    }

    //1222. 可以攻击国王的皇后
    public List<List<Integer>> queensAttacktheKing(int[][] queens, int[] king) {
        Set<Integer> queenPos = new HashSet<Integer>();
        for (int[] queen : queens) {
            int x = queen[0], y = queen[1];
            queenPos.add(x * 8 + y);
        }

        List<List<Integer>> ans = new ArrayList<List<Integer>>();
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int kx = king[0] + dx, ky = king[1] + dy;
                while (kx >= 0 && kx < 8 && ky >= 0 && ky < 8) {
                    int pos = kx * 8 + ky;
                    if (queenPos.contains(pos)) {
                        List<Integer> posList = new ArrayList<Integer>();
                        posList.add(kx);
                        posList.add(ky);
                        ans.add(posList);
                        break;
                    }
                    kx += dx;
                    ky += dy;
                }
            }
        }
        return ans;
    }

    // 417. 太平洋大西洋水流问题
    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> result = new ArrayList<>();
        int m = heights.length;
        int n = heights[0].length;
        boolean[][] pacific = new boolean[m][n];
        boolean[][] atlantic = new boolean[m][n];

        for (int i = 0; i < m; i++) {
            pacificAtlanticDfs(heights, i, 0, pacific);
            pacificAtlanticDfs(heights, i, n - 1, atlantic);
        }
        for (int i = 0; i < n; i++) {
            pacificAtlanticDfs(heights, 0, i, pacific);
            pacificAtlanticDfs(heights, m - 1, i, atlantic);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (pacific[i][j] && atlantic[i][j]) {
                    result.add(Arrays.asList(i, j));
                }
            }
        }
        return result;
    }

    private void pacificAtlanticBfs(int[][] heights, int row, int col, boolean[][] oceans) {
        Deque<int[]> deque = new ArrayDeque<>();
        deque.offer(new int[]{row, col});
        oceans[row][col] = true;
        while (!deque.isEmpty()) {
            int[] cell = deque.poll();
            for (int[] dire : directions) {
                int newRow = cell[0] + dire[0];
                int newCol = cell[1] + dire[1];
                if (newRow >= 0 && newRow < heights.length && newCol >= 0 && newCol < heights[0].length
                        && heights[newRow][newCol] >= heights[cell[0]][cell[1]] && !oceans[cell[0]][cell[1]]) {
                    oceans[newRow][newCol] = true;
                    deque.offer(new int[]{newRow, newCol});
                }
            }
        }
    }

    private void pacificAtlanticDfs(int[][] heights, int row, int col, boolean[][] oceans) {
        if (oceans[row][col]) return;
        oceans[row][col] = true;
        for (int[] dire : directions) {
            int newRow = row + dire[0];
            int newCol = col + dire[1];
            if (newRow >= 0 && newRow < heights.length && newCol >= 0 && newCol < heights[0].length
                    && heights[newRow][newCol] >= heights[row][col]) {
                pacificAtlanticDfs(heights, newRow, newCol, oceans);
            }
        }
    }

    //1020 飞地的数量
    public int numEnclavesDFS(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 0; i < m; i++) {
            if (grid[i][0] == 1) dfs(grid, i, 0);
            if (grid[i][n - 1] == 1) dfs(grid, i, n - 1);
        }
        for (int i = 0; i < n; i++) {
            if (grid[0][i] == 1) dfs(grid, 0, i);
            if (grid[m - 1][i] == 1) dfs(grid, m - 1, i);
        }
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) ans++;
            }
        }
        return ans;
    }

    private void dfs(int[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] != 1) {
            return;
        }
        grid[i][j] = 2;
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j - 1);
        dfs(grid, i, j + 1);
    }

    public int numEnclavesUnionFind(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        UnionFind2 uf = new UnionFind2(grid);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    int index = i * n + j;
                    if (j + 1 < n && grid[i][j + 1] == 1) {
                        uf.union(index, index + 1);
                    }
                    if (i + 1 < m && grid[i + 1][j] == 1) {
                        uf.union(index, index + n);
                    }
                }
            }
        }
        int enclaves = 0;
        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                if (grid[i][j] == 1 && !uf.isOnEdge(i * n + j)) {
                    enclaves++;
                }
            }
        }
        return enclaves;
    }

    static class UnionFind2 {
        private int[] parent;
        private boolean[] onEdge;
        private int[] rank;

        public UnionFind2(int[][] grid) {
            int m = grid.length, n = grid[0].length;
            parent = new int[m * n];
            onEdge = new boolean[m * n];
            rank = new int[m * n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == 1) {
                        int index = i * n + j;
                        parent[index] = index;
                        if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                            onEdge[index] = true;
                        }
                    }
                }
            }
        }

        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]);
            }
            return parent[i];
        }

        public void union(int x, int y) {
            int rootx = find(x);
            int rooty = find(y);
            if (rootx != rooty) {
                if (rank[rootx] > rank[rooty]) {
                    parent[rooty] = rootx;
                    onEdge[rootx] |= onEdge[rooty];
                } else if (rank[rootx] < rank[rooty]) {
                    parent[rootx] = rooty;
                    onEdge[rooty] |= onEdge[rootx];
                } else {
                    parent[rooty] = rootx;
                    onEdge[rootx] |= onEdge[rooty];
                    rank[rootx]++;
                }
            }
        }

        public boolean isOnEdge(int i) {
            return onEdge[find(i)];
        }
    }

    //2492. 两个城市间路径的最小分数
    // 1和n 保证连接，求1的连通最小权边
    public int minScore(int n, int[][] roads) {
        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, List<Integer>> edges = new HashMap<>();
        for (int[] road : roads) {
            add(dist, edges, road[0], road[1], road[2]);
            add(dist, edges, road[1], road[0], road[2]);
        }
        int min = Integer.MAX_VALUE;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(1);
        boolean[] visited = new boolean[n + 1];
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            for (int near : edges.getOrDefault(cur, new ArrayList<>())) {
                if (visited[near]) continue;
                visited[near] = true;
                queue.offer(near);
                min = Math.min(min, dist.getOrDefault(near, Integer.MAX_VALUE));
            }
        }
        return min;
    }

    private void add(Map<Integer, Integer> dist, Map<Integer, List<Integer>> edges, int x, int y, int d) {
        List<Integer> list = edges.getOrDefault(x, new ArrayList<>());
        list.add(y);
        edges.put(x, list);
        if (!dist.containsKey(x) || (dist.containsKey(x) && dist.get(x) > d)) {
            dist.put(x, d);
        }
    }

    //3中存图方式 N点 M边
    // 邻接矩阵数组：w[a][b] = c 代表从 a 到 b 有权重为 c 的边 M = N^2
    int Nodes, Edges;
    int[][] weigth = new int[Nodes][Nodes];

    // 加边操作
    void add(int a, int b, int c) {
        weigth[a][b] = c;
    }
    // 链式前向星存图 链表头插法
//    首先 idx 是用来对边进行编号的，然后对存图用到的几个数组作简单解释：
//    he 数组：存储是某个节点所对应的边的集合（链表）的头结点；
//    e 数组：由于访问某一条边指向的节点；
//    ne 数组：由于是以链表的形式进行存边，该数组就是用于找到下一条边；
//    w 数组：用于记录某条边的权重为多少

    int[] he = new int[Nodes], e = new int[Edges], ne = new int[Edges], w = new int[Edges];
    int idx;

    void add2(int a, int b, int c) {
        e[idx] = b;
        ne[idx] = he[a];
        he[a] = idx;
        w[idx] = c;
        idx++;
    }

    void traverse(int a) {
        for (int i = he[a]; i != -1; i = ne[i]) {
            int b = e[i], c = w[i]; // 存在由 a 指向 b 的边，权重为 c
        }
    }
// endregion ----------------------------------------------------------------------------------------------------------


}
