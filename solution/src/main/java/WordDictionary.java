public class WordDictionary {
    private Trie root;

    public WordDictionary() {
        root = new Trie();
    }

    public void addWord(String word) {
        root.insert(word);
    }

    public boolean search(String word) {
        return dfs(word, 0, root);
    }

    private boolean dfs(String word, int idx, Trie node) {
        if (idx == word.length()) {
            return node.isEnd;
        }
        char c = word.charAt(idx);
        if (Character.isLetter(c)) {
            Trie child = node.children[c - 'a'];
            return child != null && dfs(word, idx + 1, child);
        } else {
            for (int i = 0; i < 26; i++) {
                Trie child = node.children[i];
                if (child != null && dfs(word, idx + 1, node)) return true;
            }
        }
        return false;
    }
}
