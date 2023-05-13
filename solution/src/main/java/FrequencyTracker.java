import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//2671. 频率跟踪器
public class FrequencyTracker {

    Map<Integer, Integer> numFre;
    Map<Integer, Set<Integer>> freNums;

    public FrequencyTracker() {
        numFre = new HashMap<>();
        freNums = new HashMap<>();
    }

    public void add(int number) {
        int lastFre = numFre.getOrDefault(number, 0);
        numFre.put(number, lastFre + 1);
        if (lastFre > 0 && freNums.containsKey(lastFre)) {
            freNums.get(lastFre).remove(number);
            if (freNums.get(lastFre).size() == 0) {
                freNums.remove(lastFre);
            }
        }
        Set<Integer> ls = freNums.getOrDefault(lastFre + 1, new HashSet<>());
        ls.add(number);
        freNums.put(lastFre + 1, ls);
    }

    public void deleteOne(int number) {
        if (!numFre.containsKey(number)) return;
        int lastFre = numFre.getOrDefault(number, 0);
        if (lastFre == 1) {
            numFre.remove(number);
        } else {
            numFre.put(number, lastFre - 1);
        }

        freNums.get(lastFre).remove(number);
        if (freNums.get(lastFre).size() == 0) {
            freNums.remove(lastFre);
        }
        if (lastFre > 1) {
            Set<Integer> ls = freNums.getOrDefault(lastFre - 1, new HashSet<>());
            ls.add(number);
            freNums.put(lastFre - 1, ls);
        }
    }

    public boolean hasFrequency(int frequency) {
        return freNums.containsKey(frequency);
    }
}
