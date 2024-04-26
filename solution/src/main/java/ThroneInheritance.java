import java.util.*;

//1600. 王位继承顺序
public class ThroneInheritance {
    Map<String, List<String>> map;
    Set<String> deleted;
    String kingName;

    public ThroneInheritance(String kingName) {
        map = new HashMap<>();
        this.kingName = kingName;
        deleted = new HashSet<>();
    }

    public void birth(String parentName, String childName) {
        List<String> ls = map.getOrDefault(parentName, new ArrayList<>());
        ls.add(childName);
        map.put(parentName, ls);
    }

    public void death(String name) {
        deleted.add(name);
    }

    public List<String> getInheritanceOrder() {
        List<String> ans = new ArrayList<>();
        Successor(kingName, ans);
        return ans;
    }

    private void Successor(String name, List<String> curOrder) {
        if(!deleted.contains(name)){
            curOrder.add(name);
        }
        for (String child : map.getOrDefault(name,new ArrayList<>())) {
            Successor(child, curOrder);
        }
    }
}
