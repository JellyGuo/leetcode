import java.util.*;

public class SolutionSimulate {
    //region-----------------------------------------模拟---------------------------------------------------
    public String toLowerCase(String str) {
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            int num = charArray[i];
            if (num >= 65 && num <= 90) {
                num += 32;
            }
            charArray[i] = (char) num;
        }

        return String.valueOf(charArray);

    }

    // 6 Z字形变换
    public String convert(String s, int numRows) {
        if (numRows == 1) return s;

        List<StringBuilder> rows = new ArrayList<>();
        for (int i = 0; i < Math.min(numRows, s.length()); i++)
            rows.add(new StringBuilder());

        int curRow = 0;
        boolean goingDown = false;

        for (char c : s.toCharArray()) {
            rows.get(curRow).append(c);
            if (curRow == 0 || curRow == numRows - 1) goingDown = !goingDown;
            curRow += goingDown ? 1 : -1;
        }

        StringBuilder ret = new StringBuilder();
        for (StringBuilder row : rows) ret.append(row);
        return ret.toString();
    }

    // 7 整数翻转
    public int reverse(int x) {
        int rev = 0;
        while (x != 0) {
            if (rev < Integer.MIN_VALUE / 10 || rev > Integer.MAX_VALUE / 10) {
                return 0;
            }
            int digit = x % 10;
            x /= 10;
            rev = rev * 10 + digit;
        }
        return rev;
    }

    // 两个句子里不同的词
    public String[] uncommonFromSentences(String A, String B) {
        HashMap<String, Integer> map = new HashMap<>();
        for (String word : A.split(" ")) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }
        for (String word : B.split(" ")) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }
        List<String> list = new ArrayList<>();
        for (String word : map.keySet()) {
            if (map.get(word) == 1)
                list.add(word);
        }
        return list.toArray(new String[0]);
    }

    // 434 字符串中的单词数
    public int countSegments(String s) {
        int n = s.length();
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) != ' ' && (i == 0 || s.charAt(i - 1) == ' ')) {
                cnt++;
            }
        }
        return cnt;
    }

    // 389 找不同
    public char findTheDifference(String s, String t) {
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            cnt[c - 'a']++;
        }
        for (char c : t.toCharArray()) {
            cnt[c - 'a']--;
        }
        for (int i = 0; i < 26; i++) {
            if (cnt[i] != 0) return (char) ('a' + i);
        }
        return ' ';
    }

    // 806 写字符串需要的行数
    public int[] numberOfLines(int[] widths, String s) {
        int[] result = new int[]{1, 0};
        for (char c : s.toCharArray()) {
            if (100 - result[1] >= widths[c - 'a']) {
                result[1] += widths[c - 'a'];
            } else {
                result[0]++;
                result[1] = widths[c - 'a'];
            }
        }
        return result;
    }

    // offer 58 左旋转字符串
    public String reverseLeftWords(String s, int n) {
        StringBuilder res = new StringBuilder();
        for (int i = n; i < n + s.length(); i++)
            res.append(s.charAt(i % s.length()));
        return res.toString();
    }

    // 面试01.09 字符串轮转
    //字符串轮转。给定两个字符串s1和s2，请编写代码检查s2是否为s1旋转而成（比如，waterbottle是erbottlewat旋转后的字符串）。
    public boolean isFlipedString(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        int n = s1.length();
        if (n == 0) return true;
        for (int i = 0; i < n; i++) {
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (s1.charAt((i + j) % n) != s2.charAt(j)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return true;
            }
        }
        return false;
    }

    // 8 字符串转换整数
    public int myAtoi(String s) {
        int index = 0;
        while (index < s.length() && s.charAt(index) == ' ') {
            index++;
        }
        if (index == s.length()) {
            return 0;
        }

        int sign = 1;
        if (s.charAt(index) == '+') {
            index++;
        } else if (s.charAt(index) == '-') {
            sign = -1;
            index++;
        }
        int res = 0;
        while (index < s.length()) {
            if (s.charAt(index) > '9' || s.charAt(index) < '0') {
                break;
            }
            if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && (s.charAt(index) - '0') > Integer.MAX_VALUE % 10)) {
                return Integer.MAX_VALUE;
            }

            if (res < Integer.MIN_VALUE / 10 || (res == Integer.MIN_VALUE / 10 && (s.charAt(index) - '0') > -(Integer.MIN_VALUE % 10))) {
                return Integer.MIN_VALUE;
            }
            res = res * 10 + sign * (s.charAt(index) - '0');
            index++;
        }
        return res;
    }

    // offer 67 把字符串转换成整数
    public int strToInt(String str) {
        int len = str.length();
        // str.charAt(i) 方法回去检查下标的合法性，一般先转换成字符数组
        char[] charArray = str.toCharArray();

        // 1、去除前导空格
        int index = 0;
        while (index < len && charArray[index] == ' ') {
            index++;
        }

        // 2、如果已经遍历完成（针对极端用例 "      "）
        if (index == len) {
            return 0;
        }

        // 3、如果出现符号字符，仅第 1 个有效，并记录正负
        int sign = 1;
        char firstChar = charArray[index];
        if (firstChar == '+') {
            index++;
        } else if (firstChar == '-') {
            index++;
            sign = -1;
        }

        // 4、将后续出现的数字字符进行转换
        // 不能使用 long 类型，这是题目说的
        int res = 0;
        while (index < len) {
            char currChar = charArray[index];
            // 4.1 先判断不合法的情况
            if (currChar > '9' || currChar < '0') {
                break;
            }

            // 题目中说：环境只能存储 32 位大小的有符号整数，因此，需要提前判：断乘以 10 以后是否越界
            if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && (currChar - '0') > Integer.MAX_VALUE % 10)) {
                return Integer.MAX_VALUE;
            }
            if (res < Integer.MIN_VALUE / 10 || (res == Integer.MIN_VALUE / 10 && (currChar - '0') > -(Integer.MIN_VALUE % 10))) {
                return Integer.MIN_VALUE;
            }

            // 4.2 合法的情况下，才考虑转换，每一步都把符号位乘进去
            res = res * 10 + sign * (currChar - '0');
            index++;
        }
        return res;
    }

    //12整数转罗马数字
    public String intToRoman(int num) {
        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            int count = num / entry.getKey();
            for (int i = 0; i < count; i++) {
                sb.append(entry.getValue());
            }
            num = num % entry.getKey();
        }
        return sb.toString();
    }

    //13 罗马数字转整数
    public int romanToInt(String s) {
        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            int cur = map.get(s.charAt(i));
            if (i < s.length() - 1 && cur < map.get(s.charAt(i + 1))) {
                ans -= cur;
            } else {
                ans += cur;
            }
        }
        return ans;
    }

    // 面试05.02 二进制小数
    public String printBin(double num) {
        StringBuilder ans = new StringBuilder("0.");
        while (ans.length() < 32 && num != 0) {
            num *= 2;
            if (num >= 1) {
                ans.append("1");
                num -= 1;
            } else {
                ans.append("0");
            }
        }
        if (ans.length() >= 32 && num != 0) return "ERROR";
        return ans.toString();
    }

    // 36 有效的数独
    public boolean isValidSudoku(char[][] board) {
        int[][] rows = new int[9][10];
        int[][] cols = new int[9][10];
        int[][] box = new int[9][10];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') continue;
                int num = board[i][j] - '0';
                if (rows[i][num] == 1) return false;
                if (cols[j][num] == 1) return false;
                if (box[(i / 3) * 3 + j / 3][num] == 1) return false;
                rows[i][num] = 1;
                cols[j][num] = 1;
                box[(i / 3) * 3 + j / 3][num] = 1;
            }
        }
        return true;
    }

    // 38 外观数列
    public String countAndSay(int n) {
        String ans = "1";
        for (int i = 2; i <= n; i++) {
            StringBuilder cur = new StringBuilder();
            int len = ans.length();
            for (int j = 0; j < len; ) {
                int k = j + 1;
                while (k < len && ans.charAt(k) == ans.charAt(j)) {
                    k++;
                }
                int cnt = k - j;
                cur.append(cnt).append(ans.charAt(j));
                j = k;
            }
            ans = cur.toString();
        }
        return ans;
    }

    // 401 二进制手表
    public List<String> readBinaryWatch(int turnedOn) {
        List<String> ans = new ArrayList<>();
        for (int h = 0; h < 12; ++h) {
            for (int m = 0; m < 60; ++m) {
                if (Integer.bitCount(h) + Integer.bitCount(m) == turnedOn) {
                    ans.add(h + ":" + (m < 10 ? "0" : "") + m);
                }
            }
        }
        return ans;
    }

    // 1331 数组序号转换
    public int[] arrayRankTransform(int[] arr) {
        Map<Integer, Integer> map = new HashMap<>();
        int[] c = arr.clone();
        Arrays.sort(c);
        int idx = 1;
        for (int v : c) {
            if (!map.containsKey(v)) {
                map.put(v, idx++);
            }
        }
        int[] ans = new int[arr.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = map.get(arr[i]);
        }
        return ans;
    }

    //946 offer31栈的压入弹出序列
    public boolean validateStackSequences(int[] pushed, int[] popped) {
        Stack<Integer> stack = new Stack<>();
        int i = 0;
        for (int n : pushed) {
            stack.push(n);
            while (!stack.isEmpty() && stack.peek() == popped[i]) {
                stack.pop();
                i++;
            }
        }
        return stack.isEmpty();
    }

    // 1598 文件夹操作日志搜集器
    public int minOperations(String[] logs) {
        Stack<String> stack = new Stack<>();
        for (String log : logs) {
            if ("../".equals(log)) {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else if ("./".equals(log)) {

            } else {
                stack.push(log);
            }
        }
        return stack.size();
    }

    // 396 旋转数组
    //给定一个长度为 n 的整数数组 nums 。
// 假设 arrk 是数组 nums 顺时针旋转 k 个位置后的数组，我们定义 nums 的 旋转函数 F 为：
// F(k) = 0 * arrk[0] + 1 * arrk[1] + ... + (n - 1) * arrk[n - 1]
// 返回 F(0), F(1), ..., F(n-1)中的最大值 。
//F(0) = (0 * 4) + (1 * 3) + (2 * 2) + (3 * 6) = 0 + 3 + 4 + 18 = 25
//F(1) = (0 * 6) + (1 * 4) + (2 * 3) + (3 * 2) = 0 + 4 + 6 + 6 = 16
//F(2) = (0 * 2) + (1 * 6) + (2 * 4) + (3 * 3) = 0 + 6 + 8 + 9 = 23
//F(3) = (0 * 3) + (1 * 2) + (2 * 6) + (3 * 4) = 0 + 2 + 12 + 12 = 26
//所以 F(0), F(1), F(2), F(3) 中的最大值是 F(3) = 26 。
    public int maxRotateFunction(int[] nums) {
        int sum = 0, f = 0, n = nums.length;
        for (int i = 0; i < n; i++) {
            sum += nums[i];
            f += i * nums[i];
        }
        int max = f;
        for (int i = n - 1; i > 0; i--) {
            f = f + sum - n * nums[i];
            max = Math.max(f, max);
        }
        return max;
    }

    // 48 旋转图像
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        // n 为偶数时，n/2 n为奇数时 列多取一列
        // 若全部<n 对于一个数 会旋转4次回到原点
        for (int i = 0; i < n / 2; i++) {
            for (int j = 0; j < (n + 1) / 2; j++) {
                int tmp = matrix[i][j];
                // 行变列，列变n-1-col
                matrix[i][j] = matrix[n - 1 - j][i];
                matrix[n - 1 - j][i] = matrix[n - 1 - i][n - 1 - j];
                matrix[n - 1 - i][n - 1 - j] = matrix[j][n - 1 - i];
                matrix[j][n - 1 - i] = tmp;
            }
        }
    }

    // 54 螺旋矩阵
    //1 模拟路径
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> order = new ArrayList<Integer>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return order;
        }
        int rows = matrix.length, columns = matrix[0].length;
        boolean[][] visited = new boolean[rows][columns];
        int total = rows * columns;
        int row = 0, column = 0;
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int directionIndex = 0;
        for (int i = 0; i < total; i++) {
            order.add(matrix[row][column]);
            visited[row][column] = true;
            int nextRow = row + directions[directionIndex][0], nextColumn = column + directions[directionIndex][1];
            if (nextRow < 0 || nextRow >= rows || nextColumn < 0 || nextColumn >= columns || visited[nextRow][nextColumn]) {
                directionIndex = (directionIndex + 1) % 4;
            }
            row += directions[directionIndex][0];
            column += directions[directionIndex][1];
        }
        return order;
    }

    //2 分层模拟
    public List<Integer> spiralOrder1(int[][] matrix) {
        List<Integer> order = new ArrayList<>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return order;
        }
        int rows = matrix.length, columns = matrix[0].length;
        int top = 0, left = 0, right = columns - 1, bottom = rows - 1;
        while (top <= bottom && left <= right) {
            for (int column = left; column <= right; column++) {
                order.add(matrix[top][column]);
            }
            for (int row = top + 1; row <= bottom; row++) {
                order.add(matrix[row][right]);
            }
            if (right > left && bottom > top) {
                for (int column = right - 1; column > left; column--) {
                    order.add(matrix[bottom][column]);
                }
                for (int row = bottom; row > top; row--) {
                    order.add(matrix[row][left]);
                }
            }

            top++;
            bottom--;
            left++;
            right--;
        }
        return order;
    }

    // 59 螺旋矩阵2
    public int[][] generateMatrix(int n) {
        int total = n * n;
        int[][] matrix = new int[n][n];
        int[][] directions = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int directionIdx = 0;
        int x = 0, y = 0;
        boolean[][] visited = new boolean[n][n];
        for (int i = 1; i <= total; i++) {
            matrix[x][y] = i;
            visited[x][y] = true;
            int newX = x + directions[directionIdx][0], newY = y + directions[directionIdx][1];
            if (newX < 0 || newX >= n || newY < 0 || newY >= n || visited[newX][newY]) {
                directionIdx = (directionIdx + 1) % 4;
            }
            x = x + directions[directionIdx][0];
            y = y + directions[directionIdx][1];
        }
        return matrix;
    }

    public int[][] spiralMatrixIII(int R, int C, int r0, int c0) {
        int[][] dirt = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // east, south, west, north
        List<int[]> res = new ArrayList<>();
        int len = 0, d = 0; // move <len> steps in the <d> direction
        res.add(new int[]{r0, c0});
        while (res.size() < R * C) {
            if (d == 0 || d == 2) len++; // when move east or west, the length of path need plus 1
            for (int i = 0; i < len; i++) {
                r0 += dirt[d][0];
                c0 += dirt[d][1];
                if (r0 >= 0 && r0 < R && c0 >= 0 && c0 < C) // check valid
                    res.add(new int[]{r0, c0});
            }
            d = (d + 1) % 4; // turn to next direction
        }
        return res.toArray(new int[R * C][2]);
    }

    //874. 模拟行走机器人
    public int robotSim(int[] commands, int[][] obstacles) {
        int x = 0;
        int y = 0;
        int[][] directions = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        Set<String> obstacleSets = new HashSet<>();
        for (int[] cell : obstacles) {
            obstacleSets.add(cell[0] + "," + cell[1]);
        }
        int directionIdx = 0;
        int max = 0;
        for (int command : commands) {
            if (command == -1) {
                directionIdx = (directionIdx + 1) % 4;
            } else if (command == -2) {
                directionIdx = (directionIdx - 1 + 4) % 4;
            } else {
                for (int i = 0; i < command; i++) {
                    int newX = x + directions[directionIdx][0];
                    int newY = y + directions[directionIdx][1];
                    if (obstacleSets.contains(newX + "," + newY)) {
                        break;
                    }
                    x = newX;
                    y = newY;
                }
                max = Math.max(max, x * x + y * y);
            }
        }
        return max;
    }

    //1041. 困于环中的机器人
    public boolean isRobotBounded(String instructions) {
        int[][] direction = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int directionIdx = 0;
        int x = 0, y = 0;
        for (char c : instructions.toCharArray()) {
            if (c == 'G') {
                x += direction[directionIdx][0];
                y += direction[directionIdx][1];
            } else if (c == 'R') {
                directionIdx = (directionIdx + 1) % 4;
            } else {
                directionIdx = (directionIdx + 3) % 4;
            }
        }
        return (x == 0 && y == 0) || directionIdx != 0;
    }

    //2500. 删除每行中的最大值
    public int deleteGreatestValue(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int cnt = n, ans = 0;
        while (cnt-- > 0) {
            int max = 0;
            for (int i = 0; i < m; i++) {
                int colMax = 0, col = -1;
                for (int j = 0; j < n; j++) {
                    if (!visited[i][j] && grid[i][j] > colMax) {
                        colMax = grid[i][j];
                        col = j;
                    }
                }
                visited[i][col] = true;
                max = Math.max(max, colMax);
            }
            ans += max;
        }
        return ans;
    }

    // 73 矩阵置0
    public void setZeroes(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        // 1. 扫描「首行」和「首列」记录「首行」和「首列」是否该被置零
        boolean r0 = false, c0 = false;
        for (int i = 0; i < m; i++) {
            if (mat[i][0] == 0) {
                r0 = true;
                break;
            }
        }
        for (int j = 0; j < n; j++) {
            if (mat[0][j] == 0) {
                c0 = true;
                break;
            }
        }
        // 2.1 扫描「非首行首列」的位置，如果发现零，将需要置零的信息存储到该行的「最左方」和「最上方」的格子内
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (mat[i][j] == 0) mat[i][0] = mat[0][j] = 0;
            }
        }
        // 2.2 根据刚刚记录在「最左方」和「最上方」格子内的置零信息，进行「非首行首列」置零
        for (int j = 1; j < n; j++) {
            if (mat[0][j] == 0) {
                for (int i = 1; i < m; i++) mat[i][j] = 0;
            }
        }
        for (int i = 1; i < m; i++) {
            if (mat[i][0] == 0) Arrays.fill(mat[i], 0);
        }
        // 3. 根据最开始记录的「首行」和「首列」信息，进行「首行首列」置零
        if (r0) for (int i = 0; i < m; i++) mat[i][0] = 0;
        if (c0) Arrays.fill(mat[0], 0);
    }

    //1267. 统计参与通信的服务器
    public int countServers(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] rows = new int[m];
        int[] cols = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    rows[i]++;
                    cols[j]++;
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1 && (rows[i] > 1 || cols[j] > 1)) {
                    ans++;
                }
            }
        }
        return ans;
    }

    //383. 赎金信
    public boolean canConstruct(String ransomNote, String magazine) {
        int[] cnt = new int[26];
        for (char c : magazine.toCharArray()) {
            cnt[c - 'a']++;
        }
        for (char c : ransomNote.toCharArray()) {
            cnt[c - 'a']--;
        }
        for (int i = 0; i < 26; i++) {
            if (cnt[i] < 0) return false;
        }
        return true;
    }

    //393. UTF-8 编码验证
    public boolean validUtf8(int[] data) {
        int n = data.length;
        for (int i = 0; i < n; ) {
            int t = data[i], j = 7;
            while (j >= 0 && (((t >> j) & 1) == 1)) j--;
            int cnt = 7 - j;
            if (cnt == 1 || cnt > 4) return false;
            if (i + cnt - 1 >= n) return false;
            for (int k = i + 1; k < i + cnt; k++) {
                if ((((data[k] >> 7) & 1) == 1) && (((data[k] >> 6) & 1) == 0)) continue;
                return false;
            }
            i += cnt == 0 ? 1 : cnt;
        }
        return true;
    }

    // 468 验证ip地址
    public String validIPAddress(String queryIP) {
        if (queryIP.contains(".") && validIPv4(queryIP)) return "IPv4";
        if (queryIP.contains(":") && validIPv6(queryIP)) return "IPv6";
        return "Neither";
    }

    private boolean validIPv4(String queryIP) {
        String[] tmp = ("-1." + queryIP + ".-1").split("\\.");
        if (tmp.length != 6) return false;
        String[] ss = new String[4];
        System.arraycopy(tmp, 1, ss, 0, 4);
        for (String s : ss) {
            if (s.length() < 1) return false;
            if (s.length() > 1 && s.charAt(0) == '0') return false;
            int num = 0;
            for (char c : s.toCharArray()) {
                if (c < '0' || c > '9') return false;
                num = num * 10 + c - '0';
            }
            if (num < 0 || num > 255) return false;
        }
        return true;
    }

    private boolean validIPv6(String queryIP) {
        String[] tmp = ("-1:" + queryIP + ":-1").split(":");
        if (tmp.length != 10) return false;
        String[] ss = new String[8];
        System.arraycopy(tmp, 1, ss, 0, 8);
        for (String s : ss) {
            if (s.length() > 4 || s.length() < 1) return false;
            for (char c : s.toCharArray()) {
                if (Character.isLetter(c)) {
                    if (!((c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))) return false;
                }
            }
        }
        return true;
    }

    //447. 回旋镖的数量
    public int numberOfBoomerangs(int[][] points) {
        int ans = 0;
        for (int[] p : points) {
            Map<Integer, Integer> cnt = new HashMap<Integer, Integer>();
            for (int[] q : points) {
                int dis = (p[0] - q[0]) * (p[0] - q[0]) + (p[1] - q[1]) * (p[1] - q[1]);
                cnt.put(dis, cnt.getOrDefault(dis, 0) + 1);
            }
            for (Map.Entry<Integer, Integer> entry : cnt.entrySet()) {
                int m = entry.getValue();
                ans += m * (m - 1);
            }
        }
        return ans;
    }

    //831. 隐藏个人信息
    public String maskPII(String s) {
        String[] country = {"", "+*-", "+**-", "+***-"};
        int at = s.indexOf("@");
        if (at > 0) {
            s = s.toLowerCase();
            return (s.charAt(0) + "*****" + s.substring(at - 1)).toLowerCase();
        }
        s = s.replaceAll("[^0-9]", "");
        return country[s.length() - 10] + "***-***-" + s.substring(s.length() - 4);
    }

    //970. 强整数 枚举
    public List<Integer> powerfulIntegers(int x, int y, int bound) {
        Set<Integer> set = new HashSet<Integer>();
        int value1 = 1;
        for (int i = 0; i < 21; i++) {
            int value2 = 1;
            for (int j = 0; j < 21; j++) {
                int value = value1 + value2;
                if (value <= bound) {
                    set.add(value);
                } else {
                    break;
                }
                value2 *= y;
            }
            if (value1 > bound) {
                break;
            }
            value1 *= x;
        }
        return new ArrayList<Integer>(set);
    }

    //1419. 数青蛙
    public int minNumberOfFrogs(String croakOfFrogs) {
        if (croakOfFrogs.length() % 5 != 0) {
            return -1;
        }
        int res = 0, frogNum = 0;
        int[] cnt = new int[4];
        Map<Character, Integer> map = new HashMap<Character, Integer>() {{
            put('c', 0);
            put('r', 1);
            put('o', 2);
            put('a', 3);
            put('k', 4);
        }};
        for (int i = 0; i < croakOfFrogs.length(); i++) {
            char c = croakOfFrogs.charAt(i);
            int t = map.get(c);
            if (t == 0) {
                cnt[t]++;
                frogNum++;
                if (frogNum > res) {
                    res = frogNum;
                }
            } else {
                if (cnt[t - 1] == 0) {
                    return -1;
                }
                cnt[t - 1]--;
                if (t == 4) {
                    frogNum--;
                } else {
                    cnt[t]++;
                }
            }
        }
        if (frogNum > 0) {
            return -1;
        }
        return res;
    }

    //1138. 字母板上的路径
    public String alphabetBoardPath(String target) {
        int cx = 0, cy = 0;
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < target.length(); i++) {
            char c = target.charAt(i);
            int nx = (c - 'a') / 5;
            int ny = (c - 'a') % 5;
            if (nx < cx) {
                for (int j = 0; j < cx - nx; j++) {
                    res.append('U');
                }
            }
            if (ny < cy) {
                for (int j = 0; j < cy - ny; j++) {
                    res.append('L');
                }
            }
            if (nx > cx) {
                for (int j = 0; j < nx - cx; j++) {
                    res.append('D');
                }
            }
            if (ny > cy) {
                for (int j = 0; j < ny - cy; j++) {
                    res.append('R');
                }
            }
            res.append('!');
            cx = nx;
            cy = ny;
        }
        return res.toString();
    }

    //1945. 字符串转化后的各位数字之和
    public int getLucky(String s, int k) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(c - 'a' + 1);
        }
        String digits = sb.toString();
        int ans = 0;
        while (k-- > 0) {
            ans = 0;
            for (char c : digits.toCharArray()) {
                ans += c - '0';
            }
            digits = String.valueOf(ans);

        }
        return ans;
    }

    //1781. 所有子字符串美丽值之和
    public int beautySum(String s) {
        int n = s.length();
        Map<Character, Integer> map = new HashMap<>();
        char[] chars = s.toCharArray();
        int max;
        final int[] min = new int[1];
        int ans = 0;
        for (int i = 0; i < n; i++) {
            map.clear();
            max = Integer.MIN_VALUE;
            for (int j = i; j < n; j++) {
                map.put(chars[j], map.getOrDefault(chars[j], 0) + 1);
                max = Math.max(map.get(chars[j]), max);
                min[0] = Integer.MAX_VALUE;
                map.forEach((k, v) -> min[0] = Math.min(min[0], v));
                ans += max - min[0];
            }
        }
        return ans;
    }

    //1832. 判断句子是否为全字母句
    public boolean checkIfPangram(String sentence) {
        int[] cnt = new int[26];
        int n = sentence.length();
        if (n < 26) return false;
        int count = 0;
        for (char c : sentence.toCharArray()) {
            if (cnt[c - 'a'] == 0) {
                cnt[c - 'a']++;
                count++;
            }
            if (count == 26) return true;
        }
        return false;
    }

    //722. 删除注释
    public List<String> removeComments(String[] source) {
        List<String> res = new ArrayList<String>();
        StringBuilder newLine = new StringBuilder();
        boolean inBlock = false;
        for (String line : source) {
            for (int i = 0; i < line.length(); i++) {
                if (inBlock) {
                    if (i + 1 < line.length() && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                        inBlock = false;
                        i++;
                    }
                } else {
                    if (i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                        inBlock = true;
                        i++;
                    } else if (i + 1 < line.length() && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                        break;
                    } else {
                        newLine.append(line.charAt(i));
                    }
                }
            }
            if (!inBlock && newLine.length() > 0) {
                res.add(newLine.toString());
                newLine.setLength(0);
            }
        }
        return res;
    }

    //2048. 下一个更大的数值平衡数
    public int nextBeautifulNumber(int n) {
        for (int i = n + 1; i <= 1224444; ++i) {
            if (isBalance(i)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isBalance(int x) {
        int[] count = new int[10];
        while (x > 0) {
            count[x % 10]++;
            x /= 10;
        }
        for (int d = 0; d < 10; ++d) {
            if (count[d] > 0 && count[d] != d) {
                return false;
            }
        }
        return true;
    }

    //2828. 判别首字母缩略词
    public boolean isAcronym(List<String> words, String s) {
        if (words.size() != s.length()) return false;
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).charAt(0) != s.charAt(i)) return false;
        }
        return true;
    }

    //2085. 统计出现过一次的公共字符串
    public int countWords(String[] words1, String[] words2) {
        Map<String, Integer> map = new HashMap<>();
        for (String s : words1) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }
        Set<String> removeKey = new HashSet<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 1) removeKey.add(entry.getKey());
        }
        for (String key : removeKey) {
            map.remove(key);
        }
        for (String s : words2) {
            if (map.containsKey(s)) {
                map.put(s, map.get(s) - 1);
            }
        }
        int ans = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 0) ans++;
        }
        return ans;
    }

    //2788. 按分隔符拆分字符串
    public List<String> splitWordsBySeparator(List<String> words, char separator) {
        List<String> res = new ArrayList<String>();
        for (String word : words) {
            StringBuilder sb = new StringBuilder();
            int length = word.length();
            for (int i = 0; i < length; i++) {
                char c = word.charAt(i);
                if (c == separator) {
                    if (sb.length() > 0) {
                        res.add(sb.toString());
                        sb.setLength(0);
                    }
                } else {
                    sb.append(c);
                }
            }
            if (sb.length() > 0) {
                res.add(sb.toString());
            }
        }
        return res;
    }
    //2103. 环和杆
    public int countPoints(String rings) {
        int n = rings.length() / 2;
        int cnt = 0;
        Map<Integer, Set<Character>> map = new HashMap<Integer, Set<Character>>();
        for (int i = 0; i < rings.length(); i += 2) {
            char color = rings.charAt(i);
            int num = rings.charAt(i + 1) - '0';
            Set<Character> set = map.getOrDefault(num, new HashSet<>());
            set.add(color);
            map.put(num, set);
        }
        for(Map.Entry<Integer,Set<Character>> entry :map.entrySet()){
            if(entry.getValue().size()==3) cnt++;
        }
        return cnt;
    }

    //1604. 警告一小时内使用相同员工卡大于等于三次的人
    public List<String> alertNames(String[] keyName, String[] keyTime) {
        Map<String, List<Integer>> map = new HashMap<>();
        int n = keyName.length;
        for (int i = 0; i < n; i++) {
            List<Integer> list = map.getOrDefault(keyName[i], new ArrayList<>());
            int hour = Integer.parseInt(keyTime[i].split(":")[0]);
            int minute = Integer.parseInt(keyTime[i].split(":")[1]);
            list.add(hour * 60 + minute);
            map.put(keyName[i], list);
        }
        List<String> names = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
            Collections.sort(entry.getValue());
            int size = entry.getValue().size();
            for (int i = 2; i < size; i++) {
                if (entry.getValue().get(i) - entry.getValue().get(i - 2) <= 60) {
                    names.add(entry.getKey());
                    break;
                }
            }
        }
        Collections.sort(names);
        return names;
    }

    //1844. 将所有数字用字符替换
    public String replaceDigits(String s) {
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ((i & 1) == 1) {
                chars[i] = (char) (chars[i - 1] + (chars[i] - '0'));
            }
        }
        return new String(chars);
    }

    //2303. 计算应缴税款总额
    public double calculateTax(int[][] brackets, int income) {
        double tax = 0;
        for (int i = 0; i < brackets.length; i++) {
            int base = (Math.min(income, brackets[i][0])) - (i > 0 ? brackets[i - 1][0] : 0);
            tax += base * brackets[i][1] / 100d;
            if (income <= brackets[i][0]) break;
        }
        return tax;
    }
    //771. 宝石与石头
    public int numJewelsInStones(String jewels, String stones) {
        int jewelsCount = 0;
        Set<Character> jewelsSet = new HashSet<Character>();
        int jewelsLength = jewels.length(), stonesLength = stones.length();
        for (int i = 0; i < jewelsLength; i++) {
            char jewel = jewels.charAt(i);
            jewelsSet.add(jewel);
        }
        for (int i = 0; i < stonesLength; i++) {
            char stone = stones.charAt(i);
            if (jewelsSet.contains(stone)) {
                jewelsCount++;
            }
        }
        return jewelsCount;
    }

    //1281. 整数的各位积和之差
    public int subtractProductAndSum(int n) {
        int product = 1;
        int sum = 0;
        while (n > 0) {
            int r = n % 10;
            product *= r;
            sum += r;
            n /= 10;
        }
        return product - sum;
    }
    //1572. 矩阵对角线元素的和
    public int diagonalSum(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int sum = 0;
        int l = 0, r = n - 1;
        for (int i = 0; i < m; i++) {
            if (l != r) {
                sum += mat[i][l];
                sum += mat[i][r];
            } else {
                sum += mat[i][l];
            }
            l++;
            r--;
        }
        return sum;
    }

    //2240. 买钢笔和铅笔的方案数
    public long waysToBuyPensPencils(int total, int cost1, int cost2) {
        if (cost1 < cost2) {
            return waysToBuyPensPencils(total, cost2, cost1);
        }
        long res = 0, cnt = 0;
        while (cnt * cost1 <= total) {
            res += (total - cnt * cost1) / cost2 + 1;
            cnt++;
        }
        return res;
    }


    //2325. 解密消息
    public String decodeMessage(String key, String message) {
        Map<Character, Integer> map = new HashMap<>();
        int idx = 0;
        for (char c : key.toCharArray()) {
            if (c == ' ') continue;
            if (!map.containsKey(c)) {
                map.put(c, idx++);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (c == ' ') {
                sb.append(" ");
                continue;
            }
            sb.append((char) (map.get(c) + 'a'));
        }
        return sb.toString();
    }

    //2319. 判断矩阵是否是一个 X 矩阵
    public boolean checkXMatrix(int[][] grid) {
        int n = grid.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j || i + j == n - 1) {
                    if (grid[i][j] == 0) return false;
                } else {
                    if (grid[i][j] != 0) return false;
                }
            }
        }
        return true;
    }

    //2315. 统计星号
    public int countAsterisks(String s) {
        int ans = 0;
        boolean valid = true;
        for (char c : s.toCharArray()) {
            if (c == '|') {
                valid = !valid;
            } else if (c == '*' && valid) {
                ans++;
            }
        }
        return ans;
    }

    //2309. 兼具大小写的最好英文字母
    public String greatestLetter(String s) {
        Set<Character> ht = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            ht.add(c);
        }
        for (int i = 25; i >= 0; i--) {
            if (ht.contains((char) ('a' + i)) && ht.contains((char) ('A' + i))) {
                return String.valueOf((char) ('A' + i));
            }
        }
        return "";
    }

    //2341. 数组能形成多少数对
    public int[] numberOfPairs(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        int[] ans = new int[2];
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            ans[0] += entry.getValue() / 2;
            ans[1] += entry.getValue() % 2;
        }
        return ans;
    }

    //2347. 最好的扑克手牌
    public String bestHand(int[] ranks, char[] suits) {
        if (check(suits)) return "Flush";
        int[] cnt = new int[14];
        for (int r : ranks) {
            cnt[r]++;
            if (cnt[r] == 3) return "Three of a Kind";
        }
        for (int i = 1; i <= 13; i++) {
            if (cnt[i] == 2) return "Pair";
        }
        return "High Card";
    }

    private boolean check(char[] suits) {
        for (char c : suits) {
            if (c != suits[0]) return false;
        }
        return true;
    }

    //2357. 使数组中所有元素都等于零
    public int minimumOperations2357(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (num != 0) set.add(num);
        }
        return set.size();
    }

    public int minimumOperations2(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int[] diff = new int[n];
        diff[0] = nums[0];
        for (int i = n - 1; i > 0; i--) {
            diff[i] = nums[i] - nums[i - 1];
        }
        int cnt = 0;
        for (int i : diff) {
            if (i > 0) cnt++;
        }
        return cnt;
    }

    //2399. 检查相同字母间的距离
    public boolean checkDistances(String s, int[] distance) {
        Map<Integer, int[]> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            int idx = s.charAt(i) - 'a';
            if (map.containsKey(idx)) {
                map.get(idx)[1] = i;
            } else {
                map.put(idx, new int[]{i, -1});
            }
        }
        for (Map.Entry<Integer, int[]> entry : map.entrySet()) {
            if ((entry.getValue()[1] - entry.getValue()[0] - 1) != distance[entry.getKey()]) return false;
        }
        return true;
    }

    //2404. 出现最频繁的偶数元素
    public int mostFrequentEven(int[] nums) {
        TreeMap<Integer, Integer> cnt = new TreeMap<>();
        int max = 0;
        for (int num : nums) {
            if ((num & 1) == 0) {
                cnt.put(num, cnt.getOrDefault(num, 0) + 1);
                max = Math.max(cnt.get(num), max);
            }
        }
        for (Map.Entry<Integer, Integer> entry : cnt.entrySet()) {
            if (entry.getValue() == max) return entry.getKey();
        }
        return -1;
    }

    //2475. 数组中不等三元组的数目
    public int unequalTriplets(int[] nums) {
        int n = nums.length;
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        int ans = 0, a = 0;
        for (int b : map.values()) {
            int c = n - a - b;
            ans += a * b * c;
            a += b;
        }
        return ans;
    }

    //2481. 分割圆的最少切割次数
    public int numberOfCuts(int n) {
        if (n == 1) {
            return 0;
        }
        if (n % 2 == 0) {
            return n / 2;
        }
        return n;
    }

    //2496. 数组中字符串的最大值
    public int maximumValue(String[] strs) {
        int max = 0;
        for (String s : strs) {
            int num = 0;
            for (char c : s.toCharArray()) {
                if (c >= '0' && c <= '9') {
                    num = num * 10 + c - '0';
                } else {
                    num = s.length();
                    break;
                }
            }
            max = Math.max(max, num);
        }
        return max;
    }

    //2413. 最小偶倍数
    public int smallestEvenMultiple(int n) {
        if (n % 2 == 0) return n;
        return 2 * n / gcd(n, 2);
    }

    //2520. 统计能整除数字的位数
    public int countDigits(int num) {
        int c = num;
        int cnt = 0;
        while (c > 0) {
            int d = c % 10;
            if (num % d == 0) cnt++;
            c /= 10;
        }
        return cnt;
    }

    public int countDigits2(int num) {
        char[] chars = String.valueOf(num).toCharArray();
        int res = 0;
        for(char c:chars){
            if(num%(c-'0')==0) res++;
        }
        return res;
    }

    //2525. 根据规则将箱子分类
    public String categorizeBox(int length, int width, int height, int mass) {
        long maxd = Math.max(length, Math.max(width, height)), vol = 1L * length * width * height;
        boolean isBulky = maxd >= 10000 || vol >= 1000000000, isHeavy = mass >= 100;
        if (isBulky && isHeavy) {
            return "Both";
        } else if (isBulky) {
            return "Bulky";
        } else if (isHeavy) {
            return "Heavy";
        } else {
            return "Neither";
        }
    }

    //2562. 找出数组的串联值
    public long findTheArrayConcVal(int[] nums) {
        long sum = 0;
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            if (l < r) {
                sum += getSum2562(nums[l++], nums[r--]);
            } else {
                sum += nums[l++];
            }
        }
        return sum;
    }

    private long getSum2562(int l, int r) {
        int digit = String.valueOf(r).length();
        return (long) Math.pow(10, digit) * l + r;
    }

    //2639查询网格图中每一列的宽度
    public int[] findColumnWidth(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] ans = new int[n];
        for (int j = 0; j < n; j++) {
            int max = 0;
            for (int i = 0; i < m; i++) {
                max = Math.max(max, cntDigit(grid[i][j]));
            }
            ans[j] = max;
        }
        return ans;
    }

    private int cntDigit(int x) {
        if (x == 0) return 1;
        int len = x < 0 ? 1 : 0;
        x = Math.abs(x);
        while (x != 0) {
            len++;
            x /= 10;
        }
        return len;
    }

    //2644. 找出可整除性得分最大的整数
    public int maxDivScore(int[] nums, int[] divisors) {
        int ans = 0, maxCnt = -1;
        for (int i = 0; i < divisors.length; i++) {
            int cnt = 0;
            for (int num : nums) {
                cnt += (num % divisors[i] == 0 ? 1 : 0);
            }
            if (cnt > maxCnt) {
                maxCnt = cnt;
                ans = divisors[i];
            } else if (cnt == maxCnt && divisors[i] < ans) {
                ans = divisors[i];
            }
        }
        return ans;
    }

    //2643. 一最多的行
    public int[] rowAndMaximumOnes(int[][] mat) {
        int[] ans = new int[2];
        for (int i = 0; i < mat.length; i++) {
            int sum = 0;
            for (int j = 0; j < mat[i].length; j++) {
                sum += mat[i][j];
            }
            if (sum > ans[1]) {
                ans[0] = i;
                ans[1] = sum;
            }
        }
        return ans;
    }

    //2651. 计算列车到站时间
    public int findDelayedArrivalTime(int arrivalTime, int delayedTime) {
        return (arrivalTime + delayedTime) % 24;
    }

    //2652. 倍数求和
    public int sumOfMultiples(int n) {
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            if (i % 3 == 0 || i % 5 == 0 || i % 7 == 0) {
                ans += i;
            }
        }
        return ans;
    }

    //2733. 既不是最小值也不是最大值
    public int findNonMinOrMax(int[] nums) {
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int num : nums) {
            max = Math.max(max, num);
            min = Math.min(min, num);
        }
        for (int num : nums) {
            if (num != max && num != min) return num;
        }
        return -1;
    }

    //2729. 判断一个数是否迷人
    public boolean isFascinating(int n) {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append((long) 2 * n).append((long) 3 * n);
        int[] cnt = new int[10];
        for (char c : sb.toString().toCharArray()) {
            cnt[c - '0']++;
            if (cnt[0] > 0 || cnt[c - '0'] > 1) return false;
        }
        return true;
    }

    //2739. 总行驶距离
    public int distanceTraveled(int mainTank, int additionalTank) {
        int cost = 0;
        while (mainTank >= 5) {
            cost += 5;
            mainTank -= 5;
            if (additionalTank > 0) {
                additionalTank--;
                mainTank++;
            }
        }
        cost += mainTank;
        return cost * 10;
    }

    //2740. 找出分区值
    public int findValueOfPartition(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        int min = Integer.MAX_VALUE;
        for (int i = 1; i < n; i++) {
            min = Math.min(min, nums[i] - nums[i - 1]);
        }
        return min;
    }

    //2744. 最大字符串配对数目
    public int maximumNumberOfStringPairs(String[] words) {
        int n = words.length;
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (check(words[i], words[j])) cnt++;
            }
        }
        return cnt;
    }

    private boolean check(String w1, String w2) {
        if (w1.length() != w2.length()) return false;
        int l = 0, r = w1.length() - 1;
        while (l < w1.length()) {
            if (w1.charAt(l++) != w2.charAt(r--)) return false;
        }
        return true;
    }

    public int maximumNumberOfStringPairs2(String[] words) {
        int n = words.length;
        int ans = 0;
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < n; ++i) {
            if (seen.contains(words[i].charAt(1) * 100 + words[i].charAt(0))) {
                ++ans;
            }
            seen.add(words[i].charAt(0) * 100 + words[i].charAt(1));
        }
        return ans;
    }

    //2352. 相等行列对
    public int equalPairs(int[][] grid) {
        int n = grid.length;
        Map<String, Integer> map = new HashMap<>();
        for (int[] row : grid) {
            StringBuilder sb = new StringBuilder();
            for (int num : row) {
                sb.append(num).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            String key = sb.toString();
            map.put(key, map.getOrDefault(key, 0) + 1);
        }
        int ans = 0;
        for (int j = 0; j < n; j++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                sb.append(grid[i][j]).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            String key = sb.toString();
            if (map.containsKey(key)) {
                ans += map.get(key);
            }
        }
        return ans;
    }

    //2460. 对数组执行操作
    public int[] applyOperations(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                nums[i] <<= 1;
                nums[i + 1] = 0;
            }
        }
        int slow = 0, fast = 0;
        while (fast < n) {
            while (slow <= fast && nums[slow] != 0) {
                slow++;
            }
            if (nums[fast] > 0 && slow < fast) {
                swap(nums, slow, fast);
            }
            fast++;
        }
        return nums;
    }

    public void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    //2511 最多可以摧毁的敌人城堡数目
    public int captureForts(int[] forts) {
        int n = forts.length;
        int ans = 0, pre = -1;
        for (int i = 0; i < n; i++) {
            if (forts[i] == 1 || forts[i] == -1) {
                if (pre >= 0 && forts[i] != forts[pre]) {
                    ans = Math.max(ans, i - pre - 1);
                }
                pre = i;
            }
        }
        return ans;
    }

    //2706. 购买两块巧克力
    public int buyChoco(int[] prices, int money) {
        int first = 101, second = 101;
        for (int price : prices) {
            if (price < first) {
                second = first;
                first = price;
            } else if (price < second) {
                second = price;
            }
        }
        int left = money - first - second;
        if (left < 0) return money;
        return left;
    }

    //2710. 移除字符串中的尾随零
    public String removeTrailingZeros(String num) {
        int ptr = num.length() - 1;
        while (ptr >= 0 && num.charAt(ptr) == '0') {
            ptr--;
        }
        return num.substring(0, ptr + 1);
    }

    //2711. 对角线上不同值的数量差
    public int[][] differenceOfDistinctValues(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] ans = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int x = i, y = j;
                Set<Integer> topLeftSet = new HashSet<>();
                Set<Integer> bottomRightSet = new HashSet<>();
                while (x - 1 >= 0 && y - 1 >= 0) {
                    topLeftSet.add(grid[--x][--y]);
                }
                x = i;
                y = j;
                while (x + 1 < m && y + 1 < n) {
                    bottomRightSet.add(grid[++x][++y]);
                }
                ans[i][j] = Math.abs(topLeftSet.size() - bottomRightSet.size());
            }
        }
        return ans;
    }

    //2716. 最小化字符串长度
    public int minimizedStringLength(String s) {
        Set<Character> set = new HashSet<>();
        for (char c : s.toCharArray()) {
            set.add(c);
        }
        return set.size();
    }

    //2717. 半有序排列
    public int semiOrderedPermutation(int[] nums) {
        int n = nums.length;
        int l = 0, r = n - 1;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) l = i;
            if (nums[i] == n) r = i;
        }
        int left = l, right = n - 1 - r;
        return l > r ? left + right - 1 : left + right;
    }

    //LCP 50. 宝石补给
    public int giveGem(int[] gem, int[][] operations) {
        for(int[] op:operations){
            int diff = gem[op[0]]/2;
            gem[op[1]]+=diff;
            gem[op[0]]-=diff;
        }
        int max=0,min=Integer.MAX_VALUE;
        for(int i=0;i<gem.length;i++){
            max = Math.max(max,gem[i]);
            min = Math.min(min,gem[i]);
        }
        return max-min;
    }

    //2409. 统计共同度过的日子数
    public int countDaysTogether(String arriveAlice, String leaveAlice, String arriveBob, String leaveBob) {
        int[] months = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int[] sum = new int[13];
        for (int i = 1; i <= 12; i++) {
            sum[i] = sum[i - 1] + months[i - 1];
        }
        int as = convertDate(sum, arriveAlice), ae = convertDate(sum, leaveAlice);
        int bs = convertDate(sum, arriveBob), be = convertDate(sum, leaveBob);
        if (as > be || bs > ae) return 0;
        return Math.min(Math.min(ae - as + 1, be - bs + 1), Math.min(ae - bs + 1, be - as + 1));
    }

    private int convertDate(int[] sum, String date) {
        int month = Integer.parseInt(date.split("-")[0]);
        int day = Integer.parseInt(date.split("-")[1]);
        return sum[month - 1] + day;
    }

    //2614. 对角线上的质数
    public int diagonalPrime(int[][] nums) {
        int max = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (isPrime(nums[i][i])) {
                max = Math.max(max, nums[i][i]);
            }
            if (isPrime(nums[i][n - 1 - i])) {
                max = Math.max(max, nums[i][n - 1 - i]);

            }
        }
        return max;
    }

    private boolean isPrime(int n) {
        if (n == 1) return false;
        // n/i 当i大于sqrt(n)时另一个因子肯定小于sqrt(n),所以只遍历到sqrt(n)
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    //2656. K 个元素的最大和
    public int maximizeSum(int[] nums, int k) {
        int max = 0;
        for (int num : nums) {
            max = Math.max(max, num);
        }
        int sum = 0;
        while (k-- > 0) {
            sum += max;
            max++;
        }
        return sum;
    }

    //2657. 找到两个数组的前缀公共数组
    public int[] findThePrefixCommonArray(int[] A, int[] B) {
        int n = A.length;
        int[] cntA = new int[n + 1];
        int[] cntB = new int[n + 1];
        int[] C = new int[n];
        for (int i = 0; i < n; i++) {
            cntA[A[i]]++;
            cntB[B[i]]++;
            for (int j = 0; j <= n; j++) {
                C[i] += (cntA[j] > 0 && cntB[j] > 0) ? 1 : 0;
            }
        }
        return C;
    }

    //2660. 保龄球游戏的获胜者
    public int isWinner(int[] player1, int[] player2) {
        int value1 = value(player1);
        int value2 = value(player2);
        if (value1 == value2) return 0;
        return value1 > value2 ? 1 : 2;
    }

    private int value(int[] player) {
        if (player.length == 0) return 0;
        if (player.length == 1) return player[0];
        int pp = -1, p = player[0];
        int ans = player[0];
        for (int i = 1; i < player.length; i++) {
            if (p == 10 || pp == 10) {
                ans += 2 * player[i];
            } else {
                ans += player[i];
            }
            pp = p;
            p = player[i];
        }
        return ans;
    }

    //2661. 找出叠涂元素
    public int firstCompleteIndex(int[] arr, int[][] mat) {
        int m = mat.length, n = mat[0].length;
        Map<Integer, int[]> map = new HashMap<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                map.put(mat[i][j], new int[]{i, j});
            }
        }
        int[] row = new int[m];
        int[] col = new int[n];
        for (int i = 0; i < arr.length; i++) {
            int x = map.get(arr[i])[0], y = map.get(arr[i])[1];
            row[x]++;
            col[y]++;
            if (row[x] == n || col[y] == m) return i;
        }
        return -1;
    }

    //2670. 找出不同元素数目差数组
    public int[] distinctDifferenceArray(int[] nums) {
        Map<Integer, Integer> preCnt = new HashMap<>();
        Map<Integer, Integer> sufCnt = new HashMap<>();
        for (int num : nums) {
            sufCnt.put(num, sufCnt.getOrDefault(num, 0) + 1);
        }
        int[] diff = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            preCnt.put(nums[i], preCnt.getOrDefault(nums[i], 0) + 1);
            sufCnt.put(nums[i], sufCnt.get(nums[i]) - 1);
            if (sufCnt.get(nums[i]) == 0) {
                sufCnt.remove(nums[i]);
            }
            diff[i] = preCnt.size() - sufCnt.size();
        }
        return diff;
    }

    //2672. 有相同颜色的相邻元素数目
    public int[] colorTheArray(int n, int[][] queries) {
        int[] arr = new int[n];
        int[] ans = new int[queries.length];
        int cnt = 0;
        for (int i = 0; i < queries.length; i++) {
            int idx = queries[i][0];
            int color = queries[i][1];
            if (arr[idx] > 0) {
                if (idx > 0 && arr[idx] == arr[idx - 1]) cnt--;
                if (idx < n - 1 && arr[idx] == arr[idx + 1]) cnt--;
            }
            arr[idx] = color;
            if (idx > 0 && arr[idx] == arr[idx - 1]) cnt++;
            if (idx < n - 1 && arr[idx] == arr[idx + 1]) cnt++;
            ans[i] = cnt;
        }
        return ans;
    }

    //2185. 统计包含给定前缀的字符串
    public int prefixCount(String[] words, String pref) {
        int cnt = 0;
        for (String word : words) {
            if (isPrefix(word, pref)) cnt++;
        }
        return cnt;
    }

    private boolean isPrefix(String word, String pref) {
        if (pref.length() > word.length()) return false;
        for (int i = 0; i < pref.length(); i++) {
            if (word.charAt(i) != pref.charAt(i)) return false;
        }
        return true;
    }

    //2283. 判断一个数的数字计数是否等于数位的值
    public boolean digitCount(String num) {
        int n = num.length();
        Map<Integer, Integer> cnt = new HashMap<>();
        for (char c : num.toCharArray()) {
            cnt.put(c - '0', cnt.getOrDefault(c - '0', 0) + 1);
        }
        for (int i = 0; i < n; i++) {
            if (cnt.getOrDefault(i, 0) != num.charAt(i) - '0') return false;
        }
        return true;
    }

    //2287. 重排字符形成目标字符串
    public int rearrangeCharacters(String s, String target) {
        int[] cnt = new int[26];
        for (char c : target.toCharArray()) {
            cnt[c - 'a']++;
        }
        int[] cnt2 = new int[26];
        for (char c : s.toCharArray()) {
            cnt2[c - 'a']++;
        }
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < 26; i++) {
            if (cnt[i] > 0) ans = Math.min(ans, cnt2[i] / cnt[i]);
        }
        return ans;
    }

    //2529. 正整数和负整数的最大计数
    public int maximumCount(int[] nums) {
        int ct1 = 0, ct2 = 0;
        for (int num : nums) {
            if (num > 0) ct1++;
            if (num < 0) ct2++;
        }
        return Math.max(ct1, ct2);
    }

    //2531. 使字符串总不同字符的数目相等
    public boolean isItPossible(String word1, String word2) {
        int[] cnt1 = new int[26];
        int[] cnt2 = new int[26];
        int diff1 = 0, diff2 = 0;
        for (char c : word1.toCharArray()) {
            if (cnt1[c - 'a'] == 0) diff1++;
            cnt1[c - 'a']++;
        }
        for (char c : word2.toCharArray()) {
            if (cnt2[c - 'a'] == 0) diff2++;
            cnt2[c - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                cnt1[i]--;
                cnt1[j]++;
                cnt2[i]++;
                cnt2[j]--;
                int d1 = diff1 + (cnt1[i] == 0 ? -1 : 0) + (cnt1[j] == 1 ? 1 : 0);
                int d2 = diff2 + (cnt2[i] == 1 ? 1 : 0) + (cnt2[j] == 0 ? -1 : 0);
                if (d1 == d2) return true;
                cnt1[i]++;
                cnt1[j]--;
                cnt2[i]--;
                cnt2[j]++;
            }
        }
        return false;
    }

    //2293. 极大极小游戏
    public int minMaxGame(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];
        int len = n / 2;
        int[] newNums = new int[len];
        for (int i = 0; i < len; i++) {
            if (i % 2 == 0) {
                newNums[i] = Math.min(nums[2 * i], nums[2 * i + 1]);
            } else {
                newNums[i] = Math.max(nums[2 * i], nums[2 * i + 1]);
            }
        }
        return minMaxGame(newNums);
    }


    //2446. 判断两个事件是否存在冲突
    public boolean haveConflict(String[] event1, String[] event2) {
        int[] event1int = convert(event1);
        int[] event2int = convert(event2);
        return event1int[0] <= event2int[1] && event1int[1] >= event2int[0];
    }

    private int[] convert(String[] event) {
        int startHour = Integer.parseInt(event[0].substring(0, 2));
        int startMinute = Integer.parseInt(event[0].substring(3, 5));
        int endHour = Integer.parseInt(event[1].substring(0, 2));
        int endMinute = Integer.parseInt(event[1].substring(3, 5));
        return new int[]{startHour * 60 + startMinute, endHour * 60 + endMinute};
    }

    //2451. 差值数组不同的字符串
    public String oddString(String[] words) {
        Map<String, Integer> cnt = new HashMap<>();
        Map<String, String> wordMap = new HashMap<>();
        for (String word : words) {
            int n = word.length();
            StringBuilder key = new StringBuilder();
            for (int i = 1; i < n; i++) {
                key.append(word.charAt(i) - word.charAt(i - 1)).append(",");
            }
            cnt.put(key.toString(), cnt.getOrDefault(key.toString(), 0) + 1);
            wordMap.put(key.toString(), word);
        }
        for (Map.Entry<String, Integer> entry : cnt.entrySet()) {
            if (entry.getValue() == 1) return wordMap.get(entry.getKey());
        }
        return "";
    }

    //2455. 可被三整除的偶数的平均值
    public int averageValue(int[] nums) {
        int sum = 0, cnt = 0;
        for (int num : nums) {
            if (num % 6 == 0) {
                sum += num;
                cnt++;
            }
        }
        return cnt == 0 ? 0 : sum / cnt;
    }

    //2465. 不同的平均值数目
    public int distinctAverages(int[] nums) {
        Arrays.sort(nums);
        Set<Integer> set = new HashSet<>();
        for (int l = 0, r = nums.length - 1; l < r; l++, r--) {
            set.add(nums[l] + nums[r]);
        }
        return set.size();
    }

    //2682. 找出转圈游戏输家
    public int[] circularGameLosers(int n, int k) {
        Set<Integer> set = new HashSet<>();
        int idx = 0;
        int i = 0;
        while (true) {
            int next = (idx + i * k % n) % n;
            int no = next + 1;
            if (set.contains(no)) break;
            set.add(no);
            idx = next;
            i++;
        }
        int[] ans = new int[n - set.size()];
        int ii = 0;
        for (int j = 1; j <= n; j++) {
            if (set.contains(j)) continue;
            ans[ii++] = j;
        }
        return ans;
    }
    public int[] circularGameLosers2(int n, int k) {
        Set<Integer> set = new HashSet<>();
        int idx = 1;
        int round = 1;
        while (!set.contains(idx)) {
            set.add(idx);
            idx = (idx - 1 + round * k) % n + 1;
            round++;
        }
        int[] res = new int[n - set.size()];
        int ii = 0;
        for (int i = 1; i <= n; i++) {
            if (set.contains(i)) continue;
            res[ii++] = i;
        }
        return res;
    }

    //2544. 交替数字和
    public int alternateDigitSum(int n) {
        int size = String.valueOf(n).length();
        boolean flag = size % 2 != 0;
        int ans = 0;
        while (n != 0) {
            ans += (flag ? 1 : -1) * n % 10;
            flag = !flag;
            n /= 10;
        }
        return ans;
    }

    //2570. 合并两个二维数组 - 求和法
    public int[][] mergeArrays(int[][] nums1, int[][] nums2) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int[] num : nums1) {
            map.put(num[0], map.getOrDefault(num[0], 0) + num[1]);
        }
        for (int[] num : nums2) {
            map.put(num[0], map.getOrDefault(num[0], 0) + num[1]);
        }
        int[][] ans = new int[map.size()][2];
        int idx = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            ans[idx++] = new int[]{entry.getKey(), entry.getValue()};
        }
        Arrays.sort(ans, Comparator.comparingInt(o -> o[0]));
        return ans;
    }

    //2586. 统计范围内的元音字符串数
    public int vowelStrings(String[] words, int left, int right) {
        Set<Character> set = new HashSet<>();
        set.add('a');
        set.add('e');
        set.add('i');
        set.add('o');
        set.add('u');
        int ans = 0;
        for (int i = left; i <= right; i++) {
            if (set.contains(words[i].charAt(0)) && set.contains(words[i].charAt(words[i].length() - 1))) {
                ans++;
            }
        }
        return ans;

    }

    //2678. 老人的数目
    public int countSeniors(String[] details) {
        int cnt = 0;
        for (String detail : details) {
            Integer age = Integer.valueOf(detail.substring(11, 13));
            if (age > 60) {
                cnt++;
            }
        }
        return cnt;
    }

    //1072. 按列翻转得到最大值等行数
    public int maxEqualRowsAfterFlips(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        //求等价行的最大数量[1,0,0,1]=>[1,0,0,1]或[0,1,1,0]
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < m; i++) {
            char[] arr = new char[n];
            Arrays.fill(arr, '0');
            for (int j = 0; j < n; j++) {
                // 如果 matrix[i][0] 为 1，则对该行元素进行翻转
                arr[j] = (char) ('0' + (matrix[i][j] ^ matrix[i][0]));
            }
            String s = new String(arr);
            map.put(s, map.getOrDefault(s, 0) + 1);
        }
        int res = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            res = Math.max(res, entry.getValue());
        }
        return res;
    }

    // 1742 盒子中小球的最大数量
    public int countBalls(int lowLimit, int highLimit) {
        Map<Integer, Integer> map = new HashMap<>();
        int max = 0;
        for (int i = lowLimit; i <= highLimit; i++) {
            int digitSum = getDigitSum(i);
            int cnt = map.getOrDefault(digitSum, 0);
            cnt++;
            max = Math.max(cnt, max);
            map.put(digitSum, cnt);
        }
        return max;
    }

    private int getDigitSum(int num) {
        int sum = 0;
        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }

    //2535. 数组元素和与数字和的绝对差
    public int differenceOfSum(int[] nums) {
        int sum1 = 0, sum2 = 0;
        for (int num : nums) {
            sum1 += num;
            sum2 += digitSum(num);
        }
        return Math.abs(sum1 - sum2);
    }

    private int digitSum(int x) {
        int sum = 0;
        while (x != 0) {
            sum += x % 10;
            x /= 10;
        }
        return sum;
    }

    // 1260 二维网格迁移
    public List<List<Integer>> shiftGrid(int[][] grid, int k) {
        int m = grid.length, n = grid[0].length;
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            List<Integer> rows = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                rows.add(0);
            }
            result.add(rows);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int index = (i * n + j + k) % (m * n);
                result.get(index / n).set(index % n, grid[i][j]);
            }
        }
        return result;
    }

    // 2028. 找出缺失的观测数据
    public int[] missingRolls(int[] rolls, int mean, int n) {
        int m = rolls.length;
        int total = mean * (m + n);
        int sum = 0;
        for (int num : rolls) {
            sum += num;
        }
        int diff = total - sum;
        if (diff < n || diff > n * 6) return new int[0];
        int avg = diff / n, r = diff % n;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = avg + (i < r ? 1 : 0);
        }
        return ans;
    }

    //1805. 字符串中不同整数的数目
    public int numDifferentIntegers(String word) {
        Set<String> set = new HashSet<>();
        int idx = 0, n = word.length();
        char[] chars = word.toCharArray();
        while (idx < n) {
            if (Character.isLetter(chars[idx])) {
                idx++;
                continue;
            }
            StringBuilder sb = new StringBuilder();
            while (idx < n && Character.isDigit(chars[idx])) {
                sb.append(chars[idx++]);
            }
            String s = sb.toString();
            int i = 0;
            while (i < s.length() && s.charAt(i) == '0') {
                i++;
            }
            set.add(s.substring(i));
        }
        return set.size();
    }

    //1806. 还原排列的最少操作步数
    public int reinitializePermutation(int n) {
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = i;
        }
        int[] arr = perm.clone();
        int cnt = 0;
        do {
            int[] tmp = arr.clone();
            for (int i = 0; i < n; i++) {
                if (i % 2 == 0) arr[i] = tmp[i / 2];
                else arr[i] = tmp[n / 2 + (i - 1) / 2];
            }
            cnt++;
        } while (!check(arr, perm));
        return cnt;
    }

    private boolean check(int[] array1, int[] array2) {
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) return false;
        }
        return true;
    }

    public int reinitializePermutationMath(int n) {
        int i = 1, step = 1;
        while (true) {
            i = i % 2 == 0 ? i / 2 : (n - 1 + i) / 2;
            if (i == 1) return step;
            step++;
        }
    }


    //1582. 二进制矩阵中的特殊位置
    public int numSpecial(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[] rows = new int[m];
        int[] cols = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                rows[i] += mat[i][j];
                cols[j] += mat[i][j];
            }
        }
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 1 && rows[i] == 1 && cols[j] == 1) {
                    ans++;
                }
            }
        }
        return ans;
    }

    //1592. 重新排列单词间的空格
    public String reorderSpaces(String text) {
        int n = text.length();
        int l = -1, r = 0;
        List<String> list = new ArrayList<>();
        int cnt = 0, wordSize = 0;
        while (r < n) {
            while (r < n && text.charAt(r) == ' ') {
                r++;
            }
            l = r;
            while (r < n && text.charAt(r) != ' ') {
                r++;
            }
            if (r != l) {
                cnt++;
                wordSize += r - l;
                list.add(text.substring(l, r));
            }
        }
        int num = cnt > 1 ? ((n - wordSize) / (cnt - 1)) : 0;
        int suffix = cnt > 1 ? ((n - wordSize) % (cnt - 1)) : n - wordSize;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            sb.append(list.get(i));
            if (i != cnt - 1) {
                for (int j = 0; j < num; j++) {
                    sb.append(" ");
                }
            }

        }
        for (int i = 0; i < suffix; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    // 1620 网络信号最好的坐标 枚举
    public int[] bestCoordinate(int[][] towers, int radius) {
        int xMax = Integer.MIN_VALUE, yMax = Integer.MIN_VALUE;
        for (int[] tower : towers) {
            int x = tower[0], y = tower[1];
            xMax = Math.max(xMax, x);
            yMax = Math.max(yMax, y);
        }
        int cx = 0, cy = 0;
        int maxQuality = 0;
        for (int x = 0; x <= xMax; x++) {
            for (int y = 0; y <= yMax; y++) {
                int[] coordinate = {x, y};
                int quality = 0;
                for (int[] tower : towers) {
                    int squaredDistance = getSquaredDistance(coordinate, tower);
                    if (squaredDistance <= radius * radius) {
                        double distance = Math.sqrt(squaredDistance);
                        quality += (int) Math.floor(tower[2] / (1 + distance));
                    }
                }
                if (quality > maxQuality) {
                    cx = x;
                    cy = y;
                    maxQuality = quality;
                }
            }
        }
        return new int[]{cx, cy};
    }

    public int getSquaredDistance(int[] coordinate, int[] tower) {
        return (tower[0] - coordinate[0]) * (tower[0] - coordinate[0]) + (tower[1] - coordinate[1]) * (tower[1] - coordinate[1]);
    }

    // 1640 能否连接形成数组
    public boolean canFormArray(int[] arr, int[][] pieces) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < pieces.length; i++) {
            map.put(pieces[i][0], i);
        }
        for (int i = 0; i < arr.length; i++) {
            if (!map.containsKey(arr[i])) {
                return false;
            }
            int[] piece = pieces[map.get(arr[i])];
            int j = i + 1, k = 1;
            while (j < arr.length && k < piece.length) {
                if (arr[j] == piece[k]) {
                    j++;
                    k++;
                } else {
                    return false;
                }
            }
            i = j - 1;
        }
        return true;
    }

    //1694. 重新格式化电话号码
    public String reformatNumber(String number) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (char c : number.toCharArray()) {
            if (c == ' ' || c == '-') continue;

            sb.append(c);

            if (++count % 3 == 0) {
                sb.append('-');
            }
        }

        if (count % 3 == 1) {
            sb.deleteCharAt(sb.length() - 2);
            sb.insert(sb.length() - 2, '-');
        }

        if (sb.charAt(sb.length() - 1) == '-') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    // 1700 无法吃午餐的学生数量
    public int countStudents(int[] students, int[] sandwiches) {
        int n = students.length;
        int idx1 = 0, idx2 = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        while (idx1 < n) {
            if (students[idx1] == sandwiches[idx2]) {
                idx1++;
                idx2++;
            } else {
                queue.offer(students[idx1++]);
            }
        }
        int cnt = queue.size();
        while (idx2 < n && queue.size() > 0 && cnt > 0) {
            if (queue.peek() == sandwiches[idx2]) {
                idx2++;
                queue.poll();
                cnt = queue.size();
            } else {
                cnt--;
                queue.offer(queue.poll());
            }
        }
        return queue.size();
    }

    // 学生顺序无关
    public int countStudents2(int[] students, int[] sandwiches) {
        int s1 = Arrays.stream(students).sum();
        int s0 = students.length - s1;
        for (int sandwich : sandwiches) {
            if (sandwich == 0 && s0 > 0) {
                s0--;
            } else if (sandwich == 1 && s1 > 0) {
                s1--;
            } else {
                break;
            }
        }
        return s0 + s1;
    }

    //1812. 判断国际象棋棋盘中一个格子的颜色
    public boolean squareIsWhite(String coordinates) {
        int col = coordinates.charAt(0) - 'a';
        int row = coordinates.charAt(1) - '1';
        return ((col & 1) == 1 && (row & 1) == 0) || ((col & 1) == 0 && (row & 1) == 1);
    }

    //1814. 统计一个数组中好对子的数目
    public int countNicePairs(int[] nums) {
        final int MOD = 1000000007;
        int res = 0;
        Map<Integer, Integer> h = new HashMap<Integer, Integer>();
        for (int i : nums) {
            int temp = i, j = 0;
            while (temp > 0) {
                j = j * 10 + temp % 10;
                temp /= 10;
            }
            res = (res + h.getOrDefault(i - j, 0)) % MOD;
            h.put(i - j, h.getOrDefault(i - j, 0) + 1);
        }
        return res;
    }

    //1817. 查找用户活跃分钟数
    public int[] findingUsersActiveMinutes(int[][] logs, int k) {
        int[] ans = new int[k];
        Map<Integer, Set<Integer>> cnt = new HashMap<>();
        for (int[] log : logs) {
            Set<Integer> set = cnt.getOrDefault(log[0], new HashSet<>());
            set.add(log[1]);
            cnt.put(log[0], set);
        }
        for (Map.Entry<Integer, Set<Integer>> entry : cnt.entrySet()) {
            ans[entry.getValue().size() - 1]++;
        }
        return ans;
    }

    //2506. 统计相似字符串对的数目
    // 两两匹配
    public int similarPairs(String[] words) {
        boolean[][] w = new boolean[words.length][26];
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words[i].length(); j++) {
                w[i][words[i].charAt(j) - 'a'] = true;
            }
        }
        int res = 0;
        for (int i = 0; i < w.length - 1; i++) {
            for (int j = i + 1; j < w.length; j++) {
                boolean flag = true;
                for (int k = 0; k < 26; k++) {
                    if (w[i][k] != w[j][k]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    res++;
                }
            }
        }
        return res;
    }

    //面试题 16.18. 模式匹配
    //输入： pattern = "abba", value = "dogcatcatdog"
    //输出： true
    public boolean patternMatching(String pattern, String value) {
        int m = value.length(), n = pattern.length();
        // pattern 为空,只能匹配value为空
        if (n == 0) return m == 0;
        int[] cnt = new int[2];
        for (char c : pattern.toCharArray()) {
            cnt[c - 'a']++;
        }
        // value为空,pattern只能有1个字符组成
        if (m == 0) return cnt[0] == 0 || cnt[1] == 0;
        // value不为空
        // 1.pattern只有a或者b
        if (cnt[0] == 0) return helper(value, cnt[1]);
        if (cnt[1] == 0) return helper(value, cnt[0]);
        // a,b都有
        // 2.a或b其中一个匹配空
        if (helper(value, cnt[0])) return true;
        if (helper(value, cnt[1])) return true;
        // 3.a,b都不匹配空, 枚举a, b匹配的长度，使得a * len_a + b * len_b = m; len_a唯一确定len_b，只需枚举len_a
        for (int len_a = 1; len_a * cnt[0] <= m - cnt[1]; len_a++) {
            if ((m - len_a * cnt[0]) % cnt[1] != 0) continue;
            int len_b = (m - len_a * cnt[0]) / cnt[1];
            if (check(pattern, value, len_a, len_b)) return true;
        }
        return false;
    }

    private boolean check(String pattern, String value, int len_a, int len_b) {
        Map<Character, String> dict = new HashMap<>();
        for (int i = 0, j = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == 'a') {
                String word_a = value.substring(j, j + len_a);
                if (!dict.containsKey('a')) {
                    dict.put('a', word_a);
                } else if (!dict.get('a').equals(word_a)) {
                    return false;
                }
                j += len_a;
            } else if (pattern.charAt(i) == 'b') {
                String word_b = value.substring(j, j + len_b);
                if (!dict.containsKey('b')) {
                    dict.put('b', word_b);
                } else if (!dict.get('b').equals(word_b)) {
                    return false;
                }
                j += len_b;
            }
        }
        return !dict.getOrDefault('a', "").equals(dict.getOrDefault('b', ""));
    }

    // value能不能被k次切分
    private boolean helper(String value, int k) {
        int m = value.length();
        if (m % k != 0) return false;
        int len = m / k;
        String s = value.substring(0, len);
        for (int i = len; i < m; i += len) {
            if (!value.substring(i, i + len).equals(s)) return false;
        }
        return true;
    }

    // 168 excel表列名称 XX进制模拟
    public String convertToTitle(int columnNumber) {
        StringBuilder sb = new StringBuilder();
        while (columnNumber != 0) {
            columnNumber--;
            sb.append((char) (columnNumber % 26 + 'A'));
            columnNumber /= 26;
        }
        return sb.reverse().toString();
    }

    //1017. 负二进制转换  进制模拟
    public String baseNeg2(int n) {
        if (n == 0 || n == 1) {
            return String.valueOf(n);
        }
        StringBuilder res = new StringBuilder();
        while (n != 0) {
            int remainder = n & 1;
            res.append(remainder);
            n -= remainder;
            n /= -2;
        }
        return res.reverse().toString();
    }

    public String baseNeg2Two(int n) {
        if (n == 0 || n == 1) {
            return String.valueOf(n);
        }
        StringBuilder res = new StringBuilder();
        while (n != 0) {
            int remainder = n % (-2);
            res.append(Math.abs(remainder));
            // 负数取余，会有余数=-1的情况，即奇数位上的1
            // 需要左边一位和当前位一正一负凑出来
            n = remainder < 0 ? (n / (-2) + 1) : (n / (-2));
        }
        return res.reverse().toString();
    }

    // 58最后一个单词长度
    public int lengthOfLastWord(String s) {
        int index = s.length() - 1;
        while (index >= 0 && s.charAt(index) == ' ') {
            index--;
        }
        int index2 = index;
        while (index2 >= 0 && s.charAt(index2) != ' ') {
            index2--;
        }
        return index - index2;
    }

    // 43 字符串相乘
    public String multiply(String num1, String num2) {
        if ("0".equals(num1) || "0".equals(num2)) return "0";
        String ans = "0";
        int n = num1.length(), m = num2.length();
        for (int i = n - 1; i >= 0; i--) {
            StringBuilder cur = new StringBuilder();
            int add = 0;
            for (int j = n - 1; j > i; j--) {
                cur.append("0");
            }
            int y = num1.charAt(i) - '0';
            for (int j = m - 1; j >= 0; j--) {
                int x = num2.charAt(j) - '0';
                int product = x * y + add;
                cur.append(product % 10);
                add = product / 10;
            }
            if (add != 0) {
                cur.append(add % 10);
            }
            ans = addStrings(ans, cur.reverse().toString());
        }
        return ans;
    }

    public String multiply2(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        int[] res = new int[num1.length() + num2.length()];
        for (int i = num1.length() - 1; i >= 0; i--) {
            int n1 = num1.charAt(i) - '0';
            for (int j = num2.length() - 1; j >= 0; j--) {
                int n2 = num2.charAt(j) - '0';
                int sum = (res[i + j + 1] + n1 * n2);
                res[i + j + 1] = sum % 10;
                res[i + j] += sum / 10;
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < res.length; i++) {
            if (i == 0 && res[i] == 0) continue;
            result.append(res[i]);
        }
        return result.toString();
    }


    // 415 字符串相加
    public String addStrings(String num1, String num2) {
        int i = num1.length() - 1, j = num2.length() - 1;
        int add = 0;
        StringBuilder sb = new StringBuilder();
        while (i >= 0 || j >= 0 || add != 0) {
            int x = i >= 0 ? num1.charAt(i) - '0' : 0;
            int y = j >= 0 ? num2.charAt(j) - '0' : 0;
            int sum = x + y + add;
            sb.append(sum % 10);
            add = sum / 10;
            i--;
            j--;
        }
        sb.reverse();
        return sb.toString();
    }

    // 371 两整数之和 不用+ -符号
    public int getSum(int a, int b) {
        int ans = 0, r = 0;
        for (int i = 0; i < 32; i++) {
            int x = (a >> i) & 1, y = (b >> i) & 1;
            if (x == 1 && y == 1) {
                ans |= (r << i);
                r = 1;
            } else if (x == 1 || y == 1) {
                ans |= ((1 ^ r) << i);
            } else {
                ans |= (r << i);
                r = 0;
            }
        }
        return ans;
    }

    //1015. 可被 K 整除的最小整数
    public int smallestRepunitDivByK(int k) {
        int resid = 1 % k, len = 1; // resid为余数，len为数字长度，初始值为1
        Set<Integer> set = new HashSet<Integer>(); // 创建一个无序集合，用于存储余数
        set.add(resid); // 插入余数1
        while (resid != 0) { // 当余数为0时退出循环
            resid = (resid * 10 + 1) % k; // 计算下一个余数
            len++; // 数字长度+1
            if (set.contains(resid)) { // 如果余数重复出现，则无解
                return -1;
            }
            set.add(resid); // 将余数插入集合
        }
        return len; // 返回数字长度
    }

    // 163 缺失的区间
    public List<String> findMissingRanges(int[] nums, int lower, int upper) {
        int n = nums.length;
        List<String> result = new ArrayList<>();
        if (n == 0) {
            result.add(helper(lower - 1, upper + 1));
            return result;
        }
        if (lower < nums[0]) result.add(helper(lower - 1, nums[0]));
        for (int i = 0; i < n - 1; i++) {
            if (nums[i] + 1 != nums[i + 1]) {
                result.add(helper(nums[i], nums[i + 1]));
            }
        }
        if (upper > nums[n - 1]) result.add(helper(nums[n - 1], upper + 1));
        return result;
    }

    private String helper(int left, int right) {
        StringBuilder sb = new StringBuilder();
        if (right - left == 2) sb.append(left + 1);
        else sb.append(left + 1).append("->").append(right - 1);
        return sb.toString();
    }

    // 69 x的平方根
    // 除法可以转换成乘法的二分
    public int mySqrt(int x) {
        if (x <= 1) return x;
        int l = 1, r = x / 2;
        while (l < r) {
            int mid = (l + r + 1) >> 1;
            if ((long) mid * mid > x) {
                r = mid - 1;
            } else {
                l = mid;
            }
        }
        return l;
    }

    // 29 两数相除
    public int divide(int dividend, int divisor) {
        long x = dividend, y = divisor;
        boolean sign = true;
        if (x < 0) {
            sign = !sign;
            x = -x;
        }
        if (y < 0) {
            sign = !sign;
            y = -y;
        }
        long l = 0, r = x;
        while (l < r) {
            long mid = (l + r + 1) >> 1;
            if (mul(mid, y) <= x) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        long ans = sign ? l : -l;
        if (ans > Integer.MAX_VALUE || ans < Integer.MIN_VALUE) return Integer.MAX_VALUE;
        return (int) ans;
    }

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

    // 166 分数到小数
    //输入：numerator = 1, denominator = 2
//输出："0.5"
//输入：numerator = 2, denominator = 1
//输出："2"
//输入：numerator = 4, denominator = 333
//输出："0.(012)"
    public String fractionToDecimal(int numerator, int denominator) {
        long a = numerator, b = denominator;
        if (a % b == 0) return String.valueOf(a / b);
        StringBuilder sb = new StringBuilder();
        if (a * b < 0) sb.append("-");
        a = Math.abs(a);
        b = Math.abs(b);
        sb.append(a / b).append(".");
        a %= b;
        Map<Long, Integer> map = new HashMap<>();
        while (a != 0) {
            map.put(a, sb.length());
            a *= 10;
            sb.append(a / b);
            a %= b;
            if (map.containsKey(a)) {
                int pos = map.get(a);
                return String.format("%s(%s)", sb.substring(0, pos), sb.substring(pos));
            }
        }
        return sb.toString();
    }

    // 592 分数加减运算
    //输入: expression = "-1/2+1/2"
//输出: "0/1"
    public String fractionAddition(String expression) {
        long x = 0, y = 1; // 分子，分母
        int index = 0, n = expression.length();
        while (index < n) {
            // 读取分子
            long x1 = 0, sign = 1;
            if (expression.charAt(index) == '-' || expression.charAt(index) == '+') {
                sign = expression.charAt(index) == '-' ? -1 : 1;
                index++;
            }
            while (index < n && Character.isDigit(expression.charAt(index))) {
                x1 = x1 * 10 + expression.charAt(index) - '0';
                index++;
            }
            x1 = sign * x1;
            index++;

            // 读取分母
            long y1 = 0;
            while (index < n && Character.isDigit(expression.charAt(index))) {
                y1 = y1 * 10 + expression.charAt(index) - '0';
                index++;
            }

            x = x * y1 + x1 * y;
            y *= y1;
        }
        if (x == 0) {
            return "0/1";
        }
        long g = gcd(Math.abs(x), y); // 获取最大公约数
        return Long.toString(x / g) + "/" + Long.toString(y / g);
    }

    public String fractionAddition2(String s) {
        int n = s.length();
        char[] cs = s.toCharArray();
        String ans = "";
        for (int i = 0; i < n; ) {
            int j = i + 1;
            while (j < n && cs[j] != '+' && cs[j] != '-') j++;
            String num = s.substring(i, j);
            if (cs[i] != '+' && cs[i] != '-') num = "+" + num;
            if (!ans.equals("")) ans = calc(num, ans);
            else ans = num;
            i = j;
        }
        return ans.charAt(0) == '+' ? ans.substring(1) : ans;
    }

    private String calc(String a, String b) {
        boolean fa = a.charAt(0) == '+', fb = b.charAt(0) == '+';
        if (!fa && fb) return calc(b, a);
        long[] p = parse(a), q = parse(b);
        long p1 = p[0] * q[1], q1 = q[0] * p[1];
        if (fa && fb) {
            long r1 = p1 + q1, r2 = p[1] * q[1], c = gcd(r1, r2);
            return "+" + (r1 / c) + "/" + (r2 / c);
        } else if (!fa && !fb) {
            long r1 = p1 + q1, r2 = p[1] * q[1], c = gcd(r1, r2);
            return "-" + (r1 / c) + "/" + (r2 / c);
        } else {
            long r1 = p1 - q1, r2 = p[1] * q[1], c = gcd(Math.abs(r1), r2);
            String ans = (r1 / c) + "/" + (r2 / c);
            if (p1 >= q1) ans = "+" + ans;
            return ans;
        }
    }

    private long[] parse(String s) {
        int n = s.length(), idx = 1;
        while (idx < n && s.charAt(idx) != '/') idx++;
        long a = Long.parseLong(s.substring(1, idx)), b = Long.parseLong(s.substring(idx + 1));
        return new long[]{a, b};
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    //1073. 负二进制数相加
    public int[] addNegabinary(int[] arr1, int[] arr2) {
        int i = arr1.length - 1, j = arr2.length - 1;
        int carry = 0;
        List<Integer> ans = new ArrayList<Integer>();
        while (i >= 0 || j >= 0 || carry != 0) {
            int x = carry;
            if (i >= 0) {
                x += arr1[i];
            }
            if (j >= 0) {
                x += arr2[j];
            }
            if (x >= 2) {
                ans.add(x - 2);
                carry = -1;
            } else if (x >= 0) {
                ans.add(x);
                carry = 0;
            } else {
                ans.add(1);
                carry = 1;
            }
            --i;
            --j;
        }
        while (ans.size() > 1 && ans.get(ans.size() - 1) == 0) {
            ans.remove(ans.size() - 1);
        }
        int[] arr = new int[ans.size()];
        for (i = 0, j = ans.size() - 1; j >= 0; i++, j--) {
            arr[i] = ans.get(j);
        }
        return arr;
    }

    // 66 加一
    //输入：digits = [1,2,3]
//输出：[1,2,4]
    public int[] plusOne(int[] digits) {
        int len = digits.length;
        for (int i = len - 1; i >= 0; i--) {
            digits[i] = (digits[i] + 1) % 10;
            if (digits[i] != 0) {
                return digits;
            }
        }
        digits = new int[len + 1];
        digits[0] = 1;
        return digits;
    }

    // 67 二进制求和
    //输入：a = "1010", b = "1011"
//输出："10101"
    public String addBinary(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int n = Math.max(a.length(), b.length());
        int carry = 0;
        for (int i = 0; i < n; i++) {
            carry += i < a.length() ? (a.charAt(a.length() - 1 - i) - '0') : 0;
            carry += i < b.length() ? (b.charAt(b.length() - 1 - i) - '0') : 0;
            sb.append((char) (carry % 2 + '0'));
            carry /= 2;
        }
        if (carry > 0) {
            sb.append("1");
        }
        return sb.reverse().toString();
    }

    // 640 求解方程式
    //输入: equation = "x+5-3+x=6+x-2"
//输出: "x=2"
    public String solveEquation(String s) {
        int x = 0, num = 0, n = s.length();
        char[] cs = s.toCharArray();
        for (int i = 0, op = 1; i < n; ) {
            if (cs[i] == '+') {
                op = 1;
                i++;
            } else if (cs[i] == '-') {
                op = -1;
                i++;
            } else if (cs[i] == '=') {
                x *= -1;
                num *= -1;
                op = 1;
                i++;
            } else {
                int j = i;
                while (j < n && cs[j] != '+' && cs[j] != '-' && cs[j] != '=') j++;
                if (cs[j - 1] == 'x') x += (i < j - 1 ? Integer.parseInt(s.substring(i, j - 1)) : 1) * op;
                else num += Integer.parseInt(s.substring(i, j)) * op;
                i = j;
            }
        }
        if (x == 0) return num == 0 ? "Infinite solutions" : "No solution";
        else return "x=" + (num / -x);
    }

    //1410. HTML 实体解析器
    public String entityParser(String text) {
        Map<String, String> map = new HashMap<>();
        map.put("&quot;", "\"");
        map.put("&apos;", "'");
        map.put("&amp;", "&");
        map.put("&gt;", ">");
        map.put("&lt;", "<");
        map.put("&frasl;", "/");
        int n = text.length();
        StringBuilder sb = new StringBuilder();
        for (int l = 0, r = 0; l < n; l++) {
            if (text.charAt(l) == '&') {
                r = l + 1;
                while (r < n && text.charAt(r) != ';') {
                    r++;
                }
                String sub = text.substring(l, Math.min(r + 1, n));
                if (map.containsKey(sub)) {
                    sb.append(map.get(sub));
                    l = r;
                    continue;
                }
            }
            sb.append(text.charAt(l));
        }
        return sb.toString();
    }

    //388. 文件的最长绝对路径
    public int lengthLongestPath(String input) {
        Map<Integer, String> map = new HashMap<>();
        int n = input.length();
        String ans = null;
        for (int i = 0; i < n; ) {
            int level = 0;
            while (i < n && input.charAt(i) == '\t') {
                level++;
                i++;
            }
            int j = i;
            boolean isDir = true;
            while (j < n && input.charAt(j) != '\n') {
                if (input.charAt(j++) == '.') {
                    isDir = false;
                }
            }
            String cur = input.substring(i, j);
            String prev = map.getOrDefault(level - 1, null);
            String path = prev == null ? cur : prev + "/" + cur;
            if (isDir) {
                map.put(level, path);
            } else {
                if (ans == null || path.length() > ans.length()) {
                    ans = path;
                }
            }
            i = j + 1;
        }
        return ans == null ? 0 : ans.length();
    }

    static int[] hash = new int[10010];
    public int lengthLongestPath2(String s) {
        Arrays.fill(hash, -1);
        int n = s.length(), ans = 0;
        for (int i = 0; i < n; ) {
            int level = 0;
            while (i < n && s.charAt(i) == '\t' && ++level >= 0) i++;
            int j = i;
            boolean isDir = true;
            while (j < n && s.charAt(j) != '\n') {
                if (s.charAt(j++) == '.') isDir = false;
            }
            int cur = j - i;
            int prev = level - 1 >= 0 ? hash[level - 1] : -1;
            int path = prev + 1 + cur;
            if (isDir) hash[level] = path;
            else if (path > ans) ans = path;
            i = j + 1;
        }
        return ans;
    }

    //363. 矩形区域不超过 K 的最大数值和
    public int maxSumSubmatrix(int[][] matrix, int k) {
        int ans = Integer.MIN_VALUE;
        int m = matrix.length, n = matrix[0].length;
        for (int i = 0; i < m; ++i) { // 枚举上边界
            int[] sum = new int[n];
            for (int j = i; j < m; ++j) { // 枚举下边界
                for (int c = 0; c < n; ++c) {
                    sum[c] += matrix[j][c]; // 更新每列的元素和
                }
                //二分查找
                // Sr-Sl<=k => Sl>=Sr-k 要使得Sr-Sl尽可能大，就要使Sl尽可能小，枚举r找满足Sr-k的最小Sl
                TreeSet<Integer> sumSet = new TreeSet<Integer>();
                sumSet.add(0);
                int s = 0;
                for (int v : sum) {
                    s += v;
                    Integer ceil = sumSet.ceiling(s - k);
                    if (ceil != null) {
                        ans = Math.max(ans, s - ceil);
                    }
                    sumSet.add(s);
                }
            }
        }
        return ans;
    }

    //390. 消除游戏
    public int lastRemaining(int n) {
        int head = 1;
        int step = 1;
        boolean left = true;
        //int n = n;

        while (n > 1) {
            //从左边开始移除 or（从右边开始移除，数列总数为奇数）
            if (left || n % 2 != 0) {
                head += step;
            }
            step *= 2; //步长 * 2
            left = !left; //取反移除方向
            n /= 2; //总数 / 2
        }

        return head;
    }


    //2342. 数位和相等数对的最大和
    public int maximumSum(int[] nums) {
        int[][] val = new int[100][2];
        for (int x : nums) {
            int t = x, cur = 0;
            while (t != 0) {
                cur += t % 10;
                t /= 10;
            }
            if (x >= val[cur][1]) { // 最大沦为次大, 更新最大
                val[cur][0] = val[cur][1];
                val[cur][1] = x;
            } else if (x > val[cur][0]) { // 更新次大
                val[cur][0] = x;
            }
        }
        int ans = -1;
        for (int i = 0; i < 100; i++) {
            if (val[i][0] != 0 && val[i][1] != 0) ans = Math.max(ans, val[i][0] + val[i][1]);
        }
        return ans;
    }

    public int maximumSum2(int[] nums) {
        Map<Integer, PriorityQueue<String>> group = new HashMap<>();
        for (int num : nums) {
            int digitSum = getDigitSum(num);
            PriorityQueue<String> pq = group.getOrDefault(digitSum, new PriorityQueue<>((o1, o2) -> {
                if (o1.length() != o2.length()) {
                    return o2.length() - o1.length();
                }
                return o2.compareTo(o1);
            }));
            pq.offer(String.valueOf(num));
            group.put(digitSum, pq);
        }
        int max = -1;
        for (Map.Entry<Integer, PriorityQueue<String>> entry : group.entrySet()) {
            if (entry.getValue().size() > 1) {
                max = Math.max(max, Integer.valueOf(entry.getValue().poll()) + Integer.valueOf(entry.getValue().poll()));
            }
        }
        return max;
    }

    // 989 数组形式的整数加法
    //输入：num = [1,2,0,0], k = 34
//输出：[1,2,3,4]
//解释：1200 + 34 = 1234
    public List<Integer> addToArrayForm(int[] num, int k) {
        List<Integer> result = new ArrayList<>();
        int n = num.length;
        for (int i = n - 1; i >= 0 || k > 0; i--, k /= 10) {
            k += (i >= 0 ? num[i] : 0);
            result.add(0, k % 10);
        }
        return result;
    }


    // 728 自除数
    //自除数 是指可以被它包含的每一位数整除的数
// 例如，128 是一个 自除数 ，因为 128 % 1 == 0，128 % 2 == 0，128 % 8 == 0。
// 自除数 不允许包含 0 。
// 给定两个整数 left 和 right ，返回一个列表，列表的元素是范围 [left, right] 内所有的 自除数 。
    public List<Integer> selfDividingNumbers(int left, int right) {
        List<Integer> result = new ArrayList<>();
        for (int i = left; i <= right; i++) {
            if (isSelfDividingNumber(i))
                result.add(i);
        }
        return result;
    }

    private boolean isSelfDividingNumber(int number) {
        if (number <= 9 && number >= 1) {
            return true;
        }
        int original = number;
        while (number > 0) {
            int divide = number % 10;
            if ((divide == 0) || (original % divide) != 0) {
                return false;
            }
            number = number / 10;
        }
        return true;
    }

    // 804 唯一摩尔斯密码词
    public int uniqueMorseRepresentations(String[] words) {
        String[] MorseCodeList = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        HashMap<String, String> MorseCodeMap = new HashMap<>();
        for (int i = 0; i < letters.length; i++) {
            MorseCodeMap.put(letters[i], MorseCodeList[i]);
        }
        Set<String> morseCodes = new HashSet<>();
        for (String word : words) {
            StringBuilder morseCode = new StringBuilder();
            for (char letter : word.toCharArray()) {
                morseCode.append(MorseCodeMap.get(String.valueOf(letter)));
            }
            morseCodes.add(morseCode.toString());
        }
        return morseCodes.size();
    }

    // 68 文本左右对齐
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> resultList = new ArrayList<>();

        // 每次取出来满足maxWidth的数量最多的单词
        int count = 0;
        int start = 0;
        for (int i = 0; i < words.length; i++) {
            count += words[i].length();
            if (count > maxWidth) {
                resultList.add(helper(words, start, i - 1, maxWidth));
                start = i;
                count = words[i].length();
            }
            // 一个单词结束至少要有一个空格
            count++;
        }

        // 处理最后一截
        resultList.add(helper(words, start, words.length - 1, maxWidth));

        return resultList;
    }

    private String helper(String[] words, int start, int end, int maxWidth) {
        StringBuilder sb = new StringBuilder();
        if (start == end) {
            // 一行一词
            oneWordOneRow(words, start, maxWidth, sb);
        } else if (end == words.length - 1) {
            // 最后一行
            lastRow(words, start, end, maxWidth, sb);
        } else {
            // 一行多词
            normal(words, start, end, maxWidth, sb);
        }

        return sb.toString();
    }

    private void oneWordOneRow(String[] words, int start, int maxWidth, StringBuilder sb) {
        // 一行只有一个单词的情况：右边添加多余的空格
        sb.append(words[start]);
        int num = maxWidth - words[start].length();
        for (int i = 0; i < num; i++) {
            sb.append(" ");
        }
    }

    private void lastRow(String[] words, int start, int end, int maxWidth, StringBuilder sb) {
        // 最后一行：单词之间不用添加额外的空格，多余的空格全部在右边
        for (int i = start; i <= end; i++) {
            sb.append(words[i]);
            if (i != end) {
                // 单词之间只有一个空格
                sb.append(" ");
            } else {
                // 判断要加几个空格
                int num = maxWidth - sb.length();
                for (int j = 0; j < num; j++) {
                    sb.append(" ");
                }
            }
        }
    }

    private void normal(String[] words, int start, int end, int maxWidth, StringBuilder sb) {
        // 正常情况：单词间的空格尽量均匀分配，可能左边的空格会多一
        // 先计算出单词总长度
        int wordsLength = 0;
        for (int i = start; i <= end; i++) {
            wordsLength += words[i].length();
        }
        // 再看均匀分配能分配几个
        int seperate = (maxWidth - wordsLength) / (end - start);
        // 多余出来的空格，即不能平均的部分，这部分要按照从左到右依次分配
        // 比如，有4个单词，即3个间隔，一共有5个空格的话
        // 平均的话是每个间隔一个空格，还多了2个空格，从左到右分配
        // 最后的间隔就是前两个各占2个空格，最后一个占1个空格
        int remain = (maxWidth - wordsLength) % (end - start);

        for (int i = start; i <= end; i++) {
            sb.append(words[i]);
            if (i != end) {
                // 先加上平均分配的空格
                for (int j = 0; j < seperate; j++) {
                    sb.append(" ");
                }
                // 再看还有没有多余的空格
                if (remain-- > 0) {
                    sb.append(" ");
                }
            }
        }
    }

    //1106 解析布尔表达式 toreview
    public boolean parseBoolExpr(String s) {
        Deque<Character> nums = new ArrayDeque<>(), ops = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == ',') continue;
            if (c == 't' || c == 'f') nums.addLast(c);
            if (c == '|' || c == '&' || c == '!') ops.addLast(c);
            if (c == '(') nums.addLast('-');
            if (c == ')') {
                char op = ops.pollLast(), cur = ' ';
                while (!nums.isEmpty() && nums.peekLast() != '-') {
                    char top = nums.pollLast();
                    cur = cur == ' ' ? top : calc(top, cur, op);
                }
                if (op == '!') cur = cur == 't' ? 'f' : 't';
                nums.pollLast();
                nums.addLast(cur);
            }
        }
        return nums.peekLast() == 't';
    }

    char calc(char a, char b, char op) {
        boolean x = a == 't', y = b == 't';
        boolean ans = op == '|' ? x | y : x & y;
        return ans ? 't' : 'f';
    }

    // 224 基本计算器
    public int calculate(String s) {
        s = s.replace(" ", "");
        Stack<Character> opts = new Stack<>();
        Stack<Integer> nums = new Stack<>();
        nums.push(0);
        char[] chars = s.toCharArray();
        int n = chars.length;
        for (int i = 0; i < n; i++) {
            char c = chars[i];
            if (c == '(') {
                opts.push(c);
            } else if (c == ')') {
                while (!opts.isEmpty() && opts.peek() != '(') {
                    calc(opts, nums);
                }
                if (!opts.isEmpty()) {
                    opts.pop();
                }
            } else {
                if (Character.isDigit(c)) {
                    int j = i, num = 0;
                    while (j < n && Character.isDigit(chars[j])) {
                        num = num * 10 + chars[j] - '0';
                        j++;
                    }
                    i = j - 1;
                    nums.push(num);
                } else {
                    if (i > 0 && (chars[i - 1] == '(' || chars[i - 1] == '+' || chars[i - 1] == '-')) {
                        nums.push(0);
                    }
                    while (!opts.isEmpty() && opts.peek() != '(') {
                        calc(opts, nums);
                    }
                    opts.push(c);
                }
            }
        }
        while (!opts.isEmpty()) calc(opts, nums);
        return nums.peek();

    }

    private void calc(Stack<Character> opts, Stack<Integer> nums) {
        if (nums.isEmpty() || nums.size() < 2) return;
        if (opts.isEmpty()) return;
        int prev = nums.pop();
        int preprev = nums.pop();
        char opt = opts.pop();
        nums.push(opt == '+' ? preprev + prev : preprev - prev);
    }

    // 227 基本计算器2
    Map<Character, Integer> map = new HashMap<Character, Integer>() {{
        put('-', 1);
        put('+', 1);
        put('*', 2);
        put('/', 2);
        put('%', 2);
        put('^', 3);
    }};

    public int calculate2(String s) {
        s = s.replace(" ", "");
        Stack<Character> opts = new Stack<>();
        Stack<Integer> nums = new Stack<>();
        nums.push(0);
        char[] chars = s.toCharArray();
        int n = chars.length;
        for (int i = 0; i < n; i++) {
            char c = chars[i];
            if (c == '(') {
                opts.push(c);
            } else if (c == ')') {
                while (!opts.isEmpty() && opts.peek() != '(') {
                    calc2(opts, nums);
                }
                if (!opts.isEmpty()) {
                    opts.pop();
                }
            } else {
                if (Character.isDigit(c)) {
                    int j = i, num = 0;
                    while (j < n && Character.isDigit(chars[j])) {
                        num = num * 10 + chars[j] - '0';
                        j++;
                    }
                    i = j - 1;
                    nums.push(num);
                } else {
                    if (i > 0 && (chars[i - 1] == '(' || chars[i - 1] == '+' || chars[i - 1] == '-')) {
                        nums.push(0);
                    }
                    while (!opts.isEmpty() && opts.peek() != '(') {
                        char prev = opts.peek();
                        if (map.get(prev) >= map.get(c)) {
                            calc2(opts, nums);
                        } else {
                            break;
                        }
                    }
                    opts.push(c);
                }
            }
        }
        while (!opts.isEmpty()) calc2(opts, nums);
        return nums.peek();
    }

    private void calc2(Stack<Character> opts, Stack<Integer> nums) {
        if (nums.isEmpty() || nums.size() < 2) return;
        if (opts.isEmpty()) return;
        int prev = nums.pop();
        int preprev = nums.pop();
        char opt = opts.pop();
        int ans = 0;
        if (opt == '+') ans = preprev + prev;
        else if (opt == '-') ans = preprev - prev;
        else if (opt == '*') ans = preprev * prev;
        else if (opt == '/') ans = preprev / prev;
        else if (opt == '^') ans = (int) Math.pow(preprev, prev);
        else if (opt == '%') ans = preprev % prev;
        nums.push(ans);
    }

    //面试题 16.26. 计算器 无括号
    public int calculate3(String s) {
        int n = s.length();
        Stack<Integer> stack = new Stack<>();
        char preSign = '+';
        int num = 0;
        char[] chars = s.toCharArray();
        for (int i = 0; i < n; i++) {
            if (Character.isDigit(chars[i])) {
                num = num * 10 + (chars[i] - '0');
            }
            if (!Character.isDigit(chars[i]) && chars[i] != ' ' || i == n - 1) {
                switch (preSign) {
                    case '+':
                        stack.push(num);
                        break;
                    case '-':
                        stack.push(-num);
                        break;
                    case '*':
                        stack.push(stack.pop() * num);
                        break;
                    case '/':
                        stack.push(stack.pop() / num);
                        break;
                }
                preSign = chars[i];
                num = 0;
            }
        }
        int ans = 0;
        while (!stack.isEmpty()) {
            ans += stack.pop();
        }
        return ans;
    }

    // 273 整数转换英文表示
    // 单个数字，0，1，2，3，4，5，6，7，8，9
    private static String[] OneNum = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
    // 整十，10，20，30，40，50，60，70，80，90
    private static String[] AnyTen = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    // 十几，11，12，13，14，15，16，17，18，19
    private static String[] TenNum = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    // 三位一组，几千，几百万，几十亿
    private static String[] ThreeNum = {"", "Thousand", "Million", "Billion"};

    public String numberToWords(int num) {
        if (num == 0) return "Zero";
        // 每三位一组，比如对于1234567891，在英文里面1,234,567,891表示 1 Billion 234 Million 567 Thousand 891
        StringBuilder sb = new StringBuilder();
        int idx = 3;
        for (int x = 1000000000; x > 0; x /= 1000) {
            if (num / x % 1000 != 0) {
                // 三位一组进行计算，从高到低
                sb.append(calcThreeNum(num / x % 1000)).append(" ").append(ThreeNum[idx]).append(" ");
            }
            idx--;
        }

        return sb.toString().trim();
    }

    private String calcThreeNum(int num) {
        // 计算三位数，比如 001 或者 012 或者 456
        return num / 100 == 0 ? calcTwoNum(num) : calcOneNum(num / 100) + " Hundred" + (num % 100 == 0 ? "" : " " + calcTwoNum(num % 100));
    }

    private String calcTwoNum(int num) {
        // 计算两位数，比如 01 或者 12 或者 23
        if (num >= 10 && num < 20) {
            return TenNum[num % 10];
        }
        return num / 10 == 0 ? calcOneNum(num % 10) : AnyTen[num / 10] + (num % 10 == 0 ? "" : " " + calcOneNum(num % 10));
    }

    private String calcOneNum(int num) {
        // 计算三位数，比如 4
        return OneNum[num];
    }

    // 289 生命游戏
    public void gameOfLife(int[][] board) {
        int[] neighbors = {0, 1, -1};

        int rows = board.length;
        int cols = board[0].length;

        // 创建复制数组 copyBoard
        int[][] copyBoard = new int[rows][cols];

        // 从原数组复制一份到 copyBoard 中
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                copyBoard[row][col] = board[row][col];
            }
        }

        // 遍历面板每一个格子里的细胞
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                // 对于每一个细胞统计其八个相邻位置里的活细胞数量
                int liveNeighbors = 0;

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {

                        if (!(neighbors[i] == 0 && neighbors[j] == 0)) {
                            int r = (row + neighbors[i]);
                            int c = (col + neighbors[j]);

                            // 查看相邻的细胞是否是活细胞
                            if ((r < rows && r >= 0) && (c < cols && c >= 0) && (copyBoard[r][c] == 1)) {
                                liveNeighbors += 1;
                            }
                        }
                    }
                }

                // 规则 1 或规则 3
                if ((copyBoard[row][col] == 1) && (liveNeighbors < 2 || liveNeighbors > 3)) {
                    board[row][col] = 0;
                    // -1 代表这个细胞过去是活的现在死了
//                    board[row][col] = -1;
                }
                // 规则 4
                if (copyBoard[row][col] == 0 && liveNeighbors == 3) {
                    board[row][col] = 1;
                    // 2 代表这个细胞过去是死的现在活了
//                    board[row][col] = 2;
                }
            }
        }

//        // 遍历 board 得到一次更新后的状态
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                if (board[row][col] > 0) {
//                    board[row][col] = 1;
//                } else {
//                    board[row][col] = 0;
//                }
//            }
//        }
    }


    // 498 对角线遍历
    //输入：mat = [[1,2,3],
    // [4,5,6],
    // [7,8,9]]
//输出：[1,2,4,7,5,3,6,8,9]
    public int[] findDiagonalOrder(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[] res = new int[m * n];
        int idx = 0;
        for (int i = 0; i <= m + n - 2; i++) {
            if (i % 2 == 0) {
                // 大于边界的时候，在最后一行开始，i变道m-1减少了i-m+1,对应的y从0加这么多
                int x = i >= m ? m - 1 : i;
                int y = i >= m ? i - m + 1 : 0;
                while (x >= 0 && y < n) {
                    res[idx++] = mat[x--][y++];
                }
            } else if (i % 2 == 1) {
                int x = i >= n ? i - n + 1 : 0;
                int y = i >= n ? n - 1 : i;
                while (x < m && y >= 0) {
                    res[idx++] = mat[x++][y--];
                }
            }
        }
        return res;
    }

    //636. 函数的独占时间
    public int[] exclusiveTime(int n, List<String> logs) {
        Stack<Integer> stack = new Stack<>();
        int[] result = new int[n];
        int cur = -1;
        for (String log : logs) {
            String[] arr = log.split(":");
            int idx = Integer.parseInt(arr[0]);
            int ts = Integer.parseInt(arr[2]);
            if ("start".equals(arr[1])) {
                if (!stack.isEmpty()) {
                    result[stack.peek()] += ts - cur;
                }
                cur = ts;
                stack.push(idx);
            } else {
                stack.pop();
                result[idx] += ts - cur + 1;
                cur = ts + 1;
            }
        }
        return result;
    }

    //766 托普利茨矩阵
    public boolean isToeplitzMatrix(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] != matrix[i - 1][j - 1]) {
                    return false;
                }
            }
        }
        return true;
    }

    // 735 行星碰撞
    public int[] asteroidCollision(int[] ats) {
        Deque<Integer> d = new ArrayDeque<>();
        for (int t : ats) {
            // ok 当前t是否可以压进栈
            boolean ok = true;
            // 只比较栈顶为正，t为负的情况
            while (ok && !d.isEmpty() && d.peekLast() > 0 && t < 0) {
                int a = Math.abs(d.peekLast()), b = Math.abs(t);
                if (a <= b) d.pollLast();//相等时也出栈
                if (a >= b) ok = false;//相等时不可压栈
            }
            if (ok) d.addLast(t);
        }
        int sz = d.size();
        int[] ans = new int[sz];
        while (!d.isEmpty()) ans[--sz] = d.pollLast();
        return ans;
    }

    //面试题 16.15 珠玑妙算
    public int[] masterMind(String solution, String guess) {
        int[] res = new int[2];
        Map<Character, Integer> map = new HashMap();
        for (int i = 0; i < solution.length(); i++) {
            if (solution.charAt(i) == guess.charAt(i)) {
                res[0] = res[0] + 1;
            } else {
                map.put(solution.charAt(i), map.getOrDefault(solution.charAt(i), 0) + 1);
            }
        }

        for (int i = 0; i < solution.length(); i++) {
            if (solution.charAt(i) != guess.charAt(i) && map.containsKey(guess.charAt(i)) && map.get(guess.charAt(i)) > 0) {
                map.put(guess.charAt(i), map.get(guess.charAt(i)) - 1);
                res[1] = res[1] + 1;
            }
        }
        return res;
    }

    //299. 猜数字游戏
    public String getHint(String secret, String guess) {
        int n = secret.length();
        char[] chars = secret.toCharArray();
        Map<Character, Integer> cnt = new HashMap<>();
        for (char c : chars) {
            cnt.put(c, cnt.getOrDefault(c, 0) + 1);
        }
        int cntA = 0, cntB = 0;
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < n; i++) {
            if (chars[i] == guess.charAt(i)) {
                set.add(i);
                cntA++;
                cnt.put(chars[i], cnt.get(chars[i]) - 1);
                if (cnt.get(chars[i]) == 0) cnt.remove(chars[i]);
            }
        }
        for (int i = 0; i < n; i++) {
            if (set.contains(i)) continue;
            if (cnt.containsKey(guess.charAt(i))) {
                cntB++;
                cnt.put(guess.charAt(i), cnt.get(guess.charAt(i)) - 1);
                if (cnt.get(guess.charAt(i)) == 0) cnt.remove(guess.charAt(i));
            }
        }
        return cntA + "A" + cntB + "B";
    }

    //539 最小时间差
    public int findMinDifference(List<String> timePoints) {
        timePoints.sort(String::compareTo);
        int min = Integer.MAX_VALUE;
        int n = timePoints.size();
        for (int i = 0; i < n - 1; i++) {
            String prev = timePoints.get(i);
            String next = timePoints.get(i + 1);
            min = Math.min(min, getDiff(next, prev));
        }
        int last = getDiff("24:00", timePoints.get(n - 1)) + getDiff(timePoints.get(0), "00:00");
        min = Math.min(last, min);
        return min;
    }

    private int getDiff(String next, String prev) {
        int hour1 = 0;
        int hour2 = 0;
        int m1 = 0;
        int m2 = 0;
        for (int i = 0; i < 2; i++) {
            hour1 = hour1 * 10 + next.charAt(i) - '0';
            hour2 = hour2 * 10 + prev.charAt(i) - '0';
            m1 = m1 * 10 + next.charAt(i + 3) - '0';
            m2 = m2 * 10 + prev.charAt(i + 3) - '0';
        }
        return (hour1 - hour2) * 60 + m1 - m2;
    }

    // 867 转置矩阵
    public int[][] transpose(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] mat = new int[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                mat[j][i] = matrix[i][j];
            }
        }
        return mat;
    }

    // 883 三维形体投影面积
    public int projectionArea(int[][] grid) {
        int x = 0, y = 0, z = 0;
        for (int i = 0; i < grid.length; i++) {
            int max = 0, max1 = 0;
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != 0) {
                    z++;
                }
                if (max <= grid[i][j]) {
                    max = grid[i][j];
                }
                if (max1 <= grid[j][i]) {
                    max1 = grid[j][i];
                }
            }
            y += max;
            x += max1;
        }
        return x + y + z;
    }

    // 896 单调队列
    public boolean isMonotonic(int[] nums) {
        int n = nums.length;
        if (nums[0] < nums[n - 1]) {
            for (int i = 0; i < n - 1; i++) {
                if (nums[i] > nums[i + 1]) return false;
            }
        } else {
            for (int i = 0; i < n - 1; i++) {
                if (nums[i] < nums[i + 1]) return false;
            }
        }
        return true;
    }

    // 1104 二叉树寻路
    public List<Integer> pathInZigZagTree(int label) {
        int row = 1, rowStart = 1;
        while (rowStart * 2 <= label) {
            row++;
            rowStart *= 2;
        }
        if (row % 2 == 0) {
            label = getReverse(label, row);
        }
        List<Integer> path = new ArrayList<>();
        while (row > 0) {
            if (row % 2 == 0) {
                path.add(getReverse(label, row));
            } else {
                path.add(label);
            }
            row--;
            label >>= 1;
        }
        Collections.reverse(path);
        return path;
    }

    public int getReverse(int label, int row) {
        return (1 << row - 1) + (1 << row) - 1 - label;
    }

    // 1184 公交站间的距离
    public int distanceBetweenBusStops(int[] distance, int start, int destination) {
        int i = start, j = start;
        int cost1 = 0, cost2 = 0;
        int n = distance.length;
        while (i != destination) {
            cost1 += distance[i++];
            i %= n;
        }
        while (j != destination) {
            if (--j < 0) j = n - 1;
            cost2 += distance[j];
        }
        return Math.min(cost1, cost2);
    }

    //2515. 到目标字符串的最短距离  环形下标技巧
    //给你一个下标从 0 开始的 环形 字符串数组 words 和一个字符串 target 。环形数组 意味着数组首尾相连。
    //形式上， words[i] 的下一个元素是 words[(i + 1) % n] ，
    //而 words[i] 的前一个元素是 words[(i - 1 + n) % n] ，其中 n 是 words 的长度。
    public int closetTarget(String[] words, String target, int startIndex) {
        int n = words.length;
        if (words[startIndex].equals(target)) return 0;
        Set<String> dict = new HashSet<>(Arrays.asList(words));
        if (!dict.contains(target)) return -1;
        int dist1 = 1, idx1 = startIndex, dist2 = 1, idx2 = startIndex;
        while (!words[(idx1 + 1) % n].equals(target)) {
            idx1 = (idx1 + 1) % n;
            dist1++;
        }
        while (!words[(idx2 - 1 + n) % n].equals(target)) {
            idx2 = (idx2 - 1 + n) % n;
            dist2++;
        }
        return Math.min(dist1, dist2);
    }

    // 1200最小绝对值差
    public List<List<Integer>> minimumAbsDifference(int[] arr) {
        Arrays.sort(arr);
        List<List<Integer>> ans = new ArrayList<>();
        int n = arr.length, min = arr[1] - arr[0];
        for (int i = 0; i < n - 1; i++) {
            int cur = arr[i + 1] - arr[i];
            if (cur < min) {
                ans.clear();
                min = cur;
            }
            if (cur == min) {
                List<Integer> temp = new ArrayList<>();
                temp.add(arr[i]);
                temp.add(arr[i + 1]);
                ans.add(temp);
            }
        }
        return ans;
    }

    //1247. 交换字符使得字符串相同
    //s1[i] = s2[i] 的位置不用处理。
    //s1[i] ≠ s2[i] 的个数计作d ；
    //若d为奇数，则 x与y的个数一定为奇数，不可能通过交换使s1,s2 相同
    //若d为偶数，则不同的情况为 s1[i] = 'x', s2[i] = 'y' 或 s1[i] = 'y', s2[i] = 'x'。 通过交换一次最多使 d-2。 贪心思想：每次交换尽量使d减少最多。
    //case1: 不同的x的个数为偶数，则不同的y的个数也为偶数，每次交换都可以使 d - 2 ，总次数 d/2
    //xxyyyy
    //yyxxxx
    //case2: 不同的x的个数为奇数，则不同的y的个数也为奇数; 先使用xx或yy，每次交换使d-2, 最后只剩下一对不同，通过两次交换即可。总次数为 (d-2)/2 + 2 即 d/2 + 1
    //xxxyyy   --> xy
    //yyyxxx   --> yx
    public int minimumSwap(String s1, String s2) {
        int[] cnt = new int[2];
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                ++cnt[s1.charAt(i) % 2];
            }
        }
        int d = cnt[0] + cnt[1];
        return d % 2 != 0 ? -1 : d / 2 + cnt[0] % 2;
    }

    // 1252 奇数值单元格的数目
    public int oddCells(int m, int n, int[][] indices) {
        int[][] matrix = new int[m][n];
        for (int[] indice : indices) {
            int row = indice[0];
            int col = indice[1];
            for (int i = 0; i < m; i++) {
                matrix[i][col] += 1;
            }
            for (int i = 0; i < n; i++) {
                matrix[row][i] += 1;
            }
        }
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] % 2 != 0) ans++;
            }
        }
        return ans;
    }

    public int oddCells2(int m, int n, int[][] indices) {
        int[] rows = new int[m];
        int[] cols = new int[n];
        for (int[] index : indices) {
            rows[index[0]]++;
            cols[index[1]]++;
        }
        int oddx = 0, oddy = 0;
        for (int i = 0; i < m; i++) {
            if ((rows[i] & 1) != 0) {
                oddx++;
            }
        }
        for (int i = 0; i < n; i++) {
            if ((cols[i] & 1) != 0) {
                oddy++;
            }
        }
        // 对于奇数行，只有偶数列才最终是奇数
        return oddx * (n - oddy) + (m - oddx) * oddy;
    }

    // 1282 用户分组
    public List<List<Integer>> groupThePeople(int[] groupSizes) {
        List<List<Integer>> result = new ArrayList<>();
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < groupSizes.length; i++) {
            List<Integer> group = map.getOrDefault(groupSizes[i], new ArrayList<>());
            if (group.size() == groupSizes[i]) {
                result.add(group);
                group = new ArrayList<>();
            }
            group.add(i);
            map.put(groupSizes[i], group);
        }
        result.addAll(map.values());
        return result;
    }

    // 1295 统计位数为偶数的数字
    public int findNumbers(int[] nums) {
        int ans = 0;
        for (int num : nums) {
            if (String.valueOf(num).length() % 2 == 0) {
                ans++;
            }
        }
        return ans;
    }

    //1487. 保证文件名唯一
    public String[] getFolderNames(String[] names) {
        int n = names.length;
        String[] ans = new String[n];
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String name = names[i];
            if (!map.containsKey(name)) {
                map.put(name, 1);
                ans[i] = name;
            } else {
                int k = map.get(name);
                while (map.containsKey(name + "(" + k + ")")) {
                    k++;
                }
                ans[i] = name + "(" + k + ")";
                map.put(name, k + 1);
                map.put(name + "(" + k + ")", 1);
            }
        }
        return ans;
    }

    //1599. 经营摩天轮的最大利润
    public int minOperationsMaxProfit(int[] customers, int boardingCost, int runningCost) {
        int ans = -1;
        int maxProfit = 0;
        int totalProfit = 0;
        int operations = 0;
        int customersCount = 0;
        int n = customers.length;
        for (int i = 0; i < n; i++) {
            operations++;
            customersCount += customers[i];
            int curCustomers = Math.min(customersCount, 4);
            customersCount -= curCustomers;
            totalProfit += boardingCost * curCustomers - runningCost;
            if (totalProfit > maxProfit) {
                maxProfit = totalProfit;
                ans = operations;
            }
        }
        if (customersCount == 0) {
            return ans;
        }
        int profitEachTime = boardingCost * 4 - runningCost;
        if (profitEachTime <= 0) {
            return ans;
        }
        if (customersCount > 0) {
            int fullTimes = customersCount / 4;
            totalProfit += profitEachTime * fullTimes;
            operations += fullTimes;
            if (totalProfit > maxProfit) {
                maxProfit = totalProfit;
                ans = operations;
            }
            int remainingCustomers = customersCount % 4;
            int remainingProfit = boardingCost * remainingCustomers - runningCost;
            totalProfit += remainingProfit;
            if (totalProfit > maxProfit) {
                maxProfit = totalProfit;
                operations++;
                ans++;
            }
        }
        return ans;
    }

    //2180. 统计各位数字之和为偶数的整数个数
    public int countEven(int num) {
        int cnt = 0;
        for (int i = 1; i <= num; i++) {
            int x = i;
            int sum = 0;
            while (x > 0) {
                sum += x % 10;
                x /= 10;
            }
            if ((sum & 1) == 0) cnt++;
        }
        return cnt;
    }

    //2042. 检查句子中的数字是否递增
    public boolean areNumbersAscending(String s) {
        int n = s.length();
        char[] chars = s.toCharArray();
        int i = 0;
        Integer prev = null;
        while (i < n) {
            while (i < n && !Character.isDigit(chars[i])) i++;
            if (i == n) return true;
            int num = 0;
            while (i < n && Character.isDigit(chars[i])) {
                num = num * 10 + chars[i++] - '0';
            }
            if (prev != null) {
                if (num <= prev) return false;
            }
            prev = num;
        }
        return true;
    }

    //2363. 合并相似的物品
    public List<List<Integer>> mergeSimilarItems(int[][] items1, int[][] items2) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int[] item : items1) {
            map.put(item[0], map.getOrDefault(item[0], 0) + item[1]);
        }
        for (int[] item : items2) {
            map.put(item[0], map.getOrDefault(item[0], 0) + item[1]);
        }
        List<List<Integer>> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            result.add(Arrays.asList(entry.getKey(), entry.getValue()));
        }
        result.sort(Comparator.comparingInt(o -> o.get(0)));
        return result;
    }

    //2373. 矩阵中的局部最大值
    public int[][] largestLocal(int[][] grid) {
        int n = grid.length;
        int[][] res = new int[n - 2][n - 2];
        for (int i = 0; i < n - 2; i++) {
            for (int j = 0; j < n - 2; j++) {
                for (int x = i; x < i + 3; x++) {
                    for (int y = j; y < j + 3; y++) {
                        res[i][j] = Math.max(res[i][j], grid[x][y]);
                    }
                }
            }
        }
        return res;
    }

    //2574. 左右元素和的差值
    public int[] leftRigthDifference(int[] nums) {
        int n = nums.length;
        int[] leftSum = new int[n];
        int[] rightSum = new int[n];
        leftSum[0] = nums[0];
        rightSum[n - 1] = nums[n - 1];
        for (int i = 1; i < n; i++) {
            leftSum[i] = nums[i] + leftSum[i - 1];
            rightSum[n - i - 1] = nums[n - i - 1] + rightSum[n - i];
        }
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = Math.abs(leftSum[i] - nums[i] - (rightSum[i] - nums[i]));
        }
        return ans;
    }

    //2582. 递枕头
    public int passThePillow(int n, int time) {
        int k = time / (n - 1);
        int r = time % (n - 1);
        if (k % 2 == 0) return 1 + r;
        return n - r;
    }

    //2595. 奇偶位数
    public int[] evenOddBit(int n) {
        int odd = 0, even = 0;
        int i = 0;
        while (n > 0) {
            if (n % 2 == 1) {
                if (i % 2 == 0) even++;
                else odd++;
            }
            n /= 2;
            i++;
        }
        return new int[]{even, odd};
    }

    //2383. 赢得比赛需要的最少训练时长
    public int minNumberOfHours(int initialEnergy, int initialExperience, int[] energy, int[] experience) {
        int n = energy.length;
        int leastEnergy = energy[n - 1] + 1;
        for (int i = n - 2; i >= 0; i--) {
            leastEnergy += energy[i];
        }
        int energyHour = leastEnergy > initialEnergy ? leastEnergy - initialEnergy : 0;
        int exp = initialExperience, sum = 0;
        for (int i = 0; i < n; i++) {
            sum = Math.max(sum, Math.max(experience[i] + 1 - exp, 0));
            exp += experience[i];
        }
        int expHour = sum;
        return energyHour + expHour;
    }

    public int minNumberOfHours2(int initialEnergy, int initialExperience, int[] energy, int[] experience) {
        int minEnergy = 0;
        int sumEnergy = 0;
        for (int e : energy) {
            sumEnergy += e;
        }
        minEnergy = Math.max(0, sumEnergy + 1 - initialEnergy);
        int minExp = 0;
        for (int exp : experience) {
            minExp = Math.max(minExp, exp + 1 - initialExperience);
            initialExperience += exp;
        }
        minExp = Math.max(0, minExp);
        return minEnergy + minExp;
    }

    //2384. 最大回文数字
    public String largestPalindromic(String num) {
        // 定义暂存数组
        int[] dig = new int[10];
        // 定义插入的索引
        int index = 0;
        StringBuilder s = new StringBuilder();
        // 统计各个数字出现的次数
        for (int i = 0; i < num.length(); i++) {
            dig[num.charAt(i) - '0']++;
        }
        // 从9开始遍历各个数字
        for (int i = 9; i >= 0; i--) {
            // 如果是偶数个的，依次填入两个值，0最后填入
            if ((dig[i] > 0 && dig[i] % 2 == 0 && i != 0) ||
                    (i == 0 && s.length() != 0 && dig[i] > 0 && dig[i] % 2 == 0)) {
                while (dig[i] != 0) {
                    s.insert(index, i);
                    s.insert(index + 1, i);
                    index++;
                    dig[i] -= 2;
                }
            }
            // 如果是奇数个，依次填入两个值，直到为1，0最后填入
            if ((dig[i] > 1 && dig[i] % 2 == 1 && i != 0) ||
                    (i == 0 && s.length() != 0 && dig[i] > 1 && dig[i] % 2 == 1)) {
                while (dig[i] != 1) {
                    s.insert(index, i);
                    s.insert(index + 1, i);
                    index++;
                    dig[i] -= 2;
                }
            }
        }
        // 填入单个的数字
        for (int i = 9; i >= 0; i--) {
            if (dig[i] == 1) {
                s.insert(index, i);
                break;
            }
        }
        return s.length() == 0 ? "0" : s.toString();
    }

    //2490. 回环句
    public boolean isCircularSentence(String sentence) {
        String[] words = sentence.split(" ");
        int n = words.length;
        if (words[n - 1].charAt(words[n - 1].length() - 1) != sentence.charAt(0)) return false;
        for (int i = 1; i < n; i++) {
            if (words[i].charAt(0) != words[i - 1].charAt(words[i - 1].length() - 1)) return false;
        }
        return true;
    }

    //2395. 和相等的子数组
    public boolean findSubarrays(int[] nums) {
        int n = nums.length;
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < n - 1; i++) {
            int sum = nums[i] + nums[i + 1];
            if (set.contains(sum)) return true;
            set.add(sum);
        }
        return false;
    }

    //2610. 转换二维数组
    public List<List<Integer>> findMatrix(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        List<List<Integer>> res = new ArrayList<>();
        while (!map.isEmpty()) {
            List<Integer> list = new ArrayList<>();
            Set<Integer> keySet = new HashSet<>();
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                list.add(entry.getKey());
                entry.setValue(entry.getValue() - 1);
                if (entry.getValue() == 0) {
                    keySet.add(entry.getKey());
                }
            }
            for (int k : keySet) {
                map.remove(k);
            }
            res.add(list);
        }
        return res;
    }

    //2923. 找到冠军 I
    public int findChampion(int[][] grid) {
        int ans = 0;
        for (int i = 1; i < grid.length; i++) {
            if (grid[i][ans] == 1) {
                ans = i;
            }
        }
        return ans;
    }

    //2908. 元素和最小的山形三元组 I
    public int minimumSum(int[] nums) {
        int n = nums.length, res = 1000;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (nums[i] < nums[j] && nums[k] < nums[j]) {
                        res = Math.min(res, nums[i] + nums[j] + nums[k]);
                    }
                }
            }
        }
        return res < 1000 ? res : -1;
    }

    //2810. 故障键盘
    public String finalString(String s) {
        StringBuilder sb = new StringBuilder();
        boolean left = true;
        for (char c : s.toCharArray()) {
            if (c == 'i') {
                left = !left;
            } else {
                if (left) {
                    sb.append(c);
                } else {
                    sb.insert(0, c);
                }
            }
        }
        return left ? sb.toString() : sb.reverse().toString();
    }

    //2864. 最大二进制奇数
    public String maximumOddBinaryNumber(String s) {
        int cnt = 0;
        for (int i = 0; i < s.length(); i++) {
            cnt += s.charAt(i) - '0';
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt - 1; i++) {
            sb.append('1');
        }
        for (int i = 0; i < s.length() - cnt; i++) {
            sb.append('0');
        }
        sb.append('1');
        return sb.toString();
    }

    //2129. 将标题首字母大写
    public String capitalizeTitle(String title) {
        String[] array = title.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String t : array) {
            if (t.length() == 1 || t.length() == 2) {
                sb.append(t.toLowerCase());
            } else {
                sb.append(t.substring(0, 1).toUpperCase()).append(t.substring(1).toLowerCase());
            }
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    //2917. 找出数组中的 K-or 值
    public int findKOr(int[] nums, int k) {
        int[] cnt = new int[32];
        for (int num : nums) {
            cntDigit(cnt, num);
        }
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            if (cnt[i] >= k) {
                ans |= (1 << i);
            }
        }
        return ans;
    }

    private void cntDigit(int[] cnt, int num) {
        int idx = 0;
        while (num != 0) {
            if ((num & 1) == 1) {
                cnt[idx]++;
            }
            num >>= 1;
            idx++;
        }
    }

    //2859. 计算 K 置位下标对应元素的和
    public int sumIndicesWithKSetBits(List<Integer> nums, int k) {
        int sum = 0;
        int n = nums.size();
        for (int i = 0; i < n; i++) {
            if (bitCount(i) == k) sum += nums.get(i);
        }
        return sum;
    }

    private int bitCount(int num) {
        int cnt = 0;
        while (num != 0) {
            cnt += (num & 1);
            num >>= 1;
        }
        return cnt;
    }

    //2808. 使循环数组所有元素相等的最少秒数
    public int minimumSeconds(List<Integer> nums) {
        HashMap<Integer, List<Integer>> mp = new HashMap<>();
        int n = nums.size(), res = n;
        for (int i = 0; i < n; ++i) {
            mp.computeIfAbsent(nums.get(i), k -> new ArrayList<>()).add(i);
        }
        for (List<Integer> positions : mp.values()) {
            int mx = positions.get(0) + n - positions.get(positions.size() - 1);
            for (int i = 1; i < positions.size(); ++i) {
                mx = Math.max(mx, positions.get(i) - positions.get(i - 1));
            }
            res = Math.min(res, mx / 2);
        }
        return res;
    }

    //1224. 最大相等频率
    public int maxEqualFreq(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        Map<Integer, Integer> freq = new HashMap<>();
        int maxFreq = 0, ans = 0;
        for (int i = 0; i < nums.length; i++) {
            int cnt = count.getOrDefault(nums[i], 0);
            if (cnt > 0) {
                freq.put(cnt, freq.get(cnt) - 1);
            }
            cnt++;
            count.put(nums[i], cnt);
            freq.put(cnt, freq.getOrDefault(cnt, 0) + 1);
            maxFreq = Math.max(maxFreq, cnt);
            if (maxFreq == 1 ||
                    maxFreq * freq.get(maxFreq) + 1 == i + 1 ||
                    maxFreq + (maxFreq - 1) * freq.get(maxFreq - 1) == i + 1) {
                ans = Math.max(ans, i + 1);
            }
        }
        return ans;
    }

    //2509. 查询树中环的长度
    //    设 LCA 为 aa 和 bb 的最近公共祖先，那么环长等于LCA 到 a 的距离加 LCA 到 b 的距离加一。
//    如何找 LCA？
//    不断循环，每次循环比较 a 和 b 的大小：
//    如果 a>b，说明 a 的深度大于等于 b 的深度，那么把 a 移动到其父节点，即 a=a/2；
//    如果 a<b，说明 a 的深度小于等于 b 的深度，那么把 b 移动到其父节点，即 b=b/2；
//    如果 a=b，说明找到了 LCA，退出循环。
//    循环次数加一即为环长。
    public int[] cycleLengthQueries(int n, int[][] queries) {
        int m = queries.length;
        int[] ans = new int[m];
        for (int i = 0; i < m; i++) {
            int res = 1, a = queries[i][0], b = queries[i][1];
            while (a != b) {
                if (a > b) a /= 2;
                else b /= 2;
                ++res;
            }
            ans[i] = res;
        }
        return ans;
    }

    //2508. 添加边使所有节点度数都为偶数
    // 分类讨论
    public boolean isPossible(int n, List<List<Integer>> edges) {
        int[] deg = new int[n];
        List<Integer>[] g = new List[n + 1];
        for (int i = 0; i <= n; i++) {
            g[i] = new ArrayList<>();
        }
        for (List<Integer> edge : edges) {
            int x = edge.get(0), y = edge.get(1);
            g[x].add(y);
            g[y].add(x);
            deg[x - 1]++;
            deg[y - 1]++;
        }
        List<Integer> odd = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (deg[i] % 2 != 0) {
                odd.add(i + 1);
            }
        }
        if (odd.size() == 0) return true;
        if (odd.size() == 2) {
            int x = odd.get(0), y = odd.get(1);
            if (!isConnected(x, y, g)) return true;
            for (int i = 1; i <= n; i++) {
                if (i == x || i == y) continue;
                if (!isConnected(i, x, g) && !isConnected(i, y, g)) return true;
            }
        } else if (odd.size() == 4) {
            int a = odd.get(0), b = odd.get(1), c = odd.get(2), d = odd.get(3);
            return (!isConnected(a, b, g) && !isConnected(c, d, g))
                    || (!isConnected(a, c, g) && !isConnected(b, d, g))
                    || (!isConnected(a, d, g) && !isConnected(b, c, g));
        }
        return false;
    }

    private boolean isConnected(int x, int y, List<Integer>[] g) {
        return g[x].contains(y);
    }

    //面试题 16.22. 兰顿蚂蚁
    private class Position {

        // 横坐标 x 纵坐标 y
        int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof Position)) return false;
            Position o = (Position) obj;
            return x == o.x && y == o.y;
        }

        // 改写哈希算法，使两个 Position 对象可以比较坐标而不是内存地址
        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    public List<String> printKMoves(int K) {
        char[] direction = {'L', 'U', 'R', 'D'};
        // 用“向量”记录方向，顺序与上一行方向的字符顺序保持一致，每个元素的后一个元素都是可以90°向右变换得到的
        int[][] offset = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        // 蚂蚁的位置
        Position antPos = new Position(0, 0);
        // 蚂蚁方向的向量序号
        int antDir = 2;
        // 用集合存储所有黑块的坐标，一开始想再定义一个路径的坐标集合，发现可以直接用黑块+蚂蚁位置也能过
        Set<Position> blackSet = new HashSet<>();
        while (K > 0) {
            // 新的坐标对象用于放入集合
            Position t = new Position(antPos.x, antPos.y);
            // 如果黑块集合能存入，说明脚下的块不在集合中，也就意味着是白色，方向序号循环自增1
            if (blackSet.add(t)) antDir = (antDir + 1) % 4;
            else {
                // 否则说明脚下的块已经在集合中，也就意味着是黑色，方向序号循环自增3，相当于自减1，但是Math.floorMod取模可能消耗大？用+3替代
                antDir = (antDir + 3) % 4;
                // 别忘了删除，即将黑块变白
                blackSet.remove(t);
            }
            // 蚂蚁移动位置
            antPos.x += offset[antDir][0];
            antPos.y += offset[antDir][1];
            K--;
        }
        // 计算边界，即输出网格的行数和列数
        int left = antPos.x, top = antPos.y, right = antPos.x, bottom = antPos.y;
        for (Position pos : blackSet) {
            left = pos.x < left ? pos.x : left;
            top = pos.y < top ? pos.y : top;
            right = pos.x > right ? pos.x : right;
            bottom = pos.y > bottom ? pos.y : bottom;
        }
        char[][] grid = new char[bottom - top + 1][right - left + 1];
        // 填充白块
        for (char[] row : grid)
            Arrays.fill(row, '_');
        // 替换黑块
        for (Position pos : blackSet)
            grid[pos.y - top][pos.x - left] = 'X';
        // 替换蚂蚁
        grid[antPos.y - top][antPos.x - left] = direction[antDir];
        // 利用网格生成字符串列表
        List<String> result = new ArrayList<>();
        for (char[] row : grid)
            result.add(String.valueOf(row));
        return result;
    }

    //endregion ----------------------------------------------------------------------------------------------

}
