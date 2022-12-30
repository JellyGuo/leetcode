import java.util.TreeSet;

//855. 考场就座
public class ExamRoom {
    TreeSet<Integer> set;
    int n;

    public ExamRoom(int n) {
        set = new TreeSet<>();
        this.n = n;
    }

    public int seat() {
        int seat = 0;

        if (set.size() > 0) {
            Integer prev = null;
            // 当first=0 leave时，set中的first剩余4前面都为空，dist就是第一个元素距离0的距离
            int dist = set.first();
            for (int s : set) {
                if (prev != null) {
                    int d = (s - prev) / 2;
                    if (d > dist) {
                        dist = d;
                        seat = prev + d;
                    }
                }
                prev = s;
            }
            if (n - 1 - set.last() > dist) {
                seat = n - 1;
            }
        }
        set.add(seat);
        return seat;
    }

    public void leave(int p) {
        set.remove(p);
    }
}
