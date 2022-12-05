import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 244 最短单词距离
public class WordDistance {
    Map<String, List<Integer>> map;

    public WordDistance(String[] wordsDict) {
        map = new HashMap<>();
        for (int i = 0; i < wordsDict.length; i++) {
            List<Integer> list = map.getOrDefault(wordsDict[i], new ArrayList<>());
            list.add(i);
            map.put(wordsDict[i], list);
        }
    }

    public int shortest(String word1, String word2) {
        List<Integer> list1 = map.get(word1);
        List<Integer> list2 = map.get(word2);
        int min = Integer.MAX_VALUE;
        for (int idx1 = 0, idx2 = 0; idx1 < list1.size() && idx2 < list2.size(); ) {
            min = Math.min(min, Math.abs(list1.get(idx1) - list2.get(idx2)));
            if (list1.get(idx1) < list2.get(idx2)) {
                idx1++;
            } else {
                idx2++;
            }
        }
        return min;
    }
}
