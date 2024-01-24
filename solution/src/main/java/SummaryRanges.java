import java.util.ArrayList;
import java.util.List;


//352. 将数据流变为多个不相交区间
public class SummaryRanges {
    List<int[]> list;

    public SummaryRanges() {
        list = new ArrayList<>();
        list.add(new int[]{-10, -10});
        list.add(new int[]{10050, 10050});
    }

    public void addNum(int value) {
        int n = list.size();
        if (n == 0) {
            list.add(new int[]{0, 0});
            return;
        }
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (list.get(mid)[0] >= value) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        int[] cur = new int[]{value, value};
        int[] prev = list.get(l - 1);
        int[] next = list.get(l);
        if ((prev[0] <= value && value <= prev[1]) || (next[0] <= value && value <= next[1])) {

        } else if (prev[1] + 1 == value && value == next[0] - 1) {
            next[0] = prev[0];
            list.remove(prev);
        } else if (prev[1] + 1 == value) {
            prev[1] = value;
        } else if (value == next[0] - 1) {
            next[0] = value;
        } else {
            list.add(r, cur);
        }
    }

    public int[][] getIntervals() {
        int[][] ans = new int[list.size() - 2][2];
        for (int i = 1; i < list.size() - 1; i++) {
            ans[i - 1] = list.get(i);
        }
        return ans;
    }
}
