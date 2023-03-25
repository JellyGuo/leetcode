//1032. 字符流
class StreamChecker {
    class Trie {
        Trie[] children;
        boolean isEnd;

        public Trie() {
            children = new Trie[26];
        }

        public void insert(String word) {
            Trie node = this;
            char[] chars = word.toCharArray();
            int n = chars.length;
            for (int i = n - 1; i >= 0; i--) {
                if (node.children[chars[i] - 'a'] == null) {
                    node.children[chars[i] - 'a'] = new Trie();
                }
                node = node.children[chars[i] - 'a'];
            }
            node.isEnd = true;
        }

        public boolean query(StringBuilder sb) {
            Trie node = this;
            for (int i = sb.length() - 1; i >= 0; i--) {
                if (node.children[sb.charAt(i) - 'a'] == null) {
                    return false;
                }
                node = node.children[sb.charAt(i) - 'a'];
                if (node.isEnd) return true;
            }
            return false;
        }

    }

    StringBuilder sb;
    Trie trie;

    public StreamChecker(String[] words) {
        sb = new StringBuilder();
        trie = new Trie();
        for (String word : words) {
            trie.insert(word);
        }
    }

    public boolean query(char letter) {
        sb.append(letter);
        return trie.query(sb);
    }
}

/**
 * Your StreamChecker object will be instantiated and called as such:
 * StreamChecker obj = new StreamChecker(words);
 * boolean param_1 = obj.query(letter);
 */
