import java.util.HashMap;
import java.util.Map;

//677. 键值映射
public class MapSum {
    Trie trie;
    Map<String, Integer> map;

    class Trie {
        Trie[] children;
        boolean isEnd;
        int val;

        public Trie() {
            children = new Trie[26];
        }

    }

    public MapSum() {
        trie = new Trie();
        map = new HashMap<>();
    }

    public void insert(String key, int val) {
        int delta = val - map.getOrDefault(key, 0);
        map.put(key, val);
        Trie node = trie;
        for (int i = 0; i < key.length(); i++) {
            if (node.children[key.charAt(i) - 'a'] == null) {
                node.children[key.charAt(i) - 'a'] = new Trie();
            }
            node = node.children[key.charAt(i) - 'a'];
            node.val += delta;
        }
    }

    public int sum(String prefix) {
        Trie node = trie;
        for (int i = 0; i < prefix.length(); i++) {
            if (node.children[prefix.charAt(i) - 'a'] == null) {
                return 0;
            }
            node = node.children[prefix.charAt(i) - 'a'];
        }
        return node.val;
    }
}
