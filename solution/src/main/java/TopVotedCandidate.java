import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//911. 在线选举
public class TopVotedCandidate {
    Map<Integer, Integer> voteCounts;
    List<Integer> tops;// 记录每一时刻最高得票人
    int[] times;

    public TopVotedCandidate(int[] persons, int[] times) {
        this.times = times;
        voteCounts = new HashMap<>();
        voteCounts.put(-1, -1);
        tops = new ArrayList<>();
        int top = -1;
        for (int i = 0; i < persons.length; i++) {
            voteCounts.put(persons[i], voteCounts.getOrDefault(persons[i], 0) + 1);
            if (voteCounts.get(persons[i]) >= voteCounts.get(top)) {
                top = persons[i];
            }
            tops.add(top);
        }
    }

    public int q(int t) {
        int l = 0, r = times.length - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (times[mid] <= t) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return tops.get(l);

    }
}
