import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class SolutionBracket {
    // region------------------------------------------------------------括号问题-------------------------------------------
//20 有效的括号
    public boolean isValidSe(String s) {
        Stack<Character> stack = new Stack<>();
        if (s.length() % 2 != 0) {
            return false;
        }
        for (char c : s.toCharArray()) {
            if (c == '{' || c == '[' || c == '(') {
                stack.push(c);
                continue;
            }
            if (c == '}' || c == ']' || c == ')') {
                if (stack.isEmpty()) {
                    return false;
                }
                char tmp = stack.peek();
                if ((c == '}' && tmp == '{') || (c == ']' && tmp == '[') || (c == ')' && tmp == '(')) {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    // 678有效的括号字符串
    // 贪心
    public boolean checkValidStringGreedy(String s) {
        // 待匹配的(的最小个数和最多个数
        int min = 0, max = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                min++;
                max++;
            } else if (c == ')') {
                min--;
                max--;
            } else {
                min--;
                max++;
            }
            min = Math.max(min, 0);
            if (min > max) return false;
        }
        return min == 0;
    }

    public boolean checkValidStringDP(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '*') {
                dp[i][i] = true;
            }
        }
        for (int i = 1; i < n; i++) {
            char c1 = s.charAt(i - 1), c2 = s.charAt(i);
            dp[i - 1][i] = (c1 == '(' || c1 == '*') && (c2 == ')' || c2 == '*');
        }
        for (int i = n - 3; i >= 0; i--) {
            char c1 = s.charAt(i);
            for (int j = i + 2; j < n; j++) {
                char c2 = s.charAt(j);
                if ((c1 == '(' || c1 == '*') && (c2 == ')' || c2 == '*')) {
                    dp[i][j] = dp[i + 1][j - 1];
                }
                for (int k = i; k < j && !dp[i][j]; k++) {
                    dp[i][j] = dp[i][k] && dp[k + 1][j];
                }
            }
        }
        return dp[0][n - 1];
    }

    public boolean checkValidStringStack(String s) {
        Deque<Integer> leftStack = new LinkedList<>();
        Deque<Integer> asteriskStack = new LinkedList<>();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                leftStack.push(i);
            } else if (c == '*') {
                asteriskStack.push(i);
            } else {
                if (!leftStack.isEmpty()) {
                    leftStack.pop();
                } else if (!asteriskStack.isEmpty()) {
                    asteriskStack.pop();
                } else {
                    return false;
                }
            }
        }
        while (!leftStack.isEmpty() && !asteriskStack.isEmpty()) {
            int leftIndex = leftStack.pop();
            int asteriskIndex = asteriskStack.pop();
            if (leftIndex > asteriskIndex) {
                return false;
            }
        }
        return leftStack.isEmpty();
    }

    //921. 使括号有效的最少添加
    public int minAddToMakeValid(String s) {
        int cnt = 0;
        int ans = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                cnt++;
            } else {
                if (cnt != 0) cnt--;
                else ans++;
            }
        }
        return ans + cnt;
    }

    // endregion

}
