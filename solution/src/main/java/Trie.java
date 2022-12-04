public class Trie {
    Trie[] children;
    boolean isEnd;

    public void insert(String word) {
        Trie node = this;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new Trie();
            }
            node = node.children[c - 'a'];
        }
        node.isEnd = true;
    }

    public boolean search(String word) {
        Trie prefix = searchPrefix(word);
        return prefix != null && prefix.isEnd;
    }

    public boolean startWith(String prefix) {
        return searchPrefix(prefix) != null;
    }

    public boolean hasPrefix(String word) {
        Trie node = this;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null || !node.children[c - 'a'].isEnd) return false;
            node = node.children[c - 'a'];
        }
        return node != null && node.isEnd;
    }

    private Trie searchPrefix(String word) {
        Trie node = this;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) return null;
            node = node.children[c - 'a'];
        }
        return node;
    }

    public String findRoot(String word) {
        Trie node = this;
        for (int i = 0; i < word.length(); i++) {
            int idx = word.charAt(i) - 'a';
            if (node.children[idx] == null) return word;
            node = node.children[idx];
            if (node.isEnd) return word.substring(0, i + 1);
        }
        return word;
    }
}
