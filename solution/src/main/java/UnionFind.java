public class UnionFind {
    int[] parents;
    int[] rank;

    public UnionFind(int n) {
        parents = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parents[i] = i;
            rank[i] = i;
        }
    }

    public int find(int x) {
        return parents[x] == x ? x : (parents[x] = find(parents[x]));
    }

    public void merge(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return;
        if (rank[rootX] > rank[rootY]) {
            parents[y] = rootX;
        } else if (rank[rootX] < rank[rootY]) {
            parents[x] = rootY;
        } else {
            parents[y] = rootX;
            rank[rootX]++;
        }
    }
}
