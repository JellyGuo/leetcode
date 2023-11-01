package corpset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Ctrip {
    //构造一个和 a[]等长的排列 b[]，使得 a[i]！=b[i]且 b[]的字典序最小
    public int[] nextArray(int[] a) {
        int n = a.length;
        int[] copy = Arrays.copyOf(a, n);
        Arrays.sort(copy);
        for (int i = 0; i < n - 1; i++) {
            if (copy[i] == a[i]) {
                swap(copy, i, i + 1);
            }
        }
        if (copy[n - 1] == a[n - 1]) {
            swap(copy, n - 1, n - 2);
        }
        return copy;
    }

    private void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    //小红拿到了一个字符串，她可以进行任意次以下操作：
    //选择字符串中的一个字母 ch1和任意一个字母 ch2（ch2可以不在字符串中出现），将字符串s中所有 ch1变成 ch2
    //能否通过一些操作将 s 变成 t
    public static boolean query(String s, String t) {
        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char ch1 = s.charAt(i);
            char ch2 = t.charAt(i);
            if (map.containsKey(ch1)) {
                if (map.get(ch1) != ch2) {
                    return false;
                }
            } else {
                map.put(ch1, ch2);
            }
        }
        return true;
    }

    //游游拿到了一个 m 行 n 列的字母矩阵，矩阵中所有字符都是小写字母；
    //游游想知道，有多少个子矩阵满足每个字母最多只出现一次？
    //1<=n,m<=500 输出只出现一次的子矩阵数量
    public static int subMatrixCnt(char[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][][] sum = new int[m][n][26];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < 26; k++) {
                    sum[i][j][k] = (i > 0 ? sum[i - 1][j][k] : 0) +
                            (j > 0 ? sum[i][j - 1][k] : 0) -
                            ((i > 0 && j > 0) ? sum[i - 1][j - 1][k] : 0);
                }
                sum[i][j][matrix[i][j] - 'a']++;
            }
        }
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                //最多往后找 26行/列，第 27行的话必定有和前面重复的
                for (int p = 0; p < 26; p++) {
                    for (int q = 0; q < 26; q++) {
                        //从 i，j往后的矩形区域大于 26时跳过
                        if ((p + 1) * (q + 1) > 26) continue;
                        if (check(sum, matrix, i, j, i + p, j + q)) ans++;
                    }
                }
            }
        }
        return ans;
    }

    private static boolean check(int[][][] sum, char[][] matrix, int lr, int lc, int rr, int rc) {
        int m = matrix.length, n = matrix[0].length;
        if (rr >= m || rc >= n) return false;
        for (int k = 0; k < 26; k++) {
            if (sum[rr][rc][k] - sum[lr][rc][k] - sum[rr][lc][k] + sum[lr][lc][k] > 1) return false;
        }
        return true;
    }

    //游游拿到了一个正整数，她准备恰好修改其中 k 位，使得该正整数变成75的倍数，一共多少种修改方案
    public int solve(char[] cs, int k) {
        int mod = (int) 1e9 + 7;
        int n = cs.length;
        if (n < 2 || n < k) return 0;
        //f[i][j][m]表示 s[0,i）修改其中的 j 位后，所有位和 %3==m 的方案数
        int[][][] f = new int[n + 1][k + 1][3];
        f[0][0][0] = 1;
        for (int i = 0; i < n - 2; i++) {
            int c = cs[i] - 'a';
            for (int nc = 0; nc < 10; nc++) {
                //第一位不能为 0
                if (i == 0 && nc == 0) continue;
                //没有变化
                if (nc == c) {
                    for (int j = 0; j <= k; j++) {
                        f[i + 1][j][0] = (f[i + 1][j][0] + f[i][j][3 - nc % 3]) % mod;
                        f[i + 1][j][1] = (f[i + 1][j][1] + f[i][j][4 - nc % 3]) % mod;
                        f[i + 1][j][2] = (f[i + 1][j][2] + f[i][j][5 - nc % 3]) % mod;
                    }
                }
            }
        }
        int rs = 0;
        for (String end : new String[]{"00", "25", "50", "75"}) {
            int d = 0;
            for (int i = 0; i < 2; i++) {
                if (end.charAt(2 - 1 - i) != cs[n - 1 - i]) {
                    d++;
                }
            }
            if (k >= d) {
                //s[0,n-2)+end 的数组，能被 75整除则需要所有位的元素和为 3 的倍数
                int r = (3 - sumDigit(end) % 3) % 3;
                rs = rs + f[n - 2][k - d][r] % mod;
            }
        }
        return rs;
    }

    private int sumDigit(String s) {
        int sum = 0;
        for (char c : s.toCharArray()) {
            sum += (c - '0');
        }
        return sum;
    }

}
