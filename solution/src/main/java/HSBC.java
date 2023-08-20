import java.util.HashSet;
import java.util.Set;

public class HSBC {
    public int beautifulHouses(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int ans = 0; 
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    Set<String> set = new HashSet<>();
                    dfs(grid, i, j, set);
                    if (check(grid, set)) ans++;
                }
            }
        }
        return ans;
    }

    private boolean check(int[][] grid, Set<String> set) {
        int m = grid.length, n = grid[0].length;
        int[][] directions = new int[][]{{1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
        for (String s : set) {
            int r = Integer.parseInt(s.split(",")[0]);
            int c = Integer.parseInt(s.split(",")[1]);
            for (int[] d : directions) {
                int x = r + d[0];
                int y = c + d[1];
                if (x < 0 || x >= m || y < 0 || y >= n) {
                    continue;
                } else {
                    if (grid[x][y] != 0 && !set.contains(x + "," + y)) return false;
                }
            }
        }
        return true;
    }

    private void dfs(int[][] grid, int i, int j, Set<String> set) {
        int m = grid.length, n = grid[0].length;
        if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] != 1) return;
        set.add(i + "," + j);
        grid[i][j] = 2;
        dfs(grid, i + 1, j, set);
        dfs(grid, i - 1, j, set);
        dfs(grid, i, j + 1, set);
        dfs(grid, i, j - 1, set);
    }

    public static void main(String[] args) {
        HSBC hsbc = new HSBC();
        hsbc.beautifulHouses(new int[][]{
                {1,0,0,0,1,1},
                {0,1,0,0,1,0},
                {0,0,1,0,1,1},
                {1,0,0,0,1,1},
                {1,0,0,0,0,0}});
    }
}
