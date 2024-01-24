import java.util.*;

public class SolutionUnionFind {

    // region ------------------------------------------------并查集------------------------------------------------------

    // 399 计算除法
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        int equationsSize = equations.size();

        UnionFind3 unionFind = new UnionFind3(2 * equationsSize);
        // 第 1 步：预处理，将变量的值与 id 进行映射，使得并查集的底层使用数组实现，方便编码
        Map<String, Integer> hashMap = new HashMap<>(2 * equationsSize);
        int id = 0;
        for (int i = 0; i < equationsSize; i++) {
            List<String> equation = equations.get(i);
            String var1 = equation.get(0);
            String var2 = equation.get(1);

            if (!hashMap.containsKey(var1)) {
                hashMap.put(var1, id);
                id++;
            }
            if (!hashMap.containsKey(var2)) {
                hashMap.put(var2, id);
                id++;
            }
            unionFind.union(hashMap.get(var1), hashMap.get(var2), values[i]);
        }

        // 第 2 步：做查询
        int queriesSize = queries.size();
        double[] res = new double[queriesSize];
        for (int i = 0; i < queriesSize; i++) {
            String var1 = queries.get(i).get(0);
            String var2 = queries.get(i).get(1);

            Integer id1 = hashMap.get(var1);
            Integer id2 = hashMap.get(var2);

            if (id1 == null || id2 == null) {
                res[i] = -1.0d;
            } else {
                res[i] = unionFind.isConnected(id1, id2);
            }
        }
        return res;
    }

    private class UnionFind3 {

        private int[] parent;

        /**
         * 指向的父结点的权值
         */
        private double[] weight;


        public UnionFind3(int n) {
            this.parent = new int[n];
            this.weight = new double[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                weight[i] = 1.0d;
            }
        }

        public void union(int x, int y, double value) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return;
            }

            parent[rootX] = rootY;
            // 关系式的推导请见「参考代码」下方的示意图
            // x/y= value,y/root[y] = weight[y],x/root[x] = weight[x]
            // => root[x]/root[y] = (x/weight[x]) / (y/weight[y]) = x * weight[y]/ y*weight[x]
            weight[rootX] = weight[y] * value / weight[x];
        }

        /**
         * 路径压缩
         *
         * @param x
         * @return 根结点的 id
         */
        public int find(int x) {
            if (x != parent[x]) {
                int origin = parent[x];
                parent[x] = find(parent[x]);
                weight[x] *= weight[origin];
            }
            return parent[x];
        }

        public double isConnected(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                // x/rooX = weight[x]
                // y/rootY = weight[y]
                //x/y == weight[x]/weight[y]
                return weight[x] / weight[y];
            } else {
                return -1.0d;
            }
        }
    }

    //765 情侣牵手
    public int minSwapsCouples(int[] row) {
        int n = row.length, m = n / 2;
        UnionFind1 unionFind = new UnionFind1(m);
        for (int i = 0; i < n; i += 2) {
            // row[i]/2 是情侣对的下标，下标相同的属于同一情侣对
            unionFind.union(row[i] / 2, row[i + 1] / 2);
        }
        int count = 0;//连通环的个数  合并后，每有一个顶点相同的就有一个环
        for (int i = 0; i < m; i++) {
            if (unionFind.find(i) == i) count++;
        }
        // 重新遍历情侣对，这样会合并路径，最后每个环的情侣对-1，累加=情侣对-环数

//        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
//        for (int i = 0; i < m; i++) {
//            int fx = unionFind.find( i);
//            map.put(fx, map.getOrDefault(fx, 0) + 1);
//        }
//
//        int ret = 0;
//        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
//            ret += entry.getValue() - 1;
//        }
//        return ret
        return m - count;
    }

    // 323 无向图中连通分量的数目
    public int countComponents(int n, int[][] edges) {
        UnionFind1 unionFind = new UnionFind1(n);
        for (int[] edge : edges) {
            unionFind.union(edge[0], edge[1]);
        }
        return unionFind.getConnectedNum();
    }

    // 547 省份数量
    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        UnionFind1 unionFind = new UnionFind1(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isConnected[i][j] == 1) {
                    unionFind.union(i, j);
                }
            }
        }
        return unionFind.getConnectedNum();
    }

    // 1319 连通网络的操作次数 连通分量-1
    // 拓扑排序做法 makeConnectedDfs
    public int makeConnected(int n, int[][] connections) {
        //connections.length =线缆数量 +1>=节点数
        if (connections.length + 1 < n) return -1;
        UnionFind1 unionFind = new UnionFind1(n);
        for (int[] con : connections) {
            unionFind.union(con[0], con[1]);
        }
        return unionFind.getConnectedNum() - 1;
    }

    // 684 冗余链接 offer 118 多余的边
//    初始时，每个节点都属于不同的连通分量。遍历每一条边，判断这条边连接的两个顶点是否属于相同的连通分量。
//    如果两个顶点属于不同的连通分量，则说明在遍历到当前的边之前，这两个顶点之间不连通，因此当前的边不会导致环出现，合并这两个顶点的连通分量。
//    如果两个顶点属于相同的连通分量，则说明在遍历到当前的边之前，这两个顶点之间已经连通，因此当前的边导致环出现，为附加的边，将当前的边作为答案返回。
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        UnionFind1 unionFind = new UnionFind1(n);
        for (int[] edge : edges) {
            int x = edge[0], y = edge[1];
            if (unionFind.isConnect(x, y)) return edge;
            unionFind.union(x, y);
        }
        return new int[0];
    }

    // 261 以图判树
    public boolean validTree(int n, int[][] edges) {
        UnionFind1 unionFind = new UnionFind1(n);
        for (int[] edge : edges) {
            if (unionFind.isConnect(edge[0], edge[1])) {
                return false;
            }
            unionFind.union(edge[0], edge[1]);
        }
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (i == unionFind.find(i)) {
                cnt++;
            }
        }
        return cnt == 1;
    }

    //2316. 统计无向图中无法互相到达点对数
    public long countPairs(int n, int[][] edges) {
        UnionFind2316 uf = new UnionFind2316(n);
        for (int[] edge : edges) {
            int x = edge[0], y = edge[1];
            uf.union(x, y);
        }
        long res = 0;
        for (int i = 0; i < n; i++) {
            res += n - uf.getSize(uf.find(i));
        }
        return res / 2;
    }

    class UnionFind2316 {
        int[] parents;
        int[] sizes;

        public UnionFind2316(int n) {
            parents = new int[n];
            for (int i = 0; i < n; i++) {
                parents[i] = i;
            }
            sizes = new int[n];
            Arrays.fill(sizes, 1);
        }

        public int find(int x) {
            if (parents[x] == x) {
                return x;
            } else {
                parents[x] = find(parents[x]);
                return parents[x];
            }
        }

        public void union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx != ry) {
                if (sizes[rx] > sizes[ry]) {
                    parents[ry] = rx;
                    sizes[rx] += sizes[ry];
                } else {
                    parents[rx] = ry;
                    sizes[ry] += sizes[rx];
                }
            }
        }

        public int getSize(int x) {
            return sizes[x];
        }
    }

    // 839 相似字符串
    int[] parents;

    public int numSimilarGroups(String[] strs) {
        int n = strs.length;
        parents = new int[n];
        for (int i = 0; i < n; i++) {
            parents[i] = i;
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int rootI = find(i);
                int rootJ = find(j);
                if (rootI == rootJ) {
                    continue;
                }
                if (check(strs[i], strs[j])) {
                    parents[rootI] = rootJ;
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (parents[i] == i) {
                ans++;
            }
        }
        return ans;
    }

    public boolean check(String s1, String s2) {
        int n = s1.length();
        int diff = 0;
        for (int i = 0; i < n; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                diff++;
            }
            if (diff > 2) return false;
        }
        return true;
    }

    // 952 按公因式计算最大组件大小
    public int largestComponentSize(int[] nums) {
        int m = Arrays.stream(nums).max().getAsInt();
        //union的值，所以范围是值+1
        UnionFind1 uf = new UnionFind1(m + 1);
        for (int num : nums) {
            for (int i = 2; i * i <= num; i++) {
                if (num % i == 0) {
                    uf.union(num, i);
                    uf.union(num, num / i);
                }
            }
        }
        int[] counts = new int[m + 1];
        int ans = 0;
        for (int num : nums) {
            int root = uf.find(num);
            counts[root]++;
            ans = Math.max(ans, counts[root]);
        }
        return ans;
    }

    //解法2
    static int Node = 20010;
    static int[] p = new int[Node], sz = new int[Node];
    int ans952 = 1;

    int find(int x) {
        if (p[x] != x) p[x] = find(p[x]);
        return p[x];
    }

    void union(int a, int b) {
        if (find(a) == find(b)) return;
        sz[find(a)] += sz[find(b)];
        p[find(b)] = p[find(a)];
        ans952 = Math.max(ans952, sz[find(a)]);
    }

    public int largestComponentSize2(int[] nums) {
        int n = nums.length;
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int cur = nums[i];
            for (int j = 2; j * j <= cur; j++) {
                if (cur % j == 0) add(map, j, i);
                while (cur % j == 0) cur /= j;
            }
            if (cur > 1) add(map, cur, i);
        }
        for (int i = 0; i <= n; i++) {
            p[i] = i;
            sz[i] = 1;
        }
        for (int key : map.keySet()) {
            List<Integer> list = map.get(key);
            for (int i = 1; i < list.size(); i++) union(list.get(0), list.get(i));
        }
        return ans952;
    }

    void add(Map<Integer, List<Integer>> map, int key, int val) {
        List<Integer> list = map.getOrDefault(key, new ArrayList<>());
        list.add(val);
        map.put(key, list);
    }

    static class UnionFind1 {
        int[] parents;
        int[] rank;
        int connectedNum; // 连通分量数

        public UnionFind1(int n) {
            parents = new int[n];
            rank = new int[n];
            connectedNum = n; //默认所有点都不连通
            for (int i = 0; i < n; i++) {
                parents[i] = i;
            }
        }

        public int find(int x) {
            return parents[x] == x ? x : (parents[x] = find(parents[x]));
        }

        public void union(int x, int y) {
            int rootx = find(x);
            int rooty = find(y);
            if (rootx != rooty) {
                connectedNum--; // union 两个，连通分量减少1个
                if (rank[rootx] > rank[rooty]) {
                    parents[rooty] = rootx;
                } else if (rank[rootx] < rank[rooty]) {
                    parents[rootx] = rooty;
                } else {
                    parents[rooty] = rootx;
                    rank[rootx]++;
                }
            }
        }

        public int getConnectedNum() {
            return connectedNum;
        }

        public boolean isConnect(int x, int y) {
            return find(x) == find(y);
        }
    }

    // 1697. 检查边长度限制的路径是否存在
    public boolean[] distanceLimitedPathsExist(int n, int[][] edgeList, int[][] queries) {
        // 将edgeList和queries分别按边的权重从小到大排序
        Arrays.sort(edgeList, Comparator.comparingInt(o -> o[2]));
        // 保存下标数组，用于ans的下标，避免排序完queries后ans找不到对应的idx
        Integer[] idx = new Integer[queries.length];
        for (int i = 0; i < idx.length; i++) {
            idx[i] = i;
        }
        Arrays.sort(idx, Comparator.comparingInt(o -> queries[o][2]));
        int k = 0;
        UnionFind1 unionFind = new UnionFind1(n);
        boolean[] ans = new boolean[queries.length];
        for (int i : idx) {
            // 离线查询：将小于当前query的limit的边合并，k指向下一个大于limit的边
            // 下一个查询时，利用已有的结果
            while (k < edgeList.length && edgeList[k][2] < queries[i][2]) {
                unionFind.union(edgeList[k][0], edgeList[k][1]);
                k++;
            }
            ans[i] = unionFind.isConnect(queries[i][0], queries[i][1]);
        }
        return ans;

    }

    //2685. 统计完全连通分量的数量

    public int countCompleteComponents1(int n, int[][] edges) {
        List<Integer>[] list = new List[n];
        for (int i = 0; i < n; i++) {
            list[i] = new ArrayList<>();
        }
        UnionFind1 uf = new UnionFind1(n);
        for (int[] edge : edges) {
            list[edge[0]].add(edge[1]);
            uf.union(edge[0], edge[1]);
        }
        int[] pointCnt = new int[n];
        int[] edgeCnt = new int[n];
        for (int i = 0; i < n; i++) {
            int p = uf.find(i);
            pointCnt[p]++;
            edgeCnt[p] += list[i].size();
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (pointCnt[i] > 0 && pointCnt[i] * (pointCnt[i] - 1) / 2 == edgeCnt[i]) {
                ans++;
            }
        }
        return ans;
    }

    public int countCompleteComponents(int n, int[][] edges) {
        UnionFind2685 uf = new UnionFind2685(n);
        for (int[] e : edges) {
            uf.union(e[0], e[1]);
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (uf.find(i) == i && uf.pointCnt[i] * (uf.pointCnt[i] - 1) / 2 == uf.edgeCnt[i]) {
                ans++;
            }
        }
        return ans;
    }


    class UnionFind2685 {
        int[] p;
        // 联通分量个数
        int count;
        // 某联通分量内点的个数
        int[] pointCnt;
        // 某联通分量内边的个数
        int[] edgeCnt;

        public UnionFind2685(int n) {
            p = new int[n];
            pointCnt = new int[n];
            edgeCnt = new int[n];
            for (int i = 0; i < n; i++) {
                p[i] = i;
                pointCnt[i] = 1;
                edgeCnt[i] = 0;
            }
            count = n;
        }

        public int find(int x) {
            return p[x] == x ? x : (p[x] = find(p[x]));
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                edgeCnt[rootX]++;
                return;
            }
            if (rootX < rootY) {
                p[rootX] = rootY;
                pointCnt[rootY] += pointCnt[rootX];
                edgeCnt[rootY] += edgeCnt[rootX] + 1;
            } else {
                p[rootY] = rootX;
                pointCnt[rootX] += pointCnt[rootY];
                edgeCnt[rootX] += edgeCnt[rootY] + 1;
            }
            count--;

        }
    }

    //面试题 17.07. 婴儿名字 字符串并查集
    class UnionFindString {
        Map<String, String> parents;
        Map<String, Integer> frequency;

        public UnionFindString(String[] names) {
            parents = new HashMap<>();
            frequency = new HashMap<>();
            for (String s : names) {
                String name = s.substring(0, s.indexOf('('));
                int freq = Integer.parseInt(s.substring(s.indexOf('(') + 1, s.indexOf(')')));
                parents.put(name, name);
                frequency.put(name, freq);
            }
        }

        public String find(String name) {
            if (!parents.containsKey(name)) {
                parents.put(name, name);
                frequency.put(name, 0);
                return name;
            }
            if (parents.get(name).equals(name)) return name;
            parents.put(name, find(parents.get(name)));
            return parents.get(name);
        }

        public void union(String name1, String name2) {
            String root1 = find(name1);
            String root2 = find(name2);
            if (root1.equals(root2)) return;
            int freq1 = frequency.get(root1);
            int freq2 = frequency.get(root2);
            if (root1.compareTo(root2) < 0) {
                parents.put(root2, root1);
                frequency.put(root1, freq1 + freq2);
            } else {
                parents.put(root1, root2);
                frequency.put(root2, freq1 + freq2);
            }
        }

        public int getFreq(String name) {
            return frequency.get(find(name));
        }
    }

    public String[] trulyMostPopular(String[] names, String[] synonyms) {
        UnionFindString unionFind = new UnionFindString(names);
        for (String synonym : synonyms) {
            String[] ns = synonym.split(",");
            unionFind.union(ns[0].substring(1), ns[1].substring(0, ns[1].length() - 1));
        }
        List<String> res = new ArrayList<>();
        for (String s : names) {
            String name = s.substring(0, s.indexOf('('));
            if (name.equals(unionFind.find(name))) {
                res.add(name + "(" + unionFind.getFreq(name) + ")");
            }
        }
        return res.toArray(new String[0]);
    }

    // endregion -----------------------------------------------------------------------------------------
}
