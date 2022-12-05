//676. 实现一个魔法字典
public class MagicDictionary {
    Trie trie;

    public MagicDictionary() {
        trie = new Trie();
    }

    public void buildDict(String[] dictionary) {
        for (String word : dictionary) {
            trie.insert(word);
        }
    }

    public boolean search(String searchWord) {
        char[] chars = searchWord.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char origin = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == origin) continue;
                chars[i] = c;
                if (trie.search(new String(chars))) return true;
            }
            chars[i] = origin;
        }
        return false;
    }

    public boolean search2(String searchWord) {
        return dfs(searchWord, 0, trie,false);
    }

    private boolean dfs(String word, int idx, Trie node, boolean modified) {
        if (idx == word.length()) return modified && node.isEnd;
        int offset = word.charAt(idx) - 'a';
        if (node.children[offset] != null) {
            if (dfs(word, idx + 1, node.children[offset], modified)) {
                return true;
            }
        }
        if (!modified) {
            for (int j = 0; j < 26; j++) {
                if (j == offset) continue;
                Trie child = node.children[j];
                if (child != null && dfs(word, idx + 1, child,true)) {
                    return true;
                }
            }
        }

        return false;
    }
}
