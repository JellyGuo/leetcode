import java.util.ArrayList;
import java.util.List;

//面试题 10.10. 数字流的秩
class StreamRank {
    List<Integer> list;

    public StreamRank() {
        list = new ArrayList<>();
    }

    public void track(int x) {
        int l = 0, r = list.size();
        while (l < r) {
            int mid = l + r >> 1;
            if (list.get(mid) >= x) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        list.add(l, x);
    }

    public int getRankOfNumber(int x) {
        if (list.size() == 0) return 0;
        int l = 0, r = list.size() - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (list.get(mid) <= x) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return list.get(l) <= x ? l + 1 : 0;
    }
}