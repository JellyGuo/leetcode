import java.util.Arrays;
import java.util.Random;

//1206. 设计跳表
public class Skiplist {

    private static final int MAX_LEVEL = 32;
    private static final double P_FACTOR = 0.25;
    private SkipNode head;
    private int level;
    private Random random;

    class SkipNode {
        int val;
        SkipNode[] next;

        public SkipNode(int val, int maxLevel) {
            this.val = val;
            next = new SkipNode[maxLevel];
        }
    }

    public Skiplist() {
        this.head = new SkipNode(-1, MAX_LEVEL);
        this.level = 0;
        random = new Random();
    }

    public boolean search(int target) {
        SkipNode cur = this.head;
        for (int i = level - 1; i >= 0; i--) {
            while (cur.next[i] != null && cur.next[i].val < target) {
                cur = cur.next[i];
            }
        }
        cur = cur.next[0];
        return cur != null && cur.val == target;
    }

    public void add(int num) {
        SkipNode[] update = new SkipNode[MAX_LEVEL];
        Arrays.fill(update, head);
        SkipNode cur = this.head;
        for (int i = level - 1; i >= 0; i--) {
            while (cur.next[i] != null && cur.next[i].val < num) {
                cur = cur.next[i];
            }
            update[i] = cur;
        }

        int lv = randomLevel();
        level = Math.max(level, lv);
        SkipNode node = new SkipNode(num, lv);
        for (int i = 0; i < lv; i++) {
            node.next[i] = update[i].next[i];
            update[i].next[i] = node;
        }
    }

    private int randomLevel() {
        int lv = 1;
        while (random.nextDouble() < P_FACTOR && lv < MAX_LEVEL) {
            lv++;
        }
        return lv;
    }

    public boolean erase(int num) {
        SkipNode[] update = new SkipNode[MAX_LEVEL];
        SkipNode cur = this.head;
        for (int i = level - 1; i >= 0; i--) {
            while (cur.next[i] != null && cur.next[i].val < num) {
                cur = cur.next[i];
            }
            update[i] = cur;
        }
        cur = cur.next[0];
        if (cur == null || cur.val != num) return false;
        for (int i = 0; i < level; i++) {
            if (update[i].next[i] != cur) break;
            update[i].next[i] = cur.next[i];
        }
        while (level > 1 && head.next[level - 1] == null) {
            level--;
        }
        return true;
    }
}
