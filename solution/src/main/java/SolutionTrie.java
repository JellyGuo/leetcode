import java.util.*;

public class SolutionTrie {
    //region ------------------------------------------------------字典树---------------------------------------------------
    // 720 词典中最长的单词
    //返回 words 中最长的一个单词，该单词是由 words 词典中其他单词逐步添加一个字母组成
    public String longestWord720(String[] words) {
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(word);
        }
        String longest = "";
        for (String word : words) {
            if (trie.hasPrefix(word)) {
                if (word.length() > longest.length() || (word.length() == longest.length() && word.compareTo(longest) < 0)) {
                    longest = word;
                }
            }
        }
        return longest;
    }

    //17.15 给定一组单词words，编写一个程序，找出其中的最长单词，且该单词由这组单词中的其他单词组合而成
    public String longestWord(String[] words) {
        String res = "";
        List<String> wordList = Arrays.asList(words);
        //按字符长度从大到小排列，相同长度的字符，按字典序正序排列，这样第一个返回的是满足题意要求的字符
        wordList.sort((a, b) -> a.length() == b.length() ? a.compareTo(b) : b.length() - a.length());
        for (String target : wordList) {
            if (longestWordDfs(target, 0, wordList)) return target;
        }
        return res;

    }

    /**
     * @param target   待处理的目标单词
     * @param start    该目标单词目前处理到的的下标索引，初始化的时候是0，从该单词的第一个字符开始
     * @param wordList 包含这个目标单词的所有单词的列表
     * @return
     */
    private boolean longestWordDfs(String target, int start, List<String> wordList) {
        if (start == target.length()) return true;//当下标到达字符的结尾时，说明这个是满足条件的
        for (int end = start; end < target.length(); end++) {
            //下面这一行是为了排除目标单词target本身，题意要求由其他的至少两个单词组成
            //当遍历的时候只有一轮，一直没找到其他的目标单词，这个目标单词做为一个候选词，需要被排除掉
            if (end - start + 1 == target.length()) continue;
            String prev = target.substring(start, end + 1);//切出来[start,end]之间的字符作为一个候选单词进入下一轮递归
            //这个切出来的单词是在单词列表&&剩下的单词也在单词列表（可能需要再切）
            if (wordList.contains(prev) && longestWordDfs(target, end + 1, wordList)) return true;
        }
        return false;
    }

    public String longestWordTrie(String[] words) {
        Trie root = new Trie();
        String res = "";
        List<String> wordList = Arrays.asList(words);
        //排序好，第一个返回的即是结果
        wordList.sort((a, b) -> a.length() == b.length() ? a.compareTo(b) : b.length() - a.length());
        //构造字典树
        for (String word : wordList) root.insert(word);
        for (String word : wordList) {
            Trie cur = root;
            int n = word.length();
            for (int i = 0; i < n; i++) {
                char c = word.charAt(i);
                //排除掉自己组成自己，当前遍历到的字符是个单词，且剩余部分可以再次被切分
                if (i < n - 1 && cur.children[c - 'a'].isEnd && canSplitToWord(word.substring(i + 1), root)) {
                    return word;
                }
                cur = cur.children[c - 'a'];
            }
        }
        return res;
    }

    /**
     * 当前的单词可以被切分，在wordList中找到
     *
     * @param remain
     * @return
     */
    private boolean canSplitToWord(String remain, Trie root) {
        //当没有可以切分的了 返回True
        if (remain.equals("")) return true;
        Trie cur = root;
        for (int i = 0; i < remain.length(); i++) {
            char c = remain.charAt(i);//拿到当前的字符
            if (cur.children[c - 'a'] == null) return false;//这个节点找不到
            //当前的节点是个单词，且剩余部分可以再次被切分
            if (cur.children[c - 'a'].isEnd && canSplitToWord(remain.substring(i + 1), root)) {
                return true;
            }
            cur = cur.children[c - 'a'];
        }
        return false;
    }

    // 648单词替换
    public String replaceWords(List<String> dictionary, String sentence) {
        Trie648 trie = new Trie648();
        for (String s : dictionary) {
            trie.insert(s);
        }
        String[] words = sentence.split(" ");
        List<String> resultList = new ArrayList<>();
        for (String word : words) {
            resultList.add(trie.findRoot(word));
        }
        return String.join(" ", resultList);
    }

    class Trie648 {
        Trie648[] children;
        boolean isEnd;

        public Trie648() {
            children = new Trie648[26];
        }

        public void insert(String word) {
            Trie648 cur = this;
            for (int i = 0; i < word.length(); i++) {
                if (cur.children[word.charAt(i) - 'a'] == null) {
                    cur.children[word.charAt(i) - 'a'] = new Trie648();
                }
                cur = cur.children[word.charAt(i) - 'a'];
            }
            cur.isEnd = true;
        }

        public String findRoot(String word) {
            Trie648 cur = this;
            for (int i = 0; i < word.length(); i++) {
                if (cur.children[word.charAt(i) - 'a'] == null) return word;
                cur = cur.children[word.charAt(i) - 'a'];
                if (cur.isEnd) return word.substring(0, i + 1);
            }
            return word;
        }
    }

    //336. 回文对
    //https://leetcode.cn/problems/palindrome-pairs/solutions/187969/qian-zhui-shu-jie-fa-by-dufre/
    private TrieNode336 root;
    public boolean isPalindrome(String s){
        int i=0, j=s.length()-1;
        while (i < j){
            if (s.charAt(i) != s.charAt(j)){
                return false;
            }
            i++;
            j--;
        }

        return true;
    }
    public List<List<Integer>> palindromePairs(String[] words) {
        this.root = new TrieNode336();
        int n = words.length;

        //build TrieNode Tree
        for (int i=0; i<n; i++){
            String word = new StringBuilder(words[i]).reverse().toString();
            TrieNode336 cur = root;

            if (isPalindrome(word.substring(0)))
                cur.suffixs.add(i);
            for (int j=0; j<word.length(); j++){
                int index = word.charAt(j) - 'a';
                if (cur.children[index] == null)
                    cur.children[index] = new TrieNode336();
                cur = cur.children[index];
                if (isPalindrome(word.substring(j+1)))
                    cur.suffixs.add(i);
            }
            cur.index = i;
        }

        //search
        List<List<Integer>> res = new ArrayList<>();
        for (int i=0; i<n; i++){
            String word = words[i];
            TrieNode336 cur = root;

            int j=0;
            for (; j<word.length(); j++){
                if (isPalindrome(word.substring(j)) && cur.index!=-1){
                    res.add(Arrays.asList(i, cur.index));
                }

                int index = word.charAt(j) - 'a';
                if (cur.children[index] == null)
                    break;
                cur = cur.children[index];
            }

            if (j == word.length()){
                for (int k : cur.suffixs){
                    if (k != i)
                        res.add(Arrays.asList(i, k));
                }
            }
        }

        return res;
    }

    class TrieNode336 {
        public TrieNode336[] children;
        public int index;
        public List<Integer> suffixs;

        public TrieNode336(){
            this.children = new TrieNode336[26];
            this.index = -1;
            this.suffixs = new ArrayList<>();
        }
    }

    //1268. 搜索推荐系统
    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
        Trie1268 trie = new Trie1268();
        for (String product : products) {
            trie.insert(product);
        }
        return trie.search(searchWord);
    }

    class Trie1268 {
        private Trie1268[] children;
        private PriorityQueue<String> words;

        public Trie1268() {
            children = new Trie1268[26];
            words = new PriorityQueue<>(Comparator.reverseOrder());
        }

        public void insert(String word) {
            Trie1268 node = this;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    node.children[c - 'a'] = new Trie1268();
                }
                node = node.children[c - 'a'];
                node.words.offer(word);
                if (node.words.size() > 3) {
                    node.words.poll();
                }
            }

        }

        public List<List<String>> search(String word) {
            Trie1268 node = this;
            List<List<String>> res = new ArrayList<>();
            boolean flag = false;
            for (char c : word.toCharArray()) {
                if (flag || node.children[c - 'a'] == null) {
                    res.add(new ArrayList<>());
                    flag = true;
                } else {
                    node = node.children[c - 'a'];
                    List<String> ls = new ArrayList<>();
                    while (!node.words.isEmpty()) {
                        ls.add(0, node.words.poll());
                    }
                    res.add(ls);
                }
            }
            return res;
        }
    }

    // 2416 字符串的前缀分数和
    public int[] sumPrefixScores(String[] words) {
        int n = words.length;
        int[] answers = new int[n];
        Trie2416 trie = new Trie2416();
        for (String word : words) {
            trie.insert(word);
        }
        for (int i = 0; i < words.length; i++) {
            answers[i] = trie.totalCnt(words[i]);
        }
        return answers;
    }

    class Trie2416 {
        Trie2416[] children;
        boolean isEnd;
        int cnt;

        public Trie2416() {
            children = new Trie2416[26];
            isEnd = false;
            cnt = 0;
        }

        public void insert(String word) {
            Trie2416 trie = this;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (trie.children[idx] == null) {
                    trie.children[idx] = new Trie2416();
                }
                trie = trie.children[idx];
                trie.cnt++;
            }
            trie.isEnd = true;
        }

        public int totalCnt(String word) {
            Trie2416 trie = this;
            int total = 0;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (trie.children[idx] == null) {
                    return total;
                }
                trie = trie.children[idx];
                total += trie.cnt;
            }
            return total;
        }
    }

    //面试题 17.17. 多次搜索
    public int[][] multiSearch(String big, String[] smalls) {
        MultiSearchTrie trie = new MultiSearchTrie();
        for (String small : smalls) {
            trie.insert(small);
        }
        Map<String, List<Integer>> hits = new HashMap<>();
        for (int i = 0; i < big.length(); i++) {
            List<String> matches = trie.search(big.substring(i));
            for (String match : matches) {
                List<Integer> indexes = hits.getOrDefault(match, new ArrayList<>());
                indexes.add(i);
                hits.put(match, indexes);
            }
        }
        int[][] ans = new int[smalls.length][];
        for (int i = 0; i < smalls.length; i++) {
            String small = smalls[i];
            List<Integer> indexed = hits.getOrDefault(small, new ArrayList<>());
            if (indexed.size() == 0) {
                ans[i] = new int[0];
            }
            ans[i] = indexed.stream().mapToInt(p -> p).toArray();
        }
        return ans;

    }

    class MultiSearchTrie {
        MultiSearchTrie[] children;
        String word;

        public MultiSearchTrie() {
            children = new MultiSearchTrie[26];
        }

        public void insert(String word) {
            MultiSearchTrie node = this;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    node.children[c - 'a'] = new MultiSearchTrie();
                }
                node = node.children[c - 'a'];
            }
            node.word = word;
        }

        public List<String> search(String word) {
            MultiSearchTrie node = this;
            List<String> ans = new ArrayList<>();
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    break;
                }
                node = node.children[c - 'a'];
                if (node.word != null) {
                    ans.add(node.word);
                }
            }
            return ans;
        }
    }

    // 472 连接词
    public List<String> findAllConcatenatedWordsInADict(String[] words) {
        Trie trie = new Trie();
        Arrays.sort(words, Comparator.comparingInt(String::length));
        List<String> ans = new ArrayList<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                if (findAllConcatenatedWordsInADictDfs(word, 0, trie)) {
                    ans.add(word);
                } else {
                    trie.insert(word);
                }
            }
        }
        return ans;
    }

    private boolean findAllConcatenatedWordsInADictDfs(String word, int idx, Trie root) {
        if (idx == word.length()) return true;
        Trie node = root;
        for (int i = idx; i < word.length(); i++) {
            if (node.children[word.charAt(i) - 'a'] == null) return false;
            node = node.children[word.charAt(i) - 'a'];
            if (node.isEnd && findAllConcatenatedWordsInADictDfs(word, i + 1, root)) return true;
        }
        return false;
    }

    // 820 单词的压缩编码
    public int minimumLengthEncoding(String[] words) {
        TrieNode820 trie = new TrieNode820();
        Map<TrieNode820, Integer> nodes = new HashMap<>();

        for (int i = 0; i < words.length; ++i) {
            String word = words[i];
            TrieNode820 cur = trie;
            for (int j = word.length() - 1; j >= 0; --j) {
                cur = cur.get(word.charAt(j));
            }
            nodes.put(cur, i);
        }

        int ans = 0;
        for (TrieNode820 node : nodes.keySet()) {
            if (node.count == 0) {
                ans += words[nodes.get(node)].length() + 1;
            }
        }
        return ans;

    }

    class TrieNode820 {
        TrieNode820[] children;
        int count;

        TrieNode820() {
            children = new TrieNode820[26];
            count = 0;
        }

        public TrieNode820 get(char c) {
            if (children[c - 'a'] == null) {
                children[c - 'a'] = new TrieNode820();
                count++;
            }
            return children[c - 'a'];
        }
    }

    //421 数组中两个数的最大异或值
//    思路分析：这种题就不要暴力法，指名道姓说要O(n)。根据提示需要使用建树。
//    首先我们需要知道，二进制高位为1会大于低位的所有和，比如"11111111"最高位代表的"1"按权展开为128，
//    而后面的“1111111”按权展开的和也只是127。所以进行异或时应该尽量选择高位异或结果为“1”的。
//    第一步：遍历数组，我们按照二进制[31,30,…,1, 0]各位的状态进行建树，left放置0，right放置1。
//    比如某个int型数的二进制是"0110110…"，我们需要将其放置到[left,right,right,left,right,right,left…]。
//    第二步：遍历数组，按照贪心策略，尽量维持当前选择的方向能保证当前能位异或结果为1。
    // 最高位的二进制位编号为 30
    static final int HIGH_BIT = 30;

    public int findMaximumXOR(int[] nums) {
        int x = 0;
        for (int k = HIGH_BIT; k >= 0; --k) {
            Set<Integer> seen = new HashSet<>();
            // 将所有的 pre^k(a_j) 放入哈希表中
            for (int num : nums) {
                // 如果只想保留从最高位开始到第 k 个二进制位为止的部分
                // 只需将其右移 k 位
                seen.add(num >> k);
            }

            // 目前 x 包含从最高位开始到第 k+1 个二进制位为止的部分
            // 我们将 x 的第 k 个二进制位置为 1，即为 x = x*2+1
            int xNext = x * 2 + 1;
            boolean found = false;

            // 枚举 i
            for (int num : nums) {
                if (seen.contains(xNext ^ (num >> k))) {
                    found = true;
                    break;
                }
            }

            if (found) {
                x = xNext;
            } else {
                // 如果没有找到满足等式的 a_i 和 a_j，那么 x 的第 k 个二进制位只能为 0
                // 即为 x = x*2
                x = xNext - 1;
            }
        }
        return x;
    }

    public int findMaximumXORTrie(int[] nums) {
        int n = nums.length;
        int x = 0;
        BinaryTrie root = new BinaryTrie();
        for (int i = 1; i < n; ++i) {
            // 将 nums[i-1] 放入字典树，此时 nums[0 .. i-1] 都在字典树中
            root.insert(nums[i - 1]);
            // 将 nums[i] 看作 ai，找出最大的 x 更新答案
            x = Math.max(x, root.getMaxXor(nums[i]));
        }
        return x;
    }

    //1707 与数组中元素最大的异或值
    public int[] maximizeXor(int[] nums, int[][] queries) {
        Arrays.sort(nums);
        int numQ = queries.length;
        int[][] newQueries = new int[numQ][3];
        for (int i = 0; i < numQ; ++i) {
            newQueries[i][0] = queries[i][0];
            newQueries[i][1] = queries[i][1];
            newQueries[i][2] = i;
        }
        Arrays.sort(newQueries, Comparator.comparingInt(query -> query[1]));

        int[] ans = new int[numQ];
        BinaryTrie trie = new BinaryTrie();
        int idx = 0, n = nums.length;
        for (int[] query : newQueries) {
            int x = query[0], m = query[1], qid = query[2];
            while (idx < n && nums[idx] <= m) {
                trie.insert(nums[idx]);
                ++idx;
            }
            if (idx == 0) { // 字典树为空
                ans[qid] = -1;
            } else {
                ans[qid] = trie.getMaxXor(x);
            }
        }
        return ans;
    }

    class BinaryTrie {
        static final int L = 30;
        BinaryTrie[] children = new BinaryTrie[2];

        public void insert(int val) {
            BinaryTrie node = this;
            for (int i = L; i >= 0; --i) {
                int bit = (val >> i) & 1;
                if (node.children[bit] == null) {
                    node.children[bit] = new BinaryTrie();
                }
                node = node.children[bit];
            }
        }

        public int getMaxXor(int val) {
            int ans = 0;
            BinaryTrie node = this;
            for (int i = L; i >= 0; --i) {
                int bit = (val >> i) & 1;
                if (node.children[bit ^ 1] != null) {
                    ans |= 1 << i;
                    bit ^= 1;
                }
                node = node.children[bit];
            }
            return ans;
        }
    }

    class T9WordsTrie {
        T9WordsTrie[] children;
        boolean isEnd;
        char val;

        public T9WordsTrie() {
            children = new T9WordsTrie[26];
        }

        public void insert(String word, Map<Character, Character> map) {
            T9WordsTrie node = this;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    node.children[c - 'a'] = new T9WordsTrie();
                }
                node = node.children[c - 'a'];
                node.val = map.get(c);
            }
            node.isEnd = true;
        }

        public boolean search(String word, String num, Map<Character, Character> map) {
            int numIdx = 0;
            T9WordsTrie node = this;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null || node.children[c - 'a'].val != num.charAt(numIdx))
                    return false;
                node = node.children[c - 'a'];
                numIdx++;
            }
            return node.isEnd;
        }

    }

    public List<String> getValidT9Words(String num, String[] words) {

        Map<Character, Character> map = new HashMap<>();
        map.put('a', '2');
        map.put('b', '2');
        map.put('c', '2');
        map.put('d', '3');
        map.put('e', '3');
        map.put('f', '3');
        map.put('g', '4');
        map.put('h', '4');
        map.put('i', '4');
        map.put('j', '5');
        map.put('k', '5');
        map.put('l', '5');
        map.put('m', '6');
        map.put('n', '6');
        map.put('o', '6');
        map.put('p', '7');
        map.put('q', '7');
        map.put('r', '7');
        map.put('s', '7');
        map.put('t', '8');
        map.put('u', '8');
        map.put('v', '8');
        map.put('w', '9');
        map.put('x', '9');
        map.put('y', '9');
        map.put('z', '9');

        List<String> result = new ArrayList<>();
        T9WordsTrie trie = new T9WordsTrie();
        for (String word : words) {
            trie.insert(word, map);
        }
        for (String word : words) {
            if (trie.search(word, num, map)) {
                result.add(word);
            }
        }
        return result;
    }
    // endregion--------------------------------------------------------------------------------------------------
}
