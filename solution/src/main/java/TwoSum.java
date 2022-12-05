import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    Map<Integer, Integer> map;

    public TwoSum() {
        map = new HashMap<>();
    }

    public void add(int x) {
        map.put(x, map.getOrDefault(x, 0) + 1);
    }

    public boolean find(int value) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int target = value - entry.getKey();
            if (target == entry.getKey()) {
                return entry.getValue() > 1;
            }
            return map.containsKey(target);
        }
        return false;
    }
}
