import java.util.*;

//381. O(1) 时间插入、删除和获取随机元素 - 允许重复
public class RandomizedCollection {
    Random random;
    List<Integer> list;
    Map<Integer, Set<Integer>> map;

    public RandomizedCollection() {
        list = new ArrayList<>();
        map = new HashMap<>();
        random = new Random();
    }

    public boolean insert(int val) {
        boolean flag = true;
        if (map.containsKey(val)) {
            flag = false;
        }
        Set<Integer> set = map.getOrDefault(val, new HashSet<>());
        list.add(val);
        set.add(list.size() - 1);
        map.put(val, set);
        return flag;
    }

    public boolean remove(int val) {
        boolean flag = false;
        if (map.containsKey(val)) {
            flag = true;
            Iterator<Integer> iterator = map.get(val).iterator();
            int idx = iterator.next();
            int lastNum = list.get(list.size() - 1);
            list.set(idx, lastNum);
            map.get(val).remove(idx);
            map.get(lastNum).remove(list.size() - 1);
            if (idx < list.size() - 1) {
                map.get(lastNum).add(idx);
            }

            if (map.get(val).size() == 0) {
                map.remove(val);
            }
            list.remove(list.size() - 1);
        }
        return flag;
    }

    public int getRandom() {
        return list.get(random.nextInt(list.size()));
    }
}
